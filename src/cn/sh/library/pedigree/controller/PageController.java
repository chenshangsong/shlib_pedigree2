package cn.sh.library.pedigree.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by ly on 2014-11-21.
 */
@Controller
@RequestMapping("/page")
public class PageController {

    @RequestMapping("/{path}")
    public String page(@PathVariable("path") String path){
        return "page/" + path;
    }

}
