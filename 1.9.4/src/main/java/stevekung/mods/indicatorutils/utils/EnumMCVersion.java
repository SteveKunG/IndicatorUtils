/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.utils;

public enum EnumMCVersion
{
    MC_1_7_10(0, "1.7.10"),
    MC_1_8_9(1, "1.8.9"),
    MC_1_9_4(2, "1.9.4"),
    MC_1_10_2(3, "1.10.2"),
    MC_1_11(4, "1.11");

    private int versionIndex;
    private String version;

    private EnumMCVersion(int versionIndex, String version)
    {
        this.versionIndex = versionIndex;
        this.version = version;
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