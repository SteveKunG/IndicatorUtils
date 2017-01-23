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
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.ForgeHooks;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.utils.AutoLogin.AutoLoginData;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandAutoLogin extends CommandBase
{
    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    @Override
    public String getUsage(ICommandSender sender)
    {
        return "/" + this.getName();
    }

    @Override
    public String getName()
    {
        return "autologin";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonMessageUtils json = new JsonMessageUtils();
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

                if (!mc.isSingleplayer() && !mc.isConnectedToRealms())
                {
                    if (data != null)
                    {
                        if (ExtendedModSettings.loginData.getAutoLogin(IndicatorUtils.USERNAME) != null)
                        {
                            sender.sendMessage(json.text("An auto login data already set for Username: " + IndicatorUtils.USERNAME + "!").setStyle(json.red()));
                            return;
                        }
                        ITextComponent component = this.getChatComponentFromNthArg(args, 2);
                        String value = component.createCopy().getUnformattedText();
                        ExtendedModSettings.loginData.addAutoLogin(data.serverIP, "/" + args[1] + " ", DatatypeConverter.printBase64Binary(value.getBytes()), IndicatorUtils.USERNAME);
                        sender.sendMessage(json.text("Set auto login data for Server: " + data.serverIP));
                        ExtendedModSettings.saveExtendedSettings();
                    }
                }
                else if (mc.isSingleplayer())
                {
                    sender.sendMessage(json.text("Cannot add auto login data in singleplayer!").setStyle(json.red()));
                    return;
                }
                else if (mc.isConnectedToRealms())
                {
                    sender.sendMessage(json.text("Cannot add auto login data in realms server!").setStyle(json.red()));
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
                else if (mc.isConnectedToRealms())
                {
                    sender.sendMessage(json.text("Cannot remove auto login data in realms server!").setStyle(json.red()));
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
    public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove", "list");
        }
        return super.getTabCompletions(server, sender, args, pos);
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