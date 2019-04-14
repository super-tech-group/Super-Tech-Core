package com.supertechgroup.core.network;

import java.util.Arrays;

import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.integration.jei.JEIMainPlugin;
import com.supertechgroup.core.research.Research;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

public class CompleteResearchPacket implements IMessage {
	public static class Handler implements IMessageHandler<CompleteResearchPacket, IMessage> {

		@Override
		public IMessage onMessage(CompleteResearchPacket message, MessageContext ctx) {
			if (SuperTechCoreMod.proxy.getWorld(null) != null) {
				NBTTagList list = message.tag.getTagList("complete", Constants.NBT.TAG_STRING);
				list.forEach((cr) -> {
					Research r = Research.REGISTRY.getValue(new ResourceLocation(((NBTTagString) cr).getString()));
					r.getUnlockedItems().forEach((item) -> {
						JEIMainPlugin.handleItemBlacklisting(item, false);
					});
					// TODO unlock recipies
				});
			}

			return null;
		}

	}

	private NBTTagCompound tag;

	public CompleteResearchPacket() {
	}

	public CompleteResearchPacket(Research... cr) {
		tag = new NBTTagCompound();
		NBTTagList list = new NBTTagList();
		System.out.println("Completing researches " + Arrays.toString(cr));
		for (Research r : cr) {
			list.appendTag(new NBTTagString(r.getRegistryName().toString()));
		}
		tag.setTag("complete", list);
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
