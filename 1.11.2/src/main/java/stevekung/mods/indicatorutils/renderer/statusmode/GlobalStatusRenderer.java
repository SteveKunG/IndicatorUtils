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
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.MobEffects;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult.Type;
import net.minecraft.world.chunk.Chunk;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class GlobalStatusRenderer
{
    public static void init(Minecraft mc)
    {
        List<String> list = GlobalStatusRenderer.renderIndicator(mc);
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

                if (!IndicatorUtilsEventHandler.checkUUID && GameInfoHelper.INSTANCE.isHypixel())
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
                String inNether = mc.player.dimension == -1 ? nether : "";
                list.add(inNether + xyz + xPosition + " " + yPosition + " " + zPosition);

                if (ConfigManager.useCustomTextXYZ)
                {
                    xyz = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZ).getFormattedText();
                    nether = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZNether).getFormattedText();
                    overworld = JsonMessageUtils.rawTextToJson(ConfigManager.customTextXYZOverworld).getFormattedText();
                }

                if (ConfigManager.enableOverworldCoordinate && mc.player.dimension == -1)
                {
                    list.add(overworld + xyz + xPosition1 + " " + yPosition + " " + zPosition1);
                }
            }
        }
        if (ConfigManager.enableLookingAtBlock)
        {
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
        if (ConfigManager.enableBiome)
        {
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
                            biome = JsonMessageUtils.rawTextToJson(ConfigManager.customTextBiome).getFormattedText();
                        }
                        list.add(biome + value);
                    }
                }
            }
        }
        if (ConfigManager.enableEntityDetector)
        {
            if (!ConfigManager.entityDetectorMode.equalsIgnoreCase("glowing"))
            {
                AxisAlignedBB range = new AxisAlignedBB(mc.player.posX - ConfigManager.entityDetectRange, mc.player.posY - ConfigManager.entityDetectRange, mc.player.posZ - ConfigManager.entityDetectRange, mc.player.posX + ConfigManager.entityDetectRange, mc.player.posY + ConfigManager.entityDetectRange, mc.player.posZ + ConfigManager.entityDetectRange);
                AxisAlignedBB ghastRange = new AxisAlignedBB(mc.player.posX - (ConfigManager.entityDetectRange + 64), mc.player.posY - (ConfigManager.entityDetectRange + 64), mc.player.posZ - (ConfigManager.entityDetectRange + 64), mc.player.posX + (ConfigManager.entityDetectRange + 64), mc.player.posY + (ConfigManager.entityDetectRange + 64), mc.player.posZ + (ConfigManager.entityDetectRange + 64));
                List<EntityZombie> zombie = GameInfoHelper.INSTANCE.detectEntities(EntityZombie.class, range);
                List<EntitySkeleton> skeleton = GameInfoHelper.INSTANCE.detectEntities(EntitySkeleton.class, range);
                List<EntityCreeper> creeper = GameInfoHelper.INSTANCE.detectEntities(EntityCreeper.class, range);
                List<EntitySpider> spider = GameInfoHelper.INSTANCE.detectEntities(EntitySpider.class, range);
                List<EntityEnderman> enderman = GameInfoHelper.INSTANCE.detectEntities(EntityEnderman.class, range);
                List<EntityWitch> witch = GameInfoHelper.INSTANCE.detectEntities(EntityWitch.class, range);
                List<EntitySlime> slime = GameInfoHelper.INSTANCE.detectEntities(EntitySlime.class, range);
                List<EntityBlaze> blaze = GameInfoHelper.INSTANCE.detectEntities(EntityBlaze.class, range);
                List<EntityGhast> ghast = GameInfoHelper.INSTANCE.detectEntities(EntityGhast.class, ghastRange);
                List<EntityGuardian> guardian = GameInfoHelper.INSTANCE.detectEntities(EntityGuardian.class, range);
                List<EntityShulker> shulker = GameInfoHelper.INSTANCE.detectEntities(EntityShulker.class, range);

                String zombieCount = !zombie.isEmpty() ? mc.player.dimension == -1 ? "PZ: " + zombie.size() + ", " : "Z: " + zombie.size() + ", " : "";
                String skeletonCount = !skeleton.isEmpty() ? "S: " + skeleton.size() + ", " : "";
                String creeperCount = !creeper.isEmpty() ? "C: " + creeper.size() + ", " : "";
                String spiderCount = !spider.isEmpty() ? "SD: " + spider.size() + ", " : "";
                String endermanCount = !enderman.isEmpty() ? "E: " + enderman.size() + ", " : "";
                String witchCount = !witch.isEmpty() ? "W: " + witch.size() + ", " : "";
                String slimeCount = !slime.isEmpty() ? mc.player.dimension == -1 ? "M: " + slime.size() + ", " : "SL: " + slime.size() + ", " : "";
                String blazeCount = !blaze.isEmpty() ? "B: " + blaze.size() + ", " : "";
                String ghastCount = !ghast.isEmpty() ? "G: " + ghast.size() + ", " : "";
                String guardianCount = !guardian.isEmpty() ? "GD: " + guardian.size() + ", " : "";
                String shulkerCount = !shulker.isEmpty() ? "SK: " + shulker.size() + ", " : "";

                if (!zombieCount.isEmpty() || !skeletonCount.isEmpty() || !creeperCount.isEmpty() || !spiderCount.isEmpty())
                {
                    list.add(zombieCount + skeletonCount + creeperCount + spiderCount);
                }
                if (!endermanCount.isEmpty() || !witchCount.isEmpty() || !slimeCount.isEmpty() || !guardianCount.isEmpty())
                {
                    list.add(endermanCount + witchCount + slimeCount + guardianCount);
                }
                if (!blazeCount.isEmpty() || !ghastCount.isEmpty() || !shulkerCount.isEmpty())
                {
                    list.add(blazeCount + ghastCount + shulkerCount);
                }
            }
        }
        if (ConfigManager.enablePlayerDetector)
        {
            if (ConfigManager.playerDetectorMode.equalsIgnoreCase("NORMAL"))
            {
                AxisAlignedBB range = new AxisAlignedBB(mc.player.posX - 32, mc.player.posY - 32, mc.player.posZ - 32, mc.player.posX + 32, mc.player.posY + 32, mc.player.posZ + 32);
                List<EntityPlayer> player = Minecraft.getMinecraft().player.world.getEntitiesWithinAABB(EntityPlayer.class, range, GameInfoHelper.IS_DEATH_OR_SPECTATOR);
                int size = player.size() - 1;

                if (mc.world != null)
                {
                    for (Entity playerList : mc.world.loadedEntityList)
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
            if (ConfigManager.playerDetectorMode.equalsIgnoreCase("LIST"))
            {
                String name;

                if (mc.world != null)
                {
                    for (Entity playerList : mc.world.loadedEntityList)
                    {
                        if (playerList instanceof EntityOtherPlayerMP)
                        {
                            ((EntityOtherPlayerMP)playerList).setGlowing(((EntityOtherPlayerMP) playerList).isPotionActive(MobEffects.GLOWING));
                        }
                    }
                    for (Entity player : mc.world.loadedEntityList)
                    {
                        if (player instanceof EntityOtherPlayerMP)
                        {
                            int xPosP = (int)mc.player.posX;
                            int yPosP = (int)mc.player.posY;
                            int zPosP = (int)mc.player.posZ;
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
        if (ConfigManager.enableCPS)
        {
            if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("left"))
            {
                String cps = json.text("CPS: ").setStyle(json.colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
                String cpsValue = json.text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setStyle(json.colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();

                if (ConfigManager.useCustomTextCPS)
                {
                    cps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
                }
                list.add(cps + cpsValue);
            }
        }
        return list;
    }
}