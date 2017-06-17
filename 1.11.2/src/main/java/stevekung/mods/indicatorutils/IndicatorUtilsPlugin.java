package stevekung.mods.indicatorutils;

import java.util.Map;

import net.minecraftforge.fml.relauncher.IFMLCallHook;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.MCVersion;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin.TransformerExclusions;
import stevekung.mods.indicatorutils.utils.IULog;

@TransformerExclusions(value = { "stevekung.mods.indicatorutils" })
@MCVersion(value = "1.11.2")
public class IndicatorUtilsPlugin implements IFMLLoadingPlugin, IFMLCallHook
{
    @Override
    public String[] getASMTransformerClass()
    {
        return null;
    }

    @Override
    public String getModContainerClass()
    {
        return IndicatorUtilsDummyContainer.class.getName();
    }

    @Override
    public String getSetupClass()
    {
        return IndicatorUtilsPlugin.class.getName();
    }

    @Override
    public String getAccessTransformerClass()
    {
        IULog.info("Calling access transformer class %s", IndicatorUtilsAT.class.getName());
        return IndicatorUtilsAT.class.getName();
    }

    @Override
    public void injectData(Map<String, Object> data) {}

    @Override
    public Void call() throws Exception
    {
        return null;
    }
}