package waterpower.common.item.other;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.client.render.IIconRegister;
import waterpower.client.render.item.IItemIconProvider;
import waterpower.common.item.ItemBase;

public class ItemOthers extends ItemBase implements IItemIconProvider, IIconRegister {

    public ItemOthers() {
        super("updater");
        setHasSubtypes(true);
    }

    @Override
    @SideOnly(Side.CLIENT)
    public String getTextureFolder() {
        return "updaters";
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        String s = ItemType.values()[par1ItemStack.getItemDamage()].getInformation();
        if (s != null)
            par3List.add(s);
    }

    @Override
    public String getItemStackDisplayName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= ItemType.values().length)
            return null;
        return ItemType.values()[itemstack.getItemDamage()].getShowedName();
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= ItemType.values().length)
            return null;

        return "item." + ItemType.values()[itemstack.getItemDamage()].unlocalizedName;
    }
}
