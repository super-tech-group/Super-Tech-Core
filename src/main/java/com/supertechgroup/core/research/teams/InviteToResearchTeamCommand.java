package com.supertechgroup.core.research.teams;

import java.util.Arrays;
import java.util.List;

import com.google.common.collect.Lists;
import com.supertechgroup.core.Reference;
import com.supertechgroup.core.research.ResearchSavedData;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;

public class InviteToResearchTeamCommand extends CommandBase {

	private final List<String> aliases;

	public InviteToResearchTeamCommand() {
		aliases = Lists.newArrayList(Reference.MODID, "Invite", "invite");
	}

	@Override
	public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
		return true;
	}

	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if (args.length == 0) {
			return;
		}
		String s = args[0];
		if (sender instanceof EntityPlayer) {
			EntityPlayerMP player = (EntityPlayerMP) sender;
			ResearchSavedData rsd = ResearchSavedData.get(player.getServerWorld());
			ITeamCapability cap = player.getCapability(TeamCapabilityProvider.TEAM_CAP, null);
			if (!cap.getTeam().equals(TeamCapability.NULL_TEAM)) {
				if (Arrays.asList(server.getPlayerList().getOnlinePlayerNames()).contains(s)) {
					EntityPlayer otherPlayer = sender.getEntityWorld().getPlayerEntityByName(s);
					if (!s.equals(player.getName())) {
						otherPlayer.sendMessage(
								new TextComponentString(TextFormatting.GREEN + "You have been invited to join "
										+ cap.getTeam() + ". Type /join to join their team!"));
						player.sendMessage(
								new TextComponentString(TextFormatting.GREEN + "Your invite has been sent!"));
						rsd.addInvite(otherPlayer.getUniqueID(), rsd.getTeamByName(cap.getTeam()));
					} else {
						player.sendMessage(
								new TextComponentString(TextFormatting.RED + "Sorry, you can't invite yourself."));
					}
				} else {
					sender.sendMessage(
							new TextComponentString(TextFormatting.RED + "Can not find player to be invited."));
				}
			} else {
				sender.sendMessage(
						new TextComponentString(TextFormatting.RED + "You are not on a team to invite another player"));
			}

		}

	}

	@Override
	public List<String> getAliases() {
		return aliases;
	}

	@Override
	public String getName() {
		return "invite";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "invite <Player>";
	}

}
