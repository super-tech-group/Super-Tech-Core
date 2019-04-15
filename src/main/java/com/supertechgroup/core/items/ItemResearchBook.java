package com.supertechgroup.core.items;

import java.util.HashMap;
import java.util.List;

import javax.annotation.Nullable;

import com.supertechgroup.core.ModRegistry;
import com.supertechgroup.core.util.ItemBase;

import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.world.World;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class ItemResearchBook extends ItemBase {

	public ItemResearchBook() {
		super("researchBook");
	}

	public void registerModels() {
		ModelLoader.setCustomModelResourceLocation(this, 0,
				new ModelResourceLocation("supertechcore:researchbook", "inventory"));
	}

	public static NBTTagCompound getEmptyBookTag() {
		NBTTagCompound emptyResearchBook = new NBTTagCompound();
		emptyResearchBook.setInteger("remaining", 50);
		emptyResearchBook.setTag("tasks", new NBTTagList());
		return emptyResearchBook;
	}

	public static ItemStack getEmptyBookStack() {
		ItemStack emptyBook = new ItemStack(ModRegistry.itemResearchBook);
		emptyBook.setTagCompound(getEmptyBookTag());
		return emptyBook;
	}

	/**
	 * allows items to add custom lines of information to the mouseover description
	 */
	@SideOnly(Side.CLIENT)
	public void addInformation(ItemStack stack, @Nullable World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		NBTTagCompound tag = stack.getTagCompound();
		if (tag != null) {
			tooltip.add("Pages left: " + tag.getInteger("remaining"));
			tooltip.add("Tasks Researched:");
			NBTTagList taskList = tag.getTagList("tasks", Constants.NBT.TAG_STRING);
			HashMap<String, Integer> taskMap = new HashMap<>();
			taskList.forEach((task) -> {
				NBTTagString t = (NBTTagString) task;
				taskMap.put(t.getString(), taskMap.getOrDefault(t.getString(), 0) + 1);
			});
			taskMap.forEach((k, v) -> {
				tooltip.add(v + "x " + k);
			});
		} else {
			tooltip.add("Invalid NBT, did you cheat this in?");
		}

	}
}
