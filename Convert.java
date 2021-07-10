class ModelPart {
	static java.util.ArrayList<ModelPart> parts = new java.util.ArrayList<>(20);
	final String name;
	ModelPart parent = null;
	boolean mirror = false;
	
	float x = 0, y = 0, z = 0, rx = 0, ry = 0, rz = 0;
	int u = 0, v = 0;
	float bx = 0, by = 0, bz = 0, ba = 0, bb = 0, bc = 0;
	float scale = 1;
	static int wd = 64;
	static int hg = 32;
	
	
	boolean hadBox = false;
	
	ModelPart(Convert c, String name) {
		this(c, 0, 0, name);
		
	}
	
	ModelPart(Convert c, int u, int v, String name) {
		this.name = name;
		this.u = u;
		this.v = v;
		parts.add(this);
	}
	
	ModelPart(int wd, int hg, int u, int v, String name) {
		this.name = name;
		this.u = u;
		this.v = v;
		ModelPart.wd = wd;
		ModelPart.hg = hg;
		parts.add(this);
	}
	
	ModelPart setPos(float x, float y, float z) {
		this.x = x;
		this.y = y;
		this.z = z;
		return this;
	}
	
	ModelPart setRotationAngle(float x, float y, float z) {
		this.rx = x;
		this.ry = y;
		this.rz = z;
		return this;
	}
	
	ModelPart addChild(ModelPart p) {
		p.parent = this;
		return this;
	}
	
	ModelPart texOffs(int u, int v) {
		this.u = u;
		this.v = v;
		return this;
	}
	
	ModelPart addBox(float x, float y, float z, float a, float b, float c) {
		return addBox(x, y, z, a, b, c, 1);
	}
	
	ModelPart addBox(float x, float y, float z, float a, float b, float c, float _d) {
		bx = x;
		by = y;
		bz = z;
		ba = a;
		bb = b;
		bc = c;
		scale = _d;
		hadBox = true;
		return this;
	}
	
	ModelPart addBox(float x, float y, float z, float a, float b, float c, float _d, boolean mirror) {
		this.mirror = mirror;
		bx = x;
		by = y;
		bz = z;
		ba = a;
		bb = b;
		bc = c;
		hadBox = true;
		return this;
	}
	
	public String toString() {
		String s = "";
		String pName = parent == null ? "modelPartData" : parent.name;
		if (scale != 1) {
			s += "CubeDeformation deformation_" + name + " = new CubeDeformation(" + scale + "f);\n";
		}
		s += "PartDefinition " + name + " = ";
		s += pName + ".addOrReplaceChild(\"" + name + "\", CubeListBuilder.create()\n";
		if (this.mirror) s += ".mirror()\n";
		s += ".texOffs(" + u + ", " + v + ")";
		if (this.hadBox) {
			s += "\n";
			if (scale != 1)
				s += ".addBox(" + bx + "f, " + by + "f, " + bz + "f, " + ba + "f, " + bb + "f, " + bc + "f, deformation_" + name + "),\n";
			else s += ".addBox(" + bx + "f, " + by + "f, " + bz + "f, " + ba + "f, " + bb + "f, " + bc + "f),\n";
		}
		else {
			s += ",\n";
		}
		
		if (x == 0 && y == 0 && z == 0 && rx == 0 && ry == 0 && rz == 0) {
			s += "PartPose.ZERO";
		}
		else if (rx == 0 && ry == 0 && rz == 0) {
			s += "PartPose.offset(" + x + "f, " + y + "f, " + z + "f)";
		}
		else {
			s += "PartPose.offsetAndRotation(" + x + "f, " + y + "f, " + z + "f, \n" + rx + "f, " + ry + "f, " + rz + "f)";
		}
		s += ");";
		
		return s;
	}
	
	public static void print() {
		System.out.println("public static LayerDefinition getTexturedModelData() {");
		System.out.println("    MeshDefinition modelData = new MeshDefinition();");
		System.out.println("    PartDefinition modelPartData = modelData.getRoot();");
		for (ModelPart p : parts) {
			System.out.println(p);
			System.out.println();
		}
		System.out.println("return LayerDefinition.create(modelData, " + wd + ", " + hg + ");");
		System.out.println("}");
		
		System.out.println();
		System.out.println();
		
		for (ModelPart p : parts) {
			String pName = p.parent == null ? "modelPart" : p.parent.name;
			System.out.println(p.name + " = " + pName + ".getChild(\"" + p.name + "\");");
		}
	}
}

public class Convert {
	public static void main(String[] args) {
		new Convert().c();
		
		ModelPart.print();
	}
	
	void setRotationAngle(ModelPart p, float x, float y, float z) {
		p.setRotationAngle(x, y, z);
	}
	
	public void c() {
		float scale = 1;
		ModelPart partC = new ModelPart(64, 64, 0, 19, "partC");
		partC.addBox(1.0F, 0.0F, 1.0F, 14.0F, 9.0F, 14.0F, 0.0F);
		ModelPart partA = new ModelPart(64, 64, 0, 0, "partA");
		partA.addBox(1.0F, 0.0F, 0.0F, 14.0F, 5.0F, 14.0F, 0.0F);
		partA.y = 9.0F;
		partA.z = 1.0F;
		ModelPart partB = new ModelPart(64, 64, 0, 0, "partB");
		partB.addBox(7.0F, -1.0F, 15.0F, 2.0F, 4.0F, 1.0F, 0.0F);
		partB.y = 8.0F;
		ModelPart partRightC = new ModelPart(64, 64, 0, 19, "partRightC");
		partRightC.addBox(1.0F, 0.0F, 1.0F, 15.0F, 9.0F, 14.0F, 0.0F);
		ModelPart partRightA = new ModelPart(64, 64, 0, 0, "partRightA");
		partRightA.addBox(1.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
		partRightA.y = 9.0F;
		partRightA.z = 1.0F;
		ModelPart partRightB = new ModelPart(64, 64, 0, 0, "partRightB");
		partRightB.addBox(15.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
		partRightB.y = 8.0F;
		ModelPart partLeftC = new ModelPart(64, 64, 0, 19, "partLeftC");
		partLeftC.addBox(0.0F, 0.0F, 1.0F, 15.0F, 9.0F, 14.0F, 0.0F);
		ModelPart partLeftA = new ModelPart(64, 64, 0, 0, "partLeftA");
		partLeftA.addBox(0.0F, 0.0F, 0.0F, 15.0F, 5.0F, 14.0F, 0.0F);
		partLeftA.y = 9.0F;
		partLeftA.z = 1.0F;
		ModelPart partLeftB = new ModelPart(64, 64, 0, 0, "partLeftB");
		partLeftB.addBox(0.0F, -1.0F, 15.0F, 1.0F, 4.0F, 1.0F, 0.0F);
		partLeftB.y = 8.0F;
	}
}
