package waterpower.common.block;

import javax.annotation.Nullable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.world.World;
import waterpower.common.block.tileentity.TileEntityRotor;
import waterpower.common.item.rotor.ItemRotor;

public abstract class BlockRotor extends BlockWaterPower {

    public BlockRotor(String id, Material material, Class<? extends ItemBlock> c) {
        super(id, material, c);
    }

    @Override
    public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer entityPlayer, EnumHand hand, @Nullable ItemStack heldItem, EnumFacing side, float hitX, float hitY, float hitZ) {
        TileEntity te = world.getTileEntity(pos);
        if (te instanceof TileEntityRotor) {
            TileEntityRotor ter = (TileEntityRotor) te;
            ItemStack now = ter.slotRotor.get();
            if (heldItem != null && heldItem.getItem() instanceof ItemRotor) {
                ter.slotRotor.put(heldItem);
                int id = entityPlayer.inventory.currentItem;
                if (0 <= id && id < 9)
                    entityPlayer.inventory.mainInventory[id] = null;
                if (now != null)
                    entityPlayer.inventory.addItemStackToInventory(now);
                return true;
            } else if (heldItem == null && entityPlayer.isSneaking()) {
                ter.slotRotor.put(null);
                if (now != null)
                    entityPlayer.inventory.addItemStackToInventory(now);
            }
        }
        return super.onBlockActivated(world, pos, state, entityPlayer, hand, heldItem, side, hitX, hitY, hitZ);
    }
}
