package cn.jj.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

public class BeanToMapUtils {
	/**
	 * 多条件查询时,需要把传入的对象转化成map
	 */
	public static Map<String, String> selectfunction(Object record) throws Exception {
		// 定义返回对象
		Map<String, String> map = new LinkedHashMap<>();
		// 得到目标类的字节码对象
		Class<? extends Object> clazz = record.getClass();
		// 得到目标类的所有字段
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 1; i < fields.length; i++) {
			// 设置字段的可见性
			fields[i].setAccessible(true);
			// 获得当前字段的字段名
			String fieldName = fields[i].getName();
			String truefieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			// 得到当前字段值的get方法
			Method getMethod = clazz.getMethod("get" + truefieldName);
			// 得到当前字段的修饰符
			Class<?> fieldClass = fields[i].getType();
			// 执行get方法
			Object object = getMethod.invoke(record);
			if (object != null) {
				if (fieldClass.equals(java.util.Date.class)) {
					// 如果当前字段是date类型的字段,将返回结果转化成Date类型的数据
					Date date = (Date) object;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					// 将日期类型的数据转化成字符串(格式化以后的数据)
					String dateStr = sdf.format(date);
					// 放入map中
					map.put(fieldName, dateStr);
				} else {
					map.put(fieldName, object.toString());
				}
			}
		}
		// 返回结果
		return map;
	}

	/**
	 * 插入一条记录时,需要把传入的对象转化成map
	 */
	public static Map<String, String> insertfunction(Object record) throws Exception {
		// 定义返回对象
		Map<String, String> map = new LinkedHashMap<>();
		// 得到目标类的字节码对象
		Class<? extends Object> clazz = record.getClass();
		// 得到目标类的所有字段
		Field[] fields = clazz.getDeclaredFields();
		for (int i = 1; i < fields.length; i++) {
			// 设置字段的可见性
			fields[i].setAccessible(true);
			// 获得当前字段的字段名
			String fieldName = fields[i].getName();
			String truefieldName = fieldName.substring(0, 1).toUpperCase() + fieldName.substring(1);
			// 得到当前字段的修饰符
			Class<?> fieldClass = fields[i].getType();
			// 得到当前字段值的get方法
			Method getMethod = clazz.getMethod("get" + truefieldName);
			// 执行get方法
			Object object = getMethod.invoke(record);
			if (object == null) {
				map.put(fieldName, "");
			} else {
				if (fieldClass.equals(java.util.Date.class)) {
					// 如果当前字段是date类型的字段,将返回结果转化成Date类型的数据
					Date date = (Date) object;
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					// 将日期类型的数据转化成字符串(格式化以后的数据)
					String dateStr = sdf.format(date);
					// 放入map中
					map.put(fieldName, dateStr);
				} else {
					map.put(fieldName, object.toString());
				}
			}
		}
		// 返回结果
		return map;
	}

}
