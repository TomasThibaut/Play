package cn.gc.customerview;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * 一个ListView用来加载单独的自定义Item
 */
public class MainActivity extends AppCompatActivity {
    private ListView lv_main;
    private MyAdapter mAdapter;
    private Context mContext;
    private ArrayList<String> dataList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }

    private void init() {
        dataList = new ArrayList<>();
        for (int i = 0; i < 150; i++) {
            dataList.add("" + i);
        }
        mListener = new MyStatusChangedListener();
        mContext = this;
        mAdapter = new MyAdapter();
        lv_main = (ListView) findViewById(R.id.lv_main);
        lv_main.setAdapter(mAdapter);
    }

    /**
     * 用于标识Item打开的状态
     */
    private boolean flag;

    private class MyAdapter extends BaseAdapter implements View.OnClickListener {

        @Override
        public int getCount() {
            return dataList.size();
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            final ViewHolder holder;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = View.inflate(mContext, R.layout.item_slidedelete, null);
                //得到tv_content的实例,并且添加点击监听
                holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
                holder.tv_content.setOnClickListener(this);
                holder.tv_delete = (TextView) convertView.findViewById(R.id.tv_delete);
                holder.tv_delete.setOnClickListener(this);
                holder.mSlideDeleteView = (SlideDeleteView) convertView.findViewById(R.id.slideView);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            holder.tv_content = (TextView) convertView.findViewById(R.id.tv_content);
            holder.tv_content.setText(dataList.get(position));
            holder.tv_content.setTag(dataList.get(position));
            holder.tv_delete.setTag(dataList.get(position));
            holder.mSlideDeleteView.setOnStatusChangedListener(mListener);
            return convertView;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.tv_content:
                    String position = v.getTag().toString();
                    Toast.makeText(mContext, "我是" + position + "号,Content", Toast.LENGTH_SHORT).show();

                    break;
                case R.id.tv_delete:
                    // remove掉dataList的这一行数据
                    dataList.remove(v.getTag().toString());
                    mAdapter.notifyDataSetChanged();
                    break;
            }
        }
    }

    private MyStatusChangedListener mListener;

    private class MyStatusChangedListener implements SlideDeleteView.onStatusChangedListener {
        /**
         * 声明一个引用来保存被打开的SlideView
         */
        private SlideDeleteView openedSlideView;

        /**
         * 当执行打开操作的时候被调用,保存打开的SlideView
         */
        @Override
        public void onOpen(SlideDeleteView view) {
            openedSlideView = view;
        }

        /**
         * 当执行关闭操作的时候被调用,置空关闭的SlideView
         */
        @Override
        public void onClose(SlideDeleteView view) {
            openedSlideView = null;
        }

        /**
         * 只有唯一的一个可以被打开,所以只要不是保存的对象,就返回false
         */
        @Override
        public boolean isOpenable(SlideDeleteView view) {
            return openedSlideView == view;
        }
    }

    static class ViewHolder {
        private TextView tv_content;
        private TextView tv_delete;
        private SlideDeleteView mSlideDeleteView;
    }

    /**
     * 回调方式①
     */
    public class MyCallback extends SlideDeleteView.ItemToggleCallback {

        @Override
        public void onOpen(SlideDeleteView view) {
            view.open();
        }

        @Override
        public void onClose(SlideDeleteView view) {
        }
    }
}
