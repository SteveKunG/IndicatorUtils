/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.gui;

import java.io.IOException;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class GuiNewChatIU extends GuiChat
{
    private boolean isDragging;
    private int lastPosX;
    private int lastPosY;
    private static int page;
    private GuiButton swLobby;
    private GuiButton swSoloNormal;
    private GuiButton swSoloInsane;
    private GuiButton nextSWMode;
    private GuiButton swTeamNormal;
    private GuiButton swTeamInsane;
    private GuiButton swMegaMode;
    private GuiButton previousSWMode;

    public GuiNewChatIU() {}

    public GuiNewChatIU(String input)
    {
        super(input);
    }

    @Override
    public void initGui()
    {
        super.initGui();
        boolean enableCPS = ConfigManager.enableCPS && ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom");

        if (enableCPS)
        {
            this.buttonList.add(new GuiButton(0, this.width - 63, this.height - 35, 60, 20, "Reset CPS"));
            this.buttonList.add(new GuiOptionSliderIU(1, this.width - 165, this.height - 35, GuiOptionSliderIU.Options.CPS_OPACITY));
        }
        if (GameInfoHelper.INSTANCE.isHypixel())
        {
            String skywars = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().toLowerCase();

            this.buttonList.add(new GuiButton(100, this.width - 63, enableCPS ? this.height - 56 : this.height - 35, 60, 20, "Reset Chat"));
            this.buttonList.add(new GuiButton(101, this.width - 63, enableCPS ? this.height - 77 : this.height - 56, 60, 20, "Party Chat"));
            this.buttonList.add(new GuiButton(102, this.width - 63, enableCPS ? this.height - 98 : this.height - 77, 60, 20, "Guild Chat"));

            if (GameInfoHelper.INSTANCE.removeFormattingCodes(skywars).contains("skywars"))
            {
                this.buttonList.add(this.swLobby = new GuiButton(1000, this.width - 72, 2, 70, 20, "SW Lobby"));
                this.buttonList.add(this.swSoloNormal = new GuiButton(1001, this.width - 72, 23, 70, 20, "Solo Normal"));
                this.buttonList.add(this.swSoloInsane = new GuiButton(1002, this.width - 72, 44, 70, 20, "Solo Insane"));
                this.buttonList.add(this.nextSWMode = new GuiButton(150, this.width - 22, 65, 20, 20, ">"));
                this.buttonList.add(this.swTeamNormal = new GuiButton(1003, this.width - 72, 2, 70, 20, "Team Normal"));
                this.buttonList.add(this.swTeamInsane = new GuiButton(1004, this.width - 72, 23, 70, 20, "Team Insane"));
                this.buttonList.add(this.swMegaMode = new GuiButton(1005, this.width - 72, 44, 70, 20, "Mega Mode"));
                this.buttonList.add(this.previousSWMode = new GuiButton(151, this.width - 22, 65, 20, 20, "<"));
                this.swTeamNormal.visible = false;
                this.swTeamInsane.visible = false;
                this.swMegaMode.visible = false;
                this.previousSWMode.visible = false;
                this.swLobby.visible = false;
                this.swSoloNormal.visible = false;
                this.swSoloInsane.visible = false;
                this.nextSWMode.visible = false;
            }
        }
    }

    @Override
    public void updateScreen()
    {
        super.updateScreen();
        String skywars = this.mc.theWorld.getScoreboard().getObjectiveInDisplaySlot(1).getDisplayName().toLowerCase();

        if (GameInfoHelper.INSTANCE.removeFormattingCodes(skywars).contains("skywars"))
        {
            if (GuiNewChatIU.page == 0)
            {
                this.swLobby.visible = true;
                this.swSoloNormal.visible = true;
                this.swSoloInsane.visible = true;
                this.nextSWMode.visible = true;
                this.swTeamNormal.visible = false;
                this.swTeamInsane.visible = false;
                this.swMegaMode.visible = false;
                this.previousSWMode.visible = false;
            }
            else
            {
                this.swLobby.visible = false;
                this.swSoloNormal.visible = false;
                this.swSoloInsane.visible = false;
                this.nextSWMode.visible = false;
                this.swTeamNormal.visible = true;
                this.swTeamInsane.visible = true;
                this.swMegaMode.visible = true;
                this.previousSWMode.visible = true;
            }
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
                if (ExtendedModSettings.CHAT_MODE.equalsIgnoreCase("mineplex_party_chat"))
                {
                    s = "@" + s;
                }
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
            String cps = new JsonUtils().text("CPS: ").setStyle(new JsonUtils().colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
            String rps = ConfigManager.enableRPS ? new JsonUtils().text(" RPS: ").setStyle(new JsonUtils().colorFromConfig(ConfigManager.customColorRPS)).getFormattedText() : "";
            String cpsValue = new JsonUtils().text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setStyle(new JsonUtils().colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();
            String rpsValue = ConfigManager.enableRPS ? new JsonUtils().text(String.valueOf(GameInfoHelper.INSTANCE.getRPS())).setStyle(new JsonUtils().colorFromConfig(ConfigManager.customColorRPSValue)).getFormattedText() : "";

            if (ConfigManager.useCustomTextCPS)
            {
                cps = JsonUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
            }
            if (ConfigManager.useCustomTextRPS)
            {
                rps = JsonUtils.rawTextToJson(ConfigManager.customTextRPS).getFormattedText();
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
        case 100:
            ExtendedModSettings.CHAT_MODE = "";
            ExtendedModSettings.saveExtendedSettings();
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat a");
            Minecraft.getMinecraft().thePlayer.addChatMessage(new JsonUtils().text("Reset Hypixel Chat"));
            break;
        case 101:
            ExtendedModSettings.CHAT_MODE = "hypixel_party_chat";
            ExtendedModSettings.saveExtendedSettings();
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat p");
            Minecraft.getMinecraft().thePlayer.addChatMessage(new JsonUtils().text("Set chat mode to Hypixel Party Chat"));
            break;
        case 102:
            ExtendedModSettings.CHAT_MODE = "hypixel_guild_chat";
            ExtendedModSettings.saveExtendedSettings();
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/chat g");
            Minecraft.getMinecraft().thePlayer.addChatMessage(new JsonUtils().text("Set chat mode to Hypixel Guild Chat"));
            break;
        case 150:
            GuiNewChatIU.page = 1;
            break;
        case 151:
            GuiNewChatIU.page = 0;
            break;
        case 1000:
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/lobby sw");
            break;
        case 1001:
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play solo_normal");
            break;
        case 1002:
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play solo_insane");
            break;
        case 1003:
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play teams_normal");
            break;
        case 1004:
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play teams_insane");
            break;
        case 1005:
            Minecraft.getMinecraft().thePlayer.sendChatMessage("/play mega_normal");
            break;
        }
    }
}