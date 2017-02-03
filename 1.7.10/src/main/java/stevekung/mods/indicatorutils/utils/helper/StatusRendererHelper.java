/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils.helper;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;
import com.mojang.realmsclient.gui.ChatFormatting;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.ExtendedModSettings;
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
        double distance = entityLivingBase.getDistanceSqToEntity(mc.renderViewEntity);

        if (distance <= d * d)
        {
            if (entityLivingBase.isSneaking())
            {
                float f = 1.6F;
                float f1 = 0.016666668F * f;
                FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
                GL11.glPushMatrix();
                GL11.glTranslatef((float)x + 0.0F, (float)y + entityLivingBase.height + 1.0F, (float)z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef((RenderManager.instance.options.thirdPersonView == 2 ? -1 : 1) * RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(-f1, -f1, f1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glTranslatef(0.0F, 0.25F / f1, 0.0F);
                GL11.glDepthMask(false);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                Tessellator tessellator = Tessellator.instance;
                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tessellator.startDrawingQuads();
                int i = fontrenderer.getStringWidth(text) / 2;
                tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                tessellator.addVertex(-i - 1, -1.0D, 0.0D);
                tessellator.addVertex(-i - 1, 8.0D, 0.0D);
                tessellator.addVertex(i + 1, 8.0D, 0.0D);
                tessellator.addVertex(i + 1, -1.0D, 0.0D);
                tessellator.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                GL11.glDepthMask(true);
                fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, 0, 553648127);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
            else
            {
                FontRenderer fontrenderer = Minecraft.getMinecraft().fontRenderer;
                float f = 1.6F;
                float f1 = 0.016666668F * f;
                GL11.glPushMatrix();
                GL11.glTranslatef((float)x, (float) (y + entityLivingBase.height + 1.0F), (float)z);
                GL11.glNormal3f(0.0F, 1.0F, 0.0F);
                GL11.glRotatef(-RenderManager.instance.playerViewY, 0.0F, 1.0F, 0.0F);
                GL11.glRotatef((RenderManager.instance.options.thirdPersonView == 2 ? -1 : 1) * RenderManager.instance.playerViewX, 1.0F, 0.0F, 0.0F);
                GL11.glScalef(-f1, -f1, f1);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDepthMask(false);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                GL11.glEnable(GL11.GL_BLEND);
                OpenGlHelper.glBlendFunc(770, 771, 1, 0);
                Tessellator tessellator = Tessellator.instance;
                byte b0 = 0;

                if (text.equals("deadmau5"))
                {
                    b0 = -10;
                }

                GL11.glDisable(GL11.GL_TEXTURE_2D);
                tessellator.startDrawingQuads();
                int j = fontrenderer.getStringWidth(text) / 2;
                tessellator.setColorRGBA_F(0.0F, 0.0F, 0.0F, 0.25F);
                tessellator.addVertex(-j - 1, -1 + b0, 0.0D);
                tessellator.addVertex(-j - 1, 8 + b0, 0.0D);
                tessellator.addVertex(j + 1, 8 + b0, 0.0D);
                tessellator.addVertex(j + 1, -1 + b0, 0.0D);
                tessellator.draw();
                GL11.glEnable(GL11.GL_TEXTURE_2D);
                fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, b0, 553648127);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                GL11.glDepthMask(true);
                fontrenderer.drawString(text, -fontrenderer.getStringWidth(text) / 2, b0, -1);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_BLEND);
                GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
                GL11.glPopMatrix();
            }
        }
    }

    public static void renderArmorStatus(Minecraft mc)
    {
        ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
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

        if (ObjectModeHelper.getArmorStatusMode(ArmorStatusPosition.HOTBAR_LEFT))
        {
            if (ConfigManager.enableArmorStatus)
            {
                if (StatusRendererHelper.INSTANCE.getHelmet())
                {
                    width = mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET));
                    StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.HELMET, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT));
                    StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT), EnumTextColor.WHITE, true);
                }
                if (StatusRendererHelper.INSTANCE.getChestplate())
                {
                    width = mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                    StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.CHESTPLATE, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 16);
                    StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 16, EnumTextColor.WHITE, true);
                }
                if (StatusRendererHelper.INSTANCE.getLeggings())
                {
                    width = mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS));
                    StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.LEGGINGS, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 32);
                    StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 32, EnumTextColor.WHITE, true);
                }
                if (StatusRendererHelper.INSTANCE.getBoots())
                {
                    width = mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS));
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
                        itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem);
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

                    if (mainItem.getItem().equals(Items.bow))
                    {
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 + 90 + 8, scaledRes.getScaledHeight() - 20, EnumTextColor.WHITE, true);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                    }
                }
            }
        }
        else if (ObjectModeHelper.getArmorStatusMode(ArmorStatusPosition.HOTBAR_RIGHT))
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
            if (ConfigManager.enableHeldItemInHand)
            {
                if (mainItem != null)
                {
                    boolean isTools = mainItem.isItemStackDamageable();

                    if (mainItem.getMaxStackSize() > 1)
                    {
                        itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem);
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

                    width = mc.fontRenderer.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                    StatusRendererHelper.INSTANCE.renderItemWithEffect(mainItem, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.LEFT_AND_RIGHT) + 48);
                    StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 48, EnumTextColor.WHITE, true);

                    if (mainItem.getItem().equals(Items.bow))
                    {
                        width = font.getStringWidth(arrowCountString);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 - 94 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.LEFT_AND_RIGHT) + 52, EnumTextColor.WHITE, true);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
                    }
                }
            }
        }
        else if (ObjectModeHelper.getArmorStatusMode(ArmorStatusPosition.HOTBAR))
        {
            if (ConfigManager.enableArmorStatus)
            {
                if (StatusRendererHelper.INSTANCE.getHelmet())
                {
                    width = mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET));
                    StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.HELMET, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR));
                    StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR), EnumTextColor.WHITE, true);
                }
                if (StatusRendererHelper.INSTANCE.getChestplate())
                {
                    width = mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                    StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.CHESTPLATE, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) + 16);
                    StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) + 16, EnumTextColor.WHITE, true);
                }
                if (StatusRendererHelper.INSTANCE.getLeggings())
                {
                    StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.LEGGINGS, scaledRes.getScaledWidth() / 2 + 91 + 4, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR));
                    StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), scaledRes.getScaledWidth() / 2 + 90 + 24, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR), EnumTextColor.WHITE, true);
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
                        itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem);
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

                    width = mc.fontRenderer.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                    StatusRendererHelper.INSTANCE.renderItemWithEffect(mainItem, scaledRes.getScaledWidth() / 2 - 91 - 20, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(false, EnumSide.HOTBAR) - 16);
                    StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, scaledRes.getScaledWidth() / 2 - 90 - 24 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) - 16, EnumTextColor.WHITE, true);

                    if (mainItem.getItem().equals(Items.bow))
                    {
                        width = font.getStringWidth(arrowCountString);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        font.drawString(arrowCountString, scaledRes.getScaledWidth() / 2 - 94 - width, scaledRes.getScaledHeight() - OffsetHelper.getHotbarArmorOffset(true, EnumSide.HOTBAR) - 12, EnumTextColor.WHITE, true);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
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
                        width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET));
                        StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.HELMET, armorPosition, helItem);
                        StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.HELMET), flag ? armorTextPosition : width, helText, EnumTextColor.WHITE, true);
                    }
                    if (StatusRendererHelper.INSTANCE.getChestplate())
                    {
                        width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE));
                        StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.CHESTPLATE, armorPosition, chestItem);
                        StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.CHESTPLATE), flag ? armorTextPosition : width, chestText, EnumTextColor.WHITE, true);
                    }
                    if (StatusRendererHelper.INSTANCE.getLeggings())
                    {
                        width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS));
                        StatusRendererHelper.INSTANCE.renderArmorWithEffect(ArmorType.LEGGINGS, armorPosition, legItem);
                        StatusRendererHelper.INSTANCE.drawString(StatusRendererHelper.getArmorStatusType(ArmorType.LEGGINGS), flag ? armorTextPosition : width, legText, EnumTextColor.WHITE, true);
                    }
                    if (StatusRendererHelper.INSTANCE.getBoots())
                    {
                        width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRenderer.getStringWidth(StatusRendererHelper.getArmorStatusType(ArmorType.BOOTS));
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
                        itemCount = StatusRendererHelper.INSTANCE.countItemInInventory(mc.thePlayer, mainItem);
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

                    width = scaledRes.getScaledWidth() - armorTextPosition - mc.fontRenderer.getStringWidth(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString);
                    StatusRendererHelper.INSTANCE.renderItemWithEffect(mainItem, armorPosition, bootItem + 16);
                    StatusRendererHelper.INSTANCE.drawString(isTools ? StatusRendererHelper.getHeldItemStatus(mainItem) : countString, flag ? armorTextPosition : width, bootText + 16, EnumTextColor.WHITE, true);

                    if (mainItem.getItem().equals(Items.bow))
                    {
                        width = scaledRes.getScaledWidth() - armorTextPosition + 18 - font.getStringWidth(arrowCountString);
                        GL11.glDisable(GL11.GL_DEPTH_TEST);
                        font.drawString(arrowCountString, flag ? armorTextPosition - 16 : width, bootText + 16 + 4, EnumTextColor.WHITE, true);
                        GL11.glEnable(GL11.GL_DEPTH_TEST);
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
                ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
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

                    for (Iterator iterator = mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext();)
                    {
                        PotionEffect potioneffect = (PotionEffect)iterator.next();
                        Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                        String s = Potion.getDurationString(potioneffect);
                        String s1 = I18n.format(potion.getName(), new Object[0]);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

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
                        int stringwidth1 = mc.fontRenderer.getStringWidth(s);
                        int stringwidth2 = mc.fontRenderer.getStringWidth(s1);

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
                ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
                int xPotion = scaledRes.getScaledWidth() / 2 + 91 - 20;
                int yPotion = scaledRes.getScaledHeight() - 36;
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

                    for (Iterator iterator = mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext();)
                    {
                        PotionEffect potioneffect = (PotionEffect)iterator.next();
                        Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                        String s = Potion.getDurationString(potioneffect);
                        String s1 = I18n.format(potion.getName(), new Object[0]);
                        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

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
                    ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
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

                        for (Iterator iterator = mc.thePlayer.getActivePotionEffects().iterator(); iterator.hasNext();)
                        {
                            PotionEffect potioneffect = (PotionEffect)iterator.next();
                            Potion potion = Potion.potionTypes[potioneffect.getPotionID()];
                            String s = Potion.getDurationString(potioneffect);
                            String s1 = I18n.format(potion.getName(), new Object[0]);
                            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);

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
                            int stringwidth1 = mc.fontRenderer.getStringWidth(s);
                            int stringwidth2 = mc.fontRenderer.getStringWidth(s1);

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
            ScaledResolution scaledRes = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
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
                    String rps = ConfigManager.enableRPS ? JsonMessageUtils.textToJson(" RPS: ", ConfigManager.customColorRPS).getFormattedText() : "";
                    String cpsValue = JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getCPS()), ConfigManager.customColorCPSValue).getFormattedText();
                    String rpsValue = ConfigManager.enableRPS ? JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getRPS()), ConfigManager.customColorRPSValue).getFormattedText() : "";

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

            for (int i = 0; i < list.size(); ++i)
            {
                String s = list.get(i);

                if (!Strings.isNullOrEmpty(s))
                {
                    float y = 3.5F;
                    int j = mc.fontRenderer.FONT_HEIGHT + 1;
                    int k = mc.fontRenderer.getStringWidth(s);
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
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        if (ConfigManager.enableArmorStatusRenderBug)
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
        new RenderItem().renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), this.getArmorType(type), x, y);
        if (ConfigManager.enableArmorStatusRenderBug)
        {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_LIGHTING);

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        new RenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), this.getArmorType(type), x, y);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public void renderItemWithEffect(ItemStack itemStack, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        if (ConfigManager.enableHeldItemRenderBug)
        {
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
        new RenderItem().renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemStack, x, y);
        if (ConfigManager.enableHeldItemRenderBug)
        {
            GL11.glEnable(GL11.GL_DEPTH_TEST);
        }
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        GL11.glDisable(GL11.GL_BLEND);
        RenderHelper.disableStandardItemLighting();

        if (itemStack.isItemStackDamageable())
        {
            RenderHelper.enableGUIStandardItemLighting();
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL12.GL_RESCALE_NORMAL);
            GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_CULL_FACE);
            new RenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemStack, x, y);
            GL11.glBlendFunc(770, 771);
            GL11.glDisable(GL11.GL_LIGHTING);
        }
    }

    public void drawString(String message, float x, float y, EnumTextColor color, boolean shadow)
    {
        this.drawString(message, x, y, color.getColor(), shadow);
    }

    public void drawString(String message, float x, float y, int color, boolean shadow)
    {
        Minecraft.getMinecraft().fontRenderer.drawString(message, (int)x, (int)y, color, shadow);
    }

    public void drawStringAtRecord(String text, float partialTicks)
    {
        Minecraft mc = Minecraft.getMinecraft();
        ScaledResolution scaledresolution = new ScaledResolution(mc, mc.displayWidth, mc.displayHeight);
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
            GL11.glPushMatrix();
            GL11.glTranslatef(i / 2, j - height + -0.5F, 0.0F);
            StatusRendererHelper.INSTANCE.drawRect(6, -6, mc.fontRenderer.getStringWidth(text) / 2 + 2, -mc.fontRenderer.getStringWidth(text) / 2 - 2, 16777216);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(i / 2, j - height, 0.0F);
            GL11.glEnable(GL11.GL_BLEND);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            int l = 16777215;
            Minecraft.getMinecraft().fontRenderer.drawString(text, -mc.fontRenderer.getStringWidth(text) / 2, -4, l + (l1 << 24 & -16777216), true);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glPopMatrix();
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
        Minecraft.getMinecraft().ingameGUI.func_110326_a(JsonMessageUtils.textToJson("Change display mode to " + "[" + mode + "]").getFormattedText(), false);
    }

    public void drawRectNew(int left, int top, int right, int bottom, int color, float alpha)
    {
        int j1;

        if (left < right)
        {
            j1 = left;
            left = right;
            right = j1;
        }
        if (top < bottom)
        {
            j1 = top;
            top = bottom;
            bottom = j1;
        }
        float f = (color >> 16 & 255) / 255.0F;
        float f1 = (color >> 8 & 255) / 255.0F;
        float f2 = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.instance;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glColor4f(f, f1, f2, alpha);
        tessellator.startDrawingQuads();
        tessellator.addVertex(left, bottom, 0.0D);
        tessellator.addVertex(right, bottom, 0.0D);
        tessellator.addVertex(right, top, 0.0D);
        tessellator.addVertex(left, top, 0.0D);
        tessellator.draw();
        GL11.glEnable(GL11.GL_TEXTURE_2D);
        GL11.glDisable(GL11.GL_BLEND);
    }

    public void drawRect(double top, double bottom, double left, double right, int color)
    {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(r, g, b, 0.35F);
        tessellator.addVertex(right, top, 0.0D);
        tessellator.addVertex(left, top, 0.0D);
        tessellator.setColorRGBA_F(r, g, b, 0.35F);
        tessellator.addVertex(left, bottom, 0.0D);
        tessellator.addVertex(right, bottom, 0.0D);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
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

        if (potion == Potion.field_76444_x)
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
        else if (potion == Potion.field_76434_w)
        {
            color = EnumTextColor.HEALTH_BOOST;
        }
        return color;
    }

    public static String getBetterBiomeName(Chunk chunk, World world, int x, int z)
    {
        String name = chunk.getBiomeGenForWorldCoords(x & 15, z & 15, world.getWorldChunkManager()).biomeName;

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