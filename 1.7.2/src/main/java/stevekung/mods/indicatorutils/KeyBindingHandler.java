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
    public static KeyBinding KEY_REC_COMMAND;
    public static KeyBinding KEY_END_GAME_MESSAGE;
    public static KeyBinding KEY_OPEN_CAPE_DOWNLOADER_GUI;

    public static void initKeyBinding()
    {
        KeyBindingHandler.KEY_REC_COMMAND = new KeyBindingIU("key.reccommand.desc", Keyboard.KEY_F9);
        KeyBindingHandler.KEY_END_GAME_MESSAGE = new KeyBindingIU("key.endgamemessage.desc", Keyboard.KEY_G);

        if (ConfigManager.enableCustomCapeFeature)
        {
            KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI = new KeyBindingIU("key.capedownloadgui.desc", Keyboard.KEY_H);
            ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI);
        }

        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_REC_COMMAND);
        ClientRegistry.registerKeyBinding(KeyBindingHandler.KEY_END_GAME_MESSAGE);
    }
}