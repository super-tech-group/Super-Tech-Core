package com.supertechgroup.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Nonnull;

import org.apache.logging.log4j.core.util.Loader;

import com.supertechgroup.core.metallurgy.Material;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBoat;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemHoe;
import net.minecraft.item.ItemStack;
import net.minecraft.item.ItemSword;
import net.minecraft.item.ItemTool;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.oredict.OreDictionary;

/**
 * Some of the methods in this class are inspired/lifted from Modular Machinery
 * and vanilla classes
 * 
 * @author oa10712
 */
public class Helpers {

	public static void copyFileUsingStream(String source, File dest) throws IOException {

		InputStream is = Loader.getResource(source, null).openStream();
		OutputStream os = new FileOutputStream(dest);
		byte[] buffer = new byte[1024];
		int length;
		while ((length = is.read(buffer)) > 0) {
			os.write(buffer, 0, length);
		}
		os.close();
	}

	public static void copyFileUsingStream(String source, String dest) throws IOException {

		copyFileUsingStream(source, new File(dest));
	}

	public static int getItemBurnTime(ItemStack stack) {
		if (stack.isEmpty()) {
			return 0;
		} else {
			int burnTime = net.minecraftforge.event.ForgeEventFactory.getItemBurnTime(stack);
			if (burnTime >= 0) {
				return burnTime;
			}
			Item item = stack.getItem();

			if (item == Item.getItemFromBlock(Blocks.WOODEN_SLAB)) {
				return 150;
			} else if (item == Item.getItemFromBlock(Blocks.WOOL)) {
				return 100;
			} else if (item == Item.getItemFromBlock(Blocks.CARPET)) {
				return 67;
			} else if (item == Item.getItemFromBlock(Blocks.LADDER)) {
				return 300;
			} else if (item == Item.getItemFromBlock(Blocks.WOODEN_BUTTON)) {
				return 100;
			} else if (Block.getBlockFromItem(item).getDefaultState()
					.getMaterial() == net.minecraft.block.material.Material.WOOD) {
				return 300;
			} else if (item == Item.getItemFromBlock(Blocks.COAL_BLOCK)) {
				return 16000;
			} else if (item instanceof ItemTool && "WOOD".equals(((ItemTool) item).getToolMaterialName())) {
				return 200;
			} else if (item instanceof ItemSword && "WOOD".equals(((ItemSword) item).getToolMaterialName())) {
				return 200;
			} else if (item instanceof ItemHoe && "WOOD".equals(((ItemHoe) item).getMaterialName())) {
				return 200;
			} else if (item == Items.STICK) {
				return 100;
			} else if (item != Items.BOW && item != Items.FISHING_ROD) {
				if (item == Items.SIGN) {
					return 200;
				} else if (item == Items.COAL) {
					return 1600;
				} else if (item == Items.LAVA_BUCKET) {
					return 20000;
				} else if (item != Item.getItemFromBlock(Blocks.SAPLING) && item != Items.BOWL) {
					if (item == Items.BLAZE_ROD) {
						return 2400;
					} else if (item instanceof ItemDoor && item != Items.IRON_DOOR) {
						return 200;
					} else {
						return item instanceof ItemBoat ? 400 : 0;
					}
				} else {
					return 100;
				}
			} else {
				return 300;
			}
		}
	}

