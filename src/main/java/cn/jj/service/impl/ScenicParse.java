package cn.jj.service.impl;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.eclipse.jetty.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.jj.dao.service.RedisService;
import cn.jj.dao.service.impl.RedisServiceImpl;
import cn.jj.controller.RouteSpiderStart;
import cn.jj.controller.ScenicSpiderStart;
import cn.jj.entity.Params;
import cn.jj.entity.Scenic;
import cn.jj.entity.data.Addressinfo;
import cn.jj.entity.data.Pictureinfo;
import cn.jj.entity.data.Sceinfo;
import cn.jj.entity.data.Scepriceinfo;
import cn.jj.service.IParse;
import cn.jj.utils.NumUtils;
import cn.jj.utils.Param;

public class ScenicParse implements IParse {

	//redis中存放params对象的key
	public static final String MISSION_PARAMS = "mission-params";

	//redis持久化接口
	private RedisService redisService=new RedisServiceImpl();

	@Override
	public void parse(Serializable seri) {
		try {
			Scenic scenic = (Scenic) seri;
			// 驴妈妈门票解析
			if (scenic.getUrl().startsWith("http://ticket.lvmama.com/scenic-")) {
				//驴妈妈门票详情
				lvmamaScenicParse(scenic);
				//驴妈妈门票评论url
				lvmamaScenicCommentURL(scenic);

			}
			//携程门票解析
			if(scenic.getUrl().startsWith("http://piao.ctrip.com/dest")){
				//获得携程门票详情
				ctripScenicParse(scenic);
				//获得携程门票详情中的图片链接
				//ctripScenicPictureURL(scenic);
				//获得携程门票中的评论url
				//ctripScenicCommentURL(scenic);
			}
			//途牛门票解析
			if(scenic.getUrl().startsWith("http://menpiao.tuniu.com")){
				//途牛门票详情解析,评论url也在里面
				tuniuScenicParse(scenic);
			}
			//同程门票解析
			if(scenic.getUrl().startsWith("https://www.ly.com")){
				//解析同程门票详情
				tongChengScenicParse(scenic);
				//获取同程门票价格url
				tongChengScenicPriceURL(scenic);
			}
			//去哪儿门票解析
			if(scenic.getUrl().startsWith("http://piao.qunar.com")){
				//去哪儿门票详情解析
				qunarScenicParse(scenic);
				//去哪儿门票评论url获取
				qunarScenicCommentURL(scenic);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @Description 去哪儿门票评论url获取
	 * @author 汤玉林
	 * @date 2017年12月26日 下午5:37:52
	 * @action qunarScenicCommentURL
	 * @param scenic
	 */
	private void qunarScenicCommentURL(Scenic scenic) {
		String uuid = scenic.getId();
		String sightId = scenic.getOtherinformation();
		String gradeNum = scenic.getSceinfo().getGradenum();
		int commentCount=0;
		if(StringUtils.isNotBlank(gradeNum)){
			commentCount=Integer.valueOf(gradeNum);
		}
		int totalPage = commentCount%1000==0?commentCount/1000:commentCount/1000+1;
		for (int i = 1; i <= totalPage; i++) {
			String commentUrl = "http://piao.qunar.com/ticket/detailLight/sightCommentList.json?sightId="
					+ sightId + "&index="+i+"&page=" + i  + "&pageSize=1000&tagType=0";

			Params params=new Params();
			params.setUuid(uuid);
			params.setUrl(commentUrl);
			params.setDataSource(Param.QUNAER);
			params.setHttpType(Param.GET);
			params.setType(Param.QUNAER_SCENIC_COMMENT);

			ScenicSpiderStart.redis.add(MISSION_PARAMS, params);

		}

	}

	/**
	 * @Description 去哪儿门票详情解析
	 * @author 汤玉林
	 * @date 2017年12月26日 下午5:15:26
	 * @action qunarScenicParse
	 * @param scenic
	 */
	private void qunarScenicParse(Scenic scenic) {
		String url = scenic.getUrl();
		String uuid = scenic.getId();

		//开始解析详情
		Document doc = Jsoup.parse(scenic.getContent());
		Sceinfo sceInfo =scenic.getSceinfo();//从种子链接那边传过来的值
		sceInfo.setUrl(url);
		sceInfo.setId(uuid);
		try {
			//获取门票的评论id，用于后面评论url获取
			Elements sightidEle = doc.select("div#mp-tickets");
			String sightId=sightidEle.attr("data-sightid");
			scenic.setOtherinformation(sightId);//放入otherinformation中，获取评论url需要用得到

			// 获取景点介绍信息
			Elements introductionEle = doc.select("div#mp-charact>div>div.mp-charact-intro>div.mp-charact-desc");
			String introduction = introductionEle.isEmpty() ? "" : introductionEle.text();
			sceInfo.setIntroduction(introduction);
			// 开放时间
			Elements openTimeEle = doc.select("div#mp-charact>div>div.mp-charact-time>div.mp-charact-content>div.mp-charact-desc");
			String openTime = openTimeEle.isEmpty() ? "" : openTimeEle.text();
			sceInfo.setOpentime(openTime);
			// 景点评分
			Elements gradeEle = doc.select("span#mp-description-commentscore>span");
			String grade = gradeEle.isEmpty() ? "" : gradeEle.text();
			sceInfo.setGrade(grade);
			// 景点评分个数
			Elements gradeNumEle = doc.select("div.mp-description-comments>span.mp-description-commentCount>a");
			String gradeNum = gradeNumEle.isEmpty() ? "" : (NumUtils.getInteger(gradeNumEle.text())).toString();
			
			sceInfo.setGradenum(gradeNum);
			sceInfo.setDatasource("Qunaer");
			sceInfo.setCreatedate(new Date());
			sceInfo.setCreator("姚良良");
			sceInfo.setDataType("2");
			sceInfo.setCreatorid("13783985208");

			scenic.setSceinfo(sceInfo);

			redisService.insertAndGetId(sceInfo);
			
			
			Elements  priceEle = doc.select("div.mp-littletips-item");
			if(priceEle.size()>0){
				for (Element element : priceEle) {
					String itemtitle =element.select("div.mp-littletips-itemtitle").text();
					String itemdesc = element.select("div.mp-littletips-desc").html();
					Scepriceinfo sceprice = new Scepriceinfo();
					sceprice.setId(UUID.randomUUID().toString());
					sceprice.setScenicid(uuid);
					sceprice.setPricetype(itemtitle);
					sceprice.setPriceitem(itemdesc);
					sceprice.setCreatedate(new Date());
					sceprice.setCreator("汤玉林");
					sceprice.setCreatorid("tyl13564205515");
					switch (itemtitle) {
					case "免票政策":
						redisService.insertAndGetId(sceprice);
						break;
					case "优惠政策":
						redisService.insertAndGetId(sceprice);
						break;
					case "特惠政策":
						redisService.insertAndGetId(sceprice);
						break;
					default:
						break;
					}
				}
				
			}

			String cityName = scenic.getCityName();
			Addressinfo addressInfo = new Addressinfo();
			addressInfo.setId(UUID.randomUUID().toString());
			addressInfo.setInfoid(uuid);
			addressInfo.setProvince(cityName);
			addressInfo.setCity(cityName);
			addressInfo.setDetailaddress(sceInfo.getAddress());
			addressInfo.setCountry("中国");
			addressInfo.setType(1);
			addressInfo.setCreatedate(new Date());
			addressInfo.setCreator("姚良良");
			addressInfo.setCreatorid("13783985208");
			redisService.insertAndGetId(addressInfo);

			// 景点图片
			Elements pictureEle = doc.select("div#mp-slider-content>div.mp-description-image>img");
			if (!pictureEle.isEmpty()) {
				System.out.println("图片的数目" + pictureEle.size());
				int n = 0;
				for (int j = 1; j <= pictureEle.size(); j++) {
					Pictureinfo pictureinfo = new Pictureinfo();
					String pictureurl = pictureEle.attr("src");
					String uurid = UUID.randomUUID().toString();
					pictureinfo.setId(uurid);
					pictureinfo.setInfoid(uuid);
					pictureinfo.setImgurl(pictureurl);
					pictureinfo.setSort(j);
					pictureinfo.setType(1);
					pictureinfo.setDownload(0);
					pictureinfo.setCreatedate(new Date());
					pictureinfo.setCreator("姚良良");
					pictureinfo.setCreatorid("13783985208");

					redisService.insertAndGetId(pictureinfo);
					n++;
				}
				System.out.println("景点已保存图片 " + pictureEle.size() + "-" + n + " 张!");
			}

			// 景点价格模块
			Elements ticketEle = doc.select("div#mp-tickets>div.mp-tickettype");
			if (!ticketEle.isEmpty()) {
				System.out.println(uuid + "景点的景点价格模块共有" + ticketEle.size() + "个!");
				for (Element element : ticketEle) {
					try {
						// 价格类型
						Elements priceTypeEle = element.select(
								"div.mp-tickettype-head>div.mp-tickettype-select>h3.mp-tickettype-overbtn>span");
						String priceType = priceTypeEle.isEmpty() ? "" : priceTypeEle.text();
						// 价格类型模块
						Elements priceMsgEles = element.select("div.mp-tickettype-group>div.mp-group-head.clrfix");
						if (priceMsgEles.isEmpty()) {
							System.out.println("该价格类型里的信息获取失败!");
							continue;
						}
						System.out.println(uuid + "的景点价格共有" + priceMsgEles.size() + "个!");
						for (Element priceMsgEle : priceMsgEles) {
							try {
								Scepriceinfo priceInfo = new Scepriceinfo();
								priceInfo.setPricetype(priceType);
								// 价格条目
								Elements priceItemEle = priceMsgEle.select(
										"div.mp-group-titlemain.mp-group-morepricetitle>div.mp-group-titleouter>h3.mp-group-title>span>span");
								if (priceMsgEles.isEmpty()) {
									System.out.println("该价格条目获取失败!");
									continue;
								}
								String priceItem = priceItemEle.text();
								priceInfo.setPriceitem(priceItem);
								// 销售条件
								Elements saleConditionEle = priceMsgEle.select(
										"div.mp-group-titlemain.mp-group-morepricetitle>div.mp-group-titleouter>p.mp-group-subtitle");
								String saleCondition = saleConditionEle.isEmpty() ? "" : saleConditionEle.text();
								priceInfo.setSalecondition(saleCondition);
								// 销售价
								Elements salePriceEle = priceMsgEle.select(
										"div.mp-group-price>div.mp-price-outter>div.mp-price-inner>span.mp-group-supplierprice>em>strong");
								String salePrice = salePriceEle.isEmpty() ? "" : salePriceEle.text();
								priceInfo.setSaleprice(salePrice);
								priceInfo.setId(UUID.randomUUID().toString());
								priceInfo.setScenicid(uuid);
								priceInfo.setCreatedate(new Date());
								priceInfo.setCreator("姚良良");
								priceInfo.setCreatorid("13783985208");

								redisService.insertAndGetId(priceInfo);

								System.out.println(uuid + "的景点价格已获得" + priceMsgEles.size());
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
						System.out.println(uuid + "景点的景点价格模块已经获取了" + ticketEle.size());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				System.out.println(uuid + "景点的景点价格模块共获取了" + ticketEle.size());
			}
		} catch (Exception e) {
			e.printStackTrace();
			ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, scenic);
		}
	}


	/**
	 * @Description 获取同程门票价格url
	 * @author 汤玉林
	 * @date 2017年12月26日 上午11:00:13
	 * @action tongChengScenicPriceURL
	 * @param scenic
	 */
	private void tongChengScenicPriceURL(Scenic scenic) {
		String url = scenic.getUrl();
		String uuid = scenic.getId();
		//门票编号
		String sid = url.substring(url.lastIndexOf("_")+1, url.lastIndexOf("html")-1);
		String priceUrl = "https://www.ly.com/scenery/AjaxHelper/SceneryPriceFrame.aspx?action=GETNEWFRAMEFORLIST&ids="+sid;
		Params params = new Params();
		params.setUuid(uuid);
		params.setUrl(priceUrl);
		params.setType(Param.TONGCHENG_SCENIC_PRICE);
		params.setDataSource(Param.TONGCHENG);
		params.setHttpType(Param.GET);

		ScenicSpiderStart.redis.add(MISSION_PARAMS, params);
	}

	/**
	 * @Description 同程门票详情解析
	 * @author 汤玉林
	 * @date 2017年12月25日 下午4:30:40
	 * @action tongChengScenicParse
	 * @param scenic
	 */
	private void tongChengScenicParse(Scenic scenic) {
		String url = scenic.getUrl();
		System.out.println(url);
		String uuid = scenic.getId();
		//门票编号
		String sid = url.substring(url.lastIndexOf("_")+1, url.lastIndexOf("html")-1);
		Document doc=Jsoup.parse(scenic.getContent());
		Sceinfo sceInfo = new Sceinfo();
		sceInfo.setUrl(url);
		sceInfo.setId(uuid);
		try {
			//获取景点图片
			Elements img_wElements = doc.select("div.info_l>div.img_w>img");
			if(!img_wElements.isEmpty()){
				String imgUrl = img_wElements.first().attr("nsrc");

				Pictureinfo holyrobotPictureinfo = new Pictureinfo();
				holyrobotPictureinfo.setInfoid(uuid);
				holyrobotPictureinfo.setId(UUID.randomUUID().toString());
				holyrobotPictureinfo.setImgurl("https://www.ly.com"+imgUrl);
				holyrobotPictureinfo.setSort(0);
				holyrobotPictureinfo.setType(2);
				holyrobotPictureinfo.setDownload(0);
				holyrobotPictureinfo.setCreatedate(new Date());
				holyrobotPictureinfo.setCreator("陈文奇");
				holyrobotPictureinfo.setCreatorid("chenwenqi-13263625152");

				redisService.insertAndGetId(holyrobotPictureinfo);
			}
			
			//循环图片
			Elements img_ulElements = doc.select("div.img_s_ul>ul>li");
			System.out.println("图片数："+img_ulElements.size());
			for(int i = 0 ;i< img_ulElements.size()-1;i++){
				Pictureinfo holyrobotPictureinfoi = new Pictureinfo();
				String imgUrl = img_ulElements.get(i).select("img").attr("nsrc");

				holyrobotPictureinfoi.setInfoid(uuid);
				holyrobotPictureinfoi.setId(UUID.randomUUID().toString());
				holyrobotPictureinfoi.setImgurl(imgUrl);
				holyrobotPictureinfoi.setSort(0);
				holyrobotPictureinfoi.setType(2);
				holyrobotPictureinfoi.setDownload(0);
				holyrobotPictureinfoi.setCreatedate(new Date());
				holyrobotPictureinfoi.setCreator("陈文奇");
				holyrobotPictureinfoi.setCreatorid("chenwenqi-13263625152");

				redisService.insertAndGetId(holyrobotPictureinfoi);
			}

			//获取景点名称
			Elements nameElements = doc.select("div.info_r>h3.s_name");
			sceInfo.setName(nameElements.first().text());

			//景点星级
			Elements starEle = doc.select("div.info_r>h3.s_name>span");
			String star="";
			if(!starEle.isEmpty()){
				star=starEle.text();
			}
			sceInfo.setStarlevel(star);
			String address="";
			//获取景点名称
			Elements comElements = doc.select("div.info_r>p.s_com");
			for(Element element :comElements){
				String leftKey =  element.text().substring(0, 4);
				String leftValue = element.select("span").text();
				switch (leftKey) {
				case "景点地址":
					// 景点地址
					address = leftValue;
					sceInfo.setAddress(address);
					break;
				case "开放时间":
					// 开放时间
					String openTime = doc.select("div.s-tShow").text();
					sceInfo.setOpentime(openTime);
					break;
				case "服务保障":
					// 景点类型
					String service = leftValue;
					sceInfo.setServicecommitment(service);
					break;
				default:
					break;
				}
			}

			//获取景点参考价格
			Elements spbElements = doc.select("div.s_price>span.s_p_b>s");
			sceInfo.setReferprice(spbElements.text());
			//获取景点简介
			Elements fconElements = doc.select("div.inf-f-con");
			sceInfo.setIntroduction(fconElements.text());
			//景点经纬度
			Elements mapLng = doc.select("input#mapLng");
			String lng="";
			if(!mapLng.isEmpty()){
				lng=mapLng.attr("value");
			}
			sceInfo.setLongitude(lng);
			Elements maplat = doc.select("input#mapLat");
			String lat="";
			if(!maplat.isEmpty()){
				lat=maplat.attr("value");
			}
			sceInfo.setLatitude(lat);


			System.out.println(nameElements.first().text()+"\n"+star+"\n"+spbElements.text()+"\n"+lat+";"+lng+"\n");

			sceInfo.setDataType("2");
			sceInfo.setCreatedate(new Date());
			sceInfo.setCreator("陈文奇");
			sceInfo.setCreatorid("chenwenqi-13263625152");
			sceInfo.setDatasource("Tongcheng");

			redisService.insertAndGetId(sceInfo);

			String cityName=scenic.getCityName();

			Addressinfo addressInfo=new Addressinfo();
			addressInfo.setId(UUID.randomUUID().toString());
			addressInfo.setInfoid(uuid);
			addressInfo.setProvince(cityName);
			addressInfo.setCity(cityName);
			addressInfo.setType(1);
			addressInfo.setDetailaddress(address);
			addressInfo.setCountry("中国");
			addressInfo.setCreatedate(new Date());
			addressInfo.setCreator("陈文奇");
			addressInfo.setCreatorid("chenwenqi-13263625152");
			redisService.insertAndGetId(addressInfo);

			//获取评论第一页的数据,用于获取评分人数和评分
			String firstCommentURL="https://www.ly.com/scenery/AjaxHelper/DianPingAjax.aspx?action=GetDianPingList&pageSize=20&sid="+sid;
			Params params = new Params();
			params.setUuid(uuid);
			params.setUrl(firstCommentURL);
			params.setType(Param.TONGCHENG_SCENIC_COMMENT_FIRST);
			params.setDataSource(Param.TONGCHENG);
			params.setHttpType(Param.GET);
			
			ScenicSpiderStart.redis.add(MISSION_PARAMS, params);
			
			//获取页面的优惠政策信息
			String bookKnowsNewURL = "https://www.ly.com/scenery/AjaxHelper/SceneryPriceFrame.aspx?action=GetSceneryBookKnowsNew&id="+sid;
			Params params2 = new Params();
			params2.setUuid(uuid);
			params2.setUrl(bookKnowsNewURL);
			params2.setType(Param.TONGCHENG_SCENIC_BOOKNOWNEW);
			params2.setDataSource(Param.TONGCHENG);
			params2.setHttpType(Param.GET);
			
			ScenicSpiderStart.redis.add(MISSION_PARAMS, params2);
		
		} catch (Exception e) {
			e.printStackTrace();
			ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, scenic);
		}
	}


	/**
	 * @Description 途牛门票详情解析
	 * @author 汤玉林
	 * @date 2017年12月20日 下午4:26:05
	 * @action tuniuScenicParse
	 * @param scenic
	 */
	private void tuniuScenicParse(Scenic scenic) {

		String uuid = scenic.getId();
		String url = scenic.getUrl();
		try {
			Document document=Jsoup.parse(scenic.getContent());
			//爬取景点详情信息
			Sceinfo holyrobotSceinfo = new Sceinfo();
			holyrobotSceinfo.setId(uuid);
			holyrobotSceinfo.setUrl(url);
			//景点名称
			Elements nameEle=document.select("div.v2_ticket_proinf>div.v2_tp_text>div.v2_ct_title");
			String name=nameEle.get(0).ownText();
			holyrobotSceinfo.setName(name);
			//景点星级(门票不应该有星级，这个字段可以不要)
			Elements starEle=document.select("div.v2_ticket_proinf>div.v2_tp_text>div.v2_ct_title>span.v2_ct_lable");
			String star=starEle.text();
			//holyrobotSceinfo.setStarlevel(star);
			//景点地址
			Elements addressEle=document.select("div.v2_ticket_proinf>div.v2_tp_text>p.v2_detail_address>span");
			String address="";
			if(addressEle.size()>0){
				address=addressEle.get(0).text();
			}
			holyrobotSceinfo.setAddress(address);
			//景点开放时间
			Elements opentimeEle=document.select("div.v2_ticket_proinf>div.v2_tp_text>div.v2_open_time>p>span.con");
			String opentime=opentimeEle.text();
			holyrobotSceinfo.setOpentime(opentime);
			//景点价格
			Elements priceEle=document.select("div.v2_ticket_proinf>div.v2_tp_text>div.v2-price>span.v2-money");
			String price = priceEle.text();
			holyrobotSceinfo.setReferprice(price);
			//景点评分和评论人数
			Elements gradeEle=document.select("div.v2_ticket_proinf>div.v2_tp_text>div.v2_tp_btm>p.v2_tp_sat>span");
			String grade="";
			String gradeNum="";
			if(gradeEle.size()>0){
				grade=gradeEle.get(0).text();
				gradeNum=gradeEle.get(1).text();
			}
			holyrobotSceinfo.setGrade(grade);
			holyrobotSceinfo.setGradenum(gradeNum);
			//景点介绍
			String scenicIntroduction=document.select("div.v2_di.detail_infor").text();
			holyrobotSceinfo.setIntroduction(scenicIntroduction);
			//经纬度
			String script=document.select("script").html();
			String lng="";
			String lat="";
			try {
				if(script.indexOf("GuideMap")>-1){
					String guideMap=script.substring(script.indexOf("GuideMap"));
					String[] guideMapSplit=guideMap.substring(guideMap.indexOf("(")+1, guideMap.indexOf(")")).split(",");
					lng=guideMapSplit[4].replace("'", "");
					lat=guideMapSplit[3].replace("'", "");
				}
			} catch (Exception e) {
				e.printStackTrace();
				ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, scenic);
			}
			holyrobotSceinfo.setLongitude(lng);
			holyrobotSceinfo.setLatitude(lat);
			//服务承诺
			Elements promiseElement=document.select("div.v2_ticket_proinf.clearfix>div.v2_tp_text>p.v2_tp_promise>span.tp_tips");
			String promise=promiseElement.text();
			holyrobotSceinfo.setServicecommitment(promise);

			
			holyrobotSceinfo.setDataType("2");
			holyrobotSceinfo.setDatasource("Tuniu");
			holyrobotSceinfo.setCreator("徐仁杰");
			holyrobotSceinfo.setCreatorid("xurenjie-13621935220");
			holyrobotSceinfo.setCreatedate(new Date());
			
			if(StringUtils.isBlank(holyrobotSceinfo.getName())){
				System.out.println("数据不完整放回队列");
				ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, scenic);
				
			}else{
				redisService.insertAndGetId(holyrobotSceinfo);
				
				//四级地址表信息
				Elements select = document.select("div.v2_search_nav>p.v2_crumbs>a");
				String province="";
				String city="";
				if(select.size()>2){
					String provinceStr = select.get(2).text();
					province=provinceStr.substring(0,provinceStr.indexOf("景点门票"));
				}
				if("上海".equals(province)){
					city=province;
				}
				if("海南".equals(province)){
					if(select.size()>3){
						String cityStr = select.get(3).text();
						city=cityStr.substring(0,cityStr.indexOf("景点门票"));
					}else{
						city="海南";
					}
					
				}

				Addressinfo addressInfo=new Addressinfo();
				addressInfo.setId(UUID.randomUUID().toString());
				addressInfo.setInfoid(uuid);
				addressInfo.setProvince(province);
				addressInfo.setCity(city);
				addressInfo.setDetailaddress(address);
				addressInfo.setCountry("中国");
				addressInfo.setCreatedate(new Date());
				addressInfo.setCreator("徐仁杰");
				addressInfo.setCreatorid("xurenjie-13621935220");
				redisService.insertAndGetId(addressInfo);

				//景点图片
				Elements picElements=document.select("div.rg-thumbs>div.es-carousel-wrapper>div.es-carousel>ul>li");
				int count=0;
				for(Element element:picElements){
					String picAddress=element.select("a>img").attr("data-large");
					Pictureinfo holyrobotPictureinfo = new Pictureinfo();
					holyrobotPictureinfo.setId(UUID.randomUUID().toString());
					holyrobotPictureinfo.setInfoid(uuid);
					holyrobotPictureinfo.setSort(count++);
					holyrobotPictureinfo.setType(2);
					holyrobotPictureinfo.setDownload(0);
					holyrobotPictureinfo.setImgurl(picAddress);
					holyrobotPictureinfo.setCreatedate(new Date());
					holyrobotPictureinfo.setCreator("tyl");
					holyrobotPictureinfo.setCreatorid("tyl13564205515");

					redisService.insertAndGetId(holyrobotPictureinfo);
				}

				//门票评论url
				String scenicId=url.substring(url.lastIndexOf("=")+1, url.length());

				if(StringUtils.isBlank(gradeNum)){
					gradeNum="0";
				}
				int commentCount=Integer.valueOf(gradeNum);
				int totalPage=commentCount%100==0?commentCount/100:commentCount/100+1;
				for(int i=1;i<=totalPage;i++){
					String commentURL="http://menpiao.tuniu.com/tn?r=ticket/scenic/newRemarkList&currentPage="+i+"&scenicId%5B%5D="+scenicId+"&pageLimit=100";
					Params params=new Params();
					params.setUuid(uuid);
					params.setUrl(commentURL);
					params.setType(Param.TUNIU_SCENIC_COMMENT);
					params.setDataSource(Param.TUNIU);
					params.setHttpType(Param.GET);

					ScenicSpiderStart.redis.add(MISSION_PARAMS, params);
				}
				//门票预定须知
				Elements select2 = document.select("div.detail_infor>div.pro_man_recom>div.order_detail_imfor>dl.clearfix ");
				for (Element element : select2) {
					Elements select3 = element.select("dt.clearfix");
					Elements select4 = element.select("dd");
					String type=select3.text();
					String item=select4.toString().replaceAll("<dd>", "").replaceAll("</dd>", "").replaceAll("<div>", "").replaceAll("</div>", "");
					switch (type) {
					case "特殊人群政策：":
						Scepriceinfo holyrobotScepriceinfo = new Scepriceinfo();
						holyrobotScepriceinfo.setId(UUID.randomUUID().toString());
						holyrobotScepriceinfo.setScenicid(uuid);
						holyrobotScepriceinfo.setUrlid(url);
						holyrobotScepriceinfo.setCreator("徐仁杰");
						holyrobotScepriceinfo.setCreatorid("xurenjie-13621935220");
						holyrobotScepriceinfo.setCreatedate(new Date());
						
						holyrobotScepriceinfo.setPricetype("特殊人群政策");
						holyrobotScepriceinfo.setPriceitem(item);
						
						redisService.insertAndGetId(holyrobotScepriceinfo);
						
						break;

					default:
						break;
					}
					
				}
				
				
				//门票价格列表
				String priceType="";
				Elements priceElement=document.select("div.v2_line_box.line_box>div.line_cont>ul.content>li.lc_title");
				//其中priceElement中第一个元素不是所要的
				for(int i=0;i<priceElement.size();i++){
					if(priceElement.get(i).attr("class").contains("v2_pro_title")){
						priceType=priceElement.get(i).select("div.lct>p.ticket_type").text();
						priceElement.remove(i);
					}
					Element priceInfo=priceElement.get(i);
					Scepriceinfo holyrobotScepriceinfo = new Scepriceinfo();
					//价格类型
					holyrobotScepriceinfo.setPricetype(priceType);
					//价格条目
					String priceitem=priceInfo.select("div.lct.clearfix>p.l_name.name_color>a").attr("title");
					holyrobotScepriceinfo.setPriceitem(priceitem);
					//预定时间
					String saleCondition=priceInfo.select("div.lct.clearfix>p.l_time.v2_product_com").text();
					holyrobotScepriceinfo.setSalecondition(saleCondition);
					//市场价
					String marketingPrice=priceInfo.select("div.lct.clearfix>p.l_g_price.g_price_color").text();
					holyrobotScepriceinfo.setMarketingprice(marketingPrice);
					//途牛价
					String salePrice=priceInfo.select("div.lct.clearfix>p.l_price.price_color").text();
					holyrobotScepriceinfo.setSaleprice(salePrice);
					//优惠信息
					Elements ticket_detail=priceInfo.select("div.yhhd_detail>ul.yhhd_table>li");
					String discountInfo="";
					if(ticket_detail.size()>0){
						discountInfo=ticket_detail.get(1).select("span.col.col2").text();
					}
					holyrobotScepriceinfo.setDiscountinfo(discountInfo);	

					holyrobotScepriceinfo.setId(UUID.randomUUID().toString());
					holyrobotScepriceinfo.setScenicid(uuid);
					holyrobotScepriceinfo.setUrlid(url);
					holyrobotScepriceinfo.setCreator("徐仁杰");
					holyrobotScepriceinfo.setCreatorid("xurenjie-13621935220");
					holyrobotScepriceinfo.setCreatedate(new Date());

					redisService.insertAndGetId(holyrobotScepriceinfo);
				}
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(scenic.getContent());
			ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, scenic);
		}
	}


	/**
	 * @Description 获得携程门票评论url
	 * @author 汤玉林
	 * @date 2017年12月12日 上午9:35:23
	 * @action ctripScenicCommentURL
	 * @param scenic
	 */
	private void ctripScenicCommentURL(Scenic scenic) {
		String url=scenic.getUrl();
		String uuId=scenic.getId();
		String scenicSpotId=url.substring(url.lastIndexOf("/")+2, url.lastIndexOf("."));
		//获得评论总数
		String gradeNum=scenic.getSceinfo().getGradenum();
		System.out.println(gradeNum);
		int commentCount=Integer.valueOf(gradeNum);
		//评论产品id
		String productId=scenic.getOtherinformation();
		int totalPage=commentCount%10==0?commentCount/10:commentCount/10+1;
		totalPage=totalPage>100?100:totalPage;
		for(int i=1;i<=totalPage;i++){
			String commentUrl="http://piao.ctrip.com/Thingstodo-Booking-ShoppingWebSite/api/TicketDetailApi/action/GetUserComments?productId="
					+ productId + "&scenicSpotId=" + scenicSpotId + "&page=" +i;
			Params params=new Params();
			params.setUuid(uuId);
			params.setUrl(commentUrl);
			params.setType(Param.CTRIP_SCENIC_COMMENT);
			params.setDataSource(Param.CTRIP);
			params.setHttpType(Param.GET);

			ScenicSpiderStart.redis.add(MISSION_PARAMS, params);

		}
	}

	/**
	 * 
	 * @Description 获得ctrip的门票图片链接
	 * @author 汤玉林
	 * @date 2017年12月7日 上午10:45:24
	 * @action ctripScenicPicture
	 * @param Scenic
	 */
	public void ctripScenicPictureURL(Scenic scenic) {
		String uuId=scenic.getId();
		String url=scenic.getUrl();
		String scenicSpotId=url.substring(url.lastIndexOf("/")+2, url.lastIndexOf("."));
		String pictureUrl = "http://piao.ctrip.com/Thingstodo-Booking-ShoppingWebSite/api/TicketDetailApi/action/GetMultimedia?scenicSpotId="+scenicSpotId;

		Params params=new Params();
		params.setUuid(uuId);
		params.setUrl(pictureUrl);
		params.setType(Param.CTRIP_SCENIC_PICTURE);
		params.setDataSource(Param.CTRIP);
		params.setHttpType(Param.GET);

		ScenicSpiderStart.redis.add(MISSION_PARAMS, params);

	}

	/**
	 * @Description 解析携程门票详情
	 * @author 汤玉林
	 * @date 2017年12月7日 上午10:46:37
	 * @action ctripScenicUrlParse
	 * @param Scenic
	 */
	public void ctripScenicParse(Scenic scenic) {
		String uuId=scenic.getId();
		Document document=Jsoup.parse(scenic.getContent());
		try {
			// 景点详情信息
			Sceinfo holyrobotSceinfo = new Sceinfo();
			holyrobotSceinfo.setId(uuId);
			holyrobotSceinfo.setUrl(scenic.getUrl());
			//景点名称
			Elements nameEle=document.select("div.media-right>h2.media-title");
			String name=nameEle.text();
			holyrobotSceinfo.setName(name);
			//景点星级
			Elements starEle=document.select("div.media-right>span.media-grade>strong");
			String star=starEle.text();
			holyrobotSceinfo.setStarlevel(star);
			// 推荐价格
			Elements select = document.select("div#media-wrapper>div.media-price>div.price-box>span");
			String text = select.text();
			holyrobotSceinfo.setReferprice(text);
			// 开放时间
			Elements select2 = document.select("div#media-wrapper>div.media-right>ul>li.time>span.j-limit");
			String text2 = select2.text();
			holyrobotSceinfo.setOpentime(text2);
			// 服务承诺
			Elements select4 = document.select("div#J-MediaLabel>div.jmp.pop-content");
			String text3 = select4.text();
			holyrobotSceinfo.setServicecommitment(text3);
			// 景点简介
			Elements select3 = document.select("div#J-Jdjj>div.feature-wrapper");
			String text4 = select3.text();
			holyrobotSceinfo.setIntroduction(text4);
			//景点评论人数
			Elements gradeElements=document.select("div.grade>a.mark");
			Integer gradeNum=NumUtils.getInteger(gradeElements.text());
			if(gradeNum==null){
				gradeNum=0;
			}
			holyrobotSceinfo.setGradenum(gradeNum.toString());

			//景点评分
			Elements gradeEle=document.select("div.grade>i");
			String grade=gradeEle.text();
			holyrobotSceinfo.setGrade(grade);
			//景点地址
			String address="";
			Elements addressEle=document.select("div.layoutfix>div.media-right>ul>li>span");
			if(addressEle.size()>0){
				address=addressEle.get(0).text();
			}
			holyrobotSceinfo.setAddress(address);
			System.out.println("url:"+scenic.getUrl());

			// 景点经纬度
			String lng="";
			String lat="";
			String script = document.select("script").html();
			try {
				String lnglat=script.substring(script.indexOf("position:")+1, script.indexOf("cityName"));
				lng=lnglat.substring(10, lnglat.indexOf("|"));
				lat=lnglat.substring(lnglat.indexOf("|")+1, lnglat.indexOf(",")-1);

			} catch (Exception e) {
				e.printStackTrace();
			}
			holyrobotSceinfo.setLongitude(lng);
			holyrobotSceinfo.setLatitude(lat);
			String cityName="";
			String productId="";
			//城市名称
			try {
				String cityNameScript= script.substring(script.indexOf("cityName"), script.indexOf("jsPathRoot"));
				cityName=cityNameScript.substring(10, cityNameScript.lastIndexOf(",")-1);
				String productStr=script.substring(script.indexOf("productid:")+11, script.indexOf("address"));
				productId=productStr.substring(0, productStr.indexOf(","));
			} catch (Exception e) {
				e.printStackTrace();
			}
			//景点productID，存入otherinformation字段中用于取评论
			scenic.setOtherinformation(productId);

			holyrobotSceinfo.setDatasource("Ctrip");
			holyrobotSceinfo.setDataType("2");
			holyrobotSceinfo.setCreator("徐仁杰");
			holyrobotSceinfo.setCreatorid("xurenjie-13621935220");
			holyrobotSceinfo.setCreatedate(new Date());

			scenic.setSceinfo(holyrobotSceinfo);

			if(StringUtils.isBlank(holyrobotSceinfo.getName())){
				System.out.println("数据不完整，放回队列");
				ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, scenic);

			}else{
				redisService.insertAndGetId(holyrobotSceinfo);

				Addressinfo addressInfo = new Addressinfo();
				addressInfo.setId(UUID.randomUUID().toString());
				addressInfo.setInfoid(uuId);
				addressInfo.setDetailaddress(address);
				addressInfo.setCity(cityName);
				addressInfo.setProvince(scenic.getCityName());//种子链接传过来的值是省份
				addressInfo.setType(1);
				addressInfo.setCountry("中国");
				addressInfo.setCreator("徐仁杰");
				addressInfo.setCreatorid("xurenjie-13621935220");
				addressInfo.setCreatedate(new Date());
				redisService.insertAndGetId(addressInfo);
				// 门票价格部分
				Elements select13 = document.select("dl.c-wrapper-info>dd");
				for (Element element:select13) {
					String type = element.select("strong").text();
					String textValue = element.select("div").toString();
					String newtextValue=textValue.replaceAll("<div>", "").replaceAll("</div>", "").toString().trim();
					switch (type) {

					case "优待政策":
						Scepriceinfo holyrobotScepriceinfo = new Scepriceinfo();
						String priceinfoId=UUID.randomUUID().toString();
						holyrobotScepriceinfo.setId(priceinfoId);
						holyrobotScepriceinfo.setScenicid(uuId);
						holyrobotScepriceinfo.setUrlid(scenic.getUrl());
						holyrobotScepriceinfo.setPricetype("优待政策");
						System.out.println(newtextValue);
						holyrobotScepriceinfo.setPriceitem(newtextValue);

						holyrobotScepriceinfo.setCreator("徐仁杰");
						holyrobotScepriceinfo.setCreatorid("xurenjie-13621935220");
						holyrobotScepriceinfo.setCreatedate(new Date());

						redisService.insertAndGetId(holyrobotScepriceinfo);

						break;
					default:
						break;
					}
				}
				Elements select5 = document
						.select("div#booking-wrapper>div#J-Ticket>table.ticket-table>tbody>tr.ticket-info");
				if(select5.size()<=0){
					select5=document.select("div#booking-wrapper>div#J-Activity>table.ttd-table>tbody>tr");
				}
				String priceType = "";
				for (Element element : select5) {
					Scepriceinfo holyrobotScepriceinfo = new Scepriceinfo();
					String priceinfoId=UUID.randomUUID().toString();
					holyrobotScepriceinfo.setId(priceinfoId);
					holyrobotScepriceinfo.setScenicid(uuId);
					holyrobotScepriceinfo.setUrlid(scenic.getUrl());
					// 价格类型
					Elements select6 = element.select("td.ticket-type>span");
					String str = select6.text();
					if (StringUtils.isNotBlank(str)) {
						priceType = str;
					}
					holyrobotScepriceinfo.setPricetype(priceType);
					// 门票类型
					Elements select7 = element.select("td.ticket-title-wrapper>span");
					String text6 = select7.text();
					holyrobotScepriceinfo.setTickettype(text6);
					// 价格条目
					Elements select8 = element.select("td.ticket-title-wrapper>a");
					if(select8.size()<=0){
						select8 = element.select("td.ttd-title>a");
					}
					String text7 = select8.text();
					holyrobotScepriceinfo.setPriceitem(text7);
					// 市场价
					Elements select9 = element.select("td.del-price>strong");
					if(select9.size()<=0){
						select9 = element.select("td>span.del-price");
					}
					String text8 = select9.text();
					holyrobotScepriceinfo.setMarketingprice(text8);
					// 销售条件（预定时间）
					Element select10 = null;
					if (StringUtils.isNoneBlank(str)) {
						select10 = element.select("td").get(2);
					} else {
						select10 = element.select("td").get(1);
					}
					String text9 = select10.text();
					holyrobotScepriceinfo.setSalecondition(text9);
					// 销售价
					Elements select11 = element.select("td>span.ctrip-price>strong");

					String text10 = select11.text();
					holyrobotScepriceinfo.setSaleprice(text10);
					// 优惠信息
					Elements select12 = element.select("td>div.icon-wrapper.pop-wrapper");
					String text11 = select12.text();
					holyrobotScepriceinfo.setDiscountinfo(text11);


					holyrobotScepriceinfo.setCreator("徐仁杰");
					holyrobotScepriceinfo.setCreatorid("xurenjie-13621935220");
					holyrobotScepriceinfo.setCreatedate(new Date());

					redisService.insertAndGetId(holyrobotScepriceinfo);

				}
				//放图片链接
				ctripScenicPictureURL(scenic);
				//放评论链接
				ctripScenicCommentURL(scenic);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, scenic);
		}
	}

	/**
	 * @Description lvmama门票评论url获取
	 * @author 汤玉林
	 * @date 2017年12月19日 上午9:17:21
	 * @action lvmamaCommentURL
	 * @param scenic
	 */
	private void lvmamaScenicCommentURL(Scenic scenic) {
		String uuid = scenic.getId();
		String url = scenic.getUrl();
		//获得产品id
		String productId=url.substring(url.indexOf("-")+1, url.length());
		//获取评论总数
		String gradeNum=scenic.getSceinfo().getGradenum();
		int commentTotal=Integer.valueOf(gradeNum);
		int totalPage=commentTotal%10==0?commentTotal/10:commentTotal/10+1;
		for(int i=1;i<=totalPage;i++){
			String commentUrl = "http://ticket.lvmama.com/vst_front/comment/newPaginationOfComments?type=all&currentPage="+i+""
					+ "&totalCount="+gradeNum+"&placeId="+productId+"&productId=&placeIdType=PLACE&isPicture=&isBest=&isPOI=Y&isELong=N";
			Params params=new Params();
			params.setUuid(uuid);
			params.setUrl(commentUrl);
			params.setType(Param.LVMAMA_SCENIC_COMMENT);
			params.setDataSource(Param.LVMAMA);
			params.setHttpType(Param.GET);

			ScenicSpiderStart.redis.add(MISSION_PARAMS, params);
		}

	}


	/**
	 * @Description 驴妈妈门票详情解析操作
	 * @author 徐仁杰
	 * @date 2017年12月5日 上午10:29:24
	 * @action lvmamaScenicParse
	 * @return void
	 */
	public void lvmamaScenicParse(Scenic page) {
		String uuid = page.getId();
		String url = page.getUrl();

		// 获取门票页面
		String html = page.getContent();
		Document doc = Jsoup.parse(html);
		// 门票详情操作
		Sceinfo sceinfo =page.getSceinfo();
		sceinfo.setUrl(url);
		sceinfo.setId(uuid);

		try {
			//景点名称
			Elements nameEle=doc.select("div.dtitle>div.titbox>h1.tit");
			String name=nameEle.text();
			sceinfo.setName(name);

			// 经纬度
			String scriptStr = doc.html();
			int lngIndex = scriptStr.indexOf("coordinate: { lng:");
			int latIndex = scriptStr.indexOf("},//地图中心");
			String longitude="";
			String latitude="";
			try {
				if(lngIndex!=-1||latIndex!=-1){
					String[] coordinate = scriptStr.substring(lngIndex, latIndex).split(",");
					longitude = coordinate[0];
					latitude = coordinate[1];
				}else{
					lngIndex=0;
					latIndex=0;
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			if(!"".equals(longitude) && !"".equals(latitude)){
				sceinfo.setLatitude(NumUtils.getDouble(latitude).toString());
				sceinfo.setLongitude(NumUtils.getDouble(longitude).toString());
			}


			Elements select = doc.select("div.overview>div.dtitle.clearfix>span.xorder>span.price>dfn>i");
			// 价格
			String referPrice = select.isEmpty() ? "" : select.text();
			sceinfo.setReferprice(referPrice);
			Elements select2 = doc.select("div.overview>div.dtitle.clearfix>div.titbox>span.mp_star>i");
			// 景点星级
			String starLevel = select2.isEmpty() ? "" : select2.text();
			sceinfo.setStarlevel(starLevel);
			Elements select3 = doc.select(
					"div.overview>div.dcontent.clearfix>div.dinfo>div.sec-info>div.sec-inner>dl.dl-hor.service_list>dd");
			// 服务保障
			String serviceCommitment = select3.isEmpty() ? "" : select3.text();
			sceinfo.setServicecommitment(serviceCommitment);

			//景点地址
			Elements addressEle=doc.select("div.sec-info>div.sec-inner>dl.dl-hor>dd>p.linetext");
			String address= addressEle.isEmpty()?"":addressEle.text();
			sceinfo.setAddress(address);

			//评论人数
			Elements commentEle=doc.select("div.tab-dest>ul.ul-hor>li>span#totalCmt");
			String commentStr=commentEle.text();
			Integer commentCount=0;
			if(StringUtils.isNotBlank(commentStr)){
				commentCount=NumUtils.getInteger(commentStr);
			}
			sceinfo.setGradenum(commentCount+"");

			//评分
			Elements scoreEle=doc.select("div.c_09c>span>i");
			String score=scoreEle.text();
			if(StringUtils.isBlank(score)){
				score="";
			}
			sceinfo.setGrade(score);
			sceinfo.setDataType("2");
			sceinfo.setDatasource("Lvmama");
			sceinfo.setCreator("徐仁杰");
			sceinfo.setCreatorid("xurenjie-13621935220");
			sceinfo.setCreatedate(new Date());

			page.setSceinfo(sceinfo);
			//判断爬取的景点名称是否是空
			if(StringUtils.isNotBlank(sceinfo.getName())){

				String insertAndGetId = redisService.insertAndGetId(sceinfo);
				//插入成功
				if(StringUtils.isNotBlank(insertAndGetId)){
					Addressinfo addressInfo = new Addressinfo();
					addressInfo.setId(UUID.randomUUID().toString());
					addressInfo.setInfoid(uuid);
					addressInfo.setDetailaddress(address);
					addressInfo.setCity(page.getCityName());
					addressInfo.setProvince(page.getCityName());
					addressInfo.setType(1);
					addressInfo.setCountry("中国");
					addressInfo.setCreator("徐仁杰");
					addressInfo.setCreatorid("xurenjie-13621935220");
					addressInfo.setCreatedate(new Date());
					redisService.insertAndGetId(addressInfo);

					// 景点图片
					Elements select4 = doc.select("div.ticket_img_scroll>div.xtu.fr>dl.pic_tab_dl>dd>img");
					if (!select4.isEmpty()) {
						for (int i = 0; i < select4.size(); i++) {
							Pictureinfo pictureinfo = new Pictureinfo();
							String imgUrl = select4.get(i).absUrl("src");
							String id=UUID.randomUUID().toString();
							pictureinfo.setId(id);
							pictureinfo.setInfoid(uuid);
							pictureinfo.setImgurl(imgUrl);
							pictureinfo.setSort(i);
							pictureinfo.setType(2);
							pictureinfo.setDownload(0);
							pictureinfo.setCreator("徐仁杰");
							pictureinfo.setCreatorid("xurenjie-13621935220");
							pictureinfo.setCreatedate(new Date());

							redisService.insertAndGetId(pictureinfo);

						}
					}
					//预定须知部分
					Elements select13 = doc.select("div.dcontent>div.dactive>div.darea");
					//免票政策
					for (Element element : select13) {
						String select5 = element.select("h5").text();
						Elements select6 = element.select("p");
						switch (select5) {

						case "免票政策":
							Scepriceinfo scepriceinfo = new Scepriceinfo();
							String id=UUID.randomUUID().toString();
							scepriceinfo.setId(id);
							scepriceinfo.setScenicid(uuid);
							scepriceinfo.setUrlid(url);
							scepriceinfo.setPricetype("免票政策");
							String priceitem="";
							for (Element element2 : select6) {
								priceitem=priceitem+element2.text()+"<br>";
							}
							scepriceinfo.setPriceitem(priceitem);

							scepriceinfo.setCreator("徐仁杰");
							scepriceinfo.setCreatorid("xurenjie-13621935220");
							scepriceinfo.setCreatedate(new Date());

							redisService.insertAndGetId(scepriceinfo);
							break;
						case "优惠政策":

							Scepriceinfo scepriceinfo2 = new Scepriceinfo();
							scepriceinfo2.setId(UUID.randomUUID().toString());
							scepriceinfo2.setScenicid(uuid);
							scepriceinfo2.setUrlid(url);
							scepriceinfo2.setPricetype("优惠政策");

							String priceitem2="";
							for (Element element2 : select6) {
								priceitem2=priceitem2+element2.text()+"<br>";
							}
							scepriceinfo2.setPriceitem(priceitem2);
							scepriceinfo2.setPriceitem(select6.text());

							scepriceinfo2.setCreator("徐仁杰");
							scepriceinfo2.setCreatorid("xurenjie-13621935220");
							scepriceinfo2.setCreatedate(new Date());

							redisService.insertAndGetId(scepriceinfo2);

							break;
						default:
							break;
						}
					}

					//优惠政策

					// 景点门票价格部分
					Elements select5 = doc
							.select("div.dcontent.dorder-list>div.dpro-list>table.ptable.table-full>tbody.ptbox.short");
					if (!select5.isEmpty()) {
						for (Element element : select5) {
							// 价格类型（成人、儿童、其他等）
							Elements select6 = element.select("tr>td.ptdname>div.ptname>h5");
							String priceType = select6.isEmpty() ? "" : select6.text();
							Elements priceListElements = element.select("tr>td>div.ptdlist>div.pdlist-inner>dl.ptditem");
							for (Element element2 : priceListElements) {
								Scepriceinfo scepriceinfo = new Scepriceinfo();
								String id=UUID.randomUUID().toString();
								scepriceinfo.setId(id);
								scepriceinfo.setScenicid(uuid);
								scepriceinfo.setUrlid(url);
								scepriceinfo.setPricetype(priceType);
								// 门票类型（单票、套票等）
								Elements select7 = element2.select("dl.ptditem>dt.pdname");
								String ScenicType = select6.isEmpty() ? "" : select7.text();
								if (StringUtils.isNoneBlank(ScenicType) && ScenicType.contains("[") && ScenicType.contains("]")) {
									int indexOf = ScenicType.indexOf("[");
									int indexOf2 = ScenicType.indexOf("]");
									String substring = ScenicType.substring(indexOf + 1, indexOf2);
									scepriceinfo.setTickettype(substring);
								}
								// 价格条目
								Elements select8 = element2.select("dl.ptditem>dt.pdname");
								String priceItem = select6.isEmpty() ? "" : select8.text();
								scepriceinfo.setPriceitem(priceItem);
								// 市场价
								Elements select9 = element2.select("dl.ptditem>dd.pdprice");
								String marketingPrice = select6.isEmpty() ? "" : select9.text();
								scepriceinfo.setMarketingprice(marketingPrice);
								// 销售条件（预定时间）
								Elements select10 = element2.select("dl.ptditem>dd.pdAdvbookingTime");
								String saleCondition = select6.isEmpty() ? "" : select10.text();
								scepriceinfo.setSalecondition(saleCondition);
								// 销售价
								Elements select11 = element2.select("dl.ptditem>dd.pdlvprice");
								String salePrice = select6.isEmpty() ? "" : select11.text();
								scepriceinfo.setSaleprice(salePrice);
								// 优惠信息
								Elements select12 = element2.select("dl.ptditem>dd.pdprefer");
								String discountInfo = select6.isEmpty() ? "" : select12.text();
								scepriceinfo.setDiscountinfo(discountInfo);

								scepriceinfo.setCreator("徐仁杰");
								scepriceinfo.setCreatorid("xurenjie-13621935220");
								scepriceinfo.setCreatedate(new Date());

								redisService.insertAndGetId(scepriceinfo);

							}
						}
					}
				}else{
					ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, page);
				}

			}else{
				ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, page);
			}

		} catch (Exception e) {
			e.printStackTrace();
			ScenicSpiderStart.redis.add(ScenicSpiderStart.KEY_SCENIC, page);
		}
	}



}
