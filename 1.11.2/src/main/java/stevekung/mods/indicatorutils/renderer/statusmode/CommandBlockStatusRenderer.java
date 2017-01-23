/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer.statusmode;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class CommandBlockStatusRenderer
{
    public static void init(Minecraft mc)
    {
        List<String> list = CommandBlockStatusRenderer.renderIndicator(mc);
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
                    Collection<PotionEffect> collection = mc.player.getActivePotionEffects();

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

    public static List<String> renderIndicator(Minecraft mc)
    {
        List<String> list = Lists.newArrayList();
        JsonMessageUtils json = new JsonMessageUtils();

        if (ConfigManager.enablePing)
        {
            String ping = json.text("Ping: ").setStyle(json.colorFromConfig(ConfigManager.customColorPing)).getFormattedText();

            if (ConfigManager.useCustomTextPing)
            {
                ping = JsonMessageUtils.rawTextToJson(ConfigManager.customTextPing).getFormattedText();
            }

            if (mc.getConnection().getPlayerInfo(mc.player.getUniqueID()) != null)
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
                    list.add(ping + json.text(GameInfoHelper.INSTANCE.getPing() + "ms").setStyle(json.colorFromConfig(pingcolor)).getFormattedText());
                }
            }
            else
            {
                String pingna = json.text("n/a").setStyle(json.colorFromConfig(ConfigManager.customColorPingNA)).getFormattedText();

                if (!IndicatorUtilsEventHandler.CHECK_UUID && GameInfoHelper.INSTANCE.isHypixel())
                {
                    IndicatorUtilsEventHandler.CHECK_UUID = true;
                    IndicatorUtils.STATUS_CHECK[3] = IndicatorUtilsEventHandler.CHECK_UUID;
                }
                list.add(ping + pingna);
            }
        }
        if (ConfigManager.enableServerIP)
        {
            if (mc.isConnectedToRealms())
            {
                list.add(JsonMessageUtils.rawTextToJson(ConfigManager.customTextRealms).getFormattedText());
            }
            else
            {
                if (mc.getCurrentServerData() != null)
                {
                    String ip = json.text("IP: ").setStyle(json.colorFromConfig(ConfigManager.customColorIP)).getFormattedText();
                    String serverIP = json.text(mc.getCurrentServerData().serverIP).setStyle(json.colorFromConfig(ConfigManager.customColorIPValue)).getFormattedText();
                    String version = "";

                    if (ConfigManager.useCustomTextIP)
                    {
                        ip = JsonMessageUtils.rawTextToJson(ConfigManager.customTextIP).getFormattedText();
                    }

                    if (ConfigManager.enableServerIPWithMCVersion)
                    {
                        version = "/" + json.text(IndicatorUtils.MC_VERSION).setStyle(json.colorFromConfig(ConfigManager.customColorIPMCValue)).getFormattedText();
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
            String fps = json.text("FPS: ").setStyle(json.colorFromConfig(ConfigManager.customColorFPS)).getFormattedText();
            String color = ConfigManager.customColorFPSValue1;

            if (ConfigManager.useCustomTextFPS)
            {
                fps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextFPS).getFormattedText();
            }

            if (Minecraft.getDebugFPS() >= 26 && Minecraft.getDebugFPS() <= 40)
            {
                color = ConfigManager.customColorFPSValue2;
            }
            else if (Minecraft.getDebugFPS() <= 25)
            {
                color = ConfigManager.customColorFPSValue3;
            }
            list.add(fps + json.text(String.valueOf(Minecraft.getDebugFPS())).setStyle(json.colorFromConfig(color)).getFormattedText());
        }
        if (ConfigManager.enableXYZ)
        {
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

                if (ConfigManager.useCustomTextXYZ)
                {
                    xyz = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZ).getFormattedText();
                    nether = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZNether).getFormattedText();
                    overworld = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZOverworld).getFormattedText();
                }

                String inNether = mc.player.dimension == -1 ? nether : "";
                list.add(inNether + xyz + xPosition + " " + yPosition + " " + zPosition);

                if (ConfigManager.enableOverworldCoordinate && mc.player.dimension == -1)
                {
                    list.add(overworld + xyz + xPosition1 + " " + yPosition + " " + zPosition1);
                }
            }
        }
        if (mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit == Type.BLOCK && mc.objectMouseOver.getBlockPos() != null)
        {
            BlockPos pos = mc.objectMouseOver.getBlockPos();
            String xPosition = json.text(String.valueOf(pos.getX())).setStyle(json.colorFromConfig(ConfigManager.customColorXValue)).getFormattedText();
            String yPosition = json.text(String.valueOf(pos.getY())).setStyle(json.colorFromConfig(ConfigManager.customColorYValue)).getFormattedText();
            String zPosition = json.text(String.valueOf(pos.getZ())).setStyle(json.colorFromConfig(ConfigManager.customColorZValue)).getFormattedText();
            String lookingAt = json.text("Looking at: ").setStyle(json.colorFromConfig(ConfigManager.customColorLookingAt)).getFormattedText();

            if (ConfigManager.useCustomTextLookingAt)
            {
                lookingAt = JsonMessageUtils.rawTextToJson(ConfigManager.customTextLookingAt).getFormattedText();
            }
            list.add(lookingAt + xPosition + " " + yPosition + " " + zPosition);
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

            String directionText = json.text("Direction: ").setStyle(json.colorFromConfig(ConfigManager.customColorDirection)).getFormattedText();
            String directionValue = json.text(direction).setStyle(json.colorFromConfig(ConfigManager.customColorDirectionValue)).getFormattedText();

            if (ConfigManager.useCustomTextDirection)
            {
                directionText = JsonMessageUtils.rawTextToJson(ConfigManager.customTextDirection).getFormattedText();
            }
            list.add(directionText + directionValue);
        }
        return list;
    }
}