package cn.jj.controller;

import java.io.Serializable;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.lang3.StringUtils;

import cn.jj.entity.Params;
import cn.jj.service.IDownLoadService;
import cn.jj.service.IParse;
import cn.jj.service.impl.DownLoad;
import cn.jj.service.impl.OtherParse;
import cn.jj.utils.Param;
import cn.jj.utils.RedisUtil;
import cn.jj.utils.ValidateUtil;

/**
 * @Description: 细分链接爬虫
 * @author 徐仁杰
 * @date 2017年12月7日 下午3:30:45
 */
public class OtherSpiderStart {

	/**
	 * 从redis中取出细分链接对象所用的key
	 */
	public static final String OTHER_MISSION_KEY = "mission-params";
	/**
	 * 下载接口
	 */
	public IDownLoadService downLoad;

	/**
	 * 解析接口
	 */
	public IParse parse;

	/**
	 * 队列
	 */
	public static RedisUtil queue;

	static ExecutorService executor= Executors.newFixedThreadPool(4);

	public OtherSpiderStart() {
		
		queue = RedisUtil.getInstance();
	}
	public IDownLoadService getDownLoad() {
		return downLoad;
	}

	public void setDownLoad(IDownLoadService downLoad) {
		this.downLoad = downLoad;
	}

	public IParse getParse() {
		return parse;
	}

	public void setParse(IParse parse) {
		this.parse = parse;
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
	 * @Description 解析
	 * @author 徐仁杰
	 * @date 2017年11月30日 上午9:45:19
	 * @action parseRoute
	 * @return void
	 */
	public void parse(Params params) {
		this.parse.parse(params);
	}

	public static int count=0;

	/**
	 * @Description 启动程序
	 * @author 徐仁杰
	 * @date 2017年12月7日 下午3:33:20
	 * @action startSpider
	 * @return void
	 */
	public void startSpider() {

		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {
					while (true) {
						try {
							long currentTime= System.currentTimeMillis();
 							Params params = (Params) queue.poll(OTHER_MISSION_KEY);
							if (Objects.nonNull(params)) {
								Map<String,String> header=params.getHeader();
								Param httpTyep = params.getHttpType();
								String url = params.getUrl();
								System.out.println(url);
								String content = "";
								//Thread.sleep(1000);
								switch (httpTyep) {  
								case GET:
									try { 
										content = (String) downLoad.download(url,header);
										if(ValidateUtil.valid(content)){
											queue.add(OTHER_MISSION_KEY, params);
											continue;
										}
										System.out.println("下载耗时："+(System.currentTimeMillis()-currentTime));
									} catch (Exception e) {
										queue.add(OTHER_MISSION_KEY, params);
										continue;
									}
									break;
								case POST:
									String param = params.getPostParams();
									try {
										content = (String) downLoad.downloadByPost(url, param,header);
										if(ValidateUtil.valid(content)){
											queue.add(OTHER_MISSION_KEY, params);
											continue;
										}
									} catch (Exception e) {
										queue.add(OTHER_MISSION_KEY, params);
										continue;
									}
									break;
								default:
									break;
								}
								long currentTime2=System.currentTimeMillis();
								if (!ValidateUtil.valid(content)) {
									params.setContent(content);
									parse.parse(params);
								}
								System.out.println("解析耗时："+(System.currentTimeMillis()-currentTime2));
							}else{
								try {
									Thread.sleep(5000);
									System.out.println("队列中没有链接了");
								} catch (InterruptedException e) {
									// TODO Auto-generated catch block
									e.printStackTrace();
								}

							}
						} catch (Exception e) {
							// TODO: handle exception
						}
					}
				}
			}).start();
		}	
	}


	public void start(){
		while(true){
			executor.execute(new Runnable() {
				
				@Override
				public void run() {
					
					System.out.println(Thread.currentThread().getName()+"----111111");
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			});
			
		}
	}

	public static void main(String[] args) {

		OtherSpiderStart start = new OtherSpiderStart();
		start.setDownLoad(new DownLoad());
		start.setParse(new OtherParse());
		start.startSpider();
		//start.start();

	}

}
