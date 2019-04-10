package com.supertechgroup.core.worldgen.ores;

import java.util.Collection;
import java.util.Collections;
import java.util.function.Function;

import com.google.common.collect.ImmutableSet;
import com.supertechgroup.core.Reference;

import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.common.model.IModelState;
import net.minecraftforge.common.model.TRSRTransformation;

/**
 *
 * @author oa10712
 */
public class OreModel implements IModel {

	@Override
	public IBakedModel bake(IModelState state, VertexFormat format,
			Function<ResourceLocation, TextureAtlasSprite> bakedTextureGetter) {
		return new OreBakedModel(state, format, bakedTextureGetter);
	}

	@Override
	public IModelState getDefaultState() {
		return TRSRTransformation.identity();
	}

	@Override
	public Collection<ResourceLocation> getDependencies() {
		return Collections.emptySet();
	}

	@Override
	public Collection<ResourceLocation> getTextures() {
		return ImmutableSet.of(new ResourceLocation(Reference.MODID, "blocks/ore1"),
				new ResourceLocation(Reference.MODID, "blocks/ore2"),
				new ResourceLocation(Reference.MODID, "blocks/ore3"),
				new ResourceLocation(Reference.MODID, "blocks/ore4"),
				new ResourceLocation(Reference.MODID, "blocks/ore5"),
				new ResourceLocation(Reference.MODID, "blocks/ore6"),
				new ResourceLocation(Reference.MODID, "blocks/ore7"));
	}
}