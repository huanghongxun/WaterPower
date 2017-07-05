/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.waila

import mcp.mobius.waila.api.IWailaConfigHandler
import mcp.mobius.waila.api.IWailaDataAccessor
import mcp.mobius.waila.api.IWailaDataProvider
import net.minecraft.entity.player.EntityPlayerMP
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fml.common.Optional
import net.minecraftforge.fml.common.Optional.Interface
import net.minecraftforge.fml.common.Optional.InterfaceList
import waterpower.client.i18n
import waterpower.common.block.tile.TileEntityRotorGenerator
import waterpower.integration.IDs
import waterpower.util.DEFAULT_DECIMAL_FORMAT
import waterpower.util.emptyStack

@InterfaceList(Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = IDs.Waila))
object HUDHandlerRotorGenerator : IWailaDataProvider {

    @Optional.Method(modid = IDs.Waila)
    override fun getWailaBody(stack: ItemStack, tips: MutableList<String>, accessor: IWailaDataAccessor, configHandler: IWailaConfigHandler): List<String> {
        val te = accessor.tileEntity as? TileEntityRotorGenerator ?: return tips
        tips.add(i18n("waterpower.gui.latest_output") + ": " + DEFAULT_DECIMAL_FORMAT.format(te.getFromEU(te.latestOutput))
                + te.getEnergyUnit().name)
        return tips
    }

    @Optional.Method(modid = IDs.Waila)
    override fun getWailaHead(stack: ItemStack, tips: List<String>, accessor: IWailaDataAccessor, configHandler: IWailaConfigHandler): List<String> {
        return tips
    }

    @Optional.Method(modid = IDs.Waila)
    override fun getWailaStack(accessor: IWailaDataAccessor, configHandler: IWailaConfigHandler): ItemStack {
        return emptyStack
    }

    @Optional.Method(modid = IDs.Waila)
    override fun getWailaTail(stack: ItemStack, tips: List<String>, accessor: IWailaDataAccessor, configHandler: IWailaConfigHandler): List<String> {
        return tips
    }

    @Optional.Method(modid = IDs.Waila)
    override fun getNBTData(player: EntityPlayerMP, te: TileEntity, tag: NBTTagCompound, world: World, pos: BlockPos): NBTTagCompound {
        return tag
    }

}
