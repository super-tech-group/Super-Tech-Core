package com.supertechgroup.core.research;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.util.BlockTileEntity;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.common.util.Constants;

public class BlockResearchStation extends BlockTileEntity<TileEntityResearchStation> {

	private static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0, 0, 0, 1, 1.0625, 1);

	public BlockResearchStation() {
		super(Material.ROCK, "researchStation");
	}

	@Nullable
	@Override
	public TileEntityResearchStation createTileEntity(World world, IBlockState state) {
		return new TileEntityResearchStation();
	}

	@Override
	public AxisAlignedBB getBoundingBox(IBlockState state, IBlockAccess source, BlockPos pos) {
		return BASE_AABB;
	}

	@Nullable
	@Override
	public AxisAlignedBB getCollisionBoundingBox(IBlockState blockState, IBlockAccess worldIn, BlockPos pos) {
		return BASE_AABB;
	}

	@Override
	public Class<TileEntityResearchStation> getTileEntityClass() {
		return TileEntityResearchStation.class;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityResearchStation tile = getTileEntity(world, pos);
			ItemStack stack = player.getHeldItemMainhand();
			if (stack.getItem().equals(ModRegistry.itemResearchBook)) {
				NBTTagCompound tag = stack.getTagCompound();
				if (tag != null) {
					System.out.println(tag);
					NBTTagList list = tag.getTagList("tasks", Constants.NBT.TAG_STRING);
					list.forEach((task) -> {
						NBTTagString t = (NBTTagString) task;
						tile.addResearchProgress(new ResourceLocation(t.getString()));
					});
					tag.setTag("tasks", new NBTTagList());

				}
			} else {
				HashMap<ResourceLocation, Integer> taskMap = tile.getTasks();
				final StringBuilder message = new StringBuilder();
				message.append("Currently studied:\n");
				taskMap.forEach((k, v) -> {
					message.append(v + "x " + k + "\n");
				});
				message.append("researched:\n");
				tile.getTeam().getCompletedResearch().forEach((r) -> {
					// todo
					message.append(r.toString() + "\n");
				});
				player.sendMessage(new TextComponentString(message.toString()));
			}
		}
		return true;
	}

	@Override
	public void onBlockPlacedBy(World worldIn, BlockPos pos, IBlockState state, EntityLivingBase placer,
			ItemStack stack) {
		if (!worldIn.isRemote) {
			((TileEntityResearchStation) worldIn.getTileEntity(pos)).setTeam(
					ResearchSavedData.get(worldIn).findPlayersResearchTeam(((EntityPlayerMP) placer).getUniqueID()));
			worldIn.getTileEntity(pos).markDirty();
		}
	}
}
