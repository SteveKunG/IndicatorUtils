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
import net.minecraft.init.Items;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandAutoFish extends CommandBase
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
        return "autofish";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        JsonMessageUtils json = new JsonMessageUtils();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.autofish.usage");
        }
        else
        {
            if (args.length > 1)
            {
                throw new WrongUsageException("commands.autofish.usage");
            }
            if ("disable".equalsIgnoreCase(args[0]))
            {
                if (IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
                {
                    IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = false;
                    sender.sendMessage(json.text("Disabled auto fish"));
                }
                else
                {
                    sender.sendMessage(json.text("You have not start using /autofish command").setStyle(json.red()));
                }
            }
            else if ("enable".equalsIgnoreCase(args[0]))
            {
                if (!IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
                {
                    for (EnumHand hand : EnumHand.values())
                    {
                        if (Minecraft.getMinecraft().player.getHeldItem(hand).getItem() != Items.FISHING_ROD)
                        {
                            sender.sendMessage(json.text("You are not held the fishing rod").setStyle(json.red()));
                            return;
                        }
                        else
                        {
                            IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = true;
                            sender.sendMessage(json.text("Enabled auto fish"));
                            return;
                        }
                    }
                }
                else
                {
                    sender.sendMessage(json.text("You have already start /autofish command").setStyle(json.red()));
                }
            }
            else
            {
                throw new WrongUsageException("commands.autofish.usage");
            }
        }
    }

    @Override
    public List getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "enable", "disable");
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}