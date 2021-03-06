package stevekung.mods.indicatorutils.command;

import java.util.List;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.gui.GuiRenderStatusSettings;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandIndicatorUtils extends ClientCommandBaseIU
{
    @Override
    public String getName()
    {
        return "iu";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtils json = new JsonUtils();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.indicatorutils.usage");
        }
        else
        {
            if ("togglesprint".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.indicatorutils.togglesprint.usage");
                }

                if ("enable".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    sender.sendMessage(json.text("Set toggle sprint to Enabled"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.TOGGLE_SPRINT = false;
                    sender.sendMessage(json.text("Set toggle sprint to Disabled"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("mode".equalsIgnoreCase(args[1]))
                {
                    if (args.length < 3 || args.length > 3)
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesprint.mode.usage");
                    }

                    if ("keybinding".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.TOGGLE_SPRINT_USE_MODE = "keybinding";
                        sender.sendMessage(json.text("Set toggle sprint to use Key Binding"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                    else if ("command".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.TOGGLE_SPRINT_USE_MODE = "command";
                        sender.sendMessage(json.text("Set toggle sprint to use Command"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                    else
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesprint.mode.usage");
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.indicatorutils.togglesprint.usage");
                }
            }
            else if ("togglesneak".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.indicatorutils.togglesneak.usage");
                }

                if ("enable".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    sender.sendMessage(json.text("Set toggle sneak to Enabled"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.TOGGLE_SNEAK = false;
                    sender.sendMessage(json.text("Set toggle sneak to Disabled"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("mode".equalsIgnoreCase(args[1]))
                {
                    if (args.length < 3 || args.length > 3)
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesneak.mode.usage");
                    }
                    if ("keybinding".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.TOGGLE_SNEAK_USE_MODE = "keybinding";
                        sender.sendMessage(json.text("Set toggle sneak to use Key Binding"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                    else if ("command".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.TOGGLE_SNEAK_USE_MODE = "command";
                        sender.sendMessage(json.text("Set toggle sneak to use Command"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                    else
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesneak.mode.usage");
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.indicatorutils.togglesneak.usage");
                }
            }
            else if ("cps".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1 || args.length > 2)
                {
                    throw new WrongUsageException("commands.indicatorutils.cps.usage");
                }

                if ("left".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.CPS_POSITION = "left";
                    sender.sendMessage(json.text("Set CPS position to Left"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("right".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.CPS_POSITION = "right";
                    sender.sendMessage(json.text("Set CPS position to Right"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("record".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.CPS_POSITION = "record";
                    sender.sendMessage(json.text("Set CPS position to Record"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("keystroke".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.CPS_POSITION = "keystroke";
                    sender.sendMessage(json.text("Set CPS position to Keystroke"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("custom".equalsIgnoreCase(args[1]))
                {
                    ExtendedModSettings.CPS_POSITION = "custom";
                    sender.sendMessage(json.text("Set CPS position to Customize"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else
                {
                    throw new WrongUsageException("commands.indicatorutils.cps.usage");
                }
            }
            else if ("autoswim".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.indicatorutils.autoswim.usage");
                }

                if ("enable".equalsIgnoreCase(args[1]))
                {
                    if (args.length > 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoswim.usage");
                    }
                    ExtendedModSettings.AUTO_SWIM = true;
                    sender.sendMessage(json.text("Set auto swim to Enabled"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    if (args.length > 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoswim.usage");
                    }
                    ExtendedModSettings.AUTO_SWIM = false;
                    sender.sendMessage(json.text("Set auto swim to Disabled"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("mode".equalsIgnoreCase(args[1]))
                {
                    if (args.length == 2 || args.length > 3)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoswim.mode.usage");
                    }

                    if ("keybinding".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.AUTO_SWIM_USE_MODE = "keybinding";
                        sender.sendMessage(json.text("Set auto swim to use Key Binding"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                    else if ("command".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.AUTO_SWIM_USE_MODE = "command";
                        sender.sendMessage(json.text("Set auto swim to use Command"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                    else
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoswim.mode.usage");
                    }
                }
                else
                {
                    throw new WrongUsageException("commands.indicatorutils.autoswim.usage");
                }
            }
            else if ("autoclearchat".equalsIgnoreCase(args[0]))
            {
                if (args.length == 1)
                {
                    throw new WrongUsageException("commands.indicatorutils.autoclearchat.usage");
                }

                if ("enable".equalsIgnoreCase(args[1]))
                {
                    if (args.length > 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.usage");
                    }
                    ExtendedModSettings.AUTO_CLEAR_CHAT = true;
                    sender.sendMessage(json.text("Set auto clear chat to Enabled"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("disable".equalsIgnoreCase(args[1]))
                {
                    if (args.length > 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.usage");
                    }
                    ExtendedModSettings.AUTO_CLEAR_CHAT = false;
                    sender.sendMessage(json.text("Set auto clear chat to Disabled"));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else if ("mode".equalsIgnoreCase(args[1]))
                {
                    if (args.length == 2 || args.length > 3)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.mode.usage");
                    }

                    if ("all".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = "all";
                        sender.sendMessage(json.text("Set auto clear chat mode to clear all"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                    if ("onlychat".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = "onlychat";
                        sender.sendMessage(json.text("Set auto clear chat mode to chat only"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                    if ("onlysentmessage".equalsIgnoreCase(args[2]))
                    {
                        ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = "onlysentmessage";
                        sender.sendMessage(json.text("Set auto clear chat mode to sent message only"));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                }
                if ("set".equalsIgnoreCase(args[1]))
                {
                    if (args.length == 2 || args.length > 3)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.set.usage");
                    }

                    ExtendedModSettings.AUTO_CLEAR_CHAT_TIME = CommandBase.parseInt(args[2], 1);
                    String s = "Set auto clear chat time to " + CommandBase.parseInt(args[2], 1) + " second";

                    if (CommandBase.parseInt(args[2], 1) > 1)
                    {
                        s = s + "s";
                    }
                    sender.sendMessage(json.text(s));
                    ExtendedModSettings.saveExtendedSettings();
                }
            }
            else if (args[0].equalsIgnoreCase("gui"))
            {
                new GuiRenderStatusSettings().display();
            }
            else
            {
                throw new WrongUsageException("commands.indicatorutils.usage");
            }
        }
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "togglesprint", "togglesneak", "cps", "autoclearchat", "autoswim", "gui");
        }
        if (args.length == 2)
        {
            if (args[0].equalsIgnoreCase("autoclearchat"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable", "mode", "set");
            }
            if (args[0].equalsIgnoreCase("togglesprint") || args[0].equalsIgnoreCase("togglesneak") || args[0].equalsIgnoreCase("autoswim"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable", "mode");
            }
            if (args[0].equalsIgnoreCase("cps"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "left", "right", "record", "keystroke", "custom");
            }
        }
        if (args.length == 3)
        {
            if ((args[0].equalsIgnoreCase("togglesprint") || args[0].equalsIgnoreCase("togglesneak") || args[0].equalsIgnoreCase("autoswim")) && args[1].equalsIgnoreCase("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "keybinding", "command");
            }
            if (args[0].equalsIgnoreCase("autoclearchat") && args[1].equalsIgnoreCase("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "all", "onlychat", "onlysentmessage");
            }
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}