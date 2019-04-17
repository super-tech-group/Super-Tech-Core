package com.supertechgroup.core.machinery.basicsmelter;

import javax.annotation.Nullable;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.items.SuperTechItem;
import com.supertechgroup.core.machinery.multiblock.TileMultiBlock;

import net.minecraft.block.BlockFurnace;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.ISidedInventory;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.items.CapabilityItemHandler;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.wrapper.SidedInvWrapper;
import net.minecraftforge.oredict.OreIngredient;

public class TileEntityBasicSmelter extends TileMultiBlock implements ISidedInventory, ITickable {
	/**
	 * Slots: 0: item to be processed 1: fuel 2: primary output 3: flux 4: slag
	 * output
	 */

	private static final int[] SLOTS_TOP = new int[] { 0, 3 };
	private static final int[] SLOTS_BOTTOM = new int[] { 2, 1 };
	private static final int[] SLOTS_SIDES = new int[] { 1, 4 };

	/** The ItemStacks that hold the items currently being used in the furnace */
	private NonNullList<ItemStack> furnaceItemStacks = NonNullList.<ItemStack>withSize(5, ItemStack.EMPTY);

	/** The number of ticks that the furnace will keep burning */
	private int furnaceBurnTime;

	/**
	 * The number of ticks that a fresh copy of the currently-burning item would
	 * keep the furnace burning for
	 */
	private int currentItemBurnTime;

	private int cookTime;

	private int totalCookTime;

	IItemHandler handlerTop = new SidedInvWrapper(this, EnumFacing.UP);
	IItemHandler handlerBottom = new SidedInvWrapper(this, EnumFacing.DOWN);
	IItemHandler handlerSide = new SidedInvWrapper(this, EnumFacing.WEST);
	ItemStackHandler stackHandler = new ItemStackHandler(furnaceItemStacks);
	private String customName;

	/**
	 * Returns true if automation can extract the given item in the given slot from
	 * the given side.
	 */
	@Override
	public boolean canExtractItem(int index, ItemStack stack, EnumFacing direction) {
		return true;
	}

	/**
	 * Returns true if automation can insert the given item in the given slot from
	 * the given side.
	 */
	@Override
	public boolean canInsertItem(int index, ItemStack itemStackIn, EnumFacing direction) {
		return this.isItemValidForSlot(index, itemStackIn);
	}

	@Override
	public void clear() {
		this.furnaceItemStacks.clear();
	}

	@Override
	public void closeInventory(EntityPlayer player) {
	}

	/**
	 * Removes up to a specified number of items from an inventory slot and returns
	 * them in a new stack.
	 */
	@Override
	public ItemStack decrStackSize(int index, int count) {
		return ItemStackHelper.getAndSplit(this.furnaceItemStacks, index, count);
	}

	@SuppressWarnings("unchecked")
	@Override
	@javax.annotation.Nullable
	public <T> T getCapability(Capability<T> capability, @Nullable EnumFacing facing) {
		if (capability == CapabilityItemHandler.ITEM_HANDLER_CAPABILITY) {
			if (facing == null) {
				return (T) stackHandler;
			} else if (facing == EnumFacing.DOWN) {
				return (T) handlerBottom;
			} else if (facing == EnumFacing.UP) {
				return (T) handlerTop;
			} else {
				return (T) handlerSide;
			}
		}
		return super.getCapability(capability, facing);
	}

	public boolean hasGUI() {
		return true;
	}

	/**
	 * ticks for an operation
	 *
	 * @param stack
	 * @return
	 */
	private int getCookTime(ItemStack stack) {
		// TODO Auto-generated method stub
		return 200;
	}

	@Override
	public int getField(int id) {
		switch (id) {
		case 0:
			return this.furnaceBurnTime;
		case 1:
			return this.currentItemBurnTime;
		case 2:
			return this.cookTime;
		case 3:
			return this.totalCookTime;
		default:
			return 0;
		}
	}

	@Override
	public int getFieldCount() {
		return 4;
	}

	@Override
	public int getGuiID() {
		return Reference.BASIC_SMELTER_ID;
	}

	/**
	 * Returns the maximum stack size for a inventory slot.
	 */
	@Override
	public int getInventoryStackLimit() {
		return 64;
	}

	/**
	 * Get the name of this object. For players this returns their username
	 */
	@Override
	public String getName() {
		return this.hasCustomName() ? this.customName : "container.basicSmelter";
	}

