package waterpower.common.block.ore;

import net.minecraft.block.Block;
import net.minecraft.client.resources.I18n;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import waterpower.common.item.crafting.MaterialTypes;

public class ItemBlockMaterial extends ItemBlock {

    public ItemBlockMaterial(Block par1) {
        super(par1);

        setHasSubtypes(true);
    }

    @Override
    public String getItemStackDisplayName(ItemStack par1ItemStack) {
        return MaterialTypes.values()[par1ItemStack.getItemDamage()].getShowedName() + " " + I18n.format("cptwtrml.forms.block");
    }

    @Override
    public int getMetadata(int i) {
        if (i < MaterialTypes.values().length)
            return i;
        else
            return 0;
    }

    @Override
    public String getUnlocalizedName(ItemStack itemstack) {
        if (itemstack.getItemDamage() >= MaterialTypes.values().length)
            return null;
        return MaterialTypes.values()[itemstack.getItemDamage()].getShowedName();
    }

}
