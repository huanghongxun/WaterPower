/**
 * Copyright (c) Huang Yuhui, 2017
 *
 * "WaterPower" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */
package waterpower.common.item

import net.minecraft.init.Blocks
import net.minecraft.init.Items
import net.minecraft.item.ItemStack
import net.minecraftforge.fml.common.LoaderState
import net.minecraftforge.oredict.OreDictionary
import net.minecraftforge.oredict.OreDictionary.doesOreNameExist
import waterpower.annotations.Init
import waterpower.annotations.NewInstance
import waterpower.common.INameable
import waterpower.common.init.WPBlocks
import waterpower.common.init.WPItems
import waterpower.common.recipe.Recipes
import waterpower.common.recipe.Recipes.craft
import waterpower.common.recipe.Recipes.craftShapeless
import waterpower.integration.IDs
import waterpower.integration.Mod
import waterpower.integration.RailcraftModule
import waterpower.integration.ic2.ICItemFinder
import waterpower.util.generalize
import waterpower.util.getItemStack


@NewInstance(LoaderState.ModState.PREINITIALIZED)
class ItemCrafting : ItemEnum<EnumCrafting>("crafting", EnumCrafting.values()) {

    init {
        WPItems.crafting = this
        WPItems.items += this
    }

    override fun getTextureFolder() = "crafting"

