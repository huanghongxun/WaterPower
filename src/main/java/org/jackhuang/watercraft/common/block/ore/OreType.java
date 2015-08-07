package org.jackhuang.watercraft.common.block.ore;

import org.jackhuang.watercraft.client.render.RecolorableItemRenderer;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;
import org.jackhuang.watercraft.common.recipe.RecipeAdder;
import org.jackhuang.watercraft.integration.ic2.ICItemFinder;
import org.jackhuang.watercraft.integration.ic2.IndustrialCraftModule;
import org.jackhuang.watercraft.util.Mods;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public enum OreType {

    Monazite(MaterialTypes.Neodymium),
    Vanadium(MaterialTypes.Vanadium),
    Manganese(MaterialTypes.Manganese),
    Magnet(MaterialTypes.Magnet),
    Zinc(MaterialTypes.Zinc);

    public short R, G, B, A;

    private OreType(MaterialTypes t) {
	R = t.R;
	G = t.G;
	B = t.B;
	A = t.A;
    }

    public String getShowedName() {
	return StatCollector.translateToLocal(getUnlocalizedName());
    }

    public String getUnlocalizedName() {
	return "cptwtrml.ore." + name().toLowerCase();
    }

    public static void registerRecipes() {
	for (OreType o : OreType.values()) {
	    RecipeAdder.macerator(new ItemStack(GlobalBlocks.ore, 1, o.ordinal()), new ItemStack(GlobalItems.oreDust, 2, o.ordinal()));
	}
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Monazite.ordinal()),
		ItemMaterial.get(MaterialTypes.Neodymium, MaterialForms.ingot), 0f);
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Vanadium.ordinal()),
		ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.ingot), 0f);
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Manganese.ordinal()),
		ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.ingot), 0f);
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Magnet.ordinal()),
		ItemMaterial.get(MaterialTypes.Magnet, MaterialForms.ingot), 0f);
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, Zinc.ordinal()),
		ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.ingot), 0f);

	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Monazite.ordinal()),
		ItemMaterial.get(MaterialTypes.Neodymium, MaterialForms.ingot), 0f);
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Vanadium.ordinal()),
		ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.ingot), 0f);
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Manganese.ordinal()),
		ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.ingot), 0f);
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Magnet.ordinal()),
		ItemMaterial.get(MaterialTypes.Magnet, MaterialForms.ingot), 0f);
	FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, Zinc.ordinal()),
		ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.ingot), 0f);

	if (Mods.IndustrialCraft2.isAvailable) {
	    ItemStack item = ICItemFinder.getIC2Item("stoneDust");
	    ItemStack iron = ICItemFinder.getIC2Item("smallIronDust");

	    IndustrialCraftModule.oreWashing(new ItemStack(GlobalItems.oreDust, 1, Monazite.ordinal()),
		    ItemMaterial.get(MaterialTypes.Neodymium, MaterialForms.dust), item);
	    IndustrialCraftModule.oreWashing(new ItemStack(GlobalItems.oreDust, 1, Vanadium.ordinal()),
		    ItemMaterial.get(MaterialTypes.Vanadium, MaterialForms.dust), iron, item);
	    IndustrialCraftModule.oreWashing(new ItemStack(GlobalItems.oreDust, 1, Manganese.ordinal()),
		    ItemMaterial.get(MaterialTypes.Manganese, MaterialForms.dust), iron, item);
	    IndustrialCraftModule.oreWashing(new ItemStack(GlobalItems.oreDust, 1, Magnet.ordinal()),
		    ItemMaterial.get(MaterialTypes.Magnet, MaterialForms.dust), iron, item);
	    IndustrialCraftModule.oreWashing(new ItemStack(GlobalItems.oreDust, 1, Zinc.ordinal()),
		    ItemMaterial.get(MaterialTypes.Zinc, MaterialForms.dust), iron, item);
	}
    }
}
