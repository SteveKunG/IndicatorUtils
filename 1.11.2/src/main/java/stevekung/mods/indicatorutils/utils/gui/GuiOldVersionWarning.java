/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils.gui;

import java.net.URI;

import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.utils.VersionChecker;

@SideOnly(Side.CLIENT)
public class GuiOldVersionWarning extends GuiScreen
{
    private int urlX;
    private int urlY;
    private int urlWidth;
    private int urlHeight;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        int offset = this.height / 2 - 50;
        this.drawCenteredString(this.fontRendererObj, "You are using an old version of Indicator Utils!", this.width / 2, offset, 43690);
        offset += 25;
        this.drawCenteredString(this.fontRendererObj, "We recommended latest because we fix some bugs that occur", this.width / 2, offset, 16733525);
        offset += 20;
        this.drawCenteredString(this.fontRendererObj, "Close Minecraft and install the latest " + TextFormatting.GREEN + "[Indicator Utils " + VersionChecker.INSTANCE.getLatestVersionReplaceMC() + "]", this.width / 2, offset, 16733525);
        offset += 20;
        String s = TextFormatting.UNDERLINE + "Click Here to Download [Recommended]";
        this.urlX = this.width / 2 - this.fontRendererObj.getStringWidth(s) / 2 - 2;
        this.urlY = offset - 2;
        this.urlWidth = this.fontRendererObj.getStringWidth(s) + 4;
        this.urlHeight = 14;
        Gui.drawRect(this.urlX, this.urlY, this.urlX + this.urlWidth, this.urlY + this.urlHeight, 1684300900);
        this.drawCenteredString(this.fontRendererObj, s, this.width / 2, offset, 43520);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {}

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (mouseX > this.urlX && mouseX < this.urlX + this.urlWidth && mouseY > this.urlY && mouseY < this.urlY + this.urlHeight)
        {
            try
            {
                Class<?> oclass = Class.forName("java.awt.Desktop");
                Object object = oclass.getMethod("getDesktop").invoke((Object) null);
                oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { new URI("http://adf.ly/1cDWrG") });
                FMLCommonHandler.instance().exitJava(0, false);
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }
}