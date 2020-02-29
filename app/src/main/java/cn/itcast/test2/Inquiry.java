package cn.itcast.test2;

public class Inquiry {
    //查表得到的
    int id;
    int num;
    String moneyDescribe;
    int type;
    int year;
    int month;
    int day;
    int hour;
    int minute;
    int second;
    //计算出来的
    float percent;//收支百分比
    //float percent_out;//支出百分比
    static int[] month_income_sum=new int[12];
    static int[] month_out_sum=new int[12];
    //static int income_sum;
    //static int out_sum;
    static int temp=0;


}
