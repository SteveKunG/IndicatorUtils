/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import java.awt.Color;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.helper.ClientRendererHelper;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.EnumTextColor;

public class KeystrokeRenderer
{
    public static void init(Minecraft mc)
    {
        ScaledResolution res = new ScaledResolution(mc);
        SmallFontRenderer smallFontRenderer = new SmallFontRenderer();
        KeystrokeRenderer.renderStyleNormal(mc, smallFontRenderer, res.getScaledWidth());
    }

    private static void renderStyleNormal(Minecraft mc, SmallFontRenderer smallFontRenderer, int width)
    {
        width = ConfigManager.keystrokePosition.equalsIgnoreCase("left") ? 96 : width;
        int widthSquare = 80;
        int heightSquare = 48 + ExtendedModSettings.KEYSTROKE_Y_OFFSET;
        boolean nullScreen = mc.currentScreen == null;
        boolean wDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean aDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean sDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean dDown = nullScreen && Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean lmbDown = nullScreen && Mouse.isButtonDown(0);
        boolean rmbDown = nullScreen && Mouse.isButtonDown(1);
        boolean sprintDown = mc.thePlayer.isSprinting();
        boolean sneakDown = mc.thePlayer.isSneaking();
        boolean blockDown = mc.thePlayer.isActiveItemStackBlocking();
        boolean useRainbow = false;
        int rainbow = Math.abs(Color.HSBtoRGB(System.currentTimeMillis() % 2500L / 2500.0F, 0.8F, 0.8F));
        float red = (rainbow >> 16 & 255) / 255.0F;
        float green = (rainbow >> 8 & 255) / 255.0F;
        float blue = (rainbow & 255) / 255.0F;
        float r = 0;
        float g = 0;
        float b = 0;

        GlStateManager.enableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);

