package waterpower.common.block.machines;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import waterpower.client.Local;
import net.minecraft.client.resources.I18n;

public class ItemMachines extends ItemBlock {

    public ItemMachines(Block blockMachines) {
        super(blockMachines);

        setHasSubtypes(true);
    }

    @Override
    public int getMetadata(int par1) {
        if (par1 > 7)
            return 0;
        return par1;
    }

    @Override
    public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List list, boolean par4) {
        list.add(Local.get("cptwtrml.machine.info"));
        int metadata = par1ItemStack.getItemDamage();
        switch (metadata) {
        case 1:
            list.add("80mb/t, 60s, tot: 96000mb, storage: 96000mb");
            break;
        case 2:
            list.add("2000mb/t, 2s, tot: 80000mb, storage: 80000mb");
            break;
        case 3:
            list.add("1000mb/t, 1s, tot: 20000mb, storage: 20000mb");
            break;
        case 4:
            list.add("5000mb/t, (itnt)s, tot: ?, storage: 1000000mb");
            break;
        case 5:
            list.add("500*(gt)mb/t, 0.2*(gt)s, tot: ?, storage: 1000000mb");
            break;
        case 6:
            list.add("8000mb/t, 1s, tot: 160000mb, storage: 160000mb");
            break;
        case 7:
            list.add("8000mb/t, 1s, tot: 160000mb, storage: 160000mb");
            break;
        default:
            break;
        }
    }

    @Override
    public String getUnlocalizedName(ItemStack par1ItemStack) {
        int metadata = par1ItemStack.getItemDamage();
        switch (metadata) {
        case 1:
            return "cptwtrml.machine.macerator";
        case 2:
            return "cptwtrml.machine.compressor";
        case 3:
            return "cptwtrml.machine.sawmill";
        case 4:
            return "cptwtrml.machine.advancedCompressor";
        case 5:
            return "cptwtrml.machine.centrifuge";
        case 6:
            return "cptwtrml.machine.lathe";
        case 7:
            return "cptwtrml.machine.cutter";
        default:
            return null;
        }
    }

}
