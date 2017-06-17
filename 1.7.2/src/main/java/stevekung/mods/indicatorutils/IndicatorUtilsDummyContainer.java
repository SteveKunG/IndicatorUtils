package stevekung.mods.indicatorutils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.collect.Lists;
import com.google.common.eventbus.EventBus;

import cpw.mods.fml.common.DummyModContainer;
import cpw.mods.fml.common.LoadController;
import cpw.mods.fml.common.ModMetadata;
import cpw.mods.fml.common.versioning.ArtifactVersion;
import cpw.mods.fml.common.versioning.VersionParser;
import cpw.mods.fml.common.versioning.VersionRange;

public class IndicatorUtilsDummyContainer extends DummyModContainer
{
    public IndicatorUtilsDummyContainer()
    {
        super(new ModMetadata());
        ModMetadata info = this.getMetadata();
        info.modId = "indicatorutils_asm";
        info.name = "Indicator Utils ASM";
        info.version = IndicatorUtils.VERSION;
        info.description = "A core mod used for getting private data in Minecraft";
        info.url = "https://www.youtube.com/watch?v=9YJZFqiGXuA";
        info.authorList = Arrays.asList("SteveKunG");
    }

    @Override
    public boolean registerBus(EventBus bus, LoadController controller)
    {
        bus.register(this);
        return true;
    }

    @Override
    public List<ArtifactVersion> getDependencies()
    {
        LinkedList<ArtifactVersion> deps = Lists.newLinkedList();
        deps.add(VersionParser.parseVersionReference(IndicatorUtils.FORGE_VERSION.replace(";", "")));
        return deps;
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange()
    {
        return VersionParser.parseRange("[" + IndicatorUtils.MC_VERSION + "]");
    }
}