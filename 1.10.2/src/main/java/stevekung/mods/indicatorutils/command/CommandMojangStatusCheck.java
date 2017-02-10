/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatorutils.utils.ThreadMojangStatusCheck;

public class CommandMojangStatusCheck extends ClientCommandBaseIU
{
    @Override
    public String getCommandName()
    {
        return "mojangstatus";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        new ThreadMojangStatusCheck(false).start();
    }
}