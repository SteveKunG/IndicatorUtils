/*******************************************************************************
 *
 * Copyright 2016 Wasinthorn Suksri/SteveKunG - Indicator Utils
 *
 ******************************************************************************/

package stevekung.mods.indicatorutils.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.player.EntityPlayer;
import stevekung.mods.indicatorutils.utils.IULog;

@SideOnly(Side.CLIENT)
public class RenderPlayerMOD extends RenderPlayer
{
    public RenderPlayerMOD()
    {
        super();
        this.mainModel = new ModelBipedMOD(0.0F);
        this.modelBipedMain = (ModelBipedMOD)this.mainModel;
        this.modelArmorChestplate = new ModelBipedMOD(1.0F);
        this.modelArmor = new ModelBipedMOD(0.5F);
        IULog.info("New RenderPlayerMOD.class successfully loaded");
    }

    @Override
    public void renderFirstPersonArm(EntityPlayer p_82441_1_)
    {
        float f = 1.0F;
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(770, 771);
        GL11.glColor3f(f, f, f);
        this.modelBipedMain.onGround = 0.0F;
        this.modelBipedMain.setRotationAngles(0.0F, 0.0F, 0.0F, 0.0F, 0.0F, 0.0625F, p_82441_1_);
        this.modelBipedMain.bipedRightArm.render(0.0625F);
        GL11.glDisable(GL11.GL_BLEND);
    }
}