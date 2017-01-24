/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer.statusmode;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.world.chunk.Chunk;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class UHCStatusRenderer
{
    public static void init(Minecraft mc)
    {
        List<String> list = UHCStatusRenderer.renderIndicator(mc);
        StatusRendererHelper.renderArmorStatus(mc);
        StatusRendererHelper.renderTimeInformation(mc);
        StatusRendererHelper.renderPotionEffect(mc);

        for (int i = 0; i < list.size(); ++i)
        {
            String string = list.get(i);

            if (!Strings.isNullOrEmpty(string))
            {
                ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                boolean swapToRight = ConfigManager.swapMainRenderInfoToRight;
                int height = mc.fontRenderer.FONT_HEIGHT + 1;
                float y = 3.5F + height * i;
                int xPosition = scaledRes.getScaledWidth() - 2 - mc.fontRenderer.getStringWidth(string);

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
            String ping = JsonMessageUtils.textToJson("Ping: ", ConfigManager.customColorPing).getFormattedText();

            if (ConfigManager.useCustomTextPing)
            {
                ping = JsonMessageUtils.rawTextToJson(ConfigManager.customTextPing).getFormattedText();
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

            if (!GameInfoHelper.INSTANCE.isSinglePlayer())
            {
                list.add(ping + JsonMessageUtils.textToJson(GameInfoHelper.INSTANCE.getPing() + "ms", pingcolor).getFormattedText());
            }
        }
        if (ConfigManager.enableServerIP)
        {
            if (mc.func_147104_D() != null)
            {
                String ip = JsonMessageUtils.textToJson("IP: ", ConfigManager.customColorIP).getFormattedText();
                String serverIP = JsonMessageUtils.textToJson(mc.func_147104_D().serverIP, ConfigManager.customColorIPValue).getFormattedText();
                String version = "";

                if (ConfigManager.useCustomTextIP)
                {
                    ip = JsonMessageUtils.rawTextToJson(ConfigManager.customTextIP).getFormattedText();
                }

                if (ConfigManager.enableServerIPWithMCVersion)
                {
                    version = "/" + JsonMessageUtils.textToJson(IndicatorUtils.MC_VERSION, ConfigManager.customColorIPMCValue).getFormattedText();
                }
                list.add(ip + serverIP + version);
            }
            else
            {
                list.clear();
            }
        }
        if (ConfigManager.enableFPS)
        {
            int fpsValue = ReflectionHelper.getPrivateValue(Minecraft.class, Minecraft.getMinecraft(), new String[] { "debugFPS", "field_71470_ab" });
            String fps = JsonMessageUtils.textToJson("FPS: ", ConfigManager.customColorFPS).getFormattedText();
            String color = ConfigManager.customColorFPSValue1;

            if (ConfigManager.useCustomTextFPS)
            {
                fps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextFPS).getFormattedText();
            }

            if (fpsValue >= 26 && fpsValue <= 40)
            {
                color = ConfigManager.customColorFPSValue2;
            }
            else if (fpsValue <= 25)
            {
                color = ConfigManager.customColorFPSValue3;
            }
            list.add(fps + JsonMessageUtils.textToJson(String.valueOf(fpsValue), color).getFormattedText());
        }
        if (ConfigManager.enableXYZ)
        {
            if (mc.renderViewEntity != null)
            {
                int x = MathHelper.floor_double(mc.thePlayer.posX);
                int y = MathHelper.floor_double(mc.thePlayer.boundingBox.minY);
                int z = MathHelper.floor_double(mc.thePlayer.posZ);
                String xyz = JsonMessageUtils.textToJson("XYZ: ", ConfigManager.customColorXYZ).getFormattedText();
                String nether = JsonMessageUtils.textToJson("Nether ", ConfigManager.customColorXYZNether).getFormattedText();
                String overworld = JsonMessageUtils.textToJson("Overworld ", ConfigManager.customColorXYZOverworld).getFormattedText();
                String xPosition = JsonMessageUtils.textToJson(String.valueOf(x), ConfigManager.customColorXValue).getFormattedText();
                String yPosition = JsonMessageUtils.textToJson(String.valueOf(y), ConfigManager.customColorYValue).getFormattedText();
                String zPosition = JsonMessageUtils.textToJson(String.valueOf(z), ConfigManager.customColorZValue).getFormattedText();
                String xPosition1 = JsonMessageUtils.textToJson(String.valueOf(x * 8), ConfigManager.customColorXValue).getFormattedText();
                String zPosition1 = JsonMessageUtils.textToJson(String.valueOf(z * 8), ConfigManager.customColorZValue).getFormattedText();
                String inNether = mc.thePlayer.dimension == -1 ? nether : "";
                list.add(inNether + xyz + xPosition + " " + yPosition + " " + zPosition);

                if (ConfigManager.useCustomTextXYZ)
                {
                    xyz = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZ).getFormattedText();
                    nether = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZNether).getFormattedText();
                    overworld = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZOverworld).getFormattedText();
                }

                if (ConfigManager.enableOverworldCoordinate && mc.thePlayer.dimension == -1)
                {
                    list.add(overworld + xyz + xPosition1 + " " + yPosition + " " + zPosition1);
                }
            }
        }
        if (ConfigManager.enableDirection)
        {
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

            String directionText = JsonMessageUtils.textToJson("Direction: ", ConfigManager.customColorDirection).getFormattedText();
            String directionValue = JsonMessageUtils.textToJson(direction, ConfigManager.customColorDirectionValue).getFormattedText();

            if (ConfigManager.useCustomTextDirection)
            {
                directionText = JsonMessageUtils.rawTextToJson(ConfigManager.customTextDirection).getFormattedText();
            }
            list.add(directionText + directionValue);
        }
        if (ConfigManager.enableBiome)
        {
            int x = MathHelper.floor_double(mc.thePlayer.posX);
            int y = MathHelper.floor_double(mc.thePlayer.posY);
            int z = MathHelper.floor_double(mc.thePlayer.posZ);

            if (mc.theWorld != null && mc.theWorld.blockExists(x, y, z))
            {
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(x, z);
                String biome = JsonMessageUtils.textToJson("Biome: ", ConfigManager.customColorBiome).getFormattedText();
                String value = JsonMessageUtils.textToJson(StatusRendererHelper.getBetterBiomeName(chunk, mc.theWorld, x, z), ConfigManager.customColorBiomeValue).getFormattedText();

                if (ConfigManager.useCustomTextBiome)
                {
                    biome = JsonMessageUtils.rawTextToJson(ConfigManager.customTextBiome).getFormattedText();
                }
                list.add(biome + value);
            }
        }
        if (ConfigManager.enableCPS)
        {
            if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("left"))
            {
                String cps = JsonMessageUtils.textToJson("CPS: ", ConfigManager.customColorCPS).getFormattedText();
                String rps = JsonMessageUtils.textToJson(" RPS: ", ConfigManager.customColorRPS).getFormattedText();
                String cpsValue = JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getCPS()), ConfigManager.customColorCPSValue).getFormattedText();
                String rpsValue = JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getRPS()), ConfigManager.customColorRPSValue).getFormattedText();

                if (ConfigManager.useCustomTextCPS)
                {
                    cps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
                }
                if (ConfigManager.useCustomTextRPS)
                {
                    rps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextRPS).getFormattedText();
                }
                list.add(cps + cpsValue + rps + rpsValue);
            }
        }
        if (ConfigManager.enablePlayerDetector)
        {
            if (ConfigManager.playerDetectorMode.equals("NORMAL"))
            {
                AxisAlignedBB range = AxisAlignedBB.getBoundingBox(mc.thePlayer.posX - 32, mc.thePlayer.posY - 32, mc.thePlayer.posZ - 32, mc.thePlayer.posX + 32, mc.thePlayer.posY + 32, mc.thePlayer.posZ + 32);
                List<EntityPlayer> player = Minecraft.getMinecraft().thePlayer.worldObj.selectEntitiesWithinAABB(EntityPlayer.class, range, GameInfoHelper.IS_NOT_DEATH_OR_SPECTATOR);
                int size = player.size() - 1;

                if (mc.theWorld != null)
                {
                    if (size > 0)
                    {
                        list.add("P: " + size);
                    }
                }
            }
            if (ConfigManager.playerDetectorMode.equals("LIST"))
            {
                String name;

                if (mc.theWorld != null)
                {
                    for (Object player : mc.theWorld.loadedEntityList)
                    {
                        if (player instanceof EntityOtherPlayerMP)
                        {
                            EntityOtherPlayerMP player1 = (EntityOtherPlayerMP) player;
                            int xPosP = (int)mc.thePlayer.posX;
                            int yPosP = (int)mc.thePlayer.posY;
                            int zPosP = (int)mc.thePlayer.posZ;
                            int xPos = (int)player1.posX;
                            int yPos = (int)player1.posY;
                            int zPos = (int)player1.posZ;
                            double deltaX = xPos - xPosP;
                            double deltaY = yPos - yPosP;
                            double deltaZ = zPos - zPosP;
                            int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                            if (distance < 512)
                            {
                                name = player1.getCommandSenderName() + ": " + distance + "m";
                                list.add(name);
                            }
                        }
                    }
                }
            }
        }
        return list;
    }
}