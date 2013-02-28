package com.snapwork.areacharts;

import net.rim.device.api.ui.Color;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;

/**
 * In this class
 * <p>Values for all charts in this application loaded from this class.
 * <p>Charts - BSE, NSE, Company.
 * <p>Full Screen Chart are common for BSE, NSE and Company.
 *
 */

public class ChartProperties {
	private int chartWidth = 0;
	private int chartHeight = 0;
	private int chartBackColor = 0;
	private int chartBorderColor = 0;
	private int chartAxisLineColor = 0;
	private int chartSpikeLineColor = 0;
	private int chartCoveredAreaColor = 0;
	private byte padding = 0;
	private byte chartAxisLineWidth = 0;
	private int chartxAxisHeight = 0;
	private int chartXYAxisLineColor = 0;
	private int chartMidLineColor = 0;
	private byte chartYAxisLabelFontType = 0;
	private byte chartXAxisLabelFontType = 0;
	private int chartFontColor = 0;
	public int getChartWidth() {
		return chartWidth;
	}
	public void setChartWidth(int chartWidth) {
		this.chartWidth = chartWidth;
	}
	public int getChartHeight() {
		return chartHeight;
	}
	public void setChartHeight(int chartHeight) {
		this.chartHeight = chartHeight;
	}
	public int getChartxAxisHeight() {
		return chartxAxisHeight;
	}
	public void setChartxAxisHeight(int chartxAxisHeight) {
		this.chartxAxisHeight = chartxAxisHeight;
	}
	public int getChartBackColor() {
		return chartBackColor;
	}
	public void setChartBackColor(int chartBackColor) {
		this.chartBackColor = chartBackColor;
	}
	public int getChartBorderColor() {
		return chartBorderColor;
	}
	public void setChartBorderColor(int chartBorderColor) {
		this.chartBorderColor = chartBorderColor;
	}
	public int getChartAxisLineColor() {
		return chartAxisLineColor;
	}
	public void setChartAxisLineColor(int chartAxisLineColor) {
		this.chartAxisLineColor = chartAxisLineColor;
	}
	public int getChartSpikeLineColor() {
		return chartSpikeLineColor;
	}
	public void setChartSpikeLineColor(int chartSpikeLineColor) {
		this.chartSpikeLineColor = chartSpikeLineColor;
	}
	public int getChartMidLineColor() {
		return chartMidLineColor;
	}
	public void setChartMidLineColor(int chartMidLineColor) {
		this.chartMidLineColor = chartMidLineColor;
	}
	public int getChartAxisLineWidth() {
		return chartAxisLineWidth;
	}
	public void setChartAxisLineWidth(byte chartAxisLineWidth) {
		this.chartAxisLineWidth = chartAxisLineWidth;
	}
	public int getChartCoveredAreaColor() {
		return chartCoveredAreaColor;
	}
	public void setChartCoveredAreaColor(int chartCoveredAreaColor) {
		this.chartCoveredAreaColor = chartCoveredAreaColor;
	}
	public int getChartXYAxisLineColor() {
		return chartXYAxisLineColor;
	}
	public void setChartXYAxisLineColor(int chartXYAxisLineColor) {
		this.chartXYAxisLineColor = chartXYAxisLineColor;
	}
	public byte getPadding() {
		return padding;
	}
	public void setPadding(byte padding) {
		this.padding = padding;
	}
	public byte getChartYAxisLabelFontType() {
		return chartYAxisLabelFontType;
	}
	public void setChartYAxisLabelFontType(byte chartYAxisLabelFontType) {
		this.chartYAxisLabelFontType = chartYAxisLabelFontType;
	}
	public byte getChartXAxisLabelFontType() {
		return chartXAxisLabelFontType;
	}
	public void setChartXAxisLabelFontType(byte chartXAxisLabelFontType) {
		this.chartXAxisLabelFontType = chartXAxisLabelFontType;
	}
	public int getChartFontColor() {
		return chartFontColor;
	}
	public void setChartFontColor(int chartFontColor) {
		this.chartFontColor = chartFontColor;
	}

