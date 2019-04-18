/*
 * BluSunrize
 * Copyright (c) 2017
 *
 * This code is originally taken from Immersive Engineering (https://minecraft.curseforge.com/projects/immersive-engineering?gameCategorySlug=mc-mods&projectID=231951)
 */
package com.supertechgroup.core.machinery.multiblock;

import java.util.ArrayList;

import com.supertechgroup.core.machinery.multiblock.matcher.BlockMatcher;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.fml.common.eventhandler.Cancelable;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * @author BluSunrize - 27.04.2015 <br>
 *         The handler for multiblocks. TO handle custom structures, create a
 *         class implementing IMultiblock and register it
 */
public class MultiblockHandler {
	public interface IMultiblock {
		/**
		 * returns true to add a button that will switch between the assembly of
		 * multiblocks and the finished render
		 */
		@SideOnly(Side.CLIENT)
		boolean canRenderFormedStructure();

		/**
		 * This method checks the structure .
		 *
		 * @return if the structure was valid.
		 */
		default boolean checkStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
			BlockPos fll = pos.add(getTriggerOffset());
			System.out.println("checking master at " + fll);
			for (int x = 0; x < getStructureManual().length; x++) {
				for (int y = 0; y < getStructureManual()[0].length; y++) {
					for (int z = 0; z < getStructureManual()[0][0].length; z++) {
						BlockPos np = fll.offset(EnumFacing.UP, y).offset(player.getHorizontalFacing(), x)
								.offset(player.getHorizontalFacing().rotateY(), z);
						if (!getStructureManual()[x][y][z].apply(world, pos)) {
							player.sendMessage(new TextComponentString(
									"Check failed at " + np + "; Expected " + getStructureManual()[x][y][z].toString()
											+ " found " + world.getBlockState(np).getBlock()));
							return false;
						} else {
							TileEntity tileAtPos = world.getTileEntity(np);
							if (tileAtPos != null && tileAtPos instanceof TileMultiBlock) {
								TileMultiBlock multiTile = (TileMultiBlock) tileAtPos;
								if (!multiTile.getMasterPos().equals(new BlockPos(-1, -1, -1))) {
									player.sendMessage(new TextComponentString(
											"Check failed at " + np + "; block already part of multiblock structure"));
									return false;
								}
							}
						}
					}
				}
			}
			System.out.println("Formed Structure");
			return true;
		}

		/**
		 * This method sets up the structure.
		 *
		 * @return if the structure was transformed.
		 */
		boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player);

		default IBlockState getBlockstateFromStack(int index, ItemStack stack) {
			if (!stack.isEmpty() && stack.getItem() instanceof ItemBlock) {
				return ((ItemBlock) stack.getItem()).getBlock().getStateFromMeta(stack.getItemDamage());
			}
			return null;
		}

		/**
		 * A three-dimensional array (height, width, length) of the structure
		 */
		BlockMatcher[][][] getStructureManual();

		/**
		 * Returns the blockpos offset
		 */
		Vec3i getTriggerOffset();

		/**
		 * returns name of the Multiblock. This is used for the interdiction NBT system
		 * on the hammer, so this name /must/ be unique.
		 */
		String getUniqueName();

		/**
		 * Check whether the given block can be used to trigger the structure creation
		 * of the multiblock.<br>
		 * Basically, a less resource-intensive preliminary check to avoid checking
		 * every structure.
		 */
		public default boolean isBlockTrigger(World world, BlockPos pos) {
			Vec3i offset = getTriggerOffset();
			return getStructureManual()[offset.getX()][-offset.getY()][offset.getZ()].apply(world, pos);
		}

		/**
		 * Use this to overwrite the rendering of a Multiblock's Component
		 */
		@SideOnly(Side.CLIENT)
		boolean overwriteBlockRender(ItemStack stack, int iterator);

		/**
		 * use this function to render the complete multiblock
		 */
		@SideOnly(Side.CLIENT)
		void renderFormedStructure();
	}

	/**
	 * This event is fired BEFORE the multiblock is attempted to be formed.<br>
	 * No checks of the structure have been made. The event simply exists to cancel
	 * the formation of the multiblock before it ever happens.
	 */
	@Cancelable
	public static class MultiblockFormEvent extends PlayerEvent {
		private final IMultiblock multiblock;
		private final BlockPos clickedBlock;
		private final ItemStack hammer;

		public MultiblockFormEvent(EntityPlayer player, IMultiblock multiblock, BlockPos clickedBlock,
				ItemStack hammer) {
			super(player);
			this.multiblock = multiblock;
			this.clickedBlock = clickedBlock;
			this.hammer = hammer;
		}

		public BlockPos getClickedBlock() {
			return clickedBlock;
		}

		public ItemStack getHammer() {
			return hammer;
		}

		public IMultiblock getMultiblock() {
			return multiblock;
		}
	}

	private static ArrayList<IMultiblock> multiblocks = new ArrayList<>();

	public static ArrayList<IMultiblock> getMultiblocks() {
		return multiblocks;
	}

	public static MultiblockFormEvent postMultiblockFormationEvent(EntityPlayer player, IMultiblock multiblock,
			BlockPos clickedBlock, ItemStack hammer) {
		MultiblockFormEvent event = new MultiblockFormEvent(player, multiblock, clickedBlock, hammer);
		MinecraftForge.EVENT_BUS.post(event);
		return event;
	}

	public static void registerMultiblock(IMultiblock multiblock) {
		multiblocks.add(multiblock);
	}
}