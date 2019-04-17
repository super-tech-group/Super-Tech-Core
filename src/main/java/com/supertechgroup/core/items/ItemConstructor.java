package com.supertechgroup.core.items;

import javax.annotation.Nonnull;

import com.supertechgroup.core.machinery.multiblock.MultiblockHandler;
import com.supertechgroup.core.util.ItemBase;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ItemConstructor extends ItemBase {

	public ItemConstructor() {
		super("itemConstructor");
	}

	@Nonnull
	@Override
	public EnumActionResult onItemUseFirst(EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX,
			float hitY, float hitZ, EnumHand hand) {
		ItemStack stack = player.getHeldItem(hand);
		side = player.getHorizontalFacing();
		for (MultiblockHandler.IMultiblock mb : MultiblockHandler.getMultiblocks()) {
			if (mb.isBlockTrigger(world.getBlockState(pos))) {
				if (MultiblockHandler.postMultiblockFormationEvent(player, mb, pos, stack).isCanceled()) {
					continue;
				}
				if (side.getAxis() == Axis.Y) {
					side = EnumFacing.fromAngle(player.rotationYaw);
				} else {
					side = side.getOpposite();
				}
				if (!mb.checkStructure(world, pos, side, player)) {
					continue;
				}
				if (mb.createStructure(world, pos, side, player)) {
					return EnumActionResult.SUCCESS;
				}
			}
		}

		return EnumActionResult.PASS;
	}

}