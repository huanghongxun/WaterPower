/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block

import net.minecraft.client.util.ITooltipFlag
import net.minecraft.item.ItemBlock
import net.minecraft.item.ItemStack
import net.minecraft.util.IStringSerializable
import net.minecraft.world.World
import waterpower.common.INameable

class ItemBlockEnum<T>(val blockEnum: BlockEnum<T>, val types: Array<T>) : ItemBlock(blockEnum)
where T : Enum<T>, T : INameable, T : IStringSerializable {
    init {
        setHasSubtypes(true)
    }

    override fun getItemStackDisplayName(stack: ItemStack) =
            types[stack.itemDamage].getLocalizedName()

    override fun getMetadata(damage: Int) =
            if (damage < blockEnum.maxMetaData()) damage
            else 0

    override fun getUnlocalizedName(stack: ItemStack) =
            if (stack.itemDamage >= types.size) null
            else types[stack.itemDamage].getUnlocalizedName()

    override fun addInformation(stack: ItemStack, worldIn: World?, tooltip: MutableList<String>, flagIn: ITooltipFlag) {
        types[stack.itemDamage].addInformation(tooltip)
    }
}