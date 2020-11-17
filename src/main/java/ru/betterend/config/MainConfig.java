package ru.betterend.config;

public class MainConfig extends Config {
	
	private static MainConfig instance;
	
	public static MainConfig getInstance() {
		if (instance == null) {
			instance = new MainConfig();
		}
		
		return instance;
	}
	
	private final ConfigWriter writer;
	
	private MainConfig() {
		this.writer = new ConfigWriter("settings");
		this.settings = this.writer.load();
		this.registerEntries();
		if (settings.size() > 0) {
			this.configKeeper.fromJson(settings);
		} else {
			this.configKeeper.toJson(settings);
			this.writer.save();
		}
	}
	
	@Override
	protected void registerEntries() {
//		this.configKeeper.registerEntry("add_armor_and_equipment", new BooleanEntry(true));
//		this.configKeeper.registerEntry("add_terminite", new BooleanEntry(true));
//		this.configKeeper.registerEntry("add_terminite_armor", new BooleanEntry(true));
//		this.configKeeper.registerEntry("add_terminite_tools", new BooleanEntry(true));
//		this.configKeeper.registerEntry("add_aeternuim", new BooleanEntry(true));
//		this.configKeeper.registerEntry("add_aeternuim_armor", new BooleanEntry(true));
//		this.configKeeper.registerEntry("add_aeternuim_tools", new BooleanEntry(true));
//		this.configKeeper.registerEntry("add_pedestals", new BooleanEntry(true));
//		this.configKeeper.registerEntry("add_hammers", new BooleanEntry(true));
	}
	
	@Override
	public void saveChanges() {
		this.configKeeper.toJson(settings);
		this.writer.save();
	}
}
