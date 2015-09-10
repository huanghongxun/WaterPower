package org.jackhuang.watercraft.common.item.range;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

import org.jackhuang.watercraft.common.item.ItemBase;

public class ItemRange extends ItemBase {

    public ItemRange() {
        super("cptItemRange");
        setHasSubtypes(true);
    }

    @Override
    public String getTextureFolder() {
        return "range";
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(RangeType.values()[par1ItemStack.getItemDamage()].getInfo());
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= RangeType.values().length)
            return null;
        return RangeType.values()[itemstack.getItemDamage()].getShowedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= RangeType.values().length)
            return null;
        return "item." + RangeType.values()[itemstack.getItemDamage()].unlocalizedName;
    }
}