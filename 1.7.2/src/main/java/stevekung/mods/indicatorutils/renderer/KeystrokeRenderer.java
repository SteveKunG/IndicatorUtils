/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.ScaledResolution;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.helper.ClientRendererHelper;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.EnumTextColor;

public class KeystrokeRenderer
{
    public static void init(Minecraft mc)
    {
        GL11.glPushMatrix();

        if (ConfigManager.keystrokeMode.equalsIgnoreCase("NORMAL"))
        {
            if (ConfigManager.keystrokeSize.equalsIgnoreCase("NORMAL"))
            {
                KeystrokeRenderer.renderNormalMode(mc);
            }
            else
            {
                KeystrokeRenderer.renderNormalModeSmall(mc);
            }
        }
        else
        {
            if (GameInfoHelper.INSTANCE.isHalfScreen(mc))
            {
                if (ConfigManager.keystrokeSize.equalsIgnoreCase("NORMAL"))
                {
                    KeystrokeRenderer.renderNormalAdvancedMode(mc);
                }
                else
                {
                    KeystrokeRenderer.renderSmallAdvancedMode(mc);
                }
            }
        }
        GL11.glPopMatrix();
    }

    private static void renderNormalMode(Minecraft mc)
    {
        int blackColor = 0;
        int whiteColor = 16777215;
        boolean chatOpen = !(mc.currentScreen instanceof GuiChat);
        boolean keyWPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyAPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keySPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyDPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean sprint = mc.thePlayer.isSprinting();
        boolean sneak = mc.thePlayer.isSneaking();
        boolean lmbClicked = chatOpen && Mouse.isButtonDown(0);
        boolean rmbClicked = chatOpen && Mouse.isButtonDown(1);
        boolean blocking = mc.thePlayer.isBlocking();
        ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        boolean left = ConfigManager.keystrokePosition.equalsIgnoreCase("left");
        int leftPos = left ? -328 : scaledRes.getScaledWidth() - 403;

        GL11.glTranslatef(leftPos, scaledRes.getScaledHeight() - 175 + ExtendedModSettings.KEYSTROKE_Y_OFFSET, 0.0F);

        if (ConfigManager.enableKeystrokeLMBRMB)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(302.5F, -27.5F, 0.0F);
            ClientRendererHelper.drawRect(25, 55, 30, 50, lmbClicked ? whiteColor : blackColor);
            ClientRendererHelper.drawString("LMB", 32.0F, 34.0F, lmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(347.5F, -27.5F, 0.0F);
            ClientRendererHelper.drawRect(25, 55, 30, 50, rmbClicked ? whiteColor : blackColor);
            ClientRendererHelper.drawString("RMB", 32.0F, 34.0F, rmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();
        }
        if (ConfigManager.enableKeystrokeSprintSneak)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(312.5F, 22.5F, 0.0F);
            ClientRendererHelper.drawRect(30, 50, 20, 51, sprint ? whiteColor : blackColor);
            ClientRendererHelper.drawString("SPNT", 24.0F, 36.5F, sprint ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(347.5F, 22.5F, 0.0F);
            ClientRendererHelper.drawRect(30, 50, 19, 50, sneak ? whiteColor : blackColor);
            ClientRendererHelper.drawString("SNK", 26.0F, 36.5F, sneak ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();
        }
        if (ConfigManager.enableKeystrokeBlocking)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(330.5F, ConfigManager.enableKeystrokeSprintSneak ? 45.0F : 22.5F, 0.0F);
            ClientRendererHelper.drawRect(30, 50, 15, 55, blocking ? whiteColor : blackColor);
            ClientRendererHelper.drawString("BLOCK", 21.0F, 36.5F, blocking ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(325.0F, -22.5F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyWPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("W", 37.5F, 36.5F, keyWPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(302.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyAPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("A", 37.5F, 36.5F, keyAPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(325.0F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keySPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("S", 37.5F, 36.5F, keySPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(347.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyDPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("D", 37.5F, 36.5F, keyDPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();
    }

    private static void renderNormalModeSmall(Minecraft mc)
    {
        int blackColor = 0;
        int whiteColor = 16777215;
        boolean chatOpen = !(mc.currentScreen instanceof GuiChat);
        boolean keyWPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyAPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keySPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyDPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean sprint = mc.thePlayer.isSprinting();
        boolean sneak = mc.thePlayer.isSneaking();
        boolean lmbClicked = chatOpen && Mouse.isButtonDown(0);
        boolean rmbClicked = chatOpen && Mouse.isButtonDown(1);
        boolean blocking = mc.thePlayer.isBlocking();
        ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        SmallFontRenderer font = new SmallFontRenderer();
        boolean left = ConfigManager.keystrokePosition.equalsIgnoreCase("left");
        int leftPos = left ? -335 : scaledRes.getScaledWidth() - 395;
        GL11.glTranslatef(leftPos, scaledRes.getScaledHeight() - 180 + ExtendedModSettings.KEYSTROKE_Y_OFFSET, 0.0F);

        if (ConfigManager.enableKeystrokeLMBRMB)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(302.5F, -21.5F, 0.0F);
            ClientRendererHelper.drawRect(36, 60, 36, 52, lmbClicked ? whiteColor : blackColor);
            font.drawString("LMB", 38.5F, 41.5F, lmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(337.5F, -21.5F, 0.0F);
            ClientRendererHelper.drawRect(36, 60, 36, 52, rmbClicked ? whiteColor : blackColor);
            font.drawString("RMB", 38.5F, 41.5F, rmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();
        }
        if (ConfigManager.enableKeystrokeSprintSneak)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(317.5F, 23.5F, 0.0F);
            ClientRendererHelper.drawRect(34, 50, 21, 45, sprint ? whiteColor : blackColor);
            font.drawString("SPNT", 25.5F, 37.5F, sprint ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();

            GL11.glPushMatrix();
            GL11.glTranslatef(344.5F, 23.5F, 0.0F);
            ClientRendererHelper.drawRect(34, 50, 21, 45, sneak ? whiteColor : blackColor);
            font.drawString("SNK", 27.5F, 37.5F, sneak ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();
        }
        if (ConfigManager.enableKeystrokeBlocking)
        {
            GL11.glPushMatrix();
            GL11.glTranslatef(330.5F, ConfigManager.enableKeystrokeSprintSneak ? 41.0F : 23.5F, 0.0F);
            ClientRendererHelper.drawRect(34, 50, 19, 48, blocking ? whiteColor : blackColor);
            font.drawString("BLOCK", 24.5F, 37.5F, blocking ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
            GL11.glPopMatrix();
        }

        GL11.glPushMatrix();
        GL11.glTranslatef(320.0F, -17.5F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyWPressed ? whiteColor : blackColor);
        font.drawString("W", 42.5F, 43.5F, keyWPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(302.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyAPressed ? whiteColor : blackColor);
        font.drawString("A", 42.5F, 43.5F, keyAPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(320.0F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keySPressed ? whiteColor : blackColor);
        font.drawString("S", 42.5F, 43.5F, keySPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(337.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyDPressed ? whiteColor : blackColor);
        font.drawString("D", 42.5F, 43.5F, keyDPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();
    }

    private static void renderNormalAdvancedMode(Minecraft mc)
    {
        int blackColor = 0;
        int whiteColor = 16777215;
        boolean chatOpen = !(mc.currentScreen instanceof GuiChat);
        boolean key1Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_1);
        boolean key2Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_2);
        boolean key3Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_3);
        boolean key4Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_4);
        boolean key5Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_5);
        boolean key6Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_6);
        boolean keyQPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_Q);
        boolean keyWPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyEPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_E);
        boolean keyRPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_R);
        boolean keyTPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_T);
        boolean keyYPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_Y);
        boolean keyAPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keySPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyDPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean keyFPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_F);
        boolean keyGPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_G);
        boolean keyHPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_H);
        boolean keyZPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_Z);
        boolean keyXPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_X);
        boolean keyCPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_C);
        boolean keyVPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_V);
        boolean keyBPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_B);
        boolean sprint = mc.thePlayer.isSprinting();
        boolean sneak = mc.thePlayer.isSneaking();
        boolean space = chatOpen && mc.thePlayer.movementInput.jump;
        boolean blocking = mc.thePlayer.isBlocking();
        boolean lmbClicked = chatOpen && Mouse.isButtonDown(0);
        boolean rmbClicked = chatOpen && Mouse.isButtonDown(1);
        boolean mmbClicked = chatOpen && Mouse.isButtonDown(2);
        float xPosition = 145.0F;
        ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        GL11.glTranslatef(scaledRes.getScaledWidth() - 410 + ExtendedModSettings.KEYSTROKE_X_OFFSET, scaledRes.getScaledHeight() - 175 + ExtendedModSettings.KEYSTROKE_Y_OFFSET, 0.0F);

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 155.0F, 4.5F, 0.0F);
        ClientRendererHelper.drawRect(25, 55, 30, 50, lmbClicked ? whiteColor : blackColor);
        ClientRendererHelper.drawString("LMB", 32.0F, 34.0F, lmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 155.0F + 21.0F, 4.5F, 0.0F);
        ClientRendererHelper.drawRect(25, 55, 30, 50, mmbClicked ? whiteColor : blackColor);
        ClientRendererHelper.drawString("MMB", 32.0F, 34.0F, mmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 155.0F + 42.0F, 4.5F, 0.0F);
        ClientRendererHelper.drawRect(25, 55, 30, 50, rmbClicked ? whiteColor : blackColor);
        ClientRendererHelper.drawString("RMB", 32.0F, 34.0F, rmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 158.5F + 5.0F, 30.5F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 20, 51, sprint ? whiteColor : blackColor);
        ClientRendererHelper.drawString("SPNT", 24.0F, 36.5F, sprint ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 193.5F + 5.0F, 30.5F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 19, 50, sneak ? whiteColor : blackColor);
        ClientRendererHelper.drawString("SNK", 26.0F, 36.5F, sneak ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 154.5F + 5.0F, 51.5F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 15, 55, space ? whiteColor : blackColor);
        ClientRendererHelper.drawString("JUMP", 24.0F, 36.5F, space ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 197.5F + 5.0F, 51.5F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 15, 55, blocking ? whiteColor : blackColor);
        ClientRendererHelper.drawString("BLOCK", 21.0F, 36.5F, blocking ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 12.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, key1Pressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("1", 37.5F, 36.5F, key1Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 33.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, key2Pressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("2", 37.5F, 36.5F, key2Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 54.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, key3Pressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("3", 37.5F, 36.5F, key3Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 75.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, key4Pressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("4", 37.5F, 36.5F, key4Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 96.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, key5Pressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("5", 37.5F, 36.5F, key5Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 117.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, key6Pressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("6", 37.5F, 36.5F, key6Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 12.5F, 21.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyQPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("Q", 37.5F, 36.5F, keyQPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 33.5F, 21.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyWPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("W", 37.5F, 36.5F, keyWPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 54.5F, 21.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyEPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("E", 37.5F, 36.5F, keyEPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 75.5F, 21.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyRPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("R", 37.5F, 36.5F, keyRPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 96.5F, 21.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyTPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("T", 37.5F, 36.5F, keyTPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 117.5F, 21.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyYPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("Y", 37.5F, 36.5F, keyYPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 12.5F, 42.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyAPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("A", 37.5F, 36.5F, keyAPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 33.5F, 42.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keySPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("S", 37.5F, 36.5F, keySPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 54.5F, 42.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyDPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("D", 37.5F, 36.5F, keyDPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 75.5F, 42.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyFPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("F", 37.5F, 36.5F, keyFPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 96.5F, 42.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyGPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("G", 37.5F, 36.5F, keyGPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 117.5F, 42.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyHPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("H", 37.5F, 36.5F, keyHPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 15.5F + 7.5F, 63.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyZPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("Z", 37.5F, 36.5F, keyZPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 36.5F + 7.5F, 63.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyXPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("X", 37.5F, 36.5F, keyXPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 57.5F + 7.5F, 63.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyCPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("C", 37.5F, 36.5F, keyCPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 78.5F + 7.5F, 63.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyVPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("V", 37.5F, 36.5F, keyVPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 99.5F + 7.5F, 63.0F, 0.0F);
        ClientRendererHelper.drawRect(30, 50, 30, 50, keyBPressed ? whiteColor : blackColor);
        ClientRendererHelper.drawString("B", 37.5F, 36.5F, keyBPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();
    }

    private static void renderSmallAdvancedMode(Minecraft mc)
    {
        int blackColor = 0;
        int whiteColor = 16777215;
        boolean chatOpen = !(mc.currentScreen instanceof GuiChat);
        boolean key1Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_1);
        boolean key2Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_2);
        boolean key3Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_3);
        boolean key4Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_4);
        boolean key5Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_5);
        boolean key6Pressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_6);
        boolean keyQPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_Q);
        boolean keyWPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_W);
        boolean keyEPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_E);
        boolean keyRPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_R);
        boolean keyTPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_T);
        boolean keyYPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_Y);
        boolean keyAPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_A);
        boolean keySPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_S);
        boolean keyDPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_D);
        boolean keyFPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_F);
        boolean keyGPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_G);
        boolean keyHPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_H);
        boolean keyZPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_Z);
        boolean keyXPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_X);
        boolean keyCPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_C);
        boolean keyVPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_V);
        boolean keyBPressed = chatOpen && Keyboard.isKeyDown(Keyboard.KEY_B);
        boolean sprint = mc.thePlayer.isSprinting();
        boolean sneak = mc.thePlayer.isSneaking();
        boolean space = chatOpen && mc.thePlayer.movementInput.jump;
        boolean blocking = mc.thePlayer.isBlocking();
        boolean lmbClicked = chatOpen && Mouse.isButtonDown(0);
        boolean rmbClicked = chatOpen && Mouse.isButtonDown(1);
        boolean mmbClicked = chatOpen && Mouse.isButtonDown(2);
        float xPosition = 177.0F;
        ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
        SmallFontRenderer font = new SmallFontRenderer();
        GL11.glTranslatef(scaledRes.getScaledWidth() - 395 + ExtendedModSettings.KEYSTROKE_X_OFFSET, scaledRes.getScaledHeight() - 175 + ExtendedModSettings.KEYSTROKE_Y_OFFSET, 0.0F);

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 12.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, key1Pressed ? whiteColor : blackColor);
        font.drawString("1", 42.5F, 43.5F, key1Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 29.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, key2Pressed ? whiteColor : blackColor);
        font.drawString("2", 42.5F, 43.5F, key2Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 46.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, key3Pressed ? whiteColor : blackColor);
        font.drawString("3", 42.5F, 43.5F, key3Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 63.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, key4Pressed ? whiteColor : blackColor);
        font.drawString("4", 42.5F, 43.5F, key4Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 80.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, key5Pressed ? whiteColor : blackColor);
        font.drawString("5", 42.5F, 43.5F, key5Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 97.5F, 0.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, key6Pressed ? whiteColor : blackColor);
        font.drawString("6", 42.5F, 43.5F, key6Pressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 12.5F, 17.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyQPressed ? whiteColor : blackColor);
        font.drawString("Q", 42.5F, 43.5F, keyQPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 29.5F, 17.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyWPressed ? whiteColor : blackColor);
        font.drawString("W", 42.5F, 43.5F, keyWPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 46.5F, 17.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyEPressed ? whiteColor : blackColor);
        font.drawString("E", 42.5F, 43.5F, keyEPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 63.5F, 17.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyRPressed ? whiteColor : blackColor);
        font.drawString("R", 42.5F, 43.5F, keyRPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 80.5F, 17.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyTPressed ? whiteColor : blackColor);
        font.drawString("T", 42.5F, 43.5F, keyTPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 97.5F, 17.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyYPressed ? whiteColor : blackColor);
        font.drawString("Y", 42.5F, 43.5F, keyYPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 12.5F, 34.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyAPressed ? whiteColor : blackColor);
        font.drawString("A", 42.5F, 43.5F, keyAPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 29.5F, 34.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keySPressed ? whiteColor : blackColor);
        font.drawString("S", 42.5F, 43.5F, keySPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 46.5F, 34.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyDPressed ? whiteColor : blackColor);
        font.drawString("D", 42.5F, 43.5F, keyDPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 63.5F, 34.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyFPressed ? whiteColor : blackColor);
        font.drawString("F", 42.5F, 43.5F, keyFPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 80.5F, 34.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyGPressed ? whiteColor : blackColor);
        font.drawString("G", 42.5F, 43.5F, keyGPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 97.5F, 34.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyHPressed ? whiteColor : blackColor);
        font.drawString("H", 42.5F, 43.5F, keyHPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 12.5F + 10.0F, 51.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyZPressed ? whiteColor : blackColor);
        font.drawString("Z", 42.5F, 43.5F, keyZPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 29.5F + 10.0F, 51.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyXPressed ? whiteColor : blackColor);
        font.drawString("X", 42.5F, 43.5F, keyXPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 46.5F + 10.0F, 51.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyCPressed ? whiteColor : blackColor);
        font.drawString("C", 42.5F, 43.5F, keyCPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 63.5F + 10.0F, 51.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyVPressed ? whiteColor : blackColor);
        font.drawString("V", 42.5F, 43.5F, keyVPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 80.5F + 10.0F, 51.0F, 0.0F);
        ClientRendererHelper.drawRect(40, 56, 36, 52, keyBPressed ? whiteColor : blackColor);
        font.drawString("B", 42.5F, 43.5F, keyBPressed ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 125.0F, 2.0F, 0.0F);
        ClientRendererHelper.drawRect(38, 60, 36, 51, lmbClicked ? whiteColor : blackColor);
        font.drawString("LMB", 38.0F, 42.0F, lmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 125.0F + 16.0F, 2.0F, 0.0F);
        ClientRendererHelper.drawRect(38, 60, 36, 51, mmbClicked ? whiteColor : blackColor);
        font.drawString("MMB", 38.0F, 42.0F, mmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 125.0F + 32.0F, 2.0F, 0.0F);
        ClientRendererHelper.drawRect(38, 60, 36, 51, rmbClicked ? whiteColor : blackColor);
        font.drawString("RMB", 38.0F, 42.0F, rmbClicked ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 136.0F, 30.0F, 0.0F);
        ClientRendererHelper.drawRect(33, 49, 23, 48, sprint ? whiteColor : blackColor);
        font.drawString("SPNT", 27.5F, 36.5F, sprint ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 161.0F, 30.0F, 0.0F);
        ClientRendererHelper.drawRect(33, 49, 24, 50, sneak ? whiteColor : blackColor);
        font.drawString("SNK", 31.5F, 36.5F, sneak ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 136.0F, 47.0F, 0.0F);
        ClientRendererHelper.drawRect(33, 49, 23, 48, space ? whiteColor : blackColor);
        font.drawString("JUMP", 28.0F, 36.5F, space ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();

        GL11.glPushMatrix();
        GL11.glTranslatef(xPosition + 161.0F, 47.0F, 0.0F);
        ClientRendererHelper.drawRect(33, 49, 24, 50, blocking ? whiteColor : blackColor);
        font.drawString("BLOCK", 27.5F, 36.5F, blocking ? EnumTextColor.BLACK : EnumTextColor.WHITE, false);
        GL11.glPopMatrix();
    }
}