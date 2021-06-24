
class ModelPart {
    static java.util.ArrayList<ModelPart> parts = new java.util.ArrayList<>(20);
    final String name;
    ModelPart parent = null;
    boolean mirror = false;

    float x=0, y=0, z=0, rx=0, ry=0, rz=0;
    int u=0, v=0;
    float bx=0,by=0,bz=0,ba=0,bb=0,bc=0;


    boolean hadBox = false;
    ModelPart(Convert c, String name){
        this.name = name;
        parts.add(this);
    }

    ModelPart setPos(float x, float y, float z){
        this.x=x;
        this.y=y;
        this.z=z;
        return this;
    }

    ModelPart setRotationAngle(float x, float y, float z){
        this.rx=x;
        this.ry=y;
        this.rz=z;
        return this;
    }

    ModelPart addChild(ModelPart p){
        p.parent = this;
        return this;
    }

    ModelPart texOffs(int u, int v){
        this.u=u;
        this.v=v;
        return this;
    }

    ModelPart addBox(float x, float y, float z, float a, float b, float c, float _d){
        bx=x;
        by=y;
        bz=z;
        ba=a;
        bb=b;
        bc=c;
        hadBox = true;
        return this;
    }
    ModelPart addBox(float x, float y, float z, float a, float b, float c, float _d, boolean mirror){
        this.mirror = mirror;
        bx=x;
        by=y;
        bz=z;
        ba=a;
        bb=b;
        bc=c;
        hadBox = true;
        return this;
    }

    public String toString(){
        String pName = parent==null?"modelPartData":parent.name;
        String s = "PartDefinition " + name + " = ";        
        s +=  pName+".addOrReplaceChild(\""+name+"\", CubeListBuilder.create()\n";
        if (this.mirror) s+= ".mirror()\n";        
        if (this.hadBox) s+= ".addBox("+bx+"f, "+by+"f, "+bz+"f, "+ba+"f, "+bb+"f, "+bc+"f)\n";
        s+= ".texOffs("+u+", "+v+"),\n";                        
        if (x==0 && y==0 && z==0 && rx==0 && ry==0 && rz==0){
            s+= "PartPose.ZERO";
        } else if (rx==0 && ry==0 && rz==0){
            s+= "PartPose.offset("+x+"f, "+y+"f, "+z+"f)";
        } else {
            s+= "PartPose.offsetAndRotation("+x+"f, "+y+"f, "+z+"f, \n"+rx+"f, "+ry+"f, "+rz+"f)";
        }
        s +=");";

        return s;
    }

    public static void print(){
        for(ModelPart p : parts){
            System.out.println(p);
            System.out.println();
        }

        for(ModelPart p : parts){
            String pName = p.parent==null?"modelPart":p.parent.name;
            System.out.println(p.name +" = "+pName+".getChild(\""+p.name+"\");");
        }
    }
}
public class Convert {
    public static void main(String[] args){
        new Convert().c();

        ModelPart.print();
    }
    void setRotationAngle(ModelPart p, float x, float y, float z){
        p.setRotationAngle(x, y, z);
    }
    public void c (){
        ModelPart legsL = new ModelPart(this, "legsL");
		legsL.setPos(1.5F, 19.9F, -0.45F);
		setRotationAngle(legsL, 0.0F, 0.0F, 0.6981F);

		ModelPart cube_r1 = new ModelPart(this, "cube_r1");
		cube_r1.setPos(0.0F, 0.0F, -1.0F);
		legsL.addChild(cube_r1);
		setRotationAngle(cube_r1, 0.0F, 0.2182F, 0.3927F);
		cube_r1.texOffs(0, 13).addBox(0.0216F, 0.0F, -0.5976F, 3.0F, 0.0F, 1.0F, 0.0F);

		ModelPart cube_r2 = new ModelPart(this, "cube_r2");
		cube_r2.setPos(0.5F, 0.1F, -0.05F);
		legsL.addChild(cube_r2);
		setRotationAngle(cube_r2, 0.0F, 0.0F, 0.3927F);
		cube_r2.texOffs(0, 15).addBox(0.0F, 0.0F, -0.6F, 3.0F, 0.0F, 1.0F, 0.0F);

		ModelPart cube_r3 = new ModelPart(this, "cube_r3");
		cube_r3.setPos(0.0F, 0.0F, 0.9F);
		legsL.addChild(cube_r3);
		setRotationAngle(cube_r3, 0.0F, -0.2182F, 0.3927F);
		cube_r3.texOffs(0, 14).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F);

