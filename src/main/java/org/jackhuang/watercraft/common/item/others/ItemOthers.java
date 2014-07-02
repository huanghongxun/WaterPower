package org.jackhuang.watercraft.common.item.others;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.item.GlobalItems;
import org.jackhuang.watercraft.common.item.ItemBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOthers extends ItemBase {
	
	public ItemOthers() {
		super(InternalName.cptItemUpdaters);
		setHasSubtypes(true);
	}
	
	@Override
	public String getTextureFolder() {
		return "updaters";
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		String s = ItemType.values()[par1ItemStack.getItemDamage()].getInformation();
		if(s != null) par3List.add(s);
	}
	
	
	
	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= ItemType.values().length) return null;
		return ItemType.values()[itemstack.getItemDamage()].getShowedName();
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= ItemType.values().length) return null;
		return "item." + ItemType.values()[itemstack.getItemDamage()].unlocalizedName;
	}
}
