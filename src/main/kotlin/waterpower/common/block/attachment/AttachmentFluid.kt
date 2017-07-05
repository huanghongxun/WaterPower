/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.attachment

import com.google.common.base.Predicate
import com.google.common.base.Predicates
import com.google.common.base.Supplier
import com.google.common.base.Suppliers
import ic2.api.recipe.ILiquidAcceptManager
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraftforge.common.capabilities.Capability
import net.minecraftforge.fluids.Fluid
import net.minecraftforge.fluids.FluidStack
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fluids.capability.IFluidHandler
import net.minecraftforge.fluids.capability.IFluidTankProperties
import waterpower.common.block.inventory.InventorySlot
import waterpower.common.block.tile.TileEntityBase
import waterpower.util.INBTSerializable
import java.util.*

class AttachmentFluid(base: TileEntityBase) : TileEntityAttachment(base) {
    protected val managedTanks: MutableList<FluidTankImpl> = ArrayList()
    protected val unmanagedTanks: MutableList<Supplier<out Collection<FluidTankImpl>>> = ArrayList()

    override fun getName() = "fluid"

    fun addTankInsert(name: String, capacity: Int): FluidTankImpl {
        return this.addTankInsert(name, capacity, Predicates.alwaysTrue<Fluid>())
    }

    fun addTankInsert(name: String, capacity: Int, acceptedFluids: Predicate<Fluid>): FluidTankImpl {
        return this.addTankInsert(name, capacity, InventorySlot.InvSide.ANY, acceptedFluids)
    }

    fun addTankInsert(name: String, capacity: Int, side: InventorySlot.InvSide): FluidTankImpl {
        return this.addTankInsert(name, capacity, side, Predicates.alwaysTrue<Fluid>())
    }

    fun addTankInsert(name: String, capacity: Int, side: InventorySlot.InvSide, acceptedFluids: Predicate<Fluid>): FluidTankImpl {
        return this.addTank(name, capacity, InventorySlot.Access.I, side, acceptedFluids)
    }

    fun addTankExtract(name: String, capacity: Int): FluidTankImpl {
        return this.addTankExtract(name, capacity, InventorySlot.InvSide.ANY)
    }

    fun addTankExtract(name: String, capacity: Int, side: InventorySlot.InvSide): FluidTankImpl {
        return this.addTank(name, capacity, InventorySlot.Access.O, side)
    }

    fun addTank(name: String, capacity: Int): FluidTankImpl {
        return this.addTank(name, capacity, InventorySlot.Access.IO)
    }

    fun addTank(name: String, capacity: Int, access: InventorySlot.Access): FluidTankImpl {
        return this.addTank(name, capacity, access, InventorySlot.InvSide.ANY)
    }

    fun addTank(name: String, capacity: Int, acceptedFluids: Predicate<Fluid>): FluidTankImpl {
        return this.addTank(name, capacity, InventorySlot.Access.IO, InventorySlot.InvSide.ANY, acceptedFluids)
    }

    fun addTank(name: String, capacity: Int, access: InventorySlot.Access, side: InventorySlot.InvSide): FluidTankImpl {
        return this.addTank(name, capacity, access, side, Predicates.alwaysTrue())
    }

    fun addTank(name: String, capacity: Int, access: InventorySlot.Access, side: InventorySlot.InvSide, acceptedFluids: Predicate<Fluid>): FluidTankImpl {
        return this.addTank(name, capacity,
                (if (access.isInput()) side.acceptedSides else emptySet()),
                (if (access.isOutput()) side.acceptedSides else emptySet()),
                acceptedFluids)
    }

    fun addTank(name: String, capacity: Int, inputSides: Collection<EnumFacing>, outputSides: Collection<EnumFacing>, acceptedFluids: Predicate<Fluid>): FluidTankImpl {
        return this.addTank(FluidTankImpl(name, inputSides, outputSides, acceptedFluids, capacity))
    }

    fun addTank(tank: FluidTankImpl): FluidTankImpl {
        this.managedTanks.add(tank)
        return tank
    }

