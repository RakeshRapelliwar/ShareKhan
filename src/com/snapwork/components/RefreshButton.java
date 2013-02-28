package com.snapwork.components;
import com.snapwork.util.ImageManager;

import net.rim.device.api.system.Bitmap;
import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Graphics;

/**
 * Button field with a bitmap as its label.
 */
public class RefreshButton extends Field {
        private Bitmap bitmap;
        private Bitmap bitmapHighlight;
        private boolean highlighted = false;
        private boolean loading;

        /**
         * Instantiates a new bitmap button field.
         * 
         * @param bitmap the bitmap to use as a label
         */
       

        public RefreshButton() {
            super(FOCUSABLE/*|ButtonField.CONSUME_CLICK|ButtonField.FIELD_HCENTER|ButtonField.FIELD_VCENTER*/);
            this.bitmap = ImageManager.getRefreshButton();
            this.bitmapHighlight = ImageManager.getRefreshButtonLoading();
        }

        /* (non-Javadoc)
         * @see net.rim.device.api.ui.component.ButtonField#layout(int, int)
         */
        /*protected void layout(int width, int height) {
                setExtent(getPreferredWidth(), getPreferredHeight());
        }*/

        /* (non-Javadoc)
         * @see net.rim.device.api.ui.component.ButtonField#getPreferredWidth()
         */
        public int getPreferredWidth() {
                return bitmap.getWidth()+2;
        }

        /* (non-Javadoc)
         * @see net.rim.device.api.ui.component.ButtonField#getPreferredHeight()
         */
        public int getPreferredHeight() {
                return bitmap.getHeight()+2;
        }

        /* (non-Javadoc)
         * @see net.rim.device.api.ui.component.ButtonField#paint(net.rim.device.api.ui.Graphics)
         */
        protected void paint(Graphics graphics) {
                int width = bitmap.getWidth();
                int height = bitmap.getHeight();
                Bitmap b ;
                if (loading)
                    b = bitmapHighlight;
                else
                	b = bitmap;
                if(isFocus())
                {
                	graphics.setColor(Color.ORANGE);
                	graphics.fillRoundRect(0, 0, getPreferredWidth(), getPreferredHeight(), 4, 4);
                }
                graphics.drawBitmap(1, 1, width, height, b, 0, 0);
        }
        public boolean getLoading()
        {
        	return loading;
        }
        public void setLoading(boolean loading)
        {
        	this.loading = loading;
        	invalidate();
        }

        public void setHighlight(boolean highlight)
        {
            this.highlighted = highlight;           
        }

		protected void layout(int arg0, int arg1) {
			setExtent(arg0, arg1);
		}
		
		 protected void drawFocus(Graphics graphics, boolean on) {
             //Dont do anything and dont let the System
     }
}

