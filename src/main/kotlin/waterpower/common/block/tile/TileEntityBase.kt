/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.tile

import net.minecraft.block.state.IBlockState
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.network.play.server.SPacketUpdateTileEntity
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.ITickable
import net.minecraft.util.math.BlockPos
import net.minecraftforge.common.capabilities.Capability
import waterpower.Preference
import waterpower.annotations.Flag
import waterpower.annotations.SaveNBT
import waterpower.annotations.Sync
import waterpower.common.block.BlockBase
import waterpower.common.block.attachment.TileEntityAttachment
import waterpower.common.network.NetworkHandler
import waterpower.common.network.PacketTileEntity
import waterpower.util.INBTSerializable
import java.lang.reflect.Field


open class TileEntityBase : TileEntity(), ITickable {
    protected var tick = Preference.General.updateTicks
    protected var loaded = false
    protected var ticked = false

    @Sync @SaveNBT protected var facing: Byte = EnumFacing.DOWN.ordinal.toByte()
    @Flag protected var renderUpdate = false

    fun isRedstonePowered() =
            this.world.isBlockIndirectlyGettingPowered(this.pos) > 0

    fun isServerSide() = !getWorld().isRemote

    open fun onNeighborTileChanged(pos: BlockPos) {
        for (a in attachments.values)
            a.onNeighborTileChange(pos)
    }
    open fun onNeighborBlockChanged() {}
    open fun onUpdate() {
        for (a in attachments.values)
            a.onUpdate()
    }

    open fun onFirstTick() {}

    open fun getDrops(): List<ItemStack> = emptyList()

    open fun getBlockState(): IBlockState {
        val state = getBlockType().defaultState
        if (state.propertyKeys.contains(BlockBase.FACINGS))
            return state.withProperty(BlockBase.FACINGS, getFacing())
        else
            return state
    }

    protected fun rerender() {
        val state = getBlockState()
        getWorld().notifyBlockUpdate(this.pos, state, state, 3)
    }

    override fun update() {
        if (!ticked) {
            onFirstTick()
            ticked = true
        }

        if (isServerSide() && !isRedstonePowered() && tick-- == 0) {
            onUpdate()
            tick = Preference.General.updateTicks

            sendUpdateToClient(true)
        } else
            sendUpdateToClient()

        for (a in attachments.values)
            a.onTick()
    }

    final override fun validate() {
        onLoaded();
        super.validate()
    }

    final override fun invalidate() {
        if (loaded)
            onUnloaded()
        super.invalidate()
    }

    open fun onLoaded() {
        loaded = true

        for (a in attachments.values)
            a.onLoaded()
    }

    open fun onUnloaded() {
        loaded = false

        for (a in attachments.values)
            a.onUnloaded()
    }

    override fun readFromNBT(tag: NBTTagCompound) {
        super.readFromNBT(tag)
        if (tag.hasKey("attachments")) {
            val a = tag.getCompoundTag("attachments")
            for (s in a.keySet)
                if (attachments.containsKey(s))
                    attachments[s]!!.readFromNBT(a.getCompoundTag(s))
        }

        if (!initedFields)
            initFields()
        for ((name, field) in nbtFields)
            field.set(this, readFieldFromNBT(field.get(this), name, tag))
    }

    override fun writeToNBT(tag: NBTTagCompound): NBTTagCompound {
        super.writeToNBT(tag)
        val compound = NBTTagCompound()
        tag.setTag("attachments", compound)
        for ((name, a) in attachments.entries)
            compound.setTag(name, a.writeToNBT())

        if (!initedFields)
            initFields()
        for ((name, field) in nbtFields) {
            val x: Any = field.get(this)
            writeFieldToNBT(x, name, tag)
        }
        return tag
    }

    open fun onBlockBreak() {}

    open fun getSupportedFacings(): Array<EnumFacing> = EnumFacing.HORIZONTALS

    fun setFacing(newFacing: EnumFacing): Boolean {
        if (!getSupportedFacings().contains(newFacing))
            return false
        facing = newFacing.ordinal.toByte()
        sendUpdateToClient()
        return true
    }

