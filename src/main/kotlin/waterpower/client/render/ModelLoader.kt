/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render

import net.minecraft.client.resources.IResourceManager
import net.minecraft.util.ResourceLocation
import net.minecraftforge.client.model.ICustomModelLoader
import net.minecraftforge.client.model.IModel
import net.minecraftforge.client.model.ModelLoaderRegistry
import net.minecraftforge.fml.common.LoaderState
import waterpower.annotations.Init
import waterpower.common.init.WPItems

object ModelLoader : ICustomModelLoader {

    override fun loadModel(modelLocation: ResourceLocation): IModel {
        return IconRegisterService;
    }

    override fun onResourceManagerReload(resourceManager: IResourceManager?) {
    }

    override fun accepts(modelLocation: ResourceLocation): Boolean {
        if (modelLocation.resourcePath.contains("models/item/")) {
            val loc = ResourceLocation(modelLocation.resourceDomain, modelLocation.resourcePath.replace("models/item/", ""))
            return WPItems.registryNames.contains(loc);
        } else return false
    }

    @JvmStatic
    @Init(LoaderState.ModState.PREINITIALIZED, side = 0)
    fun preInit() {
        ModelLoaderRegistry.registerLoader(this)
    }
}