    companion object {
        fun get(type: EnumCrafting, amount: Int = 1) = WPItems.crafting.getItemStack(type, amount)

        @JvmStatic
        @Init(LoaderState.ModState.POSTINITIALIZED)
        fun addRecipes() {
            OreDictionary.registerOre("plateDenseRedstone", get(EnumCrafting.dense_redstone_plate))
            OreDictionary.registerOre("dustCactus", get(EnumCrafting.cactus_dust))
            OreDictionary.registerOre("dustIron", get(EnumCrafting.iron_dust))
            OreDictionary.registerOre("dustGold", get(EnumCrafting.gold_dust))
            OreDictionary.registerOre("dustDiamond", get(EnumCrafting.diamond_dust))

            val ic2 = Mod.IndustrialCraft2.isAvailable
            val copperCable: Any = getItemStack(IDs.Mekanism, "PartTransmitter", 2) ?: ItemStack.EMPTY
            val industrialDiamond: Any = ICItemFinder.getItem("crafting", "industrial_diamond") ?: "gemDiamond"
            val advancedAlloy: Any = ICItemFinder.getItem("crafting", "alloy") ?: "plateSteel"
            val iridiumPlate: Any = ICItemFinder.getItem("crafting", "iridium") ?: "plateVanadiumSteel"
            val carbonPlate: Any = ICItemFinder.getItem("crafting", "carbon_plate") ?: "gemDiamond"
            val machine: Any = ICItemFinder.getItem("resource", "machine") ?: getItemStack(IDs.Mekanism, "BasicBlock", 8) ?: "blockIron"
            val transformerUpgrade: Any = ICItemFinder.getItem("upgrade", "transformer") ?: "circuitBasic"

            craft(get(EnumCrafting.stone_structure), "WW ", "WWS", "WW ", 'W', Blocks.STONEBRICK, 'S', WPItems.hammer)
            craft(ItemStack(WPItems.hammer), "WW ", "WWI", "WW ", 'W', "logWood", 'I', Items.STICK)
            craft(get(EnumCrafting.silver_coil), "SSS", "SIS", "SSS", 'S', "ingotSilver", 'I', "ingotIndustrialSteel")
            craft(get(EnumCrafting.silver_coil), "SSS", "SIS", "SSS", 'S', "ingotNeodymium", 'I', "ingotIndustrialSteel")
            var flag = 0
            if (ic2 && !Mod.GregTech.isAvailable) {
                flag = 1
                craftShapeless(get(EnumCrafting.dense_copper_coil), ICItemFinder.getItem("crafting", "coil"), ICItemFinder.getItem("cable", "type:copper,insulation:0"))
                Recipes.blastFurnace(ICItemFinder.getItem("crafting", "carbon_mesh")!!, ItemCrafting.get(EnumCrafting.high_purity_carbon_dust), 1000)
            } else {
                Recipes.blastFurnace(ItemStack(Items.COAL), ItemCrafting.get(EnumCrafting.high_purity_carbon_dust), 1000)
            }
            if (ic2 && Mod.Mekanism.isAvailable) {
                flag = 1
                craftShapeless(get(EnumCrafting.dense_copper_coil), ICItemFinder.getItem("crafting", "coil"), copperCable)
            }
            if (flag == 0) {
                craft(get(EnumCrafting.dense_copper_coil), " I ", "CCC", " I ", 'C', "ingotVanadium", 'I', "ingotIron")
                craft(get(EnumCrafting.dense_copper_coil), " I ", "CCC", " I ", 'C', "ingotCopper", 'I', "ingotIron")
            }
            Recipes.craftShapeless(get(EnumCrafting.dense_silver_coil), get(EnumCrafting.silver_coil), get(EnumCrafting.silver_coil))

            val stack = get(EnumCrafting.water_uranium_ingot, 36)
            val stack2 = get(EnumCrafting.water_uranium_ingot, 4)

            if (ic2)
                craft(stack, "SAS", "ASA", "SAS", 'A', "blockUranium", 'S', ICItemFinder.getItem("hex_heat_storage"))
            else
                craft(get(EnumCrafting.water_uranium_ingot), "SSS", "SAS", "SSS", 'A', "ingotManganese", 'S', "gemLapis")

            if (doesOreNameExist("ingotUranium") && ic2) {
                if (Mod.RailCraft.isAvailable)
                    for (sasasasa in OreDictionary.getOres("ingotUranium"))
                        RailcraftModule.rollingMachine(stack2, "SAS", "ASA", "SAS", 'A', sasasasa, 'S', ICItemFinder.getItem("fluid_cell,ic2coolant"));
                else
                    craft(stack2, "SAS", "ASA", "SAS", 'A', "ingotUranium", 'S', ICItemFinder.getItem("fluid_cell,ic2coolant"))
            }
            craft(get(EnumCrafting.water_uranium_plate_mk1), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_ingot), 'S', "plateBronze")
            craft(get(EnumCrafting.water_uranium_plate_mk1), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_ingot), 'S', "plateManganese")
            craft(get(EnumCrafting.water_uranium_plate_mk2), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_plate_mk1), 'S', carbonPlate)

            craft(get(EnumCrafting.water_uranium_plate_mk3), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_plate_mk2), 'S', advancedAlloy)

            craft(get(EnumCrafting.water_uranium_plate_mk4), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_plate_mk3), 'S',
                    "plateIndustrialSteel")
            craft(get(EnumCrafting.water_uranium_plate_mk5), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_plate_mk4), 'S', "plateManganeseSteel")
            craft(get(EnumCrafting.water_uranium_plate_mk6), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_plate_mk5), 'S',
                    "plateVanadiumSteel")

            if (ic2)
                craft(get(EnumCrafting.water_uranium_plate_mk7), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_plate_mk6), 'S',
                        ICItemFinder.getItem("crafting,iridium"))
            else
                craft(get(EnumCrafting.water_uranium_plate_mk7), "SSS", "SAS", "SSS", 'A', get(EnumCrafting.water_uranium_plate_mk6), 'S',
                        "plateNeodymiumMagnet")

            var iron: Any? = null
            if (ic2)
                iron = "plateIron"
            if (iron == null)
                iron = "plateVanadium"

            craft(get(EnumCrafting.updater_mk0), "SAS", "AGA", "SAS", 'S', advancedAlloy, 'A', get(EnumCrafting.water_uranium_plate_mk1), 'G',
                    transformerUpgrade)
            craft(get(EnumCrafting.updater_mk1), "SMS", "UGU", "SAS", 'S', Items.REDSTONE, 'U', get(EnumCrafting.water_uranium_plate_mk1), 'A', "gemDiamond",
                    'M', "plateSteel", 'G', get(EnumCrafting.updater_mk0))
            craft(get(EnumCrafting.updater_mk2), "SMS", "UGU", "SAS", 'S', advancedAlloy, 'U', get(EnumCrafting.water_uranium_plate_mk2), 'A', carbonPlate,
                    'M', Blocks.LAPIS_BLOCK, 'G', get(EnumCrafting.updater_mk1))
            craft(get(EnumCrafting.updater_mk3), "SMS", "UGU", "SAS", 'S', advancedAlloy, 'U', get(EnumCrafting.water_uranium_plate_mk3), 'A',
                    industrialDiamond, 'M', "gemDiamond", 'G', get(EnumCrafting.updater_mk2))
            craft(get(EnumCrafting.iridium_iron_alloy_plate), "SSS", "SGS", "SSS", 'S', iron, 'G', iridiumPlate)
            if (ic2) {
                craft(get(EnumCrafting.updater_mk4), "SMS", "UGU", "SAS", 'S', ICItemFinder.getItem("crafting,alloy"), 'U',
                        get(EnumCrafting.water_uranium_plate_mk4), 'A', ICItemFinder.getItem("advanced_re_battery")?.generalize(), 'M', Blocks.EMERALD_BLOCK, 'G',
                        get(EnumCrafting.updater_mk3))
                craft(get(EnumCrafting.updater_mk5), "SMS", "UGU", "SAS", 'S', ICItemFinder.getItem("crafting,alloy"), 'U',
                        get(EnumCrafting.water_uranium_plate_mk5), 'A', ICItemFinder.getItem("energy_crystal")?.generalize(), 'M',
                        if (doesOreNameExist("blockRuby")) "blockRuby" else "blockManganeseSteel", 'G', get(EnumCrafting.updater_mk4))
                craft(get(EnumCrafting.updater_mk6), "SMS", "UGU", "SAS", 'S', advancedAlloy, 'U', get(EnumCrafting.water_uranium_plate_mk6), 'A',
                        ICItemFinder.getItem("lapotron_crystal")?.generalize(), 'M', if (doesOreNameExist("blockSapphire")) "blockSapphire" else "blockVanadium",
                        'G', get(EnumCrafting.updater_mk5))
                craft(get(EnumCrafting.updater_mk7), "SMS", "UGU", "SAS", 'S', ICItemFinder.getItem("crafting,alloy"), 'U',
                        get(EnumCrafting.water_uranium_plate_mk7), 'A', ICItemFinder.getItem("single_use_battery")?.generalize(), 'M',
                        if (doesOreNameExist("blockChrome")) "blockChrome" else "blockVanadiumSteel", 'G', get(EnumCrafting.updater_mk6))
            } else {
                craft(get(EnumCrafting.updater_mk4), "SMS", "UGU", "SAS", 'S', "plateSteel", 'U', get(EnumCrafting.water_uranium_plate_mk4), 'A',
                        "stickManganese", 'M', "blockEmerald", 'G', get(EnumCrafting.updater_mk3))
                craft(get(EnumCrafting.updater_mk5), "SMS", "UGU", "SAS", 'S', "plateSteel", 'U', get(EnumCrafting.water_uranium_plate_mk5), 'A',
                        "stickVanadium", 'M', if (doesOreNameExist("blockRuby")) "blockRuby" else "blockManganeseSteel", 'G', get(EnumCrafting.updater_mk4))
                craft(get(EnumCrafting.updater_mk6), "SMS", "UGU", "SAS", 'S', "plateSteel", 'U', get(EnumCrafting.water_uranium_plate_mk6), 'A',
                        "stickNeodymium", 'M', if (doesOreNameExist("blockSapphire")) "blockSapphire" else "blockVanadium", 'G', get(EnumCrafting.updater_mk5))
                craft(get(EnumCrafting.updater_mk7), "SMS", "UGU", "SAS", 'S', "plateSteel", 'U', get(EnumCrafting.water_uranium_plate_mk7), 'A',
                        "stickNeodymiumMagnet", 'M', if (doesOreNameExist("blockChrome")) "blockChrome" else "blockVanadiumSteel", 'G', get(EnumCrafting.updater_mk6))
            }

            craft(get(EnumCrafting.reservoir_core), "ASA", "SMS", "CSC", 'A', "circuitBasic", 'S', advancedAlloy, 'M', machine, 'C',
                    get(EnumCrafting.water_uranium_plate_mk1))
            if (ic2) {
                craft(get(EnumCrafting.reservoir_core_advanced), "IDI", "AMA", "IDI", 'I', iridiumPlate, 'A',
                        ICItemFinder.getItem("crafting,advanced_circuit"), 'M', get(EnumCrafting.reservoir_core), 'D', "gemDiamond")
            }
            craft(get(EnumCrafting.reservoir_core), "RPR", "PCP", "RPR", 'R', "stickZinc", 'P', "plateZinc", 'C', "gearZinc")
            craft(get(EnumCrafting.reservoir_core_advanced), "RPR", "PCP", "RPR", 'R', "stickVanadiumSteel", 'P', "plateVanadiumSteel", 'C',
                    "gearVanadiumSteel")

            craft(get(EnumCrafting.water_uranium_alloy_plate), "UIU", "IDI", "UIU", 'U', get(EnumCrafting.water_uranium_ingot), 'I', iridiumPlate, 'D',
                    "gemDiamond")

            craft(get(EnumCrafting.plasma_uranium_alloy_plate), "OPO", "PUP", "OPO", 'O', if (doesOreNameExist("plateOsmium"))
                "plateOsmium"
            else
                "plateVanadium", 'P', get(EnumCrafting.plasma_uranium_ingot), 'U', get(EnumCrafting.water_uranium_alloy_plate))

            craft(get(EnumCrafting.diamond_blade), "ITI", "TDT", "ITI", 'I', "plateIron", 'D', "gemDiamond", 'T', "dustDiamond")
            craft(get(EnumCrafting.diamond_glazing_wheel), "SSS", "SAS", "SSS", 'S', Items.FLINT, 'A', get(EnumCrafting.diamond_blade))
            craft(get(EnumCrafting.industrial_steel_hydraulic_cylinder), "PCP", "CBC", " I ", 'P', "plateIndustrialSteel", 'C',
                    ItemComponent.get(EnumComponent.casing, EnumLevel.MK4), 'B', "blockIndustrialSteel", 'I', "ingotIndustrialSteel")
            craft(get(EnumCrafting.brass_centrifuge_pot), "P P", "P P", "PDP", 'P', "plateZincAlloy", 'D', "plateDenseZincAlloy")
            craft(get(EnumCrafting.brass_centrifuge_pot), "P P", "P P", "PDP", 'P', "plateZincAlloy", 'D', "plateDenseZincAlloy")
            craft(get(EnumCrafting.vanadium_steel_piston_cylinder), "PCP", "DBD", "PPP", 'P', "plateVanadiumSteel", 'C', WPBlocks.compressor, 'D',
                    "plateDenseVanadiumSteel", 'B', "blockVanadiumSteel")
            craft(get(EnumCrafting.vanadium_steel_water_pipe), "DDD", "D D", "DDD", 'D', "plateVanadium")
            craft(get(EnumCrafting.ruby_water_hole), "IBI", "B B", "IBI", 'B', "blockRuby", 'I', "plateVanadium")
            craft(get(EnumCrafting.ruby_water_hole), "IBI", "B B", "IBI", 'B', "blockVanadium", 'I', "plateVanadium")
            craft(get(EnumCrafting.data_ball), " D ", "CIC", " D ", 'D', Items.EMERALD, 'C',
                    "circuitBasic", 'I', "circuitAdvanced")
            Recipes.crusher(ItemStack(Blocks.CACTUS).generalize(), get(EnumCrafting.cactus_dust))

            // If IC2 isn't loaded, I'll add crafting recipes for diamond dust.
            if (!ic2) {
                Recipes.crusher(ItemStack(Items.DIAMOND), get(EnumCrafting.diamond_dust))
                Recipes.crusher(ItemStack(Blocks.IRON_ORE), get(EnumCrafting.iron_dust, 2))
                Recipes.crusher(ItemStack(Blocks.GOLD_ORE), get(EnumCrafting.gold_dust, 2))
                Recipes.crusher(ItemStack(Items.IRON_INGOT), get(EnumCrafting.iron_dust))
                Recipes.crusher(ItemStack(Items.GOLD_INGOT), get(EnumCrafting.gold_dust))
                Recipes.furnace(get(EnumCrafting.iron_dust), ItemStack(Items.IRON_INGOT))
                Recipes.furnace(get(EnumCrafting.gold_dust), ItemStack(Items.GOLD_INGOT))
            }

            Recipes.compressor(ItemStack(Blocks.REDSTONE_BLOCK), get(EnumCrafting.dense_redstone_plate))

            if (Mod.IndustrialCraft2.isAvailable)
                craft(get(EnumCrafting.base_rotor), "ILI", "LCL", "ILI", 'I', "plateDenseIron", 'L', "plateDenseLead", 'C', carbonPlate);
            else
                craft(get(EnumCrafting.base_rotor), "ILI", "LCL", "ILI", 'I', "plateDenseSteel", 'L', "plateDenseZinc", 'C', carbonPlate);

        }
    }
}