	/**
	 * Returns the number of slots in the inventory.
	 */
	@Override
	public int getSizeInventory() {
		return this.furnaceItemStacks.size();
	}

	@Override
	public int[] getSlotsForFace(EnumFacing side) {
		if (side == EnumFacing.DOWN) {
			return SLOTS_BOTTOM;
		} else {
			return side == EnumFacing.UP ? SLOTS_TOP : SLOTS_SIDES;
		}
	}

	/**
	 * Returns the stack in the given slot.
	 */
	@Override
	public ItemStack getStackInSlot(int index) {
		return this.furnaceItemStacks.get(index);
	}

	/**
	 * Returns true if this thing is named
	 */
	@Override
	public boolean hasCustomName() {
		return this.customName != null && !this.customName.isEmpty();
	}

	@Override
	public boolean isEmpty() {
		for (ItemStack itemstack : this.furnaceItemStacks) {
			if (!itemstack.isEmpty()) {
				return false;
			}
		}

		return true;
	}

	/**
	 * Returns true if automation is allowed to insert the given stack (ignoring
	 * stack size) into the given slot. For guis use Slot.isItemValid
	 */
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack) {
		switch (index) {
		case 0:
			return ForgeEventFactory.getItemBurnTime(stack) > 0;
		case 1:
			return true;
		case 3:
			return (new OreIngredient("flux")).apply(stack);
		default:
			return false;
		}
	}

	/**
	 * Don't rename this method to canInteractWith due to conflicts with Container
	 */
	@Override
	public boolean isUsableByPlayer(EntityPlayer player) {
		if (this.world.getTileEntity(this.pos) != this) {
			return false;
		} else {
			return player.getDistanceSq(this.pos.getX() + 0.5D, this.pos.getY() + 0.5D,
					this.pos.getZ() + 0.5D) <= 64.0D;
		}
	}

	@Override
	public void openInventory(EntityPlayer player) {
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		this.furnaceItemStacks = NonNullList.<ItemStack>withSize(this.getSizeInventory(), ItemStack.EMPTY);
		ItemStackHelper.loadAllItems(compound, this.furnaceItemStacks);
		this.furnaceBurnTime = compound.getInteger("BurnTime");
		this.cookTime = compound.getInteger("CookTime");
		this.totalCookTime = compound.getInteger("CookTimeTotal");
		this.currentItemBurnTime = ForgeEventFactory.getItemBurnTime(this.furnaceItemStacks.get(1));

		if (compound.hasKey("CustomName", 8)) {
			this.customName = compound.getString("CustomName");
		}
	}

	/**
	 * Removes a stack from the given slot and returns it.
	 */
	@Override
	public ItemStack removeStackFromSlot(int index) {
		return ItemStackHelper.getAndRemove(this.furnaceItemStacks, index);
	}

	public void setCustomInventoryName(String name) {
		this.customName = name;
	}

	@Override
	public void setField(int id, int value) {
		switch (id) {
		case 0:
			this.furnaceBurnTime = value;
			break;
		case 1:
			this.currentItemBurnTime = value;
			break;
		case 2:
			this.cookTime = value;
			break;
		case 3:
			this.totalCookTime = value;
		}
	}

	/**
	 * Sets the given item stack to the specified slot in the inventory (can be
	 * crafting or armor sections).
	 */
	@Override
	public void setInventorySlotContents(int index, ItemStack stack) {
		ItemStack itemstack = this.furnaceItemStacks.get(index);
		boolean flag = !stack.isEmpty() && stack.isItemEqual(itemstack)
				&& ItemStack.areItemStackTagsEqual(stack, itemstack);
		this.furnaceItemStacks.set(index, stack);

		if (stack.getCount() > this.getInventoryStackLimit()) {
			stack.setCount(this.getInventoryStackLimit());
		}

		if (index == 0 && !flag) {
			this.totalCookTime = this.getCookTime(stack);
			this.cookTime = 0;
			this.markDirty();
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		super.writeToNBT(compound);
		compound.setInteger("BurnTime", (short) this.furnaceBurnTime);
		compound.setInteger("CookTime", (short) this.cookTime);
		compound.setInteger("CookTimeTotal", (short) this.totalCookTime);
		ItemStackHelper.saveAllItems(compound, this.furnaceItemStacks);

		if (this.hasCustomName()) {
			compound.setString("CustomName", this.customName);
		}

		return compound;
	}

	/**
	 * Furnace isBurning
	 */
	public boolean isBurning() {
		return this.furnaceBurnTime > 0;
	}

	@SideOnly(Side.CLIENT)
	public static boolean isBurning(IInventory inventory) {
		return inventory.getField(0) > 0;
	}

	@Override
	public void update() {
		boolean flag = this.isBurning();
		boolean flag1 = false;

		if (this.isBurning()) {
			--this.furnaceBurnTime;
		}

		if (!this.world.isRemote) {
			ItemStack itemstack = this.furnaceItemStacks.get(1);

			if (this.isBurning() || !itemstack.isEmpty() && !((ItemStack) this.furnaceItemStacks.get(0)).isEmpty()) {
				if (!this.isBurning() && this.canSmelt()) {
					this.furnaceBurnTime = ForgeEventFactory.getItemBurnTime(itemstack);
					this.currentItemBurnTime = this.furnaceBurnTime;

					if (this.isBurning()) {
						flag1 = true;

						if (!itemstack.isEmpty()) {
							Item item = itemstack.getItem();
							itemstack.shrink(1);

							if (itemstack.isEmpty()) {
								ItemStack item1 = item.getContainerItem(itemstack);
								this.furnaceItemStacks.set(1, item1);
							}
						}
					}
				}

				if (this.isBurning() && this.canSmelt()) {
					++this.cookTime;

					if (this.cookTime == this.totalCookTime) {
						this.cookTime = 0;
						this.totalCookTime = this.getCookTime(this.furnaceItemStacks.get(0));
						this.smeltItem();
						flag1 = true;
					}
				} else {
					this.cookTime = 0;
				}
			} else if (!this.isBurning() && this.cookTime > 0) {
				this.cookTime = MathHelper.clamp(this.cookTime - 2, 0, this.totalCookTime);
			}

			if (flag != this.isBurning()) {
				flag1 = true;
				BlockFurnace.setState(this.isBurning(), this.world, this.pos);
			}
		}

		if (flag1) {
			this.markDirty();
		}
	}

	/**
	 * Returns true if the furnace can smelt an item, i.e. has a source item,
	 * destination stack isn't full, etc.
	 */
	private boolean canSmelt() {
		if (this.furnaceItemStacks.get(0).isEmpty() || this.furnaceItemStacks.get(3).isEmpty()) {
			// if we are out of input or flux
			return false;
		} else {
			ItemStack result = RecipiesBasicSmelter.instance().getSmeltingResult(this.furnaceItemStacks.get(0));

			if (result.isEmpty()) {
				// if there is no valid recipe
				return false;
			} else {
				ItemStack output = this.furnaceItemStacks.get(2);
				ItemStack slag = this.furnaceItemStacks.get(4);
				if (slag.getCount() >= slag.getItem().getItemStackLimit(slag)) {
					// if the slag slot is full
					return false;
				}
				if (output.isEmpty()) {
					return true;
				} else if (!output.isItemEqual(result)) {
					return false;
				} else if (output.getCount() + result.getCount() <= this.getInventoryStackLimit()
						&& output.getCount() + result.getCount() <= output.getMaxStackSize()) {
					return true;
				} else {
					return output.getCount() + result.getCount() <= result.getMaxStackSize();
				}
			}
		}
	}

	/**
	 * Turn one item from the furnace source stack into the appropriate smelted item
	 * in the furnace result stack
	 */
	public void smeltItem() {
		if (this.canSmelt()) {
			ItemStack input = this.furnaceItemStacks.get(0);
			ItemStack flux = this.furnaceItemStacks.get(3);
			ItemStack result = RecipiesBasicSmelter.instance().getSmeltingResult(input);
			ItemStack primaryOutput = this.furnaceItemStacks.get(2);
			ItemStack slagOutput = this.furnaceItemStacks.get(4);

			if (primaryOutput.isEmpty()) {
				this.furnaceItemStacks.set(2, result.copy());
			} else if (primaryOutput.getItem() == result.getItem()) {
				primaryOutput.grow(result.getCount());
			}

			if (slagOutput.isEmpty()) {
				this.furnaceItemStacks.set(4, new ItemStack(ModRegistry.itemTech, 1, SuperTechItem.SLAG));
			} else {
				slagOutput.grow(1);
			}

			input.shrink(1);
			flux.shrink(1);
		}
	}

	public static int getSlotsNumber() {
		return 5;
	}
}
