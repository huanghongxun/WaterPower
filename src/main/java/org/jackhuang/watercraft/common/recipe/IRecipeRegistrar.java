/**
 * Copyright (c) Huang Yuhui, 2014
 * <p/>
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package org.jackhuang.watercraft.common.recipe;

import static org.jackhuang.watercraft.common.item.crafting.CraftingTypes.*;
import static org.jackhuang.watercraft.common.item.crafting.LevelTypes.*;
import gregtech.api.GregTech_API;
import mods.railcraft.api.crafting.RailcraftCraftingManager;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.common.config.Property;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.block.turbines.TurbineType;
import org.jackhuang.watercraft.common.block.watermills.WaterType;
import org.jackhuang.watercraft.common.item.crafting.CraftingTypes;
import org.jackhuang.watercraft.common.item.crafting.ItemCrafting;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.LevelTypes;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;
import org.jackhuang.watercraft.common.item.others.ItemType;
import org.jackhuang.watercraft.integration.ImmersiveEngineeringModule;
import org.jackhuang.watercraft.integration.MekanismModule;
import org.jackhuang.watercraft.integration.RailcraftModule;
import org.jackhuang.watercraft.integration.ic2.ICItemFinder;
import org.jackhuang.watercraft.util.Mods;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;

public abstract class IRecipeRegistrar {

    protected Object advancedAlloy, 
            carbonPlate, iridiumPlate, machine, industrialDiamond,
            transformerUpgrade;
    
    protected ItemStack copperCable = new ItemStack(GameRegistry.findItem(Mods.IDs.Mekanism, "PartTransmitter"), 1, 2),
            goldCable = new ItemStack(GameRegistry.findItem(Mods.IDs.Mekanism, "PartTransmitter"), 1, 3);

    protected static IRecipeRegistrar instance;

    public static IRecipeRegistrar getInstance() {
        return instance;
    }

    public static boolean gregtechRecipe, thaumcraftRecipe;

    protected Configuration config;

    public IRecipeRegistrar(Configuration c) {
        instance = this;

        Property p = c.get("recipe", "EnableGregTechRecipe", true);
        gregtechRecipe = p.getBoolean(true) && Mods.GregTech.isAvailable;
        p = c.get("recipe", "EnableThaumcraftRecipe", true);
        thaumcraftRecipe = p.getBoolean(true) && Mods.Thaumcraft.isAvailable;

        if (Mods.IndustrialCraft2.isAvailable) {
            advancedAlloy = ICItemFinder.getIC2Item("advancedAlloy");
            carbonPlate = ICItemFinder.getIC2Item("carbonPlate");
            iridiumPlate = ICItemFinder.getIC2Item("iridiumPlate");
            machine = ICItemFinder.getIC2Item("machine");
            transformerUpgrade = ICItemFinder.getIC2Item("transformerUpgrade");

            p = c.get("recipe", "UseIndustrialDiamond", true);
            industrialDiamond = p.getBoolean(true) ? ICItemFinder
                    .getIC2Item("industrialDiamond") : ICItemFinder
                    .getIC2Item("coalChunk");
        } else {
            advancedAlloy = "plateSteel";
            carbonPlate = "gemDiamond";
            iridiumPlate = "plateVanadiumSteel";
            if(Mods.Mekanism.isAvailable) {
                machine = new ItemStack(GameRegistry.findItem(Mods.IDs.Mekanism, "BasicBlock"), 1, 8);
            } else
                machine = "blockIron";
            industrialDiamond = "gemDiamond";
            transformerUpgrade = ItemCrafting.get(circuit, MK1);
        }

        config = c;
    }

    public void registerAllRecipes() {
        Property p = config.get("enable", "EnableUpdaters", true);
        if (p.getBoolean(true)) {
            registerUpdater();
        }

        // -- Normal recipes registering

        if (Mods.IndustrialCraft2.isAvailable)
            addRecipeByOreDictionary(
                    new ItemStack(GlobalBlocks.waterMill, 1, 2), "W", 'W',
                    ICItemFinder.getIC2Item("WaterKineticGenerator"));

        for (int i = 1; i < WaterType.values().length; i++) {
            addRecipeByOreDictionary(
                    new ItemStack(GlobalBlocks.waterMill, 1, i), " W ", "WTW",
                    " W ", 'W',
                    new ItemStack(GlobalBlocks.waterMill, 1, i - 1), 'T',
                    transformerUpgrade);
        }

        for (int i = 0; i < WaterType.values().length; i++) {
            GameRegistry.addShapelessRecipe(new ItemStack(
                    WaterType.values()[i].trousers, 1, 0), new ItemStack(
                    GlobalBlocks.waterMill, 1, i), Items.iron_leggings);
        }

        registerBaseMaterialRecipes();
        registerWatermills();
        registerPlugins();
        if (Reference.General.enableMachines)
            registerMachines();
        registerRange();

        p = config.get("enable", "EnableReservoir", true);
        if (p.getBoolean(true)) {

            for (int i = 1; i < TurbineType.values().length; i++) {
                addRecipeByOreDictionary(new ItemStack(GlobalBlocks.turbine, 1,
                                i), " W ", "WTW", " W ", 'W', new ItemStack(
                                GlobalBlocks.turbine, 1, i - 1), 'T',
                        transformerUpgrade);
            }

            registerTurbine();
        }
    }

    private void addPaddleBaseRecipe(LevelTypes level) {
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.paddleBase, level), "W W",
                "SAS", 'W',
                ItemCrafting.get(CraftingTypes.drainagePlate, level), 'S',
                ItemCrafting.get(CraftingTypes.fixedTool, level), 'A',
                ItemCrafting.get(CraftingTypes.fixedFrame, level));
    }

    private void addCasingRecipe(LevelTypes level, Object stick, Object plate,
                                 Object casing) {
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.casing, level), "WSW", "WAW",
                "WSW", 'W', stick, 'S', plate, 'A', casing);
    }

    void registerBaseMaterialRecipes() {

        this.addRecipeByOreDictionary(ItemCrafting.get(stator, LevelTypes.MK1),
                "M", "M", 'M', "dustMagnet");
        this.addRecipeByOreDictionary(ItemCrafting.get(casing, LevelTypes.MK1),
                "WSW", "WSW", "WSW", 'W', ItemType.StoneStruct.item(), 'S',
                Blocks.stonebrick);
        this.addRecipeByOreDictionary(ItemType.StoneStruct.item(), "WW ",
                "WWS", "WW ", 'W', Blocks.stonebrick, 'S',
                ItemType.WoodenHammer.item());
        addPaddleBaseRecipe(LevelTypes.MK1);
        this.addRecipeByOreDictionary(
                ItemCrafting.get(drainagePlate, LevelTypes.MK1), "WW ", "WWS",
                "WW ", 'W', "plankWood", 'S', ItemType.WoodenHammer.item());
        this.addRecipeByOreDictionary(
                ItemCrafting.get(fixedFrame, LevelTypes.MK1), "WWW", "WSW",
                "WWW", 'W', "logWood", 'S', "plankWood");
        this.addRecipeByOreDictionary(
                ItemCrafting.get(fixedTool, LevelTypes.MK1), "WW", "AW", 'W',
                "logWood", 'A', Items.string);
        this.addRecipeByOreDictionary(ItemType.WoodenHammer.item(6), "WW ",
                "WWI", "WW ", 'W', "logWood", 'I', Items.stick);
        int flag = 0;
        if (Mods.IndustrialCraft2.isAvailable) {
            flag = 1;
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(outputInterface, LevelTypes.MK1), "GW",
                    " G", "GW", 'G', ICItemFinder.getIC2Item("goldCableItem"),
                    'W', "plankWood");
            this.addShapelessRecipeByOreDictionary(
                    ItemCrafting.get(circuit, MK1),
                    ItemType.WaterResistantRubber.item(),
                    "circuitBasic");
        }
        if (Mods.Mekanism.isAvailable) {
            flag = 1;
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(outputInterface, LevelTypes.MK1), "GW",
                    " G", "GW", 'G', goldCable,
                    'W', "plankWood");
            this.addShapelessRecipeByOreDictionary(
                    ItemCrafting.get(circuit, MK1),
                    ItemType.WaterResistantRubber.item(),
                    getItem(Mods.IDs.Mekanism, "ControlCircuit", 0));
        }
        
        if (flag == 0) {
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(outputInterface, MK1), " W", "G ", " W",
                    'G', "ingotGold", 'W', "plankWood");
            this.addRecipeByOreDictionary(ItemCrafting.get(circuit, MK1),
                    "NNN", "RPR", "NNN", 'N', "stickNeodymium", 'R',
                    "dustRedstone", 'P', "plateZinc");
        }
        this.addShapelessRecipeByOreDictionary(
                ItemType.WaterResistantRubber.item(), "itemRubber",
                "itemRubber", "itemRubber", "itemRubber");
        this.addShapelessRecipeByOreDictionary(
                ItemType.WaterResistantRubber.item(), Items.slime_ball,
                Items.slime_ball, Items.slime_ball, Items.slime_ball);
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotationAxle, MK1), "SBS",
                "SHS", "SBS", 'S', Items.stick, 'B', "plankWood", 'H',
                ItemType.WoodenHammer.item());
        if (gregtechRecipe) {
            GregTech_API.sRecipeAdder.addBenderRecipe(
                    ItemType.WaterResistantRubber.item(),
                    ItemType.WaterResistantRubberPlate.item(), 2 * 20, 2);
        } else {
            RecipeAdder.compressor(ItemType.WaterResistantRubber.item(),
                    ItemType.WaterResistantRubberPlate.item());
        }
        OreDictionary.registerOre("plateRubber",
                ItemType.WaterResistantRubberPlate.item());
        OreDictionary.registerOre("plateDenseRubber",
                ItemType.WaterResistantRubberDensePlate.item());
        RecipeAdder.compressor(ItemType.WaterResistantRubberPlate.item(9),
                ItemType.WaterResistantRubberDensePlate.item());
        flag = 0;
        if (Mods.IndustrialCraft2.isAvailable) {
            flag = 1;
            this.addRecipeByOreDictionary(ItemCrafting.get(rotor, MK1), "CCC",
                    "CAC", "CCC", 'C',
                    ICItemFinder.getIC2Item("copperCableItem"), 'A',
                    "dustMagnet");
        }
        if (Mods.Mekanism.isAvailable) {
            flag = 1;
            this.addRecipeByOreDictionary(ItemCrafting.get(rotor, MK1), "CCC",
                    "CAC", "CCC", 'C',
                    copperCable, 'A',
                    "dustMagnet");
        }
        if (flag == 0) {
            this.addRecipeByOreDictionary(ItemCrafting.get(rotor, MK1), " C ",
                    "CAC", " C ", 'C', "ingotIron", 'A', "dustMagnet");
        }

        // MK3

        addCasingRecipe(MK3, "stickZincAlloy", "plateZincAlloy", "blockZinc");
        addPaddleBaseRecipe(MK3);
        if (Mods.IndustrialCraft2.isAvailable) {
            this.addRecipeByOreDictionary(ItemCrafting.get(drainagePlate, MK3),
                    "WW", "AW", "KW", 'W', "plateZincAlloy", 'A',
                    ICItemFinder.getIC2Item("ironScaffold"), 'K', "screwZinc");
        } else {
            this.addRecipeByOreDictionary(ItemCrafting.get(drainagePlate, MK3),
                    "WW", "AW", "KW", 'W', "plateZincAlloy", 'A',
                    "stickZincAlloy", 'K', "screwZinc");
        }
        if (gregtechRecipe) {
            GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemMaterial.get(
                            MaterialTypes.Zinc, MaterialForms.plate, 4), ItemMaterial
                            .get(MaterialTypes.Zinc, MaterialForms.screw, 4),
                    ItemCrafting.get(CraftingTypes.fixedFrame, LevelTypes.MK3),
                    4000, 5);
            GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemMaterial.get(
                            MaterialTypes.Zinc, MaterialForms.ingot, 2), ItemMaterial
                            .get(MaterialTypes.Zinc, MaterialForms.screw, 4),
                    ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK3),
                    4000, 5);
        } else {
            addRecipeByOreDictionary(
                    ItemCrafting.get(fixedFrame, LevelTypes.MK3), "PSP", "S S",
                    "PSP", 'P',
                    ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.plate),
                    'S',
                    ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.screw));
            addRecipeByOreDictionary(
                    ItemCrafting.get(fixedTool, LevelTypes.MK3), "PSP", "   ",
                    "PSP", 'P',
                    ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.ingot),
                    'P',
                    ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.screw));
        }
        this.addRecipeByOreDictionary(
                ItemCrafting.get(rotationAxle, LevelTypes.MK3), "GPG", "ICI",
                "GPG", 'G', "gearZincAlloy", 'P', "plateZincAlloy", 'I',
                "plateIron", 'C', ItemCrafting.get(casing, LevelTypes.MK3));
        this.addRecipeByOreDictionary(
                ItemCrafting.get(rotationAxle, LevelTypes.MK3), "GPG", "ICI",
                "GPG", 'G', "gearZincAlloy", 'P', "plateZincAlloy", 'I',
                "plateSteel", 'C', ItemCrafting.get(casing, LevelTypes.MK3));
        this.addRecipeByOreDictionary(
                ItemCrafting.get(circuit, LevelTypes.MK3), "PPP", "CDC", "PPP",
                'P', ItemType.WaterResistantRubberDensePlate.item(), 'C',
                ItemCrafting.get(circuit, LevelTypes.MK1), 'P',
                ItemType.WaterResistantRubberPlate.item(), 'D',
                ItemCrafting.get(casing, LevelTypes.MK3));
        this.addRecipeByOreDictionary(ItemCrafting.get(stator, LevelTypes.MK3),
                "PIS", "PI ", "PIS", 'P', "plateZincAlloy", 'I', "dustMagnet",
                'S', "stickZincAlloy");
        flag = 0;

        if (Mods.IndustrialCraft2.isAvailable) {
            flag = 1;
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(rotor, LevelTypes.MK3), "CIC", "GIG",
                    "G G", 'G', ICItemFinder.getIC2Item("goldCableItem"), 'C',
                    ItemType.DenseCoil.item(), 'I', "ingotIron");
        }
        
        if (Mods.Mekanism.isAvailable) {
            flag = 1;
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(rotor, LevelTypes.MK3), "CIC", "GIG",
                    "G G", 'G', goldCable, 'C',
                    ItemType.DenseCoil.item(), 'I', "ingotIron");
        }
        
        if(flag == 0) {
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(rotor, LevelTypes.MK3), "CIC", " I ",
                    " G ", 'G', "ingotGold", 'C', ItemType.DenseCoil.item(),
                    'I', "ingotIron");
        }
        
        if (Mods.IndustrialCraft2.isAvailable) {
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(outputInterface, LevelTypes.MK3), "PRB",
                    "RAI", "PRB", 'P', "plateZincAlloy", 'R', "plateRubber",
                    'I', "ingotZincAlloy", 'A',
                    ICItemFinder.getIC2Item("lvTransformer"), 'B',
                    ICItemFinder.getIC2Item("reBattery"));
        } else {
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(outputInterface, LevelTypes.MK3), "PRB",
                    "RAI", "PRB", 'P', "plateZincAlloy", 'R', "plateRubber",
                    'I', "ingotZincAlloy", 'A', ItemType.DenseCoil.item(), 'B',
                    "dustRedstone");
        }

        // MK4

        addCasingRecipe(LevelTypes.MK4, "stickIndustrialSteel",
                "plateIndustrialSteel", "blockIndustrialSteel");
        addPaddleBaseRecipe(LevelTypes.MK4);
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.drainagePlate, LevelTypes.MK4),
                "WW", "KW", "KW", 'W', "plateIndustrialSteel", 'K',
                "blockIndustrialSteel");
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.fixedFrame, LevelTypes.MK4),
                "P  ", "DP ", "SDP", 'P', "plateIndustrialSteel", 'D',
                "plateDenseIndustrialSteel", 'S', "screwIndustrialSteel");
        if (gregtechRecipe) {
            GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemMaterial.get(
                            MaterialTypes.IndustrialSteel, MaterialForms.plateDense),
                    ItemMaterial.get(MaterialTypes.IndustrialSteel,
                            MaterialForms.ingot), ItemCrafting.get(
                            CraftingTypes.fixedTool, LevelTypes.MK4), 6000, 5);
        } else {
            this.addShapelessRecipeByOreDictionary(
                    ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK4),
                    "plateDenseIndustrialSteel", "ingotIndustrialSteel");
        }
        if (Mods.IndustrialCraft2.isAvailable) {
            this.addRecipeByOreDictionary(ItemCrafting.get(
                            CraftingTypes.outputInterface, LevelTypes.MK4), "SSP",
                    "PTB", "SSP", 'S', "blockSilver", 'P',
                    "plateIndustrialSteel", 'T', ICItemFinder
                            .getIC2Item("mvTransformer"), 'B', ICItemFinder
                            .getIC2Item("suBattery"));
            this.addRecipeByOreDictionary(ItemCrafting.get(
                            CraftingTypes.outputInterface, LevelTypes.MK4), "SSP",
                    "PTB", "SSP", 'S', "blockNeodymium", 'P',
                    "plateIndustrialSteel", 'T', ICItemFinder
                            .getIC2Item("mvTransformer"), 'B', ICItemFinder
                            .getIC2Item("suBattery"));
        } else {
            this.addRecipeByOreDictionary(ItemCrafting.get(
                            CraftingTypes.outputInterface, LevelTypes.MK4), "SSP",
                    "PTB", "SSP", 'S', "blockSilver", 'P',
                    "plateIndustrialSteel", 'T', "blockIron", 'B',
                    "dustRedstone");
            this.addRecipeByOreDictionary(ItemCrafting.get(
                            CraftingTypes.outputInterface, LevelTypes.MK4), "SSP",
                    "PTB", "SSP", 'S', "blockNeodymium", 'P',
                    "plateIndustrialSteel", 'T', "blockIron", 'B',
                    "dustRedstone");
        }
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK4),
                "GPG", "ICI", "GPG", 'G', "gearIndustrialSteel", 'P',
                "plateIndustrialSteel", 'I', "plateIron", 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4));
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK4),
                "GPG", "ICI", "GPG", 'G', "gearIndustrialSteel", 'P',
                "plateIndustrialSteel", 'I', "plateSteel", 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK4));
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK4), "PPP",
                "CDC", "BPB", 'P', ItemType.DenseRedstonePlate.item(), 'C',
                "circuitAdvanced", 'P',
                ItemType.WaterResistantRubberPlate.item(), 'D',
                ItemType.DataBall.item(), 'B', "platePlatinum");
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK4), "PPP",
                "CDC", "BPB", 'P', ItemType.DenseRedstonePlate.item(), 'C',
                "circuitAdvanced", 'P',
                ItemType.WaterResistantRubberPlate.item(), 'D',
                ItemType.DataBall.item(), 'B', "plateVanadiumSteel");
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.stator, LevelTypes.MK4), "PP",
                "PB", "PP", 'P', "plateIndustrialSteel", 'B',
                "blockNeodymiumMagnet");
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotor, LevelTypes.MK4), "SPS",
                "PBP", "SPS", 'S', ItemType.DenseSilverCoil.item(), 'P',
                "plateIndustrialSteel", 'B', Blocks.diamond_block);

        // MK5
        addCasingRecipe(LevelTypes.MK5, "plateManganeseSteel",
                "plateManganeseSteel", "blockManganeseSteel");
        addPaddleBaseRecipe(LevelTypes.MK5);
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.drainagePlate, LevelTypes.MK5),
                "WW", "KW", "KW", 'W', "plateManganeseSteel", 'K',
                "blockManganeseSteel");
        if (Mods.IndustrialCraft2.isAvailable) {
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK5),
                    "PDP", "DCD", "PDP", 'P',
                    ItemType.WaterResistantRubberDensePlate.item(), 'C',
                    getUsualItemStack(ICItemFinder.getIC2Item("energyCrystal")), 'D',
                    ItemType.DataBall.item());
        } else {
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK5),
                    "PDP", "DCD", "PDP", 'P',
                    ItemType.WaterResistantRubberDensePlate.item(), 'C',
                    "gemDiamond", 'D', ItemType.DataBall.item());
        }
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.fixedFrame, LevelTypes.MK5),
                "P  ", "DP ", "SDP", 'P', "plateManganeseSteel", 'D',
                "plateDenseManganeseSteel", 'S', "screwManganeseSteel");
        if (gregtechRecipe) {
            GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemMaterial.get(
                            MaterialTypes.ManganeseSteel, MaterialForms.plateDense),
                    ItemMaterial.get(MaterialTypes.ManganeseSteel,
                            MaterialForms.ingot), ItemCrafting.get(
                            CraftingTypes.fixedTool, LevelTypes.MK5), 6000, 5);
        } else {
            this.addShapelessRecipeByOreDictionary(
                    ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK5),
                    "plateDenseManganeseSteel", "ingotManganeseSteel");
        }
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.stator, LevelTypes.MK5), "PP",
                "PB", "PP", 'P', "plateManganeseSteel", 'B',
                "blockNeodymiumMagnet");

        if (Mods.IndustrialCraft2.isAvailable) {
            this.addRecipeByOreDictionary(ItemCrafting.get(
                            CraftingTypes.outputInterface, LevelTypes.MK5), "SSP",
                    "PTB", "SSP", 'S', "blockNeodymiumMagnet", 'P',
                    "plateManganeseSteel", 'T', ICItemFinder
                            .getIC2Item("hvTransformer"), 'B', ICItemFinder
                            .getIC2Item("suBattery"));
        } else {
            this.addRecipeByOreDictionary(ItemCrafting.get(
                            CraftingTypes.outputInterface, LevelTypes.MK5), "SSP",
                    "PTB", "SSP", 'S', "blockNeodymiumMagnet", 'P',
                    "plateManganeseSteel", 'T', "blockManganese", 'B',
                    "dustRedstone");
        }
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK5),
                "GPG", "ICI", "GPG", 'G', "gearManganeseSteel", 'P',
                "plateManganeseSteel", 'I', "plateIron", 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK5));
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK5),
                "GPG", "ICI", "GPG", 'G', "gearManganeseSteel", 'P',
                "plateManganeseSteel", 'I', "plateSteel", 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK5));
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotor, LevelTypes.MK5), "SPS",
                "PBP", "SPS", 'S', ItemType.DenseSilverCoil.item(), 'P',
                "plateManganeseSteel", 'B', Blocks.diamond_block);

        // MK7
        addCasingRecipe(LevelTypes.MK7, "plateVanadiumSteel",
                "plateVanadiumSteel", "blockVanadiumSteel");
        addPaddleBaseRecipe(LevelTypes.MK7);
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.drainagePlate, LevelTypes.MK7),
                "WW", "KW", "KW", 'W', "plateVanadiumSteel", 'K',
                "blockVanadiumSteel");
        if (Mods.IndustrialCraft2.isAvailable) {
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK7),
                    "PDP", "DCD", "PDP", 'P',
                    ItemType.WaterResistantRubberDensePlate.item(), 'C',
                    getUsualItemStack(ICItemFinder.getIC2Item("lapotronCrystal")), 'D',
                    ItemType.DataBall.item());
        } else {
            this.addRecipeByOreDictionary(
                    ItemCrafting.get(CraftingTypes.circuit, LevelTypes.MK7),
                    "PDP", "DCD", "PDP", 'P',
                    ItemType.WaterResistantRubberDensePlate.item(), 'C',
                    "blockDiamond", 'D', ItemType.DataBall.item());
        }
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.fixedFrame, LevelTypes.MK7),
                "P  ", "DP ", "SDP", 'P', "plateVanadiumSteel", 'D',
                "plateDenseVanadiumSteel", 'S', "screwVanadiumSteel");
        if (gregtechRecipe) {
            GregTech_API.sRecipeAdder.addAssemblerRecipe(ItemMaterial.get(
                            MaterialTypes.VanadiumSteel, MaterialForms.plateDense),
                    ItemMaterial.get(MaterialTypes.VanadiumSteel,
                            MaterialForms.ingot), ItemCrafting.get(
                            CraftingTypes.fixedTool, LevelTypes.MK7), 6000, 5);
        } else {
            this.addShapelessRecipeByOreDictionary(
                    ItemCrafting.get(CraftingTypes.fixedTool, LevelTypes.MK7),
                    "plateDenseVanadiumSteel", "ingotVanadiumSteel");
        }
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.stator, LevelTypes.MK7), "PP",
                "PB", "PP", 'P', "plateVanadiumSteel", 'B',
                "blockNeodymiumMagnet");

        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.outputInterface, LevelTypes.MK7),
                "SSP", "PTB", "SSP", 'S', "blockNeodymiumMagnet", 'P',
                "plateVanadiumSteel", 'T', "blockVanadium", 'B', "dustRedstone");
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK7),
                "GPG", "ICI", "GPG", 'G', "gearVanadiumSteel", 'P',
                "plateVanadiumSteel", 'I', "plateIron", 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK7));
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotationAxle, LevelTypes.MK7),
                "GPG", "ICI", "GPG", 'G', "gearVanadiumSteel", 'P',
                "plateVanadiumSteel", 'I', "plateSteel", 'C',
                ItemCrafting.get(CraftingTypes.casing, LevelTypes.MK7));
        this.addRecipeByOreDictionary(
                ItemCrafting.get(CraftingTypes.rotor, LevelTypes.MK7), "SPS",
                "PBP", "SPS", 'S', ItemType.DenseSilverCoil.item(), 'P',
                "plateVanadiumSteel", 'B', Blocks.diamond_block);

        if (gregtechRecipe) {
            GregTech_API.sRecipeAdder.addChemicalRecipe(
                    changeMount(ICItemFinder.getIC2Item("airCell"), 3),
                    ICItemFinder.getIC2Item("biofuelCell"),
                    ItemType.OxygenEthanolFuel.item(4), 20);
            GregTech_API.sRecipeAdder.addAssemblerRecipe(
                    ICItemFinder.getIC2Item("coil"),
                    ICItemFinder.getIC2Item("copperCableItem"),
                    ItemType.DenseCoil.item(), 120 * 20, 2);
            
            if(Mods.Mekanism.isAvailable)
                GregTech_API.sRecipeAdder.addAssemblerRecipe(
                        ICItemFinder.getIC2Item("coil"), copperCable,
                        ItemType.DenseCoil.item(), 120 * 20, 2);

            GregTech_API.sRecipeAdder.addAssemblerRecipe(
                    ItemType.SilverCoil.item(), ItemType.SilverCoil.item(),
                    ItemType.DenseSilverCoil.item(), 240 * 20, 4);

            GregTech_API.sRecipeAdder.addBlastRecipe(
                    ICItemFinder.getIC2Item("carbonMesh"), null,
                    ItemType.HighPurityCarbonDust.item(), null, 240 * 20, 512,
                    3000);
        } else {
            flag = 0;
            if (Mods.IndustrialCraft2.isAvailable) {
                flag = 1;
                addShapelessRecipeByOreDictionary(ItemType.DenseCoil.item(),
                        ICItemFinder.getIC2Item("coil"),
                        ICItemFinder.getIC2Item("copperCableItem"));
                RecipeAdder.blastFurnace(ICItemFinder.getIC2Item("carbonMesh"),
                        ItemType.HighPurityCarbonDust.item(), 1000);
            } else {
                RecipeAdder.blastFurnace(new ItemStack(Items.coal),
                        ItemType.HighPurityCarbonDust.item(), 1000);
            }

            if (Mods.Mekanism.isAvailable) {
                flag = 1;
                addShapelessRecipeByOreDictionary(ItemType.DenseCoil.item(),
                        ICItemFinder.getIC2Item("coil"), copperCable);
            }
            
            if(flag == 0) {
                    addRecipeByOreDictionary(ItemType.DenseCoil.item(), " I ",
                            "CCC", " I ", 'C', "ingotVanadium", 'I',
                            "ingotIron");
                addRecipeByOreDictionary(ItemType.DenseCoil.item(), " I ",
                        "CCC", " I ", 'C', "ingotCopper", 'I', "ingotIron");
            }

            addShapelessRecipeByOreDictionary(ItemType.DenseSilverCoil.item(),
                    ItemType.SilverCoil.item(), ItemType.SilverCoil.item());
        }
        addRecipeByOreDictionary(ItemType.SilverCoil.item(), "SSS", "SIS",
                "SSS", 'S', "ingotSilver", 'I', "ingotIndustrialSteel");
        addRecipeByOreDictionary(ItemType.SilverCoil.item(), "SSS", "SIS",
                "SSS", 'S', "ingotNeodymium", 'I', "ingotIndustrialSteel");

    }

    private ItemStack changeMount(ItemStack base, int newMount) {
        ItemStack iStack = base.copy();
        iStack.stackSize = newMount;
        return iStack;
    }

    public abstract void registerWatermills();

    public abstract void registerRange();

    public abstract void registerUpdater();

    public abstract void registerTurbine();

    public abstract void registerMachines();

    public abstract void registerPlugins();

    public static ItemStack getUsualItemStack(ItemStack in) {
        if (in == null) return null;
        return new ItemStack(in.getItem(), in.stackSize,
                OreDictionary.WILDCARD_VALUE);
    }

    public static boolean addRecipeByOreDictionary(ItemStack output,
                                                   Object... params) {
        for (Object object : params) if (object == null) return false;
        GameRegistry.addRecipe(new ShapedOreRecipe(output, params));
        return true;
    }

    public static boolean addShapelessRecipeByOreDictionary(ItemStack output,
                                                            Object... params) {
        for (Object object : params) if (object == null) return false;
        GameRegistry.addRecipe(new ShapelessOreRecipe(output, params));
        return true;
    }

    public static void addSmelting(ItemStack input, ItemStack output) {
        FurnaceRecipes.smelting().func_151394_a(input, output, 0);
    }

    public static void registerOreDict(String name, ItemStack stack) {
        OreDictionary.registerOre(name, stack);
    }
    
    public static boolean doesOreNameExist(String name) {
        return OreDictionary.getOres(name).size() > 0;
    }
    
    public static ItemStack getItem(String modId, String itemId, int meta) {
        return new ItemStack(GameRegistry.findItem(modId, itemId), 1, meta);
    }

}
