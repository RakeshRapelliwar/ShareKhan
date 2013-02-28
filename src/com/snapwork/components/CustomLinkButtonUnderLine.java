package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

/**
 * 
 * <p>This class provide the same theme of customLinkButton with underline for all Versions of BlackBerry
 * 
 */

public class CustomLinkButtonUnderLine extends LabelField {
        
        private int fontColor;
        private int marginLeft;
        public CustomLinkButtonUnderLine(String strLabelText,long style,int intColor,Font fntFont) {
                super(strLabelText,style);
                this.setFont(fntFont);
                this.fontColor = intColor;
                marginLeft = 0;
        }
        public CustomLinkButtonUnderLine(String strLabelText,long style,int intColor,Font fntFont, int margenLeft) {
            super(strLabelText,style);
            this.setFont(fntFont);
            this.fontColor = intColor;
            this.marginLeft = margenLeft;
    }
        
        protected void onFocus(int direction) {
                super.onFocus(direction);
                invalidate();
        }
        
        protected void onUnfocus() {
                super.onUnfocus();
                invalidate();
        }
        
        public int getPreferredWidth() {
                return this.getFont().getAdvance(this.getText())+marginLeft+2;
        }
        
        public int getPreferredHeight() {
            return this.getFont().getHeight()+2;
    }
        
        protected void paint(Graphics graphics) {
                if(isFocus()) {
                    graphics.setColor(Color.ORANGE);
                } else {
                    graphics.setColor(0xeeeeee);                	
                }
                graphics.setColor(fontColor);
                graphics.setFont(this.getFont());
                graphics.drawText(this.getText(), marginLeft+1, 1);
                if(isFocus()) {
                    graphics.setColor(Color.ORANGE);
                } else {
                    graphics.setColor(Color.BLACK);                	
                }
                graphics.drawLine(marginLeft+1, this.getFont().getHeight()+1,  this.getFont().getAdvance(this.getText())+marginLeft+1, this.getFont().getHeight()+1);
                
        }

        protected void layout(int width, int height) {
            setExtent(this.getFont().getAdvance(this.getText())+marginLeft+2, this.getFont().getHeight()+2);
        }

        protected void drawFocus(Graphics graphics, boolean on) {
                //Dont do anything and dont let the System
        }
        
        
        
}
