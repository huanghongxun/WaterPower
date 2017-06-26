/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.client.render

import net.minecraft.client.Minecraft
import net.minecraft.client.renderer.block.model.ItemCameraTransforms
import net.minecraft.client.renderer.block.model.ModelBlock
import net.minecraft.util.ResourceLocation
import net.minecraftforge.fml.relauncher.Side
import net.minecraftforge.fml.relauncher.SideOnly
import java.io.IOException
import java.io.InputStreamReader

@SideOnly(Side.CLIENT)
object Models {

    var DEFAULT_TRANSFORMS = getTransformsFromModel("models/item/generated.json")
    var HANDHELD_TRANSFORMS = getTransformsFromModel("models/item/handheld.json")
    var BLOCK_TRANSFORMS = getTransformsFromModel("models/block/block.json")

    fun getTransformsFromModel(path: String): ItemCameraTransforms {
        try {
            val location = ResourceLocation(path)
            val resource = Minecraft.getMinecraft().resourceManager.getResource(location)
            InputStreamReader(resource.inputStream).use { reader ->
                val modelBlock = ModelBlock.deserialize(reader)
                return modelBlock.allTransforms
            }
        } catch (notFound: IOException) {
        }

        return ItemCameraTransforms.DEFAULT
    }
}
