package com.mw.customCalender;

import java.util.Calendar;

import net.rim.device.api.ui.Field;
import net.rim.device.api.ui.Font;

public class AttendanceCalendar extends Field {

	private Calendar calendar;
	private Font font = Font.getDefault();
	private int spacing = 3;
	private int cellWidth;
	private int cellHeight;
	private int startDay;
	private int noOfRows;

	private String formattedDate;
	private String[] weekArray = { "S", "M", "T", "W", "T", "F", "S" };
	private int numberOfDays;
	private int[] colorArray;

	public int[] getColorArray() {
		return colorArray;
	}

	public void setColorArray(int[] colorArray) {
		this.colorArray = colorArray;
	}

	public AttendanceCalendar() {
		calendar = Calendar.getInstance();

		startDay = DateUtils.getFirstDayOfMonth(calendar);
		numberOfDays = DateUtils.getNumberOfDaysInMonth(calendar
				.get(Calendar.MONTH), Calendar.YEAR);
		formattedDate = DateUtils.getFormatedDate();

		int noCells = numberOfDays + startDay - 1 + weekArray.length;

		if (noCells % 7 == 0) {
			noOfRows = noCells / 7;
		} else {
			noOfRows = (noCells / 7) + 1;
		}

		System.out.println("startDay  " + startDay + " no of days "
				+ numberOfDays);
	}

	protected void layout(int maxWidth, int maxHeight) {
		cellHeight = font.getHeight() + (spacing * 2);
		cellWidth = font.getAdvance("88") + (spacing * 2);

		int minCellWidth = (maxWidth / 7);
		if (cellWidth < minCellWidth) {
			cellWidth = minCellWidth;
		}

		int titleWidth = font.getAdvance(formattedDate);
		int titleHeight = font.getHeight();

		int calWidth = 7 * cellWidth;
		int calHeight = cellHeight * noOfRows;

		int height = titleHeight + calHeight + 2 * spacing;
		int width = ((calWidth > titleWidth) ? calWidth : titleWidth);

		setExtent(width, height);
	}

	protected void paint(net.rim.device.api.ui.Graphics graphics) {

		int x = getLeft();
		int y = getTop();

		int titleHeight = font.getHeight() + spacing * 2;

		graphics.setFont(font);
		graphics.setColor(0x0072BC);
		graphics.fillRect(x, y, getWidth(), titleHeight);

		graphics.setColor(0);
		graphics.drawRect(x, y, getWidth(), getHeight());

		graphics.setColor(0xFFFFFF);
		graphics.drawText(formattedDate, x
				+ (getWidth() - font.getAdvance(formattedDate)) / 2, y
				+ spacing);

		int currentTop = y + titleHeight;
		int currentLeft = x;

		int currentDay = 1;

		for (int row = 0; row < noOfRows; row++) {
			currentLeft = x;
			for (int colomn = 0; colomn < 7; colomn++) {

				int bgColor = 0xFFFFFF;
				int borderColor = 0;
				String string = "";

				if (row == 0) {
					bgColor = 0xAAAADD;
					string = weekArray[colomn];
				} else {

					if ((row == 1 && colomn < startDay - 1)
							|| currentDay > numberOfDays) {
						string = "";
						bgColor = -1;
					} else {
						bgColor = colorArray[currentDay];
						System.out.println("paint color " + currentDay + "  "
								+ bgColor);
						string = "" + currentDay;
						currentDay++;
					}
				}

				if (bgColor != -1) {
					graphics.setColor(bgColor);
					graphics.fillRect(currentLeft, currentTop, cellWidth,
							cellHeight);

					graphics.setColor(0);

					graphics.drawText(string, currentLeft
							+ (cellWidth - font.getAdvance(string)) / 2,
							currentTop + spacing);

				}
				graphics.setColor(borderColor);
				graphics.drawRect(currentLeft, currentTop, cellWidth,
						cellHeight);
				currentLeft += cellWidth;
			}
			currentTop += cellHeight;
		}
	}
}