	/**
	 * 
	 * @return Set chart properties for Company chart
	 */
	public static ChartProperties getDefaultChartProperties() {
		ChartProperties chartProperties = new ChartProperties();
		if(AppConstants.screenWidth==320)
			chartProperties.setChartWidth(300);//
		else if(AppConstants.screenWidth==360)
		{
			chartProperties.setChartWidth(300);
		}
		else
			chartProperties.setChartWidth(300);
		if(AppConstants.screenHeight==240)
			chartProperties.setChartHeight(70);//60);//Should be divide by 5 element
		else if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
			chartProperties.setChartHeight(75);
		else if(AppConstants.screenHeight==640 && AppConstants.screenWidth==480)
			chartProperties.setChartHeight(115);
		else if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			chartProperties.setChartHeight(140);
		else if(AppConstants.screenHeight==480 && AppConstants.screenWidth==360)
			chartProperties.setChartHeight(130);
		else if(AppConstants.screenHeight==480)
		{
			chartProperties.setChartHeight(135);
		}
		else
			chartProperties.setChartHeight(75);
		chartProperties.setChartBackColor(16777215);
		chartProperties.setChartBorderColor(2763306);
		chartProperties.setChartAxisLineColor(2697257);//15790315
		chartProperties.setChartAxisLineWidth((byte)2);
		chartProperties.setChartSpikeLineColor(16777215);
		chartProperties.setChartCoveredAreaColor(4343106);
		chartProperties.setChartXYAxisLineColor(16777215);//2763306
		chartProperties.setChartMidLineColor(Color.YELLOW);
		chartProperties.setPadding((byte)3);
		chartProperties.setChartXAxisLabelFontType(AppConstants.EXTRA_SMALL_BOLD_FONT);
		chartProperties.setChartYAxisLabelFontType(AppConstants.EXTRA_SMALL_BOLD_FONT);
		chartProperties.setChartFontColor(16777215);
		chartProperties.setChartxAxisHeight(FontLoader.getFont(chartProperties.getChartXAxisLabelFontType()).getHeight()+chartProperties.getPadding());
		chartProperties.doPorting();
		return chartProperties;
	} 

	/**
	 * 
	 * @return Set chart properties for Market Screen shows BSE chart
	 */
	public static ChartProperties getBSEChartProperties() {
		ChartProperties chartProperties = new ChartProperties();
		if(AppConstants.screenWidth==320)
			chartProperties.setChartWidth(300);//
		else if(AppConstants.screenWidth==360)
		{
			chartProperties.setChartWidth(300);
		}
		else
			chartProperties.setChartWidth(300);
		if(AppConstants.screenHeight==240)
			chartProperties.setChartHeight(85);//60);//Should be divide by 5 element
		else if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
			chartProperties.setChartHeight(105);
		else if(AppConstants.screenHeight==640 && AppConstants.screenWidth==480)
			chartProperties.setChartHeight(135);
		else if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			chartProperties.setChartHeight(150);
		else if(AppConstants.screenHeight==480 && AppConstants.screenWidth==360)
			chartProperties.setChartHeight(150);
		else if(AppConstants.screenHeight==480)
		{
			chartProperties.setChartHeight(150);
		}
		else
			chartProperties.setChartHeight(95);
		chartProperties.setChartBackColor(16777215);
		chartProperties.setChartBorderColor(2763306);
		chartProperties.setChartAxisLineColor(2697257);//15790315
		chartProperties.setChartAxisLineWidth((byte)2);
		chartProperties.setChartSpikeLineColor(16777215);
		chartProperties.setChartCoveredAreaColor(4343106);
		chartProperties.setChartXYAxisLineColor(16777215);//2763306
		chartProperties.setChartMidLineColor(Color.YELLOW);
		chartProperties.setPadding((byte)3);
		chartProperties.setChartXAxisLabelFontType(AppConstants.EXTRA_SMALL_BOLD_FONT);
		chartProperties.setChartYAxisLabelFontType(AppConstants.EXTRA_SMALL_BOLD_FONT);
		chartProperties.setChartFontColor(16777215);
		chartProperties.setChartxAxisHeight(FontLoader.getFont(chartProperties.getChartXAxisLabelFontType()).getHeight()+chartProperties.getPadding());
		chartProperties.doPorting();
		return chartProperties;
	} 

