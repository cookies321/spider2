   package cn.jj.controller;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;

import cn.jj.entity.Route;
import cn.jj.service.IDownLoadService;
import cn.jj.service.IParse;
import cn.jj.service.impl.DownLoad;
import cn.jj.service.impl.RouteParse; 
import cn.jj.utils.RedisUtil;
import cn.jj.utils.ValidateUtil;

/**
 * @Description: 行程爬虫入口
 * @author 徐仁杰
 * @date 2017年11月30日 上午9:43:59
 */
public class RouteSpiderStart {

	/**
	 * 下载接口
	 */
	private IDownLoadService downLoad;

	/**
	 * 行程解析接口
	 */
	private IParse routeParse;
	
	//redis 队列
	public static RedisUtil queueRedis =RedisUtil.getInstance();
	
	//redis中存放分页链接的key
	public static final String KEY_ROUTE = "route";

	public IParse getRouteParse() {
		return routeParse;
	}

	public void setRouteParse(IParse routeParse) {
		this.routeParse = routeParse;
	}

	public IDownLoadService getDownLoad() {
		return downLoad;
	}

	public void setDownLoad(IDownLoadService downLoad) {
		this.downLoad = downLoad;
	}

	public RouteSpiderStart() {
		super();
	}

	/**
	 * @Description 下载详情
	 * @author 徐仁杰
	 * @date 2017年11月30日 上午9:45:06
	 * @action downLoadPage
	 * @return Serializable
	 */
	public Serializable downLoadPage(String url) {
		return this.downLoad.download(url);
	}

	/**
	 * @Description 行程解析
	 * @author 徐仁杰
	 * @date 2017年11月30日 上午9:45:19
	 * @action parseRoute
	 * @return void
	 */
	public void parseRoute(Route route) {
		this.routeParse.parse(route);
	}
	
	/**
	 * @Description 启动程序
	 * @author 徐仁杰
	 * @date 2017年11月30日 上午9:46:13
	 * @action startRouteSpider
	 * @return void
	 */
	public void startRouteSpider() {
		for(int i = 0; i <10; i++){
			new Thread(new Runnable() {
				@Override
				public void run() {
					// TODO Auto-generated method stub
					while (true) {
						try {
							Route route = (Route) queueRedis.poll(KEY_ROUTE);
							Map<String,String> header = new HashMap<String, String>();
							if (Objects.nonNull(route)) {
								String url=route.getUrl();
								if(url.contains("touch.dujia.qunar.com")){
									header.put("Accept", "application/json,*/*");
									header.put("user-agent", "Mozilla/5.0 (Linux; U; Android 7.0; zh-CN; VIE-AL10 Build/HUAWEIVIE-AL10) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/40.0.2214.89 UCBrowser/11.4.2.936 Mobile Safari/537.36");
								}
								String uuid=UUID.randomUUID().toString();
								//存入redis景点详情的uuid
								queueRedis.set(url,uuid);
								String content="";
								try {
									content = (String) downLoad.download(url,header);
									if(ValidateUtil.valid(content)){
										queueRedis.add(KEY_ROUTE, route);
										continue;
									}else{
										if(content.contains("线路特卖")){
											continue;
										}
									}
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									queueRedis.add(KEY_ROUTE, route);
									continue;
								}
								route.setId(uuid);
								route.setContent(content);
								
								try {
									//解析详情
									parseRoute(route);
								} catch (Exception e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
									queueRedis.add(KEY_ROUTE, route);
									continue;
								}
							}else {
								try {
									Thread.sleep(2000);
									System.out.println("没有链接了");
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}
							}
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
							try {
								Thread.sleep(10000);
							} catch (InterruptedException e1) {
								// TODO Auto-generated catch block
								System.out.println("链接池，等待10s");
								e1.printStackTrace();
							}
						}
					}
					
					
				}
			}).start();
		}
	}
	
	public static void main(String[] args) {
		RouteSpiderStart spiderStart = new RouteSpiderStart();
		spiderStart.setDownLoad(new DownLoad());
		spiderStart.setRouteParse(new RouteParse());
		spiderStart.startRouteSpider();
	}
}