		ModelPart legsR = new ModelPart(this,"legsR");
		legsR.setPos(-1.5F, 19.9F, -0.55F);
		setRotationAngle(legsR, 0.0F, 3.1416F, -0.6545F);

		ModelPart cube_r4 = new ModelPart(this, "cube_r4");
		cube_r4.setPos(0.0F, 0.0F, -1.0F);
		legsR.addChild(cube_r4);
		setRotationAngle(cube_r4, 0.0F, 0.2182F, 0.3927F);
		cube_r4.texOffs(0, 10).addBox(0.0F, 0.0F, -0.5F, 3.0F, 0.0F, 1.0F, 0.0F);

		ModelPart cube_r5 = new ModelPart(this, "cube_r5");
		cube_r5.setPos(0.5F, 0.1F, -0.05F);
		legsR.addChild(cube_r5);
		setRotationAngle(cube_r5, 0.0F, 0.0F, 0.3927F);
		cube_r5.texOffs(0, 11).addBox(0.0F, 0.0F, -0.4F, 3.0F, 0.0F, 1.0F, 0.0F);

		ModelPart cube_r6 = new ModelPart(this, "cube_r6");
		cube_r6.setPos(0.0F, 0.0F, 0.9F);
		legsR.addChild(cube_r6);
		setRotationAngle(cube_r6, 0.0F, -0.2182F, 0.3927F);
		cube_r6.texOffs(0, 12).addBox(0.0216F, 0.0F, -0.4024F, 3.0F, 0.0F, 1.0F, 0.0F);

		ModelPart head_pivot = new ModelPart(this, "head_pivot");
		head_pivot.setPos(0.0F, 18.0F, -3.0F);
		head_pivot.texOffs(15, 10).addBox(-1.5F, -1.5F, -2.0F, 3.0F, 3.0F, 3.0F, 0.0F);

		ModelPart tendril_r_r1 = new ModelPart(this, "tendril_r_r1");
		tendril_r_r1.setPos(1.0F, -1.15F, -1.0F);
		head_pivot.addChild(tendril_r_r1);
		setRotationAngle(tendril_r_r1, 0.0F, 0.0F, 0.3927F);
		tendril_r_r1.texOffs(23, 0).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F, true);

		ModelPart tendril_r_r2 = new ModelPart(this, "tendril_r_r2");
		tendril_r_r2.setPos(-1.0F, -1.15F, -1.0F);
		head_pivot.addChild(tendril_r_r2);
		setRotationAngle(tendril_r_r2, 0.0F, 0.0F, -0.3927F);
		tendril_r_r2.texOffs(23, 0).addBox(-1.5F, -5.0F, 0.0F, 3.0F, 5.0F, 0.0F, 0.0F);

		ModelPart bb_main = new ModelPart(this, "bb_main");
		bb_main.setPos(0.0F, 24.0F, 0.0F);
		bb_main.texOffs(19, 19).addBox(-2.5F, -8.5F, -3.0F, 5.0F, 5.0F, 3.0F, 0.0F);

		ModelPart wingR_r1 = new ModelPart(this, "wingR_r1");
		wingR_r1.setPos(-1.5F, -6.5F, 0.5F);
		bb_main.addChild(wingR_r1);
		setRotationAngle(wingR_r1, 0.0F, 0.0F, 0.3927F);
		wingR_r1.texOffs(0, 5).addBox(-7.0F, 0.0F, -3.0F, 9.0F, 0.0F, 5.0F, 0.0F, true);

		ModelPart wingL_r1 = new ModelPart(this, "wingL_r1");
		wingL_r1.setPos(1.5F, -6.5F, 0.5F);
		bb_main.addChild(wingL_r1);
		setRotationAngle(wingL_r1, 0.0F, 0.0F, -0.3927F);
		wingL_r1.texOffs(0, 5).addBox(-2.0F, 0.0F, -3.0F, 9.0F, 0.0F, 5.0F, 0.0F);

		ModelPart abdomen_r1 = new ModelPart(this, "abdomen_r1");
		abdomen_r1.setPos(1.0F, -3.9F, 0.0F);
		bb_main.addChild(abdomen_r1);
		setRotationAngle(abdomen_r1, -0.3927F, 0.0F, 0.0F);
		abdomen_r1.texOffs(0, 10).addBox(-3.0F, -4.0F, -1.0F, 4.0F, 4.0F, 7.0F, 0.0F);
    }
}
