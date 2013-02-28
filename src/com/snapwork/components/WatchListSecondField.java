package com.snapwork.components;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;

/**
 * 
 * <p>This class is a creates components for Search Screen.
 * <p>This class Shows the company Name and Company code as as a label field, its clickable.
 *
 */

public class WatchListSecondField extends Manager
{
	private LabelField lblCompanyName = null;
	private final static int itemBackColor = 4343106;
	private boolean requireNullField = true;
	private ActionListener actionListner;
	private static byte padding = 4;
	private int _width = 0;
	private Font font;
	public WatchListSecondField(long style,String companyName, final int width_)
	{
		super(style);
		if(AppConstants.screenHeight<480)
			font = FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);
		else
			font = FontLoader.getFont(AppConstants.LABEL_FONT);
		setFont(font);
		_width = width_;
		lblCompanyName = new LabelField(companyName, 0)
		{
			public int getPreferredHeight()
			{
				return getFont().getHeight();
			}

			public int getPreferredWidth()
			{
				return _width;
			}

			protected void paint(Graphics graphics)
			{
				graphics.setColor(0xeeeeee);
				super.paint(graphics);
			}

			protected void drawFocus(Graphics graphics, boolean on)
			{
			}

			protected void layout(int width, int height)
			{
				super.layout(width, height);
				//setPosition(8+ImageManager.getIcon().getWidth(),0);
				setExtent(getPreferredWidth(), getPreferredHeight());
			}
		};

		
		lblCompanyName.setFont(getFont());
		add(lblCompanyName);
		requireNullField = true;
		for(int i=0;i<this.getFieldCount();i++)
		{
			if(this.getField(i).isFocusable())
			{
				requireNullField = false;
				i=this.getFieldCount();
			}
		}

		if(requireNullField)
		{
			NullField objNullField = new NullField(FOCUSABLE)
			{
				protected void onFocus(int direction)
				{
					if(direction==-1)
					{
						this.setPosition(0, 0);
					}
					else
					{
						this.setPosition(this.getManager().getWidth(), this.getManager().getHeight());
					}
					super.onFocus(direction);
				}

				protected void onUnfocus()
				{
					super.onUnfocus();
				}
			};
			add(objNullField);//A Field which will show focus on manager
		}
	}

	public int getTotalFields()
	{
		if(requireNullField)
			return this.getFieldCount()-1;
		else
			return this.getFieldCount(); 
	}

	public int getPreferredHeight()
	{
		return getFont().getHeight()*2+padding*2+8;
	}

	public int getPreferredWidth()
	{
		return _width;
	}

	protected void onFocus(int direction)
	{
		super.onFocus(direction);
		invalidate();
	}

	protected void onUnfocus()
	{
		super.onUnfocus();
		invalidate();
	}

	/*protected boolean navigationClick(int status, int time)
	{
		Vector vectorCommandData = new Vector();
		vectorCommandData.addElement(_url);
		Action action = new Action(ActionCommand.CMD_WL_FILL_SCREEN, vectorCommandData);
		ActionInvoker.processCommand(action);
		
		return super.navigationClick(status, time);
	}

	protected boolean touchEvent(TouchEvent message)
	{
		Vector vectorCommandData = new Vector();
		vectorCommandData.addElement(_url);
		Action action = new Action(ActionCommand.CMD_WL_FILL_SCREEN, vectorCommandData);
		ActionInvoker.processCommand(action);
		return super.touchEvent(message);
	}*/
	protected void paintBackground(Graphics graphics)
	{
		//graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
		if(isFocus())
			graphics.setColor(Color.ORANGE);
		else
			graphics.setColor(itemBackColor);
		graphics.fillRoundRect(padding/2, padding/2, getPreferredWidth()-padding, getPreferredHeight()-padding, 10, 10);
		graphics.setColor(0x000000);
		graphics.fillRoundRect((padding/2)+1, (padding/2)+1, (getPreferredWidth()-padding)-2, (getPreferredHeight()-padding)-2, 10, 10);
		
		graphics.setColor(0x828077);
		graphics.drawBitmap(3, (getPreferredHeight()/2)-(ImageManager.getIcon().getHeight()/2), ImageManager.getIcon().getWidth(), ImageManager.getIcon().getHeight(), ImageManager.getIcon(), 0, 0);
		/*if(isFocus())
			graphics.drawBitmap(getPreferredWidth()-25, (getPreferredHeight()/2)-(ImageManager.getArrow(false).getHeight()/2), ImageManager.getArrow(false).getWidth(), ImageManager.getArrow(false).getHeight(), ImageManager.getArrow(false), 0, 0);
		else
			graphics.drawBitmap(getPreferredWidth()-25, (getPreferredHeight()/2)-(ImageManager.getArrow(true).getHeight()/2), ImageManager.getArrow(true).getWidth(), ImageManager.getArrow(true).getHeight(), ImageManager.getArrow(true), 0, 0);
		*///graphics.drawLine(getPreferredWidth()-20, (getPreferredHeight()/2)-5, getPreferredWidth()-5, getPreferredHeight()/2);
		//graphics.drawLine(getPreferredWidth()-20, (getPreferredHeight()/2)+5, getPreferredWidth()-5, getPreferredHeight()/2);
	}

	protected void sublayout(int width, int height)
	{
		layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
		setPositionChild(getField(0),padding+8+ImageManager.getIcon().getWidth(), (getPreferredHeight()/2)-(getField(0).getPreferredHeight()/2));
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
}