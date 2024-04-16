package com.unleqitq.jeat.tools;

import lombok.Builder;
import lombok.experimental.UtilityClass;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Range;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.function.DoubleUnaryOperator;

@UtilityClass
public class PlotTool {
	
	public static BufferedImage plot(DoubleUnaryOperator function, int width, int height,
		PlotViewConfig config) {
		BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		Graphics2D g = image.createGraphics();
		
		g.setColor(config.backgroundColor);
		g.fillRect(0, 0, width, height);
		
		if (config.drawGrid) drawGrid(g, width, height, config);
		
		if (config.drawAxis) drawAxis(g, width, height, config);
		
		if (config.drawTicks) drawTicks(g, width, height, config);
		
		drawPlot(g, width, height, function, config);
		
		if (config.drawTickLabels) drawTickLabels(g, width, height, config);
		
		if (config.drawAxisLabels) drawAxisLabels(g, width, height, config);
		
		g.dispose();
		return image;
	}
	
	private static void drawPlot(Graphics2D g, int width, int height, DoubleUnaryOperator function,
		PlotViewConfig config) {
		g.setStroke(
			new BasicStroke(config.strokeWeight, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(config.plotColor);
		
		double prevY = Double.NaN;
		for (int xPixel = 0; xPixel < width; xPixel++) {
			double x = config.xMin + (double) xPixel / width * (config.xMax - config.xMin);
			double y = function.applyAsDouble(x);
			if (!Double.isNaN(y) && !Double.isNaN(prevY)) {
				int yPixel1 = (int) ((config.yMax - prevY) / (config.yMax - config.yMin) * height);
				int yPixel2 = (int) ((config.yMax - y) / (config.yMax - config.yMin) * height);
				g.drawLine(xPixel - 1, yPixel1, xPixel, yPixel2);
			}
			prevY = y;
		}
	}
	
	private static void drawAxis(Graphics2D g, int width, int height, PlotViewConfig config) {
		g.setStroke(new BasicStroke(config.axisWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(config.axisColor);
		
		int x =
			Math.max(0, Math.min(width - 1, (int) (-config.xMin / (config.xMax - config.xMin) * width)));
		g.drawLine(x, 0, x, height);
		
		int y =
			Math.max(0, Math.min(height - 1, (int) (config.yMax / (config.yMax - config.yMin) * height)));
		g.drawLine(0, y, width, y);
	}
	
	private static void drawAxisLabels(Graphics2D g, int width, int height, PlotViewConfig config) {
		int yAxisPosX =
			Math.max(0, Math.min(width - 1, (int) (-config.xMin / (config.xMax - config.xMin) * width)));
		
		int xAxisPosY =
			Math.max(0, Math.min(height - 1, (int) (config.yMax / (config.yMax - config.yMin) * height)));
		FontMetrics fm = g.getFontMetrics(config.axisFont);
		FontMetrics fm2 = g.getFontMetrics(config.tickFont);
		{
			// draw x-axis label
			int labelX = yAxisPosX < 2 * width / 3 ? width - fm.stringWidth(config.xLabel) - 10 : 10;
			boolean below = xAxisPosY < 2 * height / 3;
			int labelY = config.drawTickLabels ?
				(int) (below ? xAxisPosY + fm.getHeight() + config.tickLength + fm2.getHeight() + 5 :
					xAxisPosY - config.tickLength - fm2.getHeight() - 5) :
				below ? xAxisPosY + fm.getHeight() + 5 : xAxisPosY - 5;
			drawOutlinedText(g, config.xLabel, labelX, labelY, config.axisFont, config.textColor,
				config.textOutlineColor, config.textOutline);
		}
		{
			// draw y-axis label
			boolean right = yAxisPosX < width / 3;
			int labelX = config.drawTickLabels ? (int) (right ?
				yAxisPosX + config.tickLength + fm2.stringWidth(config.yTickFormat.formatted(10.0)) + 5 :
				yAxisPosX - fm.stringWidth(config.yLabel) - config.tickLength -
					fm2.stringWidth(config.yTickFormat.formatted(10.0)) - 5) :
				right ? yAxisPosX + 5 : yAxisPosX - fm.stringWidth(config.yLabel) - 5;
			int labelY = xAxisPosY < height / 3 ? height - 5 : 10 + fm.getHeight();
			drawOutlinedText(g, config.yLabel, labelX, labelY, config.axisFont, config.textColor,
				config.textOutlineColor, config.textOutline);
		}
	}
	
	private static void drawTicks(Graphics2D g, int width, int height, PlotViewConfig config) {
		g.setStroke(new BasicStroke(config.tickWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		g.setColor(config.tickColor);
		
		double xStep = Math.pow(10,
			Math.ceil(Math.log10((config.xMax - config.xMin) / width * config.minTickSpacing)));
		double yStep = Math.pow(10,
			Math.ceil(Math.log10((config.yMax - config.yMin) / height * config.minTickSpacing)));
		int xAxisPosY =
			Math.max(0, Math.min(height - 1, (int) (config.yMax / (config.yMax - config.yMin) * height)));
		int yAxisPosX =
			Math.max(0, Math.min(width - 1, (int) (-config.xMin / (config.xMax - config.xMin) * width)));
		
		for (int xPixel = 0; xPixel <= (config.xMax - config.xMin) / xStep; xPixel++) {
			double x = config.xMin + xPixel * xStep;
			int xPixel1 = (int) ((x - config.xMin) / (config.xMax - config.xMin) * width);
			if (Math.abs(xPixel1 - yAxisPosX) <= config.tickLength) continue;
			int yPixel1 = xAxisPosY - (int) config.tickLength;
			int yPixel2 = xAxisPosY + (int) config.tickLength;
			g.drawLine(xPixel1, yPixel1, xPixel1, yPixel2);
		}
		
		for (int yPixel = 0; yPixel <= (config.yMax - config.yMin) / yStep; yPixel++) {
			double y = config.yMin + yPixel * yStep;
			int yPixel1 = (int) ((config.yMax - y) / (config.yMax - config.yMin) * height);
			if (Math.abs(yPixel1 - xAxisPosY) <= config.tickLength) continue;
			int xPixel1 = yAxisPosX - (int) config.tickLength;
			int xPixel2 = yAxisPosX + (int) config.tickLength;
			g.drawLine(xPixel1, yPixel1, xPixel2, yPixel1);
		}
	}
	
	private static void drawTickLabels(Graphics2D g, int width, int height, PlotViewConfig config) {
		g.setFont(config.tickFont);
		g.setColor(config.textColor);
		FontMetrics fm = g.getFontMetrics();
		
		double xStep = Math.pow(10,
			Math.ceil(Math.log10((config.xMax - config.xMin) / width * config.minTickSpacing)));
		double yStep = Math.pow(10,
			Math.ceil(Math.log10((config.yMax - config.yMin) / height * config.minTickSpacing)));
		int xAxisPosY =
			Math.max(0, Math.min(height - 1, (int) (config.yMax / (config.yMax - config.yMin) * height)));
		int yAxisPosX =
			Math.max(0, Math.min(width - 1, (int) (-config.xMin / (config.xMax - config.xMin) * width)));
		
		for (int xPixel = 0; xPixel <= (config.xMax - config.xMin) / xStep; xPixel++) {
			double x = config.xMin + xPixel * xStep;
			int xPixel1 = (int) ((x - config.xMin) / (config.xMax - config.xMin) * width);
			if (Math.abs(xPixel1 - yAxisPosX) <= config.tickLength) continue;
			int yPixel = xAxisPosY + (int) config.tickLength + fm.getHeight();
			if (Math.abs(xPixel1 + config.xMin / xStep) % config.tickLabelSpacing == 0) {
				String text = String.format(config.xTickFormat, x);
				int textWidth = fm.stringWidth(text);
				int textX = Math.max(5, Math.min(width - textWidth - 5, xPixel1 - textWidth / 2));
				int textY = Math.max(fm.getHeight() + 5, Math.min(height - 5, yPixel));
				drawOutlinedText(g, text, textX, textY, config.tickFont, config.textColor,
					config.textOutlineColor, config.textOutline);
			}
		}
		
		for (int yPixel = 0; yPixel <= (config.yMax - config.yMin) / yStep; yPixel++) {
			double y = config.yMin + yPixel * yStep;
			int yPixel1 = (int) ((config.yMax - y) / (config.yMax - config.yMin) * height);
			if (Math.abs(yPixel1 - xAxisPosY) <= config.tickLength) continue;
			int xPixel = yAxisPosX - (int) config.tickLength - fm.stringWidth("0") - 5;
			if (Math.abs(yPixel1 + config.yMin / yStep) % config.tickLabelSpacing == 0) {
				String text = String.format(config.yTickFormat, y);
				int textWidth = fm.stringWidth(text);
				int textX = Math.max(5, Math.min(width - textWidth - 5, xPixel - textWidth));
				int textY =
					Math.max(fm.getHeight() + 5, Math.min(height - 5, yPixel1 + fm.getHeight() / 2));
				drawOutlinedText(g, text, textX, textY, config.tickFont, config.textColor,
					config.textOutlineColor, config.textOutline);
			}
		}
	}
	
	private static void drawOutlinedText(Graphics2D g, String text, int x, int y, Font font,
		Color textColor, Color outlineColor, int outlineWidth) {
		g.setFont(font);
		g.setColor(outlineColor);
		for (int dx = -outlineWidth; dx <= outlineWidth; dx++) {
			for (int dy = -outlineWidth; dy <= outlineWidth; dy++) {
				g.drawString(text, x + dx, y + dy);
			}
		}
		g.setColor(textColor);
		g.drawString(text, x, y);
	}
	
	private static void drawGrid(Graphics2D g, int width, int height, PlotViewConfig config) {
		double xStep = Math.pow(10,
			Math.ceil(Math.log10((config.xMax - config.xMin) / width * config.minGridSpacing)));
		double yStep = Math.pow(10,
			Math.ceil(Math.log10((config.yMax - config.yMin) / height * config.minGridSpacing)));
		
		g.setColor(config.gridColor);
		g.setStroke(new BasicStroke(config.gridWidth, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
		for (int xPixel = 0; xPixel <= (config.xMax - config.xMin) / xStep; xPixel++) {
			double x = config.xMin + xPixel * xStep;
			int xPixel1 = (int) ((x - config.xMin) / (config.xMax - config.xMin) * width);
			g.drawLine(xPixel1, 0, xPixel1, height);
		}
		for (int yPixel = 0; yPixel <= (config.yMax - config.yMin) / yStep; yPixel++) {
			double y = config.yMin + yPixel * yStep;
			int yPixel1 = (int) ((config.yMax - y) / (config.yMax - config.yMin) * height);
			g.drawLine(0, yPixel1, width, yPixel1);
		}
	}
	
	@Builder
	public static class PlotViewConfig {
		
		@Builder.Default
		public double xMin = -1;
		@Builder.Default
		public double xMax = 1;
		@Builder.Default
		public double yMin = -1;
		@Builder.Default
		public double yMax = 1;
		
		@Builder.Default
		@Range (from = 1, to = 10)
		public float strokeWeight = 1;
		@Builder.Default
		@Range (from = 1, to = 10)
		public float tickWidth = 1;
		@Builder.Default
		@Range (from = 1, to = 10)
		public float axisWidth = 2;
		@Builder.Default
		@Range (from = 1, to = 10)
		public float gridWidth = 1;
		
		@Builder.Default
		@Range (from = 10, to = 1000)
		public int minGridSpacing = 20;
		@Builder.Default
		@Range (from = 10, to = 1000)
		public int minTickSpacing = 50;
		
		@Builder.Default
		@Range (from = 1, to = 100)
		public float tickLength = 5;
		
		@Builder.Default
		public boolean drawGrid = true;
		@Builder.Default
		public boolean drawAxis = true;
		@Builder.Default
		public boolean drawTicks = true;
		
		@Builder.Default
		public boolean drawAxisLabels = false;
		@Builder.Default
		public boolean drawTickLabels = false;
		@Builder.Default
		@Range (from = 1, to = 10)
		public int tickLabelSpacing = 2;
		
		@Builder.Default
		@NotNull
		public String xLabel = "X";
		@Builder.Default
		@NotNull
		public String yLabel = "Y";
		@Builder.Default
		@NotNull
		public String xTickFormat = "%.1f";
		@Builder.Default
		@NotNull
		public String yTickFormat = "%.1f";
		
		@Builder.Default
		@NotNull
		public Color backgroundColor = Color.WHITE;
		@Builder.Default
		@NotNull
		public Color gridColor = Color.LIGHT_GRAY;
		@Builder.Default
		@NotNull
		public Color axisColor = Color.BLACK;
		@Builder.Default
		@NotNull
		public Color tickColor = Color.BLACK;
		@Builder.Default
		@NotNull
		public Color plotColor = Color.BLACK;
		@Builder.Default
		@NotNull
		public Color textColor = Color.BLACK;
		@Builder.Default
		@NotNull
		public Color textOutlineColor = Color.WHITE;
		
		@Builder.Default
		@NotNull
		public Font axisFont = new Font("Arial", java.awt.Font.PLAIN, 16);
		@Builder.Default
		@NotNull
		public Font tickFont = new Font("Arial", java.awt.Font.PLAIN, 10);
		@Builder.Default
		@Range (from = 0, to = 10)
		public int textOutline = 1;
		
	}
	
}
