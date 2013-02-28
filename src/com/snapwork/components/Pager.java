package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.component.LabelField;

import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionListener;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;

/**
 * 
 * <p>This class shows Next and Previous clickable labels in followings screens.
 * <p>Commentary, News and Announcement Snippet Screens.
 *
 */

public class Pager extends Manager
{
	public int pageNo = -1;
	private LabelField lblNext = null,lblPrev = null,lblSep = null;
	private boolean lockNextPaging = false;
	public Pager(final ActionListener actionListner,final int pageNu)
	{
		super(FOCUSABLE | FIELD_RIGHT);
		this.pageNo = pageNu;
		lblNext = new LabelField("Next", FOCUSABLE)
		{
			protected void drawFocus(Graphics graphics, boolean on)
			{

			}
			protected void layout(int width, int height)
			{
				super.layout(width, height);
				setExtent(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getAdvance(this.getText()) + 4, FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getHeight() + 2);
			}

			public int getPreferredWidth()
			{
				return FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getAdvance(this.getText()) + 4;
			}

			protected void paint(Graphics graphics)
			{
				graphics.setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
				if (isFocus())
				{
					graphics.setColor(Color.ORANGE);
					graphics.drawLine(0, graphics.getFont().getHeight() ,graphics.getFont().getAdvance(this.getText()) + 1,graphics.getFont().getHeight() );
				}
				graphics.setColor(0xeeeeee);
				graphics.drawText(this.getText(), 0, 0);
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
				if(!lockNextPaging)
					actionListner.actionPerfomed(ActionCommand.CMD_NEXT_PAGE,new Integer(++pageNo));
				return super.navigationClick(status, time);
			}
			protected boolean touchEvent(TouchEvent arg0) 
			{
				if(!lockNextPaging)
					actionListner.actionPerfomed(ActionCommand.CMD_NEXT_PAGE,new Integer(++pageNo));
				return super.touchEvent(arg0);
			};
		};

		lblSep = new LabelField(" | ", NON_FOCUSABLE)
		{
			public int getPreferredWidth()
			{
				return FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getAdvance(this.getText());
			}
			protected void paint(Graphics graphics)
			{
				if(pageNo!=1)
				{
					graphics.setColor(0xeeeeee);
					graphics.setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
					graphics.drawText(this.getText(), 0, 0);
				}
				invalidate();
			}
		};
		lblPrev = new LabelField("Previous", FOCUSABLE)
		{
			protected void drawFocus(Graphics graphics, boolean on){

			}

			public int getPreferredWidth()
			{
				return FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getAdvance(this.getText()) + 2;
			}

			protected void layout(int width, int height)
			{
				super.layout(width, height);
				setExtent(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getAdvance(this.getText()) + 2, FontLoader.getFont(AppConstants.SMALL_BOLD_FONT).getHeight() + 2);
			}

			protected void paint(Graphics graphics)
			{
				if(pageNo!=1)
				{
					graphics.setFont(FontLoader.getFont(AppConstants.SMALL_BOLD_FONT));
					if (isFocus() && pageNo>1)
					{
						graphics.setColor(Color.ORANGE);
						graphics.drawLine(0, graphics.getFont().getHeight() ,graphics.getFont().getAdvance(this.getText()) + 1,graphics.getFont().getHeight() );
					}
					graphics.setColor(0xeeeeee);
					graphics.drawText(this.getText(), 0, 0);
				}
				invalidate();
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
				if(pageNo>1)
				{
					actionListner.actionPerfomed(ActionCommand.CMD_PREV_PAGE,new Integer(--pageNo));
				}
				return super.navigationClick(status, time);
			}
			protected boolean touchEvent(TouchEvent arg0) 
			{
				if(pageNo>1)
				{
					actionListner.actionPerfomed(ActionCommand.CMD_PREV_PAGE,new Integer(--pageNo));
				}
				return super.touchEvent(arg0);
			};
		};
		add(lblPrev);
		add(lblSep);
		add(lblNext);
	}

	public void blockNextPaging(boolean status)
	{
		lockNextPaging = status;
	}

	public boolean isNextPagingBlocked()
	{
		return lockNextPaging;
	}

	protected void paintBackground(Graphics graphics)
	{
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getWidth(), getHeight());
	}

	protected void sublayout(int width,int height)
	{
		layoutChild(this.getField(0), width, height);
		layoutChild(this.getField(1), width, height);
		layoutChild(this.getField(2), width, height);
		setPositionChild(this.getField(0), 0, 0);
		setPositionChild(this.getField(1), lblPrev.getPreferredWidth(), 0);
		setPositionChild(this.getField(2), lblPrev.getPreferredWidth() + lblSep.getPreferredWidth(), 0);
		setExtent(lblNext.getPreferredWidth() + lblPrev.getPreferredWidth()+ lblSep.getPreferredWidth(), lblPrev.getHeight());
	}
}