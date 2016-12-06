/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils.helper;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.google.common.collect.Ordering;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.item.EntityBoat;
import net.minecraft.entity.item.EntityEnderCrystal;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.item.EntityMinecart;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.entity.monster.EntityBlaze;
import net.minecraft.entity.monster.EntityCreeper;
import net.minecraft.entity.monster.EntityEnderman;
import net.minecraft.entity.monster.EntityEndermite;
import net.minecraft.entity.monster.EntityGhast;
import net.minecraft.entity.monster.EntityGuardian;
import net.minecraft.entity.monster.EntityIronGolem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.monster.EntityPolarBear;
import net.minecraft.entity.monster.EntityShulker;
import net.minecraft.entity.monster.EntitySilverfish;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntitySlime;
import net.minecraft.entity.monster.EntitySnowman;
import net.minecraft.entity.monster.EntitySpider;
import net.minecraft.entity.monster.EntityWitch;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.entity.monster.IMob;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.entity.passive.EntityHorse;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityPig;
import net.minecraft.entity.passive.EntityRabbit;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntitySquid;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.MobEffects;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemArrow;
import net.minecraft.item.ItemElytra;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.renderer.SmallFontRenderer;
import stevekung.mods.indicatorutils.utils.ArmorType;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.helper.ObjectModeHelper.ArmorStatusPosition;
import stevekung.mods.indicatorutils.utils.helper.OffsetHelper.EnumSide;

public class StatusRendererHelper
{
    public static StatusRendererHelper INSTANCE = new StatusRendererHelper();
    public int enumRenderMode;

