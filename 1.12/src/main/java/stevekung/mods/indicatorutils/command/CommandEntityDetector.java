package stevekung.mods.indicatorutils.command;

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
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.GameProfileUtils;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandEntityDetector extends ClientCommandBaseIU
{
    @Override
    public String getName()
    {
        return "entitydetect";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (!IndicatorUtils.isSteveKunG() && !IndicatorUtils.ALLOWED)
        {
            Minecraft.getMinecraft().shutdown();
        }
        if (args.length == 1)
        {
            EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[0]);

            if ("all".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "all";
            }
            else if ("only_mob".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "only_mob";
            }
            else if ("only_creature".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "only_creature";
            }
            else if ("only_non_mob".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "only_non_mob";
            }
            else if ("only_player".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "only_player";
            }
            else if ("reset".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "reset";
            }
            else if ("zombie".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "zombie";
            }
            else if ("zombie_villager".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "zombie_villager";
            }
            else if ("husk".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "husk";
            }
            else if ("creeper".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "creeper";
            }
            else if ("skeleton".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "skeleton";
            }
            else if ("wither_skeleton".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "wither_skeleton";
            }
            else if ("stray".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "stray";
            }
            else if ("spider".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "spider";
            }
            else if ("slime".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "slime";
            }
            else if ("magma_cube".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "magma_cube";
            }
            else if ("ghast".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "ghast";
            }
            else if ("enderman".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "enderman";
            }
            else if ("silverfish".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "silverfish";
            }
            else if ("blaze".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "blaze";
            }
            else if ("witch".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "witch";
            }
            else if ("guardian".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "guardian";
            }
            else if ("elder_guardian".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "elder_guardian";
            }
            else if ("shulker".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "shulker";
            }
            else if ("pig".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "pig";
            }
            else if ("sheep".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "sheep";
            }
            else if ("cow".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "cow";
            }
            else if ("chicken".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "chicken";
            }
            else if ("squid".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "squid";
            }
            else if ("wolf".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "wolf";
            }
            else if ("snowman".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "snowman";
            }
            else if ("ocelot".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "ocelot";
            }
            else if ("iron_golem".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "iron_golem";
            }
            else if ("horse".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "horse";
            }
            else if ("donkey".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "donkey";
            }
            else if ("mule".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "mule";
            }
            else if ("skeleton_horse".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "skeleton_horse";
            }
            else if ("zombie_horse".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "zombie_horse";
            }
            else if ("rabbit".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "rabbit";
            }
            else if ("polar_bear".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "polar_bear";
            }
            else if ("llama".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "llama";
            }
            else if ("parrot".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "parrot";
            }
            else if ("evoker".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "evoker";
            }
            else if ("vex".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "vex";
            }
            else if ("vindicator".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "vindicator";
            }
            else if ("illusion_illager".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "illusion_illager";
            }
            else if ("villager".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "villager";
            }
            else if ("item".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "item";
            }
            else if ("xp".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "xp";
            }
            else if ("armor_stand".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "armor_stand";
            }
            else if ("boat".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "boat";
            }
            else if ("minecart".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "minecart";
            }
            else if ("ender_crystal".equalsIgnoreCase(args[0]))
            {
                ExtendedModSettings.ENTITY_DETECT_TYPE = "ender_crystal";
            }
            else if (player != null && player.getName().equalsIgnoreCase(args[0]))
            {
                if (GameProfileUtils.getUsername().equalsIgnoreCase(args[0]))
                {
                    sender.sendMessage(new JsonUtils().text("Cannot set entity detector type to yourself!").setStyle(new JsonUtils().red()));
                    return;
                }
                else
                {
                    ExtendedModSettings.ENTITY_DETECT_TYPE = player.getName();
                }
            }
            else
            {
                throw new WrongUsageException("commands.entitydetect.usage");
            }
            sender.sendMessage(new JsonUtils().text("Set entity detector type to " + args[0]));
            ExtendedModSettings.saveExtendedSettings();
            return;
        }
        throw new WrongUsageException("commands.entitydetect.usage");
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] args, BlockPos pos)
    {
        NetHandlerPlayClient connection = Minecraft.getMinecraft().player.connection;
        List<NetworkPlayerInfo> playerInfo = Lists.newArrayList(connection.getPlayerInfoMap());
        List<String> playerList = Lists.newArrayList("all", "only_mob", "only_creature", "only_non_mob", "only_player", "reset", "zombie", "zombie_villager", "husk", "creeper", "skeleton", "wither_skeleton", "stray", "spider", "slime", "magma_cube", "ghast", "enderman", "silverfish", "blaze", "witch", "guardian", "elder_guardian", "shulker", "pig", "sheep", "cow", "chicken", "squid", "wolf", "snowman", "ocelot", "iron_golem", "horse", "donkey", "mule", "skeleton_horse", "zombie_horse", "rabbit", "polar_bear", "llama", "parrot", "villager", "evoker", "vex", "vindicator", "illusion_illager", "item", "xp", "armor_stand", "boat", "minecart", "ender_crystal");

        for (int i = 0; i < playerInfo.size(); ++i)
        {
            if (i < playerInfo.size())
            {
                playerList.add(playerInfo.get(i).getGameProfile().getName().replace(GameProfileUtils.getUsername(), ""));
            }
        }
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, playerList);
        }
        return super.getTabCompletions(server, sender, args, pos);
    }
}