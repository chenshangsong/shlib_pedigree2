package cn.sh.library.pedigree.controller;

import java.io.ByteArrayOutputStream;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.hp.hpl.jena.rdf.model.Statement;

import cn.sh.library.pedigree.base.Constant;
import cn.sh.library.pedigree.common.CommonSparql;
import cn.sh.library.pedigree.sparql.VocabSparql;
import cn.sh.library.pedigree.utils.RDFUtils;

/**
 * Created by ly on 2015-1-12.
 */
@Controller
public class RDFController {

    private Logger logger = Logger.getLogger(RDFController.class);

    @Resource
    public CommonSparql commonSparql;

    @Resource
    public VocabSparql vocabSparql;

    @ResponseBody
    @RequestMapping(value = {"/work/{id}", "/person/{id}", "/instance/{id}",
            "/place/{id}", "/annotation/{id}", "/category/{id}",
            "/family/{id}"
    },
            headers = "accept=application/rdf+xml", produces = "application/rdf+xml")
    public void rdf(HttpServletRequest request, HttpServletResponse response){
        try {
            String url = request.getRequestURL().toString();
            URL _url = new URL(url);
            if(!_url.getHost().equals("http://data.library.sh.cn/jp/entity")){
                url = "http://data.library.sh.cn/jp/entity" + request.getRequestURI();
            }
            ByteArrayOutputStream bos = (ByteArrayOutputStream) this.commonSparql.getTriples(getGraphName(url), false, url);
            byte[] data = bos.toByteArray();
            String rdfData = new String(data, Charset.forName("utf-8"));
            response.getWriter().write(rdfData);
        } catch( Exception e ){
            logger.error(e);
        }
    }


    @RequestMapping(value = {"/work/{id}", "/person/{id}", "/instance/{id}",
            "/place/{id}", "/annotation/{id}", "/category/{id}","/family/{id}"
    },
            headers = "accept=text/html", produces = "text/html")
    public String rdfView(Model model, HttpServletRequest request, HttpServletResponse response){
        try {
            String url = request.getRequestURL().toString();
            URL _url = new URL(url);
            if(!_url.getHost().equals("http://data.library.sh.cn/jp/entity")){
                url = "http://data.library.sh.cn/jp/entity" + request.getRequestURI();
            }
            List<Statement> stats = this.commonSparql.getTriples(getGraphName(url), url);
            Collection<Map<String, String>> list = CollectionUtils.collect(stats, new Transformer() {
                @Override
                public Object transform(Object input) {
                    Statement statement = (Statement) input;
                    String predicate = RDFUtils.toString(statement.getPredicate());
                    String label = vocabSparql.getPropertyLabel(predicate);
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("label", label);
                    map.put("value", RDFUtils.toString(statement.getObject()));
                    return map;
                }
            });
            model.addAttribute("stats", list);
            model.addAttribute("url", url);
            return "rdf/view";
        } catch( Exception e ){
            logger.error(e);
            throw new RuntimeException(e);
        }
    }

    public static String getGraphName(String uri) {
        if (uri.contains("/person/")) {
            return Constant.GRAPH_PERSON;
        } else {
            return Constant.GRAPH_BASEINFO;
        }
    }
}
