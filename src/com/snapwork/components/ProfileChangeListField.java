package com.snapwork.components;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;

import com.snapwork.actions.ActionCommand;
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

public class ProfileChangeListField extends Manager
{
	private LabelField lblCompanyName = null;
	private final static int itemBackColor = 4343106;
	private boolean requireNullField = true;
	private ActionListener actionListner;
	private static byte padding = 4;
	private long timer;
	public ProfileChangeListField(long style,String companyName, ActionListener actionListner)
	{
		super(style);
		timer  = System.currentTimeMillis();
		setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
		this.actionListner = actionListner;
		lblCompanyName = new LabelField(companyName, 0)
		{
			public int getPreferredHeight()
			{
				return getFont().getHeight();
			}

			public int getPreferredWidth()
			{
				return AppConstants.screenWidth;
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
		return getFont().getHeight()*2+padding*2;
	}

	public int getPreferredWidth()
	{
		return AppConstants.screenWidth;
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

	protected boolean navigationClick(int status, int time)
	{
		if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
		Vector vectorCommandData = new Vector();
		vectorCommandData.addElement(lblCompanyName.getText());
		actionListner.actionPerfomed(ActionCommand.CMD_PROFILE_CHANGE_SCREEN, vectorCommandData);
		}
		return super.navigationClick(status, time);
	}

	protected boolean touchEvent(TouchEvent message)
	{ setFocus();
		if(message.getEvent() == TouchEvent.CLICK) {
			if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
			Vector vectorCommandData = new Vector();
		vectorCommandData.addElement(lblCompanyName.getText());
		actionListner.actionPerfomed(ActionCommand.CMD_PROFILE_CHANGE_SCREEN, vectorCommandData);
	}
		}
	return super.touchEvent(message);
	}
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
		if(isFocus())
			graphics.drawBitmap(getPreferredWidth()-25, (getPreferredHeight()/2)-(ImageManager.getArrow(false).getHeight()/2), ImageManager.getArrow(false).getWidth(), ImageManager.getArrow(false).getHeight(), ImageManager.getArrow(false), 0, 0);
		else
			graphics.drawBitmap(getPreferredWidth()-25, (getPreferredHeight()/2)-(ImageManager.getArrow(true).getHeight()/2), ImageManager.getArrow(true).getWidth(), ImageManager.getArrow(true).getHeight(), ImageManager.getArrow(true), 0, 0);
		//graphics.drawLine(getPreferredWidth()-20, (getPreferredHeight()/2)-5, getPreferredWidth()-5, getPreferredHeight()/2);
		//graphics.drawLine(getPreferredWidth()-20, (getPreferredHeight()/2)+5, getPreferredWidth()-5, getPreferredHeight()/2);
	}

	protected void sublayout(int width, int height)
	{
		layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
		setPositionChild(getField(0), padding, (getPreferredHeight()/2)-(getField(0).getPreferredHeight()/2));
		setExtent(getPreferredWidth(), getPreferredHeight());
	}
}