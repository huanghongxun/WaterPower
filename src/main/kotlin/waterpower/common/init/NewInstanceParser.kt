/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.init

import com.google.common.collect.Lists
import net.minecraftforge.fml.common.FMLLog
import net.minecraftforge.fml.common.LoaderState
import waterpower.JavaAdapter
import waterpower.annotations.ClassEngine
import waterpower.annotations.NewInstance
import waterpower.annotations.Parser
import java.lang.invoke.MethodType
import java.util.*

@Parser
object NewInstanceParser {

    val inits = LinkedHashMap<LoaderState.ModState, LinkedList<Class<*>>>()
    var state = LoaderState.ModState.UNLOADED

    /**
     * @see waterpower.annotations.Parser
     */
    @JvmStatic
    fun loadClass(clazz: Class<*>) {
        val newInstance = clazz.getAnnotation(NewInstance::class.java)
        if (newInstance != null) {
            if (!inits.containsKey(newInstance.modState))
                inits.put(newInstance.modState, Lists.newLinkedList())
            if (newInstance.priority < 0)
                inits.get(newInstance.modState)!!.addFirst(clazz)
            else
                inits.get(newInstance.modState)!!.add(clazz)
        }
    }

    fun init(state: LoaderState.ModState) {
        if (this.state.ordinal >= state.ordinal || !inits.containsKey(state))
            return
        this.state = state

        for (clazz in inits.get(state)!!.iterator())
            JavaAdapter.invokeMethodHandle(ClassEngine.lookup.findConstructor(clazz, MethodType.methodType(Void.TYPE)))
    }
}