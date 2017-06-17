package stevekung.mods.indicatorutils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.MathHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;

@SideOnly(Side.CLIENT)
public class GuiRenderStatusSliderInt extends GuiButton
{
    private float sliderValue;
    public boolean dragging;
    private Options options;

    public GuiRenderStatusSliderInt(int buttonId, int x, int y, Options option)
    {
        super(buttonId, x, y, 200, 20, "");
        this.sliderValue = 1.0F;
        this.options = option;
        this.sliderValue = option.normalizeValue(this.getOptionValue(option));
        this.displayString = option.getEnumString() + ": " + this.getOptionValue(option);
    }

    public GuiRenderStatusSliderInt(int buttonId, int x, int y, int width, Options option)
    {
        super(buttonId, x, y, width, 20, "");
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
            mc.getTextureManager().bindTexture(buttonTextures);
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

    public void setOptionValue(Options option, float value)
    {
        int ivalue = (int) value;

        switch (option)
        {
        case ARMOR_Y:
            ExtendedModSettings.ARMOR_STATUS_OFFSET = ivalue;
            break;
        case KEYSTOKE_Y:
            ExtendedModSettings.KEYSTROKE_Y_OFFSET = ivalue;
            break;
        case KEYSTROKE_BLOCK_RED:
            ExtendedModSettings.KEYSTROKE_BLOCK_RED = ivalue;
            break;
        case KEYSTROKE_BLOCK_GREEN:
            ExtendedModSettings.KEYSTROKE_BLOCK_GREEN = ivalue;
            break;
        case KEYSTROKE_BLOCK_BLUE:
            ExtendedModSettings.KEYSTROKE_BLOCK_BLUE = ivalue;
            break;
        case KEYSTROKE_CPS_RED:
            ExtendedModSettings.KEYSTROKE_CPS_RED = ivalue;
            break;
        case KEYSTROKE_CPS_GREEN:
            ExtendedModSettings.KEYSTROKE_CPS_GREEN = ivalue;
            break;
        case KEYSTROKE_CPS_BLUE:
            ExtendedModSettings.KEYSTROKE_CPS_BLUE = ivalue;
            break;
        case KEYSTROKE_WASD_RED:
            ExtendedModSettings.KEYSTROKE_WASD_RED = ivalue;
            break;
        case KEYSTROKE_WASD_GREEN:
            ExtendedModSettings.KEYSTROKE_WASD_GREEN = ivalue;
            break;
        case KEYSTROKE_WASD_BLUE:
            ExtendedModSettings.KEYSTROKE_WASD_BLUE = ivalue;
            break;
        case KEYSTROKE_LMBRMB_RED:
            ExtendedModSettings.KEYSTROKE_LMBRMB_RED = ivalue;
            break;
        case KEYSTROKE_LMBRMB_GREEN:
            ExtendedModSettings.KEYSTROKE_LMBRMB_GREEN = ivalue;
            break;
        case KEYSTROKE_LMBRMB_BLUE:
            ExtendedModSettings.KEYSTROKE_LMBRMB_BLUE = ivalue;
            break;
        case KEYSTROKE_SPRINT_RED:
            ExtendedModSettings.KEYSTROKE_SPRINT_RED = ivalue;
            break;
        case KEYSTROKE_SPRINT_GREEN:
            ExtendedModSettings.KEYSTROKE_SPRINT_GREEN = ivalue;
            break;
        case KEYSTROKE_SPRINT_BLUE:
            ExtendedModSettings.KEYSTROKE_SPRINT_BLUE = ivalue;
            break;
        case KEYSTROKE_SNEAK_RED:
            ExtendedModSettings.KEYSTROKE_SNEAK_RED = ivalue;
            break;
        case KEYSTROKE_SNEAK_GREEN:
            ExtendedModSettings.KEYSTROKE_SNEAK_GREEN = ivalue;
            break;
        case KEYSTROKE_SNEAK_BLUE:
            ExtendedModSettings.KEYSTROKE_SNEAK_BLUE = ivalue;
            break;
        case MAX_POTION_DISPLAY:
            ExtendedModSettings.MAX_POTION_DISPLAY = ivalue;
            break;
        case POTION_LENGTH_Y_OFFSET:
            ExtendedModSettings.POTION_LENGTH_Y_OFFSET = ivalue;
            break;
        case POTION_LENGTH_Y_OFFSET_OVERLAP:
            ExtendedModSettings.POTION_LENGTH_Y_OFFSET_OVERLAP = ivalue;
            break;
        case POTION_Y:
            ExtendedModSettings.POTION_STATUS_OFFSET = ivalue;
            break;
        default:
            break;
        }
    }

    public int getOptionValue(Options option)
    {
        switch (option)
        {
        case ARMOR_Y:
            return ExtendedModSettings.ARMOR_STATUS_OFFSET;
        case KEYSTOKE_Y:
            return ExtendedModSettings.KEYSTROKE_Y_OFFSET;
        case KEYSTROKE_BLOCK_RED:
            return ExtendedModSettings.KEYSTROKE_BLOCK_RED;
        case KEYSTROKE_BLOCK_GREEN:
            return ExtendedModSettings.KEYSTROKE_BLOCK_GREEN;
        case KEYSTROKE_BLOCK_BLUE:
            return ExtendedModSettings.KEYSTROKE_BLOCK_BLUE;
        case KEYSTROKE_CPS_RED:
            return ExtendedModSettings.KEYSTROKE_CPS_RED;
        case KEYSTROKE_CPS_GREEN:
            return ExtendedModSettings.KEYSTROKE_CPS_GREEN;
        case KEYSTROKE_CPS_BLUE:
            return ExtendedModSettings.KEYSTROKE_CPS_BLUE;
        case KEYSTROKE_WASD_RED:
            return ExtendedModSettings.KEYSTROKE_WASD_RED;
        case KEYSTROKE_WASD_GREEN:
            return ExtendedModSettings.KEYSTROKE_WASD_GREEN;
        case KEYSTROKE_WASD_BLUE:
            return ExtendedModSettings.KEYSTROKE_WASD_BLUE;
        case KEYSTROKE_LMBRMB_BLUE:
            return ExtendedModSettings.KEYSTROKE_LMBRMB_BLUE;
        case KEYSTROKE_LMBRMB_GREEN:
            return ExtendedModSettings.KEYSTROKE_LMBRMB_GREEN;
        case KEYSTROKE_LMBRMB_RED:
            return ExtendedModSettings.KEYSTROKE_LMBRMB_RED;
        case KEYSTROKE_SPRINT_RED:
            return ExtendedModSettings.KEYSTROKE_SPRINT_RED;
        case KEYSTROKE_SPRINT_GREEN:
            return ExtendedModSettings.KEYSTROKE_SPRINT_GREEN;
        case KEYSTROKE_SPRINT_BLUE:
            return ExtendedModSettings.KEYSTROKE_SPRINT_BLUE;
        case KEYSTROKE_SNEAK_RED:
            return ExtendedModSettings.KEYSTROKE_SNEAK_RED;
        case KEYSTROKE_SNEAK_GREEN:
            return ExtendedModSettings.KEYSTROKE_SNEAK_GREEN;
        case KEYSTROKE_SNEAK_BLUE:
            return ExtendedModSettings.KEYSTROKE_SNEAK_BLUE;
        case MAX_POTION_DISPLAY:
            return ExtendedModSettings.MAX_POTION_DISPLAY;
        case POTION_LENGTH_Y_OFFSET:
            return ExtendedModSettings.POTION_LENGTH_Y_OFFSET;
        case POTION_LENGTH_Y_OFFSET_OVERLAP:
            return ExtendedModSettings.POTION_LENGTH_Y_OFFSET_OVERLAP;
        case POTION_Y:
            return ExtendedModSettings.POTION_STATUS_OFFSET;
        }
        return 0;
    }

    @SideOnly(Side.CLIENT)
    public static enum Options
    {
        ARMOR_Y("Armor Status Y Position", -512.0F, 512.0F, 1.0F),
        POTION_Y("Potion Status Y Position", -512.0F, 512.0F, 1.0F),
        KEYSTOKE_Y("Keystroke Y Position", -512.0F, 512.0F, 1.0F),
        MAX_POTION_DISPLAY("Max Potion Display", 2.0F, 8.0F, 1.0F),
        POTION_LENGTH_Y_OFFSET("Potion Length Y Offset", 1.0F, 256.0F, 1.0F),
        POTION_LENGTH_Y_OFFSET_OVERLAP("Potion Length Y Offset (Overlap)", 1.0F, 256.0F, 1.0F),
        KEYSTROKE_WASD_RED("WASD Red", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_WASD_GREEN("WASD Green", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_WASD_BLUE("WASD Blue", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_LMBRMB_RED("LMB/RMB Red", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_LMBRMB_GREEN("LMB/RMB Green", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_LMBRMB_BLUE("LMB/RMB Blue", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_CPS_RED("CPS Red", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_CPS_GREEN("CPS Green", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_CPS_BLUE("CPS Blue", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_SPRINT_RED("Sprint Red", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_SPRINT_GREEN("Sprint Green", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_SPRINT_BLUE("Sprint Blue", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_SNEAK_RED("Sneak Red", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_SNEAK_GREEN("Sneak Green", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_SNEAK_BLUE("Sneak Blue", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_BLOCK_RED("Block Red", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_BLOCK_GREEN("Block Green", 0.0F, 255.0F, 1.0F),
        KEYSTROKE_BLOCK_BLUE("Block Blue", 0.0F, 255.0F, 1.0F);

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