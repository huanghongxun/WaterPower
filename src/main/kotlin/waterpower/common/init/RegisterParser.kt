/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.init

import net.minecraft.tileentity.TileEntity
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.fml.common.registry.GameRegistry
import waterpower.annotations.Init
import waterpower.annotations.Parser
import waterpower.annotations.Register
import waterpower.annotations.isSubClass

@Parser
object RegisterParser {
    val classes = mutableListOf<Pair<Class<*>, String>>()

    @JvmStatic
    fun loadClass(cls: Class<*>) {
        val register = cls.getAnnotation(Register::class.java)
        if (register != null) {
            classes += cls to register.id
        }
    }

    @JvmStatic
    @Init(LoaderState.ModState.POSTINITIALIZED)
    fun init() {
        for ((cls, id) in classes)
            if (isSubClass(TileEntity::class.java, cls))
                GameRegistry.registerTileEntity(cls as Class<out TileEntity>, id)
    }
}