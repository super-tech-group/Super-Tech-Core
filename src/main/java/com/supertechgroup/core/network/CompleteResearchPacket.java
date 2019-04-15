package com.supertechgroup.core.network;

import java.util.ArrayList;
import java.util.Arrays;

import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.integration.jei.JEIMainPlugin;
import com.supertechgroup.core.research.IUnlockable;
import com.supertechgroup.core.research.Research;
import com.supertechgroup.core.research.ResearchSavedData;
import com.supertechgroup.core.research.ResearchTeam;

import io.netty.buffer.ByteBuf;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.registry.GameRegistry;

public class CompleteResearchPacket implements IMessage {
	public static ArrayList<Research> clientCompleted = new ArrayList<>();

	public static class Handler implements IMessageHandler<CompleteResearchPacket, IMessage> {

		@Override
		public IMessage onMessage(CompleteResearchPacket message, MessageContext ctx) {
			if (SuperTechCoreMod.proxy.getWorld(null) != null) {
				NBTTagList list = message.tag.getTagList("complete", Constants.NBT.TAG_STRING);
				list.forEach((cr) -> {
					Research r = Research.REGISTRY.getValue(new ResourceLocation(((NBTTagString) cr).getString()));
					clientCompleted.add(r);
				});
				GameRegistry.findRegistry(IRecipe.class).forEach((iRecipe) -> {
					if (iRecipe instanceof IUnlockable) {
						IUnlockable unlockable = (IUnlockable) iRecipe;
						if (unlockable.isUnlocked()) {
							JEIMainPlugin.handleItemBlacklisting(iRecipe.getRecipeOutput(), false);
						}
					}
				});

			}

			return null;
		}

	}

	private NBTTagCompound tag;

	public CompleteResearchPacket() {
	}

	public CompleteResearchPacket(ResearchTeam team, Research... cr) {
		tag = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		System.out.println("Completing researches " + Arrays.toString(cr));
		for (Research r : cr) {
			list.appendTag(new NBTTagString(r.getRegistryName().toString()));
		}
		tag.setTag("complete", list);
		tag.setString("team", team.getTeamName());
	}

	@Override
	public void fromBytes(ByteBuf buf) {
		tag = ByteBufUtils.readTag(buf);
	}

	@Override
	public void toBytes(ByteBuf buf) {
		ByteBufUtils.writeTag(buf, tag);
	}

}
