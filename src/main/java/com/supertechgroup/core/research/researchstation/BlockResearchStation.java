package com.supertechgroup.core.research.researchstation;

import java.util.HashMap;

import javax.annotation.Nullable;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.capabilities.team.TeamCapabilityProvider;
import com.supertechgroup.core.capabilities.teamlist.IListCapability;
import com.supertechgroup.core.capabilities.teamlist.ListCapabilityProvider;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
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
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.Constants;

public class BlockResearchStation extends Block {

	private static final AxisAlignedBB BASE_AABB = new AxisAlignedBB(0, 0, 0, 1, 1.0625, 1);

	public BlockResearchStation() {
		super(Material.ROCK);
		this.setUnlocalizedName(Reference.MODID + ".researchStation");
		this.setRegistryName(Reference.MODID, "researchStation");
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
	public boolean hasTileEntity(IBlockState state) {
		return true;
	}

	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand,
			EnumFacing side, float hitX, float hitY, float hitZ) {
		if (!world.isRemote) {
			TileEntityResearchStation tile = (TileEntityResearchStation) world.getTileEntity(pos);
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
				IListCapability listCap = DimensionManager.getWorld(0)
						.getCapability(ListCapabilityProvider.TEAM_LIST_CAP, null);
				final StringBuilder message = new StringBuilder();
				message.append("Research Station for " + listCap.getTeamName(tile.getTeam()) + "\n");
				message.append("Currently studied:\n");
				taskMap.forEach((k, v) -> {
					message.append(v + "x " + k + "\n");
				});
				message.append("researched:\n");
				listCap.getCompletedForTeam(tile.getTeam()).forEach((r) -> {
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
			System.out.println("Setting team to " + placer.getName() + "'s team: "
					+ placer.getCapability(TeamCapabilityProvider.TEAM_CAP, null).getTeam());
			((TileEntityResearchStation) worldIn.getTileEntity(pos))
					.setTeam(((EntityPlayerMP) placer).getCapability(TeamCapabilityProvider.TEAM_CAP, null).getTeam());
			worldIn.getTileEntity(pos).markDirty();
		}
	}

	public void registerModels() {
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(this.getRegistryName().toString(),
				"inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), DEFAULT_ITEM_SUBTYPE,
				itemModelResourceLocation);
	}
}
