/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.LoaderState
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.api.IUpgrade
import waterpower.common.INameable
import waterpower.common.block.reservoir.Reservoirs
import waterpower.common.init.WPBlocks
import waterpower.common.init.WPItems
import waterpower.common.recipe.Recipes.craft
import waterpower.common.recipe.Recipes.craftShapeless
import waterpower.integration.Mod
import waterpower.integration.ic2.ICItemFinder

@Init
@NewInstance(LoaderState.ModState.PREINITIALIZED)
class ItemPlugins() : ItemEnum<Plugins>("plugin", Plugins.values()), IUpgrade {

    override fun getUnderworldAdditionalValue(stack: ItemStack): Int {
        if (stack.metadata >= Plugins.values().size) return 0
        else return Plugins.values()[stack.itemDamage].under
    }

    override fun getOverworldAdditionalValue(stack: ItemStack): Int {
        if (stack.metadata >= Plugins.values().size) return 0
        else return Plugins.values()[stack.itemDamage].over
    }

    override fun getRainAdditionalValue(stack: ItemStack): Int {
        if (stack.metadata >= Plugins.values().size) return 0
        else return Plugins.values()[stack.itemDamage].rain
    }

    override fun getSpeedAdditionalValue(stack: ItemStack): Double {
        if (stack.metadata >= Plugins.values().size) return 0.0
        else return Plugins.values()[stack.itemDamage].speed
    }

    override fun getStorageAdditionalValue(stack: ItemStack): Int {
        if (stack.metadata >= Plugins.values().size) return 0
        else return Plugins.values()[stack.itemDamage].storage * 10000
    }

    override fun getEnergyDemandMultiplier(stack: ItemStack): Double {
        if (stack.metadata >= Plugins.values().size) return 0.0
        else return Plugins.values()[stack.itemDamage].demand
    }

    init {
        WPItems.plugin = this
        WPItems.items += this
    }

    override fun getTextureFolder() = "plugins"

    companion object {
        @JvmStatic
        fun postInit() {
            craft(WPItems.plugin.getItemStack(Plugins.storage_mk4), "AA", "AA", 'A', WPItems.plugin.getItemStack(Plugins.storage_mk3))
            craft(WPItems.plugin.getItemStack(Plugins.storage_mk3), "AA", "AA", 'A', WPItems.plugin.getItemStack(Plugins.storage_mk2))
            craft(WPItems.plugin.getItemStack(Plugins.storage_mk2), "AA", "AA", 'A', WPItems.plugin.getItemStack(Plugins.storage_mk1))
            craftShapeless(WPItems.plugin.getItemStack(Plugins.all_round_mk1), WPItems.plugin.getItemStack(Plugins.storage_mk1), WPItems.plugin.getItemStack(Plugins.rain_mk1), WPItems.plugin.getItemStack(Plugins.over_mk1),
                    WPItems.plugin.getItemStack(Plugins.under_mk1))
            craftShapeless(WPItems.plugin.getItemStack(Plugins.storage_mk1), WPBlocks.reservoir.getItemStack(Reservoirs.iron), Items.IRON_INGOT, Items.IRON_INGOT,
                    Items.IRON_INGOT)
            craftShapeless(WPItems.plugin.getItemStack(Plugins.storage_mk2), WPBlocks.reservoir.getItemStack(Reservoirs.vanadium_steel), WPItems.plugin.getItemStack(Plugins.storage_mk1),
                    Items.IRON_INGOT, Items.IRON_INGOT, Items.IRON_INGOT)
            craftShapeless(WPItems.plugin.getItemStack(Plugins.speed_mk1), ItemCrafting.get(EnumCrafting.data_ball), Items.IRON_INGOT, Items.IRON_INGOT, Items.IRON_INGOT)
            var extractor = ItemCrafting.get(EnumCrafting.ruby_water_hole)
            if (Mod.IndustrialCraft2.isAvailable) {
                extractor = ICItemFinder.getItem("te,extractor") ?: ItemCrafting.get(EnumCrafting.ruby_water_hole)
                craft(WPItems.plugin.getItemStack(Plugins.over_mk1), "CBC", "EPE", "CBC", 'C', "ringVanadiumSteel", 'B', ICItemFinder.getItem("te,pump"), 'E',
                        extractor, 'P', ItemCrafting.get(EnumCrafting.vanadium_steel_water_pipe))
            }
            /*if (Mod.BuildCraftFactory.isAvailable) {
                craft(WPItems.plugin.getItemStack(Plugins.over_mk1), "CBC", "EPE", "CBC", 'C', "ringVanadiumSteel", 'B',
                        getItemStack(IDs.BuildCraftFactory, "pumpBlock", 0), 'E', extractor, 'P', ItemCrafting.get(EnumCrafting.vanadium_steel_water_pipe))
            }*/
            if (!Mod.IndustrialCraft2.isAvailable && !Mod.BuildCraftFactory.isAvailable)
                craft(WPItems.plugin.getItemStack(Plugins.over_mk1), "CBC", "EPE", "CBC", 'C', "ringVanadiumSteel", 'B', "gearVanadiumSteel", 'E',
                        ItemCrafting.get(EnumCrafting.ruby_water_hole), 'P', ItemCrafting.get(EnumCrafting.vanadium_steel_water_pipe))
            craftShapeless(WPItems.plugin.getItemStack(Plugins.under_mk1), WPItems.plugin.getItemStack(Plugins.over_mk1))
            craftShapeless(WPItems.plugin.getItemStack(Plugins.over_mk1), WPItems.plugin.getItemStack(Plugins.under_mk1))
            craft(WPItems.plugin.getItemStack(Plugins.rain_mk1), "C C", "EPE", "C C", 'C', "ringVanadiumSteel", 'E', extractor, 'P', ItemCrafting.get(EnumCrafting.vanadium_steel_water_pipe))
        }
    }
}

enum class Plugins(val under: Int, val over: Int, val rain: Int, val speed: Double, val storage: Int, val demand: Double)
    : INameable {
    under_mk1(1, 0, 0, 0.0, 0, 0.0),
    over_mk1(0, 1, 0, 0.0, 0, 0.0),
    rain_mk1(0, 0, 1, 0.0, 0, 0.0),
    storage_mk1(0, 0, 0, 0.0, 1, 0.0),
    storage_mk2(0, 0, 0, 0.0, 4, 0.0),
    storage_mk3(0, 0, 0, 0.0, 16, 0.0),
    storage_mk4(0, 0, 0, 0.0, 64, 0.0),
    all_round_mk1(1, 1, 1, 0.0, 1, 0.0),
    speed_mk1(0, 0, 0, 0.7, 0, 1.6);

    override fun getUnlocalizedName() = "waterpower.plugin." + name

    override fun getName() = name.toLowerCase()

}