package com.snapwork.components;

import net.rim.device.api.ui.Color;
import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;
import net.rim.device.api.ui.Graphics;

import com.snapwork.util.AppConstants;

/**
 * 
 * <p>
 * This class is part of Company Screen.
 * <p>
 * This class create custom field which shows details of Companies as follows,
 * <p>
 * 1.Day High/Low
 * <p>
 * 2.52 week High/Low
 * <p>
 * 3.Volume
 * 
 */

public class CustomInfoLabelField extends Field {
	private boolean isValueSet;
	private String key1 = null;
	private String value1 = null;
	private String key2 = null;
	private String value2 = null;
	private String key3 = null;
	private String value3 = null;

	public CustomInfoLabelField(Font font) {
		super(FIELD_HCENTER | FIELD_HCENTER);
		setFont(font);
	}

	public int getPreferredHeight() {
		return getFont().getHeight() * 3 + AppConstants.padding * 2;
	}

	public int getPreferredWidth() {
		return AppConstants.screenWidth - 20;
	}

	public void setValue(String key1, String value1, String key2,
			String value2, String key3, String value3) {
		isValueSet = true;
		this.key1 = key1;
		this.value1 = value1;
		this.key2 = key2;
		this.value2 = value2;
		this.key3 = key3;
		this.value3 = value3;
		invalidate();
	}

	public void reset() {
		isValueSet = false;
	}

	protected void layout(int width, int height) {
		setExtent(getPreferredWidth(), getPreferredHeight());
	}

	protected void paint(Graphics graphics) {
		graphics.setColor(Color.BLACK);
		graphics.fillRect(0, 0, getPreferredWidth(), getPreferredHeight());
		graphics.setColor(4343106);
		graphics.fillRoundRect(0, 0, getPreferredWidth(), getPreferredHeight(),
				10, 10);
		graphics.setColor(0xeeeeee);
		graphics.setFont(getFont());
		if (isValueSet) {
			graphics.drawText(key1, (AppConstants.padding / 3),
					(AppConstants.padding / 3));
			graphics.drawText(value1,
					getPreferredWidth() - (AppConstants.padding / 3)
							- getFont().getAdvance(value1),
					(AppConstants.padding / 3));

			graphics.drawText(key2, (AppConstants.padding / 3),
					(AppConstants.padding) + getFont().getHeight());
			graphics.drawText(value2,
					getPreferredWidth() - (AppConstants.padding / 3)
							- getFont().getAdvance(value2),
					(AppConstants.padding) + getFont().getHeight());

			graphics.drawText(key3, (AppConstants.padding / 3),
					(AppConstants.padding) + getFont().getHeight() * 2
							+ (AppConstants.padding / 3) * 2);
			graphics.drawText(value3,
					getPreferredWidth() - (AppConstants.padding / 3)
							- getFont().getAdvance(value3),
					(AppConstants.padding) + getFont().getHeight() * 2
							+ (AppConstants.padding / 3) * 2);
			graphics.setColor(Color.GRAY);
			if (!(key2.equals(""))) {
				graphics.drawLine(0, (AppConstants.padding / 3) * 2
						+ getFont().getHeight(), getPreferredWidth(),
						(AppConstants.padding / 3) * 2 + getFont().getHeight());
				graphics.drawLine(
						0,
						((AppConstants.padding / 3) * 4 + getFont().getHeight() * 2),
						getPreferredWidth(),
						((AppConstants.padding / 3) * 4 + getFont().getHeight() * 2));
			} else
				graphics.drawLine(
						0,
						((AppConstants.padding / 3) * 5 + getFont().getHeight()),
						getPreferredWidth(),
						((AppConstants.padding / 3) * 5 + getFont().getHeight()));

		} else {
			graphics.drawText(AppConstants.loadingMessage, getPreferredWidth()
					/ 2 - getFont().getAdvance("Retriving Values...") / 2,
					getPreferredHeight() / 2 - getFont().getHeight() / 2);
		}

	}

}
