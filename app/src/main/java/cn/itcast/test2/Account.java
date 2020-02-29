package cn.itcast.test2;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.text.format.Time;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class Account extends AppCompatActivity {

    private Button btn_return;
    private Button btn_classify;
    private Button btn_save;
    private Button btn_account_expend;
    private Button btn_account_income;
    private EditText et_money;
    private Time date;
    private boolean inorout = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        btn_return = findViewById(R.id.btn_return);
        btn_classify = findViewById(R.id.btn_classify);
        btn_save = findViewById(R.id.btn_save);
        et_money = findViewById(R.id.et_money);
        btn_account_expend = findViewById(R.id.btn_account_expend);
        btn_account_income = findViewById(R.id.btn_account_income);

        if(checkIntentAvailable(getIntent()))
        {
            if(getIntent().getIntExtra("chart",0)==3)
            {
                et_money.setText(getIntent().getStringExtra("money"));
                btn_classify.setText(getIntent().getStringExtra("costTile"));
                inorout = getIntent().getBooleanExtra("costInOut",true);
            }
        }



        //按键监听
        btn_return.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, home_page.class);
                startActivity(intent);
            }
        });

        btn_classify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Account.this, label.class);
                startActivityForResult(intent,2);
                //startActivity(intent);
            }
        });
        btn_account_income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inorout = false;
                btn_account_income.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_account_expend.setBackgroundColor(Color.parseColor("#ececec"));
            }
        });

        btn_account_expend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inorout = true;
                btn_account_expend.setBackgroundColor(Color.parseColor("#ffffff"));
                btn_account_income.setBackgroundColor(Color.parseColor("#ececec"));
            }
        });

        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String a= et_money.getText().toString();
                int i=Integer.parseInt(et_money.getText().toString());
                int j=getIntent().getIntExtra("chart",0);
                if(j==0)
                {
                    if(Integer.parseInt(et_money.getText().toString())==0||et_money.getText().equals(""))
                    {
                        Toast.makeText(Account.this, "请输入合法数值", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Intent intent = new Intent(Account.this, home_page.class);
                        intent.putExtra("money",et_money.getText().toString());
                        //Toast.makeText(Account.this,btn_classify.getText().toString(),Toast.LENGTH_SHORT).show();
                        intent.putExtra("classify",btn_classify.getText().toString());//Title
                        intent.putExtra("inorout",inorout);
                        intent.putExtra("chart",0);
                        date = new Time();
                        date.setToNow();

                        intent.putExtra("Date",""+date.year +""+ date.month +""+ date.monthDay+"" + date.hour+"" + date.minute+"" + date.second);
                        intent.putExtra("Year",""+date.year);
                        intent.putExtra("Month",""+date.month+"");
                        intent.putExtra("Day",""+date.monthDay+"");
                        intent.putExtra("Hour",""+date.hour+"");
                        intent.putExtra("Minute",""+date.minute+"");
                        intent.putExtra("Second",""+date.second+"");

                        setResult(RESULT_OK,intent);

                        finish();
                        //出现了问题
                        //startActivity(intent);
                        //startActivityForResult(intent,1);

                    }
                }
                else if(j==1)
                {
                    Intent intent = new Intent(Account.this, home_page.class);
                    intent.putExtra("money",et_money.getText().toString());
                    //Toast.makeText(Account.this,btn_classify.getText().toString(),Toast.LENGTH_SHORT).show();
                    intent.putExtra("classify",btn_classify.getText().toString());//Title
                    intent.putExtra("inorout",inorout);
                    intent.putExtra("chart",1);
                    date = new Time();
                    date.setToNow();

                    intent.putExtra("Date",""+date.year +""+ date.month +""+ date.monthDay+"" + date.hour+"" + date.minute+"" + date.second);
                    intent.putExtra("Year",""+date.year);
                    intent.putExtra("Month",""+date.month+"");
                    intent.putExtra("Day",""+date.monthDay+"");
                    intent.putExtra("Hour",""+date.hour+"");
                    intent.putExtra("Minute",""+date.minute+"");
                    intent.putExtra("Second",""+date.second+"");
                    startActivity(intent);
                }
                else if(j==3)
                {
                    //更新数据
                    Intent intent = new Intent(Account.this, home_page.class);
                    intent.putExtra("money",et_money.getText().toString());
                    intent.putExtra("classify",btn_classify.getText().toString());//Title
                    intent.putExtra("inorout",inorout);
                    intent.putExtra("chart",2);
                    intent.putExtra("position",getIntent().getIntExtra("position",0));
                    startActivity(intent);
                }
            }
        });
    }


    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case 2:
                if(resultCode == RESULT_OK){
                    //Toast.makeText(Account.this,data.getStringExtra("classify"),Toast.LENGTH_SHORT).show();
                    btn_classify.setText(data.getStringExtra("classify"));
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
}