    fun addUnmanagedTanks(tank: FluidTankImpl) {
        this.unmanagedTanks.add(Suppliers.ofInstance<Set<FluidTankImpl>>(setOf<FluidTankImpl>(tank)))
    }

    fun addUnmanagedTanks(tanks: Collection<FluidTankImpl>) {
        this.addUnmanagedTankHook(Suppliers.ofInstance<Collection<FluidTankImpl>>(tanks))
    }

    fun addUnmanagedTankHook(suppl: Supplier<out Collection<FluidTankImpl>>) {
        this.unmanagedTanks.add(suppl)
    }

    fun changeConnectivity(tank: FluidTankImpl, access: InventorySlot.Access, side: InventorySlot.InvSide) {
        this.changeConnectivity(tank,
                (if (access.isInput()) side.acceptedSides else emptySet()),
                (if (access.isOutput()) side.acceptedSides else emptySet()))
    }

    fun changeConnectivity(tank: FluidTankImpl, inputSides: Collection<EnumFacing>, outputSides: Collection<EnumFacing>) {
        tank.inputSides = inputSides
        tank.outputSides = outputSides
    }

    fun getFluidTank(name: String): FluidTank {
        val var2 = this.getAllTanks().iterator()

        var tank: FluidTankImpl
        do {
            if (!var2.hasNext()) {
                throw IllegalArgumentException("Unable to find tank: " + name)
            }

            tank = var2.next()
        } while (tank.id != name)

        return tank
    }

    override fun readFromNBT(tag: NBTTagCompound) {
        val var2 = this.managedTanks.iterator()

        while (var2.hasNext()) {
            val tank = var2.next()
            if (tag.hasKey(tank.id, 10)) {
                tank.readFromNBT(tag.getCompoundTag(tank.id))
            }
        }

    }

    override fun writeToNBT(): NBTTagCompound {
        val nbt = NBTTagCompound()
        val var2 = this.managedTanks.iterator()

        while (var2.hasNext()) {
            val tank = var2.next()
            var subTag = NBTTagCompound()
            subTag = tank.writeToNBT(subTag)
            nbt.setTag(tank.id, subTag)
        }

        return nbt
    }

    override fun getCapabilities(side: EnumFacing?) =
            setOf(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY)

    override fun <T> getCapability(capability: Capability<T>, side: EnumFacing?): T? =
            if (capability == CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY) FluidHandler(side) as T
            else super.getCapability(capability, side)

    fun fluidPredicate(vararg fluids: Fluid): Predicate<Fluid> {
        val acceptedFluids: Any
        if (fluids.size > 10) {
            acceptedFluids = HashSet(Arrays.asList(*fluids))
        } else {
            acceptedFluids = Arrays.asList(*fluids)
        }

        return Predicate<Fluid> { fluid -> (acceptedFluids as Collection<*>).contains(fluid) }
    }

    fun fluidPredicate(manager: ILiquidAcceptManager): Predicate<Fluid> {
        return Predicate<Fluid> { fluid -> manager.acceptsFluid(fluid) }
    }

    fun getAllTanks(): Iterable<FluidTankImpl> {
        if (this.unmanagedTanks.isEmpty()) {
            return this.managedTanks
        } else {
            val tanks = ArrayList<FluidTankImpl>()
            tanks.addAll(this.managedTanks)
            val var2 = this.unmanagedTanks.iterator()

            while (var2.hasNext()) {
                tanks.addAll(var2.next().get() as Collection<FluidTankImpl>)
            }

            return tanks
        }
    }

