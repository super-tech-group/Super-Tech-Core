package com.supertechgroup.core.research;

import java.util.List;

import com.google.common.collect.Lists;
import com.supertechgroup.core.Reference;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class JoinResearchTeamCommmand extends CommandBase {

	private final List<String> aliases;

	public JoinResearchTeamCommmand() {
		aliases = Lists.newArrayList(Reference.MODID, "join", "Join");
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (sender instanceof EntityPlayer) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			ResearchSavedData rsd = ResearchSavedData.get(player.getServerWorld());
			rsd.joinTeam(player);
		}
	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public String getName() {
		return "join";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "join";
	}

}
