$(function(){
    var pager = new Pager($("#workForm"));
    var worksTpl = $("#worksTpl").html();
    var loadDataList = function(){$("#workForm").submit()};
    $("#workForm").submit(function(){
        showLoading();
        $.post($(this).attr("action"), $(this).serialize(), function(result){
            $("#pager").html(pager.paint(result.pager));
            var map = new Map();
            $.each(result.works, function(i){
                var item = result.works[i];
                var list = null;
                list = map.get(item.fn_chs);
                if(!list){
                    list = [];
                    map.put(item.fn_chs, list);
                }
                list.push(item);
            })
            var provs = map.arr;
            var html = "";
            $.each(provs, function(i){
                var prov = provs[i].key;
                html += "<h3>" + prov + "</h3><ul class='left_ul'>";
                $.each(provs[i].value, function(j){
                	var url = ctx+'/ancestraltempleManager/dataContentlist?id='+provs[i].value[j]['uri'];   
                    html += "<li><a href='" + url + "'>" + provs[i].value[j]['chs'] + "</a></li>";
                });
                html += "</ul>";
            });
            $("#originDiv").html(html);
            
            hideLoading();
        });
        return false;
    });
    $("#btn_query").click(function(){$("#workForm [name=pageth]").val(1);loadDataList();});
    
    var initQuery = function(){
        PARAM_MGR.loadParams();
        var hashQuery = false;
        var params = ["title","place", "familyName"];
        var uri = PARAM_MGR.getParam("uri");
        if(uri){
            $("#top span").hide();
            $("#workForm").hide();
            showWork(uri)
        } else {
            $.each(params, function (i) {
                var param = params[i];
                var value = PARAM_MGR.getParam(param);
                if (value) {
                    hashQuery = true;
                    $("#workForm input[name=" + param + "]").val(value);
                }
            });
			loadDataList();
        }
    };
    var showWork = function(uri){
    	window.location.href='${ctx}/ancestraltempleManager/dataContentlist?id='+uri;
    }
    $("#dataDiv").on("click", "a[tag=work]", function(){
        var uri = $(this).attr("href");
        showWork(uri)
        return false;
    })
    $("#btn_back").click(function(){
        $("#top span").text($("#top span").attr("text"));
        $(this).hide();
        $("#btn_mig").hide();
        $("#btn_tree").hide();
        $("#workView").hide();
        $("#workForm").fadeIn("slow");
    });
    initQuery();
    // tree
    $("#btn_tree").click(function(){
        $("#treeModal .modal-title").text("上川明经胡氏家族世系表");
        var root = $("<div class='tree'>");
        var data = {"number":"始祖","descs":"远行十七公于陕会朱温谋逆迁帝洛阳义祖三公乃得公以归考水义养为子遂从胡姓寻以易登后唐同光乙酉明经进士第故称为明经胡氏事迹详具列传娶江西德兴利丰詹氏享年九十有三生三子公葬婺源本里之锡子坞夫人葬婺源高仓浮舟塘坞口山向均载墓图","children":[{"start":"后唐天成근丑十二月十五","fns":"胡延政/胡以礼","uri":"http://gen.library.sh.cn/Person/16608","zi":"克修","number":"2世","descs":"事迹详具列传享年八十有一娶浮溪汪氏生五子合葬星源黄嵩塘山向详载墓图公为始迁绩溪祖","size":1983,"children":[{"start":"","fns":"胡百彦/胡嶽","uri":"http://gen.library.sh.cn/Person/33098","zi":"","number":"3世","descs":"登开宝癸酉进士第厯官翰林承旨事迹详具列传娶汪氏继娶戴氏赵氏叶氏生八子合葬通镇桥庚山甲向","size":1983,"children":[{"start":"","fns":"胡简/","uri":"http://gen.library.sh.cn/Person/33099","zi":"居敬","number":"4世","descs":"行名千三登进士第厯宜至侍御事迹详具列传娶闵氏继娶汪氏秦氏生五子合葬十都龙塘胡家坦莫字三百五十七号","size":1983,"children":[{"start":"","fns":"胡文安/","uri":"http://gen.library.sh.cn/Person/33100","zi":"","number":"5世","descs":"登进士第厯官谏议大夫事迹详具列传娶歙西郑氏生五子葬翚北中王村际头","size":1983,"children":[{"start":"","fns":"胡遇/胡通","uri":"http://gen.library.sh.cn/Person/33101","zi":"君锡","number":"6世","descs":"登庆厯壬午进士第累官至少保定国军节度使事迹详具列传娶罗氏闵氏汪氏郑氏生四子合葬胡里后花园山向详载墓图","size":1983,"children":[{"start":"","fns":"胡全信/胡端","uri":"http://gen.library.sh.cn/Person/33102","zi":"","number":"7世","descs":"五道州助教寻擢京西路提学事迹详具列传娶罗氏生一子","size":1983,"children":[{"start":"","fns":"胡忠/胡昭","uri":"http://gen.library.sh.cn/Person/33097","zi":"良臣","number":"8世","descs":"娶柯氏生三子合葬黄观坦山向详载墓图公始迁县西乡翚北","size":1983,"children":[{"start":"","fns":"胡昉/","uri":"http://gen.library.sh.cn/Person/33103","zi":"","number":"9世","descs":"娶沈氏生三子合葬本都李家塘撒网形山向详载墓图","size":1983,"children":[{"start":"","fns":"胡文谅/","uri":"http://gen.library.sh.cn/Person/33104","zi":"","number":"10世","descs":"娶汪氏生二子合葬壶芦岭铁索系龙形辛山乙向","size":1983,"children":[{"start":"","fns":"胡贵良/","uri":"http://gen.library.sh.cn/Person/33105","zi":"","number":"11世","descs":"娶汪氏生二子合葬李家塘撒网形山向详载墓图","size":1983,"children":[{"start":"","fns":"胡德真/","uri":"http://gen.library.sh.cn/Person/33106","zi":"","number":"12世","descs":"娶冯氏生一子合葬大坞外仙人大座形山向详载墓图公始迁杨林","size":1983,"children":[{"start":"","fns":"胡宗颜/","uri":"http://gen.library.sh.cn/Person/33107","zi":"","number":"13世","descs":"娶葛氏生二子合葬本都后里村蛇形山向详载墓图","size":1983,"children":[{"start":"","fns":"胡唐卿/","uri":"http://gen.library.sh.cn/Person/33108","zi":"","number":"14世","descs":"娶戴氏生三子合葬朱家冲燕窝形画梁案亥山巳向","size":1983,"children":[{"start":"","fns":"胡谧/","uri":"http://gen.library.sh.cn/Person/33109","zi":"","number":"15世","descs":"娶冯氏生二子合葬本都白㙮路黄蛇出草形子山午向","size":1983,"children":[{"start":"","fns":"胡公羡/","uri":"http://gen.library.sh.cn/Person/33110","zi":"","number":"16世","descs":"娶程氏生二子合葬下井山山向详载墓图","size":1983,"children":[{"start":"","fns":"胡节/","uri":"http://gen.library.sh.cn/Person/33111","zi":"","number":"17世","descs":"娶许氏生三子合葬大坞外仙人大座形山向详载墓图","size":1983,"children":[{"start":"","fns":"胡汝龙/","uri":"http://gen.library.sh.cn/Person/33112","zi":"","number":"18世","descs":"娶叶氏生五子合葬下井山山向详载墓图","size":1983,"children":[{"start":"","fns":"胡*昌/","uri":"http://gen.library.sh.cn/Person/33113","zi":"","number":"19世","descs":"娶舒氏生二子合葬上溪口美女献花形山向详载墓图","size":1983,"children":[{"start":"","fns":"胡七二/","uri":"http://gen.library.sh.cn/Person/16609","zi":"","number":"20世","descs":"娶汪氏生一子合葬本村后岸荷叶盖金龟形山向详载墓图","size":1983,"children":[{"start":"","fns":"胡千/","uri":"http://gen.library.sh.cn/Person/33114","zi":"","number":"21世","descs":"娶冯氏生二子合葬本村后岸荷叶盖金龟形山向详载墓图","size":1983,"children":[{"start":"","fns":"胡贵一/胡崇祖","uri":"http://gen.library.sh.cn/Person/33115","zi":"以恭","number":"22世","descs":"娶汪氏生一子合葬本村后岸荷叶盖金龟形山向详载墓图","size":1983,"children":[{"start":"","fns":"胡福孙/","uri":"http://gen.library.sh.cn/Person/33116","zi":"","number":"23世","descs":"娶程氏生四子合葬本村后岸荷叶盖金龟形山向详载墓图","size":1983,"children":[{"start":"元延祐甲寅正月初三辰时","fns":"胡庆佑/","uri":"http://gen.library.sh.cn/Person/33117","zi":"","number":"24世","descs":"娶冯氏生二子合葬狮子石山向详载墓图","size":1983,"children":[{"start":"至正癸卯正月初一戌时","fns":"胡巽/胡玉","uri":"http://gen.library.sh.cn/Person/33118","zi":"","number":"25世","descs":"娶汗氏生三子以长子玄寿为顺公后合葬竦岭脚永安桥头㢠龙顾祖形山向详载墓图","size":1983,"children":[{"start":"明洪武丙子正月十三寅时","fns":"胡祖寿/","uri":"http://gen.library.sh.cn/Person/33119","zi":"","number":"26世","descs":"娶程氏生二子以长子佛宗为玄寿公后合葬下井山山向详载墓图","size":1983,"children":[{"start":"永乐壬寅二月初十亥时","fns":"胡满宗/","uri":"http://gen.library.sh.cn/Person/33120","zi":"文敬","number":"27世","descs":"娶冯氏生宣德丁未七月二十六戌时殁弘治丁巳十二月十九丑时享年七十有一生四子合葬下井山山向详载墓图","size":1983,"children":[{"start":"景泰壬申","fns":"胡普义/胡华","uri":"http://gen.library.sh.cn/Person/33121","zi":"","number":"28世","descs":"娶汪氏生七子合葬狮子石山向详载墓图","size":1983,"children":[{"start":"成化乙未","fns":"胡道正/","uri":"http://gen.library.sh.cn/Person/33122","zi":"景志","number":"29世","descs":"娶王氏生三子合葬狮子石山向详载墓图","size":1983,"children":[{"start":"正德근巳","fns":"胡玄礼/胡仁","uri":"http://gen.library.sh.cn/Person/33123","zi":"克复","number":"30世","descs":"乡饮宾娶汪氏生四子公葬北庄象形氏葬堈*头","size":1983,"children":[{"start":"嘉靖乙巳","fns":"胡永留/","uri":"http://gen.library.sh.cn/Person/33124","zi":"","number":"31世","descs":"娶程氏生三子公葬北庄氏葬下井山","size":1983,"children":[{"start":"","fns":"胡元当/","uri":"http://gen.library.sh.cn/Person/33125","zi":"","number":"32世","descs":"","size":1983,"children":[{"start":"天啟辛酉正月初八","fns":"胡文法/","uri":"http://gen.library.sh.cn/Person/33126","zi":"尔遵","number":"33世","descs":"娶季氏生天啟乙丑殁康熙乙酉九月初四生四子合葬歙邑栈石狮形山向详载墓图","size":1983,"children":[{"start":"顺治癸巳九月初五","fns":"胡志柏/","uri":"http://gen.library.sh.cn/Person/33127","zi":"子殷","number":"34世","descs":"娶程氏生顺治癸巳五月十三殁康熙乙酉十月十四生二子合葬呈金塘山向详载墓图","size":1983,"children":[{"start":"康熙乙卯正月二十二","fns":"胡兆孔/","uri":"http://gen.library.sh.cn/Person/33128","zi":"绍圣","number":"35世","descs":"娶程氏生康熙戊午九月十六殁乾隆丁巳九月二十六生五子合葬洪家山向详载墓图","size":1983,"children":[{"start":"康熙壬辰三月二十九","fns":"胡应进/","uri":"http://gen.library.sh.cn/Person/33129","zi":"从光","number":"36世","descs":"乡饮宾娶曹氏生康熙丁酉七月十八生一子合葬照坑蛇形冈山向详载墓图","size":1983,"children":[{"start":"乾隆甲子三月二十一","fns":"胡天旭/胡旭","uri":"http://gen.library.sh.cn/Person/33130","zi":"日初","number":"37世","descs":"国学生事迹见善行娶程氏生乾隆甲子三月二十二生三子合葬余村岱","size":1983,"children":[{"start":"乾隆丁亥三月十八","fns":"胡德江/胡瑞杰","uri":"http://gen.library.sh.cn/Person/33131","zi":"宗海","number":"38世","descs":"国学生诰封通议大夫娶曹氏生一子继娶曹氏生乾隆丁未卒咸丰丙辰生二子公与继配曹氏合葬黄泥岭山向详载墓图元配曹氏葬洪家","size":1983,"children":[{"start":"乾隆壬子十二月初十","fns":"胡锡镛/","uri":"http://gen.library.sh.cn/Person/33132","zi":"序东","number":"39世","descs":"诰封奉政大夫晋封通议大夫娶曹氏节行见列女生乾隆癸丑六月十八卒同治壬戌三月初十生二子合葬余村岱","size":1983,"children":[{"number":"40世","descs":"邑庠优增生事迹见学林娶曹氏生嘉庆辛未继娶石氏生嘉庆辛巳二月初二殁咸丰辛酉八月初十生二子三娶汪氏生道光庚寅六月十九殁宣统근酉十一月十三曹氏葬刘家塘石氏葬蟹形","size":1983,"hao":"星五","start":"嘉庆甲戌八月二十四","name":"胡贞焘/胡奎照","fns":"胡贞焘/胡奎照","end":"同治乙丑正月初九","sn":"贞焘","uri":"http://gen.library.sh.cn/Person/33141","zi":"崇恩","order":"一"},{"start":"道光壬午三月初九辰时","fns":"胡贞锜/胡奎熙","uri":"http://gen.library.sh.cn/Person/33133","zi":"世恩","number":"40世","descs":"诰封奉政大夫晋封通议大夫娶程氏生嘉庆庚辰七月十六时卒光绪丙戌二月十一申时生五子侧室张氏生道光壬寅十一月初五","size":1983,"children":[{"start":"道光辛丑二月十九戌时","fns":"胡祥蛟/胡传","uri":"http://gen.library.sh.cn/Person/33134","zi":"守三","number":"41世","descs":"岁贡生诰授通议大夫钦加三品衔赏戴花翎台湾台东直隶洲知州统领镇海后军在任候补知府事迹分见仕宦学林善行","size":1983,"children":[{"number":"42世","descs":"从九品娶章氏生同治근巳十一月十三卯时生二子","size":1983,"hao":"","start":"同治辛未正月十四辰时","name":"胡洪骏/胡嗣稼","fns":"胡洪骏/胡嗣稼","end":"","sn":"洪骏","uri":"http://gen.library.sh.cn/Person/33136","zi":"耕云","order":"一"},{"number":"42世","descs":"国学生候选知县娶汪氏生光绪戊寅十二月十一申时生二子","size":1983,"hao":"","start":"光绪丁丑八月二十三戌时","name":"胡洪骓/胡国琦","fns":"胡洪骓/胡国琦","end":"","sn":"洪骓","uri":"http://gen.library.sh.cn/Person/33135","zi":"绍之","order":"二"},{"number":"42世","descs":"考派美国留学生名适字适之事迹见学了生光绪辛卯十一月十七未时娶江氏生光绪庚寅十一月初八辰时","size":1983,"hao":"","start":"光绪辛卯十一月十七未时","name":"胡洪骍/胡适","fns":"胡洪骍/胡适","end":"","sn":"洪骍","uri":"http://gen.library.sh.cn/Person/16610","zi":"适之","order":"三"}],"hao":"铁花、钝夫","name":"胡祥蛟/胡传","end":"光绪乙未七月初三子时","sn":"祥蛟","order":"一"},{"number":"41世","descs":"国学生娶曹氏贤行见列女生道光戊申十一月十一生一子","size":1983,"hao":"汉生","start":"道光丙午二月初二","name":"胡祥糓/胡玮","fns":"胡祥糓/胡玮","end":"光绪근亥","sn":"祥糓","uri":"http://gen.library.sh.cn/Person/33140","zi":"守玉","order":"二"},{"number":"41世","descs":"娶姚氏生道光庚午三月十五丑時生二子","size":1983,"hao":"","start":"道光戊申正月十八辰時","name":"胡祥纪/","fns":"胡祥纪/","end":"光緒戊子二月十八午時","sn":"祥纪","uri":"http://gen.library.sh.cn/Person/33139","zi":"守璸","order":"三"},{"number":"41世","descs":"廪贡生阜阳县训导事迹分见仕宦学林娶朱氏生咸丰丁巳八月十六卒光绪辛卯十二月初一生二子继娶林氏二子以次子洪駩为祥糓后","size":1983,"hao":"介如","start":"咸丰甲寅正月十五","name":"胡祥绂/胡玠","fns":"胡祥绂/胡玠","end":"光绪甲辰十月十七","sn":"祥绂","uri":"http://gen.library.sh.cn/Person/33138","zi":"守璪","order":"四"},{"number":"41世","descs":"翰林院待诏娶汪氏生咸丰丁巳七月十六酉时","size":1983,"hao":"吉庭","start":"咸丰丙辰元旦午时","name":"胡祥祐/胡守璐","fns":"胡祥祐/胡守璐","end":"","sn":"祥祐","uri":"http://gen.library.sh.cn/Person/33137","zi":"","order":"五"}],"hao":"律均","name":"胡贞锜/胡奎熙","end":"","sn":"贞锜","order":"二"}],"hao":"","name":"胡锡镛/","end":"道光壬午三月二十三","sn":"锡镛","order":"一"}],"hao":"","name":"胡德江/胡瑞杰","end":"道光甲辰九月十八","sn":"德江","order":"二"}],"hao":"","name":"胡天旭/胡旭","end":"","sn":"天旭","order":"一"}],"hao":"","name":"胡应进/","end":"","sn":"应进","order":"四"}],"hao":"","name":"胡兆孔/","end":"乾隆丁巳四月十八","sn":"兆孔","order":"一"}],"hao":"","name":"胡志柏/","end":"雍正丁未九月二十八","sn":"志柏","order":"一"}],"hao":"","name":"胡文法/","end":"康熙戊午六月二十","sn":"文法","order":"一"}],"hao":"","name":"胡元当/","end":"","sn":"元当","order":"三"}],"hao":"","name":"胡永留/","end":"万厯甲申","sn":"永留","order":"四"}],"hao":"","name":"胡玄礼/胡仁","end":"嘉靖乙丑","sn":"玄礼","order":"三"}],"hao":"","name":"胡道正/","end":"","sn":"道正","order":"一"}],"hao":"","name":"胡普义/胡华","end":"","sn":"普义","order":"二"}],"hao":"","name":"胡满宗/","end":"成化乙巳九月十二辰时","sn":"满宗","order":"一"}],"hao":"","name":"胡祖寿/","end":"","sn":"祖寿","order":"一"}],"hao":"","name":"胡巽/胡玉","end":"","sn":"巽","order":"一"}],"hao":"","name":"胡庆佑/","end":"","sn":"庆佑","order":"四"}],"hao":"","name":"胡福孙/","end":"","sn":"福孙","order":"一"}],"hao":"","name":"胡贵一/胡崇祖","end":"","sn":"贵一","order":"一"}],"hao":"","name":"胡千/","end":"","sn":"千","order":"一"}],"hao":"","name":"胡七二/","end":"","sn":"七二","order":"二"}],"hao":"","name":"胡*昌/","end":"","sn":"*昌","order":"四"}],"hao":"","name":"胡汝龙/","end":"","sn":"汝龙","order":"二"}],"hao":"","name":"胡节/","end":"","sn":"节","order":"一"}],"hao":"","name":"胡公羡/","end":"","sn":"公羡","order":"一"}],"hao":"","name":"胡谧/","end":"","sn":"谧","order":"一"}],"hao":"","name":"胡唐卿/","end":"","sn":"唐卿","order":"一"}],"hao":"","name":"胡宗颜/","end":"","sn":"宗颜","order":"一"}],"hao":"","name":"胡德真/","end":"","sn":"德真","order":"二"}],"hao":"","name":"胡贵良/","end":"","sn":"贵良","order":"一"}],"hao":"","name":"胡文谅/","end":"","sn":"文谅","order":"一"}],"hao":"","name":"胡昉/","end":"","sn":"昉","order":"三"}],"hao":"桂崖","name":"胡忠/胡昭","end":"","sn":"忠","order":"一"}],"hao":"","name":"胡全信/胡端","end":"","sn":"全信","order":"四"}],"hao":"道亨","name":"胡遇/胡通","end":"","sn":"遇","order":"一"}],"hao":"","name":"胡文安/","end":"","sn":"文安","order":"一"}],"hao":"","name":"胡简/","end":"","sn":"简","order":"一"}],"hao":"","name":"胡百彦/胡嶽","end":"","sn":"百彦","order":"一"}],"hao":"节庵","name":"胡延政/胡以礼","end":"宋大中祥符근酉","sn":"延政","order":"一"}],"hao":"","start":"天祐甲子三月一日","name":"胡昌翼/胡宏","fns":"胡昌翼/胡宏","end":"未咸平근亥十月初三","sn":"昌翼","uri":"http://gen.library.sh.cn/Person/16607","zi":"","order":""};
        var nodeHtml = function(data){
            var d = "";
            var sn = data.sn;
            var zi = data.zi;
            var hao = data.hao;
            var order = data.order;
            var start = data.start;
            var end = data.end;
            var descs = data.descs;
            var name = data.name;

            if ($.endWith(name, "/")) {
                name = name.substring(0, name.length - 1);
            }

            if (start) {
                d += start;

                if (end) {d += "----" + end + "<br>";} else {d += "<br>";}
            }

            if (sn) {d += "【谱名】" + sn + "<br>";}
            if (zi) {d += "【字】" + zi + "<br>";}
            if (hao) {d += "【号】" + hao + "<br>";}
            if (order) {d += "【排行】" + order + "<br>";}
            if (descs) {d += "【简介】" + descs + "<br>";}
//            return '<a href="javascript:void(0);" data-container="body" data-toggle="popover" data-trigger="focus" data-placement="right" data-title="' + data.name + '" data-content="' +(data.start?("【" + data.start + "】\r\n"):"") + (data.descs||'无摘要') + '">' + data.name + '</a>'
            return '<a href="javascript:void(0);" data-container="body" data-html="true" data-toggle="popover" data-trigger="focus" data-placement="right" data-title="' + name + " [" + data.number + "]" + '" ' +
                'data-content="' + d + '">' + name + '</a>'
        }
        var appendChildren = function(data, dom){
            if(data && data.children){
                var ul = $("<ul>");
                dom.append(ul);
                for(var i=0;i<data.children.length;i++){
                    var li = $("<li>");
                    ul.append(li);
                    li.append(nodeHtml(data.children[i]));
                    appendChildren(data.children[i], li);
                }
            }
        }
        var ul = $("<ul>");
        var li = $("<li>");
        root.append(ul);
        ul.append(li)
        li.append(nodeHtml(data));
        appendChildren(data, li);
        $("#treeModal").modal("show").find(".modal-body #tree_container").html("").append(root);
        //$("#tree_container .tree").css("width", $("#tree_container .tree>ul>li").width())
        $('[data-toggle="popover"]').popover()
    });
    // track
    $("#btn_mig").click(function(){showTrack()});
    var showTrack = function(){
        var geocoords;
        var lines;
        var locations;
        $("#migModal .modal-title").text("上川明经胡氏家族迁徙图")
        $("#migModal .modal-body").html("")
        require.config({
            paths: {
                echarts: '/res/plugin/echarts/2.2.0'
            }
        });
        // Step:4 require echarts and use it in the callback.
        // Step:4 动态加载echarts然后在回调函数中开始使用，注意保持按需加载结构定义图表路径
        var draw = function(){
            //console.info("geocoords", geocoords)
            //console.info("lines", lines)
            //console.info("locations", locations)
            require(
                [
                    'echarts',
                    'echarts/chart/bar',
                    'echarts/chart/line',
                    'echarts/chart/map'
                ],
                function (ec) {
                    $("#migModal").modal("show").on("shown.bs.modal", function(){
                        $("#migModal .modal-body").html('<div id="mainMap" style="height:500px;border:1px solid #ccc;padding:10px;"></div>');
                        var myChart=ec.init(document.getElementById('mainMap'));myChart.setOption({backgroundColor:"#1b1b1b",color:["gold","aqua","lime"],title:{text:"上川明经胡氏家族迁徙图",x:"center",textStyle:{color:"#fff"}},tooltip:{trigger:"item",formatter:"{b}"},legend:{orient:"vertical",x:"left",data:[],selectedMode:"single",selected:{},textStyle:{color:"#fff"}},toolbox:{show:true,orient:"vertical",x:"right",y:"center",feature:{restore:{show:true},saveAsImage:{show:true}}},dataRange:{min:0,max:100,calculable:true,color:["#ff3333","orange","yellow","lime","aqua"],textStyle:{color:"#fff"}},series:[{name:"全国",type:"map",roam:true,hoverable:false,mapType:"china",itemStyle:{normal:{borderColor:"rgba(100,149,237,1)",borderWidth:0.2,areaStyle:{color:"#1b1b1b"}}},data:[],markLine:{smooth:true,symbol:["none","circle"],symbolSize:1,itemStyle:{normal:{color:"#fff",borderWidth:0.5,borderColor:"rgba(30,144,255,0.5)"}},data:[[{name:"北京"},{name:"包头"}],[{name:"北京"},{name:"北海"}],[{name:"北京"},{name:"广州"}],[{name:"北京"},{name:"郑州"}],[{name:"北京"},{name:"长春"}],[{name:"北京"},{name:"长治"}],[{name:"北京"},{name:"重庆"}],[{name:"北京"},{name:"长沙"}],[{name:"北京"},{name:"成都"}],[{name:"北京"},{name:"常州"}],[{name:"北京"},{name:"丹东"}],[{name:"北京"},{name:"大连"}],[{name:"北京"},{name:"东营"}],[{name:"北京"},{name:"延安"}],[{name:"北京"},{name:"福州"}],[{name:"北京"},{name:"海口"}],[{name:"北京"},{name:"呼和浩特"}],[{name:"北京"},{name:"合肥"}],[{name:"北京"},{name:"杭州"}],[{name:"北京"},{name:"哈尔滨"}],[{name:"北京"},{name:"舟山"}],[{name:"北京"},{name:"银川"}],[{name:"北京"},{name:"衢州"}],[{name:"北京"},{name:"南昌"}],[{name:"北京"},{name:"昆明"}],[{name:"北京"},{name:"贵阳"}],[{name:"北京"},{name:"兰州"}],[{name:"北京"},{name:"拉萨"}],[{name:"北京"},{name:"连云港"}],[{name:"北京"},{name:"临沂"}],[{name:"北京"},{name:"柳州"}],[{name:"北京"},{name:"宁波"}],[{name:"北京"},{name:"南京"}],[{name:"北京"},{name:"南宁"}],[{name:"北京"},{name:"南通"}],[{name:"北京"},{name:"上海"}],[{name:"北京"},{name:"沈阳"}],[{name:"北京"},{name:"西安"}],[{name:"北京"},{name:"汕头"}],[{name:"北京"},{name:"深圳"}],[{name:"北京"},{name:"青岛"}],[{name:"北京"},{name:"济南"}],[{name:"北京"},{name:"太原"}],[{name:"北京"},{name:"乌鲁木齐"}],[{name:"北京"},{name:"潍坊"}],[{name:"北京"},{name:"威海"}],[{name:"北京"},{name:"温州"}],[{name:"北京"},{name:"武汉"}],[{name:"北京"},{name:"无锡"}],[{name:"北京"},{name:"厦门"}],[{name:"北京"},{name:"西宁"}],[{name:"北京"},{name:"徐州"}],[{name:"北京"},{name:"烟台"}],[{name:"北京"},{name:"盐城"}],[{name:"北京"},{name:"珠海"}],[{name:"上海"},{name:"包头"}],[{name:"上海"},{name:"北海"}],[{name:"上海"},{name:"广州"}],[{name:"上海"},{name:"郑州"}],[{name:"上海"},{name:"长春"}],[{name:"上海"},{name:"重庆"}],[{name:"上海"},{name:"长沙"}],[{name:"上海"},{name:"成都"}],[{name:"上海"},{name:"丹东"}],[{name:"上海"},{name:"大连"}],[{name:"上海"},{name:"福州"}],[{name:"上海"},{name:"海口"}],[{name:"上海"},{name:"呼和浩特"}],[{name:"上海"},{name:"合肥"}],[{name:"上海"},{name:"哈尔滨"}],[{name:"上海"},{name:"舟山"}],[{name:"上海"},{name:"银川"}],[{name:"上海"},{name:"南昌"}],[{name:"上海"},{name:"昆明"}],[{name:"上海"},{name:"贵阳"}],[{name:"上海"},{name:"兰州"}],[{name:"上海"},{name:"拉萨"}],[{name:"上海"},{name:"连云港"}],[{name:"上海"},{name:"临沂"}],[{name:"上海"},{name:"柳州"}],[{name:"上海"},{name:"宁波"}],[{name:"上海"},{name:"南宁"}],[{name:"上海"},{name:"北京"}],[{name:"上海"},{name:"沈阳"}],[{name:"上海"},{name:"秦皇岛"}],[{name:"上海"},{name:"西安"}],[{name:"上海"},{name:"石家庄"}],[{name:"上海"},{name:"汕头"}],[{name:"上海"},{name:"深圳"}],[{name:"上海"},{name:"青岛"}],[{name:"上海"},{name:"济南"}],[{name:"上海"},{name:"天津"}],[{name:"上海"},{name:"太原"}],[{name:"上海"},{name:"乌鲁木齐"}],[{name:"上海"},{name:"潍坊"}],[{name:"上海"},{name:"威海"}],[{name:"上海"},{name:"温州"}],[{name:"上海"},{name:"武汉"}],[{name:"上海"},{name:"厦门"}],[{name:"上海"},{name:"西宁"}],[{name:"上海"},{name:"徐州"}],[{name:"上海"},{name:"烟台"}],[{name:"上海"},{name:"珠海"}],[{name:"广州"},{name:"北海"}],[{name:"广州"},{name:"郑州"}],[{name:"广州"},{name:"长春"}],[{name:"广州"},{name:"重庆"}],[{name:"广州"},{name:"长沙"}],[{name:"广州"},{name:"成都"}],[{name:"广州"},{name:"常州"}],[{name:"广州"},{name:"大连"}],[{name:"广州"},{name:"福州"}],[{name:"广州"},{name:"海口"}],[{name:"广州"},{name:"呼和浩特"}],[{name:"广州"},{name:"合肥"}],[{name:"广州"},{name:"杭州"}],[{name:"广州"},{name:"哈尔滨"}],[{name:"广州"},{name:"舟山"}],[{name:"广州"},{name:"银川"}],[{name:"广州"},{name:"南昌"}],[{name:"广州"},{name:"昆明"}],[{name:"广州"},{name:"贵阳"}],[{name:"广州"},{name:"兰州"}],[{name:"广州"},{name:"拉萨"}],[{name:"广州"},{name:"连云港"}],[{name:"广州"},{name:"临沂"}],[{name:"广州"},{name:"柳州"}],[{name:"广州"},{name:"宁波"}],[{name:"广州"},{name:"南京"}],[{name:"广州"},{name:"南宁"}],[{name:"广州"},{name:"南通"}],[{name:"广州"},{name:"北京"}],[{name:"广州"},{name:"上海"}],[{name:"广州"},{name:"沈阳"}],[{name:"广州"},{name:"西安"}],[{name:"广州"},{name:"石家庄"}],[{name:"广州"},{name:"汕头"}],[{name:"广州"},{name:"青岛"}],[{name:"广州"},{name:"济南"}],[{name:"广州"},{name:"天津"}],[{name:"广州"},{name:"太原"}],[{name:"广州"},{name:"乌鲁木齐"}],[{name:"广州"},{name:"温州"}],[{name:"广州"},{name:"武汉"}],[{name:"广州"},{name:"无锡"}],[{name:"广州"},{name:"厦门"}],[{name:"广州"},{name:"西宁"}],[{name:"广州"},{name:"徐州"}],[{name:"广州"},{name:"烟台"}],[{name:"广州"},{name:"盐城"}]]},geoCoord:{"上海":[121.4648,31.2891],"东莞":[113.8953,22.901],"东营":[118.7073,37.5513],"中山":[113.4229,22.478],"临汾":[111.4783,36.1615],"临沂":[118.3118,35.2936],"丹东":[124.541,40.4242],"丽水":[119.5642,28.1854],"乌鲁木齐":[87.9236,43.5883],"佛山":[112.8955,23.1097],"保定":[115.0488,39.0948],"兰州":[103.5901,36.3043],"包头":[110.3467,41.4899],"北京":[116.4551,40.2539],"北海":[109.314,21.6211],"南京":[118.8062,31.9208],"南宁":[108.479,23.1152],"南昌":[116.0046,28.6633],"南通":[121.1023,32.1625],"厦门":[118.1689,24.6478],"台州":[121.1353,28.6688],"合肥":[117.29,32.0581],"呼和浩特":[111.4124,40.4901],"咸阳":[108.4131,34.8706],"哈尔滨":[127.9688,45.368],"唐山":[118.4766,39.6826],"嘉兴":[120.9155,30.6354],"大同":[113.7854,39.8035],"大连":[122.2229,39.4409],"天津":[117.4219,39.4189],"太原":[112.3352,37.9413],"威海":[121.9482,37.1393],"宁波":[121.5967,29.6466],"宝鸡":[107.1826,34.3433],"宿迁":[118.5535,33.7775],"常州":[119.4543,31.5582],"广州":[113.5107,23.2196],"廊坊":[116.521,39.0509],"延安":[109.1052,36.4252],"张家口":[115.1477,40.8527],"徐州":[117.5208,34.3268],"德州":[116.6858,37.2107],"惠州":[114.6204,23.1647],"成都":[103.9526,30.7617],"扬州":[119.4653,32.8162],"承德":[117.5757,41.4075],"拉萨":[91.1865,30.1465],"无锡":[120.3442,31.5527],"日照":[119.2786,35.5023],"昆明":[102.9199,25.4663],"杭州":[119.5313,29.8773],"枣庄":[117.323,34.8926],"柳州":[109.3799,24.9774],"株洲":[113.5327,27.0319],"武汉":[114.3896,30.6628],"汕头":[117.1692,23.3405],"江门":[112.6318,22.1484],"沈阳":[123.1238,42.1216],"沧州":[116.8286,38.2104],"河源":[114.917,23.9722],"泉州":[118.3228,25.1147],"泰安":[117.0264,36.0516],"泰州":[120.0586,32.5525],"济南":[117.1582,36.8701],"济宁":[116.8286,35.3375],"海口":[110.3893,19.8516],"淄博":[118.0371,36.6064],"淮安":[118.927,33.4039],"深圳":[114.5435,22.5439],"清远":[112.9175,24.3292],"温州":[120.498,27.8119],"渭南":[109.7864,35.0299],"湖州":[119.8608,30.7782],"湘潭":[112.5439,27.7075],"滨州":[117.8174,37.4963],"潍坊":[119.0918,36.524],"烟台":[120.7397,37.5128],"玉溪":[101.9312,23.8898],"珠海":[113.7305,22.1155],"盐城":[120.2234,33.5577],"盘锦":[121.9482,41.0449],"石家庄":[114.4995,38.1006],"福州":[119.4543,25.9222],"秦皇岛":[119.2126,40.0232],"绍兴":[120.564,29.7565],"聊城":[115.9167,36.4032],"肇庆":[112.1265,23.5822],"舟山":[122.2559,30.2234],"苏州":[120.6519,31.3989],"莱芜":[117.6526,36.2714],"菏泽":[115.6201,35.2057],"营口":[122.4316,40.4297],"葫芦岛":[120.1575,40.578],"衡水":[115.8838,37.7161],"衢州":[118.6853,28.8666],"西宁":[101.4038,36.8207],"西安":[109.1162,34.2004],"贵阳":[106.6992,26.7682],"连云港":[119.1248,34.552],"邢台":[114.8071,37.2821],"邯郸":[114.4775,36.535],"郑州":[113.4668,34.6234],"鄂尔多斯":[108.9734,39.2487],"重庆":[107.7539,30.1904],"金华":[120.0037,29.1028],"铜川":[109.0393,35.1947],"银川":[106.3586,38.1775],"镇江":[119.4763,31.9702],"长春":[125.8154,44.2584],"长沙":[113.0823,28.2568],"长治":[112.8625,36.4746],"阳泉":[113.4778,38.0951],"青岛":[120.4651,36.3373],"韶关":[113.7964,24.7028],"洛阳":[112.45,34.62],"考川":[117.757787,29.27642],"绩溪":[118.6,30.07],"龙井":[118.45425,30.140055],"西村":[118.453271,30.144974],"西递":[117.999032,29.907604],"仁里":[118.383059,29.976173],"上庄":[118.444977,30.128287]}},{name:"胡氏迁徙图",type:"map",mapType:"china",data:[],markLine:{smooth:true,effect:{show:true,scaleSize:2,period:100,color:"red",shadowBlur:10},itemStyle:{normal:{borderWidth:1,lineStyle:{type:"solid",shadowBlur:10}}},data:eval(lines)},markPoint:{symbol:"emptyCircle",symbolSize:function(v){return 10+v/10
                        },effect:{show:true,shadowBlur:0},itemStyle:{normal:{label:{show:false}},emphasis:{label:{position:"top"}}},data:eval(locations)}}]});
                    })
                }
            );
        }
        var count = 0;
        $.get(ctx + "/service/migration/geocoords", function(data){
            geocoords = data;
            if(++count >= 3) draw();
        });

        $.get(ctx + "/service/migration/lines", function(data){
            lines = data;
            if(++count >= 3) draw();
        });

        $.get(ctx + "/service/migration/locations", function(data){
            locations = data;
            if(++count >= 3) draw();
        });
    }

})