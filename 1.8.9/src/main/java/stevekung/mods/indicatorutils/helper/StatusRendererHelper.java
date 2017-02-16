/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.helper;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.lwjgl.opengl.GL11;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.helper.ObjectModeHelper.ArmorStatusPosition;
import stevekung.mods.indicatorutils.helper.OffsetHelper.EnumSide;
import stevekung.mods.indicatorutils.renderer.SmallFontRenderer;
import stevekung.mods.indicatorutils.utils.ArmorType;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.JsonUtils;

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
            GlStateManager.translate((float)x, hasName ? y + entityLivingBase.height + 0.75F - f : !mc.isSingleplayer() ? y + entityLivingBase.height + 1F - f : y + entityLivingBase.height + 0.5F - f, (float)z);
            GL11.glNormal3f(0.0F, 1.0F, 0.0F);
            GlStateManager.rotate(-mc.getRenderManager().playerViewY, 0.0F, 1.0F, 0.0F);
            GlStateManager.rotate((mc.getRenderManager().options.thirdPersonView == 2 ? -1 : 1) * mc.getRenderManager().playerViewX, 1.0F, 0.0F, 0.0F);
            GlStateManager.scale(-0.025F, -0.025F, 0.025F);
            GlStateManager.disableLighting();
            GlStateManager.depthMask(false);

            if (!entityLivingBase.isSneaking())
            {
                GlStateManager.disableDepth();
            }

            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            FontRenderer fontrenderer = Minecraft.getMinecraft().fontRendererObj;
            int j = fontrenderer.getStringWidth(text) / 2;
            GlStateManager.disableTexture2D();
            Tessellator tessellator = Tessellator.getInstance();
            WorldRenderer vertexbuffer = tessellator.getWorldRenderer();
            vertexbuffer.begin(7, DefaultVertexFormats.POSITION_COLOR);
            vertexbuffer.pos(-j - 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexbuffer.pos(-j - 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexbuffer.pos(j + 1, 8, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            vertexbuffer.pos(j + 1, -1, 0.0D).color(0.0F, 0.0F, 0.0F, 0.25F).endVertex();
            tessellator.draw();
            GlStateManager.enableTexture2D();

            if (!entityLivingBase.isSneaking())
            {
                ClientRendererHelper.drawString(text, -fontrenderer.getStringWidth(text) / 2, 0, 553648127, false);
                GlStateManager.enableDepth();
            }

            GlStateManager.depthMask(true);
            ClientRendererHelper.drawString(text, -fontrenderer.getStringWidth(text) / 2, 0, entityLivingBase.isSneaking() ? 553648127 : -1, false);
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
            ItemStack mainItem = mc.thePlayer.getCurrentEquippedItem();
            int itemCount = 0;
            int arrowCount = StatusRendererHelper.INSTANCE.countArrowInInventory(mc.thePlayer);
            SmallFontRenderer font = new SmallFontRenderer();
            JsonUtils json = new JsonUtils();

            if (ObjectModeHelper.getArmorStatusMode(ArmorStatusPosition.HOTBAR_LEFT))
            {
                if (!mc.thePlayer.isSpectator())
                {
                    if (ConfigManager.enableArmorStatus)
                    {
                        if (StatusRendererHelper.INSTANCE.getHelmet())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET));
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.HELMET, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT));
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT), EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getChestplate())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.CHESTPLATE, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 16);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 16, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getLeggings())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS));
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.LEGGINGS, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 32);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 32, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getBoots())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS));
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.BOOTS, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);
                        }
                    }
                    if (ConfigManager.enableHeldItemInHand)
                    {
                        if (mainItem != null)
                        {
                            boolean isTools = mainItem.isItemStackDamageable();

                            if (mainItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem);
                            }

                            String countString = json.text(String.valueOf(itemCount)).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItem)).getFormattedText();
                            String arrowCountString = json.text(String.valueOf(arrowCount)).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItemArrowCount)).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            ClientRendererHelper.renderItemWithEffect(mainItem, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                            ClientRendererHelper.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);

                            if (mainItem.getItem().equals(Items.bow))
                            {
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 + 90 + 8, scaledRes.getScaledHeight() - 20, EnumTextColor.WHITE, true);
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
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.HELMET, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT));
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT), EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getChestplate())
                        {
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.CHESTPLATE, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 16);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 16, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getLeggings())
                        {
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.LEGGINGS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 32);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 32, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getBoots())
                        {
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.BOOTS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);
                        }
                    }
                    if (ConfigManager.enableHeldItemInHand && !mc.thePlayer.isSpectator())
                    {
                        if (mainItem != null)
                        {
                            boolean isTools = mainItem.isItemStackDamageable();

                            if (mainItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem);
                            }

                            String countString = json.text(String.valueOf(itemCount)).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItem)).getFormattedText();
                            String arrowCountString = json.text(String.valueOf(arrowCount)).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItemArrowCount)).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            width = mc.fontRendererObj.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                            ClientRendererHelper.renderItemWithEffect(mainItem, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                            ClientRendererHelper.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);

                            if (mainItem.getItem().equals(Items.bow))
                            {
                                width = font.getStringWidth(arrowCountString);
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 - 94 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 52, EnumTextColor.WHITE, true);
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
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.HELMET, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR));
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR), EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getChestplate())
                        {
                            width = mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.CHESTPLATE, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) + 16);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) + 16, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getLeggings())
                        {
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.LEGGINGS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR));
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR), EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getBoots())
                        {
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.BOOTS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) + 16);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) + 16, EnumTextColor.WHITE, true);
                        }
                    }
                    if (ConfigManager.enableHeldItemInHand)
                    {
                        if (mainItem != null)
                        {
                            boolean isTools = mainItem.isItemStackDamageable();

                            if (mainItem.getMaxStackSize() > 1)
                            {
                                itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem);
                            }

                            String countString = json.text(String.valueOf(itemCount)).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItem)).getFormattedText();
                            String arrowCountString = json.text(String.valueOf(arrowCount)).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItemArrowCount)).getFormattedText();

                            if (itemCount == 0 || itemCount == 1)
                            {
                                countString = "";
                            }
                            if (arrowCount == 0)
                            {
                                arrowCountString = "";
                            }

                            width = mc.fontRendererObj.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                            ClientRendererHelper.renderItemWithEffect(mainItem, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) - 16);
                            ClientRendererHelper.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) - 16, EnumTextColor.WHITE, true);

                            if (mainItem.getItem().equals(Items.bow))
                            {
                                width = font.getStringWidth(arrowCountString);
                                GlStateManager.disableDepth();
                                font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 - 94 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) - 12, EnumTextColor.WHITE, true);
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
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.HELMET, armorPosition, helItem);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), flag ? armorTextPosition : width, helText, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getChestplate())
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.CHESTPLATE, armorPosition, chestItem);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), flag ? armorTextPosition : width, chestText, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getLeggings())
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS));
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.LEGGINGS, armorPosition, legItem);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), flag ? armorTextPosition : width, legText, EnumTextColor.WHITE, true);
                        }
                        if (StatusRendererHelper.INSTANCE.getBoots())
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS));
                            ClientRendererHelper.renderArmorWithEffect(ArmorType.BOOTS, armorPosition, bootItem);
                            ClientRendererHelper.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS), flag ? armorTextPosition : width, bootText, EnumTextColor.WHITE, true);
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
                            itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem);
                        }

                        String countString = json.text(String.valueOf(itemCount)).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItem)).getFormattedText();
                        String arrowCountString = json.text(String.valueOf(arrowCount)).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItemArrowCount)).getFormattedText();

                        if (itemCount == 0 || itemCount == 1)
                        {
                            countString = "";
                        }
                        if (arrowCount == 0)
                        {
                            arrowCountString = "";
                        }

                        width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRendererObj.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                        ClientRendererHelper.renderItemWithEffect(mainItem, armorPosition, bootItem + 16);
                        ClientRendererHelper.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, flag ? armorTextPosition : width, bootText + 16, EnumTextColor.WHITE, true);

                        if (mainItem.getItem().equals(Items.bow))
                        {
                            width = scaledRes.getScaledWidth() - armorTextPosition + 18 - font.getStringWidth(arrowCountString);
                            GlStateManager.disableDepth();
                            font.drawString(arrowCountString, flag ? armorTextPosition - 16 : width, bootText + 16 + 4, EnumTextColor.WHITE, true);
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

                    for (PotionEffect potioneffect : mc.thePlayer.getActivePotionEffects())
                    {
                        Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                        String s = Potion.getDurationString(potioneffect);
                        String s1 = I18n.format(potion.getName(), new Object[0]);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                        if (showIcon)
                        {
                            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
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
                            ClientRendererHelper.drawString(s1, showIcon ? xPotion + 8 - stringwidth2 : xPotion + 28 - stringwidth2, yPotion + 6, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                        }
                        ClientRendererHelper.drawString(s, showIcon ? xPotion + 8 - stringwidth1 : xPotion + 28 - stringwidth1, iconAndTime ? yPotion + 11 : yPotion + 16, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
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

                    for (PotionEffect potioneffect : mc.thePlayer.getActivePotionEffects())
                    {
                        Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                        String s = Potion.getDurationString(potioneffect);
                        String s1 = I18n.format(potion.getName(), new Object[0]);
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                        if (showIcon)
                        {
                            mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
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
                            ClientRendererHelper.drawString(s1, showIcon ? xPotion + 46 : xPotion + 28, yPotion + 6, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                        }
                        ClientRendererHelper.drawString(s, showIcon ? xPotion + 46 : xPotion + 28, iconAndTime ? yPotion + 11 : yPotion + 16, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
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

                        for (PotionEffect potioneffect : mc.thePlayer.getActivePotionEffects())
                        {
                            Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                            String s = Potion.getDurationString(potioneffect);
                            String s1 = I18n.format(potion.getName(), new Object[0]);
                            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

                            if (showIcon)
                            {
                                mc.getTextureManager().bindTexture(new ResourceLocation("textures/gui/container/inventory.png"));
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
                                ClientRendererHelper.drawString(s1, right ? showIcon ? xPotion + 8 - stringwidth2 : xPotion + 28 - stringwidth2 : showIcon ? xPotion + 50 : xPotion + 28, yPotion + 6, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
                            }
                            ClientRendererHelper.drawString(s, right ? showIcon ? xPotion + 8 - stringwidth1 : xPotion + 28 - stringwidth1 : showIcon ? xPotion + 50 : xPotion + 28, iconAndTime ? yPotion + 11 : yPotion + 16, ConfigManager.highlightPotionColor ? StatusRendererHelper.highlightPotionTextColor(potion) : EnumTextColor.WHITE, true);
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
            JsonUtils json = new JsonUtils();
            String day = json.text(new SimpleDateFormat("d", StatusRendererHelper.getDateFormat()).format(new Date())).setChatStyle(json.colorFromConfig(ConfigManager.customColorCurrentTimeDay)).getFormattedText();
            String month = json.text(new SimpleDateFormat(shortDateBool ? "M" : "MMM", StatusRendererHelper.getDateFormat()).format(new Date())).setChatStyle(json.colorFromConfig(ConfigManager.customColorCurrentTimeMonth)).getFormattedText();
            String year = json.text(new SimpleDateFormat("yyyy", StatusRendererHelper.getDateFormat()).format(new Date())).setChatStyle(json.colorFromConfig(ConfigManager.customColorCurrentTimeYear)).getFormattedText();
            String timeValue = json.text(new SimpleDateFormat("HH:mm:ss", StatusRendererHelper.getDateFormat()).format(new Date())).setChatStyle(json.colorFromConfig(ConfigManager.customColorCurrentTimeValue)).getFormattedText();
            String AMPM = json.text(new SimpleDateFormat("a", StatusRendererHelper.getDateFormat()).format(new Date())).setChatStyle(json.colorFromConfig(ConfigManager.customColorTimeAMPM)).getFormattedText();
            // ��ѧ = \u0e2b\u0e25\u0e31\u0e07, ��͹ = \u0e01\u0e48\u0e2d\u0e19, ���§ = \u0e40\u0e17\u0e35\u0e48\u0e22\u0e07
            AMPM = AMPM.replace("\u0e2b\u0e25\u0e31\u0e07\u0e40\u0e17\u0e35\u0e48\u0e22\u0e07", "PM").replace("\u0e01\u0e48\u0e2d\u0e19\u0e40\u0e17\u0e35\u0e48\u0e22\u0e07", "AM");
            String date = day + "/" + month + "/" + year + " " + timeValue + " " + AMPM;
            String timeText = json.text("Time: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorCurrentTime)).getFormattedText();
            String timeZoneText = json.text("Time Zone: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorTimeZone)).getFormattedText();
            String timeZoneValue = json.text(TimeZone.getDefault().getDisplayName()).setChatStyle(json.colorFromConfig(ConfigManager.customColorTimeZoneValue)).getFormattedText();

            if (ConfigManager.useCustomTextTime)
            {
                timeText = JsonUtils.rawTextToJson(ConfigManager.customTextTime).getFormattedText();
            }
            if (ConfigManager.useCustomTextTimeZone)
            {
                timeZoneText = JsonUtils.rawTextToJson(ConfigManager.customTextTimeZone).getFormattedText();
            }

            if (ConfigManager.enableCurrentTime)
            {
                list.add(timeText + date);
            }
            if (ConfigManager.enableGameTime)
            {
                String prefixText = json.text(" | ").setChatStyle(json.colorFromConfig(ConfigManager.customColorWeatherPrefix)).getFormattedText();
                String rainingText = json.text("Raining").setChatStyle(json.colorFromConfig(ConfigManager.customColorRaining)).getFormattedText();
                String thunderText = json.text("Thunder").setChatStyle(json.colorFromConfig(ConfigManager.customColorThunder)).getFormattedText();

                if (ConfigManager.useCustomWeather)
                {
                    prefixText = JsonUtils.rawTextToJson(ConfigManager.customTextWeather).getFormattedText();
                    rainingText = JsonUtils.rawTextToJson(ConfigManager.customTextRaining).getFormattedText();
                    thunderText = JsonUtils.rawTextToJson(ConfigManager.customTextThunder).getFormattedText();
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
                String timeZone = json.text(gmt.format(new Date())).setChatStyle(json.colorFromConfig(ConfigManager.customColorWorldTime)).getFormattedText();
                list.add(timeZone);
            }
            if (ConfigManager.enableCPS)
            {
                if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("right"))
                {
                    String cps = json.text("CPS: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
                    String rps = ConfigManager.enableRPS ? json.text(" RPS: ").setChatStyle(json.colorFromConfig(ConfigManager.customColorRPS)).getFormattedText() : "";
                    String cpsValue = json.text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setChatStyle(json.colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();
                    String rpsValue = ConfigManager.enableRPS ? json.text(String.valueOf(GameInfoHelper.INSTANCE.getRPS())).setChatStyle(json.colorFromConfig(ConfigManager.customColorRPSValue)).getFormattedText() : "";

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

            for (int i = 0; i < list.size(); ++i)
            {
                String s = list.get(i);

                if (!Strings.isNullOrEmpty(s))
                {
                    float y = 3.5F;
                    int j = mc.fontRendererObj.FONT_HEIGHT + 1;
                    int k = mc.fontRendererObj.getStringWidth(s);
                    int l = scaledRes.getScaledWidth() - 2 - k;
                    float i1 = y + j * i;
                    ClientRendererHelper.drawString(s, swapToRight ? 3.5F : l, i1, EnumTextColor.WHITE, true);
                }
            }
        }
    }

    public boolean getHelmet()
    {
        return this.getArmorType(ArmorType.HELMET) != null && this.getArmorType(ArmorType.HELMET).getItem() instanceof ItemArmor;
    }

    public boolean getChestplate()
    {
        return this.getArmorType(ArmorType.CHESTPLATE) != null && this.getArmorType(ArmorType.CHESTPLATE).getItem() instanceof ItemArmor;
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
        JsonUtils json = new JsonUtils();
        return json.text(String.valueOf(this.getArmorType(type).getMaxDamage() - this.getArmorType(type).getItemDamage())).setChatStyle(json.colorFromConfig(ConfigManager.customColorArmorDamageDurability)).getFormattedText() + "/" + json.text(String.valueOf(this.getArmorType(type).getMaxDamage())).setChatStyle(json.colorFromConfig(ConfigManager.customColorArmorMaxDurability)).getFormattedText();
    }

    public String getArmorDurability2(ArmorType type)
    {
        JsonUtils json = new JsonUtils();
        return json.text(String.valueOf(this.getArmorType(type).getMaxDamage() - this.getArmorType(type).getItemDamage())).setChatStyle(json.colorFromConfig(ConfigManager.customColorArmorDamageDurability)).getFormattedText();
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
        if (ConfigManager.armorStatusMode.equalsIgnoreCase("NORMAL_1"))
        {
            return StatusRendererHelper.INSTANCE.getArmorDurability1(armorType);
        }
        else if (ConfigManager.armorStatusMode.equalsIgnoreCase("NORMAL_2"))
        {
            return StatusRendererHelper.INSTANCE.getArmorDurability2(armorType);
        }
        else if (ConfigManager.armorStatusMode.equalsIgnoreCase("PERCENT"))
        {
            return new JsonUtils().text(String.valueOf(StatusRendererHelper.INSTANCE.calculateArmorDurabilityPercent(armorType) + "%")).setChatStyle(new JsonUtils().colorFromConfig(ConfigManager.customColorArmorPercent)).getFormattedText();
        }
        else
        {
            return "";
        }
    }

    public static String getHeldItemStatus(ItemStack itemStack)
    {
        JsonUtils json = new JsonUtils();

        if (ConfigManager.heldItemStatusMode.equalsIgnoreCase("PERCENT"))
        {
            return json.text(String.valueOf(StatusRendererHelper.INSTANCE.calculateItemDurabilityPercent(itemStack) + "%")).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItem)).getFormattedText();
        }
        else if (ConfigManager.heldItemStatusMode.equalsIgnoreCase("NORMAL_2"))
        {
            return json.text(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage())).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItem)).getFormattedText() + "/" + json.text(String.valueOf(itemStack.getMaxDamage())).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItem)).getFormattedText();
        }
        else if (ConfigManager.heldItemStatusMode.equalsIgnoreCase("NORMAL"))
        {
            return json.text(String.valueOf(itemStack.getMaxDamage() - itemStack.getItemDamage())).setChatStyle(json.colorFromConfig(ConfigManager.customColorHeldItem)).getFormattedText();
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
        GameInfoHelper.INSTANCE.setActionBarMessage(new JsonUtils().text("Change display mode to " + "[" + mode + "]"), false);
    }

    private static Locale getDateFormat()
    {
        if (ConfigManager.dateFormat.equalsIgnoreCase("US"))
        {
            return Locale.ENGLISH;
        }
        else if (ConfigManager.dateFormat.equalsIgnoreCase("JP"))
        {
            return Locale.JAPAN;
        }
        else if (ConfigManager.dateFormat.equalsIgnoreCase("CN"))
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

        if (potion == Potion.absorption)
        {
            color = EnumTextColor.ABSORPTION;
        }
        else if (potion == Potion.regeneration)
        {
            color = EnumTextColor.REGENERATION;
        }
        else if (potion == Potion.damageBoost)
        {
            color = EnumTextColor.STRENGTH;
        }
        else if (potion == Potion.moveSpeed)
        {
            color = EnumTextColor.SPEED;
        }
        else if (potion == Potion.fireResistance)
        {
            color = EnumTextColor.FIRE_RESISTANCE;
        }
        else if (potion == Potion.resistance)
        {
            color = EnumTextColor.RESISTANCE;
        }
        else if (potion == Potion.jump)
        {
            color = EnumTextColor.JUMP_BOOST;
        }
        else if (potion == Potion.nightVision)
        {
            color = EnumTextColor.NIGHT_VISION;
        }
        else if (potion == Potion.waterBreathing)
        {
            color = EnumTextColor.WATER_BREATHING;
        }
        else if (potion == Potion.moveSlowdown)
        {
            color = EnumTextColor.SLOWNESS;
        }
        else if (potion == Potion.digSpeed)
        {
            color = EnumTextColor.HASTE;
        }
        else if (potion == Potion.digSlowdown)
        {
            color = EnumTextColor.MINING_FATIGUE;
        }
        else if (potion == Potion.confusion)
        {
            color = EnumTextColor.NAUSEA;
        }
        else if (potion == Potion.invisibility)
        {
            color = EnumTextColor.INVISIBILITY;
        }
        else if (potion == Potion.blindness)
        {
            color = EnumTextColor.BLINDNESS;
        }
        else if (potion == Potion.hunger)
        {
            color = EnumTextColor.HUNGER;
        }
        else if (potion == Potion.weakness)
        {
            color = EnumTextColor.WEAKNESS;
        }
        else if (potion == Potion.poison)
        {
            color = EnumTextColor.POISON;
        }
        else if (potion == Potion.wither)
        {
            color = EnumTextColor.WITHER;
        }
        else if (potion == Potion.healthBoost)
        {
            color = EnumTextColor.HEALTH_BOOST;
        }
        return color;
    }

    public static String getBetterBiomeName(Chunk chunk, World world, BlockPos pos)
    {
        String name = chunk.getBiome(pos, world.getWorldChunkManager()).biomeName;

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

    public int countItemInInventory(EntityPlayer player, ItemStack other)
    {
        int count = 0;

        for (int i = 0; i < player.inventory.getSizeInventory(); i++)
        {
            ItemStack playerItems = player.inventory.getStackInSlot(i);

            if (playerItems != null && playerItems.getItem() == other.getItem() && playerItems.getItemDamage() == other.getItemDamage() && playerItems.getTagCompound() == other.getTagCompound())
            {
                count += playerItems.stackSize;
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

            if (itemstack != null && itemstack.getItem() == Items.arrow)
            {
                arrowCount += itemstack.stackSize;
            }
        }
        return arrowCount;
    }
}