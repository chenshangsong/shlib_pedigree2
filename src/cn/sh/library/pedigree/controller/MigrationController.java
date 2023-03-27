package cn.sh.library.pedigree.controller;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import cn.sh.library.pedigree.services.MigrationService;

/**
 * Created by yesonme on 15-2-8.
 */
@Controller
@RequestMapping("/service/Migration")
public class MigrationController {
    private Logger logger = Logger.getLogger(MigrationController.class);

    @Resource
    private MigrationService migrationService;

    @RequestMapping(value = "/list", method = RequestMethod.GET)
    public String list(){
        return "Migration/list";
    }

    @RequestMapping(value = "/track", method = RequestMethod.GET)
    public String bmap(){
        return "Migration/track";
    }

//    @ResponseBody
//    @RequestMapping(value = "/persons", method = RequestMethod.GET)
//    public String getTree(){
//        return this.migrationService.familyTimeline();
//    }

    //迁徙图
    @ResponseBody
    @RequestMapping(value = "/locations", method = RequestMethod.GET)
    public String locations(){
        return this.migrationService.migrationLocations();
    }

    @ResponseBody
    @RequestMapping(value = "/geocoords", method = RequestMethod.GET)
    public String geocoords(){
        return this.migrationService.migrationGeoCoords();
    }

    @ResponseBody
    @RequestMapping(value = "/lines", method = RequestMethod.GET)
    public String lines(){
        return this.migrationService.migrationLines();
    }
}