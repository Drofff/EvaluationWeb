package com.drofff.edu.component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.drofff.edu.cache.AttendanceCache;
import com.drofff.edu.cache.OnlineCache;
import com.drofff.edu.entity.User;

@Component
public class OnlineInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOG = LoggerFactory.getLogger(OnlineInterceptor.class);
	private static final String ORIGINAL_IP_ADDRESS_HEADER = "X-Forwarded-For";

	private final OnlineCache onlineCache;
	private final AttendanceCache attendanceCache;
	private final UserContext userContext;

	@Autowired
	public OnlineInterceptor(OnlineCache onlineCache, AttendanceCache attendanceCache,
	                         UserContext userContext) {
		this.onlineCache = onlineCache;
		this.attendanceCache = attendanceCache;
		this.userContext = userContext;
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		LOG.debug("Request:\n\tIP address = {}\n\tmethod = {}\n\trequestURI = {}",
				getRemoteAddress(request), request.getMethod(), request.getRequestURI());
		User currentUser = userContext.getCurrentUser();
		if(currentUser != null) {
			onlineCache.setOnline(currentUser);
			attendanceCache.refreshAttendance(currentUser);
		}
		return true;
	}

	private String getRemoteAddress(HttpServletRequest request) {
		String originalIpAddress = request.getHeader(ORIGINAL_IP_ADDRESS_HEADER);
		return originalIpAddress == null ? request.getRemoteAddr() : originalIpAddress;
	}

}
