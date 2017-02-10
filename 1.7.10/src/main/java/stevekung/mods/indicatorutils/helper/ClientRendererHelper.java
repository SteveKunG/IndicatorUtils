/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.helper;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import stevekung.mods.indicatorutils.utils.ArmorType;
import stevekung.mods.indicatorutils.utils.EnumTextColor;

public class ClientRendererHelper
{
    public static int to32BitColor(int a, int r, int g, int b)
    {
        a = a << 24;
        r = r << 16;
        g = g << 8;
        return a | r | g | b;
    }

    public static void drawGradientRect(int left, int top, int right, int bottom, int startColor, int endColor)
    {
        float f = (startColor >> 24 & 255) / 255.0F;
        float f1 = (startColor >> 16 & 255) / 255.0F;
        float f2 = (startColor >> 8 & 255) / 255.0F;
        float f3 = (startColor & 255) / 255.0F;
        float f4 = (endColor >> 24 & 255) / 255.0F;
        float f5 = (endColor >> 16 & 255) / 255.0F;
        float f6 = (endColor >> 8 & 255) / 255.0F;
        float f7 = (endColor & 255) / 255.0F;
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glDisable(GL11.GL_ALPHA_TEST);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        GL11.glShadeModel(GL11.GL_SMOOTH);
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();
        tessellator.setColorRGBA_F(f1, f2, f3, f);
        tessellator.addVertex(right, top, 0.0D);
        tessellator.addVertex(left, top, 0.0D);
        tessellator.setColorRGBA_F(f5, f6, f7, f4);
        tessellator.addVertex(left, bottom, 0.0D);
        tessellator.addVertex(right, bottom, 0.0D);
        tessellator.draw();
        GL11.glShadeModel(GL11.GL_FLAT);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glEnable(GL11.GL_ALPHA_TEST);
        GL11.glEnable(GL11.GL_TEXTURE_2D);
    }

    public static void drawRect(double top, double bottom, double left, double right, int color)
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

    public static void drawRectNew(int left, int top, int right, int bottom, int color, float alpha)
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

    public static void drawString(String message, float x, float y, EnumTextColor color, boolean shadow)
    {
        ClientRendererHelper.drawString(message, x, y, color.getColor(), shadow);
    }

    public static void drawString(String message, float x, float y, int color, boolean shadow)
    {
        Minecraft.getMinecraft().fontRenderer.drawString(message, (int)x, (int)y, color, shadow);
    }

    public static void drawStringAtRecord(String text, float partialTicks)
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
            ClientRendererHelper.drawRect(6, -6, mc.fontRenderer.getStringWidth(text) / 2 + 2, -mc.fontRenderer.getStringWidth(text) / 2 - 2, 16777216);
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

    public static void renderArmorWithEffect(ArmorType type, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        new RenderItem().renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), StatusRendererHelper.INSTANCE.getArmorType(type), x, y);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_LIGHTING);

        RenderHelper.enableGUIStandardItemLighting();
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glEnable(GL11.GL_CULL_FACE);
        new RenderItem().renderItemOverlayIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), StatusRendererHelper.INSTANCE.getArmorType(type), x, y);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(GL11.GL_LIGHTING);
    }

    public static void renderItemWithEffect(ItemStack itemStack, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GL11.glEnable(GL11.GL_BLEND);
        OpenGlHelper.glBlendFunc(770, 771, 1, 0);
        new RenderItem().renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemStack, x, y);
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
}