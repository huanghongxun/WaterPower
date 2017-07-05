/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.entity.player.EntityPlayer
import net.minecraft.item.ItemStack
import waterpower.WaterPower
import waterpower.client.i18n
import waterpower.util.emptyStack

abstract class ItemTool(id: String, maxUse: Int) : ItemBase(id) {
    init {
        maxDamage = maxUse - 1
        setMaxStackSize(1)
    }

    override fun addInformation(stack: ItemStack, playerIn: EntityPlayer, tooltip: MutableList<String>, advanced: Boolean) {
        tooltip.add(i18n("waterpower.tooltip.uses_left", getRemainingUses(stack)))
    }

    override fun hasContainerItem(stack: ItemStack?): Boolean {
        return true
    }

    override fun getContainerItem(stack: ItemStack): ItemStack {
        val ret = stack.copy()
        if (ret.attemptDamageItem(1, WaterPower.random)) {
            return emptyStack
        }
        return ret
    }

    companion object {
        fun getRemainingUses(stack: ItemStack)
                = stack.maxDamage - stack.itemDamage + 1
    }
}