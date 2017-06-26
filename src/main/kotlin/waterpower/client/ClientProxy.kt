/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client

import waterpower.common.CommonProxy
import waterpower.common.init.WPBlocks

class ClientProxy() : CommonProxy() {
    override fun onPreInit() {
        for (block in WPBlocks.blocks)
            block.registerModels()
    }
}