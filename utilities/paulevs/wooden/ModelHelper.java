package paulevs.wooden;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class ModelHelper {
	public static void main(String[] args) throws IOException {
		new ModelHelper(args[0]);
	}
	
	private ModelHelper(String name) throws IOException {
		clearOutput();
		editBlockstates(name);
		editBlocks(name);
		editItems(name);
		printStates(name);
	}
	
	private void clearOutput() {
		File out = new File("./output");
		if (out.exists()) {
			for (File file: new File("./output/blockstates").listFiles())
				file.delete();
			for (File file: new File("./output/models/block").listFiles())
				file.delete();
			for (File file: new File("./output/models/item").listFiles())
				file.delete();
		}
		else {
			out.mkdir();
		}
	}
	
	private void editBlockstates(String name) throws IOException {
		String string;
		new File("./output/blockstates").mkdirs();
		for (String state: Helper.BLOCKSTATES) {
			InputStream stream = Helper.class.getResourceAsStream("/blockstates/" + state);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			BufferedWriter wr = new BufferedWriter(new FileWriter(new File("./output/blockstates/" + state.replace(Helper.MASK, name))));
			while ((string = br.readLine()) != null) wr.write(string.replace(Helper.MASK, name) + "\n");
			wr.close();
			br.close();
		}
	}
	
	private void editBlocks(String name) throws IOException {
		String string;
		new File("./output/models/block").mkdirs();
		for (String block: Helper.BLOCKS) {
			InputStream stream = Helper.class.getResourceAsStream("/block/" + block);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			BufferedWriter wr = new BufferedWriter(new FileWriter(new File("./output/models/block/" + block.replace(Helper.MASK, name))));
			while ((string = br.readLine()) != null) wr.write(string.replace(Helper.MASK, name) + "\n");
			wr.close();
			br.close();
		}
	}
	
	private void editItems(String name) throws IOException {
		String string;
		new File("./output/models/item").mkdirs();
		for (String item: Helper.ITEMS) {
			InputStream stream = Helper.class.getResourceAsStream("/item/" + item);
			BufferedReader br = new BufferedReader(new InputStreamReader(stream));
			BufferedWriter wr = new BufferedWriter(new FileWriter(new File("./output/models/item/" + item.replace(Helper.MASK, name))));
			while ((string = br.readLine()) != null) wr.write(string.replace(Helper.MASK, name) + "\n");
			wr.close();
			br.close();
		}
	}
	
	private String capitalize(String str) {
		String out = "";
		for (int i = 0; i < str.length(); i++)
		{
			if (i == 0 || str.charAt(i - 1) == ' ')
				out += Character.toUpperCase(str.charAt(i));
			else
				out += str.charAt(i);
		}
		return out;
	}
	
	private void printStates(String name) throws IOException {
		BufferedWriter wr = new BufferedWriter(new FileWriter(new File("./output/states.txt")));
		for (String state: Helper.BLOCKSTATES)
		{
			String rname = state.replace(Helper.MASK, name);
			String onlyName = rname.substring(0, rname.indexOf('.'));
			String finName = name.replace('_', ' ') + " " + state.substring(0, state.indexOf('.')).replace(Helper.MASK, "").replace('_', ' ').trim();
			wr.write("	\"block.betterend." + onlyName + "\": \"" + capitalize(finName) + "\",\n");
		}
		wr.close();
	}
}
