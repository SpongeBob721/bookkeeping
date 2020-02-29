package cn.itcast.test2;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class label extends AppCompatActivity implements ClassifyListAdapter.OnShowItemClickListener{

    private Button btn_c_return;
    private Button btn_set;

    private TextView tv_title;

    private Button btn_redo;
    private Button btn_edit;
    private Button btn_delete;

    private Button btn_add_item;

    private CheckBox recylerView_long_checkbox;

    private RecyclerView mRecyclerView;
    private ListView listView;
    private List<CostClassify> mList;
    private List<CostClassify> sList;

    private ClassifyListAdapter mAdapter;
    private ClassifyBaseHelper mClassifybaseHelper;

    //是否显示CheckBox
    private static boolean isShow;

    private RelativeLayout lay;
    private Button btn_lay_return;
    private Button btn_lay_yes;
    private EditText et_classify;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_label);


        btn_c_return = findViewById(R.id.btn_c_return);
        btn_redo = findViewById(R.id.btn_redo);
        btn_edit = findViewById(R.id.btn_edit);
        btn_delete = findViewById(R.id.btn_delete);
        btn_add_item = findViewById(R.id.btn_add_item);

        lay = findViewById(R.id.lay);
        btn_lay_return = findViewById(R.id.btn_lay_return);
        btn_lay_yes = findViewById(R.id.btn_lay_yes);
        et_classify = findViewById(R.id.et_classify);

        mRecyclerView = findViewById(R.id.id_recylerView);

        recylerView_long_checkbox = findViewById(R.id.recylerView_long_checkbox);

        //按键监听
        btn_c_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                isShow = false;
                Intent intent = new Intent(label.this, Account.class);
                startActivity(intent);
            }
        });

        btn_add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(label.this,"添加栏",Toast.LENGTH_SHORT).show();
                ShowLay(1);
            }
        });


        mClassifybaseHelper = new ClassifyBaseHelper(this);
        mList = new ArrayList<CostClassify>();
        sList = new ArrayList<CostClassify>();
        initList();
        initView();
        mAdapter = new ClassifyListAdapter(this,mList);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnShowItemClickListener(this);


        //设置RecyclerView的布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置RecyclerView的Item分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(new ClassifyListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(label.this,"click" + position,Toast.LENGTH_SHORT).show();
                CostClassify bean = mList.get(position);
                if(isShow){

                    boolean isChecked = bean.isChecked();
                    if(isChecked){
                        bean.setChecked(false);
                    }else
                    {
                        bean.setChecked(true);
                    }
                    mAdapter.notifyDataSetChanged();
                }
                else
                {
                    //Toast.makeText(label.this,mList.get(position).getClassifyName(),Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(label.this, Account.class);
                    intent.putExtra("classify",bean.classifyName);
                    setResult(RESULT_OK,intent);
                    finish();
//                  startActivity(intent);
                }
            }

            @Override
            public boolean onItemLongClick(View view, int position) {
//                Toast.makeText(label.this,"long click"+position,Toast.LENGTH_SHORT).show();
////                recylerView_long_checkbox = findViewById(R.id.recylerView_long_checkbox);
////                recylerView_long_checkbox.setVisibility(View.VISIBLE);
////                recylerView_long_checkbox.setClickable(true);
//                Toast.makeText(label.this,"long click" + position,Toast.LENGTH_SHORT).show();
                if(isShow)
                {
                    return  false;
                }
                else
                {
                    isShow = true;
                    int a = 0;
                    for(CostClassify bean:mList){
                        bean.setShow(true);
                    }
                    //Toast.makeText(label.this,"long click" + position,Toast.LENGTH_SHORT).show();
                    mAdapter.notifyDataSetChanged();
                    showOpervate();
                    mRecyclerView.setLongClickable(false);
                }
                return true;
            }
        });
    }


    public void onShowItemClick(CostClassify bean)
    {
        if(bean.isChecked()&&!sList.contains(bean))
        {
            sList.add(bean);
            if(sList.size()>1)
            {
                btn_edit.setVisibility(View.INVISIBLE);
            }
        }
        else if(!bean.isChecked()&&sList.contains(bean))
        {
            sList.remove(bean);
            if(sList.size()<=1)
            {
                btn_edit.setVisibility(View.VISIBLE);
            }
        }
    }

    //显示操作界面
    private void showOpervate()
    {
        btn_delete.setVisibility(View.VISIBLE);
        btn_redo.setVisibility(View.VISIBLE);
        btn_edit.setVisibility(View.VISIBLE);
        btn_add_item.setVisibility(View.GONE);

        btn_redo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isShow){
                    sList.clear();
                    for(CostClassify bean:mList){
                        bean.setChecked(false);
                        bean.setShow(false);
                    }
                    mAdapter.notifyDataSetChanged();
                    isShow = false;
                    mRecyclerView.setLongClickable(true);
                    dismissOperate();
                }
            }
        });

        btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sList != null && sList.size()>0)
                {
                    String[] temp = new String[50];
                    int i = 0;
                    for(CostClassify bean:sList)
                    {
                        //Toast.makeText(label.this,bean.classifyName,Toast.LENGTH_SHORT).show();
                        temp[i] = bean.classifyName;
                        i++;
                    }
                    String[] temp1 = new String[1];
                    for(int j=0;j<i;j++)
                    {
                        temp1[0]=temp[j];
                        //Toast.makeText(label.this,temp1[j],Toast.LENGTH_SHORT).show();
                        mClassifybaseHelper.deleteData(temp1);
                    }


                    mList.removeAll(sList);
                    sList.clear();
                    for(CostClassify bean:mList){
                        bean.setChecked(false);
                        bean.setShow(false);
                    }
                    mAdapter.notifyDataSetChanged();
                    isShow = false;
                    mRecyclerView.setLongClickable(true);
                    dismissOperate();
                }
                else{
                    Toast.makeText(label.this,"请选择条目",Toast.LENGTH_SHORT).show();
                }
            }
        });

        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(sList != null && sList.size()>0)
                {
                    ShowLay(0);
                }
                else
                {
                    Toast.makeText(label.this,"请选择item编辑",Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void ShowLay(int code)
    {
        btn_add_item.setVisibility(View.GONE);
        if(code == 0)
        {
            btn_add_item.setVisibility(View.GONE);
            lay.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.operate_in);
            lay.setAnimation(animation);
            btn_lay_return.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DismissLay();
                }
            });

            btn_lay_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //修改item名称----Upgrade数据
                    CostClassify bean1 = sList.get(0);
                    String[] temp = new String[1];
                    temp[0] = bean1.classifyName;
                    bean1.setClassifyName(et_classify.getText().toString());
                    mClassifybaseHelper.upgradeCost(bean1,temp);

                    if(isShow){
                        sList.clear();
                        for(CostClassify bean:mList){
                            bean.setChecked(false);
                            bean.setShow(false);
                        }
                        mAdapter.notifyDataSetChanged();
                        isShow = false;
                        mRecyclerView.setLongClickable(true);
                        dismissOperate();
                    }
                    DismissLay();
                    mAdapter.notifyDataSetChanged();
                    //btn_add_item.setVisibility(View.VISIBLE);
                    //Toast.makeText(label.this,et_classify.getText(),Toast.LENGTH_SHORT).show();
                }
            });
        }
        else if(code == 1)
        {
            lay.setVisibility(View.VISIBLE);
            Animation animation = AnimationUtils.loadAnimation(this,R.anim.operate_in);
            lay.setAnimation(animation);
            btn_lay_return.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DismissLay();
                }
            });

            btn_lay_yes.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //设定item名称----add数据
                    CostClassify costClassify = new CostClassify();
                    costClassify.setClassifyName(et_classify.getText().toString());
                    costClassify.costInOut = "1";//可能修改
                    costClassify.setShow(false);
                    costClassify.setChecked(false);

                    mClassifybaseHelper.insertCost(costClassify);
                    mAdapter.addData(costClassify,0);
                    DismissLay();
                    mAdapter.notifyDataSetChanged();
                    //Toast.makeText(label.this,et_classify.getText(),Toast.LENGTH_SHORT).show();
                }
            });
        }

    }

    private void DismissLay()
    {
        lay.setVisibility(View.GONE);
        Animation animation = AnimationUtils.loadAnimation(this,R.anim.operate_out);
        lay.setAnimation(animation);
        btn_add_item.setVisibility(View.VISIBLE);
    }

    //隐藏操作界面
    private void  dismissOperate()
    {
        btn_add_item.setVisibility(View.VISIBLE);
        btn_delete.setVisibility(View.GONE);
        btn_redo.setVisibility(View.GONE);
        btn_edit.setVisibility(View.GONE);
    }



    private void initList()
    {
        //测试数据前清空数据库
        //mClassifybaseHelper.deleteAllData();

        //初始数据
//        for(int i = 0;i <= 2;i++)
//        {
//            CostClassify test1 = new CostClassify();
//            test1.classifyName = "餐饮-实物"+" " + i;
//            test1.costInOut = "0";
//            mClassifybaseHelper.insertCost(test1);
//        }

        //游标
        Cursor cursor = mClassifybaseHelper.getAllCostData();
        if(cursor != null)
        {
            while (cursor.moveToNext())
            {
                CostClassify costClassify = new CostClassify();
                costClassify.classifyName = cursor.getString(cursor.getColumnIndex("classifyName"));
                costClassify.costInOut = cursor.getString(cursor.getColumnIndex("costInOut"));
                mList.add(costClassify);
            }
            cursor.close();
        }
    }

    private void initView()
    {
        mRecyclerView = findViewById(R.id.id_recylerView);
    }
}