        ClientRendererHelper.bindKeystrokeTexture("key_square");
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 20, heightSquare, wDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare - 4, heightSquare + 24, aDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 20, heightSquare + 24, sDown ? 24 : 0, 0, 24, 24, 48, 24);
        Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 44, heightSquare + 24, dDown ? 24 : 0, 0, 24, 24, 48, 24);
        r = ExtendedModSettings.KEYSTROKE_WASD_RED;
        g = ExtendedModSettings.KEYSTROKE_WASD_GREEN;
        b = ExtendedModSettings.KEYSTROKE_WASD_BLUE;
        useRainbow = ExtendedModSettings.KEYSTROKE_WASD_RAINBOW;
        ClientRendererHelper.drawString("W", width - widthSquare + 29.5F, heightSquare + 9, wDown ? EnumTextColor.BLACK : useRainbow ? EnumTextColor.RAINBOW.setRainbow() : EnumTextColor.CUSTOM.setColor(ClientRendererHelper.to32BitColor(255, (int)r, (int)g, (int)b)), false);
        ClientRendererHelper.drawString("A", width - widthSquare + 5.5F, heightSquare + 32, aDown ? EnumTextColor.BLACK : useRainbow ? EnumTextColor.RAINBOW.setRainbow() : EnumTextColor.CUSTOM.setColor(ClientRendererHelper.to32BitColor(255, (int)r, (int)g, (int)b)), false);
        ClientRendererHelper.drawString("S", width - widthSquare + 29.5F, heightSquare + 32, sDown ? EnumTextColor.BLACK : useRainbow ? EnumTextColor.RAINBOW.setRainbow() : EnumTextColor.CUSTOM.setColor(ClientRendererHelper.to32BitColor(255, (int)r, (int)g, (int)b)), false);
        ClientRendererHelper.drawString("D", width - widthSquare + 53.5F, heightSquare + 32, dDown ? EnumTextColor.BLACK : useRainbow ? EnumTextColor.RAINBOW.setRainbow() : EnumTextColor.CUSTOM.setColor(ClientRendererHelper.to32BitColor(255, (int)r, (int)g, (int)b)), false);

        if (ConfigManager.enableKeystrokeLMBRMB)
        {
            ClientRendererHelper.bindKeystrokeTexture("mouse_square");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare - 4, heightSquare - 12, lmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 44, heightSquare - 12, rmbDown ? 24 : 0, 0, 24, 36, 48, 36);
            r = ExtendedModSettings.KEYSTROKE_LMBRMB_RED;
            g = ExtendedModSettings.KEYSTROKE_LMBRMB_GREEN;
            b = ExtendedModSettings.KEYSTROKE_LMBRMB_BLUE;
            useRainbow = ExtendedModSettings.KEYSTROKE_LMBRMB_RAINBOW;
            ClientRendererHelper.drawString("LMB", width - widthSquare - 0.5F, heightSquare - 4, lmbDown ? EnumTextColor.BLACK : useRainbow ? EnumTextColor.RAINBOW.setRainbow() : EnumTextColor.CUSTOM.setColor(ClientRendererHelper.to32BitColor(255, (int)r, (int)g, (int)b)), false);
            ClientRendererHelper.drawString("RMB", width - widthSquare + 47.5F, heightSquare - 4, rmbDown ? EnumTextColor.BLACK : useRainbow ? EnumTextColor.RAINBOW.setRainbow() : EnumTextColor.CUSTOM.setColor(ClientRendererHelper.to32BitColor(255, (int)r, (int)g, (int)b)), false);

            r = ExtendedModSettings.KEYSTROKE_CPS_RED;
            g = ExtendedModSettings.KEYSTROKE_CPS_GREEN;
            b = ExtendedModSettings.KEYSTROKE_CPS_BLUE;
            useRainbow = ExtendedModSettings.KEYSTROKE_CPS_RAINBOW;

            if (ConfigManager.enableCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("keystroke"))
            {
                String cps = "CPS:" + GameInfoHelper.INSTANCE.getCPS();
                int smallFontWidth = smallFontRenderer.getStringWidth(cps);
                smallFontRenderer.drawString(cps, width - widthSquare + 8.5F - smallFontWidth / 2, heightSquare + 12, lmbDown ? EnumTextColor.BLACK : useRainbow ? EnumTextColor.RAINBOW.setRainbow() : EnumTextColor.CUSTOM.setColor(ClientRendererHelper.to32BitColor(255, (int)r, (int)g, (int)b)), lmbDown ? false : true);
            }
            if (ConfigManager.enableRCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("keystroke"))
            {
                String rcps = "RCPS:" + GameInfoHelper.INSTANCE.getRCPS();
                int smallFontWidth = smallFontRenderer.getStringWidth(rcps);
                smallFontRenderer.drawString(rcps, width - widthSquare + 56.5F - smallFontWidth / 2, heightSquare + 12, rmbDown ? EnumTextColor.BLACK : useRainbow ? EnumTextColor.RAINBOW.setRainbow() : EnumTextColor.CUSTOM.setColor(ClientRendererHelper.to32BitColor(255, (int)r, (int)g, (int)b)), rmbDown ? false : true);
            }
        }
        if (ConfigManager.enableKeystrokeSprintSneak)
        {
            ClientRendererHelper.bindKeystrokeTexture("button_square_2");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 2, heightSquare + 48, sprintDown ? 20 : 0, 0, 20, 20, 40, 20);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 22, heightSquare + 48, sneakDown ? 20 : 0, 0, 20, 20, 40, 20);

            ClientRendererHelper.bindKeystrokeTexture("sprint");
            r = ExtendedModSettings.KEYSTROKE_SPRINT_RED / 255.0F;
            g = ExtendedModSettings.KEYSTROKE_SPRINT_GREEN / 255.0F;
            b = ExtendedModSettings.KEYSTROKE_SPRINT_BLUE / 255.0F;
            useRainbow = ExtendedModSettings.KEYSTROKE_SPRINT_RAINBOW;
            GlStateManager.color(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 2, heightSquare + 48, sprintDown ? 0 : 20, 0, 20, 20, 40, 20);

            ClientRendererHelper.bindKeystrokeTexture("sneak");
            r = ExtendedModSettings.KEYSTROKE_SNEAK_RED / 255.0F;
            g = ExtendedModSettings.KEYSTROKE_SNEAK_GREEN / 255.0F;
            b = ExtendedModSettings.KEYSTROKE_SNEAK_BLUE / 255.0F;
            useRainbow = ExtendedModSettings.KEYSTROKE_SNEAK_RAINBOW;
            GlStateManager.color(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 22, heightSquare + 48, sneakDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        if (ConfigManager.enableKeystrokeBlocking)
        {
            ClientRendererHelper.bindKeystrokeTexture("button_square_2");
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 42, heightSquare + 48, blockDown ? 20 : 0, 0, 20, 20, 40, 20);

            ClientRendererHelper.bindKeystrokeTexture("block");
            r = ExtendedModSettings.KEYSTROKE_BLOCK_RED / 255.0F;
            g = ExtendedModSettings.KEYSTROKE_BLOCK_GREEN / 255.0F;
            b = ExtendedModSettings.KEYSTROKE_BLOCK_BLUE / 255.0F;
            useRainbow = ExtendedModSettings.KEYSTROKE_BLOCK_RAINBOW;
            GlStateManager.color(useRainbow ? red : r, useRainbow ? green : g, useRainbow ? blue : b);
            Gui.drawModalRectWithCustomSizedTexture(width - widthSquare + 42, heightSquare + 48, blockDown ? 0 : 20, 0, 20, 20, 40, 20);
        }
        GlStateManager.disableBlend();
    }
}