package org.jackhuang.compactwatermills.util;

import java.util.Arrays;

import net.minecraft.world.World;

public class Utilities {
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
	
	  public static int[] moveForward(World world, int xCoord, int yCoord, int zCoord, short facing, int step)
	  {
	    int x = xCoord;
	    int y = yCoord;
	    int z = zCoord;

	    switch (facing) {
	    case 1:
	      y += step;
	      break;
	    case 2:
	      y -= step;
	      break;
	    case 3:
	      z += step;
	      break;
	    case 4:
	      z -= step;
	      break;
	    case 5:
	      x += step;
	      break;
	    case 6:
	      x -= step;
	      break;
	    }

	    return new int[] { x, y, z };
	  }
}
