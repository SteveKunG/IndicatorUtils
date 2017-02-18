/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import net.minecraft.client.Minecraft;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandSetNickHypixel extends ClientCommandBaseIU
{
    @Override
    public String getName()
    {
        return "setnick";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.setnick.usage");
        }
        else
        {
            if (GameInfoHelper.INSTANCE.isHypixel())
            {
                ExtendedModSettings.HYPIXEL_NICK_NAME = args[0];
                Minecraft.getMinecraft().player.sendChatMessage("/nick " + args[0]);
                return;
            }
            else
            {
                sender.sendMessage(new JsonUtils().text("You are not in the Hypixel Server").setStyle(new JsonUtils().red()));
            }
        }
    }
}