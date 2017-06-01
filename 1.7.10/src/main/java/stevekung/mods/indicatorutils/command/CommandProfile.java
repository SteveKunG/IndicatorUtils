/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.command;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.EnumChatFormatting;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.profile.ProfileConfigData;
import stevekung.mods.indicatorutils.profile.ProfileData;
import stevekung.mods.indicatorutils.profile.ProfileData.ProfileSettingData;
import stevekung.mods.indicatorutils.profile.ProfileSettings;
import stevekung.mods.indicatorutils.utils.JsonUtils;

public class CommandProfile extends ClientCommandBaseIU
{
    @Override
    public String getCommandName()
    {
        return "profileiu";
    }

    @Override
    public void processCommand(ICommandSender sender, String[] args) throws CommandException
    {
        JsonUtils json = new JsonUtils();
        ProfileConfigData configData = new ProfileConfigData();

        if (args.length < 1)
        {
            throw new WrongUsageException("commands.profileiu.usage");
        }
        else
        {
            if ("add".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.profileiu.add.usage");
                }
                if (ProfileSettings.profileData.getProfile(args[1]) != null)
                {
                    sender.addChatMessage(json.text("Profile data was already set for name: " + args[1] + "!").setChatStyle(json.red()));
                    return;
                }
                ProfileSettings.profileData.addProfileData(args[1], ConfigManager.enablePing, ConfigManager.enableServerIP, ConfigManager.enableFPS, ConfigManager.enableXYZ, ConfigManager.enableLookingAtBlock,
                        ConfigManager.enableDirection, ConfigManager.enableBiome, ConfigManager.enableArmorStatus, ConfigManager.enablePotionStatus, ConfigManager.enableKeystroke, ConfigManager.enableCPS, ConfigManager.enableHeldItemInHand,
                        ConfigManager.armorStatusMode, ConfigManager.heldItemStatusMode, ConfigManager.armorStatusPosition, ConfigManager.potionStatusPosition, ConfigManager.keystrokePosition, ExtendedModSettings.ARMOR_STATUS_OFFSET,
                        ExtendedModSettings.POTION_STATUS_OFFSET, ExtendedModSettings.KEYSTROKE_Y_OFFSET, ConfigManager.enableCurrentTime, ConfigManager.enableGameTime, ConfigManager.enableMoonPhase, ConfigManager.enableWeatherStatus,
                        ConfigManager.enableSlimeChunkFinder);
                sender.addChatMessage(json.text("Add profile data name: " + args[1]));
                ProfileSettings.saveProfileSettings();
            }
            else if ("load".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.profileiu.load.usage");
                }
                if (ProfileSettings.profileData.getProfileList().isEmpty())
                {
                    sender.addChatMessage(json.text("Cannot load profile data, Empty profile data file"));
                    return;
                }

                for (ProfileData.ProfileSettingData data : ProfileSettings.profileData.getProfileList())
                {
                    if (ProfileSettings.profileData.getProfile(args[1]) != null)
                    {
                        if (args[1].equals(data.getProfileName()))
                        {
                            configData.load(data);
                            ConfigManager.config.save();
                            ExtendedModSettings.saveExtendedSettings();
                            sender.addChatMessage(json.text("Load profile data for name: " + args[1]));
                        }
                    }
                    else
                    {
                        sender.addChatMessage(json.text("Cannot load profile data from: " + args[1]).setChatStyle(json.red()));
                        return;
                    }
                }
            }
            else if ("save".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.profileiu.save.usage");
                }
                for (ProfileData.ProfileSettingData data : ProfileSettings.profileData.getProfileList())
                {
                    if (ProfileSettings.profileData.getProfile(args[1]) == null)
                    {
                        sender.addChatMessage(json.text("Cannot save profile data to: " + args[1]).setChatStyle(json.red()));
                        return;
                    }
                    if (args[1].equals(data.getProfileName()))
                    {
                        ProfileSettings.profileData.saveProfileData(args[1], ConfigManager.enablePing, ConfigManager.enableServerIP, ConfigManager.enableFPS, ConfigManager.enableXYZ, ConfigManager.enableLookingAtBlock,
                                ConfigManager.enableDirection, ConfigManager.enableBiome, ConfigManager.enableArmorStatus, ConfigManager.enablePotionStatus, ConfigManager.enableKeystroke, ConfigManager.enableCPS, ConfigManager.enableHeldItemInHand,
                                ConfigManager.armorStatusMode, ConfigManager.heldItemStatusMode, ConfigManager.armorStatusPosition, ConfigManager.potionStatusPosition, ConfigManager.keystrokePosition, ExtendedModSettings.ARMOR_STATUS_OFFSET,
                                ExtendedModSettings.POTION_STATUS_OFFSET, ExtendedModSettings.KEYSTROKE_Y_OFFSET, ConfigManager.enableCurrentTime, ConfigManager.enableGameTime, ConfigManager.enableMoonPhase, ConfigManager.enableWeatherStatus,
                                ConfigManager.enableSlimeChunkFinder);
                        ProfileSettings.saveProfileSettings();
                        sender.addChatMessage(json.text("Save profile data for name: " + args[1]));
                    }
                }
            }
            else if ("remove".equalsIgnoreCase(args[0]))
            {
                if (args.length < 2)
                {
                    throw new WrongUsageException("commands.profileiu.remove.usage");
                }
                if (ProfileSettings.profileData.getProfile(args[1]) != null)
                {
                    ProfileSettings.profileData.removeProfile(args[1]);
                    sender.addChatMessage(json.text("Remove profile data for name: " + args[1]));
                }
                else
                {
                    sender.addChatMessage(json.text("Cannot remove or find profile data from: " + args[1]).setChatStyle(json.red()));
                }
            }
            else if ("list".equalsIgnoreCase(args[0]))
            {
                Collection<ProfileSettingData> collection = ProfileSettings.profileData.getProfileList();

                if (collection.isEmpty())
                {
                    throw new CommandException("commands.profileiu.list.empty");
                }
                else
                {
                    ChatComponentTranslation textcomponenttranslation = new ChatComponentTranslation("commands.profileiu.list.count", new Object[] {Integer.valueOf(collection.size())});
                    textcomponenttranslation.getChatStyle().setColor(EnumChatFormatting.DARK_GREEN);
                    sender.addChatMessage(textcomponenttranslation);

                    for (ProfileData.ProfileSettingData data : collection)
                    {
                        sender.addChatMessage(new ChatComponentTranslation("commands.profileiu.list.entry", new Object[] {data.getProfileName()}));
                    }
                }
            }
            else
            {
                throw new WrongUsageException("commands.profileiu.usage");
            }
        }
    }

    @Override
    public List<String> addTabCompletionOptions(ICommandSender sender, String[] args)
    {
        if (args.length == 1)
        {
            return CommandBase.getListOfStringsMatchingLastWord(args, "add", "load", "save", "remove", "list");
        }
        if (args.length == 2)
        {
            if ("load".equalsIgnoreCase(args[0]) || "remove".equalsIgnoreCase(args[0]) || "save".equalsIgnoreCase(args[0]))
            {
                Collection<ProfileSettingData> collection = ProfileSettings.profileData.getProfileList();
                List<String> list = Lists.newArrayList();

                for (ProfileData.ProfileSettingData data : collection)
                {
                    list.add(data.getProfileName());
                }
                return this.getListOfStringsMatchingLastWord2(args, list);
            }
        }
        return super.addTabCompletionOptions(sender, args);
    }
}