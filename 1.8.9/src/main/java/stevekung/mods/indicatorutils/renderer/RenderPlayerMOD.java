/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPlayerMOD extends RenderPlayer
{
    private boolean remove = false;

    public RenderPlayerMOD()
    {
        this(false);
    }

    public RenderPlayerMOD(boolean useSmallArms)
    {
        super(Minecraft.getMinecraft().getRenderManager(), useSmallArms);
        this.mainModel = new ModelPlayerMOD(0.0F, useSmallArms);
        this.addLayer(new LayerCapeMOD(this));
        this.addLayer(new LayerCustomHeadMOD(this.getMainModel().bipedHead));
    }

    @Override
    public void renderRightArm(AbstractClientPlayer clientPlayer)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        super.renderRightArm(clientPlayer);
        GlStateManager.disableBlend();
    }

    @Override
    public void renderLeftArm(AbstractClientPlayer clientPlayer)
    {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(770, 771);
        super.renderLeftArm(clientPlayer);
        GlStateManager.disableBlend();
    }

    @Override
    public void doRender(AbstractClientPlayer entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        if (!this.remove)
        {
            for (int i = 0; i < this.layerRenderers.size(); i++)
            {
                if (i == 5)
                {
                    this.layerRenderers.remove(i);
                }
            }
            this.remove = true;
        }
        super.doRender(entity, x, y, z, entityYaw, partialTicks);
    }
}