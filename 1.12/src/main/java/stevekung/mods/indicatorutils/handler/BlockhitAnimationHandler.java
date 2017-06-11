/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.handler;

import javax.annotation.Nullable;

import org.lwjgl.util.glu.Project;

import com.google.common.base.MoreObjects;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.init.Items;
import net.minecraft.item.ItemMap;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumHand;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.math.MathHelper;
import net.minecraftforge.client.event.RenderHandEvent;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import stevekung.mods.indicatorutils.config.ConfigManager;

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
            this.renderHand(event.getPartialTicks(), event.getRenderPass());
        }
    }

    private void renderHand(float partialTicks, int pass)
    {
        GlStateManager.matrixMode(5889);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate(-(pass * 2 - 1) * 0.07F, 0.0F, 0.0F);
        }

        Project.gluPerspective(this.entityRenderer.getFOVModifier(partialTicks, false), (float)this.mc.displayWidth / (float)this.mc.displayHeight, 0.05F, this.entityRenderer.farPlaneDistance * 2.0F);
        GlStateManager.matrixMode(5888);
        GlStateManager.loadIdentity();

        if (this.mc.gameSettings.anaglyph)
        {
            GlStateManager.translate((pass * 2 - 1) * 0.1F, 0.0F, 0.0F);
        }

        GlStateManager.pushMatrix();
        this.entityRenderer.hurtCameraEffect(partialTicks);

        if (this.mc.gameSettings.viewBobbing)
        {
            this.entityRenderer.applyBobbing(partialTicks);
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
            this.entityRenderer.applyBobbing(partialTicks);
        }
    }

    private void renderItemInFirstPerson(float partialTicks)
    {
        AbstractClientPlayer abstractclientplayer = this.mc.player;
        float f = abstractclientplayer.getSwingProgress(partialTicks);
        EnumHand enumhand = MoreObjects.firstNonNull(abstractclientplayer.swingingHand, EnumHand.MAIN_HAND);
        float f1 = abstractclientplayer.prevRotationPitch + (abstractclientplayer.rotationPitch - abstractclientplayer.prevRotationPitch) * partialTicks;
        float f2 = abstractclientplayer.prevRotationYaw + (abstractclientplayer.rotationYaw - abstractclientplayer.prevRotationYaw) * partialTicks;
        boolean flag = true;
        boolean flag1 = true;

        if (abstractclientplayer.isHandActive())
        {
            ItemStack itemstack = abstractclientplayer.getActiveItemStack();

            if (!itemstack.isEmpty() && itemstack.getItem() == Items.BOW)
            {
                EnumHand enumhand1 = abstractclientplayer.getActiveHand();
                flag = enumhand1 == EnumHand.MAIN_HAND;
                flag1 = !flag;
            }
        }

        this.itemRenderer.rotateArroundXAndY(f1, f2);
        this.itemRenderer.setLightmap();
        this.itemRenderer.rotateArm(partialTicks);
        GlStateManager.enableRescaleNormal();

        if (flag)
        {
            float f3 = enumhand == EnumHand.MAIN_HAND ? f : 0.0F;
            float f5 = 1.0F - (this.itemRenderer.prevEquippedProgressMainHand + (this.itemRenderer.equippedProgressMainHand - this.itemRenderer.prevEquippedProgressMainHand) * partialTicks);
            this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.MAIN_HAND, f3, this.itemRenderer.itemStackMainHand, f5);
        }
        if (flag1)
        {
            float f4 = enumhand == EnumHand.OFF_HAND ? f : 0.0F;
            float f6 = 1.0F - (this.itemRenderer.prevEquippedProgressOffHand + (this.itemRenderer.equippedProgressOffHand - this.itemRenderer.prevEquippedProgressOffHand) * partialTicks);
            this.renderItemInFirstPerson(abstractclientplayer, partialTicks, f1, EnumHand.OFF_HAND, f4, this.itemRenderer.itemStackOffHand, f6);
        }
        GlStateManager.disableRescaleNormal();
        RenderHelper.disableStandardItemLighting();
    }

    private void renderItemInFirstPerson(AbstractClientPlayer player, float partialTicks, float rotationPitch, EnumHand hand, float swingProgress, @Nullable ItemStack itemStack, float equipProgress)
    {
        boolean flag = hand == EnumHand.MAIN_HAND;
        EnumHandSide enumhandside = flag ? player.getPrimaryHand() : player.getPrimaryHand().opposite();
        GlStateManager.pushMatrix();

        if (itemStack.isEmpty())
        {
            if (flag && !player.isInvisible())
            {
                this.itemRenderer.renderArmFirstPerson(equipProgress, swingProgress, enumhandside);
            }
        }
        else if (itemStack.getItem() instanceof ItemMap)
        {
            if (flag && this.itemRenderer.itemStackOffHand.isEmpty())
            {
                this.itemRenderer.renderMapFirstPerson(rotationPitch, equipProgress, swingProgress);
            }
            else
            {
                this.itemRenderer.renderMapFirstPersonSide(equipProgress, enumhandside, swingProgress, itemStack);
            }
        }
        else
        {
            boolean flag1 = enumhandside == EnumHandSide.RIGHT;

            if (player.isHandActive() && player.getItemInUseCount() > 0 && player.getActiveHand() == hand)
            {
                int j = flag1 ? 1 : -1;
                float f = MathHelper.sin(swingProgress * swingProgress * (float)Math.PI);
                float f1 = MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);

                switch (itemStack.getItemUseAction())
                {
                case NONE:
                    this.itemRenderer.transformSideFirstPerson(enumhandside, equipProgress);
                    //
                    GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
                    GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case EAT:
                case DRINK:
                    this.itemRenderer.transformEatFirstPerson(partialTicks, enumhandside, itemStack);
                    this.itemRenderer.transformSideFirstPerson(enumhandside, equipProgress);
                    //
                    GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
                    GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case BLOCK:
                    this.itemRenderer.transformSideFirstPerson(enumhandside, equipProgress);
                    //
                    GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
                    GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);
                    break;
                case BOW:
                    this.itemRenderer.transformSideFirstPerson(enumhandside, equipProgress);
                    //
                    GlStateManager.translate(0.0F, equipProgress * 0.6F, 0.0F);
                    GlStateManager.rotate(0.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f * 20.0F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(f1 * 20.0F, 0.0F, 0.0F, 1.0F);
                    GlStateManager.rotate(f1 * -80.0F, 1.0F, 0.0F, 0.0F);

                    GlStateManager.translate(j * -0.2785682F, 0.18344387F, 0.15731531F);
                    GlStateManager.rotate(-13.935F, 1.0F, 0.0F, 0.0F);
                    GlStateManager.rotate(j * 35.3F, 0.0F, 1.0F, 0.0F);
                    GlStateManager.rotate(j * -9.785F, 0.0F, 0.0F, 1.0F);
                    float f5 = itemStack.getMaxItemUseDuration() - (this.mc.player.getItemInUseCount() - partialTicks + 1.0F);
                    float f6 = f5 / 20.0F;
                    f6 = (f6 * f6 + f6 * 2.0F) / 3.0F;

                    if (f6 > 1.0F)
                    {
                        f6 = 1.0F;
                    }

                    if (f6 > 0.1F)
                    {
                        float f7 = MathHelper.sin((f5 - 0.1F) * 1.3F);
                        float f3 = f6 - 0.1F;
                        float f4 = f7 * f3;
                        GlStateManager.translate(f4 * 0.0F, f4 * 0.004F, f4 * 0.0F);
                    }

                    GlStateManager.translate(f6 * 0.0F, f6 * 0.0F, f6 * 0.04F);
                    GlStateManager.scale(1.0F, 1.0F, 1.0F + f6 * 0.2F);
                    GlStateManager.rotate(j * 45.0F, 0.0F, -1.0F, 0.0F);
                }
            }
            else
            {
                float f = -0.4F * MathHelper.sin(MathHelper.sqrt(swingProgress) * (float)Math.PI);
                float f1 = 0.2F * MathHelper.sin(MathHelper.sqrt(swingProgress) * ((float)Math.PI * 2F));
                float f2 = -0.2F * MathHelper.sin(swingProgress * (float)Math.PI);
                int i = flag1 ? 1 : -1;
                GlStateManager.translate(i * f, f1, f2);
                this.itemRenderer.transformSideFirstPerson(enumhandside, equipProgress);
                this.itemRenderer.transformFirstPerson(enumhandside, swingProgress);
            }
            this.itemRenderer.renderItemSide(player, itemStack, flag1 ? ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND : ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, !flag1);
        }
        GlStateManager.popMatrix();
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