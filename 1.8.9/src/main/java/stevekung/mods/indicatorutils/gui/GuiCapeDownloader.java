package stevekung.mods.indicatorutils.gui;

import java.io.IOException;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EnumPlayerModelParts;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.Base64Utils;
import stevekung.mods.indicatorutils.utils.CapeUtils;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class GuiCapeDownloader extends GuiScreen
{
    protected GuiTextField inputField;
    private GuiButton doneBtn;
    private GuiButton cancelBtn;
    private GuiButton resetBtn;
    private GuiButton capeBtn;
    private int capeOption;
    private int prevCapeOption;

    @Override
    public void initGui()
    {
        Keyboard.enableRepeatEvents(true);
        this.inputField = new GuiTextField(2, this.fontRendererObj, this.width / 2 - 150, this.height / 4 + 85, 300, 20);
        this.inputField.setMaxStringLength(32767);
        this.inputField.setFocused(true);
        this.inputField.setCanLoseFocus(true);
        this.inputField.setText(ExtendedModSettings.CAPE_URL.isEmpty() ? "" : Base64Utils.decode(ExtendedModSettings.CAPE_URL));
        this.buttonList.add(this.doneBtn = new GuiButton(0, this.width / 2 - 50 - 100 - 4, this.height / 4 + 100 + 12, 100, 20, I18n.format("gui.done")));
        this.doneBtn.enabled = !this.inputField.getText().isEmpty();
        this.buttonList.add(this.cancelBtn = new GuiButton(1, this.width / 2 + 50 + 4, this.height / 4 + 100 + 12, 100, 20, I18n.format("gui.cancel")));
        this.buttonList.add(this.resetBtn = new GuiButton(2, this.width / 2 - 50, this.height / 4 + 100 + 12, 100, 20, "Reset Cape"));
        this.resetBtn.enabled = !ExtendedModSettings.CAPE_URL.isEmpty();

        if (!this.mc.gameSettings.getModelParts().contains(EnumPlayerModelParts.CAPE) && !ExtendedModSettings.SHOW_CAPE)
        {
            this.capeOption = 0;
        }
        if (ExtendedModSettings.SHOW_CAPE)
        {
            this.capeOption = 1;
        }
        if (this.mc.gameSettings.getModelParts().contains(EnumPlayerModelParts.CAPE))
        {
            this.capeOption = 2;
        }
        this.prevCapeOption = this.capeOption;
        this.buttonList.add(this.capeBtn = new GuiButton(3, this.width / 2 + 50 + 4, this.height / 4 + 50, 100, 20, ""));
        this.setTextForCapeOption();
    }

    @Override
    public void updateScreen()
    {
        this.doneBtn.enabled = !this.inputField.getText().isEmpty() || this.prevCapeOption != this.capeOption;
        this.resetBtn.enabled = !ExtendedModSettings.CAPE_URL.isEmpty();
        this.setTextForCapeOption();
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
                this.capeOption = this.prevCapeOption;
                this.saveCapeOption();
                this.mc.displayGuiScreen((GuiScreen)null);
            }
            if (button.id == 2)
            {
                ExtendedModSettings.CAPE_URL = "";
                CapeUtils.CAPE_TEXTURE.remove(IndicatorUtils.USERNAME);
                this.mc.thePlayer.addChatMessage(new JsonUtils().text("Reset cape texture"));
                this.inputField.setText("");
                ExtendedModSettings.saveExtendedSettings();
            }
            if (button.id == 3)
            {
                int i = 0;
                i++;
                this.capeOption = (this.capeOption + i) % 3;
                this.saveCapeOption();
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
        this.renderPlayer();
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, "Custom Cape Downloader", this.width / 2, 20, 16777215);
        this.drawCenteredString(this.fontRendererObj, "Put your Cape URL (Must be .png or image format)", this.width / 2, 37, 10526880);
        this.inputField.drawTextBox();
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }

    private void setTextForCapeOption()
    {
        switch (this.capeOption)
        {
        case 0:
            this.capeBtn.displayString = "Cape: OFF";
            break;
        case 1:
            this.capeBtn.displayString = "Cape: Custom";
            break;
        case 2:
            this.capeBtn.displayString = "Cape: OptiFine";
            break;
        }
    }

    private void saveCapeOption()
    {
        if (this.capeOption == 0)
        {
            ExtendedModSettings.SHOW_CAPE = false;
            Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
            Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
            Minecraft.getMinecraft().gameSettings.saveOptions();
            ExtendedModSettings.saveExtendedSettings();
        }
        if (this.capeOption == 1)
        {
            ExtendedModSettings.SHOW_CAPE = true;
            Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, false);
            Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
            Minecraft.getMinecraft().gameSettings.saveOptions();
            ExtendedModSettings.saveExtendedSettings();
        }
        if (this.capeOption == 2)
        {
            ExtendedModSettings.SHOW_CAPE = false;
            Minecraft.getMinecraft().gameSettings.setModelPartEnabled(EnumPlayerModelParts.CAPE, true);
            Minecraft.getMinecraft().gameSettings.sendSettingsToServer();
            Minecraft.getMinecraft().gameSettings.saveOptions();
            ExtendedModSettings.saveExtendedSettings();
        }
    }

    private void renderPlayer()
    {
        float f2 = this.mc.thePlayer.renderYawOffset;
        float f3 = this.mc.thePlayer.rotationYaw;
        float f4 = this.mc.thePlayer.rotationPitch;
        float f5 = this.mc.thePlayer.rotationYawHead;
        float scale = 40.0F + this.height / 8 - 28;
        GlStateManager.enableColorMaterial();
        GlStateManager.pushMatrix();
        GlStateManager.translate(this.width / 2 - 50, this.height / 4 + 55, 0.0F);
        GlStateManager.scale(-scale, scale, scale);
        GlStateManager.rotate(180.0F, 0.0F, 0.0F, 1.0F);
        RenderHelper.enableStandardItemLighting();
        GlStateManager.rotate(180.0F, 0.0F, 1.0F, 0.0F);
        this.mc.thePlayer.renderYawOffset = 0.0F;
        this.mc.thePlayer.rotationYaw = (float) Math.atan(19 / 40.0F) * 40.0F;
        this.mc.thePlayer.rotationYaw = 0.0F;
        this.mc.thePlayer.rotationYawHead = this.mc.thePlayer.rotationYaw;
        GlStateManager.translate(0.0F, (float) this.mc.thePlayer.getYOffset(), 0.0F);
        RenderManager rendermanager = Minecraft.getMinecraft().getRenderManager();
        rendermanager.setPlayerViewY(180.0F);
        rendermanager.setRenderShadow(false);
        rendermanager.doRenderEntity(this.mc.thePlayer, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F, false);
        rendermanager.setRenderShadow(true);
        this.mc.thePlayer.renderYawOffset = f2;
        this.mc.thePlayer.rotationYaw = f3;
        this.mc.thePlayer.rotationPitch = f4;
        this.mc.thePlayer.rotationYawHead = f5;
        GlStateManager.popMatrix();
        RenderHelper.disableStandardItemLighting();
        GlStateManager.disableRescaleNormal();
        GlStateManager.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GlStateManager.disableTexture2D();
        GlStateManager.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
}