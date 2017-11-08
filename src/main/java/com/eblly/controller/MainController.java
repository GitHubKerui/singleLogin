package com.eblly.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import redis.clients.jedis.Jedis;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;


/**
 * 单点登录例子
 *
 * @author eblly
 * @date 2017-11-07
 */
@Controller
public class MainController {

    private Logger log = LoggerFactory.getLogger(this.getClass());

    private List<String> sesseionList = new ArrayList<>();

    private Jedis jedis = new Jedis("127.0.0.1", 6379);


    // 首页
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index(@CookieValue(value = "token", defaultValue = "-1") String token, ModelMap map) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest req = attr.getRequest();

        map.put("ip", req.getServerName());
        map.put("port", req.getServerPort());
        map.put("token", token);


        log.info("==>token: {}", token);
        if ("-1".equals(token)) {
            return "login";
        }

        List<String> tokenList = jedis.lrange("token", 0, -1);

        if (tokenList.contains(token)) {
            return "index";
        } else {
            return "login";
        }

    }

    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String login(@CookieValue(value = "token", defaultValue = "-1") String token, ModelMap map) {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest req = attr.getRequest();
        HttpServletResponse response = attr.getResponse();

        map.put("ip", req.getServerName());
        map.put("port", req.getServerPort());

        List<String> tokenList = jedis.lrange("token", 0, -1);


        if (!tokenList.contains(token)) {
            token = UUID.randomUUID().toString();
            Cookie cookie = new Cookie("token", token);
            cookie.setMaxAge(360000);
            response.addCookie(cookie);
//            sesseionList.add(token);
            jedis.lpush("token", token);

        }

        log.info("====>token:{} ", token);
        map.put("token", token);


        return "index";
    }

    @RequestMapping(value = "/clear", method = RequestMethod.GET)
    public String clear(ModelMap map) {

        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpServletRequest req = attr.getRequest();

        map.put("ip", req.getServerName());
        map.put("port", req.getServerPort());
//        sesseionList.clear();
        jedis.del("token");

        return "login";
    }

}