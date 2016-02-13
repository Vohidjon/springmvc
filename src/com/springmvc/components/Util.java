package com.springmvc.components;

import com.google.gson.Gson;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by vohidjon-linux on 1/11/16.
 */
@Component
public class Util {
    private Gson gson = new Gson();
    private SimplePager pager = new SimplePager();
    public String toGson(Object o) {
        return gson.toJson(o, o.getClass());
    }
    public SimplePager getPager() {
        return pager;
    }
}
