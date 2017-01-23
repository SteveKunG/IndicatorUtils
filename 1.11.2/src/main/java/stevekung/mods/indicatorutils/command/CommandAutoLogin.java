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
            if (mc.isSingleplayer())
            {
                sender.sendMessage(json.text("Cannot use this command in singleplayer!").setStyle(json.red()));
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

                if (data != null && !mc.isSingleplayer())
                {
                    if (ExtendedModSettings.loginData.getAutoLogin(data.serverIP) != null)
                    {
                        sender.sendMessage(json.text("An auto login data already set for Server: " + data.serverIP + "!").setStyle(json.red()));
                        return;
                    }
                    ITextComponent component = this.getChatComponentFromNthArg(args, 2);
                    String value = component.createCopy().getUnformattedText();
                    ExtendedModSettings.loginData.addAutoLogin(data.serverIP, "/" + args[1] + " ", DatatypeConverter.printBase64Binary(value.getBytes()));
                    sender.sendMessage(json.text("Set auto login data for Server: " + data.serverIP));
                    ExtendedModSettings.saveExtendedSettings();
                }
                else
                {
                    sender.sendMessage(json.text("Cannot add auto login data in singleplayer!").setStyle(json.red()));
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                ServerData data = mc.getCurrentServerData();

                if (data != null && !mc.isSingleplayer())
                {
                    if (ExtendedModSettings.loginData.getAutoLogin(data.serverIP) != null)
                    {
                        ExtendedModSettings.loginData.removeAutoLogin(data.serverIP);
                        sender.sendMessage(json.text("Remove auto login data from Server: " + data.serverIP));
                    }
                    else
                    {
                        sender.sendMessage(json.text("No auto login data was set for Server: " + data.serverIP + "!").setStyle(json.red()));
                    }
                }
                else
                {
                    sender.sendMessage(json.text("Cannot remove auto login data in singleplayer!").setStyle(json.red()));
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
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "remove");
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