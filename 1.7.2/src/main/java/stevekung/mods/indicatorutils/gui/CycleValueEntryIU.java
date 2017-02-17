/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.gui;

import cpw.mods.fml.client.config.GuiConfig;
import cpw.mods.fml.client.config.GuiConfigEntries;
import cpw.mods.fml.client.config.IConfigElement;
import net.minecraft.client.resources.I18n;

public class CycleValueEntryIU extends ButtonEntry
{
    protected int beforeIndex;
    protected int defaultIndex;
    protected int currentIndex;

    public CycleValueEntryIU(GuiConfig owningScreen, GuiConfigEntries owningEntryList, IConfigElement configElement)
    {
        super(owningScreen, owningEntryList, configElement);
        this.beforeIndex = this.getIndex(configElement.get().toString());
        this.defaultIndex = this.getIndex(configElement.getDefault().toString());
        this.currentIndex = this.beforeIndex;
        this.btnValue.enabled = this.enabled();
        this.updateValueButtonText();
    }

    private int getIndex(String s)
    {
        for (int i = 0; i < this.configElement.getValidValues().length; i++)
        {
            if (this.configElement.getValidValues()[i].equalsIgnoreCase(s))
            {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void updateValueButtonText()
    {
        this.btnValue.displayString = I18n.format(this.configElement.getValidValues()[this.currentIndex]);
    }

    @Override
    public void valueButtonPressed(int slotIndex)
    {
        if (this.enabled())
        {
            if (++this.currentIndex >= this.configElement.getValidValues().length)
            {
                this.currentIndex = 0;
            }
            this.updateValueButtonText();
        }
    }

    @Override
    public boolean isDefault()
    {
        return this.currentIndex == this.defaultIndex;
    }

    @Override
    public void setToDefault()
    {
        if (this.enabled())
        {
            this.currentIndex = this.defaultIndex;
            this.updateValueButtonText();
        }
    }

    @Override
    public boolean isChanged()
    {
        return this.currentIndex != this.beforeIndex;
    }

    @Override
    public void undoChanges()
    {
        if (this.enabled())
        {
            this.currentIndex = this.beforeIndex;
            this.updateValueButtonText();
        }
    }

    @Override
    public boolean saveConfigElement()
    {
        if (this.enabled() && this.isChanged())
        {
            this.configElement.set(this.configElement.getValidValues()[this.currentIndex]);
            return this.configElement.requiresMcRestart();
        }
        return false;
    }

    @Override
    public String getCurrentValue()
    {
        return this.configElement.getValidValues()[this.currentIndex];
    }

    @Override
    public String[] getCurrentValues()
    {
        return new String[] { this.getCurrentValue() };
    }
}