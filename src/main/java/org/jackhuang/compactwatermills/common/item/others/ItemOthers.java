package org.jackhuang.compactwatermills.common.item.others;

import java.util.List;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

import org.jackhuang.compactwatermills.CompactWatermills;
import org.jackhuang.compactwatermills.InternalName;
import org.jackhuang.compactwatermills.Reference;
import org.jackhuang.compactwatermills.common.item.GlobalItems;
import org.jackhuang.compactwatermills.common.item.ItemBase;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class ItemOthers extends ItemBase {
	
	public ItemOthers() {
		super(InternalName.cptItemUpdaters);
		setHasSubtypes(true);

		/*try {
			for(UpdaterType type : UpdaterType.values()) {
				GlobalItems.class.getDeclaredField("Updater" + type.name()).set(
						GlobalItems.class,
						new ItemStack(this, 1, type.ordinal())
						);
			}
		} catch (Exception e) {
			throw new RuntimeException(Reference.ModName + " has raised an exception: failed to new an instance for updater items.", e);
		}*/
		/*GlobalItems.UpdaterIR_FE = new ItemStack(this, 1, UpdaterType.IR_FE.ordinal());
		GlobalItems.UpdaterMK0 = new ItemStack(this, 1, UpdaterType.MK0.ordinal());
		GlobalItems.UpdaterMK1 = new ItemStack(this, 1, UpdaterType.MK1.ordinal());
		GlobalItems.UpdaterMK2 = new ItemStack(this, 1, UpdaterType.MK2.ordinal());
		GlobalItems.UpdaterMK3 = new ItemStack(this, 1, UpdaterType.MK3.ordinal());
		GlobalItems.UpdaterMK4 = new ItemStack(this, 1, UpdaterType.MK4.ordinal());
		GlobalItems.UpdaterMK5 = new ItemStack(this, 1, UpdaterType.MK5.ordinal());
		GlobalItems.UpdaterMK6 = new ItemStack(this, 1, UpdaterType.MK6.ordinal());
		GlobalItems.UpdaterMK7 = new ItemStack(this, 1, UpdaterType.MK7.ordinal());
		GlobalItems.UpdaterWaterUraniumIngot = new ItemStack(this, 1, UpdaterType.WaterUraniumIngot.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK1 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK1.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK2 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK2.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK3 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK3.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK4 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK4.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK5 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK5.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK6 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK6.ordinal());
		GlobalItems.UpdaterWaterUraniumPlateMK7 = new ItemStack(this, 1, UpdaterType.WaterUraniumPlateMK7.ordinal());
		GlobalItems.ReservoirCore = new ItemStack(this, 1, UpdaterType.RESERVOIR_CORE.ordinal());
		GlobalItems.ReservoirCoreAdvanced = new ItemStack(this, 1, UpdaterType.RESERVOIR_CORE_ADVANCED.ordinal());
		GlobalItems.PlasmaUraniumIngot = new ItemStack(this, 1, UpdaterType.PlasmaUraniumIngot.ordinal());
		GlobalItems.PlasmaUraniumAlloyPlate = new ItemStack(this, 1, UpdaterType.PlasmaUraniumAlloyPlate.ordinal());
		GlobalItems.WaterUraniumAlloyPlate = new ItemStack(this, 1, UpdaterType.WaterUraniumAlloyPlate.ordinal());
		GlobalItems.BaseRotor = new ItemStack(this, 1, UpdaterType.BaseRotor.ordinal());*/
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
