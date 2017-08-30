package com.example.wangaishi20170830unit1_internet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xutils.common.Callback;
import org.xutils.http.RequestParams;
import org.xutils.view.annotation.ContentView;
import org.xutils.view.annotation.ViewInject;
import org.xutils.x;

import java.util.ArrayList;

import view.xlistview.XListView;

@ContentView(R.layout.activity_main)
public class MainActivity extends AppCompatActivity implements XListView.IXListViewListener {

    @ViewInject(R.id.xlv)
    XListView xlistView;
    private XListView xlv;
    private ArrayList<News> list;
    private String url="http://v.juhe.cn/toutiao/index?type=&key=22a108244dbb8d1f49967cd74a0c144d";
    private final int atype=0;
    private final int btype=1;
    private int type_num=2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        x.view().inject(this);
        initView();
        initDate();
        initImageLoader();
    }

    private void initImageLoader() {
        DisplayImageOptions options=new DisplayImageOptions.Builder()
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .build();
        ImageLoaderConfiguration config=new ImageLoaderConfiguration.Builder(this)
                .defaultDisplayImageOptions(options)
                .build();
        ImageLoader.getInstance().init(config);
    }

    private void initDate() {
        RequestParams params = new RequestParams(url);
        x.http().post(params, new Callback.CommonCallback<String>() {

            private Myadapter ma;

            @Override
            public void onSuccess(String result) {
                try {
                    JSONObject obj = new JSONObject(result);
                    JSONObject resultobj = obj.getJSONObject("result");
                    JSONArray dataArray = resultobj.getJSONArray("data");
                    if (dataArray.length() > 0 && dataArray != null) {
                        for (int i = 0; i < dataArray.length(); i++) {
                            News news = new News();
                            JSONObject data = (JSONObject) dataArray.get(i);
                            news.title = data.getString("title");
                            news.thumbnail_pic_s = data.getString("thumbnail_pic_s");
                            list.add(news);
                        }
                        ma = new Myadapter();
                        xlv.setAdapter(ma);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onError(Throwable ex, boolean isOnCallback) {

            }

            @Override
            public void onCancelled(CancelledException cex) {

            }

            @Override
            public void onFinished() {

            }
        });
    }


    private void initView() {
        xlv = (XListView) findViewById(R.id.xlv);
        xlv.setPullLoadEnable(true);
        xlv.setXListViewListener(this);
        list = new ArrayList<News>();
    }

    @Override
    public void onRefresh() {

    }

    @Override
    public void onLoadMore() {

    }


    class Myadapter extends BaseAdapter
    {

        @Override
        public int getItemViewType(int position) {
            if(position%2==0){
                return atype;
            }else
            {
                return btype;
            }
        }

        @Override
        public int getViewTypeCount() {
            return type_num;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            ViewHolder1 holder1=null;
            ViewHolder2 holder2=null;
            int type = getItemViewType(i);
            if(view==null)
            {
                switch (type)
                {
                    case atype:
                        holder1=new ViewHolder1();
                        view = View.inflate(MainActivity.this, R.layout.item1, null);
                        holder1.tv_title1=view.findViewById(R.id.tv_title1);
                        holder1.tv_image1=view.findViewById(R.id.tv_image1);
                        view.setTag(holder1);
                        break;
                    case btype:
                        holder2=new ViewHolder2();
                        view = View.inflate(MainActivity.this, R.layout.item2, null);
                        holder2.tv_title2=view.findViewById(R.id.tv_title2);
                        holder2.tv_image2=view.findViewById(R.id.tv_image2);
                        view.setTag(holder2);
                        break;
                }
            }else
            {
                switch (type)
                {
                    case atype:
                        holder1= (ViewHolder1) view.getTag();
                        break;
                    case btype:
                        holder2= (ViewHolder2) view.getTag();
                        break;
                }
            }
            switch (type)
            {
                case atype:
                    holder1.tv_title1.setText(list.get(i).title);
                    ImageLoader.getInstance().displayImage(list.get(i).thumbnail_pic_s,holder1.tv_image1);
                    break;
                case btype:
                    holder2.tv_title2.setText(list.get(i).title);
                    ImageLoader.getInstance().displayImage(list.get(i).thumbnail_pic_s,holder2.tv_image2);
                    break;
            }
            return view;
        }
    }

    public class ViewHolder1
    {
        public ImageView tv_image1;
        public TextView tv_title1;
    }
    public class ViewHolder2
    {
        public ImageView tv_image2;
        public TextView tv_title2;
    }
}