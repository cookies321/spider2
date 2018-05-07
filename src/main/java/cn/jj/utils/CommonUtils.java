package cn.jj.utils;

import java.util.Map;

public class CommonUtils {

	/**
	 * 根据传来的字节码对象,获取类名
	 */
	public static  String getClassName(Class<?> clazz){
		String className =  "";
		 className =  clazz.getName().split("\\.")[4];
		return className;
		
	}
	/**
	 * 根据传来的两个map,看是否满足多条件查询的条件
	 */
	public static Boolean isOK(Map<String,String> map ,Map<String,String> redisMap){
		Boolean flag = true;
		for(String key:map.keySet()){
			String redisStr = redisMap.get(key);
			String str = map.get(key);
			if(str.equals(redisStr)){
				//说明这个字段是符合条件的
				flag =  flag && true;
			}else{
				//只要有一个条件不符合,把标志置成false
				flag = false;
				return flag;
			}
		}
		return flag;
	}
	/**
	 * 把两个map进行整合
	 * 
	 */
	public static  Map<String,String> updateMap(Map<String,String> existMap,Map<String,String> updateMap){
		for(String key:updateMap.keySet()){
			//遍历已经存在map
			existMap.put(key,updateMap.get(key));
		}
		return existMap;
	}
	
	
}
