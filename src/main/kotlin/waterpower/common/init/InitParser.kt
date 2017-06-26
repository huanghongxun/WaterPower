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
import waterpower.annotations.Init
import waterpower.annotations.Parser
import waterpower.util.isClientSide
import waterpower.util.isServerSide
import java.lang.reflect.Method
import java.util.*

@Parser
object InitParser {

    val inits = LinkedHashMap<LoaderState.ModState, LinkedList<Pair<Init, Method>>>()
    val initsLast = LinkedHashMap<LoaderState.ModState, LinkedList<Pair<Init, Method>>>()
    var state = LoaderState.ModState.UNLOADED

    init {
        for (modState in LoaderState.ModState.values()) {
            inits[modState] = Lists.newLinkedList()
            initsLast[modState] = Lists.newLinkedList()
        }
    }

    /**
     * @see waterpower.annotations.Parser
     */
    @JvmStatic
    fun loadClass(clazz: Class<*>) {
        for (method in clazz.declaredMethods) {
            val init = method.getAnnotation(Init::class.java)
            if (init != null) {
                if (init.priority < 0)
                    inits.get(init.modState)!!.addFirst(init to method)
                else if (init.priority == 0)
                    inits.get(init.modState)!!.add(init to method)
                else
                    initsLast.get(init.modState)!!.add(init to method)
            }
        }
    }

    fun init(state: LoaderState.ModState) {
        if (this.state.ordinal >= state.ordinal || !inits.containsKey(state))
            return
        this.state = state

        inits[state]!!.addAll(initsLast[state]!!)
        for ((init, method) in inits.get(state)!!.iterator())
            if (init.side == 0 && isClientSide() || init.side == 1 && isServerSide() || init.side == 2)
            // kotlin will also generate a non-static method in Companion class, we need to solve this.
                if (!method.declaringClass.name.endsWith("Companion"))
                    method.invoke(null)
    }

}