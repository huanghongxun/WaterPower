/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block

import net.minecraft.block.Block
import net.minecraft.block.material.Material
import net.minecraft.block.properties.PropertyDirection
import net.minecraft.block.state.IBlockState
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper
import net.minecraft.entity.EntityLiving
import net.minecraft.entity.EntityLivingBase
import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.Item
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.tileentity.TileEntity
import net.minecraft.util.EnumFacing
import net.minecraft.util.EnumHand
import net.minecraft.util.math.BlockPos
import net.minecraft.util.math.RayTraceResult
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.client.model.ModelLoader
import net.minecraftforge.fml.common.registry.ForgeRegistries
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.WaterPower
import waterpower.client.GuiHandler
import waterpower.common.block.tile.TileEntityBase
import waterpower.integration.BuildCraftModule
import waterpower.util.dropItems
import waterpower.util.isStackEmpty

typealias ItemBlockProvider = (BlockBase) -> ItemBlock

abstract class BlockBase(id: String, material: Material, item: ItemBlockProvider) : Block(material) {

    init {
        unlocalizedName = id
        setCreativeTab(WaterPower.creativeTab)
        setHardness(3.0f)

        setRegistryName(id)
        ForgeRegistries.BLOCKS.register(this)

        val itemBlock = item(this)
        itemBlock.registryName = registryName
        ForgeRegistries.ITEMS.register(itemBlock)
    }

    override fun damageDropped(state: IBlockState)
            = getMetaFromState(state)

    @SideOnly(Side.CLIENT)
    fun registerModels() {
        val item = Item.getItemFromBlock(this)
        val locations = DefaultStateMapper().putStateModelLocations(this)
        for (t in 0 until maxMetaData()) {
            val state = getStateFromMeta(t)
            ModelLoader.setCustomModelResourceLocation(item, t, locations[state])
        }
    }

    abstract fun maxMetaData(): Int

    override fun canCreatureSpawn(state: IBlockState?, world: IBlockAccess?, pos: BlockPos?, type: EntityLiving.SpawnPlacementType?) = false

    override fun rotateBlock(world: World, pos: BlockPos, axis: EnumFacing): Boolean {
        val te = world.getTileEntity(pos)
        if (te is TileEntityBase)
            return te.setFacing(axis)
        return false
    }

    override fun getActualState(state: IBlockState?, worldIn: IBlockAccess?, pos: BlockPos?): IBlockState {
        val te = worldIn?.getTileEntity(pos)
        if (te != null && te is TileEntityBase)
            return te.getBlockState()
        return super.getActualState(state, worldIn, pos)
    }

    override fun breakBlock(worldIn: World?, pos: BlockPos?, state: IBlockState?) {
        super.breakBlock(worldIn, pos, state)

        val te = worldIn?.getTileEntity(pos)
        if (te is TileEntityBase)
            te.onBlockBreak()
    }

    override fun getStateForPlacement(world: World, pos: BlockPos, facing: EnumFacing?, hitX: Float, hitY: Float, hitZ: Float, meta: Int, placer: EntityLivingBase, hand: EnumHand): IBlockState {
        val state = super.getStateForPlacement(world, pos, facing, hitX, hitY, hitZ, meta, placer, hand)
        if (state.propertyKeys.contains(FACINGS))
            return state.withProperty(FACINGS, placer.horizontalFacing.opposite)
        else
            return state
    }

    override fun onBlockPlacedBy(worldIn: World, pos: BlockPos, state: IBlockState?, placer: EntityLivingBase?, stack: ItemStack?) {
        if (worldIn.isRemote) return
        val te = worldIn.getTileEntity(pos)
        if (te is TileEntityBase)
            if (placer == null)
                te.setFacing(EnumFacing.NORTH)
            else
                te.setFacing(placer.horizontalFacing.opposite)
    }

    override fun onNeighborChange(world: IBlockAccess, pos: BlockPos, neighbor: BlockPos) {
        val te = world.getTileEntity(pos)
        if (te is TileEntityBase)
            te.onNeighborTileChanged(neighbor)
    }

    override fun onBlockActivated(worldIn: World, pos: BlockPos, state: IBlockState, playerIn: EntityPlayer, hand: EnumHand, facing: EnumFacing, hitX: Float, hitY: Float, hitZ: Float): Boolean {

        val stack = playerIn.inventory.getCurrentItem()
        val stackWrench = !isStackEmpty(stack) && stack.item.javaClass.name.contains("Wrench")

        if (BuildCraftModule.isWrench(playerIn, stack, hand, RayTraceResult(playerIn)) && playerIn.isSneaking) {
            val tileEntity = worldIn.getTileEntity(pos)
            if (tileEntity != null && tileEntity is TileEntityBase) {
                val drops = state.block.getDrops(worldIn, pos, state, 0)
                if (state.block.removedByPlayer(state, worldIn, pos, playerIn, false)) {
                    dropItems(worldIn, pos, drops)
                }
                return false
            }
        }

        if (playerIn.isSneaking) return false
        val te = worldIn.getTileEntity(pos)
        if (te != null && !stackWrench) {
            val id = GuiHandler.ids[te.javaClass]
            if (id != null) {
                playerIn.openGui(WaterPower, id, worldIn, pos.x, pos.y, pos.z)
                return true
            }
        }
        return false
    }

    override fun getPickBlock(state: IBlockState?, target: RayTraceResult?, world: World, pos: BlockPos, player: EntityPlayer?): ItemStack {
        val te = world.getTileEntity(pos)
        if (te is TileEntityBase)
            return te.getDroppedItemStack(getMetaFromState(state))
        else
            return super.getPickBlock(state, target, world, pos, player)
    }

    var tmpTE: TileEntity? = null

    override fun harvestBlock(worldIn: World?, player: EntityPlayer?, pos: BlockPos?, state: IBlockState?, te: TileEntity?, stack: ItemStack?) {
        tmpTE = te
        super.harvestBlock(worldIn, player, pos, state, te, stack)
        tmpTE = null
    }

    override fun getDrops(world: IBlockAccess, pos: BlockPos?, state: IBlockState?, fortune: Int): MutableList<ItemStack> {
        val te = world.getTileEntity(pos) ?: tmpTE
        return getDrops(world, pos, state, te, fortune)
    }

    fun getDrops(world: IBlockAccess, pos: BlockPos?, state: IBlockState?, te: TileEntity?, fortune: Int): MutableList<ItemStack> {
        if (te is TileEntityBase) {
            val list = mutableListOf<ItemStack>()
            list += te.getDroppedItemStack(getMetaFromState(state))
            list += te.getDrops()
            return list
        }
        return super.getDrops(world, pos, state, fortune)
    }

    open fun colorMultiplier(state: IBlockState, world: IBlockAccess?, pos: BlockPos?, index: Int) = 0

    companion object {
        val FACINGS: PropertyDirection = PropertyDirection.create("facing", EnumFacing.Plane.HORIZONTAL.facings().asList())
    }

}
