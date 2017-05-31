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

            IULog.info("Loading extended settings : %s", file.getPath());
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
            nbt.setBoolean("FPS", (boolean) login.getObjects()[1]);
            list.appendTag(nbt);
        }
        return list;
    }

    private static void readAutoLoginData(NBTTagList list)
    {
        for (int i = 0; i < list.tagCount(); ++i)
        {
            NBTTagCompound nbt = list.getCompoundTagAt(i);
            ProfileSettings.profileData.addProfileData(nbt.getString("Name"), nbt.getBoolean("Ping"), nbt.getBoolean("FPS"));
        }
    }
}