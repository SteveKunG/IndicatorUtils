/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import java.util.Iterator;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelPlayer;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.renderer.entity.layers.LayerCustomHead;
import net.minecraft.client.renderer.entity.layers.LayerRenderer;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class RenderPlayerMOD extends RenderPlayer
{
    public RenderPlayerMOD()
    {
        this(false);
    }

    public RenderPlayerMOD(boolean useSmallArms)
    {
        super(Minecraft.getMinecraft().getRenderManager(), useSmallArms);
        this.mainModel = new ModelPlayer(0.0F, useSmallArms);
        this.addLayer(new LayerCapeMOD(this));

        boolean removedVanilla = false;
        Iterator<LayerRenderer<AbstractClientPlayer>> iterator = this.layerRenderers.iterator();

        while (iterator.hasNext())
        {
            LayerRenderer<AbstractClientPlayer> renderer = iterator.next();

            if (renderer.getClass().equals(LayerCustomHead.class))
            {
                iterator.remove();
                removedVanilla = true;
            }
        }

        if (removedVanilla)
        {
            this.addLayer(new LayerCustomHead(this.getMainModel().bipedHead));
        }
    }
}