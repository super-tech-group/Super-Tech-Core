package com.supertechgroup.core.research.teams;

import java.util.List;

import com.google.common.collect.Lists;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.capabilities.teamlist.IListCapability;
import com.supertechgroup.core.capabilities.teamlist.ListCapabilityProvider;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.DimensionManager;

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
			IListCapability listCap = DimensionManager.getWorld(0).getCapability(ListCapabilityProvider.TEAM_LIST_CAP,
					null);
			listCap.joinTeam(player);
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
