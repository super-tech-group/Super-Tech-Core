package com.supertechgroup.core.research;

import java.lang.reflect.Field;

import com.google.common.base.Throwables;

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

public class ShapedResearchRecipe extends ShapedOreRecipe {

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

	ComplexResearchRequirement req = new ComplexResearchRequirement(1);

	public ShapedResearchRecipe(ResourceLocation group, ItemStack result, Object[] recipe) {
		super(group, result, recipe);
	}

	/**
	 * Add another researchRequirement for this recipe. Note, each call of this adds
	 * an optional way to get this recipe. The player only needs to meet one of
	 * these requirements, but you can use PartialREsearch to make a more complex
	 * unlock
	 *
	 * @param rr
	 */
	public void addResearchUnlock(IResearchRequirement rr) {
		req.addRequirement(rr);
	}

	/**
	 * Do the research check to see if the player is allowed to make this
	 */
	@Override
	public boolean matches(InventoryCrafting inv, World world) {
		EntityPlayer p = findPlayer(inv);
		ResearchTeam team = ResearchSavedData.get(world).findPlayersResearchTeam(p.getUniqueID());
		return p != null && team != null && req.isFulfilled(team) && super.matches(inv, world);
	}

	public void setRequirementsNeeded(int num) {
		req.setNeeded(num);
	}
}