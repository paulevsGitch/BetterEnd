
class ModelPart {
    static java.util.ArrayList<ModelPart> parts = new java.util.ArrayList<>(20);
    final String name;
    ModelPart parent = null;
    boolean mirror = false;

    float x=0, y=0, z=0, rx=0, ry=0, rz=0;
    int u=0, v=0;
    float bx=0,by=0,bz=0,ba=0,bb=0,bc=0;
    float scale = 1;


    boolean hadBox = false;
    ModelPart(Convert c, String name){
        this(c, 0, 0, name);
        
    }

    ModelPart(Convert c, int u, int v, String name){
        this.name = name;
        this.u = u;
        this.v = v;
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
        scale = _d;
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
        String s = "";
        String pName = parent==null?"modelPartData":parent.name;
        if (scale!=1){
            s += "CubeDeformation deformation_"+name+" = new CubeDeformation("+scale+"f);\n";
        }
        s += "PartDefinition " + name + " = ";        
        s +=  pName+".addOrReplaceChild(\""+name+"\", CubeListBuilder.create()\n";
        if (this.mirror) s+= ".mirror()\n";        
        if (this.hadBox) {
            if (scale!=1)
            s+= ".addBox("+bx+"f, "+by+"f, "+bz+"f, "+ba+"f, "+bb+"f, "+bc+"f, deformation_"+name+")\n";
            else
            s+= ".addBox("+bx+"f, "+by+"f, "+bz+"f, "+ba+"f, "+bb+"f, "+bc+"f)\n";
        }
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
        System.out.println("public static LayerDefinition getTexturedModelData() {");
        System.out.println("    MeshDefinition modelData = new MeshDefinition();");
        System.out.println("    PartDefinition modelPartData = modelData.getRoot();");
        for(ModelPart p : parts){
            System.out.println(p);
            System.out.println();
        }
        System.out.println("return LayerDefinition.create(modelData, 48, 48);");
	    System.out.println("}");

        System.out.println();
        System.out.println();

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
        float scale = 0;
        ModelPart body = new ModelPart(this, 16, 16, "body");
		body.addBox(-4.0F, 0.0F, -2.0F, 8.0F, 12.0F, 4.0F, scale);
		body.setPos(0.0F, 0.0F, 0.0F);
		ModelPart leftLeg = new ModelPart(this, 0, 32, "leftLeg");
		leftLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
		leftLeg.setPos(1.9F, 12.0F, 0.0F);
		ModelPart rightLeg = new ModelPart(this, 0, 16, "rightLeg");
		rightLeg.addBox(-2.0F, 0.0F, -2.0F, 4.0F, 12.0F, 4.0F, scale);
		rightLeg.setPos(-1.9F, 12.0F, 0.0F);
    }
}