    fun getDroppedItemStack(meta: Int) =
            ItemStack(getBlockState().block, 1, meta)

    fun getFacing(): EnumFacing {
        val nowFacing = EnumFacing.VALUES[facing.toInt()]
        if (!getSupportedFacings().contains(nowFacing))
            setFacing(getSupportedFacings().first())
        return EnumFacing.VALUES[facing.toInt()]
    }

    /**************************************************
     *                   Capability                   *
     **************************************************/

    override fun hasCapability(capability: Capability<*>, facing: EnumFacing?): Boolean {
        if (super.hasCapability(capability, facing)) return true
        else return capabilities.containsKey(capability)
    }

    override fun <T> getCapability(capability: Capability<T>, facing: EnumFacing?): T? {
        if (!capabilities.containsKey(capability))
            return super.getCapability(capability, facing)
        else return capabilities[capability]!!.getCapability(capability, facing)
    }

    fun <T : TileEntityAttachment> addAttachment(attachment: T): T {
        attachments[attachment.getName()] = attachment
        for (cap in attachment.getCapabilities(null))
            if (capabilities.containsKey(cap))
                throw IllegalArgumentException("Conflict capability $cap")
            else
                capabilities[cap] = attachment
        return attachment
    }

    var attachments = HashMap<String, TileEntityAttachment>()
    var capabilities = LinkedHashMap<Capability<*>, TileEntityAttachment>()

    /****************************************************
     *                   Data Syncing                   *
     ****************************************************/

    fun sendUpdateToClient(force: Boolean = false) {
        if (isServerSide()) {
            val packet = PacketTileEntity(this)
            packet.tag = NBTTagCompound()
            val flag = writePacketData(packet.tag as NBTTagCompound, force)
            if (flag && !packet.tag!!.hasNoTags())
                NetworkHandler.instance.sendToAll(packet)
        }
    }

    companion object {
        private val COMMON_syncedFields = HashMap<String, HashMap<String, Field>>()
        private val COMMON_flagFields = HashMap<String, HashMap<String, Field>>()
        private val COMMON_nbtFields = HashMap<String, HashMap<String, Field>>()
        private val COMMON_enumFields = HashMap<String, HashMap<String, Array<Enum<*>>>>()
        private val COMMON_initedFields = mutableSetOf<String>()
    }

    lateinit var syncedFields: HashMap<String, Field>
    lateinit var flagFields: HashMap<String, Field>
    lateinit var nbtFields: HashMap<String, Field>
    lateinit var fieldValues: HashMap<String, Any?>
    var initedFields = false

    @Synchronized
    private fun initFields() {
        if (initedFields)
            return
        val cls = javaClass.name
        fieldValues = HashMap()
        if (COMMON_initedFields.contains(cls)) {
            initedFields = true
            syncedFields = COMMON_syncedFields[cls]!!
            flagFields = COMMON_flagFields[cls]!!
            nbtFields = COMMON_nbtFields[cls]!!
        } else {
            COMMON_initedFields += cls
            syncedFields = HashMap()
            COMMON_syncedFields.put(cls, syncedFields)
            nbtFields = HashMap()
            COMMON_nbtFields.put(cls, nbtFields)
            flagFields = HashMap()
            COMMON_flagFields.put(cls, flagFields)
            var clz: Class<*> = this.javaClass
            do {
                for (field in clz.declaredFields) {
                    val sync = field.getAnnotation(Sync::class.java)
                    if (sync != null) {
                        val name = if (sync.name != "") sync.name else field.name
                        syncedFields[name] = field
                        field.isAccessible = true
                    }
                    val flag = field.getAnnotation(Flag::class.java)
                    if (flag != null) {
                        val name = if (flag.name != "") flag.name else field.name
                        flagFields[name] = field
                        field.isAccessible = true
                    }
                    val nbt = field.getAnnotation(SaveNBT::class.java)
                    if (nbt != null) {
                        val name = if (nbt.name != "") nbt.name else field.name
                        nbtFields[name] = field
                        field.isAccessible = true
                    }
                }
                clz = clz.superclass
            } while (clz != TileEntity::class.java && clz != Object::class.java && clz != Any::class.java)
        }
        for ((name, field) in syncedFields.entries)
            fieldValues[name] = cloneAndNewInstance(field.get(this))
        initedFields = true
    }

