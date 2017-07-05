/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.integration

import net.minecraftforge.fml.common.Loader
import net.minecraftforge.fml.common.ModAPIManager
import net.minecraftforge.fml.common.versioning.VersionParser
import waterpower.annotations.Init
import waterpower.annotations.Integration
import waterpower.annotations.Parser
import waterpower.annotations.objectInstance
import java.util.*

@Init
@Parser
object IntegrationParser {
    val modules = LinkedList<IModule>()

    @JvmStatic
    fun loadClass(clz: Class<*>) {
        val integration = clz.getAnnotation(Integration::class.java)
        if (integration != null && isModAvailable(integration.modID)) {
            modules += objectInstance(clz) as IModule
        }
    }

    @JvmStatic
    fun preInit() {
        modules.forEach { it.onPreInit() }
    }

    @JvmStatic
    fun init() {
        modules.forEach { it.onInit() }
    }

    @JvmStatic
    fun postInit() {
        modules.forEach { it.onPostInit() }
    }
}

fun isModAvailable(id: String): Boolean {
    val version = VersionParser.parseVersionReference(id)
    if (Loader.isModLoaded(version.label))
        return version.containsVersion(Loader.instance().indexedModList.get(version.label)?.processedVersion)
    else
        return ModAPIManager.INSTANCE.hasAPI(version.label)
}