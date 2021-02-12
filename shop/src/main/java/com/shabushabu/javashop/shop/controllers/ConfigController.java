package com.shabushabu.javashop.shop.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;


@Controller
public class ConfigController {

    public boolean lbenabled = true;    

    private static Config loadbalancerEnabled = new Config(){{setLb("true");}};

    @GetMapping("/Config")

    public String setConfig(@ModelAttribute() Config config, Model model, @RequestParam(value="lb", required=false) String lb) {

        if(lb == null){
            model.addAttribute("status", loadbalancerEnabled.getLBStatus());
        } else{
            loadbalancerEnabled.setLb(lb);
            model.addAttribute("status", loadbalancerEnabled.getLBStatus());
        }
        return "config";
	}

    public static Boolean getLoadBalancer(){
        return Boolean.parseBoolean(loadbalancerEnabled.getLb());
    }
}