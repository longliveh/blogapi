package com.bobilwm.weibo.config.shiro.rewritefilter;

import com.bobilwm.weibo.controller.respmsg.Result;
import com.bobilwm.weibo.controller.respmsg.ResultCode;
import net.sf.json.JSONObject;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

public class ShiroFormAuthenticationFilter extends FormAuthenticationFilter {
	@Override
	protected boolean onAccessDenied(ServletRequest request, ServletResponse response) throws Exception {
		HttpServletResponse resp = (HttpServletResponse) response;
		resp.setContentType("application/json; charset=utf-8");
		PrintWriter out = resp.getWriter();
		Result result = Result.error(ResultCode.USER_NOT_LOGGED_IN);
		out.write(JSONObject.fromObject(result).toString()); // 返回自己的json
		out.flush();
		out.close();
		return false;
	}
}