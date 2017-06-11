/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.Collection;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.AutoLogin.AutoLoginData;
import stevekung.mods.indicatorutils.utils.Base64Utils;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandAutoLogin extends ClientCommandBaseIU
{
    @Override
    public String getName()
    {
        return "autologin";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
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
                            sender.sendMessage(json.text("An auto login data already set for Username: " + IndicatorUtils.USERNAME + "!").setStyle(json.red()));
                            return;
                        }
                        ITextComponent component = this.getChatComponentFromNthArg(args, 2);
                        String value = component.createCopy().getUnformattedText();
                        ExtendedModSettings.loginData.addAutoLogin(data.serverIP, "/" + args[1] + " ", Base64Utils.encode(value), IndicatorUtils.USERNAME);
                        sender.sendMessage(json.text("Set auto login data for Server: " + data.serverIP));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.sendMessage(json.text("Cannot add auto login data in singleplayer!").setStyle(json.red()));
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
                            sender.sendMessage(json.text("Remove auto login data from Username: " + IndicatorUtils.USERNAME));
                        }
                        else
                        {
                            sender.sendMessage(json.text("No auto login data was set for Username: " + IndicatorUtils.USERNAME + "!").setStyle(json.red()));
                        }
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.sendMessage(json.text("Cannot remove auto login data in singleplayer!").setStyle(json.red()));
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
                    TextComponentTranslation textcomponenttranslation = new TextComponentTranslation("commands.autologin.list.count", new Object[] {Integer.valueOf(collection.size())});
                    textcomponenttranslation.getStyle().setColor(TextFormatting.DARK_GREEN);
                    sender.sendMessage(textcomponenttranslation);

                    for (AutoLoginData data : collection)
                    {
                        sender.sendMessage(new TextComponentTranslation("commands.autologin.list.entry", new Object[] {data.getServerIP(), data.getUsername()}));
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
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "list");
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}