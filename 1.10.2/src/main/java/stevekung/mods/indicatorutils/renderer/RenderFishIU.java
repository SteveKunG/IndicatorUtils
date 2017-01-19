/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.VertexBuffer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.projectile.EntityFishHook;
import net.minecraft.util.EnumHandSide;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import stevekung.mods.indicatorutils.utils.IULog;

@SideOnly(Side.CLIENT)
public class RenderFishIU extends Render<EntityFishHook>
{
    private static final ResourceLocation FISH_PARTICLES = new ResourceLocation("textures/particle/particles.png");

    public RenderFishIU(RenderManager manager)
    {
        super(manager);
        IULog.info("New RenderFishIU.class successfully loaded");
    }

    @Override
    public void doRender(EntityFishHook entity, double x, double y, double z, float entityYaw, float partialTicks)
    {
        GlStateManager.pushMatrix();
        GlStateManager.translate((float)x, (float)y, (float)z);
        GlStateManager.enableRescaleNormal();
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        this.bindEntityTexture(entity);
        Tessellator tessellator = Tessellator.getInstance();
        VertexBuffer vertexbuffer = tessellator.getBuffer();
        GlStateManager.rotate(180.0F - this.renderManager.playerViewY, 0.0F, 1.0F, 0.0F);
        GlStateManager.rotate((this.renderManager.options.thirdPersonView == 2 ? -1 : 1) * -this.renderManager.playerViewX, 1.0F, 0.0F, 0.0F);

        if (this.renderOutlines)
        {
            GlStateManager.enableColorMaterial();
            GlStateManager.enableOutlineMode(this.getTeamColor(entity));
        }

        vertexbuffer.begin(7, DefaultVertexFormats.POSITION_TEX_NORMAL);
        vertexbuffer.pos(-0.5D, -0.5D, 0.0D).tex(0.0625D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(0.5D, -0.5D, 0.0D).tex(0.125D, 0.1875D).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(0.5D, 0.5D, 0.0D).tex(0.125D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
        vertexbuffer.pos(-0.5D, 0.5D, 0.0D).tex(0.0625D, 0.125D).normal(0.0F, 1.0F, 0.0F).endVertex();
        tessellator.draw();

        if (this.renderOutlines)
        {
            GlStateManager.disableOutlineMode();
            GlStateManager.disableColorMaterial();
        }

        GlStateManager.disableRescaleNormal();
        GlStateManager.popMatrix();

        if (entity.angler != null && !this.renderOutlines)
        {
            int k = entity.angler.getPrimaryHand() == EnumHandSide.RIGHT ? 1 : -1;
            float f7 = entity.angler.getSwingProgress(partialTicks);
            float f8 = MathHelper.sin(MathHelper.sqrt_float(f7) * (float)Math.PI);
            float f9 = (entity.angler.prevRenderYawOffset + (entity.angler.renderYawOffset - entity.angler.prevRenderYawOffset) * partialTicks) * 0.017453292F;
            double d0 = MathHelper.sin(f9);
            double d1 = MathHelper.cos(f9);
            double d2 = k * 0.35D;
            double d4;
            double d5;
            double d6;
            double d7;
            double dz = 0.0D;

            if ((this.renderManager.options == null || this.renderManager.options.thirdPersonView <= 0) && entity.angler == Minecraft.getMinecraft().thePlayer)
            {
                Vec3d vec3d = new Vec3d(k * -0.5D, 0.03D, 0.95D);
                vec3d = vec3d.rotatePitch(-(entity.angler.prevRotationPitch + (entity.angler.rotationPitch - entity.angler.prevRotationPitch) * partialTicks) * 0.017453292F);
                vec3d = vec3d.rotateYaw(-(entity.angler.prevRotationYaw + (entity.angler.rotationYaw - entity.angler.prevRotationYaw) * partialTicks) * 0.017453292F);
                vec3d = vec3d.rotateYaw(f8 * 0.5F);
                vec3d = vec3d.rotatePitch(-f8 * 0.7F);
                d4 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * partialTicks + vec3d.xCoord;
                d5 = entity.angler.prevPosY + (entity.angler.posY - entity.angler.prevPosY) * partialTicks + vec3d.yCoord;
                d6 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * partialTicks + vec3d.zCoord;
                d7 = entity.angler.getEyeHeight();
            }
            else
            {
                d4 = entity.angler.prevPosX + (entity.angler.posX - entity.angler.prevPosX) * partialTicks - d1 * d2 - d0 * 0.8D;
                d5 = entity.angler.prevPosY + entity.angler.getEyeHeight() + (entity.angler.posY - entity.angler.prevPosY) * partialTicks - 0.45D;
                d6 = entity.angler.prevPosZ + (entity.angler.posZ - entity.angler.prevPosZ) * partialTicks - d0 * d2 + d1 * 0.8D;
                d7 = entity.angler.isSneaking() ? -0.45D : 0.0D;
                dz = entity.angler.isSneaking() ? -0.03D : 0.0D;
            }

            double d13 = entity.prevPosX + (entity.posX - entity.prevPosX) * partialTicks;
            double d8 = entity.prevPosY + (entity.posY - entity.prevPosY) * partialTicks + 0.25D;
            double d9 = entity.prevPosZ + (entity.posZ - entity.prevPosZ) * partialTicks;
            double d10 = (float)(d4 - d13) + dz;
            double d11 = (float)(d5 - d8) + d7;
            double d12 = (float)(d6 - d9) - 0.05D;
            GlStateManager.disableTexture2D();
            GlStateManager.disableLighting();
            vertexbuffer.begin(3, DefaultVertexFormats.POSITION_COLOR);

            for (int i1 = 0; i1 <= 16; ++i1)
            {
                float f10 = i1 / 16.0F;
                vertexbuffer.pos(x + d10 * f10, y + d11 * (f10 * f10 + f10) * 0.5D + 0.25D, z + d12 * f10).color(0, 0, 0, 255).endVertex();
            }
            tessellator.draw();
            GlStateManager.enableLighting();
            GlStateManager.enableTexture2D();
            super.doRender(entity, x, y, z, entityYaw, partialTicks);
        }
    }

    @Override
    protected ResourceLocation getEntityTexture(EntityFishHook entity)
    {
        return FISH_PARTICLES;
    }
}