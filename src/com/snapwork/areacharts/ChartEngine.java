package com.snapwork.areacharts;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.UiApplication;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.beans.ChartItem;
import com.snapwork.util.Debug;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.view.CompanyDetailsSnippetsScreen;

public class ChartEngine {

	private ChartProperties chartProperties = null;
	private String chartFullScreenTitle = null;
	private boolean requireFullScreen = false;

	public final static byte CHART_BSE_NSE = 0;
	public final static byte CHART_COMPANY_DETAIL = 1;
	public final static byte CHART_WEEK_CHART = 2;

	private byte chartType = 0;
	private String companyCode  = "";
	private int change = 1;

	public static ChartEngine getInstance(ChartProperties chartProperties,Vector chartData,boolean requireFullScreen,byte chartType) {

		ChartEngine chartComponent = new ChartEngine();

		if(chartProperties==null) {
			chartComponent.setChartProperties(ChartProperties.getDefaultChartProperties());
		} else {
			chartComponent.setChartProperties(chartProperties);
		}

		if(chartData==null) {
			chartComponent.chartFullScreenTitle = "";
			chartComponent.companyCode = "";
		} else {
			chartComponent.chartFullScreenTitle = chartData.elementAt(0).toString();
			if(chartType == CHART_COMPANY_DETAIL)
				chartComponent.companyCode = chartData.elementAt(1).toString();
		}
		chartComponent.requireFullScreen = requireFullScreen;
		chartComponent.chartType = chartType;

		return chartComponent;
	}

	public ChartProperties getChartProperties() {
		return chartProperties;
	}

	public void setChartProperties(ChartProperties chartProperties) {
		this.chartProperties = chartProperties;
	}

