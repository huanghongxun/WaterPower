package waterpower.common;


import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.IFluidHandler;
import waterpower.util.StackUtil;

public class Tanks {

    /** Deactivate constructor */

    private Tanks() {}

    public static boolean handleRightClick(IFluidHandler tank, EnumFacing side, EntityPlayer player, boolean fill, boolean drain) {
        if (player == null || tank == null) {
            return false;
        }
        ItemStack current = player.inventory.getCurrentItem();
        if (current != null) {

            FluidStack liquid = FluidContainerRegistry.getFluidForFilledItem(current);

            if (fill && liquid != null) {
                int used = tank.fill(side, liquid, true);

                if (used > 0) {
                    if (!player.capabilities.isCreativeMode) {
                        player.inventory.setInventorySlotContents(player.inventory.currentItem, StackUtil.consumeItem(current));
                        player.inventory.markDirty();
                    }
                    return true;
                }

            } else if (drain) {

                FluidStack available = tank.drain(side, Integer.MAX_VALUE, false);
                if (available != null) {
                    ItemStack filled = FluidContainerRegistry.fillFluidContainer(available, current);

                    liquid = FluidContainerRegistry.getFluidForFilledItem(filled);
                    if (liquid != null) {

                        if (current.stackSize > 1) {
                            if (!player.inventory.addItemStackToInventory(filled)) {
                                return false;
                            }
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, StackUtil.consumeItem(current));
                            player.inventory.markDirty();
                        } else {
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, StackUtil.consumeItem(current));
                            player.inventory.setInventorySlotContents(player.inventory.currentItem, filled);
                            player.inventory.markDirty();
                        }

                        tank.drain(side, liquid.amount, true);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public static Block getFluidBlock(Fluid fluid, boolean moving) {
        if (fluid == FluidRegistry.WATER) {
            return moving ? Blocks.FLOWING_WATER : Blocks.WATER;
        }
        if (fluid == FluidRegistry.LAVA) {
            return moving ? Blocks.FLOWING_LAVA : Blocks.LAVA;
        }
        return fluid.getBlock();
    }
}
