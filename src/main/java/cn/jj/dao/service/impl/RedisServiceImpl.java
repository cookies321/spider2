package cn.jj.dao.service.impl;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

import cn.jj.dao.service.RedisService;
import cn.jj.utils.BeanToMapUtils;
import cn.jj.utils.CommonUtils;
import cn.jj.utils.MapToBeanUtils;
import cn.jj.utils.RedisUtil;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.Pipeline;



public class RedisServiceImpl implements RedisService {
	/**
	 * 多条件查询
	 */
	@Override
	public <T> List<T> selectByMultipleAttribute(Object record) {
	     List<T> list =  new LinkedList<>();
	     try { 
	    	  Map<String,String> map =  new LinkedHashMap<>();
	    	  //得到多条件查询的map
	    	  map = BeanToMapUtils.selectfunction(record);
	    	  //得到当前参数的类名
	    	  String className = CommonUtils.getClassName(record.getClass());
	    	  //找到当前对象的类的所有的key
	    	  RedisUtil jedis = RedisUtil.getInstance();
	    	  Set<String> keysSet = jedis.key(className+"-*");
	    	  //循环
	    	  for(String key :keysSet){
	    		  //根据key查找哈希结构的数据
	    		  Map<String,String> redisMap = jedis.hgetAll(key);
	    		  //调用工具类,判断当前redis中的哈希数据是否满足条件
	    		  Boolean flag =  CommonUtils.isOK(map,redisMap);
	    		  if(flag){
	    			  //满足条件,把这个map转化成对象
	    			  Object object = MapToBeanUtils.getEntityByMap(record.getClass(), redisMap);
	    			  //把转化成的对象加入到集合中
	    			  list.add((T) object);
	    		  }
	    	  }
	    	  
		} catch (Exception e) {
			e.printStackTrace();
		}
	     if(list.size()==0){
	    	 System.out.println("未查询出结果");
	     }
	     return list;
	}
	
	/**
	 * 根据id查找特定对象(直接使用根据key,找到特定的map,并封装成对象返回)
	 */
	@Override
	public Object getBeaninfoById(Class clazz,String id) { 
		Object  object =  null;
		Map<String,String> map= new HashMap<>();
		RedisUtil utils = RedisUtil.getInstance();
		//得到查询redis的key
		String key = CommonUtils.getClassName(clazz)+"-"+id;
		try {
		//得到map
		map = utils.hgetAll(key);
		//转化成实体类
		object  = MapToBeanUtils.getEntityByMap(clazz, map);
		} catch (Exception e) {
			
			e.printStackTrace();
		}
		return object;
	}
	/**
	 * 插入单条记录,并得到id
	 */
	@Override
	public String insertAndGetId(Object record) {
		Map<String,String> map = new HashMap<>();
		Map<String,String> existMap =  new HashMap<>();
		Map<String,String> updateMap =  new HashMap<>();
		RedisUtil jedis =  RedisUtil.getInstance();
		try {
			//把传过来的对象转化成map
			map = BeanToMapUtils.insertfunction(record);
			//判断是否有id
			String id = map.get("id");
			if("".equals(id)){
				//对象中没有id,自己设置一个id
				id = UUID.randomUUID().toString();
				map.put("id", id);
				//执行插入
				jedis.hmset(CommonUtils.getClassName(record.getClass())+"-"+id, map);
				return id;
			}else{
				//对象中有id
				//判断缓存中是否含有这条记录
				String key = CommonUtils.getClassName(record.getClass())+"-"+id;
				Set<String> set = jedis.key(key);
				if(set.size()==0){
					//找不到,说明缓存数据库中没有相同的记录,直接插入
					jedis.hmset(key, map);
					return id;
				}else{
					//找的到,则先要删除从数据库中删除相同的记录
					//把需要更新的对象转化成map
					updateMap = BeanToMapUtils.selectfunction(record);
					existMap  = jedis.hgetAll(key);
					//删除旧的对象
					jedis.del(key);
					//整个两个map到一个map中
					updateMap = CommonUtils.updateMap(existMap, updateMap);
					//再执行插入
					jedis.hmset(key, updateMap);
					return id ;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 批量添加,返回插入的成功的记录数
	 */
	@Override
	public <T> long insertBatch(List<T> objectList) {
		 Jedis jedis = RedisUtil.getInstance().getJedis();
		 Pipeline pipelined = jedis.pipelined();
		long count = 0;
		try{
			for( Object object : objectList){
				Map<String, String> map = BeanToMapUtils.insertfunction(object);
				pipelined.hmset(object.getClass().getSimpleName()+"-"+map.get("id"), map);
				//调用单条插入
				//this.insertAndGetId(object);
				count++;
			}
			
		}catch(Exception e){
			e.printStackTrace();
		}finally {
			pipelined.sync();
			jedis.close();
		}
		return count;
	}
	/**
	 * 根据id删除特定对象(这种就是直接根据key删除单条记录)
	 */
	@Override
	public int deleteById(Object record) {
		RedisUtil jedis =  RedisUtil.getInstance();
		Map<String,String> map =  new LinkedHashMap<>();
		//把传过来的对象转化成map
		try {
			map = BeanToMapUtils.selectfunction(record);
			String id =  map.get("id");
			if(map.get("id") != null){
				//如果传入的对象有id,执行删除
				String key  = CommonUtils.getClassName(record.getClass())+"-"+id;
				jedis.del(key);
				return 1;
			}else{
				//传入的对象没有id,无法删除
				return 0 ;
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return 0;
	}

	/**
	 * 多条件删除(可以调用多条件查询,查询玩以后得到的list,把里面的数据一个个地删除)
	 */
	public void deleteByMultipleAttribute(Object record) {
		//调用多条件查询,得到符合条件的list
		List<Object> list = new LinkedList<>();
		list = this.selectByMultipleAttribute(record);
		try{
			//循环删除
			for(Object object:list){
				//调用单条删除的方法进行删除进行删除
				this.deleteById(object);
			}
		}catch(Exception e){
			e.printStackTrace();
		}
		
	}

}
