package stevekung.mods.indicatorutils.gui;

import java.net.URI;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.EnumChatFormatting;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.utils.VersionChecker;

@SideOnly(Side.CLIENT)
public class GuiOldVersionWarning extends GuiScreen
{
    private int urlX;
    private int urlY;
    private int urlWidth;
    private int urlHeight;

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        if (IndicatorUtils.ALLOWED)
        {
            this.buttonList.clear();
            this.buttonList.add(new GuiButton(0, this.width / 2 - 100, Math.min(this.height / 2 + 32, this.height - 30), I18n.format("Ignore this message")));
        }
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        int offset = this.height / 2 - 50;
        this.drawCenteredString(this.fontRendererObj, "You are using an old version of Indicator Utils!", this.width / 2, offset, 43690);
        offset += 25;
        this.drawCenteredString(this.fontRendererObj, "We recommended latest because we fix some bugs that occur", this.width / 2, offset, 16733525);
        offset += 20;
        this.drawCenteredString(this.fontRendererObj, "Close Minecraft and install the latest " + EnumChatFormatting.GREEN + "[Indicator Utils " + VersionChecker.INSTANCE.getLatestVersionReplaceMC() + "]", this.width / 2, offset, 16733525);
        offset += 20;
        String s = EnumChatFormatting.UNDERLINE + "Click Here to Download [Recommended]";
        this.urlX = this.width / 2 - this.fontRendererObj.getStringWidth(s) / 2 - 2;
        this.urlY = offset - 2;
        this.urlWidth = this.fontRendererObj.getStringWidth(s) + 4;
        this.urlHeight = 14;
        Gui.drawRect(this.urlX, this.urlY, this.urlX + this.urlWidth, this.urlY + this.urlHeight, 1684300900);
        this.drawCenteredString(this.fontRendererObj, s, this.width / 2, offset, 43520);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (IndicatorUtils.ALLOWED)
        {
            if (button.id == 0)
            {
                this.mc.displayGuiScreen(new GuiMainMenu());
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) {}

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        if (mouseX > this.urlX && mouseX < this.urlX + this.urlWidth && mouseY > this.urlY && mouseY < this.urlY + this.urlHeight)
        {
            try
            {
                Class oclass = Class.forName("java.awt.Desktop");
                Object object = oclass.getMethod("getDesktop").invoke((Object) null);
                oclass.getMethod("browse", new Class[] { URI.class }).invoke(object, new Object[] { new URI("http://adf.ly/1cDWrG") });
                Minecraft.getMinecraft().shutdown();
            }
            catch (Throwable throwable)
            {
                throwable.printStackTrace();
            }
        }
    }
}