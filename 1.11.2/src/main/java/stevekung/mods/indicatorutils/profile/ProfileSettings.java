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

    public static void loadExtendedSettings()
    {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "indicatorutils_profiles.dat");

        try
        {
            NBTTagCompound nbt = CompressedStreamTools.read(file);

            if (nbt == null)
            {
                return;
            }
            ProfileSettings.readAutoLoginData(nbt.getTagList("ProfileData", 10));
            IULog.info("Loading profile data settings : %s", file.getPath());
        }
        catch (Exception e) {}
    }

    public static void saveExtendedSettings()
    {
        File file = new File(Minecraft.getMinecraft().mcDataDir, "indicatorutils_profiles.dat");

        try
        {
            NBTTagCompound nbttagcompound = new NBTTagCompound();
            nbttagcompound.setTag("ProfileData", ProfileSettings.writeAutoLoginData());
            CompressedStreamTools.safeWrite(nbttagcompound, file);
        }
        catch (Exception e) {}
    }

    private static NBTTagList writeAutoLoginData()
    {
        NBTTagList list = new NBTTagList();

        for (ProfileData.ProfileSettingData login : ProfileSettings.profileData.getProfileList())
        {
            NBTTagCompound nbt = new NBTTagCompound();
            nbt.setString("Name", login.getProfileName());
            nbt.setBoolean("Ping", (boolean) login.getObjects()[0]);
            nbt.setBoolean("IP", (boolean) login.getObjects()[1]);
            nbt.setBoolean("FPS", (boolean) login.getObjects()[2]);
            nbt.setBoolean("XYZ", (boolean) login.getObjects()[3]);
            nbt.setBoolean("LookBlock", (boolean) login.getObjects()[4]);
            nbt.setBoolean("Direction", (boolean) login.getObjects()[5]);
            nbt.setBoolean("Biome", (boolean) login.getObjects()[6]);
            nbt.setBoolean("Armor", (boolean) login.getObjects()[7]);
            nbt.setBoolean("Potion", (boolean) login.getObjects()[8]);
            nbt.setBoolean("Keystroke", (boolean) login.getObjects()[9]);
            nbt.setBoolean("CPS", (boolean) login.getObjects()[10]);
            nbt.setBoolean("Held", (boolean) login.getObjects()[11]);
            list.appendTag(nbt);
        }
        return list;
    }

    private static void readAutoLoginData(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            ProfileSettings.profileData.addProfileData(nbt.getString("Name"), nbt.getBoolean("Ping"), nbt.getBoolean("IP"), nbt.getBoolean("FPS"), nbt.getBoolean("XYZ"), nbt.getBoolean("LookBlock"),
                    nbt.getBoolean("Direction"), nbt.getBoolean("Biome"), nbt.getBoolean("Armor"), nbt.getBoolean("Potion"), nbt.getBoolean("Keystroke"), nbt.getBoolean("CPS"), nbt.getBoolean("Held"));
        }
    }
}