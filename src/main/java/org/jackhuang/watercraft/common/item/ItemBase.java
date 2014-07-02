package org.jackhuang.watercraft.common.item;

import java.util.List;

import org.jackhuang.watercraft.WaterCraft;
import org.jackhuang.watercraft.InternalName;
import org.jackhuang.watercraft.Reference;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;

/**
 * 继承此类能获得帮助读取图片和处理meta的功能
 * @author hyh
 */
public abstract class ItemBase extends Item {

	protected final InternalName internalName;
	public static int id;
	protected IIcon[] textures;

	public ItemBase(InternalName internalName) {
		super();

		setCreativeTab(WaterCraft.creativeTabWaterCraft);
		setNoRepair();
		
		GameRegistry.registerItem(this, internalName.name());

		this.internalName = internalName;
	}

	public String getTextureName(int index) {
		if (this.hasSubtypes)
			return getUnlocalizedName(new ItemStack(this, 1, index));
		if (index == 0) {
			return getUnlocalizedName();
		}
		return null;
	}

	public abstract String getTextureFolder();
	
	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		if(WaterCraft.instance.iconRegister == null) {
			WaterCraft.instance.iconRegister = iconRegister;
			WaterCraft.instance.loadAllIcons();
		}
		
		this.textures = new IIcon[32768];
		String textureFolder = getTextureFolder() + "/";

		for (int index = 0; index < 32768; index++) {
			String resource = Reference.ModID
					+ ":" + textureFolder + getTextureName(index);
			this.textures[index] = getTextureName(index) != null ? 
					iconRegister.registerIcon(resource)
					: null;
		}
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		if (meta < this.textures.length) {
			return this.textures[meta];
		}
		return this.textures.length < 1 ? null : this.textures[0];
	}

	@SideOnly(Side.CLIENT)
	public ResourceLocation getRenderTexture() {
		return new ResourceLocation(Reference.ModID + ":textures/items/"
				+ this.getUnlocalizedName() + ".png");
	}

	@Override
	public void getSubItems(Item item, CreativeTabs par2CreativeTabs,
			List par3List) {
		for (int meta = 0; meta < 32767; meta++) {
			ItemStack stack = new ItemStack(item, 1, meta);
			if (stopScanning(stack))
				break;
			if (validStack(stack))
				par3List.add(stack);
		}
	}
	
	public boolean stopScanning(ItemStack stack) {
		return getUnlocalizedName(stack) == null;
	}
	
	public boolean validStack(ItemStack stack) {
		return getUnlocalizedName(stack) != null;
	}
	
	public ItemStack get(int i) {
		return get(i, 1);
	}
	
	public ItemStack get(int i, int j) {
		return new ItemStack(this, j, i);
	}
}
