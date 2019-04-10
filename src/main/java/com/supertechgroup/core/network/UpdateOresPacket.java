package com.supertechgroup.core.network;

import com.supertechgroup.core.SuperTechCoreMod;
import com.supertechgroup.core.worldgen.OreSavedData;

import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.network.ByteBufUtils;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 *
 * @author oa10712
 */
public class UpdateOresPacket implements IMessage {

	public static class Handler implements IMessageHandler<UpdateOresPacket, IMessage> {

		@Override
		public IMessage onMessage(UpdateOresPacket message, MessageContext ctx) {
			if (SuperTechCoreMod.proxy.getWorld(null) != null) {
				OreSavedData.get(SuperTechCoreMod.proxy.getWorld(null)).readFromNBT(message.tag);
				OreSavedData.get(SuperTechCoreMod.proxy.getWorld(null)).markDirty();
			}

			return null;
		}

	}

	private NBTTagCompound tag;

	public UpdateOresPacket() {
	}

	public UpdateOresPacket(OreSavedData get, BlockPos pos) {
		tag = get.getForPos(pos);
	}

	public UpdateOresPacket(OreSavedData data, int chunkX, int chunkZ) {
		tag = data.getForChunk(chunkX, chunkZ);
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