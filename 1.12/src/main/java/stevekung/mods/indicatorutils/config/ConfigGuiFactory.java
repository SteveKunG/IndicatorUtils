/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.config;

import java.util.Set;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.resources.I18n;
import net.minecraftforge.fml.client.IModGuiFactory;
import net.minecraftforge.fml.client.config.GuiConfig;
import stevekung.mods.indicatorutils.IndicatorUtils;

public class ConfigGuiFactory implements IModGuiFactory
{
    @Override
    public void initialize(Minecraft mc) {}

    @Override
    public Class<? extends GuiScreen> mainConfigGuiClass()
    {
        return ConfigGUI.class;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories()
    {
        return null;
    }

    @SuppressWarnings("deprecation")
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element)
    {
        return null;
    }

    @Override
    public boolean hasConfigGui()
    {
        return true;
    }

    @Override
    public GuiScreen createConfigGui(GuiScreen gui)
    {
        return new ConfigGUI(gui);
    }

    public static class ConfigGUI extends GuiConfig
    {
        public ConfigGUI(GuiScreen gui)
        {
            super(gui, ConfigManager.getConfigElements(), IndicatorUtils.MOD_ID, false, false, I18n.format("gui.indicatorutils.config.name"));
        }
    }
}