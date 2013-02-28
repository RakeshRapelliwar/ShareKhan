package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

/**
 * 
 * <p>This class provide the same theme of customLinkButton for all Versions of BlackBerry
 *
 */
public class CustomLinkButton extends LabelField
{       
        private int fontColor;       
        public CustomLinkButton(String strLabelText,long style,int intColor,Font fntFont) 
        {
                super(strLabelText,style);
                this.setFont(fntFont);
                this.fontColor = intColor;
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
        
        public int getPreferredWidth() 
        {
                return this.getFont().getAdvance(this.getText())+2;
        }
        
        public int getPreferredHeight() 
        {
            return this.getFont().getHeight()+2;
    }
        
        protected void paint(Graphics graphics) 
        {
                if(isFocus()) 
                {
                    graphics.setColor(Color.ORANGE);
                } else {
                    graphics.setColor(fontColor);                	
                }
                graphics.drawRect(0, 0, this.getFont().getAdvance(this.getText())+2, this.getFont().getHeight());
                graphics.setColor(fontColor);
                graphics.setFont(this.getFont());
                graphics.drawText(this.getText(),1, 1);
        }

        protected void layout(int width, int height)
        {
            setExtent(this.getFont().getAdvance(this.getText())+2, this.getFont().getHeight()+2);
        }

        protected void drawFocus(Graphics graphics, boolean on) 
        {
                //Dont do anything and dont let the System
        }
}
