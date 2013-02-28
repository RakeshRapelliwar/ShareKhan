package com.snapwork.view.trade;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.Consensus;
import com.snapwork.beans.Expiry;
import com.snapwork.beans.ForexList;
import com.snapwork.beans.Market;
import com.snapwork.beans.TopGainLoseItem;
import com.snapwork.beans.KeyValueBean;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.Utils;

public class SlideViewMargin extends VerticalFieldManager {
	
	private static byte padding = 4; 
	private Font font; 
	public static int ALIGN_LEFT = 1;
	public static int ALIGN_LEFT_WITH_COLON = 10;
	public static int ALIGN_CENTER = 2;
	public static int ALIGN_CENTER_WITH_COLON = 20;
	public static int ALIGN_RIGHT = 3;
	public static int ALIGN_RIGHT_WITH_LAST_BOLD = 4;
	public static int ALIGN_RIGHT_WITH_COLON = 30;
	private int align;
	
	public SlideViewMargin(Vector vector, Font font, int align) {
		super(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR);
		setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
		this.font = font;
		this.align = align;
		TabbedList tlv = null;
		String[] vect = new String[2];
		double additionResult = 0;
		tlv = new TabbedList("ONE", this.font, vect, 0x608080);
			KeyValueBean modifyBean = null;
			int color;
			int bgcolor;
			int size = vector.size();
			for(int i=0;i<size;i++)
			{
				if(i%2==0) {
					color = 0x000000;
					bgcolor = 0x333333;
					}
				else {
					color = 0x333333;
					bgcolor = 0x000000;
					}
				modifyBean = (KeyValueBean)vector.elementAt(i);
				String[] data2 = new String[2];
					data2[0]=modifyBean.getKey();
					if(modifyBean.getValue().equalsIgnoreCase(" "))
					{
						data2[1] = " - ";
					}
					else
					{
						if(i==size-1)
						{
							try
							{
								data2[1] = Utils.DecimalRoundString(additionResult, 2);
								}
							catch(Exception e)
							{
								data2[1] = additionResult+"";
							}
						}
						else
							{
							data2[1] = modifyBean.getValue();
							double add = 0;
							try{
								add = Double.parseDouble(modifyBean.getValue());
								//add = Utils.DecimalRound(add, 2);
								data2[1] = Utils.DecimalRoundString(add, 2);
							}catch(Exception e)
							{
								add = 0;
								data2[1] = modifyBean.getValue();
							}
							additionResult = additionResult + add;
							}
					}
					if(i==(size-1) && align == ALIGN_RIGHT_WITH_LAST_BOLD)
					{
						tlv.addItem("ONES", FontLoader.getFont(AppConstants.REPORTSMARGINBOLD_FONT), data2, color, bgcolor);
					}
					else
					{
						tlv.addItem("ONES", this.font, data2, color, bgcolor);
					}
			
				
			}		
		add(tlv);
	}
	public void scrollChanged(Manager mgr, int newHorizScroll, int newVertScroll) {
	    if (newHorizScroll < 0) {
	        setHorizontalScroll(0);
	    }
	    // and so on
	}
	public int getPreferredHeight() {
		try {
			return AppConstants.screenHeight;
		} catch(Exception ex) {
		}
		return 0;
	}
	protected boolean touchEvent(TouchEvent message) 
	{
		return super.touchEvent(message);
	}
	private class TabbedList extends VerticalFieldManager {
		LabelField lblListTitle = null;
		private final static int titleColor = 6521476;
		int color = Color.BLACK;
		String[] data = new String[2];
		String[] data2 = new String[2];
		Font font = null;
		boolean flag = true;
		public TabbedList(String strTitle, Font fnt, String[] dat, int clr) {
			super(FOCUSABLE | VERTICAL_SCROLL | VERTICAL_SCROLLBAR);
			setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
			color = clr;
			font = fnt;
			if(dat.length==2)
				data = dat;
			else
			{
				data2 = dat;
				flag = false;
			}
				
			lblListTitle = new LabelField(strTitle, SlideViewMargin.NON_FOCUSABLE) {

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
				
				public int getPreferredHeight() {
					return getFont().getHeight()+(padding*3)+8;
				}

				public int getPreferredWidth() {
					return AppConstants.screenWidth;
				}

				protected void paint(Graphics graphics) {
					graphics.setColor(Color.BLACK);
					graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
					graphics.setColor(color);
					graphics.fillRect(0, 6, getPreferredWidth(), getPreferredHeight()-12);
					
					graphics.setColor(0xeeeeee);
					graphics.setFont(font);
					if(flag)
					{
						graphics.drawText(data[0],0+2,padding/2+(padding/2)+4,0);
						graphics.drawText(":",0+(AppConstants.screenWidth/2) - 20,padding/2+(padding/2)+4,0);
						graphics.drawText(data[1],0+AppConstants.screenWidth-70-2,padding/2+(padding/2)+4,0);
					}
				}

				protected void drawFocus(Graphics graphics, boolean on) {}
				
				
			};
			//lblListTitle.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			lblListTitle.setFont(font);
			//testadd(lblListTitle);
		}
		protected boolean touchEvent(TouchEvent message) 
		{
			return super.touchEvent(message);
		}
		private void addItem(final String strTitle,final  Font fnt,final  String[] dat,final  int clr, final  int bgclr) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					add(new TabbedListItem(FOCUSABLE | Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR , strTitle ,fnt,dat,clr, bgclr));
				}
			});
		}

		
	}
	
	private class TabbedListItem extends Manager {
		private LabelField lblCompanyName = null;
		private boolean requireNullField = true;
		int color = Color.BLACK;
		int bgcolor;
		String[] data = new String[3];
		String[] data2 = new String[3];
		Font font = null;
		boolean flag = true;
		
		public TabbedListItem(long style,String title,Font fnt,String[] dat,int clr, int bg) {
			super(style);
			setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
			color = clr;
			bgcolor = bg;
			font = fnt;
			if(dat.length==2)
				data = dat;
			else
			{
				data2 = dat;
				flag = false;
			}
				
			lblCompanyName = new LabelField("ONE", 0) {

				public int getPreferredHeight() {
					return getFont().getHeight()+(padding*3)+8;
				}
				public int getPreferredWidth() {
					return AppConstants.screenWidth;
				}

				
				protected void paint(Graphics graphics) {
					graphics.setColor(0x333333);
					graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
					graphics.setColor(bgcolor);
					graphics.fillRect(0, 1, getPreferredWidth(), getPreferredHeight()-2);
					graphics.setColor(color);
					graphics.fillRect(0, 1, getPreferredWidth(), getPreferredHeight()-2);
					graphics.setColor(0xeeeeee);
					graphics.setFont(font);
					if(flag){
					graphics.drawText(data[0],0+2,padding*2+(padding/2)+4,0);
					if(data[1].length()>0)
					{
						graphics.drawText(":",0+(AppConstants.screenWidth/2) - 20,padding*2+(padding/2)+4,0);
						graphics.drawText(data[1],0+AppConstants.screenWidth-70-2,padding*2+(padding/2)+4,0);
					}
					}
				}
				protected void drawFocus(Graphics graphics, boolean on) {
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
				protected void paintBackground(Graphics graphics) 
				{
					graphics.setColor(0x333333);
					graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
					
				}
			};
			
			lblCompanyName.setFont(getFont());
				add(lblCompanyName);
			
			requireNullField = true;
			for(int i=0;i<this.getFieldCount();i++) {
				if(this.getField(i).isFocusable()) {
					requireNullField = false;
					i=this.getFieldCount();
				}
			}
			
			if(requireNullField) {
				NullField objNullField = new NullField(FOCUSABLE) {
					
					protected void onFocus(int direction) {
						if(direction==-1) {
							this.setPosition(0, 0);
						}
						else {
							this.setPosition(this.getManager().getWidth(), this.getManager().getHeight());
						}
						super.onFocus(direction);
					}

					protected void onUnfocus() {
						super.onUnfocus();
					}
				};
				add(objNullField);//A Field which will show focus on manager
			}
			
		}
		
		public int getTotalFields() {
			if(requireNullField)
				return this.getFieldCount()-1;
			else
				return this.getFieldCount(); 
		}

		public int getPreferredHeight() {
			return getFont().getHeight()+(padding*3)+8;
		}

		public int getPreferredWidth() {
			return AppConstants.screenWidth;
		}

		protected void onFocus(int direction) {
			super.onFocus(direction);
			invalidate();
		}

		protected void onUnfocus() {
			super.onUnfocus();
			invalidate();
		}

		protected boolean navigationClick(int status, int time) {
			return super.navigationClick(status, time);
		}
		protected boolean touchEvent(TouchEvent message) 
		{
			return super.touchEvent(message);
		}

		protected void paintBackground(Graphics graphics) {
			graphics.setColor(0x000000);
			graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
			graphics.setColor(bgcolor);
			graphics.drawRect(2, 1, getPreferredWidth()-4, getPreferredHeight()-2);
			graphics.setColor(color);
			graphics.fillRect(3, 3, getPreferredWidth()-6, getPreferredHeight()-6);
			graphics.setColor(isFocus() == true ? Color.ORANGE : bgcolor);
			graphics.drawRect(2, 1, getPreferredWidth()-4, getPreferredHeight()-2);
			
			graphics.setColor(0xeeeeee);
			graphics.setFont(font);
			String text = data[0].replace(' ', '_');
			if(flag){
				if(text.equalsIgnoreCase("Total_Amount"))
				graphics.setFont(FontLoader.getFont(AppConstants.REPORTSMARGINBOLD_FONT));
					graphics.drawText(data[0],0+10,(getPreferredHeight()/2)-(graphics.getFont().getHeight()/2),0);
			if(data[1].length()>0)
			{
				if(align == ALIGN_LEFT_WITH_COLON )
				{
					graphics.drawText(": "+data[1],(AppConstants.screenWidth/2)/*0+AppConstants.screenWidth-70-2*/,padding*2+(padding/2)+4,0);
				}
				else if(align == ALIGN_LEFT )
				{
					graphics.drawText(data[1],(AppConstants.screenWidth/2)/*0+AppConstants.screenWidth-70-2*/,padding*2+(padding/2)+4,0);
				}
				else if(align == ALIGN_RIGHT_WITH_LAST_BOLD )
				{
					if(text.equalsIgnoreCase("Total_Amount"))
					graphics.setFont(FontLoader.getFont(AppConstants.REPORTSMARGINBOLD_FONT));
						graphics.drawText(data[1], AppConstants.screenWidth - graphics.getFont().getAdvance(data[1]) - (padding+4),(getPreferredHeight()/2)-(graphics.getFont().getHeight()/2),0);
					//setFont(font);
				}
				else
					graphics.drawText(": "+data[1],(AppConstants.screenWidth/2)/*0+AppConstants.screenWidth-70-2*/,padding*2+(padding/2)+4,0);
				
			}
			}	
		}
		
		protected void sublayout(int width, int height) {
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
	}
}
