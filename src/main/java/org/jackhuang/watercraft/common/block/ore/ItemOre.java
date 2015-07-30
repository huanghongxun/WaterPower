package org.jackhuang.watercraft.common.block.ore;

import org.jackhuang.watercraft.common.recipe.IRecipeRegistrator;

import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemOre extends ItemBlock {

	public ItemOre(Block par1) {
		super(par1);
		
		setHasSubtypes(true);
		registerOreDict();
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return OreType.values()[par1ItemStack.getItemDamage()].getShowedName();
	}
	
	@Override
	public int getMetadata(int i) {
		if (i < OreType.values().length) return i;
		else return 0;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= OreType.values().length) return null;
		return OreType.values()[itemstack.getItemDamage()].getUnlocalizedName();
	}
	
	public void registerOreDict() {
                for (OreType value : OreType.values()) {
                    IRecipeRegistrator.registerOreDict(value.name(), new ItemStack(this, 1, value.ordinal()));
                }
	}

}
