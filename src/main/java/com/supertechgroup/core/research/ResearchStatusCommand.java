package com.supertechgroup.core.research;

import java.util.List;

import com.google.common.collect.Lists;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.capabilities.team.TeamCapabilityProvider;
import com.supertechgroup.core.capabilities.teamlist.IListCapability;
import com.supertechgroup.core.capabilities.teamlist.ListCapabilityProvider;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.DimensionManager;

public class ResearchStatusCommand extends CommandBase {

	private final List<String> aliases;

	public ResearchStatusCommand() {

		aliases = Lists.newArrayList(Reference.MODID, "Status", "status");
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public String getName() {
		return "status";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "status";
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {

		IListCapability listCap = DimensionManager.getWorld(0).getCapability(ListCapabilityProvider.TEAM_LIST_CAP,
				null);
		final StringBuilder message = new StringBuilder();
		if (sender instanceof EntityPlayerMP) {
			message.append("researched:\n");
			listCap.getCompletedForTeam(
					((EntityPlayerMP) sender).getCapability(TeamCapabilityProvider.TEAM_CAP, null).getTeam())
					.forEach((r) -> {
						// todo
						message.append(r.toString() + "\n");
					});
		} else {
			message.append("Only usable by players");
		}
		sender.sendMessage(new TextComponentString(message.toString()));
	}
}
