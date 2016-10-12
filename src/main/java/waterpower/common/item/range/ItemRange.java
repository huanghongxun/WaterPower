package waterpower.common.item.range;

import java.util.List;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import waterpower.client.render.IIconRegister;
import waterpower.client.render.item.IItemIconProvider;
import waterpower.common.item.ItemBase;

public class ItemRange extends ItemBase implements IItemIconProvider, IIconRegister {

    public ItemRange() {
        super("range");
        setHasSubtypes(true);
    }

    @Override
	@SideOnly(Side.CLIENT)
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