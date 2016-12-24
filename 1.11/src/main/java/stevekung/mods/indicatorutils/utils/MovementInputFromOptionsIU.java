/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.init.MobEffects;
import net.minecraft.util.MovementInputFromOptions;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.keybinding.KeyBindingHandler;

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
            int afkMoveTick = IndicatorUtilsEventHandler.afkMoveTick;

            if (afkMoveTick > 0 && afkMoveTick < 2)
            {
                ++this.moveForward;
                this.forwardKeyDown = true;
            }
            else if (afkMoveTick > 2 && afkMoveTick < 4)
            {
                ++this.moveStrafe;
                this.leftKeyDown = true;
            }
            else if (afkMoveTick > 4 && afkMoveTick < 6)
            {
                --this.moveForward;
                this.backKeyDown = true;
            }
            else if (afkMoveTick > 6 && afkMoveTick < 8)
            {
                --this.moveStrafe;
                this.rightKeyDown = true;
            }

            if (this.gameSettings.keyBindForward.isKeyDown())
            {
                ++this.moveForward;
                this.forwardKeyDown = true;
            }
            else
            {
                this.forwardKeyDown = false;
            }

            if (this.gameSettings.keyBindBack.isKeyDown() && !(KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown() || KeyBindingHandler.KEY_TOGGLE_SNEAK.isKeyDown()))
            {
                --this.moveForward;
                this.backKeyDown = true;
            }
            else
            {
                this.backKeyDown = false;
            }

            if (this.gameSettings.keyBindLeft.isKeyDown())
            {
                ++this.moveStrafe;
                this.leftKeyDown = true;
            }
            else
            {
                this.leftKeyDown = false;
            }

            if (this.gameSettings.keyBindRight.isKeyDown())
            {
                --this.moveStrafe;
                this.rightKeyDown = true;
            }
            else
            {
                this.rightKeyDown = false;
            }

            boolean swim = ExtendedModSettings.AUTO_SWIM && (this.mc.player.isInWater() || this.mc.player.isInLava()) && !this.mc.player.isSpectator();
            this.jump = this.gameSettings.keyBindJump.isKeyDown() || swim;
            this.sneak = this.gameSettings.keyBindSneak.isKeyDown() || ExtendedModSettings.TOGGLE_SNEAK;

            if (ExtendedModSettings.TOGGLE_SPRINT && !this.mc.player.isPotionActive(MobEffects.BLINDNESS) && !ExtendedModSettings.TOGGLE_SNEAK)
            {
                this.mc.player.setSprinting(true);
            }

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