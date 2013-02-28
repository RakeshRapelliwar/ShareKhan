package com.snapwork.components;

import java.util.Vector;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.NullField;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.HomeJson;
import com.snapwork.util.AppConstants;
import com.snapwork.util.AutoRefreshableStream;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.Utils;
import com.snapwork.view.CompanyFNODetailsSnippetsScreen;
import com.snapwork.view.MyWatchList;
import com.snapwork.view.WatchListScreen;

/**
 * 
 * <p>
 * This class is creates WatchList fields and added to WatchList Screen.
 * <p>
 * This class create custom field which shows details of Company like Name,
 * Change in percent, etc.
 * 
 */

public class WatchListField extends Manager {
	private LabelField lblCompanyName = null;
	private LabelField lblValue = null;
	private LabelField lblChangedValue = null;
	private LabelField lblPercentage = null;
	private final static int itemBackColor = 4343106;
	private boolean requireNullField = true;
	private boolean isGainer = true;
	private ActionListener actionListner;
	private String companyCode = null;
	private String source = null;
	private static byte padding = 4;
	private static short fieldWidth = -1, fieldHeight = -1;
	private boolean startUpdate;
	private Bitmap refreshImage;
	private byte borderWidth = 3;
	private int h = 0;
	private boolean hLess = false;
	private String stringNSE = null;
	private long timer;
	private HomeJson homeJson = null;
	private Thread animationThread = null;
	private int companyFieldWidth;
	private Font font;
	public static boolean BSE = true;
	public static boolean NSE = false;
	private boolean screen = BSE;
	private String streamName;
	private int stream = 0;
	private int colorCode;
	private String compnameNODATA = "";
	private long time = System.currentTimeMillis();

