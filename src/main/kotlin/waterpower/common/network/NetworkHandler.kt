/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.network

import net.minecraftforge.fml.common.network.NetworkRegistry
import net.minecraftforge.fml.common.network.simpleimpl.IMessage
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler
import net.minecraftforge.fml.relauncher.Side
import waterpower.WaterPower
import waterpower.annotations.Init
import waterpower.annotations.Message
import waterpower.annotations.Parser
import java.util.*

@Init
@Parser
object NetworkHandler {

    val instance = NetworkRegistry.INSTANCE.newSimpleChannel(WaterPower.MOD_ID)
    val messages = LinkedList<Pair<Class<Packet<*>>, Side>>()
    var id = 0

    /**
     * @see waterpower.annotations.Parser
     */
    @JvmStatic
    fun loadClass(clz: Class<*>) {
        val message = clz.getAnnotation(Message::class.java)
        if (message != null)
            messages += (clz as Class<Packet<*>>) to message.handlerSide
    }

    @Synchronized
    fun nextId() = ++id

    @JvmStatic
    fun <T> preInit()
            where T : IMessage, T : IMessageHandler<T, IMessage> {
        for (message in messages) {
            instance.registerMessage(message.first as Class<T>,
                    message.first as Class<T>, nextId(), message.second)
        }
    }
}