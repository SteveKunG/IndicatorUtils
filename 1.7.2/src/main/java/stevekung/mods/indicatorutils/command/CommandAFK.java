/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.net.URI;
import java.util.Collections;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.event.ClickEvent;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.IChatComponent;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;

public class CommandAFK extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "commands.afk.usage";
    }

    @Override
    public String getCommandName()
    {
        return "afk";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length == 1)
        {
            if ("stop".equals(args[0]))
            {
                if (IndicatorUtilsEventHandler.afkEnabled == true)
                {
                    IndicatorUtilsEventHandler.afkEnabled = false;
                    IndicatorUtilsEventHandler.afkMoveTick = 0;

                    if (ConfigManager.enableAFKMessage)
                    {
                        Minecraft.getMinecraft().thePlayer.sendChatMessage("I'm back! AFK Time is : " + GameInfoHelper.INSTANCE.ticksToElapsedTime(IndicatorUtilsEventHandler.afkTick) + " minutes");
                    }
                    return;
                }
                else
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("You have not start using /afk command", "red"));
                    return;
                }
            }
        }
        if (args.length == 1 || args.length >= 2)
        {
            if ("start".equals(args[0]))
            {
                if (IndicatorUtilsEventHandler.afkEnabled == false)
                {
                    IChatComponent component = this.getChatComponentFromNthArg(args, 1);
                    ChatComponentTranslation textcomponent = new ChatComponentTranslation("commands.afk.reason", new Object[] { component.createCopy() });
                    String reason = textcomponent.getUnformattedText();
                    IndicatorUtilsEventHandler.afkEnabled = true;
                    IndicatorUtilsEventHandler.afkReason = reason;

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
                    return;
                }
                else
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("You have already start /afk command", "red"));
                    return;
                }
            }
        }
        if (args.length >= 2)
        {
            if ("changereason".equals(args[0]))
            {
                if (IndicatorUtilsEventHandler.afkEnabled)
                {
                    String afkTemp = IndicatorUtilsEventHandler.afkReason;
                    IChatComponent component = this.getChatComponentFromNthArg(args, 1);
                    ChatComponentTranslation textcomponent = new ChatComponentTranslation("commands.afk.reason", new Object[] { component.createCopy() });
                    String reason = textcomponent.getUnformattedText();
                    IndicatorUtilsEventHandler.afkReason = reason;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Change AFK Reason from " + afkTemp + " to " + reason));
                    return;
                }
                else
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("You have not start using /afk command", "red"));
                    return;
                }
            }
        }
        if (args.length == 2)
        {
            if ("mode".equals(args[0]))
            {
                if (args[1].equals("idle"))
                {
                    IndicatorUtilsEventHandler.afkMode = "idle";
                    IndicatorUtilsEventHandler.afkMoveTick = 0;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set AFK mode to idle"));
                    return;
                }
                if (args[1].equals("move"))
                {
                    IndicatorUtilsEventHandler.afkMode = "move";
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set AFK mode to move"));
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.afk.usage");
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "start", "stop", "mode", "changereason");
        }
        if (args.length == 2)
        {
            if (args[0].equals("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "idle", "move");
            }
        }
        return Collections.<String>emptyList();
    }

    public IChatComponent getChatComponentFromNthArg(String[] args, int index)
    {
        IChatComponent itextcomponent = new ChatComponentText("");

        for (int i = index; i < args.length; ++i)
        {
            if (i > index)
            {
                itextcomponent.appendText(" ");
            }
            IChatComponent itextcomponent1 = this.newChatWithLinks(args[i]);
            itextcomponent.appendSibling(itextcomponent1);
        }
        return itextcomponent;
    }

    public IChatComponent newChatWithLinks(String string)
    {
        // Includes ipv4 and domain pattern
        // Matches an ip (xx.xxx.xx.xxx) or a domain (something.com) with or
        // without a protocol or path.
        final Pattern URL_PATTERN = Pattern.compile(
                //         schema                          ipv4            OR           namespace                 port     path         ends
                //   |-----------------|        |-------------------------|  |----------------------------|    |---------| |--|   |---------------|
                "((?:[a-z0-9]{2,}:\\/\\/)?(?:(?:[0-9]{1,3}\\.){3}[0-9]{1,3}|(?:[-\\w_\\.]{1,}\\.[a-z]{2,}?))(?::[0-9]{1,5})?.*?(?=[!\"\u00A7 \n]|$))",
                Pattern.CASE_INSENSITIVE);
        IChatComponent ichat = new ChatComponentText("");
        Matcher matcher = URL_PATTERN.matcher(string);
        int lastEnd = 0;
        // Find all urls
        while (matcher.find())
        {
            int start = matcher.start();
            int end = matcher.end();

            // Append the previous left overs.
            ichat.appendText(string.substring(lastEnd, start));
            lastEnd = end;
            String url = string.substring(start, end);
            IChatComponent link = new ChatComponentText(url);

            // Add schema so client doesn't crash.
            if (URI.create(url).getScheme() == null)
            {
                url = "http://" + url;
            }

            // Set the click event and append the link.
            ClickEvent click = new ClickEvent(ClickEvent.Action.OPEN_URL, url);
            link.getChatStyle().setChatClickEvent(click);
            ichat.appendSibling(link);
        }

        // Append the rest of the message.
        ichat.appendText(string.substring(lastEnd));
        return ichat;
    }
}