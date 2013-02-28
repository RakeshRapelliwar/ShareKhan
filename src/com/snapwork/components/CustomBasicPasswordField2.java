package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.BasicEditField;
import net.rim.device.api.ui.component.PasswordEditField;

import com.snapwork.util.AppConstants;
import com.snapwork.util.Utils;

/**
 * 
 * <p>This class created customPasswordField which background white and textcolor black for all versions of BlackBerry OS.
 *
 */
public class CustomBasicPasswordField2 extends PasswordEditField
{

public CustomBasicPasswordField2(String string, String string2, int i, long filterNumeric) {
			super(string, string2, i, filterNumeric);
		}

		protected void paintBackground(Graphics graphics) {
        	graphics.setColor(0xeeeeee);
        	graphics.fillRect(0,0,getWidth(),getHeight());
        	graphics.setColor(Color.BLACK);
        }
        
        protected void paint(Graphics graphics) {
                graphics.setFont(this.getFont());
                graphics.setColor(Color.BLACK);
                super.paint(graphics);
        }
        /*protected void layout(int width, int height)
    	{
    		super.setPosition(5, 0);
    		super.layout(AppConstants.screenWidth-10, height);
    	}*/
}