    public static void renderHealthStatus(EntityLivingBase entityLivingBase, String text, double x, double y, double z, double d)
    {
        Minecraft mc = Minecraft.getMinecraft();
        double distance = entityLivingBase.getDistanceSqToEntity(mc.getRenderViewEntity());
        boolean hasName = entityLivingBase.hasCustomName();

        if (distance <= d * d)
        {
            GlStateManager.pushMatrix();
            float f = 0.0F;
            GlStateManager.translate((float)x, hasName ? y + entityLivingBase.height + 0.75F - f : !GameInfoHelper.INSTANCE.isSinglePlayer() ? y + entityLivingBase.height + 1F - f : y + entityLivingBase.height + 0.5F - f, (float)z);
            GlStateManager.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((mc.getRenderManager().options.thirdPersonView == 2 ? -1 : 1) * mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-0.025F, -0.025F, 0.025F);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);
            GlStateManager.disableDepth();
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
            int j = fontrenderer.getStringWidth(text) / 2;
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            VertexBuffer vertexbuffer = tessellator.getBuffer();
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vertexbuffer.pos(-j - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexbuffer.pos(-j - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexbuffer.pos(j + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexbuffer.pos(j + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();
            StatusRendererHelper.INSTANCE.drawString(text, -fontrenderer.getStringWidth(text) / 2, 0, EnumTextColor.WHITE, false);
            GlStateManager.enableDepth();
            GlStateManager.depthMask(true);
            StatusRendererHelper.INSTANCE.drawString(text, -fontrenderer.getStringWidth(text) / 2, 0, EnumTextColor.WHITE, false);
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            GlStateManager.popMatrix();
        }
    }

    public static void renderArmorStatus(Minecraft mc)
    {
        if (mc.getRenderItem() != null)
        {
            ScaledResolution scaledRes = new ScaledResolution(mc);
            boolean flag = ObjectModeHelper.getArmorStatusMode(ArmorStatusPosition.LEFT);

            if (flag && mc.currentScreen instanceof GuiChat && !ConfigManager.displayArmorHeldWhileChatOpen && !(mc.displayWidth >= 1268 && mc.displayHeight >= 720))
            {
                return;
            }

            int helItem = flag ? ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 16 : ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 16;
            int chestItem = flag ? ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 32 : ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 32;
            int legItem = flag ? ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 48 : ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 48;
            int bootItem = flag ? ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 64 : ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 64;
            float helText = flag ? ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 20 : ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 20;
            float chestText = flag ? ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 36 : ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 36;
            float legText = flag ? ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 52 : ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 52;
            float bootText = flag ? ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 68 : ExtendedModSettings.ARMOR_STATUS_OFFSET + 32 + 68;
            int armorPosition = flag ? 2 : scaledRes.getScaledWidth() - 18;
            float armorTextPosition = 20.5F;
            float width = 0;
            ItemStack mainItem = mc.thePlayer.getHeldItemMainhand();
            ItemStack offItem = mc.thePlayer.getHeldItemOffhand();
            int itemCount = 0;
            int arrowCount = StatusRendererHelper.INSTANCE.countArrowInInventory(mc.thePlayer);
            SmallFontRenderer font = new SmallFontRenderer();

            if (ObjectModeHelper.getArmorStatusMode(ArmorStatusPosition.HOTBAR_LEFT))
            {
                if (!mc.thePlayer.isSpectator())
                {
                    if (ConfigManager.enableArmorStatus)
                    {
                        if (StatusRendererHelper.INSTANCE.getHelmet())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.HELMET, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT));
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT), EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getChestplate())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.CHESTPLATE, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 16);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 16, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getLeggings())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.LEGGINGS, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 32);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 32, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getBoots())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.BOOTS, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);
                        }
                    }
                    if (ConfigManager.enableHeldItemInHand)
                    {
                        if (mainItem != null)
                        {
                            boolean isTools = mainItem.isItemStackDamageable();

                            if (mainItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem.getItem(), mainItem.getMetadata());
                            }

                            String countString = JsonMessageUtils.textToJson(String.valueOf(itemCount), ConfigManager.customColorHeldItem).getFormattedText();
                            String arrowCountString = JsonMessageUtils.textToJson(String.valueOf(arrowCount), ConfigManager.customColorHeldItemArrowCount).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            StatusRendererHelper.INSTANCE.renderItemWithEffect(mainItem, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                            StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);

                            if (mainItem.getItem().equals(Items.BOW))
                            {
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 + 90 + 8, scaledRes.getScaledHeight() - 32, EnumTextColor.WHITE, true);
                                GlStateManager.enableDepth();
                            }
                        }
                        if (offItem != null)
                        {
                            boolean isTools = offItem.isItemStackDamageable();

                            if (offItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, offItem.getItem(), offItem.getMetadata());
                            }

                            String countString = JsonMessageUtils.textToJson(String.valueOf(itemCount), ConfigManager.customColorHeldItem).getFormattedText();
                            String arrowCountString = JsonMessageUtils.textToJson(String.valueOf(arrowCount), ConfigManager.customColorHeldItemArrowCount).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            StatusRendererHelper.INSTANCE.renderItemWithEffect(offItem, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 32);
                            StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(offItem) : countString, scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 32, EnumTextColor.WHITE, true);

                            if (offItem.getItem().equals(Items.BOW))
                            {
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 + 90 + 8, scaledRes.getScaledHeight() - 48, EnumTextColor.WHITE, true);
                                GlStateManager.enableDepth();
                            }
                        }
                    }
                }
            }
            else if (ObjectModeHelper.getArmorStatusMode(ArmorStatusPosition.HOTBAR_RIGHT))
            {
                if (!mc.thePlayer.isSpectator())
                {
                    if (ConfigManager.enableArmorStatus)
                    {
                        if (StatusRendererHelper.INSTANCE.getHelmet())
                        {
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.HELMET, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT));
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT), EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getChestplate())
                        {
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.CHESTPLATE, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 16);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 16, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getLeggings())
                        {
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.LEGGINGS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 32);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 32, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getBoots())
                        {
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.BOOTS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);
                        }
                    }
                    if (ConfigManager.enableHeldItemInHand && !mc.thePlayer.isSpectator())
                    {
                        if (mainItem != null)
                        {
                            boolean isTools = mainItem.isItemStackDamageable();

                            if (mainItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem.getItem(), mainItem.getMetadata());
                            }

                            String countString = JsonMessageUtils.textToJson(String.valueOf(itemCount), ConfigManager.customColorHeldItem).getFormattedText();
                            String arrowCountString = JsonMessageUtils.textToJson(String.valueOf(arrowCount), ConfigManager.customColorHeldItemArrowCount).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            width = mc.fontRendererObj.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                            StatusRendererHelper.INSTANCE.renderItemWithEffect(mainItem, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                            StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);

                            if (mainItem.getItem().equals(Items.BOW))
                            {
                                width = font.getStringWidth(arrowCountString);
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 - 94 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 52, EnumTextColor.WHITE, true);
                                GlStateManager.enableDepth();
                            }
                        }
                        if (offItem != null)
                        {
                            boolean isTools = offItem.isItemStackDamageable();

                            if (offItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, offItem.getItem(), offItem.getMetadata());
                            }

                            String countString = JsonMessageUtils.textToJson(String.valueOf(itemCount), ConfigManager.customColorHeldItem).getFormattedText();
                            String arrowCountString = JsonMessageUtils.textToJson(String.valueOf(arrowCount), ConfigManager.customColorHeldItemArrowCount).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            width = mc.fontRendererObj.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(offItem) : countString);
                            StatusRendererHelper.INSTANCE.renderItemWithEffect(offItem, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 32);
                            StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(offItem) : countString, scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 32, EnumTextColor.WHITE, true);

                            if (offItem.getItem().equals(Items.BOW))
                            {
                                width = font.getStringWidth(arrowCountString);
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 - 94 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 36, EnumTextColor.WHITE, true);
                                GlStateManager.enableDepth();
                            }
                        }
                    }
                }
            }
            else if (ObjectModeHelper.getArmorStatusMode(ArmorStatusPosition.HOTBAR))
            {
                if (!mc.thePlayer.isSpectator())
                {
                    if (ConfigManager.enableArmorStatus)
                    {
                        if (StatusRendererHelper.INSTANCE.getHelmet())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.HELMET, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR));
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR), EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getChestplate())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.CHESTPLATE, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) + 16);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) + 16, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getLeggings())
                        {
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.LEGGINGS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR));
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - 52, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getBoots())
                        {
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.BOOTS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) + 16);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) + 16, EnumTextColor.WHITE, true);
                        }
                    }
                    if (ConfigManager.enableHeldItemInHand)
                    {
                        if (mainItem != null)
                        {
                            boolean isTools = mainItem.isItemStackDamageable();

                            if (mainItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem.getItem(), mainItem.getMetadata());
                            }

                            String countString = JsonMessageUtils.textToJson(String.valueOf(itemCount), ConfigManager.customColorHeldItem).getFormattedText();
                            String arrowCountString = JsonMessageUtils.textToJson(String.valueOf(arrowCount), ConfigManager.customColorHeldItemArrowCount).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            width = mc.fontRendererObj.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                            StatusRendererHelper.INSTANCE.renderItemWithEffect(mainItem, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) - 16);
                            StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) - 16, EnumTextColor.WHITE, true);

                            if (mainItem.getItem().equals(Items.BOW))
                            {
                                width = font.getStringWidth(arrowCountString);
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 - 94 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) - 12, EnumTextColor.WHITE, true);
                                GlStateManager.enableDepth();
                            }
                        }
                        if (offItem != null)
                        {
                            boolean isTools = offItem.isItemStackDamageable();

                            if (offItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, offItem.getItem(), offItem.getMetadata());
                            }

                            String countString = JsonMessageUtils.textToJson(String.valueOf(itemCount), ConfigManager.customColorHeldItem).getFormattedText();
                            String arrowCountString = JsonMessageUtils.textToJson(String.valueOf(arrowCount), ConfigManager.customColorHeldItemArrowCount).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            StatusRendererHelper.INSTANCE.renderItemWithEffect(offItem, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) - 16);
                            StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(offItem) : countString, scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - 52 - 16, EnumTextColor.WHITE, true);

                            if (offItem.getItem().equals(Items.BOW))
                            {
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 + 90 + 8, scaledRes.getScaledHeight() - 52 - 12, EnumTextColor.WHITE, true);
                                GlStateManager.enableDepth();
                            }
                        }
                    }
                }
            }
            else
            {
                if (ConfigManager.enableArmorStatus)
                {
                    if (!mc.gameSettings.showDebugInfo)
                    {
                        if (StatusRendererHelper.INSTANCE.getHelmet())
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.HELMET, armorPosition, helItem);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), flag ? armorTextPosition : width, helText, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getChestplate())
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.CHESTPLATE, armorPosition, chestItem);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), flag ? armorTextPosition : width, chestText, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getLeggings())
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.LEGGINGS, armorPosition, legItem);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), flag ? armorTextPosition : width, legText, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getBoots())
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS));
                            StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.BOOTS, armorPosition, bootItem);
                            StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS), flag ? armorTextPosition : width, bootText, EnumTextColor.WHITE, true);
                        }
                    }
                }
                if (ConfigManager.enableHeldItemInHand)
                {
                    if (mainItem != null)
                    {
                        boolean isTools = mainItem.isItemStackDamageable();

                        if (mainItem.getMaxStackSize() > 1)
                        {
                            itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem.getItem(), mainItem.getMetadata());
                        }

                        String countString = JsonMessageUtils.textToJson(String.valueOf(itemCount), ConfigManager.customColorHeldItem).getFormattedText();
                        String arrowCountString = JsonMessageUtils.textToJson(String.valueOf(arrowCount), ConfigManager.customColorHeldItemArrowCount).getFormattedText();

                        if (itemCount == 0 || itemCount == 1)
                        {
                            countString = "";
                        }
                        if (arrowCount == 0)
                        {
                            arrowCountString = "";
                        }

                        width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                        StatusRendererHelper.INSTANCE.renderItemWithEffect(mainItem, armorPosition, bootItem + 16);
                        StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, flag ? armorTextPosition : width, bootText + 16, EnumTextColor.WHITE, true);

                        if (mainItem.getItem().equals(Items.BOW))
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition + 18 - font.getStringWidth(arrowCountString);
                            GlStateManager.disableDepth();
                            font.drawString(arrowCountString, flag ? armorTextPosition - 16 : width, bootText + 16 + 4, EnumTextColor.WHITE, true);
                            GlStateManager.enableDepth();
                        }
                    }
                    if (offItem != null)
                    {
                        boolean isTools = offItem.isItemStackDamageable();

                        if (offItem.getMaxStackSize() > 1)
                        {
                            itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, offItem.getItem(), offItem.getMetadata());
                        }

                        String countString = JsonMessageUtils.textToJson(String.valueOf(itemCount), ConfigManager.customColorHeldItem).getFormattedText();
                        String arrowCountString = JsonMessageUtils.textToJson(String.valueOf(arrowCount), ConfigManager.customColorHeldItemArrowCount).getFormattedText();

                        if (itemCount == 0 || itemCount == 1)
                        {
                            countString = "";
                        }
                        if (arrowCount == 0)
                        {
                            arrowCountString = "";
                        }

                        width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(offItem) : countString);
                        StatusRendererHelper.INSTANCE.renderItemWithEffect(offItem, armorPosition, bootItem + 32);
                        StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(offItem) : countString, flag ? armorTextPosition : width, bootText + 32, EnumTextColor.WHITE, true);

                        if (offItem.getItem().equals(Items.BOW))
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition + 18 - font.getStringWidth(arrowCountString);
                            GlStateManager.disableDepth();
                            font.drawString(arrowCountString, flag ? armorTextPosition - 16 : width, bootText + 32 + 4, EnumTextColor.WHITE, true);
                            GlStateManager.enableDepth();
                        }
                    }
                }
            }
        }
    }

    public static void renderPotionEffect(Minecraft mc)
    {
        if (ConfigManager.enablePotionStatus)
        {
            if (ConfigManager.potionStatusPosition.equals("HOTBAR_LEFT"))
            {
                boolean showIcon = ConfigManager.showPotionIcon;
                boolean iconAndTime = ConfigManager.potionStatusStyle.equals("ICON_AND_TIME");
                ScaledResolution scaledRes = new ScaledResolution(mc);
                int xPotion = scaledRes.getScaledWidth() / 2 - 91 - 35;
                int yPotion = scaledRes.getScaledHeight() - OffsetHelper.getHotbarPotionOffset();
                int size = ConfigManager.potionSize;
                int length = ConfigManager.potionLength;
                int lengthVal = ConfigManager.potionLengthVal;
                Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

                if (!collection.isEmpty())
                {
                    if (collection.size() > size)
                    {
                        length = lengthVal / (collection.size() - 1);
                    }

                    for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection))
                    {
                        Potion potion = potioneffect.getPotion();
                        String s = Potion.getPotionDurationString(potioneffect, 1.0F);
                        String s1 = I18n.format(potion.getName(), new Object[0]);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                        if (showIcon)
                        {
                            mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                            int i1 = potion.getStatusIconIndex();
                            mc.ingameGUI.drawTexturedModalRect(xPotion + 12, yPotion + 6, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                        }

                        if (potioneffect.getAmplifier() == 1)
                        {
                            s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
                        }
                        else if (potioneffect.getAmplifier() == 2)
                        {
                            s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
                        }
                        else if (potioneffect.getAmplifier() == 3)
                        {
                            s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
                        }
                        int stringwidth1 = mc.fontRendererObj.getStringWidth(s);
                        int stringwidth2 = mc.fontRendererObj.getStringWidth(s1);

                        if (!iconAndTime)
                        {
                            StatusRendererHelper.INSTANCE.drawString(s1, showIcon ? xPotion + 8 - stringwidth2 : xPotion + 28 - stringwidth2, yPotion + 6, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                        }
                        StatusRendererHelper.INSTANCE.drawString(s, showIcon ? xPotion + 8 - stringwidth1 : xPotion + 28 - stringwidth1, iconAndTime ? yPotion + 11 : yPotion + 16, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                        yPotion -= length;
                    }
                }
            }
            else if (ConfigManager.potionStatusPosition.equals("HOTBAR_RIGHT"))
            {
                boolean showIcon = ConfigManager.showPotionIcon;
                boolean iconAndTime = ConfigManager.potionStatusStyle.equals("ICON_AND_TIME");
                ScaledResolution scaledRes = new ScaledResolution(mc);
                int xPotion = scaledRes.getScaledWidth() / 2 + 91 - 20;
                int yPotion = scaledRes.getScaledHeight() - 42;
                int size = ConfigManager.potionSize;
                int length = ConfigManager.potionLength;
                int lengthVal = ConfigManager.potionLengthVal;
                Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

                if (!collection.isEmpty())
                {
                    if (collection.size() > size)
                    {
                        length = lengthVal / (collection.size() - 1);
                    }

                    for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection))
                    {
                        Potion potion = potioneffect.getPotion();
                        String s = Potion.getPotionDurationString(potioneffect, 1.0F);
                        String s1 = I18n.format(potion.getName(), new Object[0]);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                        if (showIcon)
                        {
                            mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                            int i1 = potion.getStatusIconIndex();
                            mc.ingameGUI.drawTexturedModalRect(xPotion + 24, yPotion + 6, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                        }

                        if (potioneffect.getAmplifier() == 1)
                        {
                            s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
                        }
                        else if (potioneffect.getAmplifier() == 2)
                        {
                            s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
                        }
                        else if (potioneffect.getAmplifier() == 3)
                        {
                            s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
                        }

                        if (!iconAndTime)
                        {
                            StatusRendererHelper.INSTANCE.drawString(s1, showIcon ? xPotion + 46 : xPotion + 28, yPotion + 6, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                        }
                        StatusRendererHelper.INSTANCE.drawString(s, showIcon ? xPotion + 46 : xPotion + 28, iconAndTime ? yPotion + 11 : yPotion + 16, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                        yPotion -= length;
                    }
                }
            }
            else
            {
                if (!mc.gameSettings.showDebugInfo)
                {
                    boolean right = ConfigManager.potionStatusPosition.equals("RIGHT");
                    boolean showIcon = ConfigManager.showPotionIcon;
                    boolean iconAndTime = ConfigManager.potionStatusStyle.equals("ICON_AND_TIME");
                    ScaledResolution scaledRes = new ScaledResolution(mc);
                    int xPotion = right ? scaledRes.getScaledWidth() - 32 : -24;
                    int yPotion = scaledRes.getScaledHeight() - 220 + ExtendedModSettings.POTION_STATUS_OFFSET + 90;
                    int size = ConfigManager.potionSize;
                    int length = ConfigManager.potionLength;
                    int lengthVal = ConfigManager.potionLengthVal;
                    Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

                    if (!collection.isEmpty())
                    {
                        if (collection.size() > size)
                        {
                            length = lengthVal / (collection.size() - 1);
                        }

                        for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection))
                        {
                            Potion potion = potioneffect.getPotion();
                            String s = Potion.getPotionDurationString(potioneffect, 1.0F);
                            String s1 = I18n.format(potion.getName(), new Object[0]);
                            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                            if (showIcon)
                            {
                                mc.getTextureManager().bindTexture(GuiContainer.INVENTORY_BACKGROUND);
                                int i1 = potion.getStatusIconIndex();
                                mc.ingameGUI.drawTexturedModalRect(right ? xPotion + 12 : xPotion + 28, yPotion + 6, 0 + i1 % 8 * 18, 198 + i1 / 8 * 18, 18, 18);
                            }

                            if (potioneffect.getAmplifier() == 1)
                            {
                                s1 = s1 + " " + I18n.format("enchantment.level.2", new Object[0]);
                            }
                            else if (potioneffect.getAmplifier() == 2)
                            {
                                s1 = s1 + " " + I18n.format("enchantment.level.3", new Object[0]);
                            }
                            else if (potioneffect.getAmplifier() == 3)
                            {
                                s1 = s1 + " " + I18n.format("enchantment.level.4", new Object[0]);
                            }
                            int stringwidth1 = mc.fontRendererObj.getStringWidth(s);
                            int stringwidth2 = mc.fontRendererObj.getStringWidth(s1);

                            if (!iconAndTime)
                            {
                                StatusRendererHelper.INSTANCE.drawString(s1, right ? showIcon ? xPotion + 8 - stringwidth2 : xPotion + 28 - stringwidth2 : showIcon ? xPotion + 50 : xPotion + 28, yPotion + 6, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                            }
                            StatusRendererHelper.INSTANCE.drawString(s, right ? showIcon ? xPotion + 8 - stringwidth1 : xPotion + 28 - stringwidth1 : showIcon ? xPotion + 50 : xPotion + 28, iconAndTime ? yPotion + 11 : yPotion + 16, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                            yPotion += length;
                        }
                    }
                }
            }
        }
    }

    public static void renderTimeInformation(Minecraft mc)
    {
        if (!mc.gameSettings.showDebugInfo)
        {
            List<String> list = Lists.newArrayList(new String[] {});
            ScaledResolution scaledRes = new ScaledResolution(mc);
            boolean swapToRight = ConfigManager.swapMainRenderInfoToRight;
            boolean shortDateBool = ConfigManager.useShortDate;
            String day = JsonMessageUtils.textToJson(new SimpleDateFormat("d", StatusRendererHelper.getDateFormat()).format(new Date()), ConfigManager.customColorCurrentTimeDay).getFormattedText();
            String month = JsonMessageUtils.textToJson(new SimpleDateFormat(shortDateBool ? "M" : "MMM", StatusRendererHelper.getDateFormat()).format(new Date()), ConfigManager.customColorCurrentTimeMonth).getFormattedText();
            String year = JsonMessageUtils.textToJson(new SimpleDateFormat("yyyy", StatusRendererHelper.getDateFormat()).format(new Date()), ConfigManager.customColorCurrentTimeYear).getFormattedText();
            String timeValue = JsonMessageUtils.textToJson(new SimpleDateFormat("HH:mm:ss", StatusRendererHelper.getDateFormat()).format(new Date()), ConfigManager.customColorCurrentTimeValue).getFormattedText();
            String AMPM = JsonMessageUtils.textToJson(new SimpleDateFormat("a", StatusRendererHelper.getDateFormat()).format(new Date()), ConfigManager.customColorTimeAMPM).getFormattedText();
            // หลัง = \u0e2b\u0e25\u0e31\u0e07, ก่อน = \u0e01\u0e48\u0e2d\u0e19, เที่ยง = \u0e40\u0e17\u0e35\u0e48\u0e22\u0e07
            AMPM = AMPM.replace("\u0e2b\u0e25\u0e31\u0e07\u0e40\u0e17\u0e35\u0e48\u0e22\u0e07", "PM").replace("\u0e01\u0e48\u0e2d\u0e19\u0e40\u0e17\u0e35\u0e48\u0e22\u0e07", "AM");
            String date = day + "/" + month + "/" + year + " " + timeValue + " " + AMPM;
            String timeText = JsonMessageUtils.textToJson("Time: ", ConfigManager.customColorCurrentTime).getFormattedText();
            String timeZoneText = JsonMessageUtils.textToJson("Time Zone: ", ConfigManager.customColorTimeZone).getFormattedText();
            String timeZoneValue = JsonMessageUtils.textToJson(TimeZone.getDefault().getDisplayName(), ConfigManager.customColorTimeZoneValue).getFormattedText();

            if (ConfigManager.useCustomTextTime)
            {
                timeText = JsonMessageUtils.rawTextToJson(ConfigManager.customTextTime).getFormattedText();
            }
            if (ConfigManager.useCustomTextTimeZone)
            {
                timeZoneText = JsonMessageUtils.rawTextToJson(ConfigManager.customTextTimeZone).getFormattedText();
            }

            if (ConfigManager.enableCurrentTime)
            {
                list.add(timeText + date);
            }
            if (ConfigManager.enableGameTime)
            {
                String prefixText = JsonMessageUtils.textToJson(" | ", ConfigManager.customColorWeatherPrefix).getFormattedText();
                String rainingText = JsonMessageUtils.textToJson("Raining", ConfigManager.customColorRaining).getFormattedText();
                String thunderText = JsonMessageUtils.textToJson("Thunder", ConfigManager.customColorThunder).getFormattedText();

                if (ConfigManager.useCustomWeather)
                {
                    prefixText = JsonMessageUtils.rawTextToJson(ConfigManager.customTextWeather).getFormattedText();
                    rainingText = JsonMessageUtils.rawTextToJson(ConfigManager.customTextRaining).getFormattedText();
                    thunderText = JsonMessageUtils.rawTextToJson(ConfigManager.customTextThunder).getFormattedText();
                }
                String prefix = mc.theWorld.isRaining() | mc.theWorld.isThundering() ? prefixText : "";
                String weather = mc.theWorld.isRaining() && !mc.theWorld.isThundering() ? rainingText : mc.theWorld.isRaining() && mc.theWorld.isThundering() ? thunderText : "";

                if (ConfigManager.enableWeatherStatus)
                {
                    list.add(GameInfoHelper.INSTANCE.getInGameTime(mc.theWorld.getWorldTime() % 24000) + prefix + weather);
                }
                else
                {
                    list.add(GameInfoHelper.INSTANCE.getInGameTime(mc.theWorld.getWorldTime() % 24000));
                }
            }
            if (ConfigManager.enableTimeZone)
            {
                list.add(timeZoneText + timeZoneValue);
            }
            if (ConfigManager.enableStandardWorldTime)
            {
                SimpleDateFormat gmt = new SimpleDateFormat("d MMM yyyy HH:mm:ss z", Locale.US);
                gmt.setTimeZone(TimeZone.getTimeZone(ConfigManager.timeZoneName));
                String timeZone = JsonMessageUtils.textToJson(gmt.format(new Date()), ConfigManager.customColorWorldTime).getFormattedText();
                list.add(timeZone);
            }
            if (ConfigManager.enableCPS)
            {
                if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("right"))
                {
                    String cps = JsonMessageUtils.textToJson("CPS: ", ConfigManager.customColorCPS).getFormattedText();
                    String cpsValue = JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getCPS()), ConfigManager.customColorCPSValue).getFormattedText();

                    if (ConfigManager.useCustomTextCPS)
                    {
                        cps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
                    }
                    list.add(cps + cpsValue);
                }
            }

            for (int i = 0; i < list.size(); ++i)
            {
                String s = list.get(i);

                if (!Strings.isNullOrEmpty(s))
                {
                    float y = 3.5F;

                    if (!swapToRight && !IndicatorUtils.MC_VERSION.equals("1.8.9"))
                    {
                        Collection<PotionEffect> collection = mc.thePlayer.getActivePotionEffects();

                        if (!collection.isEmpty() && ConfigManager.renderIngamePotionEffect)
                        {
                            for (PotionEffect potioneffect : Ordering.natural().sortedCopy(collection))
                            {
                                if (potioneffect.doesShowParticles())
                                {
                                    y = 53.0F;
                                }
                            }
                        }
                    }
                    int j = mc.fontRendererObj.FONT_HEIGHT + 1;
                    int k = mc.fontRendererObj.getStringWidth(s);
                    int l = scaledRes.getScaledWidth() - 2 - k;
                    float i1 = y + j * i;
                    StatusRendererHelper.INSTANCE.drawString(s, swapToRight ? 3.5F : l, i1, EnumTextColor.WHITE, true);
                }
            }
        }
    }

    public void renderArmorWithEffect(ArmorType type, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        if (ConfigManager.enableArmorStatusRenderBug)
        {
            GlStateManager.disableDepth();
        }
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(this.getArmorType(type), x, y);
        if (ConfigManager.enableArmorStatusRenderBug)
        {
            GlStateManager.enableDepth();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, this.getArmorType(type), x, y);
        GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
        GlStateManager.disableLighting();
    }

    public void renderItemWithEffect(ItemStack itemStack, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        if (ConfigManager.enableHeldItemRenderBug)
        {
            GlStateManager.disableDepth();
        }
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
        if (ConfigManager.enableHeldItemRenderBug)
        {
            GlStateManager.enableDepth();
        }
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();

        if (itemStack.isItemStackDamageable())
        {
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.disableLighting();
            GlStateManager.enableRescaleNormal();
            GlStateManager.enableColorMaterial();
            GlStateManager.disableLighting();
            GlStateManager.enableCull();
            Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, itemStack, x, y);
            GlStateManager.blendFunc(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA);
            GlStateManager.disableLighting();
        }
    }

    public void drawString(String message, float x, float y, EnumTextColor color, boolean shadow)
    {
        this.drawString(message, x, y, color.getColor(), shadow);
    }

    public void drawString(String message, float x, float y, int color, boolean shadow)
    {
        Minecraft.getMinecraft().fontRendererObj.drawString(message, x, y, color, shadow);
    }

    public void drawStringAtRecord(String text, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc);
        boolean isCreative = mc.thePlayer.capabilities.isCreativeMode;
        int height = isCreative ? 60 : 80;
        int i = scaledresolution.getScaledWidth();
        int j = scaledresolution.getScaledHeight();
        float f2 = 256 - partialTicks;
        int l1 = (int)(f2 * 255.0F / 20.0F);

        if (l1 > 255)
        {
            l1 = 255;
        }
        if (l1 > 8)
        {
            GlStateManager.pushMatrix();
            GlStateManager.translate(i / 2, j - height + -0.5F, 0.0F);
            StatusRendererHelper.INSTANCE.drawRect(6, -6, mc.fontRendererObj.getStringWidth(text) / 2 + 2, -mc.fontRendererObj.getStringWidth(text) / 2 - 2, 16777216);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(i / 2, j - height, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            int l = 16777215;
            Minecraft.getMinecraft().fontRendererObj.drawString(text, -mc.fontRendererObj.getStringWidth(text) / 2, -4, l + (l1 << 24 & -16777216), true);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public boolean getHelmet()
    {
        return this.getArmorType(ArmorType.HELMET) != null && this.getArmorType(ArmorType.HELMET).getItem() instanceof ItemArmor;
    }

    public boolean getChestplate()
    {
        return this.getArmorType(ArmorType.CHESTPLATE) != null && (this.getArmorType(ArmorType.CHESTPLATE).getItem() instanceof ItemArmor || this.getArmorType(ArmorType.CHESTPLATE).getItem() instanceof ItemElytra);
    }

    public boolean getLeggings()
    {
        return this.getArmorType(ArmorType.LEGGINGS) != null && this.getArmorType(ArmorType.LEGGINGS).getItem() instanceof ItemArmor;
    }

    public boolean getBoots()
    {
        return this.getArmorType(ArmorType.BOOTS) != null && this.getArmorType(ArmorType.BOOTS).getItem() instanceof ItemArmor;
    }

    public int calculateArmorDurabilityPercent(ArmorType type)
    {
        if (this.getArmorType(type).getMaxDamage() <= 0)
        {
            return 0;
        }
        else
        {
            return 100 - this.getArmorType(type).getItemDamage() * 100 / this.getArmorType(type).getMaxDamage();
        }
    }

    public int calculateItemDurabilityPercent(ItemStack itemStack)
    {
        if (itemStack.getMaxDamage() <= 0)
        {
            return 0;
        }
        else
        {
            return 100 - itemStack.getItemDamage() * 100 / itemStack.getMaxDamage();
        }
    }

    public String getArmorDurability1(ArmorType type)
    {
        return JsonMessageUtils.textToJson(String.valueOf(this.getArmorType(type).getMaxDamage() - this.getArmorType(type).getItemDamage()), ConfigManager.customColorArmorDamageDurability).getFormattedText() + "/" + JsonMessageUtils.textToJson(String.valueOf(this.getArmorType(type).getMaxDamage()), ConfigManager.customColorArmorMaxDurability).getFormattedText();
    }

    public String getArmorDurability2(ArmorType type)
    {
        return JsonMessageUtils.textToJson(String.valueOf(this.getArmorType(type).getMaxDamage() - this.getArmorType(type).getItemDamage()), ConfigManager.customColorArmorDamageDurability).getFormattedText();
    }

    public ItemStack getArmorType(ArmorType type)
    {
        if (type == ArmorType.HELMET)
        {
            return Minecraft.getMinecraft().thePlayer.inventory.armorInventory[3];
        }
        else if (type == ArmorType.CHESTPLATE)
        {
            return Minecraft.getMinecraft().thePlayer.inventory.armorInventory[2];
        }
        else if (type == ArmorType.LEGGINGS)
        {
            return Minecraft.getMinecraft().thePlayer.inventory.armorInventory[1];
        }
        else
        {
            return Minecraft.getMinecraft().thePlayer.inventory.armorInventory[0];
        }
    }

    public static String getArmorStatusType(ArmorType armorType)
    {
        if (ConfigManager.armorStatusMode.equals("NORMAL_1"))
        {
            return StatusRendererHelper.INSTANCE.getArmorDurability1(armorType);
        }
        else if (ConfigManager.armorStatusMode.equals("NORMAL_2"))
        {
            return StatusRendererHelper.INSTANCE.getArmorDurability2(armorType);
        }
        else if (ConfigManager.armorStatusMode.equals("PERCENT"))
        {
            return JsonMessageUtils.textToJson(String.valueOf(StatusRendererHelper.INSTANCE.calculateArmorDurabilityPercent(armorType) + "%"), ConfigManager.customColorArmorPercent).getFormattedText();
        }
        else
        {
            return "";
        }
    }

    public static String getHeldItemStatus(ItemStack itemStack)
    {
        if (ConfigManager.heldItemStatusMode.equals("PERCENT"))
        {
            return JsonMessageUtils.textToJson(String.valueOf(StatusRendererHelper.INSTANCE.calculateItemDurabilityPercent(itemStack) + "%"), ConfigManager.customColorHeldItem).getFormattedText();
        }
        else if (ConfigManager.heldItemStatusMode.equals("NORMAL_2"))
        {
            return JsonMessageUtils.textToJson(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage()), ConfigManager.customColorHeldItem).getFormattedText() + "/" + JsonMessageUtils.textToJson(String.valueOf(itemStack.getMaxDamage()), ConfigManager.customColorHeldItem).getFormattedText();
        }
        else if (ConfigManager.heldItemStatusMode.equals("NORMAL"))
        {
            return JsonMessageUtils.textToJson(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage()), ConfigManager.customColorHeldItem).getFormattedText();
        }
        else
        {
            return "";
        }
    }

    public void setDisplayMode(int type)
    {
        String mode = "";

        if (type == 0)
        {
            mode = "Default Mode";
            ExtendedModSettings.DISPLAY_MODE = "default";
        }
        if (type == 1)
        {
            mode = "UHC Mode";
            ExtendedModSettings.DISPLAY_MODE = "uhc";
        }
        if (type == 2)
        {
            mode = "PvP Mode";
            ExtendedModSettings.DISPLAY_MODE = "pvp";
        }
        if (type == 3)
        {
            mode = "Command Block Mode";
            ExtendedModSettings.DISPLAY_MODE = "command";
        }
        ExtendedModSettings.saveExtendedSettings();
        Minecraft.getMinecraft().ingameGUI.setRecordPlaying(JsonMessageUtils.textToJson("Change display mode to " + "[" + mode + "]"), false);
    }

    public void drawRect(double top, double bottom, double left, double right, int color)
    {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        vertexbuffer.pos(right, top, 0.0D).color(r, g, b, 0.35F).endVertex();
        vertexbuffer.pos(left, top, 0.0D).color(r, g, b, 0.35F).endVertex();
        vertexbuffer.pos(left, bottom, 0.0D).color(r, g, b, 0.35F).endVertex();
        vertexbuffer.pos(right, bottom, 0.0D).color(r, g, b, 0.35F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static String convertToRainbowColor(String[] value)
    {
        int[] colors = { 3, 5, 13, 1, 2, 4, 12 };
        int i = 0;
        String text = "";
        String[] as;
        int k = (as = value).length;

        for (int j = 0; j < k; j++)
        {
            String part = as[j];
            char[] __tmpChars = part.toCharArray();
            char[] ac;
            int i1 = (ac = __tmpChars).length;

            for (int l = 0; l < i1; l++)
            {
                char __curr = ac[l];
                text = text + ChatFormatting.PREFIX_CODE + Integer.toString(colors[i] % 15 + 1, 16) + Character.toString(__curr);

                if (i < colors.length - 1)
                {
                    i++;
                }
                else
                {
                    i = 0;
                }
            }
            text = text + " ";
        }
        return text;
    }

    private static Locale getDateFormat()
    {
        if (ConfigManager.dateFormat.equals("US"))
        {
            return Locale.ENGLISH;
        }
        else if (ConfigManager.dateFormat.equals("JP"))
        {
            return Locale.JAPAN;
        }
        else if (ConfigManager.dateFormat.equals("CN"))
        {
            return Locale.CHINA;
        }
        else
        {
            return Locale.forLanguageTag("th-TH");
        }
    }

    private static EnumTextColor highlightPotionTextColor(Potion potion)
    {
        EnumTextColor color = EnumTextColor.WHITE;

        if (potion == MobEffects.ABSORPTION)
        {
            color = EnumTextColor.ABSORPTION;
        }
        else if (potion == MobEffects.REGENERATION)
        {
            color = EnumTextColor.REGENERATION;
        }
        else if (potion == MobEffects.STRENGTH)
        {
            color = EnumTextColor.STRENGTH;
        }
        else if (potion == MobEffects.SPEED)
        {
            color = EnumTextColor.SPEED;
        }
        else if (potion == MobEffects.FIRE_RESISTANCE)
        {
            color = EnumTextColor.FIRE_RESISTANCE;
        }
        else if (potion == MobEffects.RESISTANCE)
        {
            color = EnumTextColor.RESISTANCE;
        }
        else if (potion == MobEffects.JUMP_BOOST)
        {
            color = EnumTextColor.JUMP_BOOST;
        }
        else if (potion == MobEffects.NIGHT_VISION)
        {
            color = EnumTextColor.NIGHT_VISION;
        }
        else if (potion == MobEffects.WATER_BREATHING)
        {
            color = EnumTextColor.WATER_BREATHING;
        }
        else if (potion == MobEffects.SLOWNESS)
        {
            color = EnumTextColor.SLOWNESS;
        }
        else if (potion == MobEffects.HASTE)
        {
            color = EnumTextColor.HASTE;
        }
        else if (potion == MobEffects.MINING_FATIGUE)
        {
            color = EnumTextColor.MINING_FATIGUE;
        }
        else if (potion == MobEffects.NAUSEA)
        {
            color = EnumTextColor.NAUSEA;
        }
        else if (potion == MobEffects.INVISIBILITY)
        {
            color = EnumTextColor.INVISIBILITY;
        }
        else if (potion == MobEffects.BLINDNESS)
        {
            color = EnumTextColor.BLINDNESS;
        }
        else if (potion == MobEffects.HUNGER)
        {
            color = EnumTextColor.HUNGER;
        }
        else if (potion == MobEffects.WEAKNESS)
        {
            color = EnumTextColor.WEAKNESS;
        }
        else if (potion == MobEffects.POISON)
        {
            color = EnumTextColor.POISON;
        }
        else if (potion == MobEffects.WITHER)
        {
            color = EnumTextColor.WITHER;
        }
        else if (potion == MobEffects.HEALTH_BOOST)
        {
            color = EnumTextColor.HEALTH_BOOST;
        }
        else if (potion == MobEffects.GLOWING)
        {
            color = EnumTextColor.GLOWING;
        }
        else if (potion == MobEffects.LEVITATION)
        {
            color = EnumTextColor.LEVITATION;
        }
        else if (potion == MobEffects.LUCK)
        {
            color = EnumTextColor.LUCK;
        }
        else if (potion == MobEffects.UNLUCK)
        {
            color = EnumTextColor.UNLUCK;
        }
        return color;
    }

    public static void initEntityDetectorWithGlowing()
    {
        Minecraft mc = Minecraft.getMinecraft();

        if (ConfigManager.enableEntityDetector && ConfigManager.entityDetectorMode.equalsIgnoreCase("glowing"))
        {
            if (mc.theWorld != null)
            {
                for (Entity list : mc.theWorld.loadedEntityList)
                {
                    if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("all"))
                    {
                        if (!(list instanceof EntityPlayerSP))
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("onlymob"))
                    {
                        if (list instanceof EntityMob || list instanceof IMob)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("onlycreature"))
                    {
                        if (list instanceof EntityAnimal)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("onlynonmob"))
                    {
                        if (list instanceof EntityItem || list instanceof EntityXPOrb || list instanceof EntityArmorStand || list instanceof EntityBoat || list instanceof EntityMinecart)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("onlyplayer"))
                    {
                        if (list instanceof EntityOtherPlayerMP)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("zombie"))
                    {
                        if (list instanceof EntityZombie)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("creeper"))
                    {
                        if (list instanceof EntityCreeper)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("skeleton"))
                    {
                        if (list instanceof EntitySkeleton)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("spider"))
                    {
                        if (list instanceof EntitySpider)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("slime"))
                    {
                        if (list instanceof EntitySlime)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("ghast"))
                    {
                        if (list instanceof EntityGhast)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("enderman"))
                    {
                        if (list instanceof EntityEnderman)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("silverfish"))
                    {
                        if (list instanceof EntitySilverfish || list instanceof EntityEndermite)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("blaze"))
                    {
                        if (list instanceof EntityBlaze)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("witch"))
                    {
                        if (list instanceof EntityWitch)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("guardian"))
                    {
                        if (list instanceof EntityGuardian)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("shulker"))
                    {
                        if (list instanceof EntityShulker)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("pig"))
                    {
                        if (list instanceof EntityPig)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("sheep"))
                    {
                        if (list instanceof EntitySheep)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("cow"))
                    {
                        if (list instanceof EntityCow || list instanceof EntityMooshroom)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("chicken"))
                    {
                        if (list instanceof EntityChicken)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("squid"))
                    {
                        if (list instanceof EntitySquid)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("wolf"))
                    {
                        if (list instanceof EntityWolf)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("snowman"))
                    {
                        if (list instanceof EntitySnowman)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("ocelot"))
                    {
                        if (list instanceof EntityOcelot)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("irongolem"))
                    {
                        if (list instanceof EntityIronGolem)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("horse"))
                    {
                        if (list instanceof EntityHorse)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("rabbit"))
                    {
                        if (list instanceof EntityRabbit)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("polarbear"))
                    {
                        if (list instanceof EntityPolarBear)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("villager"))
                    {
                        if (list instanceof EntityVillager)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("item"))
                    {
                        if (list instanceof EntityItem)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("xp"))
                    {
                        if (list instanceof EntityXPOrb)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("armorstand"))
                    {
                        if (list instanceof EntityArmorStand)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("boat"))
                    {
                        if (list instanceof EntityBoat)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("minecart"))
                    {
                        if (list instanceof EntityMinecart)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("endercrystal"))
                    {
                        if (list instanceof EntityEnderCrystal)
                        {
                            list.setGlowing(true);
                        }
                        else
                        {
                            list.setGlowing(false);
                        }
                    }
                    else if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase("reset"))
                    {
                        if (list instanceof EntityLivingBase)
                        {
                            list.setGlowing(((EntityLivingBase)list).isPotionActive(MobEffects.GLOWING));
                        }
                        list.setGlowing(false);
                    }
                    else
                    {
                        for (String name : GameInfoHelper.INSTANCE.getPlayerInfoListClient())
                        {
                            if (ExtendedModSettings.ENTITY_DETECT_TYPE.equalsIgnoreCase(name))
                            {
                                if (list instanceof EntityOtherPlayerMP && ((EntityOtherPlayerMP)list).getName().equalsIgnoreCase(ExtendedModSettings.ENTITY_DETECT_TYPE))
                                {
                                    list.setGlowing(true);
                                }
                                else
                                {
                                    list.setGlowing(false);
                                }
                            }
                        }
                    }
                }
            }
        }
        else
        {
            if (mc.theWorld != null)
            {
                for (Entity list : mc.theWorld.loadedEntityList)
                {
                    if (list instanceof EntityLivingBase)
                    {
                        list.setGlowing(((EntityLivingBase)list).isPotionActive(MobEffects.GLOWING));
                    }
                    list.setGlowing(false);
                }
            }
        }
    }

    public static String getBetterBiomeName(Chunk chunk, World world, BlockPos pos)
    {
        String name = chunk.getBiome(pos, world.getBiomeProvider()).getBiomeName();

        if (name.equals("FrozenOcean"))
        {
            return "Frozen Ocean";
        }
        else if (name.equals("FrozenRiver"))
        {
            return "Frozen River";
        }
        else if (name.equals("MushroomIsland"))
        {
            return "Mushroom Island";
        }
        else if (name.equals("MushroomIslandShore"))
        {
            return "Mushroom Island Shore";
        }
        else if (name.equals("DesertHills"))
        {
            return "Desert Hills";
        }
        else if (name.equals("ForestHills"))
        {
            return "Forest Hills";
        }
        else if (name.equals("TaigaHills"))
        {
            return "Taiga Hills";
        }
        else if (name.equals("JungleHills"))
        {
            return "Jungle Hills";
        }
        else if (name.equals("JungleEdge"))
        {
            return "Jungle Edge";
        }
        else if (name.equals("JungleEdge M"))
        {
            return "Jungle Edge M";
        }
        else
        {
            return name;
        }
    }

    public int countItemInInventory(EntityPlayer player, Item item, int meta)
    {
        int count = 0;

        for (int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            if (player.inventory.getStackInSlot(i) != null && item.equals(player.inventory.getStackInSlot(i).getItem()) && (meta == -1 || player.inventory.getStackInSlot(i).getMetadata() == meta))
            {
                count += player.inventory.getStackInSlot(i).stackSize;
            }
        }
        return count;
    }

    public int countArrowInInventory(EntityPlayer player)
    {
        int arrowCount = 0;

        for (int i = 0; i < player.inventory.getSizeInventory(); ++i)
        {
            ItemStack itemstack = player.inventory.getStackInSlot(i);

            if (itemstack != null && itemstack.getItem() instanceof ItemArrow)
            {
                arrowCount += itemstack.stackSize;
            }
        }
        return arrowCount;
    }
}