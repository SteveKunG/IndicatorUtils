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
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.Chunk;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
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
            String ping = JsonMessageUtils.textToJson("Ping: ", ConfigManager.customColorPing).getFormattedText();

            if (ConfigManager.useCustomTextPing)
            {
                ping = JsonMessageUtils.rawTextToJson(ConfigManager.customTextPing).getFormattedText();
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
                    list.add(ping + JsonMessageUtils.textToJson(GameInfoHelper.INSTANCE.getPing() + "ms", pingcolor).getFormattedText());
                }
            }
            else
            {
                String pingna = JsonMessageUtils.textToJson("n/a", ConfigManager.customColorPingNA).getFormattedText();

                if (IndicatorUtilsEventHandler.checkUUID == false && GameInfoHelper.INSTANCE.isHypixel())
                {
                    IndicatorUtilsEventHandler.checkUUID = true;
                    IndicatorUtils.STATUS_CHECK[3] = IndicatorUtilsEventHandler.checkUUID;
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
                    String ip = JsonMessageUtils.textToJson("IP: ", ConfigManager.customColorIP).getFormattedText();
                    String serverIP = JsonMessageUtils.textToJson(mc.getCurrentServerData().serverIP, ConfigManager.customColorIPValue).getFormattedText();
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
        }
        if (ConfigManager.enableFPS)
        {
            String fps = JsonMessageUtils.textToJson("FPS: ", ConfigManager.customColorFPS).getFormattedText();
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
            list.add(fps + JsonMessageUtils.textToJson(String.valueOf(Minecraft.getDebugFPS()), color).getFormattedText());
        }
        if (ConfigManager.enableXYZ)
        {
            if (mc.getRenderViewEntity() != null)
            {
                BlockPos pos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
                String xyz = JsonMessageUtils.textToJson("XYZ: ", ConfigManager.customColorXYZ).getFormattedText();
                String nether = JsonMessageUtils.textToJson("Nether ", ConfigManager.customColorXYZNether).getFormattedText();
                String overworld = JsonMessageUtils.textToJson("Overworld ", ConfigManager.customColorXYZOverworld).getFormattedText();
                String xPosition = JsonMessageUtils.textToJson(String.valueOf(pos.getX()), ConfigManager.customColorXValue).getFormattedText();
                String yPosition = JsonMessageUtils.textToJson(String.valueOf(pos.getY()), ConfigManager.customColorYValue).getFormattedText();
                String zPosition = JsonMessageUtils.textToJson(String.valueOf(pos.getZ()), ConfigManager.customColorZValue).getFormattedText();
                String xPosition1 = JsonMessageUtils.textToJson(String.valueOf(pos.getX() * 8), ConfigManager.customColorXValue).getFormattedText();
                String zPosition1 = JsonMessageUtils.textToJson(String.valueOf(pos.getZ() * 8), ConfigManager.customColorZValue).getFormattedText();
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
            if (mc.theWorld != null)
            {
                BlockPos blockpos = new BlockPos(mc.getRenderViewEntity().posX, mc.getRenderViewEntity().getEntityBoundingBox().minY, mc.getRenderViewEntity().posZ);
                Chunk chunk = mc.theWorld.getChunkFromBlockCoords(blockpos);

                if (mc.theWorld.isBlockLoaded(blockpos) && blockpos.getY() >= 0 && blockpos.getY() < 256)
                {
                    if (!chunk.isEmpty())
                    {
                        String biome = JsonMessageUtils.textToJson("Biome: ", ConfigManager.customColorBiome).getFormattedText();
                        String value = JsonMessageUtils.textToJson(StatusRendererHelper.getBetterBiomeName(chunk, mc.theWorld, blockpos), ConfigManager.customColorBiomeValue).getFormattedText();

                        if (ConfigManager.useCustomTextBiome)
                        {
                            biome = JsonMessageUtils.rawTextToJson(ConfigManager.customTextBiome).getFormattedText();
                        }
                        list.add(biome + value);
                    }
                }
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
                AxisAlignedBB range = new AxisAlignedBB(mc.thePlayer.posX - 32, mc.thePlayer.posY - 32, mc.thePlayer.posZ - 32, mc.thePlayer.posX + 32, mc.thePlayer.posY + 32, mc.thePlayer.posZ + 32);
                List<EntityPlayer> player = Minecraft.getMinecraft().thePlayer.worldObj.getEntitiesWithinAABB(EntityPlayer.class, range, GameInfoHelper.IS_NOT_DEATH_OR_SPECTATOR);
                int size = player.size() - 1;

                if (mc.theWorld != null)
                {
                    for (Entity playerList : mc.theWorld.loadedEntityList)
                    {
                        if (playerList instanceof EntityOtherPlayerMP)
                        {
                            ((EntityOtherPlayerMP)playerList).setGlowing(((EntityOtherPlayerMP) playerList).isPotionActive(MobEffects.GLOWING));
                        }
                    }
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
                    for (Entity playerList : mc.theWorld.loadedEntityList)
                    {
                        if (playerList instanceof EntityOtherPlayerMP)
                        {
                            ((EntityOtherPlayerMP)playerList).setGlowing(((EntityOtherPlayerMP) playerList).isPotionActive(MobEffects.GLOWING));
                        }
                    }
                    for (Entity player : mc.theWorld.loadedEntityList)
                    {
                        if (player instanceof EntityOtherPlayerMP)
                        {
                            int xPosP = (int)mc.thePlayer.posX;
                            int yPosP = (int)mc.thePlayer.posY;
                            int zPosP = (int)mc.thePlayer.posZ;
                            int xPos = (int)player.posX;
                            int yPos = (int)player.posY;
                            int zPos = (int)player.posZ;
                            double deltaX = xPos - xPosP;
                            double deltaY = yPos - yPosP;
                            double deltaZ = zPos - zPosP;
                            int distance = (int) Math.sqrt(deltaX * deltaX + deltaY * deltaY + deltaZ * deltaZ);

                            if (distance < 512)
                            {
                                name = player.getName() + ": " + distance + "m";
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