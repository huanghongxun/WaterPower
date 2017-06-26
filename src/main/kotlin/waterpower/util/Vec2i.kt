/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.util

data class Vec2i(var x: Int = 0, var y: Int = 0) {

    operator fun plusAssign(b: Vec2i) {
        x += b.x
        y += b.y
    }

    fun reset() {
        x = 0
        y = 0
    }
}
