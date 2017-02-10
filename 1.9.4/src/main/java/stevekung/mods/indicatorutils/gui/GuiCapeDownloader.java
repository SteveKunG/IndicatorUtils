/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.resources.I18n;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.CapeUtils;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class GuiCapeDownloader extends GuiScreen
{
    protected GuiTextField inputField;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton resetBtn;

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.inputField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, 50, 300, 20);
        this.inputField.setMaxStringLength(32767);
        this.inputField.setFocused(true);
        this.inputField.setCanLoseFocus(true);
        this.inputField.setText(ExtendedModSettings.CAPE_URL.isEmpty() ? "" : CapeUtils.decodeCapeURL(ExtendedModSettings.CAPE_URL));
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 100 + 12, 100, 20, I18n.format("gui.done")));
        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
        this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 100 + 12, 100, 20, I18n.format("gui.cancel")));
        this.buttonList.add(this.resetBtn = new GuiButton(2, this.width / 2 - 50, this.height / 4 + 100 + 12, 100, 20, "Reset Cape"));
        this.resetBtn.enabled = !ExtendedModSettings.CAPE_URL.isEmpty();
    }

    @Override
    public void updateScreen()
    {
        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
        this.resetBtn.enabled = !ExtendedModSettings.CAPE_URL.isEmpty();
        this.inputField.updateCursorCounter();
    }

    @Override
    public void onGuiClosed()
    {
        Keyboard.enableRepeatEvents(false);
    }

    @Override
    protected void actionPerformed(GuiButton button) throws IOException
    {
        if (button.enabled)
        {
            if (button.id == 0)
            {
                if (!this.inputField.getText().isEmpty())
                {
                    CapeUtils.textureUploaded = true;
                    CapeUtils.setCapeURL(this.inputField.getText(), false);
                }
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 1)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 2)
            {
                ExtendedModSettings.CAPE_URL = "";
                CapeUtils.CAPE_TEXTURE.remove(IndicatorUtils.USERNAME);
                this.mc.thePlayer.addChatMessage(new JsonUtils().text("Reset cape texture"));
                ExtendedModSettings.saveExtendedSettings();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
    {
        this.inputField.textboxKeyTyped(typedChar, keyCode);

        if (keyCode != 28 && keyCode != 156)
        {
            if (keyCode == 1)
            {
                this.actionPerformed(this.cancelBtn);
            }
        }
        else
        {
            this.actionPerformed(this.doneBtn);
        }
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);
        this.inputField.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks)
    {
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Custom Cape Downloader", this.width / 2, 20, 16777215);
        this.drawString(this.fontRendererObj, "Put your Cape URL (Must be .png or image format)", this.width / 2 - 150, 37, 10526880);
        this.inputField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }
}