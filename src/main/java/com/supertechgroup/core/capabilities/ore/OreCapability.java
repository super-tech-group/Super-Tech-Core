package com.supertechgroup.core.capabilities.ore;

import java.util.HashMap;

public class OreCapability implements IOreCapability {

	HashMap<Long, Object[]> map = new HashMap<>();
	public static final Object[] empty = new Object[] { 1.0f };

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
}
