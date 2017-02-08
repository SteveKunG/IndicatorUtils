/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer.mode;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.helper.StatusRendererHelper;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class PvP
{
    public static void init(Minecraft mc)
    {
        List<String> list = PvP.renderIndicator(mc);
        StatusRendererHelper.renderArmorStatus(mc);
        StatusRendererHelper.renderTimeInformation(mc);
        StatusRendererHelper.renderPotionEffect(mc);

        for (int i = 0; i < list.size(); ++i)
        {
            String string = list.get(i);

            if (!Strings.isNullOrEmpty(string))
            {
                ScaledResolution scaledRes = new ScaledResolution(mc);
                boolean swapToRight = ConfigManager.swapMainRenderInfoToRight;
                int height = mc.fontRendererObj.FONT_HEIGHT + 1;
                float y = 3.5F + height * i;
                int xPosition = scaledRes.getScaledWidth() - 2 - mc.fontRendererObj.getStringWidth(string);

                if (swapToRight && !GameInfoHelper.INSTANCE.isBelowMinecraft19())
                {
                    Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

                    if (!collection.isEmpty() && ConfigManager.renderIngamePotionEffect)
                    {
                        for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection))
                        {
                            if (potioneffect.doesShowParticles())
                            {
                                y = 53.0F + height * i;
                            }
                        }
                    }
                }
                if (!mc.gameSettings.showDebugInfo)
                {
                    StatusRendererHelper.INSTANCE.drawString(string, swapToRight ? xPosition : 3.5F, y, EnumTextColor.WHITE, true);
                }
            }
        }
    }

    private static List<String> renderIndicator(Minecraft mc)
    {
        List<String> list = Lists.newArrayList(new String[] {} );

        if (ConfigManager.enablePing)
        {
            String ping = JsonUtils.textToJson("Ping: ", ConfigManager.customColorPing).getFormattedText();

            if (ConfigManager.useCustomTextPing)
            {
                ping = JsonUtils.rawTextToJson(ConfigManager.customTextPing).getFormattedText();
            }

            if (mc.getConnection().getPlayerInfo(mc.thePlayer.getUniqueID()) != null)
            {
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

                if (!GameInfoHelper.INSTANCE.isSinglePlayer())
                {
                    list.add(ping + JsonUtils.textToJson(GameInfoHelper.INSTANCE.getPing() + "ms", pingcolor).getFormattedText());
                }
            }
            else
            {
                String pingna = JsonUtils.textToJson("n/a", ConfigManager.customColorPingNA).getFormattedText();

                if (IndicatorUtilsEventHandler.checkUUID == false && GameInfoHelper.INSTANCE.isHypixel())
                {
                    IndicatorUtilsEventHandler.checkUUID = true;
                    IndicatorUtils.STATUS_CHECK[2] = IndicatorUtilsEventHandler.checkUUID;
                }
                list.add(ping + pingna);
            }
        }
        if (ConfigManager.enableServerIP)
        {
            if (mc.isConnectedToRealms())
            {
                list.add(JsonUtils.rawTextToJson(ConfigManager.customTextRealms).getFormattedText());
            }
            else
            {
                if (mc.getCurrentServerData() != null && !mc.isSingleplayer())
                {
                    String ip = JsonUtils.textToJson("IP: ", ConfigManager.customColorIP).getFormattedText();
                    String serverIP = JsonUtils.textToJson(mc.getCurrentServerData().serverIP, ConfigManager.customColorIPValue).getFormattedText();
                    String version = "";

                    if (ConfigManager.useCustomTextIP)
                    {
                        ip = JsonUtils.rawTextToJson(ConfigManager.customTextIP).getFormattedText();
                    }

                    if (ConfigManager.enableServerIPWithMCVersion)
                    {
                        version = "/" + JsonUtils.textToJson(IndicatorUtils.MC_VERSION, ConfigManager.customColorIPMCValue).getFormattedText();
                    }
                    list.add(ip + serverIP + version);
                }
                else
                {
                    list.clear();
                }
            }
        }
        if (ConfigManager.enableFPS)
        {
            String fps = JsonUtils.textToJson("FPS: ", ConfigManager.customColorFPS).getFormattedText();
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
            list.add(fps + JsonUtils.textToJson(String.valueOf(Minecraft.getDebugFPS()), color).getFormattedText());
        }
        if (ConfigManager.enableCPS)
        {
            if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("left"))
            {
                String cps = JsonUtils.textToJson("CPS: ", ConfigManager.customColorCPS).getFormattedText();
                String rps = ConfigManager.enableRPS ? JsonUtils.textToJson(" RPS: ", ConfigManager.customColorRPS).getFormattedText() : "";
                String cpsValue = JsonUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getCPS()), ConfigManager.customColorCPSValue).getFormattedText();
                String rpsValue = ConfigManager.enableRPS ? JsonUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getRPS()), ConfigManager.customColorRPSValue).getFormattedText() : "";

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
        }
        if (ConfigManager.enableDirection)
        {
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

            String directionText = JsonUtils.textToJson("Direction: ", ConfigManager.customColorDirection).getFormattedText();
            String directionValue = JsonUtils.textToJson(direction, ConfigManager.customColorDirectionValue).getFormattedText();

            if (ConfigManager.useCustomTextDirection)
            {
                directionText = JsonUtils.rawTextToJson(ConfigManager.customTextDirection).getFormattedText();
            }
            list.add(directionText + directionValue);
        }
        return list;
    }
}