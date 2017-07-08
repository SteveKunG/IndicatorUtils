package stevekung.mods.indicatorutils.handler;

import java.net.URI;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.authlib.GameProfile;

import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.*;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.network.NetworkPlayerInfo;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderLivingBase;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityArmorStand;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.item.ItemStack;
import net.minecraft.scoreboard.ScoreObjective;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.util.text.event.ClickEvent;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.*;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.DrawScreenEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ClientTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.RenderTickEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import stevekung.mods.indicatorutils.IndicatorUtils;
import stevekung.mods.indicatorutils.config.ConfigGuiFactory;
import stevekung.mods.indicatorutils.config.ConfigManager;
import stevekung.mods.indicatorutils.config.ExtendedModSettings;
import stevekung.mods.indicatorutils.gui.*;
import stevekung.mods.indicatorutils.helper.ClientRendererHelper;
import stevekung.mods.indicatorutils.helper.GameInfoHelper;
import stevekung.mods.indicatorutils.helper.StatusRendererHelper;
import stevekung.mods.indicatorutils.keybinding.KeyBindingHandler;
import stevekung.mods.indicatorutils.renderer.HUDInfo;
import stevekung.mods.indicatorutils.renderer.KeystrokeRenderer;
import stevekung.mods.indicatorutils.renderer.LayerCapeMOD;
import stevekung.mods.indicatorutils.utils.*;
import stevekung.mods.indicatorutils.window.WindowGameXYZ;

public class IndicatorUtilsEventHandler
{
    public static boolean AFK_ENABLED;
    public static String AFK_MODE = "idle";
    public static String AFK_REASON;
    public static int AFK_MOVE_TICK;
    public static int AFK_TICK;

    public static boolean REC_ENABLED;
    private int recTick;

    public static boolean AUTO_FISH_ENABLED;
    public static int AUTO_FISH_TICK;

    public static List<Long> L_CLICK = Lists.newArrayList();
    public static List<Long> R_CLICK = Lists.newArrayList();

    private int pressTime;
    private int pressOneTimeTick;
    private int pressTimeDelay;
    private int clearChatTick;
    private int closeScreen;

    private Minecraft mc;
    private JsonUtils json;

    private GuiPlayerTabOverlayIU overlayPlayerList;
    private GuiBossOverlayIU overlayBoss;

    private long sneakTimeOld = 0L;
    private boolean sneakingOld = false;

    public static Map<String, Integer> PLAYER_PING_MAP = Maps.newHashMap();
    private static boolean windowStartup = true;

    public IndicatorUtilsEventHandler()
    {
        this.mc = Minecraft.getMinecraft();
        this.json = new JsonUtils();
        this.overlayBoss = new GuiBossOverlayIU(this.mc);
    }

