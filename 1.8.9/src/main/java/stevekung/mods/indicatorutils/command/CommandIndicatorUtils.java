/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.settings.GameSettings;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.KeyBindingHandler;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandIndicatorUtils extends CommandBase
{
    @Override
    public List<String> getCommandAliases()
    {
        return Arrays.<String>asList("iu");
    }

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
        return "indicatorutils";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length > 0)
        {
            if (args[0].equals("help"))
            {
                sender.addChatMessage(JsonMessageUtils.json("\"text\":\"[Debug]: \",\"color\":\"gold\",\"bold\":\"true\",\"extra\":[{\"text\":\"" + GameSettings.getKeyDisplayString(KeyBindingHandler.KEY_DISPLAY_MODE_NEXT.getKeyCode()) + "/" + GameSettings.getKeyDisplayString(KeyBindingHandler.KEY_DISPLAY_MODE_PREVIOUS.getKeyCode()) + " = Switch Display Mode\",\"color\":\"white\",\"bold\":\"false\"}]"));
                sender.addChatMessage(JsonMessageUtils.json("\"text\":\"[Debug]: \",\"color\":\"gold\",\"bold\":\"true\",\"extra\":[{\"text\":\"" + GameSettings.getKeyDisplayString(KeyBindingHandler.KEY_TOGGLE_SPRINT.getKeyCode()) + " = Toggle Sprint\",\"color\":\"white\",\"bold\":\"false\"}]"));
                sender.addChatMessage(JsonMessageUtils.json("\"text\":\"[Debug]: \",\"color\":\"gold\",\"bold\":\"true\",\"extra\":[{\"text\":\"" + GameSettings.getKeyDisplayString(KeyBindingHandler.KEY_TOGGLE_SNEAK.getKeyCode()) + " = Toggle Sneak\",\"color\":\"white\",\"bold\":\"false\"}]"));
                sender.addChatMessage(JsonMessageUtils.json("\"text\":\"[Debug]: \",\"color\":\"gold\",\"bold\":\"true\",\"extra\":[{\"text\":\"" + GameSettings.getKeyDisplayString(KeyBindingHandler.KEY_AUTO_SWIM.getKeyCode()) + " = Auto Swim\",\"color\":\"white\",\"bold\":\"false\"}]"));
                sender.addChatMessage(JsonMessageUtils.json("\"text\":\"[Debug]: \",\"color\":\"gold\",\"bold\":\"true\",\"extra\":[{\"text\":\"" + GameSettings.getKeyDisplayString(KeyBindingHandler.KEY_REC_COMMAND.getKeyCode()) + " = Record Overlay\",\"color\":\"white\",\"bold\":\"false\"}]"));
                sender.addChatMessage(JsonMessageUtils.json("\"text\":\"[Debug]: \",\"color\":\"gold\",\"bold\":\"true\",\"extra\":[{\"text\":\"" + GameSettings.getKeyDisplayString(KeyBindingHandler.KEY_END_GAME_MESSAGE.getKeyCode()) + " = End Game Message\",\"color\":\"white\",\"bold\":\"false\"}]"));
                return;
            }
            else if (args[0].equals("togglesprint"))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.indicatorutils.togglesprint.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("enable") || args[1].equals("disable") || args[1].equals("mode")))
                {
                    throw new WrongUsageException("commands.indicatorutils.togglesprint.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("enable"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesprint.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set toggle sprint to Enabled"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("disable"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesprint.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.TOGGLE_SPRINT = false;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set toggle sprint to Disabled"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("mode"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesprint.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (!(args[2].equals("keybinding") || args[2].equals("command")))
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesprint.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (args[2].equals("keybinding"))
                    {
                        ExtendedModSettings.TOGGLE_SPRINT_USE_MODE = "keybinding";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set toggle sprint to use Key Binding"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                    if (args[2].equals("command"))
                    {
                        ExtendedModSettings.TOGGLE_SPRINT_USE_MODE = "command";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set toggle sprint to use Command"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                }
            }
            else if (args[0].equals("togglesneak"))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.indicatorutils.togglesneak.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("enable") || args[1].equals("disable") || args[1].equals("mode")))
                {
                    throw new WrongUsageException("commands.indicatorutils.togglesneak.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("enable"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesneak.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set toggle sneak to Enabled"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("disable"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesneak.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.TOGGLE_SNEAK = false;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set toggle sneak to Disabled"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("mode"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesneak.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (!(args[2].equals("keybinding") || args[2].equals("command")))
                    {
                        throw new WrongUsageException("commands.indicatorutils.togglesneak.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (args[2].equals("keybinding"))
                    {
                        ExtendedModSettings.TOGGLE_SNEAK_USE_MODE = "keybinding";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set toggle sneak to use Key Binding"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                    if (args[2].equals("command"))
                    {
                        ExtendedModSettings.TOGGLE_SNEAK_USE_MODE = "command";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set toggle sneak to use Command"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                }
            }
            else if (args[0].equals("cps"))
            {
                if (args.length != 2)
                {
                    throw new WrongUsageException("commands.indicatorutils.cps.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("left") || args[1].equals("right") || args[1].equals("record")))
                {
                    throw new WrongUsageException("commands.indicatorutils.cps.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("left"))
                {
                    ExtendedModSettings.CPS_POSITION = "left";
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set CPS position to Left"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("right"))
                {
                    ExtendedModSettings.CPS_POSITION = "right";
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set CPS position to Right"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("record"))
                {
                    ExtendedModSettings.CPS_POSITION = "record";
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set CPS position to Record"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
            }
            else if (args[0].equals("keystroke"))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.indicatorutils.keystroke.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("reset") || args[1].equals("y") || args[1].equals("x")))
                {
                    throw new WrongUsageException("commands.indicatorutils.keystroke.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("reset"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.keystroke.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.KETSTROKE_Y_OFFSET = 0;
                    ExtendedModSettings.KETSTROKE_X_OFFSET = 0;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Reset Keystroke Offset"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("y"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.keystroke.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.KETSTROKE_Y_OFFSET = CommandBase.parseInt(args[2]);
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set Keystroke Y Offset to " + args[2]));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("x"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.keystroke.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.KETSTROKE_X_OFFSET = CommandBase.parseInt(args[2]);
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set Keystroke X Offset to " + args[2]));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
            }
            else if (args[0].equals("displaymode"))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.indicatorutils.displaymode.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("default") || args[1].equals("uhc") || args[1].equals("pvp") || args[1].equals("command") || args[1].equals("mode")))
                {
                    throw new WrongUsageException("commands.indicatorutils.displaymode.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("default"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.displaymode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.DISPLAY_MODE = "default";
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set display mode to Default"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("pvp"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.displaymode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.DISPLAY_MODE = "pvp";
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set display mode to PvP"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("uhc"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.displaymode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.DISPLAY_MODE = "uhc";
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set display mode to UHC"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("command"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.displaymode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.DISPLAY_MODE = "command";
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set display mode to Command Block"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("mode"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.displaymode.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (!(args[2].equals("keybinding") || args[2].equals("command")))
                    {
                        throw new WrongUsageException("commands.indicatorutils.displaymode.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (args[2].equals("keybinding"))
                    {
                        ExtendedModSettings.DISPLAY_MODE_USE_MODE = "keybinding";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set display mode to use Key Binding"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                    if (args[2].equals("command"))
                    {
                        ExtendedModSettings.DISPLAY_MODE_USE_MODE = "command";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set display mode to use Command"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                }
            }
            else if (args[0].equals("autoswim"))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.indicatorutils.autoswim.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("enable") || args[1].equals("disable") || args[1].equals("mode")))
                {
                    throw new WrongUsageException("commands.indicatorutils.autoswim.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("enable"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoswim.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.AUTO_SWIM = true;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set auto swim to Enabled"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("disable"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoswim.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.AUTO_SWIM = false;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set auto swim to Disabled"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("mode"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoswim.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (!(args[2].equals("keybinding") || args[2].equals("command")))
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoswim.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (args[2].equals("keybinding"))
                    {
                        ExtendedModSettings.AUTO_SWIM_USE_MODE = "keybinding";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set auto swim to use Key Binding"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                    if (args[2].equals("command"))
                    {
                        ExtendedModSettings.AUTO_SWIM_USE_MODE = "command";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set auto swim to use Command"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                }
            }
            else if (args[0].equals("autoclearchat"))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.indicatorutils.autoclearchat.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("enable") || args[1].equals("disable") || args[1].equals("mode") || args[1].equals("set")))
                {
                    throw new WrongUsageException("commands.indicatorutils.autoclearchat.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("enable"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.AUTO_CLEAR_CHAT = true;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set auto clear chat to Enabled"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("disable"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.AUTO_CLEAR_CHAT = false;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set auto clear chat to Disabled"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("mode"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (!(args[2].equals("all") || args[2].equals("onlychat") || args[2].equals("onlysentmessage")))
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.mode.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    if (args[2].equals("all"))
                    {
                        ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = "all";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set auto clear chat mode to clear all"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                    if (args[2].equals("onlychat"))
                    {
                        ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = "onlychat";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set auto clear chat mode to chat only"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                    if (args[2].equals("onlysentmessage"))
                    {
                        ExtendedModSettings.AUTO_CLEAR_CHAT_MODE = "onlysentmessage";
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set auto clear chat mode to sent message only"));
                        ExtendedModSettings.saveExtendedSettings();
                        return;
                    }
                }
                if (args[1].equals("set"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.autoclearchat.set.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.AUTO_CLEAR_CHAT_TIME = CommandBase.parseInt(args[2], 1);
                    String s = "Set auto clear chat time to " + CommandBase.parseInt(args[2], 1) + " second";

                    if (CommandBase.parseInt(args[2], 1) > 1)
                    {
                        s = s + "s";
                    }
                    sender.addChatMessage(JsonMessageUtils.textToJson(s));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
            }
            else if (args[0].equals("armorstatus"))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.indicatorutils.armorstatus.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("reset") || args[1].equals("y")))
                {
                    throw new WrongUsageException("commands.indicatorutils.armorstatus.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("reset"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.armorstatus.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.ARMOR_STATUS_OFFSET = 0;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Reset Armor Status Offset"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("y"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.armorstatus.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.ARMOR_STATUS_OFFSET = CommandBase.parseInt(args[2]);
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set Armor Status Y Offset to " + CommandBase.parseInt(args[2])));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
            }
            else if (args[0].equals("potionstatus"))
            {
                if (args.length != 2 && args.length != 3)
                {
                    throw new WrongUsageException("commands.indicatorutils.potionstatus.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (!(args[1].equals("reset") || args[1].equals("y")))
                {
                    throw new WrongUsageException("commands.indicatorutils.potionstatus.usage", new Object[] { this.getCommandUsage(sender) });
                }
                if (args[1].equals("reset"))
                {
                    if (args.length != 2)
                    {
                        throw new WrongUsageException("commands.indicatorutils.potionstatus.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.POTION_STATUS_OFFSET = 0;
                    sender.addChatMessage(JsonMessageUtils.textToJson("Reset Potion Status Offset"));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
                if (args[1].equals("y"))
                {
                    if (args.length != 3 && args.length != 4)
                    {
                        throw new WrongUsageException("commands.indicatorutils.potionstatus.usage", new Object[] { this.getCommandUsage(sender) });
                    }
                    ExtendedModSettings.POTION_STATUS_OFFSET = CommandBase.parseInt(args[2]);
                    sender.addChatMessage(JsonMessageUtils.textToJson("Set Potion Status Y Offset to " + CommandBase.parseInt(args[2])));
                    ExtendedModSettings.saveExtendedSettings();
                    return;
                }
            }
        }
        throw new WrongUsageException("commands.indicatorutils.usage", new Object[] { this.getCommandUsage(sender) });
    }

    @Override
    public List addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "help", "displaymode", "togglesprint", "togglesneak", "cps", "keystroke", "autoclearchat", "armorstatus", "potionstatus", "autoswim");
        }
        if (args.length == 2)
        {
            if (args[0].equals("displaymode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "default", "uhc", "pvp", "command", "mode");
            }
            if (args[0].equals("autoclearchat"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable", "mode", "set");
            }
            if (args[0].equals("togglesprint") || args[0].equals("togglesneak") || args[0].equals("autoswim"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable", "mode");
            }
            if (args[0].equals("cps"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "left", "right", "record");
            }
            if (args[0].equals("keystroke"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "reset", "x", "y");
            }
            if (args[0].equals("armorstatus"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "reset", "y");
            }
            if (args[0].equals("potionstatus"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "reset", "y");
            }
        }
        if (args.length == 3)
        {
            if ((args[0].equals("togglesprint") || args[0].equals("togglesneak") || args[0].equals("displaymode") || args[0].equals("autoswim")) && args[1].equals("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "keybinding", "command");
            }
            if (args[0].equals("autoclearchat") && args[1].equals("mode"))
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, "all", "onlychat", "onlysentmessage");
            }
        }
        return Collections.<String>emptyList();
    }
}