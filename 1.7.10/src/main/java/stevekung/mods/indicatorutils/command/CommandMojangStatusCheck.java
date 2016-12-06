/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import stevekung.mods.indicatorutils.utils.ThreadMojangStatusCheck;

public class CommandMojangStatusCheck extends CommandBase
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
        return "mojangstatus";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        new ThreadMojangStatusCheck(false).start();
    }
}