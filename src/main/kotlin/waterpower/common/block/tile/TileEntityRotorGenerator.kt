/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.block.tile

import waterpower.Preference
import waterpower.common.block.inventory.InventorySlotRotor
import waterpower.common.item.ItemRotor
import waterpower.util.isStackEmpty

abstract class TileEntityRotorGenerator(production: Int, maxStorage: Double, tier: Int)
    : TileEntityGenerator(production, maxStorage, tier) {
    val slotRotor = InventorySlotRotor(this)

    fun hasRotor() = !slotRotor.isEmpty() && !isStackEmpty(slotRotor[0]) && slotRotor[0].item is ItemRotor
    fun getRotor() = (slotRotor.get(0).item as ItemRotor).rotor
    fun damageRotor(tick: Int) {
        if (!hasRotor()) return
        val rotor = getRotor()
        if (rotor.isDamageable()) {
            slotRotor[0].itemDamage++
            if (slotRotor[0].itemDamage > rotor.maxDamage)
                slotRotor.clear(0)
            markDirty()
        }
    }

    fun getEfficiency() =
            if (Preference.General.rotorNeeded)
                if (hasRotor()) getRotor().efficiency
                else 0.0
            else
                1.0
}