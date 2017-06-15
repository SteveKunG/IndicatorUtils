/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

public enum EnumMCVersion
{
    MC_1_7_2(0, "1.7.2"),
    MC_1_7_10(1, "1.7.10"),
    MC_1_8_9(2, "1.8.9"),
    MC_1_9_4(3, "1.9.4"),
    MC_1_10_2(4, "1.10.2"),
    MC_1_11_2(5, "1.11.2"),
    MC_1_12(5, "1.12");

    private int versionIndex;
    private String version;
    private static EnumMCVersion[] values = EnumMCVersion.values();

    private EnumMCVersion(int versionIndex, String version)
    {
        this.versionIndex = versionIndex;
        this.version = version;
    }

    public static EnumMCVersion[] valuesCached()
    {
        return EnumMCVersion.values;
    }

    public int getVersionIndex()
    {
        return this.versionIndex;
    }

    public String getVersion()
    {
        return this.version;
    }
}