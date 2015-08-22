package org.jackhuang.watercraft.common.block.watermills;

import java.util.List;

import org.jackhuang.watercraft.common.item.ItemMeta;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.util.StatCollector;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class ItemWatermill extends ItemMeta {

    public ItemWatermill(int block) {
        super(block);
        setMaxDamage(0);
        setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack par1ItemStack,
            EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(StatCollector
                .translateToLocal("cptwtrml.watermill.max_output")
                + ": "
                + WaterType.values()[par1ItemStack.getItemDamage()].output
                + "EU/t");
    }

    @Override
    public String getItemDisplayName(ItemStack par1ItemStack) {
        return WaterType.values()[par1ItemStack.getItemDamage()]
                .getShowedName();
    }

    @Override
    public int getMetadata(int i) {
        if (i < WaterType.values().length) {
            return i;
        } else {
            return 0;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= WaterType.values().length)
            return null;
        return WaterType.values()[itemstack.getItemDamage()].name();
    }

}
