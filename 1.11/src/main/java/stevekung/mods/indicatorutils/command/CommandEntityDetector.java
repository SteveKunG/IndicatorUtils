/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.ArrayList;
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
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;

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
        if (args.length == 1)
        {
            EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[0]);

            if ("all".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "all";
            }
            else if ("only_mob".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "only_mob";
            }
            else if ("only_creature".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "only_creature";
            }
            else if ("only_non_mob".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "only_non_mob";
            }
            else if ("only_player".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "only_player";
            }
            else if ("reset".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "reset";
            }
            else if ("zombie".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "zombie";
            }
            else if ("zombie_villager".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "zombie_villager";
            }
            else if ("husk".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "husk";
            }
            else if ("creeper".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "creeper";
            }
            else if ("skeleton".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "skeleton";
            }
            else if ("wither_skeleton".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "wither_skeleton";
            }
            else if ("stray".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "stray";
            }
            else if ("spider".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "spider";
            }
            else if ("slime".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "slime";
            }
            else if ("magma_cube".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "magma_cube";
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
            else if ("elder_guardian".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "elder_guardian";
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
            else if ("iron_golem".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "iron_golem";
            }
            else if ("horse".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "horse";
            }
            else if ("donkey".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "donkey";
            }
            else if ("mule".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "mule";
            }
            else if ("skeleton_horse".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "skeleton_horse";
            }
            else if ("zombie_horse".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "zombie_horse";
            }
            else if ("rabbit".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "rabbit";
            }
            else if ("polar_bear".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "polar_bear";
            }
            else if ("llama".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "llama";
            }
            else if ("evoker".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "evoker";
            }
            else if ("vex".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "vex";
            }
            else if ("vindicator".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "vindicator";
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
            else if ("armor_stand".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "armor_stand";
            }
            else if ("boat".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "boat";
            }
            else if ("minecart".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "minecart";
            }
            else if ("ender_crystal".equals(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "ender_crystal";
            }
            else if (player != null && player.getName().equalsIgnoreCase(args[0]))
            {
                if (Minecraft.getMinecraft().getSession().getProfile().getName().equalsIgnoreCase(args[0]))
                {
                    sender.addChatMessage(JsonMessageUtils.textToJson("Cannot set entity detector type to yourself!", "red"));
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
            sender.addChatMessage(JsonMessageUtils.textToJson("Set entity detector type to " + args[0]));
            ExtendedModSettings.saveExtendedSettings();
            return;
        }
        throw new WrongUsageException("commands.entitydetect.usage", new Object[] { this.getCommandUsage(sender) });
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        NetHandlerPlayClient connection = Minecraft.getMinecraft().thePlayer.connection;

        List<NetworkPlayerInfo> playerInfo = new ArrayList(connection.getPlayerInfoMap());
        List<String> playerList = Lists.newArrayList(new String[] { "all", "only_mob", "only_creature", "only_non_mob", "only_player", "reset", "zombie", "zombie_villager", "husk", "creeper", "skeleton", "wither_skeleton", "stray", "spider", "slime", "magma_cube", "ghast", "enderman", "silverfish", "blaze", "witch", "guardian", "elder_guardian", "shulker", "pig", "sheep", "cow", "chicken", "squid", "wolf", "snowman", "ocelot", "iron_golem", "horse", "donkey", "mule", "skeleton_horse", "zombie_horse", "rabbit", "polar_bear", "llama", "villager", "evoker", "vex", "vindicator", "item", "xp", "armor_stand", "boat", "minecart", "ender_crystal" });

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
        return super.getTabCompletionOptions(server, sender, args, pos);
    }
}