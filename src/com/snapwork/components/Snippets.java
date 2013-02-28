package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.component.ObjectChoiceField;

import com.snapwork.actions.ActionListener;
import com.snapwork.util.AppConstants;

/**
 * 
 * <p>This class used to create of layout login screen UserRegistration class.
 *
 */

public class Snippets extends Manager
{
	public static int BlockHeight = 161;
	public static int BlockImageWidth = 310,BlockImageHeight = 150;
	public static int BitmapWidth = 42,BitmapHeight = 42;
	public static byte padding = 4;
	public BlockField[] blockFields;
	public Snippets instance;
	public ActionListener actionListner = null;
	public byte command;
	public Object key = null;
	private boolean isFullScreen = false;
	private int snippetHeight = 0;
	private int yDiff = 5;
	private int fields=0;

	public Snippets(long style,BlockField[] blockFields,ActionListener actionListner,byte command,Object key,int snippetHeight)
	{
		this(style,blockFields,actionListner,command,key);
		isFullScreen = true;
		this.snippetHeight = snippetHeight;
	} 

	public Snippets(long style,BlockField[] blockFields,ActionListener actionListner,byte command,Object key)
	{
		super(style);
		//Sets the instance varriables
		instance = this;
		instance.actionListner = actionListner;
		instance.command = command;
		instance.key = key;
		this.blockFields = blockFields;
		for(int i=0;i<blockFields.length;i++)
		{
			if(blockFields[i].field instanceof Field)
			{
				add((Field)blockFields[i].field);       
			}
			blockFields[i].field = null;
		}
	}

	protected void paintBackground(Graphics graphics)
	{
		/*graphics.setColor(Color.BLACK);
		if(isFullScreen)
		{
			graphics.fillRect(0, 0, AppConstants.screenWidth, snippetHeight);                       
		}
		else
		{
			graphics.fillRect(0, 0, AppConstants.screenWidth, BlockHeight);
		}*/
	}

	protected void sublayout(int width, int height)
	{
		if(isFullScreen)
		{
			for(int i=0;i<this.getFieldCount();i++)
			{
				layoutChild(this.getField(i), BlockImageWidth - blockFields[i].xPos-padding, (snippetHeight-padding*2)- blockFields[i].yPos-padding);
			}
		}
		else
		{
			for(int i=0;i<this.getFieldCount();i++)
			{
				if(blockFields[i].getHeight()!=-1)
				{
					layoutChild(this.getField(i), BlockImageWidth - blockFields[i].xPos-padding, blockFields[i].getHeight());                                       
				}
				else
				{
					layoutChild(this.getField(i), BlockImageWidth - blockFields[i].xPos-padding, BlockImageHeight- blockFields[i].yPos-padding);
				}
			}
		}

		for(int i=0;i<this.getFieldCount();i++)
		{
			switch(blockFields[i].horizontalAlignOnBlock)
			{
			case BlockField.H_CENTER:
				if(blockFields[i].xPos==-1 || fields<2)
				{
				blockFields[i].xPos = (AppConstants.screenWidth/2-BlockImageWidth/2) + (BlockImageWidth/2 - this.getField(i).getWidth()/2); 
				fields++;
				}break;
			case BlockField.H_LEFT:
				if(blockFields[i].xPos==-1)
				blockFields[i].xPos = (AppConstants.screenWidth/2-BlockImageWidth/2)+padding;
				break;
			case BlockField.H_RIGHT:
				if(blockFields[i].xPos==-1)
				blockFields[i].xPos = AppConstants.screenWidth - this.getField(i).getWidth() -  (AppConstants.screenWidth/2-BlockImageWidth/2)-padding;
				break;
			case BlockField.H_NONE:
				if(blockFields[i].xPos==-1)
				blockFields[i].xPos += (AppConstants.screenWidth/2-BlockImageWidth/2);
				break;
			}
			switch(blockFields[i].verticalAlignOnBlock)
			{
			case BlockField.V_CENTER:
				if(isFullScreen)
				{
					if(blockFields[i].yPos==-1)
					blockFields[i].yPos = (snippetHeight/2-(snippetHeight-padding*2)/2) + ((snippetHeight-padding*2)/2 - this.getField(i).getHeight()/2);
				}
				else
				{
					if(blockFields[i].yPos==-1)
					blockFields[i].yPos = (BlockHeight/2-BlockImageHeight/2) + (BlockImageHeight/2 - this.getField(i).getHeight()/2);
				}
				break;
			case BlockField.V_TOP:
				if(isFullScreen)
				{
					if(blockFields[i].yPos==-1)
					blockFields[i].yPos = (snippetHeight/2-(snippetHeight-padding*2)/2)+padding;
				}
				else
				{
					if(blockFields[i].yPos==-1)
					blockFields[i].yPos = (BlockHeight/2-BlockImageHeight/2)+padding;
				}
				break;
			case BlockField.V_BOTTOM:
				if(isFullScreen)
				{
					if(blockFields[i].yPos==-1 && fields<2)
					{
						blockFields[i].yPos = snippetHeight - this.getField(i).getHeight() - (snippetHeight/2-(snippetHeight-padding*2)/2)-padding;
						fields++;
					}
				}
				else
				{
					if(blockFields[i].yPos==-1 && fields<2)
					{
						blockFields[i].yPos = BlockHeight - this.getField(i).getHeight() - (BlockHeight/2-BlockImageHeight/2)-padding;
						fields++;
					}
				}
				break;
			case BlockField.V_NONE:
				if(isFullScreen)
				{
					if(blockFields[i].yPos==-1)
					blockFields[i].yPos += (snippetHeight/2-(snippetHeight-padding*2)/2);
				}
				else
				{
					if(blockFields[i].yPos==-1)
					blockFields[i].yPos += (BlockHeight/2-BlockImageHeight/2);
				}
				break;
			}
			setPositionChild(this.getField(i), blockFields[i].xPos, blockFields[i].yPos);
		}
		if(isFullScreen)
		{
			setExtent(AppConstants.screenWidth, snippetHeight);
		}
		else
		{
			setExtent(AppConstants.screenWidth, BlockHeight);
		}
	}

	/*protected boolean navigationClick(int status, int time)
	{
		instance.actionListner.actionPerfomed(command, key);
		super.navigationClick(status, time);
		return true;
	}*/
}
