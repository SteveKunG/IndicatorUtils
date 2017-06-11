/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.common.eventbus.EventBus;

import net.minecraftforge.fml.common.DummyModContainer;
import net.minecraftforge.fml.common.LoadController;
import net.minecraftforge.fml.common.ModMetadata;
import net.minecraftforge.fml.common.versioning.ArtifactVersion;
import net.minecraftforge.fml.common.versioning.VersionParser;
import net.minecraftforge.fml.common.versioning.VersionRange;

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
        LinkedList<ArtifactVersion> deps = new LinkedList<ArtifactVersion>();
        deps.add(VersionParser.parseVersionReference("required-after:Forge@[14.21.0.2321,)"));
        return deps;
    }

    @Override
    public VersionRange acceptableMinecraftVersionRange()
    {
        return VersionParser.parseRange("[" + IndicatorUtils.MC_VERSION + "]");
    }
}