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
import net.minecraft.util.ResourceLocation
import net.minecraft.util.math.BlockPos
import net.minecraft.world.IBlockAccess
import net.minecraft.world.World
import net.minecraftforge.fml.client.FMLClientHandler
import net.minecraftforge.fml.common.FMLCommonHandler
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.WaterPower
import java.text.DecimalFormat
import java.util.*

fun isServerSide() = FMLCommonHandler.instance().effectiveSide.isServer
fun isClientSide() = FMLCommonHandler.instance().effectiveSide.isClient

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
        return mc.theWorld
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
            if (!isStackEmpty(item)) {
                val rx = WaterPower.random.nextFloat() * 0.8f + 0.1f
                val ry = WaterPower.random.nextFloat() * 0.8f + 0.1f
                val rz = WaterPower.random.nextFloat() * 0.8f + 0.1f

                val entityItem = EntityItem(world, (pos.x + rx).toDouble(), (pos.y + ry).toDouble(), (pos.z + rz).toDouble(), item.copy())

                val factor = 0.05f
                entityItem.motionX = WaterPower.random.nextGaussian() * factor
                entityItem.motionY = WaterPower.random.nextGaussian() * factor + 0.2
                entityItem.motionZ = WaterPower.random.nextGaussian() * factor
                world.spawnEntityInWorld(entityItem)
            }
        }
    }
}

/**
 * @return int - weather: 0 -> sunshine; 1 -> raining; 2 -> thundering; double - acquirement
 */
fun getWaterIncomeAndExpenseByBiome(world: World, biomeID: String): Pair<Int, Double> {
    val weather = if (world.isThundering) 2 else if (world.isRaining) 1 else 0
    var acquirement = 0.0
    val map = mapOf(
            "beach" to 1.0,
            "river" to 1.0,
            "taiga" to 1.0,
            "forest" to 1.0,
            "plains" to 0.75,
            "extreme" to 0.75,
            "mushroom" to 1.2,
            "ocean" to 1.2,
            "desert" to 0.0,
            "hell" to 0.0,
            "frozen" to 1.2,
            "ice" to 1.0,
            "jungle" to 1.5,
            "swapland" to 1.2
    )
    for ((key, value) in map)
        if (biomeID.contains(key)) {
            acquirement = value
            break
        }

    return weather to acquirement
}

val DEFAULT_DECIMAL_FORMAT = DecimalFormat("#.00")