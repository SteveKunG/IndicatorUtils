package stevekung.mods.indicatorutils.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.BossInfoClient;
import net.minecraft.client.gui.GuiBossOverlay;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.BossInfo;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import stevekung.mods.indicatorutils.config.ConfigManager;

public class GuiBossOverlayIU extends GuiBossOverlay
{
    private ResourceLocation GUI_BARS_TEXTURES = new ResourceLocation("textures/gui/bars.png");
    private Minecraft mc;

    public GuiBossOverlayIU(Minecraft mc)
    {
        super(mc);
        this.mc = mc;
    }

    @Override
    public void renderBossHealth()
    {
        if (!this.mc.ingameGUI.getBossOverlay().mapBossInfos.isEmpty())
        {
            ScaledResolution scaledresolution = new ScaledResolution(this.mc);
            int i = scaledresolution.getScaledWidth();
            int j = 12;

            for (BossInfoClient bossinfolerping : this.mc.ingameGUI.getBossOverlay().mapBossInfos.values())
            {
                int k = i / 2 - 91;
                RenderGameOverlayEvent.BossInfo event = ForgeHooksClient.bossBarRenderPre(scaledresolution, bossinfolerping, k, j, 10 + this.mc.fontRendererObj.FONT_HEIGHT);

                if (!event.isCanceled())
                {
                    if (!ConfigManager.hideBossHealthBar)
                    {
                        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                        this.mc.getTextureManager().bindTexture(this.GUI_BARS_TEXTURES);
                        this.render(k, j, bossinfolerping);
                    }
                    String s = bossinfolerping.getName().getFormattedText();
                    this.mc.fontRendererObj.drawStringWithShadow(s, i / 2 - this.mc.fontRendererObj.getStringWidth(s) / 2, j - 9, 16777215);
                }

                j += ConfigManager.hideBossHealthBar ? 12 : event.getIncrement();
                ForgeHooksClient.bossBarRenderPost(scaledresolution);

                if (ConfigManager.hideBossHealthBar ? j >= scaledresolution.getScaledHeight() / 4.5D : j >= scaledresolution.getScaledHeight() / 3)
                {
                    break;
                }
            }
        }
    }

    private void render(int x, int y, BossInfo info)
    {
        this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2, 182, 5);

        if (info.getOverlay() != BossInfo.Overlay.PROGRESS)
        {
            this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2, 182, 5);
        }

        int i = (int)(info.getPercent() * 183.0F);

        if (i > 0)
        {
            this.drawTexturedModalRect(x, y, 0, info.getColor().ordinal() * 5 * 2 + 5, i, 5);

            if (info.getOverlay() != BossInfo.Overlay.PROGRESS)
            {
                this.drawTexturedModalRect(x, y, 0, 80 + (info.getOverlay().ordinal() - 1) * 5 * 2 + 5, i, 5);
            }
        }
    }
}