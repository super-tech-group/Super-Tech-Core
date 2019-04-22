package com.supertechgroup.core.machinery.multiblock.crudeio;

import com.supertechgroup.core.Reference;
import com.supertechgroup.core.machinery.multiblock.BlockMultiWall;

import net.minecraft.block.ITileEntityProvider;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.item.Item;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;

public class CrudeIOBlock extends BlockMultiWall implements ITileEntityProvider {
	public CrudeIOBlock() {
		super(Material.ROCK);
		this.setUnlocalizedName(Reference.MODID + ".crudeIO");
		this.setRegistryName(Reference.MODID, "crudeIO");
	}

	@Override
	public void breakBlock(World worldIn, BlockPos pos, IBlockState state) {

		IItemHandler handler = worldIn.getTileEntity(pos).getCapability(CapabilityItemHandler.ITEM_HANDLER_CAPABILITY,
				null);

		if (handler != null) {
			for (int i = 0; i < handler.getSlots(); i++) {
				if (!handler.getStackInSlot(i).isEmpty()) {
					// Create a new entity item with the item stack in the slot
					EntityItem item = new EntityItem(worldIn, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
							handler.extractItem(i, Integer.MAX_VALUE, false));

					// Apply some random motion to the item
					float multiplier = 0.1f;
					float motionX = worldIn.rand.nextFloat() - 0.5f;
					float motionY = worldIn.rand.nextFloat() - 0.5f;
					float motionZ = worldIn.rand.nextFloat() - 0.5f;

					item.motionX = motionX * multiplier;
					item.motionY = motionY * multiplier;
					item.motionZ = motionZ * multiplier;

					// Spawn the item in the world
					worldIn.spawnEntity(item);
				}
			}
		}

		// Super MUST be called last because it removes the tile entity
		super.breakBlock(worldIn, pos, state);
	}

	@Override
	public TileEntity createNewTileEntity(World worldIn, int meta) {
		return new CrudeIOTileEntity();
	}

	public void registerModels() {
		ModelResourceLocation itemModelResourceLocation = new ModelResourceLocation(this.getRegistryName().toString(),
				"inventory");
		final int DEFAULT_ITEM_SUBTYPE = 0;
		ModelLoader.setCustomModelResourceLocation(Item.getItemFromBlock(this), DEFAULT_ITEM_SUBTYPE,
				itemModelResourceLocation);
	}
}
