/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.chunk.Chunk;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.helper.StatusRendererHelper;

public class RenderInfoBase
{
    public static List<String> renderPing()
    {
        List<String> list = Lists.newArrayList();
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();
        String ping = json.text("Ping: ").setStyle(json.colorFromConfig(ConfigManager.customColorPing)).getFormattedText();

        if (ConfigManager.useCustomTextPing)
        {
            ping = JsonUtils.rawTextToJson(ConfigManager.customTextPing).getFormattedText();
        }

        if (mc.getConnection().getPlayerInfo(mc.player.getUniqueID()) != null)
        {
            String pingcolor = ConfigManager.customColorPingValue1;
            int pingValue = GameInfoHelper.INSTANCE.getPing(true);

            if (pingValue >= 200 && pingValue <= 300)
            {
                pingcolor = ConfigManager.customColorPingValue2;
            }
            else if (pingValue >= 301 && pingValue <= 499)
            {
                pingcolor = ConfigManager.customColorPingValue3;
            }
            else if (pingValue >= 500)
            {
                pingcolor = ConfigManager.customColorPingValue4;
            }

            if (!mc.isSingleplayer())
            {
                list.add(ping + json.text(pingValue + "ms").setStyle(json.colorFromConfig(pingcolor)).getFormattedText());
            }
        }
        else
        {
            String pingcolor = ConfigManager.customColorPingValue1;
            int pingValue = GameInfoHelper.INSTANCE.getPing(false);

            if (pingValue >= 200 && pingValue <= 300)
            {
                pingcolor = ConfigManager.customColorPingValue2;
            }
            else if (pingValue >= 301 && pingValue <= 499)
            {
                pingcolor = ConfigManager.customColorPingValue3;
            }
            else if (pingValue >= 500)
            {
                pingcolor = ConfigManager.customColorPingValue4;
            }

            if (!mc.isSingleplayer())
            {
                list.add(ping + json.text(pingValue + "ms").setStyle(json.colorFromConfig(pingcolor)).getFormattedText());
            }
        }
        return list;
    }

    public static List<String> renderServerIP()
    {
        List<String> list = Lists.newArrayList();
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();

        if (mc.isConnectedToRealms())
        {
            list.add(JsonUtils.rawTextToJson(ConfigManager.customTextRealms).getFormattedText());
        }
        else
        {
            if (mc.getCurrentServerData() != null && !mc.isSingleplayer())
            {
                String ip = json.text("IP: ").setStyle(json.colorFromConfig(ConfigManager.customColorIP)).getFormattedText();
                String serverIP = json.text(mc.getCurrentServerData().serverIP).setStyle(json.colorFromConfig(ConfigManager.customColorIPValue)).getFormattedText();
                String version = "";

                if (ConfigManager.useCustomTextIP)
                {
                    ip = JsonUtils.rawTextToJson(ConfigManager.customTextIP).getFormattedText();
                }

                if (ConfigManager.enableServerIPWithMCVersion)
                {
                    version = "/" + json.text(IndicatorUtils.MC_VERSION).setStyle(json.colorFromConfig(ConfigManager.customColorIPMCValue)).getFormattedText();
                }
                list.add(ip + serverIP + version);
            }
        }
        return list;
    }

    public static String renderFPS()
    {
        JsonUtils json = new JsonUtils();
        String fps = json.text("FPS: ").setStyle(json.colorFromConfig(ConfigManager.customColorFPS)).getFormattedText();
        String color = ConfigManager.customColorFPSValue1;

        if (ConfigManager.useCustomTextFPS)
        {
            fps = JsonUtils.rawTextToJson(ConfigManager.customTextFPS).getFormattedText();
        }

        if (Minecraft.getDebugFPS() >= 26 && Minecraft.getDebugFPS() <= 40)
        {
            color = ConfigManager.customColorFPSValue2;
        }
        else if (Minecraft.getDebugFPS() <= 25)
        {
            color = ConfigManager.customColorFPSValue3;
        }
        return fps + json.text(String.valueOf(Minecraft.getDebugFPS())).setStyle(json.colorFromConfig(color)).getFormattedText();
    }

    public static List<String> renderXYZ()
    {
        List<String> list = Lists.newArrayList();
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();

        if (mc.getRenderViewEntity() != null)
        {
            BlockPos pos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
            String xyz = json.text("XYZ: ").setStyle(json.colorFromConfig(ConfigManager.customColorXYZ)).getFormattedText();
            String nether = json.text("Nether ").setStyle(json.colorFromConfig(ConfigManager.customColorXYZNether)).getFormattedText();
            String overworld = json.text("Overworld ").setStyle(json.colorFromConfig(ConfigManager.customColorXYZOverworld)).getFormattedText();
            String xPosition = json.text(String.valueOf(pos.getX())).setStyle(json.colorFromConfig(ConfigManager.customColorXValue)).getFormattedText();
            String yPosition = json.text(String.valueOf(pos.getY())).setStyle(json.colorFromConfig(ConfigManager.customColorYValue)).getFormattedText();
            String zPosition = json.text(String.valueOf(pos.getZ())).setStyle(json.colorFromConfig(ConfigManager.customColorZValue)).getFormattedText();
            String xPosition1 = json.text(String.valueOf(pos.getX() * 8)).setStyle(json.colorFromConfig(ConfigManager.customColorXValue)).getFormattedText();
            String zPosition1 = json.text(String.valueOf(pos.getZ() * 8)).setStyle(json.colorFromConfig(ConfigManager.customColorZValue)).getFormattedText();
            String inNether = mc.player.dimension == -1 ? nether : "";

            if (ConfigManager.useCustomTextXYZ)
            {
                xyz = JsonUtils.rawTextToJson(ConfigManager.customTextXYZ).getFormattedText();
                nether = JsonUtils.rawTextToJson(ConfigManager.customTextXYZNether).getFormattedText();
                overworld = JsonUtils.rawTextToJson(ConfigManager.customTextXYZOverworld).getFormattedText();
            }
            if (ConfigManager.enableOverworldCoordinate && mc.player.dimension == -1)
            {
                list.add(overworld + xyz + xPosition1 + " " + yPosition + " " + zPosition1);
            }
            list.add(inNether + xyz + xPosition + " " + yPosition + " " + zPosition);
        }
        return list;
    }

