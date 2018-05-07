package cn.jj.service.impl;

import java.io.Serializable;
import java.net.URLDecoder;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import cn.jj.controller.OtherSpiderStart;
import cn.jj.controller.StrokeSpiderStart;
import cn.jj.dao.service.RedisService;
import cn.jj.dao.service.impl.RedisServiceImpl;
import cn.jj.entity.Params;
import cn.jj.entity.data.Commentinfo;
import cn.jj.entity.data.Hotelinfo;
import cn.jj.entity.data.Pictureinfo;
import cn.jj.entity.data.Roombasicinfo;
import cn.jj.entity.data.Roomprice;
import cn.jj.entity.data.Routeinfo;
import cn.jj.entity.data.Routepriceinfo;
import cn.jj.entity.data.Sceinfo;
import cn.jj.entity.data.Scepriceinfo;
import cn.jj.service.IParse;
import cn.jj.utils.DateUtil;
import cn.jj.utils.NumUtils;
import cn.jj.utils.Param;

public class OtherParse implements IParse {

	static String creatorID = "tyl13564205515";

	// redis持久化接口
	private RedisService redisService = new RedisServiceImpl();

	@Override
	public void parse(Serializable str) {
		Params params = (Params) str;
		Param key = params.getType();
		switch (key) {
		case LVMAMA_SCENIC_COMMENT:
			this.parseLvmamaScenicComment(params);
			break;
		case LVMAMA_STROKE_COMMENTURL:
			this.parseLvmamaStrokeCommentUrl(params);
			break;
		case LVMAMA_STROKE_COMMENT:
			this.parseLvmamaStrokeComment(params);
			break;
		case LVMAMA_STROKE_WANTANDBEEN:
			this.parseLvmamaStrokeWantAndBeen(params);
			break;
		case LVMAMA_HOTEL_ROOM:
			this.parseLvmamaHotelRoom(params);
			break;
		case LVMAMA_HOTEL_COMMENT:
			this.parseLvmamaHotelComment(params);
			break;
		case LVMAMA_ROUTE_PRICE:
			this.parseLvmamaRoutePrice(params);
			break;
		case LVMAMA_ROUTE_COMMENT:
			this.parseLvmamaRouteComment(params);
			break;
		case CTRIP_HOTEL_ROOM:
			this.parseCtripHotelRoom(params);
			break;
		case CTRIP_SCENIC_PICTURE:
			this.parseCtripScenicPicture(params);
			break;
		case CTRIP_SCENIC_COMMENT:
			this.parseCtripScenicComment(params);
			break;
		case CTRIP_STROKE_WANTANDBEEN:
			this.parseCtripStrokeWantAndBeen(params);
			break;
		case CTRIP_STROKE_COMMENT:
			this.parseCtripStrokeComment(params);
			break;
		case CTRIP_ROUTE_PRICE:
			this.parseCtripRoutePrice(params);
			break;
		case CTRIP_ROUTE_DEPARTURE:
			this.parseCtripRouteDeparture(params);
			break;
		case CTRIP_ROUTE_EXPENSEANDRESERVEINFO:
			this.parseCtripExpenseAndReserveinfo(params);
			break;
		case CTRIP_ROUTE_COMMENT_FIRST:
			this.parseCtripRouteCommentFirst(params);
			break;
		case CTRIP_ROUTE_COMMENT:
			this.parseCtripRouteComment(params);
			break;
		case TUNIU_STROKE_COMMENT:
			this.parseTuniuStrokeComment(params);
			break;
		case TUNIU_HOTEL_PICTURE:
			this.parseTuniuHotelPicture(params);
			break;
		case TUNIU_HOTEL_INTRODUCTION:
			this.parseTuniuHotelIntroduction(params);
			break;
		case TUNIU_HOTEL_ROOM:
			this.parseTuniuHotelRoom(params);
			break;
		case TUNIU_HOTEL_COMMENT_FIRST:
			this.parseTuniuHotelCommentFirst(params);
			break;
		case TUNIU_HOTEL_COMMENT:
			this.parseTuniuHotelComment(params);
			break;
		case TUNIU_SCENIC_COMMENT:
			this.parseTuniuScenicComment(params);
			break;
		case TUNIU_ROUTE_PRICE:
			this.parseTuniuRoutePrice(params);
			break;
		case TUNIU_ROUTE_COMMENT:
			this.parseTuniuRouteComment(params);
			break;
		case TONGCHENG_HOTEL_ROOM:
			this.parseTongChengHotelRoom(params);
			break;
		case TONGCHENG_HOTEL_COMMENT:
			this.parseTongChengHotelComment(params);
			break;
		case TONGCHENG_STROKE_COMMENT_FIRST:
			this.parseTongchengStrokeCommentFirst(params);
			break;
		case TONGCHENG_STROKE_COMMENT:
			this.parseTongchengStrokeComment(params);
			break;
		case TONGCHENG_SCENIC_COMMENT_FIRST:
			this.parseTongchengScenicCommentFirst(params);
			break;
		case TONGCHENG_SCENIC_PRICE:
			this.parseTongChengScenicPrice(params);
			break;
		case TONGCHENG_SCENIC_COMMENT:
			this.parseTongChengScenicComment(params);
			break;
		case TONGCHENG_SCENIC_BOOKNOWNEW:
			this.parseTongChengScenicBookNowNew(params);
			break;
		case TONGCHENG_ROUTE_PRICE:
			this.parseTongchengRoutePrice(params);
			break;
		case TONGCHENG_ROUTE_COMMENT_FIRST:
			this.parseTongchengRouteCommentFirst(params);
			break;
		case TONGCHENG_ROUTE_COMMENT:
			this.parseTongchengRouteComment(params);
			break;
		case QUNAER_STROKE_COMMENT:
			this.parseQunaerStrokeComment(params);
			break;
		case QUNAER_SCENIC_COMMENT:
			this.parseQunaerScenicComment(params);
			break;
		case QUNAER_ROUTE_PRICE:
			this.parseQunaerRoutePrice(params);
			break;
		case QUNAER_ROUTE_COMMENT_FIRST:
			this.parseQunaerRouteCommentFirst(params);
			break;
		case QUNAER_ROUTE_COMMENT:
			this.parseQunaerRouteComment(params);
			break;
		case QUNAER_HOTEL_INTRODUCTION:
			this.parseQunaerHotelIntroduction(params);
			break;
		case QUNAER_HOTEL_PICTURE:
			this.parseQunaerHotelPicture(params);
			break;
		case QUNAER_HOTEL_ROOM:
			this.parseQunaerHotelRoom(params);
			break;
		case QUNAER_HOTEL_COMMENT:
			this.parseQunaerHotelComment(params);
			break;
		case QUNAER_HOTEL_ROOM_PRICE:
			this.parseQunaerHotelRoomPrice(params);
			break;
		case FEIZHU_ROUTE_PRICE:
			this.paraseFeizhuRoutePrice(params);
			break;
		case FEIZHU_ROUTE_COMMENT:
			this.paraseFeizhuRouteComment(params);
			break;
		default:
			break;
		}
	}

