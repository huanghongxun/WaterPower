package org.jackhuang.watercraft.common.block.reservoir;

import java.util.List;

import org.jackhuang.watercraft.common.item.ItemMeta;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

public class ItemReservoir extends ItemMeta {

    public ItemReservoir(Block id) {
	super(id);
	setMaxDamage(0);
	setHasSubtypes(true);
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer,
	    List par3List, boolean par4) {
	par3List.add(StatCollector.translateToLocal("cptwtrml.reservoir.info"));
	par3List.add(StatCollector.translateToLocal("cptwtrml.reservoir.info2"));
	ReservoirType t = ReservoirType.values()[par1ItemStack.getItemDamage()];
	par3List.add(StatCollector.translateToLocal("cptwtrml.reservoir.MAXSOTRE") + ": " + t.capacity + "mb");
	par3List.add(StatCollector.translateToLocal("cptwtrml.reservoir.MAXUSE") + ": " + t.maxUse + "mb/s");
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
	return ReservoirType.values()[par1ItemStack.getItemDamage()].getShowedName();
    }

    @Override
    public int getMetadata(int i) {
	if (i < ReservoirType.values().length) {
	    return i;
	} else {
	    return 0;
	}
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
	if (itemstack.getItemDamage() >= ReservoirType.values().length) {
	    return null;
	}
	return ReservoirType.values()[itemstack.getItemDamage()].name();
    }
}
