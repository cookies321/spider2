package cn.jj.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.Validate;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;

import cn.jj.entity.Hotel;
import cn.jj.service.IDownLoadService;
import cn.jj.service.IParse;
import cn.jj.service.impl.DownLoad;
import cn.jj.service.impl.HotelParse;
import cn.jj.utils.RedisUtil;
import cn.jj.utils.ValidateUtil;

/**
 * @Description: 酒店爬虫入口
 * @author 徐仁杰
 * @date 2017年11月30日 上午9:43:59
 */
public class HotelSpiderStart {

	/**
	 * 下载接口
	 */
	public IDownLoadService downLoad;

	/**
	 * 酒店解析接口
	 */
	public IParse hotelParse;


	static ExecutorService executor= (ThreadPoolExecutor) Executors.newFixedThreadPool(10);

	/**
	 * java队列
	 */
	public static RedisUtil redis = RedisUtil.getInstance();

	//redis中存放分页链接的key
	public static final String KEY_HOTLE = "hotel";

	public IParse getHotelParse() {
		return hotelParse;
	}

	public void setHotelParse(IParse hotelParse) {
		this.hotelParse = hotelParse;
	}

	public IDownLoadService getDownLoad() {
		return downLoad;
	}

	public void setDownLoad(IDownLoadService downLoad) {
		this.downLoad = downLoad;
	}

	public HotelSpiderStart() {
		super();
	}

	/**
	 * @Description 下载url页面
	 * @author 汤玉林
	 * @date 2017年12月5日 下午1:55:44
	 * @action downLoadPage
	 * @param url
	 * @return
	 */
	public Serializable downLoadPage(String url) {
		return this.downLoad.download(url);
	}

	/**
	 * @Description 解析酒店
	 * @author 汤玉林
	 * @date 2017年12月5日 下午1:55:22
	 * @action parseHotel
	 * @param hotel
	 */
	public void parseHotel(Hotel hotel) {
		this.hotelParse.parse(hotel);
	}

	/**
	 * @Description 启动程序
	 * @author 徐仁杰
	 * @date 2017年11月30日 上午9:46:13
	 * @action startRouteSpider
	 * @return void
	 */
	public void startHotelSpider() {
		for (int i = 0; i < 5; i++) {
			new Thread(new Runnable() {
				@Override
				public void run() {

					while (true) {
						try {
						Hotel hotel = (Hotel) redis.poll(KEY_HOTLE);
						if (Objects.nonNull(hotel)) {
							//用于单个酒店的标识
							String uuid=UUID.randomUUID().toString();
							String url = hotel.getUrl();
							System.out.println(url);
							//一个url对应一个UUID
							redis.add(url, uuid);
							// 详情下载0
							String content ="";
							try {
								content = (String) downLoad.download(url);
								if(!url.contains("http://hotel.tuniu.com")&&ValidateUtil.valid(content)){
									redis.add(KEY_HOTLE, hotel);
									continue;
								}else if(content.contains("验证码")||content.contains("reload")){
									System.out.println("出现验证码，重新放入队列！");
									redis.add(KEY_HOTLE, hotel);
									continue;
								}
							} catch (Exception e) {
								redis.add(KEY_HOTLE, hotel);
								continue;
							}
							try {
								Thread.sleep(1000);
							} catch (InterruptedException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							hotel.setId(uuid);
							hotel.setContent(content);
							// 详情解析，详情解析操作中已经获取了全部酒店链接
							try {
								parseHotel(hotel);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
								redis.add(KEY_HOTLE, hotel);
								continue;
							}

						} else {
							try {
								System.out.println("沒有信息");
								Thread.sleep(2000);
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

	/**
	 * @see 1、设置下载实现类
	 * @see 2、设置解析实现类
	 * @see 3、设置缓存队列
	 * @Description 启动示例
	 * @author 徐仁杰
	 * @date 2017年11月30日 上午9:45:59
	 * @action main
	 * @return void
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {

		HotelSpiderStart spiderStart = new HotelSpiderStart();
		spiderStart.setDownLoad(new DownLoad());
		spiderStart.setHotelParse(new HotelParse());
		spiderStart.startHotelSpider();



	}
}
