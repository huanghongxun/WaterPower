package org.jackhuang.compactwatermills.item.rotors;


import java.util.List;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.block.watermills.TileEntityWatermill;
import org.jackhuang.compactwatermills.tileentity.TileEntityBaseGenerator;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class ItemRotor extends Item {
	
	public RotorType type;
	
	public ItemRotor(RotorType type) {
		super(type.id + Reference.ItemIDDifference);
		this.type = type;
		setCreativeTab(CompactWatermills.creativeTabCompactWatermills);
		setMaxStackSize(1);
		setNoRepair();
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		if (! type.isInfinite()) {
			int leftOverTicks = par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage();
			par3List.add("剩余时间" + leftOverTicks + "tick");
			
			String str = "(";
			str = str + (leftOverTicks / 72000) + " 小时";
			str = str + ((leftOverTicks % 72000) / 1200) + " 分钟";
			str = str + ((leftOverTicks % 1200) / 20) + " 秒";
			str = str + ").";
			par3List.add(str);
		}
		else {
			par3List.add("永久");
		}
		par3List.add("获得效率: "
			+ (int) (((ItemRotor) par1ItemStack.getItem()).type.efficiency * 100) + "%");
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		itemIcon = iconRegister.registerIcon(Reference.ModID + ":" + this.getUnlocalizedName());
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getRenderTexture() {
		return new ResourceLocation(Reference.ModID + ":textures/rotors/"
			+ this.getUnlocalizedName() + ".png");
	}
	
	public void tickRotor(ItemStack rotor, TileEntityBaseGenerator tileEntity, World worldObj) {
		return;
	}
	
}
