package stevekung.mods.indicatorutils;

import java.io.IOException;

import cpw.mods.fml.common.asm.transformers.AccessTransformer;

public class IndicatorUtilsAT extends AccessTransformer
{
    public IndicatorUtilsAT() throws IOException
    {
        super("indicatorutils_at.cfg");
    }
}