/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.ArrayList;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandGetPlayerPosition extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/" + this.getCommandName();
    }

    @Override
    public String getCommandName()
    {
        return "getplayerpos";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.getplayerpos.fail", new Object[] { this.getCommandUsage(sender) });
        }
        else
        {
            EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[0]);

            if (player == null)
            {
                sender.addChatMessage(JsonMessageUtils.json("\"text\":\"" + I18n.format("commands.getplayerpos.playernull", args[0]) + "\",\"color\":\"red\""));
            }
            else
            {
                BlockPos pos = player.getPosition();
                sender.addChatMessage(new ChatComponentTranslation("commands.getplayerpos.success", new Object[] { player.getName(), pos.getX(), pos.getY(), pos.getZ(), player.worldObj.provider.getDimensionName() }));
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        NetHandlerPlayClient connection = Minecraft.getMinecraft().thePlayer.sendQueue;

        if (connection != null)
        {
            List<NetworkPlayerInfo> playerInfo = new ArrayList(connection.getPlayerInfoMap());
            List<String> playerList = Lists.<String>newArrayList();

            for (int i = 0; i < playerInfo.size(); ++i)
            {
                if (i < playerInfo.size())
                {
                    playerList.add(playerInfo.get(i).getGameProfile().getName());
                }
            }
            return CommandBase.getListOfStringsMatchingLastWord(args, playerList);
        }
        return super.addTabCompletionOptions(sender, args, pos);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}