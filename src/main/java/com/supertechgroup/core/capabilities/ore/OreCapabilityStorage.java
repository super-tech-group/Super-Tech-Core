package com.supertechgroup.core.capabilities.ore;

import java.util.HashMap;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.Capability.IStorage;
import net.minecraftforge.common.util.Constants;

public class OreCapabilityStorage implements IStorage<IOreCapability> {

	@Override
	public void readNBT(Capability<IOreCapability> capability, IOreCapability instance, EnumFacing side, NBTBase nbt) {
		HashMap<Long, Object[]> map = new HashMap<>();
		NBTTagList list = (NBTTagList) nbt;
		list.forEach((tag) -> {
			NBTTagCompound data = (NBTTagCompound) tag;
			NBTTagList ores = data.getTagList("ores", Constants.NBT.TAG_STRING);
			Object[] d = new Object[ores.tagCount() + 2];
			d[0] = data.getFloat("hardness");
			d[1] = new ResourceLocation(data.getString("texture"));
			for (int i = 0; i < ores.tagCount(); i++) {
				d[i + 2] = new ResourceLocation(ores.getStringTagAt(i));
			}
			map.put(data.getLong("pos"), d);
		});
		instance.setData(map);
	}

	@Override
	public NBTBase writeNBT(Capability<IOreCapability> capability, IOreCapability instance, EnumFacing side) {
		NBTTagList list = new NBTTagList();
		instance.getData().entrySet().forEach((set) -> {
			NBTTagCompound data = new NBTTagCompound();
			data.setLong("pos", set.getKey());
			data.setFloat("hardness", (float) set.getValue()[0]);
			data.setString("texture", ((ResourceLocation) set.getValue()[1]).toString());
			NBTTagList ores = new NBTTagList();
			for (int i = 2; i < set.getValue().length; i++) {
				ores.appendTag(new NBTTagString(((ResourceLocation) set.getValue()[i]).toString()));
			}
			data.setTag("ores", ores);
			list.appendTag(data);
		});
		return list;
	}

}