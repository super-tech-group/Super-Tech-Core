package com.supertechgroup.core;

import net.minecraft.util.ResourceLocation;

public class Reference {
	public static final String MODID = "supertechcore";
	public static final String MODNAME = "Super Tech Core";
	public static final String VERSION = "1.0";
	public static final String ACCEPTED_MINECRAFT_VERSIONS = "[1.12.2]";

	public static final String ORE_DATA_NAME = Reference.MODID + "_OreData";
	public static final String RESEARCH_DATA_NAME = Reference.MODID + "_ResearchData";

	public static final String RESEARCH_CRAFTING = "crafting";
	public static final String RESEARCH_SMELTING = "smelting";
	public static final String RESEARCH_TOOL_USE = "toolUsage";

	public static final int GUI_CRUDE_IO = 0;
	public static final int GUI_CRUDE_HEATER = 1;

	public static final ResourceLocation EMPTY_RESOURCE = new ResourceLocation("void:null");

	public static final String CATEGORY_BASIC_SMELTING = "SuperTechCore.Basic_Smelting";

	public static final ResourceLocation GUI_JEI_1 = new ResourceLocation(MODID, "textures/gui/jei_gui_1.png");
}