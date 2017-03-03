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
import net.minecraft.init.Items;
import net.minecraft.util.BlockPos;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandAutoFish extends ClientCommandBaseIU
{
    @Override
    public String getCommandName()
    {
        return "autofish";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtils json = new JsonUtils();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.autofish.usage");
        }
        else
        {
            if (args.length > 1)
            {
                throw new WrongUsageException("commands.autofish.usage");
            }
            if ("disable".equalsIgnoreCase(args[0]))
            {
                if (IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
                {
                    IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = false;
                    sender.addChatMessage(json.text("Disabled auto fish"));
                }
                else
                {
                    sender.addChatMessage(json.text("You have not start using /autofish command").setChatStyle(json.red()));
                }
            }
            else if ("enable".equalsIgnoreCase(args[0]))
            {
                if (!IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
                {
                    if (Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem() == null || Minecraft.getMinecraft().thePlayer.getCurrentEquippedItem().getItem() != Items.fishing_rod)
                    {
                        sender.addChatMessage(json.text("You are not held the fishing rod").setChatStyle(json.red()));
                        return;
                    }
                    else
                    {
                        IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = true;
                        sender.addChatMessage(json.text("Enabled auto fish"));
                        return;
                    }
                }
                else
                {
                    sender.addChatMessage(json.text("You have already start /autofish command").setChatStyle(json.red()));
                }
            }
            else
            {
                throw new WrongUsageException("commands.autofish.usage");
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable");
        }
        return super.addTabCompletionOptions(sender, args, pos);
    }
}