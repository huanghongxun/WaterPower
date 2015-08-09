package org.jackhuang.watercraft.common.item.rotors;

import java.util.List;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.Reference;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityGenerator;
import org.jackhuang.watercraft.common.item.ItemBase;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;
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
	protected IIcon[] textures;
	
	public ItemRotor(RotorType type) {
		super();
		this.type = type;
		setMaxDamage(type.maxDamage);
		setUnlocalizedName(type.unlocalizedName);
		setCreativeTab(WaterPower.creativeTabWaterPower);
		setMaxStackSize(1);
		setNoRepair();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister iconRegister) {
		int indexCount = 0;

		while (getTextureName(indexCount) != null)
			indexCount++;

		this.textures = new IIcon[indexCount];
		String textureFolder = "rotors/";

		for (int index = 0; index < indexCount; index++)
			this.textures[index] = iconRegister.registerIcon(Reference.ModID
					+ ":" + textureFolder + getTextureName(index));
	}

	public String getTextureName(int index) {
		if (this.hasSubtypes)
			return getUnlocalizedName(new ItemStack(this, 1, index));
		if (index == 0) {
			return getUnlocalizedName();
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta) {
		if (meta < this.textures.length) {
			return this.textures[meta];
		}
		return this.textures.length < 1 ? null : this.textures[0];
	}
	
	@SuppressWarnings({"rawtypes", "unchecked"})
	@Override
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
		List par3List, boolean par4) {
		if (! type.isInfinite()) {
			int leftOverTicks = par1ItemStack.getMaxDamage() - par1ItemStack.getItemDamage();
			par3List.add(StatCollector.translateToLocal("cptwtrml.rotor.REMAINTIME") + leftOverTicks + "tick");
			
			String str = "(";
			str = str + (leftOverTicks / 72000) + " " + StatCollector.translateToLocal("cptwtrml.rotor.HOUR");
			str = str + ((leftOverTicks % 72000) / 1200) + " " + StatCollector.translateToLocal("cptwtrml.rotor.MINUTE");
			str = str + ((leftOverTicks % 1200) / 20) + " " + StatCollector.translateToLocal("cptwtrml.rotor.SECOND");
			str = str + ").";
			par3List.add(str);
		}
		else {
			par3List.add(StatCollector.translateToLocal("cptwtrml.rotor.INFINITE"));
		}
		par3List.add(StatCollector.translateToLocal("cptwtrml.rotor.GOT_EFFICIENCY") + " "
			+ (int) (((ItemRotor) par1ItemStack.getItem()).type.getEfficiency() * 100) + "%");
	}
	
	@Override
	public String getItemStackDisplayName(ItemStack itemstack) {
		return type.getShowedName();
	}
	
	@SideOnly(Side.CLIENT)
	public ResourceLocation getRenderTexture() {
		return new ResourceLocation(Reference.ModID + ":textures/items/rotors/"
			+ this.getUnlocalizedName() + ".png");
	}
	
	public void tickRotor(ItemStack rotor, TileEntityGenerator tileEntity, World worldObj) {
		return;
	}
	
}