package cn.jj.flight.service.impl;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.junit.Test;
import cn.jj.dao.service.RedisService;
import cn.jj.dao.service.impl.RedisServiceImpl;
import cn.jj.entity.data.Flightinfo;
import cn.jj.entity.data.Flightinputinfo;
import cn.jj.entity.data.Flightpriceinfo;
import cn.jj.flight.entity.Flight;
import cn.jj.utils.PageDownLoadUtil;

public class FlightAPI {
	private RedisService redisService = new RedisServiceImpl();
	/**
	 * 
	 * @Description 得到请求链接
	 * @author xdl
	 * @date 2018年3月5日 上午10:16:15
	 * @action getUrl
	 * @return String
	 */
	public String getUrl(String beginCity,String endCity,String date){
		// 获得出发城市的code
		String beginCityCode = cityCodeManager(beginCity);
		// 获得目的城市的code
		String endCityCode = cityCodeManager(endCity);
		//返回结果
		return  "http://api2.vvtrip.com/test/ibe.aspx?BeginCity="+beginCityCode+"&EndCity="+endCityCode+"&BeginDate="+date+"&Key=681F7AC496B7FEAC";
	}
	/**
	 * 
	 * @Description 根据城市名返回城市三字码
	 * @author xdl
	 * @date 2018年3月5日 上午10:26:40
	 * @action cityCodeManager
	 * @return String
	 */
	public  String cityCodeManager(String cityName){
		if("上海".equals(cityName)){
			return "SHA"; 
		}else if("三亚".equals(cityName)){
			return  "SYX";
		}else if("海口".equals(cityName)){
			return "HAK";
		}
		return null;
	}
	/**
	 * 
	 * @Description 发送请求得到返回结果
	 * @author xdl
	 * @date 2018年3月5日 上午10:19:00
	 * @action getXML
	 * @return String
	 */
	public String getXML(String url){
		Map<String,String> params = new HashMap<>();
		String result = PageDownLoadUtil.sendPostByEncoding(url, "","gb2312");
		int num = 0;
		if (StringUtils.isBlank(result)) {
			for (int x = 0; x < 5; x++) {
				result =  PageDownLoadUtil.sendPostByEncoding(url, "","gb2312");
				if (StringUtils.isNotBlank(result)) {
					break;
				}
			}
		}
		
		return result;
	}
	/**
	 * 
	 * @Description 解析xml，得到业务对象
	 * @author xdl
	 * @date 2018年3月5日 上午10:22:18
	 * @action parseXML
	 * @return List<Flight>
	 */
	public List<Flight> parseXML(String xmlStr,String beginCity,String endCity,String flightDate){
		List flightList =  new ArrayList<>();
		try {
			Document parseText = DocumentHelper.parseText(xmlStr);
			Element rootElement =  parseText.getRootElement();
			//得到航班信息
			List<Element> flightinfoElements = rootElement.elements("FlightInfo");
			//inputinfo的数据
			Flightinputinfo inputinfo = new Flightinputinfo();
			//ID
			String flightinputid = UUID.randomUUID().toString();
			inputinfo.setId(flightinputid);
			//UrlID
			//FlightDate
			inputinfo.setFlightdate(flightDate);
			//BeginCity
			inputinfo.setBegincity(beginCity);
			//EndCity
			inputinfo.setEndcity(endCity);
			//FlightNum
			inputinfo.setFlightnum(String.valueOf(flightinfoElements.size()));
			//LastUpateTime
			//Status
			//CreateDate
			inputinfo.setCreatedate(new Date());
			//Creator
			inputinfo.setCreator("谢大磊");
			//CreatorID
			inputinfo.setCreatorid("xiedalei-15255178580");
			for (Element flightElement : flightinfoElements) {
				Flightinfo flightinfo = new Flightinfo();
				Flight flight =  new Flight();
				//ID
				String  flightinfoID = UUID.randomUUID().toString();
				flightinfo.setId(flightinfoID);
				//InputID
				flightinfo.setInputid(flightinputid);
				//FlightNo
				String flightNo = flightElement.elementText("FlightNo");
				flightinfo.setFlightno(flightNo);
				//FlightDate
				flightinfo.setFlightdate(flightDate);
				//Plane
				String plane = flightElement.elementText("Plane");
				flightinfo.setPlane(plane);
				//BeginCity
				flightinfo.setBegincity(beginCity);
				//EndCity
				flightinfo.setEndcity(endCity);
				//BeginTime
				String beginTime = flightElement.elementText("BeginTime");
				flightinfo.setBegintime(beginTime);
				//EndTime
				String endTime = flightElement.elementText("EndTime");
				flightinfo.setEndtime(endTime);
				//RealCostTime
				String realCostTime = flightElement.elementText("Times");
				flightinfo.setRealcosttime(realCostTime);
				//FlightArriveDate
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
				Date beginDate = sdf.parse(flightDate+" "+beginTime+":00");
				int hours =  Integer.valueOf(realCostTime.split(":")[0]);
				int minutes = Integer.valueOf(realCostTime.split(":")[1]);
				Date tempDate = DateUtils.addHours(beginDate, hours);
				System.out.println(sdf.format(tempDate));
				Date endDate = DateUtils.addMinutes(tempDate, minutes);
				String flightArriveDate = sdf.format(endDate).split(" ")[0];
				flightinfo.setFlightarrivedate(flightArriveDate);
				//FlightStatus
				//Stop
				String stop = flightElement.elementText("Stop");
				flightinfo.setStop(stop);
				//Times
				//BeginTerminal
				String beignTerm = flightElement.elementText("BeginTerm");
				flightinfo.setBeginterminal(beignTerm);
				//EndTerminal
				String endTerm = flightElement.elementText("EndTerm");
				flightinfo.setEndterminal(endTerm);
				//BeginCode
				String beginCode = flightElement.elementText("BeginCity");
				flightinfo.setBegincode(beginCode);
				//EndCode
				String endCode = flightElement.elementText("EndCity");
				flightinfo.setEndcode(endCode);
				//Size
				//得到航班价格信息
				//得到燃油费
				String oilFee = flightElement.elementText("AduOil");
				//得到基建费
				String taxFee = flightElement.elementText("Tax");
				//得到具体的价格信息
				List<Element> priceElementList = flightElement.elements("Cabin");
				List<Flightpriceinfo> priceList = new ArrayList<>();
				for(Element priceElement:priceElementList){
					int  price =  Integer.valueOf(priceElement.elementText("Price"));
					if(price>0){
						Flightpriceinfo flightpriceinfo = new Flightpriceinfo();
						//ID
						String flightpriceid  = UUID.randomUUID().toString();
						flightpriceinfo.setId(flightpriceid);
						//FlightID
						flightpriceinfo.setFlightid(flightinfoID);
						//FlightNo
						flightpriceinfo.setFlightno(flightNo);
						//FlightDate
						flightpriceinfo.setFlightdate(flightDate);
						//ClassType
						String classType = priceElement.elementText("Code");
						flightpriceinfo.setClasstype(classType);
						//IdentityType
						//Price
						flightpriceinfo.setPrice(String.valueOf(price));
						//Discount
						String discount = priceElement.elementText("Class");
						flightpriceinfo.setDiscount(discount);
						//TaxFee
						flightpriceinfo.setConstructionfee(taxFee);
						//OilFee
						flightpriceinfo.setOilfee(oilFee);
						//DataSource
						flightpriceinfo.setDatasource("API");
						//CreateDate'
						flightpriceinfo.setCreatedate(new Date());
						//Creator
						flightpriceinfo.setCreator("谢大磊");
						//CreatorID
						flightpriceinfo.setCreatorid("xiedalei-15255178580");
						priceList.add(flightpriceinfo);
						
					}
				}
				//LowestPrice
				int lowestPrice = Integer.valueOf(priceList.get(0).getPrice());
				int minIndex = 0;
				for(int i=0;i<priceList.size();i++){
					Integer price = Integer.valueOf(priceList.get(i).getPrice());
					if(price<lowestPrice){
						lowestPrice = price ;
						minIndex = i;
					}
				}
				flightinfo.setLowestprice(String.valueOf(lowestPrice));
				//Discount
				flightinfo.setDiscount(priceList.get(minIndex).getDiscount());
				//DataSource
				flightinfo.setDatasource("API");
				//CreateDate
				flightinfo.setCreatedate(new Date());
				//Creator
				flightinfo.setCreator("谢大磊");
				//CreatorID
				flightinfo.setCreatorid("xiedalei-15255178580");
				//封装业务对象
				flight.setFlightinputinfo(inputinfo);
				flight.setFlightinfo(flightinfo);
				flight.setFlightpriceinfoList(priceList);
				
				flightList.add(flight);
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println(xmlStr);
		}
		return flightList;
	}
	/**
	 * 
	 * @Description 
	 * @author xdl
	 * @date 2018年3月5日 上午10:28:34
	 * @action save
	 * @return void
	 */
	public  void save(List<Flight> flightList){
		for (Flight flight : flightList) {
			redisService.insertAndGetId(flight.getFlightinputinfo());
			redisService.insertAndGetId(flight.getFlightinfo());
			List<Flightpriceinfo> priceList = flight.getFlightpriceinfoList();
			for (Flightpriceinfo flightpriceinfo : priceList) {
				redisService.insertAndGetId(flightpriceinfo);
			}
		}
	}
	/**
	 * 
	 * @Description 用于方法之间的调度
	 * @author xdl
	 * @date 2018年3月5日 下午2:06:46
	 * @action apiTest
	 * @return void
	 */
	@Test
	public void apiTest(){
		//出发地到达地的数组
		String[][] cityArr = { { "上海", "三亚" }, { "三亚", "上海" }, { "上海", "海口" }, { "海口", "上海" } };
		int index = 0 ;
		 for(int i=0;i<cityArr.length;i++){
			 Date date = new Date();
			 for (int j = 0; j < 30; j++) {
				 System.out.println(index++);
				 Date dateData = DateUtils.addDays(date, 1 + j);
					SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
					String trueDate = sdf.format(dateData);
					save(parseXML(getXML(getUrl(cityArr[i][0], cityArr[i][1], trueDate)), cityArr[i][0], cityArr[i][1], trueDate));
			 }
		 }
	}
	
	
	public static void main(String[] args) {
		String url="http://www.lvmama.com/lvyou/";
		String result = PageDownLoadUtil.sendPost(url, "");
		System.out.println(result);
	}
}
