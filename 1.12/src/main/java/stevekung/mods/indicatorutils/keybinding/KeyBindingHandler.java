package stevekung.mods.indicatorutils.keybinding;

import org.lwjgl.input.Keyboard;

import net.minecraft.client.settings.KeyBinding;
import net.minecraftforge.client.settings.KeyModifier;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import stevekung.mods.indicatorutils.config.ConfigManager;

public class KeyBindingHandler
{
    public static KeyBinding KEY_TOGGLE_SPRINT;
    public static KeyBinding KEY_TOGGLE_SNEAK;
    public static KeyBinding KEY_AUTO_SWIM;
    public static KeyBinding KEY_REC_COMMAND;
    public static KeyBinding KEY_END_GAME_MESSAGE;
    public static KeyBinding KEY_OPEN_CAPE_DOWNLOADER_GUI;

    public static void initKeyBinding()
    {
        KeyBindingHandler.KEY_TOGGLE_SPRINT = new KeyBindingIU("key.togglesprint.desc", KeyModifier.CONTROL, Keyboard.KEY_S);
        KeyBindingHandler.KEY_TOGGLE_SNEAK = new KeyBindingIU("key.togglesneak.desc", KeyModifier.CONTROL, Keyboard.KEY_LSHIFT);
        KeyBindingHandler.KEY_AUTO_SWIM = new KeyBindingIU("key.autoswim.desc", KeyModifier.CONTROL, Keyboard.KEY_R);
        KeyBindingHandler.KEY_REC_COMMAND = new KeyBinding("key.reccommand.desc", Keyboard.KEY_F9, "key.indicatorutils.category");
        KeyBindingHandler.KEY_END_GAME_MESSAGE = new KeyBinding("key.endgamemessage.desc", Keyboard.KEY_G, "key.indicatorutils.category");

        if (ConfigManager.enableCustomCapeFeature)
        {
            KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI = new KeyBinding("key.capedownloadgui.desc", Keyboard.KEY_H, "key.indicatorutils.category");
            ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI);
        }

        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_TOGGLE_SPRINT);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_TOGGLE_SNEAK);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_AUTO_SWIM);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_REC_COMMAND);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_END_GAME_MESSAGE);
    }
}