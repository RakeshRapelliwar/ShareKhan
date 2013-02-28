package com.snapwork.view.trade;

import java.util.Vector;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.container.VerticalFieldManager;

import com.snapwork.beans.KeyValueBean;
import com.snapwork.util.AppConstants;

public class SlideViewOrderCancel extends VerticalFieldManager {
	
	private static byte padding = 4; 
	private Font font;
	public static boolean orderCancel;
	public static String MESSAGE = "";
	
	public SlideViewOrderCancel(Vector vector, Font font) {
		this.font = font;
		TabbedList tlv = null;
		String[] vect = new String[2];
		tlv = new TabbedList("ONE", this.font, vect, 0x608080);
			KeyValueBean modifyBean = null;
			int color;
			for(int i=0;i<vector.size();i++)
			{
				/*if(i%2==0) color = 0x4e4947;
				else color = 0x71746a;*/
				
				if(i%2!=0) color = 0x000000;
				else color = 0x333333;
				modifyBean = (KeyValueBean)vector.elementAt(i);
				String[] data2 = new String[2];
					data2[0]=modifyBean.getKey();
					//data2[]=stat.getValue().trim();
					
					data2[1] = modifyBean.getValue();
					tlv.addItem("ONES", this.font, data2, color, i);
			
				
			}
		add(tlv);
		//add(tabbedGlobalList);
	}
	
	public int getPreferredHeight() {
		try {
			return AppConstants.screenHeight;
		} catch(Exception ex) {
		}
		return 0;
	}
	
	private class TabbedList extends VerticalFieldManager {
		
		LabelField lblListTitle = null;
		int color = Color.BLACK;
		String[] data = new String[2];
		String[] data2 = new String[2];
		Font font = null;
		boolean flag = true;
		public TabbedList(String strTitle, Font fnt, String[] dat, int clr) {
			super(FOCUSABLE);
			color = clr;
			font = fnt;
			if(dat.length==2)
				data = dat;
			else
			{
				data2 = dat;
				flag = false;
			}
				
			lblListTitle = new LabelField(strTitle, SlideViewOrderCancel.NON_FOCUSABLE) {

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
					if(color == 0x333333)
						graphics.setColor(0x999999);
					else
						graphics.setColor(0xeeeeee);
					graphics.setFont(font);
					//graphics.drawText(getText(), padding/2, padding/2);
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
		
		private void addItem(final String strTitle,final  Font fnt,final  String[] dat,final  int clr, final int id) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				
				public void run() {
					add(new TabbedListItem(FOCUSABLE, strTitle ,fnt,dat,clr, id));
				}
			});
		}

		
	}
	
	private class TabbedListItem extends Manager {

		private LabelField lblCompanyName = null;
		private boolean requireNullField = true;
		int color = Color.BLACK;
		String[] data = new String[3];
		String[] data2 = new String[3];
		Font font = null;
		boolean flag = true;
		int id ;
		
		public TabbedListItem(long style,String title,Font fnt,String[] dat,int clr, int id) {
			super(style);
			
			color = clr;
			font = fnt;
			this.id = id;
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
					graphics.setColor(color);
					graphics.fillRect(0, 1, getPreferredWidth(), getPreferredHeight()-2);
					if(color == 0x333333)
						graphics.setColor(0x999999);
					else
						graphics.setColor(0xeeeeee);
					graphics.setFont(font);
					//graphics.drawText(getText(), padding/2, padding/2);
					if(flag){
					graphics.drawText(data[0],0+2,padding*2+(padding/2)+4,0);
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

		protected void paintBackground(Graphics graphics) {
			graphics.setColor(0x333333);
			graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
			graphics.setColor(color);
			graphics.fillRect(0, 1, getPreferredWidth(), getPreferredHeight()-2);
			graphics.setColor(isFocus() == true ? Color.ORANGE : 0x333333);
			graphics.drawRect(0, 0, getPreferredWidth(), getPreferredHeight());
			
			if(color == 0x333333)
				graphics.setColor(0x999999);
			else
				graphics.setColor(0xeeeeee);
			graphics.setFont(font);
			//graphics.drawText(getText(), padding/2, padding/2);
			if(flag){
				int y = 10;
				if(id % 2 != 0)
					y = y*2;
			graphics.drawText(data[0],0+y,padding*2+(padding/2)+4,0);
		}		
			
		}
		
		protected void sublayout(int width, int height) {
			setExtent(getPreferredWidth(), getPreferredHeight());
		}
	}
}