    private inner class FluidHandler(private val side: EnumFacing?) : IFluidHandler {

        override fun getTankProperties(): Array<IFluidTankProperties> {
            val props = ArrayList<IFluidTankProperties>(managedTanks.size)
            val var2 = getAllTanks().iterator()

            while (true) {
                var tank: FluidTankImpl
                do {
                    if (!var2.hasNext()) {
                        return props.toTypedArray()
                    }

                    tank = var2.next()
                } while (!tank.canFill(side) && !tank.canDrain(side))

                props.add(tank.getTankProperties(side))
            }
        }

        override fun fill(resource: FluidStack?, doFill: Boolean): Int {
            if (resource != null && resource.amount > 0) {
                var total = 0
                val missing = resource.copy()
                val var5 = getAllTanks().iterator()

                while (var5.hasNext()) {
                    val tank = var5.next()
                    if (tank.canFill(this.side)) {
                        total += tank.fill(missing, doFill)
                        missing.amount = resource.amount - total
                        if (missing.amount <= 0) {
                            break
                        }
                    }
                }

                return total
            } else {
                return 0
            }
        }

        override fun drain(resource: FluidStack?, doDrain: Boolean): FluidStack? {
            if (resource != null && resource.amount > 0) {
                val ret = FluidStack(resource.fluid, 0)
                val var4 = getAllTanks().iterator()

                while (var4.hasNext()) {
                    val tank = var4.next()
                    if (tank.canDrain(this.side)) {
                        val inTank = tank.fluid
                        if (inTank != null && inTank.fluid == resource.fluid) {
                            val add = tank.drain(resource.amount - ret.amount, doDrain)
                            if (add != null) {
                                assert(add.fluid == resource.fluid)

                                ret.amount += add.amount
                                if (ret.amount >= resource.amount) {
                                    break
                                }
                            }
                        }
                    }
                }

                return if (ret.amount == 0) null else ret
            } else {
                return null
            }
        }

        override fun drain(maxDrain: Int, doDrain: Boolean): FluidStack? {
            val var3 = getAllTanks().iterator()

            while (var3.hasNext()) {
                val tank = var3.next()
                if (tank.canDrain(this.side)) {
                    val stack = tank.drain(maxDrain, false)
                    if (stack != null) {
                        stack.amount = maxDrain
                        return this.drain(stack, doDrain)
                    }
                }
            }

            return null
        }
    }

    class FluidTankImpl(val id: String, var inputSides: Collection<EnumFacing>, var outputSides: Collection<EnumFacing>,
                        val acceptsFluidPredicate: Predicate<Fluid>, capacity: Int) : FluidTank(capacity), INBTSerializable<FluidTankImpl> {
        override fun canFillFluidType(fluidStack: FluidStack?)
                = fluidStack != null && acceptsFluidPredicate.apply(fluidStack.fluid)

        override fun canDrainFluidType(fluidStack: FluidStack?)
                = fluidStack != null && acceptsFluidPredicate.apply(fluidStack.fluid)

        fun acceptsFluid(fluid: Fluid) = acceptsFluidPredicate.apply(fluid)

        internal fun getTankProperties(side: EnumFacing?): IFluidTankProperties {
            return object : IFluidTankProperties {
                override fun getContents() = getFluid()
                override fun getCapacity() = this@FluidTankImpl.capacity

                override fun canFill() = canFill(side)
                override fun canDrain() = canDrain(side)

                override fun canFillFluidType(fluidStack: FluidStack?): Boolean {
                    return if (fluidStack != null && fluidStack.amount > 0) acceptsFluid(fluidStack.fluid) && (side == null || canFill(side)) else false
                }

                override fun canDrainFluidType(fluidStack: FluidStack?): Boolean {
                    return if (fluidStack != null && fluidStack.amount > 0) acceptsFluid(fluidStack.fluid) && (side == null || canDrain(side)) else false
                }

            }
        }

        fun canFill(side: EnumFacing?) = inputSides.contains(side)
        fun canDrain(side: EnumFacing?) = outputSides.contains(side)
        fun getFluidName() = fluid?.fluid?.name

        override fun equals(other: Any?): Boolean {
            if (other is FluidTank) {
                return fluidAmount == other.fluidAmount && fluid == other.fluid && capacity == other.capacity
            } else return false
        }

        override fun serializeNBT(): NBTTagCompound {
            val tag = NBTTagCompound()
            writeToNBT(tag)
            tag.setInteger("capacity", capacity)
            return tag
        }

        override fun deserializeNBT(tag: NBTTagCompound) {
            readFromNBT(tag)
            capacity = tag.getInteger("capacity")
        }

        override fun clone(): Any {
            val f = FluidTankImpl(id, inputSides, outputSides, acceptsFluidPredicate, 0)
            f.deserializeNBT(serializeNBT())
            return f
        }
    }
}