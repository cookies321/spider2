package cn.jj.service.impl;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.jj.controller.RouteSpiderStart;
import cn.jj.controller.SpiderScheduler;
import cn.jj.controller.StrokeSpiderStart;
import cn.jj.dao.service.RedisService;
import cn.jj.dao.service.impl.RedisServiceImpl;
import cn.jj.entity.Params;
import cn.jj.entity.Route;
import cn.jj.entity.data.Pictureinfo;
import cn.jj.entity.data.Routeinfo;
import cn.jj.service.IParse;
import cn.jj.utils.NumUtils;
import cn.jj.utils.Param;

public class RouteParse implements IParse {

	//redis持久化接口
	private RedisService redisService=new RedisServiceImpl();

	//redis中存放params对象的key
	public static final String MISSION_PARAMS = "mission-params";

	private static final String DATASOURCE = "Ctrip";// 数据来源，英文或全拼，首字符大写，如Ctrip、Tuniu等
	private static final Integer XTYPE = 3;// 数据类型:1.景点；2.景点价格；3.行程；4 酒店; 5 机票
	private static final String CREATOR = "姚良良";// 创建者
	private static final String CREATORID = "13783985208";// 创建者ID

	@Override
	public void parse(Serializable seri) {
		try {
			Route route = (Route) seri;
			String destination=route.getCityName();
			// 驴妈妈行程解析
			if ((route.getUrl().startsWith("http://dujia.lvmama.com/")||route.getUrl().startsWith("http://www.lvmama.com/tuangou"))) {
				//获取驴妈妈行程价格的url
				//lvmamaRoutePriceUrl(route);
				//获取驴妈妈行程信息
				lvmamaRouteinfo(route, destination);
				//获取评论url
				//lvmamaCommentURL(route);
			}
			//携程行程解析
			if(route.getUrl().startsWith("http://vacations.ctrip.com/")){
				//获取携程行程详细信息 
				ctripRouteinfoAndPictureInfo(route);
				//获取驴妈妈行程价格的url
				//ctripRoutePriceUrl(route);
				//获取携程行程评论的第一页url，获取pageNum总数
				//ctripRouteCommentTotalURL(route);
			}
			//途牛行程解析
			if(route.getUrl().startsWith("http://www.tuniu.com")){
				//获取途牛行程详细信息
				tuniuRouteinfo(route);
				//获取途牛行程价格url
				//tuniuRoutePriceURL(route);
				//获取途牛行程评论url
				//tuniuRouteCommentURL(route);
			}
			//同程行程解析
			if(route.getUrl().startsWith("https://gny.ly.com")){
				//获取同程行程详细信息
				tongchengRouteinfo(route);
				//获取同程行程价格url
				tongchengRoutePriceUrl(route);
				//获取同程评论url
				tongchengRouteCommentUrl(route);
			}
			//去哪儿行程解析
			if(route.getUrl().contains("qunar")){
				//获取去哪儿行程详细信息
				qunarRouteinfo(route);
				//获取去哪儿行程价格url
				//qunarRoutePriceUrl(route);
				//获取去哪儿行程评论第一页url
				//qunarRouteCommentUrl(route);
			}
			//飞猪行程解析
			if(route.getUrl().startsWith("https://traveldetail.fliggy.com")){
				//获取飞猪行程详细信息
				feizhuRouteinfo(route);
				//获取飞猪评论url
				feizhuRouteCommentUrl(route);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//解析飞猪，获取评论url
	private void feizhuRouteCommentUrl(Route route) {
		// TODO Auto-generated method stub
		String parentUrl = route.getUrl();
		String gradenum = route.getRouteInfo().getGradenum();
		String itemno = route.getRouteInfo().getItemno();
		
		if(StringUtils.isNotBlank(gradenum)){
			
			Integer num=Integer.parseInt(gradenum);
			Integer pageNum=num%1000==0?num/1000:num/1000+1;
			
			for(int i=1 ;i<=pageNum;i++){
				Params params = new Params();
				String url="https://traveldetail.fliggy.com/async/queryRateList.do?id="+itemno+"&pageNo="+i+"&pageSize=1000";
				params.setUrl(url);
				params.setParentUrl(parentUrl);
				params.setHeader("referer", "https://traveldetail.fliggy.com/item.htm");
				params.setHttpType(Param.GET);
				params.setType(Param.FEIZHU_ROUTE_COMMENT);
				params.setDataSource(Param.FEIZHU);
				System.out.println("存入评论url");
				RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
			}
		}
		
	}
	
	/**
	 * 
	 * @Description 解析飞猪行程详情
	 * @author 赵乐
	 * @date 2018年1月25日 下午5:05:13
	 * @action feizhuRouteinfo
	 * @param @param route
	 * @return void
	 */
	private void feizhuRouteinfo(Route route) {
		// TODO Auto-generated method stub
		String parentUrl = route.getUrl();
		String content = route.getContent();
		
		Routeinfo routeInfo = route.getRouteInfo();
		
		if(StringUtils.isNotBlank(content)){
			Document document = Jsoup.parse(content);
			
			//获取图片信息
			Elements select2 = document.select("div.item-gallery>ul.item-gallery-bottom>li.item-gallery-thumb");
			System.out.println("插入图片");
			for (int i=0;i<select2.size();i++) {
				Pictureinfo pictureinfo = new Pictureinfo();
				String imgSrc = "https:"+select2.get(i).select("img.item-gallery-thumb__img").attr("src");
				pictureinfo.setId(UUID.randomUUID().toString());
				pictureinfo.setInfoid(parentUrl);
				pictureinfo.setImgurl(imgSrc);
				pictureinfo.setSort(i);
				pictureinfo.setCreatedate(new Date());
				pictureinfo.setCreator("赵乐");
				pictureinfo.setCreatorid("15736708180");
				pictureinfo.setType(3);
				redisService.insertAndGetId(pictureinfo);
			}
			
			
			/*
			//费用包含
			String expenseInclude= document.select("div#J_FeeInclude>div.txt-content.clearfix").text();
			//费用不包含
			String expenseExclude = document.select("div#J_FeeExclude>div.txt-content.clearfix").text();
			routeInfo.setExpense(expenseInclude+expenseExclude);
			
			//行程描述
			Elements select = document.select("div.daily-detail-wrapper>ul>li.single-day-wrapper");
			StringBuffer sbItineraryOutline=null;
			
			for (Element element : select) {
				String textDayTitle = element.select("div.day-title-wrapper>div.day-title").text();
				String textDetailTitle = element.select("div.day-detail-wrapper>div.day-detail-title").text();
				String textDetail = element.select("div.day-detail-wrapper>div.day-detail-desc").text();
				String text = element.text();
				sbItineraryOutline.append(text);
			}
			routeInfo.setItineraryoutline(sbItineraryOutline.toString());
			//预定须知
			String textReserveInfo = document.select("div#J_BookTips").text();
			routeInfo.setItineraryoutline(textReserveInfo);
			System.out.println("插入行程详情");
			redisService.insertAndGetId(routeInfo);*/
			
			//解析飞猪行程详情，获取价格url
			String itemno = routeInfo.getItemno();
			
			String url="https://traveldetail.fliggy.com/async/queryItemDetailAjaxInfo.do?id="+itemno;
			
			Params params = new Params();
			
			params.setUrl(url);
			params.setParentUrl(parentUrl);
			params.setRouteInfo(routeInfo);
			params.setHeader("referer", "https://traveldetail.fliggy.com/item.htm");
			params.setHttpType(Param.GET);
			params.setType(Param.FEIZHU_ROUTE_PRICE);
			params.setDataSource(Param.FEIZHU);
			System.out.println("存入价格的url");
			RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
			
		}
		
	}

	/**
	 * @Description 去哪儿行程评论第一页url获取
	 * @author 汤玉林
	 * @date 2017年12月27日 下午5:19:08
	 * @action qunarRouteCommentUrl
	 * @param route
	 */
	private void qunarRouteCommentUrl(Route route) {
		try {
			String uuid = route.getId();
			String url=route.getUrl();
			String itemNo=route.getRouteInfo().getItemno();
			if(StringUtils.isBlank(itemNo)){
				System.out.println("行程评论ulr获取失败");
			}else{
				String commentFirstUrl = "https://yntx3.package.qunar.com/user/comment/product/queryComments.json?type=all&pageNo=1&pageSize=10&productId="+itemNo+"&rateStatus=ALL";

				Params params=new Params();
				params.setUuid(uuid);
				params.setUrl(commentFirstUrl);
				params.setDataSource(Param.QUNAER);
				params.setType(Param.QUNAER_ROUTE_COMMENT_FIRST);
				params.setHttpType(Param.GET);

				RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	/**
	 * @Description 去哪儿行程价格url获取
	 * @author 汤玉林
	 * @date 2017年12月27日 下午5:12:03
	 * @action qunarRoutePriceUrl
	 * @param route
	 */
	private void qunarRoutePriceUrl(Route route) {
		try {
			String uuid = route.getId();
			String url=route.getUrl();
			String itemNo=route.getRouteInfo().getItemno();
			if(StringUtils.isBlank(itemNo)){
				System.out.println("价格url获取失败");
			}else{
				String month = new SimpleDateFormat("yyyy-MM").format(new Date());
				String priceUrl = "https://mbly1.package.qunar.com/api/calPrices.json?tuId=&pId="+itemNo+"&month="+month;

				Params params=new Params();
				params.setUuid(uuid);
				params.setUrl(priceUrl);
				params.setDataSource(Param.QUNAER);
				params.setType(Param.QUNAER_ROUTE_PRICE);
				params.setHttpType(Param.GET);

				RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

	}

	/**
	 * @Description 去哪儿行程详细信息解析
	 * @author 汤玉林
	 * @date 2017年12月27日 下午4:35:28
	 * @action qunarRouteinfo
	 * @param route
	 */
	private void qunarRouteinfo(Route route) {
		String uuid = route.getId();
		String url=route.getUrl();
		String content=route.getContent();
		//解析json
		if(StringUtils.isNotBlank(content)){
			try {
				JSONObject dataJSON=null;
				JSONObject productJSON=null;
				try {
					JSONObject pageJSON = new JSONObject(content);
					String data = pageJSON.get("data").toString();
					dataJSON = new JSONObject(data);
					String product = dataJSON.get("product").toString();
					productJSON = new JSONObject(product);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
					RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
				}
				if(productJSON!=null){
					Routeinfo routeInfo = route.getRouteInfo();//通过种子链接传过来的对象
					routeInfo.setUrl(url);
					routeInfo.setId(uuid);
					routeInfo.setDatasource("Qunaer");
					routeInfo.setCreator("姚良良");
					routeInfo.setCreatorid("13783985208");
					//routeInfo.setDestination(route.getCityName());//通过种子链接传过来的目的地
					routeInfo.setCreatedate(new Date());
					//获取产品概要
					if(!productJSON.isNull("productFeatures")){
						String productOutline = productJSON.get("productFeatures").toString();
						routeInfo.setProductoutline(productOutline);
					}
					//费用说明
					StringBuffer expense = new StringBuffer();
					if(!productJSON.isNull("costIncludeDesc")){
						String costIncludeDesc = productJSON.get("costIncludeDesc").toString();
						expense.append(costIncludeDesc);
					}
					if(!productJSON.isNull("costExcludeDesc")){
						String costExcludeDesc = productJSON.get("costExcludeDesc").toString();			
						expense.append(costExcludeDesc);
					}
					routeInfo.setExpense(expense.toString());
					//获取线路介绍
					if(!dataJSON.isNull("dailySchedules")){
						String itineraryDetails = dataJSON.get("dailySchedules").toString();
						routeInfo.setItinerarydetails(itineraryDetails);
					}
					//获取产品特色（线路特色）
					if(!productJSON.isNull("feature")){
						String productFeature = productJSON.get("feature").toString();
						routeInfo.setProductfeature(productFeature);
					}
					//获取行程概要
					if(!productJSON.isNull("arrive")){
						String itineraryOutline = productJSON.get("arrive").toString();
						routeInfo.setItineraryoutline(itineraryOutline);
					}
					//获取预定须知
					StringBuffer reserveInfo = new StringBuffer();
					if(!productJSON.isNull("attention")){
						String attention = productJSON.get("attention").toString();
						reserveInfo.append(attention);
					}
					if(!productJSON.isNull("refundDesc")){
						String refundDesc = productJSON.get("refundDesc").toString();
						reserveInfo.append(refundDesc);
					}
					if(!productJSON.isNull("tip")){
						String tip = productJSON.get("tip").toString();
						reserveInfo.append(tip);
					}
					routeInfo.setReserveinfo(reserveInfo.toString());
					
					if(StringUtils.isBlank(routeInfo.getName())){
						
						System.out.println("数据获取不完整，放回队列");
						RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
						
					}else{
						redisService.insertAndGetId(routeInfo);

						String imageInfos = productJSON.get("imageInfos").toString();
						if(StringUtils.isNotBlank(imageInfos)){
							JSONArray imageArr = new JSONArray(imageInfos);
							int n = 0;
							System.out.println("该行程的图片数量为 "+imageArr.length()+" 张!");
							for (int j = 0; j < imageArr.length(); j++){						
								try {
									String imgStr = imageArr.get(j).toString();
									JSONObject imgJSON = new JSONObject(imgStr);
									String imgUrl = imgJSON.get("url").toString();							
									if(StringUtils.isBlank(imgUrl)){
										System.out.println("图片信息获取失败!");
										continue;
									}
									//实例一个行程图片对象
									Pictureinfo pictureInfo = new Pictureinfo();
									//设置行程图片的固定属性
									pictureInfo.setId(UUID.randomUUID().toString());
									pictureInfo.setInfoid(uuid);
									pictureInfo.setCreatedate(new Date());
									pictureInfo.setCreator("姚良良");
									pictureInfo.setCreatorid("13783985208");
									pictureInfo.setType(3);
									pictureInfo.setDownload(0);
									pictureInfo.setImgurl(imgUrl);
									pictureInfo.setSort(j+1);

									redisService.insertAndGetId(pictureInfo);
								} catch (Exception e) {
									e.printStackTrace();
								}
							}	
							System.out.println("该行程共保存图片 "+imageArr.length()+"/"+n+"张!");
						}
						//放入下一级链接
						qunarRoutePriceUrl(route);
						
						qunarRouteCommentUrl(route);
					}
				}
			}catch (Exception e) {
				e.printStackTrace();
				RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
			}				
		} else {
			System.out.println("未获取到信息，重新放入队列");
			RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
		}
	}

	/**
	 * 
	 * @Description 获取同城评论url
	 * @author 赵乐
	 * @date 2017年12月26日 上午8:47:19
	 * @action tongchengRouteCommentUrl
	 * @param @param route
	 * @return void
	 */
	private void tongchengRouteCommentUrl(Route route) {
		// TODO Auto-generated method stub
		String uuid = route.getId();
		String url=route.getUrl();

		String [] urlArr = url.split("/");
		String msgStr = urlArr[urlArr.length-1];
		String pcStr = msgStr.substring(0,msgStr.indexOf("."));
		String [] pcArr = pcStr.split("p");
		String [] numArr = pcArr[pcArr.length-1].split("c");
		String productId = numArr[0];

		//拼接评论请求的URL
		String comUrl = "https://gny.ly.com/fetch/index/getCommentList?productId="+productId+"&projectId=11&page=1&pageSize=10";

		Params params=new Params();
		params.setUuid(uuid);
		params.setUrl(comUrl);
		params.setDataSource(Param.TONGCHENG);
		params.setType(Param.TONGCHENG_ROUTE_COMMENT_FIRST);
		params.setHttpType(Param.GET);

		RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
	}

	/**
	 * 
	 * @Description 获取同城行程价格url
	 * @author 赵乐
	 * @date 2017年12月25日 下午5:11:58
	 * @action tongchengRoutePriceUrl
	 * @param @param route
	 * @return void
	 */
	private void tongchengRoutePriceUrl(Route route) {
		// TODO Auto-generated method stub
		String uuid = route.getId();
		String url=route.getUrl();

		String [] urlArr = url.split("/");
		String msgStr = urlArr[urlArr.length-1];
		String pcStr = msgStr.substring(0,msgStr.indexOf("."));
		String [] pcArr = pcStr.split("p");
		String [] numArr = pcArr[pcArr.length-1].split("c");
		String productId = numArr[0];
		String cityId = numArr[numArr.length-1];


		//根据目的地的名字拼接行程价格请求URL
		String priceUrl = "https://gny.ly.com/fetch/index/getCalendarList";
		String postParams="ProductId="+productId+"&depId="+cityId+"&AdultNum=2&ChildNum=0";
		//使用Post请求方式获取行程价格的数据
		Map<String, String> map = new HashMap<String, String>();
		map.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
		//header.put("X-Requested-With", "XMLHttpRequest");
		map.put("Referer", "https://gny.ly.com/line/t1j3p97597c321.html?pos=8");

		map.put("Cookie", "td_sid=MTUxNDE5NDU4MywzMDQ2ZDM3MTIzYjJmMzhjM2FlOWVlYzA1Y2M3ZGI5MDgyOWUyMGRkMzE1NTI1NzA2MzlmZDczMzNhZmQ1NmMzLGZlZWM1OWJjZjFhM2YwMzFkN2Y2OTcxMDg5ZGJlODk2ZTViNTNkZWViZDA5MjEwZGQxMjNiZDY5N2I1YTQ4MGE=;td_did=a05e9zLkro%2BL9Km2ROphe1RzfRT3Fu9Y8rW4pRygVMKiHu41TBJ8djcWo3np%2BIpGmHtNm9TH0zlsgG0ElDlTW7j9lLTUTbALVzOFCr7ALewz9pRgTtLlOekoNpMfY87ciHeNIzAkUeQ09%2BGr3wcSITqmdd7bY5u52DwhwiUddx3wHjTjoB%2F04E6RTl7fEx3VVsbzKJTucaqUbMPLSguovVLzOry6v6%2BNu3ndoK05pmRFqvVJL7CgEyEj0Oty%2B1%2B3");
		Params params=new Params();
		params.setUuid(uuid);
		params.setUrl(priceUrl);
		params.setPostParams(postParams);
		params.setDataSource(Param.TONGCHENG);
		params.setType(Param.TONGCHENG_ROUTE_PRICE);
		params.setHttpType(Param.POST);

		RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
	}

	/**
	 * 
	 * @Description 解析同城行程信息
	 * @author 赵乐
	 * @date 2017年12月25日 下午4:29:28
	 * @action tongchengRouteinfo
	 * @param @param route
	 * @return void
	 */
	private void tongchengRouteinfo(Route route) {
		// TODO Auto-generated method stub
		String uuid=route.getId();
		String url = route.getUrl();
		String content = route.getContent();
		Routeinfo routeInfo = route.getRouteInfo();
		routeInfo.setId(uuid);
		//对页面实体进行Jsoup解析
		try {
			Document doc = Jsoup.parse(content);

			String [] urlArr = url.split("/");
			String msgStr = urlArr[urlArr.length-1];
			String pcStr = msgStr.substring(0,msgStr.indexOf("."));
			String [] pcArr = pcStr.split("p");
			String [] numArr = pcArr[pcArr.length-1].split("c");
			String productId = numArr[0];

			//获取产品编号
			Elements itemNoEle = doc.select("li#ProductInfo>div.list:nth-child(2)");
			if(!itemNoEle.isEmpty()){
				String itemNoStr = itemNoEle.text();
				String itemNo = (NumUtils.getInteger(itemNoStr)).toString();
				routeInfo.setItemno(itemNo);
			}else{

				routeInfo.setItemno(productId);
			}

			//获取出发地
			Elements departureEle = doc.select("li#ProductInfo>div.list.city_more>div.cfd_.J_cfd");
			if (!departureEle.isEmpty()) {
				String departure = departureEle.text();
				routeInfo.setDeparture(departure);
			}			

			//获取特色服务
			Elements featureServiceEle = doc.select("dl.features");
			if(!featureServiceEle.isEmpty()){
				String featureService = featureServiceEle.text();
				routeInfo.setFeatureservice(featureService);
			}

			//获取行程概要
			Elements itineraryOutlineEle = doc.select("div.less-circuit");
			if(!itineraryOutlineEle.isEmpty()){
				String itineraryOutline = itineraryOutlineEle.text();// 行程概要
				routeInfo.setItineraryoutline(itineraryOutline);
			}

			//获取产品推荐
			Elements productRecommendEle = doc.select("div.recommend");
			if(!productRecommendEle.isEmpty()){
				String productRecommend = productRecommendEle.text();
				routeInfo.setProductrecommend(productRecommend);
			}			

			//获取优惠信息
			Elements reducedPriceEle = doc.select("div#youhui");
			if(!reducedPriceEle.isEmpty()){
				String reducedPrice = reducedPriceEle.first().text();
				routeInfo.setReducedprice(reducedPrice);
			}

			//获取产品特色（产品详情）
			Elements productFeatureEle = doc.select("div#Special");
			if(!productFeatureEle.isEmpty()){
				String productFeature = productFeatureEle.text();
				routeInfo.setProductfeature(productFeature);
			}else{
				productFeatureEle = doc.select("div#ProductDetail");
				Elements recommendEle = doc.select("div#Recommend");
				StringBuffer proFeature = new StringBuffer();
				if(!productFeatureEle.isEmpty()){
					proFeature.append(productFeatureEle.text());
				}
				if(!recommendEle.isEmpty()){
					proFeature.append(recommendEle.text());
				}
				routeInfo.setProductfeature(proFeature.toString());
			}

			//获取线路介绍
			Elements itineraryDetailsEle = doc.select("div#Schedule");
			if(!itineraryDetailsEle.isEmpty()){
				String itineraryDetails = itineraryDetailsEle.html();
				routeInfo.setItinerarydetails(itineraryDetails);
			}

			//获取费用说明
			Elements expenseEle = doc.select("div#Fee");
			if(!expenseEle.isEmpty()){
				String expense = expenseEle.text();// 费用说明
				routeInfo.setExpense(expense);
			}

			//获取预定须知				
			Elements reserveInfoEle = doc.select("div#Notice");
			if(!reserveInfoEle.isEmpty()){
				String reserveInfo = reserveInfoEle.text();// 预定须知
				routeInfo.setReserveinfo(reserveInfo);
			}

			//获取行程图片集合
			Elements pictureEle = doc.select("div#Slider>ul.slider-content>li>a>img");
			if(!pictureEle.isEmpty()){
				for (int j = 0; j < pictureEle.size(); j++){						
					String imgUrl = "https:"+pictureEle.get(j).attr("src");
					if(imgUrl == null || "".equals(imgUrl)){
						System.out.println("图片信息获取失败!");
						continue;
					}
					//实例一个行程图片对象
					Pictureinfo pictureInfo = new Pictureinfo();
					//设置行程图片的固定属性
					pictureInfo.setId(UUID.randomUUID().toString());
					pictureInfo.setInfoid(uuid);
					pictureInfo.setCreatedate(new Date());
					pictureInfo.setCreator("姚良良");
					pictureInfo.setCreatorid("13783985208");
					pictureInfo.setType(3);
					pictureInfo.setDownload(0);
					pictureInfo.setImgurl(imgUrl);
					pictureInfo.setSort(j+1);
					//将HolyrobotPictureinfo对象放入集合
					redisService.insertAndGetId(pictureInfo);
				}					
			}
			redisService.insertAndGetId(routeInfo);
		} catch (Exception e) {
			e.printStackTrace();
			RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
		}

		/*String priceStr = HttpClientService.httpClientGet(priceUrl, 5, cookieMap);
		//对JSON数据进行解析
		String jsonStr = (priceStr.substring(1,priceStr.length()-1)).replaceAll("\\\\", "");
		System.out.println(jsonStr);
		JSONObject json = new JSONObject(jsonStr);
		String priceList = json.get("priceList").toString();
		JSONArray dataArr = new JSONArray(priceList);
		System.out.println(routeId+"对应的行程价钱对象共有"+dataArr.length()+"个!");
		//记录保存行程价钱的天数
		int k = 0;
		for (int i = 0; i < dataArr.length(); i++) {
			JSONObject priceDateJson = new JSONObject(dataArr.get(i).toString());
			String priceDate = priceDateJson.get("Date").toString();
			String lowestPrice = priceDateJson.get("Price").toString();
			String dayOfWeek = DateUtil.dateToWeek(priceDate);
			//创建HolyrobotRoutepriceinfo对象并设置相关属性
			HolyrobotRoutepriceinfo routepriceInfo = new HolyrobotRoutepriceinfo();
			routepriceInfo.setId(UUID.randomUUID().toString());
			routepriceInfo.setCreator("姚良良");
			routepriceInfo.setCreatorid("13783985208");
			routepriceInfo.setCreatedate(new Date());
			routepriceInfo.setRouteid(routeId);
			routepriceInfo.setPricedate(priceDate);
			routepriceInfo.setLowestprice(lowestPrice);
			routepriceInfo.setDayofweek(dayOfWeek);
			holyrobotRoutepriceinfoList.add(routepriceInfo);
			k++;
		}	
		System.out.println(routeId+" 的行程价钱共有 "+dataArr.length()+" 条,共获取"+k+"条!");
		return holyrobotRoutepriceinfoList;*/



	}

	/**
	 * @Description 途牛行程评论url获取
	 * @author 汤玉林
	 * @date 2017年12月21日 上午11:23:07
	 * @action tuniuRouteCommentURL
	 * @param route
	 */
	private void tuniuRouteCommentURL(Route route) {
		try {
			String uuid = route.getId();
			String url = route.getUrl();
			String productId=url.substring(url.lastIndexOf("/")+1, url.length());
			//获取评论总数
			String gradeNum=route.getRouteInfo().getGradenum();
			int commentCount=0;
			if(StringUtils.isNotBlank(gradeNum)){
				try {
					commentCount=Integer.valueOf(gradeNum);
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			int totalPage=commentCount%10==0?commentCount/10:commentCount/10+1;
			for (int i = 1; i <= totalPage; i++) {
				String commentURL="http://www.tuniu.com/papi/tour/comment/product?page=1&productId="+productId+"&selectedType=0&stamp=040084942428522051510819092235";
				Params params = new Params();
				params.setType(Param.TUNIU_ROUTE_COMMENT);
				params.setHttpType(Param.GET);
				params.setDataSource(Param.TUNIU);
				params.setUrl(commentURL);
				params.setUuid(uuid);
				RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}


	/**
	 * @Description 获取途牛行程评论url
	 * @author 汤玉林
	 * @date 2017年12月21日 上午10:54:09
	 * @action tuniuRouteCommentURL
	 * @param route
	 */
	private void tuniuRoutePriceURL(Route route) {
		try {
			String uuid = route.getId();
			String url = route.getUrl();
			String productId=url.substring(url.lastIndexOf("/")+1, url.length());
			String cityCode=route.getOtherInformation().trim();//目前只存了一个编号，因为三个一致，若发现不一致，可在上一个方法中重新设置
			String priceUrl="";
			//获取种子链接传过来的城市名称
			String cityName=route.getCityName();
			if("海南".equals(cityName)){
				priceUrl = "http://www.tuniu.com/tour/api/calendar?productId="+productId+"&bookCityCode="+cityCode+"&departCityCode="+cityCode+"&backCityCode="+cityCode;
			}else if("上海".equals(cityName)){
				priceUrl = "http://www.tuniu.com/tour/api/calendar?productId="+productId+"&bookCityCode="+cityCode+"&departCityCode="+cityCode+"&backCityCode="+cityCode;
			}else{
				System.err.println("------行程价格链接为空--------"+cityName);
			}
			Params params = new Params();
			params.setType(Param.TUNIU_ROUTE_PRICE);
			params.setHttpType(Param.GET);
			params.setDataSource(Param.TUNIU);
			params.setUrl(priceUrl);
			params.setUuid(uuid);

			RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @Description 途牛行程信息解析
	 * @author 汤玉林
	 * @date 2017年12月21日 上午9:17:13
	 * @action tuniuRouteinfo
	 * @param route
	 */
	private void tuniuRouteinfo(Route route) {

		String uuid = route.getId();
		String url = route.getUrl();
		//对页面实体进行Jsoup解析
		try {
			Document doc = Jsoup.parse(route.getContent());

			//创建HolyrobotRouteinfoWithBLOBs对象并设置相关属性
			Routeinfo routeInfo = new Routeinfo();
			routeInfo.setId(uuid);
			routeInfo.setUrl(url);
			routeInfo.setDatasource("Tuniu");
			routeInfo.setCreator("姚良良");
			routeInfo.setCreatorid("13783985208");
			routeInfo.setDestination(route.getCityName());//通过种子链接传过来的目的地
			routeInfo.setCreatedate(new Date());
			//获取行程的名字,跟团或自助游或半自助游
			Elements nameEle = doc.select("div.product-body.product-tour>div.product-body-inner>div.resource>h1.resource-title>strong");
			if(nameEle.isEmpty()){
				//自驾游
				nameEle = doc.select("div.wrapper_bg>div.wrapper>div.product_info>div.product_name_bar>h1");
			}	
			if(nameEle.isEmpty()){
				nameEle = doc.select("div.product-body.product-tour>div.product-body-inner>div.m-head>div.m-title>h1.title>strong");
			}
			//当地游乐
			if(nameEle.isEmpty()){
				nameEle = doc.select("div.main_top>div.top_tit>h1");
			}
			if(nameEle.isEmpty()){
				System.out.println(url+"行程的名字获取失败!==================================================");
				
				System.out.println("数据获取不完整");
				RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
			}else{
				String name = nameEle.first().text();
				System.out.println("行程的名字为  :"+name);

				routeInfo.setName(name);

				//获取行程类型
				Elements teamTypeEle = doc.select("div.resource-title-sub>i.resource-tag");
				if(teamTypeEle.isEmpty()){
					teamTypeEle = doc.select("div.subtitle>i.tag");
				}
				if(!teamTypeEle.isEmpty()){
					String teamType = teamTypeEle.text();
					routeInfo.setTeamtype(teamType);
				}else{
					teamTypeEle = doc.select("div.product_name_tips>span.icon_style_driver");
					if(!teamTypeEle.isEmpty()){
						String teamType = teamTypeEle.text();
						if(teamType != null && !"".equals(teamType)){
							routeInfo.setTeamtype(teamType);
						}else{
							String teamTypeStr = teamTypeEle.attr("class");
							System.out.println(teamTypeStr);
							if("icon_style_driver".equals(teamTypeStr)){
								teamType = "自驾游";
								System.out.println(teamType);
								routeInfo.setTeamtype(teamType);
							}
						}						
					}else{
						//当地游乐
						teamTypeEle = doc.select("div.tours-sub-info.clearfix>div.ser_sm.fl>span.style_tour");
						if(!teamTypeEle.isEmpty()){
							String teamType = "当地玩乐";
							System.out.println(teamType);
							routeInfo.setTeamtype(teamType);
						}
						
					}
				}

				//获取行程价钱
				Elements priceEle = doc.select("div.resource-section-content>span.price-quantity>span.price-number");
				if(priceEle.isEmpty()){
					priceEle = doc.select("div.price_bar>p>span.sale_price");
				}
				if(priceEle.isEmpty()){
					priceEle = doc.select("div.head-section-content>span.price-quantity>span.price-number");
				}
				if(priceEle.isEmpty()){
					priceEle = doc.select("div.new_price>p.promotion>span.price");
				}if(priceEle.isEmpty()){
					priceEle = doc.select("div.tour-price>div.local-promotion-price>span.price");
				}
				if(!priceEle.isEmpty()){
					String price = priceEle.first().text();
					System.out.println("行程的价钱为:"+price);
					routeInfo.setPrice(price);
				}

				//获取产品编号
				String itemNo = url.substring(url.lastIndexOf("/")+1, url.length());
				System.out.println("行程的编号为:"+itemNo);
				routeInfo.setItemno(itemNo);

				//获取供应商
				Elements supplierNameEle = doc.select("div.resource-title-sub>span.reource-vendor");
				if(supplierNameEle.isEmpty()){
					supplierNameEle = doc.select("div.product_name_tips>span:nth-child(3)");
				}
				if(supplierNameEle.isEmpty()){
					supplierNameEle = doc.select("div.subtitle>span.vendor");
				}
				if(supplierNameEle.isEmpty()){
					supplierNameEle = doc.select("div.tours-sub-info.clearfix>div.ser_sm.fl>span:nth-child(3)");
				}
				if(!supplierNameEle.isEmpty()){
					String supplierName = supplierNameEle.text();
					routeInfo.setSuppliername(supplierName);
					System.out.println("供应商为: "+supplierName);
				}

				//获取出发地
				Elements departureEle = doc.select("div#J_ResourceDepartCity>div.resource-city-more-label>div.resource-city-more-selected");
				if(departureEle.isEmpty()){
					departureEle = doc.select("div#J_cityList>div.J_cityItem.u-checkbox.active");
				}
				if(departureEle.isEmpty()){
					departureEle = doc.select("dd#selectDepartDate>div.select_con>p.select_result");
				}
				if(departureEle.isEmpty()){
					departureEle = doc.select("div.resource-section-item.resource-city.resource-city-depart>div.resource-section-content>div.resource-city-more>div.resource-city-more-label>div.resource-city-more-selected");
				}
				if (!departureEle.isEmpty()) {
					String departure = departureEle.text().substring(0,2);
					routeInfo.setDeparture(departure);
				}			

				//获取特色服务
				Elements featureServiceEle = doc.select("div#J_basisFeature");
				if(!featureServiceEle.isEmpty()){
					String featureService = featureServiceEle.select("script").html();
					routeInfo.setFeatureservice(featureService);
				}else{
					featureServiceEle = doc.select("div#J_ResourceFeature>div.resource-section-content>div.resource-feature-content-inner>"
							+ "div.resource-feature-list");
					if(!featureServiceEle.isEmpty()){
						String featureService = featureServiceEle.text();
						routeInfo.setFeatureservice(featureService);
					}
				}

				//获取行程概要
				Elements itineraryOutlineEle = doc.select("div#J_ResourceJourney>div.resource-section-box>div.resource-section-item>"
						+ "div.resource-section-content>div.resource-section-content-inner");
				if(!itineraryOutlineEle.isEmpty()){
					String itineraryOutline = itineraryOutlineEle.text();// 行程概要
					routeInfo.setItineraryoutline(itineraryOutline);
				}

				//获取产品推荐
				Elements productRecommendEle = doc.select("div#J_ResourceRecommend>div.resource-section-content>div.resource-recommend-content-outer>"
						+ "div.resource-recommend-content-inner");
				if(productRecommendEle.isEmpty()){
					productRecommendEle = doc.select("div.recommend>ul.recommend_list");
				}
				if(productRecommendEle.isEmpty()){
					productRecommendEle = doc.select("div.section-box-body>div.J_productDetail_recommendReason");
				}
				if(productRecommendEle.isEmpty()){
					productRecommendEle = doc.select("div.tour-desc>div.d-content.jinglituijian");
				}
				if(!productRecommendEle.isEmpty()){
					String productRecommend = productRecommendEle.text();
					routeInfo.setProductrecommend(productRecommend);
				}			

				//获取优惠信息
				Elements reducedPriceEle = doc.select("div#J_Detail>div.detail-sections>div.J_DetailFavor.detail-favor>div.section-box");
				if(reducedPriceEle.isEmpty()){
					reducedPriceEle = doc.select("div#yhhd>div.block_content>ul.favor_list");					
				}
				if(!reducedPriceEle.isEmpty()){
					String reducedPrice = reducedPriceEle.first().text();
					routeInfo.setReducedprice(reducedPrice);
				}

				//获取产品特色（产品详情）
				Elements productFeatureEle = doc.select("div#J_Detail>div.detail-sections>div.J_DetailFeature.section-box.detail-feature");
				if(productFeatureEle.isEmpty()){
					productFeatureEle = doc.select("div#cpts");
				}
				if(productFeatureEle.isEmpty()){
					productFeatureEle = doc.select("div.section-box-body>div.J_productDetail_productIntroduce");
				}
				if(!productFeatureEle.isEmpty()){
					String productFeature = productFeatureEle.text();
					routeInfo.setProductfeature(productFeature);
				}

				//获取线路介绍
				Elements itineraryDetailsEle = doc.select("div#J_Detail>div.detail-sections>div.J_DetailRoute.section-box.detail-route.detail-route4");
				if(itineraryDetailsEle.isEmpty()){
					itineraryDetailsEle = doc.select("div#cpts");
				}
				if(itineraryDetailsEle.isEmpty()){
					itineraryDetailsEle = doc.select("div.J_DetailRoute.section-box.detail-route.detail-route3");
				}
				if(itineraryDetailsEle.isEmpty()){
					//定制游
					itineraryDetailsEle = doc.select("div#tjxc");
				}
				if(!itineraryDetailsEle.isEmpty()){
					String itineraryDetails = itineraryDetailsEle.html();
					routeInfo.setItinerarydetails(itineraryDetails);
				}

				//获取费用说明
				Elements expenseEle = doc.select("div#J_Detail>div.detail-sections>div.J_DetailFee.section-box.detail-upgrade");
				if(expenseEle.isEmpty()){
					expenseEle = doc.select("div#fysm");
				}
				if(expenseEle.isEmpty()){
					expenseEle = doc.select("div.J_PkgInstruction.section-box.product-box-instruction");
				}
				if(!expenseEle.isEmpty()){
					String expense = expenseEle.text();// 费用说明
					routeInfo.setExpense(expense);
				}

				//获取预定须知				
				Elements reserveInfoEle = doc.select("div#J_Detail>div.detail-sections>div.J_DetailPolicy.section-box.detail-policy");
				if(reserveInfoEle.isEmpty()){
					reserveInfoEle = doc.select("div#ydxz");
				}
				if(reserveInfoEle.isEmpty()){
					reserveInfoEle = doc.select("div.J_PkgInstruction.section-box.product-box-instruction>div.section-box-body>div.J_pkgInstruction_reserveNotice");
				}
				if(!reserveInfoEle.isEmpty()){
					String reserveInfo = reserveInfoEle.text();// 预定须知
					routeInfo.setReserveinfo(reserveInfo);
				}

				//获取评分
				Elements gradeEle = doc.select("div.resource-count>div.resource-statisfaction>a");
				if(gradeEle.isEmpty()){
					gradeEle = doc.select("div.atisfaction_bar>p>span");
				}
				if(gradeEle.isEmpty()){
					gradeEle = doc.select("div.product_static_bar.J_ProductStatic>div.static>p.degree");
				}
				//当地游玩
				if(gradeEle.isEmpty()){
					gradeEle = doc.select("div.local-promotion-price>span.right>span");
				}
				if(!gradeEle.isEmpty()){
					String grade = gradeEle.text();
					routeInfo.setGrade(grade);
				}	

				//获取评论个数
				/*Elements gradeNumEle = doc.select("div.resource-people>div.resource-people-item:nth-child(2)>a.resource-people-number");
				if(gradeNumEle.isEmpty()){
					gradeNumEle = doc.select("div.product_static_bar.J_ProductStatic>div.criticism>p.degree");
				}
				if(!gradeNumEle.isEmpty()){
					String gradeNum = gradeNumEle.text();
					if("新品上线".equals(gradeNum)){
						routeInfo.setGradenum("0");
					}else{
						routeInfo.setGradenum(gradeNum);
						System.out.println("点评人数: "+gradeNum);
					}
					
				}else{
					gradeNumEle = doc.select("div.atisfaction_bar>p:nth-child(2)");
					if(!gradeNumEle.isEmpty()){
						String gradeNumStr = gradeNumEle.text();
						String gradeNum = NumUtils.getInteger(gradeNumStr).toString();
						
						if("新品上线".equals(gradeNum)){
							routeInfo.setGradenum("0");
						}else{
							routeInfo.setGradenum(gradeNum);
							System.out.println("点评人数: "+gradeNum);
						}
					}
				}*/
				//游客点评中获取
				String gradeNum="0";
				
				Elements gradeNumEle = doc.select("div#J_DetailTab>div.detail-tab-box.fixed>div.detail-tab-list>div[data-rel=#J_Comment]");
				if(gradeNumEle.isEmpty()){
					gradeNumEle = doc.select("ul#pkg-detail-tab-bd>li[ref=dpjl]>a");
				}
				if(gradeNumEle.isEmpty()){
					//防止不适合游客点评模板
					Elements gradeNumEle1 = doc.select("div.resource-people>div.resource-people-item:nth-child(2)>a.resource-people-number");
					if(gradeNumEle1.isEmpty()){
						gradeNumEle1 = doc.select("div.product_static_bar.J_ProductStatic>div.criticism>p.degree");
					}
					if(!gradeNumEle1.isEmpty()){
						String gradeNum1 = gradeNumEle1.text();
						if("新品上线".equals(gradeNum1)){
							routeInfo.setGradenum("0");
						}else{
							routeInfo.setGradenum(gradeNum1);
							System.out.println("点评人数: "+gradeNum1);
						}
						
					}else{
						gradeNumEle1 = doc.select("div.atisfaction_bar>p:nth-child(2)");
						if(!gradeNumEle1.isEmpty()){
							String gradeNumStr = gradeNumEle1.text();
							String gradeNum1 = NumUtils.getInteger(gradeNumStr).toString();
							
							if("新品上线".equals(gradeNum1)){
								routeInfo.setGradenum("0");
							}else{
								routeInfo.setGradenum(gradeNum1);
								System.out.println("点评人数: "+gradeNum1);
							}
						}
					}
					
				}else{
					gradeNum=NumUtils.getInteger(gradeNumEle.text())+"";
					routeInfo.setGradenum(gradeNum);
				}
				
				
				//获取出游人数
				Elements beenNumEle = doc.select("div.resource-people>div.resource-people-item:nth-child(1)>a.resource-people-number");
				if(beenNumEle.isEmpty()){
					beenNumEle = doc.select("div.product_static_bar.J_ProductStatic>div.viewer>p.degree");
				}
				if(!beenNumEle.isEmpty()){
					String beenNum = beenNumEle.first().text();
					if("新品上线".equals(beenNum)){
						beenNum="0";
					}
					routeInfo.setBeennum(beenNum);
					System.out.println("出游人数: "+beenNum);
				}else{
					beenNumEle = doc.select("div.head-section-text>span:nth-child(2)");
					if(!beenNumEle.isEmpty()){
						String beenNumStr = beenNumEle.first().text();
						String beenNum = (NumUtils.getInteger(beenNumStr)).toString();
						if("新品上线".equals(beenNum)){
							beenNum="0";
						}
						routeInfo.setBeennum(beenNum);
						System.out.println("出游人数: "+beenNum);
					}
				}
				//获取产品编号，存入route的otherInformation中
				System.out.println("url:"+url);
				String bookCityCode="";
				try {
					String script=doc.select("script").html();
					String scriptEle=script.substring(script.indexOf("bookCityCode:"), script.indexOf("backCityCode:"));
					bookCityCode=scriptEle.substring(13, scriptEle.indexOf(","));
					System.out.println("行程价格编号："+bookCityCode);
				} catch (Exception e) {
					
				}
				
				route.setOtherInformation(bookCityCode);
				route.setRouteInfo(routeInfo);
				
				if(StringUtils.isBlank(routeInfo.getName())){
					System.out.println("数据获取不完整");
					RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
				
				}else{
					redisService.insertAndGetId(routeInfo);

					//获取行程图片集合
					Elements pictureEle = doc.select("div.gallery-thumbs>div.gallery-nav-box>ul.gallery-nav-list>li.gallery-thumb>img");
					if(pictureEle.isEmpty()){
						pictureEle = doc.select("div#gallery>div.gallery_con>div.gallery_item.clearfix>div.gy-nav>div.gy-thumbs>ul.gy-thumb-list>li>a>img");
					}
					if(pictureEle.isEmpty()){
						pictureEle = doc.select("div.navs>div.thumbs>div.navs-box>ul.navs-list>li.thumb>img");
					}
					if(pictureEle.isEmpty()){
						pictureEle = doc.select("div.gallery_con>div.gallery_item.clearfix>div.gy-nav>div.gy-thumbs>ul.gy-thumb-list>li>a>img");
					}
					if(!pictureEle.isEmpty()){
						int n = 0;
						System.out.println("该行程的图片数量为 "+pictureEle.size()+" 张!");
						for (int j = 0; j < pictureEle.size(); j++){						
							String imgUrl = pictureEle.get(j).attr("src");
							if(imgUrl == null || "".equals(imgUrl)){
								System.out.println("图片信息获取失败!");
								continue;
							}
							//实例一个行程图片对象
							Pictureinfo pictureInfo = new Pictureinfo();
							//设置行程图片的固定属性
							pictureInfo.setId(UUID.randomUUID().toString());
							pictureInfo.setCreator("姚良良");
							pictureInfo.setCreatorid("13783985208");
							pictureInfo.setCreatedate(new Date());
							pictureInfo.setInfoid(uuid);
							pictureInfo.setType(3);
							pictureInfo.setDownload(0);
							pictureInfo.setImgurl(imgUrl);
							pictureInfo.setSort(j+1);

							redisService.insertAndGetId(pictureInfo);
							n++;
						}
						System.out.println("该行程已保存图片 "+pictureEle.size()+"-"+n+" 张!");
					}else{
						System.out.println("没有图片");
					}
					//放入下一级链接
					try {
						tuniuRoutePriceURL(route);
						
						tuniuRouteCommentURL(route);
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
						
			}	
		} catch (Exception e) {
			e.printStackTrace();
		}				

	}


	/**
	 * @Description 获取携程行程中的获取评论总数的url
	 * @author 汤玉林
	 * @date 2017年12月12日 下午2:32:16
	 * @action ctripRouteCommentTotalURL
	 * @param route
	 */
	private void ctripRouteCommentTotalURL(Route route) {
		try {
			//获得行程类型
			String teamType=route.getRouteInfo().getTeamtype();
			String url = route.getUrl();
			String uuId=route.getId();
			String itemNo=url.substring(url.indexOf("/p")+2, url.lastIndexOf("s")); 
			Params params=new Params();
			if("跟团游".equals(teamType) || "半自助游".equals(teamType)){
				//拼接跟团游的评论请求URL
				String commentUrl = "http://vacations.ctrip.com/bookingnext/Comment/Search?pkg="+itemNo+"&pageIndex=1";
				//route.setCommentURL(commentUrl);
				params.setType(Param.CTRIP_ROUTE_COMMENT_FIRST);
				params.setHttpType(Param.GET);
				params.setDataSource(Param.CTRIP);
				params.setUrl(commentUrl);
				params.setHeader("Referer", url);
				params.setUuid(uuId);

			}else if("自由行".equals(teamType)){
				String commentUrl = "http://online.ctrip.com/restapi/soa2/12447/json/GetCommentInfoList";
				String param="{\"version\":70400,\"platformId\":4,\"channelCode\":0,\"CommentLevel\":0,\"ProductId\":"+itemNo+",\"PageIndex\":1,\"PageSize\":5}";

				params.setType(Param.CTRIP_ROUTE_COMMENT_FIRST);
				params.setHttpType(Param.POST);
				params.setDataSource(Param.CTRIP);
				params.setUrl(commentUrl);
				params.setPostParams(param);
				params.setUuid(uuId);
				params.setHeader("Content-Type", "application/json; charset=UTF-8");
				
			}else{
				System.out.println("暂时没有此类型");
			}
			
			if(params.getHttpType()!=null){
				System.out.println("存入评论首页url");
				RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description 获取携程的价格url
	 * @author 赵乐
	 * @date 2017年12月20日 上午8:37:45
	 * @action ctripRoutePriceUrl
	 * @param @param route
	 * @return void
	 */
	private void ctripRoutePriceUrl(Route route) {
		// TODO Auto-generated method stub
		try {
			String uuId = route.getId();
			String routepriceURL = route.getRoutepriceURL();
			//将行程价格url放入redis
			Params params=new Params();
			params.setUuid(uuId);
			params.setType(Param.CTRIP_ROUTE_PRICE);
			params.setHttpType(Param.GET);
			params.setDataSource(Param.CTRIP);
			params.setUrl(routepriceURL);
			System.out.println("存入行程的价格url---"+routepriceURL);
			RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	/**
	 * 
	 * @Description 获取携程行程的详情和图片信息
	 * @author 赵乐
	 * @date 2017年12月7日 下午1:48:19
	 * @action ctripRouteinfoAndPictureInfo
	 * @param route
	 * @param destination
	 */
	public void ctripRouteinfoAndPictureInfo(Route route){
		String url=route.getUrl();
		String uuId=route.getId();
		String content = route.getContent();
		Document doc = Jsoup.parse(content);
		try {
			Routeinfo routeInfo=route.getRouteInfo();
			routeInfo.setId(uuId);
			routeInfo.setUrl(url);
			
			//获取行程的名字（作为判断是否是成功页面的标识）
			Elements selectName = doc.select("div.product_scroll_wrap.new_scroll_wrap>h1");
			if(selectName.isEmpty()){
				System.err.println("出现错误，名字未取到");
			}
			String name=selectName.isEmpty()?"":selectName.text();
			
			//获取行程的线路编号（区分同一Url中A线、B线、C线（如果没有线路区别，则为空））
			Elements routeTypeEle = doc.select("ul#js_detail_tab>li>dl.current>dt.basefix>a.inner_current");
			if(!routeTypeEle.isEmpty()){
				String routeType = routeTypeEle.text();
				routeInfo.setRoutetype(routeType);
			}else{
				System.out.println("该行程为单线路!");
			}
			//获取价格	
			String html = doc.html();
			String price="";
			String effectDate="";
			String expireDate="";
			try {
				int indexOf = html.indexOf("{\"minPrice\":");
				price = html.substring(html.indexOf("{\"minPrice\":")+12,html.indexOf(", \"effectDate\""));
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			if(!"".equals(price)){
				routeInfo.setPrice(price);
				int effectDateIndexOf = html.indexOf("effectDate");
				int expireDateIndexOf = html.indexOf("expireDate");
				//获取出发地链接中的参数日期
				effectDate=html.substring(effectDateIndexOf+13,html.indexOf("\",", effectDateIndexOf));
				expireDate=html.substring(expireDateIndexOf+13,html.indexOf("\"}", expireDateIndexOf));
			}

			//获取行程概要
			Elements itineraryOutlineEle = doc.select("div#simple_route_div");
			if(!itineraryOutlineEle.isEmpty()){
				String itineraryOutline =itineraryOutlineEle.text();// 行程概要
				routeInfo.setItineraryoutline(itineraryOutline);
			}
			//获取产品推荐
			Elements productRecommendEle = doc.select("div.product_scroll_wrap.new_scroll_wrap>div.pm_face_recommend>div.pm_recommend");
			if(productRecommendEle.isEmpty()){
				productRecommendEle = doc.select("div.product_scroll_wrap.new_scroll_wrap>div.pm_recommend");
				if(productRecommendEle.isEmpty()){
					productRecommendEle = doc.select("div.pm_face_recommend>div.pm_recommend.pm_recom_cur>ul");
				}
			}
			if(!productRecommendEle.isEmpty()){
				String productRecommend = productRecommendEle.text();
				routeInfo.setProductrecommend(productRecommend);
			}			

			//获取产品特色（产品详情）
			Elements productFeatureEle = doc.select("div#js_detail>div.product_feature");
			if(productFeatureEle.isEmpty()){
				productFeatureEle = doc.select("div#divTourSchedule>div.detail_content.first_detail_content");
			}
			if(!productFeatureEle.isEmpty()){
				String productFeature = productFeatureEle.text();
				routeInfo.setProductfeature(productFeature);
			}

			//获取产品概要
			Elements productOutlineEle = doc.select("div.abc_content>div.abc_detail_col>dl.abc_detail");
			if(!productOutlineEle.isEmpty()){
				String productOutline = productOutlineEle.html();
				routeInfo.setProductoutline(productOutline);
			}				

			//获取线路介绍
			Elements itineraryDetailsDetailEle = doc.select("div#js_detail_travelCtrip");	//获取图文模式
			Elements itineraryDetailsSimpleEle = doc.select("div#simpleJourneyBox");		//获取日历模式
			//定义一个StringBuffer用于拼接图文和日历模式
			StringBuffer itineraryDetails = new StringBuffer();
			//如果没有图文和日历模式则查找单模式
			if(itineraryDetailsDetailEle.isEmpty() && itineraryDetailsSimpleEle.isEmpty()){
				Elements itineraryDetailsEle = doc.select("div#divTourSchedule>div.detail_content:nth-child(2)");
				if(!itineraryDetailsEle.isEmpty()){
					itineraryDetails.append(itineraryDetailsEle.html());											
				}
			}else{
				//如果有图文或日历模式则对其进行拼接保存
				if(!itineraryDetailsDetailEle.isEmpty()){
					String itineraryDetailsDetail = itineraryDetailsDetailEle.html();
					itineraryDetails.append(itineraryDetailsDetail);
				}
				if(!itineraryDetailsDetailEle.isEmpty()){
					String itineraryDetailsSimple = itineraryDetailsSimpleEle.html();
					itineraryDetails.append(itineraryDetailsSimple);
				}															
			}
			routeInfo.setItinerarydetails(itineraryDetails.toString());	

			//获取评分
			Elements gradeEle = doc.select("div#js_main_price_wrap>div.comment_wrap>a.score");
			if(gradeEle.isEmpty()){
				gradeEle = doc.select("div#js-min-price-change>div.comment_wrap>a.score");
			}
			if(!gradeEle.isEmpty()){
				String gradeStr = gradeEle.text().toString();
				String grade=NumUtils.getDouble(gradeStr)*20+"";
				routeInfo.setGrade(grade);
			}	
			//获取评论个数
			Elements gradeNumEle = doc.select("div#js_main_price_wrap>div.comment_wrap>a.comment_num");
			if(gradeNumEle.isEmpty()){
				gradeNumEle = doc.select("div#js-min-price-change>div.comment_wrap>a.comment_num");
			}
			if(!gradeNumEle.isEmpty()){
				String gradeNumStr = gradeNumEle.text();
				String gradeNum = NumUtils.getInteger(gradeNumStr)+"";
				routeInfo.setGradenum(gradeNum);
				System.out.println("点评人数: "+gradeNum);
			}
			//获取出游人数
			Elements beenNumEle = doc.select("div#js_main_price_wrap>div.comment_wrap>span");
			if(beenNumEle.isEmpty()){
				beenNumEle = doc.select("span#orderPersonCount");
			}
			if(!beenNumEle.isEmpty()){
				String beenNum = NumUtils.getInteger(beenNumEle.text())+"";
				routeInfo.setBeennum(beenNum);
				System.out.println("出游人数: "+beenNum);
			}
			routeInfo.setCreatedate(new Date());
			routeInfo.setCreator(CREATOR);
			routeInfo.setCreatorid(CREATORID);
			routeInfo.setDatasource(DATASOURCE);
			
			if(StringUtils.isBlank(name) ||StringUtils.isBlank(route.getName())){
				System.out.println("数据不完整，放回队列");
				RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
			}else{
				//放入redis
				String insertAndGetId = redisService.insertAndGetId(routeInfo);
				if(StringUtils.isNotBlank(insertAndGetId)){
					
					String routepriceURL = route.getRoutepriceURL();
					//http://vacations.ctrip.com/tour-mainsite-vacations/api/product/Calendar?PRO=7193115&DepartureCity=32&SaleCity=316
					String productID="";
					String departureCity="";
					String saleCity="";
					if(!"".equals(routepriceURL)){
						try {
							productID=routepriceURL.substring(routepriceURL.indexOf("PRO=")+4,routepriceURL.indexOf("&DepartureCity"));
							departureCity=routepriceURL.substring(routepriceURL.indexOf("DepartureCity")+14,routepriceURL.indexOf("&SaleCity"));
							saleCity=routepriceURL.substring(routepriceURL.indexOf("SaleCity")+9,routepriceURL.length());
						} catch (Exception e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
					}
					//获取西城西城中的出发地
					if(!"".equals(productID)&&!"".equals(departureCity)&&!"".equals(saleCity)&&!"".equals(effectDate)&&!"".equals(expireDate)){
						Params params=new Params();

						params.setUuid(uuId);
						String departureurl="http://vacations.ctrip.com/bookingnext/CalendarV2/CalendarInfo?ProductID="+productID+"&StartCity="+departureCity+"&SalesCity="+saleCity+"&EffectDate="+effectDate+"&ExpireDate="+expireDate;
						params.setType(Param.CTRIP_ROUTE_DEPARTURE);
						params.setHttpType(Param.GET);
						params.setDataSource(Param.CTRIP);
						params.setUrl(departureurl);
						params.setHeader("Referer", url);
						RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
					}
					
					
					//获取费用说明和预定须知信息的url链接
					String hrefExpenseAndReserveInfo="http://vacations.ctrip.com/bookingnext/Product/DescriptionInfo?";
					String param="ProductID="+productID+"&StartCity="+departureCity+"&SalesCity="+saleCity;

					Params params=new Params();
					params.setUuid(uuId);
					params.setType(Param.CTRIP_ROUTE_EXPENSEANDRESERVEINFO);
					params.setHttpType(Param.POST);
					params.setDataSource(Param.CTRIP);
					params.setUrl(hrefExpenseAndReserveInfo+param);
					params.setPostParams("");
					params.setHeader("Referer", url);
					RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);

					Elements pictureEle = doc.select("div#js_photoviewer>div.attraction_photo_s>div.small_photo_wrap>ul>li>a>img");
					if(pictureEle.isEmpty()){
						pictureEle = doc.select("div#js_photoviewer>div.attraction_photo_small>div.small_photo_wrap>ul>li>a>img");
					}
					if(!pictureEle.isEmpty()){
						int n = 0;
						System.out.println("该行程的图片数量为 "+pictureEle.size()+" 张!");
						for (int j = 0; j < pictureEle.size(); j++){				
							String imgUrl = pictureEle.get(j).attr("_src");
							if(imgUrl == null || "".equals(imgUrl)){
								System.out.println("图片信息获取失败!");
								continue;
							}
							//实例一个行程图片对象
							Pictureinfo pictureInfo = new Pictureinfo();
							String pictureId=UUID.randomUUID().toString();
							//设置行程图片的固定属性
							pictureInfo.setId(pictureId);
							pictureInfo.setInfoid(uuId);
							pictureInfo.setType(3);
							pictureInfo.setDownload(0);
							pictureInfo.setImgurl(imgUrl);
							pictureInfo.setSort(j+1);
							pictureInfo.setCreatedate(new Date());
							pictureInfo.setCreator(CREATOR);
							pictureInfo.setCreatorid(CREATORID);
							//放入redis
							redisService.insertAndGetId(pictureInfo);

						}
						System.out.println("该行程已保存图片 "+pictureEle.size()+"-"+n+" 张!");
					}	
					
					//放回下一级链接
					ctripRoutePriceUrl(route);
					ctripRouteCommentTotalURL(route);
				}
				
				
			}
									
		} catch (Exception e) {
			e.printStackTrace();
			RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
		}				
	}


	/**
	 * @Description 获取驴妈妈行程评论url
	 * @author 汤玉林
	 * @date 2017年12月11日 下午3:53:39
	 * @action lvmamaCommentURL
	 * @param route
	 */
	private void lvmamaCommentURL(Route route) {
		try {
			String uuId=route.getId();
			String content = route.getContent();
			Document doc = Jsoup.parse(content);
			
			//获取评论总数
			int totalComment=0;
			//通过对象的gradenum属性获得评论总数
			Elements elements = doc.select("a#allCmt>span");
			if(elements.isEmpty()){
				//自由行
				elements = doc.select("li#taocan_Cmt>a>span");
				
			}
			totalComment=elements.isEmpty()?0:NumUtils.getInteger(elements.text());
			//产品id(通过上面解析价格的时候获取的)
			String productId = route.getOtherInformation();
			//总页数
			int totalPage=totalComment%10==0?totalComment/10:totalComment/10+1;
			System.out.println("该行程的评论页数为："+totalPage+";链接为："+route.getUrl());
			for(int i=1;i<=totalPage;i++){
				String commentUrL="http://dujia.lvmama.com/vst_front/comment/newPaginationOfComments"
						+ "?type=all&currentPage="+i+"&totalCount="+totalComment+"&placeId=&productId="+productId+"&placeIdType=&isPicture=&isBest=&isPOI=Y&isELong=N";

				Params params=new Params();
				params.setType(Param.LVMAMA_ROUTE_COMMENT);
				params.setHttpType(Param.GET);
				params.setDataSource(Param.LVMAMA);
				params.setUrl(commentUrL);
				params.setUuid(uuId);
				RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);

			}
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}



	/**
	 * 拼接驴妈妈下的行程价钱url
	 * 根据行程的URL拼接对应的行程价钱URL
	 * @param url 行程URL
	 * @return String HolyrobotRoutepriceinfo的URL访问地址
	 */
	public void lvmamaRoutePriceUrl(Route route) {
		try {
			String url=route.getUrl();
			System.out.println("url链接为："+url);
			String uuId=route.getId();
			Integer productId = 0;
			Integer startDistrictId = -1;
			if(url.indexOf("-")!=-1){
				String[] array = url.split("-");
				int size = array.length;
				try {
					if (size > 1) {
						System.out.println("正常链接");
						String productIdStr = array[0];
						productId = NumUtils.getInteger(productIdStr);
						System.out.println(productId);
						String startDistrictIdStr = array[1];
						startDistrictId = NumUtils.getInteger(startDistrictIdStr);
						System.out.println("出发地城市ID为 "+startDistrictId);
					} else {
						String productIdStr = array[0];
						productId = NumUtils.getInteger(productIdStr);
						System.out.println(productId);
						System.out.println("没有区号");
					}
				} catch (Exception e) {

				}
			}else{
				productId=Integer.valueOf(url.substring(url.lastIndexOf("/")+1, url.length()));
			}
			//行程编号,后面的评论链接需要用到

			route.setOtherInformation(productId.toString());
			String routePriceURL="http://dujia.lvmama.com/package/travelDateList.json?productId=" + productId + "&startDistrictId="
					+ startDistrictId + "&businessType=DestBu";
			//route.setRoutepriceURL(routePriceURL);

			//将行程价格url放入redis
			Params params=new Params();
			params.setType(Param.LVMAMA_ROUTE_PRICE);
			params.setHttpType(Param.GET);
			params.setDataSource(Param.LVMAMA);
			params.setUrl(routePriceURL);
			params.setUuid(uuId);
			RouteSpiderStart.queueRedis.add(MISSION_PARAMS, params);
		} catch (NumberFormatException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	/**
	 * @Description 获取lvmama行程详细信息
	 * @author 汤玉林
	 * @date 2017年12月1日 下午4:09:46
	 * @action getRouteinfo
	 * @param url 行程url
	 * @param destination 行程目的地
	 * @param doc 行程页面文档
	 * @return
	 */
	public void lvmamaRouteinfo(Route route,String destination){
		String url = route.getUrl();
		Document doc=Jsoup.parse(route.getContent());
		try {
			//实例一个行程对象
			Routeinfo routeInfo = new Routeinfo();
			//设置行程routeInfo的固定属性
			String uuId=route.getId();
			routeInfo.setId(uuId);
			routeInfo.setUrl(url);
			routeInfo.setDestination(destination);
			//获取名字
			Elements nameEle = doc.select("div.product_top_r>div.product_top_tit>h1");
			if(nameEle.isEmpty()){
				nameEle = doc.select("div.product_top.clearfix>div.product_top_r>h1.product_top_tit");
				if(nameEle.isEmpty()){
					nameEle = doc.select("h1.detail_product_tit");
					if(nameEle.isEmpty()){
						nameEle=doc.select("div.detail>div.textWrap>p.nchtitle>a");
					}
				}
			}
			if(nameEle.isEmpty()){
				System.err.println(url+"--------");
			}else{					
				String name = nameEle.first().text();
				routeInfo.setName(name);
			}
			//获取行程的线路编号（区分同一Url中A线、B线、C线（如果没有线路区别，则为空））
			Elements routeTypeEle = doc.select("div#product-travel>div.instance_box>div.clearfix>ul.instance_tab.fl>li.active:nth-child(1)");
			if(!routeTypeEle.isEmpty()){
				String routeType = routeTypeEle.text();
				routeInfo.setRoutetype(routeType);
			}
			//获取价格	
			//跟团游和半自助和当地游价格模板
			Elements priceEle = doc.select("div.product_top_price_box>dl.product_info>dd>p.product_top_price>span.price_num>dfn");
			if(priceEle.isEmpty()){
				priceEle = doc.select("span.product_price");
				if(priceEle.isEmpty()){
					//自由行价格模板
					priceEle = doc.select("div.product_top_price_box>p.product_top_price>span.price_num>dfn");
					/*if(priceEle.isEmpty()){
						priceEle = doc.select("div.nchline-price>span.nchline-price-style>em>b");
					}*/
				}
			}
			if (!priceEle.isEmpty()) {
				String priceStr = priceEle.first().text();// 驴妈妈价格
				if (StringUtils.isNoneBlank(priceStr)) {
					routeInfo.setPrice(NumUtils.getInteger(priceStr).toString());
					System.out.println("价钱为:"+priceStr);
				}
			}
			//获取产品编号
			Elements itemNoEle = doc.select("div.product_info1");
			if(itemNoEle.isEmpty()){
				itemNoEle = doc.select("p.detail_product_num>span");
			}
			String[] split = itemNoEle.text().split("：");
			if (split.length > 1) {
				String itemNo = NumUtils.getInteger(split[1]).toString();// 产品编号
				routeInfo.setItemno(itemNo);
			} else {
				String[] split2 = url.split("-");
				String itemNo = NumUtils.getInteger(split2[0]).toString();
				routeInfo.setItemno(itemNo);
			}
			//获取行程类型(跟团游、自由行或者其他)
			/*Elements teamTypeElement = doc.select("div.product-type>div.group_icon");
			if(teamTypeElement.isEmpty()){
				//判断自由行
				teamTypeElement=doc.select("div.product_top_l>span.product_top_type");
				if(teamTypeElement.isEmpty()){
					
					teamTypeElement=doc.select("div.main-detail>div.sign");
				}
			}
			String teamTypeClass=teamTypeElement.attr("class");
			String teamType="";
			if(teamTypeClass.contains("zyx")||teamTypeClass.contains("freetour")){
				teamType="自由行";
			}else if (teamTypeClass.contains("gty")){
				teamType="跟团游";
			}else if (teamTypeClass.contains("ddy")){
				teamType="当地游";
			}else if (teamTypeClass.contains("bzz")){
				teamType="半自助";
			}*/
			
			//跟团游类型和当地游适合模板
			String teamType="";
			Elements teamTypeElement = doc.select("div.detail_top_all.clearfix>p.detail_product_num>span.dpn_group");
			if(teamTypeElement.isEmpty()){
				//自由行
				teamTypeElement=doc.select("div.product_top_box.clearfix>div.product_top_l>span.product_top_type.product_type_zyx");
				if(teamTypeElement.isEmpty()){
					teamTypeElement=doc.select("div.product_top.clearfix>i.line_icon.product_zyx");
					if(!teamTypeElement.isEmpty()){
						teamType="自由行";
					}else{
						//自由行的另一种模板
						teamTypeElement=doc.select("div#tour-type");
						if(!teamTypeElement.isEmpty()){
							teamType="自由行";
						}else{
							//半自助
							teamTypeElement=doc.select("div.detail_top_all.clearfix>p.detail_product_num>span.dpn_half");
							if(!teamTypeElement.isEmpty()){
								teamType=teamTypeElement.text();
							}
						}
					}
				}else{
					teamType=teamTypeElement.text();
				}
			}else{
				teamType=teamTypeElement.text();
			}
			
			routeInfo.setTeamtype(teamType);
			//获取出发地
			/*Elements departureEle = doc.select("em.product_top_cfd>span.product_top_cfd_title");
			if (!departureEle.isEmpty()) {
				String departure = departureEle.text();
				routeInfo.setDeparture(departure);
			} */
			Elements elements = doc.select("div.product_top_booking>dl.product_info.clearfix");
			for(int i=0;i<elements.size();i++){
				String textdt = elements.get(i).select("dt").text();
				String textdd = elements.get(i).select("dd").text();
				switch (textdt) {
				case "出发城市：":
					String departure = textdd;
					routeInfo.setDeparture(departure);
					break;

				default:
					break;
				}
				
			}
			//获取行程概要
			Elements itineraryOutlineEle = doc.select("div.product_top_r>dl.product_info.clearfix.product_info_itinerary_summary>dd");
			if(itineraryOutlineEle.isEmpty()){
				System.err.println("行程概要为空");
			}
			if(!itineraryOutlineEle.isEmpty()){
				String itineraryOutline = itineraryOutlineEle.text();// 行程概要
				routeInfo.setItineraryoutline(itineraryOutline);
			}
			//获取产品推荐
			Elements productRecommendEle = doc.select("div.product_manager_recommendation>ul.pmr_content");
			if(productRecommendEle.isEmpty()){
				productRecommendEle=doc.select("div.pm-recomman-body>ul");
			}
			String productRecommend = productRecommendEle.text();
			routeInfo.setProductrecommend(productRecommend);
			//获取优惠信息
			Elements reducedPriceEle = doc.select("div.product_top_r>dl.product_info2.mt5>dd>span.tags101");
			if(reducedPriceEle.isEmpty()){
				reducedPriceEle=doc.select("dl.product_info>dd>span");
				if(reducedPriceEle.isEmpty()){
					reducedPriceEle=doc.select("div.nchline-promotion-flag>span.nchline-promotion-comment>samp");
				}
			}
			String reducedPrice = reducedPriceEle.attr("tip-content");
			routeInfo.setReducedprice(reducedPrice);
			//获取产品特色（产品详情）
			Elements productFeatureEle = doc.select("div#product-detail");
			if(!productFeatureEle.isEmpty()){
				String productFeature = productFeatureEle.text();
				routeInfo.setProductfeature(productFeature);
			}
			//获取线路介绍
			Elements itineraryDetailsEle = doc.select("div#product-travel");
			if(itineraryDetailsEle.isEmpty()){
				itineraryDetailsEle = doc.select("ul.xingcheng");
			}
			if(!itineraryDetailsEle.isEmpty()){
				String itineraryDetails = itineraryDetailsEle.html();
				routeInfo.setItinerarydetails(itineraryDetails);
			}
			//跟团游的费用说明
			Elements expenseEle = doc.select("div.product-module.pd-cost");
			if(expenseEle.isEmpty()){
				//自由行费用说明
				expenseEle = doc.select("div#product-cost");
			}
			if(!expenseEle.isEmpty()){
				String expense = expenseEle.toString();// 费用说明
				routeInfo.setExpense(expense);
			}
			//跟团游和当地游和半自助获取预定须知				
			Elements reserveInfoEle = doc.select("div.product-module.pd-notice");
			if(reserveInfoEle.isEmpty()){
				reserveInfoEle = doc.select("div#product-preorder-note");
			}
			//自由行预定须知
			if(!reserveInfoEle.isEmpty()){
				String reserveInfo = reserveInfoEle.text();// 预定须知
				routeInfo.setReserveinfo(reserveInfo);
			}
			//获取评分
			//跟团游和当地游和半自助游
			Elements gradeEle = doc.select("div.product_top_dp>div.product_top_dp_left>span");
			if(gradeEle.isEmpty()){
				//自由行评分
				gradeEle=doc.select("p.product_top_dp>span");
			}	
			String grade = gradeEle.text();
			routeInfo.setGrade(grade);
			//获取评论个数
			/*Elements gradeNumEle = doc.select("div.com-count>em>a.f60");
			if(gradeNumEle.isEmpty()){
				gradeNumEle=doc.select("ul.float_nav_list clearfix>li.product-customer-review>span.float_nav_num");
			}*/
			//去过人数
			Elements beenNumEle = doc.select("a#appraise>span>i");
			if(!(beenNumEle.isEmpty() || beenNumEle.size()<2)){
				String beenNum=beenNumEle.get(1).text();
				routeInfo.setBeennum(beenNum);
			}
			
			//跟团游和当地游和半自助游点评人数（从驴友点评中获取）
			String gradeNum ="";
			Elements gradeNumEle = doc.select("div.float-nav>div.float-nav-main>ul.float-nav-list>li[data-flag=pd-comment]>a>em");
			if(!gradeNumEle.isEmpty()){
				gradeNum=NumUtils.getInteger(StringUtils.isNotBlank(gradeNumEle.text())?gradeNumEle.text():"0").toString();
				
			}else{
				//自由行点评人数（从驴友点评中获取）
				gradeNumEle = doc.select("ul#float_nav>li[data-flag=product-customer-review]>span.float_nav_num");
				if(!gradeNumEle.isEmpty()){
					gradeNum=NumUtils.getInteger(StringUtils.isNotBlank(gradeNumEle.text())?gradeNumEle.text():"0").toString();
						
				}
			}
				
			gradeNum = NumUtils.getInteger(StringUtils.isNotBlank(gradeNumEle.text())?gradeNumEle.text():"0").toString();
			System.err.println("评论人数："+gradeNum);
			if(StringUtils.isBlank(gradeNum)){
				gradeNum="0";
			}
			routeInfo.setGradenum(gradeNum);

			routeInfo.setDatasource("Lvmama");
			routeInfo.setCreator("姚良良");
			routeInfo.setCreatorid("13783985208");
			routeInfo.setCreatedate(new Date());
			//行程对象放入route中，后面需要用到里面的属性
			route.setRouteInfo(routeInfo);
			//放入redis
			
			if(StringUtils.isBlank(routeInfo.getName()) ){
				
				System.out.println("获取行程信息不完全，放回原队列");
				RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
			}else{
				
				redisService.insertAndGetId(routeInfo);
				//获取行程图片
				Elements pictureEle = doc.select("div.img_scroll_all>div.img_scroll_box>ul.img_scroll_list>li>img");
				if(pictureEle.isEmpty()){
					pictureEle=doc.select("div.product-pic>ul>li>img");
					if(pictureEle.isEmpty()){
						pictureEle=doc.select("div.product_top_img>div.scrollImg>div.scrollImg_wrap>ul>li>img");
					}
				}
				System.out.println("该行程的图片数量为 "+pictureEle.size()+" 张!");
				for (int j = 0; j < pictureEle.size(); j++){					
					String imgUrl = pictureEle.get(j).attr("src");
					//实例一个行程图片对象
					Pictureinfo pictureInfo = new Pictureinfo();
					String id=UUID.randomUUID().toString();
					pictureInfo.setId(id);
					pictureInfo.setInfoid(uuId);
					pictureInfo.setImgurl(imgUrl);
					pictureInfo.setSort(j+1);
					//设置行程图片的固定属性
					pictureInfo.setType(3);
					pictureInfo.setDownload(0);
					pictureInfo.setCreatedate(new Date());
					pictureInfo.setCreator("姚良良");
					pictureInfo.setCreatorid("13783985208");
					//放入redis
					redisService.insertAndGetId(pictureInfo);
				}
				//插入成功之后放入价格url
				lvmamaRoutePriceUrl(route);
				//插入成功之后放入评论url
				lvmamaCommentURL(route);
			}
			
		} catch (Exception e) {
			RouteSpiderStart.queueRedis.add(RouteSpiderStart.KEY_ROUTE, route);
			e.printStackTrace();
		}	
	}

}
