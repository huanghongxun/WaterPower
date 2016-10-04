/**
 * Copyright (c) Huang Yuhui, 2014
 * 
 * "WaterCraft" is distributed under the terms of the Minecraft Mod Public
 * License 1.0, or MMPL. Please check the contents of the license located in
 * http://www.mod-buildcraft.com/MMPL-1.0.txt
 */

package waterpower.util;

import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import waterpower.WaterPower;
import waterpower.integration.ic2.ICItemFinder;

public class Utils {
    private Utils() {
    }

    public static <T> T[] concat(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static int[] concat(int[] first, int[] second) {
        int[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static float[] concat(float[] first, float[] second) {
        float[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static boolean isITNT(ItemStack is) {
        if (!Mods.IndustrialCraft2.isAvailable)
            return false;
        ItemStack b = ICItemFinder.getItem("te,itnt");
        if (b.getItem() == is.getItem() && b.getItemDamage() == is.getItemDamage())
            return true;
        else
            return false;
    }

    // 0 - weather, 1 - biomeGet, 2 - biomePut, BlockPos needn't coord y
    public static double[] getBiomeRaining(World worldObj, BlockPos pos) {
        int weather = worldObj.isThundering() ? 2 : worldObj.isRaining() ? 1 : 0;
        String biomeID = worldObj.getBiomeGenForCoords(pos).getBiomeName();
        double biomeGet = 0, biomePut = 0;
        switch (biomeID) {
        case "beach":
        case "river":
        case "taiga":
        case "taigaHills":
        case "forestHills":
        	biomeGet = 1; biomePut = 0.75; break;
        case "forest":
        	biomeGet = 1; biomePut = 1; break;
        case "plains":
        case "extremeHills":
        case "extremeHillsEdge":
        	biomeGet = 0.75; biomePut = 1; break;
        case "mushroomIsland":
        case "mushroomIslandShore":
        case "ocean":
        	biomeGet = 1.2; biomePut = 0.75; break;
        case "desertHills":
        case "hell":
        case "desert":
        	biomeGet = 0; biomePut = 4; break;
        case "frozenOcean":
        case "frozenRiver":
        	biomeGet = 1.2; biomePut = 0.5; break;
        case "icePlains":
        case "iceMountains":
        	biomeGet = 1; biomePut = 0.5; break;
        case "jungleHills":
        case "jungle":
        	biomeGet = 1.5; biomePut = 0.5; break;
        case "swampland":
        	biomeGet = 1.2; biomePut = 0.75; break;
        }

        return new double[] { weather, biomeGet, biomePut };
    }

    public static boolean isWater(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block == Blocks.WATER || block == Blocks.FLOWING_WATER;
    }

    public static boolean isLava(World world, BlockPos pos) {
        Block block = world.getBlockState(pos).getBlock();
        return block == Blocks.LAVA || block == Blocks.FLOWING_LAVA;
    }

    public static final Random rand = new Random();
    public static final DecimalFormat DEFAULT_DECIMAL_FORMAT = new DecimalFormat("#.00");

    public static void dropItems(World world, BlockPos pos, List<ItemStack> drops) {
        if (WaterPower.isServerSide()) {
            for (ItemStack item : drops) {
                if (item != null && item.stackSize > 0) {
                    float rx = rand.nextFloat() * 0.8F + 0.1F;
                    float ry = rand.nextFloat() * 0.8F + 0.1F;
                    float rz = rand.nextFloat() * 0.8F + 0.1F;

                    EntityItem entityItem = new EntityItem(world, pos.getX() + rx, pos.getY() + ry, pos.getZ() + rz, item.copy());

                    float factor = 0.05F;
                    entityItem.motionX = (rand.nextGaussian() * factor);
                    entityItem.motionY = (rand.nextGaussian() * factor + 0.2);
                    entityItem.motionZ = (rand.nextGaussian() * factor);
                    world.spawnEntityInWorld(entityItem);
                }
            }
        }
    }

    public static int convertFacingAndForgeDirection(int facing) {
        return facing + (facing % 2 * -2 + 1);
    }
}
