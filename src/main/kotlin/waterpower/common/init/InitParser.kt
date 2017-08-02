/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.init

import com.google.common.collect.LinkedHashMultimap
import com.google.common.collect.Lists
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.fml.common.eventhandler.EventPriority
import waterpower.JavaAdapter
import waterpower.annotations.ClassEngine
import waterpower.annotations.Init
import waterpower.annotations.Parser
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodType
import java.util.*

@Parser
object InitParser {

    val inits = LinkedHashMap<LoaderState.ModState, LinkedHashMultimap<EventPriority, MethodHandle>>()
    var state = LoaderState.ModState.UNLOADED

    init {
        for (modState in LoaderState.ModState.values())
            inits[modState] = LinkedHashMultimap.create()
    }

    fun addMethod(init: Init, state: LoaderState.ModState, clazz: Class<*>, methodName: String) {
        try {
            val handle = ClassEngine.lookup.findStatic(clazz, methodName, MethodType.methodType(Void.TYPE))
            inits[state]!![init.priority].add(handle)
        } catch(ignore: NoSuchMethodException) {
        }
    }

    /**
     * @see waterpower.annotations.Parser
     */
    @JvmStatic
    fun loadClass(clazz: Class<*>) {
        try {
            val init = clazz.getAnnotation(Init::class.java)
            if (init != null) {
                addMethod(init, LoaderState.ModState.INITIALIZED, clazz, "init")
                addMethod(init, LoaderState.ModState.PREINITIALIZED, clazz, "preInit")
                addMethod(init, LoaderState.ModState.POSTINITIALIZED, clazz, "postInit")
            }
        } catch(ignore: Exception) {
            FMLLog.log.fatal("", ignore)
        }
    }

    fun init(state: LoaderState.ModState) {
        if (this.state.ordinal >= state.ordinal || !inits.containsKey(state))
            return
        this.state = state

        for (priority in EventPriority.values())
            for (method in inits[state]!![priority])
            // kotlin will also generate a non-static method in Companion class, we need to solve this.
                JavaAdapter.invokeMethodHandle(method)
    }

}