	public WatchListField(long style, String companyName, String value,
			String changedValue, String percentage, String companyCode,
			String source, ActionListener actionListner, String stringnse,
			final HomeJson homejSon, boolean screen) {
		super(style);
		colorCode = itemBackColor;
		timer = System.currentTimeMillis();
		if (homejSon.getDisplayName2().indexOf(".00") > -1) {
			homejSon.setDisplayName2(Utils.replaceString(
					homejSon.getDisplayName2(), ".00", ""));
		}
		setHomeJSon(homejSon);
		
		
		
		
		
		if (AppConstants.screenHeight < 480)
			font = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT);
		else
			font = FontLoader.getFont(AppConstants.LABEL_FONT);
		setFont(font);
		setStreamName(companyName);
		/*
		 * if(homejSon.getLastTradedPrice().equals("null")) {
		 * homejSon.setChange("0"); homejSon.setPercentageDiff("0"); /*try{
		 * compnameNODATA = companyCode; Integer.parseInt(companyCode); }
		 * catch(Exception e){ compnameNODATA = companyCode; } lblValue = new
		 * LabelField("", 0) {
		 * 
		 * public int getPreferredHeight() { return getFont().getHeight(); }
		 * 
		 * protected void paint(Graphics graphics) {
		 * graphics.setColor(0xeeeeee); super.paint(graphics); }
		 * 
		 * protected void drawFocus(Graphics graphics, boolean on) { }
		 * 
		 * public int getPreferredWidth() { return
		 * getFont().getAdvance(getText())+5; }
		 * 
		 * protected void layout(int width, int height) { super.layout(width,
		 * height); setExtent(getPreferredWidth(), getPreferredHeight()); } };
		 * lblCompanyName = new LabelField("", 0) { public int
		 * getPreferredHeight() { return getFont().getHeight(); }
		 * 
		 * public int getPreferredWidth() { return companyFieldWidth; }
		 * 
		 * protected void paint(Graphics graphics) {
		 * graphics.setColor(0xeeeeee); super.paint(graphics); }
		 * 
		 * protected void drawFocus(Graphics graphics, boolean on) { }
		 * 
		 * protected void layout(int width, int height) { super.layout(width,
		 * height); setExtent(getPreferredWidth(), getPreferredHeight()); }
		 * };lblChangedValue = new LabelField("", 0) { public int
		 * getPreferredHeight() { return getFont().getHeight(); }
		 * 
		 * protected void paint(Graphics graphics) {
		 * graphics.setColor(0xeeeeee); super.paint(graphics); }
		 * 
		 * protected void drawFocus(Graphics graphics, boolean on) { }
		 * 
		 * public int getPreferredWidth() { return
		 * getFont().getAdvance(getText())+5; }
		 * 
		 * protected void layout(int width, int height) { super.layout(width,
		 * height); setExtent(getPreferredWidth(), getPreferredHeight()); } };
		 * 
		 * lblPercentage = new LabelField("", 0) { public int
		 * getPreferredHeight() { return getFont().getHeight(); }
		 * 
		 * protected void paint(Graphics graphics) {
		 * graphics.setColor(0xeeeeee); super.paint(graphics); }
		 * 
		 * protected void drawFocus(Graphics graphics, boolean on) { }
		 * 
		 * public int getPreferredWidth() { return
		 * getFont().getAdvance(getText())+5; }
		 * 
		 * protected void layout(int width, int height) { super.layout(width,
		 * height); setExtent(getPreferredWidth(), getPreferredHeight()); } }; }
		 * else
		 */
		{
			stringNSE = stringnse;
			this.screen = screen;

			value = removeComma(value);
			LOG.print("-+-+-+-+");
			LOG.print(companyName + " : " + value);
			lblChangedValue = new LabelField("", 0) {
				public int getPreferredHeight() {
					return getFont().getHeight();
				}

				protected void paint(Graphics graphics) {
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText()) + 5;
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};

			lblPercentage = new LabelField("", 0) {
				public int getPreferredHeight() {
					return getFont().getHeight();
				}

				protected void paint(Graphics graphics) {
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText()) + 5;
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};
			isGainer = (changedValue.indexOf("-") == -1);
			setChange(changedValue);
			setPercentageDiff(percentage);

			if (fieldWidth == -1)
				fieldWidth = (short) (font.getAdvance("##########") + padding);
			if (fieldHeight == -1)
				fieldHeight = (short) (getPreferredHeight() - padding);
			this.actionListner = actionListner;
			this.companyCode = companyCode;
			this.source = source;
			/*
			 * if(screen == NSE){ for(int
			 * i=0;i<Utils.getWatchListedCompanyRecords().size();i++){
			 * WatchListJson homeJSonNSE =
			 * (WatchListJson)Utils.getWatchListedCompanyRecords().elementAt(i);
			 * if(companyName.equals(homeJSonNSE.getSymbol())) { //this.source =
			 * homeJSonNSE.getCode(); //this.companyCode =
			 * homeJSonNSE.getSymbol(); //stringNSE = homeJSonNSE.getSymbol();
			 * this.source = homeJSonNSE.getSymbol(); this.companyCode =
			 * homeJSonNSE.getCode(); stringNSE = homeJSonNSE.getCode(); break;
			 * } } }
			 */
			companyFieldWidth = getPreferredWidth() - fieldWidth + 10 - padding
					* 2 - getFont().getAdvance(value);

			lblCompanyName = new LabelField(
					companyName.length() < 20 ? companyName
							: companyName.substring(0, 20), 0) {
				public int getPreferredHeight() {
					return getFont().getHeight();
				}

				public int getPreferredWidth() {
					return companyFieldWidth;
				}

				protected void paint(Graphics graphics) {
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void drawFocus(Graphics graphics, boolean on) {
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};

			lblValue = new LabelField(value, 0) {

				public int getPreferredHeight() {
					return getFont().getHeight();
				}

				protected void paint(Graphics graphics) {
					graphics.setColor(0xeeeeee);
					super.paint(graphics);
				}

				protected void paintBackground(Graphics graphics) {
					graphics.setColor(colorCode);
					graphics.fillRect(0, 0, getPreferredHeight(),
							getPreferredWidth() - 5);
					// super.paint(graphics);
				};

				protected void drawFocus(Graphics graphics, boolean on) {
				}

				public int getPreferredWidth() {
					return getFont().getAdvance(getText()) + 5;
				}

				protected void layout(int width, int height) {
					super.layout(width, height);
					setExtent(getPreferredWidth(), getPreferredHeight());
				}
			};
		}

		lblCompanyName.setFont(getFont());
		lblValue.setFont(getFont());
		lblChangedValue.setFont(getFont());
		lblPercentage.setFont(getFont());
		/*
		 * add(lblCompanyName); add(lblValue); add(lblChangedValue);
		 * add(lblPercentage);
		 */
		requireNullField = true;
		for (int i = 0; i < this.getFieldCount(); i++) {
			if (this.getField(i).isFocusable()) {
				requireNullField = false;
				i = this.getFieldCount();
			}
		}

		if (requireNullField) {
			NullField objNullField = new NullField(FOCUSABLE) {
				protected void onFocus(int direction) {
					if (direction == -1) {
						this.setPosition(0, 0);
					} else {
						this.setPosition(this.getManager().getWidth(), this
								.getManager().getHeight());
					}
					super.onFocus(direction);
				}

				protected void onUnfocus() {
					super.onUnfocus();
				}
			};
			add(objNullField);// A Field which will show focus on manager
		}

	}

	private String removeComma(String value) {
		while (value.indexOf(",") > -1) {
			value = value.substring(0, value.indexOf(","))
					+ value.substring(value.indexOf(",") + 1, value.length());
		}
		return value;
	}

	public String getStreamName() {
		return streamName;
	}

	public void setStreamName(String streamName) {
		this.streamName = streamName;
	}

	public boolean getExchange() {
		return this.screen;
	}

	public void setUpdatedData(String companyName, String value,
			String changedValue, String percentage, String companyCode,
			String source, ActionListener actionListner, String stringnse,
			HomeJson homejSon, boolean screen, boolean stre) {
		if (value.equals("null"))
			return;
		this.screen = screen;
		timer = System.currentTimeMillis();
		if (value.equals("null")) {
			homejSon.setChange("0");
			homejSon.setPercentageDiff("0");
			setHomeJSon(homejSon);
			/*
			 * try{ compnameNODATA = companyCode; Integer.parseInt(companyCode);
			 * } catch(Exception e)
			 */{
				compnameNODATA = companyCode;
			}
			if (AppConstants.screenHeight < 480)
				font = FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT);
			else
				font = FontLoader.getFont(AppConstants.LABEL_FONT);
			setFont(font);
		} else {
			stringNSE = stringnse;
			setStreamName(companyName);
			this.source = source;
			// stringNSE = null;
			this.companyCode = homejSon.getDisplayName1();
			/*
			 * if(screen == NSE){ for(int
			 * i=0;i<Utils.getWatchListedCompanyRecords().size();i++){
			 * WatchListJson homeJSonNSE =
			 * (WatchListJson)Utils.getWatchListedCompanyRecords().elementAt(i);
			 * if(companyName.equals(homeJSonNSE.getSymbol())) { //this.source =
			 * homeJSonNSE.getCode(); //this.companyCode =
			 * homeJSonNSE.getSymbol(); //stringNSE = homeJSonNSE.getSymbol();
			 * this.source = homeJSonNSE.getSymbol(); this.companyCode =
			 * homeJSonNSE.getCode(); stringNSE = homeJSonNSE.getCode(); break;
			 * } } }
			 */
			/*
			 * stringNSE = homejSon.getCompanyCode(); try {
			 * Double.parseDouble(stringNSE); } catch(Exception e) { stringNSE =
			 * null; }
			 */
			isGainer = (changedValue.indexOf("-") == -1);
			setHomeJSon(homejSon);
			this.actionListner = actionListner;

			// this.source = source;
			lblCompanyName.setText(homejSon.getDisplayName1());
			// stringNSE = homejSon.getCompanyCode();
			LOG.print("----------+++++++++++++()"
					+ homejSon.getLastTradedPrice());
			value = removeComma(homejSon.getLastTradedPrice());
			LOG.print("----------+++++++++++++" + value);
			lblValue.setText(removeComma(value));
			setChange(changedValue);
			setPercentageDiff(percentage, true);

			getHomeJSon().setLastTradedPrice(removeComma(value));
			getHomeJSon().setChange(changedValue);
			getHomeJSon().setChangePercent(percentage);
			// if(stre)
			// stream = 1;
			invalidate();
		}
	}

