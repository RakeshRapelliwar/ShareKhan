package com.snapwork.components;
import net.rim.device.api.ui.DrawStyle;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;
import net.rim.device.api.ui.component.LabelField;

public class WrapLabelField extends LabelField {

    private Object text;
    private Font font;
    private int colour;
    private long style;

    public WrapLabelField(Object text, long style , Font font, int colour) {
        super(text, style);
        this.text = text;
        this.colour = colour;
        super.setFont(font);
    }
    protected void drawFocus(Graphics arg0, boolean arg1) {
    	// TODO Auto-generated method stub
    	super.drawFocus(arg0, arg1);
    }

    protected void paint(Graphics graphics) {
    	graphics.setColor(this.colour);
    	getManager().invalidate();
		super.paint(graphics);

    }
}