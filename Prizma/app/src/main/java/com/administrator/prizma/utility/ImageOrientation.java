package com.administrator.prizma.utility;


public enum ImageOrientation {

	PORTRAIT,
	LANDSCAPE;
	
	public static ImageOrientation getOrientation(int width, int height) {
		if(width >= height) {
			return ImageOrientation.LANDSCAPE;
		} else {
			return ImageOrientation.PORTRAIT;
		}
	}
	
}
