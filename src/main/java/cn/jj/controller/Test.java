package cn.jj.controller;

import java.util.Set;

import cn.jj.utils.RedisUtil;
import redis.clients.jedis.Pipeline;

/**
 * @Description: TODO
 * @author 汤玉林
 * @date 2017年12月12日 下午4:29:58 
 */
public class Test {

	int i=0;
	RedisUtil redis=RedisUtil.getInstance();
	public void test(){

		String key="Address*";
		//redis.delAll(key);
		//redis.del(key);
		Set<String> key2 = redis.key(key);
		for (String string : key2) {
			//redis.del(string);
			/*Map<String, String> map=redis.hgetAll(string);
			String gradeNum=map.get("gradenum");
			int commentNum=Integer.valueOf(gradeNum);
			i = commentNum+i;*/
			//System.out.println(key4.size());
		}
		System.out.println(i);
		System.out.println(key2.size());
	}
	
	public void test2(){
		
		String key="Commentinfo-*";
		String keypictureinfo="Pictureinfo-*";
		String keyRoute="Routeinfo-*";
		String keypriceinfo="Routepriceinfo-*";
		//String key="Room*";
		Set<String> setCommentinfo = redis.key(key);
		Set<String> setKeypictureinfo= redis.key(keypictureinfo);
		Set<String> setkeyRoute= redis.key(keyRoute);
		Set<String> setKeypriceinfo= redis.key(keypriceinfo);
		//redis.delAll(key);
		//redis.del(key);
		
		Pipeline pipelined = redis.getJedis().pipelined();
		
		for (String string : setKeypriceinfo) {
			pipelined.del(string);
		}
		pipelined.sync();
		
		System.out.println(setCommentinfo.size());
		System.out.println(setKeypictureinfo.size());
		System.out.println(setkeyRoute.size());
		System.out.println(setKeypriceinfo.size());
	}
	
	/**
	 * @Description TODO
	 * @author 汤玉林
	 * @date 2017年12月12日 下午4:29:58
	 * @action main
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		Test test = new Test();
		test.test2();
	}

}
