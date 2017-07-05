/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common

import net.minecraft.block.Block
import net.minecraft.item.ItemStack
import net.minecraft.util.math.BlockPos
import net.minecraft.world.World
import net.minecraft.world.gen.feature.WorldGenMinable
import net.minecraft.world.gen.feature.WorldGenerator
import net.minecraftforge.common.MinecraftForge
import net.minecraftforge.event.terraingen.OreGenEvent
import net.minecraftforge.event.terraingen.TerrainGen
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import waterpower.Preference
import waterpower.annotations.Init
import waterpower.common.block.ore.Ores
import waterpower.common.init.WPBlocks
import waterpower.config.OreConfig
import java.util.*

@Init
object WorldGen : WorldGenerator(false) {

    fun minable(ore: ItemStack, num: Int): WorldGenMinable {
        val blockState = Block.getBlockFromItem(ore.item).getStateFromMeta(ore.metadata)
        return WorldGenMinable(blockState, num)
    }

    fun generateOre(ore: ItemStack, number: Int, amountPerChunk: Int, world: World, random: Random,
                    chunkX: Int, chunkZ: Int, minLevel: Int, maxLevel: Int) {
        val count = Math.round((random.nextGaussian() / 2 + 0.5) * amountPerChunk)
        for (n in 1..count) {
            val x = chunkX * 16 + random.nextInt(16)
            val y = random.nextInt(maxLevel - minLevel) + minLevel
            val z = chunkZ * 16 + random.nextInt(16)
            minable(ore, number).generate(world, random, BlockPos(x, y, z))
        }
    }

    fun generateOre(ore: ItemStack, oreConfig: OreConfig, world: World, random: Random, chunkX: Int, chunkZ: Int) =
            generateOre(ore, oreConfig.maxSize, oreConfig.amountPerChunk, world, random, chunkX, chunkZ, oreConfig.minLevel, oreConfig.maxLevel)

    override fun generate(world: World, random: Random, position: BlockPos): Boolean {
        if (!TerrainGen.generateOre(world, random, this, position, OreGenEvent.GenerateMinable.EventType.CUSTOM))
            return true
        val chunkX = position.x / 16
        val chunkZ = position.z / 16
        generateOre(WPBlocks.ore.getItemStack(Ores.Vanadium), Preference.OreGeneration.vanadiumOre, world, random, chunkX, chunkZ)
        generateOre(WPBlocks.ore.getItemStack(Ores.Manganese), Preference.OreGeneration.manganeseOre, world, random, chunkX, chunkZ)
        generateOre(WPBlocks.ore.getItemStack(Ores.Monazite), Preference.OreGeneration.monaziteOre, world, random, chunkX, chunkZ)
        generateOre(WPBlocks.ore.getItemStack(Ores.Magnetite), Preference.OreGeneration.magnetiteOre, world, random, chunkX, chunkZ)
        generateOre(WPBlocks.ore.getItemStack(Ores.Zinc), Preference.OreGeneration.zincOre, world, random, chunkX, chunkZ)
        return true
    }

    @JvmStatic
    fun preInit() {
        MinecraftForge.ORE_GEN_BUS.register(this)
    }

    var pos: BlockPos? = null

    @SubscribeEvent
    fun onOreGen(event: OreGenEvent.Post) {
        if (event.pos == pos)
            return
        pos = event.pos
        generate(event.world, event.rand, event.pos)
    }
}