enum class EnumCrafting : INameable {
    water_uranium_ingot,
    water_uranium_plate_mk1,
    water_uranium_plate_mk2,
    water_uranium_plate_mk3,
    water_uranium_plate_mk4,
    water_uranium_plate_mk5,
    water_uranium_plate_mk6,
    water_uranium_plate_mk7,
    water_uranium_alloy_plate,
    plasma_uranium_ingot,
    plasma_uranium_alloy_plate,
    iridium_iron_alloy_plate,
    updater_mk0,
    updater_mk1,
    updater_mk2,
    updater_mk3,
    updater_mk4,
    updater_mk5,
    updater_mk6,
    updater_mk7,
    reservoir_core,
    reservoir_core_advanced,
    base_rotor,
    stone_structure,
    water_resistant_rubber,
    water_resistant_rubber_plate,
    water_resistant_rubber_dense_plate,
    dense_redstone_plate,
    dense_copper_coil,
    oxygen_ethanol_fuel,
    silver_coil,
    dense_silver_coil,
    high_purity_carbon_dust,
    tungsten_carbide_ingot,
    diamond_blade,
    diamond_glazing_wheel,
    brass_centrifuge_pot,
    industrial_steel_hydraulic_cylinder,
    vanadium_steel_piston_cylinder,
    vanadium_steel_water_pipe,
    ruby_water_hole,
    data_ball,
    cactus_dust,
    iron_dust,
    gold_dust,
    diamond_dust;

    override fun getUnlocalizedName() = "waterpower.crafting." + name

    override fun getName() = name.toLowerCase()
}