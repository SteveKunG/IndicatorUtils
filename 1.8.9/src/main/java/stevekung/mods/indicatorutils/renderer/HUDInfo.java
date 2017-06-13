/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import java.util.List;

import com.google.common.base.Strings;
import com.google.common.collect.Lists;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.helper.ClientRendererHelper;
import stevekung.mods.indicatorutils.helper.StatusRendererHelper;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.RenderInfoBase;

public class HUDInfo
{
    public static void init(Minecraft mc)
    {
        List<String> list = HUDInfo.renderIndicator();
        StatusRendererHelper.renderArmorStatus(mc);
        StatusRendererHelper.renderTimeInformation(mc);
        StatusRendererHelper.renderPotionEffect(mc);

        for (int i = 0; i < list.size(); ++i)
        {
            String string = list.get(i);

            if (!Strings.isNullOrEmpty(string))
            {
                ScaledResolution scaledRes = new ScaledResolution(mc);
                boolean swapToRight = ConfigManager.swapMainRenderInfoToRight;
                int height = mc.fontRendererObj.FONT_HEIGHT + 1;
                int xPosition = scaledRes.getScaledWidth() - 2 - mc.fontRendererObj.getStringWidth(string);

                if (!mc.gameSettings.showDebugInfo)
                {
                    int stringWidth = mc.fontRendererObj.getStringWidth(string) + 2;
                    float yPosition = 3.5F + height * i;
                    int yOverlay = (int) yPosition;

                    if (swapToRight)
                    {
                        ClientRendererHelper.drawRectNew(xPosition - 2, yOverlay - 1, xPosition + stringWidth - 1, yOverlay + height - 1, 16777216, ExtendedModSettings.RENDER_INFO_OPACITY);
                    }
                    else
                    {
                        ClientRendererHelper.drawRectNew(1, yOverlay - 1, 2 + stringWidth + 1, yOverlay + height - 1, 16777216, ExtendedModSettings.RENDER_INFO_OPACITY);
                    }
                    ClientRendererHelper.drawString(string, swapToRight ? xPosition : 3.5F, yPosition, EnumTextColor.WHITE, true);
                }
            }
        }
    }

    public static List<String> renderIndicator()
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
        if (ConfigManager.enableLookingAtBlock)
        {
            list.addAll(RenderInfoBase.renderLookingAtBlock());
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