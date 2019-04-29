package com.supertechgroup.core.capabilities.ore;

import java.util.HashMap;

import net.minecraft.util.math.MathHelper;

public interface IOreCapability {
	static final int NUM_X_BITS = 1 + MathHelper.log2(MathHelper.smallestEncompassingPowerOfTwo(16));
	static final int NUM_Z_BITS = NUM_X_BITS;
	static final int NUM_Y_BITS = 64 - NUM_X_BITS - NUM_Z_BITS;
	static final int Y_SHIFT = 0 + NUM_Z_BITS;
	static final int X_SHIFT = Y_SHIFT + NUM_Y_BITS;
	static final long X_MASK = (1L << NUM_X_BITS) - 1L;
	static final long Y_MASK = (1L << NUM_Y_BITS) - 1L;
	static final long Z_MASK = (1L << NUM_Z_BITS) - 1L;

	public void setData(int x, int y, int z, Object[] data);

	/**
	 * Returns the ore data for a specified position
	 * 
	 * @param x
	 * @param y
	 * @param z
	 * @return an object array. 0th element is the hardness, 1st element is the base
	 *         texture, following elements are ores present
	 */
	public Object[] getData(int x, int y, int z);

	public HashMap<Long, Object[]> getData();

	public void setData(HashMap<Long, Object[]> data);
	
	
}
