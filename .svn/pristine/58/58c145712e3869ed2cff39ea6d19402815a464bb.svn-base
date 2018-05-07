package cn.jj.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Authenticator;
import java.net.InetSocketAddress;
import java.net.PasswordAuthentication;
import java.net.Proxy;
import java.net.Socket;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

/**
 * URL对象访问工具类 启动运用通过JVM参数走代理, 注意这种代理是全局的,设置以后全部会自动走代理, 如果需要单个请求走代理(在走代理失败的话,
 * 会自动尝试本地直接访问), 请使用proxy_protocol()协议代理函数 -DproxySet=true
 * -Dhttp.proxyHost=proxyIp -Dhttp.proxyPort=proxyPort
 * 
 * @author 徐仁杰
 * 
 */
public class UrlUtils {

	static {
		// 全局禁止ssl证书验证，防止访问非验证的https网址无法访问
		disableSslVerification();
	}

	public static void disableSslVerification() {
		try {
			// 创建不验证证书链的信任管理器
			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
				public java.security.cert.X509Certificate[] getAcceptedIssuers() {
					return null;
				}

				public void checkClientTrusted(X509Certificate[] certs, String authType) {
				}

				public void checkServerTrusted(X509Certificate[] certs, String authType) {
				}
			} };

			// 安装信任的信任经理
			SSLContext sc = SSLContext.getInstance("SSL");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());

			// 创建全信任主机名验证器
			HostnameVerifier allHostsValid = new HostnameVerifier() {
				public boolean verify(String hostname, SSLSession session) {
					return true;
				}
			};

			// 安装全信任主机验证器
			HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (KeyManagementException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @author 徐仁杰
	 * @param proxyType
	 *            代理ip协议类型
	 * @param proxyStr
	 *            代理ip及端口号
	 * @param urlStr
	 *            需要访问的网址
	 * @param timeOut
	 *            设置连接超时时间,(单位:秒)
	 * @return
	 */
	public static String proxy_property(String proxyType, String proxyStr, String urlStr, Integer timeOut) {
		String data = null;
		String proxy_ip = proxyStr.split(":")[0];
		String proxy_port = proxyStr.split(":")[1];
		// 获取当前系统属性配置对象
		Properties prop = System.getProperties();
		// http
		if (proxyType.equals("http") || proxyType.contains("HTTP")) {
			prop.setProperty("http.proxySet", "true");
			prop.setProperty("http.proxyHost", proxy_ip);
			prop.setProperty("http.proxyPort", proxy_port);
			prop.setProperty("http.nonProxyHosts", "localhost|192.168.0.*");
		}
		// https
		if (proxyType.equals("https") || proxyType.contains("HTTPS")) {
			prop.setProperty("https.proxyHost", proxy_ip);
			prop.setProperty("https.proxyPort", proxy_port);
		}
		// socks
		if (proxyType.equals("socks4") || proxyType.equals("socks5") || proxyType.contains("Socks4")
				|| proxyType.contains("Socks5")) {
			prop.setProperty("socksProxySet", "true");
			prop.setProperty("socksProxyHost", proxy_ip);
			prop.setProperty("socksProxyPort", proxy_port);
		}
		// ftp
		if (proxyType.equals("ftp")) {
			prop.setProperty("ftp.proxyHost", proxy_ip);
			prop.setProperty("ftp.proxyPort", proxy_port);
			prop.setProperty("ftp.nonProxyHosts", "localhost|192.168.0.*");
		}
		// // auth 设置登录代理服务器的用户名和密码
		// Authenticator.setDefault(new MyAuthenticator("user", "pwd"));
		try {
			URL url = new URL(urlStr);
			URLConnection conn = url.openConnection();
			conn.setConnectTimeout(timeOut * 1000);

			InputStream in = conn.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			char[] ch = new char[1024];
			int len = 0;
			StringBuffer strb = new StringBuffer();
			while ((len = reader.read(ch)) > 0) {
				String newData = new String(ch, 0, len);
				strb.append(newData);
			}
			data = strb.toString();
			System.out.println("data : " + data);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * @author 徐仁杰 加密代理账号密码登陆
	 *
	 */
	static class MyAuthenticator extends Authenticator {
		private String user = "";
		private String password = "";

		public MyAuthenticator(String user, String password) {
			this.user = user;
			this.password = password;
		}

		protected PasswordAuthentication getPasswordAuthentication() {
			return new PasswordAuthentication(user, password.toCharArray());
		}
	}

	/**
	 * 函数协议，仅支持 HTTP 和 SOCKS5
	 * 
	 * @author 徐仁杰
	 * @param proxyType
	 *            代理ip协议类型
	 * @param proxyStr
	 *            代理ip及端口号
	 * @param urlStr
	 *            需要访问的网址
	 * @return
	 */
	public static String proxy_protocol(String proxyType, String proxyStr, String urlStr) {
		String data = null;
		// -DproxySet=true
		// -Dhttp.proxyHost=proxyIp
		// -Dhttp.proxyPort=proxyPort

		String proxy_ip = proxyStr.split(":")[0];
		int proxy_port = Integer.parseInt(proxyStr.split(":")[1]);

		try {
			URL url = new URL(urlStr);

			InetSocketAddress addr = new InetSocketAddress(proxy_ip, proxy_port);
			Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
			if (proxyType.equals("socks4") || proxyType.equals("socks5")) {
				proxy = new Proxy(Proxy.Type.SOCKS, addr);
			}

			URLConnection conn = url.openConnection(proxy);
			conn.setConnectTimeout(30 * 1000);

			InputStream in = conn.getInputStream();
			InputStreamReader reader = new InputStreamReader(in);
			char[] ch = new char[1024];
			int len = 0;
			StringBuffer strb = new StringBuffer();
			while ((len = reader.read(ch)) > 0) {
				String newData = new String(ch, 0, len);
				strb.append(newData);
			}
			System.out.println("data : " + data);
			data = strb.toString();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * sokect代理测试
	 * 
	 * @author 徐仁杰
	 * @param proxyType
	 *            代理ip协议类型
	 * @param proxyStr
	 *            代理ip及端口号
	 * @return
	 */
	public static int proxy_socks(String proxyType, String proxyStr) {
		int dataLen = 0;
		Socket socket = null;

		String proxy_ip = proxyStr.split(":")[0];
		int proxy_port = Integer.parseInt(proxyStr.split(":")[1]);

		try {
			socket = new Socket(proxy_ip, proxy_port);

			byte[] ch = new String("GET http://www.mimvp.com/ HTTP/1.1\r\n\r\n").getBytes();
			socket.getOutputStream().write(ch);
			socket.setSoTimeout(30 * 1000);

			byte[] bt = new byte[1024];
			InputStream in = socket.getInputStream();
			int len = 0;
			String data = "";
			while ((len = in.read(bt)) > 0) {
				String newData = new String(bt, 0, len);
				data += newData;
			}
			System.out.println("data : " + data);
			dataLen = data.length();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (socket != null) {
					socket.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
			socket = null;
		}
		return dataLen;
	}

	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @author 徐仁杰
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPost(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36"
					+ " (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			//System.out.println("发送 POST 请求出现异常！" + e);
			//System.out.println(url + "\n" + param);
			//e.printStackTrace();
			return result;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @author 徐仁杰
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendByHeaderPost(String url, String param,Map<String,String> map) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			for(String key:map.keySet()){
				conn.setRequestProperty(key, map.get(key));
			}
			
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			System.out.println(url + "\n" + param);
			//e.printStackTrace();
			return result;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		return result;
	}
	
	/**
	 * 向指定 URL 发送POST方法的请求
	 * 
	 * @author 徐仁杰
	 * @param url
	 *            发送请求的 URL
	 * @param param
	 *            请求参数，请求参数应该是 Json 的形式。
	 * @return 所代表远程资源的响应结果
	 */
	public static String sendPostForJson(String url, String param) {
		PrintWriter out = null;
		BufferedReader in = null;
		String result = "";
		try {
			URL realUrl = new URL(url);
			// 打开和URL之间的连接
			URLConnection conn = realUrl.openConnection();
			// 设置通用的请求属性
			conn.setRequestProperty("accept", "*/*");
			conn.setRequestProperty("connection", "Keep-Alive");
			conn.setRequestProperty("user-agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36"
					+ " (KHTML, like Gecko) Chrome/61.0.3163.100 Safari/537.36");
			conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
			conn.setRequestProperty("Accept-Language", "zh-CN,zh;q=0.8");
			// 发送POST请求必须设置如下两行
			conn.setDoOutput(true);
			conn.setDoInput(true);
			// 获取URLConnection对象对应的输出流
			out = new PrintWriter(conn.getOutputStream());
			// 发送请求参数
			out.print(param);
			// flush输出流的缓冲
			out.flush();
			// 定义BufferedReader输入流来读取URL的响应
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String line;
			while ((line = in.readLine()) != null) {
				result += line;
			}
		} catch (Exception e) {
			System.out.println("发送 POST 请求出现异常！" + e);
			System.out.println(url + "\n" + param);
			//e.printStackTrace();
			return result;
		}
		// 使用finally块来关闭输出流、输入流
		finally {
			try {
				if (out != null) {
					out.close();
				}
				if (in != null) {
					in.close();
				}
			} catch (IOException ex) {
				//ex.printStackTrace();
			}
		}
		return result;
	}
	
	public static void main(String[] args) {
		Map<String,String> param=new HashMap<String, String>();
		//param.put("X-Requested-With", "XMLHttpRequest");
		param.put("Referer", "https://www.ly.com/searchlist.html?cityid=137");
		param.put("Cookie", "wangba=1511314396467");
		String str=sendByHeaderPost("https://www.ly.com/hotel/api/search/hotellist", 
							"CityId=137&ComeDate=2017-11-23&LeaveDate=2017-11-24&PageSize=20&Page=3&antitoken=ecb5fecb4609690676f6f2c6922861e8", param);
		
		System.out.println(str);
	}
}
