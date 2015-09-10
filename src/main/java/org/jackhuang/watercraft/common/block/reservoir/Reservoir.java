package org.jackhuang.watercraft.common.block.reservoir;

import java.beans.IntrospectionException;
import java.util.ArrayList;

import joptsimple.util.KeyValuePair;

import org.jackhuang.watercraft.common.block.tileentity.TileEntityMultiBlock;
import org.jackhuang.watercraft.util.Pair;
import org.jackhuang.watercraft.util.Position;
import org.jackhuang.watercraft.util.Utils;

import net.minecraft.block.material.Material;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.chunk.Chunk;

public class Reservoir {
    @Override
    public String toString() {
        return "Reservoir [width=" + width + ", height=" + height + ", length=" + length + ", blockType=" + blockType + "]";
    }

    private int width, height, length, blockType, nonAirBlock;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public int getBlockType() {
        return blockType;
    }

    public int getNonAirBlock() {
        return nonAirBlock;
    }

    public Reservoir() {
    }

    public Reservoir(int length, int width, int height, int nonAirBlock) {
        this.length = length;
        this.width = width;
        this.height = height;
        this.nonAirBlock = nonAirBlock;
    }

    public int getCapacity() {
        return (length - 2) * (width - 2) * (height - 1) - nonAirBlock;
    }

    public double getMaxWater() {
        return getCapacity() * ReservoirType.values()[blockType].capacity;
    }

    public static boolean isRes(World world, int x, int y, int z) {
        return world.getBlock(x, y, z) instanceof BlockReservoir;
    }

    public static boolean isRes(World world, int x, int y, int z, int type) {
        TileEntity tileEntity = world.getTileEntity(x, y, z);
        if (tileEntity instanceof TileEntityReservoir) {
            TileEntityReservoir te = (TileEntityReservoir) tileEntity;
            if (te.type == null)
                return type == 0;
            if (te.type.ordinal() == type)
                return true;
        }
        return false;
        // return world.getBlockId(x, y, z) == BlockReservoir.id
        // && world.getBlockMetadata(x, y, z) == type;
    }

    /*
     * ???????? L facing ???? 2 3 ???? 0 2 ???? 1 5 ???? 3 4
     */

    // W | W -- wall block
    // W | T -- turbine
    // W | O -- begin to test(x, y, z)
    // O
    public static ArrayList<Position> getNotVerticalWall(World world, int x, int y, int z, int width, int height, int meta) {
        int i, j;
        ArrayList<Position> aList = new ArrayList<Position>();
        for (i = z; i <= z + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
                if (!isRes(world, x, j, i, meta))
                    aList.add(new Position(x, j, i));
            }
        return aList;
    }

    // WWWW
    // W W
    // W W
    // OWWW
    public static ArrayList<Position> getNotCircleWall(World world, int x, int y, int z, int length, int width, int meta) {
        int i;
        ArrayList<Position> aList = new ArrayList<Position>();
        for (i = z; i <= z + width - 1; i++) {
            if (!isRes(world, x, y, i, meta))
                aList.add(new Position(x, y, i));
            if (!isRes(world, x + length - 1, y, i, meta))
                aList.add(new Position(x + length - 1, y, i));
        }
        for (i = x + 1; i < x + width - 1; i++) {
            if (!isRes(world, i, y, z, meta))
                aList.add(new Position(i, y, z));
            if (!isRes(world, i, y, z + width - 1, meta))
                aList.add(new Position(i, y, z + width - 1));
        }
        return aList;
    }

    // | W -- wall block
    // OWWW | T -- turbine
    // | O -- begin to test(x, y, z)
    public static ArrayList<Position> getNotHorizontalWall(World world, int x, int y, int z, int width, int height, int meta) {
        int i, j;
        ArrayList<Position> aList = new ArrayList<Position>();
        for (i = x; i <= x + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
                if (!isRes(world, i, j, z, meta))
                    aList.add(new Position(i, j, z));
            }
        return aList;
    }

    // WWW
    // WWW
    // OWW
    public static ArrayList<Position> getNotFloor(World world, int x, int y, int z, int length, int width, int meta) {
        int i, j;
        ArrayList<Position> aList = new ArrayList<Position>();
        for (i = x; i <= x + length - 1; i++)
            for (j = z; j <= z + width - 1; j++) {
                if (!isRes(world, i, y, j, meta))
                    aList.add(new Position(i, y, j));
            }
        return aList;
    }

