package cn.itcast.test2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

public class ClassifyBaseHelper extends SQLiteOpenHelper {

    public ClassifyBaseHelper(Context context){
        super(context,"mClassify",null,1);
    }

    public void onCreate(SQLiteDatabase db)
    {
        //容易出错
        //创建表
        db.execSQL("create table if not exists classify(" +
                "classifyName varchar primary key," +
                "costInOut varchar)");

    }

    public void  insertCost(CostClassify costClassify)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("classifyName",costClassify.classifyName);
        cv.put("costInOut",costClassify.costInOut);
        database.insert("classify",null,cv);
    }


    public Cursor getAllCostData()
    {
        SQLiteDatabase database = getWritableDatabase();
        return database.query("classify",null,null,null,null,null,"classifyName " + "ASC");
    }

    public  void onUpgrade(SQLiteDatabase db,int oldVision,int newVision)
    {
    }

    public  void upgradeCost(CostClassify costClassify,String[] msg)
    {
        SQLiteDatabase database = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("classifyName",costClassify.classifyName);
        cv.put("costInOut",costClassify.costInOut);
        database.update("classify",cv,"classifyName=?",msg);
    }

    public void deleteData(String[] msg)
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("classify","classifyName=?",msg);
    }

    public void deleteAllData()
    {
        SQLiteDatabase database = getWritableDatabase();
        database.delete("classify",null,null);
    }
}
