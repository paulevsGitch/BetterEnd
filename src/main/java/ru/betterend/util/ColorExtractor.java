package ru.betterend.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import ru.bclib.util.ColorUtil;

public class ColorExtractor {
	private List<Center> centers = new ArrayList<>();
	private List<Integer> colors;
	private Integer result;
	
	public ColorExtractor(List<Integer> colors) {
		this.colors = colors;
		Random rnd = new Random();
		int size = colors.size();
		for (int i = 0; i < 4; i++) {
			int color = colors.get(rnd.nextInt(size));
			this.centers.add(new Center(color));
		}
	}
	
	public int analize() {
		boolean moved = true;
		while (moved) {
			this.remap();
			moved = false;
			for (Center center : centers) {
				if (center.move()) {
					moved = true;
				}
			}
		}
		List<Center> toClear = new ArrayList<>();
		this.centers.forEach(center -> {
			if (center.colors.isEmpty()) {
				toClear.add(center);
			}
		});
		if (toClear.size() > 0) {
			toClear.forEach(clear ->
				centers.remove(clear));
		}
		this.centers.sort(Center.COMPARATOR);
		
		return this.getResult();
	}
	
	public int getResult() {
		if (result == null) {
			double weights = 0;
			double alpha = 0;
			double red = 0;
			double green = 0;
			double blue = 0;
			for (Center center : centers) {
				double weight = (double) center.colors.size() / colors.size();
				weights += weight;
				alpha += center.a * weight;
				red += center.r * weight;
				green += center.g * weight;
				blue += center.b * weight;
			};
			
			int a = (int) Math.round(alpha / weights);
			int r = (int) Math.round(red / weights);
			int g = (int) Math.round(green / weights);
			int b = (int) Math.round(blue / weights);
			
			this.result = a << 24 | r << 16 | g << 8 | b;
		}
		
		return this.result;
	}
	
	private void remap() {
		this.centers.forEach(entry -> entry.colors.clear());
		this.colors.forEach(color -> {
			int id = 0;
			int base = centers.get(0).getColor();
			int dst = ColorUtil.colorDistance(color, base);
			for (Center center : centers) {
				base = center.getColor();
				int dst1 = ColorUtil.colorDistance(color, base);
				if (dst1 < dst) {
					dst = dst1;
					id = centers.indexOf(center);
				}
			}
			this.centers.get(id).colors.add(color);
		});
	}
	
	private static class Center {
		static final Comparator<Center> COMPARATOR = new Comparator<Center>() {
			@Override
			public int compare(Center c1, Center c2) {
				return Integer.compare(c1.getColor(), c2.getColor());
			}
		};
		
		List<Integer> colors = new ArrayList<>();
		double a, r, g, b;
		
		Center(int color) {
			this.a = (color >> 24) & 255;
			this.r = (color >> 16) & 255;
			this.g = (color >> 8) & 255;
			this.b = color & 255;
		}
		
		private void update(double a, double r, double g, double b) {
			this.a = a;
			this.r = r;
			this.g = g;
			this.b = b;
		}
		
		public int getColor() {
			int a = (int) Math.round(this.a);
			int r = (int) Math.round(this.r);
			int g = (int) Math.round(this.g);
			int b = (int) Math.round(this.b);
			return a << 24 | r << 16 | g << 8 | b;
		}
		
		public boolean move() {
			double or = r;
			double og = g;
			double ob = b;
			double a = 0, r = 0, g = 0, b = 0;
			int size = this.colors.size();
			for (int col : colors) {
				a += (col >> 24) & 255;
				r += (col >> 16) & 255;
				g += (col >> 8) & 255;
				b += col & 255;
			}
			a /= size;
			r /= size;
			g /= size;
			b /= size;
			
			this.update(a, r, g, b);
			
			return Math.abs(r - or) > 0.1 ||
				   Math.abs(g - og) > 0.1 ||
				   Math.abs(b - ob) > 0.1;
		}
	}
}
