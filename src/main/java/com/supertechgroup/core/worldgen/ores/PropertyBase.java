package com.supertechgroup.core.worldgen.ores;

import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.property.IUnlistedProperty;

/**
 * Property used to store a string
 *
 * @author oa10712
 */
public class PropertyBase implements IUnlistedProperty<ResourceLocation> {

	private final String name;

	public PropertyBase(String name) {
		this.name = name;
	}

	@Override
	public String getName() {
		return name;
	}

	@Override
	public Class<ResourceLocation> getType() {
		return ResourceLocation.class;
	}

	@Override
	public boolean isValid(ResourceLocation value) {
		return true;
	}

	@Override
	public String valueToString(ResourceLocation value) {
		return value.toString();
	}
}