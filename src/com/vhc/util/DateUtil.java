package com.vhc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * <p>
 * Title: QLNT
 * </p>
 * <p>
 * Copyright: Copyright (c) by VHCSoft JSC 2015
 * </p>
 * <p>
 * Company: VietNam High Technology & Software Join Stock Company
 * </p>
 * 
 * @author VHCSoft JSC
 * @version 1.0
 */
public class DateUtil {
	private static SimpleDateFormat dff = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
	public static Date addDate(Date date, int days) {
	
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, days);
		
		return cal.getTime();
	}
	
	//using metro HCM
	public static String getDate(String strDate) { 
		if(strDate != null && !strDate.equals("")) {
			try {
				String day=strDate.substring(0, strDate.indexOf(" "));
				String time=strDate.substring(strDate.lastIndexOf(" ")+1);
				String milisecond=time.substring(time.lastIndexOf(".")+1);
				time=time.substring(0, time.indexOf("."));
				String[] timeList=time.split(":");
				int second=Integer.valueOf(day)*60*60*24+Integer.valueOf(timeList[0])*60*60+Integer.valueOf(timeList[1])*60
						+Integer.valueOf(timeList[2]);
				
				Date d = dff.parse("1/1/1970 07:00:00");
				Calendar cal = Calendar.getInstance();
				cal.setTime(d);
				cal.set(Calendar.SECOND, second*100+Integer.valueOf(milisecond));
				
				return dff.format(cal.getTime());
			} catch (Exception e) {
				// TODO: handle exception
				return "";
			} 
		}
		
		return "";
	}
	
	public static String getDate_2(String strDate) { 
		Date parsed;
		String str;
		try {
		    SimpleDateFormat format =  new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
		    parsed = format.parse(strDate);
		    str = dff.format(parsed);
		}
		catch(ParseException pe) {
		    return null;
		}  
		
		return str;
	}
	
	public static void main(String[] args) {
		System.out.println(new Date());
		System.out.println(getDate_2(null));
	}
}
