package org.jackhuang.compactwatermills.block.turbines;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class ItemTurbine extends ItemBlock {

	public ItemTurbine(int id) {
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		par3List.add(TurbineType.values()[par1ItemStack.getItemDamage()].percent + "EU/t");
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return TurbineType.values()[par1ItemStack.getItemDamage()].showedName;
	}
	
	@Override
	public int getMetadata(int i) {
		if(i < TurbineType.values().length)
			return i;
		else
			return 0;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= TurbineType.values().length) return null;
		return TurbineType.values()[itemstack.getItemDamage()].unlocalizedName;
	}
}
