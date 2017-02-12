package stevekung.mods.indicatorutils.gui;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;

@SideOnly(Side.CLIENT)
public class GuiRenderStatusSettings extends GuiScreen
{
    public void display()
    {
        FMLCommonHandler.instance().bus().register(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        FMLCommonHandler.instance().bus().unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 120, I18n.format("gui.done")));
        this.buttonList.add(new GuiRenderStatusSlider(201, this.width / 2 - 100, this.height / 4 - 50, GuiRenderStatusSlider.Options.ARMOR_Y));
        this.buttonList.add(new GuiRenderStatusSlider(202, this.width / 2 - 100, this.height / 4 - 25, GuiRenderStatusSlider.Options.POTION_Y));
        this.buttonList.add(new GuiRenderStatusSlider(203, this.width / 2 - 100, this.height / 4, GuiRenderStatusSlider.Options.KEYSTOKE_X));
        this.buttonList.add(new GuiRenderStatusSlider(204, this.width / 2 - 100, this.height / 4 + 25, GuiRenderStatusSlider.Options.KEYSTOKE_Y));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            ExtendedModSettings.saveExtendedSettings();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        if (button.enabled)
        {
            if (button.id == 200)
            {
                ExtendedModSettings.saveExtendedSettings();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawCenteredString(this.fontRendererObj, "Render Status Settings", this.width / 2, 10, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}