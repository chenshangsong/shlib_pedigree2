package cn.sh.library.pedigree.services.impl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

import javax.annotation.Resource;

import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.AnonId;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.util.iterator.Filter;

import cn.sh.library.pedigree.dto.ClassView;
import cn.sh.library.pedigree.dto.Vocab;
import cn.sh.library.pedigree.ontology.view.VocabOnt;
import cn.sh.library.pedigree.ontology.visualizer.AnalyzedGraph;
import cn.sh.library.pedigree.ontology.visualizer.NodeToSVG;
import cn.sh.library.pedigree.services.VocabService;
import cn.sh.library.pedigree.utils.RDFUtils;

/**
 * Created by Yi on 2014/10/27 0027.
 */
@Service
public class VocabServiceImpl extends BaseServiceImpl implements VocabService {

    @Resource
    private VocabOnt vocabOnt;
    @Resource
    private OntModel ontModel;

    @Override
    @Cacheable(value = "vocabClassesCache")
    public List<Vocab> listClasses() {
    	try {
    		 List<String> infoList = vocabOnt.getShortClasses();
    	        System.out.println(infoList.get(0));
    	        List<Vocab> vocabList = new ArrayList<Vocab>();
    	        Vocab vocab = null;
    	        for (String info : infoList) {
    	            vocab = new Vocab();
    	            vocab.setLabel(info);
    	            vocab.setAttributes(wrapper(vocabOnt.getClassInfos(info)));
    	            vocabList.add(vocab);
    	        }
    	        return vocabList;
		} catch (Exception e) {
			System.out.println(e);
		}
       return null;
       
    }

    @Override
    @Cacheable(value = "vocabPropertiesCache")
    public List<Vocab> listProperties() {
        List<String> infoList = vocabOnt.getShortProperties();
        List<Vocab> vocabList = new ArrayList<Vocab>();
        Vocab vocab = null;
        for (String info : infoList) {
            vocab = new Vocab();
            vocab.setLabel(info);
            vocab.setAttributes(wrapper(vocabOnt.getPropertyInfos(info)));
            vocabList.add(vocab);
        }
        return vocabList;
    }

    private List<Map<String, String>> wrapper(ArrayList attrs) {
        List<Map<String, String>> results = new ArrayList<>();


        for (int i=0;i<attrs.size();i++) {
            Map<String, String> attr = new HashMap<>();

            String key = ((Map) attrs.get(i)).get("p").toString();
            String value = ((Map) attrs.get(i)).get("o").toString();
            String link = RDFUtils.getLink(ontModel, value);

            attr.put("name", key);
            attr.put("value", value);
            if (StringUtils.isNotEmpty(link)) {
                if (!value.startsWith("shlgen:"))
                    attr.put("link", link);
            }
            results.add(attr);
        }

        return results;
    }

    @Override
    public List<ClassView> listClassViews(String parent) {
        List<ClassView> resultList = new ArrayList<ClassView>();
        List<String> classes = null;
        if (StringUtils.isEmpty(parent)) {
            classes = vocabOnt.getSuperClasses();
        } else {
            classes = vocabOnt.getSubClasses(parent);
        }
        if (classes != null) {
            for (String className : classes) {
                ClassView modelClass = new ClassView();
                modelClass.setClassName(className);
                modelClass.setChildren(listClassViews(className));
                resultList.add(modelClass);
            }
        }
        return resultList;
    }

