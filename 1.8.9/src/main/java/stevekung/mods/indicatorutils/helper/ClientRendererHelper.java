/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.helper;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
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
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(7, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(right, top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(left, top, 0.0D).color(f1, f2, f3, f).endVertex();
        worldRenderer.pos(left, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).color(f5, f6, f7, f4).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRect(double top, double bottom, double left, double right, int color)
    {
        float r = (color >> 16 & 255) / 255.0F;
        float g = (color >> 8 & 255) / 255.0F;
        float b = (color & 255) / 255.0F;
        GlStateManager.disableTexture2D();
        GlStateManager.enableBlend();
        GlStateManager.disableAlpha();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.shadeModel(7425);
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldRenderer = tessellator.getWorldRenderer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_COLOR);
        worldRenderer.pos(right, top, 0.0D).color(r, g, b, 0.35F).endVertex();
        worldRenderer.pos(left, top, 0.0D).color(r, g, b, 0.35F).endVertex();
        worldRenderer.pos(left, bottom, 0.0D).color(r, g, b, 0.35F).endVertex();
        worldRenderer.pos(right, bottom, 0.0D).color(r, g, b, 0.35F).endVertex();
        tessellator.draw();
        GlStateManager.shadeModel(7424);
        GlStateManager.disableBlend();
        GlStateManager.enableAlpha();
        GlStateManager.enableTexture2D();
    }

    public static void drawRectNew(int left, int top, int right, int bottom, int color, float alpha)
    {
        if (left < right)
        {
            int i = left;
            left = right;
            right = i;
        }
        if (top < bottom)
        {
            int j = top;
            top = bottom;
            bottom = j;
        }
        float f = (color >> 16 & 255) / 255.0F;
        float f1 = (color >> 8 & 255) / 255.0F;
        float f2 = (color & 255) / 255.0F;
        Tessellator tessellator = Tessellator.getInstance();
        WorldRenderer worldrenderer = tessellator.getWorldRenderer();
        GlStateManager.enableBlend();
        GlStateManager.disableTexture2D();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.color(f, f1, f2, alpha);
        worldrenderer.begin(7, DefaultVertexFormats.POSITION);
        worldrenderer.pos(left, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, bottom, 0.0D).endVertex();
        worldrenderer.pos(right, top, 0.0D).endVertex();
        worldrenderer.pos(left, top, 0.0D).endVertex();
        tessellator.draw();
        GlStateManager.enableTexture2D();
        GlStateManager.disableBlend();
    }

    public static void drawString(String message, float x, float y, EnumTextColor color, boolean shadow)
    {
        ClientRendererHelper.drawString(message, x, y, color.getColor(), shadow);
    }

    public static void drawString(String message, float x, float y, int color, boolean shadow)
    {
        Minecraft.getMinecraft().fontRendererObj.drawString(message, x, y, color, shadow);
    }

    public static void drawStringAtRecord(String text, float partialTicks)
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
            ClientRendererHelper.drawRect(6, -6, mc.fontRendererObj.getStringWidth(text) / 2 + 2, -mc.fontRendererObj.getStringWidth(text) / 2 - 2, 16777216);
            GlStateManager.popMatrix();

            GlStateManager.pushMatrix();
            GlStateManager.translate(i / 2, j - height, 0.0F);
            GlStateManager.enableBlend();
            GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
            int l = 16777215;
            Minecraft.getMinecraft().fontRendererObj.drawString(text, -mc.fontRendererObj.getStringWidth(text) / 2, -4, l + (l1 << 24 & -16777216), true);
            GlStateManager.disableBlend();
            GlStateManager.popMatrix();
        }
    }

    public static void renderArmorWithEffect(ArmorType type, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(StatusRendererHelper.INSTANCE.getArmorType(type), x, y);
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        RenderHelper.disableStandardItemLighting();

        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.disableLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableColorMaterial();
        GlStateManager.disableLighting();
        GlStateManager.enableCull();
        Minecraft.getMinecraft().getRenderItem().renderItemOverlays(Minecraft.getMinecraft().fontRendererObj, StatusRendererHelper.INSTANCE.getArmorType(type), x, y);
        GlStateManager.blendFunc(770, 771);
        GlStateManager.disableLighting();
    }

    public static void renderItemWithEffect(ItemStack itemStack, int x, int y)
    {
        RenderHelper.enableGUIStandardItemLighting();
        GlStateManager.enableRescaleNormal();
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        Minecraft.getMinecraft().getRenderItem().renderItemAndEffectIntoGUI(itemStack, x, y);
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
            GlStateManager.blendFunc(770, 771);
            GlStateManager.disableLighting();
        }
    }
}