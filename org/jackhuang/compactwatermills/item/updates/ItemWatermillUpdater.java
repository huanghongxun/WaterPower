package org.jackhuang.compactwatermills.item.updates;

import java.util.List;

import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.Configuration;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.item.GlobalItems;
import org.jackhuang.compactwatermills.item.ItemBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemWatermillUpdater extends ItemBase {
	
	public ItemWatermillUpdater(Configuration config, InternalName name) {
		super(config, name);
		setHasSubtypes(true);

		GlobalItems.UpdaterIR_FE = new ItemStack(this, 1, UpdaterType.IR_FE.ordinal());
		GlobalItems.UpdaterMK0 = new ItemStack(this, 1, UpdaterType.MK0.ordinal());
		GlobalItems.UpdaterMK1 = new ItemStack(this, 1, UpdaterType.MK1.ordinal());
		GlobalItems.UpdaterMK2 = new ItemStack(this, 1, UpdaterType.MK2.ordinal());
		GlobalItems.UpdaterMK3 = new ItemStack(this, 1, UpdaterType.MK3.ordinal());
		GlobalItems.UpdaterWaterUraniumIngot = new ItemStack(this, 1, UpdaterType.WaterUraniumIngot.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK1 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK1.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK2 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK2.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK3 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK3.ordinal());
		GlobalItems.ReservoirCore = new ItemStack(this, 1, UpdaterType.RESERVOIR_CORE.ordinal());
		GlobalItems.ReservoirCoreAdvanced = new ItemStack(this, 1, UpdaterType.RESERVOIR_CORE_ADVANCED.ordinal());
	}
	
	@Override
	public String getTextureFolder() {
		return "updaters";
	}
	
	@Override
	public void addInformation(ItemStack par1ItemStack,
			EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
		par3List.add(UpdaterType.values()[par1ItemStack.getItemDamage()].information);
	}
	
	@Override
	public String getItemDisplayName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= UpdaterType.values().length) return null;
		return UpdaterType.values()[itemstack.getItemDamage()].showedName;
	}
	
	@Override
	public String getUnlocalizedName(ItemStack itemstack) {
		if(itemstack.getItemDamage() >= UpdaterType.values().length) return null;
		setUnlocalizedName(UpdaterType.values()[itemstack.getItemDamage()].unlocalizedName);
		return super.getUnlocalizedName();
	}
}