	/**
	 * 
	 * @return Set chart properties for NSE chart
	 */
	public static ChartProperties getNSEChartProperties() {
		ChartProperties chartProperties = new ChartProperties();
		if(AppConstants.screenWidth==320)
			chartProperties.setChartWidth(300);//
		else if(AppConstants.screenWidth==360)
		{
			chartProperties.setChartWidth(300);
		}
		else
			chartProperties.setChartWidth(300);
		if(AppConstants.screenHeight==240)
			chartProperties.setChartHeight(110);//60);//Should be divide by 5 element
		else if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
			chartProperties.setChartHeight(120);
		else if(AppConstants.screenHeight==640 && AppConstants.screenWidth==480)
			chartProperties.setChartHeight(148);
		else if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			chartProperties.setChartHeight(160);
		else if(AppConstants.screenHeight==480 && AppConstants.screenWidth==360)
			chartProperties.setChartHeight(150);
		else if(AppConstants.screenHeight==480)
		{
			chartProperties.setChartHeight(150);
		}
		else
			chartProperties.setChartHeight(135);
		chartProperties.setChartBackColor(16777215);
		chartProperties.setChartBorderColor(2763306);
		chartProperties.setChartAxisLineColor(2697257);//15790315
		chartProperties.setChartAxisLineWidth((byte)2);
		chartProperties.setChartSpikeLineColor(16777215);
		chartProperties.setChartCoveredAreaColor(4343106);
		chartProperties.setChartXYAxisLineColor(16777215);//2763306
		chartProperties.setChartMidLineColor(Color.YELLOW);
		chartProperties.setPadding((byte)3);
		chartProperties.setChartXAxisLabelFontType(AppConstants.EXTRA_SMALL_BOLD_FONT);
		chartProperties.setChartYAxisLabelFontType(AppConstants.EXTRA_SMALL_BOLD_FONT);
		chartProperties.setChartFontColor(16777215);
		chartProperties.setChartxAxisHeight(FontLoader.getFont(chartProperties.getChartXAxisLabelFontType()).getHeight()+chartProperties.getPadding());
		chartProperties.doPorting();
		return chartProperties;
	}

	/**
	 * 
	 * @return Set chart properties for Full Screen Chart for BSE, NSE and Company(Daily, Weekly, Monthly, 6 Monthly, Yearly)
	 */
	public static ChartProperties getFullScreenChartProperties() {
		ChartProperties chartProperties = new ChartProperties();
		if(AppConstants.screenWidth==320)
			chartProperties.setChartWidth(300);//
		else if(AppConstants.screenWidth==360)
		{
			chartProperties.setChartWidth(300);
		}
		else
			chartProperties.setChartWidth(300);
		if(AppConstants.screenHeight==240)
			chartProperties.setChartHeight(185);//60);//Should be divide by 5 element
		else if(AppConstants.screenWidth==640 && AppConstants.screenHeight==480)
			chartProperties.setChartHeight(185);
		else if(AppConstants.screenHeight==640 && AppConstants.screenWidth==480)
			chartProperties.setChartHeight(200);
		else if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
			chartProperties.setChartHeight(200);
		else if(AppConstants.screenHeight==480 && AppConstants.screenWidth==360)
			chartProperties.setChartHeight(200);
		else if(AppConstants.screenHeight==480)
		{
			chartProperties.setChartHeight(200);
		}
		else
			chartProperties.setChartHeight(185);
		chartProperties.setChartBackColor(16777215);
		chartProperties.setChartBorderColor(2763306);
		chartProperties.setChartAxisLineColor(2697257);//15790315
		chartProperties.setChartAxisLineWidth((byte)2);
		chartProperties.setChartSpikeLineColor(16777215);
		chartProperties.setChartCoveredAreaColor(4343106);
		chartProperties.setChartXYAxisLineColor(16777215);//2763306
		chartProperties.setChartMidLineColor(Color.YELLOW);
		chartProperties.setPadding((byte)3);
		chartProperties.setChartXAxisLabelFontType(AppConstants.EXTRA_SMALL_BOLD_FONT);
		chartProperties.setChartYAxisLabelFontType(AppConstants.EXTRA_SMALL_BOLD_FONT);
		chartProperties.setChartFontColor(16777215);
		chartProperties.setChartxAxisHeight(FontLoader.getFont(chartProperties.getChartXAxisLabelFontType()).getHeight()+chartProperties.getPadding());
		chartProperties.doPorting();
		return chartProperties;
	}
	public void doPorting() {
		setChartWidth(ImageManager.CalculateRelativeDimensions(getChartWidth(), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth) - ImageManager.CalculateRelativeDimensions(getChartWidth(), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth)%10);
		setChartHeight(ImageManager.CalculateRelativeDimensions(getChartHeight(), AppConstants.screenHeight, AppConstants.baseBuildScreenHeight) - ImageManager.CalculateRelativeDimensions(getChartHeight(), AppConstants.screenHeight, AppConstants.baseBuildScreenHeight)%5);
		setChartAxisLineWidth((byte)ImageManager.CalculateRelativeDimensions(getChartAxisLineWidth(), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
		setPadding((byte)ImageManager.CalculateRelativeDimensions(getPadding(), AppConstants.screenWidth, AppConstants.baseBuildScreenWidth));
		setChartxAxisHeight(FontLoader.getFont(getChartXAxisLabelFontType()).getHeight()+getPadding());
	}

}
