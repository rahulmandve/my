package com.paysquare.deliziahr.Util;

import com.paysquare.deliziahr.Constants.Constants;

public class StaticWaits {
	
	public static void shortWait()
	{
		try {
			Thread.sleep(Constants.SHORT_WAIT);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static void mediumWait()
	{
		
		try {
			Thread.sleep(Constants.MEDIUM_WAIT);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}
	
	public static void longWait()
	{
		try {
			Thread.sleep(Constants.LONG_WAIT);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
	}

}
