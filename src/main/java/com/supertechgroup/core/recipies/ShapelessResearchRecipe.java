package com.supertechgroup.core.recipies;

import java.lang.reflect.Field;
import java.util.UUID;

import com.google.common.base.Throwables;
import com.supertechgroup.core.research.ComplexResearchRequirement;
import com.supertechgroup.core.research.IResearchRequirement;
import com.supertechgroup.core.research.IUnlockable;
import com.supertechgroup.core.research.teams.teamcapability.TeamCapability;
import com.supertechgroup.core.research.teams.teamcapability.TeamCapabilityProvider;

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
import net.minecraftforge.oredict.ShapelessOreRecipe;

public class ShapelessResearchRecipe extends ShapelessOreRecipe implements IUnlockable {

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

	ComplexResearchRequirement required = new ComplexResearchRequirement(1);

	public ShapelessResearchRecipe(ResourceLocation group, ItemStack result, Object[] recipe,
			IResearchRequirement... requirements) {
		super(group, result, recipe);
		for (IResearchRequirement rr : requirements) {
			this.addResearchUnlock(rr);
		}
	}

	@Override
	public void addResearchUnlock(IResearchRequirement rr) {
		required.addRequirement(rr);
	}

	@Override
	public boolean isUnlocked() {
		return required.isFulfilled();
	}

	@Override
	public boolean isUnlocked(UUID team) {
		return required.isFulfilled(team);
	}

	/**
	 * Do the research check to see if the player is allowed to make this
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		EntityPlayer p = findPlayer(inv);
		UUID teamID = p.getCapability(TeamCapabilityProvider.TEAM_CAP, null).getTeam();
		return p != null && teamID != TeamCapability.NULL_TEAM && required.isFulfilled(teamID)
				&& super.matches(inv, world);
	}

	@Override
	public void setRequirementsNeeded(int num) {
		required.setRequiredCount(num);
	}
}