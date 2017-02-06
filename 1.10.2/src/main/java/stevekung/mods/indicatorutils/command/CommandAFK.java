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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.common.ForgeHooks;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.JsonUtils;

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
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
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
                    sender.addChatMessage(JsonUtils.textToJson("You have not start using /afk command", "red"));
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
                    ITextComponent component = this.getChatComponentFromNthArg(args, 1);
                    TextComponentTranslation textcomponent = new TextComponentTranslation("commands.afk.reason", new Object[] { component.createCopy() });
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
                    sender.addChatMessage(JsonUtils.textToJson("You have already start /afk command", "red"));
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
                    ITextComponent component = this.getChatComponentFromNthArg(args, 1);
                    TextComponentTranslation textcomponent = new TextComponentTranslation("commands.afk.reason", new Object[] { component.createCopy() });
                    String reason = textcomponent.getUnformattedText();
                    IndicatorUtilsEventHandler.afkReason = reason;
                    sender.addChatMessage(JsonUtils.textToJson("Change AFK Reason from " + afkTemp + " to " + reason));
                    return;
                }
                else
                {
                    sender.addChatMessage(JsonUtils.textToJson("You have not start using /afk command", "red"));
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
                    sender.addChatMessage(JsonUtils.textToJson("Set AFK mode to idle"));
                    return;
                }
                if (args[1].equals("move"))
                {
                    IndicatorUtilsEventHandler.afkMode = "move";
                    sender.addChatMessage(JsonUtils.textToJson("Set AFK mode to move"));
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.afk.usage");
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
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

    public ITextComponent getChatComponentFromNthArg(String[] args, int index)
    {
        ITextComponent itextcomponent = new TextComponentString("");

        for (int i = index; i < args.length; ++i)
        {
            if (i > index)
            {
                itextcomponent.appendText(" ");
            }
            ITextComponent itextcomponent1 = ForgeHooks.newChatWithLinks(args[i]);
            itextcomponent.appendSibling(itextcomponent1);
        }
        return itextcomponent;
    }
}