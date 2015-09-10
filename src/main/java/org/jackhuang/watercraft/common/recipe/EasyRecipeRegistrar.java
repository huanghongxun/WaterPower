/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.oredict.OreDictionary;

import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.crafting.CraftingTypes;
import org.jackhuang.watercraft.common.item.crafting.ItemCrafting;
import org.jackhuang.watercraft.common.item.crafting.LevelTypes;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.common.item.range.PluginType;
import org.jackhuang.watercraft.integration.RailcraftModule;
import org.jackhuang.watercraft.integration.ic2.ICItemFinder;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.registry.GameRegistry;

public class EasyRecipeRegistrar extends IRecipeRegistrar {

    public EasyRecipeRegistrar(Configuration c) {
        super(c);
    }

    public void registerWatermills() {
        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 0), "CUC", "SAS", "PMP", 'C', ICItemFinder.getIC2Item("FluidCell"), 'A', "blockIron",
                'S', "plateIron", 'P', "circuitBasic", 'M', ItemType.MK0.item(), 'U', ItemType.WaterUraniumIngot.item());

        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 1), "CUC", "SAS", "PMP", 'C', ICItemFinder.getIC2Item("FluidCell"), 'A',
                new ItemStack(GlobalBlocks.waterMill, 1, 0), 'S', "plateIron", 'P', "circuitAdvanced", 'M', ItemType.MK0.item(), 'U',
                ItemType.WaterUraniumIngot.item());

        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 3), "CUC", "SAS", "PMP", 'C', ICItemFinder.getIC2Item("FluidCell"), 'A',
                new ItemStack(GlobalBlocks.waterMill, 1, 1), 'S', carbonPlate, 'P', "circuitAdvanced", 'M', ItemType.MK1.item(), 'U',
                ItemType.WaterUraniumPlateMK1.item());

        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 4), "CUC", "SAS", "PMP", 'C', ICItemFinder.getIC2Item("FluidCell"), 'A',
                new ItemStack(GlobalBlocks.waterMill, 1, 3), 'S', advancedAlloy, 'P', "circuitAdvanced", 'M', ItemType.MK2.item(), 'U',
                ItemType.WaterUraniumPlateMK2.item());

        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 5), "CUC", "SAS", "PMP", 'C', ICItemFinder.getIC2Item("FluidCell"), 'A',
                new ItemStack(GlobalBlocks.waterMill, 1, 4), 'S', ICItemFinder.getIC2Item("elemotor"), 'P', "circuitAdvanced", 'M', ItemType.MK2.item(), 'U',
                ItemType.WaterUraniumPlateMK2.item());

        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 6), "CUC", "SAS", "PMP", 'C', ICItemFinder.getIC2Item("FluidCell"), 'A',
                new ItemStack(GlobalBlocks.waterMill, 1, 5), 'S', getUsualItemStack(ICItemFinder.getIC2Item("lapotronCrystal")), 'P', "circuitAdvanced", 'M',
                ItemType.MK3.item(), 'U', ItemType.WaterUraniumPlateMK3.item());

        /*
         * if (!GregTech_API.isGregTechLoaded()) return;
         * 
         * // GregTech_API.getGregTechItem(aIndex, aAmount, aMeta)
         * 
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill, 1, 7),
         * "CUC", "SAS", "PMP", 'C',
         * IndustrialCraftIntegration.getIC2Item("FluidCell"), 'A', new
         * ItemStack(GlobalBlocks.waterMill, 1, 6), 'S',
         * IndustrialCraftIntegration.getIC2Item("iridiumPlate"), 'P',
         * IndustrialCraftIntegration.getIC2Item("advancedCircuit"), 'M',
         * UpdaterType.MK3.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK3.item());
         * 
         * // Data Stream addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.waterMill, 1, 8), "CUC", "SAS", "PMP", 'C',
         * GregTech_API.getGregTechItem(34, 1, 0), 'A', new ItemStack(
         * GlobalBlocks.waterMill, 1, 7), 'S',
         * GregTech_API.getGregTechComponent(1, 1), 'P', Items // Turn // Data
         * Stream Circuit .getItem("advancedCircuit"), 'M',
         * UpdaterType.MK4.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK4.item());
         * 
         * // Data Stream addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.waterMill, 1, 9), "CUC", "SAS", "PMP", 'C',
         * GregTech_API.getGregTechItem(35, 1, 0), 'A', new ItemStack(
         * GlobalBlocks.waterMill, 1, 8), 'S', GregTech_API.getGregTechItem(43,
         * 1, 0), 'P', Items // Turn Data Stream Circuit
         * .getItem("advancedCircuit"), 'M', UpdaterType.MK4.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK4.item());
         * 
         * // He180k Cooling Agent addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.waterMill, 1, 10), "CUC", "SAS", "PMP", 'C',
         * GregTech_API.getGregTechItem(36, 1, 0), 'A', new ItemStack(
         * GlobalBlocks.waterMill, 1, 9), 'S', GregTech_API.getGregTechItem(36,
         * 1, 0), 'P', Items // Turn Data Stream Circuit
         * .getItem("advancedCircuit"), 'M', UpdaterType.MK5.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK5.item());
         * 
         * // Nak180K Cooling Agent addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.waterMill, 1, 11), "CUC", "SAS", "PMP", 'C',
         * GregTech_API.getGregTechItem(60, 1, 0), 'A', new ItemStack(
         * GlobalBlocks.waterMill, 1, 10), 'S', GregTech_API.getGregTechItem(62,
         * 1, 0), 'P', IndustrialCraftIntegration.getIC2Item("advancedCircuit"),
         * 'M', UpdaterType.MK5.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK5.item());
         * 
         * // Energy addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.waterMill, 1, 12), "CUC", "SAS", "PMP", 'C',
         * GregTech_API.getGregTechItem(61, 1, 0), 'A', new ItemStack(
         * GlobalBlocks.waterMill, 1, 11), 'S',
         * GregTech_API.getGregTechComponent(1, 0), 'P',
         * IndustrialCraftIntegration.getIC2Item("advancedCircuit"), 'M',
         * UpdaterType.MK6.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK6.item());
         * 
         * // SuperConductor addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.waterMill, 1, 13), "CUC", "SAS", "PMP", 'C',
         * GregTech_API.getGregTechItem(62, 1, 0), 'A', new ItemStack(
         * GlobalBlocks.waterMill, 1, 12), 'S',
         * GregTech_API.getGregTechComponent(1, 2), 'P',
         * IndustrialCraftIntegration.getIC2Item("advancedCircuit"), 'M',
         * UpdaterType.MK6.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK6.item());
         * 
         * // Ir Neutron plate addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.waterMill, 1, 14), "CUC", "SAS", "PMP", 'C',
         * IndustrialCraftIntegration.getIC2Item("FluidCell"), 'A', new
         * ItemStack(GlobalBlocks.waterMill, 1, 13), 'S',
         * GregTech_API.getGregTechItem(40, 1, 0), 'P',
         * IndustrialCraftIntegration.getIC2Item("advancedCircuit"), 'M',
         * UpdaterType.MK7.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK7.item());
         * 
         * // Ir addRecipeByOreDictionary(new ItemStack(GlobalBlocks.waterMill,
         * 1, 15), "CUC", "SAS", "PMP", 'C',
         * IndustrialCraftIntegration.getIC2Item("FluidCell"), 'A', new
         * ItemStack(GlobalBlocks.waterMill, 1, 14), 'S',
         * GregTech_API.getGregTechBlock(1, 1, 12), 'P',
         * IndustrialCraftIntegration.getIC2Item("advancedCircuit"), 'M',
         * UpdaterType.MK7.item(), 'U',
         * UpdaterType.WaterUraniumPlateMK7.item());
         */

    }

    public void registerRange() {
        if (gregtechRecipe) {
            addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 0), "WSW", "SAS", "WSW", 'W', "circuitBasic", 'S',
                    getUsualItemStack(ICItemFinder.getIC2Item("energyCrystal")), 'A', machine);
            addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 1), "WSW", "SAS", "WSW", 'W', "circuitAdvanced", 'S',
                    getUsualItemStack(ICItemFinder.getIC2Item("lapotronCrystal")), 'A', new ItemStack(GlobalItems.range, 1, 0));
            addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 2), "WSW", "SAS", "WSW", 'W',
                    getUsualItemStack(ICItemFinder.getIC2Item("energyCrystal")), 'S', ICItemFinder.getIC2Item("mfeUnit"), 'A', new ItemStack(GlobalItems.range,
                            1, 1));
            addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 3), "WSW", "SAS", "WSW", 'W',
                    getUsualItemStack(ICItemFinder.getIC2Item("lapotronCrystal")), 'S', ICItemFinder.getIC2Item("mfsUnit"), 'A', new ItemStack(
                            GlobalItems.range, 1, 2));
        } else {
            addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 0), "WSW", "SAS", "WSW", 'W', "circuitBasic", 'S', ICItemFinder.getIC2Item("batBox"),
                    'A', machine);
            addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 1), "WSW", "SAS", "WSW", 'W', "circuitAdvanced", 'S',
                    ICItemFinder.getIC2Item("cesuUnit"), 'A', new ItemStack(GlobalItems.range, 1, 0));
            addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 2), "WSW", "SAS", "WSW", 'W',
                    getUsualItemStack(ICItemFinder.getIC2Item("energyCrystal")), 'S', ICItemFinder.getIC2Item("mfeUnit"), 'A', new ItemStack(GlobalItems.range,
                            1, 1));
            addRecipeByOreDictionary(new ItemStack(GlobalItems.range, 1, 3), "WSW", "SAS", "WSW", 'W',
                    getUsualItemStack(ICItemFinder.getIC2Item("lapotronCrystal")), 'S', ICItemFinder.getIC2Item("mfsUnit"), 'A', new ItemStack(
                            GlobalItems.range, 1, 2));
        }
    }

    public void registerUpdater() {
        ItemStack is = ItemType.WaterUraniumIngot.item().copy();
        is.stackSize = 36;
        ItemStack is2 = ItemType.WaterUraniumIngot.item().copy();
        is2.stackSize = 4;
        /*
         * if(buildcraftRecipe) { ItemStack uraniumBlock =
         * IndustrialCraftIntegration.getIC2Item("uraniumBlock").copy();
         * ItemStack reactorCollantSix =
         * IndustrialCraftIntegration.getIC2Item("reactorCoolantSix").copy();
         * uraniumBlock.stackSize = 4; reactorCollantSix.stackSize = 4;
         * AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe( new
         * ItemStack[] {uraniumBlock, reactorCollantSix }, 1000, is));
         * 
         * if (GregTech_API.isGregTechLoaded()) {
         * AssemblyRecipe.assemblyRecipes.add(new AssemblyRecipe( new
         * ItemStack[] {GregTech_API.getGregTechMaterial(43, 1),
         * IndustrialCraftIntegration.getIC2Item("electrolyzedWaterCell") },
         * 250, is)); } } else {
         */
        if (Mods.IndustrialCraft2.isAvailable)
            addRecipeByOreDictionary(is, "SAS", "ASA", "SAS", 'A', "blockUranium", 'S', ICItemFinder.getIC2Item("reactorCoolantSix"));
        else
            addRecipeByOreDictionary(ItemType.WaterUraniumIngot.item(), "SSS", "SAS", "SSS", 'A', "ingotManganese", 'S', "gemLapis");

        if (doesOreNameExist("ingotUranium") && Mods.IndustrialCraft2.isAvailable) {
            if (Mods.Railcraft.isAvailable)
                for (ItemStack sasasasa : OreDictionary.getOres("ingotUranium"))
                    RailcraftModule.rollingMachine(is2, "SAS", "ASA", "SAS", 'A', sasasasa, 'S', ICItemFinder.getIC2Item("electrolyzedWaterCell"));
            else
                addRecipeByOreDictionary(is2, "SAS", "ASA", "SAS", 'A', "ingotUranium", 'S', ICItemFinder.getIC2Item("electrolyzedWaterCell"));
        }
        addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK1.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumIngot.item(), 'S', "plateBronze");
        addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK1.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumIngot.item(), 'S', "plateManganese");
        addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK2.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK1.item(), 'S', carbonPlate);

        addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK3.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK2.item(), 'S', advancedAlloy);

        if (gregtechRecipe) {
            addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK4.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK3.item(), 'S', "plateTungsten");
            addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK5.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK4.item(), 'S',
                    "plateStainlessSteel");
            addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK6.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK5.item(), 'S',
                    "plateTungstenSteel");
        } else {
            addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK4.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK3.item(), 'S',
                    "plateIndustrialSteel");
            addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK5.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK4.item(), 'S',
                    "plateManganeseSteel");
            addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK6.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK5.item(), 'S',
                    "plateVanadiumSteel");
        }
        if (Mods.IndustrialCraft2.isAvailable)
            addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK7.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK6.item(), 'S',
                    ICItemFinder.getIC2Item("iridiumPlate"));
        else
            addRecipeByOreDictionary(ItemType.WaterUraniumPlateMK7.item(), "SSS", "SAS", "SSS", 'A', ItemType.WaterUraniumPlateMK6.item(), 'S',
                    "plateNeodymiumMagnet");

        Object iron = null;
        if (Mods.IndustrialCraft2.isAvailable)
            iron = "plateIron";
        if (gregtechRecipe) {
            iron = "plateStainlessSteel";
        }
        if (iron == null)
            iron = "plateVanadium";

        addRecipeByOreDictionary(ItemType.MK0.item(), "SAS", "AGA", "SAS", 'S', advancedAlloy, 'A', ItemType.WaterUraniumPlateMK1.item(), 'G',
                transformerUpgrade);
        addRecipeByOreDictionary(ItemType.MK1.item(), "SMS", "UGU", "SAS", 'S', Items.redstone, 'U', ItemType.WaterUraniumPlateMK1.item(), 'A', "gemDiamond",
                'M', "plateSteel", 'G', ItemType.MK0.item());
        addRecipeByOreDictionary(ItemType.MK2.item(), "SMS", "UGU", "SAS", 'S', advancedAlloy, 'U', ItemType.WaterUraniumPlateMK2.item(), 'A', carbonPlate,
                'M', Blocks.lapis_block, 'G', ItemType.MK1.item());
        addRecipeByOreDictionary(ItemType.MK3.item(), "SMS", "UGU", "SAS", 'S', advancedAlloy, 'U', ItemType.WaterUraniumPlateMK3.item(), 'A',
                industrialDiamond, 'M', "gemDiamond", 'G', ItemType.MK2.item());
        addRecipeByOreDictionary(ItemType.IR_FE.item(), "SSS", "SGS", "SSS", 'S', iron, 'G', iridiumPlate);
        if (Mods.IndustrialCraft2.isAvailable) {
            addRecipeByOreDictionary(ItemType.MK4.item(), "SMS", "UGU", "SAS", 'S', ICItemFinder.getIC2Item("advancedAlloy"), 'U',
                    ItemType.WaterUraniumPlateMK4.item(), 'A', getUsualItemStack(ICItemFinder.getIC2Item("advBattery")), 'M', Blocks.emerald_block, 'G',
                    ItemType.MK3.item());
            addRecipeByOreDictionary(ItemType.MK5.item(), "SMS", "UGU", "SAS", 'S', ICItemFinder.getIC2Item("advancedAlloy"), 'U',
                    ItemType.WaterUraniumPlateMK5.item(), 'A', getUsualItemStack(ICItemFinder.getIC2Item("energyCrystal")), 'M',
                    doesOreNameExist("blockRuby") ? "blockRuby" : "blockManganeseSteel", 'G', ItemType.MK4.item());
            addRecipeByOreDictionary(ItemType.MK6.item(), "SMS", "UGU", "SAS", 'S', advancedAlloy, 'U', ItemType.WaterUraniumPlateMK6.item(), 'A',
                    getUsualItemStack(ICItemFinder.getIC2Item("lapotronCrystal")), 'M', doesOreNameExist("blockSapphire") ? "blockSapphire" : "blockVanadium",
                    'G', ItemType.MK5.item());
            addRecipeByOreDictionary(ItemType.MK7.item(), "SMS", "UGU", "SAS", 'S', ICItemFinder.getIC2Item("advancedAlloy"), 'U',
                    ItemType.WaterUraniumPlateMK7.item(), 'A', getUsualItemStack(ICItemFinder.getIC2Item("suBattery")), 'M',
                    doesOreNameExist("blockChrome") ? "blockChrome" : "blockVanadiumSteel", 'G', ItemType.MK6.item());
        } else {
            addRecipeByOreDictionary(ItemType.MK4.item(), "SMS", "UGU", "SAS", 'S', "plateSteel", 'U', ItemType.WaterUraniumPlateMK4.item(), 'A',
                    "stickManganese", 'M', "blockEmerald", 'G', ItemType.MK3.item());
            addRecipeByOreDictionary(ItemType.MK5.item(), "SMS", "UGU", "SAS", 'S', "plateSteel", 'U', ItemType.WaterUraniumPlateMK5.item(), 'A',
                    "stickVanadium", 'M', doesOreNameExist("blockRuby") ? "blockRuby" : "blockManganeseSteel", 'G', ItemType.MK4.item());
            addRecipeByOreDictionary(ItemType.MK6.item(), "SMS", "UGU", "SAS", 'S', "plateSteel", 'U', ItemType.WaterUraniumPlateMK6.item(), 'A',
                    "stickNeodymium", 'M', doesOreNameExist("blockSapphire") ? "blockSapphire" : "blockVanadium", 'G', ItemType.MK5.item());
            addRecipeByOreDictionary(ItemType.MK7.item(), "SMS", "UGU", "SAS", 'S', "plateSteel", 'U', ItemType.WaterUraniumPlateMK7.item(), 'A',
                    "NeodymiumMagnet", 'M', doesOreNameExist("blockChrome") ? "blockChrome" : "blockVanadiumSteel", 'G', ItemType.MK6.item());
        }

        addRecipeByOreDictionary(ItemType.ReservoirCore.item(), "ASA", "SMS", "CSC", 'A', "circuitBasic", 'S', advancedAlloy, 'M', machine, 'C',
                ItemType.WaterUraniumPlateMK1.item());
        if (Mods.IndustrialCraft2.isAvailable) {
            addRecipeByOreDictionary(ItemType.ReservoirCoreAdvanced.item(), "IDI", "AMA", "IDI", 'I', iridiumPlate, 'A',
                    ICItemFinder.getIC2Item("advancedCircuit"), 'M', ItemType.ReservoirCore.item(), 'D', "gemDiamond");
        }
        addRecipeByOreDictionary(ItemType.ReservoirCore.item(), "RPR", "PCP", "RPR", 'R', "stickZinc", 'P', "plateZinc", 'C', "gearZinc");
        addRecipeByOreDictionary(ItemType.ReservoirCoreAdvanced.item(), "RPR", "PCP", "RPR", 'R', "stickVanadiumSteel", 'P', "plateVanadiumSteel", 'C',
                "gearVanadiumSteel");

        addRecipeByOreDictionary(ItemType.WaterUraniumAlloyPlate.item(), "UIU", "IDI", "UIU", 'U', ItemType.WaterUraniumIngot.item(), 'I', iridiumPlate, 'D',
                "gemDiamond");

        addRecipeByOreDictionary(ItemType.PlasmaUraniumAlloyPlate.item(), "OPO", "PUP", "OPO", 'O', doesOreNameExist("plateOsmium") ? "plateOsmium"
                : "plateVanadium", 'P', ItemType.PlasmaUraniumIngot.item(), 'U', ItemType.WaterUraniumAlloyPlate.item());

        addRecipeByOreDictionary(ItemType.DiamondBlade.item(), "ITI", "TDT", "ITI", 'I', "plateIron", 'D', "gemDiamond", 'T', "dustDiamond");
        addRecipeByOreDictionary(ItemType.DiamondGlazingWheel.item(), "SSS", "SAS", "SSS", 'S', Items.flint, 'A', ItemType.DiamondBlade.item());
        addRecipeByOreDictionary(ItemType.IndustrialSteelHydraulicCylinder.item(), "PCP", "CBC", " I ", 'P', "plateIndustrialSteel", 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4), 'B', "blockIndustrialSteel", 'I', "ingotIndustrialSteel");
        addRecipeByOreDictionary(ItemType.BrassCentrifugePot.item(), "P P", "P P", "PDP", 'P', "plateZincAlloy", 'D', "plateDenseZincAlloy");
        addRecipeByOreDictionary(ItemType.BrassCentrifugePot.item(), "P P", "P P", "PDP", 'P', "plateZincAlloy", 'D', "plateDenseZincAlloy");
        addRecipeByOreDictionary(ItemType.VSteelPistonCylinder.item(), "PCP", "DBD", "PPP", 'P', "plateVanadiumSteel", 'C', GlobalBlocks.compressor, 'D',
                "plateDenseVanadiumSteel", 'B', "blockVanadiumSteel");
        addRecipeByOreDictionary(ItemType.VSteelWaterPipe.item(), "DDD", "D D", "DDD", 'D', "plateVanadium");
        addRecipeByOreDictionary(ItemType.RubyWaterHole.item(), "IBI", "B B", "IBI", 'B', "blockRuby", 'I', "plateVanadium");
        addRecipeByOreDictionary(ItemType.RubyWaterHole.item(), "IBI", "B B", "IBI", 'B', "blockVanadium", 'I', "plateVanadium");
        addRecipeByOreDictionary(ItemType.DataBall.item(), " D ", "CIC", " D ", 'D', Items.emerald, 'C',
                ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK1), 'I', "circuitBasic");
        OreDictionary.registerOre("dustCactus", ItemType.DustCactus.item());
        RecipeAdder.macerator(new ItemStack(Blocks.cactus, 1, 16), ItemType.DustCactus.item());

        OreDictionary.registerOre("dustIron", ItemType.DustIron.item());
        OreDictionary.registerOre("dustGold", ItemType.DustGold.item());
        OreDictionary.registerOre("dustDiamond", ItemType.DustDiamond.item());
        // If IC2 isn't loaded, I'll add crafting recipes for diamond dust.
        if (!Mods.IndustrialCraft2.isAvailable) {
            RecipeAdder.macerator(new ItemStack(Items.diamond), ItemType.DustDiamond.item());
            RecipeAdder.macerator(new ItemStack(Blocks.iron_ore), ItemType.DustIron.item(2));
            RecipeAdder.macerator(new ItemStack(Blocks.gold_ore), ItemType.DustGold.item(2));
            RecipeAdder.macerator(new ItemStack(Items.iron_ingot), ItemType.DustIron.item());
            RecipeAdder.macerator(new ItemStack(Items.gold_ingot), ItemType.DustGold.item());
            FurnaceRecipes.smelting().func_151394_a(ItemType.DustIron.item(), new ItemStack(Items.iron_ingot), 0f);
            FurnaceRecipes.smelting().func_151394_a(ItemType.DustGold.item(), new ItemStack(Items.gold_ingot), 0f);
        }

        OreDictionary.registerOre("plateDenseRedstone", ItemType.DenseRedstonePlate.item());
        RecipeAdder.compressor(new ItemStack(Blocks.redstone_block), ItemType.DenseRedstonePlate.item());
    }

    @Override
    public void registerTurbine() {
        if (Mods.IndustrialCraft2.isAvailable)
            addRecipeByOreDictionary(ItemType.BaseRotor.item(), "ILI", "LCL", "ILI", 'I', "plateDenseIron", 'L', "plateDenseLead", 'C', carbonPlate);
        else
            addRecipeByOreDictionary(ItemType.BaseRotor.item(), "ILI", "LCL", "ILI", 'I', "plateDenseSteel", 'L', "plateDenseZinc", 'C', carbonPlate);

        // Turbine recipe registering
        /*
         * if (gregtechRecipe) { addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.turbine, 1, 0), "SAU", "CGA", "SAU", 'S',
         * Blocks.iron_bars, 'A', new ItemStack(GlobalBlocks.waterMill, 1, 4),
         * 'G', UpdaterType.ReservoirCore.item(), 'U', UpdaterType.MK1.item(),
         * 'C', GregTech_API.getGregTechComponent(4, 1));
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 1),
         * "IUI", "TCT", "IUI", 'T', new ItemStack( GlobalBlocks.turbine, 1, 0),
         * 'I', "plateInvar", 'U',
         * IndustrialCraftIntegration.getIC2Item("advancedMachine"), 'C',
         * IndustrialCraftIntegration.getIC2Item("advancedCircuit"));
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 2),
         * "IUI", "TCT", "IRI", 'T', new ItemStack( GlobalBlocks.turbine, 1, 1),
         * 'I', "plateAluminium", 'U', GregTech_API.getGregTechComponent(9, 1),
         * 'C', IndustrialCraftIntegration.getIC2Item("advancedMachine"), 'R',
         * GregTech_API.getGregTechComponent(27, 1));
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 3),
         * "SSS", "TRT", "WWW", 'T', new ItemStack( GlobalBlocks.turbine, 1, 2),
         * 'S', "gearStainlessSteel", 'W', GregTech_API.getGregTechComponent(9,
         * 1), 'R', GregTech_API.getGregTechComponent(27, 1));
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 4),
         * "IUI", "TCT", "IUI", 'T', new ItemStack( GlobalBlocks.turbine, 1, 3),
         * 'I', "plateTitanium", 'U',
         * IndustrialCraftIntegration.getIC2Item("teleporter"), 'C',
         * GregTech_API.getGregTechComponent(27, 1));
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 5),
         * "IUI", "TCT", "IUI", 'T', new ItemStack( GlobalBlocks.turbine, 1, 4),
         * 'I', "plateChrome", 'U', "gearIridium", 'C',
         * GregTech_API.getGregTechBlock(1, 1, 109));
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 6),
         * "IUI", "TCT", "IRI", 'T', new ItemStack( GlobalBlocks.turbine, 1, 5),
         * 'I', "plateTungstenSteel", 'U', GregTech_API.getGregTechBlock(1, 1,
         * 109), 'C', UpdaterType.WaterUraniumIngot.item(), 'R',
         * GregTech_API.getGregTechComponent(0, 1));
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 7),
         * "IUI", "TCT", "IUI", 'T', new ItemStack( GlobalBlocks.turbine, 1, 6),
         * 'I', "plateIridium", 'U', GregTech_API.getGregTechComponent(9, 1),
         * 'C', "gearIridium"); addRecipeByOreDictionary(new
         * ItemStack(GlobalBlocks.turbine, 1, 8), "IUI", "TCT", "IRI", 'T', new
         * ItemStack( GlobalBlocks.turbine, 1, 7), 'I',
         * IndustrialCraftIntegration.getIC2Item("iridiumPlate"), 'U',
         * "gearIridium", 'C', GregTech_API.getGregTechBlock(1, 1, 4), 'R',
         * GregTech_API.getGregTechComponent(2, 1));
         * addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 9),
         * "IUI", "TCT", "IUI", 'T', new ItemStack( GlobalBlocks.turbine, 1, 8),
         * 'I', "plateOsmium", 'U', "gearIridium", 'C',
         * GregTech_API.getGregTechBlock(1, 1, 47)); addRecipeByOreDictionary(
         * new ItemStack(GlobalBlocks.turbine, 1, 10), "WCW", "TUT", "IGI", 'T',
         * new ItemStack(GlobalBlocks.turbine, 1, 9), 'W',
         * UpdaterType.WaterUraniumAlloyPlate.item(), 'I',
         * GregTech_API.getGregTechComponent(0, 1), 'U',
         * GregTech_API.getGregTechBlock(0, 1, 10), 'G',
         * GregTech_API.getGregTechBlock(1, 1, 90), 'C', "gearOsmium");
         * addRecipeByOreDictionary( new ItemStack(GlobalBlocks.turbine, 1, 11),
         * "IUI", "TCT", "IUI", 'T', new ItemStack(GlobalBlocks.turbine, 1, 10),
         * 'I', UpdaterType.PlasmaUraniumAlloyPlate.item(), 'U',
         * GregTech_API.getGregTechBlock(1, 1, 90), 'C', Items.nether_star); }
         * else {
         */
        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 0), "SAU", "CGA", "SAU", 'S', Blocks.iron_bars, 'A', new ItemStack(
                GlobalBlocks.waterMill, 1, 4), 'G', ItemType.ReservoirCore.item(), 'U', ItemType.MK1.item(), 'C', Blocks.glass_pane);
        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 1), "IUI", "TCT", "IRI", 'T', new ItemStack(GlobalBlocks.turbine, 1, 0), 'I',
                Mods.IndustrialCraft2.isAvailable ? "plateDenseIron" : "plateDenseSteel", 'U', transformerUpgrade, 'C', "circuitAdvanced", 'R',
                ItemType.BaseRotor.item());
        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 2), "IUI", "TCT", "IRI", 'T', new ItemStack(GlobalBlocks.turbine, 1, 1), 'I',
                carbonPlate, 'U', "circuitAdvanced", 'C', ICItemFinder.getIC2Item("advancedMachine"), 'R', ItemType.BaseRotor.item());
        addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1, 3), "IRI", "TCT", "IRI", 'T', new ItemStack(GlobalBlocks.turbine, 1, 2), 'I',
                ICItemFinder.getIC2Item("reactorPlating"), 'C', ICItemFinder.getIC2Item("teleporter"), 'R', ItemType.BaseRotor.item());
        // }

    }

    @Override
    public void registerMachines() {
        addRecipeByOreDictionary(GlobalBlocks.sawmill, " H ", "CPC", "KKK", 'H', ItemType.WoodenHammer.item(), 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1), 'K', Blocks.stonebrick, 'P', ItemType.DiamondBlade.item());
        addRecipeByOreDictionary(GlobalBlocks.sawmill, " H ", "CPC", "KKK", 'H', ItemType.WoodenHammer.item(), 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1), 'K', Blocks.stonebrick, 'P', Items.flint);
        addRecipeByOreDictionary(GlobalBlocks.macerator, " H ", "CPC", "KRK", 'H', ItemType.WoodenHammer.item(), 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1), 'K', Blocks.stonebrick, 'P', ItemType.DiamondGlazingWheel.item(), 'R', Blocks.hopper);
        addRecipeByOreDictionary(GlobalBlocks.macerator, " H ", "CPC", "KRK", 'H', ItemType.WoodenHammer.item(), 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1), 'K', Blocks.stonebrick, 'P', Items.flint, 'R', Blocks.hopper);
        addRecipeByOreDictionary(GlobalBlocks.compressor, "CDC", "DGD", "ICA", 'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1), 'D',
                "plateDenseIndustrialSteel", 'G', ItemType.IndustrialSteelHydraulicCylinder.item(), 'I',
                ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK1), 'A', ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK1));
        addRecipeByOreDictionary(GlobalBlocks.centrifuge, "CBC", "BRB", "XCX", 'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4), 'B',
                ItemType.BrassCentrifugePot.item(), 'R', ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK4), 'X', ItemType.DataBall.item());
        addRecipeByOreDictionary(GlobalBlocks.cutter, "CPC", "SMS", "SSS", 'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK1), 'P',
                ItemType.VSteelWaterPipe.item(), 'M', ItemType.RubyWaterHole.item(), 'S', "plateManganese");
        addRecipeByOreDictionary(GlobalBlocks.cutter, "A", 'A', GlobalBlocks.lathe);
        addRecipeByOreDictionary(GlobalBlocks.lathe, "A", 'A', GlobalBlocks.cutter);
        addRecipeByOreDictionary(GlobalBlocks.advancedCompressor, "CDC", "CPC", "CIC", 'C', ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK5), 'D',
                "plateDenseVanadiumSteel", 'P', ItemType.VSteelPistonCylinder.item(), 'I', ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK4));
    }

    @Override
    public void registerPlugins() {
        addShapelessRecipeByOreDictionary(PluginType.StorageMK4.item(), PluginType.StorageMK3.item(), PluginType.StorageMK3.item(),
                PluginType.StorageMK3.item(), PluginType.StorageMK3.item());
        addShapelessRecipeByOreDictionary(PluginType.StorageMK3.item(), PluginType.StorageMK2.item(), PluginType.StorageMK2.item(),
                PluginType.StorageMK2.item(), PluginType.StorageMK2.item());
        addShapelessRecipeByOreDictionary(PluginType.StorageMK2.item(), PluginType.StorageMK1.item(), PluginType.StorageMK1.item(),
                PluginType.StorageMK1.item(), PluginType.StorageMK1.item());
        addShapelessRecipeByOreDictionary(PluginType.AllRoundMK1.item(), PluginType.StorageMK1.item(), PluginType.RainMK1.item(), PluginType.OverMK1.item(),
                PluginType.UnderMK1.item());
        addShapelessRecipeByOreDictionary(PluginType.StorageMK1.item(), new ItemStack(GlobalBlocks.reservoir, 1, 8), Items.iron_ingot, Items.iron_ingot,
                Items.iron_ingot);
        addShapelessRecipeByOreDictionary(PluginType.StorageMK2.item(), new ItemStack(GlobalBlocks.reservoir, 1, 17), PluginType.StorageMK1.item(),
                Items.iron_ingot, Items.iron_ingot, Items.iron_ingot);
        addShapelessRecipeByOreDictionary(PluginType.SpeedMK1.item(), ItemType.DataBall.item(), Items.iron_ingot, Items.iron_ingot, Items.iron_ingot);
        Object extractor = ItemType.RubyWaterHole.item();
        if (Mods.IndustrialCraft2.isAvailable) {
            extractor = ICItemFinder.getIC2Item("extractor");
            addRecipeByOreDictionary(PluginType.OverMK1.item(), "CBC", "EPE", "CBC", 'C', "ringVanadiumSteel", 'B', ICItemFinder.getIC2Item("pump"), 'E',
                    extractor, 'P', ItemType.VSteelWaterPipe.item());
        }
        if (Mods.BuildCraftFactory.isAvailable) {
            addRecipeByOreDictionary(PluginType.OverMK1.item(), "CBC", "EPE", "CBC", 'C', "ringVanadiumSteel", 'B',
                    GameRegistry.findBlock("BuildCraft|Factory", "pumpBlock"), 'E', ItemType.RubyWaterHole.item(), 'P', ItemType.VSteelWaterPipe.item());
        }
        if (!Mods.IndustrialCraft2.isAvailable && !Mods.BuildCraftFactory.isAvailable)
            addRecipeByOreDictionary(PluginType.OverMK1.item(), "CBC", "EPE", "CBC", 'C', "ringVanadiumSteel", 'B', "gearVanadiumSteel", 'E',
                    ItemType.RubyWaterHole.item(), 'P', ItemType.VSteelWaterPipe.item());
        addShapelessRecipeByOreDictionary(PluginType.UnderMK1.item(), PluginType.OverMK1.item());
        addShapelessRecipeByOreDictionary(PluginType.OverMK1.item(), PluginType.UnderMK1.item());
        addRecipeByOreDictionary(PluginType.RainMK1.item(), "C C", "EPE", "C C", 'C', "ringVanadiumSteel", 'E', extractor, 'P', ItemType.VSteelWaterPipe.item());
    }

}
