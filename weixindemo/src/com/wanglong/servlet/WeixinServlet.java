package com.wanglong.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.dom4j.DocumentException;

import com.wanglong.pro.TextMessage;
import com.wanglong.util.CheckUtil;
import com.wanglong.util.MessageUtil;

public class WeixinServlet extends HttpServlet {

	/**
	 * 验证服务器地址的有效性
	 * 
	 * 开发者提交信息后，微信服务器将发送GET请求到填写的服务器地址URL上，GET请求携带四个参数
	 */
	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		/*
		 * 加密/校验流程如下： 1. 将token、timestamp、nonce三个参数进行字典序排序 2.
		 * 将三个参数字符串拼接成一个字符串进行sha1加密 3. 开发者获得加密后的字符串可与signature对比，标识该请求来源于微信
		 */
		// 微信加密签名，signature结合了开发者填写的token参数和请求中的timestamp参数、nonce参数
		String signature = req.getParameter("signature");
		// 时间戳
		String timestamp = req.getParameter("timestamp");
		// 随机数
		String nonce = req.getParameter("nonce");
		// 随机字符串
		String echostr = req.getParameter("echostr");

		// 返回对象
		PrintWriter out = resp.getWriter();

		// 开发者通过对签名（即signature）的效验，来判断此条消息的真实性。
		// 如果成功则返回之前传入的echostr参数值
		if (CheckUtil.checkSignature(signature, timestamp, nonce)) {
			out.print(echostr);
		}

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		req.setCharacterEncoding("UTF-8");
		resp.setCharacterEncoding("UTF-8");
		PrintWriter out = resp.getWriter();

		// 获取用户输入的XML格式消息，进行XML解析后处理，处理后组装为XML发送到服务器
		try {
			Map<String, String> map = MessageUtil.xmlToMap(req);

			String toUserName = map.get("ToUserName");
			String fromUserName = map.get("FromUserName");
			String msgType = map.get("MsgType");
			String content = map.get("Content");

			String message = null;

			if (MessageUtil.MESSAGE_TEXT.equals(msgType)) {
				TextMessage text = new TextMessage();

				if ("1".equals(content)) {
					message = MessageUtil.initText(fromUserName, toUserName,
							MessageUtil.firstMenu());
				} else if ("2".equals(content)) {
					message = MessageUtil.initText(fromUserName, toUserName,
							MessageUtil.secondMenu());
				} else if ("?".equals(content) || "？".equals(content)) {
					message = MessageUtil.initText(fromUserName, toUserName,
							MessageUtil.menuText());
				}

			} else if (MessageUtil.MESSAGE_EVENT.equals(msgType)) {
				String type = map.get("Event");
				if (MessageUtil.MESSAGE_SUBSCRIBE.equals(type)) {
					message = MessageUtil.initText(fromUserName, toUserName,
							MessageUtil.menuText());
				}

			}
			System.out.println(message);
			out.print(message);

		} catch (DocumentException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}

		super.doPost(req, resp);
	}

}
