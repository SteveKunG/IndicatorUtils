/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.Collections;
import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandRecTemp extends CommandBase
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
        return "rectemp";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.rectemp.usage");
        }
        else
        {
            if (args.length > 1)
            {
                throw new WrongUsageException("commands.rectemp.usage");
            }

            if ("stop".equalsIgnoreCase(args[0]))
            {
                if (IndicatorUtilsEventHandler.recEnabled)
                {
                    IndicatorUtilsEventHandler.recEnabled = false;
                }
                else
                {
                    sender.addChatMessage(new JsonMessageUtils().text("You have not start using /rectemp command").setStyle(new JsonMessageUtils().red()));
                }
            }
            else if ("start".equalsIgnoreCase(args[0]))
            {
                if (!IndicatorUtilsEventHandler.recEnabled)
                {
                    IndicatorUtilsEventHandler.recEnabled = true;
                }
                else
                {
                    sender.addChatMessage(new JsonMessageUtils().text("You have already start /rectemp command").setStyle(new JsonMessageUtils().red()));
                }
            }
            else
            {
                throw new WrongUsageException("commands.rectemp.usage");
            }
        }
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "start", "stop");
        }
        return Collections.<String>emptyList();
    }
}