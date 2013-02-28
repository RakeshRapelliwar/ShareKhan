package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.UiApplication;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.util.AppConstants;
import com.snapwork.util.Debug;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.Utils;

/**
 * 
 * <p>This loading components used for BSE, NSE Labels and Charts.
 *
 */
public class LoadingComponent extends Field
{
	private int width = 0;
	private int height = 0;
	private String message = ""; 
	private Thread threadLoadingAnimation = null;
	private byte padding = 4;
	private byte loadingBlock = 10;
	private byte animationCounter = 0;
	private boolean isCompClosed = false;
	private boolean loading = false;

	public LoadingComponent(String message,int width,int height)
	{
		this.message = message;
		this.width = width;
		this.height = height;
		padding = (byte)ImageManager.CalculateRelativeDimensions(4, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
		loadingBlock = (byte)ImageManager.CalculateRelativeDimensions(10, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
	}
	public LoadingComponent(String message,int width,int height, boolean loading)
	{
		this.message = message;
		this.width = width;
		this.height = height;
		this.loading = loading;
		padding = (byte)ImageManager.CalculateRelativeDimensions(4, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
		loadingBlock = (byte)ImageManager.CalculateRelativeDimensions(10, AppConstants.screenWidth, AppConstants.baseBuildScreenWidth);
	}

	protected void layout(int width, int height)
	{
		setExtent(this.width, this.height);
	}

	protected void onUndisplay()
	{
		super.onUndisplay();
		isCompClosed = true;
		threadLoadingAnimation = null;
	}

	protected void paint(Graphics graphics)
	{
		graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		graphics.setColor(0x636563);
		//graphics.drawText(message, width/2 - graphics.getFont().getAdvance(message)/2, (height/2 - graphics.getFont().getHeight()/2)-FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight());
		
		if(loading)
		{
			graphics.setColor(0xeeeeee);
		graphics.fillRect(width/2 - (loadingBlock*10)/2-padding/2,  (height/2 - graphics.getFont().getHeight()/2)+padding*4-padding/2, loadingBlock*10+padding, loadingBlock+padding);
		graphics.setColor(Color.GRAY);
		graphics.fillRect(width/2 - (loadingBlock*10)/2,  (height/2 - graphics.getFont().getHeight()/2)+padding*4, loadingBlock*10, loadingBlock);
		for(byte i=animationCounter;i<(animationCounter+(animationCounter < 8 ? 3 : (animationCounter == 8 ? 2 : 1)));i++)
		{
			graphics.setColor(0xeeeeee);
			graphics.fillRect((width/2 - (loadingBlock*10)/2)+loadingBlock*i+1,  (height/2 - graphics.getFont().getHeight()/2)+padding*4+1, loadingBlock-2, loadingBlock-2);
		}


		if(threadLoadingAnimation==null)
		{
			threadLoadingAnimation = new Thread(new Runnable()
			{
				public void run()
				{
					try
					{
						while(!isCompClosed)
						{
							animationCounter++;
							if(animationCounter==9)
								animationCounter = 0;
							Thread.sleep(100);
							invalidate();
						}
					}
					catch(Exception ex)
					{
						Debug.debug("Error : "+ex.toString());
					}
				}
			});
			threadLoadingAnimation.start();
		}
		}
		else
		{
			graphics.setColor(0x636563);
			graphics.setStrokeStyle(2);
			graphics.drawLine(padding*4, padding*2, padding*4, height-padding*4);
			graphics.drawLine(padding*4, height-padding*4, width-padding*4, height-padding*4);
		}
	}
}
