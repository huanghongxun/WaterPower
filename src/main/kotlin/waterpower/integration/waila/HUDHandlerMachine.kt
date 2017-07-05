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
import net.minecraftforge.fml.common.Optional.*
import waterpower.client.i18n
import waterpower.common.block.machine.TileEntityBaseMachine
import waterpower.integration.IDs
import waterpower.util.emptyStack

@InterfaceList(Interface(iface = "mcp.mobius.waila.api.IWailaDataProvider", modid = IDs.Waila))
object HUDHandlerMachine : IWailaDataProvider {

    @Method(modid = IDs.Waila)
    override fun getWailaBody(stack: ItemStack, tips: MutableList<String>, accessor: IWailaDataAccessor, configHandler: IWailaConfigHandler): List<String> {
        val tag = accessor.nbtData

        if (tag.hasKey("using")) {
            tips.add(i18n("waterpower.gui.using") + ": " + tag.getInteger("using") + "mb/t")
            tips.add(i18n("waterpower.gui.stored") + ": " + tag.getInteger("stored") + "mb")
            tips.add(i18n("waterpower.gui.capacity") + ": " + tag.getInteger("capacity") + "mb")
        }
        return tips
    }

    @Method(modid = IDs.Waila)
    override fun getWailaHead(stack: ItemStack, tips: List<String>, accessor: IWailaDataAccessor, configHandler: IWailaConfigHandler): List<String> {
        return tips
    }

    @Method(modid = IDs.Waila)
    override fun getWailaStack(accessor: IWailaDataAccessor, configHandler: IWailaConfigHandler): ItemStack? {
        return emptyStack
    }

    @Method(modid = IDs.Waila)
    override fun getWailaTail(stack: ItemStack, tips: List<String>, accessor: IWailaDataAccessor, configHandler: IWailaConfigHandler): List<String> {
        return tips
    }

    @Method(modid = IDs.Waila)
    override fun getNBTData(player: EntityPlayerMP, te: TileEntity, tag: NBTTagCompound, world: World, pos: BlockPos): NBTTagCompound {
        if (te is TileEntityBaseMachine) {
            tag.setInteger("using", te.energyConsume)
            tag.setInteger("stored", te.getEnergy().energyStored)
            tag.setInteger("capacity", te.getEnergy().maxEnergyStored)
        }
        return tag
    }

}