    @Override
    public Map<String, Map<String, String>> listClassProperties(String className) {
        Assert.hasText(className);
        Map<String, Map<String, String>> results = new HashMap<>();
        className = StringUtils.trim(className);
        List<String> properties = this.vocabOnt.getClassProperties(className);
        if (properties == null) {
            return null;
        }

        for (String property : properties) {
            Map<String, String> attrs = new HashMap<>();
            ArrayList list = this.vocabOnt.getPropertyInfos(property);

            String nameLink = RDFUtils.getLink(ontModel, property);
            attrs.put("name", property);

            String rangeLink = "";
            String domainLink = RDFUtils.getLink(ontModel, className);
            attrs.put("domain", className);

            String subPropertyOfLink = "";

            for (int i=0;i<list.size();i++) {
                String p = ((Map) list.get(i)).get("p").toString();
                String o = ((Map) list.get(i)).get("o").toString();

                if (p.equals("rdfs:range")) {
                    rangeLink = RDFUtils.getLink(ontModel, o);
                    attrs.put("range", o);
                } else if (p.equals("rdfs:subPropertyOf")) {
                    subPropertyOfLink = RDFUtils.getLink(ontModel, o);
                    attrs.put("subPropertyOf", o);
                } else if (p.equals("rdfs:label")) {
                    attrs.put("label", o);
                }
            }

            if (StringUtils.isNotEmpty(nameLink)) {
                attrs.put("name.link", nameLink);
            }
            if (StringUtils.isNotEmpty(rangeLink)) {
                attrs.put("range.link", rangeLink);
            }
            if (StringUtils.isNotEmpty(domainLink)) {
                attrs.put("domain.link", domainLink);
            }
            if(StringUtils.isNotEmpty(subPropertyOfLink)){
                attrs.put("subPropertyOf.link", subPropertyOfLink);
            }

            results.put(property, attrs);
        }
        return results;
    }

    @Override
    public List<String> listModelClasses() {
        List<String> classes = this.vocabOnt.getModelViewClasses();
        return classes;
    }

    @Override
    public String getLink(String className) {
        return RDFUtils.getLink(ontModel, className);
    }

    @Override
    public String getComment(String className) {
        return this.vocabOnt.getClassComment(className);
    }

    @Override
    @Cacheable(value = "vocabRdfCache")
    public String rdfData() {
        ByteArrayOutputStream bos = (ByteArrayOutputStream) this.vocabOnt.write();
        byte[] data = bos.toByteArray();
        String rdfData = new String(data, Charset.forName("utf-8"));
        return rdfData;
    }

    @Override
    public String rdfData(String term) {
        ByteArrayOutputStream bos = (ByteArrayOutputStream) this.vocabOnt.write(term);
        byte[] data = bos.toByteArray();
        String rdfData = new String(data, Charset.forName("utf-8"));
        return rdfData;
    }

    @Override
    public String modelGraph(String className, String uri) {

        ByteArrayOutputStream output = new ByteArrayOutputStream();

        PrintStream out = null;
        try {
            out = new PrintStream(output, true, "utf-8");
        } catch( Exception e){}

        String resourceString = RDFUtils.getLink(this.ontModel, className);
        if( StringUtils.isEmpty(resourceString)) resourceString = uri;
        resourceString = resourceString.replaceAll(";hash;", "#").replaceAll(";dot;", ".");

        com.hp.hpl.jena.rdf.model.Resource toDisplay;
        com.hp.hpl.jena.rdf.model.Model tempModel = ModelFactory.createDefaultModel();

        if (resourceString.startsWith("_")) {
            resourceString = resourceString.substring(1); // remove leading "_"
            toDisplay = tempModel.createResource(new AnonId(resourceString));
        } else
            toDisplay = tempModel.createResource(resourceString);

        NodeToSVG.PageInfo pageInfo = new NodeToSVG.PageInfo();
        String modelName = "";

        pageInfo.resourceToHREF = new myResourceToHREF(modelName, null, "list");
        pageInfo.propertyToHREF = new myResourceToHREF(modelName, null, "arcs");
        pageInfo.maxBackArcs = 1;
        pageInfo.maxForwardArcs = 2;
        AnalyzedGraph modelToVisualize = this.vocabOnt.getAnalyzedGraph();
        NodeToSVG visualizer = new NodeToSVG();

        visualizer.visualizeStart(out, pageInfo);
        visualizer.visualizeSubHeading(out, pageInfo, modelName);
        visualizer.advancePage(pageInfo);
//            if (i == 0 && styleString != null && styleString.equalsIgnoreCase("arcs")) {
                // first look for and display any nodes with a matching
                // predicate
                SortedSet arcs = modelToVisualize.findArcInfos(toDisplay);
                if (arcs != null) {
                    Iterator ait = arcs.iterator();
                    pageInfo.maxBackArcs = 0;
                    pageInfo.maxForwardArcs = 1;
                    int previousMaxLiteralLines = pageInfo.maxLiteralLines;
                    pageInfo.maxLiteralLines = 2;
                    pageInfo.ySpacing *= 0.5;
//                    if (searchString != null)
//                        pageInfo.literalsToHighlight = modelToVisualize
//                                .findLiteralNodeInfos(searchString);
                    int count = 0;
                    while (ait.hasNext() && count <= 10) {
                        AnalyzedGraph.ArcInfo ainfo = (AnalyzedGraph.ArcInfo) ait.next();
                        if (count++ == 10 && arcs.size() > 11) {
                            visualizer.visualizeVerticalContinuation(out, pageInfo);
                            ainfo = (AnalyzedGraph.ArcInfo) arcs.last();
                        }
                        visualizer.visualizeNodeInfo(out, modelToVisualize, ainfo.start,
                                Filter.any, new EqualityFilter(ainfo),
                                pageInfo);
                    }
                    pageInfo.ySpacing *= 2.0;
                    pageInfo.maxLiteralLines = previousMaxLiteralLines;
                    if (arcs.size() > 0)
                        visualizer.advancePage(pageInfo, 2.0);
                    pageInfo.maxBackArcs = 2;
                    pageInfo.maxForwardArcs = 2;
                }
//            }
        visualizer.visualizeNode(out, modelToVisualize, toDisplay, Filter.any,
                Filter.any, pageInfo);
        visualizer.visualizeEnd(out, pageInfo);

        byte[] data = output.toByteArray();
        String graph = new String(data, Charset.forName("utf-8"));
        return graph;
    }

