/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.Collection;
import java.util.List;

import com.google.common.base.Functions;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.ForgeHooks;

public abstract class ClientCommandBaseIU extends CommandBase
{
    @Override
    public String getCommandUsage(ICommandSender sender)
    {
        return "/" + this.getCommandName();
    }

    @Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }

    protected IChatComponent getChatComponentFromNthArg(String[] args, int index)
    {
        IChatComponent itextcomponent = new ChatComponentText("");

        for (int i = index; i < args.length; ++i)
        {
            if (i > index)
            {
                itextcomponent.appendText(" ");
            }
            IChatComponent itextcomponent1 = ForgeHooks.newChatWithLinks(args[i]);
            itextcomponent.appendSibling(itextcomponent1);
        }
        return itextcomponent;
    }

    protected List<String> getListOfStringsMatchingLastWord2(String[] stringList, Collection<?> collection)
    {
        String s = stringList[stringList.length - 1];
        List<String> list = Lists.<String>newArrayList();

        if (!collection.isEmpty())
        {
            for (String s1 : Iterables.transform(collection, Functions.toStringFunction()))
            {
                if (doesStringStartWith(s, s1))
                {
                    list.add(s1);
                }
            }
            if (list.isEmpty())
            {
                for (Object object : collection)
                {
                    if (object instanceof ResourceLocation && doesStringStartWith(s, ((ResourceLocation)object).getResourcePath()))
                    {
                        list.add(String.valueOf(object));
                    }
                }
            }
        }
        return list;
    }
}