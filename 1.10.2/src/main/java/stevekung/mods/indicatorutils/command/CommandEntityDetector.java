/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraftforge.fml.common.FMLCommonHandler;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandEntityDetector extends CommandBase
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
        return "entitydetect";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!IndicatorUtils.isSteveKunG())
        {
            FMLCommonHandler.instance().exitJava(-1, true);
        }
        if (args.length == 1)
        {
            EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[0]);

            if ("all".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "all";
            }
            else if ("onlymob".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "onlymob";
            }
            else if ("onlycreature".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "onlycreature";
            }
            else if ("onlynonmob".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "onlynonmob";
            }
            else if ("onlyplayer".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "onlyplayer";
            }
            else if ("reset".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "reset";
            }
            else if ("zombie".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "zombie";
            }
            else if ("creeper".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "creeper";
            }
            else if ("skeleton".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "skeleton";
            }
            else if ("spider".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "spider";
            }
            else if ("slime".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "slime";
            }
            else if ("ghast".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "ghast";
            }
            else if ("enderman".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "enderman";
            }
            else if ("silverfish".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "silverfish";
            }
            else if ("blaze".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "blaze";
            }
            else if ("witch".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "witch";
            }
            else if ("guardian".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "guardian";
            }
            else if ("shulker".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "shulker";
            }
            else if ("pig".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "pig";
            }
            else if ("sheep".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "sheep";
            }
            else if ("cow".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "cow";
            }
            else if ("chicken".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "chicken";
            }
            else if ("squid".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "squid";
            }
            else if ("wolf".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "wolf";
            }
            else if ("snowman".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "snowman";
            }
            else if ("ocelot".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "ocelot";
            }
            else if ("irongolem".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "irongolem";
            }
            else if ("horse".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "horse";
            }
            else if ("rabbit".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "rabbit";
            }
            else if ("polarbear".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "polarbear";
            }
            else if ("villager".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "villager";
            }
            else if ("item".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "item";
            }
            else if ("xp".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "xp";
            }
            else if ("armorstand".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "armorstand";
            }
            else if ("boat".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "boat";
            }
            else if ("minecart".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "minecart";
            }
            else if ("endercrystal".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "endercrystal";
            }
            else if (player != null && player.getName().equalsIgnoreCase(args[0]))
            {
                if (Minecraft.getMinecraft().getSession().getProfile().getName().equalsIgnoreCase(args[0]))
                {
                    sender.addChatMessage(JsonUtils.textToJson("Cannot set entity detector type to yourself!", "red"));
                    return;
                }
                else
                {
                    ExtendedModSettings.ENTITY_DETECT_TYPE = player.getName();
                }
            }
            else
            {
                throw new WrongUsageException("commands.entitydetect.usage", new Object[] { this.getCommandUsage(sender) });
            }
            sender.addChatMessage(JsonUtils.textToJson("Set entity detector type to " + args[0], "white"));
            ExtendedModSettings.saveExtendedSettings();
            return;
        }
        throw new WrongUsageException("commands.entitydetect.usage", new Object[] { this.getCommandUsage(sender) });
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        NetHandlerPlayClient connection = Minecraft.getMinecraft().thePlayer.connection;

        if (connection != null)
        {
            List<NetworkPlayerInfo> playerInfo = new ArrayList(connection.getPlayerInfoMap());
            List<String> playerList = Lists.newArrayList(new String[] { "all", "onlymob", "onlycreature", "onlynonmob", "onlyplayer", "reset", "zombie", "creeper", "skeleton", "spider", "slime", "ghast", "enderman", "silverfish", "blaze", "witch", "guardian", "shulker", "pig", "sheep", "cow", "chicken", "squid", "wolf", "snowman", "ocelot", "irongolem", "horse", "rabbit", "polarbear", "villager", "item", "xp", "armorstand", "boat", "minecart", "endercrystal" });

            for (int i = 0; i < playerInfo.size(); ++i)
            {
                if (i < playerInfo.size())
                {
                    playerList.add(playerInfo.get(i).getGameProfile().getName().replace(Minecraft.getMinecraft().getSession().getProfile().getName(), ""));
                }
            }
            if (args.length == 1)
            {
                return CommandBase.getListOfStringsMatchingLastWord(args, playerList);
            }
        }
        return Collections.<String>emptyList();
    }
}