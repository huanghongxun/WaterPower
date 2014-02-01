package gregtechmod.api.interfaces;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IOnItemClick {
	public boolean onItemUse(Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ);
	public boolean onItemUseFirst(Item aItem, ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int aSide, float hitX, float hitY, float hitZ);
    public ItemStack onItemRightClick(Item aItem, ItemStack aStack, World aWorld, EntityPlayer aPlayer);
}