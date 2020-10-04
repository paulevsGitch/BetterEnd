package paulevs.converter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;

public class BlockBenchToFabricJava {
	public static void main(String[] args) throws Exception {
		new BlockBenchToFabricJava("D:/blockbench_models/", "dragonfly.java");
	}
	
	private BlockBenchToFabricJava(String path, String nameIn) throws Exception {
		String string;
		File in = new File(path + nameIn);
		File out = new File(path + "out.txt");
		
		BufferedReader br = new BufferedReader(new FileReader(in));
		BufferedWriter wr = new BufferedWriter(new FileWriter(out));
		
		boolean write = false;
		while ((string = br.readLine()) != null) {
			string = string
					.replace("ModelRenderer", "ModelPart")
					.replace("setRotationPoint", "setPivot")
					.replace("addBox", "addCuboid");
			
			if (write)
				wr.write(string + "\n");
			
			if (string.contains("{"))
				write = true;
			
			if (string.contains("}"))
				break;
		}
		
		wr.close();
		br.close();
	}
}
