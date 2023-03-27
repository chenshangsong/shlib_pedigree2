package cn.sh.library.pedigree.services.impl;

import java.util.ArrayList;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import cn.sh.library.pedigree.services.MigrationService;
import cn.sh.library.pedigree.sparql.BaseinfoSparql;
import cn.sh.library.pedigree.sparql.MigrationSparql;
import cn.sh.library.pedigree.utils.RDFUtils;

/**
 * Created by yesonme on 15-2-8.
 */
@Service
public class MigrationServiceImpl extends BaseServiceImpl implements MigrationService {
    @Resource
    private MigrationSparql migrationSparql;

    @Resource
    private BaseinfoSparql baseinfoSparql;

    @Override
    public String pedigree() {
        String output = "{";

//        String first_level = migrationSparql.getFirstLevel();
//
//        Map info_map = migrationSparql.getInfos(first_level);
//        output += "\"name\": \"" + info_map.get("fns") + "\"";
//
//        output += getData("", first_level);
//
//        output += "}";
        return output;
    }

    private String getData(String output, String uri) {
        ArrayList results = migrationSparql.getAfterLevel(uri);

        if (results.size() > 0) {
            output += ",\n" +
                    "\"children\": [";

            for (int i=0;i<results.size();i++) {
                String u = ((Map) results.get(i)).get("uri").toString();
                Map map = migrationSparql.getInfos(u);

                output += "{" +
                        "\"uri\": \"" + u + "\"," +
                        "\"name\": \"" + map.get("fns").toString() + "\"";

                getData(output, u);
            }

            for (int i=results.size();i>0;i--) {
                output += "}]";
            }
        } else {
            output += "},";
        }

        return output;
    }

//    @Override
//    public String familyTimeline() {
//        String output = "<?xml version=\"1.0\" encoding=\"UTF8\" ?>\n";
//        output += "<data>\n";
//
////        ArrayList results = migrationSparql.getPersonInfos();
////
////        //"fns", "sn", "zi", "hao", "order", "start", "end", "descs"
////        for (int i=0;i<results.size();i++) {
////            Object fns = ((Map) results.get(i)).get("fns");
////            Object sn = ((Map) results.get(i)).get("sn");
////            Object zis = ((Map) results.get(i)).get("zis");
////            Object hao = ((Map) results.get(i)).get("hao");
////            Object order = ((Map) results.get(i)).get("order");
////            Object start = ((Map) results.get(i)).get("start");
////            Object end = ((Map) results.get(i)).get("end");
////            Object descs = ((Map) results.get(i)).get("descs");
////
////            output += "<event start='" + RDFUtils.getValue(start.toString()) + "' ";
////
////            if (end != null) {
////                output += "end='" + RDFUtils.getValue(end.toString()) + "' ";
////            }
////
////            output += "isDuration=\"true\" title=\"" + fns.toString() + "\">\n";
////
////            if (sn != null) {
////                output += "[名]：" + sn.toString() + "; ";
////            }
////
////            if (zis != null) {
////                output += "[字]：" + zis.toString() + "; ";
////            }
////
////            if (hao != null) {
////                output += "[号]：" + hao.toString() + "; ";
////            }
////
////            if (descs != null) {
////                output += "[描述]：" + descs.toString() + "; ";
////            }
////
////            output += "\n</event>\n";
////        }
////
////        output += "</data>";
//
//        return output;
//    }

    @Override
    public String migrationLocations() {
        String str = "[\n";

        ArrayList results = migrationSparql.getMigrationLocations();
        int size = results.size();

        for (int i=0;i<size;i++) {
            String label = ((Map) results.get(i)).get("label").toString();

            int value = 100 - (int)Math.floor(i*100/size);
            str += "{name:'" + label + "',value:" + value + "}";

            if (i < results.size() - 1) {
                str += ",\n";
            }
        }

        str += "\n]";

        return str;
    }

    @Override
    public String migrationGeoCoords() {
        String str = "{\n";

        ArrayList results = migrationSparql.getMigrationLocations();

        for (int i=0;i<results.size();i++) {
            String label = ((Map) results.get(i)).get("label").toString();
            String _long = RDFUtils.getValue(((Map) results.get(i)).get("long").toString());
            String lat = RDFUtils.getValue(((Map) results.get(i)).get("lat").toString());

            str += "'" + label + "': [" + _long + "," + lat + "]";

            if (i < results.size() - 1) {
                str += ",\n";
            }
        }

        str += "\n}";

        return str;
    }

    @Override
    public String migrationLines() {
        String str = "[\n";

        ArrayList results = migrationSparql.getMigrationLines();

        for (int i=0;i<results.size();i++) {
            String start = ((Map) results.get(i)).get("start").toString();
            String dest = ((Map) results.get(i)).get("dest").toString();

            str += "[{name:'" + start + "'},{name:'" + dest + "'}]";

            if (i < results.size() - 1) {
                str += ",\n";
            }
        }

        str += "\n]";

        return str;
    }
}
