package com.example.friendlist;

import static android.content.Context.MODE_PRIVATE;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ContactFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ContactFragment extends Fragment {

    ArrayList<User> friendList = new ArrayList<>();
    private String[] friendName = {
            "Pancake Tekon", "Vvokos", "China Boy","Leafeon BaiLong",
            "Pinkcandy Zhou", "DanielL 04", "Rokidna UG",
            "Ice Wings","Joy Project", "White sheep",
            "Dreamland Palesky","Sliver Cat", "多摩 aac1"};


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public ContactFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ContactFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ContactFragment newInstance(String param1, String param2) {
        ContactFragment fragment = new ContactFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        fillArray(); // 暂时的方法，将所有的静态list存到Arraylist中，好实现一会儿的"删除"+视图更新

        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        ListView listView = view.findViewById(R.id.contactListView);
        listView.setAdapter(new MyAdapter());

        // 读取SharedPreferences中的'本地用户数据' (目前只要了uid, 其他要的后面再加)
        SharedPreferences sp = getActivity().getSharedPreferences("userdata", MODE_PRIVATE);
        String selfID = sp.getString("uid", "null");

        // 设置短按item的监听器
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Toast.makeText(ContactFragment.this.getActivity(), "You've clicked " + friendList.get(i).getName(), Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ContactFragment.this.getActivity(), ChatPage.class);
                intent.putExtra("friendName", friendList.get(i).getName());
                intent.putExtra("friendID", friendList.get(i).getUid());
                intent.putExtra("selfID", selfID);
                startActivity(intent);
            }
        });

        // 设置长按item的监听器 (弹窗提示是否要删除好友)
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {

                // 可以在这里使用item对象进行后续操作
                AlertDialog.Builder bdr = new AlertDialog.Builder(ContactFragment.this.getActivity());
                // 创建一个弹窗的"建立对象", "()"内传入Activity(作用的上下文对象)
                bdr.setCancelable(true); // 设置是否可以通过"点击对话框外部"取消对话框

                bdr.setTitle("Delete Friend"); // 设置对话框标题
                bdr.setMessage("Are you sure to delete " + friendList.get(position).getName() + "?"); // 设置对话框内容
                bdr.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    // 卧槽，很新的参数用法，这个"_"是用来占位的，表示"我不需要这个参数"

                    // 在此之后，我们就可以直接用 最外面的'position'来进行item定位了
                    public void onClick(DialogInterface dialogInterface, int _) {
                        Toast.makeText(ContactFragment.this.getActivity(), "You've canceled the deletion", Toast.LENGTH_SHORT).show();
                    }
                });
                bdr.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int _) {
                        // 删除好友item 并 更新视图
                        Toast.makeText(ContactFragment.this.getActivity(), "You've deleted " + friendList.get(position).getName(), Toast.LENGTH_SHORT).show();
                        friendList.remove(position);
                        listView.setAdapter(new MyAdapter());
                    }
                });

                AlertDialog adg = bdr.create();// 实例化"创建对象" 为 "正式弹窗"
                adg.show(); // 显示"正式弹窗"
                return true; // 返回true表示"长按"事件被消费了，不会再触发"短按"事件
            }
        });



        return view;
    }

    // 用于填充Arraylist的方法（暂用，里面填的是User对象）
    public void fillArray(){
        // User(name,email,uid);
        User u1 = new User("Rokidna UG","u1mail","aaf57298-8de9-4bd1-8ffe-4830f0926e4d");
        User u2 = new User("PinkCandy Zhou","u2mail","8faa8b99-0e47-4f9e-a5cd-23273cd9ce46");
        User u3 = new User("China Boy","u3mail","e43fd95d-2a61-4c14-9299-32633cd17ab4");
        User u4 = new User("Vvokos","u4mail","3964a988-8b32-42f3-9d11-14b75eb1b925");
        List<User> temp = Arrays.asList(u1,u2,u3,u4);
        friendList.addAll(temp);
    }

    class MyAdapter extends BaseAdapter {
        //对于每个Adapter，我们都要去重写以下四个方法 (必须重写！)
        @Override
        public int getCount() { // 获取数据的'个数'
            return friendList.size();
        }

        @Override
        public Object getItem(int i) { //获取具体某个'元素'，上面会传入'下标'进来给你定位
            return friendList.get(i);
        }

        @Override
        public long getItemId(int i) {//获取每一个item的唯一标识符"id"(偷懒可直接用下标)
            return i;
        }

        @Override
        //重点: 获取每一个item的'展示样式' (单独开个layout文件设置每个Item的样式)
        public View getView(int i, View view, ViewGroup viewGroup) {
            //参数说明: i (item的下标位置)，
            //view (缓存着'划出视野'的item, 优化listView用)
            //viewGroup (listView对象本身，用的少)

            //1. 使用View.inflate()加载布局 inflate(Activity对象，布局文件，不知道就填null);
            View item_view = view.inflate(ContactFragment.this.getActivity(), R.layout.re_item_list, null);

            //2. 从取得的View对象中，获取里面 要进行'动态设置'的'小组件' (注意要指定为上面的inflate()的view对象来找[不然默认就会去找'全局的View']XD)
            TextView capital = (TextView) item_view.findViewById(R.id.capLetter);
            TextView nameString = (TextView) item_view.findViewById(R.id.nameStr);
            //Tips: priceLable不用动(设置)，它就是放在那里展示的

            //3. 根据"原数据"设置各个控件的'展示数据'
            String fName = friendList.get(i).getName();
            capital.setText(fName.substring(0,1));
            nameString.setText(fName);

            //4. 返回该View对象 (这样以后，这个Adapter就算建立好了，接下来将这个"格式"应用到listView控件中去)
            return item_view;
        }
    }
}

