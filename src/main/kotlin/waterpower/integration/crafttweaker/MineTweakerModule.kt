/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.crafttweaker

import minetweaker.MineTweakerAPI
import waterpower.annotations.Integration
import waterpower.integration.IDs
import waterpower.integration.IModule

@Integration(IDs.CraftTweaker)
object MineTweakerModule : IModule() {
    override fun onInit() {
        MineTweakerAPI.registerClass(Machines::class.java)
    }
}
