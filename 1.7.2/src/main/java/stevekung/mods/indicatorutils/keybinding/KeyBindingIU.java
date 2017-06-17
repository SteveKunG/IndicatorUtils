package stevekung.mods.indicatorutils.keybinding;

import net.minecraft.client.settings.KeyBinding;

public class KeyBindingIU extends KeyBinding
{
    public KeyBindingIU(String description, int keyCode)
    {
        super(description, keyCode, "key.indicatorutils.category");
    }
}