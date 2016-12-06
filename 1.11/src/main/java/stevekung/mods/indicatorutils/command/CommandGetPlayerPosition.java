/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
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
                sender.addChatMessage(JsonMessageUtils.textToJson(I18n.format("commands.getplayerpos.playernull", args[0]), "red"));
            }
            else
            {
                BlockPos pos = player.getPosition();
                sender.addChatMessage(new TextComponentTranslation("commands.getplayerpos.success", new Object[] { player.getName(), pos.getX(), pos.getY(), pos.getZ(), player.worldObj.provider.getDimensionType().getName() }));
            }
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return CommandBase.getListOfStringsMatchingLastWord(args, GameInfoHelper.INSTANCE.getPlayerInfoListClient());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}