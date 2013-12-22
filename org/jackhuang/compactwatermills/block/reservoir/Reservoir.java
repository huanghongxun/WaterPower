package org.jackhuang.compactwatermills.block.reservoir;

import java.util.ArrayList;

import org.jackhuang.compactwatermills.block.turbines.Position;
import org.jackhuang.compactwatermills.helpers.LogHelper;
import org.jackhuang.compactwatermills.tileentity.TileEntityMultiBlock;

import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;

public class Reservoir {
	@Override
	public String toString() {
		return "Reservoir [width=" + width + ", height=" + height + ", length="
				+ length + ", blockType=" + blockType + "]";
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

	public Reservoir(){}
	
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
		return world.getBlockId(x, y, z) == BlockReservoir.id;
	}
	
	public static boolean isRes(World world, int x, int y, int z, int type) {
		return world.getBlockId(x, y, z) == BlockReservoir.id
				&& world.getBlockMetadata(x, y, z) == type;
	}
	
	/*
	方向 L facing
	南   0 2
	西   1 5
	北   2 3
	东   3 4*/

	
	//  W   | W -- wall block
	//  W   | T -- turbine
	//  W   | O -- begin to test(x, y, z)
	//  O
	public static ArrayList<Position> getNotVerticalWall(World world,
			int x, int y, int z,
			int width, int height, int meta) {
		int i, j;
		ArrayList<Position> aList = new ArrayList<Position>();
		for (i = z; i <= z + width - 1; i++)
			for (j = y; j <= y + height - 1; j++) {
				if (!isRes(world, x, j, i, meta))
					aList.add(new Position(x, j, i));
			}
		return aList;
	}

	//       | W -- wall block
	// OWWW  | T -- turbine
	//       | O -- begin to test(x, y, z)
	public static ArrayList<Position> getNotHorizontalWall(World world,
			int x, int y, int z,
			int width, int height, int meta) {
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
	public static ArrayList<Position> getNotFloor(World world,
			int x, int y, int z,
			int length, int width, int meta) {
		int i, j;
		ArrayList<Position> aList = new ArrayList<Position>();
		for (i = x; i <= x + length - 1; i++)
			for (j = z; j <= z + width - 1; j++) {
				if (!isRes(world, i, y, j, meta))
					aList.add(new Position(i, y, j));
			}
		return aList;
	}
	
	//  W   | W -- wall block
	//  W   | T -- turbine
	//  W   | O -- begin to test(x, y, z)
	//  O
	public static ArrayList<TileEntityMultiBlock> getVerticalWall(World world,
			int x, int y, int z,
			int width, int height, int meta) {
		int i, j;
		ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
		for (i = z; i <= z + width - 1; i++)
			for (j = y; j <= y + height - 1; j++) {
				aList.add((TileEntityMultiBlock)world.getBlockTileEntity(x, j, i));
			}
		return aList;
	}

	//       | W -- wall block
	// OWWW  | T -- turbine
	//       | O -- begin to test(x, y, z)
	public static ArrayList<TileEntityMultiBlock> getHorizontalWall(World world,
			int x, int y, int z,
			int width, int height, int meta) {
		int i, j;
		ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
		for (i = x; i <= x + width - 1; i++)
			for (j = y; j <= y + height - 1; j++) {
				aList.add((TileEntityMultiBlock)world.getBlockTileEntity(i, j, z));
			}
		return aList;
	}
	
	// WWW
	// WWW
	// OWW
	public static ArrayList<TileEntityMultiBlock> getFloor(World world,
			int x, int y, int z,
			int length, int width, int meta) {
		int i, j;
		ArrayList<TileEntityMultiBlock> aList = new ArrayList<TileEntityMultiBlock>();
		for (i = x; i <= x + length - 1; i++)
			for (j = z; j <= z + width - 1; j++) {
				aList.add((TileEntityMultiBlock)world.getBlockTileEntity(i, y, j));
			}
		return aList;
	}
	
	public static int getNonAirBlock(World world, int x, int y, int z,
			int length, int width, int height) {
		int i, j, k, nonAirBlock = 0;
		for (i = x + 1; i < x + length - 1; i++)
			for (j = z + 1; j < z + width - 1; j++) {
				for(k = y + 1; k < y + height - 1; k++)
					if(!world.isAirBlock(i, k, j)) nonAirBlock++;
			}
		return nonAirBlock;
	}
	
	public static int getCoverBlock(World world, int x, int y, int z,
			int length, int width) {
		int i, j, k, nonAirBlock = 0;
		for (i = x + 1; i < x + length - 1; i++)
			for (j = z + 1; j < z + width - 1; j++) {
				for(k = y; k < 256; k++)
					if(!world.isAirBlock(i, k, j)) {
						nonAirBlock++;
						break;
					}
			}
		return nonAirBlock;
	}
}
