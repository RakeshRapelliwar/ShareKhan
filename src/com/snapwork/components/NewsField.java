package com.snapwork.components;

import net.rim.device.api.crypto.PublicKey;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.HorizontalFieldManager;

import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionListener;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;

/**
 * 
 * <p>This class is creates news fields and added to News Screen.
 * <p>This class create custom field which shows news details.
 *
 */
public class NewsField extends HorizontalFieldManager implements ActionListener
{
	private LabelField titleLabel = null;
	private LabelField sourceLabel = null;
	private boolean requireNullField = true;
	private boolean isFocused = false;
	private ActionListener actionListner = null;
	private byte newsItemIndex = 0;
	private static short fieldWidth = -1,fieldHeight = -1;
	private long timer;
	public NewsField(final String titleText,final String sourceText,ActionListener actionListner,byte newsItemIndex) {
		super(FOCUSABLE);
		titleLabel = new LabelField(titleText, 0);
		titleLabel.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		timer = System.currentTimeMillis();
		sourceLabel = new LabelField(sourceText, 0)
		{
			protected void paintBackground(Graphics graphics)
			{
				graphics.setColor(0x3f4440);
				graphics.fillRect(0, 0, getWidth(), getHeight());
				graphics.setColor(0xeeeeee);
			};
		};
		sourceLabel.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
		add(titleLabel);
		add(sourceLabel);
		if(fieldWidth==-1)
			fieldWidth = (short)(getPadding()*2);
		if(fieldHeight==-1)
			fieldHeight = (short)(getAllFieldHeight() - getPadding());
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
		this.newsItemIndex = newsItemIndex;
		this.actionListner = actionListner;
	}

	protected void onFocus(int direction)
	{
		isFocused = true;
		super.onFocus(direction);
		invalidate();
	}

	protected void onUnfocus()
	{
		isFocused = false;
		super.onUnfocus();
		invalidate();
	}

	public int getTotalFields()
	{
		if(requireNullField)
			return this.getFieldCount()-1;
		else
			return this.getFieldCount(); 
	}

	protected void paintBackground(Graphics graphics)
	{
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
		if(isFocused)
			graphics.setColor(Color.ORANGE);
		else
			graphics.setColor(0xeeeeee);
		graphics.fillRoundRect(getPadding()/3, getPadding()/3,getAllFieldWidth(), getAllFieldHeight(),10, 10);
		graphics.setColor(AppConstants.newsScreenStaticBlock);
		graphics.fillRoundRect(getPadding()/3+getPadding()/2, getPadding()/3+getPadding()/2, getAllFieldWidth()-getPadding(), getAllFieldHeight() - getPadding(), 10, 10);
		graphics.setColor(0x3f4440);
		graphics.fillRoundRect(getPadding()/3+getPadding()/2+getPadding()*2, getPadding()/3+getPadding()/2,getAllFieldWidth()-getPadding()-getPadding()*2, getAllFieldHeight() - getPadding(),10, 10);
		graphics.fillRect(getPadding()/3+getPadding()/2+12,getPadding()/3+getPadding()/2,2,getAllFieldHeight() - getPadding());
		graphics.setColor(0xeeeeee);
	}


	protected void sublayout(int width, int height)
	{
		layoutChild(this.getField(0),AppConstants.screenWidth - 2*getPadding()/3 - getPadding() *2 - fieldWidth , FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*2);
		layoutChild(this.getField(1),AppConstants.screenWidth - 2*getPadding()/3 - getPadding() *2 - fieldWidth, FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight());
		setPositionChild(this.getField(0), getPadding()/3+getPadding() + fieldWidth, getPadding()/3+getPadding()/2);
		setPositionChild(this.getField(1), getPadding()/3+getPadding() + fieldWidth, getAllFieldHeight() -FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight() - 2*( getPadding()/3));
		if(requireNullField)
		{
			layoutChild(this.getField(this.getFieldCount()-1), 1 , 1);
			setPositionChild(this.getField(this.getFieldCount()-1), AppConstants.screenWidth/2, getAllFieldHeight()/2);
		}
		setExtent(AppConstants.screenWidth, getAllFieldHeight()+2*(getPadding()/3));
	}
	protected boolean navigationClick(int status, int time)
	{
		//Dialog.alert("NEWS FIELD CLICKED!");
		if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
		actionListner.actionPerfomed(ActionCommand.CMD_NEWS_DETAIL, new Byte(newsItemIndex));
	}
		return super.navigationClick(status, time);
	}
	protected boolean touchEvent(TouchEvent message)
	{
		if(message.getEvent() == TouchEvent.CLICK) {
			if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
			actionListner.actionPerfomed(ActionCommand.CMD_NEWS_DETAIL, new Byte(newsItemIndex));
	}}
		return super.touchEvent(message);
	}

	private static short getPadding()
	{
		return (short)6;
	}

	private static short getAllFieldWidth()
	{
		return (short)(AppConstants.screenWidth - (getPadding()/3)*2);
	}

	private static short getAllFieldHeight()
	{
		/*if(AppConstants.screenWidth==480 && AppConstants.screenHeight==640)
			return (short)((FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*3) + (getPadding()/3)*2 + getPadding()+FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight());
		else*/
			return (short)((FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()*3) + (getPadding()/3)*2 + getPadding());		
	}
	public int getPreferredHeight()
	{
		return getAllFieldHeight();
	}
	public void actionPerfomed(byte Command, Object data)
	{
	}

}
