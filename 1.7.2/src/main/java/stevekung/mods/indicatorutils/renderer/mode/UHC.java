/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer.mode;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.helper.ClientRendererHelper;
import stevekung.mods.indicatorutils.helper.StatusRendererHelper;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.RenderInfoBase;

public class UHC
{
    public static void init(Minecraft mc)
    {
        List<String> list = UHC.renderIndicator(mc);
        StatusRendererHelper.renderArmorStatus(mc);
        StatusRendererHelper.renderTimeInformation(mc);
        StatusRendererHelper.renderPotionEffect(mc);

        for (int i = 0; i < list.size(); ++i)
        {
            String string = list.get(i);

            if (!Strings.isNullOrEmpty(string))
            {
                ScaledResolution scaledRes = new ScaledResolution(mc.gameSettings, mc.displayWidth, mc.displayHeight);
                boolean swapToRight = ConfigManager.swapMainRenderInfoToRight;
                int height = mc.fontRenderer.FONT_HEIGHT + 1;
                float y = 3.5F + height * i;
                int xPosition = scaledRes.getScaledWidth() - 2 - mc.fontRenderer.getStringWidth(string);

                if (!mc.gameSettings.showDebugInfo)
                {
                    ClientRendererHelper.drawString(string, swapToRight ? xPosition : 3.5F, y, EnumTextColor.WHITE, true);
                }
            }
        }
    }

    private static List<String> renderIndicator(Minecraft mc)
    {
        List<String> list = Lists.newArrayList();

        if (ConfigManager.enablePing)
        {
            list.addAll(RenderInfoBase.renderPing());
        }
        if (ConfigManager.enableServerIP)
        {
            list.addAll(RenderInfoBase.renderServerIP());
        }
        if (ConfigManager.enableFPS)
        {
            list.add(RenderInfoBase.renderFPS());
        }
        if (ConfigManager.enableXYZ)
        {
            list.addAll(RenderInfoBase.renderXYZ());
        }
        if (ConfigManager.enableDirection)
        {
            list.add(RenderInfoBase.renderDirection());
        }
        if (ConfigManager.enableBiome)
        {
            list.addAll(RenderInfoBase.renderBiome());
        }
        if (ConfigManager.enableCPS)
        {
            list.addAll(RenderInfoBase.renderCPS());
        }
        return list;
    }
}