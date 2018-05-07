package cn.jj.utils;

import java.text.ParseException;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * 日期工具类
 * @author 汤玉林
 * 2017年11月3日
 */
public class DateUtil {

	private  int year;
	private  int month;
	private  int date;
	
	static SimpleDateFormat df =new SimpleDateFormat("yyyy-MM-dd");

	public static DateUtil dateUtil=null; 

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getDate() {
		return date;
	}

	public void setDate(int date) {
		this.date = date;
	}

	public static DateUtil getInstance(){
		if(dateUtil==null){
			dateUtil=new DateUtil();
		}
		return dateUtil;
	}

	/**
	 * 获取日期中的年月日
	 * @param date 
	 * @return DateUtil 获取的属性分别代表年月日
	 */
	public static DateUtil getDateYMD(String str){
		SimpleDateFormat sdf= new SimpleDateFormat("yyyy-MM-dd");
		Date date=null;
		try {
			date = sdf.parse(str);
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		DateUtil.getInstance();
		Calendar now=Calendar.getInstance();
		now.setTime(date);
		dateUtil.setYear(now.get(Calendar.YEAR));
		dateUtil.setMonth(now.get(Calendar.MONTH));
		dateUtil.setDate(now.get(Calendar.DATE));
		return dateUtil;
	}

	/**
	 * 	获取每个月的最大天数
	 * @param date
	 * @return
	 */
	public static int getDaysOfMonth(Date date) {  

		Calendar calendar = Calendar.getInstance();  
		calendar.setTime(date);  
		return calendar.getActualMaximum(Calendar.DAY_OF_MONTH);  
	}  

	/**
	 * 
	 * @Description 输入一个日期和时间段获取最后一天的日期
	 * @author 汤玉林
	 * @date 2017年11月14日 下午4:45:14
	 * @action getEndDate
	 * @param startTime 开始时间，格式为yyyy-MM-dd
	 * @param dateline 整型时间线（天）
	 * @return 
	 */
	public static String getEndDate(String startTime,int dateline){
		// 将字符串的日期转为Date类型，ParsePosition(0)表示从第一个字符开始解析
		Date date = df.parse(startTime, new ParsePosition(0));
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		// add方法中的第二个参数n中，正数表示该日期后n天，负数表示该日期的前n天
		calendar.add(Calendar.DATE, dateline);
		return df.format(calendar.getTime());
	}

	public static void main(String[] args) throws ParseException  {  
		String startDate="2017-11-12";
		String endDate="2018-1-13";
		System.out.println(getEndDate(startDate, 60));
		//findDates(startDate, endDate);
	}  

	/**
	 * 日期转星期
	 * @param datetime
	 * @return String 返回星期
	 * @author 姚良良
	 */
	public static String dateToWeek(String datetime) {
		SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
		String[] weekDays = { "星期日", "星期一", "星期二", "星期三", "星期四", "星期五", "星期六" };
		Calendar cal = Calendar.getInstance(); // 获得一个日历
		Date datet = null;
		try {
			datet = f.parse(datetime);
			cal.setTime(datet);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		int w = cal.get(Calendar.DAY_OF_WEEK) - 1; // 指示一个星期中的某天。
		if (w < 0)
			w = 0;
		return weekDays[w];
	}


	/**
	 * 
	 * @Description 根据输入的两个日期字符串获取中间的每一天(格式 ：yyyy-MM-dd)
	 * @author 汤玉林
	 * @date 2017年12月27日 下午2:25:40
	 * @action findDates
	 * @param begin 开始日期
	 * @param end 结束日期
	 * @return
	 */
	public static List<String> findDates(String begin, String end) {
		Date startDate=null;
		Date endDate=null;
		try {
			startDate = df.parse(begin);
			endDate = df.parse(end);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		List<String> dateList = new ArrayList<String>();
		dateList.add(df.format(startDate));
		Calendar beginCalen = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		beginCalen.setTime(startDate);
		
		/*Calendar endCalen = Calendar.getInstance();
		// 使用给定的 Date 设置此 Calendar 的时间
		endCalen.setTime(endDate);*/
		// 测试此日期是否在指定日期之后
		while (endDate.after(beginCalen.getTime()))  {
			// 根据日历的规则，为给定的日历字段添加或减去指定的时间量
			beginCalen.add(Calendar.DAY_OF_MONTH, 1);
			dateList.add(df.format(beginCalen.getTime()));
		}
		return dateList;
	}
	
}
