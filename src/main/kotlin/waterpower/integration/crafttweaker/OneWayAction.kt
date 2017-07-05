/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration.crafttweaker

import minetweaker.IUndoableAction

abstract class OneWayAction : IUndoableAction {

    override fun canUndo(): Boolean {
        return false
    }

    override fun describeUndo(): String {
        return "Impossible"
    }

    override fun getOverrideKey(): Any? {
        return null
    }

    override fun undo() {}

}
