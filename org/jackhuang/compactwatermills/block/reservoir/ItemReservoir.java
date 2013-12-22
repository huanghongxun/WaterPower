package org.jackhuang.compactwatermills.block.reservoir;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemReservoir extends ItemBlock {
	public ItemReservoir(int id) {
		super(id);
		setMaxDamage(0);
		setHasSubtypes(true);
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		par3List.add("最大存储: " + ReservoirType.values()[par1ItemStack.getItemDamage()].capacity + "桶(b)");
	}
	
	@Override
	public String getItemDisplayName(ItemStack par1ItemStack) {
		return ReservoirType.values()[par1ItemStack.getItemDamage()].showedName;
	}
	
	@Override
	public int getMetadata(int i) {
		if (i < ReservoirType.values().length) {
			return i;
		}
		else {
			return 0;
		}
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= ReservoirType.values().length) return null;
		return ReservoirType.values()[itemstack.getItemDamage()].name();
	}
}
