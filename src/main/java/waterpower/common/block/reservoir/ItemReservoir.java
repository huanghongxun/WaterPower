package waterpower.common.block.reservoir;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import waterpower.client.Local;

public class ItemReservoir extends ItemBlock {
    public ItemReservoir(Block id) {
        super(id);

        setHasSubtypes(true);
    }

    @Override
    public void addInformation(ItemStack s, EntityPlayer player, List par3List, boolean par4) {
        par3List.add(Local.get("cptwtrml.reservoir.info"));
        par3List.add(Local.get("cptwtrml.reservoir.info2"));
        ReservoirType t = ReservoirType.values()[s.getItemDamage()];
        par3List.add(Local.get("cptwtrml.reservoir.MAXSOTRE") + ": " + t.capacity + "mb");
        par3List.add(Local.get("cptwtrml.reservoir.MAXUSE") + ": " + t.maxUse + "mb/s");
    }

    @Override
    public String getItemStackDisplayName(ItemStack s) {
        return ReservoirType.values()[s.getItemDamage()].getShowedName();
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
    public String getUnlocalizedName(ItemStack s) {
        if (s.getItemDamage() >= ReservoirType.values().length)
            return null;
        return ReservoirType.values()[s.getItemDamage()].name();
    }
}
