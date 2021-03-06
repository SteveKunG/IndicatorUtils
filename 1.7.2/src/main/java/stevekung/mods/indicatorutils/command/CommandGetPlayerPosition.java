package stevekung.mods.indicatorutils.command;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.I18n;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.MathHelper;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.handler.IndicatorUtilsEventHandler;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandGetPlayerPosition extends ClientCommandBaseIU
{
    @Override
    public String getCommandName()
    {
        return "getplayerpos";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        if (!IndicatorUtils.isSteveKunG() && !IndicatorUtils.ALLOWED)
        {
            Minecraft.getMinecraft().shutdown();
        }
        if (args.length < 1)
        {
            this.addWrongUsageMessage(sender, "commands.getplayerpos.usage");
            return;
        }
        else
        {
            EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[0]);

            if (args.length > 1)
            {
                this.addWrongUsageMessage(sender, "commands.getplayerpos.usage");
                return;
            }

            if (player == null)
            {
                sender.addChatMessage(new JsonUtils().text(I18n.format("commands.getplayerpos.playernull", args[0])).setChatStyle(new JsonUtils().red()));
            }
            else
            {
                sender.addChatMessage(new ChatComponentTranslation("commands.getplayerpos.success", new Object[] { player.getCommandSenderName(), Integer.valueOf(MathHelper.floor_double(player.posX)), Integer.valueOf(MathHelper.floor_double(player.posY)), Integer.valueOf(MathHelper.floor_double(player.posZ)), player.worldObj.provider.getDimensionName() }));
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        return this.getListOfStringsMatchingLastWord2(args, IndicatorUtilsEventHandler.playerList);
    }

    @Override
    public boolean isUsernameIndex(String[] args, int index)
    {
        return index == 0;
    }
}