package com.supertechgroup.core.network;

import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.capabilities.ore.IOreCapability;
import com.supertechgroup.core.capabilities.ore.OreCapabilityProvider;
import com.supertechgroup.core.capabilities.ore.OreCapabilityStorage;

import io.netty.buffer.ByteBuf;
import net.minecraft.client.Minecraft;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class UpdateOresPacket implements IMessage {

	private NBTTagCompound tag;

	public static class Handler implements IMessageHandler<UpdateOresPacket, IMessage> {

		@Override
		public IMessage onMessage(UpdateOresPacket message, MessageContext ctx) {
			if (ctx.side == Side.CLIENT) {
				Minecraft minecraft = Minecraft.getMinecraft();
				World w = minecraft.player.getEntityWorld();
				Chunk chunk = w.getChunkFromChunkCoords(message.tag.getInteger("x"), message.tag.getInteger("z"));

				IOreCapability cap = chunk.getCapability(OreCapabilityProvider.ORE_CAP, null);
				(new OreCapabilityStorage()).readNBT(OreCapabilityProvider.ORE_CAP, cap, null,
						message.tag.getTag("payload"));
				System.out.println("chunk watched: " + message.tag.toString());
			}

			return null;
		}

	}

	public UpdateOresPacket() {
	}

	public UpdateOresPacket(NBTBase tag2, int chunkX, int chunkZ) {
		tag = new NBTTagCompound();
		tag.setTag("payload", tag2);
		tag.setInteger("x", chunkX);
		tag.setInteger("z", chunkZ);
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
