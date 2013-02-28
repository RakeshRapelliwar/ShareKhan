package com.snapwork.components;

import com.snapwork.util.LOG;


public class BlockField 
{
	public static final int V_NONE = -1;
	public static final int V_CENTER = 0;
	public static final int V_TOP = 1;
	public static final int V_BOTTOM = 2;
	public static final int H_NONE = -1;
	public static final int H_CENTER = 0;
	public static final int H_LEFT = 1;
	public static final int H_RIGHT = 2;	
	public int xPos = -1;
	public int yPos = -1;
	public int verticalAlignOnBlock = -1;
	public int horizontalAlignOnBlock = -1;
	public Object field = null;
	public BlockField(int xPos,int yPos,int horizontalAlignOnBlock,int verticalAlignOnBlock,Object field) {
		try 
		{
			this.xPos = xPos;
			this.yPos = yPos;
			this.verticalAlignOnBlock = verticalAlignOnBlock;
			this.horizontalAlignOnBlock = horizontalAlignOnBlock;
			this.field = field;
		} 
		catch(Exception ex) 
		{

		}
	}
	private int height = -1;
	public void setHeight(int height) {
		this.height = height;
		LOG.print("BlockField "+height);
	}
	public int getHeight() {
		return height;
	}


}
