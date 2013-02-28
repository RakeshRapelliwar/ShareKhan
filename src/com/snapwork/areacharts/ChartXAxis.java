package com.snapwork.areacharts;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import com.snapwork.beans.ChartItem;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;


public class ChartXAxis
{
	private short totalRecordsPerCloumn = 0;
	private short recordsPerColumn = 0;
	private short xAxisWidth = 0;
	private short xAxisHeight = 0;
	private short totalColumns = 0;
	private ChartEngine chartEngine = null;
	private Bitmap chartXAxisBitmap = null;

	public ChartXAxis(short totalRecordsPerCloumn,short totalColumns,short xAxisWidth,ChartEngine chartEngine) {
		setTotalRecordsPerColumn(totalRecordsPerCloumn);
		setTotalColumns(totalColumns);
		setxAxisWidth(xAxisWidth);
		setChartEngine(chartEngine);
	}

	public void createBitmap(Vector vector) {

		try
		{
			//create Fonts
			Font xAxisFont = FontLoader.getFont(getChartEngine().getChartProperties().getChartXAxisLabelFontType());

			//Caluculate height
			setxAxisHeight((short)(xAxisFont.getHeight()+getChartEngine().getChartProperties().getPadding()));

			//Create Bitmap
			chartXAxisBitmap = new Bitmap(getxAxisWidth(), getxAxisHeight());

			Graphics graphicsXAxis = new Graphics(chartXAxisBitmap);
			//paint blackbackground
			graphicsXAxis.setColor(Color.BLACK);
			graphicsXAxis.fillRect(0, 0, chartXAxisBitmap.getWidth(), chartXAxisBitmap.getHeight());
			//drawLine
			graphicsXAxis.setStrokeWidth(getChartEngine().getChartProperties().getChartAxisLineWidth());
			graphicsXAxis.setColor(getChartEngine().getChartProperties().getChartXYAxisLineColor());
			graphicsXAxis.drawLine(0, 0, getxAxisWidth(), 0);

			//draw X Axis Labels
			int j = 1;
			short xAxisHeight = getChartEngine().getChartProperties().getPadding();
			short xAxisLabelXPosition = (short)((getxAxisWidth()/getTotalColumns())*j);
			Debug.debug("getTotalColumns() : "+getTotalColumns());
			while(j<(getTotalColumns()))
			{
				ChartItem chartItem  = (ChartItem)vector.elementAt(getTotalRecordsPerColumn()*j);
				String xAxisLabel = chartItem.getTime();
				Debug.debug("xAxisLabel : "+xAxisLabel +" : "+j);

				Bitmap textBitmap = createTextBitmap(xAxisFont,getChartEngine().getChartProperties().getChartFontColor(), xAxisLabel);
				if(j==0)
				{
					graphicsXAxis.drawBitmap(xAxisLabelXPosition , xAxisHeight, textBitmap.getWidth(), textBitmap.getHeight(), textBitmap, 0, 0);					
				}
				else
				{
					graphicsXAxis.drawBitmap(xAxisLabelXPosition - textBitmap.getWidth()/2 , xAxisHeight, textBitmap.getWidth(), textBitmap.getHeight(), textBitmap, 0, 0);
				}
				j++;
				xAxisLabelXPosition += (getxAxisWidth()/getTotalColumns());
			}
		}
		catch(Exception ex)
		{
			Debug.debug(ex.toString());
		}
	}

	public Bitmap createTextBitmap(Font textFont,int textColor,String text)
	{
		Bitmap textBitmap = new Bitmap(textFont.getAdvance(text),textFont.getHeight());
		Graphics textGraphics = new Graphics(textBitmap);
		textGraphics.setColor(Color.BLACK);
		textGraphics.fillRect(0, 0, textBitmap.getWidth(), textBitmap.getHeight());
		textGraphics.setFont(textFont);
		textGraphics.setColor(textColor);
		textGraphics.drawText(text, 0, 0);
		textBitmap = ChartImageUtils.rotateBitmap(textBitmap, 0);
		return textBitmap;
	}

	public Bitmap getBitmap()
	{
		return chartXAxisBitmap;
	}

	public short getTotalRecordsPerColumn()
	{
		return totalRecordsPerCloumn;
	}
	public void setTotalRecordsPerColumn(short totalRecordsPerCloumn)
	{
		this.totalRecordsPerCloumn = totalRecordsPerCloumn;
	}
	public short getRecordsPerColumn()
	{
		return recordsPerColumn;
	}

	public void setRecordsPerColumn(short recordsPerColumn)
	{
		this.recordsPerColumn = recordsPerColumn;
	}

	public short getxAxisWidth()
	{
		return xAxisWidth;
	}

	public void setxAxisWidth(short xAxisWidth)
	{
		this.xAxisWidth = xAxisWidth;
	}

	public short getxAxisHeight() {
		return xAxisHeight;
	}

	public void setxAxisHeight(short xAxisHeight) {
		this.xAxisHeight = xAxisHeight;
	}

	public ChartEngine getChartEngine()
	{
		return chartEngine;
	}

	public void setChartEngine(ChartEngine chartEngine)
	{
		this.chartEngine = chartEngine;
	}

	public short getTotalColumns()
	{
		return totalColumns;
	}

	public void setTotalColumns(short totalColumns)
	{
		this.totalColumns = totalColumns;
	}
}
