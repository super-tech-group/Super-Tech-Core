package com.supertechgroup.core.worldgen.ores;

import java.util.Arrays;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Property used to store a list of ores
 * @author oa10712
 */
public class PropertyOres implements IUnlistedProperty<ResourceLocation[]> {

	private final String name;

	public PropertyOres(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<ResourceLocation[]> getType() {
		return ResourceLocation[].class;
	}

	@Override
	public boolean isValid(ResourceLocation[] value) {
		return true;
	}

	@Override
	public String valueToString(ResourceLocation[] value) {
		return Arrays.toString(value);
	}
}