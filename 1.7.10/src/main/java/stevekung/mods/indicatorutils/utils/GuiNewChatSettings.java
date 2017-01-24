/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.helper.GameInfoHelper;

public class GuiNewChatSettings extends GuiChat
{
    private boolean isDragging;
    private int lastPosX;
    private int lastPosY;

    public GuiNewChatSettings() {}

    public GuiNewChatSettings(String input)
    {
        super(input);
    }

    @Override
    public void initGui()
    {
        super.initGui();

        if (ConfigManager.enableCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            this.buttonList.add(new GuiButton(0, this.width - 63, this.height - 38, 58, 20, "Reset CPS"));
            this.buttonList.add(new GuiOptionSliderIU(4, this.width - 165, this.height - 38, GuiOptionSliderIU.Options.CPS_OPACITY));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode)
    {
        if (keyCode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        else if (keyCode != 28 && keyCode != 156)
        {
            super.keyTyped(typedChar, keyCode);
        }
        else
        {
            String s = this.inputField.getText().trim();

            if (!s.isEmpty())
            {
                this.func_146403_a(s);
            }
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        ExtendedModSettings.saveExtendedSettings();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        if (ConfigManager.enableCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            String cps = JsonMessageUtils.textToJson("CPS: ", ConfigManager.customColorCPS).getFormattedText();
            String rps = JsonMessageUtils.textToJson(" RPS: ", ConfigManager.customColorRPS).getFormattedText();
            String cpsValue = JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getCPS()), ConfigManager.customColorCPSValue).getFormattedText();
            String rpsValue = JsonMessageUtils.textToJson(String.valueOf(GameInfoHelper.INSTANCE.getRPS()), ConfigManager.customColorRPSValue).getFormattedText();

            if (ConfigManager.useCustomTextCPS)
            {
                cps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
            }
            if (ConfigManager.useCustomTextRPS)
            {
                rps = JsonMessageUtils.rawTextToJson(ConfigManager.customTextRPS).getFormattedText();
            }

            int minX = ExtendedModSettings.CPS_X_OFFSET;
            int minY = ExtendedModSettings.CPS_Y_OFFSET;
            int maxX = ExtendedModSettings.CPS_X_OFFSET + this.fontRendererObj.getStringWidth(cps + cpsValue + rps + rpsValue) + 4;
            int maxY = ExtendedModSettings.CPS_Y_OFFSET + 12;

            if (mouseX >= minX && mouseX <= maxX && mouseY >= minY && mouseY <= maxY)
            {
                this.isDragging = true;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void mouseMovedOrUp(int mouseX, int mouseY, int state)
    {
        if (ConfigManager.enableCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            if (state == 0 && this.isDragging)
            {
                this.isDragging = false;
            }
        }
        super.mouseMovedOrUp(mouseX, mouseY, state);
    }

    @Override
    protected void mouseClickMove(int mouseX, int mouseY, int clickedMouseButton, long timeSinceLastClick)
    {
        if (ConfigManager.enableCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            if (this.isDragging)
            {
                ExtendedModSettings.CPS_X_OFFSET += mouseX - this.lastPosX;
                ExtendedModSettings.CPS_Y_OFFSET += mouseY - this.lastPosY;
                this.lastPosX = mouseX;
                this.lastPosY = mouseY;
            }
        }
    }

    @Override
    protected void actionPerformed(GuiButton button)
    {
        switch (button.id)
        {
        case 0:
            ExtendedModSettings.CPS_X_OFFSET = 3;
            ExtendedModSettings.CPS_Y_OFFSET = 2;
            ExtendedModSettings.saveExtendedSettings();
            break;
        }
    }
}