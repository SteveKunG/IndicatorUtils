/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.List;

import javax.xml.bind.DatatypeConverter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.common.ForgeHooks;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandAutoLogin extends CommandBase
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
        return "autologin";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (args.length < 1)
        {
            if (mc.isSingleplayer())
            {
                sender.addChatMessage(JsonMessageUtils.textToJson("Cannot use this command in singleplayer!", "red"));
                return;
            }
            else if (mc.isConnectedToRealms())
            {
                sender.addChatMessage(JsonMessageUtils.textToJson("Cannot use this command in realms server!", "red"));
                return;
            }
            else
            {
                throw new WrongUsageException("commands.autologin.usage");
            }
        }
        else
        {
            if ("add".equalsIgnoreCase(args[0]))
            {
                ServerData data = mc.getCurrentServerData();

                if (!mc.isSingleplayer() && !mc.isConnectedToRealms())
                {
                    if (data != null)
                    {
                        if (ExtendedModSettings.loginData.getAutoLogin(IndicatorUtils.USERNAME) != null)
                        {
                            sender.addChatMessage(JsonMessageUtils.textToJson("An auto login data already set for Username: " + IndicatorUtils.USERNAME + "!", "red"));
                            return;
                        }
                        ITextComponent component = this.getChatComponentFromNthArg(args, 2);
                        String value = component.createCopy().getUnformattedText();
                        ExtendedModSettings.loginData.addAutoLogin(data.serverIP, "/" + args[1] + " ", DatatypeConverter.printBase64Binary(value.getBytes()), IndicatorUtils.USERNAME);
                        sender.addChatMessage(JsonMessageUtils.textToJson("Set auto login data for Server: " + data.serverIP));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("Cannot add auto login data in singleplayer!", "red"));
                    return;
                }
                else if (mc.isConnectedToRealms())
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("Cannot add auto login data in realms server!", "red"));
                    return;
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                ServerData data = mc.getCurrentServerData();

                if (!mc.isSingleplayer() && !mc.isConnectedToRealms())
                {
                    if (data != null)
                    {
                        if (ExtendedModSettings.loginData.getAutoLogin(IndicatorUtils.USERNAME) != null)
                        {
                            ExtendedModSettings.loginData.removeAutoLogin(IndicatorUtils.USERNAME);
                            sender.addChatMessage(JsonMessageUtils.textToJson("Remove auto login data from Username: " + IndicatorUtils.USERNAME));
                        }
                        else
                        {
                            sender.addChatMessage(JsonMessageUtils.textToJson("No auto login data was set for Username: " + IndicatorUtils.USERNAME + "!", "red"));
                        }
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("Cannot remove auto login data in singleplayer!", "red"));
                    return;
                }
                else if (mc.isConnectedToRealms())
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("Cannot remove auto login data in realms server!", "red"));
                    return;
                }
            }
            else
            {
                throw new WrongUsageException("commands.autologin.usage");
            }
        }
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove");
        }
        return super.getTabCompletionOptions(server, sender, args, pos);
    }

    private ITextComponent getChatComponentFromNthArg(String[] args, int index)
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