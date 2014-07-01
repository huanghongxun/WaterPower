package org.jackhuang.compactwatermills.common.block.watermills;

import java.util.List;

import org.jackhuang.compactwatermills.common.item.ItemMeta;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class ItemCompactWaterMill extends ItemMeta {
	
	public ItemCompactWaterMill(Block id) {
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		par3List.add("最大输出: " + WaterType.values()[par1ItemStack.getItemDamage()].output
			+ "EU/t");
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack par1ItemStack) {
		return WaterType.values()[par1ItemStack.getItemDamage()].getShowedName();
	}
	
	@Override
	public int getMetadata(int i) {
		if (i < WaterType.values().length) {
			return i;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= WaterType.values().length) return null;
		return WaterType.values()[itemstack.getItemDamage()].name();
	}
	
}
