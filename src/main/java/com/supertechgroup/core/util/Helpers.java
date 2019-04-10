package com.supertechgroup.core.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.logging.log4j.core.util.Loader;

import com.supertechgroup.core.metalurgy.Material;

import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

/**
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

	public static int getNBTInt(ItemStack stack, String key) {
		return stack.hasTagCompound() ? getTag(stack).getInteger(key) : 0;
	}

	public static NBTTagCompound getTag(ItemStack stack) {
		if (!stack.hasTagCompound()) {
			stack.setTagCompound(new NBTTagCompound());
		}
		return stack.getTagCompound();
	}

	public static void setNBTInt(ItemStack stack, String key, int val) {
		getTag(stack).setInteger(key, val);
	}
	
	public static Material getItemMaterial(ItemStack stack) {
		if (stack.hasTagCompound() && stack.getTagCompound().hasKey("sttMaterial")) {
			return Material.REGISTRY.getValue(new ResourceLocation(stack.getTagCompound().getString("sttMaterial")));
		}
		return Material.REGISTRY.getValue(new ResourceLocation("supertechtweaks:silver"));
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

}