package com.neil.XXnearby;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ThirdActivity extends Activity {
    private ListView listView;
    private List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
    private List<String>messagedata = new ArrayList<String>();
    private String title;
    /**
     * Called when the activity is first created.
     */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.second);
        listView = (ListView) findViewById(R.id.firstitem);
        listView.setDivider(new ColorDrawable(Color.BLACK));
        listView.setDividerHeight(1);
        title = getIntent().getStringExtra("title");
        init();
    }

    private void init() {
        inittitle();
        initlistView();

    }

    private void inittitle() {
        TextView textView = (TextView) findViewById(R.id.titletextview);
        textView.setText(getIntent().getCharSequenceExtra("title"));
        ImageButton backbutton = (ImageButton) findViewById(R.id.titlebackbutton);
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }


    private void initlistView(){
        initmessagedata();
        for(String s:messagedata){
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            hashMap.put("listmessage",s);
            data.add(hashMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.listitem,new String[]{"listmessage"},new int[]{R.id.listmessage}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                ImageButton button = (ImageButton) view.findViewById(R.id.nextbutton);
                button.setVisibility(View.GONE);
                final TextView textView = (TextView) view.findViewById(R.id.listmessage);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(ThirdActivity.this, PoilistActivity.class);
                        intent.putExtra("title",textView.getText());
                        startActivity(intent);
                    }
                });


                return view;    //To change body of overridden methods use File | Settings | File Templates.
            }
        };

        listView.setAdapter(simpleAdapter);

    }

    private void initmessagedata(){
        if("中餐厅".equals(title)){
            messagedata.add("综合酒楼");
            messagedata.add("四川菜（川菜）");
            messagedata.add("广东菜（粤菜）");
            messagedata.add("江苏菜");
            messagedata.add("浙江菜");
            messagedata.add("上海菜");
            messagedata.add("湖南菜（湘菜）");
            messagedata.add("安徽菜（徽菜）");
            messagedata.add("福建菜");
            messagedata.add("北京菜");
            messagedata.add("湖北菜（鄂菜）");
            messagedata.add("东北菜");
            messagedata.add("云贵菜");
            messagedata.add("西北菜");
            messagedata.add("老字号");
            messagedata.add("火锅店");
            messagedata.add("特色/地方风味餐厅");
            messagedata.add("海鲜酒楼");
            messagedata.add("中式素菜馆");
            messagedata.add("清真菜馆");
            messagedata.add("台湾菜");
            messagedata.add("潮州菜");


        }else if ("外国餐厅".equals(title)){
            messagedata.add("西餐厅（综合风味）");
            messagedata.add("日本料理");
            messagedata.add("法式菜品餐厅");
            messagedata.add("意式菜品餐厅");
            messagedata.add("泰国/越南菜品餐厅");
            messagedata.add("地中海风格菜品");
            messagedata.add("美食风味");
            messagedata.add("印度风味");
            messagedata.add("牛扒店（扒房）");
            messagedata.add("俄国菜");
            messagedata.add("德国菜");
            messagedata.add("巴西菜");
            messagedata.add("墨西哥菜");
            messagedata.add("其它亚洲菜");

        }else if("快餐厅".equals(title)){
            messagedata.add("肯德基");
            messagedata.add("麦当劳");
            messagedata.add("必胜客");
            messagedata.add("永和豆浆");
            messagedata.add("茶餐厅");
            messagedata.add("大家乐");
            messagedata.add("大快活");
            messagedata.add("美心");
            messagedata.add("吉野家");
            messagedata.add("仙跡岩");



        } else if("咖啡馆".equals(title)){
            messagedata.add("星巴克咖啡");
            messagedata.add("上岛咖啡");
            messagedata.add("Pacifi Ccoffee Company");
            messagedata.add("巴黎咖啡店");


        }else if("商场".equals(title)){
            messagedata.add("购物中心");
            messagedata.add("普通商场");
            messagedata.add("免税品店");


        }else if("便民商店/便利店".equals(title)){
            messagedata.add("7-ELEVEN便利店");
            messagedata.add("OK便利店");


        }else if("家电电子卖场".equals(title)){
            messagedata.add("综合家电市场");
            messagedata.add("国美");
            messagedata.add("大中");
            messagedata.add("苏宁");
            messagedata.add("手机销售");
            messagedata.add("数码电子");
            messagedata.add("丰泽");
            messagedata.add("镭射");


        } else if("超级市场".equals(title)){
            messagedata.add("家乐福");
            messagedata.add("沃尔玛");
            messagedata.add("华润");
            messagedata.add("北京华联");
            messagedata.add("上海华联");
            messagedata.add("麦德龙");
            messagedata.add("万客隆");
            messagedata.add("华堂");
            messagedata.add("易初莲花");
            messagedata.add("好又多");
            messagedata.add("屈臣氏");
            messagedata.add("乐购");
            messagedata.add("惠康超市");
            messagedata.add("百佳超市");
            messagedata.add("万宁超市");

        } else if("花鸟鱼虫市场".equals(title)){
            messagedata.add("花卉市场");
            messagedata.add("鱼虫市场");
        }else if("家居建材市场".equals(title)){
            messagedata.add("家居建材综合市场");
            messagedata.add("家具城");
            messagedata.add("建材五金市场");
            messagedata.add("厨卫市场");
            messagedata.add("布艺市场");
            messagedata.add("灯具瓷器市场");
        }
        else if("综合市场".equals(title)){
            messagedata.add("小商品市场");
            messagedata.add("旧货市场");
            messagedata.add("农副产品市场");
            messagedata.add("果品市场");
            messagedata.add("蔬菜市场");
            messagedata.add("水产海鲜市场");
        } else if("体育用品店".equals(title)){
            messagedata.add("李宁专卖店");
            messagedata.add("耐克专卖店");
            messagedata.add("阿迪达斯专卖店");
            messagedata.add("锐步专卖店");
            messagedata.add("彪马专卖店");
            messagedata.add("高尔夫用品店");
            messagedata.add("户外用品");
        } else if("特色商业街".equals(title)){
            messagedata.add("步行街");

        }else if("服装鞋帽皮具店".equals(title)){
            messagedata.add("品牌服装店");
            messagedata.add("品牌鞋店");
            messagedata.add("品牌皮具店");

        } else if("专卖店".equals(title)){
            messagedata.add("书店");
            messagedata.add("音像店");
            messagedata.add("儿童用品店");
            messagedata.add("自行车专卖店");
            messagedata.add("礼品饰品店");
            messagedata.add("烟酒专卖店");
            messagedata.add("宠物用品店");
            messagedata.add("摄影器材店");
            messagedata.add("宝马生活方式");

        }
        else if("个人用品/化妆品店".equals(title)){
            messagedata.add("莎莎");



        }else if("售票处".equals(title)){
            messagedata.add("飞机票代售点");
            messagedata.add("火车票代售点");
            messagedata.add("长途汽车票代售点");
            messagedata.add("船票代售点");
            messagedata.add("公园景点售票处");


        } else if("邮局".equals(title)){
            messagedata.add("邮政速递");



        }else if("电讯营业厅".equals(title)){
            messagedata.add("中国电信营业厅");
            messagedata.add("中国网通营业厅");
            messagedata.add("中国移动营业厅");
            messagedata.add("中国联通营业厅");
            messagedata.add("中国铁通营业厅");
            messagedata.add("中国卫通营业厅");
            messagedata.add("和记电讯");
            messagedata.add("数码通电讯");
            messagedata.add("电讯盈科");
            messagedata.add("中国移动万众/Peoples");


        }   else if("事务所".equals(title)){
            messagedata.add("律师事务所");
            messagedata.add("会计师事务所");
            messagedata.add("评估事务所");
            messagedata.add("审计事务所");
            messagedata.add("认证事务所");
            messagedata.add("专利事务所");



        }  else if("丧葬设施".equals(title)){
            messagedata.add("陵园");
            messagedata.add("公墓");
            messagedata.add("殡仪馆");




        } else if("运动场所".equals(title)){
            messagedata.add("综合体育馆");
            messagedata.add("保龄球馆");
            messagedata.add("网球场");
            messagedata.add("篮球场馆");
            messagedata.add("足球场");
            messagedata.add("滑雪场");
            messagedata.add("溜冰场");
            messagedata.add("户外健身场所");
            messagedata.add("游泳馆");
            messagedata.add("健身中心");
            messagedata.add("乒乓球馆");
            messagedata.add("台球厅");
            messagedata.add("壁球场");
            messagedata.add("马术俱乐部");
            messagedata.add("赛马场");
            messagedata.add("橄榄球场");
            messagedata.add("羽毛球场");
            messagedata.add("跆拳道场馆");







        }   else if("高尔夫相关".equals(title)){
            messagedata.add("高尔夫球场");
            messagedata.add("高尔夫练习场");


        }  else if("娱乐场所".equals(title)){
            messagedata.add("夜总会");
            messagedata.add("K T V");
            messagedata.add("迪厅");
            messagedata.add("酒吧");
            messagedata.add("游戏厅");
            messagedata.add("棋牌室");
            messagedata.add("博彩中心");
            messagedata.add("网吧");


        }  else if("度假疗养场所".equals(title)){
            messagedata.add("度假村");
            messagedata.add("疗养院");


        } else if("休闲场所".equals(title)){
            messagedata.add("游乐场");
            messagedata.add("垂钓园");
            messagedata.add("采摘园");
            messagedata.add("露营地");
            messagedata.add("水上活动中心");



        } else if("影剧院".equals(title)){
            messagedata.add("电影院");
            messagedata.add("音乐厅");
            messagedata.add("剧场");




        }   else if("综合医院".equals(title)){
            messagedata.add("三级甲等医院");
            messagedata.add("卫生院");

        }    else if("专科医院".equals(title)){
            messagedata.add("整形美容");
            messagedata.add("口腔医院");
            messagedata.add("眼科医院");
            messagedata.add("耳鼻喉医院");
            messagedata.add("胸科医院");
            messagedata.add("骨科医院");
            messagedata.add("肿瘤医院");
            messagedata.add("妇科医院");
            messagedata.add("精神病医院");
            messagedata.add("传染病医院");



        }    else if("医疗保健相关".equals(title)){
            messagedata.add("药房");
            messagedata.add("医疗保健用品");

        }   else if("动物医疗场所".equals(title)){
            messagedata.add("宠物诊所");
            messagedata.add("兽医站");

        } else if("宾馆酒店".equals(title)){
            messagedata.add("六星级宾馆");
            messagedata.add("五星级宾馆");
            messagedata.add("四星级宾馆");
            messagedata.add("三星级宾馆");
            messagedata.add("经济型连锁酒店");


        }  else if("旅馆招待所".equals(title)){
            messagedata.add("青年旅社");


        }    else if("博物馆".equals(title)){
            messagedata.add("奥迪博物馆");
            messagedata.add("奥迪博物馆");



        }    else if("传媒机构".equals(title)){
            messagedata.add("电视台");
            messagedata.add("电台");
            messagedata.add("报社");
            messagedata.add("杂志社");
            messagedata.add("出版社");




        }   else if("学校".equals(title)){
            messagedata.add("高等院校");
            messagedata.add("中学");
            messagedata.add("小学");
            messagedata.add("幼儿园");
            messagedata.add("成人教育");
            messagedata.add("职业技术学校");




        }  else if("港口码头".equals(title)){
            messagedata.add("客运港");
            messagedata.add("车渡口");
            messagedata.add("人渡口");





        }    else if("公交车站".equals(title)){
            messagedata.add("旅游专线车站");
            messagedata.add("普通公交站");





        }   else if ("停车场".equals(title)){
            messagedata.add("室内停车场");
            messagedata.add("室外停车场");
            messagedata.add("停车换乘点");





        }
    }

}
