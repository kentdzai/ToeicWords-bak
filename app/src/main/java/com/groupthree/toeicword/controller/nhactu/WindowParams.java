package com.groupthree.toeicword.controller.nhactu;

import android.view.WindowManager;

public class WindowParams extends WindowManager.LayoutParams {
	private static int type = 2003;
	private static int format = 1;
	private static int flags = WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;
	private static int width = -2;
	private static int height = -2;
	private static int x = 0;
	private static int y = 0;

	public WindowParams() {
		super(width, height, x, y, type, flags, format);
	}

}
