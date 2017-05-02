/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import org.lwjgl.util.glu.Project;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms.TransformType;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemMap;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatorutils.config.ConfigManager;

@SuppressWarnings("deprecation")
public class BlockhitAnimationHandler
{
    private EntityRenderer entityRenderer;
    private ItemRenderer itemRenderer;
    private RenderManager renderManager;
    private Minecraft mc;
    private GameSettings gameSettings;
    private KeyBinding zoom;

    public BlockhitAnimationHandler()
    {
        this.mc = Minecraft.getMinecraft();
        this.itemRenderer = this.mc.getItemRenderer();
        this.renderManager = this.mc.getRenderManager();
        this.gameSettings = this.mc.gameSettings;
        this.entityRenderer = this.mc.entityRenderer;

        for (KeyBinding key : this.gameSettings.keyBindings)
        {
            if (key.getKeyDescription().contains("of.key.zoom"))
            {
                this.zoom = key;
            }
        }
    }

    @SubscribeEvent
    public void onRenderFirstHand(RenderHandEvent event)
    {
        event.setCanceled(true);

        if (!this.isZoomed())
        {
            GlStateManager.clear(256);
            this.renderHand(event.partialTicks, event.renderPass);
            this.entityRenderer.renderWorldDirections(event.partialTicks);
        }
    }

    private void renderHand(float partialTicks, int renderPass)
    {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();
        float f = 0.07F;

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate(-(renderPass * 2 - 1) * f, 0.0F, 0.0F);
        }

        Project.gluPerspective(this.entityRenderer.getFOVModifier(partialTicks, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.entityRenderer.farPlaneDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate((renderPass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GlStateManager.pushMatrix();
        this.entityRenderer.hurtCameraEffect(partialTicks);

        if (this.mc.gameSettings.viewBobbing)
        {
            this.entityRenderer.setupViewBobbing(partialTicks);
        }

        boolean flag = this.mc.getRenderViewEntity() instanceof EntityLivingBase && ((EntityLivingBase)this.mc.getRenderViewEntity()).isPlayerSleeping();

        if (this.mc.gameSettings.thirdPersonView == 0 && !flag && !this.mc.gameSettings.hideGUI && !this.mc.playerController.isSpectator())
        {
            this.entityRenderer.enableLightmap();

            if (ConfigManager.enableBlockhitAnimation)
            {
                this.renderItemInFirstPerson(partialTicks);
            }
            else
            {
                this.itemRenderer.renderItemInFirstPerson(partialTicks);
            }
            this.entityRenderer.disableLightmap();
        }

        GlStateManager.popMatrix();

        if (this.mc.gameSettings.thirdPersonView == 0 && !flag)
        {
            this.itemRenderer.renderOverlays(partialTicks);
            this.entityRenderer.hurtCameraEffect(partialTicks);
        }
        if (this.mc.gameSettings.viewBobbing)
        {
            this.entityRenderer.setupViewBobbing(partialTicks);
        }
    }

    private void renderItemInFirstPerson(float partialTicks)
    {
        float f = 1.0F - (this.itemRenderer.prevEquippedProgress + (this.itemRenderer.equippedProgress - this.itemRenderer.prevEquippedProgress) * partialTicks);
        AbstractClientPlayer abstractclientplayer = this.mc.thePlayer;
        float f1 = abstractclientplayer.getSwingProgress(partialTicks);
        float f2 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f3 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        this.itemRenderer.func_178101_a(f2, f3);
        this.itemRenderer.func_178109_a(abstractclientplayer);
        this.itemRenderer.func_178110_a((EntityPlayerSP)abstractclientplayer, partialTicks);
        GlStateManager.enableRescaleNormal();
        GlStateManager.pushMatrix();

        if (this.itemRenderer.itemToRender != null)
        {
            if (this.itemRenderer.itemToRender.getItem() instanceof ItemMap)
            {
                this.itemRenderer.renderItemMap(abstractclientplayer, f2, f, f1);
            }
            else if (abstractclientplayer.getItemInUseCount() > 0)
            {
                EnumAction enumaction = this.itemRenderer.itemToRender.getItemUseAction();

                switch (enumaction)
                {
                case NONE:
                    this.itemRenderer.transformFirstPersonItem(f, f1);
                    break;
                case EAT:
                case DRINK:
                    this.itemRenderer.func_178104_a(abstractclientplayer, partialTicks);
                    this.itemRenderer.transformFirstPersonItem(f, f1);
                    break;
                case BLOCK:
                    this.itemRenderer.transformFirstPersonItem(f, f1);
                    this.itemRenderer.func_178103_d();
                    break;
                case BOW:
                    this.itemRenderer.transformFirstPersonItem(f, f1);
                    this.itemRenderer.func_178098_a(partialTicks, abstractclientplayer);
                }
            }
            else
            {
                this.itemRenderer.func_178105_d(f1);
                this.itemRenderer.transformFirstPersonItem(f, f1);
            }
            this.itemRenderer.renderItem(abstractclientplayer, this.itemRenderer.itemToRender, TransformType.FIRST_PERSON);
        }
        else if (!abstractclientplayer.isInvisible())
        {
            this.itemRenderer.func_178095_a(abstractclientplayer, f, f1);
        }
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private boolean isZoomed()
    {
        if (FMLClientHandler.instance().hasOptifine() && this.zoom.isKeyDown())
        {
            return true;
        }
        return false;
    }
}