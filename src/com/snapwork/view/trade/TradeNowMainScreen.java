package com.snapwork.view.trade;

import java.util.Hashtable;
import java.util.Vector;

import net.rim.blackberry.api.invoke.TaskArguments;
import net.rim.device.api.system.Bitmap;
import net.rim.device.api.system.Characters;
import net.rim.device.api.system.DeviceInfo;
import net.rim.device.api.system.EventInjector.KeyCodeEvent;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.ContextMenu;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.FieldChangeListener;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.Keypad;
import net.rim.device.api.ui.Manager;
import net.rim.device.api.ui.MenuItem;
import net.rim.device.api.ui.ScrollChangeListener;
import net.rim.device.api.ui.TouchEvent;
import net.rim.device.api.ui.UiApplication;
import net.rim.device.api.ui.component.BitmapField;
import net.rim.device.api.ui.component.ButtonField;
import net.rim.device.api.ui.component.Dialog;
import net.rim.device.api.ui.component.LabelField;
import net.rim.device.api.ui.component.Menu;
import net.rim.device.api.ui.component.NullField;
import net.rim.device.api.ui.component.ObjectChoiceField;
import net.rim.device.api.ui.container.HorizontalFieldManager;
import net.rim.device.api.ui.container.MainScreen;
import net.rim.device.api.ui.container.PopupScreen;
import net.rim.device.api.ui.container.VerticalFieldManager;
import net.rim.device.api.ui.decor.BackgroundFactory;

import com.snapwork.actions.Action;
import com.snapwork.actions.ActionCommand;
import com.snapwork.actions.ActionInvoker;
import com.snapwork.actions.ActionListener;
import com.snapwork.beans.FoScrips;
import com.snapwork.beans.HomeJson;
import com.snapwork.beans.TradeNowMainBean;
import com.snapwork.components.AutoRefreshableScreen;
import com.snapwork.components.BlockField;
import com.snapwork.components.BottomMenu;
import com.snapwork.components.CustomBasicEditField;
import com.snapwork.components.CustomLabelField;
import com.snapwork.components.CustomObjectChoiceField;
import com.snapwork.components.CustomObjectChoiceFieldReg;
import com.snapwork.components.FieldGroup;
import com.snapwork.components.RemovableScreen;
import com.snapwork.components.Snippets;
import com.snapwork.components.TitleBar;
import com.snapwork.components.TradeScreenBanner;
import com.snapwork.interfaces.ReturnDataWithId;
import com.snapwork.parsers.HomeJsonParser;
import com.snapwork.parsers.TradeNowMainParser;
import com.snapwork.util.AppConstants;
import com.snapwork.util.FontLoader;
import com.snapwork.util.ImageManager;
import com.snapwork.util.LOG;
import com.snapwork.util.ScreenInvoker;
import com.snapwork.util.UserInfo;
import com.snapwork.util.Utils;
import com.snapwork.view.CompanyDetailsSnippetsScreen;

public class TradeNowMainScreen extends MainScreen implements ActionListener, AutoRefreshableScreen, ReturnDataWithId, FieldChangeListener, RemovableScreen
{
        private TradeScreenBanner banner;
        public static boolean tradeStart;
        private BottomMenu bottomMenu = null;
        private HomeJson bann;
        private TradeNowMainParser tradeNowMainParser;
        private Hashtable hashmodifyReport;
        private String screenString;
        private  VerticalFieldManager vfmmain;
        public static Vector netPosVector;
        private final static int REFRESH_ID_SENSEX = 432;
                public static boolean CLOSE = true;
        private boolean refresh ;
        private long timer;
        private NullField nullField;
        private TradeNowMainScreen object = this;

