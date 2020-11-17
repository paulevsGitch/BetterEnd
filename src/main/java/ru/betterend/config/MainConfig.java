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
	protected void registerEntries() {}
	
	@Override
	public void saveChanges() {
		this.configKeeper.toJson(settings);
		this.writer.save();
	}
}
