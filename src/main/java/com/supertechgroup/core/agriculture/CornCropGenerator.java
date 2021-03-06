package com.supertechgroup.core.agriculture;

import java.util.Random;

import com.supertechgroup.core.ModRegistry;

import net.minecraft.block.BlockBush;
import net.minecraft.init.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.chunk.IChunkProvider;
import net.minecraft.world.gen.IChunkGenerator;
import net.minecraftforge.fml.common.IWorldGenerator;

public class CornCropGenerator implements IWorldGenerator {

	public CornCropGenerator() {
	}

	@Override
	public void generate(Random rand, int chunkX, int chunkZ, World worldIn, IChunkGenerator chunkGenerator,
			IChunkProvider chunkProvider) {
		BlockPos position = new BlockPos(chunkX * 16 + 8, worldIn.getHeight(chunkX * 16 + 8, chunkZ * 16 + 8),
				chunkZ * 16 + 8);
		Biome biome = worldIn.getBiome(position);
		if (biome.isSnowyBiome() || biome.getRainfall() <= .5 || biome.getRainfall() > .8) {
			return;
		}

		BlockBush flower = ModRegistry.cornCrop;

		for (int i = 0; i < 16; ++i) {
			BlockPos blockpos = position.add(rand.nextInt(8) - rand.nextInt(8), rand.nextInt(4) - rand.nextInt(4),
					rand.nextInt(8) - rand.nextInt(8));

			if (worldIn.isAirBlock(blockpos) && (!worldIn.provider.isNether() || blockpos.getY() < 255)
					&& worldIn.getBlockState(blockpos.down()).getBlock() == Blocks.GRASS) {
				worldIn.setBlockState(blockpos, flower.getDefaultState().withProperty(CornCropBlock.CORN_AGE, 7), 2);
			}
		}

	}
}