	public static Material getItemMaterial(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("sttMaterial")) {
			return Material.REGISTRY.getValue(new ResourceLocation(stack.getTagCompound().getString("sttMaterial")));
		}
		return Material.REGISTRY.getValue(new ResourceLocation("supertechcore:silver"));
	}

	public static int getNBTInt(ItemStack stack, String key) {
		return stack.hasTagCompound() ? getTag(stack).getInteger(key) : 0;
	}

	public static NBTTagCompound getTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}

	public static void setItemMaterial(ItemStack stack, Material material) {
		NBTTagCompound tag;
		if (stack.hasTagCompound()) {
			tag = stack.getTagCompound();
		} else {
			tag = new NBTTagCompound();
		}
		tag.setString("sttMaterial", material.getRegistryName().toString());
		stack.setTagCompound(tag);
	}

	public static void setNBTInt(ItemStack stack, String key, int val) {
		getTag(stack).setInteger(key, val);
	}

	public static int tryPlaceItemInInventory(@Nonnull ItemStack stack, IItemHandlerModifiable handler, int start,
			int end, boolean simulate) {
		ItemStack toAdd = stack.copy();
		if (!hasInventorySpace(toAdd, handler, start, end)) {
			return 0;
		}
		int insertedAmt = 0;
		int max = toAdd.getMaxStackSize();

		for (int i = start; i < end; i++) {
			ItemStack in = handler.getStackInSlot(i);
			if (in.isEmpty()) {
				int added = Math.min(stack.getCount(), max);
				stack.setCount(stack.getCount() - added);
				if (!simulate) {
					handler.setStackInSlot(i, copyStackWithSize(toAdd, added));
				}
				insertedAmt += added;
				if (stack.getCount() <= 0)
					return insertedAmt;
			} else {
				if (stackEqualsNonNBT(toAdd, in) && ItemStack.areItemStackTagsEqual(toAdd, in)) {
					int space = max - in.getCount();
					int added = Math.min(stack.getCount(), space);
					insertedAmt += added;
					stack.setCount(stack.getCount() - added);
					if (!simulate) {
						handler.getStackInSlot(i).setCount(handler.getStackInSlot(i).getCount() + added);
					}
					if (stack.getCount() <= 0)
						return insertedAmt;
				}
			}
		}
		return insertedAmt;
	}

	@Nonnull
	public static ItemStack copyStackWithSize(@Nonnull ItemStack stack, int amount) {
		if (stack.isEmpty() || amount <= 0)
			return ItemStack.EMPTY;
		ItemStack s = stack.copy();
		s.setCount(amount);
		return s;
	}

	public static boolean stackEqualsNonNBT(@Nonnull ItemStack stack, @Nonnull ItemStack other) {
		if (stack.isEmpty() && other.isEmpty())
			return true;
		if (stack.isEmpty() || other.isEmpty())
			return false;
		Item sItem = stack.getItem();
		Item oItem = other.getItem();
		if (sItem.getHasSubtypes() || oItem.getHasSubtypes()) {
			return sItem.equals(other.getItem()) && (stack.getItemDamage() == other.getItemDamage()
					|| stack.getItemDamage() == OreDictionary.WILDCARD_VALUE
					|| other.getItemDamage() == OreDictionary.WILDCARD_VALUE);
		} else {
			return sItem.equals(other.getItem());
		}
	}

	public static boolean hasInventorySpace(@Nonnull ItemStack stack, IItemHandler handler, int rangeMin,
			int rangeMax) {
		int size = stack.getCount();
		int max = stack.getMaxStackSize();
		for (int i = rangeMin; i < rangeMax && size > 0; i++) {
			ItemStack in = handler.getStackInSlot(i);
			if (in.isEmpty()) {
				size -= max;
			} else {
				if (stackEqualsNonNBT(stack, in) && ItemStack.areItemStackTagsEqual(stack, in)) {
					int space = max - in.getCount();
					size -= space;
				}
			}
		}
		return size <= 0;
	}

	public static boolean consumeFromInventory(IItemHandlerModifiable handler, ItemStack toConsume, boolean simulate) {
		Map<Integer, ItemStack> contents = findItemsIndexedInInventory(handler, toConsume, false);
		if (contents.isEmpty())
			return false;

		int cAmt = toConsume.getCount();
		for (int slot : contents.keySet()) {
			ItemStack inSlot = contents.get(slot);
			int toRemove = cAmt > inSlot.getCount() ? inSlot.getCount() : cAmt;
			cAmt -= toRemove;
			if (!simulate) {
				handler.setStackInSlot(slot, copyStackWithSize(inSlot, inSlot.getCount() - toRemove));
			}
			if (cAmt <= 0) {
				break;
			}
		}
		return cAmt <= 0;
	}

	public static Map<Integer, ItemStack> findItemsIndexedInInventory(IItemHandler handler, ItemStack match,
			boolean strict) {
		Map<Integer, ItemStack> stacksOut = new HashMap<>();
		for (int j = 0; j < handler.getSlots(); j++) {
			ItemStack s = handler.getStackInSlot(j);
			if (strict ? ItemStack.areItemsEqual(s, match) : matchStackLoosely(s, match)) {
				stacksOut.put(j, copyStackWithSize(s, s.getCount()));
			}
		}
		return stacksOut;
	}

	public static boolean matchStackLoosely(@Nonnull ItemStack stack, @Nonnull ItemStack other) {
		if (stack.isEmpty())
			return other.isEmpty();
		return stack.isItemEqual(other);
	}
}