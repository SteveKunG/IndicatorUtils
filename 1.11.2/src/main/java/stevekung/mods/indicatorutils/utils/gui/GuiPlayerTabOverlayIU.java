/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import stevekung.mods.indicatorutils.utils.EnumTextColor;
import stevekung.mods.indicatorutils.utils.helper.StatusRendererHelper;

public class GuiPlayerTabOverlayIU extends GuiPlayerTabOverlay
{
    private Minecraft mc;

    public GuiPlayerTabOverlayIU(Minecraft mc, GuiIngame guiIngame)
    {
        super(mc, guiIngame);
        this.mc = mc;
    }

    @Override
    protected void drawPing(int x1, int x2, int y, NetworkPlayerInfo networkPlayerInfo)
    {
        EnumTextColor color = EnumTextColor.GREEN;

        if (networkPlayerInfo.getResponseTime() >= 200 && networkPlayerInfo.getResponseTime() <= 300)
        {
            color = EnumTextColor.YELLOW;
        }
        else if (networkPlayerInfo.getResponseTime() >= 301 && networkPlayerInfo.getResponseTime() <= 499)
        {
            color = EnumTextColor.RED;
        }
        else if (networkPlayerInfo.getResponseTime() >= 500)
        {
            color = EnumTextColor.DARK_RED;
        }
        StatusRendererHelper.INSTANCE.drawString(String.valueOf(networkPlayerInfo.getResponseTime()), x1 + x2 - this.mc.fontRendererObj.getStringWidth(String.valueOf(networkPlayerInfo.getResponseTime())), y + 0.5F, color, true);
    }
}