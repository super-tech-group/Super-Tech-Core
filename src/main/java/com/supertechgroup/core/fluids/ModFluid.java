package com.supertechgroup.core.fluids;

import net.minecraft.block.material.Material;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;

public class ModFluid extends Fluid {
	protected static int mapColor = 0xFFFFFFFF;
	protected static float overlayAlpha = 0.2F;
	protected static SoundEvent emptySound = SoundEvents.ITEM_BUCKET_EMPTY;
	protected static SoundEvent fillSound = SoundEvents.ITEM_BUCKET_FILL;
	protected static Material material = Material.WATER;
	private boolean hasBlock;

	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing) {
		super(fluidName, still, flowing);
		FluidRegistry.addBucketForFluid(this);
	}

	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor) {
		this(fluidName, still, flowing);
		setColor(mapColor);
	}

	public ModFluid(String fluidName, ResourceLocation still, ResourceLocation flowing, int mapColor,
			float overlayAlpha) {
		this(fluidName, still, flowing, mapColor);
		setAlpha(overlayAlpha);
	}

	@Override
	public boolean doesVaporize(FluidStack fluidStack) {
		if (block == null) {
			return false;
		}
		return block.getDefaultState().getMaterial() == getMaterial();
	}

	public float getAlpha() {
		return overlayAlpha;
	}

	@Override
	public int getColor() {
		return mapColor;
	}

	@Override
	public SoundEvent getEmptySound() {
		return emptySound;
	}

	@Override
	public SoundEvent getFillSound() {
		return fillSound;
	}

	public Material getMaterial() {
		return material;
	}

	public boolean hasBlock() {
		return hasBlock;
	}

	public ModFluid setAlpha(float parOverlayAlpha) {
		overlayAlpha = parOverlayAlpha;
		return this;
	}

	@Override
	public ModFluid setColor(int parColor) {
		mapColor = parColor;
		return this;
	}

	@Override
	public ModFluid setEmptySound(SoundEvent parSound) {
		emptySound = parSound;
		return this;
	}

	@Override
	public ModFluid setFillSound(SoundEvent parSound) {
		fillSound = parSound;
		return this;
	}

	public ModFluid setHasBlock(boolean has) {
		this.hasBlock = has;
		return this;
	}

	public ModFluid setMaterial(Material parMaterial) {
		material = parMaterial;
		return this;
	}
}