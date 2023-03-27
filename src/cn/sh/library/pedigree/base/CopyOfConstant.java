package cn.sh.library.pedigree.base;


import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Administrator on 14-3-5.
 */
public class CopyOfConstant {
	  private static final String HOST = "http://gen.library.sh.cn/";
//    private static final String DATA_HOST = "http://data.library.sh.cn/";
    private static final String CBA = "http://www.cba.ac.cn/";

    //Ontology
    public static final String GRAPH_VOCAB = HOST + "graph/vocab"; //本体词表
//    public static final String GRAPH_DATA = DATA_HOST + "graph/baseinfo"; //数据

    //Graph
    public static final String GRAPH_PERSON = HOST + "graph/person"; //家谱名人+责任者
    public static final String GRAPH_BASEINFO = HOST + "graph/baseinfo"; //基础信息，朝代+姓氏表+Category
    //public static final String GRAPH_BASEINFO = HOST + "graph/basechen"; //数据导入测试
    public static final String GRAPH_INSTANCE = HOST + "graph/instance"; //家谱实例
    public static final String GRAPH_WORK = HOST + "graph/work"; //家谱作品
    public static final String GRAPH_PLACE = HOST + "graph/place"; //谱籍地
    public static final String GRAPH_ANNOTATION = HOST + "graph/annotation"; //Work备注
    public static final String GRAPH_FAMILY = HOST + "graph/family"; //家族
    public static final String GRAPH_MIGRATION = HOST + "graph/migration"; //迁徙图
    public static final String GRAPH_TEMPLATE = HOST + "graph/profile"; //模板
    public static final String GRAPH_TITLE = HOST + "graph/title"; //title new add
    public static final String GRAPH_GEO = CBA + "graph/geography"; //省市县

    //Prefix
    public static final String PREFIX_PERSON = HOST + "Person/";
    public static final String PREFIX_PLACE = HOST + "Place/";
    public static final String PREFIX_WORK = HOST + "Work/";
    public static final String PREFIX_INSTANCE = HOST + "Instance/";
    public static final String PREFIX_FAMILY = HOST + "Family/";
    public static final String PREFIX_FAMILYNAME = HOST + "FamilyName/";
    public static final String PREFIX_ANNOTATION = HOST + "Annotation/";
    public static final String PREFIX_TEMPORAL = HOST + "Temporal/";
    public static final String PREFIX_CATEGORY = HOST + "Category/";
    public static final String PREFIX_TITLE = HOST + "Title/";
    public static final String PREFIX_INSTANCE_TEMPORAL = HOST + "Temporal/published/";

    public  Map<String, String> GRAPH_MAP = new LinkedHashMap<>();
  //  public static final Map<String, String> GRAPH_MAP = new LinkedHashMap<>();

    //Init
    public CopyOfConstant() {
    	GRAPH_MAP.put(GRAPH_PERSON, GRAPH_PERSON);
    	GRAPH_MAP.put(GRAPH_BASEINFO, GRAPH_BASEINFO);
    	GRAPH_MAP.put(GRAPH_INSTANCE, GRAPH_INSTANCE);
    	GRAPH_MAP.put(GRAPH_WORK, GRAPH_WORK);
    	GRAPH_MAP.put(GRAPH_PLACE, GRAPH_PLACE);
    	GRAPH_MAP.put(GRAPH_ANNOTATION, GRAPH_ANNOTATION);
    	GRAPH_MAP.put(GRAPH_FAMILY, GRAPH_FAMILY);
    	GRAPH_MAP.put(GRAPH_MIGRATION, GRAPH_MIGRATION);
    	GRAPH_MAP.put(GRAPH_TEMPLATE, GRAPH_TEMPLATE);
    	GRAPH_MAP.put(GRAPH_TITLE, GRAPH_TITLE);
       /* init_person_graph();
        init_work_graph();
        init_place_graph();
        init_annotation_graph();
        init_instance_graph();*/
    }

   /* private void init_person_graph() {
        GRAPH_MAP.put("http://gen.library.sh.cn/vocab/Person", GRAPH_PERSON);
    }

    private void init_work_graph() {
        GRAPH_MAP.put("http://gen.library.sh.cn/vocab/Work", GRAPH_WORK);
    }

    private void init_place_graph() {
        GRAPH_MAP.put("http://gen.library.sh.cn/vocab/Place", GRAPH_PLACE);
    }

    private void init_annotation_graph() {
        GRAPH_MAP.put("http://gen.library.sh.cn/vocab/Annotation", GRAPH_ANNOTATION);
    }

    private void init_instance_graph() {
        GRAPH_MAP.put("http://gen.library.sh.cn/vocab/Instance", GRAPH_INSTANCE);
    }*/
}