    private class myResourceToHREF implements NodeToSVG.ResourceToString {
        String _search;
        String _modelName;
        String _style;

        public myResourceToHREF(String modelName, String search, String style) {
            _search = search;
            _modelName = modelName;
            _style = style;
        }

        /*
         * (non-Javadoc)
         *
         * @see com.hp.hpl.jena.rdf.visualize.RDFNodeToHREF#convert(com.hp.hpl.jena.rdf.model.RDFNode)
         */
        public String convert(com.hp.hpl.jena.rdf.model.Resource node) {
            try {
                String searchString;
                if (_search == null)
                    searchString = "";
                else
                    searchString = "&amp;search=" + URLEncoder.encode(_search, "UTF-8");
                String uri;
                if (node.isAnon())
                    uri = URLEncoder.encode("_" + node.getId(), "UTF-8");
                else
                    uri = URLEncoder.encode(node.getURI(), "UTF-8");
                uri = uri.replaceAll("#", ";hash;");//.replaceAll("\\.",";dot;");
                return node.toString();
            } catch (Exception e) {
                return null;
            }
        }

        /*
         * (non-Javadoc)
         *
         * @see com.hp.hpl.jena.rdf.visualize.NodeVisualizer.ResourceToString#convert(com.hp.hpl.jena.rdf.model.Resource,
         *      int)
         */
        public String convert(com.hp.hpl.jena.rdf.model.Resource resource, int length) {
            return convert(resource);
        }
    }

//    private static class ArcInfoDestinationNodeFilter extends Filter {
//
//        Set _set;
//
//        public ArcInfoDestinationNodeFilter(Set set) {
//            _set = set;
//        }
//
//        /* (non-Javadoc)
//         * @see com.hp.hpl.jena.util.iterator.Map1#map1(java.lang.Object)
//         */
//        public boolean accept(Object o) {
//            AnalyzedGraph.ArcInfo arcInfo = (AnalyzedGraph.ArcInfo) o;
//            AnalyzedGraph.NodeInfo end = arcInfo.end;
//            return _set.contains(end);
//        }
//    }
//
//
    private static class EqualityFilter extends Filter {
        Object object;

        public EqualityFilter(Object o) {
            object = o;
        }

        public boolean accept(Object o) {
            return o.equals(object);
        }
    }
}