    // W | W -- wall block
    // W | T -- turbine
    // W | O -- begin to test(x, y, z)
    // O
    public static ArrayList<TileEntityMultiBlock> getVerticalWall(World world, int x, int y, int z, int width, int height, int meta) {
        int i, j;
        ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
        for (i = z; i <= z + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
                aList.add((TileEntityMultiBlock) world.getTileEntity(x, j, i));
            }
        return aList;
    }

    // W | W -- wall block
    // W | T -- turbine
    // W | O -- begin to test(x, y, z)
    // O
    public static Pair getVerticalWallWater(World world, int x, int y, int z, int width, int height, int meta) {
        int i, j, k = 0, l = 0;
        for (i = z; i <= z + width - 1; i++)
            for (j = y; j <= y + height - 1; j++)
                if (Utils.isWater(world, x, j, i))
                    if (world.getBiomeGenForCoords(x, i).biomeID == BiomeGenBase.ocean.biomeID)
                        k++;
                    else
                        l++;
        return new Pair(k, l);
    }

    // | W -- wall block
    // OWWW | T -- turbine
    // | O -- begin to test(x, y, z)
    public static ArrayList<TileEntityMultiBlock> getHorizontalWall(World world, int x, int y, int z, int width, int height, int meta) {
        int i, j;
        ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
        for (i = x; i <= x + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
                aList.add((TileEntityMultiBlock) world.getTileEntity(i, j, z));
            }
        return aList;
    }

    // | W -- wall block
    // OWWW | T -- turbine
    // | O -- begin to test(x, y, z)
    public static Pair getHorizontalWallWater(World world, int x, int y, int z, int width, int height, int meta) {
        int i, j, k = 0, l = 0;
        for (i = x; i <= x + width - 1; i++)
            for (j = y; j <= y + height - 1; j++)
                if (Utils.isWater(world, i, j, z))
                    if (world.getBiomeGenForCoords(i, z).biomeID == BiomeGenBase.ocean.biomeID)
                        k++;
                    else
                        l++;
        return new Pair(k, l);
    }

    // WWW
    // WWW
    // OWW
    public static ArrayList<TileEntityMultiBlock> getFloor(World world, int x, int y, int z, int length, int width, int meta) {
        int i, j;
        ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
        for (i = x; i <= x + length - 1; i++)
            for (j = z; j <= z + width - 1; j++) {
                aList.add((TileEntityMultiBlock) world.getTileEntity(i, y, j));
            }
        return aList;
    }

    // WWW
    // WWW
    // OWW
    public static Pair getFloorWater(World world, int x, int y, int z, int length, int width, int meta) {
        int i, j, k = 0, l = 0;
        for (i = x; i <= x + length - 1; i++)
            for (j = z; j <= z + width - 1; j++)
                if (Utils.isWater(world, i, y, j))
                    if (world.getBiomeGenForCoords(i, j).biomeID == BiomeGenBase.ocean.biomeID)
                        k++;
                    else
                        l++;
        return new Pair(k, l);
    }

    // WWWW
    // W W
    // W W
    // OWWW
    public static ArrayList<TileEntityMultiBlock> getCircleWall(World world, int x, int y, int z, int length, int width, int meta) {
        int i;
        ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
        for (i = z; i <= z + width - 1; i++) {
            aList.add((TileEntityMultiBlock) world.getTileEntity(x, y, i));
            aList.add((TileEntityMultiBlock) world.getTileEntity(x + length - 1, y, i));
        }
        for (i = x + 1; i < x + width - 1; i++) {
            aList.add((TileEntityMultiBlock) world.getTileEntity(i, y, z));
            aList.add((TileEntityMultiBlock) world.getTileEntity(i, y, z + width - 1));
        }
        return aList;
    }

    public static int getNonAirBlock(World world, int x, int y, int z, int length, int width, int height) {
        int i, j, k, nonAirBlock = 0;
        for (i = x + 1; i < x + length - 1; i++)
            for (j = z + 1; j < z + width - 1; j++) {
                for (k = y + 1; k < y + height - 1; k++)
                    if (!world.isAirBlock(i, k, j))
                        nonAirBlock++;
            }
        return nonAirBlock;
    }

    public static int getCoverBlock(World world, int x, int y, int z, int length, int width) {
        int i, j, k, nonAirBlock = 0;
        for (i = x + 1; i < x + length - 1; i++)
            for (j = z + 1; j < z + width - 1; j++) {
                for (k = y; k < 256; k++)
                    if (!world.isAirBlock(i, k, j)) {
                        nonAirBlock++;
                        break;
                    }
            }
        return nonAirBlock;
    }
}
