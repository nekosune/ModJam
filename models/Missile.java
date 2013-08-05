// Date: 8/5/2013 10:33:05 AM
// Template version 1.1
// Java generated by Techne
// Keep in mind that you still need to fill in some blanks
// - ZeuX






package net.minecraft.src;

public class ModelMissile extends ModelBase
{
  //fields
    ModelRenderer Body;
    ModelRenderer Fin;
    ModelRenderer Head;
  
  public ModelMissile()
  {
    textureWidth = 64;
    textureHeight = 32;
    
      Body = new ModelRenderer(this, 12, 0);
      Body.addBox(-1F, 0F, -1F, 2, 12, 2);
      Body.setRotationPoint(0F, 11F, 0F);
      Body.setTextureSize(64, 32);
      Body.mirror = true;
      setRotation(Body, 0F, 0F, 0F);
      Fin = new ModelRenderer(this, 0, 0);
      Fin.addBox(1F, 20F, -0.5F, 1, 4, 1);
      Fin.setRotationPoint(0F, 0F, 0F);
      Fin.setTextureSize(64, 32);
      Fin.mirror = true;
      setRotation(Fin, 0F, 0F, 0F);
      Head = new ModelRenderer(this, 22, 0);
      Head.addBox(-1.5F, -3F, -1.5F, 3, 3, 3);
      Head.setRotationPoint(0F, 11F, 0F);
      Head.setTextureSize(64, 32);
      Head.mirror = true;
      setRotation(Head, 0F, 0F, 0F);
  }
  
  public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.render(entity, f, f1, f2, f3, f4, f5);
    setRotationAngles(f, f1, f2, f3, f4, f5);
    Body.render(f5);
    Fin.render(f5);
    Head.render(f5);
  }
  
  private void setRotation(ModelRenderer model, float x, float y, float z)
  {
    model.rotateAngleX = x;
    model.rotateAngleY = y;
    model.rotateAngleZ = z;
  }
  
  public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5)
  {
    super.setRotationAngles(f, f1, f2, f3, f4, f5);
  }

}