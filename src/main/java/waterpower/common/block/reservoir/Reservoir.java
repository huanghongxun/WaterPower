package waterpower.common.block.reservoir;

import java.util.ArrayList;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import waterpower.common.block.tileentity.TileEntityMultiBlock;
import waterpower.util.Pair;
import waterpower.util.Utils;

public class Reservoir {
    @Override
    public String toString() {
        return "Reservoir [width=" + width + ", height=" + height + ", length=" + length + ", blockType=" + blockType + "]";
    }

    public int width, height, length, nonAirBlock;
    public boolean valid;
    private ReservoirType blockType;

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public int getLength() {
        return length;
    }

    public ReservoirType getBlockType() {
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
        return getCapacity() * blockType.capacity;
    }

    public static boolean isRes(World world, BlockPos pos) {
        return world.getBlockState(pos).getBlock() instanceof BlockReservoir;
    }

    public static boolean isRes(World world, BlockPos pos, ReservoirType type) {
        TileEntity tileEntity = world.getTileEntity(pos);
        if (tileEntity instanceof TileEntityReservoir) {
            TileEntityReservoir te = (TileEntityReservoir) tileEntity;
            if (te.getType() == type)
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
    public static ArrayList<BlockPos> getNotVerticalWall(World world, int x, int y, int z, int width, int height, ReservoirType meta) {
        int i, j;
        ArrayList<BlockPos> aList = new ArrayList<BlockPos>();
        for (i = z; i <= z + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
            	BlockPos pos = new BlockPos(x, j, i);
                if (!isRes(world, pos, meta))
                    aList.add(pos);
            }
        return aList;
    }

    // WWWW
    // W W
    // W W
    // OWWW
    public static ArrayList<BlockPos> getNotCircleWall(World world, int x, int y, int z, int length, int width, ReservoirType meta) {
        int i;
        ArrayList<BlockPos> aList = new ArrayList<BlockPos>();
        for (i = z; i <= z + width - 1; i++) {
        	BlockPos pos = new BlockPos(x, y, i);
            if (!isRes(world, pos, meta))
                aList.add(pos);
            pos = pos.add(length - 1, 0, 0);
            if (!isRes(world, pos, meta))
                aList.add(pos);
        }
        for (i = x + 1; i < x + width - 1; i++) {
        	BlockPos pos = new BlockPos(i, y, z);
            if (!isRes(world, pos, meta))
                aList.add(pos);
            pos = pos.add(0, 0, width - 1);
            if (!isRes(world, pos, meta))
                aList.add(pos);
        }
        return aList;
    }

    // | W -- wall block
    // OWWW | T -- turbine
    // | O -- begin to test(x, y, z)
    public static ArrayList<BlockPos> getNotHorizontalWall(World world, int x, int y, int z, int width, int height, ReservoirType meta) {
        int i, j;
        ArrayList<BlockPos> aList = new ArrayList<BlockPos>();
        for (i = x; i <= x + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
            	BlockPos pos = new BlockPos(i, j, z);
                if (!isRes(world, pos, meta))
                    aList.add(pos);
            }
        return aList;
    }

    // WWW
    // WWW
    // OWW
    public static ArrayList<BlockPos> getNotFloor(World world, int x, int y, int z, int length, int width, ReservoirType meta) {
        int i, j;
        ArrayList<BlockPos> aList = new ArrayList<BlockPos>();
        for (i = x; i <= x + length - 1; i++)
            for (j = z; j <= z + width - 1; j++) {
            	BlockPos pos = new BlockPos(i, y, j);
                if (!isRes(world, pos, meta))
                    aList.add(pos);
            }
        return aList;
    }

    // W | W -- wall block
    // W | T -- turbine
    // W | O -- begin to test(x, y, z)
    // O
    public static ArrayList<TileEntityMultiBlock> getVerticalWall(World world, int x, int y, int z, int width, int height, ReservoirType meta) {
        int i, j;
        ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
        for (i = z; i <= z + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
            	BlockPos pos = new BlockPos(x, j, i);
                aList.add((TileEntityMultiBlock) world.getTileEntity(pos));
            }
        return aList;
    }

    // W | W -- wall block
    // W | T -- turbine
    // W | O -- begin to test(x, y, z)
    // O
    public static Pair getVerticalWallWater(World world, int x, int y, int z, int width, int height, ReservoirType meta) {
        int i, j, k = 0, l = 0;
        for (i = z; i <= z + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
            	BlockPos pos = new BlockPos(x, j, i);
                if (Utils.isWater(world, pos))
                    if ("ocean".equals(world.getBiomeGenForCoords(pos).getBiomeName()))
                        k++;
                    else
                        l++;
            }
        return new Pair(k, l);
    }

    // | W -- wall block
    // OWWW | T -- turbine
    // | O -- begin to test(x, y, z)
    public static ArrayList<TileEntityMultiBlock> getHorizontalWall(World world, int x, int y, int z, int width, int height, ReservoirType meta) {
        int i, j;
        ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
        for (i = x; i <= x + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
            	BlockPos pos = new BlockPos(i, j, z);
                aList.add((TileEntityMultiBlock) world.getTileEntity(pos));
            }
        return aList;
    }

    // | W -- wall block
    // OWWW | T -- turbine
    // | O -- begin to test(x, y, z)
    public static Pair getHorizontalWallWater(World world, int x, int y, int z, int width, int height, ReservoirType meta) {
        int i, j, k = 0, l = 0;
        for (i = x; i <= x + width - 1; i++)
            for (j = y; j <= y + height - 1; j++) {
            	BlockPos pos = new BlockPos(i, j, z);
                if (Utils.isWater(world, pos))
                    if ("ocean".equals(world.getBiomeGenForCoords(pos).getBiomeName()))
                        k++;
                    else
                        l++;
            }
        return new Pair(k, l);
    }

    // WWW
    // WWW
    // OWW
    public static ArrayList<TileEntityMultiBlock> getFloor(World world, int x, int y, int z, int length, int width, ReservoirType meta) {
        int i, j;
        ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
        for (i = x; i <= x + length - 1; i++)
            for (j = z; j <= z + width - 1; j++) {
            	BlockPos pos = new BlockPos(i, y, j);
                aList.add((TileEntityMultiBlock) world.getTileEntity(pos));
            }
        return aList;
    }

    // WWW
    // WWW
    // OWW
    public static Pair getFloorWater(World world, int x, int y, int z, int length, int width, ReservoirType meta) {
        int i, j, k = 0, l = 0;
        for (i = x; i <= x + length - 1; i++)
            for (j = z; j <= z + width - 1; j++) {
            	BlockPos pos = new BlockPos(i, y, j);
                if (Utils.isWater(world, pos))
                    if ("ocean".equals(world.getBiomeGenForCoords(pos).getBiomeName()))
                        k++;
                    else
                        l++;
            }
        return new Pair(k, l);
    }

    // WWWW
    // W W
    // W W
    // OWWW
    public static ArrayList<TileEntityMultiBlock> getCircleWall(World world, int x, int y, int z, int length, int width, ReservoirType meta) {
        int i;
        ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
        for (i = z; i <= z + width - 1; i++) {
        	BlockPos pos = new BlockPos(x, y, i);
            aList.add((TileEntityMultiBlock) world.getTileEntity(pos));
            aList.add((TileEntityMultiBlock) world.getTileEntity(pos.add(length - 1, 0, 0)));
        }
        for (i = x + 1; i < x + width - 1; i++) {
        	BlockPos pos = new BlockPos(i, y, z);
            aList.add((TileEntityMultiBlock) world.getTileEntity(pos));
            aList.add((TileEntityMultiBlock) world.getTileEntity(pos.add(0, 0, width - 1)));
        }
        return aList;
    }

    public static int getNonAirBlock(World world, int x, int y, int z, int length, int width, int height) {
        int i, j, k, nonAirBlock = 0;
        for (i = x + 1; i < x + length - 1; i++)
            for (j = z + 1; j < z + width - 1; j++) {
                for (k = y + 1; k < y + height - 1; k++)
                    if (!world.isAirBlock(new BlockPos(i, k, j)))
                        nonAirBlock++;
            }
        return nonAirBlock;
    }

    public static int getCoverBlock(World world, int x, int y, int z, int length, int width) {
        int i, j, k, nonAirBlock = 0;
        for (i = x + 1; i < x + length - 1; i++)
            for (j = z + 1; j < z + width - 1; j++) {
                for (k = y; k < 256; k++)
                    if (!world.isAirBlock(new BlockPos(i, k, j))) {
                        nonAirBlock++;
                        break;
                    }
            }
        return nonAirBlock;
    }
}
