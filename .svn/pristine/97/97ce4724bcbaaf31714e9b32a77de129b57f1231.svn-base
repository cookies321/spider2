package cn.jj.dao.service;

import java.util.List;

public interface RedisService {
		//多条件查询
		public <T> List<T> selectByMultipleAttribute(Object record);
		//根据id查询单个对象
		public  Object getBeaninfoById(Class clazz,String id);
		//添加一条记录,并得到id
		public String insertAndGetId(Object record);
		//添加一个list里面的记录
		public <T> long insertBatch(List<T> objectList);
		//删除一个带id的记录
		public int deleteById(Object record);
		//多条件删除
		public void deleteByMultipleAttribute(Object record);
}
