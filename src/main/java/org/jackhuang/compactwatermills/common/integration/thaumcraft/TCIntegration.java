package org.jackhuang.compactwatermills.common.integration.thaumcraft;

import gregtech.api.GregTech_API;
import ic2.api.item.IC2Items;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.common.block.GlobalBlocks;
import org.jackhuang.compactwatermills.common.block.reservoir.ReservoirType;
import org.jackhuang.compactwatermills.common.block.watermills.WaterType;
import org.jackhuang.compactwatermills.common.integration.IIntegration;
import org.jackhuang.compactwatermills.common.item.GlobalItems;
import org.jackhuang.compactwatermills.common.item.others.ItemType;
import org.jackhuang.compactwatermills.common.item.range.RangeType;
import org.jackhuang.compactwatermills.common.item.rotors.RotorType;
import org.jackhuang.compactwatermills.common.recipe.IRecipeHandler;
import org.jackhuang.compactwatermills.common.recipe.NormalRecipeHandler;
import org.jackhuang.compactwatermills.helpers.LogHelper;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.registry.GameRegistry;
import thaumcraft.api.ThaumcraftApi;
import thaumcraft.api.aspects.*;
import thaumcraft.api.crafting.*;
import thaumcraft.api.research.*;

/**
 * 处理兼容Thaumcraft。
 * 
 * @author hyh
 * 
 */
public class TCIntegration extends IIntegration {

	AspectList listIndustrialDiamond = new AspectList().add(Aspect.WATER, 2)
			.add(Aspect.EARTH, 4);
	AspectList listWaterUraniumIngot = new AspectList().add(Aspect.WATER, 2);
	AspectList listReservoirCore = new AspectList().add(Aspect.WATER, 8)
			.add(Aspect.EARTH, 2).add(Aspect.METAL, 10).add(Aspect.GREED, 5);
	AspectList listReservoirCoreAdvanced = new AspectList()
			.add(Aspect.WATER, 8).add(Aspect.EARTH, 4).add(Aspect.METAL, 15)
			.add(Aspect.GREED, 8).add(Aspect.CRYSTAL, 2);
	AspectList listWatermillMK1 = new AspectList().add(Aspect.WATER, 8)
			.add(Aspect.EARTH, 8).add(Aspect.MECHANISM, 8).add(Aspect.METAL, 8);
	AspectList listWatermillMK2 = new AspectList().add(Aspect.WATER, 10)
			.add(Aspect.EARTH, 10).add(Aspect.MECHANISM, 10)
			.add(Aspect.METAL, 10);
	AspectList listWatermillMK3 = new AspectList().add(Aspect.WATER, 12)
			.add(Aspect.EARTH, 12).add(Aspect.MECHANISM, 12)
			.add(Aspect.METAL, 12);
	AspectList listWatermillMK4 = new AspectList().add(Aspect.WATER, 14)
			.add(Aspect.EARTH, 14).add(Aspect.MECHANISM, 14)
			.add(Aspect.METAL, 14);
	AspectList listWatermillMK5 = new AspectList().add(Aspect.WATER, 16)
			.add(Aspect.EARTH, 16).add(Aspect.MECHANISM, 16)
			.add(Aspect.METAL, 16);
	AspectList listWatermillMK6 = new AspectList().add(Aspect.WATER, 18)
			.add(Aspect.EARTH, 18).add(Aspect.MECHANISM, 18)
			.add(Aspect.METAL, 18);
	AspectList listWatermillMK7 = new AspectList().add(Aspect.WATER, 20)
			.add(Aspect.EARTH, 20).add(Aspect.MECHANISM, 20)
			.add(Aspect.METAL, 20);
	AspectList listWatermillMK8 = new AspectList().add(Aspect.WATER, 22)
			.add(Aspect.EARTH, 22).add(Aspect.MECHANISM, 22)
			.add(Aspect.METAL, 22);
	AspectList listWatermillMK9 = new AspectList().add(Aspect.WATER, 24)
			.add(Aspect.EARTH, 24).add(Aspect.MECHANISM, 24)
			.add(Aspect.METAL, 24);
	ShapedArcaneRecipe recipeIndustrialDiamond, recipeWaterUraniumIngot,
			recipeReservoirCore;
	InfusionRecipe recipeReservoirCoreAdvanced, recipeWatermillMK1,
			recipeWatermillMK2, recipeWatermillMK3, recipeWatermillMK4,
			recipeWatermillMK5, recipeWatermillMK6, recipeWatermillMK7,
			recipeWatermillMK8, recipeWatermillMK9;

