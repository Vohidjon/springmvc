package com.springmvc.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by vohidjon-linux on 1/13/16.
 */
@Controller
@RequestMapping("")
public class EntryController {
    @RequestMapping(method = RequestMethod.GET)
    public String entry(){
        return "layout/index";
    }
}
