/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.keybinding;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;

public class KeyBindingIU extends KeyBinding
{
    public KeyBindingIU(String description, KeyModifier keyModifier, int keyCode)
    {
        super(description, new KeyConflictContextHandler(), keyModifier, keyCode, "key.indicatorutils.category");
    }
}