package com.supertechgroup.core.agriculture;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.util.ItemBase;

import net.minecraft.block.BlockFarmland;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class FertilizerItem extends ItemBase {

	public FertilizerItem() {
		super("fertilizer");
	}

	/**
	 * Called when a Block is right-clicked with this Item
	 */
	@Override
	public EnumActionResult onItemUse(EntityPlayer player, World worldIn, BlockPos pos, EnumHand hand,
			EnumFacing facing, float hitX, float hitY, float hitZ) {
		ItemStack itemstack = player.getHeldItem(hand);

		if (!player.canPlayerEdit(pos, facing, itemstack)) {
			return EnumActionResult.FAIL;
		} else {
			if (worldIn.getBlockState(pos).getBlock() == Blocks.FARMLAND) {
				worldIn.setBlockState(pos, ModRegistry.fertileBlock.getDefaultState().withProperty(
						BlockFarmland.MOISTURE, Blocks.FARMLAND.getMetaFromState(worldIn.getBlockState(pos)) & 7));
			}
			itemstack.setCount(itemstack.getCount() - 1);
			itemstack.damageItem(1, player);
			return EnumActionResult.SUCCESS;
		}
	}
}
