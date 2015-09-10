package org.jackhuang.watercraft.common.block.ore;

import java.awt.Color;

import org.jackhuang.watercraft.client.render.RecolorableItemRenderer;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.crafting.ItemMaterial;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;
import org.jackhuang.watercraft.common.recipe.RecipeAdder;
import org.jackhuang.watercraft.util.Mods;

import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.FurnaceRecipes;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;

public enum OreType {
    Monazite(MaterialTypes.Neodymium), Vanadium(MaterialTypes.Vanadium), Manganese(MaterialTypes.Manganese), Magnet(MaterialTypes.Magnet), Zinc(MaterialTypes.Zinc);

    public short R, G, B, A;
    public MaterialTypes t;

    private OreType(MaterialTypes t) {
        this.t = t;
        R = t.R;
        G = t.G;
        B = t.B;
        A = t.A;
    }

    public int getColor() {
        return new Color(R, G, B).getRGB() & 0xffffff;
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
            FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalBlocks.ore, 1, o.ordinal()), ItemMaterial.get(o.t, MaterialForms.ingot), 0f);
            FurnaceRecipes.smelting().func_151394_a(new ItemStack(GlobalItems.oreDust, 1, o.ordinal()), ItemMaterial.get(o.t, MaterialForms.ingot), 0f);
        }
    }
}
