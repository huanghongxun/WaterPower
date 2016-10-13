package waterpower.common.block.ore;

import java.awt.Color;

import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import waterpower.client.Local;
import waterpower.common.block.GlobalBlocks;
import waterpower.common.item.GlobalItems;
import waterpower.common.item.crafting.ItemMaterial;
import waterpower.common.item.crafting.MaterialForms;
import waterpower.common.item.crafting.MaterialTypes;
import waterpower.common.recipe.IRecipeRegistrar;
import waterpower.common.recipe.RecipeAdder;

public enum OreType implements IStringSerializable {
    Monazite(MaterialTypes.Neodymium), Vanadium(MaterialTypes.Vanadium), Manganese(MaterialTypes.Manganese), Magnet(MaterialTypes.Magnet), Zinc(
            MaterialTypes.Zinc);

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
        return Local.get(getUnlocalizedName());
    }

    public String getUnlocalizedName() {
        return "cptwtrml.ore." + name().toLowerCase();
    }

    public static void registerRecipes() {
        for (OreType o : OreType.values()) {
            RecipeAdder.macerator(new ItemStack(GlobalBlocks.ore, 1, o.ordinal()), new ItemStack(GlobalItems.oreDust, 2, o.ordinal()));
            IRecipeRegistrar.addSmelting(new ItemStack(GlobalBlocks.ore, 1, o.ordinal()), ItemMaterial.get(o.t, MaterialForms.ingot));
            IRecipeRegistrar.addSmelting(new ItemStack(GlobalItems.oreDust, 1, o.ordinal()), ItemMaterial.get(o.t, MaterialForms.ingot));
        }
    }

	@Override
	public String getName() {
		return name().toLowerCase();
	}
}
