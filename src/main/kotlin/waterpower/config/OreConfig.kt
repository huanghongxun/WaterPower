package waterpower.config

/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

data class OreConfig(val amountPerChunk: Int, val maxSize: Int, val minLevel: Int, val maxLevel: Int, val generate: Boolean = true)