    /**
     * Only be called in server side
     */
    open fun writePacketData(tag: NBTTagCompound, force: Boolean = false): Boolean {
        if (!initedFields)
            initFields()
        var flag = false
        for ((name, field) in syncedFields) {
            val x: Any = field.get(this)
            val y: Any = fieldValues[name]!!
            if (y != x || force) {
                onSyncDataChanged(name)
                fieldValues[name] = clone(y, x)
                writeFieldToNBT(x, name, tag)
                flag = true
            }
        }
        for ((name, field) in flagFields) {
            val x = field.get(this) as Boolean
            if (x) {
                onSyncDataChanged(name)
                writeFieldToNBT(x, name, tag)
                field.set(this, false)
                flag = true
            }
        }
        return flag
    }

    private fun cloneAndNewInstance(x: Any): Any {
        if (x is INBTSerializable<*>)
            return x.clone()
        else return x
    }

    private fun clone(y: Any, x: Any): Any { // reduce too many new object created.
        if (x is INBTSerializable<*> && y is INBTSerializable<*>) {
            y.deserializeNBT(x.serializeNBT())
            return y
        } else return x
    }

    private fun writeFieldToNBT(x: Any, name: String, tag: NBTTagCompound) {
        when (x) { // must be efficient
            is Int -> tag.setInteger(name, x)
            is Double -> tag.setDouble(name, x)
            is Long -> tag.setLong(name, x)
            is Float -> tag.setFloat(name, x)
            is Byte -> tag.setByte(name, x)
            is Short -> tag.setShort(name, x)
            is String -> tag.setString(name, x)
            is Boolean -> tag.setBoolean(name, x)
            is ByteArray -> tag.setByteArray(name, x)
            is IntArray -> tag.setIntArray(name, x)
            is INBTSerializable<*> -> tag.setTag(name, x.serializeNBT())
            is BlockPos -> tag.setLong(name, x.toLong())
        }
    }

    /**
     * Only be called in client side
     */
    open fun readPacketData(tag: NBTTagCompound) {
        if (!initedFields)
            initFields()
        val changed = mutableSetOf<String>()
        for ((name, field) in syncedFields)
            if (tag.hasKey(name))
                field.set(this, readFieldFromNBT(field.get(this), name, tag))
        for (pair in syncedFields)
            if (tag.hasKey(pair.key))
                onSyncDataChanged(pair.key)
        for (pair in flagFields)
            if (tag.hasKey(pair.key))
                onSyncDataChanged(pair.key)
    }

    private fun readFieldFromNBT(value: Any, name: String, tag: NBTTagCompound): Any {
        var x = value
        if (tag.hasKey(name))
            when (x) {
                is Int -> x = tag.getInteger(name)
                is Double -> x = tag.getDouble(name)
                is Long -> x = tag.getLong(name)
                is Float -> x = tag.getFloat(name)
                is Byte -> x = tag.getByte(name)
                is Short -> x = tag.getShort(name)
                is String -> x = tag.getString(name)
                is Boolean -> x = tag.getBoolean(name)
                is ByteArray -> x = tag.getByteArray(name)
                is IntArray -> x = tag.getIntArray(name)
                is INBTSerializable<*> -> x.deserializeNBT(tag.getCompoundTag(name))
                is BlockPos -> x = BlockPos.fromLong(tag.getLong(name))
            }
        return x
    }

    open fun onSyncDataChanged(name: String) {
        if (name == "facing")
            if (isServerSide())
                rerender()
            else
                world.markBlockRangeForRenderUpdate(pos, pos)
    }

    override fun getUpdatePacket(): SPacketUpdateTileEntity? {
        sendUpdateToClient(true)
        return super.getUpdatePacket()
    }

    override fun getUpdateTag(): NBTTagCompound {
        sendUpdateToClient(true)
        return super.getUpdateTag()
    }

}