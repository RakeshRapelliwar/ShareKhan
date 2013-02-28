package com.snapwork.areacharts;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import com.snapwork.util.Debug;
import com.snapwork.util.LOG;

public class ChartComponent extends Field
{
	private int managerWidth = 0;
	private int managerHeight = 0;
	private Bitmap xAxisBitmap = null,yAxisBitmap = null,chartBitmap = null,chartBG = null;
	private boolean isFocused = false;
	//Animation Thread
	private Thread animationThread = null;
	private int animationCounter = 1;
	private byte padding = 4; 
	public static boolean isDrawFinished = true;

	ChartComponent(long style, ChartYAxis yAxis,ChartXAxis xAxis,Bitmap chartBitmap,Bitmap chartBG,int width,int height)
	{
		super(style);
		managerWidth = width;
		managerHeight = height;
		this.xAxisBitmap = xAxis.getBitmap();
		this.yAxisBitmap = yAxis.getBitmap();
		this.chartBitmap = chartBitmap;
		this.chartBG = chartBG;
	}

	protected void onFocus(int direction) {
		isFocused = true;
		invalidate();
	}

	protected void onUnfocus() {
		isFocused = false;
		invalidate();
	}

	protected void paintBackground(Graphics graphics) {
		graphics.setBackgroundColor(Color.BLACK);
		graphics.clear();
	}

	protected void paint(Graphics graphics)
	{
		graphics.setColor((isFocused == true ? Color.ORANGE : Color.BLACK));
		graphics.fillRect(0, 0, managerWidth+padding, managerHeight+padding);

		//Draw Y Axis
		graphics.drawBitmap(padding/2, padding/2, yAxisBitmap.getWidth(), yAxisBitmap.getHeight(), yAxisBitmap, 0, 0);
		//Draw Chart BG With Lines
		graphics.drawBitmap(yAxisBitmap.getWidth()-1+padding/2, padding/2, chartBG.getWidth(), chartBitmap.getHeight(), chartBG, 0, 0);
		//Draw Chart
		graphics.drawBitmap(yAxisBitmap.getWidth()-1+padding/2, padding/2, animationCounter, chartBitmap.getHeight(), chartBitmap, 0, 0);
		//Draw empty black space
		graphics.setColor(Color.BLACK);
		graphics.fillRect(padding/2, chartBitmap.getHeight()+padding/2-1, yAxisBitmap.getWidth()-1+padding/2, xAxisBitmap.getHeight());
		//Draw X Axis
		graphics.drawBitmap(yAxisBitmap.getWidth()-1+padding/2, chartBitmap.getHeight()+padding/2-1, xAxisBitmap.getWidth(), xAxisBitmap.getHeight(), xAxisBitmap, 0, 0);

		if(animationThread == null)
		{
			animationThread = new Thread(new Runnable()
			{

				public void run()
				{
					
					while(animationCounter<=chartBitmap.getWidth())
					{
						try
						{
							animationCounter+=5;
							//isDrawFinished = false;
							invalidate();
							Thread.sleep(40);
						}
						catch(Exception ex)
						{
							Debug.debug("Error : "+ex.toString());
						}
					}
					/*if(animationCounter>=chartBitmap.getWidth())
					{
						isDrawFinished = true;
						LOG.print("TRUE________________________+++++++++++++++++++++");
					}*/
					animationCounter = chartBitmap.getWidth();
				}
			});
			animationThread.start();
		}

	}

	protected void drawFocus(Graphics graphics, boolean on) {
		//Dont do anything and let the system
	}

	protected void layout(int width, int height) {
		setExtent(managerWidth+padding, managerHeight+padding);
	}

}