	public Field createChart(final Vector vector,double minYAxisValue,double maxYAxisValue,double midValue) {
		try {
			
			ChartComponent chartComponentManager = null;
			LOG.print(minYAxisValue+"*===-=-=--=--=");
			LOG.print(maxYAxisValue+"*-=-=-=--=-=-=-=-=-=-");
			LOG.print(midValue+"*-=-=-=--=-=-=-=-=-=-");
			if(midValue>0) {
				if(midValue<=minYAxisValue) {
					if(maxYAxisValue-midValue<30)
					{
						minYAxisValue = minYAxisValue * 100;
						midValue = midValue * 100;
						maxYAxisValue = maxYAxisValue * 100;
						minYAxisValue = midValue-((maxYAxisValue-midValue)/3);//0.1;
						maxYAxisValue = maxYAxisValue +((maxYAxisValue-midValue)/3);
						minYAxisValue = minYAxisValue / 100;
						midValue = midValue / 100;
						maxYAxisValue = maxYAxisValue / 100;
					}
					else
						minYAxisValue = midValue-30;//30 is not hard coded here it is there to view the midLines atleast
				} else if(maxYAxisValue<=midValue) {
					if(midValue-minYAxisValue<30)
					{
						minYAxisValue = minYAxisValue * 100;
						midValue = midValue * 100;
						maxYAxisValue = maxYAxisValue * 100;
						maxYAxisValue = midValue+((midValue-minYAxisValue)/3);
						minYAxisValue = minYAxisValue - ((midValue-minYAxisValue)/3);
						minYAxisValue = minYAxisValue / 100;
						midValue = midValue / 100;
						maxYAxisValue = maxYAxisValue / 100;
					}
					else
						maxYAxisValue = midValue+30;//30 is not hard coded here it is there to view the midLines atleast
				}
				else
				{
					if(maxYAxisValue-minYAxisValue<30)
					{
						minYAxisValue = minYAxisValue * 100;
						midValue = midValue * 100;
						maxYAxisValue = maxYAxisValue * 100;
						maxYAxisValue = maxYAxisValue+((maxYAxisValue-minYAxisValue)/3);
						minYAxisValue = minYAxisValue - ((maxYAxisValue-minYAxisValue)/3);
						minYAxisValue = minYAxisValue / 100;
						midValue = midValue / 100;
						maxYAxisValue = maxYAxisValue / 100;
					}
				}
				
			}
			
			
			
			LOG.print(minYAxisValue+"===-=-=--=--=");
			LOG.print(maxYAxisValue+"-=-=-=--=-=-=-=-=-=-");
			LOG.print(midValue+"-=-=-=--=-=-=-=-=-=-");

			if(maxYAxisValue-minYAxisValue<30)
			{
				change = 100;
				maxYAxisValue = maxYAxisValue*change;
				minYAxisValue = minYAxisValue*change;
				midValue = midValue*change;
			}
			else
			{
				maxYAxisValue = Math.ceil(maxYAxisValue);
				minYAxisValue = Math.floor(minYAxisValue);
			}
			LOG.print(minYAxisValue+"updated ===-=-=--=--=");
			LOG.print(maxYAxisValue+"updated -=-=-=--=-=-=-=-=-=-");
			LOG.print(midValue+"updated -=-=-=--=-=-=-=-=-=-");

			//calculate YAxis Start Label and end Labels
			int yAxisStartLabel = (int)Math.floor((minYAxisValue/100))*100;
			int yAxisEndLabel = (int)Math.ceil((maxYAxisValue/100))*100;
			LOG.print(minYAxisValue+"yAxisStartLabel-=-=----===-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-"+yAxisStartLabel);
			LOG.print(maxYAxisValue+"yAxisEndLabel-=-=----===-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-"+yAxisEndLabel);

			if(((maxYAxisValue-minYAxisValue)/5)==0) {
				yAxisStartLabel = (int)(minYAxisValue);
				yAxisEndLabel = yAxisStartLabel + 10;
			} else {
				yAxisStartLabel = (int)minYAxisValue;
				yAxisEndLabel = yAxisStartLabel + ((int)Math.ceil(((maxYAxisValue-minYAxisValue)/5)))*5;
			}
			
			if(yAxisStartLabel<0)
				yAxisStartLabel = yAxisStartLabel*(-1);
			LOG.print("yAxisStartLabel-=-=----===-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-"+yAxisStartLabel);
			LOG.print("yAxisEndLabel-=-=----===-=-=-=-=-=-=-=-=-=-=-=--=-=-=-=-=-=-"+yAxisEndLabel);

			ChartYAxis yAxis = createYAxis(yAxisStartLabel,yAxisEndLabel);
			ChartXAxis xAxis;
			if(chartType==ChartEngine.CHART_WEEK_CHART) {
				xAxis = createXAxisForWeekChart(vector,(short)(getChartProperties().getChartWidth() - yAxis.getyAxisWidth()));							
			} else { 
				xAxis = createXAxis(vector,(short)(getChartProperties().getChartWidth() - yAxis.getyAxisWidth()));
			}

			Bitmap chartBitmap = createChart(vector,yAxis,xAxis,midValue);
			Bitmap chartBG = createChartBG(vector,yAxis,xAxis,midValue);
			if(UiApplication.getUiApplication().getActiveScreen() instanceof CompanyDetailsSnippetsScreen)
			{
				chartComponentManager = new ChartComponent(Field.FIELD_VCENTER | Field.FIELD_HCENTER | Field.NON_FOCUSABLE, yAxis,xAxis, chartBitmap,chartBG, getChartProperties().getChartWidth(), getChartProperties().getChartHeight()+getChartProperties().getChartxAxisHeight());
			}
			else if(requireFullScreen) {
				chartComponentManager = new ChartComponent(Field.FIELD_VCENTER | Field.FIELD_HCENTER | Field.FOCUSABLE,yAxis,xAxis, chartBitmap,chartBG, getChartProperties().getChartWidth(), getChartProperties().getChartHeight()+getChartProperties().getChartxAxisHeight()) {
					protected boolean navigationClick(int status, int time) {
						
						Vector commandVector = new Vector();
						commandVector.addElement(chartFullScreenTitle);
						switch(chartType) {
						case CHART_BSE_NSE:
							commandVector.addElement(vector);
							Action actionBSENSEChart = new Action(ActionCommand.CMD_FULL_SCREEN_CHART, commandVector);
							ActionInvoker.processCommand(actionBSENSEChart);
							break;
						case CHART_COMPANY_DETAIL:
							commandVector.addElement(companyCode);
							Action action = new Action(ActionCommand.CMD_COMPANY_FULL_INTRA_CHART, commandVector);
							ActionInvoker.processCommand(action);
							break;
						}
						return super.navigationClick(status, time);
					}
				};
			} else {
				chartComponentManager = new ChartComponent(Field.FIELD_VCENTER | Field.FIELD_HCENTER | Field.FOCUSABLE, yAxis,xAxis, chartBitmap,chartBG, getChartProperties().getChartWidth(), getChartProperties().getChartHeight()+getChartProperties().getChartxAxisHeight());
			}
			return chartComponentManager;

		} catch(Exception ex) {
			/*UiApplication.getUiApplication().invokeLater(new Runnable() {

				public void run() {
					ScreenInvoker.showDialog("Error Creating chart..Please try again later.");	
				}
			});*/
		}
		return null;
	}