    // Credit to Jarbelar
    // 0 = ShowDesc, 1 = NoConnection
    @SubscribeEvent
    public void onCheckVersion(PlayerTickEvent event)
    {
        String changeLog = "http://pastebin.com/rJ7He59c";

        if (event.player.world.isRemote)
        {
            if (ConfigManager.enableVersionChecker)
            {
                if (!IndicatorUtils.STATUS_CHECK[1] && VersionChecker.INSTANCE.noConnection())
                {
                    event.player.sendMessage(this.json.text("Unable to check latest version, Please check your internet connection").setStyle(this.json.red()));
                    event.player.sendMessage(this.json.text(VersionChecker.INSTANCE.getExceptionMessage()).setStyle(this.json.red()));
                    IndicatorUtils.STATUS_CHECK[1] = true;
                    return;
                }
                if (!IndicatorUtils.STATUS_CHECK[0] && !IndicatorUtils.STATUS_CHECK[1])
                {
                    for (String log : VersionChecker.INSTANCE.getChangeLog())
                    {
                        if (ConfigManager.showChangeLogInGame)
                        {
                            event.player.sendMessage(this.json.text(log).setStyle(this.json.style().setColor(TextFormatting.GRAY).setClickEvent(this.json.click(ClickEvent.Action.OPEN_URL, changeLog))));
                        }
                    }
                    IndicatorUtils.STATUS_CHECK[0] = true;
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientConnectedToServer(ClientConnectedToServerEvent event)
    {
        this.mc.ingameGUI.persistantChatGUI = new GuiNewChatFast(this.mc);
        this.overlayPlayerList = new GuiPlayerTabOverlayIU(this.mc, this.mc.ingameGUI);
    }

    @SubscribeEvent
    public void onGuiOpen(GuiOpenEvent event)
    {
        if (ConfigManager.enableCustomCapeFeature)
        {
            RenderPlayer renderDefault = this.mc.getRenderManager().getSkinMap().get("default");
            RenderPlayer renderSlim = this.mc.getRenderManager().getSkinMap().get("slim");
            renderDefault.addLayer(new LayerCapeMOD(renderDefault));
            renderSlim.addLayer(new LayerCapeMOD(renderSlim));
        }
    }

    @SubscribeEvent
    public void onInitGui(InitGuiEvent.Post event)
    {
        if (event.getGui() instanceof GuiIngameMenu)
        {
            event.getButtonList().add(new GuiButton(200, event.getGui().width - 145, 20, 135, 20, "Paypal"));
            event.getButtonList().add(new GuiButton(201, event.getGui().width - 145, 41, 135, 20, "Truemoney"));
        }
    }

    @SubscribeEvent
    public void onActionGui(ActionPerformedEvent.Post event)
    {
        if (event.getGui() instanceof GuiIngameMenu)
        {
            switch (event.getButton().id)
            {
            case 200:
                this.openLink("https://twitch.streamlabs.com/stevekung");
                break;
            case 201:
                this.openLink("https://tipme.in.th/stevekung");
                break;
            }
        }
    }

    @SubscribeEvent
    public void onRenderGui(DrawScreenEvent.Post event)
    {
        if (event.getGui() instanceof GuiIngameMenu)
        {
            event.getGui().drawString(this.mc.fontRendererObj, "Donate to Indicator Utils", event.getGui().width - 140, 8, 65481);
        }
    }

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent event)
    {
        if (event.getModID().equalsIgnoreCase(IndicatorUtils.MOD_ID))
        {
            ConfigManager.syncConfig(false);
        }
    }

    @SubscribeEvent
    public void onReceivedChat(ClientChatReceivedEvent event)
    {
        String dailyText = "Click the link to visit our website and claim your reward: ";
        String votingText1 = "Today's voting link is ";
        String votingText2 = "! Follow the instructions on the website to redeem 5,000 XP and 3,000 Arcade Coins!";
        String unformattedText = event.getMessage().getUnformattedText();

        if (GameInfoHelper.INSTANCE.isHypixel())
        {
            Pattern nickPattern = Pattern.compile("^You are now nicked as (?<nick>\\w+)!");
            Matcher nickMatcher = nickPattern.matcher(unformattedText);

            if (event.getType() == 0)
            {
                if (nickMatcher.matches())
                {
                    ExtendedModSettings.HYPIXEL_NICK_NAME = nickMatcher.group("nick");
                    ExtendedModSettings.saveExtendedSettings();
                }
                if (IndicatorUtils.isSteveKunG())
                {
                    if (unformattedText.contains(dailyText))
                    {
                        String replacedText = unformattedText.replace(dailyText, "").replace("\n", "");
                        this.openLink(replacedText);
                    }
                    if (unformattedText.contains(votingText1))
                    {
                        String replacedText = unformattedText.replace(votingText1, "");

                        for (int i = 0; i < 10; i++)
                        {
                            replacedText = replacedText.replace("\u00a7" + i, "");
                        }
                        replacedText = replacedText.replace("\u00a7" + "a", "").replace("\u00a7" + "b", "").replace("\u00a7" + "c", "").replace("\u00a7" + "d", "").replace("\u00a7" + "e", "").replace("\u00a7" + "f", "");
                        replacedText = replacedText.replace(votingText2, "");

                        if (replacedText.contains("vote.hypixel.net/0"))
                        {
                            this.openLink("http://minecraftservers.org/vote/221843");
                        }
                        if (replacedText.contains("vote.hypixel.net/1"))
                        {
                            this.openLink("http://minecraft-server-list.com/server/292028/vote/");
                        }
                        this.closeScreen = 20;
                    }
                }
                if (unformattedText.contains("isn't online!"))
                {
                    List<String> words = Arrays.asList(unformattedText.split("[ ]"));
                    Collections.reverse(words);
                    StringBuilder reverseString = new StringBuilder();

                    for (String word : words)
                    {
                        reverseString.append(word + " ");
                    }

                    reverseString.substring(0, reverseString.length() - 1);
                    String message = reverseString.toString().replace("online! isn't ", "");
                    String[] name = message.trim().split("\\s+");
                    this.mc.player.sendChatMessage("/p remove " + name[0]);
                }
                if (unformattedText.contains("Get free coins by clicking"))
                {
                    this.mc.player.sendChatMessage("/tip all");
                }
            }
        }
    }

    @SubscribeEvent
    public void onClientTick(ClientTickEvent event)
    {
        this.replaceChatGUI();
        this.getPingForNullUUID();
        this.initWindow();
        ClientRendererHelper.runGlowingEntityDetector();

        if (event.phase == Phase.START)
        {
            this.runAFK();
            this.runAutoFish();
            this.runAutoClearChat();

            if (this.pressTimeDelay > 0)
            {
                --this.pressTimeDelay;
            }
            if (this.pressOneTimeTick > 0)
            {
                --this.pressOneTimeTick;
            }
            if (this.pressOneTimeTick == 0)
            {
                this.pressTime = 0;
            }
            if (this.closeScreen > 1)
            {
                --this.closeScreen;
            }

            if (this.closeScreen == 1)
            {
                this.mc.displayGuiScreen((GuiScreen)null);
                this.closeScreen = 0;
            }
            if (IndicatorUtilsEventHandler.REC_ENABLED)
            {
                ++this.recTick;
            }
            else
            {
                this.recTick = 0;
            }
        }
        if (this.mc.player != null && !(this.mc.player.movementInput instanceof MovementInputFromOptionsIU))
        {
            this.mc.player.movementInput = new MovementInputFromOptionsIU(this.mc.gameSettings);
        }
        GuiIngameForge.renderBossHealth = ConfigManager.renderBossHealthBar;
        GuiIngameForge.renderObjective = ConfigManager.renderScoreboard;
    }

    @SubscribeEvent
    public void onPlayerLoggedOut(PlayerLoggedOutEvent event)
    {
        this.stopCommandTick();
    }

    @SubscribeEvent
    public void onPlayerRespawn(PlayerRespawnEvent event)
    {
        this.stopCommandTick();
    }

    @SubscribeEvent
    public void onDisconnectedFromServerEvent(ClientDisconnectionFromServerEvent event)
    {
        this.stopCommandTick();
        IndicatorUtilsEventHandler.PLAYER_PING_MAP.clear();
        this.closeScreen = 0;
    }

    @SubscribeEvent
    public void onMouseClick(MouseEvent event)
    {
        if (event.getButton() == 0 && event.isButtonstate())
        {
            IndicatorUtilsEventHandler.L_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
        if (event.getButton() == 1 && event.isButtonstate())
        {
            IndicatorUtilsEventHandler.R_CLICK.add(Long.valueOf(System.currentTimeMillis()));
        }
    }

    @SubscribeEvent
    public void onPreRender(RenderGameOverlayEvent.Pre event)
    {
        if (event.getType() == ElementType.PLAYER_LIST)
        {
            if (ConfigManager.playerPingMode.equalsIgnoreCase("number"))
            {
                event.setCanceled(true);
                ScoreObjective scoreobjective = this.mc.world.getScoreboard().getObjectiveInDisplaySlot(0);
                NetHandlerPlayClient handler = this.mc.player.connection;

                if (this.mc.gameSettings.keyBindPlayerList.isKeyDown() && (!this.mc.isIntegratedServerRunning() || handler.getPlayerInfoMap().size() > 1 || scoreobjective != null))
                {
                    this.overlayPlayerList.updatePlayerList(true);
                    this.overlayPlayerList.renderPlayerlist(event.getResolution().getScaledWidth(), this.mc.world.getScoreboard(), scoreobjective);
                }
                else
                {
                    this.overlayPlayerList.updatePlayerList(false);
                }
            }
        }
        if (event.getType() == ElementType.CHAT)
        {
            event.setCanceled(true);
            GlStateManager.pushMatrix();
            GlStateManager.translate(0, event.getResolution().getScaledHeight() - 48, 0.0F);
            GlStateManager.disableDepth();
            this.mc.ingameGUI.getChatGUI().drawChat(this.mc.ingameGUI.getUpdateCounter());
            GlStateManager.enableDepth();
            GlStateManager.popMatrix();
        }
        if (event.getType() == ElementType.POTION_ICONS)
        {
            if (!ConfigManager.renderIngamePotionEffect)
            {
                event.setCanceled(true);
            }
        }
        if (event.getType() == ElementType.BOSSHEALTH)
        {
            event.setCanceled(true);
            this.mc.getTextureManager().bindTexture(Gui.ICONS);
            GlStateManager.tryBlendFuncSeparate(GlStateManager.SourceFactor.SRC_ALPHA, GlStateManager.DestFactor.ONE_MINUS_SRC_ALPHA, GlStateManager.SourceFactor.ONE, GlStateManager.DestFactor.ZERO);
            GlStateManager.enableBlend();
            this.overlayBoss.renderBossHealth();
            GlStateManager.disableBlend();
        }
    }

    @SubscribeEvent
    public void onRenderOverlay(RenderGameOverlayEvent event)
    {
        if (event.getType() == ElementType.TEXT)
        {
            if (ConfigManager.enableAllRenderInfo)
            {
                HUDInfo.init(this.mc);

                if (ConfigManager.enableCPS)
                {
                    String cps = this.json.text("CPS: ").setStyle(this.json.colorFromConfig(ConfigManager.customColorCPS)).getFormattedText();
                    String rcps = ConfigManager.enableRCPS ? this.json.text(" RCPS: ").setStyle(this.json.colorFromConfig(ConfigManager.customColorRCPS)).getFormattedText() : "";
                    String cpsValue = this.json.text(String.valueOf(GameInfoHelper.INSTANCE.getCPS())).setStyle(this.json.colorFromConfig(ConfigManager.customColorCPSValue)).getFormattedText();
                    String rcpsValue = ConfigManager.enableRCPS ? this.json.text(String.valueOf(GameInfoHelper.INSTANCE.getRCPS())).setStyle(this.json.colorFromConfig(ConfigManager.customColorRCPSValue)).getFormattedText() : "";

                    if (ConfigManager.useCustomTextCPS)
                    {
                        cps = JsonUtils.rawTextToJson(ConfigManager.customTextCPS).getFormattedText();
                    }
                    if (ConfigManager.useCustomTextRCPS)
                    {
                        rcps = JsonUtils.rawTextToJson(ConfigManager.customTextRCPS).getFormattedText();
                    }
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("record"))
                    {
                        ClientRendererHelper.drawStringAtRecord(cps + cpsValue + rcps + rcpsValue, event.getPartialTicks());
                    }
                    if (ExtendedModSettings.CPS_POSITION.equalsIgnoreCase("custom"))
                    {
                        ClientRendererHelper.drawRectNew(ExtendedModSettings.CPS_X_OFFSET, ExtendedModSettings.CPS_Y_OFFSET, ExtendedModSettings.CPS_X_OFFSET + this.mc.fontRendererObj.getStringWidth(cps + cpsValue + rcps + rcpsValue) + 4, ExtendedModSettings.CPS_Y_OFFSET + 11, 16777216, ExtendedModSettings.CPS_OPACITY);
                        ClientRendererHelper.drawString(cps + cpsValue + rcps + rcpsValue, ExtendedModSettings.CPS_X_OFFSET + 2, ExtendedModSettings.CPS_Y_OFFSET + 2, 16777215, true);
                    }
                }

                if (IndicatorUtilsEventHandler.REC_ENABLED)
                {
                    ScaledResolution res = new ScaledResolution(this.mc);
                    EnumTextColor color = EnumTextColor.WHITE;

                    if (this.recTick % 24 >= 0 && this.recTick % 24 <= 12)
                    {
                        color = EnumTextColor.RED;
                    }
                    ClientRendererHelper.drawString("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick), res.getScaledWidth() - this.mc.fontRendererObj.getStringWidth("REC: " + GameInfoHelper.INSTANCE.ticksToElapsedTime(this.recTick)) - 2, res.getScaledHeight() - 10, color, true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onPressKey(KeyInputEvent event)
    {
        if (this.mc.currentScreen == null && this.mc.gameSettings.keyBindCommand.isPressed())
        {
            this.mc.displayGuiScreen(new GuiNewChatIU("/"));
        }
        if (ConfigManager.enableCustomCapeFeature)
        {
            if (KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI != null && KeyBindingHandler.KEY_OPEN_CAPE_DOWNLOADER_GUI.isKeyDown())
            {
                this.mc.displayGuiScreen(new GuiCapeDownloader());
            }
        }
        if (ConfigManager.enableClearChatRecentSentMessage)
        {
            if (Keyboard.isKeyDown(Keyboard.KEY_F3) && Keyboard.isKeyDown(Keyboard.KEY_D))
            {
                if (this.mc.ingameGUI != null)
                {
                    this.mc.ingameGUI.getChatGUI().clearChatMessages(true);
                }
            }
        }
        if (KeyBindingHandler.KEY_REC_COMMAND.isKeyDown())
        {
            if (IndicatorUtilsEventHandler.REC_ENABLED)
            {
                IndicatorUtilsEventHandler.REC_ENABLED = false;
            }
            else
            {
                IndicatorUtilsEventHandler.REC_ENABLED = true;
            }
        }
        if (ExtendedModSettings.TOGGLE_SPRINT_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            if (KeyBindingHandler.KEY_TOGGLE_SPRINT.isKeyDown())
            {
                if (ExtendedModSettings.TOGGLE_SPRINT)
                {
                    ExtendedModSettings.TOGGLE_SPRINT = false;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Toggle Sprint Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SPRINT = true;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Toggle Sprint Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ExtendedModSettings.TOGGLE_SNEAK_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            if (KeyBindingHandler.KEY_TOGGLE_SNEAK.isKeyDown())
            {
                if (ExtendedModSettings.TOGGLE_SNEAK)
                {
                    ExtendedModSettings.TOGGLE_SNEAK = false;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Toggle Sneak Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.TOGGLE_SNEAK = true;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Toggle Sneak Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ExtendedModSettings.AUTO_SWIM_USE_MODE.equalsIgnoreCase("keybinding"))
        {
            if (KeyBindingHandler.KEY_AUTO_SWIM.isKeyDown())
            {
                if (ExtendedModSettings.AUTO_SWIM)
                {
                    ExtendedModSettings.AUTO_SWIM = false;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Auto Swim Disabled").getFormattedText(), false);
                }
                else
                {
                    ExtendedModSettings.AUTO_SWIM = true;
                    GameInfoHelper.INSTANCE.setActionBarMessage(this.json.text("Auto Swim Enabled").getFormattedText(), false);
                }
                ExtendedModSettings.saveExtendedSettings();
            }
        }
        if (ConfigManager.enableEndGameChatMessage)
        {
            if (KeyBindingHandler.KEY_END_GAME_MESSAGE.isKeyDown())
            {
                if (this.pressTime <= 2 && this.pressTimeDelay <= 0 && this.pressOneTimeTick >= 0)
                {
                    ++this.pressTime;
                }
                if (this.pressTimeDelay <= 0)
                {
                    this.pressOneTimeTick = 10;
                }
            }
            if (this.pressTime >= 2)
            {
                this.pressTime = 0;
                this.pressTimeDelay = 200;
                this.mc.player.sendChatMessage(ConfigManager.endGameChatMessage);
            }
        }
    }

    @SubscribeEvent
    public void onRenderHealthStatus(RenderLivingEvent.Specials.Post<EntityLivingBase> event)
    {
        EntityLivingBase entity = event.getEntity();
        float health = entity.getHealth() + entity.getAbsorptionAmount();
        boolean halfHealth = health <= entity.getMaxHealth() / 2F;
        boolean halfHealth1 = health <= entity.getMaxHealth() / 4F;
        float range = entity.isSneaking() ? RenderLivingBase.NAME_TAG_RANGE_SNEAK : RenderLivingBase.NAME_TAG_RANGE;
        double distance = entity.getDistanceSqToEntity(this.mc.getRenderViewEntity());
        String status = ConfigManager.healthStatusMode;
        boolean flag = true;
        String color = "green";

        if (halfHealth)
        {
            color = "red";
        }
        if (halfHealth1)
        {
            color = "dark_red";
        }

        if (status.equalsIgnoreCase("DISABLE"))
        {
            flag = false;
        }
        else if (status.equalsIgnoreCase("POINTED"))
        {
            flag = entity == this.mc.pointedEntity;
        }

        if (distance < range * range)
        {
            if (!this.mc.gameSettings.hideGUI && !entity.isInvisible() && flag && !(entity instanceof EntityPlayerSP || entity instanceof EntityArmorStand))
            {
                String heart = this.json.text("\u2764 ").setStyle(this.json.colorFromConfig(color)).getFormattedText();
                StatusRendererHelper.renderHealthStatus(entity, heart + String.format("%.1f", health), event.getX(), event.getY(), event.getZ(), entity.getDistanceSqToEntity(this.mc.getRenderViewEntity()));
            }
        }
    }

    @SubscribeEvent
    public void onRenderTick(RenderTickEvent event)
    {
        if (event.phase == Phase.START)
        {
            if (ConfigManager.enableOldSneakFeature)
            {
                if (this.mc.player != null)
                {
                    this.mc.player.eyeHeight = this.getOldEyeHeight(this.mc.player);
                }
            }
        }
        if (event.phase == Phase.END)
        {
            if (ConfigManager.enableKeystroke)
            {
                if (!this.mc.gameSettings.hideGUI && !this.mc.gameSettings.showDebugInfo)
                {
                    if (this.mc.currentScreen == null || this.mc.currentScreen instanceof GuiChat || this.mc.currentScreen instanceof GuiRenderStatusSettings || this.mc.currentScreen instanceof GuiKeystrokeColorSettings)
                    {
                        KeystrokeRenderer.init(this.mc);
                    }
                }
            }
            if (this.mc.currentScreen instanceof GuiIngameMenu)
            {
                int i = Mouse.getEventX() * this.mc.currentScreen.width / this.mc.displayWidth;
                int j = this.mc.currentScreen.height - Mouse.getEventY() * this.mc.currentScreen.height / this.mc.displayHeight - 1;
                int k = Mouse.getEventButton();
                int deltaColor = 0;
                boolean galacticraft = Loader.isModLoaded("galacticraftcore");
                float defaultval = galacticraft ? -35.0F : 0.0F;
                boolean height = galacticraft ? j > this.mc.currentScreen.height - 70 && j < this.mc.currentScreen.height - 35 : j > this.mc.currentScreen.height - 35;

                if (Minecraft.IS_RUNNING_ON_MAC && k == 0 && (Keyboard.isKeyDown(29) || Keyboard.isKeyDown(157)))
                {
                    k = 1;
                }
                if (i > this.mc.currentScreen.width - 101 && height)
                {
                    deltaColor = 50;

                    if (k == 0)
                    {
                        if (Mouse.getEventButtonState())
                        {
                            this.mc.displayGuiScreen(new ConfigGuiFactory.ConfigGUI(this.mc.currentScreen));
                            this.mc.getSoundHandler().playSound(PositionedSoundRecord.getMasterRecord(SoundEvents.UI_BUTTON_CLICK, 1.0F));
                        }
                    }
                }
                GlStateManager.translate(0.0F, defaultval, 0.0F);
                ClientRendererHelper.drawGradientRect(this.mc.currentScreen.width - 100, this.mc.currentScreen.height - 35, this.mc.currentScreen.width, this.mc.currentScreen.height, ClientRendererHelper.to32BitColor(150, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor), ClientRendererHelper.to32BitColor(250, 10 + deltaColor, 10 + deltaColor, 10 + deltaColor));
                this.mc.fontRendererObj.drawString(I18n.format("gui.indicatorutils.config.name0"), this.mc.currentScreen.width - 50 - this.mc.fontRendererObj.getStringWidth(I18n.format("gui.indicatorutils.config.name0")) / 2, this.mc.currentScreen.height - 26, ClientRendererHelper.to32BitColor(255, 240, 240, 240));
                this.mc.fontRendererObj.drawString(I18n.format("gui.indicatorutils.config.name1"), this.mc.currentScreen.width - 50 - this.mc.fontRendererObj.getStringWidth(I18n.format("gui.indicatorutils.config.name1")) / 2, this.mc.currentScreen.height - 16, ClientRendererHelper.to32BitColor(255, 240, 240, 240));
                Gui.drawRect(this.mc.currentScreen.width - 100, this.mc.currentScreen.height - 35, this.mc.currentScreen.width - 99, this.mc.currentScreen.height, ClientRendererHelper.to32BitColor(255, 0, 0, 0));
                Gui.drawRect(this.mc.currentScreen.width - 100, this.mc.currentScreen.height - 35, this.mc.currentScreen.width, this.mc.currentScreen.height - 34, ClientRendererHelper.to32BitColor(255, 0, 0, 0));
            }
        }
    }

    private void runAutoFish()
    {
        if (IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
        {
            ++IndicatorUtilsEventHandler.AUTO_FISH_TICK;
            IndicatorUtilsEventHandler.AUTO_FISH_TICK %= 4;

            if (this.mc.player != null && this.mc.objectMouseOver != null && this.mc.world != null && this.mc.playerController != null && this.mc.entityRenderer != null)
            {
                if (IndicatorUtilsEventHandler.AUTO_FISH_TICK % 4 == 0)
                {
                    for (EnumHand enumhand : EnumHand.values())
                    {
                        ItemStack itemstack = this.mc.player.getHeldItem(enumhand);

                        if (itemstack.getItem() == Items.FISHING_ROD)
                        {
                            if (this.mc.objectMouseOver.typeOfHit == RayTraceResult.Type.BLOCK)
                            {
                                BlockPos blockpos = this.mc.objectMouseOver.getBlockPos();

                                if (this.mc.world.getBlockState(blockpos).getMaterial() != Material.AIR)
                                {
                                    int i = itemstack.getCount();
                                    EnumActionResult enumactionresult = this.mc.playerController.processRightClickBlock(this.mc.player, this.mc.world, blockpos, this.mc.objectMouseOver.sideHit, this.mc.objectMouseOver.hitVec, enumhand);

                                    if (enumactionresult == EnumActionResult.SUCCESS)
                                    {
                                        this.mc.player.swingArm(enumhand);

                                        if (!itemstack.isEmpty() && (itemstack.getCount() != i || this.mc.playerController.isInCreativeMode()))
                                        {
                                            this.mc.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                        }
                                        return;
                                    }
                                }
                            }
                            if (!itemstack.isEmpty() && this.mc.playerController.processRightClick(this.mc.player, this.mc.world, enumhand) == EnumActionResult.SUCCESS)
                            {
                                this.mc.entityRenderer.itemRenderer.resetEquippedProgress(enumhand);
                                return;
                            }
                        }
                        else
                        {
                            IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = false;
                            IndicatorUtilsEventHandler.AUTO_FISH_TICK = 0;
                            this.mc.player.sendMessage(this.json.text("Stop using /autofish command, you must hold the fishing rod!"));
                            return;
                        }
                    }
                }
            }
        }
        else
        {
            IndicatorUtilsEventHandler.AUTO_FISH_TICK = 0;
        }
    }

    private void runAFK()
    {
        if (IndicatorUtilsEventHandler.AFK_ENABLED)
        {
            ++IndicatorUtilsEventHandler.AFK_TICK;
            int tick = IndicatorUtilsEventHandler.AFK_TICK;
            int messageMin = 1200 * ConfigManager.afkMessageTime;
            String s = "s";

            if (tick == 0)
            {
                s = "";
            }
            if (ConfigManager.enableAFKMessage)
            {
                if (tick % messageMin == 0)
                {
                    if (this.mc.player != null)
                    {
                        String reason = IndicatorUtilsEventHandler.AFK_REASON;

                        if (reason.isEmpty())
                        {
                            reason = "";
                        }
                        else
                        {
                            reason = ", Reason : " + reason;
                        }
                        this.mc.player.sendChatMessage("AFK : " + GameInfoHelper.INSTANCE.ticksToElapsedTime(tick) + " minute" + s + reason);
                    }
                }
            }

            if (IndicatorUtilsEventHandler.AFK_MODE.equalsIgnoreCase("idle"))
            {
                if (this.mc.player != null)
                {
                    float angle = 0;

                    if (tick % 2 == 0)
                    {
                        angle = 0.0001F;
                    }
                    else
                    {
                        angle = -0.0001F;
                    }
                    this.mc.player.turn(angle, angle);
                }
            }
            else if (IndicatorUtilsEventHandler.AFK_MODE.equalsIgnoreCase("360"))
            {
                if (this.mc.player != null)
                {
                    this.mc.player.turn(5.0F, 0.0F);
                }
            }
            else if (IndicatorUtilsEventHandler.AFK_MODE.equalsIgnoreCase("360move"))
            {
                if (this.mc.player != null)
                {
                    this.mc.player.turn(5.0F, 0.0F);
                }
                ++IndicatorUtilsEventHandler.AFK_MOVE_TICK;
                IndicatorUtilsEventHandler.AFK_MOVE_TICK %= 8;
            }
            else
            {
                if (this.mc.player != null)
                {
                    float angle = 0;

                    if (tick % 2 == 0)
                    {
                        angle = 0.0001F;
                    }
                    else
                    {
                        angle = -0.0001F;
                    }
                    this.mc.player.turn(angle, angle);
                }
                ++IndicatorUtilsEventHandler.AFK_MOVE_TICK;
                IndicatorUtilsEventHandler.AFK_MOVE_TICK %= 8;
            }
        }
        else
        {
            IndicatorUtilsEventHandler.AFK_TICK = 0;
        }
    }

    private void runAutoClearChat()
    {
        if (ExtendedModSettings.AUTO_CLEAR_CHAT)
        {
            ++this.clearChatTick;
            int chatTick = this.clearChatTick / 20;

            if (chatTick % ExtendedModSettings.AUTO_CLEAR_CHAT_TIME == 0)
            {
                if (this.mc.ingameGUI != null)
                {
                    if (ExtendedModSettings.AUTO_CLEAR_CHAT_MODE.equalsIgnoreCase("onlychat"))
                    {
                        this.mc.ingameGUI.getChatGUI().clearChatMessages(false);
                    }
                    else if (ExtendedModSettings.AUTO_CLEAR_CHAT_MODE.equalsIgnoreCase("onlysentmessage"))
                    {
                        this.mc.ingameGUI.getChatGUI().getSentMessages().clear();
                    }
                    else
                    {
                        this.mc.ingameGUI.getChatGUI().clearChatMessages(true);
                    }
                }
            }
        }
        else
        {
            this.clearChatTick = 0;
        }
    }

    private void replaceChatGUI()
    {
        if (this.mc.currentScreen != null)
        {
            if (this.mc.currentScreen instanceof GuiChat && !(this.mc.currentScreen instanceof GuiNewChatIU || this.mc.currentScreen instanceof GuiSleepMP))
            {
                this.mc.displayGuiScreen(new GuiNewChatIU());
            }
            if (this.mc.currentScreen instanceof GuiSleepMP && !(this.mc.currentScreen instanceof GuiNewSleepMP))
            {
                this.mc.displayGuiScreen(new GuiNewSleepMP());
            }
            if (this.mc.currentScreen instanceof GuiNewSleepMP && !this.mc.player.isPlayerSleeping())
            {
                this.mc.displayGuiScreen((GuiScreen)null);
            }
        }
    }

    private float getOldEyeHeight(EntityPlayer player)
    {
        if (this.sneakingOld != player.isSneaking() || this.sneakTimeOld <= 0L)
        {
            this.sneakTimeOld = System.currentTimeMillis();
        }

        this.sneakingOld = player.isSneaking();
        float defaultEyeHeight = 1.62F;
        double sneakPress = 0.0004D;
        double sneakValue = 0.015D;
        int sneakTime = -50;
        long systemTime = 58L;

        if (player.isSneaking())
        {
            int sneakSystemTime = (int)(this.sneakTimeOld + systemTime - System.currentTimeMillis());

            if (sneakSystemTime > sneakTime)
            {
                defaultEyeHeight += (float)(sneakSystemTime * sneakPress);

                if (defaultEyeHeight < 0.0F || defaultEyeHeight > 10.0F)
                {
                    defaultEyeHeight = 1.54F;
                }
            }
            else
            {
                defaultEyeHeight = (float)(defaultEyeHeight - sneakValue);
            }
        }
        else
        {
            int sneakSystemTime = (int)(this.sneakTimeOld + systemTime - System.currentTimeMillis());

            if (sneakSystemTime > sneakTime)
            {
                defaultEyeHeight -= (float)(sneakSystemTime * sneakPress);
                defaultEyeHeight = (float)(defaultEyeHeight - sneakValue);

                if (defaultEyeHeight < 0.0F)
                {
                    defaultEyeHeight = 1.62F;
                }
            }
            else
            {
                defaultEyeHeight -= 0.0F;
            }
        }
        return defaultEyeHeight;
    }

    private void stopCommandTick()
    {
        if (IndicatorUtilsEventHandler.AFK_ENABLED)
        {
            IndicatorUtilsEventHandler.AFK_ENABLED = false;
            IndicatorUtilsEventHandler.AFK_REASON = "";
            IndicatorUtilsEventHandler.AFK_TICK = 0;
            IndicatorUtilsEventHandler.AFK_MOVE_TICK = 0;
            IndicatorUtilsEventHandler.AFK_MODE = "idle";
            IULog.info("Stopping AFK Command");
        }
        if (IndicatorUtilsEventHandler.AUTO_FISH_ENABLED)
        {
            IndicatorUtilsEventHandler.AUTO_FISH_ENABLED = false;
            IndicatorUtilsEventHandler.AUTO_FISH_TICK = 0;
            IULog.info("Stopping AutoFish Command");
        }
    }

    private void getPingForNullUUID()
    {
        if (this.mc.player != null)
        {
            NetHandlerPlayClient nethandlerplayclient = this.mc.player.connection;
            List<NetworkPlayerInfo> list = Lists.newArrayList(nethandlerplayclient.getPlayerInfoMap());
            int maxPlayers = list.size();

            for (int i = 0; i < maxPlayers; ++i)
            {
                if (i < list.size())
                {
                    NetworkPlayerInfo networkplayerinfo1 = list.get(i);
                    GameProfile gameprofile = networkplayerinfo1.getGameProfile();
                    IndicatorUtilsEventHandler.PLAYER_PING_MAP.put(gameprofile.getName(), networkplayerinfo1.getResponseTime());
                }
            }
        }
    }

    private void initWindow()
    {
        if (IndicatorUtilsEventHandler.windowStartup)
        {
            new WindowGameXYZ().setVisible(ConfigManager.enableXYZWindow);
            IndicatorUtilsEventHandler.windowStartup = false;
        }

        String windowText1 = "";
        String windowText2 = "";

        if (this.mc.getRenderViewEntity() != null)
        {
            BlockPos pos = new BlockPos(this.mc.getRenderViewEntity().posX, this.mc.getRenderViewEntity().getEntityBoundingBox().minY, this.mc.getRenderViewEntity().posZ);

            if (ConfigManager.enableOverworldCoordinate && this.mc.player.dimension == -1)
            {
                windowText1 = "Overworld " + "XYZ: " + pos.getX() * 8 + " " + pos.getY() + " " + pos.getZ() * 8;
            }
            String inNether = this.mc.player.dimension == -1 ? "Nether " : "";
            windowText2 = inNether + "XYZ: " + pos.getX() + " " + pos.getY() + " " + pos.getZ();
            WindowGameXYZ.label.setText("<html>" + "<div style='text-align: center;'>" + windowText1 + "<br>" + windowText2 + "</div>" + "</html>");
        }
        else
        {
            WindowGameXYZ.label.setText("<html>" + "<div style='text-align: center;'>" + "<br>" + "Unknown" + "</div>" + "</html>");
        }
    }

    private void openLink(String url)
    {
        try
        {
            URI uri = new URI(url);
            Class oclass = Class.forName("java.awt.Desktop");
            Object object = oclass.getMethod("getDesktop").invoke((Object)null);
            oclass.getMethod("browse", new Class[] {URI.class}).invoke(object, new Object[] {uri});
        }
        catch (Throwable throwable)
        {
            throwable.printStackTrace();
        }
    }
}