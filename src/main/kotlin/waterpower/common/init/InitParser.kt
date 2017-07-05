/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.init

import com.google.common.collect.Lists
import net.minecraftforge.fml.common.LoaderState
import waterpower.JavaAdapter
import waterpower.annotations.ClassEngine
import waterpower.annotations.Init
import waterpower.annotations.Parser
import java.lang.invoke.MethodHandle
import java.lang.invoke.MethodType
import java.util.*

@Parser
object InitParser {

    val inits = LinkedHashMap<LoaderState.ModState, LinkedList<MethodHandle>>()
    val initsLast = LinkedHashMap<LoaderState.ModState, LinkedList<MethodHandle>>()
    var state = LoaderState.ModState.UNLOADED

    init {
        for (modState in LoaderState.ModState.values()) {
            inits[modState] = Lists.newLinkedList()
            initsLast[modState] = Lists.newLinkedList()
        }
    }

    fun addMethod(init: Init, state: LoaderState.ModState, clazz: Class<*>, methodName: String) {
        try {
            val handle = ClassEngine.lookup.findStatic(clazz, methodName, MethodType.methodType(JavaAdapter.getVoidClass()))
            if (init.priority < 0)
                inits[state]!!.addFirst(handle)
            else if (init.priority == 0)
                inits[state]!!.add(handle)
            else
                initsLast[state]!!.add(handle)
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
        } catch(ignore: NoClassDefFoundError) {
            ignore.printStackTrace()
        } catch(ignore1: ClassNotFoundException) {
        }
    }

    fun init(state: LoaderState.ModState) {
        if (this.state.ordinal >= state.ordinal || !inits.containsKey(state))
            return
        this.state = state

        inits[state]!!.addAll(initsLast[state]!!)
        for (method in inits.get(state)!!.iterator())
        // kotlin will also generate a non-static method in Companion class, we need to solve this.
            JavaAdapter.invokeMethodHandle(method)
    }

}