	public ChartYAxis createYAxis(int yAxisStartLabel,int yAxisEndLabel) {
		ChartYAxis yAxis = new ChartYAxis(yAxisStartLabel, yAxisEndLabel,this.getChartProperties().getChartHeight(),this,change);
		yAxis.createBitmap();
		return yAxis;
	}

	public ChartXAxis createXAxis(Vector vector,short xAxiswidth) {
		ChartXAxis chartXAxis = new ChartXAxis((short)((vector.size()-3)/8),(short)8,xAxiswidth,this);
		chartXAxis.createBitmap(vector);
		return chartXAxis;
	}

	public ChartXAxis createXAxisForWeekChart(Vector vector,short xAxiswidth) {
		ChartXAxis chartXAxis = new ChartXAxis((short)((vector.size()-3)/5),(short)5,xAxiswidth,this);
		chartXAxis.createBitmap(vector);
		return chartXAxis;
	}

	public Bitmap createChartBG(Vector vector,ChartYAxis yAxis,ChartXAxis xAxis,double midPointValue) {
		int chartWidth = getChartProperties().getChartWidth() - yAxis.getyAxisWidth();
		int chartHeight = yAxis.getyAxisHeight();

		Bitmap chartBG = new Bitmap( chartWidth, chartHeight);
		int relativePercent = (int)Math.ceil( (yAxis.getyAxisValueDistance() * 100) / yAxis.getyAxisValueDiffrence() );
		Graphics graphics = new Graphics(chartBG);

		//Paint black background
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, chartBG.getWidth(), chartBG.getHeight());

		//DrawY Lines
		graphics.setColor(getChartProperties().getChartAxisLineColor());
		for(int yAxisStartLabel = yAxis.getyAxisStartLabel() + yAxis.getyAxisValueDiffrence();yAxisStartLabel<yAxis.getyAxisEndLabel();yAxisStartLabel+=yAxis.getyAxisValueDiffrence()) {
			graphics.setStrokeWidth(1);
			graphics.drawLine(0, ((yAxisStartLabel-yAxis.getyAxisStartLabel())/yAxis.getyAxisValueDiffrence())*yAxis.getyAxisValueDistance(),chartWidth, ((yAxisStartLabel-yAxis.getyAxisStartLabel())/yAxis.getyAxisValueDiffrence())*yAxis.getyAxisValueDistance());
			
		}

