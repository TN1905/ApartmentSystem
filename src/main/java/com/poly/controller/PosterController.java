package com.poly.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class PosterController {
    @RequestMapping("/poster/index")
    public String index() {
        return  "poster/index";
    }
    @RequestMapping("/poster/dangtinmoi")
    public String dangtin() {
        return  "poster/dangtinmoi";
    }
    @RequestMapping("/poster/lichsunaptien")
    public String lichsunaptien() {
        return  "poster/lichsunaptien";
    }
    @RequestMapping("/poster/lichsugiaodich")
    public String lichsugiaodich() {
        return  "poster/lichsugiaodich";
    }
    @RequestMapping("/poster/thongtincanhan")
    public String thongtincanhan() {
        return  "poster/thongtincanhan";
    }
    @RequestMapping("/poster/lienhe")
    public String lienhe() {
        return  "poster/lienhe";
    }
}
