/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.reservoir

import net.minecraft.block.Block
import net.minecraft.block.ITileEntityProvider
import net.minecraft.block.material.Material
import net.minecraft.block.state.IBlockState
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.init.Blocks
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidUtil
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fml.common.LoaderState
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.common.block.BlockEnum
import waterpower.common.block.turbine.BlockTurbine
import waterpower.common.init.WPBlocks
import waterpower.common.init.WPItems
import waterpower.common.item.EnumCrafting
import waterpower.common.recipe.Recipes
import waterpower.util.isStackEmpty

@Init
@NewInstance(LoaderState.ModState.PREINITIALIZED)
class BlockReservoir : BlockEnum<Reservoirs>("reservoir", Material.IRON, Reservoirs::class.java, Reservoirs.values()), ITileEntityProvider {

    init {
        WPBlocks.reservoir = this
        WPBlocks.blocks += this
    }

    override fun createNewTileEntity(worldIn: World?, meta: Int): TileEntity? {
        if (meta >= Reservoirs.values().size) return null
        else return Reservoirs.values()[meta].createNewTileEntity(worldIn, meta)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, stack: ItemStack?, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {
        if (!isStackEmpty(stack)) {
            val item = stack!!.item
            val te = worldIn.getTileEntity(pos)
            if (te is TileEntityReservoir) {
                if (Block.getBlockFromItem(item) is BlockReservoir)
                    if (te.type.ordinal == stack.itemDamage)
                        return false
                if (Block.getBlockFromItem(item) is BlockTurbine)
                    return false
                if (stack.hasCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, null) && FluidUtil.interactWithFluidHandler(stack, te, playerIn)) {
                    playerIn.inventoryContainer.detectAndSendChanges()
                    return true
                }
            }
        }
        return super.onBlockActivated(worldIn, pos, state, playerIn, hand, stack, facing, hitX, hitY, hitZ)
    }

    companion object {
        @JvmStatic
        fun postInit() {
            val res = WPBlocks.reservoir
            addReservoirRecipe(ItemStack(res, 8, 0), "logWood")
            addReservoirRecipe(ItemStack(res, 8, 1), Blocks.STONE)
            addReservoirRecipe(ItemStack(res, 8, 2), Blocks.LAPIS_BLOCK)
            addReservoirRecipe(ItemStack(res, 8, 3), "blockTin")
            addReservoirRecipe(ItemStack(res, 8, 4), "blockCopper")
            addReservoirRecipe(ItemStack(res, 8, 5), Blocks.QUARTZ_BLOCK)
            addReservoirRecipe(ItemStack(res, 8, 6), "blockZincAlloy")
            addReservoirRecipe(ItemStack(res, 8, 7), "blockBronze")
            addReservoirRecipe(ItemStack(res, 8, 8), Blocks.IRON_BLOCK)
            addReservoirRecipe(ItemStack(res, 8, 9), Blocks.OBSIDIAN)
            addReservoirAdvancedRecipe(ItemStack(res, 8, 10), "blockSteel")
            addReservoirAdvancedRecipe(ItemStack(res, 8, 11), Blocks.GOLD_BLOCK)
            addReservoirAdvancedRecipe(ItemStack(res, 8, 12), "blockManganeseSteel")
            addReservoirAdvancedRecipe(ItemStack(res, 8, 13), Blocks.DIAMOND_BLOCK)
            addReservoirAdvancedRecipe(ItemStack(res, 8, 14), "blockVanadiumSteel")
        }

        fun addReservoirRecipe(output: ItemStack, S: Any?) {
            if (S == null)
                return
            Recipes.craft(output, "SSS", "SIS", "SSS", 'S', S, 'I', WPItems.crafting.getItemStack(EnumCrafting.reservoir_core))
        }

        fun addReservoirAdvancedRecipe(output: ItemStack, S: Any?) {
            if (S == null)
                return
            Recipes.craft(output, "SSS", "SIS", "SSS", 'S', S, 'I', WPItems.crafting.getItemStack(EnumCrafting.reservoir_core_advanced))
        }
    }

}