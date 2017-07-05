/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.network.IGuiHandler
import net.minecraftforge.fml.common.network.NetworkRegistry
import waterpower.WaterPower
import waterpower.annotations.HasGui
import waterpower.annotations.Init
import waterpower.annotations.Parser
import waterpower.annotations.call
import java.util.*

@Parser
@Init
object GuiHandler : IGuiHandler {

    val guis = LinkedList<HasGui>()
    val ids = HashMap<Class<out TileEntity>, Int>()

    @JvmStatic
    fun loadClass(cls: Class<*>) {
        val hasGui = cls.getAnnotation(HasGui::class.java)
        if (hasGui != null) {
            ids[cls as Class<TileEntity>] = guis.size
            guis += hasGui
        }
    }

    override fun getClientGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        val te = world?.getTileEntity(BlockPos(x, y, z)) ?: return null
        if (ID >= guis.size) return null
        else return call(guis[ID].guiClass.java, "new", null, player, te)
    }

    override fun getServerGuiElement(ID: Int, player: EntityPlayer?, world: World?, x: Int, y: Int, z: Int): Any? {
        val te = world?.getTileEntity(BlockPos(x, y, z)) ?: return null
        if (ID >= guis.size) return null
        else return call(guis[ID].containerClass.java, "new", null, player, te)
    }

    @JvmStatic
    fun init() {
        NetworkRegistry.INSTANCE.registerGuiHandler(WaterPower, this)
    }

}