	/**
	 * 
	 * @Description 解析飞猪行程评论url，获取行程评论信息
	 * @author 赵乐
	 * @date 2018年1月26日 上午10:08:25
	 * @action paraseFeizhuRouteComment
	 * @param @param params
	 * @return void
	 */
	private void paraseFeizhuRouteComment(Params params) {
		// TODO Auto-generated method stub
		String url = params.getUrl();
		String parentUrl = params.getParentUrl();
		String content = params.getContent();
		if (content.startsWith("{")) {
			// 获取评论总评分
			try {
				JSONObject jsonObject = new JSONObject(content);
				String totalScore = jsonObject.getJSONObject("module")
						.getJSONObject("rateScore").getString("totalScore");

				if (url.contains("&pageNo=1")) {
					Routeinfo routeinfo = new Routeinfo();
					routeinfo.setUrl(parentUrl);
					// 根据行程的url，查询redis中存在的数据
					List<Routeinfo> selectByMultipleAttribute = redisService
							.selectByMultipleAttribute(routeinfo);
					// 插入总评分
					for (Routeinfo info : selectByMultipleAttribute) {
						info.setGrade(totalScore);
						redisService.insertAndGetId(info);
					}

				}
				JSONArray jsonArray = jsonObject.getJSONObject("module")
						.getJSONObject("rateList").getJSONArray("rateCellList");
				for (Object object : jsonArray) {
					String string = object.toString();
					Commentinfo commentinfo = new Commentinfo();
					commentinfo.setId(UUID.randomUUID().toString());
					commentinfo.setInfoid(parentUrl);
					commentinfo.setContent(string);
					commentinfo.setCreatedate(new Date());
					commentinfo.setCreator("赵乐");
					commentinfo.setCreatorid("15736708180");
					redisService.insertAndGetId(commentinfo);
				}
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("响应格式有误");
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("响应格式有误");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * 
	 * @Description 解析飞猪行程价格url，获取行程价格信息
	 * @author 赵乐
	 * @date 2018年1月26日 上午10:08:21
	 * @action paraseFeizhuRoutePrice
	 * @param @param params
	 * @return void
	 */
	private void paraseFeizhuRoutePrice(Params params) {
		// TODO Auto-generated method stub
		String parentUrl = params.getParentUrl();

		String content = params.getContent();
		if (content.startsWith("{")) {
			try {
				// 获取json信息
				JSONObject jsonObject = new JSONObject(content);
				JSONObject jsonObject2 = jsonObject.getJSONObject("module")
						.getJSONObject("itemProps").getJSONObject("data");
				// 获取组合信息
				JSONArray jsonArray = jsonObject2.getJSONArray("props");

				// 出发地集合
				JSONArray departure = null;
				// 套餐类型集合
				JSONArray type = null;
				// 出游人群集合
				JSONArray excursion = null;

				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject3 = jsonArray.getJSONObject(i);
					String skuPropTitle = jsonObject3.getString("skuPropTitle");
					JSONArray skuPropList = jsonObject3
							.getJSONArray("skuPropList");
					switch (skuPropTitle) {
					case "出发地":
						// 获取出发地信息
						departure = skuPropList;
						break;
					case "套餐类型":
						// 获取套餐信息
						type = skuPropList;
						break;
					case "出游人群":
						// 获取出游人群信息
						excursion = skuPropList;
						break;
					default:
						break;
					}

				}
				// 获取行程价格的json信息
				JSONObject jsonObject3 = jsonObject2.getJSONObject("skuMap");

				for (int j = 0; j < type.length(); j++) {
					// 获取套餐类型信息
					JSONObject jsonObject4 = type.getJSONObject(j);
					// 套餐类型
					String valueType = jsonObject4.getString("value");
					// 套餐类型id
					String pvIdType = jsonObject4.getString("pvId");

					for (int k = 0; k < departure.length(); k++) {
						// 获取出发地信息
						JSONObject jsonObject5 = type.getJSONObject(k);
						// 出发地类型
						String valueDeparture = jsonObject5.getString("value");
						// 找到对应的出发城市和套餐
						if ((valueDeparture + "出发").equals(valueType)) {
							// 每一个出发地是一条行程
							Routeinfo routeInfo = params.getRouteInfo();
							String routeInfoId = UUID.randomUUID().toString();
							routeInfo.setId(routeInfoId);
							routeInfo.setCreatedate(new Date());
							routeInfo.setCreator("赵乐");
							routeInfo.setCreatorid("15736708180");
							routeInfo.setDatasource("Feizhu");
							routeInfo.setDeparture(valueDeparture);

							// 出发地id
							String pvIdDeparture = jsonObject5
									.getString("pvId");

							// 获取对应组合下的价格信息
							JSONArray jsonArray2 = jsonObject3
									.getJSONArray("8558246:3622544;" + pvIdType
											+ ";" + pvIdDeparture);
							// 出发城市
							for (int m = 0; m < jsonArray2.length(); m++) {

								JSONObject jsonObject6 = jsonArray2
										.getJSONObject(m);
								// 行程价格
								Routepriceinfo routepriceinfo = new Routepriceinfo();

								// 价格的日期
								String date = jsonObject6.getString("date");
								String finalPrice = jsonObject6
										.getString("finalPrice");
								System.out.println(finalPrice + date
										+ "----行程价格");
								routepriceinfo.setId(UUID.randomUUID()
										.toString());
								routepriceinfo.setRouteid(routeInfoId);
								routepriceinfo.setCreatedate(new Date());
								routepriceinfo.setCreator("赵乐");
								routepriceinfo.setCreatorid("15736708180");
								routepriceinfo.setLowestprice(finalPrice);
								routepriceinfo.setPricedate(date);
								routepriceinfo.setRemark(valueDeparture);
								System.out.println("插入行程价格信息");
								redisService.insertAndGetId(routepriceinfo);
							}

							// 获取套餐说明
							JSONObject jsonObject7 = jsonObject
									.getJSONObject("itemFreeTour")
									.getJSONObject("data")
									.getJSONObject("data")
									.getJSONObject("propsMap");
							// 获取对应组合下的套餐信息
							JSONObject jsonObject8 = jsonObject7
									.getJSONObject(pvIdDeparture + ";"
											+ pvIdType);
							// 套餐说明(行程说明)
							routeInfo.setItineraryoutline(jsonObject8
									.toString());
							// 费用说明
							String feeInclude = jsonObject8
									.getString("feeInclude");
							String feeExclude = jsonObject8
									.getString("feeExclude");
							routeInfo.setExpense(feeInclude + "---"
									+ feeExclude);
							// 预定须知
							String notice = jsonObject8.getString("notice");
							System.out.println("插入行程基本信息");
							routeInfo.setReserveinfo(notice);

						} else {
							continue;
						}

					}
				}
				/*
				 * JSONObject jsonObject = new JSONObject(content); JSONObject
				 * jsonObject2 =
				 * jsonObject.getJSONObject("module").getJSONObject
				 * ("itemCalendar").getJSONObject("data"); JSONObject
				 * jsonObject3 = jsonObject2.getJSONObject("goodsDataTable");
				 * Iterator<String> keys = jsonObject3.keys();
				 * while(keys.hasNext()){ //价格日期 String nextkey = keys.next();
				 * JSONObject jsonObject4 = jsonObject3.getJSONObject(nextkey);
				 * //日期下面的价格 String price = jsonObject4.getString("price");
				 * 
				 * Routepriceinfo routepriceinfo = new Routepriceinfo();
				 * routepriceinfo.setId(UUID.randomUUID().toString());
				 * routepriceinfo.setRouteid(uuid);
				 * routepriceinfo.setPricedate(nextkey);
				 * routepriceinfo.setLowestprice(price);
				 * routepriceinfo.setCreatedate(new Date());
				 * routepriceinfo.setCreator("赵乐");
				 * routepriceinfo.setCreatorid("15736708180");
				 * redisService.insertAndGetId(routepriceinfo); }
				 */
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				System.out.println("json有误");
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}

		} else {
			System.out.println("响应格式有误");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 去哪儿酒店房间价格解析
	 * @author 汤玉林
	 * @date 2017年12月28日 下午2:42:04
	 * @action parseQunaerHotelRoomPrice
	 * @param params
	 */
	private void parseQunaerHotelRoomPrice(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		Roombasicinfo room = params.getRoomInfo();// 获取传过来的房型部分信息
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject roomPriceJS = new JSONObject(content);
				String data = roomPriceJS.get("data").toString();
				JSONObject dataJS = new JSONObject(data);
				String price = dataJS.get("price").toString();
				if (price == null || price.length() == 0 || "[]".equals(price)) {
					System.out.println("暂无报价!");
					return;
				}
				JSONArray priceArr = new JSONArray(price);
				System.out.println("该房型下的价格有：" + priceArr.length());
				for (int j = 0; j < priceArr.length(); j++) { // 循环取出当天的各价格集合
					JSONObject priceJS = new JSONObject(priceArr.get(j)
							.toString());
					String rtDescInfo = priceJS.get("rtDescInfo").toString();
					if (rtDescInfo == null || rtDescInfo.length() == 0
							|| "[]".equals(rtDescInfo)) {
						System.out.println("暂无房型详细信息!");
						continue;
					}
					JSONArray rtDescInfoJS = new JSONArray(rtDescInfo);
					String isWindow = null;
					String isWifi = null;
					String bedType = null;
					String floor = null;
					for (int k = 0; k < rtDescInfoJS.length(); k++) { // 获取房型其余信息
						JSONObject descJS = new JSONObject(rtDescInfoJS.get(k)
								.toString());
						if (!descJS.isNull("床型")) { // 床型
							bedType = descJS.get("床型").toString();
							continue;
						}
						if (!descJS.isNull("wifi")) { // 是否有wifi
							isWifi = descJS.get("wifi").toString();
							continue;
						}
						if (!descJS.isNull("楼层")) { // 楼层
							floor = descJS.get("楼层").toString();
							continue;
						}
						if (!descJS.isNull("窗")) { // 是否有窗
							isWindow = descJS.get("窗").toString();
							continue;
						}
					}
					room.setBedtype(bedType);
					room.setFloor(floor);
					room.setIswifi(isWifi);

					StringBuffer productName = new StringBuffer();
					String showRoomName = priceJS.get("showRoomName")
							.toString(); // 产品名称1
					String basicInfos = priceJS.get("basicInfos").toString(); // 产品名称2
					String appliyby = priceJS.get("wrapperName").toString();
					// 供应商
					productName.append(basicInfos + " ")
							.append(showRoomName + " ").append(appliyby + " ");// 产品名称
					JSONArray tagList = null;
					try {
						tagList = priceJS.getJSONArray("tagList");
					} catch (Exception e) {

					}
					if (tagList != null) {
						for (int i = 0; i < tagList.length(); i++) {
							JSONObject obj = tagList.getJSONObject(i);
							String label = obj.getString("label");
							productName.append(label);
						}
					}
					String date = priceJS.get("checkInDate").toString(); // 日期

					String paymethod = priceJS.get("pricePayStr").toString(); // 付款方式
					String priceStr = priceJS.get("priceMix").toString(); // 价格
					String productPrice = this.getRoomPrice(priceStr);

					Roomprice roomPrice = new Roomprice();
					roomPrice.setId(UUID.randomUUID().toString());
					roomPrice.setHotelid(uuid);
					roomPrice.setRoomid(room.getId());
					roomPrice.setProductname(productName.toString());
					roomPrice.setDate(date);
					roomPrice.setPrice(productPrice);
					roomPrice.setAppliyby(appliyby);
					roomPrice.setIswifi(isWifi);
					roomPrice.setIswindow(isWindow);
					roomPrice.setPaymethod(paymethod);
					roomPrice.setCreatedate(new Date());
					roomPrice.setCreator("姚良良");
					roomPrice.setCreatorid("13783985208");
					System.out.println(productName + ",该条产品的价格：" + productPrice);
					redisService.insertAndGetId(roomPrice);
				}
				room.setCreatedate(new Date());
				room.setCreator("姚良良");
				room.setCreatorid("13783985208");

				redisService.insertAndGetId(room);
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}

	}

	/**
	 * 
	 * @Description 获取房型价钱
	 * @author 姚良良
	 * @date 2017年12月28日 上午10:57:23
	 * @action getRoomPrice
	 * @return String
	 */
	public String getRoomPrice(String priceStr) {
		Document doc = Jsoup.parse(priceStr);
		Elements iEle = doc.select("i");
		int count = iEle.size();
		StringBuffer priceBuff = new StringBuffer();
		String one = "";
		String two = "";
		String three = "";
		String four = "";
		String five = "";
		switch (count) {
		case 4: // 2位数价格
			one = doc.select("i.qm35d4a492").text();
			two = doc.select("i.qma4cff832").text();
			priceBuff.append(one).append(two);
			break;
		case 5: // 3位数价格
			one = doc.select("i.qm35d4a492").text();
			two = doc.select("i.qma4cff832").text();
			three = doc.select("i.qm723dd850").text();
			priceBuff.append(one).append(two).append(three);
			break;
		case 6: // 4位数价格
			one = doc.select("i.qm35d4a492").text();
			two = doc.select("i.qma4cff832").text();
			three = doc.select("i.qm723dd850").text();
			four = doc.select("i.qmbce64ffb").text();
			priceBuff.append(one).append(two).append(three).append(four);
			break;
		case 7: // 5位数价格
			one = doc.select("i.qm35d4a492").text();
			two = doc.select("i.qma4cff832").text();
			three = doc.select("i.qm723dd850").text();
			four = doc.select("i.qmbce64ffb").text();
			five = doc.select("i.qm932c317f").text();
			priceBuff.append(one).append(two).append(three).append(four)
					.append(five);
			break;
		default:
			System.out.println("价钱获取有误!");
			break;
		}
		return priceBuff.toString();
	}

	/**
	 * @Description 去哪儿酒店评论解析
	 * @author 汤玉林
	 * @date 2017年12月28日 下午2:07:29
	 * @action parseQunaerHotelComment
	 * @param params
	 */
	private void parseQunaerHotelComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject comJS = new JSONObject(content);
				String dataStr = comJS.get("data").toString();
				JSONObject dataJSON = new JSONObject(dataStr);
				String commentData = dataJSON.get("commentData").toString();
				JSONObject commentDataJSON = new JSONObject(commentData);
				String comments = commentDataJSON.get("comments").toString();
				JSONArray commentArr = new JSONArray(comments);
				System.out.println("评论获取了：" + commentArr.length() + "条");
				for (int k = 0; k < commentArr.length(); k++) {
					JSONObject obj = commentArr.getJSONObject(k);
					String commentDate = obj.getString("date");
					Commentinfo commentInfo = new Commentinfo();
					commentInfo.setId(UUID.randomUUID().toString());
					commentInfo.setInfoid(uuid);
					commentInfo.setType(2); // 类型：1-景点，2-酒店，3-景点行程
					commentInfo.setCreatedate(new Date());
					commentInfo.setCreator("姚良良");
					commentInfo.setCreatorid("13783985208");
					commentInfo.setContent(obj.toString());
					commentInfo.setCommentdate(commentDate);
					commentInfo.setDatasource("Qunaer");

					redisService.insertAndGetId(commentInfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}

	}

	/**
	 * @Description TODO
	 * @author 汤玉林
	 * @date 2017年12月28日 下午2:07:13
	 * @action parseQunaerHotelRoom
	 * @param params
	 */
	private void parseQunaerHotelRoom(Params params) {
		String url = params.getUrl();
		String uuid = params.getUuid();
		String content = params.getContent();
		String hotelid = url.substring(url.indexOf("seq") + 4,
				url.indexOf("checkInDate") - 1);
		String checkInDate = url.substring(url.indexOf("checkInDate") + 12,
				url.indexOf("checkOutDate") - 1);
		String checkOutDate = url.substring(url.indexOf("checkOutDate") + 13,
				url.length());
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject roomTypeJS = new JSONObject(content);
				String data = roomTypeJS.get("data").toString();
				JSONObject dataJS = new JSONObject(data);
				String price = dataJS.get("price").toString();
				JSONArray priceArr = new JSONArray(price);
				System.err.println("酒店房型个数：" + priceArr.length());
				for (int i = 0; i < priceArr.length(); i++) { // 循环取出房型
					JSONObject roomJS = new JSONObject(priceArr.get(i)
							.toString());
					// System.out.println(roomJS);
					String name = roomJS.get("name").toString();
					String lowPrice = roomJS.get("lowPrice").toString();

					Roombasicinfo room = new Roombasicinfo();
					room.setId(UUID.randomUUID().toString());
					room.setHotelid(uuid);
					room.setRoomtype(name); // 房间类型(名称)
					room.setPrice(lowPrice); // 标准价格
					// 取出房型图片
					// String images = roomJS.get("images").toString();
					/*
					 * if(StringUtils.isNotBlank(images)){ try { JSONArray
					 * imagesArr = new JSONArray(images);
					 * //System.out.println("酒店房型图片获取了："
					 * +imagesArr.length()+"条"); int imageLength=
					 * imagesArr.length()>10?10:imagesArr.length(); for (int k =
					 * 0; k < imageLength; k++) { JSONObject imgJS = new
					 * JSONObject(imagesArr.get(k).toString()); String imgUrl =
					 * imgJS.get("url").toString(); Pictureinfo pictureInfo=new
					 * Pictureinfo();
					 * pictureInfo.setId(UUID.randomUUID().toString());
					 * pictureInfo.setInfoid(uuid);
					 * pictureInfo.setImgurl(imgUrl); pictureInfo.setSort(k);
					 * pictureInfo.setType(5); pictureInfo.setDownload(0);
					 * pictureInfo.setCreatedate(new Date());
					 * pictureInfo.setCreator("姚良良");
					 * pictureInfo.setCreatorid("13783985208");
					 * 
					 * redisService.insertAndGetId(pictureInfo);
					 * 
					 * } } catch (Exception e) { } }
					 */
					String roomPriceURL = "http://touch.qunar.com/api/hotel/hoteldetail/price?seq="
							+ hotelid
							+ "&checkInDate="
							+ checkInDate
							+ "&checkOutDate="
							+ checkOutDate
							+ "&desc=price&room=" + name;
					// System.out.println("该房型对应的价格url："+roomPriceURL);
					Params roomPriceParams = new Params();
					roomPriceParams.setUuid(uuid);
					roomPriceParams.setUrl(roomPriceURL);
					roomPriceParams.setHttpType(Param.GET);
					roomPriceParams.setType(Param.QUNAER_HOTEL_ROOM_PRICE);
					roomPriceParams.setDataSource(Param.QUNAER);
					roomPriceParams.setHeader("Referer",
							"http://touch.qunar.com");
					roomPriceParams.setRoomInfo(room);// 房型对象放入params,用于后面获取价格时用到

					OtherSpiderStart.queue
							.add(OtherSpiderStart.OTHER_MISSION_KEY,
									roomPriceParams);
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * @Description 去哪儿酒店图片解析
	 * @author 汤玉林
	 * @date 2017年12月28日 下午2:06:53
	 * @action parseQunaerHotelPicture
	 * @param params
	 */
	private void parseQunaerHotelPicture(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject pictureJS = new JSONObject(content);
				String data = pictureJS.get("data").toString();
				JSONObject dataJS = new JSONObject(data);
				String images = dataJS.get("images").toString();
				JSONArray imagesArr = new JSONArray(images);
				int imageLength = imagesArr.length() > 10 ? 10 : imagesArr
						.length();

				int count = 0;
				for (int i = 0; i < imageLength; i++) { // 循环取出屋子名称，如"客房、浴室"
					JSONObject tagJS = new JSONObject(imagesArr.get(i)
							.toString());
					String tag = tagJS.get("tag").toString();
					String imgNodes = tagJS.get("imgNodes").toString();
					JSONArray imgNodesArr = new JSONArray(imgNodes);
					int imgNodeLength = imgNodesArr.length();
					if (imgNodesArr.length() > 10) {
						imgNodeLength = 10;
					}
					System.out.println("酒店图片获取了" + imgNodesArr.length() + "条");
					for (int j = 0; j < imgNodeLength; j++) { // 循环取出各屋子的图片
						try {
							JSONObject imgJS = new JSONObject(imgNodesArr
									.get(j).toString());
							String imgUrl = "http:"
									+ imgJS.get("big").toString();
							// 创建图片对象
							Pictureinfo pictureInfo = new Pictureinfo();
							pictureInfo.setId(UUID.randomUUID().toString());
							pictureInfo.setInfoid(uuid);
							pictureInfo.setImgurl(imgUrl);
							pictureInfo.setSort(++count);
							pictureInfo.setType(4);
							pictureInfo.setDownload(0);
							pictureInfo.setCreatedate(new Date());
							pictureInfo.setCreator("姚良良");
							pictureInfo.setCreatorid("13783985208");

							redisService.insertAndGetId(pictureInfo);

						} catch (JSONException e) {
							e.printStackTrace();
						}
					}
					System.out.println(tag + "的图片获取完毕!");
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}

	}

	/**
	 * @Description 去哪儿酒店详情介绍解析
	 * @author 汤玉林
	 * @date 2017年12月28日 下午2:06:29
	 * @action parseQunaerHotelIntroduction
	 * @param params
	 */
	private void parseQunaerHotelIntroduction(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject hotelInfoJS = new JSONObject(content);
				String data = hotelInfoJS.get("data").toString();
				JSONObject dataJS = new JSONObject(data);
				String desc = dataJS.get("desc").toString();

				Hotelinfo hotelinfo = new Hotelinfo();
				hotelinfo.setId(uuid);
				// hotelinfo.setUrl(params.getHeader().get("Referer"));
				hotelinfo.setIntroduction(desc);
				System.out.println("酒店详情内容：" + desc);
				redisService.insertAndGetId(hotelinfo);
			} catch (JSONException e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * @Description 去哪儿行程评论计息
	 * @author 汤玉林
	 * @date 2017年12月27日 下午5:45:49
	 * @action parseQunaerRouteComment
	 * @param params
	 */
	private void parseQunaerRouteComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject commentJSON = new JSONObject(content);
				String dataStr = commentJSON.get("data").toString();
				JSONObject dataJS = new JSONObject(dataStr);
				String mainCommentList = dataJS.get("mainCommentList")
						.toString();
				JSONArray comArr = new JSONArray(mainCommentList);
				for (int k = 0; k < comArr.length(); k++) {
					try {
						JSONObject obj = comArr.getJSONObject(k);
						String commentDate = obj.getString("createdTime");
						// 获取评论内容
						Commentinfo commentInfo = new Commentinfo();
						commentInfo.setId(UUID.randomUUID().toString());
						commentInfo.setInfoid(uuid);
						commentInfo.setType(3); // 类型：1-景点，2-酒店，3-景点行程
						commentInfo.setCreatedate(new Date());
						commentInfo.setDatasource("Qunar");
						commentInfo.setCreator("姚良良");
						commentInfo.setCreatorid("13783985208");
						commentInfo.setContent(obj.toString());
						commentInfo.setCommentdate(commentDate);

						redisService.insertAndGetId(commentInfo);

					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * @Description 去哪儿行程评论第一页解析，用于获取总页数和评论url
	 * @author 汤玉林
	 * @date 2017年12月27日 下午5:38:11
	 * @action parseQunaerRouteCommentFirst
	 * @param params
	 */
	private void parseQunaerRouteCommentFirst(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		String content = params.getContent();
		String itemNo = url.substring(url.indexOf("productId") + 10,
				url.indexOf("rateStatus"));
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject comJSON = new JSONObject(content);
				String data = comJSON.get("data").toString();
				JSONObject dataJSON = new JSONObject(data);
				String totalComment = dataJSON.get("totalComment").toString();
				int total = 0;
				if (StringUtils.isNotBlank(totalComment)) {
					total = Integer.valueOf(totalComment);
				}
				Integer pageCount = total % 20 == 0 ? total / 20
						: total / 20 + 1;
				for (int i = 1; i <= pageCount; i++) {
					String comtUrl = "https://yntx3.package.qunar.com/user/comment/product/queryComments.json?pageNo="
							+ i + "&pageSize=20&productId=" + itemNo;
					Params commentParams = new Params();
					commentParams.setUuid(uuid);
					commentParams.setUrl(comtUrl);
					commentParams.setDataSource(Param.QUNAER);
					commentParams.setType(Param.QUNAER_ROUTE_COMMENT);
					commentParams.setHttpType(Param.GET);

					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, commentParams);
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * @Description 去哪儿行程价格解析
	 * @author 汤玉林
	 * @date 2017年12月27日 下午5:25:01
	 * @action parseQunaerRoutePrice
	 * @param params
	 */
	private void parseQunaerRoutePrice(Params params) {
		String url = params.getUrl();
		if (url.endsWith("tuId=&pId=&month=2018-02")) {
			System.err.println("无效链接");
		} else {
			String uuid = params.getUuid();
			String content = params.getContent();
			if (StringUtils.isNotBlank(content)) {
				try {
					JSONObject json = new JSONObject(content);
					String data = json.get("data").toString();
					JSONObject dataJSON = new JSONObject(data);
					String team = dataJSON.get("team").toString();
					JSONArray teamArr = new JSONArray(team);
					System.out.println(uuid + "对应的行程价钱对象共有" + teamArr.length()
							+ "个!");
					// 记录保存行程价钱的天数
					int k = 0;
					for (int i = 0; i < teamArr.length(); i++) {
						JSONObject priceDateJson = new JSONObject(teamArr
								.get(i).toString());
						String priceDate = priceDateJson.get("date").toString();
						String prices = priceDateJson.get("prices").toString();
						JSONObject pricesJSON = new JSONObject(prices);
						String lowestPrice = pricesJSON.get("adultPrice")
								.toString();
						String dayOfWeek = DateUtil.dateToWeek(priceDate);
						// 创建HolyrobotRoutepriceinfo对象并设置相关属性
						Routepriceinfo routepriceInfo = new Routepriceinfo();
						routepriceInfo.setId(UUID.randomUUID().toString());
						routepriceInfo.setCreator("姚良良");
						routepriceInfo.setCreatorid("13783985208");
						routepriceInfo.setCreatedate(new Date());
						routepriceInfo.setRouteid(uuid);
						routepriceInfo.setPricedate(priceDate);
						routepriceInfo.setLowestprice(lowestPrice);
						routepriceInfo.setDayofweek(dayOfWeek);

						redisService.insertAndGetId(routepriceInfo);
						k++;
					}
					System.out.println(uuid + " 的行程价钱共有 " + teamArr.length()
							+ " 条,共获取" + k + "条!");
				} catch (Exception e) {
					e.printStackTrace();
					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, params);
				}
			}
		}

	}

	/**
	 * 
	 * @Description 解析去哪景点评论
	 * @author 赵乐
	 * @date 2017年12月27日 上午11:55:04
	 * @action parseQunaerStrokeComment
	 * @param @param params
	 * @return void
	 */
	private void parseQunaerStrokeComment(Params params) {
		String uuid = params.getUuid();
		String contentJson = params.getContent();
		try {
			String data = "";
			try {
				JSONObject comJSON = new JSONObject(contentJson);
				data = comJSON.get("data").toString();
			} catch (JSONException e) {
				e.printStackTrace();
			}
			if (!"".equals(data)) {
				Document doc = Jsoup.parse(data);
				Elements commemtEle = doc.select("ul#comment_box>li");
				if (!commemtEle.isEmpty()) {
					for (Element element : commemtEle) {
						Elements dateCom = element
								.select("div.e_comment_main>div.e_comment_main_inner>div.e_comment_add_info>ul>li");
						String commentDate = dateCom.isEmpty() ? "" : dateCom
								.first().text().substring(0, 10);
						Commentinfo commentInfo = new Commentinfo();
						commentInfo.setId(UUID.randomUUID().toString());
						commentInfo.setInfoid(uuid);
						commentInfo.setType(1); // 类型：1-景点，2-酒店，3-景点行程
						commentInfo.setCreatedate(new Date());
						commentInfo.setDatasource("Qunar");
						commentInfo.setCreator("姚良良");
						commentInfo.setCreatorid("13783985208");
						commentInfo.setContent(element.toString());
						commentInfo.setCommentdate(commentDate);
						redisService.insertAndGetId(commentInfo);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 去哪儿门票评论解析
	 * @author 汤玉林
	 * @date 2017年12月26日 下午5:46:13
	 * @action parseQunaerScenicComment
	 * @param params
	 */
	private void parseQunaerScenicComment(Params params) {
		List<Commentinfo> commentsList = new LinkedList<>();
		String uuid = params.getUuid();
		String content = params.getContent();
		// 解析评论json
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject comJSON = new JSONObject(content);
				String data = comJSON.get("data").toString();
				JSONObject dataJSON = new JSONObject(data);
				String commentList = dataJSON.get("commentList").toString();
				JSONArray commentListArr = new JSONArray(commentList);
				System.out.println("当前页的评论数：" + commentListArr.length());
				for (int r = 0; r < commentListArr.length(); r++) {
					JSONObject obj = commentListArr.getJSONObject(r);

					String commentDate = obj.getString("date");

					Commentinfo commentInfo = new Commentinfo();
					commentInfo.setId(UUID.randomUUID().toString());
					commentInfo.setInfoid(uuid);
					commentInfo.setType(1); // 类型：1-景点，2-酒店，3-景点行程
					commentInfo.setCreatedate(new Date());
					commentInfo.setCreator("姚良良");
					commentInfo.setCreatorid("13783985208");
					commentInfo.setContent(obj.toString());
					commentInfo.setCommentdate(commentDate);
					commentInfo.setDatasource("Qunaer");
					// System.out.println("评论内容："+obj.toString());
					commentsList.add(commentInfo);
					// redisService.insertAndGetId(commentInfo);
				}
				redisService.insertBatch(commentsList);
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * @Description TODO
	 * @author 汤玉林
	 * @date 2018年2月9日 下午5:49:59
	 * @action parseTongChengScenicBookNowNew
	 * @param params
	 */
	private void parseTongChengScenicBookNowNew(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				content = content.substring(1, content.length() - 1);
				JSONObject jsonObject = new JSONObject(content);
				JSONArray noticejson = null;
				try {
					noticejson = jsonObject.getJSONArray("Notice");
				} catch (Exception e) {
					// TODO: handle exception
				}
				if (noticejson.length() > 0) {
					JSONObject notice = noticejson.getJSONObject(0);
					JSONArray jsonArray = null;
					try {
						jsonArray = notice.getJSONArray("BItem");
					} catch (Exception e) {
						// TODO: handle exception
					}
					if (jsonArray.length() > 0) {
						for (int i = 0; i < jsonArray.length(); i++) {
							JSONObject object = jsonArray.getJSONObject(i);
							String discountName = object.getString("Name");
							String cont = "";
							switch (discountName) {
							case "特惠政策":
								cont = object.getString("Cont");
								cont = cont.replaceAll("<span>", "")
										.replaceAll("</span>", "");
								Scepriceinfo sceprice = new Scepriceinfo();
								sceprice.setId(UUID.randomUUID().toString());
								sceprice.setScenicid(uuid);
								sceprice.setPricetype(discountName);
								sceprice.setPriceitem(cont);
								sceprice.setCreatedate(new Date());
								sceprice.setCreator("汤玉林");
								sceprice.setCreatorid("tyl13564205515");

								redisService.insertAndGetId(sceprice);
								break;
							default:
								break;
							}
						}

					}
				}

			} catch (Exception e) {

			}
		}

	}

	/**
	 * @Description 解析同程门票评论
	 * @author 汤玉林
	 * @date 2017年12月26日 上午11:45:30
	 * @action parseTongChengScenicComment
	 * @param params
	 */
	private void parseTongChengScenicComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		// 解析评论json
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject comment = new JSONObject(content);
				JSONArray jsonArray = null;
				// 获取评论内容
				try {
					jsonArray = comment.getJSONArray("dpList");
					System.out.println("评论条数：" + jsonArray.length());
				} catch (Exception e) {

				}

				if (jsonArray != null && jsonArray.length() > 0) {

					for (int i = 0; i < jsonArray.length(); i++) {
						// 获取评论内容
						String commentContent = jsonArray.getJSONObject(i)
								.toString();
						// 获取评论时间
						String dpDate = (String) jsonArray.getJSONObject(i)
								.get("dpDate");

						Commentinfo holyrobotCommentinfo = new Commentinfo();
						holyrobotCommentinfo.setType(1);
						holyrobotCommentinfo
								.setId(UUID.randomUUID().toString());
						holyrobotCommentinfo.setInfoid(uuid);
						holyrobotCommentinfo.setCommentdate(dpDate);
						holyrobotCommentinfo.setContent(commentContent);
						holyrobotCommentinfo.setCreatedate(new Date());
						holyrobotCommentinfo.setCreator("陈文奇");
						holyrobotCommentinfo.setDatasource("Tongcheng");
						holyrobotCommentinfo
								.setCreatorid("chenwenqi-13263625152");
						// System.out.println("评论内容："+commentContent);
						redisService.insertAndGetId(holyrobotCommentinfo);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}

	}

	/**
	 * @Description 解析同程门票价格
	 * @author 汤玉林
	 * @date 2017年12月26日 上午11:17:42
	 * @action parseTongChengScenicPrice
	 * @param params
	 */
	private void parseTongChengScenicPrice(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		System.out.println(params.getUrl());
		// 解析价格json
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject ticket = new JSONObject(content);
				JSONArray sceneryPrices = ticket.getJSONArray("SceneryPrices");
				System.out.println(sceneryPrices.length());
				if (sceneryPrices != null && sceneryPrices.length() > 0) {
					for (int i = 0; i < sceneryPrices.length(); i++) {
						JSONArray channelList = sceneryPrices.getJSONObject(i)
								.getJSONArray("ChannelPriceModelEntityList");
						System.out.println(channelList.length());
						if (channelList != null & channelList.length() > 0) {
							for (int j = 0; j < channelList.length(); j++) {
								JSONArray channelEntityList = channelList
										.getJSONObject(j).getJSONArray(
												"ChannelPriceEntityList");
								if (channelEntityList != null
										&& channelEntityList.length() > 0) {
									for (int k = 0; k < channelEntityList
											.length(); k++) {
										// 获取价格
										JSONObject channel = channelEntityList
												.getJSONObject(k);
										Scepriceinfo holyrobotScepriceinfo = new Scepriceinfo();

										holyrobotScepriceinfo.setId(UUID
												.randomUUID().toString());
										holyrobotScepriceinfo.setScenicid(uuid);
										// 获取门票类型（单票、套票等等）
										Object productUnitName = channel
												.get("ProductUnitName");
										if (JSONObject.NULL
												.equals(productUnitName)) {
											productUnitName = "";
										}
										holyrobotScepriceinfo
												.setTickettype(productUnitName
														.toString());
										// 获取价格类型（成人、儿童、其他等）
										Object consumers = channel
												.get("ConsumersTypeName");
										if (JSONObject.NULL.equals(consumers)) {
											consumers = "";
										}
										holyrobotScepriceinfo
												.setPricetype(consumers
														.toString());
										// 市场价
										Object amount = channel.get("Amount");
										if (JSONObject.NULL.equals(amount)) {
											amount = "";
										}
										holyrobotScepriceinfo
												.setMarketingprice(amount
														.toString());
										// 价格条目
										Object ticketName = channel
												.get("TicketName");
										if (JSONObject.NULL.equals(ticketName)) {
											ticketName = "";
										}
										holyrobotScepriceinfo
												.setPriceitem(ticketName
														.toString());
										// 价格条目
										Object amountAdvice = channel
												.get("AmountAdvice");
										if (JSONObject.NULL
												.equals(amountAdvice)) {
											amountAdvice = "";
										}
										holyrobotScepriceinfo
												.setSaleprice(amountAdvice
														.toString());

										// 预定时间
										Object priceTimeLimit = channel
												.get("PriceTimeLimit");
										if (!JSONObject.NULL
												.equals(priceTimeLimit)) {
											String priceTime = (String) priceTimeLimit;
											holyrobotScepriceinfo
													.setSalecondition(priceTime);
										}
										JSONArray ticketTagEntityList = null;
										// 优惠信息
										try {
											ticketTagEntityList = channel
													.getJSONArray("TicketTagEntityList");
										} catch (Exception e) {

										}

										if (ticketTagEntityList != null
												&& ticketTagEntityList.length() >= 2) {
											String discount = "";
											for (int l = 0; l < ticketTagEntityList
													.length(); l++) {
												JSONObject discountObj = ticketTagEntityList
														.getJSONObject(l);
												int tagType = discountObj
														.getInt("TagType");
												if (tagType == 2) {
													discount += discountObj
															.getString("Name")
															+ " ";
												}
											}
											holyrobotScepriceinfo
													.setDiscountinfo(discount);
										}
										/*
										 * if(!getTicketMode.equals("") &&
										 * getTicketMode!=null &&
										 * !getTicketMode.equals("null")){
										 * 
										 * holyrobotScepriceinfo.setDiscountinfo(
										 * (String) getTicketMode); }
										 */
										holyrobotScepriceinfo
												.setCreatedate(new Date());
										holyrobotScepriceinfo.setCreator("陈文奇");
										holyrobotScepriceinfo
												.setCreatorid("chenwenqi-13263625152");
										System.out.println(productUnitName
												+ "\n" + consumers + "\n"
												+ ticketName + "\n"
												+ amountAdvice + "\n"
												+ priceTimeLimit);
										redisService
												.insertAndGetId(holyrobotScepriceinfo);
									}

								}

							}
						}
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * @Description 解析同程门票评论第一页数据
	 * @author 汤玉林
	 * @date 2017年12月26日 上午11:17:40
	 * @action parseTongchengScenicCommentFirst
	 * @param params
	 */
	private void parseTongchengScenicCommentFirst(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		String sid = url.substring(url.indexOf("sid") + 4, url.length());
		String content = params.getContent();
		try {
			if (StringUtils.isNotBlank(content)) {
				JSONObject pcomment = new JSONObject(content);
				Sceinfo sceInfo = new Sceinfo();
				sceInfo.setId(uuid);
				String grade = pcomment.getString("degreeLevel");
				String gradenum = pcomment.getString("totalNum");
				// 获取好评率
				sceInfo.setGrade(grade + "%");
				// 获取评论总数
				sceInfo.setGradenum(gradenum);
				System.out.println("评分：" + grade + ";评论人数：" + gradenum);
				sceInfo.setCreatedate(new Date());
				redisService.insertAndGetId(sceInfo);
				JSONObject pageInfo = (JSONObject) pcomment.get("pageInfo");
				int totalPage = (int) pageInfo.get("totalPage");
				for (int i = 0; i < totalPage; i++) {
					String commentUrl = "https://www.ly.com/scenery/AjaxHelper/DianPingAjax.aspx?action=GetDianPingList&pageSize=20&sid="
							+ sid + "&page=" + i;
					Params commentParams = new Params();
					commentParams.setUuid(uuid);
					commentParams.setUrl(commentUrl);
					commentParams.setType(Param.TONGCHENG_SCENIC_COMMENT);
					commentParams.setDataSource(Param.TONGCHENG);
					commentParams.setHttpType(Param.GET);

					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, commentParams);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * 
	 * @Description 解析同城行程评论信息
	 * @author 赵乐
	 * @date 2017年12月26日 上午11:31:08
	 * @action parseTongchengRouteComment
	 * @param @param params
	 * @return void
	 */
	private void parseTongchengRouteComment(Params params) {
		String content = params.getContent();
		String uuid = params.getUuid();
		try {
			JSONObject comJSON = new JSONObject(content);
			String responseStr = comJSON.get("response").toString();
			JSONObject responseJSON = new JSONObject(responseStr);
			String bodyStr = responseJSON.get("body").toString();
			JSONObject bodyJSON = new JSONObject(bodyStr);
			String dpList = bodyJSON.get("dpList").toString();
			JSONArray comArr = new JSONArray(dpList);
			for (int k = 0; k < comArr.length(); k++) {
				String comment = comArr.get(k).toString();
				Commentinfo commentInfo = new Commentinfo();
				commentInfo.setId(UUID.randomUUID().toString());
				commentInfo.setInfoid(uuid);
				commentInfo.setType(3); // 类型：1-景点，2-酒店，3-景点行程
				commentInfo.setCreatedate(new Date());
				commentInfo.setCreator("姚良良");
				commentInfo.setCreatorid("13783985208");
				commentInfo.setContent(comment);
				redisService.insertAndGetId(commentInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * 
	 * @Description 解析同城行程评论链接
	 * @author 赵乐
	 * @date 2017年12月26日 上午11:30:41
	 * @action parseTongchengRouteCommentFirst
	 * @param @param params
	 * @return void
	 */
	private void parseTongchengRouteCommentFirst(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		String content = params.getContent();
		try {
			JSONObject commentJSON = new JSONObject(content);
			String response = commentJSON.get("response").toString();

			JSONObject responseOBJ = new JSONObject(response);
			String body = responseOBJ.get("body").toString();
			JSONObject bodyOBJ = new JSONObject(body);
			String pageInfo = bodyOBJ.get("pageInfo").toString();
			JSONObject pageInfoOBJ = new JSONObject(pageInfo);
			// 获得总页数
			Integer pageCount = Integer.valueOf(pageInfoOBJ.get("totalPage")
					.toString());
			for (int j = 1; j <= pageCount; j++) {
				// 拼接评论请求的URL
				String comUrl = url.replace("&page=1", "&page=" + j);
				Params params2 = new Params();
				params2.setUuid(uuid);
				params2.setUrl(comUrl);
				params2.setDataSource(Param.TONGCHENG);
				params2.setType(Param.TONGCHENG_ROUTE_COMMENT);
				params2.setHttpType(Param.GET);

				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params2);
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * 
	 * @Description 解析同程行程价格信息
	 * @author 赵乐
	 * @date 2017年12月26日 上午11:29:49
	 * @action parseTongchengRoutePrice
	 * @param @param params
	 * @return void
	 */
	private void parseTongchengRoutePrice(Params params) {
		String uuid = params.getUuid();
		String priceStr = params.getContent();
		try {
			// 对JSON数据进行解析
			String jsonStr = (priceStr.substring(1, priceStr.length() - 1))
					.replaceAll("\\\\", "");
			System.out.println(jsonStr);
			JSONObject json = new JSONObject(jsonStr);
			String priceList = json.get("priceList").toString();
			JSONArray dataArr = new JSONArray(priceList);
			// 记录保存行程价钱的天数
			for (int i = 0; i < dataArr.length(); i++) {
				JSONObject priceDateJson = new JSONObject(dataArr.get(i)
						.toString());
				String priceDate = priceDateJson.get("Date").toString();
				String lowestPrice = priceDateJson.get("Price").toString();
				String dayOfWeek = DateUtil.dateToWeek(priceDate);
				// 创建HolyrobotRoutepriceinfo对象并设置相关属性
				Routepriceinfo routepriceInfo = new Routepriceinfo();
				routepriceInfo.setId(UUID.randomUUID().toString());
				routepriceInfo.setCreator("姚良良");
				routepriceInfo.setCreatorid("13783985208");
				routepriceInfo.setCreatedate(new Date());
				routepriceInfo.setRouteid(uuid);
				routepriceInfo.setPricedate(priceDate);
				routepriceInfo.setLowestprice(lowestPrice);
				routepriceInfo.setDayofweek(dayOfWeek);
				redisService.insertAndGetId(routepriceInfo);
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * 
	 * @Description 解析同程行程景点评论
	 * @author 赵乐
	 * @date 2017年12月26日 上午11:29:16
	 * @action parseTongchengStrokeComment
	 * @param @param params
	 * @return void
	 */
	private void parseTongchengStrokeComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonOb2 = new JSONObject(content);
				JSONObject jsonResponse2 = jsonOb2.getJSONObject("response");
				JSONObject jsonBody2 = jsonResponse2.getJSONObject("body");
				// 获取当前页的所有评论
				JSONArray jsonArray = jsonBody2.getJSONArray("dpList");
				// 获取评论信息
				for (int j = 0; j < jsonArray.length(); j++) {
					Commentinfo commentinfo = new Commentinfo();
					String commentinfoId = UUID.randomUUID().toString();
					commentinfo.setId(commentinfoId);
					commentinfo.setInfoid(uuid);
					commentinfo.setType(1);
					commentinfo.setCreatedate(new Date());
					commentinfo.setDatasource("Tongcheng");
					commentinfo.setCreator("赵乐");
					commentinfo.setCreatorid("15736708180");
					JSONObject jsonObjectComment = jsonArray.getJSONObject(j);
					// 评论内容
					String commentContent = jsonObjectComment.toString();
					commentinfo.setContent(commentContent);
					redisService.insertAndGetId(commentinfo);
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到评论，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	private void parseTongchengStrokeCommentFirst(Params params) {
		// TODO Auto-generated method stub
		String uuid = params.getUuid();
		String url = params.getUrl();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonOb = new JSONObject(content);
				JSONObject jsonResponse = jsonOb.getJSONObject("response");
				JSONObject jsonBody = jsonResponse.getJSONObject("body");
				JSONObject jsonpageInfo = jsonBody.getJSONObject("pageInfo");
				// 补充评论评论人数
				String commentNum = jsonpageInfo.get("totalCount").toString();

				Sceinfo sceinfo = new Sceinfo();
				sceinfo.setId(uuid);
				sceinfo.setGradenum(commentNum);
				redisService.insertAndGetId(sceinfo);

				// 取评论总页数
				String pageNumstr = jsonpageInfo.get("totalPage").toString();

				Integer pageNum = Integer.parseInt(pageNumstr);
				for (int i = 1; i <= pageNum; i++) {
					System.out.println("评论分页第" + i + "页");
					String url2 = url.replace("page:1", "page:" + i);

					Params params2 = new Params();
					params2.setUuid(uuid);
					params2.setUrl(url2);
					params2.setDataSource(Param.TONGCHENG);
					params2.setHttpType(Param.GET);
					params2.setType(Param.TONGCHENG_STROKE_COMMENT);

					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, params2);
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到评论，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 解析同程酒店评论信息
	 * @author 汤玉林
	 * @date 2017年12月22日 上午11:41:24
	 * @action parseTongChengHotelComment
	 * @param params
	 */
	private void parseTongChengHotelComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONObject response = jsonObject.getJSONObject("response");
				JSONObject body = response.getJSONObject("body");
				JSONArray dpList = body.getJSONArray("dpList");
				for (int k = 0; k < dpList.length(); k++) {
					JSONObject obj = dpList.getJSONObject(k);
					String dbDate = obj.getString("dpDate");

					Commentinfo commentInfo = new Commentinfo();
					commentInfo.setType(2);
					commentInfo.setCommentdate(dbDate);
					commentInfo.setContent(obj.toString());
					commentInfo.setId(UUID.randomUUID().toString());
					commentInfo.setInfoid(uuid);
					commentInfo.setCreatedate(new Date());
					commentInfo.setCreator("tyl");
					commentInfo.setDatasource("Tongcheng");
					commentInfo.setCreatorid(creatorID);

					redisService.insertAndGetId(commentInfo);
					System.out.println("评论内容：" + obj.toString() + ";评论日期："
							+ dbDate);
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到评论，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * @Description 解析同程酒店房型信息
	 * @author 汤玉林
	 * @date 2017年12月22日 上午11:40:40
	 * @action parseTongChengHotelRoom
	 * @param params
	 */
	private void parseTongChengHotelRoom(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		String content = params.getContent();
		String priceDate = url.substring(url.indexOf("ComeDate") + 9,
				url.indexOf("LeaveDate") - 1);
		System.out.println(content);
		if (StringUtils.isNotBlank(content)) {
			if (content.contains("_leonid___capurl__")) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
				return;
			}
			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONObject hotelInfo = jsonObject.getJSONObject("HotelInfo");
				JSONArray roomInfo = null;
				try {
					roomInfo = hotelInfo.getJSONArray("RoomInfo");
				} catch (Exception e) {

				}
				if (roomInfo != null) {
					int count = 1;
					for (int i = 0; i < roomInfo.length(); i++) {
						JSONObject obj = roomInfo.getJSONObject(i);
						String roomType = obj.getString("RoomName");
						String bedType = obj.getString("Bed");
						String isaddBed = obj.getString("AddBedRemark");
						String bedSize = obj.getString("BedWidth");
						// 暂时没有找到wifi的字段
						String iswifi = "";
						String floor = obj.getString("Floor");
						Integer peopleCount = obj.getInt("InMost");

						Roombasicinfo room = new Roombasicinfo();
						String roomId = UUID.randomUUID().toString();
						room.setBedtype(bedType);
						room.setBedsize(bedSize);
						room.setFloor(floor);
						room.setIsaddbed(isaddBed);
						room.setRoomtype(roomType);
						room.setPeoplecount(peopleCount.toString());
						room.setId(roomId);
						room.setHotelid(uuid);
						room.setCreatedate(new Date());
						room.setCreator("tyl");
						room.setCreatorid(creatorID);

						redisService.insertAndGetId(room);

						String photo = obj.getString("Photo");
						Pictureinfo pictureInfo = new Pictureinfo();
						pictureInfo.setImgurl(photo);
						pictureInfo.setSort(count++);
						pictureInfo.setType(5);
						pictureInfo.setDownload(0);
						pictureInfo.setId(UUID.randomUUID().toString());
						pictureInfo.setInfoid(roomId);
						pictureInfo.setCreatedate(new Date());
						pictureInfo.setCreator("tyl");
						pictureInfo.setCreatorid(creatorID);

						redisService.insertAndGetId(pictureInfo);

						// 获取房型价格
						JSONArray policyInfo = null;
						try {
							policyInfo = obj.getJSONArray("PolicyInfo");
						} catch (Exception e) {
							// TODO: handle exception
						}
						System.out.println("\n房间名称：" + roomType + ";床型："
								+ bedType + ";床大小：" + bedSize + ";楼层：" + floor
								+ ";" + "是否可加床：" + isaddBed + ";房间可住人数："
								+ peopleCount);
						if (policyInfo != null && policyInfo.length() > 0) {
							for (int k = 0; k < policyInfo.length(); k++) {
								JSONObject roomPriceJson = policyInfo
										.getJSONObject(k);
								// 产品名称
								String productName = roomPriceJson
										.getString("PolicyName");
								// 价格
								Integer price = roomPriceJson
										.getInt("AvgPrice");
								// 是否可预定暂时没找到字段
								String isbooking = "";
								// wifi字段也暂时没有找到
								String priceWifi = "";
								String ishasBreakfast = "";
								// 获取早餐和窗户
								JSONObject roomsub = roomPriceJson
										.getJSONObject("RoomSub");
								int breakfast = roomsub.getInt("Breakfast");
								if (breakfast == 0) {
									ishasBreakfast = "无早";
								}
								if (breakfast == 1) {
									ishasBreakfast = "单早";
								}
								if (breakfast == 2) {
									ishasBreakfast = "双早";
								}
								// 窗户
								int window = roomsub.getInt("HasWindow");
								String hasWindow = "";
								if (window > 0) {
									hasWindow = "有窗";
								} else {
									hasWindow = "无窗";
								}
								// 获取取消规则
								JSONObject ruleDetail = roomPriceJson
										.getJSONObject("CancelRules");
								String iscancle = ruleDetail
										.getString("OverviewDescription");
								// 付款方式
								JSONObject guaranteeList = roomPriceJson
										.getJSONObject("GuaranteeList");
								JSONArray guaranteeArray = guaranteeList
										.getJSONArray("Guarantee");
								String payMethod = "";
								if (guaranteeArray.length() > 0) {
									JSONObject guarantee = guaranteeArray
											.getJSONObject(0);
									payMethod = guarantee
											.getString("Description");
								}

								Roomprice roomPrice = new Roomprice();
								roomPrice.setProductname(productName);
								roomPrice.setIscancled(iscancle);
								roomPrice.setIshasbreakfast(ishasBreakfast);
								roomPrice.setIswifi(iswifi);
								roomPrice.setPrice(price.toString());
								roomPrice.setIsbooking(isbooking);
								roomPrice.setIswindow(hasWindow);
								roomPrice.setPaymethod(payMethod);
								roomPrice.setDate(priceDate);
								roomPrice.setId(UUID.randomUUID().toString());
								roomPrice.setHotelid(uuid);
								roomPrice.setRoomid(roomId);
								roomPrice.setCreatedate(new Date());
								roomPrice.setCreator("tyl");
								roomPrice.setCreatorid(creatorID);

								redisService.insertAndGetId(roomPrice);

								System.out.println("产品名称：" + productName
										+ ";是否可取消：" + iscancle + ";是否含早餐："
										+ ishasBreakfast + ";是否含wifi：" + iswifi
										+ ";价格：" + price + ";是否可预订："
										+ isbooking + ";是否含窗户：" + hasWindow
										+ ";支付方式：" + payMethod);
							}
						}

					}
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}

	}

	/**
	 * @Description 途牛行程评论解析
	 * @author 汤玉林
	 * @date 2017年12月21日 上午11:35:57
	 * @action parseTuniuRouteComment
	 * @param params
	 */
	private void parseTuniuRouteComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject comJSON = new JSONObject(content);
				String data = comJSON.get("data").toString();
				JSONObject jsonData = new JSONObject(data);
				String comList = jsonData.get("list").toString();
				JSONArray comArr = new JSONArray(comList);
				for (int k = 0; k < comArr.length(); k++) {
					try {
						JSONObject comment = new JSONObject(comArr.get(k)
								.toString());
						// 获取评论时间
						String commentDate = comment.get("remarkTime")
								.toString();
						Commentinfo commentInfo = new Commentinfo();
						commentInfo.setId(UUID.randomUUID().toString());
						commentInfo.setInfoid(uuid);
						commentInfo.setType(3); // 类型：1-景点，2-酒店，3-景点行程
						commentInfo.setCreatedate(new Date());
						commentInfo.setDatasource("Tuniu");
						commentInfo.setCreator("姚良良");
						commentInfo.setCreatorid("13783985208");
						commentInfo.setContent(comment.toString());
						commentInfo.setCommentdate(commentDate);

						redisService.insertAndGetId(commentInfo);
					} catch (Exception e) {

					}
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到评论，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * @Description 途牛行程价格获取
	 * @author 汤玉林
	 * @date 2017年12月21日 上午11:35:09
	 * @action parseTuniuRoutePrice
	 * @param params
	 */
	private void parseTuniuRoutePrice(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		if (url.endsWith("&bookCityCode=&departCityCode=&backCityCode=")) {
			System.err.println("无效连接");
		} else {
			String content = params.getContent();
			if (StringUtils.isNotBlank(content)) {
				try {
					// 对JSON数据进行解析
					JSONObject json = new JSONObject(content);
					String data = json.get("data").toString();
					JSONObject dataJson = new JSONObject(data);
					String calendars = dataJson.get("calendars").toString();
					JSONArray calendarsArr = new JSONArray(calendars);
					// 记录保存行程价钱的天数
					int k = 0;
					System.out.println(url + " 的行程价钱共有 "
							+ calendarsArr.length() + " 条!");
					for (int i = 0; i < calendarsArr.length(); i++) {
						JSONObject priceDateJson = new JSONObject(calendarsArr
								.get(i).toString());
						String priceDate = priceDateJson.get("departDate")
								.toString();
						String lowestPrice = priceDateJson.get("startPrice")
								.toString();
						String dayOfWeek = DateUtil.dateToWeek(priceDate);
						// 创建HolyrobotRoutepriceinfo对象并设置相关属性
						Routepriceinfo routepriceInfo = new Routepriceinfo();
						routepriceInfo.setId(UUID.randomUUID().toString());
						routepriceInfo.setRouteid(uuid);
						routepriceInfo.setPricedate(priceDate);
						routepriceInfo.setLowestprice(lowestPrice);
						routepriceInfo.setDayofweek(dayOfWeek);
						routepriceInfo.setCreator("姚良良");
						routepriceInfo.setCreatorid("13783985208");
						routepriceInfo.setCreatedate(new Date());

						redisService.insertAndGetId(routepriceInfo);
						k++;
					}
					System.out.println(url + " 的行程价钱共有 "
							+ calendarsArr.length() + " 条,共获取" + k + "条!");
				} catch (Exception e) {
					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, params);
				}
			} else {
				System.out.println("未获取到信息，重新放入队列");
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}

	}

	/**
	 * @Description 途牛门票评论解析
	 * @author 汤玉林
	 * @date 2017年12月21日 上午9:39:49
	 * @action parseTuniuScenicComment
	 * @param params
	 */
	private void parseTuniuScenicComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONArray contents = jsonObject.getJSONArray("contents");
				for (int i = 0; i < contents.length(); i++) {
					JSONObject obj = contents.getJSONObject(i);

					JSONObject compTextContent = obj
							.getJSONObject("compTextContent");
					Commentinfo holyrobotCommentinfo = new Commentinfo();
					// 内容
					holyrobotCommentinfo.setContent(obj.toString());
					// 评论日期
					String commentDate = compTextContent
							.getString("updateTime");
					holyrobotCommentinfo.setCommentdate(commentDate);

					holyrobotCommentinfo.setId(UUID.randomUUID().toString());
					holyrobotCommentinfo.setInfoid(uuid);
					holyrobotCommentinfo.setType(1);
					holyrobotCommentinfo.setCreatedate(new Date());
					holyrobotCommentinfo.setDatasource("Tuniu");
					holyrobotCommentinfo.setCreator("tyl");
					holyrobotCommentinfo.setCreatorid("tyl13564205515");

					redisService.insertAndGetId(holyrobotCommentinfo);
					System.out.println("评论内容：" + obj.toString());
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * 
	 * @Description 解析途牛酒店评论首页url获取酒店评论所有url
	 * @author 赵乐
	 * @date 2018年1月14日 下午4:32:51
	 * @action parseTuniuHotelCommentFirst
	 * @param @param params
	 * @return void
	 */
	private void parseTuniuHotelCommentFirst(Params params) {
		// TODO Auto-generated method stub
		String uuid = params.getUuid();
		Document doc = Jsoup.parse(params.getContent());
		String url = params.getUrl();
		try {
			int commentCount = 0;
			Elements comments = doc.select("input");
			for (Element element : comments) {
				String attr = element.attr("name");
				if ("groupPages".equals(attr)) {
					String commentNum = element.attr("value");
					if (!"".equals(commentNum)) {
						commentCount = NumUtils.getInteger(commentNum);
					} else {
						commentCount = 0;
					}
					break;
				}
			}
			// 获取评论总页数
			System.out.println("tuniu评论总数目" + commentCount);
			if (commentCount >= 1) {
				for (int i = 1; i <= commentCount; i++) {
					String commentURL = url.replace("&p=1", "&p=" + i);

					Params para = new Params();
					para.setUuid(uuid);
					para.setUrl(commentURL);
					para.setHttpType(Param.GET);
					para.setType(Param.TUNIU_HOTEL_COMMENT);
					para.setDataSource(Param.TUNIU);
					para.setHeader("X-Requested-With", "XMLHttpRequest");
					System.out.println("存入途牛评论url" + commentURL);

					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, para);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * @Description 解析途牛酒店评论数据
	 * @author 汤玉林
	 * @date 2017年12月15日 上午10:03:08
	 * @action parseTuniuHotelComment
	 * @param params
	 */
	private void parseTuniuHotelComment(Params params) {
		String uuid = params.getUuid();
		Document doc = Jsoup.parse(params.getContent());
		try {
			Elements cmtElements = doc.select("div.u5.clearfix");
			System.out.println("该页评论长度为：" + cmtElements.size());
			for (Element cmt : cmtElements) {
				Elements time = cmt.select("div.a2>div.time>span");
				String commentDate = time.get(0).text();
				Commentinfo commentInfo = new Commentinfo();
				commentInfo.setType(2);
				commentInfo.setId(UUID.randomUUID().toString());
				commentInfo.setInfoid(uuid);
				commentInfo.setCommentdate(commentDate);
				commentInfo.setContent(cmt.toString());
				commentInfo.setCreatedate(new Date());
				commentInfo.setCreator("tyl");
				commentInfo.setCreatorid("tyl13564205515");
				commentInfo.setDatasource("Tuniu");
				// 插入redis
				redisService.insertAndGetId(commentInfo);

			}
		} catch (Exception e) {
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * 
	 * @Description 途牛酒店介绍解析
	 * @author xdl
	 * @date 2018年2月12日 上午11:03:00
	 * @action parseTuniuHOtelIntroduction
	 * @return void
	 */
	private void parseTuniuHotelIntroduction(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONObject hotel = jsonObject.getJSONObject("data")
						.getJSONObject("hotel");
				if (!JSONObject.NULL.equals(hotel)) {
					String introduction = hotel.getString("detail");
					System.out.println("酒店介绍：" + introduction);
					Hotelinfo hotelinfo = new Hotelinfo();
					hotelinfo.setId(uuid);
					hotelinfo.setIntroduction(introduction);
					redisService.insertAndGetId(hotelinfo);
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * 
	 * @Description 解析途牛酒店图片信息
	 * @author 赵乐
	 * @date 2018年1月14日 下午3:58:39
	 * @action parseTuniuHotelPicture
	 * @param @param params
	 * @return void
	 */
	private void parseTuniuHotelPicture(Params params) {
		// TODO Auto-generated method stub
		String uuid = params.getUuid();
		String url = params.getUrl();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONArray jsonArray = null;
				try {
					JSONObject jsonObject = new JSONObject(content);
					JSONObject jsonObject2 = jsonObject.getJSONObject("data");
					jsonArray = jsonObject2.getJSONArray("all");
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				if (jsonArray != null) {
					int length = jsonArray.length();
					length = length > 10 ? 10 : length;
					for (int i = 0; i < length; i++) {
						JSONObject obj = (JSONObject) jsonArray.get(i);
						String imgsrc = obj.getString("big");

						Pictureinfo picture = new Pictureinfo();
						String pictureId = UUID.randomUUID().toString();
						picture.setId(pictureId);
						picture.setInfoid(uuid);
						picture.setImgurl(imgsrc);
						picture.setSort(i + 1);
						picture.setType(4);
						picture.setInfoid(uuid);
						picture.setDownload(0);
						picture.setCreatedate(new Date());
						picture.setCreator("tyl");
						picture.setCreatorid(creatorID);
						redisService.insertAndGetId(picture);

						System.out.println("图片地址：" + imgsrc);
					}
				}

			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	/**
	 * @Description 解析途牛酒店房型信息
	 * @author 汤玉林
	 * @date 2017年12月15日 上午10:01:57
	 * @action parseTuniuHotelRoom
	 * @param params
	 */
	private void parseTuniuHotelRoom(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		String priceDate = url.substring(url.indexOf("checkindate") + 12,
				url.indexOf("checkoutdate") - 1);
		String content = params.getContent();
		int count = 1;
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONArray rooms = null;
				try {
					rooms = jsonObject.getJSONArray("rooms");
				} catch (Exception e) {
					rooms = null;
				}
				if (rooms != null) {
					// System.out.println(rooms);
					System.out.println("房间列表长度：" + rooms.length());
					for (int i = 0; i < rooms.length(); i++) {
						JSONObject obj = rooms.getJSONObject(i);
						Object roomName = null;
						try {
							roomName = obj.get("name");
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (jsonObject.NULL.equals(roomName)) {
							roomName = "";
						}
						Object roomBedType = null;
						try {
							roomBedType = obj.get("roomBedType");
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (jsonObject.NULL.equals(roomBedType)) {
							roomBedType = "";
						}

						Object roomBedSize = null;
						try {
							roomBedSize = obj.get("roomBedInfo");
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (jsonObject.NULL.equals(roomBedSize)) {
							roomBedSize = "";
						}

						Object hasWindowShow = null;
						try {
							hasWindowShow = obj.get("hasWindowShow");
						} catch (Exception e) {
							// TODO: handle exception
						}
						if (jsonObject.NULL.equals(hasWindowShow)) {
							hasWindowShow = "";
						}

						JSONObject facilities = obj.getJSONObject("facilities");
						// String isaddbed=obj.getString("isExtraBedShow");
						Object isaddbed = facilities.get("加床");
						if (jsonObject.NULL.equals(isaddbed)) {
							isaddbed = "";
						}
						Integer peopleCount = facilities.getInt("最多入住人数");
						Object floor = null;
						try {
							floor = facilities.get("楼层");
						} catch (Exception e) {
							// TODO: handle exception
						}

						if (jsonObject.NULL.equals(floor)) {
							floor = "";
						}
						Roombasicinfo roomInfo = new Roombasicinfo();
						String roomInfoId = UUID.randomUUID().toString();
						roomInfo.setId(roomInfoId);
						roomInfo.setHotelid(uuid);
						roomInfo.setBedtype(roomBedType.toString());
						roomInfo.setBedsize(roomBedSize.toString());
						roomInfo.setFloor(floor.toString());

						roomInfo.setIsaddbed(isaddbed.toString());
						roomInfo.setRoomtype(roomName.toString());
						roomInfo.setCreatedate(new Date());
						roomInfo.setCreator("tyl");
						roomInfo.setCreatorid("tyl13564205515");
						roomInfo.setPeoplecount(peopleCount.toString());
						// 插入redis
						redisService.insertAndGetId(roomInfo);

						JSONArray picArray = null;
						try {
							picArray = obj.getJSONArray("roomPics");
						} catch (Exception e) {
							picArray = null;
						}
						// 获取图片
						String roomPicUrl = "";
						if (picArray != null && picArray.length() > 0) {
							JSONObject picObj = picArray.getJSONObject(0);
							roomPicUrl = picObj.getString("url");

							Pictureinfo pictureInfo = new Pictureinfo();
							pictureInfo.setId(UUID.randomUUID().toString());
							pictureInfo.setInfoid(uuid);
							pictureInfo.setImgurl(roomPicUrl);
							pictureInfo.setSort(count++);
							pictureInfo.setType(5);
							pictureInfo.setDownload(0);
							pictureInfo.setCreatedate(new Date());
							pictureInfo.setCreator("tyl");
							pictureInfo.setCreatorid("tyl13564205515");
							// 插入redis
							redisService.insertAndGetId(pictureInfo);
						}
						/*
						 * System.out.println("\n房间名称："+roomName+";床型："+roomBedType
						 * +";床大小："+roomBedSize+";楼层："+floor+";" +
						 * "是否可加床："+isaddbed+";房间可住人数："+peopleCount);
						 */
						// 获取价格
						JSONArray ratePlans = obj.getJSONArray("ratePlans");
						for (int k = 0; k < ratePlans.length(); k++) {
							JSONObject roomPriceJson = ratePlans
									.getJSONObject(k);
							String productName = roomPriceJson
									.getString("ratePlanName");
							Integer price = roomPriceJson
									.getInt("averagePrice");
							// 是否可预订
							JSONObject operation = roomPriceJson
									.getJSONObject("operation");
							String order = operation.getString("name");
							String breakfast = roomPriceJson
									.getString("breakfast");
							// 是否可取消
							JSONObject cancleJson = roomPriceJson
									.getJSONObject("cancel");
							String cancle = cancleJson.getString("name");
							JSONObject optimalData = null;
							try {
								optimalData = roomPriceJson
										.getJSONObject("optimalData");
							} catch (Exception e) {
								optimalData = null;
							}
							String optimalFlag = "";
							if (optimalData != null) {
								optimalFlag = optimalData
										.getString("optimalFlag");
							}

							String iswifi = roomPriceJson
									.getString("networkDesc");
							// 支付方式
							String payMethod = "";
							JSONObject tags = roomPriceJson
									.getJSONObject("tags");
							boolean prepay = tags.getBoolean("prepay");
							boolean guarantee = tags.getBoolean("guarantee");
							if (prepay) {
								payMethod = "在线付";
							} else if (guarantee) {
								payMethod = "担保";
							} else {
								payMethod = "到店支付";
							}
							Roomprice roomPrice = new Roomprice();
							roomPrice.setId(UUID.randomUUID().toString());
							// 关联房型id
							roomPrice.setRoomid(roomInfoId);
							roomPrice.setHotelid(uuid);
							roomPrice
									.setProductname(productName
											+ (StringUtils
													.isNotBlank(optimalFlag) ? "("
													+ optimalFlag + ")"
													: ""));
							roomPrice.setIscancled(cancle);
							roomPrice.setIshasbreakfast(breakfast);
							roomPrice.setIswifi(iswifi);
							roomPrice.setPrice(price.toString());
							roomPrice.setIsbooking(order);
							roomPrice.setIswindow(hasWindowShow.toString());
							roomPrice.setPaymethod(payMethod);
							roomPrice.setDate(priceDate);
							roomPrice.setCreatedate(new Date());
							roomPrice.setCreator("tyl");
							roomPrice.setCreatorid("tyl13564205515");

							// 放入redis
							redisService.insertAndGetId(roomPrice);
							// System.out.println("产品名称："+productName+";是否可取消："+cancle+";是否含早餐："+breakfast+";是否含wifi："+iswifi
							// +";价格："+price+";是否可预订："+order+";是否含窗户："+hasWindowShow+";支付方式："+payMethod);
						}
					}
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 解析携程行程评论信息(分两种类型)
	 * @author 汤玉林
	 * @date 2017年12月15日 下午2:29:49
	 * @action parseCtripRouteComment
	 * @param params
	 */
	private void parseCtripRouteComment(Params params) {
		String url = params.getUrl();
		String uuid = params.getUuid();
		String content = params.getContent();
		// 跟团游和半自助游的解析
		if (url.contains("http://vacations.ctrip.com/bookingnext/Comment/Search")) {
			try {
				// 解析JSON数据
				JSONObject comjson = new JSONObject(content);
				String dataStr = comjson.get("data").toString();
				JSONObject jsonData = new JSONObject(dataStr);
				String commentsInfoList = jsonData.get("CommentsInfoList")
						.toString();
				JSONArray commentArr = new JSONArray(commentsInfoList);
				System.out.println(commentArr);
				// 循环取出评论的相关信息
				for (int k = 0; k < commentArr.length(); k++) {
					try {
						JSONObject comMsgJson = new JSONObject(commentArr
								.get(k).toString());
						String commentDate = comMsgJson.get("CommentDate")
								.toString();

						Commentinfo commentInfo = new Commentinfo();
						commentInfo.setId(UUID.randomUUID().toString());
						commentInfo.setInfoid(uuid);
						commentInfo.setType(3); // 类型：1-景点，2-酒店，3-景点行程
						commentInfo.setContent(comMsgJson.toString());
						commentInfo.setCommentdate(commentDate);
						commentInfo.setDatasource("Ctrip");
						commentInfo.setCreatedate(new Date());
						commentInfo.setCreator("姚良良");
						commentInfo.setCreatorid("13783985208");
						redisService.insertAndGetId(commentInfo);
						System.out
								.println("跟团游评论信息---" + comMsgJson.toString());
					} catch (Exception e) {
						e.printStackTrace();
					} finally {
						int m = k + 1;
						System.out.println("当前页行程评论共" + commentArr.length()
								+ "条，已抓取" + m + "条!");
					}
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}

			// 自由行的解析
		} else if (url
				.contains("http://online.ctrip.com/restapi/soa2/12447/json/GetCommentInfoList")) {
			// 解析JSON数据
			try {
				JSONObject comObj = new JSONObject(content);
				String dataStr = comObj.get("Data").toString();
				JSONObject jsonData = new JSONObject(dataStr);
				String commentsInfoList = jsonData.get("CommentInfoList")
						.toString();
				JSONArray commentArr = new JSONArray(commentsInfoList);
				// 循环取出评论的相关信息
				for (int k = 0; k < commentArr.length(); k++) {
					try {
						JSONObject comMsgJson = new JSONObject(commentArr
								.get(k).toString());
						String commentDate = comMsgJson.get("SubmitTime")
								.toString();

						Commentinfo commentInfo = new Commentinfo();
						commentInfo.setType(3); // 类型：1-景点，2-酒店，3-景点行程
						commentInfo.setId(UUID.randomUUID().toString());
						commentInfo.setInfoid(uuid);
						commentInfo.setContent(comMsgJson.toString());
						commentInfo.setCommentdate(commentDate);
						commentInfo.setDatasource("Ctrip");
						commentInfo.setCreatedate(new Date());
						commentInfo.setCreator("姚良良");
						commentInfo.setCreatorid("13783985208");
						redisService.insertAndGetId(commentInfo);
						System.out.println("自由行评论信息--" + comMsgJson.toString());
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}

	}

	/**
	 * @Description 解析行程评论
	 * @author 汤玉林
	 * @date 2017年12月15日 上午11:11:44
	 * @action parseCtripRouteCommentFirst
	 * @param params
	 */
	private void parseCtripRouteCommentFirst(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		String content = params.getContent();
		// 跟团游和半自助游的解析
		if (url.contains("http://vacations.ctrip.com/bookingnext/Comment/Search")) {
			String itemNo = url.substring(url.indexOf("pkg") + 4,
					url.indexOf("&pageIndex"));
			System.out.println("评论url编号：(测试截取是否正确)" + itemNo);
			try {
				// 解析JSON数据
				JSONObject jsonObject = new JSONObject(content);
				String data = jsonObject.get("data").toString();
				JSONObject dataJson = new JSONObject(data);
				Integer commentUsers = Integer.valueOf(dataJson.get(
						"CommentUsers").toString());
				if (commentUsers == 0 || "".equals(commentUsers)) {
					System.out.println("该行程暂无评论!");
				}
				// 计算总页数
				Integer pageCount = commentUsers % 5 == 0 ? commentUsers / 5
						: commentUsers / 5 + 1;
				for (int j = 1; j <= pageCount; j++) {
					// 拼接跟团游的评论请求URL
					String comUrl = "http://vacations.ctrip.com/bookingnext/Comment/Search?pkg="
							+ itemNo + "&pageIndex=" + j;
					Params paramComment = new Params();
					paramComment.setType(Param.CTRIP_ROUTE_COMMENT);
					paramComment.setHttpType(Param.GET);
					paramComment.setDataSource(Param.CTRIP);
					paramComment.setUrl(comUrl);
					paramComment.setHeader("Referer", url);
					System.out.println("url--" + url);
					System.out.println("跟团游评论url--" + comUrl);
					paramComment.setUuid(uuid);

					// 存入 队列
					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, paramComment);
				}

			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
			// 自由行的解析
		} else if (url
				.contains("http://online.ctrip.com/restapi/soa2/12447/json/GetCommentInfoList")) {
			try {
				String postParam = params.getPostParams();
				String itemNo = postParam.substring(
						postParam.indexOf("ProductId") + 12,
						postParam.indexOf("PageIndex") - 2);
				System.out.println("评论url编号：(测试截取是否正确)" + itemNo);
				// 解析JSON数据
				JSONObject jsonObject = new JSONObject(content);
				String data = jsonObject.get("Data").toString();
				if (data == null || "".equals(data) || "null".equals(data)) {
					System.out.println("该行程暂无评论!");
				}
				JSONObject dataJson = new JSONObject(data);
				String commentSummaryInfo = dataJson.get("CommentSummaryInfo")
						.toString();
				JSONObject commentInfoJson = new JSONObject(commentSummaryInfo);
				Integer commentUsers = Integer.valueOf(commentInfoJson.get(
						"TotalAmount").toString());
				if (commentUsers == 0 || "".equals(commentUsers)) {
					System.out.println("该行程暂无评论!");
				}
				// 计算总页数
				Integer pageCount = commentUsers % 5 == 0 ? commentUsers / 5
						: commentUsers / 5 + 1;
				for (int j = 1; j <= pageCount; j++) {
					String commentUrl = "http://online.ctrip.com/restapi/soa2/12447/json/GetCommentInfoList";
					// 封装请求头参数
					String urlParam = "{\"version\":70400,\"platformId\":4,\"channelCode\":0,\"CommentLevel\":0,\"ProductId\":"
							+ itemNo
							+ ",\"PageIndex\":"
							+ j
							+ ",\"PageSize\":5}";

					Params paramComment = new Params();
					paramComment.setType(Param.CTRIP_ROUTE_COMMENT);
					paramComment.setHttpType(Param.POST);
					paramComment.setDataSource(Param.CTRIP);
					paramComment.setUrl(commentUrl);
					paramComment.setPostParams(urlParam);
					paramComment.setUuid(uuid);
					paramComment.setHeader("Content-Type",
							"application/json; charset=UTF-8");
					System.out.println("自由行评论" + commentUrl + urlParam);
					// 存入队列
					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, paramComment);

				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * 
	 * @Description 获取携程行程中费用信息和介绍信息
	 * @author 赵乐
	 * @date 2017年12月20日 下午4:32:36
	 * @action parseCtripExpenseAndReserveinfo
	 * @param @param params
	 * @return void
	 */
	private void parseCtripExpenseAndReserveinfo(Params params) {
		// TODO Auto-generated method stub
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONObject jsonObjectData = jsonObject.getJSONObject("data");
				JSONArray jsonArrayExpense = jsonObjectData
						.getJSONArray("FeeInfos");
				JSONObject jsonObjectReserveinfo = jsonObjectData
						.getJSONObject("OrderingNeedToKnowInfo");
				Routeinfo routeinfo = new Routeinfo();
				routeinfo.setId(uuid);
				routeinfo.setExpense(jsonArrayExpense.toString());
				routeinfo.setReserveinfo(jsonObjectReserveinfo.toString());
				System.out.println("插入携程中费用信息和介绍信息");
				redisService.insertAndGetId(routeinfo);
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * 
	 * @Description 获取携程行程详情中的出发地信息
	 * @author 赵乐
	 * @date 2017年12月20日 下午4:31:52
	 * @action parseCtripRouteDeparture
	 * @param @param params
	 * @return void
	 */
	private void parseCtripRouteDeparture(Params params) {
		String uuid = params.getUuid();
		String url = params.getUrl();
		String productID = url.substring(url.indexOf("ProductID") + 10,
				url.indexOf("&StartCity"));
		String content = params.getContent();
		String departure = "";
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONArray jsonArray = jsonObject.getJSONArray("departureCity");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = jsonArray.getJSONObject(i);
					Integer productIdNum = (Integer) jsonObject2
							.getInt("productId");
					String productId = productIdNum + "";
					System.out.println(productId);
					if (productID.equals(productId)) {
						departure = jsonObject2.getString("name");
						Routeinfo routeinfo = new Routeinfo();
						routeinfo.setId(uuid);
						routeinfo.setDeparture(departure);
						redisService.insertAndGetId(routeinfo);
						break;
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * 
	 * @Description 解析携程行程价格信息
	 * @author 赵乐
	 * @date 2017年12月20日 上午9:00:10
	 * @action parseCtripRoutePrice
	 * @param @param params
	 * @return void
	 */
	private void parseCtripRoutePrice(Params params) {
		// TODO Auto-generated method stub
		String uuId = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				// 对JSON数据进行解析
				JSONObject json = new JSONObject(content);
				JSONObject dataJson = new JSONObject(json.get("data")
						.toString());
				JSONArray jsonArray = new JSONArray(dataJson.get(
						"ProductCalendarDailyList").toString());
				for (int j = 0; j < jsonArray.length(); j++) {

					Routepriceinfo routePrice = new Routepriceinfo();
					JSONObject priceDateJson = new JSONObject(jsonArray.get(j)
							.toString());
					String date = priceDateJson.get("Date").toString();
					String priceDate = date.substring(0, 10);
					String lowestPrice = priceDateJson.get("MinPrice")
							.toString();
					String dayOfWeek = DateUtil.dateToWeek(priceDate);
					// 创建HolyrobotRoutepriceinfo对象并设置相关属性

					routePrice.setId(UUID.randomUUID().toString());
					routePrice.setRouteid(uuId);
					routePrice.setCreator("姚良良");
					routePrice.setCreatorid("tyl13564205515");
					routePrice.setCreatedate(new Date());
					routePrice.setPricedate(priceDate);
					routePrice.setLowestprice(lowestPrice);
					routePrice.setDayofweek(dayOfWeek);
					System.out.println("插入行程价格信息");
					redisService.insertAndGetId(routePrice);
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * 
	 * @Description 解析携程景点评论信息
	 * @author 赵乐
	 * @date 2017年12月13日 下午1:45:19
	 * @action parseCtripStrokeComment
	 * @param @param params
	 * @return void
	 */
	private void parseCtripStrokeComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			// 下面解析的是app端的评论url返回的数据
			/*
			 * try { JSONObject jsonObject = new JSONObject(content); JSONObject
			 * commentResult=jsonObject.getJSONObject("CommentResult");
			 * JSONArray commentInfo=null; try {
			 * commentInfo=commentResult.getJSONArray("CommentInfo"); } catch
			 * (Exception e) { } if(Objects.nonNull(commentInfo)){ for(int
			 * i=0;i<commentInfo.length();i++){ JSONObject
			 * comment=commentInfo.getJSONObject(i);
			 * 
			 * Commentinfo commentinfo=new Commentinfo(); String
			 * uuId=UUID.randomUUID().toString(); commentinfo.setId(uuId);
			 * commentinfo.setInfoid(uuid); commentinfo.setType(1);
			 * commentinfo.setContent(comment.toString());
			 * commentinfo.setCreatedate(new Date());
			 * commentinfo.setCreator("赵乐");
			 * commentinfo.setCreatorid("15736708180"); //放入redis
			 * redisService.insertAndGetId(commentinfo); } } } catch (Exception
			 * e) {
			 * OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
			 * params); } }else { System.out.println("未获取到信息，重新放入队列");
			 * OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
			 * params); }
			 */
			// 下面解析的是网页端的评论url返回的数据，暂时不用
			// 下面解析的是网页端的评论url返回的数据，暂时不用
			try {

				Document document = Jsoup.parse(content);
				Elements elements = document
						.select("div.comment_ctrip>div.comment_single>ul");
				System.err.println("当前评论url：" + params.getUrl());
				if (!elements.isEmpty()) {
					for (int i = 0; i < elements.size(); i++) {
						Element element = elements.get(i);
						System.out.println("评论内容：" + element.text());
						Commentinfo commentinfo = new Commentinfo();
						// 主键ID
						String uuId = UUID.randomUUID().toString();
						commentinfo.setId(uuId);
						commentinfo.setInfoid(uuid);
						commentinfo.setType(1);
						commentinfo.setDatasource("Ctrip");
						commentinfo.setContent(element.toString());
						commentinfo.setCreatedate(new Date());
						commentinfo.setCreator("赵乐");
						commentinfo.setCreatorid("15736708180");
						// 放入redis
						redisService.insertAndGetId(commentinfo);
					}

				} else {
					OtherSpiderStart.queue.add(
							OtherSpiderStart.OTHER_MISSION_KEY, params);
				}
			} catch (Exception e) {
				System.out.println("未获取到信息，重新放入队列");
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		}
	}

	/**
	 * @Description 途牛景点评论解析
	 * @author 汤玉林
	 * @date 2017年12月13日 上午11:45:25
	 * @action parseTuniuStrokeComment
	 * @param params
	 */
	private void parseTuniuStrokeComment(Params params) {
		String uuid = params.getUuid();
		String str = params.getContent();
		if (StringUtils.isNotBlank(str)) {
			try {
				JSONObject object = new JSONObject(str);
				String success = object.get("success").toString();
				if ("true".equals(success)) {
					String data = object.get("data").toString();
					Document document = Jsoup.parse(data);
					Elements elementsDetail = document
							.select("div.item>div.detail");
					int size = elementsDetail.size();
					System.out.println(size + "每页评论条数");
					for (Element element : elementsDetail) {
						Commentinfo commentinfo = new Commentinfo();
						String commentId = UUID.randomUUID().toString();
						commentinfo.setId(commentId);
						commentinfo.setInfoid(uuid);
						String content = element.toString();
						commentinfo.setContent(content);
						commentinfo.setType(1);
						commentinfo.setDatasource("Tuniu");
						commentinfo.setCreatedate(new Date());
						commentinfo.setCreator("赵乐");
						commentinfo.setCreatorid("15736708180");
						redisService.insertAndGetId(commentinfo);
					}
				}
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * 
	 * @Description 解析携程想去人数和去过人数
	 * @author 赵乐
	 * @date 2017年12月13日 下午1:45:52
	 * @action parseCtripStrokeWantAndBeen
	 * @param @param params
	 * @return void
	 */
	private void parseCtripStrokeWantAndBeen(Params params) {
		String content = params.getContent();

		String beenNum = "";
		String wantToNum = "";
		if (StringUtils.isNotBlank(content)) {
			try {
				if (content.contains("{")) {
					JSONObject jsonObject = new JSONObject(content);
					try {
						if (jsonObject != null) {
							beenNum = jsonObject.get("WentTimes").toString();
							// 景点想去人数
							wantToNum = jsonObject.get("WantTimes").toString();
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
				// 补全sceinfo信息，插入redis
				String uuId = params.getUuid();

				Sceinfo sceinfo = new Sceinfo();
				sceinfo.setId(uuId);
				sceinfo.setWanttonum(wantToNum);
				sceinfo.setBeennum(beenNum);
				System.out.println("想去人数和去过人数" + wantToNum + beenNum);
				redisService.insertAndGetId(sceinfo);
			} catch (Exception e) {
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * @Description 解析携程门票评论
	 * @author 汤玉林
	 * @date 2017年12月12日 上午10:37:03
	 * @action parseCtripScenicComment
	 * @param params
	 */
	private void parseCtripScenicComment(Params params) {
		String content = params.getContent();
		String uuId = params.getUuid();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject json = new JSONObject(content);
				String comment = json.get("Comment").toString();
				JSONArray jsonArray = new JSONArray(comment);
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject object = jsonArray.getJSONObject(i);
					String commentDate = object.getString("date");
					Commentinfo holyrobotCommentinfo = new Commentinfo();
					String commentinfoId = UUID.randomUUID().toString();
					holyrobotCommentinfo.setId(commentinfoId);
					holyrobotCommentinfo.setInfoid(uuId);
					holyrobotCommentinfo.setContent(object.toString());
					holyrobotCommentinfo.setCommentdate(commentDate);
					holyrobotCommentinfo.setType(1);
					holyrobotCommentinfo.setDatasource("Ctrip");
					holyrobotCommentinfo.setCreator("徐仁杰");
					holyrobotCommentinfo.setCreatorid("xurenjie-13621935220");
					holyrobotCommentinfo.setCreatedate(new Date());
					redisService.insertAndGetId(holyrobotCommentinfo);
					System.out.println("评论插入成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * @Description 解析携程门票图片
	 * @author 汤玉林
	 * @date 2017年12月12日 上午10:33:29
	 * @action parseCtripScenicPicture
	 * @param params
	 */
	private void parseCtripScenicPicture(Params params) {
		String content = params.getContent();
		String uuId = params.getUuid();
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONArray jsonArray = new JSONArray(content);
				JSONArray imgArray = null;
				JSONObject json = null;
				for (int i = 0; i < jsonArray.length(); i++) {
					Pictureinfo holyrobotPictureinfo = new Pictureinfo();
					String dataArray = jsonArray.get(i).toString();
					json = new JSONObject(dataArray);
					String imgData = json.get("Ga").toString();
					imgArray = new JSONArray(imgData);
					// 图片排序
					Integer imgId = json.getInt("ImgID");
					// 图片地址
					String img = imgArray.get(0).toString();
					json = new JSONObject(img);
					String pictureInfoId = UUID.randomUUID().toString();
					holyrobotPictureinfo.setId(pictureInfoId);
					holyrobotPictureinfo.setInfoid(uuId);
					holyrobotPictureinfo.setSort(imgId);
					holyrobotPictureinfo.setType(2);
					holyrobotPictureinfo.setDownload(0);
					holyrobotPictureinfo.setImgurl(json.getString("Url")
							.toString());
					holyrobotPictureinfo.setCreator("徐仁杰");
					holyrobotPictureinfo.setCreatorid("xurenjie-13621935220");
					holyrobotPictureinfo.setCreatedate(new Date());
					redisService.insertAndGetId(holyrobotPictureinfo);
					System.out.println("图片插入成功");
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 携程解析房型url
	 * @author 汤玉林
	 * @date 2017年12月12日 上午8:51:53
	 * @action parseCtripHotelRoom
	 * @param params
	 */
	private void parseCtripHotelRoom(Params params) {
		String url = params.getUrl();
		String priceDate = url.substring(url.indexOf("startDate") + 10,
				url.indexOf("depDate") - 1);
		String str = params.getContent();
		String uuId = params.getUuid();
		String roomHtml = "";
		int count = 0;
		try {
			System.out.println(params.getUrl());
			str = URLDecoder.decode(str.replace("%", "%25"), "UTF-8");
			JSONObject jsonObject = new JSONObject(str);
			roomHtml = jsonObject.getString("html");
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			Document roomDoc = Jsoup.parse(roomHtml);
			Elements tbody = roomDoc.select("table#J_RoomListTbl>tbody");
			for (Element element1 : tbody) {
				// 去除没用的tr
				Elements tr = element1.select("tr");
				for (int i = 0; i < tr.size(); i++) {
					if (tr.get(i).text().contains("展开全部")
							|| tr.get(i).attr("class").contains("all-order")) {
						tr.remove(i);
					}
				}
				System.out.println("该酒店价格长度为：" + tr.size());
				// 获取房型信息
				for (int i = 2; i < tr.size(); ++i) {
					String room_type = "";
					String bedtype = "";
					String floor = "";
					String bedSize = "";
					String peopleCount = "";
					String roomPic = "";
					String addBed = "";
					String hasWindow = "";
					int isaddBed = 0;
					// 找出相同的brid的元素
					String sameBrid = tr.get(i).attr("brid");
					// System.out.println(sameBrid);
					if (StringUtils.isBlank(sameBrid)) {
						try {
							sameBrid = tr.get(i + 1).attr("brid");
						} catch (Exception e) {
							sameBrid = "";
						}
					}
					// 获取相同的房型，去除tr中的没用的一行
					Elements sametr = element1.select("tr[brid=" + sameBrid
							+ "]");
					for (int k = 0; k < sametr.size(); k++) {
						if (sametr.get(k).text().contains("展开全部")
								|| sametr.get(k).attr("class")
										.contains("all-order")) {
							sametr.remove(k);
						}
					}
					Element roomDetails = tr.get(i + sametr.size());
					Elements es1 = roomDetails.select("div.hrd-info");
					if (es1.size() > 0) {
						Elements details = roomDetails
								.select("div.hrd-info>div.hrd-info-base>ul.hrd-info-base-list>li");
						for (Element element : details) {
							String text = element.text();
							if (text.contains("楼层")) {
								floor = text;
							} else if (text.contains("床型")) {
								bedSize = text;
							} else if (text.contains("窗")) {
								hasWindow = text;
							} else if (text.contains("加床")) {
								addBed = text;
							}
						}
						/*
						 * floor=details.get(1).text(); try{
						 * bedSize=details.get(2).text(); }catch(Exception e){
						 * bedSize=""; } if(details.size()>3){
						 * addBed=details.get(3).text(); }else{ addBed="无"; }
						 * if(!addBed.contains("不可加床")){ isaddBed=1; }
						 */
					} else {
						Elements details = roomDetails
								.select("tr.clicked>td>div.searchresult_caption.basefix>div.searchresult_caplist_box>ul.searchresult_caplist");
						if (details.size() > 0) {
							details = details.get(0).select("li");
						}
						for (Element element : details) {
							String text = element.text();
							if (text.contains("楼层")) {
								floor = text;
							} else if (text.contains("床型")) {
								bedSize = text;
							} else if (text.contains("窗")) {
								hasWindow = text;
							} else if (text.contains("加床")) {
								addBed = text;
							}
						}

						/*
						 * floor=details.get(1).text(); try{
						 * bedSize=details.get(2).text(); }catch(Exception e){
						 * bedSize=""; } if(details.size()>3){
						 * addBed=details.get(3).text(); }else{ addBed="无"; }
						 * if(!addBed.contains("不可加床")){ isaddBed=1; }
						 */
					}
					Roombasicinfo roomInfo = new Roombasicinfo();
					String roomInfoId = UUID.randomUUID().toString();

					// 开始获取每种产品的价格信息
					for (int j = 0; j < sametr.size(); j++) {
						String price = "";
						String productName = "";
						String breakfast = "";
						String wifi = "";
						String cancle = "";
						String isbooking = "";
						String payMethod = "";
						// 获取每一个相同tr下的所有td,共有9列
						Elements sametd = sametr.get(j).select("td");
						// 第一列特殊处理
						if (j == 0) {
							// 获取第一个td列里面的a
							Elements td1 = sametd.get(0).select("a");
							room_type = td1.get(1).ownText();
							roomPic = td1.get(0).select("img").attr("_src");
							// 获取第二个td下第一个子元素的值
							Elements td2 = sametd.get(1).select("span");
							productName = td2.get(0).text();
							if (StringUtils.isBlank(productName)) {
								productName = td2.attr("style").substring(
										td2.attr("style").indexOf("(") + 1,
										td2.attr("style").indexOf(")"));
							}
							// 获取第二个td下文本值
							bedtype = sametd.get(2).text();
							breakfast = sametd.get(3).text();
							wifi = sametd.get(4).text();
							String peopleTitle = sametd.get(5).select("span")
									.get(0).attr("title");
							if (StringUtils.isNotBlank(peopleTitle)) {
								peopleCount = NumUtils.getInteger(peopleTitle)
										.toString();
							} else {
								peopleCount = "0";
							}
							Elements td5 = sametd.get(6).select("span");
							cancle = td5.get(0).text();
							Elements td6 = sametd.get(7).select("p");
							if (!td6.isEmpty()) {
								try {
									price = NumUtils.getInteger(
											td6.get(1).text()).toString();
								} catch (Exception e) {

								}
							}
							Elements td7 = sametd.get(8).select(
									"div.book_type>a.J_hotel_order");
							if (td7.size() <= 0) {
								td7 = sametd
										.get(8)
										.select("div.book_type>a.btns_base22.btns_base22_dis");
								isbooking = "订完";
							} else {
								isbooking = "预定";
							}
						}
						if (j >= 1) {
							Elements td2 = sametd.get(0).select("span");
							productName = td2.get(0).text();
							if (StringUtils.isBlank(productName)) {
								productName = td2.attr("style").substring(
										td2.attr("style").indexOf("(") + 1,
										td2.attr("style").indexOf(")"));
							}
							// 获取第二个td下文本值
							bedtype = sametd.get(1).text();
							breakfast = sametd.get(2).text();
							wifi = sametd.get(3).text();
							if (sametd.get(4).select("span").size() > 0) {
								String peopleTitle = sametd.get(4)
										.select("span").get(0).attr("title");
								peopleCount = NumUtils.getInteger(peopleTitle)
										.toString();
							} else {
								peopleCount = "0";
							}
							Elements td5 = sametd.get(5).select("span");
							cancle = td5.get(0).text();
							Elements td6 = sametd.get(6).select("p");
							if (!td6.isEmpty()) {
								try {
									price = NumUtils.getInteger(
											td6.get(1).text()).toString();
								} catch (Exception e) {

								}
							}
							// System.out.println(price);
							Elements td7 = sametd.get(7).select(
									"div.book_type>a.J_hotel_order");
							if (td7.size() <= 0) {
								td7 = sametd
										.get(7)
										.select("div.book_type>a.btns_base22.btns_base22_dis");
								isbooking = "订完";
							} else {
								isbooking = "预定";
								Elements payMethodEle = sametd
										.get(7)
										.select("div.btns_base22_skin01>span.payment_txt");
								if (payMethodEle.isEmpty()) {
									payMethodEle = sametd
											.get(7)
											.select("div.btns_base22_skin04>span.payment_txt");
								}
								payMethod = payMethodEle.text();
							}
						}

						// 酒店价格
						Roomprice roomPrice = new Roomprice();
						String roomPriceId = UUID.randomUUID().toString();
						roomPrice.setId(roomPriceId);
						roomPrice.setHotelid(uuId);
						roomPrice.setRoomid(roomInfoId);
						roomPrice.setProductname(productName);
						roomPrice.setIscancled(cancle);
						roomPrice.setIshasbreakfast(breakfast);
						roomPrice.setIswifi(wifi);
						roomPrice.setPrice(price);
						roomPrice.setIsbooking(isbooking);
						roomPrice.setDate(priceDate);
						roomPrice.setPaymethod(payMethod);
						roomPrice.setCreatedate(new Date());
						roomPrice.setCreator("tyl");
						roomPrice.setCreatorid("tyl13564205515");
						// System.out.println("插入房型价格信息，产品名称："+productName+";价格："+price);
						redisService.insertAndGetId(roomPrice);
						// System.out.println("产品名称："+productName+";是否可取消："+cancle+";是否含早餐:"+breakfast+";是否有wifi："+wifi+";价格："+price);
						// 记住当前的tr值，下次从当前位置开始
						++i;
					}
					// 酒店房型
					roomInfo.setId(roomInfoId);
					roomInfo.setHotelid(uuId);
					roomInfo.setBedtype(bedtype);
					roomInfo.setBedsize(bedSize);
					roomInfo.setFloor(floor);
					roomInfo.setIsaddbed(addBed);
					roomInfo.setRoomtype(room_type);
					roomInfo.setPeoplecount(Integer.valueOf(peopleCount)
							.toString());
					roomInfo.setCreatedate(new Date());
					roomInfo.setCreator("tyl");
					roomInfo.setCreatorid("tyl13564205515");
					redisService.insertAndGetId(roomInfo);
					/*
					 * System.out.println("房间名称："+room_type+";床的类型："+bedtype+
					 * ";床的大小："+bedSize+";" +
					 * "楼层："+floor+";是否可加床："+isaddBed+";可住人数："+peopleCount);
					 */
					// 获取图片
					if (StringUtils.isNotBlank(roomPic)) {
						Pictureinfo pictureInfo = new Pictureinfo();
						pictureInfo.setId(UUID.randomUUID().toString());
						pictureInfo.setInfoid(roomInfoId);
						pictureInfo.setImgurl(roomPic);
						pictureInfo.setSort(count++);
						pictureInfo.setType(5);
						pictureInfo.setDownload(0);
						pictureInfo.setCreatedate(new Date());
						pictureInfo.setCreator("tyl");
						pictureInfo.setCreatorid("tyl13564205515");
						redisService.insertAndGetId(pictureInfo);
					}

				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 解析驴妈妈行程评论信息
	 * @author 汤玉林
	 * @date 2017年12月11日 下午4:07:30
	 * @action parseLvmamaRouteComment
	 * @param params
	 */
	private void parseLvmamaRouteComment(Params params) {
		String uuId = params.getUuid();
		Document doc = Jsoup.parse(params.getContent());
		try {
			// 获取每条评论的DIV
			Elements comEles = doc.select("div.comment-li");
			if (!comEles.isEmpty()) {
				for (Element element : comEles) {
					Commentinfo commentInfo = new Commentinfo();
					String commentDate = element
							.select("div.com-userinfo>p>em").text();
					String id = UUID.randomUUID().toString();
					commentInfo.setId(id);
					commentInfo.setInfoid(uuId);
					commentInfo.setType(3); // 类型：1-景点，2-酒店，3-景点行程
					commentInfo.setCommentdate(commentDate);
					// 获取评论的内容div
					commentInfo.setContent(element.toString());
					commentInfo.setDatasource("Lvmama");
					commentInfo.setCreator("姚良良");
					commentInfo.setCreatorid("13783985208");
					commentInfo.setCreatedate(new Date());
					// 存入redis
					redisService.insertAndGetId(commentInfo);
				}
			} else {
				System.out.println("未获取到评论信息，重新放入队列");
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 驴妈妈行程价格解析
	 * @author 汤玉林
	 * @date 2017年12月11日 下午3:12:04
	 * @action parseLvmamaRoutePrice
	 * @param params
	 */
	private void parseLvmamaRoutePrice(Params params) {
		String url = params.getUrl();
		String uuid = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {

				JSONArray jsonArray = new JSONArray(content);
				System.out.println(url + " 的行程价格安排共有" + jsonArray.length()
						+ "天!");
				int count = 0;

				for (int j = 0; j < jsonArray.length(); j++) {
					Routepriceinfo routePrice = new Routepriceinfo();
					String str = jsonArray.get(j).toString();
					JSONObject json = new JSONObject(str);
					// 获取日期
					String priceDate = json.get("departureDate").toString();
					// 获取星期
					String dayOfWeek = json.get("weekOfDate").toString();
					// 获取最低价格
					String lowestPrice = json.get("lowestSaledPriceYuan")
							.toString();
					String id = UUID.randomUUID().toString();
					routePrice.setId(id);
					routePrice.setRouteid(uuid);
					routePrice.setPricedate(priceDate);
					routePrice.setDayofweek(dayOfWeek);
					routePrice.setLowestprice(lowestPrice);
					routePrice.setCreator("姚良良");
					routePrice.setCreatorid("13783985208");
					routePrice.setCreatedate(new Date());
					redisService.insertAndGetId(routePrice);

				}
				System.out.println(url + " 的行程价格安排共保存了" + count + "天!");
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到评论信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 驴妈妈酒店评论解析
	 * @author 汤玉林
	 * @date 2017年12月8日 下午1:55:41
	 * @action parseLvmamaHotelComment
	 * @param params
	 */
	private void parseLvmamaHotelComment(Params params) {
		String uuId = params.getUuid();
		try {
			Document doc2 = Jsoup.parse(params.getContent());
			Elements commentlist = doc2.select("div.comment-li");
			if (commentlist.size() > 0) {
				for (Element comment : commentlist) {
					Commentinfo commentInfo = new Commentinfo();
					String str = comment.toString();
					System.out.println(str);
					commentInfo.setId(UUID.randomUUID().toString());
					commentInfo.setInfoid(uuId);
					commentInfo.setType(2);
					commentInfo.setContent(comment.toString());
					commentInfo.setCreator("汤玉林");
					commentInfo.setCreatedate(new Date());
					commentInfo.setCreatorid("tyl13564205515");
					commentInfo.setDatasource("Lvmama");
					redisService.insertAndGetId(commentInfo);
					System.out.println("\n评论内容：" + comment);
				}
			} else {
				System.out.println("未获取到评论，重新放入队列");
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 驴妈妈酒店房型解析
	 * @author 汤玉林
	 * @date 2017年12月8日 下午1:40:18
	 * @action parseLvmamaHotelRoom
	 * @param params
	 */
	private void parseLvmamaHotelRoom(Params params) {
		String uuId = params.getUuid();
		String url = params.getUrl();
		System.out.println("进入房型解析");
		String priceDate = url.substring(url.indexOf("startDateStr") + 13,
				url.indexOf("endDateStr") - 1);
		Document doc = Jsoup.parse(params.getContent());
		try {
			// 酒店房间列表
			Elements hotelRoom = doc.select("div.room_list");
			int count = 0;
			if (hotelRoom.size() > 0) {
				for (Element element : hotelRoom) {
					// 房间图片
					Elements imgElement = element.select("div.room_list_l>img");
					// 房间名称
					if (imgElement.isEmpty()) {
						imgElement = element.select("img.roomList-img");
					}
					String picturesrc = imgElement.attr("src");
					// 房间名称
					Elements room_list_r = element.select("div.room_list_r");
					if (room_list_r.isEmpty()) {
						room_list_r = element.select("dl>dt");
					}
					String roomName = room_list_r.select("h4").text();

					String bedtype = "";
					String floor = "";
					String isaddBed = "";
					int peoplecount = 0;
					String iswifi = "";

					Elements room_info = room_list_r.select("p.room_info");

					String[] room_infos;
					if (!room_info.isEmpty()) {
						room_infos = room_info.text().split(" ");
						for (int s = 0; s < room_infos.length; s++) {
							if (room_infos[s].contains("楼层")) {
								floor = room_infos[s];
							}
							if (room_infos[s].contains("床型")) {
								bedtype = room_infos[s];
							}
							if (room_infos[s].contains("可以加床 ")) {
								isaddBed = room_infos[s];
							}
							if (room_infos[s].contains("最大入住人数")) {
								peoplecount = NumUtils
										.getInteger(room_infos[s]);
							}
							if (room_infos[s].contains("宽带：免费")) {
								iswifi = room_infos[s];
							}
						}
					} else {
						if (room_info.isEmpty()) {
							room_info = room_list_r.select("p.room-info");
						}
						room_infos = room_info.text().replace("|", "")
								.split(" ");
						for (int s = 0; s < room_infos.length; s++) {
							if (room_infos[s].contains("位于")) {
								floor = room_infos[s]; // 楼层
							}
							if ((room_infos[s].contains("大") || room_infos[s]
									.contains("双"))
									&& room_infos[s].contains("床")) {
								bedtype = room_infos[s]; // 床型
							}
							if (room_infos[s].contains("床")
									&& room_infos[s].contains("加床")) {
								isaddBed = room_infos[s]; // 是否可以加床
							}
							if (room_infos[s].contains("最大入住")) {
								peoplecount = NumUtils
										.getInteger(room_infos[s]);
							}
							if (room_infos[s].contains("宽带")) {
								iswifi = room_infos[s];
							}
						}
					}
					Roombasicinfo roomInfo = new Roombasicinfo();
					String roomInfoId = UUID.randomUUID().toString();
					roomInfo.setId(roomInfoId);
					roomInfo.setHotelid(uuId);
					roomInfo.setBedtype(bedtype);
					roomInfo.setFloor(floor);
					roomInfo.setIsaddbed(isaddBed);
					roomInfo.setIswifi(iswifi);
					roomInfo.setPeoplecount(Integer.valueOf(peoplecount)
							.toString());
					roomInfo.setRoomtype(roomName);
					roomInfo.setCreatorid("tyl13564205515");
					roomInfo.setCreator("汤玉林");
					roomInfo.setCreatedate(new Date());
					// 存放房型信息
					// System.out.println("插入酒店房型"+bedtype);
					redisService.insertAndGetId(roomInfo);

					Pictureinfo pictureInfo = new Pictureinfo();
					String pictureId = UUID.randomUUID().toString();
					pictureInfo.setImgurl(picturesrc);
					pictureInfo.setSort(count++);
					pictureInfo.setType(4);
					pictureInfo.setDownload(0);
					pictureInfo.setId(pictureId);
					pictureInfo.setInfoid(roomInfoId);
					// 存入图片
					pictureInfo.setCreatedate(new Date());
					pictureInfo.setCreator("汤玉林");
					pictureInfo.setCreatorid("tyl13564205515");
					// System.out.println("插入酒店图片"+picturesrc);
					redisService.insertAndGetId(pictureInfo);
					// System.out.println("房型："+roomName+";楼层："+floor+";床型："+bedtype+";加床与否："+isaddBed);

					// 不同房型产品列表
					Elements room_tables = element
							.select("div.room_box>table>tbody>tr.room_table");
					if (room_tables.isEmpty()) {
						room_tables = element.select("dl>dd>table>tbody>tr");
					}
					for (int l = 0; l < room_tables.size(); l++) {
						String isbooking = "";
						String available = "无";
						// 下面是每一个tr里面的数据
						Elements tds = room_tables.get(l).select("td");
						String productName = tds.get(0).text();
						String hasBreakfast = tds.get(2).text();
						String productWindow = tds.get(3).text();
						String iscancled = tds.get(4).text();
						String price = tds.get(5)
								.select("span.room_price>big.J_room_rate")
								.text();
						if (StringUtils.isBlank(price)) {
							price = tds.get(5).select("dfn>big.J_room_rate")
									.text();
						}
						// 预定
						Elements booking = tds.get(6).select(
								"span.J_yuding.btn.btn-orange.btn-sm");
						if (booking.isEmpty()) {
							booking = tds.get(6).select(
									"a.btn.btn-orange.btn-sm.btn-disabled");
						}
						if (!booking.isEmpty()) {
							isbooking = booking.text();
							if ("预订".equals(isbooking)) {
								available = "有";
							}
						}
						Roomprice roomPrice = new Roomprice();
						// 存放房间价格信息
						String roompriceId = UUID.randomUUID().toString();
						roomPrice.setId(roompriceId);
						roomPrice.setHotelid(uuId);
						roomPrice.setRoomid(roomInfoId);
						roomPrice.setProductname(productName);
						roomPrice.setIsbooking(isbooking);
						roomPrice.setIscancled(iscancled);
						roomPrice.setIshasbreakfast(hasBreakfast);
						roomPrice.setIswifi(iswifi);
						roomPrice.setIswindow(productWindow);
						roomPrice.setPrice(price);
						roomPrice.setAvailablenum(available);
						roomPrice.setDate(priceDate);

						roomPrice.setCreatedate(new Date());
						roomPrice.setCreator("汤玉林");
						roomPrice.setCreatorid("tyl13564205515");
						// System.out.println("插入房间价格信息"+price);
						redisService.insertAndGetId(roomPrice);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * @Description 获取驴妈妈门票评论
	 * @author 徐仁杰
	 * @date 2017年12月7日 下午2:37:39
	 * @action parseLvmamaComment
	 * @return void
	 */
	public void parseLvmamaScenicComment(Params params) {
		String content = params.getContent();
		String uuId = params.getUuid();
		Document doc = Jsoup.parse(content);
		try {
			Elements elements = doc.select("div.comment-li");
			if (elements.size() > 0) {
				for (Element element : elements) {
					Commentinfo commentinfo = new Commentinfo();
					commentinfo.setId(UUID.randomUUID().toString());
					commentinfo.setInfoid(uuId);
					String comment = element.html();
					System.out.println("插入评论");
					Elements timeEle = element.select("div.com-userinfo>p>em");
					String time = timeEle.text();
					commentinfo.setContent(comment);
					commentinfo.setCommentdate(time);
					commentinfo.setType(1);
					commentinfo.setDatasource("Lvmama");
					commentinfo.setCreatedate(new Date());
					commentinfo.setCreator("徐仁杰");
					commentinfo.setCreatorid("xurenjie-13621935220");
					redisService.insertAndGetId(commentinfo);
				}
			} else {
				System.out.println("未获取到评论，重新放入队列");
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} catch (Exception e) {
			e.printStackTrace();
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}

	}

	/**
	 * @Description 获取驴妈妈评分和评论人数景点评论加上分页信息的url
	 * @author 徐仁杰
	 * @date 2017年12月7日 下午3:13:35
	 * @action parseLvmamaStrokeCommentUrl
	 * @return void
	 */
	public void parseLvmamaStrokeCommentUrl(Params params) {
		String content = params.getContent();
		String uuId = params.getUuid();
		String url = params.getUrl();
		String dest_id = url.substring(url.indexOf("d_id=") + 5, url.length());
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonObject = new JSONObject(content);
				System.out.println("strokeCommentUrl---" + jsonObject);
				if (jsonObject != null) {
					// 获取评分和评论人数
					String score = "";
					JSONArray jsonArray = null;
					try {
						jsonArray = jsonObject.getJSONArray("com_dimen");
					} catch (JSONException e) {
						jsonArray = null;
					}
					if (jsonArray != null && jsonArray.length() == 5) {
						JSONObject jsonObjectCom_dimen = jsonArray
								.getJSONObject(4);
						// 评分
						score = jsonObjectCom_dimen.get("formatAvgScore")
								.toString();
						// 评论人数
					}
					Integer totalNum = jsonObject.getInt("count_pub");
					// 设置params中sceinfo的评分和评论人数
					Sceinfo sceinfo = new Sceinfo();
					sceinfo.setId(uuId);
					sceinfo.setGrade(score);
					sceinfo.setGradenum(totalNum + "");
					sceinfo.setCreatedate(new Date());
					sceinfo.setCreator("赵乐");
					sceinfo.setCreatorid("15736708180");
					// params.setSceinfo(sceinfo);
					redisService.insertAndGetId(sceinfo);
					// 评论总页数
					System.out.println("评论总条数" + totalNum);
					// 网页端每页请求10条，app端每页请求500条数据
					Integer pageNum = totalNum % 500 == 0 ? totalNum / 500
							: (totalNum / 500) + 1;
					System.out.println(pageNum);
					for (int i = 1; i <= pageNum; i++) {
						// 下面的是app端的评论url数据
						String commentPageUrl = "https://m.lvmama.com/other/router/rest.do?method=api.com.cmt.getCmtCommentList"
								+ "&version=2.0.0&currentPage="
								+ i
								+ "&isELong=N&pageSize=500&placeId="
								+ dest_id
								+ "&firstChannel=TOUCH&secondChannel=LVMM&iuf=1513238444478511647";
						Params paramComment = new Params();

						System.out.println(commentPageUrl + "评论分页url");
						paramComment.setType(Param.LVMAMA_STROKE_COMMENT);
						paramComment.setDataSource(Param.LVMAMA);
						paramComment.setUrl(commentPageUrl);
						paramComment.setHeader("signal",
								"ab4494b2-f532-4f99-b57e-7ca121a137ca");
						paramComment.setUuid(uuId);
						paramComment.setHttpType(Param.GET);

						// 下面的为网页端的评论url数据，暂时不用
						/*
						 * String commentPageUrl=
						 * "http://www.lvmama.com/lvyou/home/ajaxGetCommentPage?page="
						 * +
						 * i+"&list_type=CommentBestNew&type=N&dest_id="+dest_id
						 * ;
						 * 
						 * Params paramComment=new Params();
						 * System.out.println(commentPageUrl+"评论分页url");
						 * paramComment.setType(Param.LVMAMA_STROKE_COMMENT);
						 * paramComment.setDataSource(Param.LVMAMA);
						 * paramComment.setUrl(commentPageUrl);
						 * paramComment.setUuid(uuId);
						 * paramComment.setHttpType(Param.GET);
						 */
						// 加入消息队列
						System.out.println("把评论分页url加入reids");
						StrokeSpiderStart.queue.add("mission-params",
								paramComment);
					}
				}
			} catch (JSONException e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

	/**
	 * 
	 * @Description 获取驴妈妈的评论信息
	 * @author 赵乐
	 * @date 2017年12月11日 上午11:16:41
	 * @action parseLvmamaStrokeComment
	 * @param @param params
	 * @return void
	 */
	public void parseLvmamaStrokeComment(Params params) {
		String uuid = params.getUuid();
		String content = params.getContent();
		// 下面的解析是app端的数据
		if (StringUtils.isNotBlank(content)) {
			try {
				JSONObject jsonObject = new JSONObject(content);
				JSONObject data = jsonObject.getJSONObject("data");
				JSONArray list = null;
				try {
					list = data.getJSONArray("list");
				} catch (Exception e) {
				}
				if (Objects.nonNull(list)) {
					for (int i = 0; i < list.length(); i++) {
						JSONObject comment = list.getJSONObject(i);
						System.out.println("插入评论");
						Commentinfo commentinfo = new Commentinfo();
						String uuId = UUID.randomUUID().toString();
						commentinfo.setId(uuId);
						commentinfo.setInfoid(uuid);
						commentinfo.setType(1);
						commentinfo.setContent(comment.toString());
						commentinfo.setDatasource("Lvmama");
						commentinfo.setType(1);
						commentinfo.setCreatedate(new Date());
						commentinfo.setCreator("赵乐");
						commentinfo.setCreatorid("15736708180");
						redisService.insertAndGetId(commentinfo);
					}
				}
			} catch (Exception e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到评论信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
		// 下面的解析网页端的数据，暂时不用
		/*
		 * try { JSONObject jsonObject = new JSONObject(content); JSONObject
		 * jsonObjectData = jsonObject.getJSONObject("data"); String html=null;
		 * try { html = jsonObjectData.getString("html"); } catch (JSONException
		 * e) { html=null; System.err.println("当前评论内容为空的url为："+params.getUrl());
		 * System.out.println("当前内容错误的个数："+OtherSpiderStart.count++);
		 * OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
		 * params); } if(html!=null){ Document doc = Jsoup.parse(html); Elements
		 * elements = doc.select("div.comment-li"); String
		 * uuId=params.getUuid();
		 * System.out.println("当前评论url为："+params.getUrl()); for (Element element
		 * : elements) { Commentinfo commentinfo = new Commentinfo(); String
		 * comment=element.toString();
		 * System.out.println("评论内容"+element.text()); String
		 * id=UUID.randomUUID().toString(); commentinfo.setId(id);
		 * commentinfo.setInfoid(uuId); commentinfo.setContent(comment);
		 * commentinfo.setCreatedate(new Date()); commentinfo.setCreator("赵乐");
		 * commentinfo.setCreatorid("15736708180");
		 * redisService.insertAndGetId(commentinfo); } } } catch (JSONException
		 * e) { try { Thread.sleep(5000); } catch (InterruptedException e1) {
		 * e1.printStackTrace(); } }
		 */
	}

	/**
	 * 
	 * @Description 获取驴妈妈景点想去人数和去过人数
	 * @author 赵乐
	 * @date 2017年12月8日 下午4:27:55
	 * @action parseLvmamaStrokeWantAndBeen
	 * @param @param params
	 * @return void
	 */
	private void parseLvmamaStrokeWantAndBeen(Params params) {
		String uuId = params.getUuid();
		String content = params.getContent();
		if (StringUtils.isNotBlank(content)) {
			try {
				// 去过人数
				String count_been = "";
				// 想去人数
				String count_want = "";
				JSONObject jsonObject = new JSONObject(content);
				JSONObject jsonObjectData = jsonObject.getJSONObject("data");
				if (jsonObjectData != null) {
					count_been = jsonObjectData.getString("count_been");
					count_want = jsonObjectData.getString("count_want");
				}
				System.out.println(count_been + "----" + count_want);
				// 设置params中sceinfo的想去人数和去过人数的值
				Sceinfo sceinfo = new Sceinfo();
				sceinfo.setId(uuId);
				sceinfo.setWanttonum(count_want);
				sceinfo.setBeennum(count_been);
				sceinfo.setCreatedate(new Date());
				sceinfo.setCreator("赵乐");
				sceinfo.setCreatorid("15736708180");
				redisService.insertAndGetId(sceinfo);

			} catch (JSONException e) {
				e.printStackTrace();
				OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
						params);
			}
		} else {
			System.out.println("未获取到信息，重新放入队列");
			OtherSpiderStart.queue.add(OtherSpiderStart.OTHER_MISSION_KEY,
					params);
		}
	}

}
