package com.supertechgroup.core.recipies;

import java.lang.reflect.Field;

import com.google.common.base.Throwables;
import com.supertechgroup.core.research.IUnlockable;
import com.supertechgroup.core.research.ResearchSavedData;
import com.supertechgroup.core.research.teams.ResearchTeam;
import com.supertechgroup.core.research.teams.TeamCapabilityProvider;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.ContainerWorkbench;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

public class ShapedResearchRecipe extends ShapedOreRecipe implements IUnlockable {

	private static final Field eventHandlerField = ObfuscationReflectionHelper.findField(InventoryCrafting.class,
			"eventHandler");

	private static final Field containerPlayerPlayerField = ObfuscationReflectionHelper.findField(ContainerPlayer.class,
			"player");

	private static final Field slotCraftingPlayerField = ObfuscationReflectionHelper.findField(SlotCrafting.class,
			"player");

	private static EntityPlayer findPlayer(InventoryCrafting inv) {
		try {
			Container container = (Container) eventHandlerField.get(inv);
			if (container instanceof ContainerPlayer) {
				return (EntityPlayer) containerPlayerPlayerField.get(container);
			} else if (container instanceof ContainerWorkbench) {
				return (EntityPlayer) slotCraftingPlayerField.get(container.getSlot(0));
			} else {
				// don't know the player
				return null;
			}
		} catch (Exception e) {
			throw Throwables.propagate(e);
		}
	}

	public ShapedResearchRecipe(ResourceLocation group, ItemStack result, Object[] recipe) {
		super(group, result, recipe);
	}

	/**
	 * Do the research check to see if the player is allowed to make this
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		EntityPlayer p = findPlayer(inv);
		ResearchTeam team = ResearchSavedData.get(world)
				.getTeamByName(p.getCapability(TeamCapabilityProvider.TEAM_CAP, null).getTeam());
		return p != null && team != null && required.isFulfilled(team) && super.matches(inv, world);
	}
}