package org.jackhuang.watercraft.common.item;

import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import org.jackhuang.watercraft.WaterPower;
import org.jackhuang.watercraft.common.block.tileentity.ITileEntityMeta;
import org.jackhuang.watercraft.common.block.tileentity.TileEntityElectricMetaBlock;

/**
 * @author hyh
 *
 */
public abstract class ItemMeta extends ItemBlock {

    public ItemMeta(Block id) {
        super(id);
    }

    @Override
    public boolean placeBlockAt(ItemStack aStack, EntityPlayer aPlayer, World aWorld, int aX, int aY, int aZ, int side, float hitX, float hitY, float hitZ, int aMeta) {
        Block block = Block.getBlockFromItem(this);
        int tDamage = aStack.getItemDamage();
        if (!aWorld.setBlock(aX, aY, aZ, block, 0, 3))
            return false;
        ITileEntityMeta tTileEntity = (ITileEntityMeta) aWorld.getTileEntity(aX, aY, aZ);
        if (tTileEntity != null) {
            if (WaterPower.isServerSide())
                tTileEntity.initNBT(aStack.getTagCompound(), tDamage);
            else {
                tTileEntity.initNBT(null, tDamage);
            }
        }

        if (aWorld.getBlock(aX, aY, aZ) == block) {
            block.onBlockPlacedBy(aWorld, aX, aY, aZ, aPlayer, aStack);
            block.onPostBlockPlaced(aWorld, aX, aY, aZ, tDamage);
        }
        return true;
    }

}