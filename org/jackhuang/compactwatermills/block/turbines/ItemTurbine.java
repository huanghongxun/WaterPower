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
		par3List.add("最大输出: 32767EU/t");
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return "水轮机";
	}
	
	@Override
	public int getMetadata(int i) {
		return 0;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if(itemstack.getItemDamage() > 0) return null;
		return "turbine";
	}
}
