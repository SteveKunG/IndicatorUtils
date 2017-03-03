/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandShowCape extends ClientCommandBaseIU
{
    @Override
    public String getCommandName()
    {
        return "capeiu";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
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

            if ("disable".equals(args[0]))
            {
                ExtendedModSettings.SHOW_CAPE = false;
                ExtendedModSettings.saveExtendedSettings();
                Minecraft.getMinecraft().gameSettings.showCape = true;
                Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
                Minecraft.getMinecraft().gameSettings.saveOptions();
                sender.addChatMessage(new JsonUtils().text("Disabled Custom Cape"));
                return;
            }
            if ("enable".equals(args[0]))
            {
                ExtendedModSettings.SHOW_CAPE = true;
                ExtendedModSettings.saveExtendedSettings();
                Minecraft.getMinecraft().gameSettings.showCape = false;
                Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
                Minecraft.getMinecraft().gameSettings.saveOptions();
                sender.addChatMessage(new JsonUtils().text("Enabled Custom Cape"));
                return;
            }
        }
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable");
        }
        return super.addTabCompletionOptions(sender, args);
    }
}