/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower

import net.minecraftforge.fml.common.FMLModContainer
import net.minecraftforge.fml.common.ILanguageAdapter
import net.minecraftforge.fml.common.ModContainer
import net.minecraftforge.fml.relauncher.Side
import waterpower.annotations.objectInstance
import java.lang.reflect.Field
import java.lang.reflect.Method

class KotlinAdapter : ILanguageAdapter {
    override fun setProxy(target: Field?, proxyTarget: Class<*>?, proxy: Any?) {
        target?.set(null, proxy)
    }

    override fun getNewInstance(container: FMLModContainer?, objectClass: Class<*>, classLoader: ClassLoader?, factoryMarkedAnnotation: Method?): Any?
            = objectInstance(objectClass)

    override fun supportsStatics() = false

    override fun setInternalProxies(mod: ModContainer?, side: Side?, loader: ClassLoader?) = Unit

}
