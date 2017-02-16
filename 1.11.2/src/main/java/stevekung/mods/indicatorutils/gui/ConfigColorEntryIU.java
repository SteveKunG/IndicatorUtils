/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.gui;

import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiUtils;
import net.minecraftforge.fml.client.config.IConfigElement;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;

public class ConfigColorEntryIU extends CycleValueEntryIU
{
    public ConfigColorEntryIU(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        this.btnValue.enabled = this.enabled();
        this.updateValueButtonText();
    }

    @Override
    public void drawEntry(int slotIndex, int x, int y, int listWidth, int slotHeight, int mouseX, int mouseY, boolean isSelected)
    {
        int color = GuiUtils.getColorCode(GameInfoHelper.INSTANCE.getColorCode()[this.currentIndex].charAt(0), true);

        if (color == 0)
        {
            color = 3618615;
        }
        this.btnValue.packedFGColour = color;
        super.drawEntry(slotIndex, x, y, listWidth, slotHeight, mouseX, mouseY, isSelected);
    }

    @Override
    public void updateValueButtonText()
    {
        this.btnValue.displayString = GameInfoHelper.INSTANCE.getJsonColor()[this.currentIndex];
    }
}