/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.util

import net.minecraft.block.Block
import net.minecraft.entity.item.EntityItem
import net.minecraft.init.Blocks
import net.minecraft.item.Item
import net.minecraft.item.ItemStack
import net.minecraft.nbt.NBTTagCompound
import net.minecraft.util.EnumFacing
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fluids.FluidTank
import net.minecraftforge.fluids.capability.CapabilityFluidHandler
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.WaterPower
import java.text.DecimalFormat
import java.util.*

fun isServerSide(): Boolean {
    return FMLCommonHandler.instance().effectiveSide.isServer
}

fun isClientSide(): Boolean {
    return FMLCommonHandler.instance().effectiveSide.isClient
}

fun IBlockAccess.getActualBlockState(pos: BlockPos)
        = getBlockState(pos).getActualState(this, pos)

fun getItem(loc: ResourceLocation): Item?
        = Item.REGISTRY.getObject(loc)

fun getItem(name: String): Item?
        = getItem(ResourceLocation(name))

fun getBlock(loc: ResourceLocation): Block?
        = Block.REGISTRY.getObject(loc)

fun getBlock(name: String): Block?
        = getBlock(ResourceLocation(name))

fun getItemStack(modName: String, itemName: String, meta: Int, amount: Int = 1): ItemStack? {
    val item = getItem(ResourceLocation(modName, itemName))
    if (item == null) return null
    else return ItemStack(item, amount, meta)
}

fun isWater(world: World, pos: BlockPos): Boolean {
    val block = world.getBlockState(pos).block
    return block == Blocks.WATER || block == Blocks.FLOWING_WATER
}

fun isLava(world: World, pos: BlockPos): Boolean {
    val block = world.getBlockState(pos).block
    return block == Blocks.LAVA || block == Blocks.FLOWING_LAVA
}

@SideOnly(Side.CLIENT)
fun getWorld(): World? {
    val mc = FMLClientHandler.instance().client
    if (mc != null)
        return mc.world
    return null
}

fun NBTTagCompound.setUUID(id: String, uuid: UUID?) {
    if (uuid == null) return
    this.setLong(id + "_most", uuid.mostSignificantBits)
    this.setLong(id + "_least", uuid.leastSignificantBits)
}

fun NBTTagCompound.getUUID(id: String): UUID? {
    if (hasKey(id + "_most") && hasKey(id + "_least"))
        return UUID(getLong(id + "_most"), getLong(id + "_least"))
    else
        return null
}

fun dropItems(world: World, pos: BlockPos, drops: List<ItemStack>) {
    if (isServerSide()) {
        for (item in drops) {
            if (!item.isEmpty) {
                val rx = WaterPower.random.nextFloat() * 0.8f + 0.1f
                val ry = WaterPower.random.nextFloat() * 0.8f + 0.1f
                val rz = WaterPower.random.nextFloat() * 0.8f + 0.1f

                val entityItem = EntityItem(world, (pos.x + rx).toDouble(), (pos.y + ry).toDouble(), (pos.z + rz).toDouble(), item.copy())

                val factor = 0.05f
                entityItem.motionX = WaterPower.random.nextGaussian() * factor
                entityItem.motionY = WaterPower.random.nextGaussian() * factor + 0.2
                entityItem.motionZ = WaterPower.random.nextGaussian() * factor
                world.spawnEntity(entityItem)
            }
        }
    }
}

/**
 * @return int - weather: 0 -> sunshine; 1 -> raining; 2 -> thundering; double - acquirement
 */
fun getWaterIncomeAndExpenseByBiome(world: World, pos: BlockPos): Pair<Int, Double> {
    val weather = if (world.isThundering) 2 else if (world.isRaining) 1 else 0
    val biomeID = world.getBiomeForCoordsBody(pos).biomeName.toLowerCase()
    val acquirement = when (biomeID) {
        "beach", "river", "taiga", "taigaHills", "forestHills" -> 1.0
        "forest" -> 1.0
        "plains", "extremeHills", "extremeHillsEdge" -> 0.75
        "mushroomIsland", "mushroomIslandShore", "ocean" -> 1.2
        "desertHills", "hell", "desert" -> 0.0
        "frozenOcean", "frozenRiver" -> 1.2
        "icePlains", "iceMountains" -> 1.0
        "jungleHills", "jungle" -> 1.5
        "swampland" -> 1.2
        else -> 0.0
    }

    return weather to acquirement
}

fun pushFluidAround(world: IBlockAccess, pos: BlockPos, tank: FluidTank) {
    val potential = tank.drain(tank.fluidAmount, false)
    var drained = 0
    if (potential != null && potential.amount > 0) {
        val working = potential.copy()

        for (side in EnumFacing.VALUES) {
            if (potential.amount <= 0)
                break

            val target = world.getTileEntity(pos.offset(side))
            if (target != null) {
                val handler = target.getCapability(CapabilityFluidHandler.FLUID_HANDLER_CAPABILITY, side.opposite)
                if (handler != null) {
                    val used = handler.fill(potential, true)
                    if (used > 0) {
                        drained += used
                        potential.amount -= used
                    }
                }
            }
        }

        if (drained > 0) {
            val actuallyDrained = tank.drain(drained, true)
            if (actuallyDrained == null || actuallyDrained.amount != drained) {
                throw IllegalStateException("Bad tank! Could drain " + working + " but only drained " + actuallyDrained + "( tank " + tank.javaClass + ")")
            }
        }

    }
}

val DEFAULT_DECIMAL_FORMAT = DecimalFormat("#.00")