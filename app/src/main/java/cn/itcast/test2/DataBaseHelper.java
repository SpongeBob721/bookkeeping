package cn.itcast.test2;

import android.app.Person;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class DataBaseHelper extends SQLiteOpenHelper {

    public long idd = 0;

    public DataBaseHelper(Context context){
        super(context,"9",null,1);
    }

    private int i=0;

    public void onCreate(SQLiteDatabase db)
    {
        //容易出错
        db.execSQL("create table costlist(" +
                //"id integer primary key," +
                "costTile varchar," +
                "costDate varchar primary key," +
                "costYear varchar," +
                "costMonth varchar," +
                "costDay varchar," +
                "costHour varchar," +
                "costMinute varchar," +
                "costSecond varchar," +
                "costMoney varchar," +
                "costInOut varchar)");
    }

    public void  insertCost(CostBean costBean)
    {
        //这里ID出了问题
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        //cv.put("id",i++);
        cv.put("costTile",costBean.costTile);
        cv.put("costDate",costBean.costDate);
        cv.put("costYear",costBean.costYear);
        cv.put("costMonth",costBean.costMonth);
        cv.put("costDay",costBean.costDay);
        cv.put("costHour",costBean.costHour);
        cv.put("costMinute",costBean.costMinute);
        cv.put("costSecond",costBean.costSecond);
        cv.put("costMoney",costBean.costMoney);
        cv.put("costInOut",costBean.costInOut);
        idd = database.insert("costlist",null,cv);
    }


    public Cursor getAllCostData()
    {
        SQLiteDatabase database = getWritableDatabase();
        return database.query("costlist",null,null,null,null,null,"costDate " + "ASC");
    }

    public  void onUpgrade(SQLiteDatabase db,int oldVersion,int newVersion)
    {

    }

    public void upgradeCost(CostBean costBean,String[] msg)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("costTile",costBean.costTile);
        cv.put("costDate",costBean.costDate);
        cv.put("costYear",costBean.costYear);
        cv.put("costMonth",costBean.costMonth);
        cv.put("costDay",costBean.costDay);
        cv.put("costHour",costBean.costHour);
        cv.put("costMinute",costBean.costMinute);
        cv.put("costSecond",costBean.costSecond);
        cv.put("costMoney",costBean.costMoney);
        cv.put("costInOut",costBean.costInOut);
        database.update("costlist",cv,"costDate=?",msg);
    }


    public void deleteCost()
    {

    }

    public void deleteAllData()
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("costlist",null,null);
    }

}
