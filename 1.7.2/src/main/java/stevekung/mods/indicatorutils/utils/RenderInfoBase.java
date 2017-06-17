package stevekung.mods.indicatorutils.utils;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
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
        String ping = json.text("Ping: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorPing)).getFormattedText();

        if (ConfigManager.useCustomTextPing)
        {
            ping = JsonUtils.rawTextToJson(ConfigManager.customTextPing).getFormattedText();
        }

        String pingcolor = ConfigManager.customColorPingValue1;

        if (GameInfoHelper.INSTANCE.getPing() >= 200 && GameInfoHelper.INSTANCE.getPing() <= 300)
        {
            pingcolor = ConfigManager.customColorPingValue2;
        }
        else if (GameInfoHelper.INSTANCE.getPing() >= 301 && GameInfoHelper.INSTANCE.getPing() <= 499)
        {
            pingcolor = ConfigManager.customColorPingValue3;
        }
        else if (GameInfoHelper.INSTANCE.getPing() >= 500)
        {
            pingcolor = ConfigManager.customColorPingValue4;
        }

        if (!mc.isSingleplayer())
        {
            list.add(ping + json.text(GameInfoHelper.INSTANCE.getPing() + "ms").setChatStyle(json.colorFromConfig(pingcolor)).getFormattedText());
        }
        return list;
    }

    public static List<String> renderServerIP()
    {
        List<String> list = Lists.newArrayList();
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();

        if (mc.func_147104_D() != null && !mc.isSingleplayer())
        {
            String ip = json.text("IP: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorIP)).getFormattedText();
            String serverIP = json.text(mc.func_147104_D().serverIP).setChatStyle(json.colorFromConfig(ConfigManager.customColorIPValue)).getFormattedText();
            String version = "";

            if (ConfigManager.useCustomTextIP)
            {
                ip = JsonUtils.rawTextToJson(ConfigManager.customTextIP).getFormattedText();
            }

            if (ConfigManager.enableServerIPWithMCVersion)
            {
                version = "/" + json.text(IndicatorUtils.MC_VERSION).setChatStyle(json.colorFromConfig(ConfigManager.customColorIPMCValue)).getFormattedText();
            }
            list.add(ip + serverIP + version);
        }
        return list;
    }

    public static String renderFPS()
    {
        JsonUtils json = new JsonUtils();
        int fpsValue = Minecraft.debugFPS;
        String fps = json.text("FPS: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorFPS)).getFormattedText();
        String color = ConfigManager.customColorFPSValue1;

        if (ConfigManager.useCustomTextFPS)
        {
            fps = JsonUtils.rawTextToJson(ConfigManager.customTextFPS).getFormattedText();
        }

        if (fpsValue >= 26 && fpsValue <= 40)
        {
            color = ConfigManager.customColorFPSValue2;
        }
        else if (fpsValue <= 25)
        {
            color = ConfigManager.customColorFPSValue3;
        }
        return fps + json.text(String.valueOf(fpsValue)).setChatStyle(json.colorFromConfig(color)).getFormattedText();
    }

    public static List<String> renderXYZ()
    {
        List<String> list = Lists.newArrayList();
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();

        if (mc.renderViewEntity != null)
        {
            int x = MathHelper.floor_double(mc.thePlayer.posX);
            int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY);
            int z = MathHelper.floor_double(mc.thePlayer.posZ);
            String xyz = json.text("XYZ: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorXYZ)).getFormattedText();
            String nether = json.text("Nether ").setChatStyle(json.colorFromConfig(ConfigManager.customColorXYZNether)).getFormattedText();
            String overworld = json.text("Overworld ").setChatStyle(json.colorFromConfig(ConfigManager.customColorXYZOverworld)).getFormattedText();
            String xPosition = json.text(String.valueOf(x)).setChatStyle(json.colorFromConfig(ConfigManager.customColorXValue)).getFormattedText();
            String yPosition = json.text(String.valueOf(y)).setChatStyle(json.colorFromConfig(ConfigManager.customColorYValue)).getFormattedText();
            String zPosition = json.text(String.valueOf(z)).setChatStyle(json.colorFromConfig(ConfigManager.customColorZValue)).getFormattedText();
            String xPosition1 = json.text(String.valueOf(x * 8)).setChatStyle(json.colorFromConfig(ConfigManager.customColorXValue)).getFormattedText();
            String zPosition1 = json.text(String.valueOf(z * 8)).setChatStyle(json.colorFromConfig(ConfigManager.customColorZValue)).getFormattedText();
            String inNether = mc.thePlayer.dimension == -1 ? nether : "";

            if (ConfigManager.useCustomTextXYZ)
            {
                xyz = JsonUtils.rawTextToJson(ConfigManager.customTextXYZ).getFormattedText();
                nether = JsonUtils.rawTextToJson(ConfigManager.customTextXYZNether).getFormattedText();
                overworld = JsonUtils.rawTextToJson(ConfigManager.customTextXYZOverworld).getFormattedText();
            }
            if (ConfigManager.enableOverworldCoordinate && mc.thePlayer.dimension == -1)
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

        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK && mc.objectMouseOver != null)
        {
            String xPosition = json.text(String.valueOf(mc.objectMouseOver.blockX)).setChatStyle(json.colorFromConfig(ConfigManager.customColorXValue)).getFormattedText();
            String yPosition = json.text(String.valueOf(mc.objectMouseOver.blockY)).setChatStyle(json.colorFromConfig(ConfigManager.customColorYValue)).getFormattedText();
            String zPosition = json.text(String.valueOf(mc.objectMouseOver.blockZ)).setChatStyle(json.colorFromConfig(ConfigManager.customColorZValue)).getFormattedText();
            String lookingAt = json.text("Looking at: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorLookingAt)).getFormattedText();

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

        int x = MathHelper.floor_double(mc.thePlayer.posX);
        int y = MathHelper.floor_double(mc.thePlayer.posY);
        int z = MathHelper.floor_double(mc.thePlayer.posZ);

        if (mc.theWorld != null && mc.theWorld.blockExists(x, y, z))
        {
            Chunk chunk = mc.theWorld.getChunkFromBlockCoords(x, z);
            String biome = json.text("Biome: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorBiome)).getFormattedText();
            String value = json.text(StatusRendererHelper.getBetterBiomeName(chunk, mc.theWorld, x, z)).setChatStyle(json.colorFromConfig(ConfigManager.customColorBiomeValue)).getFormattedText();

            if (ConfigManager.useCustomTextBiome)
            {
                biome = JsonUtils.rawTextToJson(ConfigManager.customTextBiome).getFormattedText();
            }
            list.add(biome + value);
        }
        return list;
    }

    public static List<String> renderCPS()
    {
        List<String> list = Lists.newArrayList();
        JsonUtils json = new JsonUtils();

        if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("left"))
        {
            String cps = json.text("CPS: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
            String rcps = ConfigManager.enableRCPS ? json.text(" RCPS: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorRCPS)).getFormattedText() : "";
            String cpsValue = json.text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setChatStyle(json.colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();
            String rcpsValue = ConfigManager.enableRCPS ? json.text(String.valueOf(GameInfoHelper.INSTANCE.getRCPS())).setChatStyle(json.colorFromConfig(ConfigManager.customColorRCPSValue)).getFormattedText() : "";

            if (ConfigManager.useCustomTextCPS)
            {
                cps = JsonUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
            }
            if (ConfigManager.useCustomTextRCPS)
            {
                rcps = JsonUtils.rawTextToJson(ConfigManager.customTextRCPS).getFormattedText();
            }
            list.add(cps + cpsValue + rcps + rcpsValue);
        }
        return list;
    }

    public static String renderDirection()
    {
        Minecraft mc = Minecraft.getMinecraft();
        JsonUtils json = new JsonUtils();
        Entity entity = mc.renderViewEntity;
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

        String directionText = json.text("Direction: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorDirection)).getFormattedText();
        String directionValue = json.text(direction).setChatStyle(json.colorFromConfig(ConfigManager.customColorDirectionValue)).getFormattedText();

        if (ConfigManager.useCustomTextDirection)
        {
            directionText = JsonUtils.rawTextToJson(ConfigManager.customTextDirection).getFormattedText();
        }
        return directionText + directionValue;
    }
}