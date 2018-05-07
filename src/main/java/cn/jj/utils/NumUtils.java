package cn.jj.utils;

import java.util.Random;

import org.apache.commons.lang3.StringUtils;

/**
 * 数字类型转换工具类,测试工具类的代码写在最下面的main方法中,测试完毕后请删除
 * 
 * @author 徐仁杰
 *
 */
public class NumUtils {

	/**
	 * @Description 用于截取内容中的整型数据
	 * @author 徐仁杰
	 * @date 2017年11月11日 下午3:30:57
	 * @action getInteger
	 * @return Integer 返回字符串中所有数字
	 */
	public static Integer getInteger(String str) {
		StringBuffer stb = new StringBuffer();
		if (StringUtils.isNotBlank(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					stb.append(str.charAt(i));
				}
			}
		}
		if (StringUtils.isNotBlank(stb.toString())) {
			return Integer.valueOf(stb.toString());
		} else {
			return null;
		}

	}

	/**
	 * @Description 用于截取内容中的浮点型数据
	 * @author 徐仁杰
	 * @date 2017年11月11日 下午3:30:55
	 * @action getDouble
	 * @return Double 返回字符串中所有数字
	 */
	public static Double getDouble(String str) {
		StringBuffer stb = new StringBuffer();
		if (StringUtils.isNotBlank(str)) {
			for (int i = 0; i < str.length(); i++) {
				if (".".equals(str.charAt(i) + "") || str.charAt(i) >= 48 && str.charAt(i) <= 57) {
					stb.append(str.charAt(i));
				}
			}
		}
		return Double.valueOf(stb.toString());
	}

	/**
	 * @Description 输入一个最大值和最小值，获取一个范围内的随机数
	 * @author 徐仁杰
	 * @date 2017年11月11日 下午3:30:53
	 * @action getRandom
	 * @return Integer
	 */
	public static Integer getRandom(Integer max, Integer min) {
		Random random = new Random();
		int s = random.nextInt(max) % (max - min + 1) + min;
		return s;
	}

	public static void main(String[] args) {

	}
}
