package stevekung.mods.indicatorutils.utils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiIngame;
import net.minecraft.client.gui.GuiPlayerTabOverlay;
import net.minecraft.client.network.NetworkPlayerInfo;
import stevekung.mods.indicatorutils.ConfigManager;
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
    protected void drawPing(int x1, int x2, int y, NetworkPlayerInfo networkPlayerInfoIn)
    {
        if (ConfigManager.playerPingMode.equalsIgnoreCase("number"))
        {
            EnumTextColor color = EnumTextColor.GREEN;

            if (networkPlayerInfoIn.getResponseTime() >= 200 && networkPlayerInfoIn.getResponseTime() <= 300)
            {
                color = EnumTextColor.YELLOW;
            }
            else if (networkPlayerInfoIn.getResponseTime() >= 301 && networkPlayerInfoIn.getResponseTime() <= 499)
            {
                color = EnumTextColor.RED;
            }
            else if (networkPlayerInfoIn.getResponseTime() >= 500)
            {
                color = EnumTextColor.DARK_RED;
            }
            StatusRendererHelper.INSTANCE.drawString(String.valueOf(networkPlayerInfoIn.getResponseTime()), x1 + x2 - this.mc.fontRendererObj.getStringWidth(String.valueOf(networkPlayerInfoIn.getResponseTime())), y + 0.5F, color, true);
        }
        else
        {
            super.drawPing(x1, x2, y, networkPlayerInfoIn);
        }
    }
}