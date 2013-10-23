package com.neil.XXnearby;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import com.neil.XXnearby.model.Cat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SecondActivity extends Activity {
    private ListView listView;
    private List<Map<String,Object>> data = new ArrayList<Map<String, Object>>();
    private List<Cat>messagedata = new ArrayList<Cat>();
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
        for(Cat s:messagedata){
            HashMap<String,Object> hashMap = new HashMap<String, Object>();
            hashMap.put("listmessage",s.getName());
            data.add(hashMap);
        }

        SimpleAdapter simpleAdapter = new SimpleAdapter(this,data,R.layout.listitem,new String[]{"listmessage"},new int[]{R.id.listmessage}){
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view =super.getView(position, convertView, parent);
                ImageButton button = (ImageButton) view.findViewById(R.id.nextbutton);
                final TextView textView = (TextView) view.findViewById(R.id.listmessage);

                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Log.d("wqw", view.toString());
                        Intent intent = new Intent();
                        intent.setClass(SecondActivity.this, ThirdActivity.class);
                        intent.putExtra("title",textView.getText());
                        startActivity(intent);
                    }
                });

                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent();
                        intent.setClass(SecondActivity.this, PoilistActivity.class);
                        intent.putExtra("title",textView.getText());
                        startActivity(intent);
                    }
                });
                String str = textView.getText().toString();
                if(!messagedata.get(position).isHavechild()){
                    button.setVisibility(View.GONE);
                }
                return view;    //To change body of overridden methods use File | Settings | File Templates.
            }
        };

        listView.setAdapter(simpleAdapter);

    }

    private void initmessagedata(){
        if("餐饮服务".equals(title)){
            messagedata.add(new Cat("中餐厅",true));
            messagedata.add(new Cat("外国餐厅",true));
            messagedata.add(new Cat("快餐厅",true));
            messagedata.add(new Cat("休闲餐饮场所",false));
            messagedata.add(new Cat("咖啡馆",true));
            messagedata.add(new Cat("茶艺馆",false));
            messagedata.add(new Cat("冷饮店",false));
            messagedata.add(new Cat("糕饼店",false));
            messagedata.add(new Cat("甜品店",false));
        }else if ("购物服务".equals(title)){
            messagedata.add(new Cat("商场",true));
            messagedata.add(new Cat("便民商店/便利店",true));
            messagedata.add(new Cat("家电电子卖场",true));
            messagedata.add(new Cat("超级市场",true));
            messagedata.add(new Cat("花鸟鱼虫市场",true));
            messagedata.add(new Cat("家居建材市场",true));
            messagedata.add(new Cat("综合市场",false));
            messagedata.add(new Cat("文化用品店",true));
            messagedata.add(new Cat("体育用品店",true));
            messagedata.add(new Cat("特色商业街",true));
            messagedata.add(new Cat("服装鞋帽皮具店",true));
            messagedata.add(new Cat("专卖店",true));
            messagedata.add(new Cat("特殊买卖场所",true));
            messagedata.add(new Cat("个人用品/化妆品店",true));
        }else if("生活服务".equals(title)){
            messagedata.add(new Cat("旅行社",false));
            messagedata.add(new Cat("信息咨询中心",false));
            messagedata.add(new Cat("售票处",true));
            messagedata.add(new Cat("邮局",true));
            messagedata.add(new Cat("物流速递",false));
            messagedata.add(new Cat("电讯营业厅",true));
            messagedata.add(new Cat("事务所",true));
            messagedata.add(new Cat("人才市场",false));
            messagedata.add(new Cat("自来水营业厅",false));
            messagedata.add(new Cat("电力营业厅",false));
            messagedata.add(new Cat("美容美发店",false));
            messagedata.add(new Cat("维修站店",false));
            messagedata.add(new Cat("摄影冲印店",false));
            messagedata.add(new Cat("洗浴推拿场所",false));
            messagedata.add(new Cat("洗衣店",false));
            messagedata.add(new Cat("中介机构",false));
            messagedata.add(new Cat("搬家公司",false));
            messagedata.add(new Cat("彩票彩券售点",false));
            messagedata.add(new Cat("丧葬设施",true));





        }else if("体育休闲服务".equals(title)){
            messagedata.add(new Cat("运动场所",true));
            messagedata.add(new Cat("高尔夫相关",true));
            messagedata.add(new Cat("娱乐场所",true));
            messagedata.add(new Cat("度假疗养场所",true));
            messagedata.add(new Cat("休闲场所",true));
            messagedata.add(new Cat("影剧院",true));

        }
        else if("医疗保健服务".equals(title)){
            messagedata.add(new Cat("综合医院",true));
            messagedata.add(new Cat("专科医院",true));
            messagedata.add(new Cat("诊所",false));
            messagedata.add(new Cat("急救中心",false));
            messagedata.add(new Cat("疾病预防机构",false));
            messagedata.add(new Cat("医疗保健相关",true));
            messagedata.add(new Cat("动物医疗场所",true));

        }else if("住宿服务".equals(title)){
            messagedata.add(new Cat("宾馆酒店",true));
            messagedata.add(new Cat("旅馆招待所",true));


        }else if("科教文化服务".equals(title)){
            messagedata.add(new Cat("博物馆",true));
            messagedata.add(new Cat("展览馆",false));
            messagedata.add(new Cat("会展中心",false));
            messagedata.add(new Cat("美术馆",false));
            messagedata.add(new Cat("图书馆",false));
            messagedata.add(new Cat("科技馆",false));
            messagedata.add(new Cat("天文馆",false));
            messagedata.add(new Cat("文化宫",false));
            messagedata.add(new Cat("档案馆",false));
            messagedata.add(new Cat("文艺团体",false));
            messagedata.add(new Cat("传媒机构",true));
            messagedata.add(new Cat("学校",true));
            messagedata.add(new Cat("科研机构",false));
            messagedata.add(new Cat("培训机构",false));
            messagedata.add(new Cat("驾校",false));
        }  else if("交通设施服务".equals(title)){
            messagedata.add(new Cat("飞机场",false));
            messagedata.add(new Cat("火车站",false));
            messagedata.add(new Cat("港口码头",true));
            messagedata.add(new Cat("长途汽车站",false));
            messagedata.add(new Cat("地铁站",false));
            messagedata.add(new Cat("轻铁站",false));
            messagedata.add(new Cat("公交车站",true));
            messagedata.add(new Cat("班车站",false));
            messagedata.add(new Cat("停车场",true));
            messagedata.add(new Cat("过境口岸",false));

        } else if("公共设施".equals(title)){
            messagedata.add(new Cat("报刊亭",false));
            messagedata.add(new Cat("公用电话",false));
            messagedata.add(new Cat("公共厕所",false));
            messagedata.add(new Cat("紧急避难场所",false));
        }



    }

}
