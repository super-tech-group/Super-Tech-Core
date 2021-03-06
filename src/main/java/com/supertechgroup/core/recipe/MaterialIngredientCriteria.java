package com.supertechgroup.core.recipe;

import com.supertechgroup.core.metallurgy.Material;

public class MaterialIngredientCriteria {
	public enum Type {
		ABOVE, BELOW
	}

	Material.Property property;
	Type type;
	double value;

	public MaterialIngredientCriteria(Material.Property property, Type type, Double value) {
		this.property = property;
		this.type = type;
		this.value = value;
	}

	public boolean meetsCriteria(Material mat) {
		if (value == Double.NaN) {
			return false;
		}
		if (type == Type.ABOVE && mat.getProperty(property) < value) {
			return false;
		}
		if (type == Type.BELOW && mat.getProperty(property) > value) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return "{" + property.toString() + " " + type.toString() + " " + value + "}";

	}
}