	public synchronized void setLastTradedPrice(String ltp) {
		if (getHomeJSon() != null) {
			if (getHomeJSon().getLastTradedPrice().equalsIgnoreCase("null"))
				return;
			double d = 0;
			double o = 0;
			try {
				d = Double.parseDouble(ltp);
				o = Double.parseDouble(lblValue.getText());
			} catch (Exception e) {
				return;
			}
			if (o < (d + 5))
				getHomeJSon().setLastTradedPrice(ltp);
			else if (o > (d - 5))
				getHomeJSon().setLastTradedPrice(ltp);
			else
				return;
		}
		if (lblValue.getText().length() != 0) {
			String getoldper = lblValue.getText();
			double valOld = 0.0;
			double valNew = 0.0;

			try {
				valOld = Double.parseDouble(getoldper);
			} catch (Exception e) {
				return;
			}
			try {
				valNew = Double.parseDouble(ltp);
			} catch (Exception e) {
				return;
			}
			if (valOld < valNew) {
				this.stream = 1;
				colorCode = Color.GREEN;
			} else if (valOld == valNew) {
				this.stream = 0;
				colorCode = itemBackColor;
			} else {
				this.stream = 2;
				colorCode = Color.RED;
			}

			LOG.print("-Stream ltp update " + this.stream);

		}

		lblValue.setText(ltp);
		LOG.print("Stream ltp update " + this.stream);
		invalidate();
		// if(flag && this.stream != 0)
		// setStream(this.stream);

	}

