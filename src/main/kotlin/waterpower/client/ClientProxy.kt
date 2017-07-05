/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client

import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import waterpower.common.CommonProxy
import waterpower.common.init.WPBlocks

@SideOnly(Side.CLIENT)
class ClientProxy() : CommonProxy() {
    override fun onPreInit() {
        for (block in WPBlocks.blocks)
            block.registerModels()
    }
}