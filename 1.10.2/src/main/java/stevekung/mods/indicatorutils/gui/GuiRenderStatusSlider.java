package stevekung.mods.indicatorutils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;

@SideOnly(Side.CLIENT)
public class GuiRenderStatusSlider extends GuiButton
{
    private float sliderValue;
    public boolean dragging;
    private Options options;

    public GuiRenderStatusSlider(int buttonId, int x, int y, Options option)
    {
        super(buttonId, x, y, 200, 20, "");
        this.sliderValue = 1.0F;
        this.options = option;
        this.sliderValue = option.normalizeValue(this.getOptionValue(option));
        this.displayString = option.getEnumString() + ": " + this.getOptionValue(option);
    }

    @Override
    protected int getHoverState(boolean mouseOver)
    {
        return 0;
    }

    @Override
    protected void mouseDragged(Minecraft mc, int mouseX, int mouseY)
    {
        if (this.visible)
        {
            if (this.dragging)
            {
                this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
                this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
                float f = this.options.denormalizeValue(this.sliderValue);
                this.setOptionValue(this.options, f);
                this.sliderValue = this.options.normalizeValue(f);
                this.displayString = this.options.getEnumString() + ": " + this.getOptionValue(this.options);
            }
            mc.getTextureManager().bindTexture(BUTTON_TEXTURES);
            GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)), this.yPosition, 0, 66, 4, 20);
            this.drawTexturedModalRect(this.xPosition + (int)(this.sliderValue * (this.width - 8)) + 4, this.yPosition, 196, 66, 4, 20);
        }
    }

    @Override
    public boolean mousePressed(Minecraft mc, int mouseX, int mouseY)
    {
        if (super.mousePressed(mc, mouseX, mouseY))
        {
            this.sliderValue = (float)(mouseX - (this.xPosition + 4)) / (float)(this.width - 8);
            this.sliderValue = MathHelper.clamp_float(this.sliderValue, 0.0F, 1.0F);
            this.setOptionValue(this.options, this.options.denormalizeValue(this.sliderValue));
            this.displayString = this.options.getEnumString() + ": " + this.getOptionValue(this.options);
            this.dragging = true;
            return true;
        }
        else
        {
            return false;
        }
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY)
    {
        this.dragging = false;
    }

    public void setOptionValue(Options settingsOption, float value)
    {
        int ivalue = (int) value;

        if (settingsOption == Options.ARMOR_Y)
        {
            ExtendedModSettings.ARMOR_STATUS_OFFSET = ivalue;
        }
        else if (settingsOption == Options.POTION_Y)
        {
            ExtendedModSettings.POTION_STATUS_OFFSET = ivalue;
        }
        else if (settingsOption == Options.KEYSTOKE_X)
        {
            ExtendedModSettings.KEYSTROKE_X_OFFSET = ivalue;
        }
        else if (settingsOption == Options.KEYSTOKE_Y)
        {
            ExtendedModSettings.KEYSTROKE_Y_OFFSET = ivalue;
        }
    }

    public int getOptionValue(Options settingOption)
    {
        if (settingOption == Options.ARMOR_Y)
        {
            return ExtendedModSettings.ARMOR_STATUS_OFFSET;
        }
        else if (settingOption == Options.POTION_Y)
        {
            return ExtendedModSettings.POTION_STATUS_OFFSET;
        }
        else if (settingOption == Options.KEYSTOKE_X)
        {
            return ExtendedModSettings.KEYSTROKE_X_OFFSET;
        }
        else if (settingOption == Options.KEYSTOKE_Y)
        {
            return ExtendedModSettings.KEYSTROKE_Y_OFFSET;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public static enum Options
    {
        ARMOR_Y("Armor Status Y Position", -512.0F, 512.0F, 1.0F),
        POTION_Y("Potion Status Y Position", -512.0F, 512.0F, 1.0F),
        KEYSTOKE_X("Keystroke (Advanced) X Position", -256.0F, 256.0F, 1.0F),
        KEYSTOKE_Y("Keystroke Y Position", -512.0F, 512.0F, 1.0F);

        private String enumString;
        private float valueStep;
        private float valueMin;
        private float valueMax;

        private Options(String str, float valMin, float valMax, float valStep)
        {
            this.enumString = str;
            this.valueMin = valMin;
            this.valueMax = valMax;
            this.valueStep = valStep;
        }

        public String getEnumString()
        {
            return this.enumString;
        }

        public float getValueMin()
        {
            return this.valueMin;
        }

        public float getValueMax()
        {
            return this.valueMax;
        }

        public void setValueMax(float value)
        {
            this.valueMax = value;
        }

        public float normalizeValue(float value)
        {
            return MathHelper.clamp_float((this.snapToStepclamp_float(value) - this.valueMin) / (this.valueMax - this.valueMin), 0.0F, 1.0F);
        }

        public float denormalizeValue(float value)
        {
            return this.snapToStepclamp_float(this.valueMin + (this.valueMax - this.valueMin) * MathHelper.clamp_float(value, 0.0F, 1.0F));
        }

        public float snapToStepclamp_float(float value)
        {
            value = this.snapToStep(value);
            return MathHelper.clamp_float(value, this.valueMin, this.valueMax);
        }

        private float snapToStep(float value)
        {
            if (this.valueStep > 0.0F)
            {
                value = this.valueStep * Math.round(value / this.valueStep);
            }
            return value;
        }
    }
}