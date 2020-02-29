package cn.itcast.test2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Color;
import android.renderscript.Sampler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.format.Time;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SimpleAdapter;
import android.widget.Adapter;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class home_page extends AppCompatActivity {

    private Button btn_add;
    private Button btn_piechart;
    private Button btn_payment;
    private ImageView img_write;
    private TextView tv_sentence;

    private RecyclerView mRecyclerView;
    private List<CostBean> mList;
    private CostListAdapter mAdapter;
    private DataBaseHelper mDatabaseHelper;
    private Time date;
    private Button btn_up;
    private Button btn_down;
    private TextView tv_month;
    private int month;
    private TextView expnum;
    private TextView incomum;
    private int exp_num_month;
    private int income_num_month;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);

        btn_add = findViewById(R.id.btn_add);
        btn_piechart = findViewById(R.id.btn_piechart);
        btn_payment = findViewById(R.id.btn_payment);

        img_write = findViewById(R.id.img_write);
        tv_sentence = findViewById(R.id.tv_sentence);
        btn_down=findViewById(R.id.btn_up);
        btn_up=findViewById(R.id.btn_down);
        tv_month=findViewById(R.id.tv_month);
        expnum=findViewById(R.id.tv_expendnum);
        incomum=findViewById(R.id.tv_incomenum);

        //获取当前时间
        date = new Time();
        date.setToNow();
        month=date.month;
        month++;
        tv_month.setText(String.valueOf(month)+"月");

        //按键监听
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, Account.class);
                intent.putExtra("chart",0);
                startActivityForResult(intent,1);
            }
        });

        btn_piechart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(home_page.this, Chart.class);
                btn_piechart.setBackgroundResource(R.drawable.piechart1);
                btn_payment.setBackgroundResource(R.drawable.payment);
                startActivity(intent);
            }
        });
        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month<12)
                {
                    month++;
                    tv_month.setText(String.valueOf(month)+"月");
                    initList(month);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month>1)
                {

                    month--;
                    tv_month.setText(String.valueOf(month)+"月");
                    initList(month);
                    mAdapter.notifyDataSetChanged();
                }
            }
        });

        mDatabaseHelper = new DataBaseHelper(this);
        mList = new ArrayList<CostBean>();

        initList(date.month+1);
        initView();
        mAdapter = new CostListAdapter(this,mList);
        mRecyclerView.setAdapter(mAdapter);


        //设置RecyclerView的布局管理
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        mRecyclerView.setLayoutManager(linearLayoutManager);

        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        //设置RecyclerView的Item分割线
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL_LIST));

        mAdapter.setOnItemClickListener(new CostListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Toast.makeText(home_page.this,"click"+position,Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Toast.makeText(home_page.this,"long click"+position,Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(home_page.this, Account.class);
                CostBean bean = mList.get(position);
                intent.putExtra("costTile",bean.costTile);
                intent.putExtra("costMoney",bean.costMoney);
                intent.putExtra("costInOut",bean.costInOut);
                intent.putExtra("position",position);
                intent.putExtra("chart",3);
                startActivity(intent);
            }
        });

        if(checkIntentAvailable(getIntent()))
        {
            int i = getIntent().getIntExtra("chart",0);
            if(i==1)
            {
                Intent chartdata = getIntent();
                CostBean costBean = new CostBean();
                costBean.costDate = chartdata.getStringExtra("Date");
                costBean.costYear = chartdata.getStringExtra("Year");
                costBean.costMonth = chartdata.getStringExtra("Month");
                costBean.costDay = chartdata.getStringExtra("Day");
                costBean.costMinute = chartdata.getStringExtra("Minute");
                costBean.costHour = chartdata.getStringExtra("Hour");
                costBean.costSecond = chartdata.getStringExtra("Second");
                Toast.makeText(home_page.this,costBean.costSecond,Toast.LENGTH_SHORT).show();

                costBean.costTile = chartdata.getStringExtra("classify");
                boolean inorout = chartdata.getBooleanExtra("inorout",false);
                if(inorout)
                {
                    costBean.costInOut = "支出";
                    costBean.costMoney = "-" + chartdata.getStringExtra("money");
                }
                else
                {
                    costBean.costInOut = "收入";
                    costBean.costMoney = "+" + chartdata.getStringExtra("money");
                }
                mDatabaseHelper.insertCost(costBean);
                Toast.makeText(home_page.this,"数据库"+mDatabaseHelper.idd,Toast.LENGTH_SHORT).show();
                mAdapter.addData(costBean,0);
            }
            else if(i==2)
            {
                Intent chartdata = getIntent();
                int pos = chartdata.getIntExtra("position",0);
                CostBean costBean = mList.get(pos);
                costBean.costTile = chartdata.getStringExtra("classify");
                boolean inorout = chartdata.getBooleanExtra("inorout",false);
                if(inorout)
                {
                    costBean.costInOut = "支出";
                    costBean.costMoney = "-" + chartdata.getStringExtra("money");
                }
                else
                {
                    costBean.costInOut = "收入";
                    costBean.costMoney = "+" + chartdata.getStringExtra("money");
                }
                String[] a = new String[1];
                a[0]=costBean.costDate;
                mDatabaseHelper.upgradeCost(costBean,a);
                mAdapter.upgradeData(pos);
            }
            else
            {
                Toast.makeText(home_page.this,"finish",Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initList(int month)
    {
        //测试数据前清空数据库
        //mDatabaseHelper.deleteAllData();


        //初始数据
//        for(int i = 0;i <= 2;i++)
//        {
//            CostBean test1 = new CostBean();
//            test1.costDate = "time";
//            test1.costTile = "家教"+i;
//            test1.costInOut = "收入";
//            test1.costMoney = ""+(char)i*10;
//            //mList.add(test1);
//            mDatabaseHelper.insertCost(test1);
//        }
         mList.clear();
         exp_num_month=0;
         income_num_month=0;

        //游标
        Cursor cursor = mDatabaseHelper.getAllCostData();
        if(cursor != null)
        {
            img_write.setVisibility(View.GONE);
            tv_sentence.setVisibility(View.GONE);

            while (cursor.moveToNext())
            {
                CostBean costBean = new CostBean();
                costBean.costTile = cursor.getString(cursor.getColumnIndex("costTile"));

                costBean.costDate = cursor.getString(cursor.getColumnIndex("costDate"));
                costBean.costYear = cursor.getString(cursor.getColumnIndex("costYear"));
                costBean.costMonth = cursor.getString(cursor.getColumnIndex("costMonth"));
                costBean.costDay = cursor.getString(cursor.getColumnIndex("costDay"));
                costBean.costHour= cursor.getString(cursor.getColumnIndex("costHour"));
                costBean.costMinute = cursor.getString(cursor.getColumnIndex("costMinute"));
                costBean.costSecond = cursor.getString(cursor.getColumnIndex("costSecond"));

                costBean.costMoney = cursor.getString(cursor.getColumnIndex("costMoney"));
                costBean.costInOut = cursor.getString(cursor.getColumnIndex("costInOut"));
                if(month-1==Integer.parseInt(costBean.costMonth))
                {
                    mList.add(costBean);
                    int temp=Integer.parseInt(cursor.getString(8));
                    if(temp>=0) income_num_month+=Math.abs(temp);
                    else exp_num_month+=Math.abs(temp);
                }
            }
            cursor.close();
            expnum.setText(String.valueOf(exp_num_month));
            incomum.setText(String.valueOf(income_num_month));
        }
        else
        {
            img_write.setVisibility(View.VISIBLE);
            tv_sentence.setVisibility(View.VISIBLE);
        }
    }


    private void initView()
    {
        mRecyclerView = findViewById(R.id.id_recylerView);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 1:
                if(resultCode == RESULT_OK){
                    CostBean costBean = new CostBean();
                    costBean.costDate = data.getStringExtra("Date");
                    costBean.costYear = data.getStringExtra("Year");
                    costBean.costMonth = data.getStringExtra("Month");
                    costBean.costDay = data.getStringExtra("Day");
                    costBean.costMinute = data.getStringExtra("Minute");
                    costBean.costHour = data.getStringExtra("Hour");
                    costBean.costSecond = data.getStringExtra("Second");

                    costBean.costTile = data.getStringExtra("classify");
                    boolean inorout = data.getBooleanExtra("inorout",false);
                    if(inorout)
                    {
                        //costBean.costInOut = "1";
                        //Toast.makeText(home_page.this,"支出",Toast.LENGTH_SHORT).show();

                        costBean.costInOut = "支出";
                        costBean.costMoney = "-" + data.getStringExtra("money");
                    }
                    else
                    {
                        //costBean.costInOut = "0";
                        //Toast.makeText(home_page.this,"收入",Toast.LENGTH_SHORT).show();

                        costBean.costInOut = "收入";
                        costBean.costMoney = "+" + data.getStringExtra("money");
                    }
                    //Toast.makeText(home_page.this,costBean.costInOut + costBean.costMoney ,Toast.LENGTH_LONG).show();
                    mDatabaseHelper.insertCost(costBean);
                    Toast.makeText(home_page.this,"数据库"+mDatabaseHelper.idd,Toast.LENGTH_SHORT).show();
                    mAdapter.addData(costBean,0);
                    initList(month);
                    //mAdapter.notifyDataSetChanged();
                    //Toast.makeText(home_page.this,"添加",Toast.LENGTH_SHORT).show();
                }
                break;
            case 2:
                Toast.makeText(home_page.this,"case2",Toast.LENGTH_SHORT).show();
                if(resultCode == RESULT_OK) {

                }
                break;
            default:
                break;
        }
    }



    public boolean checkIntentAvailable(Intent intent){

        if (intent.resolveActivity(getPackageManager()) != null) {
            return true;

        } else {
            return  false;
        }

    }


    /**

     * Indicates whether the specified action can be used as an intent. This

     * method queries the package manager for installed packages that can

     * respond to an intent with the specified action. If no suitable package is

     * found, this method returns false.

     *

     * @param context The application's environment.

     * @param action The Intent action to check for availability.

     *

     * @return True if an Intent with the specified action can be sent and

     *         responded to, false otherwise.

     */

    public static boolean isIntentAvailable(Context context, String action) {

        final PackageManager packageManager = context.getPackageManager();

        final Intent intent = new Intent(action);

        List<ResolveInfo> list =

                packageManager.queryIntentActivities(intent,

                        PackageManager.MATCH_DEFAULT_ONLY);

        return list.size() > 0;
    }




}
