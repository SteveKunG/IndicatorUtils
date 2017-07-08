package stevekung.mods.indicatorutils.renderer;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.entity.RenderPlayer;
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
}