package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 
 * @author grivera
 */
public class DateUtil {

	static DateFormat dfdate = new SimpleDateFormat("yyyyMMdd");
	static DateFormat dftime = new SimpleDateFormat("HHmmss");
	static DateFormat dfdatescreen = new SimpleDateFormat("dd/MM/yyyy");
	static DateFormat dfdir = new SimpleDateFormat("ddMMyyyy");
	static DateFormat dfdatefull = new SimpleDateFormat("ddMMyyHHmmss");
	static DateFormat dfdateprint = new SimpleDateFormat("ddMMyy_HH_mm_ss");
	static DateFormat dftimereport = new SimpleDateFormat("HH_mm");
	static DateFormat keyDate = new SimpleDateFormat("yyyy-MM-dd");
	static DateFormat sdfPDF = new SimpleDateFormat("ddMMyyyy_hhmmss");
	static DateFormat dfmaildate = new SimpleDateFormat("dd/MM/yyyy HH:mm");
	static DateFormat mysqlFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	static DateFormat dfUserFunction = new SimpleDateFormat("ddMMyyyyhhssss");
	
	public static Date today;

	public static String dateToString() {
		today = Calendar.getInstance().getTime();
		return dfdate.format(today);
	}

	public static String timeToString() {
		today = Calendar.getInstance().getTime();
		return dftime.format(today);
	}

	public static String dateToStringScreen() {
		today = Calendar.getInstance().getTime();
		return dfdatescreen.format(today);
	}

	public static String dateToStringDir() {
		today = Calendar.getInstance().getTime();
		return dfdir.format(today);
	}

	public static String dateToStringFull() {
		today = Calendar.getInstance().getTime();
		return dfdatefull.format(today);
	}

	public static String dateToStringPrint() {
		today = Calendar.getInstance().getTime();
		return dfdateprint.format(today);
	}
	
	public static String dateToStringReport() {
		today = Calendar.getInstance().getTime();
		return dftimereport.format(today);
	}
	
	public static Date stringToDate(String stringDate) {
		Date date = null;
		try {
			date = keyDate.parse(stringDate);
		} catch (ParseException e) {
			System.out.println("Ocurrio un error stringToDate() " + e.getMessage());
		}
		return date;
	}
	
	
	public static String dateToStringUserFunction() {
		today = Calendar.getInstance().getTime();
		return dfUserFunction.format(today);
	}
	
	public static String dateToStringUserPDF(Date fch) {
		return dfdatescreen.format(fch);
	}
	
	public static String dateToStringPDF(){
		return sdfPDF.format(Calendar.getInstance().getTime());
	}
	
	public static String dateToStringMail() {
		today = Calendar.getInstance().getTime();
		return dfmaildate.format(today);
	}
	
	public static String dateToStringMySqlFormat() {
		today = Calendar.getInstance().getTime();
		return mysqlFormat.format(today);
	}
	
	public static boolean isKeyValidDate(String date) {
		try {
			keyDate.setLenient(false);
			keyDate.parse(date);
			return true;
		} catch (ParseException e) {
			return false;
		}
	}
}
