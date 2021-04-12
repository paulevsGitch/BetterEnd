package ru.betterend.util;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.TranslatableComponent;

public class LangUtil {
	public final static String CONFIG_ELEMENT = "configuration";
	
	private String element;
	
	public LangUtil(String element) {
		this.element = element;
	}
	
	public void setElement(String key) {
		this.element = key;
	}
	
	public String getString(String key) {
		return getString(element, key);
	}
	
	public TranslatableComponent getText(String key) {
		return getText(element, key);
	}
	
	public static String translate(String key) {
		return I18n.get(key);
	}
	
	public static String getString(String element, String key) {
		return translate(String.format("%s.%s", element, key));
	}
	
	public static TranslatableComponent getText(String element, String key) {
		return new TranslatableComponent(getString(element, key));
	}
}
