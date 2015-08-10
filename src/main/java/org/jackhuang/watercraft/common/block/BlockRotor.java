package org.jackhuang.watercraft.common.block;

import org.jackhuang.watercraft.common.block.tileentity.TileEntityRotor;
import org.jackhuang.watercraft.common.item.rotors.ItemRotor;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public abstract class BlockRotor extends BlockMeta {

    public BlockRotor(String id, Material material, Class<? extends ItemBlock> c) {
        super(id, material, c);
    }

    @Override
    public boolean onBlockActivated(World world, int x, int y, int z,
            EntityPlayer entityPlayer, int s, float f1, float f2, float f3) {
        TileEntity te = world.getTileEntity(x, y, z);
        if (te instanceof TileEntityRotor) {
            TileEntityRotor ter = (TileEntityRotor) te;
            ItemStack is = entityPlayer.inventory.getCurrentItem();
            ItemStack now = ter.slotRotor.get();
            if (is != null && is.getItem() instanceof ItemRotor) {
                ter.slotRotor.put(is);
                int id = entityPlayer.inventory.currentItem;
                if(0 <= id && id < 9) entityPlayer.inventory.mainInventory[id] = null;
                if (now != null)
                    entityPlayer.inventory.addItemStackToInventory(now);
                return true;
            } else if (is == null && entityPlayer.isSneaking()) {
                ter.slotRotor.put(null);
                if (now != null)
                    entityPlayer.inventory.addItemStackToInventory(now);
            }
        }
        return super.onBlockActivated(world, x, y, z, entityPlayer, s, f1, f2,
                f3);
    }
}
