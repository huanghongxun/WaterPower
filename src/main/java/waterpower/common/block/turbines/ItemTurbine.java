package waterpower.common.block.turbines;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

/**
 * 
 * @author jackhuang1998
 * 
 */
public class ItemTurbine extends ItemBlock {

    public ItemTurbine(Block id) {
        super(id);
        setHasSubtypes(true);
    }

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {
        par3List.add(I18n.format("cptwtrml.machine.info"));
        par3List.add(I18n.format("cptwtrml.watermill.max_output") + ": " + TurbineType.values()[par1ItemStack.getItemDamage()].percent
                + "EU/t");
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        return TurbineType.values()[par1ItemStack.getItemDamage()].getShowedName();
    }

    @Override
    public int getMetadata(int i) {
        if (i < TurbineType.values().length)
            return i;
        else
            return 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= TurbineType.values().length)
            return null;
        return TurbineType.values()[itemstack.getItemDamage()].unlocalizedName;
    }
}
