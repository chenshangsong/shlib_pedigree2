package cn.sh.library.pedigree.baseTest;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;

import javax.annotation.Resource;

import org.junit.Test;

import cn.sh.library.pedigree.graph.DataParser;
import cn.sh.library.pedigree.ontology.view.VocabOnt;
import cn.sh.library.pedigree.sparql.TemplateSparql;
import cn.sh.library.pedigree.sparql.VocabSparql;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * Created by ly on 2014-11-4.
 */
public class VocabTesting extends BaseTesting {

    @Resource
    private VocabOnt vocabOnt;

//    @Resource
//    private GeoSparql geoSparql;
//
//    @Resource
//    private PersonSparql ps;
//
//    @Resource
//    private PlaceSparql pls;
//
//    @Resource
//    private GeoSparql gs;
//
//    @Resource
//    private  MigrationSparql ms;
//
//    @Resource
//    private FamilySparql fs;
//
//    @Resource
//    private BaseinfoSparql bs;
//
//    @Resource
//    private MigrationService migrationService;
//
//    @Resource
//    private WorkSparql ws;
//
//    @Resource
//    private CommonSparql cs;

    @Resource
    private TemplateSparql ts;
    @Resource
    private VocabSparql vs;


//    @Test
//    public void testShortClasses(){
//        List<String> classList = vocabOnt.getShortClasses();
//        for( String clazz : classList){
//            System.out.println("clazz = " + clazz);
//        }
//    }

