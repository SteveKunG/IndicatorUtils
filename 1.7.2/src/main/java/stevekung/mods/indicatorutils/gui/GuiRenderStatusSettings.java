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

    @SuppressWarnings("unchecked")
    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 120, I18n.format("gui.done")));
        this.buttonList.add(new GuiRenderStatusSliderInt(201, this.width / 2 - 100, this.height / 4 - 50, GuiRenderStatusSliderInt.Options.ARMOR_Y));
        this.buttonList.add(new GuiRenderStatusSliderInt(202, this.width / 2 - 100, this.height / 4 - 25, GuiRenderStatusSliderInt.Options.POTION_Y));
        this.buttonList.add(new GuiRenderStatusSliderInt(203, this.width / 2 - 100, this.height / 4, GuiRenderStatusSliderInt.Options.KEYSTOKE_Y));
        this.buttonList.add(new GuiRenderStatusSliderInt(204, this.width / 2 - 100, this.height / 4 + 25, GuiRenderStatusSliderInt.Options.MAX_POTION_DISPLAY));
        this.buttonList.add(new GuiRenderStatusSliderInt(205, this.width / 2 - 100, this.height / 4 + 50, GuiRenderStatusSliderInt.Options.POTION_LENGTH_Y_OFFSET));
        this.buttonList.add(new GuiRenderStatusSliderInt(206, this.width / 2 - 100, this.height / 4 + 75, GuiRenderStatusSliderInt.Options.POTION_LENGTH_Y_OFFSET_OVERLAP));
        this.buttonList.add(new GuiRenderStatusSliderFloat(207, this.width / 2 - 100, this.height / 4 + 100, GuiRenderStatusSliderFloat.Options.RENDER_INFO_OPACITY));
        this.buttonList.add(new GuiButton(208, this.width / 2 - 100, this.height / 4 + 125, "Keystroke Color Settings"));
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
            if (button.id == 208)
            {
                new GuiKeystrokeColorSettings().display();
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
        this.drawCenteredString(this.fontRendererObj, "Render Status Settings", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}