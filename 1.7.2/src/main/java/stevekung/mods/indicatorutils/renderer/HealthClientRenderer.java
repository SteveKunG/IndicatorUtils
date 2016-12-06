/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RendererLivingEntity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraftforge.client.event.RenderLivingEvent;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class HealthClientRenderer
{
    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Post event)
    {
        Minecraft mc = Minecraft.getMinecraft();
        EntityLivingBase entity = event.entity;
        float health = entity.getHealth() + entity.getAbsorptionAmount();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RendererLivingEntity.NAME_TAG_RANGE_SNEAK : RendererLivingEntity.NAME_TAG_RANGE;
        double distance = entity.getDistanceSqToEntity(mc.renderViewEntity);
        String status = ConfigManager.healthStatusMode;
        boolean flag = true;
        String color = "green";

        if (halfHealth)
        {
            color = "red";
        }
        if (halfHealth1)
        {
            color = "dark_red";
        }

        if (status.equals("DISABLE"))
        {
            flag = false;
        }
        else if (status.equals("POINTED"))
        {
            flag = entity == mc.pointedEntity;
        }

        if (distance < range * range)
        {
            if (!mc.gameSettings.hideGUI && !entity.isInvisible() && flag)
            {
                String heart = JsonMessageUtils.textToJson("\u2764 ", color).getFormattedText();
                StatusRendererHelper.renderHealthStatus(entity, heart + String.format("%.1f", new Object[] { health }), event.x, event.y, event.z, entity.getDistanceSqToEntity(mc.renderViewEntity));
            }
        }
    }
}