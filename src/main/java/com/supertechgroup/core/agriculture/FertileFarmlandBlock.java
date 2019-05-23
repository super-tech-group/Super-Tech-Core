package com.supertechgroup.core.agriculture;

import java.util.Random;

import com.supertechgroup.core.Reference;

import net.minecraft.block.BlockFarmland;
import net.minecraft.block.SoundType;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.EnumPlantType;
import net.minecraftforge.common.IPlantable;

public class FertileFarmlandBlock extends BlockFarmland {
	public FertileFarmlandBlock() {
		super();
		this.setHardness(0.6F);
		this.setSoundType(SoundType.GROUND);
		this.setHarvestLevel("shovel", 0);
		this.setRegistryName(Reference.MODID, "fertileFarmland");
		this.setUnlocalizedName("fertileFarmland");
	}

	@Override
	public boolean canSustainPlant(IBlockState state, IBlockAccess world, BlockPos pos, EnumFacing direction,
			IPlantable plantable) {
		return plantable.getPlantType(world, pos.up()) == EnumPlantType.Crop;
	}

	public void registerModels() {
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(this.getRegistryName().toString(),
				"inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), DEFAULT_ITEM_SUBTYPE,
				itemModelResourceLocation);
	}

	@Override
	public void updateTick(World world, BlockPos pos, IBlockState state, Random rand) {
		super.updateTick(world, pos, state, rand);
		IBlockState above = world.getBlockState(pos.up());
		// if this block gets a block tick, apply it to the plant growing on top
		if (above.getBlock() instanceof IPlantable
				&& canSustainPlant(above, world, pos, EnumFacing.UP, (IPlantable) above.getBlock())) {
			world.scheduleBlockUpdate(pos.up(), above.getBlock(), above.getBlock().tickRate(world), 5);
		}
		// 2% chance that this will go back to regular farmland.
		if (rand.nextInt(50) == 0) {
			world.setBlockState(pos,
					Blocks.FARMLAND.getDefaultState().withProperty(MOISTURE, this.getMetaFromState(state) & 7));
		}
	}
}
