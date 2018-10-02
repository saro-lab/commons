package me.saro.commons;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Date Format
 * <br>
 * this class use SimpleDateFormat(thread-not-safe)
 * <br>
 * but this class <b>thread-safe</b> because every use clone by SimpleDateFormat
 * @author		PARK Yong Seo
 * @since		0.1
 */
public class DateFormat {

	// DATE FORMAT LIST
	final private static Map<String, SimpleDateFormat> SDFM = new HashMap<>();

	// inner object
	final private Calendar calendar;

	/**
	 * private Constructor
	 * @param timeInMillis
	 * java timeInMillis 
	 */
	private DateFormat(long timeInMillis) {
		calendar = Calendar.getInstance();
		this.calendar.setTimeInMillis(timeInMillis);
	}

	/**
	 * private Constructor
	 * @param calendar
	 */
	private DateFormat(Calendar calendar) {
		this.calendar = calendar;
	}

	/**
	 * new instant DateFormat with Calendar.getInstance()
	 * @return
	 */
	public static DateFormat now() {
		return new DateFormat(Calendar.getInstance());
	}

	/**
	 * date to DateFormat
	 * @param date
	 * @return
	 */
	public static DateFormat parse(Date date) {
		return new DateFormat(date.getTime());
	}

	/**
	 * timeInMillis to DateFormat
	 * @param timeInMillis
	 * @return
	 */
	public static DateFormat parse(long timeInMillis) {
		return new DateFormat(timeInMillis);
	}
	
	/**
	 * String date to DateFormat
	 * @param date
	 * @param format
	 * @return
	 */
	public static DateFormat parse(String date, String format) {
		SimpleDateFormat sdf;
		synchronized (SDFM) {
			if ((sdf = SDFM.get(format)) == null) {
				SDFM.put(format, (sdf = new SimpleDateFormat(format)));
			}
		}
		try {
			return new DateFormat((((SimpleDateFormat)sdf.clone()).parse(date)).getTime());
		} catch (ParseException e) {
			throw new RuntimeException(e);
		}
	}

	/**
	 * convert string date format
	 * @param date
	 * @param oldFormat
	 * @param newFormat
	 * @return
	 */
	public static String format(String date, String oldFormat, String newFormat) {
		try {
			if (date != null && !date.isEmpty()) {
				return DateFormat.parse(date, oldFormat).toString(newFormat);
			}
		} catch (Exception e) {}
		return null;
	}

	/**
	 * date validator 
	 * @param date
	 * @param format
	 * @return
	 */
	public static boolean valid(String date, String format) {
		try {
			if (date != null && format != null) {
				DateFormat df = DateFormat.parse(date, format);
				return df.toString(format).equals(date);
			}
		} catch (Exception e) {}
		return false;
	}
	
	/**
	 * add Milliseconds
	 * @param milliseconds
	 * @return
	 */
	public DateFormat addMilliseconds(int milliseconds) {
		calendar.add(Calendar.MILLISECOND, milliseconds);
		return this;
	}

	/**
	 * add Minutes
	 * @param minutes
	 * @return
	 */
	public DateFormat addMinutes(int minutes) {
		calendar.add(Calendar.MINUTE, minutes);
		return this;
	}

	/**
	 * add Hours
	 * @param hours
	 * @return
	 */
	public DateFormat addHours(int hours) {
		calendar.add(Calendar.HOUR, hours);
		return this;
	}

	/**
	 * add Date
	 * @param date
	 * @return
	 */
	public DateFormat addDates(int date) {
		calendar.add(Calendar.DATE, date);
		return this;
	}

	/**
	 * add Month
	 * <br>
	 * <b>logic</b> : 
	 * <br>
	 * same calendar.add(Calendar.MONTH, month);  
	 * @param month
	 * @return
	 */
	public DateFormat addMonth(int month) {
		calendar.add(Calendar.MONTH, month);
		return this;
	}

	/**
	 * add Year
	 * <br>
	 * <b>logic</b> : 
	 * <br>
	 * same calendar.add(Calendar.YEAR, year);  
	 * @param year
	 * @return
	 */
	public DateFormat addYear(int year) {
		calendar.add(Calendar.YEAR, year);
		return this;
	}
	
	/**
	 * set TimeInMillis
	 * <b>WARNING : </b> is not Milliseconds
	 * @param timeInMillis
	 * @return
	 */
	public DateFormat setTimeInMillis(long timeInMillis) {
		calendar.setTimeInMillis(timeInMillis);
		return this;
	}

	/**
	 * set milliseconds
	 * <br>
	 * <b>WARNING : </b> is not TimeInMillis
	 * @param milliseconds
	 * @return
	 */
	public DateFormat setMilliseconds(int milliseconds) {
		calendar.set(Calendar.MILLISECOND, milliseconds);
		return this;
	}
	
