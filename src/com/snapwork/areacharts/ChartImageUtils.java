package com.snapwork.areacharts;

import com.snapwork.util.Debug;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Graphics;

public class ChartImageUtils {

	private static int[] rescaleArray(int[] ini, int x, int y, int x2, int y2)
	{
		int out[] = new int[x2 * y2];
		for (int yy = 0; yy < y2; yy++)
		{
			int dy = yy * y / y2;
			for (int xx = 0; xx < x2; xx++)
			{
				int dx = xx * x / x2;
				out[(x2 * yy) + xx] = ini[(x * dy) + dx];
			}
		}
		ini = null;
		return out;
	}

	public static Bitmap resizeBitmap(Bitmap image, int width, int height)
	{
		if (image.getWidth() == width && image.getHeight() == height)
		{
			return image;
		}

		int rgb[] = new int[image.getWidth() * image.getHeight()];

		image.getARGB(rgb, 0, image.getWidth(), 0, 0, image.getWidth(),image.getHeight());

		int rgb2[] = rescaleArray(rgb, image.getWidth(), image.getHeight(),width, height);

		Bitmap temp2 = new Bitmap(width, height);

		temp2.setARGB(rgb2, 0, width, 0, 0, width, height);

		rgb2 = null;
		rgb = null;

		return temp2;
	}

	public static Bitmap smoothTheGraphEdges(Bitmap image,ChartProperties chartProperties) {
		int emptyPixelPosition = 0;
		boolean isProcessCompleted = false;
		int height = image.getHeight();
		int width = image.getWidth();

		int rgb[] = new int[width * height];
		image.getARGB(rgb, 0, width, 0, 0, width,height);

		int spikeLineArgbColor = convertRGBtoARGB(chartProperties.getChartSpikeLineColor());

		try {
			int i,j;
			for(i=0;i<1;i++) {
				for (j = 0; j < height; j++) {
					if ( (rgb[i + (j * width)] & 16777215) == chartProperties.getChartCoveredAreaColor()) {
						emptyPixelPosition = j;
						rgb[i + (j * width)] = spikeLineArgbColor;
						j = height;
					}
				}
			}

			for (i = 0; i < width; i++) {
				isProcessCompleted = false;
				for (j = 0; j < height; j++) {
					if ( (rgb[i + (j * width)] & 16777215) ==  chartProperties.getChartCoveredAreaColor()) {
						if (j < emptyPixelPosition) {
							for (int k = emptyPixelPosition; k != (j-1); k--) {
								rgb[i + (k * width)] = spikeLineArgbColor;
							}
						} else {
							for (int k = emptyPixelPosition; k < (j+1); k++) {
								rgb[i + (k * width)] = spikeLineArgbColor;
							}
						}
						emptyPixelPosition = j;
						isProcessCompleted = true;
						j = height;
					}
				}
				if(isProcessCompleted==false) {
					if ( (rgb[i + (emptyPixelPosition * width)] & 16777215) ==  chartProperties.getChartAxisLineColor()) {
						rgb[i + (emptyPixelPosition * width)] = spikeLineArgbColor;
					} else {
						for (int k = emptyPixelPosition; k < height; k++) {
							rgb[i + (k * width)] = spikeLineArgbColor;
						}
						emptyPixelPosition = height-1;
					}
				}
			}

			Bitmap bitmap = new Bitmap(width,height);
			bitmap.setARGB(rgb, 0, width, 0, 0, width, height);

			rgb = null;
			return bitmap;
		} catch(Exception ex) {
			Debug.debug("Error in Chart Image Processing : "+ex.toString());
		}

		return null;
	}

	public static Bitmap rotateBitmap(Bitmap bitmap, int angle) {
		if (angle == 0) {
			return bitmap;
		}

		int width = bitmap.getWidth();
		int height = bitmap.getHeight();

		int[] rowData = new int[width];
		int[] rotatedData = new int[width * height];

		int rotatedIndex = 0;
		for (int i = 0; i < height; i++) {
			bitmap.getARGB(rowData, 0, width, 0, i, width, 1);

			for (int j = 0; j < width; j++) {
				rotatedIndex = angle == 90 ? (height - i - 1) + j * height
						: (angle == 270 ? i + height * (width - j - 1) : width
								* height - (i * width + j) - 1);

				rotatedData[rotatedIndex] = rowData[j];
			}
		}
		Bitmap rotatedBitmap;
		if (angle == 90 || angle == 270) {
			rotatedBitmap = new Bitmap(height, width);
			rotatedBitmap.setARGB(rotatedData, 0, height, 0, 0, height, width);
		} else {
			rotatedBitmap = new Bitmap(width, height);
			rotatedBitmap.setARGB(rotatedData, 0, width, 0, 0, width, height);
		}

		rowData = null;
		rotatedData = null;

		return rotatedBitmap;
	}

	public static int convertRGBtoARGB(int color) {
		Bitmap bitmap = new Bitmap(1,1);
		Graphics graphics = new Graphics(bitmap);
		graphics.setColor(color);
		graphics.fillRect(0, 0, 1, 1);
		int rgb[] = new int[1];
		bitmap.getARGB(rgb, 0, 1, 0, 0, 1, 1);
		bitmap = null;
		graphics = null;
		return rgb[0];
	}
}
