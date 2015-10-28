package com.wanglong.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import com.thoughtworks.xstream.XStream;
import com.wanglong.pro.TextMessage;

public class MessageUtil {
	
	public static final String MESSAGE_TEXT = "text";
	public static final String MESSAGE_IMAGE = "image";
	public static final String MESSAGE_VOICE = "voice";
	public static final String MESSAGE_VIDEO = "video";
	public static final String MESSAGE_LINK = "link";
	public static final String MESSAGE_LOCATION = "location";
	public static final String MESSAGE_EVENT = "event";
	public static final String MESSAGE_SUBSCRIBE = "subscribe";
	public static final String MESSAGE_UNSUBSCRIBE = "unsubscribe";
	public static final String MESSAGE_CLICK = "CLICK";
	public static final String MESSAGE_TVIEW = "VIEW";
	
	/**
	 * 将XML转成MAP集合
	 * @param request
	 * @return
	 * @throws IOException 
	 * @throws DocumentException 
	 */
	public static Map<String,String> xmlToMap(HttpServletRequest request) throws IOException, DocumentException{
		
		Map<String, String> map = new HashMap<String, String>();
		//dom4j 解析XML
		SAXReader reader =  new SAXReader();
		
		InputStream ins = request.getInputStream();//用户输入消息
		Document doc = reader.read(ins);//读取
		
		Element root = doc.getRootElement();//获取根目录
		
		List<Element> list = root.elements();//转为LIST
		
		//遍历list,保存至map中
		for(Element et : list){
			map.put(et.getName(), et.getText());
		}
		ins.close();
		return map;
	}
	/**
	 * 将对象转为XML
	 * @param textMessage
	 * @return
	 */
	public static String textMessageToXml(TextMessage textMessage){
		
		XStream xs = new XStream();
		xs.alias("xml", textMessage.getClass());
		return xs.toXML(textMessage);
		
	}
	
	/**
	 * 拼接主菜单
	 * @return
	 */
	public static String menuText(){
		StringBuffer sb = new StringBuffer();
		sb.append("欢迎关注，请按照菜单提示进行操作:\n\n");
		sb.append("1、关于我");
		sb.append("2、家乡");
		sb.append("3、其他");
		sb.append("回复? 调出此菜单");
		return sb.toString();
	}
	/**
	 * 拼接文本消息
	 * @return
	 */
	public static String initText(String fromUserName, String toUserName, String content){
		TextMessage text = new TextMessage();
		text.setFromUserName(toUserName);//设置从哪里来的人，就填到哪里去的人
		text.setToUserName(fromUserName);//设置到哪里去的人，就填从哪里来的人
		text.setMsgType(MESSAGE_TEXT);
		text.setCreateTime(new Date().getTime());
		text.setContent("您发送的消息是：" + content);
		return textMessageToXml(text);
		
	}
	
	public static String firstMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("我的名字是王龙");
		return sb.toString();
	}
	
	public static String secondMenu(){
		StringBuffer sb = new StringBuffer();
		sb.append("我的名字是王龙,我来自江苏泰州。");
		return sb.toString();
	}
	
}
