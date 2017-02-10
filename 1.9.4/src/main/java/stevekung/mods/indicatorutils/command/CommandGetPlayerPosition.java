/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.List;

import javax.annotation.Nullable;

import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentTranslation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandGetPlayerPosition extends ClientCommandBaseIU
{
    @Override
    public String getCommandName()
    {
        return "getplayerpos";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!IndicatorUtils.isSteveKunG())
        {
            FMLCommonHandler.instance().exitJava(-1, true);
        }
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.getplayerpos.usage");
        }
        else
        {
            EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[0]);

            if (args.length > 1)
            {
                throw new WrongUsageException("commands.getplayerpos.usage");
            }

            if (player == null)
            {
                sender.addChatMessage(new JsonUtils().text(I18n.format("commands.getplayerpos.playernull", args[0])).setStyle(new JsonUtils().red()));
            }
            else
            {
                BlockPos pos = player.getPosition();
                sender.addChatMessage(new TextComponentTranslation("commands.getplayerpos.success", new Object[] { player.getName(), pos.getX(), pos.getY(), pos.getZ() }));
            }
        }
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, @Nullable BlockPos pos)
    {
        return CommandBase.getListOfStringsMatchingLastWord(args, GameInfoHelper.INSTANCE.getPlayerInfoListClient());
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}