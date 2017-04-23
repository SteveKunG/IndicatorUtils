/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.ITextComponent;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandAutoRealms extends ClientCommandBaseIU
{
    @Override
    public String getName()
    {
        return "autorealms";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtils json = new JsonUtils();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.autorealms.usage");
        }
        else
        {
            ITextComponent component = this.getChatComponentFromNthArg(args, 0);
            String text = component.getUnformattedText();

            if (text.equals("reset"))
            {
                ExtendedModSettings.REALMS_MESSAGE = "";
                sender.sendMessage(json.text("Reset realm message"));
                ExtendedModSettings.saveExtendedSettings();
            }
            else
            {
                ExtendedModSettings.REALMS_MESSAGE = text;
                sender.sendMessage(json.text("Set realm message to " + "\"" + text + "\""));
                ExtendedModSettings.saveExtendedSettings();
            }
        }
    }
}