	static {
		IIntegration.integrations.add(new TCIntegration());
	}

	public String name() {
		return "Thaumcraft Integration";
	}

	@Override
	public void integrate() {
		if (!Loader.isModLoaded("Thaumcraft")) {
			LogHelper.log("Thaumcraft isn't loaded, tcintegration stopped.");
			return;
		}
		ResourceLocation rl = new ResourceLocation(Reference.ModID
				+ ":textures/items/item.IndustrialDiamond.png");
		ResourceLocation bg = new ResourceLocation(Reference.ModID
				+ ":textures/gui/GUIResearchBackground.png");

		ResearchCategories.registerCategory(Reference.ModID, rl, bg);
		registerRecipes();
		registerResearches();

	}

	/**
	 * 注册TC工作台和注魔台的合成配方
	 */
	public void registerRecipes() {

		recipeIndustrialDiamond = ThaumcraftApi.addArcaneCraftingRecipe(
				"industrialDiamond", IC2Items.getItem("industrialDiamond"),
				listIndustrialDiamond, "W", 'W', Items.diamond);
		/*if (CompactWatermills.isGregTechLoaded)
			recipeWaterUraniumIngot = ThaumcraftApi.addArcaneCraftingRecipe(
					"waterUraniumIngot", UpdaterType.WaterUraniumIngot.item(),
					listWaterUraniumIngot, "W", 'W',
					GregTech_API.getGregTechMaterial(43, 1));
		else*/
			recipeWaterUraniumIngot = ThaumcraftApi.addArcaneCraftingRecipe(
					"waterUraniumIngot", ItemType.WaterUraniumIngot.item(),
					listWaterUraniumIngot, "W", 'W', IC2Items.getItem("Uran238"));
		recipeReservoirCore = ThaumcraftApi.addArcaneCraftingRecipe(
				"reservoirCore",
				ItemType.ReservoirCore.item(),
				new AspectList().add(Aspect.WATER, 40).add(Aspect.EARTH, 40)
						.add(Aspect.ORDER, 40).add(Aspect.ENTROPY, 40)
						.add(Aspect.AIR, 40), " A ", "SMS", " C ", 'A',
						NormalRecipeHandler.getUsualItemStack(IC2Items
						.getItem("electronicCircuit")), 'S', IC2Items
						.getItem("advancedAlloy"), 'M', IC2Items
						.getItem("plateiron"), 'C',
				ItemType.WaterUraniumPlateMK1.item());
		recipeReservoirCoreAdvanced = ThaumcraftApi.addInfusionCraftingRecipe(
				"reservoirCoreAdvanced",
				ItemType.ReservoirCoreAdvanced.item(),
				3,
				listReservoirCoreAdvanced,
				ItemType.ReservoirCore.item(),
				new ItemStack[] { IC2Items.getItem("iridiumPlate"),
					IC2Items.getItem("iridiumPlate"),
					IC2Items.getItem("advancedCircuit"),
						NormalRecipeHandler.getInstance().industrialDiamond });
		recipeWatermillMK1 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK1",
				new ItemStack(GlobalBlocks.waterMill, 0, 0),
				1,
				listWatermillMK1,
				IC2Items.getItem("waterMill"),
				new ItemStack[] { ItemType.WaterUraniumIngot.item(),
					IC2Items.getItem("carbonPlate"),
						IC2Items.getItem("advancedAlloy"),
						IC2Items.getItem("electronicCircuit") });
		recipeWatermillMK2 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK2",
				new ItemStack(GlobalBlocks.waterMill, 0, 1),
				1,
				listWatermillMK2,
				new ItemStack(GlobalBlocks.waterMill, 0, 0),
				new ItemStack[] { ItemType.WaterUraniumIngot.item(),
					IC2Items.getItem("carbonPlate"),
						IC2Items.getItem("advancedAlloy"),
						IC2Items.getItem("electronicCircuit"),
						IC2Items.getItem("plateiron"),
						IC2Items.getItem("advancedCircuit"),
						ItemType.MK0.item() });
		recipeWatermillMK3 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK3",
				new ItemStack(GlobalBlocks.waterMill, 0, 2),
				1,
				listWatermillMK3,
				new ItemStack(GlobalBlocks.waterMill, 0, 1),
				new ItemStack[] { ItemType.WaterUraniumPlateMK1.item(),
						ItemType.WaterUraniumPlateMK1.item(),
						IC2Items.getItem("advancedCircuit"),
						ItemType.MK0.item(), ItemType.MK0.item() });
		recipeWatermillMK4 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK4",
				new ItemStack(GlobalBlocks.waterMill, 0, 3),
				1,
				listWatermillMK4,
				new ItemStack(GlobalBlocks.waterMill, 0, 2),
				new ItemStack[] { ItemType.WaterUraniumIngot.item(),
					IC2Items.getItem("carbonPlate"),
						ItemType.WaterUraniumPlateMK1.item(),
						IC2Items.getItem("advancedCircuit"),
						IC2Items.getItem("advancedCircuit"),
						ItemType.MK1.item() });
		recipeWatermillMK5 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK5",
				new ItemStack(GlobalBlocks.waterMill, 0, 4),
				1,
				listWatermillMK5,
				new ItemStack(GlobalBlocks.waterMill, 0, 3),
				new ItemStack[] { ItemType.WaterUraniumPlateMK1.item(),
						ItemType.WaterUraniumPlateMK1.item(),
						IC2Items.getItem("advancedCircuit"),
						ItemType.MK1.item(), ItemType.MK1.item() });
		recipeWatermillMK6 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK6",
				new ItemStack(GlobalBlocks.waterMill, 0, 5),
				1,
				listWatermillMK6,
				new ItemStack(GlobalBlocks.waterMill, 0, 4),
				new ItemStack[] { ItemType.WaterUraniumIngot.item(),
					IC2Items.getItem("iridiumPlate"),
						IC2Items.getItem("advancedAlloy"),
						IC2Items.getItem("advancedAlloy"),
						IC2Items.getItem("advancedCircuit"),
						IC2Items.getItem("advancedCircuit"),
						ItemType.MK2.item() });
		recipeWatermillMK7 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK7",
				new ItemStack(GlobalBlocks.waterMill, 0, 6),
				1,
				listWatermillMK7,
				new ItemStack(GlobalBlocks.waterMill, 0, 5),
				new ItemStack[] { ItemType.WaterUraniumPlateMK2.item(),
						ItemType.WaterUraniumPlateMK2.item(),
						IC2Items.getItem("advancedCircuit"),
						ItemType.MK2.item(), ItemType.MK2.item() });
		recipeWatermillMK8 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK8",
				new ItemStack(GlobalBlocks.waterMill, 0, 7),
				1,
				listWatermillMK8,
				new ItemStack(GlobalBlocks.waterMill, 0, 6),
				new ItemStack[] { ItemType.WaterUraniumIngot.item(),
					IC2Items.getItem("carbonPlate"),
					IC2Items.getItem("advancedAlloy"),
					IC2Items.getItem("advancedCircuit"),
						ItemType.MK3.item() });
		recipeWatermillMK9 = ThaumcraftApi.addInfusionCraftingRecipe(
				"cptwtrmlMK9",
				new ItemStack(GlobalBlocks.waterMill, 0, 8),
				1,
				listWatermillMK9,
				new ItemStack(GlobalBlocks.waterMill, 0, 7),
				new ItemStack[] { ItemType.WaterUraniumPlateMK3.item(),
						ItemType.WaterUraniumPlateMK3.item(),
						IC2Items.getItem("advancedCircuit"),
						IC2Items.getItem("advancedCircuit"),
						ItemType.MK3.item(), ItemType.MK3.item() });
	}

	/**
	 * 处理item和block的研究
	 */
	public void registerResearches() {
		// 水之铀锭
		new ResearchItem("waterUraniumIngot", Reference.ModID,
				listWaterUraniumIngot, -1, -1, 0,
				ItemType.WaterUraniumIngot.item())
				.setPages(new ResearchPage("cptwtrml.research.reservoirCore"),
						new ResearchPage(recipeWaterUraniumIngot)).setStub()
				.setAutoUnlock().registerResearchItem();

		// 工业钻石 炼金术
		new ResearchItem("industrialDiamond", Reference.ModID,
				listIndustrialDiamond, -2, -2, 0,
				IC2Items.getItem("industrialDiamond"))
				.setPages(
						new ResearchPage("cptwtrml.research.industrialDiamond"),
						new ResearchPage(recipeIndustrialDiamond)).setStub()
				.setParents("waterUraniumIngot").registerResearchItem();

		// 水库核心
		new ResearchItem("reservoirCore", Reference.ModID, listReservoirCore,
				-2, 1, 0, ItemType.ReservoirCore.item())
				.setPages(new ResearchPage("cptwtrml.research.reservoirCore"),
						new ResearchPage(recipeReservoirCore)).setStub()
				.setAutoUnlock().registerResearchItem();

		// 高级水库核心
		new ResearchItem("reservoirCoreAdvanced", Reference.ModID,
				listReservoirCoreAdvanced, -4, 3, 0,
				ItemType.ReservoirCoreAdvanced.item())
				.setPages(
						new ResearchPage(
								"cptwtrml.research.reservoirCoreAdvanced"),
						new ResearchPage(recipeReservoirCoreAdvanced))
				.setParents("reservoirCore").setAutoUnlock()
				.registerResearchItem();

		// 水力发电机MK1
		new ResearchItem("cptwtrmlMK1", Reference.ModID, listWatermillMK1, 1,
				1, 0, new ItemStack(GlobalBlocks.waterMill, 0, 0))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK1"),
						new ResearchPage(recipeWatermillMK1))
				.setParents("waterUraniumIngot").setAutoUnlock()
				.registerResearchItem();
		// 水力发电机MK2
		new ResearchItem("cptwtrmlMK2", Reference.ModID, listWatermillMK2, 3,
				1, 0, new ItemStack(GlobalBlocks.waterMill, 0, 1))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK2"),
						new ResearchPage(recipeWatermillMK2))
				.setParents("cptwtrmlMK1").setAutoUnlock()
				.registerResearchItem();
		// 水力发电机MK3
		new ResearchItem("cptwtrmlMK3", Reference.ModID, listWatermillMK3, 5,
				1, 0, new ItemStack(GlobalBlocks.waterMill, 0, 2))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK3"),
						new ResearchPage(recipeWatermillMK3))
				.setParents("cptwtrmlMK2").setAutoUnlock()
				.registerResearchItem();
		// 水力发电机MK4
		new ResearchItem("cptwtrmlMK4", Reference.ModID, listWatermillMK4, 7,
				1, 0, new ItemStack(GlobalBlocks.waterMill, 0, 3))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK4"),
						new ResearchPage(recipeWatermillMK4))
				.setParents("cptwtrmlMK3").setAutoUnlock()
				.registerResearchItem();
		// 水力发电机MK5
		new ResearchItem("cptwtrmlMK5", Reference.ModID, listWatermillMK5, 9,
				1, 0, new ItemStack(GlobalBlocks.waterMill, 0, 4))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK5"),
						new ResearchPage(recipeWatermillMK5))
				.setParents("cptwtrmlMK4").setAutoUnlock()
				.registerResearchItem();
		// 水力发电机MK6
		new ResearchItem("cptwtrmlMK6", Reference.ModID, listWatermillMK6, 9,
				3, 0, new ItemStack(GlobalBlocks.waterMill, 0, 5))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK6"),
						new ResearchPage(recipeWatermillMK6))
				.setParents("cptwtrmlMK5").setAutoUnlock()
				.registerResearchItem();
		// 水力发电机MK7
		new ResearchItem("cptwtrmlMK7", Reference.ModID, listWatermillMK7, 7,
				3, 0, new ItemStack(GlobalBlocks.waterMill, 0, 6))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK7"),
						new ResearchPage(recipeWatermillMK7))
				.setParents("cptwtrmlMK6").setAutoUnlock()
				.registerResearchItem();
		// 水力发电机MK8
		new ResearchItem("cptwtrmlMK8", Reference.ModID, listWatermillMK8, 5,
				3, 0, new ItemStack(GlobalBlocks.waterMill, 0, 7))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK8"),
						new ResearchPage(recipeWatermillMK8))
				.setParents("cptwtrmlMK7").setAutoUnlock()
				.registerResearchItem();
		// 水力发电机MK9
		new ResearchItem("cptwtrmlMK9", Reference.ModID, listWatermillMK9, 3,
				3, 0, new ItemStack(GlobalBlocks.waterMill, 0, 8))
				.setPages(new ResearchPage("cptwtrml.research.cptwtrmlMK9"),
						new ResearchPage(recipeWatermillMK9))
				.setParents("cptwtrmlMK8").setAutoUnlock()
				.registerResearchItem();

	}

	public void registerObjectTag() {
		ThaumcraftApi.registerObjectTag(ItemType.IR_FE.item(),
				new AspectList().add(Aspect.METAL, 10).add(Aspect.GREED, 10));
		ThaumcraftApi.registerObjectTag(ItemType.MK0.item(),
				new AspectList().add(Aspect.MECHANISM, 1).add(Aspect.METAL, 3)
						.add(Aspect.GREED, 3));
		ThaumcraftApi.registerObjectTag(ItemType.MK1.item(),
				new AspectList().add(Aspect.MECHANISM, 2).add(Aspect.METAL, 6)
						.add(Aspect.GREED, 6).add(Aspect.TOOL, 1));
		ThaumcraftApi.registerObjectTag(ItemType.MK2.item(),
				new AspectList().add(Aspect.MECHANISM, 3).add(Aspect.METAL, 9)
						.add(Aspect.GREED, 9).add(Aspect.TOOL, 2));
		ThaumcraftApi.registerObjectTag(ItemType.MK3.item(),
				new AspectList().add(Aspect.MECHANISM, 4).add(Aspect.METAL, 12)
						.add(Aspect.GREED, 12).add(Aspect.TOOL, 3));
		ThaumcraftApi.registerObjectTag(
				ItemType.WaterUraniumPlateMK1.item(),
				new AspectList().add(Aspect.WATER, 8).add(Aspect.EARTH, 2)
						.add(Aspect.METAL, 3).add(Aspect.GREED, 1));
		ThaumcraftApi.registerObjectTag(
				ItemType.WaterUraniumPlateMK2.item(),
				new AspectList().add(Aspect.WATER, 64).add(Aspect.EARTH, 16)
						.add(Aspect.METAL, 24).add(Aspect.GREED, 8));
		ThaumcraftApi.registerObjectTag(
				ItemType.WaterUraniumPlateMK3.item(),
				new AspectList().add(Aspect.WATER, 512).add(Aspect.EARTH, 128)
						.add(Aspect.METAL, 192).add(Aspect.GREED, 64));
		ThaumcraftApi.registerObjectTag(ItemType.ReservoirCore.item(),
				listReservoirCore);
		ThaumcraftApi.registerObjectTag(
				ItemType.ReservoirCoreAdvanced.item(),
				listReservoirCoreAdvanced);
		int i = 1;
		for (RangeType t : RangeType.values()) {
			ThaumcraftApi.registerObjectTag(new ItemStack(GlobalItems.range, 1,
					t.ordinal()), new AspectList().add(Aspect.WATER, 2 * i)
					.add(Aspect.EARTH, 2 * i).add(Aspect.METAL, 2 * i));
			i *= 2;
		}
		for (RotorType t : RotorType.values()) {
			ThaumcraftApi.registerObjectTag(
					new ItemStack(t.getItem(),
					1, OreDictionary.WILDCARD_VALUE),
					new AspectList().add(Aspect.WATER, 2).add(Aspect.EARTH, 2)
							.add(Aspect.MECHANISM, 2).add(Aspect.TOOL, 2)
							.add(Aspect.GREED, 2));
		}
		for (ReservoirType t : ReservoirType.values()) {
			ThaumcraftApi.registerObjectTag( new ItemStack(GlobalBlocks.reservoir,
					1, t.ordinal()),
					new AspectList().add(Aspect.WATER, 16)
							.add(Aspect.EARTH, 16).add(Aspect.MECHANISM, 16)
							.add(Aspect.TOOL, 16).add(Aspect.GREED, 16));
		}
		for (WaterType w : WaterType.values()) {
			ThaumcraftApi.registerObjectTag(
					new ItemStack(GlobalBlocks.waterMill, 1,
					w.ordinal()),
					new AspectList().add(Aspect.WATER, 2 * w.ordinal())
							.add(Aspect.EARTH, 2 * w.ordinal())
							.add(Aspect.MECHANISM, 2 * w.ordinal())
							.add(Aspect.TOOL, 2 * w.ordinal())
							.add(Aspect.GREED, 2 * w.ordinal()));
		}
	}

}