		//DrawX Lines
		graphics.setColor(getChartProperties().getChartAxisLineColor());
		for(int i  = 1 ; i < xAxis.getTotalColumns() ; i++) {
			graphics.setStrokeWidth(1);
			graphics.drawLine(xAxis.getxAxisWidth()/xAxis.getTotalColumns() * i,0 ,xAxis.getxAxisWidth()/xAxis.getTotalColumns() * i, chartHeight);
		}

		//Draw MidPoint Line
		if(midPointValue>0) {
			graphics.setColor(getChartProperties().getChartMidLineColor());
			graphics.drawLine(0, (int)(chartHeight - (((midPointValue - yAxis.getyAxisStartLabel())*relativePercent)/100)), chartBG.getWidth(), (int)(chartHeight - (((midPointValue - yAxis.getyAxisStartLabel())*relativePercent)/100)));
		}
		return chartBG;
	}

	public Bitmap createChart(Vector vector,ChartYAxis yAxis,ChartXAxis xAxis,double midPointValue) {

		int chartReqWidth = getChartProperties().getChartWidth() - yAxis.getyAxisWidth(); 
		int chartWidth = vector.size()-3;
		int chartHeight = yAxis.getyAxisHeight();

		Bitmap chartBitmap = new Bitmap( chartWidth , chartHeight);

		int relativePercent = (int)Math.ceil( (yAxis.getyAxisValueDistance() * 100) / yAxis.getyAxisValueDiffrence() ); 

		Graphics graphics = new Graphics(chartBitmap);

		//Paint black background
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, chartBitmap.getWidth(), chartBitmap.getHeight());

		//draw Chart
		graphics.setColor(getChartProperties().getChartCoveredAreaColor());
		for(int i=0;i<vector.size()-3;i++) {
			ChartItem chartItem = (ChartItem) vector.elementAt(i);
			graphics.setStrokeWidth(1);
			graphics.drawLine(i, chartHeight, i, (int)(chartHeight - (((chartItem.getValue()*change - yAxis.getyAxisStartLabel())*relativePercent)/100)));
		}

		//DrawY Lines
		graphics.setColor(getChartProperties().getChartAxisLineColor());
		for(int yAxisStartLabel = yAxis.getyAxisStartLabel() + yAxis.getyAxisValueDiffrence();yAxisStartLabel<yAxis.getyAxisEndLabel();yAxisStartLabel+=yAxis.getyAxisValueDiffrence()) {
			graphics.setStrokeWidth(1);
			graphics.drawLine(0, ((yAxisStartLabel-yAxis.getyAxisStartLabel())/yAxis.getyAxisValueDiffrence())*yAxis.getyAxisValueDistance(),chartWidth, ((yAxisStartLabel-yAxis.getyAxisStartLabel())/yAxis.getyAxisValueDiffrence())*yAxis.getyAxisValueDistance());
		}

		chartBitmap = ChartImageUtils.resizeBitmap(chartBitmap, chartReqWidth, chartHeight);
		graphics = new Graphics(chartBitmap);

		//DrawX Lines
		graphics.setColor(getChartProperties().getChartAxisLineColor());
		for(int i  = 1 ; i < xAxis.getTotalColumns() ; i++) {
			graphics.setStrokeWidth(1);
			graphics.drawLine(xAxis.getxAxisWidth()/xAxis.getTotalColumns() * i,0 ,xAxis.getxAxisWidth()/xAxis.getTotalColumns() * i, chartHeight);
		}

		//Draw MidPoint Line
		if(midPointValue>0) {
			graphics.setColor(getChartProperties().getChartMidLineColor());
			graphics.drawLine(0, (int)(chartHeight - (((midPointValue - yAxis.getyAxisStartLabel())*relativePercent)/100)), chartBitmap.getWidth(), (int)(chartHeight - (((midPointValue - yAxis.getyAxisStartLabel())*relativePercent)/100)));
		}

		return ChartImageUtils.smoothTheGraphEdges(chartBitmap,getChartProperties());
	}

}