        public TradeNowMainScreen(String screenString, String url,HomeJson banne) {
                getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
                getMainManager().setScrollListener(new ScrollChangeListener() {
                    public void scrollChanged(Manager manager, int i, int j) {
                        invalidate();
                    }
                });
                if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
                        Utils.snippetDiff = 15;
                refresh = true;
                this.bann = banne;
                timer  = System.currentTimeMillis();
                TradeNowMainScreen.tradeStart = true;
                this.screenString = screenString;
                //createUI(AppConstants.appTitle+" : Registration");
                //parser + url
                url = url + "&debug=2";
                //System.out.println(url);
                banner = new TradeScreenBanner(FIELD_HCENTER, banne.getSymbol(),banne.isExchange());
                banner.setData(banne.getLastTradedPrice(), banne.getChange(), (banne.getPercentageDiff().indexOf("-") == -1 ? "+"+banne.getPercentageDiff() : banne.getPercentageDiff()));

                final int banh = banner.getPreferredHeight()+8;

                vfmmain = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
                        public void paintBackground(Graphics graphics)
                        {
                                // graphics.setColor(Color.BLACK);
                                graphics.setBackgroundColor(Color.BLACK);//fillRect(0, 0, getWidth(), getHeight());
                                graphics.clear();
                                graphics.setColor(0x333333);
                                graphics.fillRoundRect(5, banh, AppConstants.screenWidth - 10, (((getField(2).getHeight()+4)*8)),8,8);
                                graphics.setColor(0xeeeeee);
                                int rowHeight = getField(2).getHeight()+4;
                                graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight/2)-(getField(2).getHeight()/2))+banh)+(getField(2).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh)+(getField(4).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh)+(getField(6).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh)+(getField(8).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh)+(getField(10).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh)+(getField(12).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh)+(getField(14).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh)+(getField(16).getHeight()/2)-(graphics.getFont().getHeight()/2));
                        }     
                        protected void sublayout( int maxWidth, int maxHeight )
                        {
                                layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
                                layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
                                layoutChild(getField(2), (AppConstants.screenWidth/2)-15, getField(2).getPreferredHeight());
                                layoutChild(getField(3), AppConstants.screenWidth/2, getField(3).getPreferredHeight());
                                layoutChild(getField(4), AppConstants.screenWidth/2, getField(4).getPreferredHeight());

                                layoutChild(getField(5), AppConstants.screenWidth/2, getField(5).getPreferredHeight());
                                layoutChild(getField(6), (AppConstants.screenWidth/2)-20, getField(6).getPreferredHeight());
                                layoutChild(getField(7), AppConstants.screenWidth/2, getField(7).getPreferredHeight());
                                layoutChild(getField(8), (AppConstants.screenWidth/2)-20, getField(8).getPreferredHeight());

                                layoutChild(getField(9), AppConstants.screenWidth/2, getField(9).getPreferredHeight());
                                layoutChild(getField(10), AppConstants.screenWidth/2, getField(10).getPreferredHeight());

                                layoutChild(getField(11), AppConstants.screenWidth/2, getField(11).getPreferredHeight());
                                layoutChild(getField(12), (AppConstants.screenWidth/2)-20, getField(12).getPreferredHeight());


                                layoutChild(getField(13), AppConstants.screenWidth/2, getField(13).getPreferredHeight());
                                layoutChild(getField(14), (AppConstants.screenWidth/2)-20, getField(14).getPreferredHeight());

                                layoutChild(getField(15), AppConstants.screenWidth/2, getField(15).getPreferredHeight());
                                layoutChild(getField(16), AppConstants.screenWidth/2, getField(16).getPreferredHeight());

                                layoutChild(getField(17), getField(17).getPreferredWidth(), getField(17).getPreferredHeight());

                                //int rowHeight = (FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4;
                                int rowHeight = getField(2).getHeight()+4;
                                setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getPreferredWidth()/2), 0);
                                setPositionChild(getField(1), 15, ((rowHeight/2)-(getField(1).getHeight()/2))+banh);
                                setPositionChild(getField(2), AppConstants.screenWidth/2, ((rowHeight/2)-(getField(2).getHeight()/2))+banh);
                                setPositionChild(getField(3), 15, ((rowHeight)+(rowHeight/2)-(getField(3).getHeight()/2))+banh);
                                setPositionChild(getField(4), AppConstants.screenWidth/2, ((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh);

                                setPositionChild(getField(5), 15, ((rowHeight*2)+(rowHeight/2)-(getField(5).getHeight()/2))+banh);
                                setPositionChild(getField(6), AppConstants.screenWidth/2, ((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh);
                                setPositionChild(getField(7), 15,((rowHeight*3)+(rowHeight/2)-(getField(7).getHeight()/2))+banh);
                                setPositionChild(getField(8), AppConstants.screenWidth/2, ((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh);

                                setPositionChild(getField(9), 15, ((rowHeight*4)+(rowHeight/2)-(getField(9).getHeight()/2))+banh);
                                setPositionChild(getField(10), AppConstants.screenWidth/2, ((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh);

                                setPositionChild(getField(11), 15, ((rowHeight*5)+(rowHeight/2)-(getField(11).getHeight()/2))+banh);
                                setPositionChild(getField(12), AppConstants.screenWidth/2, ((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh);


                                setPositionChild(getField(13), 15, ((rowHeight*6)+(rowHeight/2)-(getField(13).getHeight()/2))+banh);
                                setPositionChild(getField(14), AppConstants.screenWidth/2, ((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh);

                                setPositionChild(getField(15), 15, ((rowHeight*7)+(rowHeight/2)-(getField(15).getHeight()/2))+banh);
                                setPositionChild(getField(16), AppConstants.screenWidth/2, ((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh);
                                setPositionChild(getField(17), (AppConstants.screenWidth/2)-(getField(17).getPreferredWidth()/2), 4+(rowHeight*8)+banh);

                                setExtent(AppConstants.screenWidth,((38+(rowHeight*9)+banh+getField(17).getPreferredHeight())<AppConstants.screenHeight?AppConstants.screenHeight:((38+(rowHeight*9)+banh+getField(17).getPreferredHeight())))-TitleBar.getItemHeight());
                        }

                };
                tradeNowMainParser = new TradeNowMainParser(url,this,0);

        }
        public TradeNowMainScreen(HomeJson banne) {
                getMainManager().setBackground(BackgroundFactory.createSolidBackground(Color.BLACK));
            getMainManager().setScrollListener(new ScrollChangeListener() {
                public void scrollChanged(Manager manager, int i, int j) {
                    invalidate();
                }
            });
           if(AppConstants.screenHeight==800 && AppConstants.screenWidth==480)
                        Utils.snippetDiff = 15;
                this.bann = banne;
                banner = new TradeScreenBanner(FIELD_HCENTER, banne.getSymbol(),banne.isExchange());
                banner.setData(banne.getLastTradedPrice(), banne.getChange(), (banne.getPercentageDiff().indexOf("-") == -1 ? "+"+banne.getPercentageDiff() : banne.getPercentageDiff()));

                final int banh = banner.getPreferredHeight()+8;

                vfmmain = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
                        public void paintBackground(Graphics graphics)
                        {
                                // graphics.setColor(Color.BLACK);
                                graphics.setBackgroundColor(Color.BLACK);//fillRect(0, 0, getWidth(), getHeight());
                                graphics.clear();
                                graphics.setColor(0x333333);
                                graphics.fillRoundRect(5, banh, AppConstants.screenWidth - 10, (((getField(2).getHeight()+4)*8)),8,8);
                                int rowHeight = getField(2).getHeight()+4;
                                graphics.setColor(0xeeeeee);
                                graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight/2)-(getField(2).getHeight()/2))+banh)+(getField(2).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh)+(getField(4).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh)+(getField(6).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh)+(getField(8).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh)+(getField(10).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh)+(getField(12).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh)+(getField(14).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh)+(getField(16).getHeight()/2)-(graphics.getFont().getHeight()/2));

                        }     
                        /*protected void sublayout( int maxWidth, int maxHeight )
                    {
                            super.sublayout(maxWidth,AppConstants.screenHeight);
                            super.setExtent(maxWidth,AppConstants.screenHeight-TitleBar.getItemHeight());
                    }*/
                        protected void sublayout( int maxWidth, int maxHeight )
                        {
                                layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
                                layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
                                layoutChild(getField(2), (AppConstants.screenWidth/2)-15, getField(2).getPreferredHeight());
                                layoutChild(getField(3), AppConstants.screenWidth/2, getField(3).getPreferredHeight());
                                layoutChild(getField(4), AppConstants.screenWidth/2, getField(4).getPreferredHeight());

                                layoutChild(getField(5), AppConstants.screenWidth/2, getField(5).getPreferredHeight());
                                layoutChild(getField(6), (AppConstants.screenWidth/2)-20, getField(6).getPreferredHeight());
                                layoutChild(getField(7), AppConstants.screenWidth/2, getField(7).getPreferredHeight());
                                layoutChild(getField(8), (AppConstants.screenWidth/2)-20, getField(8).getPreferredHeight());

                                layoutChild(getField(9), AppConstants.screenWidth/2, getField(9).getPreferredHeight());
                                layoutChild(getField(10), AppConstants.screenWidth/2, getField(10).getPreferredHeight());

                                layoutChild(getField(11), AppConstants.screenWidth/2, getField(11).getPreferredHeight());
                                layoutChild(getField(12), (AppConstants.screenWidth/2)-20, getField(12).getPreferredHeight());


                                layoutChild(getField(13), AppConstants.screenWidth/2, getField(13).getPreferredHeight());
                                layoutChild(getField(14), (AppConstants.screenWidth/2)-20, getField(14).getPreferredHeight());

                                layoutChild(getField(15), AppConstants.screenWidth/2, getField(15).getPreferredHeight());
                                layoutChild(getField(16), AppConstants.screenWidth/2, getField(16).getPreferredHeight());

                                layoutChild(getField(17), getField(17).getPreferredWidth(), getField(17).getPreferredHeight());

                                //int rowHeight = (FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()*2)+4;
                                int rowHeight = getField(2).getHeight()+4;
                                setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getPreferredWidth()/2), 0);
                                setPositionChild(getField(1), 15, ((rowHeight/2)-(getField(1).getHeight()/2))+banh);
                                setPositionChild(getField(2), AppConstants.screenWidth/2, ((rowHeight/2)-(getField(2).getHeight()/2))+banh);
                                setPositionChild(getField(3), 15, ((rowHeight)+(rowHeight/2)-(getField(3).getHeight()/2))+banh);
                                setPositionChild(getField(4), AppConstants.screenWidth/2, ((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh);

                                setPositionChild(getField(5), 15, ((rowHeight*2)+(rowHeight/2)-(getField(5).getHeight()/2))+banh);
                                setPositionChild(getField(6), AppConstants.screenWidth/2, ((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh);
                                setPositionChild(getField(7), 15,((rowHeight*3)+(rowHeight/2)-(getField(7).getHeight()/2))+banh);
                                setPositionChild(getField(8), AppConstants.screenWidth/2, ((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh);

                                setPositionChild(getField(9), 15, ((rowHeight*4)+(rowHeight/2)-(getField(9).getHeight()/2))+banh);
                                setPositionChild(getField(10), AppConstants.screenWidth/2, ((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh);

                                setPositionChild(getField(11), 15, ((rowHeight*5)+(rowHeight/2)-(getField(11).getHeight()/2))+banh);
                                setPositionChild(getField(12), AppConstants.screenWidth/2, ((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh);


                                setPositionChild(getField(13), 15, ((rowHeight*6)+(rowHeight/2)-(getField(13).getHeight()/2))+banh);
                                setPositionChild(getField(14), AppConstants.screenWidth/2, ((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh);

                                setPositionChild(getField(15), 15, ((rowHeight*7)+(rowHeight/2)-(getField(15).getHeight()/2))+banh);
                                setPositionChild(getField(16), AppConstants.screenWidth/2, ((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh);
                                setPositionChild(getField(17), (AppConstants.screenWidth/2)-(getField(17).getPreferredWidth()/2), 4+(rowHeight*8)+banh);

                                setExtent(AppConstants.screenWidth,((38+(rowHeight*9)+banh+getField(17).getPreferredHeight())<AppConstants.screenHeight?AppConstants.screenHeight:((38+(rowHeight*9)+banh+getField(17).getPreferredHeight())))-TitleBar.getItemHeight());
                        }

                };

        }
        protected void paintBackground(Graphics graphics) {
                graphics.setColor(Color.BLACK);
                graphics.fillRect(0, 0, this.getWidth(), this.getHeight());
        }

        CustomObjectChoiceFieldReg exchChoice = null; String[] exchChoiceText = {"NSE","BSE"};
        CustomObjectChoiceFieldReg actionChoice = null;
        String[] actionChoiceText = {"Buy","Sell","Sell against Margin","Buy Max","Sell Max"};
        String[] actionChoiceTextShort = {"B","S","SS","BM","SM"};
        CustomObjectChoiceFieldReg orderTypeChoice = null; String[] orderTypeChoiceText = {"Market","Limit"};
        CustomObjectChoiceFieldReg dp_idChoice = null; String[] dp_idChoiceText = {"13185700"};
        CustomBasicEditField qty, disc_qty, trigger_price,limit;
        CustomLabelField limitLabel;
        CustomLabelField orderTypeLabel;
        String errorStatus = "";
        public void createUI(String strTitle,final  HomeJson homeJson) {
                //set Title
                final Bitmap bmp = ImageManager.getTradeNow();

                TitleBar titleBar = new TitleBar("Place Order"); //strTitle
                setTitle(titleBar);




                CustomLabelField exchLabel = new CustomLabelField("Exchange", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                LOG.print("First ObjectChoiceField");
                if(exchChoice == null)
                        /*       exchChoice = new CustomObjectChoiceFieldReg("", exchChoiceText,0, ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                        public int getPreferredHeight() {return getFont().getHeight()*2;}
                        public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                        protected void layout(int width, int height) {
                                setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                        };
                };*/
                {
                        int iselect = 1;
                        if(AppConstants.NSE)
                                iselect = 0;
                        exchChoice = new CustomObjectChoiceFieldReg("", exchChoiceText,iselect, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT);
                }//exchChoice.setEditable(false);
                //exchChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                CustomLabelField actionLabel = new CustomLabelField("Action", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                LOG.print("Second ObjectChoiceField");
                if(actionChoice == null)
                        actionChoice = new CustomObjectChoiceFieldReg("", actionChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        /*  public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
                        public int getPreferredHeight() {return getFont().getHeight()*2;}
                        public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                        protected void layout(int width, int height) {
                                setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                        };*/
                        protected void fieldChangeNotify(int context)
                        {
                                vfmmain.invalidate();
                        }
                };
                //actionChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);

                //actionChoice.setMinimalWidth(actionChoice.getFont().getAdvance("@@@@@@@@@@@"));

                CustomLabelField qtyLabel = new CustomLabelField("Qty", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(qty == null)
                {
                        qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);
                        //qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        qty.setMaxSize(8);
                        qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }

                CustomLabelField disqtyLabel = new CustomLabelField("Disc.Qty", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(disc_qty == null)
                {
                        disc_qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);
                        disc_qty.setMaxSize(8);
                        //disc_qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        disc_qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }
                limitLabel = new CustomLabelField("Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                limitLabel.setColor(0xc0c0c0);
                if(limit == null){
                        limit = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                        limit.setChangeListener(this);
                        /*protected boolean keyChar(char ch, int status, int time)
                            {
                                        if(getText().indexOf('.')>-1)
                                        {
                                                if(ch == Characters.BACKSPACE || ch == Characters.DELETE)
                                                {
                                                        return super.keyChar(ch, status, time);
                                                }
                                                else if(getText().indexOf('.')==getTextLength()-3 )
                                                {
                                                        return true;
                                                }
                                                else
                                                {
                                                        return super.keyChar(ch, status, time);
                                    }
                                        }
                                        else
                                                return super.keyChar(ch, status, time);
                        }
                        };*/
                        limit.setText("0");
                        limit.setMaxSize(8);
                        limit.setEditable(false);
                        limit.setColor(0xc0c0c0);
                        //limit.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        limit.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }
                orderTypeLabel = new CustomLabelField("Order Type", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                boolean me = false;
                if(orderTypeChoice == null)
                {       orderTypeChoice = new CustomObjectChoiceFieldReg("", orderTypeChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE |  ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        /*     public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                        public int getPreferredHeight() {return getFont().getHeight()*2;}
                        public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                        protected void layout(int width, int height) {
                                setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                        };*/
                        protected void fieldChangeNotify(int context)
                        {
                                LOG.print("Context "+context);
                                LOG.print("getSelectedIndex() "+getSelectedIndex());
                                //Do what you want here
                                //getSelectedIndex()
                                if(getSelectedIndex() == 0)
                                {
                                        limitLabel.setColor(0xc0c0c0);
                                        limit.setColor(0xc0c0c0);
                                        limit.setText("0");
                                        limit.setEditable(false);
                                        //invalidateLayout();
                                }
                                else
                                {
                                        limitLabel.setColor(0xeeeeee);
                                        limit.setColor(0xeeeeee);
                                        limit.setEditable(true);

                                        //invalidateLayout();
                                }
                                vfmmain.invalidate();
                                LOG.print(" After vfm.invalidate() ");
                                //orderTypeLabel.setColor(0xeeeeee);//setVisualState(LabelField.VISUAL_STATE_ACTIVE);
                        }
                };
                me = true;
                }
                //orderTypeChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                //
                if(me)
                        orderTypeChoice.setSelectedIndex(1);

                CustomLabelField triggerPriceLabel = new CustomLabelField("Trigger Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(trigger_price == null)
                {
                        trigger_price = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                        trigger_price.setChangeListener(this);
                        /*  {
                                protected boolean keyChar(char ch, int status, int time)
                        {
                                        if(getText().indexOf('.')>-1)
                                        {
                                                if(ch == Characters.BACKSPACE || ch == Characters.DELETE)
                                                {
                                                        return super.keyChar(ch, status, time);
                                                }
                                        else if(getText().indexOf('.')==getTextLength()-3 )
                                        {
                                                return true;
                                        }
                                        else
                                        {
                                                return super.keyChar(ch, status, time);
                            }
                                }
                                else
                                        return super.keyChar(ch, status, time);
                    }
                    };*/
                        trigger_price.setText("0");
                        trigger_price.setMaxSize(8);
                        //trigger_price.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        trigger_price.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }

                CustomLabelField dp_idLabel = new CustomLabelField("DP ID", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(dp_idChoice == null)
                {/*dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0, ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                        public int getPreferredHeight() {return getFont().getHeight()*2;}
                        public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                        protected void layout(int width, int height) {
                                setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                        };
                };*/
                        dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT);
                        dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                }final String textButton = "Trade Now";
                final Bitmap bitmap = ImageManager.resizeBitmap(bmp, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance("Trade_Now")+18, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()+16) ;
                
                /*HorizontalFieldManager hfmobj = new HorizontalFieldManager(HorizontalFieldManager.FIELD_HCENTER)
                {
                        public void paintBackground(Graphics graphics)
                        {
                                graphics.setColor(Color.BLACK);
                                graphics.fillRect(0, 0, getWidth(), getHeight());
                        }

                };
                hfmobj.add(new Field(FOCUSABLE | DrawStyle.HCENTER)  {

                        
                         protected boolean navigationClick(int status,int time) {
                         //if(isValid())
                         {
                                 if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                 submit();
                                 }  
                         }
                         return super.navigationClick(status, time);
                 }

                 protected boolean touchEvent(TouchEvent message)
                 { setFocus();
                 if(message.getEvent() == TouchEvent.CLICK) {
                         if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                         submit();
                         }
                 }
                 return super.touchEvent(message);
                 }

                 protected void paint(Graphics graphics) 
                 {
                         if(isFocus()) {
                                 graphics.setColor(Color.ORANGE);
                         } else {
                                 graphics.setColor(0xeeeeee);
                         }
                         //graphics.fillRect(0, 0, getWidth(), getHeight());
                         graphics.drawBitmap(0,0, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);                                        
                         graphics.drawText(textButton,(bitmap.getWidth()/2)-(getFont().getAdvance("Trade_Now")/2),(bitmap.getHeight()/2)-(getFont().getHeight()/2));

                 }
                 protected void drawFocus(Graphics graphics, boolean on) 
                 {

                 }
                 public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
                 public int getPreferredHeight() {return  bitmap.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
                 public int getPreferredWidth() {return bitmap.getWidth();}//getFont().getAdvance("Trade Now");}

                 protected void layout(int arg0, int arg1) {
                         setExtent(getPreferredWidth(), getPreferredHeight());}
                });*/
                BitmapField btn = new BitmapField(bitmap, FOCUSABLE | DrawStyle.HCENTER)  {

                        protected boolean navigationClick(int status,int time) {
                                //if(isValid())
                                {
                                        if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                        
                                       submit();
                                        //if(vec !=null)
                                        //      ActionInvoker.processCommand(new Action(ActionCommand.CMD_TRADE_MODIFY,vec));
                                        }  
                                }
                                return super.navigationClick(status, time);
                        }

                        protected boolean touchEvent(TouchEvent message)
                        { //setFocus();
                        if(message.getEvent() == TouchEvent.CLICK) {
                                if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                //CLOSE = false;
                                submit();
                                //if(vec !=null)
                                        //ActionInvoker.processCommand(new Action(ActionCommand.CMD_TRADE_MODIFY,vec));
                                }
                        }
                        return super.touchEvent(message);
                        }
                       /* private boolean isFocused = false;
                        protected void onFocus(int direction) 
                        {
                                isFocused = true;
                                invalidate();
                        }
                        protected void onUnfocus() {
                                isFocused = false;
                                invalidate();
                        }*/
                        protected void paintBackground(Graphics graphics) {
                                /*graphics.setBackgroundColor(Color.BLACK);
                                graphics.clear();*/
                        }
                        protected void paint(Graphics graphics) 
                        {
                                if(isFocus()) {
                                        graphics.setColor(Color.ORANGE);
                                } else {
                                        graphics.setColor(0xeeeeee);
                                }
                                //graphics.fillRect(0, 0, getWidth(), getHeight());
                                graphics.drawBitmap(0,0, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);                                        
                                graphics.drawText(textButton,(bitmap.getWidth()/2)-(getFont().getAdvance("Trade_Now")/2),(bitmap.getHeight()/2)-(getFont().getHeight()/2));

                        }
                        protected void drawFocus(Graphics graphics, boolean on) 
                        {

                        }
                        public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
                        public int getPreferredHeight() {return  bitmap.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
                        public int getPreferredWidth() {return bitmap.getWidth();}//getFont().getAdvance("Trade Now");}

                        protected void layout(int arg0, int arg1) {
                                setExtent(getPreferredWidth(), getPreferredHeight());
                        }
                };
                try{
                	if(nullField==null)
                        nullField = new NullField(NullField.FOCUSABLE) ;
                	add(nullField);
                        vfmmain.add(banner);
                        vfmmain.add(exchLabel);
                        vfmmain.add(exchChoice);
                        vfmmain.add(actionLabel); 
                        vfmmain.add(actionChoice);
                        vfmmain.add(qtyLabel);
                        vfmmain.add(qty);
                        vfmmain.add(disqtyLabel);
                        vfmmain.add(disc_qty);
                        vfmmain.add(orderTypeLabel);
                        vfmmain.add(orderTypeChoice);
                        vfmmain.add(limitLabel);
                        vfmmain.add(limit);
                        vfmmain.add(triggerPriceLabel);
                        vfmmain.add(trigger_price);
                        vfmmain.add(dp_idLabel);
                        vfmmain.add(dp_idChoice);
                        //vfmmain.add(hfmobj);
                        vfmmain.add(btn);
                }
                catch(java.lang.IllegalArgumentException e)
                {
                        LOG.print(" ---------=======>>>>>> java.lang.IllegalArgumentException"+e);
                }
                catch(Exception e)
                {
                        LOG.print(" ---===>> Exception "+e);
                }

                add(vfmmain);
                LOG.print("All components added");
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                                ScreenInvoker.removeRemovableScreen();  
                        }
                });
                final TradeNowMainScreen tradeNowMainScreen = this;
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                                ScreenInvoker.pushScreen(tradeNowMainScreen, true, true);
                        }
                });
                bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);
        }


        public boolean onMenu(int instance) 
        {
                return false;
        }
        protected void makeMenu(Menu menu, int instance) {
                //super.makeMenu(menu, instance);
               /* Action action = new Action(ActionCommand.CMD_GRID_SCREEN,null);
                ActionInvoker.processCommand(action);*/
                 ContextMenu contextMenu = ContextMenu.getInstance();
             contextMenu.setTarget(this);
             contextMenu.clear();
             this.makeContextMenu(contextMenu);
             menu.deleteAll();
             menu.add(contextMenu);
        }
        /* protected  void makeMenu(Menu menu, int instance)
        {
                ContextMenu contextMenu = ContextMenu.getInstance();
                contextMenu.setTarget(this);
                contextMenu.clear();
                this.makeContextMenu(contextMenu);
                menu.deleteAll();
                menu.add(contextMenu);
        }*/
        public boolean onSavePrompt() {
                return true;
        }

        public void actionPerfomed(byte Command, Object sender) {
                switch(Command) {
                case ActionCommand.CMD_GRID_SCREEN:
                case ActionCommand.CMD_WATCHLIST_SCREEN:
                case ActionCommand.CMD_BSE_GL_SCREEN:
                case ActionCommand.CMD_NEWS_SCREEN:
                case ActionCommand.CMD_SEARCH_SCREEN:
                case ActionCommand.CMD_REPORTSB_SCREEN:
                        ActionInvoker.processCommand(new Action(Command,null));
                        break;
                default:
                };
        }


        public LabelField getLabels(final String text1, final String text2, final String text3)
        {
                return new LabelField("",FOCUSABLE)
                {
                        public void setFont(Font arg0) {
                                super.setFont(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT));
                        }
                        protected void paintBackground(Graphics graphics) {
                                graphics.setColor(0x000000);
                                graphics.fillRect(0, 0, getPreferredWidth(),getPreferredHeight());
                        }
                        protected void paint(Graphics graphics) {
                                graphics.setColor(0x222222);
                                graphics.fillRect(0, 2+(getFont().getHeight()), getPreferredWidth(), 4+(getFont().getHeight()*2));
                                graphics.setColor(0x999999);
                                graphics.drawText(text1,5,2);
                                graphics.drawText(text2,5,4+(getFont().getHeight()));
                                graphics.drawText(text3, 5, 6+(getFont().getHeight()*2));
                        }
                        public int getPreferredHeight() {
                                return 6+(FontLoader.getFont(AppConstants.MEDIUM_PLAIN_FONT).getHeight()*5);
                        }
                        public int getPreferredWidth() {
                                return AppConstants.screenWidth;
                        }
                        protected void layout(int width, int height) {
                                super.layout(width, getPreferredHeight());
                                setExtent(getPreferredWidth(), getPreferredHeight());
                        }
                };
        }

        public void setExchangeDisable() {
                try {
                        //exchChoice.setEditable(false);
                } catch(Exception ex) {
                        System.out.println("Exception is occured disabling Exchange Label");
                }
        }

        public void setData(Vector vector, int id) {
                if(!TradeNowMainScreen.tradeStart)
                        return;
                if(id == REFRESH_ID_SENSEX){
                        if(!(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowMainScreen))
                                return;
                        refresh = true;
                        final HomeJson homeJson = (HomeJson) vector.elementAt(0);
                        bann = homeJson;
                        UiApplication.getUiApplication().invokeLater(new Runnable() {

                                public void run() {
                                        banner.setStartFlag(false);
                                        banner.setData(homeJson.getLastTradedPrice(), homeJson.getChange(), (homeJson.getPercentageDiff().indexOf("-") == -1 ? "+"+homeJson.getPercentageDiff() : homeJson.getPercentageDiff()));
                                        banner.startUpdate();
                                }
                        });
                }else{
                        LOG.print("TradeNow setData id " + id);
                        if(id == 0)
                        {
                                //System.out.println("Response TradeNowMain");
                                if(vector.size()!=0)
                                {
                                        TradeNowMainBean bean = (TradeNowMainBean)vector.elementAt(0);
                                        // System.out.println("bean.getDpId().size()"+bean.getDpId().size());
                                        if(bean.getDpId().size()!=0)
                                        {
                                                String[] strx = new String[bean.getDpId().size()];

                                                for(int i=0;i<bean.getDpId().size();i++)
                                                {
                                                        strx[i]=(String)bean.getDpId().elementAt(i);
                                                }
                                                //System.out.println(strx[0]);
                                                // System.out.println(strx.length);
                                                dp_idChoiceText = strx;
                                                dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                                                {
                                                        /*  public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                                                public int getPreferredHeight() {return getFont().getHeight()*2;}
                                                public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                                                protected void layout(int width, int height) {
                                                        setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                                                };*/
                                                        protected void fieldChangeNotify(int context)
                                                        {
                                                                vfmmain.invalidate();
                                                        }
                                                };
                                                dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                                        }
                                        else
                                        {
                                                dp_idChoiceText = new String[1];
                                                dp_idChoiceText[0] = "0";
                                                dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0,  ObjectChoiceField.FORCE_SINGLE_LINE |  ObjectChoiceField.NON_FOCUSABLE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                                                {
                                                        /*  public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                                                public int getPreferredHeight() {return getFont().getHeight()*2;}
                                                public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                                                protected void layout(int width, int height) {
                                                        setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                                                };*/
                                                        protected void fieldChangeNotify(int context)
                                                        {
                                                                vfmmain.invalidate();
                                                        }
                                                };
                                                dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_DISABLED);
                                        }
                                        LOG.print("vector.elementAt(1) instanceof Hashtable wait");
                                        if(vector.elementAt(1) instanceof Hashtable)
                                        {
                                                LOG.print("vector.elementAt(1) instanceof Hashtable True");
                                                Hashtable hash = (Hashtable)vector.elementAt(1);
                                                String str = (String)hash.get("seldpId");
                                                String str2 = (String)hash.get("dpId");
                                                LOG.print(" seldpId "+str);
                                                LOG.print(" dpId "+str2);

                                                if(str != null || str2!=null)
                                                {
                                                        LOG.print(" inside seldpId ");
                                                if(str != null)
                                                        for(int i=0;i<dp_idChoiceText.length;i++)
                                                        {
                                                                if(str.equalsIgnoreCase(dp_idChoiceText[i]))
                                                                {
                                                                        dp_idChoice.setSelectedIndex(i);
                                                                        break;
                                                                }
                                                        }
                                                else
                                                {
                                                        dp_idChoiceText = new String[1];
                                                        dp_idChoiceText[0] = str2 ; 
                                                        dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0,  ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                                                        {
                                                                /*  public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                                                                public int getPreferredHeight() {return getFont().getHeight()*2;}
                                                                public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                                                                protected void layout(int width, int height) {
                                                                        setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                                                                };*/
                                                                protected void fieldChangeNotify(int context)
                                                                {
                                                                        vfmmain.invalidate();
                                                                }
                                                        };
                                                        dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                                                }

                                                LOG.print("dp_idChoice -->");

                                                str = (String)hash.get("exchange");
                                                int iselect = 1;
                                                if(str.equalsIgnoreCase("NSE"))
                                                        iselect = 0;
                                                exchChoice = new CustomObjectChoiceFieldReg("", exchChoiceText,iselect,  ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                                                {
                                                        /* public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                                                        public int getPreferredHeight() {return getFont().getHeight()*2;}
                                                        public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                                                        protected void layout(int width, int height) {
                                                                setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                                                        };*/
                                                        protected void fieldChangeNotify(int context)
                                                        {
                                                                vfmmain.invalidate();
                                                        }
                                                };
                                                /* if(str.equalsIgnoreCase("BSE"))
                                                        exchChoice.setSelectedIndex(0);
                                                else
                                                        exchChoice.setSelectedIndex(1);*/

                                                str = (String)hash.get("action");
                                                actionChoice = new CustomObjectChoiceFieldReg("", actionChoiceText,0,  ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                                                {
                                                        /* public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                                                        public int getPreferredHeight() {return getFont().getHeight()*2;}
                                                        public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                                                        protected void layout(int width, int height) {
                                                                setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                                                        };*/
                                                        protected void fieldChangeNotify(int context)
                                                        {
                                                                vfmmain.invalidate();
                                                        }
                                                };
                                                for(int i=0;i<actionChoiceTextShort.length;i++)
                                                {
                                                        if(str.equalsIgnoreCase(actionChoiceTextShort[i]))
                                                        {
                                                                actionChoice.setSelectedIndex(i);
                                                                break;
                                                        }
                                                }
                                                str = (String)hash.get("qty");
                                                qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);
                                                //qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                                                qty.setMaxSize(8);
                                                qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                                                qty.setText(str);

                                                str = (String)hash.get("disc_qty");
                                                disc_qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);
                                                disc_qty.setMaxSize(8);
                                                //disc_qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                                                disc_qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                                                disc_qty.setText(str);

                                                str = (String)hash.get("stopPrice");
                                                trigger_price = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                                                trigger_price.setChangeListener(this);/* {
                                                        protected boolean keyChar(char ch, int status, int time)
                                                    {
                                                                if(getText().indexOf('.')>-1)
                                                                {
                                                                        if(ch == Characters.BACKSPACE || ch == Characters.DELETE)
                                                                        {
                                                                                return super.keyChar(ch, status, time);
                                                                        }
                                                                else 
                                                                        if(getText().indexOf('.')==getTextLength()-3)
                                                                        {
                                                                                return true;
                                                                        }
                                                                        else
                                                                        {
                                                                                return super.keyChar(ch, status, time);
                                                            }
                                                                }
                                                                else
                                                                        return super.keyChar(ch, status, time);
                                                }
                                                };*/
                                                trigger_price.setText("0");
                                                trigger_price.setMaxSize(8);
                                                //trigger_price.setFilter(TextFilter.get(TextFilter.DEFAULT));
                                                trigger_price.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                                                trigger_price.setText(str);
                                                limitLabel = new CustomLabelField("Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                                                limitLabel.setColor(0xc0c0c0);

                                                str = (String)hash.get("limitPrice");
                                                limit = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                                                limit.setChangeListener(this);
                                                /*{
                                                        protected boolean keyChar(char ch, int status, int time)
                                                    {
                                                                if(getText().indexOf('.')>-1)
                                                                {
                                                                        if(ch == Characters.BACKSPACE || ch == Characters.DELETE)
                                                                        {
                                                                                return super.keyChar(ch, status, time);
                                                                        }
                                                                else if(getText().indexOf('.')==getTextLength()-3 )
                                                                        {
                                                                                return true;
                                                                        }
                                                                        else
                                                                        {
                                                                                return super.keyChar(ch, status, time);
                                                            }
                                                                }
                                                                else
                                                                        return super.keyChar(ch, status, time);
                                                }
                                                };*/
                                                limit.setText("0");
                                                limit.setMaxSize(8);
                                                limit.setEditable(false);
                                                limit.setColor(0xc0c0c0);
                                                //limit.setFilter(TextFilter.get(TextFilter.DEFAULT));
                                                limit.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                                                limit.setText(str);
                                                orderTypeLabel = new CustomLabelField("Order Type", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));

                                                str = (String)hash.get("orderType");
                                                LOG.print("Modify --> "+str);
                                                orderTypeChoice = new CustomObjectChoiceFieldReg("", orderTypeChoiceText,0,  ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                                                {
                                                        /*  public Font getFont() { return FontLoader.getFont(AppConstants.BIG_BOLD_FONT);}
                                                        public int getPreferredHeight() {return getFont().getHeight()*2;}
                                                        public int getPreferredWidth() {return getFont().getAdvance("@@@@@@@@@@@@");}
                                                        protected void layout(int width, int height) {
                                                                setExtent(getFont().getAdvance("@@@@@@@@@"), getFont().getHeight()*2);
                                                        };*/
                                                        protected void fieldChangeNotify(int context)
                                                        {
                                                                // System.out.println("Context "+context);
                                                                // System.out.println("getSelectedIndex() "+getSelectedIndex());
                                                                //Do what you want here
                                                                //getSelectedIndex()
                                                                if(getSelectedIndex() == 0)
                                                                {
                                                                        limitLabel.setColor(0xc0c0c0);
                                                                        limit.setColor(0xc0c0c0);
                                                                        limit.setText("0");
                                                                        limit.setEditable(false);
                                                                        //invalidateLayout();
                                                                }
                                                                else
                                                                {
                                                                        limitLabel.setColor(0xeeeeee);
                                                                        limit.setColor(0xeeeeee);
                                                                        limit.setEditable(true);
                                                                        //invalidateLayout();
                                                                }
                                                                orderTypeLabel.setVisualState(LabelField.VISUAL_STATE_ACTIVE);
                                                                vfmmain.invalidate();
                                                        }
                                                };
                                                orderTypeChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                                                int selectedindexorderTypeChoice = 0;
                                                if(str == null)
                                                        selectedindexorderTypeChoice = 0;
                                                else if(str.length() == 0)
                                                        selectedindexorderTypeChoice = 0;
                                                if(str.equalsIgnoreCase("market"))
                                                {  //orderTypeChoice.setSelectedIndex(0);
                                                selectedindexorderTypeChoice = 0;}
                                                else if(str.equalsIgnoreCase("limit"))
                                                {//orderTypeChoice.setSelectedIndex(1);
                                                        selectedindexorderTypeChoice = 1;}
                                                LOG.print("selectedindexorderTypeChoice "+ selectedindexorderTypeChoice);
                                                orderTypeChoice.setSelectedIndex(selectedindexorderTypeChoice);

                                                }
                                        }

                                }
                                if(screenString.equalsIgnoreCase("ReportNetPosition"))
                                        modifyScreenForNetPosition(screenString, vector, id, false);
                                else
                                {
                                        createUI("Trade Now",bann);
                                }
                        }
                }
        }

        public void submit()
        {
                double qtyvalue = 0;
                double discqtyvalue = 0;
                if(qty.getText().length()==0)
                {
                        Dialog.alert("Please enter Qty value");
                        return ;
                }
                else if(qty.getText().length()>0)
                {
                        qtyvalue = Double.parseDouble(qty.getText());
                        if(qtyvalue==0 || qtyvalue<0)
                        {
                                Dialog.alert("Order quantity cannot be zero or negative.");
                                return ;
                        }

                }

                if(disc_qty.getText().length()==0)
                {

                        //Dialog.alert("Please enter Disclosed value");
                        //return;
                }
                else if(disc_qty.getText().length()>0)
                {
                        discqtyvalue = Double.parseDouble(disc_qty.getText());
                        if(discqtyvalue<0)
                        {
                                Dialog.alert("Disclosed quantity cannot be negative.");
                                disc_qty.setFocus();
                                return ;
                        }
                        else if(qtyvalue==discqtyvalue);
                        else if(qtyvalue<=discqtyvalue)
                        {
                                Dialog.alert("Disclosed Quantity Should be Less Than Order Quantity");
                                disc_qty.setFocus();
                                return ;
                        }
                        else if(discqtyvalue<(qtyvalue/10) && discqtyvalue!=0)
                        {
                                Dialog.alert("Disclosed Quantity must be at least 10% of Order Quantity");
                                disc_qty.setFocus();
                                return ;
                        }

                }

                //For Limit
                if(orderTypeChoice.getSelectedIndex()==1){
                        if(limit.getText().length()==0)
                        {
                                Dialog.alert("Please enter Order Price value");
                                limit.setFocus();
                                return ;
                        }
                        else
                        {
                                double limitPrice = Double.parseDouble(limit.getText());
                                if(limitPrice == 0)
                                {
                                        Dialog.alert("Please enter Order Price value");
                                        limit.setFocus();
                                        return ;
                                }
                                else if(limitPrice < 0)
                                {
                                        Dialog.alert("Order Price cannot be negative.");
                                        limit.setFocus();
                                        return ;
                                }
                                else if(limit.getText().indexOf(".")>-1)
                                {
                                        if(limit.getText().indexOf(".")<(limit.getText().length()-3))
                                        {
                                                Dialog.alert("Only two decimals are allowed for Order Price");
                                                limit.setFocus();
                                                return ;
                                        }
                                }
                        }

                        if(actionChoice.getSelectedIndex()==0 || actionChoice.getSelectedIndex()==3 ) //Buy Max
                        {
                                if(trigger_price.getText().length()>0)
                                {
                                        double limitPrice = Double.parseDouble(limit.getText());

                                        double triggerPrice = Double.parseDouble(trigger_price.getText());
                                        if(triggerPrice < 0)
                                        {
                                                Dialog.alert("Trigger Price cannot be negative.");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                        else if(limitPrice < triggerPrice)
                                        {
                                                Dialog.alert("Trigger Price should be less than Order Price");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                }
                        }

                        if(actionChoice.getSelectedIndex()==1 || actionChoice.getSelectedIndex()==2 || actionChoice.getSelectedIndex()==4) //Sell Max
                        {
                                if(trigger_price.getText().length()>0)
                                {
                                        double limitPrice = 0;
                                        double triggerPrice = 0;
                                        try{
                                                limitPrice = Double.parseDouble(limit.getText());

                                                triggerPrice = Double.parseDouble(trigger_price.getText());
                                        }
                                        catch(Exception e){

                                        }
                                        if(triggerPrice < 0)
                                        {
                                                Dialog.alert("Trigger Price cannot be negative.");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                        else if(limitPrice > triggerPrice)
                                        {
                                                if(actionChoice.getSelectedIndex()==1 || actionChoice.getSelectedIndex()==4)
                                                {
                                                        if(triggerPrice != 0) 
                                                        {
                                                                Dialog.alert("Trigger Price should be greater than Order Price");
                                                                trigger_price.setFocus();
                                                                return ;
                                                        }

                                                }
                                                else
                                                {
                                                        Dialog.alert("Trigger Price should be greater than Order Price");
                                                        trigger_price.setFocus();
                                                        return ;
                                                }
                                        }
                                }
                        }
                }
                if(trigger_price.getText().length()==0)trigger_price.setText("0");
                else if(trigger_price.getText().indexOf("-")>-1)
                {
                        Dialog.alert("Trigger Price cannot be negative.");
                        trigger_price.setFocus();
                        return ;
                }
                CLOSE = false;
                LOG.print("submit TradeNowMainScreen.CLOSE "+CLOSE);
                if(limit.getText().length()==0)limit.setText("0");
                //Market url http://50.17.18.243/SK/placeOrder.php?companyId=12150008&ltp=830.40&per_change=-0.92&change=-7.70&custId=250037&exchange=NSE&action=BM&qty=3&disc-qty=1&orderType=market&stopPrice=0&dpId=13185788

                //Limit url  http://50.17.18.243/SK/placeOrder.php?companyId=12150008&ltp=830.40&per_change=-0.92&change=-7.70&custId=250037&exchange=NSE&action=BM&qty=3&disc-qty=1&orderType=limit&limitPrice=1&stopPrice=0&dpId=13185788

                String url = AppConstants.domainUrl+"placeOrder.php?companyId="+AppConstants.source+"&ltp="+bann.getLastTradedPrice()+"&per_change="+bann.getChangePercent()+"&change="+bann.getChange()+"&custId="+UserInfo.getUserID()+"&exchange=";
                url = url + exchChoiceText[exchChoice.getSelectedIndex()];
                url = url + "&action="+actionChoiceTextShort[actionChoice.getSelectedIndex()]+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType=";

                if(orderTypeChoice.getSelectedIndex()==0)
                        url = url + "market&stopPrice="+trigger_price.getText();
                else
                        url = url + "limit&limitPrice="+limit.getText()+"&stopPrice="+trigger_price.getText();

                url = url+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()];
                //AppConstants.WEBVIEW_URL = url;
                //////////////////////For confirm screen
                //http://50.17.18.243/SK/placeOrder.php?companyId=RELIANCE&exchange=NSE&ltp=830.40&action=BM&qty=3&disc-qty=1&orderType=market&limitPrice=0&stopPrice=0&per_change=-0.92&type=NEW&change=-7.70&custId=250037&dpId=13185788&btnConfirm=Confirm&btnModify=&x=46&y=21
                String mtext = "market";
                if(orderTypeChoice.getSelectedIndex()==1)
                        mtext = "limit";
                String urlConfirm = AppConstants.domainUrl+"placeOrder.php?companyId="+"##company_code##"+"&exchange="+exchChoiceText[exchChoice.getSelectedIndex()]+"&ltp="+bann.getLastTradedPrice()+"&action="+actionChoiceTextShort[actionChoice.getSelectedIndex()]+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+mtext+"&limitPrice="+limit.getText()+"&stopPrice="+trigger_price.getText()+"&per_change="+bann.getChangePercent()+"&type=NEW&change="+bann.getChange()+"&custId="+UserInfo.getUserID()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"&btnConfirm=Confirm&btnModify=";

                //////////////////////


                ////////////////For modify Screen
                //http://50.17.18.243/SK/placeOrder.php?companyId=RELIANCE&exchange=NSE&ltp=814.00&action=SM&qty=3&disc-qty=1&orderType=limit&limitPrice=2&stopPrice=0&per_change=-0.77&type=NEW&change=-6.35&custId=250037&dpId=13185788&btnConfirm=&btnModify=Modify&userAgent=bb&x=24&y=19
                String urlModify = AppConstants.domainUrl+"placeOrder.php?companyId="+"##company_code##"+"&exchange="+exchChoiceText[exchChoice.getSelectedIndex()]+"&ltp="+bann.getLastTradedPrice()+"&action="+actionChoiceTextShort[actionChoice.getSelectedIndex()]+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+mtext+"&limitPrice="+limit.getText()+"&stopPrice="+trigger_price.getText()+"&per_change="+bann.getChangePercent()+"&type=NEW&change="+bann.getChange()+"&custId="+UserInfo.getUserID()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"&btnConfirm=&btnModify=Modify=";
                ///////////////
                Vector urls = new Vector();
                urls.addElement(url);
                urls.addElement(urlConfirm);
                LOG.print("URL Confirm : "+urlConfirm);
                urls.addElement(urlModify);
                Vector second = new Vector();
                second.addElement(urls);
                second.addElement(bann);
                second.addElement("tradenow");
                ActionInvoker.processCommand(new Action(ActionCommand.CMD_TRADE_MODIFY,second));
                
        }

        /* public boolean onClose() {
                return super.onClose();
        }*/
        public boolean keyDown( int keyCode, int time )
        {
                int key = Keypad.key( keyCode );

                if(key == Keypad.KEY_MENU) {
                        UiApplication.getUiApplication().invokeLater(new Runnable() {
                                public void run() {
                                        try {
                                                if(bottomMenu != null)
                                                        bottomMenu.autoAttachDetachFromScreen();
                                        } catch (Exception e) {
                                        }
                                }
                        });
                }
                else if(key == Keypad.KEY_ESCAPE) {
                        synchronized( UiApplication.getEventLock() ){

                                if(isDisplayed()) 
                                {
                                        close();
                                }
                        }
                } 
                else if(key == Keypad.KEY_END)
                {
                        LOG.print("KEY_END EXIT from app");
                        System.exit(0);
                }
                else
                        return super.keyDown(keyCode, time);

                return true;
        }
        public void modifyScreenForReport(final String screenString, Vector vector, int id, final boolean PARTLY_EXEC_FLAG) {
                //set Title
                if(vector.size()==0) ;//
                TradeNowMainBean bean_dpid = (TradeNowMainBean)vector.elementAt(0);

                hashmodifyReport = new Hashtable();
                if(vector.elementAt(1) instanceof Hashtable)
                {
                        hashmodifyReport = (Hashtable)vector.elementAt(1);
                }
                final Bitmap bmp = ImageManager.getTradeNow();
                String exchange = (String)hashmodifyReport.get("exchange");
                LOG.print(hashmodifyReport.size()+" Exchange : "+exchange);
                if(exchange.equalsIgnoreCase("BSE"))
                	bann.setExchange(true);
                else
                	bann.setExchange(false);
                banner = new TradeScreenBanner(FIELD_HCENTER, bann.getSymbol(),bann.isExchange());
                banner.setData(bann.getLastTradedPrice(), bann.getChange(), (bann.getPercentageDiff().indexOf("-") == -1 ? "+"+bann.getPercentageDiff() : bann.getPercentageDiff()));

                final int banh = banner.getPreferredHeight()+8;

                TitleBar titleBar = new TitleBar("Trade Now"); 
                setTitle(titleBar);
                final VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
                        public void paintBackground(Graphics graphics)
                        {
                                graphics.setBackgroundColor(Color.BLACK);
                                graphics.clear();
                                graphics.setColor(0x333333);
                                graphics.fillRoundRect(5, banh, AppConstants.screenWidth - 10,  (((getField(12).getHeight()+4)*11))-(getField(12).getHeight()*2)-10,8,8);
                                int rowHeight = getField(12).getHeight()+4;
                                graphics.setColor(0xeeeeee);
                                graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight/2)-(getField(2).getHeight()/2))+banh)+(getField(2).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh)+(getField(4).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh)+(getField(6).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh)+(getField(8).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh)+(getField(10).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh)+(getField(12).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh)+(getField(14).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh)+(getField(16).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*8)+(rowHeight/2)-(getField(18).getHeight()/2))+banh)+(getField(18).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                //graphics.drawText(":", (AppConstants.screenWidth/2)-8, (((rowHeight*9)+(rowHeight/2)-(getField(20).getHeight()/2))+banh)+(getField(20).getHeight()/2)-(graphics.getFont().getHeight()/2));

                        }            
                        protected void sublayout( int maxWidth, int maxHeight )
                        {
                                //super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
                                layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
                                // layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
                                // layoutChild(getField(2), AppConstants.screenWidth/2, getField(2).getPreferredHeight());
                                layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
                                layoutChild(getField(2), AppConstants.screenWidth/2, getField(2).getPreferredHeight());
                                layoutChild(getField(3), AppConstants.screenWidth/2, getField(3).getPreferredHeight());
                                layoutChild(getField(4), AppConstants.screenWidth/2, getField(4).getPreferredHeight());
                                layoutChild(getField(5), AppConstants.screenWidth/2, getField(5).getPreferredHeight());
                                layoutChild(getField(6), AppConstants.screenWidth/2, getField(6).getPreferredHeight());
                                layoutChild(getField(7), AppConstants.screenWidth/2, getField(7).getPreferredHeight());

                                layoutChild(getField(8), (AppConstants.screenWidth/2)-20, getField(8).getPreferredHeight());
                                layoutChild(getField(9), (AppConstants.screenWidth/2), getField(9).getPreferredHeight());
                                layoutChild(getField(10),( AppConstants.screenWidth/2)-20, getField(10).getPreferredHeight());
                                layoutChild(getField(11), (AppConstants.screenWidth/2), getField(11).getPreferredHeight());

                                layoutChild(getField(12), AppConstants.screenWidth/2, getField(12).getPreferredHeight());
                                layoutChild(getField(13), AppConstants.screenWidth/2, getField(13).getPreferredHeight());

                                layoutChild(getField(14), (AppConstants.screenWidth/2)-20, getField(14).getPreferredHeight());
                                layoutChild(getField(15), (AppConstants.screenWidth/2), getField(15).getPreferredHeight());


                                layoutChild(getField(16), (AppConstants.screenWidth/2)-20, getField(16).getPreferredHeight());
                                layoutChild(getField(17), (AppConstants.screenWidth/2), getField(17).getPreferredHeight());

                                layoutChild(getField(18), AppConstants.screenWidth/2, getField(18).getPreferredHeight());

                                layoutChild(getField(19), getField(19).getPreferredWidth(), getField(19).getPreferredHeight());

                                int rowHeight = getField(12).getHeight()+4;
                                setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getPreferredWidth()/2), 0);
                                // setPositionChild(getField(1), 15, ((rowHeight/2)-(getField(1).getHeight()/2))+banh);
                                // setPositionChild(getField(2), AppConstants.screenWidth/2, ((rowHeight/2)-(getField(2).getHeight()/2))+banh);
                                setPositionChild(getField(1), 15, (rowHeight/2)-(getField(1).getHeight()/2)+banh);
                                setPositionChild(getField(2), AppConstants.screenWidth/2, ((rowHeight/2)-(getField(2).getHeight()/2))+banh);

                                setPositionChild(getField(3), 15, ((rowHeight)+(rowHeight/2)-(getField(3).getHeight()/2))+banh);
                                setPositionChild(getField(4), AppConstants.screenWidth/2, ((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh);
                                setPositionChild(getField(5), 15,((rowHeight*2)+(rowHeight/2)-(getField(5).getHeight()/2))+banh);
                                setPositionChild(getField(6), AppConstants.screenWidth/2, ((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh);

                                setPositionChild(getField(7), 15, ((rowHeight*3)+(rowHeight/2)-(getField(7).getHeight()/2))+banh);
                                setPositionChild(getField(8), AppConstants.screenWidth/2, ((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh);

                                setPositionChild(getField(9), 15, ((rowHeight*4)+(rowHeight/2)-(getField(9).getHeight()/2))+banh);
                                setPositionChild(getField(10), AppConstants.screenWidth/2, ((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh);


                                setPositionChild(getField(11), 15, ((rowHeight*5)+(rowHeight/2)-(getField(11).getHeight()/2))+banh);
                                setPositionChild(getField(12), AppConstants.screenWidth/2, ((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh);

                                setPositionChild(getField(13), 15, ((rowHeight*6)+(rowHeight/2)-(getField(13).getHeight()/2))+banh);
                                setPositionChild(getField(14), AppConstants.screenWidth/2, ((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh);
                                setPositionChild(getField(15), 15, ((rowHeight*7)+(rowHeight/2)-(getField(15).getHeight()/2))+banh);
                                setPositionChild(getField(16), AppConstants.screenWidth/2, ((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh);

                                setPositionChild(getField(17), 15, ((rowHeight*8)+(rowHeight/2)-(getField(17).getHeight()/2))+banh);
                                setPositionChild(getField(18), AppConstants.screenWidth/2, ((rowHeight*8)+(rowHeight/2)-(getField(18).getHeight()/2))+banh);


                                setPositionChild(getField(19), (AppConstants.screenWidth/2)-(getField(19).getPreferredWidth()/2), 4+(rowHeight*9)+banh);

                                setExtent(AppConstants.screenWidth,((38+(rowHeight*11)+banh+getField(19).getPreferredHeight())<AppConstants.screenHeight?AppConstants.screenHeight:((38+(rowHeight*10)+banh+getField(19).getPreferredHeight())))-TitleBar.getItemHeight());

                        }
                };

                //  CustomLabelField companyLabel = new CustomLabelField("Company", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                //  CustomLabelField companyLabelValue = new CustomLabelField((String)hashmodifyReport.get("company_name"), 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_BOLD_FONT));

                CustomLabelField orderNoLabel = new CustomLabelField("Order No", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                final CustomLabelField orderNoLabelValue = new CustomLabelField((String)hashmodifyReport.get("order_id"), 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
               

                CustomLabelField exchLabel = new CustomLabelField("Exchange", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                final CustomLabelField exchLabelValue = new CustomLabelField((String)hashmodifyReport.get("exchange"), 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
              //  String strxe = (String)hashmodifyReport.get("exchange");
             //   int iselect = 1;
              //  if(strxue.equalsIgnoreCase("NSE"))
               //         iselect = 0;
               // exchChoice = new CustomObjectChoiceFieldReg("", exchChoiceText,iselect, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT);
                
                CustomLabelField actionLabel = new CustomLabelField("Action", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
              /*  if(actionChoice == null)
                        actionChoice = new CustomObjectChoiceFieldReg("", actionChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        protected void fieldChangeNotify(int context)
                        {
                                vfm.invalidate();
                        }
                };
                String str = (String)hashmodifyReport.get("action");
                LOG.print("action - - - - - - - - - "+ str);
                for(int i=0;i<actionChoiceTextShort.length;i++)
                {
                        if(str.equalsIgnoreCase(actionChoiceTextShort[i]))
                        {
                                actionChoice.setSelectedIndex(i);
                                break;
                        }
                }
                actionChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_DISABLED);
                */
                String str = (String)hashmodifyReport.get("action");
                int ID = 0;
                for(int i=0;i<actionChoiceTextShort.length;i++)
                {
                        if(str.equalsIgnoreCase(actionChoiceTextShort[i]))
                        {
                                ID = i;
                                str = actionChoiceText[i];
                                break;
                        }
                }
                final int actionID = ID;
                final CustomLabelField actionLabelValue = new CustomLabelField(str, 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                
                CustomLabelField qtyLabel = new CustomLabelField("Qty", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(qty == null)
                {
                        qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);

                        //qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        qty.setMaxSize(8);
                        qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                        qty.setText((String)hashmodifyReport.get("qty"));
                }

                CustomLabelField disqtyLabel = new CustomLabelField("Disc.Qty", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(disc_qty == null)
                {
                        disc_qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE  | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);
                        disc_qty.setMaxSize(8);
                        //disc_qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        disc_qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                        disc_qty.setText((String)hashmodifyReport.get("disc_qty"));
                }
                limitLabel = new CustomLabelField("Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                limitLabel.setColor(0xc0c0c0);
                if(limit == null){
                        limit = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                        limit.setChangeListener(this);
                        limit.setText((String)hashmodifyReport.get("limitPrice"));
                        limit.setMaxSize(8);
                        limit.setEditable(false);
                        limit.setColor(0xc0c0c0);
                        //limit.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        limit.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }
                orderTypeLabel = new CustomLabelField("Order Type", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(orderTypeChoice == null)
                        orderTypeChoice = new CustomObjectChoiceFieldReg("", orderTypeChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        protected void fieldChangeNotify(int context)
                        {
                                //System.out.println("Context "+context);
                                // System.out.println("getSelectedIndex() "+getSelectedIndex());
                                //Do what you want here
                                //getSelectedIndex()
                                if(getSelectedIndex() == 0)
                                {
                                        limitLabel.setColor(0xc0c0c0);
                                        limit.setColor(0xc0c0c0);
                                        limit.setText("0");
                                        limit.setEditable(false);
                                        //invalidateLayout();
                                }
                                else
                                {
                                        limitLabel.setColor(0xeeeeee);
                                        limit.setEditable(true);
                                        limit.setColor(0xeeeeee);

                                        //invalidateLayout();
                                }
                                vfm.invalidate();
                                //orderTypeLabel.setColor(0xeeeeee);//setVisualState(LabelField.VISUAL_STATE_ACTIVE);
                        }
                };
                str = (String)hashmodifyReport.get("orderType") ;
                LOG.print("orderType --------> "+str);
                int selectedindexorderTypeChoice = 0;
                boolean flag = false;
                if(str == null)
                        flag = true;
                else if(str.length()==0)
                        flag = true;
                if(flag)
                {
                        str = (String)hashmodifyReport.get("limitPrice") ;
                        try
                        {
                                double dbl = Double.parseDouble((str));
                                if(dbl>0)
                                { orderTypeChoice.setSelectedIndex(1);
                                selectedindexorderTypeChoice = 1;}
                                else
                                {orderTypeChoice.setSelectedIndex(0);
                                selectedindexorderTypeChoice = 0;}
                        }
                        catch(Exception e)
                        {
                                { orderTypeChoice.setSelectedIndex(0);
                                selectedindexorderTypeChoice = 0;}
                        }
                }
                else
                {
                        if(str.equalsIgnoreCase("market"))
                        { orderTypeChoice.setSelectedIndex(0);
                        selectedindexorderTypeChoice = 0;}
                        else
                        {    orderTypeChoice.setSelectedIndex(1);
                        selectedindexorderTypeChoice = 1;}
                }

                orderTypeChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);


                CustomLabelField triggerPriceLabel = new CustomLabelField("Trigger Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(trigger_price == null)
                {
                        trigger_price = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                        trigger_price.setChangeListener(this);
                        trigger_price.setText((String)hashmodifyReport.get("stopPrice"));
                        trigger_price.setMaxSize(8);
                        //trigger_price.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        trigger_price.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }

                CustomLabelField dp_idLabel = new CustomLabelField("DP ID", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(bean_dpid.getDpId().size()!=0)
                {
                        String[] strx = new String[bean_dpid.getDpId().size()];

                        for(int i=0;i<bean_dpid.getDpId().size();i++)
                        {
                                strx[i]=(String)bean_dpid.getDpId().elementAt(i);
                        }
                        dp_idChoiceText = strx;
                        //dp_idChoice.setChoices(strx);
                        //CustomLabelField dp_idLabel = new CustomLabelField("DP ID", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                        dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                        {
                                protected void fieldChangeNotify(int context)
                                {
                                        vfm.invalidate();
                                }
                        };
                        dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                }
                else
                {
                        dp_idChoiceText = new String[1];
                        dp_idChoiceText[0] = "0";
                        dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.NON_FOCUSABLE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                        {
                                protected void fieldChangeNotify(int context)
                                {
                                        vfm.invalidate();
                                }
                        };
                        dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_DISABLED);
                }
                final String textButton = "Modify";
                final Bitmap bitmap = ImageManager.resizeBitmap(bmp, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance(textButton)+10, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()+16) ;

                BitmapField btn = new BitmapField(bitmap,  FOCUSABLE | DrawStyle.HCENTER)  {

                        protected boolean navigationClick(int status,int time) {
                                //if(isValid())
                                {
                                        if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                        submitModifyReport(screenString, exchLabelValue.getText(), actionLabelValue.getText(), actionID, PARTLY_EXEC_FLAG);
                                        }  
                                }
                                return super.navigationClick(status, time);
                        }

                        protected boolean touchEvent(TouchEvent message)
                        { setFocus();
                        if(message.getEvent() == TouchEvent.CLICK) {
                                if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                submitModifyReport(screenString, exchLabelValue.getText(), actionLabelValue.getText(), actionID, PARTLY_EXEC_FLAG);
                                }
                        }
                        return super.touchEvent(message);
                        }
                        private boolean isFocused = false;
                        protected void onFocus(int direction) 
                        {
                                isFocused = true;
                                invalidate();
                        }
                        protected void onUnfocus() {
                                isFocused = false;
                                invalidate();
                        }
                        protected void paintBackground(Graphics graphics) {
                                /*graphics.setBackgroundColor(Color.BLACK);
                                graphics.clear();*/
                        }
                        protected void paint(Graphics graphics) 
                        {
                                if(isFocused) {
                                        graphics.setColor(Color.ORANGE);
                                } else {
                                        graphics.setColor(0xeeeeee);
                                }
                                //graphics.fillRect(0, 0, getWidth(), getHeight());
                                graphics.drawBitmap(0,0, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);                                        
                                graphics.drawText(textButton,(bitmap.getWidth()/2)-(getFont().getAdvance(textButton)/2),(bitmap.getHeight()/2)-(getFont().getHeight()/2));

                        }
                        protected void drawFocus(Graphics graphics, boolean on) 
                        {

                        }
                        public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
                        public int getPreferredHeight() {return  bitmap.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
                        public int getPreferredWidth() {return bitmap.getWidth();}//getFont().getAdvance("Trade Now");}

                        protected void layout(int arg0, int arg1) {
                                setExtent(getPreferredWidth(), getPreferredHeight());
                        }
                };
                orderTypeChoice.setSelectedIndex(selectedindexorderTypeChoice);
                vfm.add(banner);
                //vfm.add(companyLabel);
                //vfm.add(companyLabelValue);
                vfm.add(orderNoLabel);
                vfm.add(orderNoLabelValue);
                vfm.add(exchLabel);
                vfm.add(exchLabelValue);
                vfm.add(actionLabel);
                vfm.add(actionLabelValue);
                vfm.add(qtyLabel);
                vfm.add(qty);
                vfm.add(disqtyLabel);
                vfm.add(disc_qty);
                vfm.add(orderTypeLabel);
                vfm.add(orderTypeChoice);
                vfm.add(limitLabel);
                vfm.add(limit);
                vfm.add(triggerPriceLabel);
                vfm.add(trigger_price);
                vfm.add(dp_idLabel);
                vfm.add(dp_idChoice);
                vfm.add(btn);
                add(vfm);
                bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_BSE_GL_SCREEN, AppConstants.bottomMenuCommands);

               /* UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                                ScreenInvoker.removeRemovableScreen();  
                        }
                });*/

                final TradeNowMainScreen tradeNowMainScreen = this;
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                        	ScreenInvoker.removeRemovableScreen();  
                                ScreenInvoker.pushScreen(tradeNowMainScreen, true, true); 
                        }
                });
                /*UiApplication.getUiApplication().invokeLater(new Runnable() {
                    public void run() {
                            Dialog.alert(screenString);
                    }
            });*/
        }




        public void modifyScreenForReportDPSRsell(Vector vector, int id, boolean flagMod) {
                //set Title
                if(vector.size()==0);//
                netPosVector = vector;
                TradeNowMainBean bean_dpid ;
                if(!flagMod){
                        bean_dpid = (TradeNowMainBean)vector.elementAt(0);

                        hashmodifyReport = new Hashtable();
                        if(vector.elementAt(1) instanceof Hashtable)
                        {
                                hashmodifyReport = (Hashtable)vector.elementAt(1);
                        }
                }
                else
                {
                        hashmodifyReport = (Hashtable)vector.elementAt((vector.size()-1));
                        bean_dpid = new TradeNowMainBean();
                        bean_dpid.setDpId((String)hashmodifyReport.get("dpId"));
                }
                limitLabel = new CustomLabelField("Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                limitLabel.setColor(0xc0c0c0);
                if(limit == null){
                        limit = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                        limit.setChangeListener(this);
                        limit.setText((String)hashmodifyReport.get("limitPrice"));
                        limit.setMaxSize(8);
                        limit.setEditable(false);
                        limit.setColor(0xc0c0c0);
                        //limit.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        limit.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }
                final Bitmap bmp = ImageManager.getTradeNow();
                if(((String)hashmodifyReport.get("exchange")).equalsIgnoreCase("BSE"))
                	bann.setExchange(true);
                else
                	bann.setExchange(false);
                banner = new TradeScreenBanner(FIELD_HCENTER, bann.getSymbol(),bann.isExchange());
                banner.setData(bann.getLastTradedPrice(), bann.getChange(), (bann.getPercentageDiff().indexOf("-") == -1 ? "+"+bann.getPercentageDiff() : bann.getPercentageDiff()));

                final int banh = banner.getPreferredHeight()+8;

                TitleBar titleBar = new TitleBar("Place Order"); 
                setTitle(titleBar);
                final VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
                        public void paintBackground(Graphics graphics)
                        {
                                graphics.setBackgroundColor(Color.BLACK);
                                graphics.clear();
                                graphics.setColor(0x333333);
                                graphics.fillRoundRect(5, banh, AppConstants.screenWidth - 10,  (((getField(10).getHeight()+4)*8)),8,8);
                                int rowHeight = getField(10).getHeight()+4;
                                graphics.setColor(0xeeeeee);
                                graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight/2)-(getField(2).getHeight()/2))+banh)+(getField(2).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh)+(getField(4).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh)+(getField(6).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh)+(getField(8).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh)+(getField(10).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh)+(getField(12).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh)+(getField(14).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh)+(getField(16).getHeight()/2)-(graphics.getFont().getHeight()/2));

                        }             
                        protected void sublayout( int maxWidth, int maxHeight )
                        {
                                //super.sublayout(AppConstants.screenWidth, AppConstants.screenHeight);
                                layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
                                layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
                                layoutChild(getField(2), (AppConstants.screenWidth/2)-15, getField(2).getPreferredHeight());
                                layoutChild(getField(3), AppConstants.screenWidth/2, getField(3).getPreferredHeight());
                                layoutChild(getField(4), AppConstants.screenWidth/2, getField(4).getPreferredHeight());

                                layoutChild(getField(5), AppConstants.screenWidth/2, getField(5).getPreferredHeight());
                                layoutChild(getField(6), (AppConstants.screenWidth/2)-20, getField(6).getPreferredHeight());
                                layoutChild(getField(7), AppConstants.screenWidth/2, getField(7).getPreferredHeight());
                                layoutChild(getField(8), (AppConstants.screenWidth/2)-20, getField(8).getPreferredHeight());

                                layoutChild(getField(9), AppConstants.screenWidth/2, getField(9).getPreferredHeight());
                                layoutChild(getField(10), AppConstants.screenWidth/2, getField(10).getPreferredHeight());

                                layoutChild(getField(11), AppConstants.screenWidth/2, getField(11).getPreferredHeight());
                                layoutChild(getField(12), (AppConstants.screenWidth/2)-20, getField(12).getPreferredHeight());


                                layoutChild(getField(13), AppConstants.screenWidth/2, getField(13).getPreferredHeight());
                                layoutChild(getField(14), (AppConstants.screenWidth/2)-20, getField(14).getPreferredHeight());

                                layoutChild(getField(15), AppConstants.screenWidth/2, getField(15).getPreferredHeight());
                                layoutChild(getField(16), AppConstants.screenWidth/2, getField(16).getPreferredHeight());

                                layoutChild(getField(17), getField(17).getPreferredWidth(), getField(17).getPreferredHeight());

                                int rowHeight = getField(10).getHeight()+4;
                                setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getPreferredWidth()/2), 0);
                                setPositionChild(getField(1), 15, ((rowHeight/2)-(getField(1).getHeight()/2))+banh);
                                setPositionChild(getField(2), AppConstants.screenWidth/2, ((rowHeight/2)-(getField(2).getHeight()/2))+banh);
                                setPositionChild(getField(3), 15, ((rowHeight)+(rowHeight/2)-(getField(3).getHeight()/2))+banh);
                                setPositionChild(getField(4), AppConstants.screenWidth/2, ((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh);
                                setPositionChild(getField(5), 15, ((rowHeight*2)+(rowHeight/2)-(getField(5).getHeight()/2))+banh);
                                setPositionChild(getField(6), AppConstants.screenWidth/2, ((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh);
                                setPositionChild(getField(7), 15,((rowHeight*3)+(rowHeight/2)-(getField(7).getHeight()/2))+banh);
                                setPositionChild(getField(8), AppConstants.screenWidth/2, ((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh);
                                setPositionChild(getField(9), 15, ((rowHeight*4)+(rowHeight/2)-(getField(9).getHeight()/2))+banh);
                                setPositionChild(getField(10), AppConstants.screenWidth/2, ((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh);
                                setPositionChild(getField(11), 15, ((rowHeight*5)+(rowHeight/2)-(getField(11).getHeight()/2))+banh);
                                setPositionChild(getField(12), AppConstants.screenWidth/2, ((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh);
                                setPositionChild(getField(13), 15, ((rowHeight*6)+(rowHeight/2)-(getField(13).getHeight()/2))+banh);
                                setPositionChild(getField(14), AppConstants.screenWidth/2, ((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh);
                                setPositionChild(getField(15), 15, ((rowHeight*7)+(rowHeight/2)-(getField(15).getHeight()/2))+banh);
                                setPositionChild(getField(16), AppConstants.screenWidth/2, ((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh);
                                setPositionChild(getField(17), (AppConstants.screenWidth/2)-(getField(17).getPreferredWidth()/2), 4+(rowHeight*8)+banh);

                                setExtent(AppConstants.screenWidth,((38+(rowHeight*9)+banh+getField(17).getPreferredHeight())<AppConstants.screenHeight?AppConstants.screenHeight:((38+(rowHeight*9)+banh+getField(17).getPreferredHeight())))-TitleBar.getItemHeight());
                        }
                };


                CustomLabelField exchLabel = new CustomLabelField("Exchange", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                final CustomLabelField exchLabelValue = new CustomLabelField((String)hashmodifyReport.get("exchange"), 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                String str = "";
                CustomLabelField actionLabel = new CustomLabelField("Action", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                final CustomLabelField actionLabelValue = new CustomLabelField("Sell", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                CustomLabelField qtyLabel = new CustomLabelField("Qty", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(qty == null)
                {
                        qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);
                        //qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        qty.setMaxSize(8);
                        qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                        qty.setText((String)hashmodifyReport.get("qty"));
                }
                CustomLabelField disqtyLabel = new CustomLabelField("Disc.Qty", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(disc_qty == null)
                {
                        disc_qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE| CustomBasicEditField.FILTER_INTEGER);
                        disc_qty.setMaxSize(8);
                        //disc_qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        disc_qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                        disc_qty.setText((String)hashmodifyReport.get("disc_qty"));
                }

                orderTypeLabel = new CustomLabelField("Order Type", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(orderTypeChoice == null)
                        orderTypeChoice = new CustomObjectChoiceFieldReg("", orderTypeChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        protected void fieldChangeNotify(int context)
                        {
                                // System.out.println("Context "+context);
                                //System.out.println("getSelectedIndex() "+getSelectedIndex());
                                //Do what you want here
                                //getSelectedIndex()
                                if(getSelectedIndex() == 0)
                                {
                                        limitLabel.setColor(0xc0c0c0);
                                        limit.setColor(0xc0c0c0);
                                        limit.setText("0");
                                        limit.setEditable(false);
                                        //invalidateLayout();
                                }
                                else
                                {
                                        limitLabel.setColor(0xeeeeee);
                                        limit.setEditable(true);
                                        limit.setColor(0xeeeeee);

                                        //invalidateLayout();
                                }
                                vfm.invalidate();
                                //orderTypeLabel.setColor(0xeeeeee);//setVisualState(LabelField.VISUAL_STATE_ACTIVE);
                        }
                };
                int selectedindexorderTypeChoice = 0;
                str = (String)hashmodifyReport.get("orderType") ;
                LOG.print((String)hashmodifyReport.get("orderType")+"modify report orderType -->"+str);
                boolean flag = false;
                if(str == null)
                        flag = true;
                else if(str.length()==0)
                        flag = true;
                if(flag)
                {
                        str = (String)hashmodifyReport.get("limitPrice") ;
                        try
                        {
                                double dbl = Double.parseDouble((str));
                                if(dbl>0)
                                {       orderTypeChoice.setSelectedIndex(1);
                                selectedindexorderTypeChoice = 1;}
                                else
                                {    orderTypeChoice.setSelectedIndex(0);
                                selectedindexorderTypeChoice = 0;
                                }
                        }
                        catch(Exception e)
                        {
                                orderTypeChoice.setSelectedIndex(0);
                                selectedindexorderTypeChoice = 0;
                        }
                }
                else
                {
                        if(str.equalsIgnoreCase("market"))
                        {      orderTypeChoice.setSelectedIndex(0);
                        selectedindexorderTypeChoice = 0;}
                        else
                        {     orderTypeChoice.setSelectedIndex(1);
                        selectedindexorderTypeChoice = 1;}
                }

                orderTypeChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);


                CustomLabelField triggerPriceLabel = new CustomLabelField("Trigger Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(trigger_price == null)
                {
                        trigger_price = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                        trigger_price.setChangeListener(this);
                        trigger_price.setText((String)hashmodifyReport.get("stopPrice"));
                        trigger_price.setMaxSize(8);
                        //trigger_price.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        trigger_price.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }

                CustomLabelField dp_idLabel = new CustomLabelField("DP ID", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(bean_dpid.getDpId().size()!=0)
                {
                        String[] strx = new String[bean_dpid.getDpId().size()];
                        LOG.print( "dpid length --============>"+strx.length);
                        for(int i=0;i<bean_dpid.getDpId().size();i++)
                        {
                                strx[i]=(String)bean_dpid.getDpId().elementAt(i);
                                LOG.print( strx[i]);
                        }
                        dp_idChoiceText = strx;
                        //dp_idChoice.setChoices(strx);
                        //CustomLabelField dp_idLabel = new CustomLabelField("DP ID", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                        dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                        {
                                protected void fieldChangeNotify(int context)
                                {
                                        vfm.invalidate();
                                }
                        };
                        dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                }
                else
                        LOG.print( "dpid length --============>"+bean_dpid.getDpId().size());
                //final String textButton = "Modify";
                final String textButton = "Trade Now";
                final Bitmap bitmap = ImageManager.resizeBitmap(bmp, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance(textButton)+10, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()+16) ;

                BitmapField btn = new BitmapField(bitmap,  FOCUSABLE | DrawStyle.HCENTER)  {

                        protected boolean navigationClick(int status,int time) {
                                //if(isValid())
                                {
                                        if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                        submitModifyDPSR("DPSR", exchLabelValue.getText());
                                        }  
                                }
                                return super.navigationClick(status, time);
                        }

                        protected boolean touchEvent(TouchEvent message)
                        { setFocus();
                        if(message.getEvent() == TouchEvent.CLICK) {
                                if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                submitModifyDPSR("DPSR", exchLabelValue.getText());
                                }
                        }
                        return super.touchEvent(message);
                        }
                        private boolean isFocused = false;
                        protected void onFocus(int direction) 
                        {
                                isFocused = true;
                                invalidate();
                        }
                        protected void onUnfocus() {
                                isFocused = false;
                                invalidate();
                        }
                        protected void paintBackground(Graphics graphics) {
                                /*graphics.setBackgroundColor(Color.BLACK);
                                graphics.clear();*/
                        }
                        protected void paint(Graphics graphics) 
                        {
                                if(isFocused) {
                                        graphics.setColor(Color.ORANGE);
                                } else {
                                        graphics.setColor(0xeeeeee);
                                }
                                //graphics.fillRect(0, 0, getWidth(), getHeight());
                                graphics.drawBitmap(0,0, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);                                        
                                graphics.drawText(textButton,(bitmap.getWidth()/2)-(getFont().getAdvance(textButton)/2),(bitmap.getHeight()/2)-(getFont().getHeight()/2));

                        }
                        protected void drawFocus(Graphics graphics, boolean on) 
                        {

                        }
                        public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
                        public int getPreferredHeight() {return  bitmap.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
                        public int getPreferredWidth() {return bitmap.getWidth();}//getFont().getAdvance("Trade Now");}

                        protected void layout(int arg0, int arg1) {
                                setExtent(getPreferredWidth(), getPreferredHeight());
                        }
                };
                orderTypeChoice.setSelectedIndex(1);//selectedindexorderTypeChoice);
                vfm.add(banner);
                vfm.add(exchLabel);
                vfm.add(exchLabelValue);
                vfm.add(actionLabel);
                vfm.add(actionLabelValue);
                vfm.add(qtyLabel);
                vfm.add(qty);
                vfm.add(disqtyLabel);
                vfm.add(disc_qty);
                vfm.add(orderTypeLabel);
                vfm.add(orderTypeChoice);
                vfm.add(limitLabel);
                vfm.add(limit);
                vfm.add(triggerPriceLabel);
                vfm.add(trigger_price);
                vfm.add(dp_idLabel);
                vfm.add(dp_idChoice);
                vfm.add(btn);
                add(vfm);
                bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomTradeMenuImages(true),ImageManager.getBottomTradeMenuImages(false), ActionCommand.CMD_NONE, AppConstants.bottomTradeMenuCommands);

               /* UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                                ScreenInvoker.removeRemovableScreen();  
                        }
                });*/
                final TradeNowMainScreen tradeNowMainScreen = this;
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                        	    ScreenInvoker.pushScreen(tradeNowMainScreen, true, true); 
                        	    ScreenInvoker.removeRemovableScreen();  
                                
                        }
                });
        }

        public void modifyScreenForNetPosition(final String screenString, Vector vector, int id, boolean flagMod) {
                //set Title
                if(vector.size()==0);//
                netPosVector = vector;
                TradeNowMainBean bean_dpid ;
                if(!flagMod){
                        bean_dpid = (TradeNowMainBean)vector.elementAt(0);

                        hashmodifyReport = new Hashtable();
                        if(vector.elementAt(1) instanceof Hashtable)
                        {
                                hashmodifyReport = (Hashtable)vector.elementAt(1);
                        }
                }
                else
                {
                        hashmodifyReport = (Hashtable)vector.elementAt((vector.size()-1));
                        bean_dpid = new TradeNowMainBean();
                        bean_dpid.setDpId((String)hashmodifyReport.get("dpId"));
                }
                final Bitmap bmp = ImageManager.getTradeNow();
                if(((String)hashmodifyReport.get("exchange")).equalsIgnoreCase("BSE"))
                	bann.setExchange(true);
                else
                	bann.setExchange(false);
                banner = new TradeScreenBanner(FIELD_HCENTER, bann.getSymbol(),bann.isExchange());
                banner.setData(bann.getLastTradedPrice(), bann.getChange(), (bann.getPercentageDiff().indexOf("-") == -1 ? "+"+bann.getPercentageDiff() : bann.getPercentageDiff()));

                final int banh = banner.getPreferredHeight()+8;

                TitleBar titleBar = new TitleBar("Place Order"); 
                setTitle(titleBar);
                final VerticalFieldManager vfm = new VerticalFieldManager(Manager.VERTICAL_SCROLL | Manager.VERTICAL_SCROLLBAR) {
                        public void paintBackground(Graphics graphics)
                        {
                                graphics.setBackgroundColor(Color.BLACK);
                                graphics.clear();
                                graphics.setColor(0x333333);
                                graphics.fillRoundRect(5, banh, AppConstants.screenWidth - 10,  (((getField(10).getHeight()+4)*8)),8,8);
                                int rowHeight = getField(10).getHeight()+4;
                                graphics.setColor(0xeeeeee);
                                graphics.setFont(FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight/2)-(getField(2).getHeight()/2))+banh)+(getField(2).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh)+(getField(4).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh)+(getField(6).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh)+(getField(8).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh)+(getField(10).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh)+(getField(12).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh)+(getField(14).getHeight()/2)-(graphics.getFont().getHeight()/2));
                                graphics.drawText(":", (AppConstants.screenWidth/2)-10, (((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh)+(getField(16).getHeight()/2)-(graphics.getFont().getHeight()/2));

                        }            
                        protected void sublayout( int maxWidth, int maxHeight )
                        {
                                layoutChild(getField(0), getField(0).getPreferredWidth(), getField(0).getPreferredHeight());
                                layoutChild(getField(1), AppConstants.screenWidth/2, getField(1).getPreferredHeight());
                                layoutChild(getField(2), AppConstants.screenWidth/2, getField(2).getPreferredHeight());
                                layoutChild(getField(3), AppConstants.screenWidth/2, getField(3).getPreferredHeight());
                                layoutChild(getField(4), AppConstants.screenWidth/2, getField(4).getPreferredHeight());
                                layoutChild(getField(5), AppConstants.screenWidth/2, getField(5).getPreferredHeight());

                                layoutChild(getField(6), (AppConstants.screenWidth/2)-20, getField(6).getPreferredHeight());
                                layoutChild(getField(7), (AppConstants.screenWidth/2), getField(7).getPreferredHeight());
                                layoutChild(getField(8),( AppConstants.screenWidth/2)-20, getField(8).getPreferredHeight());
                                layoutChild(getField(9), (AppConstants.screenWidth/2), getField(9).getPreferredHeight());

                                layoutChild(getField(10), AppConstants.screenWidth/2, getField(10).getPreferredHeight());
                                layoutChild(getField(11), AppConstants.screenWidth/2, getField(11).getPreferredHeight());

                                layoutChild(getField(12), (AppConstants.screenWidth/2)-20, getField(12).getPreferredHeight());
                                layoutChild(getField(13), (AppConstants.screenWidth/2), getField(13).getPreferredHeight());


                                layoutChild(getField(14), (AppConstants.screenWidth/2)-20, getField(14).getPreferredHeight());
                                layoutChild(getField(15), (AppConstants.screenWidth/2), getField(15).getPreferredHeight());
                                layoutChild(getField(16), AppConstants.screenWidth/2, getField(16).getPreferredHeight());
                                layoutChild(getField(17), getField(17).getPreferredWidth(), getField(17).getPreferredHeight());

                                int rowHeight = getField(10).getHeight()+4;
                                setPositionChild(getField(0), (AppConstants.screenWidth/2)-(getField(0).getPreferredWidth()/2), 0);
                                setPositionChild(getField(1), 15, ((rowHeight/2)-(getField(1).getHeight()/2))+banh);
                                setPositionChild(getField(2), AppConstants.screenWidth/2, ((rowHeight/2)-(getField(2).getHeight()/2))+banh);
                                setPositionChild(getField(3), 15, ((rowHeight)+(rowHeight/2)-(getField(3).getHeight()/2))+banh);
                                setPositionChild(getField(4), AppConstants.screenWidth/2, ((rowHeight)+(rowHeight/2)-(getField(4).getHeight()/2))+banh);
                                setPositionChild(getField(5), 15, ((rowHeight*2)+(rowHeight/2)-(getField(5).getHeight()/2))+banh);
                                setPositionChild(getField(6), AppConstants.screenWidth/2, ((rowHeight*2)+(rowHeight/2)-(getField(6).getHeight()/2))+banh);
                                setPositionChild(getField(7), 15,((rowHeight*3)+(rowHeight/2)-(getField(7).getHeight()/2))+banh);
                                setPositionChild(getField(8), AppConstants.screenWidth/2, ((rowHeight*3)+(rowHeight/2)-(getField(8).getHeight()/2))+banh);
                                setPositionChild(getField(9), 15, ((rowHeight*4)+(rowHeight/2)-(getField(9).getHeight()/2))+banh);
                                setPositionChild(getField(10), AppConstants.screenWidth/2, ((rowHeight*4)+(rowHeight/2)-(getField(10).getHeight()/2))+banh);
                                setPositionChild(getField(11), 15, ((rowHeight*5)+(rowHeight/2)-(getField(11).getHeight()/2))+banh);
                                setPositionChild(getField(12), AppConstants.screenWidth/2, ((rowHeight*5)+(rowHeight/2)-(getField(12).getHeight()/2))+banh);
                                setPositionChild(getField(13), 15, ((rowHeight*6)+(rowHeight/2)-(getField(13).getHeight()/2))+banh);
                                setPositionChild(getField(14), AppConstants.screenWidth/2, ((rowHeight*6)+(rowHeight/2)-(getField(14).getHeight()/2))+banh);
                                setPositionChild(getField(15), 15, ((rowHeight*7)+(rowHeight/2)-(getField(15).getHeight()/2))+banh);
                                setPositionChild(getField(16), AppConstants.screenWidth/2, ((rowHeight*7)+(rowHeight/2)-(getField(16).getHeight()/2))+banh);
                                setPositionChild(getField(17), (AppConstants.screenWidth/2)-(getField(17).getPreferredWidth()/2), 4+(rowHeight*8)+banh);

                                setExtent(AppConstants.screenWidth,((38+(rowHeight*9)+banh+getField(17).getPreferredHeight())<AppConstants.screenHeight?AppConstants.screenHeight:((38+(rowHeight*9)+banh+getField(17).getPreferredHeight())))-TitleBar.getItemHeight());

                        }
                };



                CustomLabelField exchLabel = new CustomLabelField("Exchange", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                final CustomLabelField exchLabelValue = new CustomLabelField((String)hashmodifyReport.get("exchange"), 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));

                CustomLabelField actionLabel = new CustomLabelField("Action", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(actionChoice == null)
                        actionChoice = new CustomObjectChoiceFieldReg("", actionChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        protected void fieldChangeNotify(int context)
                        {
                                vfm.invalidate();
                        }
                };



                String str = /*action;*/(String)hashmodifyReport.get("action");
                LOG.print("action - - - - - - - - - "+ str);
                //if(action.length()>0)
                {
                        for(int i=0;i<actionChoiceTextShort.length;i++)
                        {
                                if(str.equalsIgnoreCase(actionChoiceTextShort[i]))
                                {
                                        str = actionChoiceText[i];
                                        actionChoice.setSelectedIndex(i);
                                        break;
                                }
                        }
                }
                actionChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_DISABLED);
                final CustomLabelField actionLabelValue = new CustomLabelField(str, 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));


                CustomLabelField qtyLabel = new CustomLabelField("Qty", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                //if(qty == null)
                {
                        qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);
                        //qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        qty.setMaxSize(8);
                        qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                        qty.setText((String)hashmodifyReport.get("qty"));
                        LOG.print("(String)hashmodifyReport.get(\"qty\") : "+(String)hashmodifyReport.get("qty"));
                }

                CustomLabelField disqtyLabel = new CustomLabelField("Disc.Qty", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(disc_qty == null)
                {
                        disc_qty = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE | CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_INTEGER);
                        disc_qty.setMaxSize(8);
                        //disc_qty.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        disc_qty.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                        if((String)hashmodifyReport.get("disc_qty")==null)
                                disc_qty.setText("0");
                        else
                                disc_qty.setText((String)hashmodifyReport.get("disc_qty"));
                }
                limitLabel = new CustomLabelField("Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                limitLabel.setColor(0xc0c0c0);
                if(limit == null){
                        limit = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE| CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                        limit.setChangeListener(this);
                        limit.setText((String)hashmodifyReport.get("limitPrice"));
                        limit.setMaxSize(8);
                        limit.setEditable(false);
                        limit.setColor(0xc0c0c0);
                        //limit.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        limit.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }
                orderTypeLabel = new CustomLabelField("Order Type", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(orderTypeChoice == null)
                        orderTypeChoice = new CustomObjectChoiceFieldReg("", orderTypeChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                {
                        protected void fieldChangeNotify(int context)
                        {
                                // System.out.println("Context "+context);
                                //System.out.println("getSelectedIndex() "+getSelectedIndex());
                                if(getSelectedIndex() == 0)
                                {
                                        limitLabel.setColor(0xc0c0c0);
                                        limit.setColor(0xc0c0c0);
                                        limit.setText("0");
                                        limit.setEditable(false);
                                }
                                else
                                {
                                        limitLabel.setColor(0xeeeeee);
                                        limit.setEditable(true);
                                        limit.setColor(0xeeeeee);
                                }
                                vfm.invalidate();
                        }
                };
                int selectedindexorderTypeChoice = 0;
                str = (String)hashmodifyReport.get("orderType") ;
                boolean flag = false;
                if(str == null)
                        flag = true;
                else if(str.length()==0)
                        flag = true;
                if(flag)
                {
                        str = (String)hashmodifyReport.get("limitPrice") ;
                        try
                        {
                                double dbl = Double.parseDouble((str));
                                if(dbl>0)
                                {      orderTypeChoice.setSelectedIndex(1);
                                selectedindexorderTypeChoice = 1;}
                                else
                                {        orderTypeChoice.setSelectedIndex(0);
                                selectedindexorderTypeChoice = 0;}
                        }
                        catch(Exception e)
                        {
                                orderTypeChoice.setSelectedIndex(0);
                                selectedindexorderTypeChoice = 0;
                        }
                }
                else
                {
                        if(str.equalsIgnoreCase("market"))
                        {     orderTypeChoice.setSelectedIndex(0);
                        selectedindexorderTypeChoice = 0;}
                        else
                        {orderTypeChoice.setSelectedIndex(1);
                        selectedindexorderTypeChoice = 1;}
                }

                orderTypeChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);


                CustomLabelField triggerPriceLabel = new CustomLabelField("Trigger Price", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(trigger_price == null)
                {
                        trigger_price = new CustomBasicEditField(Color.BLACK | CustomBasicEditField.NO_NEWLINE| CustomBasicEditField.EDITABLE | CustomBasicEditField.FILTER_REAL_NUMERIC);
                        trigger_price.setChangeListener(this);
                        trigger_price.setText((String)hashmodifyReport.get("stopPrice"));
                        trigger_price.setMaxSize(8);
                        //trigger_price.setFilter(TextFilter.get(TextFilter.DEFAULT));
                        trigger_price.setFont(FontLoader.getFont(AppConstants.BIG_BOLD_FONT));
                }

                CustomLabelField dp_idLabel = new CustomLabelField("DP ID", 0, 0xeeeeee, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT));
                if(bean_dpid.getDpId().size()!=0)
                {
                        String[] strx = new String[bean_dpid.getDpId().size()];

                        for(int i=0;i<bean_dpid.getDpId().size();i++)
                        {
                                strx[i]=(String)bean_dpid.getDpId().elementAt(i);
                        }
                        dp_idChoiceText = strx;
                        dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                        {
                                protected void fieldChangeNotify(int context)
                                {
                                        vfm.invalidate();
                                }
                        };
                        dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_ACTIVE);
                }
                else
                {
                        dp_idChoiceText = new String[1];
                        dp_idChoiceText[0] = "0";
                        dp_idChoice = new CustomObjectChoiceFieldReg("", dp_idChoiceText,0, ObjectChoiceField.FORCE_SINGLE_LINE | ObjectChoiceField.NON_FOCUSABLE | ObjectChoiceField.USE_ALL_WIDTH | FIELD_LEFT)
                        {
                                protected void fieldChangeNotify(int context)
                                {
                                        vfm.invalidate();
                                }
                        };
                        dp_idChoice.setVisualState(ObjectChoiceField.VISUAL_STATE_DISABLED);
                }
                final String textButton = "Trade Now";
                final Bitmap bitmap = ImageManager.resizeBitmap(bmp, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getAdvance(textButton)+10, FontLoader.getFont(AppConstants.BIG_PLAIN_FONT).getHeight()+16) ;

                BitmapField btn = new BitmapField(bitmap,  FOCUSABLE | DrawStyle.HCENTER)  {

                        protected boolean navigationClick(int status,int time) {
                                //if(isValid())
                                {
                                        if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                        submitModifyNetPosition(screenString, exchLabelValue.getText(), actionLabelValue.getText(), (String)hashmodifyReport.get("PRODUCTTYPE"), (String)hashmodifyReport.get("NETQTY"));
                                        }  
                                }
                                return super.navigationClick(status, time);
                        }

                        protected boolean touchEvent(TouchEvent message)
                        { setFocus();
                        if(message.getEvent() == TouchEvent.CLICK) {
                                if((timer+100)<System.currentTimeMillis()){ timer = System.currentTimeMillis();
                                submitModifyNetPosition(screenString, exchLabelValue.getText(), actionLabelValue.getText(), (String)hashmodifyReport.get("PRODUCTTYPE"), (String)hashmodifyReport.get("NETQTY"));
                                }
                        }
                        return super.touchEvent(message);
                        }
                        private boolean isFocused = false;
                        protected void onFocus(int direction) 
                        {
                                isFocused = true;
                                invalidate();
                        }
                        protected void onUnfocus() {
                                isFocused = false;
                                invalidate();
                        }
                        protected void paintBackground(Graphics graphics) {
                                /*graphics.setBackgroundColor(Color.BLACK);
                                graphics.clear();*/
                        }
                        protected void paint(Graphics graphics) 
                        {
                                if(isFocused) {
                                        graphics.setColor(Color.ORANGE);
                                } else {
                                        graphics.setColor(0xeeeeee);
                                }
                                //graphics.fillRect(0, 0, getWidth(), getHeight());
                                graphics.drawBitmap(0,0, bitmap.getWidth(), bitmap.getHeight(), bitmap, 0, 0);                                        
                                graphics.drawText(textButton,(bitmap.getWidth()/2)-(getFont().getAdvance(textButton)/2),(bitmap.getHeight()/2)-(getFont().getHeight()/2));

                        }
                        protected void drawFocus(Graphics graphics, boolean on) 
                        {

                        }
                        public Font getFont() { return FontLoader.getFont(AppConstants.BIG_PLAIN_FONT);}
                        public int getPreferredHeight() {return  bitmap.getHeight();}//(getFont().getHeight()*2)-(getFont().getHeight()/2);}
                        public int getPreferredWidth() {return bitmap.getWidth();}//getFont().getAdvance("Trade Now");}

                        protected void layout(int arg0, int arg1) {
                                setExtent(getPreferredWidth(), getPreferredHeight());
                        }
                };
                orderTypeChoice.setSelectedIndex(1);//selectedindexorderTypeChoice);
                vfm.add(banner);
                vfm.add(exchLabel);
                vfm.add(exchLabelValue);
                vfm.add(actionLabel);
                vfm.add(actionLabelValue);
                vfm.add(qtyLabel);
                vfm.add(qty);
                vfm.add(disqtyLabel);
                vfm.add(disc_qty);
                vfm.add(orderTypeLabel);
                vfm.add(orderTypeChoice);
                vfm.add(limitLabel);
                vfm.add(limit);
                vfm.add(triggerPriceLabel);
                vfm.add(trigger_price);
                vfm.add(dp_idLabel);
                vfm.add(dp_idChoice);
                vfm.add(btn);
                add(vfm);
                bottomMenu = BottomMenu.getBottomMenuInstance(this,ImageManager.getBottomMenuImages(true),ImageManager.getBottomMenuImages(false), ActionCommand.CMD_BSE_GL_SCREEN, AppConstants.bottomMenuCommands);

              /*  UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                                ScreenInvoker.removeRemovableScreen();  
                        }
                });*/

                final TradeNowMainScreen tradeNowMainScreen = this;
                UiApplication.getUiApplication().invokeLater(new Runnable() {
                        public void run() {
                        	ScreenInvoker.removeRemovableScreen();  
                                ScreenInvoker.pushScreen(tradeNowMainScreen, true, true); 
                        }
                });
        }

        public void submitModifyReport(String screenString, String exchLabelValue, String actionLabelValue, int actionID, boolean PARTLY_EXEC_FLAG)
        {
                double qtyvalue = 0;
                double discqtyvalue = 0;
                if(qty.getText().length()==0)
                {
                        Dialog.alert("Please enter Qty value");
                        return ;
                }
                else if(qty.getText().length()>0)
                {
                        qtyvalue = Double.parseDouble(qty.getText());
                        if(qtyvalue==0 || qtyvalue<0)
                        {
                                Dialog.alert("Order quantity cannot be zero or negative.");
                                return ;
                        }

                }

                if(disc_qty.getText().length()==0)
                {

                        //Dialog.alert("Please enter Disclosed value");
                        //return;
                }
                else if(disc_qty.getText().length()>0)
                {
                        discqtyvalue = Double.parseDouble(disc_qty.getText());
                        if(discqtyvalue<0)
                        {
                                Dialog.alert("Disclosed quantity cannot be negative.");
                                disc_qty.setFocus();
                                return ;
                        }
                        else if(qtyvalue==discqtyvalue);
                        else if(qtyvalue<=discqtyvalue)
                        {
                                Dialog.alert("Disclosed Quantity Should be Less Than Order Quantity");
                                disc_qty.setFocus();
                                return ;
                        }
                        else if(discqtyvalue<(qtyvalue/10) && discqtyvalue!=0)
                        {
                                //System.out.println("discqtyvalue :"+discqtyvalue);
                                //System.out.println("qtyvalue :"+qtyvalue);
                                Dialog.alert("Disclosed Quantity must be at least 10% of Order Quantity");
                                disc_qty.setFocus();
                                return ;
                        }

                }

                //For Limit
                if(orderTypeChoice.getSelectedIndex()==1){
                        if(limit.getText().length()==0)
                        {
                                Dialog.alert("Please enter Order Price value");
                                limit.setFocus();
                                return ;
                        }
                        else
                        {
                                double limitPrice = Double.parseDouble(limit.getText());
                                if(limitPrice == 0)
                                {
                                        Dialog.alert("Please enter Order Price value");
                                        limit.setFocus();
                                        return ;
                                }
                                else if(limitPrice < 0)
                                {
                                        Dialog.alert("Order Price cannot be negative.");
                                        limit.setFocus();
                                        return ;
                                }
                                else if(limit.getText().indexOf(".")>-1)
                                {
                                        if(limit.getText().indexOf(".")<(limit.getText().length()-3))
                                        {
                                                Dialog.alert("Only two decimals are allowed for Order Price");
                                                limit.setFocus();
                                                return ;
                                        }
                                }
                        }
                        
                        if(actionID==0 || actionID==3 ) //Buy Max
                        {
                                if(trigger_price.getText().length()>0 && trigger_price.getText().length()>0)
                                {
                                        double limitPrice = Double.parseDouble(limit.getText());

                                        double triggerPrice = Double.parseDouble(trigger_price.getText());
                                        if(triggerPrice < 0)
                                        {
                                                Dialog.alert("Trigger Price cannot be negative.");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                        else if(limitPrice < triggerPrice)
                                        {
                                                Dialog.alert("Trigger Price should be less than Order Price");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                }
                        }

                        if(actionID==1 || actionID==2 || actionID==4) //Sell Max
                        {
                                if(limit.getText().length()>0 && trigger_price.getText().length()>0)
                                {
                                        double limitPrice = 0;
                                        double triggerPrice = 0;
                                        try{
                                                limitPrice = Double.parseDouble(limit.getText());

                                                triggerPrice = Double.parseDouble(trigger_price.getText());
                                        }
                                        catch(Exception e){

                                        } if(triggerPrice < 0)
                                        {
                                                Dialog.alert("Trigger Price cannot be negative.");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                        else if(limitPrice > triggerPrice)
                                        {
                                                if(actionChoice!=null)
                                                {
                                                    if(actionChoice.getSelectedIndex()==1 || actionChoice.getSelectedIndex()==4)
                                                {
                                                        if(triggerPrice != 0) 
                                                        {
                                                                Dialog.alert("Trigger Price should be greater than Order Price");
                                                                trigger_price.setFocus();
                                                                return;
                                                        }

                                                }
                                                else
                                                {
                                                        Dialog.alert("Trigger Price should be greater than Order Price");
                                                        trigger_price.setFocus();
                                                        return;
                                                }
                                            }
                                                else
                                                {
                                                	if(actionLabelValue.equalsIgnoreCase("Sell"))
                                                    {
                                                            if(triggerPrice != 0) 
                                                            {
                                                                    Dialog.alert("Trigger Price should be greater than Order Price");
                                                                    trigger_price.setFocus();
                                                                    return;
                                                            }

                                                    }
                                                }
                                        }
                                }
                        }
                }
                if(trigger_price.getText().length()==0)trigger_price.setText("0");
                else if(trigger_price.getText().indexOf("-")>-1)
                {
                        Dialog.alert("Trigger Price cannot be negative.");
                        trigger_price.setFocus();
                        return ;
                }
                String partly_exe_qty="order_exe_qty=";
                LOG.print("MODIFY SCREEN PARTLY_EXEC_FLAG : "+PARTLY_EXEC_FLAG);
                if(PARTLY_EXEC_FLAG)
                	partly_exe_qty="order_exe_qty="+qty.getText();
                if(limit.getText().length()==0)limit.setText("0");

                String textorderTypeChoice = "market";
                if(orderTypeChoice.getSelectedIndex()==1)
                        textorderTypeChoice = "limit"+"&limitPrice="+limit.getText();
                String url = "";
                
               // if(screenString.equalsIgnoreCase("ReportsOrdere"))// || screenString.equalsIgnoreCase("OrderBKEquity") )
                //        url = AppConstants.domainUrl + /*"http://220.226.189.186/SK_live/"+ */"modifyOrder.php?companyId="+(String)hashmodifyReport.get("company_code")+"&exchange="+exchLabelValue+"&ltp="+(String)hashmodifyReport.get("ltp")+"&per_change="+(String)hashmodifyReport.get("per_change")+"&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&orderId="+(String)hashmodifyReport.get("order_id")+"&page=EQreports&"+partly_exe_qty+"&order_exe_price=&action="+actionLabelValue+"&rmsCode="+(String)hashmodifyReport.get("rmscode")+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+textorderTypeChoice+"&stopPrice="+trigger_price.getText()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()];
                //else if(screenString.equalsIgnoreCase("OrderBKEquity"))
                 //       url = AppConstants.domainUrl + /*"http://220.226.189.186/SK_live/"+ */"modifyOrder.php?companyId="+(String)hashmodifyReport.get("company_code")+"&exchange="+exchLabelValue+"&ltp="+(String)hashmodifyReport.get("ltp")+"&per_change="+(String)hashmodifyReport.get("per_change")+"&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&orderId="+(String)hashmodifyReport.get("order_id")+"&page=orderBK&"+partly_exe_qty+"&order_exe_price=&action="+actionLabelValue+"&rmsCode="+(String)hashmodifyReport.get("rmscode")+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+textorderTypeChoice+"&stopPrice="+trigger_price.getText()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()];
              /*  String marketlimit;
                if(orderTypeChoice.getSelectedIndex()==1)
                	marketlimit = "";
                else
                	marketlimit = limit.getText();
                url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqo03&restrictBack=1&btnModify=&btnConfirm=&companyId="+(String)hashmodifyReport.get("company_code")+"&exchange="+exchLabelValue+"&ltp="+(String)hashmodifyReport.get("ltp")+"&action="+actionLabelValue+"&orderType="+textorderTypeChoice+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&stopPrice="+trigger_price.getText()+"&per_change="+bann.getChangePercent()+"&change="+bann.getChange()+"&custId="+UserInfo.getUserID()+"&orderId="+(String)hashmodifyReport.get("order_id")+"&limitPrice="+marketlimit+"&validity=GFD&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"&rmsCode="+(String)hashmodifyReport.get("rmscode")+"&page=reports&"+partly_exe_qty+"&order_exe_price=";
         */
                url = AppConstants.domainUrl + "SK_android/controller.php?RequestId=steqo03&restrictBack=1&btnModify=&btnConfirm=&companyId="+(String)hashmodifyReport.get("company_code")+"&exchange="+exchLabelValue+"&ltp="+(String)hashmodifyReport.get("ltp")+"&action="+(String)hashmodifyReport.get("action")+"&orderType="+textorderTypeChoice+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&stopPrice="+trigger_price.getText()+"&per_change="+(String)hashmodifyReport.get("per_change")+"&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&orderId="+(String)hashmodifyReport.get("order_id")+"&validity=GFD&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"&rmsCode="+(String)hashmodifyReport.get("rmscode")+"&page=reports&order_exe_qty=0&order_exe_price=0.00";
                LOG.print("Modify Url : "+url);
                Vector dataUrl = new Vector();
                dataUrl.addElement(screenString);
                dataUrl.addElement(url);
                LOG.print(url);
                SlideViewOrderCancel.orderCancel = true;
/*synchronized( UiApplication.getEventLock() ){
                            
                            if(isDisplayed()) 
                                   {
                                if(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowMainScreen)
                                        {
                                                UiApplication.getUiApplication().getActiveScreen().close();
                                        }

                                   }
                }*/
                ActionInvoker.processCommand(new Action(ActionCommand.CMD_REPORTS_ORDER_VIEW,dataUrl));

        }

        public void submitModifyDPSR(String screensString, String exchLabelValue)
        {
                double qtyvalue = 0;
                double discqtyvalue = 0;

                if(qty.getText().length()==0)
                {
                        Dialog.alert("Please enter Qty value");
                        return ;
                }
                else if(qty.getText().length()>0)
                {
                        qtyvalue = Double.parseDouble(qty.getText());
                        if(qtyvalue==0 || qtyvalue<0)
                        {
                                Dialog.alert("Order quantity cannot be zero or negative.");
                                return ;
                        }
                }

                if(disc_qty.getText().length()==0)
                {

                        //Dialog.alert("Please enter Disclosed value");
                        //return;
                }
                else if(disc_qty.getText().length()>0)
                {
                        discqtyvalue = Double.parseDouble(disc_qty.getText());
                        if(discqtyvalue<0)
                        {
                                Dialog.alert("Disclosed quantity cannot be negative.");
                                disc_qty.setFocus();
                                return ;
                        }
                        else if(qtyvalue==discqtyvalue);
                        else if(qtyvalue<=discqtyvalue)
                        {
                                Dialog.alert("Disclosed Quantity Should be Less Than Order Quantity");
                                disc_qty.setFocus();
                                return ;
                        }
                        else if(discqtyvalue<(qtyvalue/10) && discqtyvalue!=0)
                        {
                                Dialog.alert("Disclosed Quantity must be at least 10% of Order Quantity");
                                disc_qty.setFocus();
                                return ;
                        }

                }

                //For Limit
                if(orderTypeChoice.getSelectedIndex()==1){
                        if(limit.getText().length()==0)
                        {
                                Dialog.alert("Please enter Order Price value");
                                limit.setFocus();
                                return ;
                        }
                        else
                        {
                                double limitPrice = Double.parseDouble(limit.getText());
                                if(limitPrice == 0)
                                {
                                        Dialog.alert("Please enter Order Price value");
                                        limit.setFocus();
                                        return ;
                                }
                                else if(limitPrice < 0)
                                {
                                        Dialog.alert("Order Price cannot be negative.");
                                        limit.setFocus();
                                        return ;
                                }
                                else if(limit.getText().indexOf(".")>-1)
                                {
                                        if(limit.getText().indexOf(".")<(limit.getText().length()-3))
                                        {
                                                Dialog.alert("Only two decimals are allowed for Order Price");
                                                limit.setFocus();
                                                return ;
                                        }
                                }
                        }

                        {
                                if(limit.getText().length()>0 && trigger_price.getText().length()>0)
                                {
                                        double limitPrice = 0;
                                        double triggerPrice = 0;
                                        try{
                                                limitPrice = Double.parseDouble(limit.getText());

                                                triggerPrice = Double.parseDouble(trigger_price.getText());
                                        }
                                        catch(Exception e){

                                        }
                                        if(triggerPrice < 0)
                                        {
                                                Dialog.alert("Trigger Price cannot be negative.");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                        else if(limitPrice > triggerPrice)
                                        {
                                                if(actionChoice!=null)
                                                if(actionChoice.getSelectedIndex()==1 || actionChoice.getSelectedIndex()==4)
                                                {
                                                        if(triggerPrice != 0) 
                                                        {
                                                                Dialog.alert("Trigger Price should be greater than Order Price");
                                                                trigger_price.setFocus();
                                                                return;
                                                        }

                                                }
                                                else
                                                {
                                                        Dialog.alert("Trigger Price should be greater than Order Price");
                                                        trigger_price.setFocus();
                                                        return;
                                                }
                                        }
                                }
                        }
                }
                if(trigger_price.getText().length()==0)trigger_price.setText("0");
                else if(trigger_price.getText().indexOf("-")>-1)
                {
                        Dialog.alert("Trigger Price cannot be negative.");
                        trigger_price.setFocus();
                        return ;
                }
                if(limit.getText().length()==0)limit.setText("0");

                String url = "";

                String mtext = "market";
                if(orderTypeChoice.getSelectedIndex()==1)
                {
                        mtext = "limit&limitPrice="+limit.getText();
                }
                url = AppConstants.domainUrl + "placeOrder.php?companyId="+(String)hashmodifyReport.get("company_code")+"&ltp="+(String)hashmodifyReport.get("ltp")+"&per_change="+(String)hashmodifyReport.get("per_change")+"&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&page=dpsr&exchange="+exchLabelValue+"&action=S&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+mtext+"&stopPrice="+trigger_price.getText()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"";
                String urlConfirm = AppConstants.domainUrl+"placeOrder.php?companyId="+"##company_code##"+"&exchange="+exchLabelValue+"&ltp="+(String)hashmodifyReport.get("ltp")+"&action=S&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+mtext+"&stopPrice="+trigger_price.getText()+"&per_change="+(String)hashmodifyReport.get("per_change")+"&type=NEW&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"&btnConfirm=Confirm&btnModify=&page=dpsr";
                String urlModify = AppConstants.domainUrl+"placeOrder.php?companyId="+"##company_code##"+"&exchange="+exchLabelValue+"&ltp="+(String)hashmodifyReport.get("ltp")+"&action=S&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+mtext+"&stopPrice="+trigger_price.getText()+"&per_change="+(String)hashmodifyReport.get("per_change")+"&type=NEW&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"&btnConfirm=&btnModify=Modify&page=dpsr";
                ///////////////
                Vector urls = new Vector();
                urls.addElement(url);
                urls.addElement(urlConfirm);
                urls.addElement(urlModify);
                Vector second = new Vector();
                second.addElement(urls);
                second.addElement(bann);
                second.addElement("DPSR");
/*synchronized( UiApplication.getEventLock() ){
                            
                            if(isDisplayed()) 
                                   {
                                if(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowMainScreen)
                                        {
                                                UiApplication.getUiApplication().getActiveScreen().close();
                                        }

                                   }
                }*/
                ActionInvoker.processCommand(new Action(ActionCommand.CMD_TRADE_MODIFY,second));
        }


        public void submitModifyNetPosition(String screensString, String exchLabelValue, String actionLabelValue, String productType, String netQty)
        {
                double qtyvalue = 0;
                double discqtyvalue = 0;
                actionLabelValue = actionLabelValue.replace(' ', '_');
                for(int i=0;i<actionChoiceText.length;i++)
                {
                        String txt = actionChoiceText[i].replace(' ', '_');
                        if(actionLabelValue.equalsIgnoreCase(txt))
                        {
                                actionLabelValue = actionChoiceTextShort[i];
                                break;
                        }
                }
                if(qty.getText().length()==0)
                {
                        Dialog.alert("Please enter Qty value");
                        return ;
                }
                else if(qty.getText().length()>0)
                {
                        qtyvalue = Double.parseDouble(qty.getText());
                        if(qtyvalue==0 || qtyvalue<0)
                        {
                                Dialog.alert("Order quantity cannot be zero or negative.");
                                return ;
                        }

                }

                if(disc_qty.getText().length()==0)
                {

                        //Dialog.alert("Please enter Disclosed value");
                        //return;
                }
                else if(disc_qty.getText().length()>0)
                {
                        discqtyvalue = Double.parseDouble(disc_qty.getText());
                        if(discqtyvalue<0)
                        {
                                Dialog.alert("Disclosed quantity cannot be negative.");
                                disc_qty.setFocus();
                                return ;
                        }
                        else if(qtyvalue==discqtyvalue);
                        else if(qtyvalue<=discqtyvalue)
                        {
                                Dialog.alert("Disclosed Quantity Should be Less Than Order Quantity");
                                disc_qty.setFocus();
                                return ;
                        }
                        else if(discqtyvalue<(qtyvalue/10) && discqtyvalue!=0)
                        {
                                Dialog.alert("Disclosed Quantity must be at least 10% of Order Quantity");
                                disc_qty.setFocus();
                                return ;
                        }

                }

                //For Limit
                if(orderTypeChoice.getSelectedIndex()==1){
                        if(limit.getText().length()==0)
                        {
                                Dialog.alert("Please enter Order Price value");
                                limit.setFocus();
                                return ;
                        }
                        else
                        {
                                double limitPrice = Double.parseDouble(limit.getText());
                                if(limitPrice == 0)
                                {
                                        Dialog.alert("Please enter Order Price value");
                                        limit.setFocus();
                                        return ;
                                }
                                else if(limitPrice < 0)
                                {
                                        Dialog.alert("Order Price cannot be negative.");
                                        limit.setFocus();
                                        return ;
                                }
                                else if(limit.getText().indexOf(".")>-1)
                                {
                                        if(limit.getText().indexOf(".")<(limit.getText().length()-3))
                                        {
                                                Dialog.alert("Only two decimals are allowed for Order Price");
                                                limit.setFocus();
                                                return ;
                                        }
                                }
                        }
                        if(actionChoice!=null)
                        if(actionChoice.getSelectedIndex()==0 || actionChoice.getSelectedIndex()==3 ) //Buy Max
                        {
                                if(trigger_price.getText().length()>0 && trigger_price.getText().length()>0)
                                {
                                        double limitPrice = Double.parseDouble(limit.getText());

                                        double triggerPrice = Double.parseDouble(trigger_price.getText());
                                        if(triggerPrice < 0)
                                        {
                                                Dialog.alert("Trigger Price cannot be negative.");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                        else if(limitPrice < triggerPrice)
                                        {
                                                Dialog.alert("Trigger Price should be less than Order Price");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                }
                        }
                        if(actionChoice!=null)
                        if(actionChoice.getSelectedIndex()==1 || actionChoice.getSelectedIndex()==2 || actionChoice.getSelectedIndex()==4) //Sell Max
                        {
                                if(limit.getText().length()>0 && trigger_price.getText().length()>0)
                                {
                                        double limitPrice = 0;
                                        double triggerPrice = 0;
                                        try{
                                                limitPrice = Double.parseDouble(limit.getText());

                                                triggerPrice = Double.parseDouble(trigger_price.getText());
                                        }
                                        catch(Exception e){

                                        }
                                        if(triggerPrice < 0)
                                        {
                                                Dialog.alert("Trigger Price cannot be negative.");
                                                trigger_price.setFocus();
                                                return ;
                                        }
                                        else if(limitPrice > triggerPrice)
                                        {
                                                if(actionChoice.getSelectedIndex()==1 || actionChoice.getSelectedIndex()==4)
                                                {
                                                        if(triggerPrice != 0) 
                                                        {
                                                                Dialog.alert("Trigger Price should be greater than Order Price");
                                                                trigger_price.setFocus();
                                                                return;
                                                        }

                                                }
                                                else
                                                {
                                                        Dialog.alert("Trigger Price should be greater than Order Price");
                                                        trigger_price.setFocus();
                                                        return;
                                                }
                                        }
                                }
                        }
                }
                if(trigger_price.getText().length()==0)trigger_price.setText("0");
                else if(trigger_price.getText().indexOf("-")>-1)
                {
                        Dialog.alert("Trigger Price cannot be negative.");
                        trigger_price.setFocus();
                        return ;
                }
                if(limit.getText().length()==0)limit.setText("0");

                String url = "";

                String mtext = "market";
                if(orderTypeChoice.getSelectedIndex()==1)
                {
                        mtext = "limit&limitPrice="+limit.getText();
                }
                url = AppConstants.domainUrl + "placeOrder.php?companyId="+(String)hashmodifyReport.get("company_code")+"&ltp="+(String)hashmodifyReport.get("ltp")+"&per_change="+(String)hashmodifyReport.get("per_change")+"&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&page=netpos&exchange="+exchLabelValue+"&action="+actionLabelValue+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+mtext+"&stopPrice="+trigger_price.getText()+"";
                String urlConfirm = AppConstants.domainUrl+"placeOrder.php?companyId="+"##company_code##"+"&exchange="+exchLabelValue+"&ltp="+(String)hashmodifyReport.get("ltp")+"&action="+actionLabelValue+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+mtext+"&stopPrice="+trigger_price.getText()+"&per_change="+(String)hashmodifyReport.get("per_change")+"&type=NEW&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"&btnConfirm=Confirm&btnModify=&page=netpos";
                String urlModify = AppConstants.domainUrl+"placeOrder.php?companyId="+"##company_code##"+"&exchange="+exchLabelValue+"&ltp="+(String)hashmodifyReport.get("ltp")+"&action="+actionLabelValue+"&qty="+qty.getText()+"&disc-qty="+disc_qty.getText()+"&orderType="+mtext+"&stopPrice="+trigger_price.getText()+"&per_change="+(String)hashmodifyReport.get("per_change")+"&type=NEW&change="+(String)hashmodifyReport.get("change")+"&custId="+UserInfo.getUserID()+"&dpId="+dp_idChoiceText[dp_idChoice.getSelectedIndex()]+"&btnConfirm=&btnModify=Modify&page=netpos";
                ///////////////
                Vector urls = new Vector();
                urls.addElement(url);
                urls.addElement(urlConfirm);
                urls.addElement(urlModify);
                Vector second = new Vector();
                second.addElement(urls);
                second.addElement(bann);
                second.addElement("ReportNetPosition");
/*synchronized( UiApplication.getEventLock() ){
                            
                            if(isDisplayed()) 
                                   {
                                if(UiApplication.getUiApplication().getActiveScreen() instanceof TradeNowMainScreen)
                                        {
                                                UiApplication.getUiApplication().getActiveScreen().close();
                                        }

                                   }
                }*/
                ActionInvoker.processCommand(new Action(ActionCommand.CMD_TRADE_MODIFY,second));
        }
        public void fieldChanged(Field field, int context) {
                try {
                        if(field instanceof CustomBasicEditField){
                        CustomBasicEditField targetField = (CustomBasicEditField) field;
                        if(targetField.getText().indexOf(".")!=-1) {
                                int lastIndex = targetField.getText().indexOf(".", targetField.getText().indexOf(".")+1);
                                if(lastIndex==-1) {
                                        int dotIndex = targetField.getText().indexOf(".");
                                        if((dotIndex+2)<(targetField.getText().length()-1)) {
                                                targetField.setText(targetField.getText().substring(0, dotIndex+3));
                                        }
                                        if(dotIndex==0) {
                                                targetField.setText("0"+targetField.getText());
                                        }
                                } else {
                                        targetField.setText("");
                                }
                        }
                        }
                } catch(Exception ex) {
                        LOG.print("Exception occured");
                }
        }
        public void refreshFields() {
                if(refresh){
                        refresh = false;
                        HomeJsonParser hj = new HomeJsonParser(Utils.getCompanyLatestTradingDetailsURL(bann.getCompanyCode()), this, REFRESH_ID_SENSEX);
                }
        }
               
        public void setModifyFocus()
        {
                int i = 0;
                while(true && i<5)
                {
                        if(vfmmain.getField(i).isFocusable())
                        {
                                vfmmain.getField(i).setFocus();
                                break;
                        }
                        i++;
                }
        }

}
