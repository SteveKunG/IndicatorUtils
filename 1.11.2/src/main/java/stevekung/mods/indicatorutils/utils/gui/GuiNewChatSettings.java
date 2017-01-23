/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import stevekung.mods.indicatorutils.ConfigManager;
import stevekung.mods.indicatorutils.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.GuiOptionSliderIU;
import stevekung.mods.indicatorutils.utils.JsonMessageUtils;
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
        if (GameInfoHelper.INSTANCE.isHypixel())
        {
            this.buttonList.add(new GuiButton(1, this.width - 65, this.height - 60, 60, 20, "Reset Chat"));
            this.buttonList.add(new GuiButton(2, this.width - 45, this.height - 82, 40, 20, "HParty"));
            this.buttonList.add(new GuiButton(3, this.width - 45, this.height - 104, 40, 20, "HGChat"));
        }
        if (GameInfoHelper.INSTANCE.isMineplex())
        {
            this.buttonList.add(new GuiButton(5, this.width - 65, this.height - 60, 60, 20, "Reset Chat"));
            this.buttonList.add(new GuiButton(6, this.width - 45, this.height - 82, 40, 20, "MParty"));
        }
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException
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
                this.sendChatMessage(s);
            }
            this.mc.displayGuiScreen((GuiScreen)null);
        }
        ExtendedModSettings.saveExtendedSettings();
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException
    {
        if (ConfigManager.enableCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            String cps = new JsonMessageUtils().text("CPS: ").setStyle(new JsonMessageUtils().colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
            String rps = ConfigManager.enableRPS ? new JsonMessageUtils().text(" RPS: ").setStyle(new JsonMessageUtils().colorFromConfig(ConfigManager.customColorRPS)).getFormattedText() : "";
            String cpsValue = new JsonMessageUtils().text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setStyle(new JsonMessageUtils().colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();
            String rpsValue = ConfigManager.enableRPS ? new JsonMessageUtils().text(String.valueOf(GameInfoHelper.INSTANCE.getRPS())).setStyle(new JsonMessageUtils().colorFromConfig(ConfigManager.customColorRPSValue)).getFormattedText() : "";

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
    protected void mouseReleased(int mouseX, int mouseY, int state)
    {
        if (ConfigManager.enableCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
        {
            if (state == 0 && this.isDragging)
            {
                this.isDragging = false;
            }
        }
        super.mouseReleased(mouseX, mouseY, state);
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
        case 1:
            ExtendedModSettings.CHAT_MODE = "";
            ExtendedModSettings.saveExtendedSettings();
            Minecraft.getMinecraft().player.sendChatMessage("/chat a");
            Minecraft.getMinecraft().player.sendMessage(new JsonMessageUtils().text("Reset Hypixel Chat"));
            break;
        case 2:
            ExtendedModSettings.CHAT_MODE = "hypixel_party_chat";
            ExtendedModSettings.saveExtendedSettings();
            Minecraft.getMinecraft().player.sendChatMessage("/chat p");
            Minecraft.getMinecraft().player.sendMessage(new JsonMessageUtils().text("Set chat mode to Hypixel Party Chat"));
            break;
        case 3:
            ExtendedModSettings.CHAT_MODE = "hypixel_guild_chat";
            ExtendedModSettings.saveExtendedSettings();
            Minecraft.getMinecraft().player.sendChatMessage("/chat g");
            Minecraft.getMinecraft().player.sendMessage(new JsonMessageUtils().text("Set chat mode to Hypixel Guild Chat"));
            break;
        case 5:
            ExtendedModSettings.CHAT_MODE = "";
            ExtendedModSettings.saveExtendedSettings();
            Minecraft.getMinecraft().player.sendChatMessage("/z");
            Minecraft.getMinecraft().player.sendMessage(new JsonMessageUtils().text("Reset Mineplex Chat"));
            break;
        case 6:
            ExtendedModSettings.CHAT_MODE = "mineplex_party_chat";
            ExtendedModSettings.saveExtendedSettings();
            Minecraft.getMinecraft().player.sendMessage(new JsonMessageUtils().text("Set chat mode to Mineplex Party Chat"));
            break;
        }
    }
}