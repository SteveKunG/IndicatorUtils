/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.potion.Potion;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;

@SideOnly(Side.CLIENT)
public class MovementInputFromOptionsIU extends MovementInputFromOptions
{
    private GameSettings gameSettings;
    private Minecraft mc;

    public MovementInputFromOptionsIU(GameSettings gameSettings)
    {
        super(gameSettings);
        this.gameSettings = gameSettings;
        this.mc = Minecraft.getMinecraft();
    }

    @Override
    public void updatePlayerMoveState()
    {
        if (ConfigManager.enableModifiedMovementHandler)
        {
            this.moveStrafe = 0.0F;
            this.moveForward = 0.0F;
            int afkMoveTick = IndicatorUtilsEventHandler.AFK_MOVE_TICK;

            if (afkMoveTick > 0 && afkMoveTick < 2)
            {
                ++this.moveForward;
            }
            else if (afkMoveTick > 2 && afkMoveTick < 4)
            {
                ++this.moveStrafe;
            }
            else if (afkMoveTick > 4 && afkMoveTick < 6)
            {
                --this.moveForward;
            }
            else if (afkMoveTick > 6 && afkMoveTick < 8)
            {
                --this.moveStrafe;
            }

            if (this.gameSettings.keyBindForward.isKeyDown())
            {
                ++this.moveForward;
            }
            if (this.gameSettings.keyBindBack.isKeyDown() && !(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_S)))
            {
                --this.moveForward;
            }
            if (this.gameSettings.keyBindLeft.isKeyDown() && !(Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_A)))
            {
                ++this.moveStrafe;
            }
            if (this.gameSettings.keyBindRight.isKeyDown() && !(Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_D)))
            {
                --this.moveStrafe;
            }
            if (ExtendedModSettings.TOGGLE_SPRINT && !this.mc.thePlayer.isPotionActive(Potion.blindness) && !ExtendedModSettings.TOGGLE_SNEAK)
            {
                this.mc.thePlayer.setSprinting(true);
            }

            boolean swim = IndicatorUtils.isSteveKunG() && ExtendedModSettings.AUTO_SWIM && (this.mc.thePlayer.isInWater() || this.mc.thePlayer.isInLava()) && !this.mc.thePlayer.isSpectator();
            this.jump = this.gameSettings.keyBindJump.isKeyDown() || swim;
            this.sneak = this.gameSettings.keyBindSneak.isKeyDown() || ExtendedModSettings.TOGGLE_SNEAK;

            if (this.sneak)
            {
                this.moveStrafe = (float)(this.moveStrafe * 0.3D);
                this.moveForward = (float)(this.moveForward * 0.3D);
            }
        }
        else
        {
            super.updatePlayerMoveState();
        }
    }
}