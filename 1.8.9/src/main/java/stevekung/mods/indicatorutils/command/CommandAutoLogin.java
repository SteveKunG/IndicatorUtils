/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.Collection;
import java.util.List;

import javax.xml.bind.DatatypeConverter;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.AutoLogin.AutoLoginData;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandAutoLogin extends ClientCommandBaseIU
{
    @Override
    public String getCommandName()
    {
        return "autologin";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtils json = new JsonUtils();
        Minecraft mc = Minecraft.getMinecraft();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.autologin.usage");
        }
        else
        {
            if ("add".equalsIgnoreCase(args[0]))
            {
                ServerData data = mc.getCurrentServerData();

                if (args.length <= 2)
                {
                    throw new WrongUsageException("commands.autologin.usage");
                }
                if (!mc.isSingleplayer())
                {
                    if (data != null)
                    {
                        if (ExtendedModSettings.loginData.getAutoLogin(IndicatorUtils.USERNAME + data.serverIP) != null)
                        {
                            sender.addChatMessage(json.text("An auto login data already set for Username: " + IndicatorUtils.USERNAME + "!").setChatStyle(json.red()));
                            return;
                        }
                        IChatComponent component = this.getChatComponentFromNthArg(args, 2);
                        String value = component.createCopy().getUnformattedText();
                        ExtendedModSettings.loginData.addAutoLogin(data.serverIP, "/" + args[1] + " ", DatatypeConverter.printBase64Binary(value.getBytes()), IndicatorUtils.USERNAME);
                        sender.addChatMessage(json.text("Set auto login data for Server: " + data.serverIP));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.addChatMessage(json.text("Cannot add auto login data in singleplayer!").setChatStyle(json.red()));
                    return;
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                ServerData data = mc.getCurrentServerData();

                if (!mc.isSingleplayer())
                {
                    if (data != null)
                    {
                        if (ExtendedModSettings.loginData.getAutoLogin(IndicatorUtils.USERNAME + data.serverIP) != null)
                        {
                            ExtendedModSettings.loginData.removeAutoLogin(IndicatorUtils.USERNAME + data.serverIP);
                            sender.addChatMessage(json.text("Remove auto login data from Username: " + IndicatorUtils.USERNAME));
                        }
                        else
                        {
                            sender.addChatMessage(json.text("No auto login data was set for Username: " + IndicatorUtils.USERNAME + "!").setChatStyle(json.red()));
                        }
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.addChatMessage(json.text("Cannot remove auto login data in singleplayer!").setChatStyle(json.red()));
                    return;
                }
            }
            else if ("list".equalsIgnoreCase(args[0]))
            {
                Collection<AutoLoginData> collection = ExtendedModSettings.loginData.getAutoLoginList();

                if (collection.isEmpty())
                {
                    throw new CommandException("commands.autologin.list.empty");
                }
                else
                {
                    ChatComponentTranslation textcomponenttranslation = new ChatComponentTranslation("commands.autologin.list.count", new Object[] {Integer.valueOf(collection.size())});
                    textcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
                    sender.addChatMessage(textcomponenttranslation);

                    for (AutoLoginData data : collection)
                    {
                        sender.addChatMessage(new ChatComponentTranslation("commands.autologin.list.entry", new Object[] {data.getServerIP(), data.getUsername()}));
                    }
                }
            }
            else
            {
                throw new WrongUsageException("commands.autologin.usage");
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "list");
        }
        return super.addTabCompletionOptions(sender, args, pos);
    }
}