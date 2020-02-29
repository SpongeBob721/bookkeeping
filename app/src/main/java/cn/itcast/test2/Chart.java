package cn.itcast.test2;

import android.content.Intent;
import android.app.Activity;
import android.content.Context;
import android.content.ContentValues;
import android.database.Cursor;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.format.Time;
import android.graphics.Color;
import com.hedan.piechart_library.PieChartBean;
import com.hedan.piechart_library.PieChart_View;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;





public class  Chart extends AppCompatActivity{

    private Button btn_add;
    private Button btn_piechart;
    private Button btn_payment;
    private Button btn_account_expend;
    private Button btn_account_income;
    private Button btn_down;
    private Button btn_up;
    private TextView textView_month;
    private TextView expnum;
    private TextView incomum;
    //private java.sql.Time date;
    private android.text.format.Time date;


    private TextView tv_cost;
    //----------------------yao
    int mark=-1,month;//mark 1代表查询收入，-1代表查询支出
    private DataBaseHelper nDatabaseHelper;
    private RelativeLayout[] expenditure=new RelativeLayout[8];
    private TextView[] expname=new TextView[8];
    private TextView[] expcolor=new TextView[8];
    int[] expid={
            R.id.expenditure_1,R.id.expenditure_2,
            R.id.expenditure_3, R.id.expenditure_4,
            R.id.expenditure_5,R.id.expenditure_6,
            R.id.expenditure_7, R.id.expenditure_8,
    };
    int[] expnameid={
            R.id.expname_1,R.id.expname_2,
            R.id.expname_3, R.id.expname_4,
            R.id.expname_5,R.id.expname_6,
            R.id.expname_7, R.id.expname_8,
    };
    int[] expcolorid={
            R.id.expcolor_1,R.id.expcolor_2,
            R.id.expcolor_3, R.id.expcolor_4,
            R.id.expcolor_5,R.id.expcolor_6,
            R.id.expcolor_7, R.id.expcolor_8,
    };
    String[] colorStr={"#ee3c5d","#ffc12c","#FFAB4FE1","#FF41DAE2","#FFD94141","#FFE2C534","FF8ACE53","FF3057BF","FF0EB19E"};

    private PieChart_View pieView;//饼图
    //MyHelper myHelper;//数据库

    //饼图数据列表
    private ArrayList<ArrayElement> lists;
    //含有饼图数据列表与说明类别
    ArrayList<ArrayElement> list2=new ArrayList<>();
    //拜托别超过50条查询数据叭
   public  Inquiry []inquiry=new Inquiry[50];//查询

    private void init(){
        for(int i=0;i<8;i++)
        {
            expenditure[i]=(RelativeLayout)findViewById(expid[i]);
            expname[i]=(TextView)findViewById(expnameid[i]);
            expcolor[i]=(TextView)findViewById(expcolorid[i]);
        }

        nDatabaseHelper = new DataBaseHelper(this);
        pieView = (PieChart_View) findViewById(R.id.pie_view);
        lists = new ArrayList<>();
        textView_month.setText(String.valueOf(month)+"月");

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);

        btn_add = findViewById(R.id.btn_add);
        btn_piechart = findViewById(R.id.btn_piechart);
        btn_payment = findViewById(R.id.btn_payment);

        btn_account_expend = findViewById(R.id.btn_account_expend);
        btn_account_income = findViewById(R.id.btn_account_income);
        btn_down=findViewById(R.id.btn_up);
        btn_up=findViewById(R.id.btn_down);
        textView_month=findViewById(R.id.tv_month);
        expnum=findViewById(R.id.tv_expendnum);
        incomum=findViewById(R.id.tv_incomenum);

