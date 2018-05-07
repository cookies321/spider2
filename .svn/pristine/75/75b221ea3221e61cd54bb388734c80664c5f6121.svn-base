package cn.jj.utils;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

public class MapToBeanUtils {
	/**
	 * 根据map,来返回一个实体对象
	 * @param map
	 * @return
	 * @throws Exception 
	 * @throws  
	 */
	public static  Object getEntityByMap (Class<?> clazz,Map<String,String> map) throws Exception{
		//创建对象
		Object  object = clazz.newInstance();
		//得到所有的字段
		Field[] fields = clazz.getDeclaredFields();
		for(int i=1 ;i<fields.length;i++){
			//得到当前字段的修饰符
			Class<?> fieldClass = fields[i].getType();
			//拼接set方法
			String fieldName = fields[i].getName();
			String truefieldName =fieldName.substring(0, 1).toUpperCase()+fieldName.substring(1);
			Method setMethod = clazz.getDeclaredMethod("set"+truefieldName, fieldClass);
			//把map中的Stirng类型强转成需要的类型
			if(!("".equals(map.get(fieldName)))){
				if(fieldClass.equals(Integer.class)){
					Integer param = Integer.valueOf(map.get(fieldName));
					setMethod.invoke(object, param);
				}else if((fieldClass.equals(java.util.Date.class))){
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
					Date param = sdf.parse(map.get(fieldName));
					setMethod.invoke(object, param);
				}else if(fieldClass.equals(String.class)){
					String param =  map.get(fieldName);
					setMethod.invoke(object, param);
				}
			}
		}
		return object;
	}
}
