package com.snapwork.areacharts;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import com.snapwork.util.FontLoader;
import com.snapwork.util.LOG;

public class ChartYAxis
{
	private int yAxisWidth = 0;
	private int yAxisHeight = 0;
	private int yAxisStartLabel = 0;
	private int yAxisEndLabel = 0;
	private int yAxisValueDiffrence = 0;
	private int yAxisValueDistance = 0;
	private Bitmap yAxisBitmap = null;
	private ChartEngine chartComponent = null;
	private int change;

	ChartYAxis(int yAxisStartLabel,int yAxisEndLabel,int yAxisHeight,ChartEngine chartComponet,int change) {
		setyAxisStartLabel(yAxisStartLabel);
		setyAxisEndLabel(yAxisEndLabel);
		setyAxisHeight(yAxisHeight);
		setChartComponent(chartComponet);
		setChange(change);
	}
public void setChange(int paramInt)
{
	change = paramInt;
	}
	public ChartEngine getChartComponent() {
		return chartComponent;
	}
	public void setChartComponent(ChartEngine chartComponent) {
		this.chartComponent = chartComponent;
	}
	public int getyAxisWidth() {
		return yAxisWidth;
	}
	public void setyAxisWidth(int yAxisWidth) {
		this.yAxisWidth = yAxisWidth;
	}
	public int getyAxisHeight() {
		return yAxisHeight;
	}
	public void setyAxisHeight(int yAxisHeight) {
		this.yAxisHeight = yAxisHeight;
	}
	public int getyAxisStartLabel() {
		return yAxisStartLabel;
	}
	public void setyAxisStartLabel(int yAxisStartLabel) {
		this.yAxisStartLabel = yAxisStartLabel;
	}
	public int getyAxisEndLabel() {
		return yAxisEndLabel;
	}
	public void setyAxisEndLabel(int yAxisEndLabel) {
		this.yAxisEndLabel = yAxisEndLabel;
	}
	public int getyAxisValueDiffrence() {
		return yAxisValueDiffrence;
	}
	public void setyAxisValueDiffrence(int yAxisValueDiffrence) {
		this.yAxisValueDiffrence = yAxisValueDiffrence;
	}
	public int getyAxisValueDistance() {
		return yAxisValueDistance;
	}
	public void setyAxisValueDistance(int yAxisValueDistance) {
		this.yAxisValueDistance = yAxisValueDistance;
	}

	public void createBitmap() {
		//create Fonts
		Font yAxisFont = FontLoader.getFont(getChartComponent().getChartProperties().getChartYAxisLabelFontType());

		//Calculate Width and height
		setyAxisWidth(yAxisFont.getAdvance(change==1?Integer.toString(yAxisEndLabel):Integer.toString(yAxisEndLabel)+".")+getChartComponent().getChartProperties().getPadding());

		setyAxisValueDiffrence((yAxisEndLabel - yAxisStartLabel)/5);//Statics related property

		if(getyAxisHeight()>getChartComponent().getChartProperties().getChartHeight()) {
			setyAxisHeight(getChartComponent().getChartProperties().getChartHeight());
		}

		setyAxisValueDistance(getyAxisHeight()/5);//Graphics/drawing related property

		//Create Image
		yAxisBitmap = new Bitmap(getyAxisWidth(), getyAxisHeight());
		Graphics yAxisBitmapGraphics = new Graphics(yAxisBitmap);
		//Paint Black background
		yAxisBitmapGraphics.setColor(Color.BLACK);
		yAxisBitmapGraphics.fillRect(0, 0, yAxisBitmap.getWidth(), yAxisBitmap.getHeight());

		yAxisBitmapGraphics.setFont(yAxisFont);

		//draw Axis Line
		yAxisBitmapGraphics.setColor(getChartComponent().getChartProperties().getChartXYAxisLineColor());
		yAxisBitmapGraphics.setStrokeWidth(getChartComponent().getChartProperties().getChartAxisLineWidth());
		yAxisBitmapGraphics.drawLine( (getyAxisWidth() - getChartComponent().getChartProperties().getChartAxisLineWidth()), 0, (getyAxisWidth() - getChartComponent().getChartProperties().getChartAxisLineWidth()) , getyAxisHeight());

		//draw Y Axis Labels
		int yAxisDrawOffset = getyAxisHeight() - getyAxisValueDistance();
		int yAxisStartLabel = getyAxisStartLabel() + getyAxisValueDiffrence();
		yAxisBitmapGraphics.setColor(getChartComponent().getChartProperties().getChartFontColor());
		while(yAxisStartLabel<=getyAxisEndLabel())
		{
			String str = Integer.toString(yAxisStartLabel);
			LOG.print(">>>>>>>>>>>>>>>>>>>>>"+str);
			String len = Integer.toString(change);
			if(change!=1)
				str = str.substring(0, str.length()-len.length()+1)+"."+ str.substring(str.length()-len.length()+1,str.length());
			
			yAxisBitmapGraphics.drawText(str, 0, yAxisDrawOffset);
			yAxisStartLabel += getyAxisValueDiffrence();
			yAxisDrawOffset = yAxisDrawOffset - getyAxisValueDistance();
		}
	}

	public Bitmap getBitmap() {
		return yAxisBitmap;
	}

} 