        //按键监听
        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chart.this, Account.class);
                intent.putExtra("chart",1);
                startActivity(intent);
                //问题待解决
            }
        });

        //收入按键监听
        btn_account_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_account_income.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_account_expend.setBackgroundColor(Color.parseColor("#ececec"));

               //固定三步
                mark=1;
                Func_percent();
                func_btn_out_in(month-1,mark);
            }
        });

        //支出按键监听
        btn_account_expend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                btn_account_expend.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_account_income.setBackgroundColor(Color.parseColor("#ececec"));
                //固定三步
                mark=-1;
                Func_percent();
                func_btn_out_in(month-1,mark);
            }
        });

        btn_payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Chart.this, home_page.class);
                btn_piechart.setBackgroundResource(R.drawable.piechart);
                btn_payment.setBackgroundResource(R.drawable.payment1);
                startActivity(intent);
            }
        });

        btn_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(month<12)
                {
                    month++;
                    textView_month.setText(String.valueOf(month)+"月");
                    //固定三步
                    mark=-1;
                    Func_percent();
                    func_btn_out_in(month-1,mark);
                    expnum.setText(String.valueOf(Inquiry.month_out_sum[month-1]));
                    incomum.setText(String.valueOf(Inquiry.month_income_sum[month-1]));
                }
            }
        });
        btn_down.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               if(month>1)
               {
                   month--;
                   textView_month.setText(String.valueOf(month)+"月");
                   //固定三步
                   mark=-1;
                   Func_percent();
                   func_btn_out_in(month-1,mark);
                   expnum.setText(String.valueOf(Inquiry.month_out_sum[month-1]));
                   incomum.setText(String.valueOf(Inquiry.month_income_sum[month-1]));
               }
            }
        });



        //获取当前时间
        date = new Time();
        date.setToNow();
        month=date.month+1;

        //控件初始化
        init();

        //-----------------------查询开始,查询得到到数据存在一个数据类中
        //游标
        Cursor cursor = nDatabaseHelper.getAllCostData();
        int length=cursor.getCount();
         Inquiry.temp=0;
        if(cursor.getCount()==0){
            Toast.makeText(this,"没有数据",Toast.LENGTH_SHORT).show();
        }else {
            cursor.moveToFirst();
            getInquiry(cursor);
        }
        while (cursor.moveToNext()){
            //获取查询
            getInquiry(cursor);
        }
        //获取百分比
        Func_percent();
        cursor.close();


        //月份代码改OK叭
        func_btn_out_in(date.month,mark);
        expnum.setText(String.valueOf(Inquiry.month_out_sum[month-1]));
        incomum.setText(String.valueOf(Inquiry.month_income_sum[month-1]));

       // pieView.setData(lists);

    }




    //将查询到到数据存入查询数据数组
    private void getInquiry(Cursor cursor)
    {
        inquiry[Inquiry.temp]=new Inquiry();
        inquiry[Inquiry.temp].id=cursor.getInt(0);

        //String costMoney=cursor.getString(9);
        //String costInOut=cursor.getString(8);
        int temp=Integer.parseInt(cursor.getString(8));
        inquiry[Inquiry.temp].num=Math.abs(temp);
        if(temp>=0) inquiry[Inquiry.temp].type=1;
        else inquiry[Inquiry.temp].type=-1;
        inquiry[Inquiry.temp].year=cursor.getInt(2);
        inquiry[Inquiry.temp].month=cursor.getInt(3);
        inquiry[Inquiry.temp].day=cursor.getInt(4);
        inquiry[Inquiry.temp].hour=cursor.getInt(5);
        inquiry[Inquiry.temp].minute=cursor.getInt(6);
        inquiry[Inquiry.temp].second=cursor.getInt(7);
        inquiry[Inquiry.temp].moneyDescribe=cursor.getString(0);
        Inquiry.temp++;
    }
    public void Func_percent()
    {
        int temp=Inquiry.temp;
        //每次重新计算每月的总支出与收入
        for(int i=0;i<12;i++)
        {
            Inquiry.month_out_sum[i]=0;
            Inquiry.month_income_sum[i]=0;
        }
        while (--temp>=0)//不能temp-->=哦，会报错
        {
            //如果是支出
            if(inquiry[temp].type<0) {

                Inquiry.month_out_sum[inquiry[temp].month]+=inquiry[temp].num;
            }
            else
            {

                Inquiry.month_income_sum[inquiry[temp].month]+=inquiry[temp].num;
            }
        }

        int temp2=Inquiry.temp;
        while (--temp2>=0)
        {

            if(inquiry[temp2].type<0) {
                inquiry[temp2].percent=((float)inquiry[temp2].num)/Inquiry.month_out_sum[inquiry[temp2].month];
            }
            else
            {
                inquiry[temp2].percent=((float)inquiry[temp2].num)/Inquiry.month_income_sum[inquiry[temp2].month];
            }
        }


    }

    private void func_btn_out_in(int month,int mark)
    {
        lists.clear();
        int temp=Inquiry.temp;
        int k=0,j=0;
        //把下面的颜色注释全隐
        for(int i=0;i<8;i++)
        {
            expenditure[i].setVisibility(View.GONE);
        }
        while (--temp>=0)//不能temp-->=哦，会报错
        {
            //最多8种颜色
            if(k>=8)k=0;

            //如果是支出
            if(mark<0) {

                if (inquiry[temp].type < 0) {
                    //月份对应
                    if(inquiry[temp].month==month) {


                        //inquiry[temp].moneyDescribe + String.valueOf(inquiry[temp].num
                        //不要文字了
                        float percent1=Float.parseFloat(String.format("%.2f",inquiry[temp].percent*100));
                       /* for(int i=0;i<Inquiry.temp;i++)
                        {
                            if(equals())
                        }*/
                        PieChartBean a= new PieChartBean(Color.parseColor(colorStr[k++]),  percent1, String.valueOf(percent1)+"%");
                        lists.add(new ArrayElement (a,inquiry[temp].moneyDescribe));

                    }
                }
            }
            else {//是收入

                if (inquiry[temp].type > 0) {
                    if (inquiry[temp].month == month) {

                        float percent2=Float.parseFloat(String.format("%.2f",inquiry[temp].percent*100));

                        PieChartBean a2= new PieChartBean(Color.parseColor(colorStr[k++]), percent2 , String.valueOf(percent2)+"%");
                        lists.add(new ArrayElement (a2,inquiry[temp].moneyDescribe));
                    }
                }
            }
        }


        ArrayList<PieChartBean> m=total_Month(lists);
        for(int t=0;t<m.size();t++)
        {
            //总共定义了8种颜色
            if (j < 8) {
                //把下面的颜色注释显示出来
                expenditure[j].setVisibility(View.VISIBLE);
                expname[j].setText(list2.get(t).b);
                expcolor[j].setBackgroundColor(m.get(t).getPieColor());
                j++;
            }
        }


        pieView.startAnimator();
        pieView.setData(m);
    }

    public ArrayList<PieChartBean> total_Month(ArrayList<ArrayElement> list)
    {
        list2.clear();
        ArrayList<PieChartBean> list3=new ArrayList<>();
       for(int i=0;i<list.size();i++)
       {
           int mark=0;
           for(int j=0;j<list2.size();j++)
           {
               if(list2.get(j).b.equals(list.get(i).b))
               {
                   //设置只要小数点后两位
                   float temp_num=list.get(i).a.getPieValue();
                   temp_num+=list2.get(j).a.getPieValue();
                   temp_num=Float.parseFloat(String.format("%.2f",temp_num));
                   list2.get(j).a.setPieValue(temp_num);
                   list2.get(j).a.setPieString(String.valueOf(temp_num)+"%");
                   mark=1;
               }
           }
           if(mark==0)
           {
               list2.add(list.get(i));
           }
       }
        for(int j=0;j<list2.size();j++)
        {
            list3.add(list2.get(j).a);
        }
       return  list3;
    }
}
