package com.vhc.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtil {

	/**
	 * Remove space character
	 * @param msg
	 * @return
	 */
	public static String removeSpaceCharacter(String msg) {
		return msg.replaceAll("\\s+", " ");
	}
	
	/**
	 * Get value by field
	 * @param field
	 * @param str
	 * @param delim
	 * @return
	 */
	public static String getValueByField(String field, String str, String delim){
		Pattern pattern =  Pattern.compile(field +" .[^" + delim + "]{0,}");
		Matcher matcher = pattern.matcher(str);
		if(matcher.find()){
			String tmp = matcher.group();
			tmp = tmp.substring(tmp.indexOf(" ") + 1);
			if(tmp.indexOf(",") == 0) return "";
			if(tmp.indexOf(";") == (tmp.length()-1)) return tmp.substring(0,tmp.length()-1);
			return tmp; 
		}
		else return null;
	}
	
	public static void main(String arg[]) {
		System.out.println(removeSpaceCharacter("dd    hi "));
		
		System.out.println(getValueByField("NotifId", "IntId  755188  NotifId  317431", " "));
	}
}
