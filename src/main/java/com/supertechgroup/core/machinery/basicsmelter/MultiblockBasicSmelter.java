package com.supertechgroup.core.machinery.basicsmelter;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.machinery.multiblock.MultiblockHandler.IMultiblock;
import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3i;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreIngredient;

public class MultiblockBasicSmelter implements IMultiblock {
	static Ingredient[][][] structure = new Ingredient[2][2][2];
	{
		for (int i = 0; i < 2; i++) {
			for (int j = 0; j < 2; j++) {
				for (int k = 0; k < 2; k++) {
					if ((i + j + k) % 2 == 0) {
						structure[i][j][k] = new OreIngredient("multiWall");
					} else {
						structure[i][j][k] = new OreIngredient("plankWood");
					}
				}
			}
		}
	}

	@Override
	public String getUniqueName() {
		// TODO Auto-generated method stub
		return Reference.MODID + ":BasicSmelter";
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
		newTile.setMasterPos(fll);
		if (oldTile instanceof TileMultiBlock) {
			player.sendMessage(new TextComponentString(oldTile.writeToNBT(new NBTTagCompound()).toString()));
		}
		world.removeTileEntity(pos.add(getTriggerOffset()));
		world.setTileEntity(pos.add(getTriggerOffset()), newTile);
		return true;
	}

	@Override
	public Ingredient[][][] getStructureManual() {
		return structure;
	}

	@Override
	public Vec3i getTriggerOffset() {
		return new Vec3i(0, 0, 0);
	}

	@Override
	public boolean overwriteBlockRender(ItemStack stack, int iterator) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean canRenderFormedStructure() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void renderFormedStructure() {
		// TODO Auto-generated method stub

	}

}
