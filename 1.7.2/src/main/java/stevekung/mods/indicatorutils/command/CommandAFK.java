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
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandAFK extends ClientCommandBaseIU
{
    @Override
    public String getCommandName()
    {
        return "afk";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtils json = new JsonUtils();

        if (args.length < 1)
        {
            this.addWrongUsageMessage(sender, "commands.afk.usage");
            return;
        }
        else
        {
            if ("stop".equalsIgnoreCase(args[0]))
            {
                if (args.length > 1)
                {
                    this.addWrongUsageMessage(sender, "commands.afk.usage");
                    return;
                }
                if (IndicatorUtilsEventHandler.AFK_ENABLED)
                {
                    IndicatorUtilsEventHandler.AFK_ENABLED = false;
                    IndicatorUtilsEventHandler.AFK_MOVE_TICK = 0;

                    if (ConfigManager.enableAFKMessage)
                    {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("I'm back! AFK Time is : " + GameInfoHelper.INSTANCE.ticksToElapsedTime(IndicatorUtilsEventHandler.AFK_TICK) + " minutes");
                    }
                }
                else
                {
                    sender.addChatMessage(json.text("You have not start using /afk command").setChatStyle(json.red()));
                }
            }
            else if ("start".equalsIgnoreCase(args[0]))
            {
                if (!IndicatorUtilsEventHandler.AFK_ENABLED)
                {
                    IChatComponent component = this.getChatComponentFromNthArg(args, 1);
                    ChatComponentTranslation textcomponent = new ChatComponentTranslation("commands.afk.reason", new Object[] { component.createCopy() });
                    String reason = textcomponent.getUnformattedText();
                    IndicatorUtilsEventHandler.AFK_ENABLED = true;
                    IndicatorUtilsEventHandler.AFK_REASON = reason;

                    if (reason.isEmpty())
                    {
                        reason = "";
                    }
                    else
                    {
                        reason = ", Reason : " + reason;
                    }

                    String message = "AFK for now";

                    if (ConfigManager.enableAFKMessage)
                    {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage(message + reason);
                    }
                }
                else
                {
                    sender.addChatMessage(json.text("You have already start /afk command").setChatStyle(json.red()));
                }
            }
            else if ("changereason".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    this.addWrongUsageMessage(sender, "commands.afk.usage");
                    return;
                }

                if (IndicatorUtilsEventHandler.AFK_ENABLED)
                {
                    String afkTemp = IndicatorUtilsEventHandler.AFK_REASON;
                    IChatComponent component = this.getChatComponentFromNthArg(args, 1);
                    ChatComponentTranslation textcomponent = new ChatComponentTranslation("commands.afk.reason", new Object[] { component.createCopy() });
                    String reason = textcomponent.getUnformattedText();
                    IndicatorUtilsEventHandler.AFK_REASON = reason;
                    sender.addChatMessage(json.text("Change AFK Reason from " + afkTemp + " to " + reason));
                }
                else
                {
                    sender.addChatMessage(json.text("You have not start using /afk command").setChatStyle(json.red()));
                }
            }
            else if ("mode".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1 || args.length > 2)
                {
                    this.addWrongUsageMessage(sender, "commands.afk.mode.usage");
                    return;
                }

                if ("idle".equalsIgnoreCase(args[1]))
                {
                    IndicatorUtilsEventHandler.AFK_MODE = "idle";
                    IndicatorUtilsEventHandler.AFK_MOVE_TICK = 0;
                    sender.addChatMessage(json.text("Set AFK mode to idle"));
                }
                else if ("move".equalsIgnoreCase(args[1]))
                {
                    IndicatorUtilsEventHandler.AFK_MODE = "move";
                    sender.addChatMessage(json.text("Set AFK mode to move"));
                }
                else if ("360".equalsIgnoreCase(args[1]))
                {
                    IndicatorUtilsEventHandler.AFK_MODE = "360";
                    sender.addChatMessage(json.text("Set AFK mode to 360"));
                }
                else if ("360move".equalsIgnoreCase(args[1]))
                {
                    IndicatorUtilsEventHandler.AFK_MODE = "360move";
                    sender.addChatMessage(json.text("Set AFK mode to 360move"));
                }
                else
                {
                    this.addWrongUsageMessage(sender, "commands.afk.mode.usage");
                    return;
                }
            }
            else
            {
                this.addWrongUsageMessage(sender, "commands.afk.usage");
                return;
            }
        }
    }

    @Override
    public List<?> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "start", "stop", "mode", "changereason");
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "idle", "move", "360", "360move");
            }
        }
        return super.addTabCompletionOptions(sender, args);
    }
}