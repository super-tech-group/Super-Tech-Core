package com.supertechgroup.core.worldgen.rocks;

import java.util.HashMap;
import java.util.LinkedHashSet;

import net.minecraft.block.state.IBlockState;
import net.minecraft.util.ResourceLocation;

/**
 * This is essentially a Registry for the different kinds of rocks we use in ore
 * gen.
 * 
 * @author oa10712
 *
 */
public class RockManager {

	public static final LinkedHashSet<IBlockState> allStones = new LinkedHashSet<>();

	public static final HashMap<String, LinkedHashSet<IBlockState>> stoneSpawns = new HashMap<String, LinkedHashSet<IBlockState>>();

	public static final HashMap<IBlockState, ResourceLocation> textureOverrides = new HashMap<IBlockState, ResourceLocation>();

	public static void addRockTypes(IBlockState state, String... types) {
		allStones.add(state);
		for (String s : types) {
			if (stoneSpawns.containsKey(s)) {
				stoneSpawns.get(s).add(state);
			} else {
				LinkedHashSet<IBlockState> newType = new LinkedHashSet<IBlockState>();
				newType.add(state);
				stoneSpawns.put(s, newType);
			}
		}
	}

	/**
	 * Define a non-standard texture location for a block. The default is
	 * "MODID:blocks/Block_Name", but some mods have alternate resource structuring.
	 * As an example, the vanilla rock types need this because they are all
	 * technically different varieties of "stone".
	 * 
	 * @param state The blockstate to map a new texture to
	 * @param rl    The REsourceLocation of the new texture
	 */
	public static void addTextureOverride(IBlockState state, ResourceLocation rl) {
		textureOverrides.put(state, rl);
	}

	public static LinkedHashSet<IBlockState> getStones(String s) {
		LinkedHashSet<IBlockState> ret = new LinkedHashSet<IBlockState>();
		if (stoneSpawns.containsKey(s)) {
			ret.addAll(stoneSpawns.get(s));
		}
		return ret;
	}

	public static ResourceLocation getTexture(IBlockState state) {
		if (textureOverrides.containsKey(state)) {
			return textureOverrides.get(state);
		} else {
			ResourceLocation rl = new ResourceLocation(state.getBlock().getRegistryName().getResourceDomain(),
					"blocks/" + state.getBlock().getRegistryName().getResourcePath());
			return rl;
		}
	}
}