package stevekung.mods.indicatorutils.profile;

import net.minecraftforge.common.config.Property;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.profile.ProfileData.ProfileSettingData;

public class ProfileConfigData
{
    public void load(ProfileSettingData data)
    {
        Property prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Ping", true);
        this.set(prop, data.getObjects()[0]);
        ConfigManager.enablePing = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Server IP", true);
        this.set(prop, data.getObjects()[1]);
        ConfigManager.enableServerIP = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable FPS", true);
        this.set(prop, data.getObjects()[2]);
        ConfigManager.enableFPS = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable XYZ", true);
        this.set(prop, data.getObjects()[3]);
        ConfigManager.enableXYZ = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Looking at Block", false);
        this.set(prop, data.getObjects()[4]);
        ConfigManager.enableLookingAtBlock = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Direction", true);
        this.set(prop, data.getObjects()[5]);
        ConfigManager.enableDirection = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Biome", true);
        this.set(prop, data.getObjects()[6]);
        ConfigManager.enableBiome = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Armor Status", true);
        this.set(prop, data.getObjects()[7]);
        ConfigManager.enableArmorStatus = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Potion Status", false);
        this.set(prop, data.getObjects()[8]);
        ConfigManager.enablePotionStatus = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Keystroke", false);
        this.set(prop, data.getObjects()[9]);
        ConfigManager.enableKeystroke = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable CPS", false);
        this.set(prop, data.getObjects()[10]);
        ConfigManager.enableCPS = prop.getBoolean();

        prop = ConfigManager.config.get(ConfigManager.RENDER_INFO_SETTINGS, "Enable Held Item", false);
        this.set(prop, data.getObjects()[11]);
        ConfigManager.enableHeldItemInHand = prop.getBoolean();
    }

    public void set(Property prop, Object object)
    {
        prop.set(object.toString());
    }
}
