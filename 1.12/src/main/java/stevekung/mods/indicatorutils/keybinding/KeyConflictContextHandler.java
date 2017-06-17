package stevekung.mods.indicatorutils.keybinding;

import net.minecraftforge.client.settings.IKeyConflictContext;
import net.minecraftforge.client.settings.KeyConflictContext;

public class KeyConflictContextHandler implements IKeyConflictContext
{
    @Override
    public boolean isActive()
    {
        return KeyConflictContext.IN_GAME.isActive();
    }

    @Override
    public boolean conflicts(IKeyConflictContext other)
    {
        return false;
    }
}