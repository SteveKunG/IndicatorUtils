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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandAFK extends ClientCommandBaseIU
{
    @Override
    public String getName()
    {
        return "afk";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtils json = new JsonUtils();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.afk.usage");
        }
        else
        {
            if ("stop".equalsIgnoreCase(args[0]))
            {
                if (args.length > 1)
                {
                    throw new WrongUsageException("commands.afk.usage");
                }
                if (IndicatorUtilsEventHandler.AFK_ENABLED)
                {
                    IndicatorUtilsEventHandler.AFK_ENABLED = false;
                    IndicatorUtilsEventHandler.AFK_MOVE_TICK = 0;

                    if (ConfigManager.enableAFKMessage)
                    {
                        Minecraft.getMinecraft().player.sendChatMessage("I'm back! AFK Time is : " + GameInfoHelper.INSTANCE.ticksToElapsedTime(IndicatorUtilsEventHandler.AFK_TICK) + " minutes");
                    }
                }
                else
                {
                    sender.sendMessage(json.text("You have not start using /afk command").setStyle(json.red()));
                }
            }
            else if ("start".equalsIgnoreCase(args[0]))
            {
                if (!IndicatorUtilsEventHandler.AFK_ENABLED)
                {
                    ITextComponent component = this.getChatComponentFromNthArg(args, 1);
                    TextComponentTranslation textcomponent = new TextComponentTranslation("commands.afk.reason", new Object[] { component.createCopy() });
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
                        Minecraft.getMinecraft().player.sendChatMessage(message + reason);
                    }
                }
                else
                {
                    sender.sendMessage(json.text("You have already start /afk command").setStyle(json.red()));
                }
            }
            else if ("changereason".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.afk.usage");
                }

                if (IndicatorUtilsEventHandler.AFK_ENABLED)
                {
                    String afkTemp = IndicatorUtilsEventHandler.AFK_REASON;
                    ITextComponent component = this.getChatComponentFromNthArg(args, 1);
                    TextComponentTranslation textcomponent = new TextComponentTranslation("commands.afk.reason", new Object[] { component.createCopy() });
                    String reason = textcomponent.getUnformattedText();
                    IndicatorUtilsEventHandler.AFK_REASON = reason;
                    sender.sendMessage(json.text("Change AFK Reason from " + afkTemp + " to " + reason));
                }
                else
                {
                    sender.sendMessage(json.text("You have not start using /afk command").setStyle(json.red()));
                }
            }
            else if ("mode".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1 || args.length > 2)
                {
                    throw new WrongUsageException("commands.afk.mode.usage");
                }

                if ("idle".equalsIgnoreCase(args[1]))
                {
                    IndicatorUtilsEventHandler.AFK_MODE = "idle";
                    IndicatorUtilsEventHandler.AFK_MOVE_TICK = 0;
                    sender.sendMessage(json.text("Set AFK mode to idle"));
                }
                else if ("move".equalsIgnoreCase(args[1]))
                {
                    IndicatorUtilsEventHandler.AFK_MODE = "move";
                    sender.sendMessage(json.text("Set AFK mode to move"));
                }
                else if ("360".equalsIgnoreCase(args[1]))
                {
                    IndicatorUtilsEventHandler.AFK_MODE = "360";
                    sender.sendMessage(json.text("Set AFK mode to 360"));
                }
                else if ("360move".equalsIgnoreCase(args[1]))
                {
                    IndicatorUtilsEventHandler.AFK_MODE = "360move";
                    sender.sendMessage(json.text("Set AFK mode to 360move"));
                }
                else
                {
                    throw new WrongUsageException("commands.afk.mode.usage");
                }
            }
            else
            {
                throw new WrongUsageException("commands.afk.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
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
        return super.getTabCompletions(server, sender, args, pos);
    }
}