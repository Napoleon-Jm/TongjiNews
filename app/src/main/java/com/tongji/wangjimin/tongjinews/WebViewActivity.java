package com.tongji.wangjimin.tongjinews;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_web_view);
        Intent intent = getIntent();
        WebView webView = new WebView(this);
        setContentView(webView);
        webView.loadUrl(intent.getStringExtra("url"));
//        String str = "<!DOCTYPE html>\n" +
//                "<html>\n" +
//                "<head>\n" +
//                "    <title></title>\n" +
//                "</head>\n" +
//                "<body>\n" +
//                "<div class=\"news_content\">\n" +
//                "    <p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 为加强贯彻党的教育方针，面向国家重大教育战略需求，调研世界一流高校在学科交叉融合和协调创新，产业发展、社会需求与科技前沿紧密衔接的体制机制建设经验，探索符合同济大学特色的人才培育、科学研究、社会服务、文化传承创新和国际交流合作综合实力提升的新途径，积极推动学校“双一流”建设，2017年2月11日至17日，我校校长钟志华率团出访美国和加拿大。代表团先后访问了麻省理工学院、哈佛大学、斯坦福大学、加州大学伯克利分校、美国劳伦斯伯克利国家实验室和加拿大英属哥伦比亚大学，与相关负责人进行了广泛深入的交流，签署多份合作协议。此外，钟志华还参观了麻省理工学院（MIT）等离子科学与聚变中心实验室、媒体实验室、大众创新实验室、欧特克公司等。我校设计创意学院、建筑与城市规划学院、土木工程学院、磁浮交通工程技术研究中心和外办的相关人员陪同出访。 <br />\n" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 11日傍晚，钟志华一行刚刚抵达大雪纷飞的波士顿，就与校友共度元宵佳节。钟校长向大家展望了母校新一轮国际化的蓝图，鼓励校友们利用学校坚实的科研能力，或回国，或在海外与顶尖机构合作，嫁接国内市场对创新的广阔应用前景，在大创新时代中做坚定的引领者。</p>\n" +
//                "<p align=\"center\"><img title=\"image001\" border=\"0\" alt=\"image001\" src=\"http://photo.tongji.edu.cn/themes/11/userfiles/images/2017/2/22/650/cypbcejyo3zqnsg.jpg\" /></p>\n" +
//                "<p align=\"center\"></p>\n" +
//                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 12日上午，代表团访问美国磁飞机公司（MTI）和麻省理工学院（MIT）等离子科学与聚变中心。MTI公司总裁、美国工程院院士布鲁斯•蒙哥马利（D. Bruce. Montegemery）教授及其团队接待了代表团。双方介绍了各自的研究历程和成果。在钟志华校长的见证下，同济大学（国家）磁浮交通工程技术研究中心主任陈小鸿教授和美国MTI公司总载蒙哥马利教授签署了磁浮交通技术合作意向书。中国中车长春轨道客车股份有限公司代表参加交流。随后，代表团参观了MIT等离子科学与聚变中心实验室。</p>\n" +
//                "<p align=\"center\"><img title=\"image004\" border=\"0\" alt=\"image004\" src=\"http://photo.tongji.edu.cn/themes/11/userfiles/images/2017/2/22/650/wkpfry4u0wfawop.gif\" /></p>\n" +
//                "<p align=\"center\"></p>\n" +
//                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 当天下午，代表团访问中车集团美国分公司，双方探讨了同济大学与美国知名高校合作，为中国企业的国际化发展培养人才的市场需求及联合办学的可能性。<br />\n" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 13日上午，代表团一行首先访问了哈佛大学设计研究生院，与院长Mohsen Mostafavi教授会谈。双方回顾了同济大学建筑与城市规划学院与哈佛设计研究生院的长期合作， Mostafavi表示高级总裁培训项目和城市研究为将来重点开展合作的领域。</p>\n" +
//                "<p align=\"center\"><img title=\"image005\" border=\"0\" alt=\"image005\" src=\"http://photo.tongji.edu.cn/themes/11/userfiles/images/2017/2/22/650/tjt4wjcwyennerf.jpg\" /></p>\n" +
//                "<p align=\"left\"></p>\n" +
//                "<p align=\"left\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 随后，代表团考察众创空间Mass Innovation Lab，与入驻企业Waveguide公司会谈，并参观众创空间NGIN workplace。同济大学高端专家Jarmo Suominen教授介绍了NGIN的理念和运营状况。<br />\n" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 下午，代表团一行首先访问了MIT Media Lab。在钟志华的见证下，我校设计创意学院院长娄永琪与美国麻省理工学院媒体实验室主任Joi ITO在麻省理工学院媒体实验室（Media Lab）正式签署合作协议，将共同在上海建设“City Science <a href=\"mailto:Lab@Shanghai\">Lab@Shanghai</a>”联合实验室。致力于推动同济大学与麻省理工学院在教学和科研方面的合作，结合上海需求开展设计、技术、政策、服务的相关研究和创新转化。</p>\n" +
//                "<p align=\"center\"><img title=\"image007\" border=\"0\" alt=\"image007\" src=\"http://photo.tongji.edu.cn/themes/11/userfiles/images/2017/2/22/650/fzpsp8oi21nm4rt.jpg\" /></p>\n" +
//                "<p align=\"center\"></p>\n" +
//                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 接着，代表团成员访问MIT建筑系，与系主任Meejin Yoon教授及Anton Gabril教授会见。双方商讨了作为代表中国和美国核心合作伙伴开展全球网络设计课程教学的设想，并就MIT接受同济CSC项目交换博士生进行了具体商谈。<br />\n" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 随后，钟志华考察了MIT机械工程系，美国工程院院士、系主任陈钢教授介绍了MIT机械工程系在学科建设、创新教学和研究领域的最新进展，并就中美工程大会的筹备交换了意见。<br />\n" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 钟志华一行先后考察了位于波士顿的Autodesk Build Space 和位于旧金山One Market的Autodesk公司总部。公司高级副总裁Chris Bradshaw代表欧特克公司欢迎了钟校长一行，并对欧特克和同济大学的合作基础和成果给予了高度评价。代表团与Bradshaw，负责教育的副总裁 Mary Hope、基金会主任Joe Speicher和全球“未来学习”总监Sunand Bhattacharya博士等会谈，探讨全球创新和院校合作战略。</p>\n" +
//                "<p align=\"center\"><img title=\"image009\" border=\"0\" alt=\"image009\" src=\"http://photo.tongji.edu.cn/themes/11/userfiles/images/2017/2/22/650/jzlt3hnwzmbsx0o.jpg\" /></p>\n" +
//                "<p align=\"center\"></p>\n" +
//                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 15日，钟志华一行访问了斯坦福大学。斯坦福大学Change Labs创始人及主任Banny Banerjee教授介绍了斯坦福大学在跨学科教学、跨学科研究领域的改革等。Banerjee还向代表团介绍了2016年由Springer出版的《Creating Innovation Leaders，a global perspective》一书，收录了包括同济大学在内的十余个世界上最为创新的跨学科教学项目。<br />\n" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 随后，钟志华一行访问了斯坦福大学哈索•普拉特纳设计研究所（The Hasso Plattner Institute of Design at Stanford University）。主任Larry Leifer教授向代表团介绍了D-School的理念和跨学科实践，以设计思维的广度来加深各专业学位教育的深度。<br />\n" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 16日，钟志华首先访问了美国劳伦斯伯克利国家实验室。<a name=\"OLE_LINK7\"></a><a name=\"OLE_LINK6\"></a><a name=\"OLE_LINK5\"></a><a name=\"OLE_LINK4\"></a><a name=\"OLE_LINK3\">实验室</a>副主任Susan S. Hubbard博士代表该实验室详细介绍了该国家实验室组织机构、运营模式、开放资源等。双方就劳伦斯伯克利国家实验室与利弗莫尔国家实验室研究方向、国家实验室与大学之间的合作关系、共同申请科研项目等进行了深入探讨。会谈后，钟校长一行参观了劳伦斯伯克利国家实验室的先进光源试验装置。<br />\n" +
//                "&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 随后，钟志华访问了加州大学伯克利分校，常务副校长Carol Christ亲切会见钟校长一行，在双方20余名师生的见证下，签署了两校新一期五年合作协议。双方探讨了联合海外办学以及建立国际大科学家工作室的可能性。代表团成员还访问了伯克利环境设计学院和雅各设计学院。</p>\n" +
//                "<p align=\"center\"><img title=\"image011\" border=\"0\" alt=\"image011\" src=\"http://photo.tongji.edu.cn/themes/11/userfiles/images/2017/2/22/650/fv5mzdiwtbj9hsj.jpg\" /></p>\n" +
//                "<p align=\"center\"></p>\n" +
//                "<p>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 17日，代表团一行访问了位于加拿大温哥华的英属哥伦比亚大学（UBC），与副校长兼教务长Angela Redish教授和相关部门领导和专家进行了友好的交流和讨论。双方就深入建设符合未来发展需求的新专业、新学科，培养社会需求的跨专业、复合型、国际化人才等进行展望。钟校长一行还访问了森林学院，参观了目前世界上最高的、充分体现可持续发展理念和现代装配化技术的木结构建筑-18层学生公寓等工程。代表团成员还与教育学院院长洽谈了教师培训项目。</p>\n" +
//                "<p align=\"center\"><img title=\"image013\" border=\"0\" alt=\"image013\" src=\"http://photo.tongji.edu.cn/themes/11/userfiles/images/2017/2/22/650/lx13bmzgxof4tji.jpg\" /></p>\n" +
//                "<p align=\"center\"></p>\n" +
//                "</div>\n" +
//                "\n" +
//                "</body>\n" +
//                "</html>";
        //注意传入的Type值，不能是简单的text/html，否则中文会乱码.
//        webView.loadData(str, "text/html; charset=UTF-8", null);
    }
}
