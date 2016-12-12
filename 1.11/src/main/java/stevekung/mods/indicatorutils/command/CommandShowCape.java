/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EnumPlayerModelParts;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandShowCape extends CommandBase
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
        return "capeiu";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.capeiu.usage");
        }
        else
        {
            if (args.length > 1)
            {
                throw new WrongUsageException("commands.capeiu.usage");
            }

            if ("disable".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.SHOW_CAPE = false;
                ExtendedModSettings.saveExtendedSettings();
                Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
                Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
                Minecraft.getMinecraft().gameSettings.saveOptions();
                sender.addChatMessage(new JsonMessageUtils().text("Disabled Custom Cape"));
            }
            else if ("enable".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.SHOW_CAPE = true;
                ExtendedModSettings.saveExtendedSettings();
                Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
                Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
                Minecraft.getMinecraft().gameSettings.saveOptions();
                sender.addChatMessage(new JsonMessageUtils().text("Enabled Custom Cape"));
            }
        }
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable");
        }
        return Collections.<String>emptyList();
    }
}