	public void setChange(String change) {
		if (getHomeJSon() != null) {
			if (getHomeJSon().getChange().equalsIgnoreCase("null"))
				return;
			getHomeJSon().setChange(change);
		}
		boolean gainer = (change.indexOf("-") == -1);
		if (gainer) {
			if (Double.parseDouble(change) == 0)
				lblChangedValue.setText("<>" + change);
			else
				lblChangedValue.setText("+" + change);
		} else
			lblChangedValue.setText(change);
	}

	public void setPercentageDiff(String per) {
		if (getHomeJSon() != null) {
			if (getHomeJSon().getPercentageDiff().equalsIgnoreCase("null"))
				return;
			getHomeJSon().setPercentageDiff(per);
		}
		/*
		 * boolean flag = true; if(lblPercentage.getText().length()!=0) { String
		 * getoldper = lblPercentage.getText(); if(getoldper.indexOf("%")>-1)
		 * getoldper = getoldper.substring(0,getoldper.length()-1); double
		 * valOld = Double.parseDouble(getoldper); double valNew =
		 * Double.parseDouble(per); if(valOld<valNew) stream = 1; else if(valOld
		 * == valNew) stream = 0; else stream = 2;
		 * 
		 * } else flag = false;
		 */
		boolean gainer = (per.indexOf("-") == -1);
		if (gainer) {
			if (Double.parseDouble(per) == 0)
				lblPercentage.setText("<>" + per + "%");
			else
				lblPercentage.setText("+" + per + "%");
		} else
			lblPercentage.setText(per + "%");
		/*
		 * if(flag) setStream(stream);
		 */
	}

	public void setPercentageDiff(String per, boolean flag) {
		if (getHomeJSon() != null) {
			if (getHomeJSon().getPercentageDiff().equalsIgnoreCase("null"))
				return;
			getHomeJSon().setPercentageDiff(per);
		}
		boolean gainer = (per.indexOf("-") == -1);
		if (gainer) {
			if (Double.parseDouble(per) == 0)
				lblPercentage.setText("<>" + per + "%");
			else
				lblPercentage.setText("+" + per + "%");
		} else
			lblPercentage.setText(per + "%");
	}

	public String getCompanyCode() {
		return this.companyCode;
	}

	public HomeJson getHomeJSon() {
		return homeJson;
	}

	public void setHomeJSon(HomeJson homeJson) {
		this.homeJson = homeJson;
	}

	public int getTotalFields() {
		if (requireNullField)
			return this.getFieldCount() - 1;
		else
			return this.getFieldCount();
	}

	public int getPreferredHeight() {
		return getFont().getHeight() * 3 + padding * 3;
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
		handlar();
		return super.navigationClick(status, time);
	}

	protected boolean touchEvent(TouchEvent message) {
		setFocus();
		if (message.getEvent() == TouchEvent.CLICK) {
			handlar();
		}
		return super.touchEvent(message);
	}

