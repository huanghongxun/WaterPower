package org.jackhuang.watercraft.common.block.simple;

import ic2.api.recipe.RecipeInputItemStack;
import ic2.api.recipe.Recipes;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;
import net.minecraftforge.oredict.OreDictionary;

import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.client.render.IIconContainer;
import org.jackhuang.watercraft.client.render.RecolorableTextures;
import org.jackhuang.watercraft.common.block.GlobalBlocks;
import org.jackhuang.watercraft.common.item.ItemBase;
import org.jackhuang.watercraft.common.item.ItemRecolorable;
import org.jackhuang.watercraft.common.item.crafting.MaterialForms;
import org.jackhuang.watercraft.common.item.crafting.MaterialTypes;

public class ItemOreDust extends ItemRecolorable {

	public ItemOreDust() {
		super(InternalName.cptItemOreDust);
		
		registerOreDict();
	}

	@Override
	public String getTextureFolder() {
		return "ore";
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= OreType.values().length) return null;
		return "item.oreDust" + OreType.values()[itemstack.getItemDamage()].name();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		return StatCollector.translateToLocal(OreType.values()[itemstack.getItemDamage()].getUnlocalizedName()) + " " +
				StatCollector.translateToLocal("cptwtrml.forms.dust");
	}
	
	public ItemStack get(OreType type) {
		return get(type.ordinal());
	}
	
	public ItemStack get(OreType type, int amount) {
		return get(type.ordinal(), amount);
	}
	
	public void registerOreDict() {
		for(OreType type : OreType.values()) {
			OreDictionary.registerOre("oreDust" + type.name(), get(type));
		}
	}
	
	@Override
	public short[] getRGBA(ItemStack stack) {
		int meta = stack.getItemDamage();
		OreType o = OreType.values()[meta];
		return new short[] {o.R, o.G, o.B, o.A};
	}

	@Override
	public IIconContainer getIconContainer(int meta) {
		return getIconContainers()[0];
	}

	@Override
	public IIconContainer[] getIconContainers() {
		return RecolorableTextures.CRUSHED;
	}

}