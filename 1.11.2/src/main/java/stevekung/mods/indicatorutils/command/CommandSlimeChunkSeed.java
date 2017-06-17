package stevekung.mods.indicatorutils.command;

import org.apache.commons.lang3.StringUtils;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandSlimeChunkSeed extends ClientCommandBaseIU
{
    @Override
    public String getName()
    {
        return "slimeseed";
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException
    {
        if (args.length < 1)
        {
            throw new WrongUsageException("commands.slimeseed.usage");
        }
        else
        {
            String seed = args[0];

            if (!StringUtils.isEmpty(seed))
            {
                try
                {
                    long longSeed = Long.parseLong(seed);

                    if (longSeed != 0L)
                    {
                        ExtendedModSettings.SLIME_CHUNK_SEED = longSeed;
                        sender.sendMessage(new JsonUtils().text("Set slime chunk seed to " + longSeed));
                    }
                }
                catch (NumberFormatException e)
                {
                    ExtendedModSettings.SLIME_CHUNK_SEED = seed.hashCode();
                    sender.sendMessage(new JsonUtils().text("Set slime chunk seed to " + seed.hashCode()));
                }
            }
            ExtendedModSettings.saveExtendedSettings();
        }
    }
}