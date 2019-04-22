package com.supertechgroup.core.machinery.basicsmelter;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.machinery.multiblock.MultiblockHandler.IMultiblock;
import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;
import com.supertechgroup.core.machinery.multiblock.matcher.BlockMatcher;
import com.supertechgroup.core.machinery.multiblock.matcher.DirectBlockMatcher;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;

public class MultiblockBasicSmelter implements IMultiblock {
	static BlockMatcher[][][] structure = new BlockMatcher[][][] {
			{ { new DirectBlockMatcher(ModRegistry.crudeHeaterBlock) },
					{ new DirectBlockMatcher(ModRegistry.crudeWallBlock) },
					{ new DirectBlockMatcher(ModRegistry.crudeIOBlock) } },
			{ { new DirectBlockMatcher(ModRegistry.crudeIOBlock) },
					{ new DirectBlockMatcher(ModRegistry.crudeIOBlock) }, { new DirectBlockMatcher(Blocks.AIR) } } };

	@Override
	public boolean canRenderFormedStructure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean createStructure(World world, BlockPos pos, EnumFacing side, EntityPlayer player) {
		BlockPos fll = pos.add(getTriggerOffset());
		for (int x = 0; x < getStructureManual().length; x++) {
			for (int y = 0; y < getStructureManual()[0].length; y++) {
				for (int z = 0; z < getStructureManual()[0][0].length; z++) {
					BlockPos np = fll.offset(EnumFacing.UP, y).offset(player.getHorizontalFacing(), x)
							.offset(player.getHorizontalFacing().rotateY(), z);
					TileEntity tileAtPos = world.getTileEntity(np);
					if (tileAtPos != null && tileAtPos instanceof TileMultiBlock) {
						TileMultiBlock multiTile = (TileMultiBlock) tileAtPos;
						multiTile.setMasterPos(fll);
					}
				}
			}
		}
		TileEntity oldTile = world.getTileEntity(pos.add(getTriggerOffset()));
		TileEntityBasicSmelter newTile = new TileEntityBasicSmelter();
		newTile.setMasterPos(pos);
		newTile.setFacing(side);
		if (oldTile instanceof TileMultiBlock) {
			player.sendMessage(new TextComponentString(oldTile.writeToNBT(new NBTTagCompound()).toString()));
		}
		world.removeTileEntity(pos);
		world.setTileEntity(pos, newTile);
		return true;
	}

	@Override
	public BlockMatcher[][][] getStructureManual() {
		return structure;
	}

	@Override
	public BlockMatcher getTrigger() {
		Vec3i offset = getTriggerOffset();
		return structure[offset.getX() * -1][offset.getY() * -1][offset.getZ() * -1];
	}

	@Override
	public Vec3i getTriggerOffset() {
		return new Vec3i(0, -1, 0);
	}

	@Override
	public String getUniqueName() {
		// TODO Auto-generated method stub
		return Reference.MODID + ":BasicSmelter";
	}

	@Override
	public boolean overwriteBlockRender(ItemStack stack, int iterator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void renderFormedStructure() {
		// TODO Auto-generated method stub

	}

}
