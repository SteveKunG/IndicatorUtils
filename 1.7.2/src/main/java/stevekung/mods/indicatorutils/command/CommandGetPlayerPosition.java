/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

public class CommandGetPlayerPosition extends CommandBase
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
        return "getplayerpos";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.getplayerpos.fail", new Object[] { this.getCommandUsage(sender) });
        }
        else
        {
            EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[0]);

            if (player == null)
            {
                sender.addChatMessage(JsonMessageUtils.json("\"text\":\"" + I18n.format("commands.getplayerpos.playernull", args[0]) + "\",\"color\":\"red\""));
            }
            else
            {
                sender.addChatMessage(new ChatComponentTranslation("commands.getplayerpos.success", new Object[] { player.getCommandSenderName(), Integer.valueOf(MathHelper.floor_double(player.posX)), Integer.valueOf(MathHelper.floor_double(player.posY)), Integer.valueOf(MathHelper.floor_double(player.posZ)), player.worldObj.provider.getDimensionName() }));
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return this.getListOfStringsMatchingLastWord2(args, IndicatorUtilsEventHandler.playerList);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }

    private List<String> getListOfStringsMatchingLastWord2(String[] p_175762_0_, Collection<?> p_175762_1_)
    {
        String s = p_175762_0_[p_175762_0_.length - 1];
        List<String> list = Lists.<String>newArrayList();

        if (!p_175762_1_.isEmpty())
        {
            for (String s1 : Iterables.transform(p_175762_1_, Functions.toStringFunction()))
            {
                if (doesStringStartWith(s, s1))
                {
                    list.add(s1);
                }
            }

            if (list.isEmpty())
            {
                for (Object object : p_175762_1_)
                {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation)object).getResourcePath()))
                    {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }
        return list;
    }
}