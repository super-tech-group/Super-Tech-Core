package com.supertechgroup.core.worldgen.ores;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.annotation.Nullable;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.capabilities.ore.IOreCapability;
import com.supertechgroup.core.capabilities.ore.OreCapabilityProvider;

import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * The ore block for world generation. Can hold many ores
 *
 * @author oa10712
 *
 */
public class OreBlock extends Block {

	public static final PropertyBase BASE = new PropertyBase("base");
	public static final PropertyOres ORES = new PropertyOres("ores");

	public OreBlock() {
		super(net.minecraft.block.material.Material.ROCK);
		this.setUnlocalizedName(Reference.MODID + "superore");
		this.setRegistryName(Reference.MODID, "superore");
	}

	@Override
	@Deprecated // Forge: State sensitive version
	protected boolean canSilkHarvest() {
		return false;
	}

	@Override
	protected BlockStateContainer createBlockState() {
		IProperty[] listedProperties = new IProperty[0]; // no listed properties
		IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[] { BASE, ORES };
		return new ExtendedBlockState(this, listedProperties, unlistedProperties);
	}

	@Override
	@Deprecated
	public float getBlockHardness(IBlockState blockState, World worldIn, BlockPos pos) {
		return worldIn.getChunkFromBlockCoords(pos).getCapability(OreCapabilityProvider.ORE_CAP, null).getHardness(pos);
	}

	// return OreSavedData.get(worldIn).getHardness(pos);// == null ?
	// OreSavedData.get(worldIn).getHardness(pos) : 1f;
	// blockState.getBlockHardness(worldIn, pos);
	// Block.REGISTRY.getObject(OreSavedData.get(worldIn).getBase(pos)).getBlockHardness(blockState,
	// worldIn,pos);

	@SideOnly(Side.CLIENT)
	@Override
	public BlockRenderLayer getBlockLayer() {
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	/**
	 * This returns a complete list of items dropped from this block.
	 *
	 * @param world   The current world
	 * @param pos     Block position in world
	 * @param state   Current state
	 * @param fortune Breakers fortune level
	 * @return A ArrayList containing all items this block drops
	 */
	@Override
	public List<ItemStack> getDrops(IBlockAccess world, BlockPos pos, IBlockState state, int fortune) {
		World worldObject = SuperTechCoreMod.proxy.getWorld(world);
		List<ItemStack> ret = new ArrayList<>();

		Chunk chunk = worldObject.getChunkFromBlockCoords(pos);
		IOreCapability cap = chunk.getCapability(OreCapabilityProvider.ORE_CAP, null);

		ResourceLocation[] ores = cap.getOres(pos);
		ResourceLocation base = cap.getBase(pos);
		byte type = 0;
		if (base.getResourcePath().contains("nether")) {
			type = -1;
		} else if (base.getResourcePath().contains("end")) {
			type = 1;
		}
		Random rand = worldObject.rand;
		for (ResourceLocation ore : ores) {
			Ore material = Ore.REGISTRY.getValue(ore);
			ret.add(material.getDrops(type));
			for (int j = 0; j < fortune; j++) {
				if (rand.nextDouble() < .25) {
					ret.add(material.getDrops(type));
				}
			}
		}

		return ret;
	}

	@Override
	public IBlockState getExtendedState(IBlockState state, IBlockAccess world, BlockPos pos) {
		IExtendedBlockState extendedBlockState = (IExtendedBlockState) state;
		World worldObject = SuperTechCoreMod.proxy.getWorld(world);

		Chunk chunk = worldObject.getChunkFromBlockCoords(pos);
		IOreCapability cap = chunk.getCapability(OreCapabilityProvider.ORE_CAP, null);

		ResourceLocation[] ores = cap.getOres(pos);
		ResourceLocation base = cap.getBase(pos);
		ArrayList<ResourceLocation> oreList = new ArrayList<>();
		for (ResourceLocation i : ores) {
			oreList.add(i);
		}
		return extendedBlockState.withProperty(BASE, base).withProperty(ORES, oreList.toArray(new ResourceLocation[0]));
	}

	/**
	 * Queries the class of tool required to harvest this block, if null is returned
	 * we assume that anything can harvest this block.
	 */
	@Override
	@Nullable
	public String getHarvestTool(IBlockState state) {
		return "pickaxe";
	}

	/**
	 * Ensures that this block does not drop any items, this is now handled in
	 * removedByPlayer.
	 *
	 * @param meta
	 * @param random
	 * @param fortune
	 * @return
	 */
	@Override
	public Item getItemDropped(IBlockState meta, Random random, int fortune) {
		return null;
	}

	@Override
	public EnumBlockRenderType getRenderType(IBlockState iBlockState) {
		return EnumBlockRenderType.MODEL;
	}

	/**
	 * Method called after the player mines a block. Causes oreChunks to spawn that
	 * match the contained ores.
	 *
	 * @param worldIn
	 * @param pos
	 * @param state
	 *
	 * @return True if the block is destroyed
	 */
	@Override
	public boolean removedByPlayer(IBlockState state, World worldIn, BlockPos pos, EntityPlayer player,
			boolean willHarvest) {
		if (!worldIn.isRemote) {
			if (player.isCreative()) {// If the player is in creative...
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
				return true;
			}
			boolean metalLeft = false;
			IOreCapability cap = worldIn.getChunkFromBlockCoords(pos).getCapability(OreCapabilityProvider.ORE_CAP,
					null);
			ResourceLocation[] ores = cap.getOres(pos);
			ResourceLocation base = cap.getBase(pos);
			int tagCount;
			tagCount = player.getHeldItemMainhand().getEnchantmentTagList() != null
					? player.getHeldItemMainhand().getEnchantmentTagList().tagCount()
					: 0;
			int fortune = 0;
			for (int i = 0; i < tagCount; i++) {
				NBTTagCompound compound = (NBTTagCompound) player.getHeldItemMainhand().getEnchantmentTagList().get(i);
				if (compound.getShort("id") == 35) {// if the enchantment is
													// Fortune
					fortune = compound.getShort("lvl");
				}
			}
			byte type = 0;
			if (base.getResourcePath().contains("nether")) {
				type = -1;
			} else if (base.getResourcePath().contains("end")) {
				type = 1;
			}
			for (int i = 0; i < ores.length; i++) {
				Ore ore = Ore.REGISTRY.getValue(ores[i]);
				worldIn.spawnEntity(
						new EntityItem(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, ore.getDrops(type)));
				for (int j = 0; j < fortune; j++) {
					if (RANDOM.nextDouble() < .25) {
						worldIn.spawnEntity(new EntityItem(worldIn, pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5,
								ore.getDrops(type)));
					}
				}
				ores[i] = null;

			}
			ores = removeNulls(ores);
			cap.setOres(pos, ores);
			willHarvest = metalLeft;
			if (!metalLeft) {// When we have removel all of the ore from the
								// block...
				worldIn.setBlockState(pos, Blocks.AIR.getDefaultState());
			}
			worldIn.notifyBlockUpdate(pos, state, state, 2);
			worldIn.getChunkFromBlockCoords(pos).markDirty();
			return !metalLeft;
		}
		return true;
	}

	private ResourceLocation[] removeNulls(ResourceLocation[] ores) {
		ArrayList<ResourceLocation> temp = new ArrayList<>();
		for (ResourceLocation ore : ores) {
			if (ore != null) {
				temp.add(ore);
			}
		}
		return temp.toArray(new ResourceLocation[0]);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess worldIn, BlockPos pos, EnumFacing side) {
		return true;
	}

}