    public static List<String> renderLookingAtBlock()
    {
        List<String> list = Lists.newArrayList();
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == Type.BLOCK && mc.objectMouseOver.getBlockPos() != null)
        {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            String xPosition = json.text(String.valueOf(pos.getX())).setStyle(json.colorFromConfig(ConfigManager.customColorXValue)).getFormattedText();
            String yPosition = json.text(String.valueOf(pos.getY())).setStyle(json.colorFromConfig(ConfigManager.customColorYValue)).getFormattedText();
            String zPosition = json.text(String.valueOf(pos.getZ())).setStyle(json.colorFromConfig(ConfigManager.customColorZValue)).getFormattedText();
            String lookingAt = json.text("Looking at: ").setStyle(json.colorFromConfig(ConfigManager.customColorLookingAt)).getFormattedText();

            if (ConfigManager.useCustomTextLookingAt)
            {
                lookingAt = JsonUtils.rawTextToJson(ConfigManager.customTextLookingAt).getFormattedText();
            }
            list.add(lookingAt + xPosition + " " + yPosition + " " + zPosition);
        }
        return list;
    }

    public static List<String> renderBiome()
    {
        List<String> list = Lists.newArrayList();
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();

        if (mc.world != null)
        {
            BlockPos blockpos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
            Chunk chunk = mc.world.getChunkFromBlockCoords(blockpos);

            if (mc.world.isBlockLoaded(blockpos) && blockpos.getY() >= 0 && blockpos.getY() < 256)
            {
                if (!chunk.isEmpty())
                {
                    String biome = json.text("Biome: ").setStyle(json.colorFromConfig(ConfigManager.customColorBiome)).getFormattedText();
                    String value = json.text(StatusRendererHelper.getBetterBiomeName(chunk, mc.world, blockpos)).setStyle(json.colorFromConfig(ConfigManager.customColorBiomeValue)).getFormattedText();

                    if (ConfigManager.useCustomTextBiome)
                    {
                        biome = JsonUtils.rawTextToJson(ConfigManager.customTextBiome).getFormattedText();
                    }
                    list.add(biome + value);
                }
            }
        }
        return list;
    }

    public static List<String> renderCPS()
    {
        List<String> list = Lists.newArrayList();
        JsonUtils json = new JsonUtils();

        if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("left"))
        {
            String cps = json.text("CPS: ").setStyle(json.colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
            String rps = ConfigManager.enableRPS ? json.text(" RPS: ").setStyle(json.colorFromConfig(ConfigManager.customColorRPS)).getFormattedText() : "";
            String cpsValue = json.text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setStyle(json.colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();
            String rpsValue = ConfigManager.enableRPS ? json.text(String.valueOf(GameInfoHelper.INSTANCE.getRPS())).setStyle(json.colorFromConfig(ConfigManager.customColorRPSValue)).getFormattedText() : "";

            if (ConfigManager.useCustomTextCPS)
            {
                cps = JsonUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
            }
            if (ConfigManager.useCustomTextRPS)
            {
                rps = JsonUtils.rawTextToJson(ConfigManager.customTextRPS).getFormattedText();
            }
            list.add(cps + cpsValue + rps + rpsValue);
        }
        return list;
    }

    public static String renderDirection()
    {
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();
        Entity entity = mc.getRenderViewEntity();
        int yaw = (int)entity.rotationYaw;
        String direction;

        yaw += 22;
        yaw %= 360;

        if (yaw < 0)
        {
            yaw += 360;
        }

        int facing = yaw / 45;

        if (facing < 0)
        {
            facing = 7;
        }

        switch (facing)
        {
        case 0:
            direction = "South";
            break;
        case 1:
            direction = "South West";
            break;
        case 2:
            direction = "West";
            break;
        case 3:
            direction = "North West";
            break;
        case 4:
            direction = "North";
            break;
        case 5:
            direction = "North East";
            break;
        case 6:
            direction = "East";
            break;
        case 7:
            direction = "South East";
            break;
        default:
            direction = "Unknown";
            break;
        }

        String directionText = json.text("Direction: ").setStyle(json.colorFromConfig(ConfigManager.customColorDirection)).getFormattedText();
        String directionValue = json.text(direction).setStyle(json.colorFromConfig(ConfigManager.customColorDirectionValue)).getFormattedText();

        if (ConfigManager.useCustomTextDirection)
        {
            directionText = JsonUtils.rawTextToJson(ConfigManager.customTextDirection).getFormattedText();
        }
        return directionText + directionValue;
    }
}