	/**
	 * set Seconds
	 * @param seconds
	 * @return
	 */
	public DateFormat setSeconds(int seconds) {
		calendar.set(Calendar.SECOND, seconds);
		return this;
	}

	/**
	 * set Minutes
	 * @param minutes
	 * @return
	 */
	public DateFormat setMinutes(int minutes) {
		calendar.set(Calendar.MINUTE, minutes);
		return this;
	}

	/**
	 * set Hours
	 * @param hours
	 * @return
	 */
	public DateFormat setHours(int hours) {
		calendar.set(Calendar.HOUR, hours);
		return this;
	}

	/**
	 * set Date
	 * @param date
	 * @return
	 */
	public DateFormat setDate(int date) {
		calendar.set(Calendar.DATE, date);
		return this;
	}

	/**
	 * set Month
	 * @param month
	 * @return
	 */
	public DateFormat setMonth(int month) {
		calendar.set(Calendar.MONTH, month);
		return this;
	}

	/**
	 * set Year
	 * @param year
	 * @return
	 */
	public DateFormat setYear(int year) {
		calendar.set(Calendar.YEAR, year);
		return this;
	}
	
	/**
	 * get TimeInMillis
	 * <br>
	 * <b>WARNING : </b> is not Milliseconds
	 * @return
	 */
	public long getTimeInMillis() {
		return calendar.getTimeInMillis();
	}

	/**
	 * get Milliseconds
	 * <br>
	 * <b>WARNING : </b> is not TimeInMillis
	 * @return
	 */
	public int getMilliseconds() {
		return calendar.get(Calendar.MILLISECOND);
	}
	
	/**
	 * get Seconds
	 * @return
	 */
	public int getSeconds() {
		return calendar.get(Calendar.SECOND);
	}

	/**
	 * get Minute
	 * @return
	 */
	public int getMinute() {
		return calendar.get(Calendar.MINUTE);
	}

	/**
	 * get Hours
	 * @return
	 */
	public int getHours() {
		return calendar.get(Calendar.HOUR);
	}

	/**
	 * get Date
	 * @return
	 */
	public int getDate() {
		return calendar.get(Calendar.DATE);
	}

	/**
	 * get Month
	 * @return
	 */
	public int getMonth() {
		return calendar.get(Calendar.MONTH);
	}

	/** 
	 * get Year
	 * @return
	 */
	public int getYear() {
		return calendar.get(Calendar.YEAR);
	}

	/**
	 * get DayOfWeek
	 * @return
	 */
	public int getDayOfWeek() {
		return calendar.get(Calendar.DAY_OF_WEEK);
	}

	/**
	 * get WeekOfMonth
	 * @return
	 */
	public int getWeekOfMonth() {
		return calendar.get(Calendar.WEEK_OF_MONTH);
	}

	/**
	 * get WeekOfYear
	 * @return
	 */
	public int getWeekOfYear() {
		return calendar.get(Calendar.WEEK_OF_YEAR);
	}
	
	/**
	 * DateFormat to String
	 * @param format
	 * @return
	 */
	public String format(String format) {
		SimpleDateFormat sdf;
		synchronized (SDFM) {
			if ((sdf = SDFM.get(format)) == null) {
				SDFM.put(format, (sdf = new SimpleDateFormat(format)));
			}
		}
		return ((SimpleDateFormat) sdf.clone()).format(this.calendar.getTime());
	}

	/**
	 * DateFormat to String
	 * <br>
	 * same format(String format)
	 * @param format
	 * @return
	 */
	public String toString(String format) {
		return format(format);
	}

	/**
	 * to ISO8601
	 * yyyy-MM-dd'T'HH:mm:ssZ[+HH:mm]
	 */
	public String toString() {
		return toISO8601();
	}

	/**
	 * to ISO8601
	 * @return
	 */
	public String toISO8601() {
		String format = this.format("yyyy-MM-dd'T'HH:mm:ssZ");
		return format.lastIndexOf('+') != format.length() - 5 ? format.replaceFirst("([\\d]{2})$", ":$1") : format;
	}

	/**
	 * to Date
	 * @return
	 */
	public Date toDate() {
		return this.calendar.getTime();
	}

	/**
	 * clone
	 */
	public DateFormat clone() {
		return new DateFormat((Calendar) this.calendar.clone());
	}
	
	/**
	 * equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (obj != null && this.getClass().getName().equals(obj.getClass().getName())) {
			return this.getTimeInMillis() == ((DateFormat)obj).getTimeInMillis();
		}
		return false;
	}
}
