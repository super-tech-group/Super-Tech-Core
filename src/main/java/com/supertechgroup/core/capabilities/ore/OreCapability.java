package com.supertechgroup.core.capabilities.ore;

import java.util.HashMap;

import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

public class OreCapability implements IOreCapability {
	HashMap<Long, Object[]> map = new HashMap<>();
	public static final Object[] empty = new Object[] { 1.0f, new ResourceLocation("minecraft:blocks/stone") };

	@Override
	public void setData(int x, int y, int z, Object[] data) {
		long hash = ((long) x & X_MASK) << X_SHIFT | ((long) y & Y_MASK) << Y_SHIFT | ((long) z & Z_MASK) << 0;
		map.put(hash, data);
	}

	@Override
	public Object[] getData(int x, int y, int z) {
		long hash = ((long) x & X_MASK) << X_SHIFT | ((long) y & Y_MASK) << Y_SHIFT | ((long) z & Z_MASK) << 0;
		return map.getOrDefault(hash, empty.clone());
	}

	@Override
	public HashMap<Long, Object[]> getData() {
		return map;
	}

	@Override
	public void setData(HashMap<Long, Object[]> data) {
		map = data;
	}

	@Override
	public float getHardness(BlockPos pos) {
		return (float) getData(pos.getX(), pos.getY(), pos.getZ())[0];
	}

	@Override
	public ResourceLocation getBase(BlockPos pos) {
		return (ResourceLocation) getData(pos.getX(), pos.getY(), pos.getZ())[1];
	}

	@Override
	public ResourceLocation[] getOres(BlockPos pos) {
		Object[] get = getData(pos.getX(), pos.getY(), pos.getZ());
		ResourceLocation[] ret = new ResourceLocation[get.length - 2];
		if (ret.length == 0) {
			return ret;
		}
		for (int i = 0; i < ret.length; i++) {
			ret[i] = (ResourceLocation) get[i + 2];
		}
		return ret;
	}

	@Override
	public void setOres(BlockPos pos, ResourceLocation[] ores) {
		Object[] get = getData(pos.getX(), pos.getY(), pos.getZ());
		Object[] data = new Object[ores.length + 2];
		data[0] = get[0];
		data[1] = get[1];
		for (int i = 0; i < ores.length; i++) {
			data[i + 2] = ores[i];
		}
		setData(pos.getX(), pos.getY(), pos.getZ(), data);
	}

	@Override
	public boolean isGenerated() {
		return !map.isEmpty();
	}
}