    public void printChineseCharacterCount(String str) {
        int ccCount = 0;
        String regEx = "[\\u4E00-\\u9FA5]";
        java.util.regex.Pattern p = java.util.regex.Pattern.compile(regEx);
        java.util.regex.Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                ccCount = ccCount + 1;
            }
        }
        System.out.println("字符串“"+str+"”中共有 " + ccCount + "个汉字 ");
        System.out.println(m.group(0));
    }

    //读文件，返回字符串
    public String ReadFile(String path){
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            //System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            //一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                //显示行号
                laststr = laststr + tempString;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
        return laststr;
    }

    @Test
    public void test(){
        JSONArray json = new JSONArray();

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("id", "100001");
        jsonObject.put("graph", "person");

        JSONArray jsonArray = new JSONArray();
        JSONObject ps = new JSONObject();
        ps.put("uri", "foaf:name");
        ps.put("type", "literal");
        ps.put("repeatable", "true");

        JSONArray jsonArray1 = new JSONArray();
        JSONObject os1 = new JSONObject();
        os1.put("value", "陈涛");
        JSONObject os2 = new JSONObject();
        os2.put("value", "Chen Tao");
        os2.put("language", "en");
        jsonArray1.add(os1);
        jsonArray1.add(os2);

        ps.put("objects", jsonArray1);

        JSONObject ps1 = new JSONObject();
        ps1.put("uri", "foaf:gender");
        ps1.put("type", "resource");
        ps1.put("repeatable", "false");

        JSONObject o = new JSONObject();
        o.put("value", "http://localhost/gender/male");
        ps1.put("objects", o);

        jsonArray.add(ps);
        jsonArray.add(ps1);

        jsonObject.put("properties", jsonArray);

        json.add(jsonObject);

        DataParser parser = new DataParser();
        parser.launch(json);
//        System.out.println(vs.getSubjectProperties("http://gen.library.sh.cn/vocab/Place"));
//        System.out.println(vs.getSubjectProperties("shlgen:Place"));
//        System.out.println(vs.getAllClasses());
//        String sets = ReadFile("d:\\Agents.json");//获得json文件的内容
//
//        ts.addBFProfile(sets);
//        String inputFileName = "D:\\shlgen.ttl";
//
//        Model model = ModelFactory.createDefaultModel();
//
//        InputStream in = FileManager.get().open(inputFileName);
//        if (in == null) {
//            throw new IllegalArgumentException("File: " + inputFileName + " not found");
//        }
//
//        //model.read(in, "","RDF/XML");//根据文件格式选用参数即可解析不同类型
//        model.read(in, "","TTL");
//        model.write(System.out, "RDF/XML");

//        System.out.println(vocabOnt.delete("<http://gen.library.sh.cn/vocab/family>","<http://www.w3.org/2000/01/rdf-schema#comment>","\"测试\"","class"));
//        String firstLevel = ms.getFirstLevel();
//        System.out.println("firstLevel = " + firstLevel);
//        Map infos = new HashMap(RDFUtils.transform(ms.getInfos(firstLevel)));
//        infos.put("uri", firstLevel);
//        infos.put("name", infos.get("fns"));
//        infos.put("sn", infos.get("sn"));
//        infos.put("zi", infos.get("zi"));
//        infos.put("hao", infos.get("hao"));
//        infos.put("start", infos.get("start").toString());
//        infos.put("end", infos.get("end").toString());
//        infos.put("order", infos.get("order"));
//        infos.put("number", "始祖");
////        infos.put("size", 1983);
//        infos.put("children", getChildren(1, firstLevel));
//        String result = new JSONSerializer().serialize(infos);
//        System.out.println("result = " + result);
    }



    @SuppressWarnings("unused")
	@Test
    public void testVocabOnt() {
        InputStream inputStream = null;
        try {
            inputStream = new FileInputStream("d:\\ld.json");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

//        ts.addNewTemplate(inputStream);
//        TemplateGraph templateGraph = new TemplateGraph(inputStream);
//        templateGraph.parserTemplate();
//
//        ts.addNewTemplate(inputStream);

//        Object jsonObject = null;
//        try {
//            jsonObject = JsonUtils.fromInputStream(inputStream);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        System.out.println(ts.getLatestTemplateModel());
//
//        Map context = new HashMap();
//        JsonLdOptions options = new JsonLdOptions();
//        options.useNamespaces = true;
//
//        Object compact = null;
//        try {
//            compact = JsonLdProcessor.compact(jsonObject, context, options);
//            RDFDataset rdf = (RDFDataset) JsonLdProcessor.toRDF(jsonObject, options);
//            System.out.println(rdf.getNamespaces());
//            System.out.println(rdf.getQuads("@default"));
//        } catch (JsonLdError jsonLdError) {
//            jsonLdError.printStackTrace();
//        }
//
//        try {
//            System.out.println(JsonUtils.toPrettyString(compact));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }

//        String str =
//                "<http://gen.library.sh.cn/Temporal/21> rdfs:label 'timeline' . " +
//                "<http://gen.library.sh.cn/Temporal/18> rdfs:label 'timeline' . " +
//                "<http://gen.library.sh.cn/Temporal/28> rdfs:label 'timeline' . " +
//                "<http://gen.library.sh.cn/Temporal/2> rdfs:label 'timeline' . " +
//                "<http://gen.library.sh.cn/Temporal/5> rdfs:label 'timeline' . " +
//                "<http://gen.library.sh.cn/Temporal/4> rdfs:label 'timeline' . " +
//                "<http://gen.library.sh.cn/Temporal/10> rdfs:label 'timeline' . ";
//现代21 民国18 近代28 清2 明5 元4 汉10
//        System.out.println(ms.getFirstLevel());
//        System.out.println(ms.getAfterLevel("http://gen.library.sh.cn/Person/33134"));

//        String firstLevel = ms.getFirstLevel();
//        System.out.println(migrationService.pedigree());

//        System.out.println(cs.insertTriples(str));

//        String str = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
//                "<data>";
//
//        ArrayList results = ws.getWorkYears();
//
//        for (int i=0;i<results.size();i++) {
//            String year = RDFUtils.getValue(((Map) results.get(i)).get("begin").toString());
//
//            ArrayList works = ws.getWorksInYear(year, 3);
//
//            for (int j=0;j<works.size();j++) {
//                str += "<event start=\"" + year + "\" title=\"" + ((Map) works.get(j)).get("title").toString() + "\">\n" +
//                        "        " + ((Map) works.get(j)).get("note").toString() + ".\n" +
//                        "        </event>\n";
//            }
//        }
//
//        str += "</data>";
//
//        System.out.println(str);

//        printChineseCharacterCount("张呈栋");
//        List list = vocabOnt.getSuperClasses();
//        List list = vocabOnt.getClassProperties("bf:Work");
//        System.out.println(vocabOnt.getClassComment("shlgen:Person"));
//        System.out.println(list.size());
//
//        for( Object clazz : list){
//            System.out.println("clazz = " + clazz);
//        }

//        List rules = Rule.rulesFromURL("d:\\shlgen.rules");
//        Reasoner reasoner = new GenericRuleReasoner(rules);
//        Model data = FileManager.get().loadModel("d:\\person100.ttl");
//        InfModel inf = ModelFactory.createInfModel(reasoner, data);
//        System.out.println(inf.size());

//        PersonSearchBean bean = new PersonSearchBean();
//        bean.setFamilyName("陈");
//        bean.setTime("清");
//        bean.setFirstChar("L");
//        bean.setName("陆通");
//        bean.setUri("http://gen.library.sh.cn/Person/1");
//
//        WorkSearchBean wb = new WorkSearchBean();
//        PublicSearchBean pb = new PublicSearchBean();
//        pb.setTitle("陆氏宗谱");

//        System.out.println(ms.getMigrations());
//        System.out.println(ms.get("http://gen.library.sh.cn/Person/33118"));
//        System.out.println(ms.getFamilyTree("http://gen.library.sh.cn/Person/33118"));
//        System.out.println(ws.getWorkYears());
//        System.out.println(bs.getTemporal());
//        wb.setFamilyName("陈");
//        wb.setPoints("BOX(120 30, 121 28)");
//        wb.setPlace_uri("http://gen.library.sh.cn/Place/103");
//        wb.setPlace("江苏");

//        System.out.println(ws.getWorks(wb, 0, 10).getResultList());

//        System.out.println(ps.getPersons(bean, false));
//        System.out.println(ps.getPersons(bean, true));
//        System.out.println(geoSparql.getCitySubject("四川省","乐山市"));
//        System.out.println(ps.getAllPersons(null));
//        System.out.println(ps.getAllPersons("c"));
//        ps.export();
//        Model temp = ModelFactory.createDefaultModel();
//        bs.generatePinYin();
//        ps.generatePinYin();
//        System.out.println(bs.getAllFamilyNames());
//        System.out.println(bs.getPersonFamilyNames());
//        System.out.println(cs.getTriples("http://gen.library.sh.cn/Work/1"));
//        System.out.println((ps.getInfos4Person("http://gen.library.sh.cn/Person/1")));
//        System.out.println(cs.moveTriples(Constant.GRAPH_PERSON, Constant.GRAPH_WORK, "bf:Work"));
//        System.out.println(cs.copyTriples(Constant.GRAPH_PERSON, Constant.GRAPH_MIGRATION, "http://gen.library.sh.cn/Person/3311"));
//        System.out.println(cs.copyTriples(Constant.GRAPH_PERSON, Constant.GRAPH_MIGRATION, "http://gen.library.sh.cn/Person/3313"));
//        System.out.println(cs.copyStatements(Constant.GRAPH_GEO, Constant.GRAPH_MIGRATION, "http://www.w3.org/2003/01/geo/wgs84_pos#long","http://www.cba.ac.cn/point/341706","http://gen.library.sh.cn/Place/210"));
//        System.out.println(pls.getAllPlacesInOrigin());
//        System.out.println(pls.getPlaces("http://gen.library.sh.cn/Work/6560"));
//        System.out.println(bs.getTemporal());
//        System.out.println(ps.countPersons("http://gen.library.sh.cn/Temporal/2"));
//        System.out.println(ps.getFamRels4Work("http://gen.library.sh.cn/Work/18710"));
//        System.out.println(ws.getWorks4Person("http://gen.library.sh.cn/Person/85", true));
//        System.out.println(ws.getInstances4Work("http://gen.library.sh.cn/Work/55"));
//        System.out.println(ws.getAllWorks());
//        System.out.println(ps.getWorks4Person("http://gen.library.sh.cn/Person/13", false));
//        System.out.println(bs.getPersons("http://gen.library.sh.cn/Temporal/2"));
//        System.out.println(bs.getWorks("http://gen.library.sh.cn/Temporal/2"));
//        System.out.println(bs.getCHT4CHS("shlgen:familyNameValue", "瓜尔佳"));
//        System.out.println(bs.getFamilyName("夏侯"));
//        System.out.println(bs.getFamilyName("夏侯惇"));
//        System.out.println(gs.getCity4Point("113.62","37.8"));
//        System.out.println(ws.getWorksInLatLong("http://gen.library.sh.cn/Place/5"));
//        System.out.println(ws.getWorkInfos("http://gen.library.sh.cn/Work/8671"));
//        System.out.println(pls.getLabel("http://gen.library.sh.cn/Place/236"));
//        System.out.println(pls.getLabel("http://gen.library.sh.cn/Place/157"));
//        System.out.println(pls.getPlacesInArea("POLYGON((119.22609375 30.24501150238497,118.78664062499999 27.240163500962083,120.98390624999999 26.691823152793674,122.214375 30.548248829487648,119.22609375 30.24501150238497))"));
//        pls.linkPlace2Geo();
//        System.out.println(pls.getUri4StandPlace("四川省","乐山市"));
//        String str = "#赵 Zhao #钱 Qian #孙 Sun #李 Li #周 Zhou #吴 Wu #郑 Zheng #王 Wáng #冯 Feng #陈 Chen #褚 Chu #卫 Wei #蒋 Jiang #沈 Shen #韩 Han #杨 Yang #朱 Zhu #秦 Qin #尤 You #许 Xu #何 He #吕 Lü #施 Shi #张 Zhang (张) #孔 Kong #曹 Cao #严 Yan #华 Hua #金 Jin #魏 Wei #陶 Tao #姜 Jiang #戚 Qi #谢 Xie #邹 Zou #喻 Yu #柏 Bai #水 Shui #窦 Dou #章 Zhang (章) #云 Yun #苏 Sū #潘 Pan #葛 Ge #奚 Xi #范 Fan #彭 Peng #郎 Lang #鲁 Lu #韦 Wei #昌 Chang #马 Ma #苗 Miao #凤 Feng #花 Hua #方 Fang #俞 Yu #任 Ren #袁 Yuan (袁) #柳 Liu #邓 Deng #鲍 Bao #史 Shi #唐 Tang #费 Fei #廉 Lian #岑 Cen #薛 Xue #雷 Lei #贺 He #倪 Ni #汤 Tang #藤 Teng #殷 Yin #罗 Luo #毕 Bi #郝 Hao #邬 Wu #安 An #常 Chang #乐 Le #于 Yu #时 Shi #付 Fu #皮 Pi #卞 Bian #齐 Qi #康 Kang #伍 Wu #余 Yu #元 Yuan (元) #卜 Bu #顾 Gu #盈 Meng #平 Ping #黄 Huang #和 He #穆 Mu #肖 Xiao #尹 Yin #姚 Yao #邵 Shao #湛 Zhan #汪 Wāng #祁 Qi #毛 Mao #禹 Yu #狄 Di #米 Mi #贝 Bei #明 Ming #臧 Zang #计 Ji #伏 Fu #成 Cheng #戴 Dai #谈 Tan #宋 Sòng #茅 Mao #庞 Pang #熊 Xiong #纪 Ji #舒 Shu #屈 Qu #项 Xiang #祝 Zhu #董 Dong #梁 Liang #杜 Du #阮 Ruan #蓝 Lan #闵 Min #席 Xi #季 Ji #麻 Ma #强 Qiang #贾 Jia #路 Lu #娄 Lou #危 Wei #江 Jiang #童 Tong #颜 Yan #郭 Guo #梅 Mei #盛 Shen #林 Lin #刁 Diao #钟 Zhong #徐 Xu #邱 Qiu #骆 Luo #高 Gao #夏 Xia #蔡 Cai #田 Tian #樊 Fan #胡 Hu #凌 Ling #霍 Huo #虞 Yu #万 Wan #支 Zhi #柯 Ke #昝 Zan #管 Guan #卢 Lu #莫 Mo #经 Jing #房 Fang #裘 Qiu #缪 Miao #干 Gan #解 Xie #应 Ying #宗 Zhong #丁 Ding #宣 Xuan #贲 Ben #邓 Deng #郁 Yu #单 Shan #杭 Hang #洪 Hong #包 Bao #诸 Zhu #左 Zuo #石 Shi #崔 Cui #吉 Ji #钮 Niu #龚 Gong #程 Cheng #嵇 Ji #邢 Xing #滑 Hua #裴 Pei #陆 Lu #荣 Rong #翁 Weng #荀 Xun #羊 Yang #於 Yu #惠 Hui #甄 Zhen #曲 Qu #家 Jia #封 Feng #芮 Rui #羿 Yi #储 Chu #靳 Jin #汲 Ji #邴 Bing #糜 Mi #松 Sōng #井 Jing #段 Duan #富 Fu #巫 Wu #乌 Wu #焦 Jiao #巴 Ba #弓 Gong #牧 Mu #隗 Kui #山 Shan #谷 Gu #车 Che #侯 Hou #宓 Mi #蓬 Peng #全 Quan #郗 Xi #班 Ban #仰 Yang #秋 Qiu #仲 Zhong #伊 Yi #宫 Gong #宁 Ning #仇 Qiu #栾 Luan #暴 Bao #甘 Gan #钭 Dou #厉 Li #戎 Rong #祖 Zu #武 Wu #符 Fu #刘 Liu #景 Jing #詹 Zhan #束 Shu #龙 Long #叶 Ye #幸 Xing #司 Si #韶 Shao #郜 Gao #黎 Li #蓟 Ji #薄 Bao #印 Yin #宿 Sù #白 Bai #怀 Huai #蒲 Pu #台 Tai #从 Cong #鄂 E #索 Suo) #咸 Xian #籍 Ji #赖 Lai #卓 Zuo #蔺 Lin #屠 Tu #蒙 Meng #池 Chi #乔 Qiao #阴 Yin #鬱 Yu #胥 Xu #能 Neng #苍 Cang #双 Shuang #闻 Wen #莘 Xin #党 Dang #翟 Cui #谭 Tan #贡 Gong #劳 Lu #逄 Pang #姬 Ji #申 Shen #扶 Fu #堵 Du #冉 Ran #宰 Zai #郦 Li #雍 Yong #却 Que #璩 Qu #桑 Sang #桂 Gui #濮 Pu #牛 Niu #寿 Shou #通 Tong #边 Bian #扈 Hu #燕 Yan #冀 Ji #郏 Jia #浦 Pu #尚 Shang #农 Nong #温 Wēn #别 Bie #庄 Zhuang #晏 Yan #柴 Chai #瞿 Qu #阎 Yan #充 Chong #慕 Mu #连 Lian #茹 Ru #习 Xi #宦 Huan #艾 Ai #鱼 Yu #容 Rong #向 Xiang #古 Gu #易 Yi #慎 Shen #戈 Ge #廖 Liao #庚 Yu #终 Zhong #暨 Ji #居 Ju #衡 Heng #步 Bu #都 Du #耿 Geng #满 Man #弘 Gong #匡 Kuang #国 Guo #文 Wen #寇 Kou #广 Guang #禄 Lü #阙 Que #东 Dong #殴 Ou #殳 Shu #沃 Wo #利 Li #蔚 Wei #越 Yue #夔 Kui #隆 Long #师 Shi #巩 Gong #厍 She #聂 Nie #晁 Chao #勾 Gou #敖 Ao #融 Rong #冷 Leng #訾 Zi #辛 Xin #阚 Kan #那 Na #简 Gan #饶 Rao #空 Kong #曾 Zeng #毋 Wu #沙 Sha #乜 Nie #养 Yang #鞠 Ju #须 Xu #丰 Feng #巢 Chao #关 Guan #蒯 Kuai #相 Xiang #查 Zha #后 Hou #荆 Jing #红 Hong #游 You #竺 Zhu #权 Quan #逯 Lu #盖 Gai #益 Yi #桓 Huan #公 Gong #万俟 Wansi #司马 Sima #上官 Shangguan #欧阳 Ouyang #夏侯 Xiahou #诸葛 Zhuge #闻人 Wenren #东方 Dongfang #赫连 Helian #皇甫 Huangfu #尉迟 Yuchi #公羊 Gongyang #澹台 Tantai #公冶 Gongye #宗政 Zongzheng #濮阳 Puyang #淳于 Chunyu #单于 Chanyu #太叔 Taishu #申屠 Shentu #公孙 Gongsun #仲孙 Zhongsun #轩辕 Xuanyuan #令狐 Linghu #钟离 Zhongli #宇文 Yuwen #长孙 Zhangsun #慕容 Murong #鲜于 Xianyu #闾丘 Lüqiu #司徒 Situ #司空 Sikong #亓官 Qiguan #司寇 Sikou #仉督 Zhangdu #子车 Ziju #颛孙 Zhuansun #端木 Duanmu #巫马 Wuma #公西 Gongxi #漆雕 Qidiao #乐正 Yuezheng #壤驷 Rangsi #公良 Gongliang #拓拔 Tuoba #夹谷 Jiagu #宰父 Zaifu #谷粱 Guliang #晋楚 Jinchu #闫法 Yanfa #汝鄢 Ruyan #涂钦 Tuqin #段干 Duangan #百里 Baili #东郭 Dongguo #南门 Nanmen #呼延 Huyan #归海 Guihai #羊舌 Yangshe #微生 Weisheng #岳 Yue #帅 Shuai #缑 Gou #亢 Kang #况 Kuang #后 Hou #有 You #琴 Qin #梁丘 Liangqiu #左丘 Zuoqiu #东门 Dongmen #西门 Ximen #商 Shang #牟 Mou #佘 She #佴 Nai #伯 Bo #赏 Shang #南宫 Nangong #墨 Mo #哈 Ha #谯 Qiao #笪 Da #年 Nian #爱 Ai #阳 Yang #佟 Tong #第五 Diwu #言福 Yanfu";
//        String[] cells = str.split("#");
//        for (int i=1;i<cells.length;i++) {
//            String[] cell = cells[i].split(" ");
//            String uri = "<http://gen.library.sh.cn/FamilyName/" + i + ">";
//            String xing = cell[0].trim();
//            String pinyin = cell[1].trim();
//
//            System.out.println(uri + " a " + "shlgen:FamilyName . ");
//            System.out.println(uri + " shlgen:familyNameValue \"" + xing + "\"@chs .");
//            System.out.println(uri + " shlgen:familyNameValue \"" + pinyin + "\"@en .");
//        }
    }
    
}
