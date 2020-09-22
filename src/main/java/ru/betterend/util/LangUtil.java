package ru.betterend.util;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.text.TranslatableText;
import ru.betterend.BetterEnd;

public class LangUtil {
	private final static String MODID = BetterEnd.MOD_ID;
	
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
	
	public TranslatableText getText(String key) {
		return getText(element, key);
	}
	
	public static String getString(String element, String key) {
		return I18n.translate(String.format("%s.%s.%s", MODID, element, key));
	}
	
	public static TranslatableText getText(String element, String key) {
		return new TranslatableText(getString(element, key));
	}
}