	protected boolean keyDown(int keyCode, int time) {
		int key = Keypad.key(keyCode);
		if (key == Keypad.KEY_ENTER) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					handlar();
					/*
					 * customPopUpScreen = new CustomPopUpScreen("1");
					 * UiApplication
					 * .getUiApplication().pushScreen(customPopUpScreen);
					 */
				}
			});
		} else if (key == Keypad.KEY_ESCAPE) {
			UiApplication.getUiApplication().invokeLater(new Runnable() {
				public void run() {
					ScreenInvoker.removeRemovableScreen();
				}
			});
		}
		return super.keyDown(keyCode, time);
	}

	private void handlar() {

		if (!getHomeJSon().getLastTradedPrice().equals("null")) {
			
			
			
			if ((timer + 100) < System.currentTimeMillis()) {
				
				
				
				
				timer = System.currentTimeMillis();
				// if(Utils.WATCHLIST_NAME.equalsIgnoreCase("NIFTY") ||
				// Utils.WATCHLIST_NAME.equalsIgnoreCase("SENSEX"));
				// else
				Utils.WATCHLIST_MODE = false;
				if (getHomeJSon().getDisplayName2().equalsIgnoreCase("BSE")
						|| getHomeJSon().getDisplayName2().equalsIgnoreCase(
								"NSE")) {
					
					
					
					
					Vector vectorCommandData = new Vector();
					MyWatchList.isFINISHED = true;
					MyWatchList.REFRESH = false;
					Utils.STOCK_PAGE_START = true;
					if (getHomeJSon().getDisplayName2().equalsIgnoreCase("BSE"))
						AppConstants.NSE = false;
					else
						AppConstants.NSE = true;
					
					
					
					/*
					 * if(stringNSE == null) {
					 * LOG.print("->->->->->->->->-> StringNSE"+stringNSE);
					 * vectorCommandData.addElement(lblCompanyName.getText());
					 * vectorCommandData.addElement(companyCode); } else {
					 * Utils.NSE_SYMBOL = lblCompanyName.getText();
					 * LOG.print("->->->->->->->->-< StringNSE"+stringNSE);
					 * vectorCommandData.addElement(lblCompanyName.getText());
					 * vectorCommandData.addElement(stringNSE); }
					 * vectorCommandData.addElement(source);
					 * actionListner.actionPerfomed
					 * (ActionCommand.CMD_COMPANY_DETAILS_SCREEN,
					 * vectorCommandData);
					 */
					vectorCommandData.addElement(getHomeJSon()
							.getDisplayName1());
					LOG.print("this.strTitle" + getHomeJSon().getDisplayName1());
					if (AppConstants.NSE) {
						
						
						
						vectorCommandData.addElement(getHomeJSon()
								.getDisplayName1());
						Utils.NSE_SYMBOL = getHomeJSon().getDisplayName1();
						LOG.print("symbol" + getHomeJSon().getDisplayName1());
					} else {
						
						
						if(getHomeJSon().getReligareCode()==null)
							vectorCommandData.addElement("");
						else
						vectorCommandData.addElement(getHomeJSon()
								.getReligareCode());
						
						
//						vectorCommandData.addElement(getHomeJSon()
//								.getReligareCode());
//						LOG.print("AppConstants.source"
//								+ getHomeJSon().getReligareCode());
					}
					AppConstants.source = getHomeJSon().getReligareCode();
					
					
					if(getHomeJSon().getReligareCode()==null)
						vectorCommandData.addElement("");
					else
					vectorCommandData.addElement(getHomeJSon()
							.getReligareCode());
					
					
					

				
					
					if (AppConstants.NSE){
						// actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN_NSE,
						// vectorCommandData);
					
						
						
						
						
						
						ActionInvoker.processCommand(new Action(
								ActionCommand.CMD_COMPANY_DETAILS_SCREEN_NSE,
								vectorCommandData));
						
					}
					else{
						// actionListner.actionPerfomed(ActionCommand.CMD_COMPANY_DETAILS_SCREEN,
						// vectorCommandData);
						
						
						
						ActionInvoker.processCommand(new Action(
								ActionCommand.CMD_COMPANY_DETAILS_SCREEN,
								vectorCommandData));
					}
				} else {

					CompanyFNODetailsSnippetsScreen csScreen = new CompanyFNODetailsSnippetsScreen(
							getHomeJSon(), -1, false);

				}
			}
		}
	}

	protected void paint(Graphics graphics) {
		if (startUpdate) {
			// Draw White Border Background
			// graphics.setColor(isFocused == true && dataFeed ? Color.ORANGE :
			// 0xeeeeee);
			// graphics.fillRoundRect(0, padding/2, getPreferredWidth(),
			// getPreferredHeight(), 10, 10);
			if (h > getPreferredHeight() - borderWidth * 2) {
				hLess = true;
				h = getPreferredHeight() - borderWidth * 2;
			}
			if (h < 3) {
				hLess = false;
			}
			if (hLess)
				h = h - 2;
			else
				h = h + 2;
			if (refreshImage != null) {
				Bitmap image = ImageManager.resizeBitmap(refreshImage,
						refreshImage.getWidth(), h);
				graphics.drawBitmap(0, (getPreferredHeight() / 2)
						- ((h + (borderWidth * 2)) / 2), image.getWidth(),
						image.getHeight(), image, 0, 0);
			} else {
				graphics.setColor(isFocus() == true ? Color.ORANGE : 0xeeeeee);
				graphics.fillRoundRect(0, (getPreferredHeight() / 2)
						- ((h + (borderWidth * 2)) / 2), getPreferredWidth(), h
						+ (borderWidth * 2), 10, 10);
				// graphics.setColor((isGainer == true ? Color.GREEN :
				// Color.RED));
				// graphics.fillRoundRect(borderWidth,
				// (((getPreferredHeight())/2)-(h/2)),
				// (getPreferredWidth()-borderWidth*2), h, 10, 10);
			}

		} else if (getHomeJSon().getLastTradedPrice().equals("null")) {
			graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
			graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
			graphics.setColor(itemBackColor);
			graphics.fillRoundRect(padding / 2, padding / 2,
					getPreferredWidth() - padding, getPreferredHeight()
							- padding, 10, 10);
			graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
			graphics.fillRect(getPreferredWidth() - padding / 2 - fieldWidth,
					padding / 2, fieldWidth, fieldHeight);
			graphics.setColor(Color.GREEN);
			graphics.fillRoundRect(getPreferredWidth() - padding / 2
					- fieldWidth, padding / 2, fieldWidth, fieldHeight, 10, 10);
			graphics.setColor(0xeeeeee);
			graphics.drawLine(getPreferredWidth() - padding / 2 - fieldWidth
					+ 10, padding / 2, getPreferredWidth() - padding / 2
					- fieldWidth + 10, fieldHeight);
			graphics.drawLine(getPreferredWidth() - padding / 2 - fieldWidth,
					padding / 2 + fieldHeight / 2, getPreferredWidth()
							- padding / 2 - 1, padding / 2 + fieldHeight / 2);
			graphics.setColor(itemBackColor);
			graphics.fillRect(getPreferredWidth() - padding / 2 - fieldWidth,
					padding / 2, 10, fieldHeight);
			graphics.setColor(0xeeeeee);
			graphics.setFont(font);
			graphics.drawText(
					getHomeJSon().getDisplayName1(),
					padding,
					getPreferredHeight() / 4
							- lblCompanyName.getPreferredHeight() / 2);
			graphics.drawText(
					getHomeJSon().getDisplayName2(),
					padding,
					getPreferredHeight()
							/ 2
							+ (getPreferredHeight() / 4 - lblCompanyName
									.getPreferredHeight() / 2));
			graphics.drawText("0.00", padding
					+ (getPreferredWidth() - fieldWidth - graphics.getFont()
							.getAdvance("0.00")), getPreferredHeight() / 2
					- lblValue.getPreferredHeight() / 2);
			// graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			graphics.drawText("0.00", getPreferredWidth()
					- padding
					/ 2
					- fieldWidth
					+ 10
					+ ((fieldWidth - 10) / 2 - lblChangedValue.getFont()
							.getAdvance("0.00") / 2),
					(getPreferredHeight() / 4 - lblChangedValue
							.getPreferredHeight() / 2)/* padding */);
			// graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			graphics.drawText(
					"0.00%",
					getPreferredWidth()
							- padding
							/ 2
							- fieldWidth
							+ 10
							+ ((fieldWidth - 10) / 2 - lblPercentage.getFont()
									.getAdvance("0.00%") / 2),
					getPreferredHeight()
							/ 2
							+ (getPreferredHeight() / 4 - lblPercentage
									.getPreferredHeight() / 2) /*
																 * graphics.getFont
																 * (
																 * ).getHeight()
																 * +
																 * padding+padding
																 * /2
																 */);

		} else {
			graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
			graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
			graphics.setColor(itemBackColor);
			graphics.fillRoundRect(padding / 2, padding / 2,
					getPreferredWidth() - padding, getPreferredHeight()
							- padding, 10, 10);
			graphics.setColor(isFocus() == true ? Color.ORANGE : Color.BLACK);
			graphics.fillRect(getPreferredWidth() - padding / 2 - fieldWidth,
					padding / 2, fieldWidth, fieldHeight);
			graphics.setColor(isGainer == true ? Color.GREEN : Color.RED);
			graphics.fillRoundRect(getPreferredWidth() - padding / 2
					- fieldWidth, padding / 2, fieldWidth, fieldHeight, 10, 10);
			graphics.setColor(0xeeeeee);
			graphics.drawLine(getPreferredWidth() - padding / 2 - fieldWidth
					+ 10, padding / 2, getPreferredWidth() - padding / 2
					- fieldWidth + 10, fieldHeight + 1);
			graphics.drawLine(getPreferredWidth() - padding / 2 - fieldWidth,
					padding / 2 + fieldHeight / 2, getPreferredWidth()
							- padding / 2 - 1, padding / 2 + fieldHeight / 2);
			graphics.setColor(itemBackColor);
			graphics.fillRect(getPreferredWidth() - padding / 2 - fieldWidth,
					padding / 2, 10, fieldHeight);
			// LOG.print("getStream()"+colorCode);
			graphics.setColor(colorCode);
			graphics.fillRect(padding
					+ (getPreferredWidth() - fieldWidth - graphics.getFont()
							.getAdvance(lblValue.getText())) - 8,
					getPreferredHeight() / 2 - lblValue.getPreferredHeight()
							/ 2 - 4,
					graphics.getFont().getAdvance(lblValue.getText()) + 10,
					lblValue.getPreferredHeight() + 7);

			/*
			 * if(this.stream==1 || colorCode == Color.GREEN) {
			 * graphics.setColor(Color.GREEN);
			 * graphics.fillRect(padding+(getPreferredWidth()-fieldWidth -
			 * graphics.getFont().getAdvance(lblValue.getText()))-8,
			 * getPreferredHeight()/2 - lblValue.getPreferredHeight()/2 -
			 * 4,graphics.getFont().getAdvance(lblValue.getText())+10,lblValue.
			 * getPreferredHeight()+7);} else if(this.stream==2 || colorCode ==
			 * Color.RED) { graphics.setColor(Color.RED);
			 * graphics.fillRect(padding+(getPreferredWidth()-fieldWidth -
			 * graphics.getFont().getAdvance(lblValue.getText()))-8,
			 * getPreferredHeight()/2 - lblValue.getPreferredHeight()/2 -
			 * 4,graphics.getFont().getAdvance(lblValue.getText())+10,lblValue.
			 * getPreferredHeight()+7); }
			 */
			graphics.setColor(0xeeeeee);
			graphics.setFont(font);
			graphics.drawText(
					getHomeJSon().getDisplayName1(),
					padding,
					getPreferredHeight() / 4
							- lblCompanyName.getPreferredHeight() / 2);
			graphics.drawText(
					getHomeJSon().getDisplayName2(),
					padding,
					getPreferredHeight()
							/ 2
							+ (getPreferredHeight() / 4 - lblCompanyName
									.getPreferredHeight() / 2));
			// graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			graphics.drawText(lblValue.getText(), padding
					+ (getPreferredWidth() - fieldWidth - graphics.getFont()
							.getAdvance(lblValue.getText())),
					getPreferredHeight() / 2 - lblValue.getPreferredHeight()
							/ 2);
			// graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			graphics.drawText(
					lblChangedValue.getText(),
					getPreferredWidth()
							- padding
							/ 2
							- fieldWidth
							+ 10
							+ ((fieldWidth - 10) / 2 - lblChangedValue
									.getPreferredWidth() / 2),
					(getPreferredHeight() / 4 - lblChangedValue
							.getPreferredHeight() / 2)/* padding */);
			// graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
			graphics.drawText(
					lblPercentage.getText(),
					getPreferredWidth()
							- padding
							/ 2
							- fieldWidth
							+ 10
							+ ((fieldWidth - 10) / 2 - lblPercentage
									.getPreferredWidth() / 2),
					getPreferredHeight()
							/ 2
							+ (getPreferredHeight() / 4 - lblPercentage
									.getPreferredHeight() / 2) /*
																 * graphics.getFont
																 * (
																 * ).getHeight()
																 * +
																 * padding+padding
																 * /2
																 */);
		}
	}

	protected void sublayout(int width, int height) {
		/*
		 * layoutChild(getField(0), getField(0).getPreferredWidth(),
		 * getField(0).getPreferredHeight()); layoutChild(getField(1),
		 * getField(1).getPreferredWidth(), getField(1).getPreferredHeight());
		 * layoutChild(getField(2), getField(2).getPreferredWidth(),
		 * getField(2).getPreferredHeight()); layoutChild(getField(3),
		 * getField(3).getPreferredWidth(), getField(3).getPreferredHeight());
		 * setPositionChild(getField(0), padding, getPreferredHeight()/2 -
		 * getField(0).getPreferredHeight()/2); setPositionChild(getField(1),
		 * padding+getField(0).getPreferredWidth(), getPreferredHeight()/2 -
		 * getField(1).getPreferredHeight()/2); setPositionChild(getField(2),
		 * getPreferredWidth
		 * ()-padding/2-fieldWidth+10+((fieldWidth-10)/2-getField
		 * (2).getPreferredWidth()/2), padding); setPositionChild(getField(3),
		 * getPreferredWidth
		 * ()-padding/2-fieldWidth+10+((fieldWidth-10)/2-getField
		 * (3).getPreferredWidth()/2), getFont().getHeight()+padding+padding/2);
		 */if (requireNullField) {
			layoutChild(this.getField(this.getFieldCount() - 1), 1, 1);
			setPositionChild(this.getField(this.getFieldCount() - 1),
					AppConstants.screenWidth / 2, getPreferredHeight() / 2);
		}
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	/*
	 * public class CustomPopUpScreen extends PopupScreen { public
	 * CustomPopUpScreen(String text) { super(new
	 * VerticalFieldManager(CustomPopUpScreen.NO_HORIZONTAL_SCROLL)); add(new
	 * ButtonField(text+"CBA")); add(new ButtonField(text+"ZYX")); }
	 * 
	 * protected boolean keyDown( int keyCode, int time ) { int key =
	 * Keypad.key( keyCode ); if(key== Keypad.KEY_ENTER) {
	 * /*UiApplication.getUiApplication().invokeLater(new Runnable() { public
	 * void run() { customPopUpScreen = new CustomPopUpScreen("1");
	 * UiApplication.getUiApplication().pushScreen(customPopUpScreen); } });* }
	 * else if(key==Keypad.KEY_ESCAPE) { synchronized(
	 * UiApplication.getEventLock() ){
	 * 
	 * if(isDisplayed()) { close();
	 * 
	 * } } } return super.keyDown(keyCode, time); } }
	 */

	public void startUpdate() {
		/*
		 * //int height = getPreferredHeight() + borderWidth + padding/2;
		 * refreshImage = new Bitmap( getPreferredWidth(),
		 * getPreferredHeight()); Graphics graphics = new
		 * Graphics(refreshImage); graphics.setColor(Color.BLACK);
		 * graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
		 * graphics.setColor(itemBackColor); graphics.fillRoundRect(padding/2,
		 * padding/2, getPreferredWidth()-padding, getPreferredHeight()-padding,
		 * 10, 10); graphics.setColor(isFocus() == true ? Color.ORANGE :
		 * Color.BLACK);
		 * graphics.fillRect(getPreferredWidth()-padding/2-fieldWidth,
		 * padding/2, fieldWidth, fieldHeight); graphics.setColor(isGainer ==
		 * true ? Color.GREEN : Color.RED);
		 * graphics.fillRoundRect(getPreferredWidth()-padding/2-fieldWidth,
		 * padding/2, fieldWidth, fieldHeight,10,10);
		 * graphics.setColor(0xeeeeee);
		 * graphics.drawLine(getPreferredWidth()-padding/2-fieldWidth+10,
		 * padding/2, getPreferredWidth()-padding/2-fieldWidth+10, fieldHeight);
		 * graphics.drawLine(getPreferredWidth()-padding/2-fieldWidth,
		 * padding/2+fieldHeight/2, getPreferredWidth()-padding/2-1,
		 * padding/2+fieldHeight/2); graphics.setColor(itemBackColor);
		 * graphics.fillRect(getPreferredWidth()-padding/2-fieldWidth,
		 * padding/2, 10, fieldHeight); graphics.setColor(0xeeeeee);
		 * graphics.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
		 * graphics.drawText(lblCompanyName.getText(), padding,
		 * getPreferredHeight()/2 - lblCompanyName.getPreferredHeight()/2);
		 * //graphics
		 * .setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		 * graphics.drawText(lblValue.getText(), padding+(getPreferredWidth()
		 * -fieldWidth- graphics.getFont().getAdvance(lblValue.getText())),
		 * getPreferredHeight()/2 - lblValue.getPreferredHeight()/2);
		 * //graphics.
		 * setFont(FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT));
		 * graphics.drawText(lblChangedValue.getText(),
		 * getPreferredWidth()-padding
		 * /2-fieldWidth+10+((fieldWidth-10)/2-lblChangedValue
		 * .getPreferredWidth()/2), padding);
		 * //graphics.setFont(FontLoader.getFont
		 * (AppConstants.MEDIUM_BOLD_FONT));
		 * graphics.drawText(lblPercentage.getText(),
		 * getPreferredWidth()-padding
		 * /2-fieldWidth+10+((fieldWidth-10)/2-lblPercentage
		 * .getPreferredWidth()/2),
		 * graphics.getFont().getHeight()+padding+padding/2);
		 * 
		 * startUpdate = true; //if(animationThread == null) //{ animationThread
		 * = new Thread(new Runnable() { public void run() { invalidate(); try {
		 * for(int i=1;i<31;i++){ Thread.sleep(10); if(i==30) startUpdate =
		 * false; invalidate(); } } catch (InterruptedException e) {
		 * LOG.print("Exception in banner screen"); } }});
		 * animationThread.start(); //}
		 */
	}

	public int getStream() {
		// LOG.print("stream---000----"+this.stream);
		return this.stream;
	}

	public void setStream(int stream) {
		/*
		 * if(stream==0) { if(time+100>System.currentTimeMillis()) { time =
		 * System.currentTimeMillis(); stream = 0; } } else {
		 */
		// LOG.print("stream-------"+stream);
		this.stream = stream;
		if (stream == 1)
			colorCode = Color.GREEN;
		else if (stream == 2)
			colorCode = Color.RED;
		else
			colorCode = itemBackColor;
		this.invalidate();
		/* } */

	}

	public void setInvalidate() {
		this.invalidate();
	}

	public static int getFieldHeight() {
		return FontLoader.getFont(AppConstants.MEDIUM_BOLD_FONT).getHeight()
				* 2 + padding * 2;
	}
}