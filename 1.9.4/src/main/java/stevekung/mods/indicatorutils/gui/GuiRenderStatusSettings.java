/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;

@SideOnly(Side.CLIENT)
public class GuiRenderStatusSettings extends GuiScreen
{
    public void display()
    {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        MinecraftForge.EVENT_BUS.unregister(this);
        Minecraft.getMinecraft().displayGuiScreen(this);
    }

    @Override
    public void initGui()
    {
        this.buttonList.add(new GuiButton(200, this.width / 2 - 100, this.height - 120, I18n.format("gui.done")));
        this.buttonList.add(new GuiRenderStatusSliderInt(201, this.width / 2 - 100, this.height / 4 - 50, GuiRenderStatusSliderInt.Options.ARMOR_Y));
        this.buttonList.add(new GuiRenderStatusSliderInt(202, this.width / 2 - 100, this.height / 4 - 25, GuiRenderStatusSliderInt.Options.POTION_Y));
        this.buttonList.add(new GuiRenderStatusSliderInt(203, this.width / 2 - 100, this.height / 4, GuiRenderStatusSliderInt.Options.KEYSTOKE_X));
        this.buttonList.add(new GuiRenderStatusSliderInt(204, this.width / 2 - 100, this.height / 4 + 25, GuiRenderStatusSliderInt.Options.KEYSTOKE_Y));
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        if (keyCode == 1)
        {
            ExtendedModSettings.saveExtendedSettings();
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
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
        this.drawCenteredString(this.fontRendererObj, "Render Status Settings", this.width / 2, 20, 16777215);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}