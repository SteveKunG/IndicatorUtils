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
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandAutoFish extends CommandBase
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
        return "autofish";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 1)
        {
            if ("disable".equals(args[0]))
            {
                if (IndicatorUtilsEventHandler.autoFishEnabled == true)
                {
                    IndicatorUtilsEventHandler.autoFishEnabled = false;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Disabled auto fish", "white"));
                    return;
                }
                else
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("You have not start using /autofish command", "red"));
                    return;
                }
            }
            if ("enable".equals(args[0]))
            {
                if (IndicatorUtilsEventHandler.autoFishEnabled == false)
                {
                    if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() == null || Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() != Items.fishing_rod)
                    {
                        sender.addChatMessage(JsonMessageUtils.textToJson("You are not held the fishing rod", "red"));
                        return;
                    }
                    else
                    {
                        IndicatorUtilsEventHandler.autoFishEnabled = true;
                        sender.addChatMessage(JsonMessageUtils.textToJson("Enabled auto fish", "white"));
                        return;
                    }
                }
                else
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("You have already start /autofish command", "red"));
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.autofish.usage", new Object[] { this.getCommandUsage(sender) });
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable");
        }
        return Collections.<String>emptyList();
    }
}