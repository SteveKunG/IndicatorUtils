/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import org.lwjgl.input.Keyboard;

import cpw.mods.fml.client.registry.ClientRegistry;
import net.minecraft.client.settings.KeyBinding;

public class KeyBindingHandler
{
    public static KeyBinding KEY_TOGGLE_SPRINT;
    public static KeyBinding KEY_TOGGLE_SNEAK;
    public static KeyBinding KEY_AUTO_SWIM;
    public static KeyBinding KEY_DISPLAY_MODE_NEXT;
    public static KeyBinding KEY_DISPLAY_MODE_PREVIOUS;
    public static KeyBinding KEY_REC_COMMAND;
    public static KeyBinding KEY_END_GAME_MESSAGE;
    public static KeyBinding KEY_OPEN_CAPE_DOWNLOADER_GUI;

    public static void initKeyBinding()
    {
        KeyBindingHandler.KEY_TOGGLE_SPRINT = new KeyBindingIU("key.togglesprint.desc", Keyboard.KEY_B);
        KeyBindingHandler.KEY_TOGGLE_SNEAK = new KeyBindingIU("key.togglesneak.desc", Keyboard.KEY_N);
        KeyBindingHandler.KEY_AUTO_SWIM = new KeyBindingIU("key.autoswim.desc", Keyboard.KEY_Y);
        KeyBindingHandler.KEY_DISPLAY_MODE_NEXT = new KeyBindingIU("key.displaymode.next.desc", Keyboard.KEY_RBRACKET);
        KeyBindingHandler.KEY_DISPLAY_MODE_PREVIOUS = new KeyBindingIU("key.displaymode.previous.desc", Keyboard.KEY_LBRACKET);
        KeyBindingHandler.KEY_REC_COMMAND = new KeyBindingIU("key.reccommand.desc", Keyboard.KEY_F9);
        KeyBindingHandler.KEY_END_GAME_MESSAGE = new KeyBindingIU("key.endgamemessage.desc", Keyboard.KEY_G);

        if (ConfigManager.enableCustomCapeFeature)
        {
            KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI = new KeyBindingIU("key.capedownloadgui.desc", Keyboard.KEY_H);
            ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI);
        }

        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_TOGGLE_SPRINT);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_TOGGLE_SNEAK);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_AUTO_SWIM);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_DISPLAY_MODE_NEXT);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_DISPLAY_MODE_PREVIOUS);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_REC_COMMAND);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_END_GAME_MESSAGE);
    }
}