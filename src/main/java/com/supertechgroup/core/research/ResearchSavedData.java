package com.supertechgroup.core.research;

import com.supertechgroup.core.Reference;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.storage.MapStorage;
import net.minecraft.world.storage.WorldSavedData;

public class ResearchSavedData extends WorldSavedData {

	private World world;

	public static ResearchSavedData get(World world) {
		MapStorage storage = world.getMapStorage();
		ResearchSavedData instance = (ResearchSavedData) storage.getOrLoadData(ResearchSavedData.class,
				Reference.RESEARCH_DATA_NAME);

		if (instance == null) {
			instance = new ResearchSavedData();
			storage.setData(Reference.RESEARCH_DATA_NAME, instance);
		}
		instance.world = world;
		return instance;
	}

	public ResearchSavedData() {
		super(Reference.RESEARCH_DATA_NAME);
	}

	public ResearchSavedData(String s) {
		super(s);
	}

	@Override
	public void readFromNBT(NBTTagCompound nbt) {
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		System.out.println("Saving research");
		return compound;
	}

	public boolean getTeamFinishedResearch(ResearchTeam rt, Research research) {
		// TODO Auto-generated method stub
		return false;
	}
}
