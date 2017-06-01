/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.profile;

import java.io.File;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import stevekung.mods.indicatorutils.utils.IULog;

public class ProfileSettings
{
    public static ProfileData profileData = new ProfileData();

    public static void loadProfileSettings()
    {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "indicatorutils_profiles.dat");

        try
        {
            NBTTagCompound nbt = CompressedStreamTools.read(file);

            if (nbt == null)
            {
                return;
            }
            ProfileSettings.readProfileData(nbt.getTagList("ProfileData", 10));
            IULog.info("Loading profile data settings : %s", file.getPath());
        }
        catch (Exception e) {}
    }

    public static void saveProfileSettings()
    {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "indicatorutils_profiles.dat");

        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("ProfileData", ProfileSettings.writeProfileData());
            CompressedStreamTools.safeWrite(nbttagcompound, file);
        }
        catch (Exception e) {}
    }

    private static NBTTagList writeProfileData()
    {
        NBTTagList list = new NBTTagList();

        for (ProfileData.ProfileSettingData login : ProfileSettings.profileData.getProfileList())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Name", login.getProfileName());
            nbt.setBoolean("Ping", (Boolean) login.getObjects()[0]);
            nbt.setBoolean("IP", (Boolean) login.getObjects()[1]);
            nbt.setBoolean("FPS", (Boolean) login.getObjects()[2]);
            nbt.setBoolean("XYZ", (Boolean) login.getObjects()[3]);
            nbt.setBoolean("LookBlock", (Boolean) login.getObjects()[4]);
            nbt.setBoolean("Direction", (Boolean) login.getObjects()[5]);
            nbt.setBoolean("Biome", (Boolean) login.getObjects()[6]);
            nbt.setBoolean("Armor", (Boolean) login.getObjects()[7]);
            nbt.setBoolean("Potion", (Boolean) login.getObjects()[8]);
            nbt.setBoolean("Keystroke", (Boolean) login.getObjects()[9]);
            nbt.setBoolean("CPS", (Boolean) login.getObjects()[10]);
            nbt.setBoolean("Held", (Boolean) login.getObjects()[11]);
            nbt.setString("ArmorStat", (String) login.getObjects()[12]);
            nbt.setString("PotionStat", (String) login.getObjects()[13]);
            nbt.setString("ArmorPos", (String) login.getObjects()[14]);
            nbt.setString("PotionPos", (String) login.getObjects()[15]);
            nbt.setString("KeystokePos", (String) login.getObjects()[16]);
            nbt.setInteger("ArmorOffset", (Integer) login.getObjects()[17]);
            nbt.setInteger("PotionOffset", (Integer) login.getObjects()[18]);
            nbt.setInteger("KeystrokeY", (Integer) login.getObjects()[19]);
            nbt.setBoolean("Time", (Boolean) login.getObjects()[20]);
            nbt.setBoolean("GameTime", (Boolean) login.getObjects()[21]);
            nbt.setBoolean("MoonPhase", (Boolean) login.getObjects()[22]);
            nbt.setBoolean("WeatherStat", (Boolean) login.getObjects()[23]);
            nbt.setBoolean("SlimeChunk", (Boolean) login.getObjects()[24]);
            list.appendTag(nbt);
        }
        return list;
    }

    private static void readProfileData(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            ProfileSettings.profileData.addProfileData(nbt.getString("Name"), nbt.getBoolean("Ping"), nbt.getBoolean("IP"), nbt.getBoolean("FPS"), nbt.getBoolean("XYZ"), nbt.getBoolean("LookBlock"),
                    nbt.getBoolean("Direction"), nbt.getBoolean("Biome"), nbt.getBoolean("Armor"), nbt.getBoolean("Potion"), nbt.getBoolean("Keystroke"), nbt.getBoolean("CPS"), nbt.getBoolean("Held"),
                    nbt.getString("ArmorStat"), nbt.getString("PotionStat"), nbt.getString("ArmorPos"), nbt.getString("PotionPos"), nbt.getString("KeystokePos"), nbt.getInteger("ArmorOffset"),
                    nbt.getInteger("PotionOffset"), nbt.getInteger("KeystrokeY"), nbt.getBoolean("Time"), nbt.getBoolean("GameTime"), nbt.getBoolean("MoonPhase"), nbt.getBoolean("WeatherStat"), nbt.getBoolean("SlimeChunk"));
        }
    }
}