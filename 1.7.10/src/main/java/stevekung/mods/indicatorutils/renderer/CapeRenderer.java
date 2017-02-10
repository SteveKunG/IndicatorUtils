/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.util.MathHelper;
import net.minecraftforge.client.event.RenderPlayerEvent;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.CapeUtils;

public class CapeRenderer
{
    @SubscribeEvent
    @SideOnly(Side.CLIENT)
    public void onPostRender(RenderPlayerEvent.Specials.Post event)
    {
        AbstractClientPlayer player = (AbstractClientPlayer) event.entityPlayer;
        float f4;

        if (event.entityPlayer.getCommandSenderName().equals(IndicatorUtils.USERNAME) && !player.isInvisible() && ExtendedModSettings.SHOW_CAPE && (!CapeUtils.CAPE_TEXTURE.isEmpty() || !ExtendedModSettings.CAPE_URL.isEmpty()))
        {
            CapeUtils.bindCapeTexture();
            GL11.glColor3f(1.0F, 1.0F, 1.0F);
            GL11.glPushMatrix();
            GL11.glTranslatef(0.0F, 0.0F, 0.125F);
            double d3 = player.field_71091_bM + (player.field_71094_bP - player.field_71091_bM) * event.partialRenderTick - (player.prevPosX + (player.posX - player.prevPosX) * event.partialRenderTick);
            double d4 = player.field_71096_bN + (player.field_71095_bQ - player.field_71096_bN) * event.partialRenderTick - (player.prevPosY + (player.posY - player.prevPosY) * event.partialRenderTick);
            double d0 = player.field_71097_bO + (player.field_71085_bR - player.field_71097_bO) * event.partialRenderTick - (player.prevPosZ + (player.posZ - player.prevPosZ) * event.partialRenderTick);
            f4 = (player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * event.partialRenderTick) / 57.29578F;
            double d1 = MathHelper.sin(f4);
            double d2 = -MathHelper.cos(f4);
            float f5 = (float) d4 * 10.0F;

            if (f5 < -6.0F)
            {
                f5 = -6.0F;
            }
            if (f5 > 32.0F)
            {
                f5 = 32.0F;
            }

            float f6 = (float) (d3 * d1 + d0 * d2) * 100.0F;
            float f7 = (float) (d3 * d2 - d0 * d1) * 100.0F;

            if (f6 < 0.0F)
            {
                f6 = 0.0F;
            }

            float f8 = player.prevCameraYaw + (player.cameraYaw - player.prevCameraYaw) * event.partialRenderTick;
            f5 += MathHelper.sin((player.prevDistanceWalkedModified + (player.distanceWalkedModified - player.prevDistanceWalkedModified) * event.partialRenderTick) * 6.0F) * 32.0F * f8;

            if (player.isSneaking())
            {
                f5 += 25.0F;
            }

            GL11.glRotatef(6.0F + f6 / 2.0F + f5, 1.0F, 0.0F, 0.0F);
            GL11.glRotatef(f7 / 2.0F, 0.0F, 0.0F, 1.0F);
            GL11.glRotatef(-f7 / 2.0F, 0.0F, 1.0F, 0.0F);
            GL11.glRotatef(180.0F, 0.0F, 1.0F, 0.0F);
            event.renderer.modelBipedMain.renderCloak(0.0625F);
            GL11.glPopMatrix();
        }
    }
}