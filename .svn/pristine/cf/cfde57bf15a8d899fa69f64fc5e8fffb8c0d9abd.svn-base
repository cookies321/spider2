package cn.jj.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public class StrUtils {

	public static void main(String[] args) {

	}

	/**
	 * @author 徐仁杰
	 * @param 传入一个字符串
	 * @return 返回字符串中所有大写字母
	 */
	public static String getBigEnglish(String str) {
		StringBuffer stb = new StringBuffer();
		if (StringUtils.isNotBlank(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 'A' && str.charAt(i) <= 'Z') {
					stb.append(str.charAt(i));
				}
			}
		}
		return stb.toString();
	}

	/**
	 * 
	 * @Description TODO
	 * @author 赵乐
	 * @date 2017年11月22日 下午12:46:54
	 * @action getAllEnglish
	 * @param @param
	 *            传入一个字符串
	 * @return 返回字符串中所有字母
	 */
	public static String getAllEnglish(String str) {
		StringBuffer stb = new StringBuffer();
		if (StringUtils.isNotBlank(str)) {
			for (int i = 0; i < str.length(); i++) {
				if ((str.charAt(i) >= 'A' && str.charAt(i) <= 'Z') || (str.charAt(i) >= 'a' && str.charAt(i) <= 'z')) {
					stb.append(str.charAt(i));
				}
			}
		}
		return stb.toString();
	}

	/**
	 * @Description 携程门票页面获取价格详情GET请求参数分析
	 * @author 徐仁杰
	 * @date 2017年11月11日 下午3:35:39
	 * @action lvmamaGetPriceParse
	 * @return String
	 */
	public static String xiechengGetPriceParse(String str) {
		try {
			String[] split = str.replace("{", "").replace("}", "").replace("'", "").split(",");
			// String ResourceID = split[0];
			// Integer resourceID = NumUtils.getInteger(ResourceID);
			String ProductID = split[1];
			Integer productID = NumUtils.getInteger(ProductID);
			// String ProductItemID = split[2];
			// Integer productItemID = NumUtils.getInteger(ProductItemID);
			String ScenicSpotID = split[3];
			Integer scenicSpotID = NumUtils.getInteger(ScenicSpotID);
			// map.put("resourceID", resourceID);
			// map.put("productID", productID);
			// map.put("productItemID", productItemID);
			// map.put("scenicSpotID", scenicSpotID);
			String data = productID + "," + scenicSpotID;
			return data;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @Description 过滤特殊字符
	 * @author 汤玉林
	 * @date 2017年11月13日 下午4:19:22
	 * @action StringFilter
	 * @return String
	 */
	public static String StringFilter(String str) {
		String regex = "[`~!@#$%^&*()+=|{}':;',\\[\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(str);
		return m.replaceAll("").trim();

	}
}
