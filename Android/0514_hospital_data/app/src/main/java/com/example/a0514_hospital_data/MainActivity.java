package com.example.a0514_hospital_data;

import android.content.res.AssetFileDescriptor;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.text.DecimalFormat;
import java.util.ArrayList;

import org.tensorflow.lite.Interpreter;

public class MainActivity extends AppCompatActivity {

    EditText Age;
    EditText BMI;
    EditText Temperature;
    EditText Pulse;
    EditText Respiration;

    EditText Wbc;
    EditText Hemoglobin;
    EditText Hct;
    EditText Platelets;
    EditText BUN;
    EditText CREATININE;

    Spinner spn_occupation;
    Spinner spn_OP_CODE;
    Spinner spn_Post_diag;
    Spinner spn_Pre_diag;
    Spinner spn_WHERE;

    String spn_occupation_Select;
    String spn_OP_CODE_Select;
    String spn_Post_diag_Select;
    String spn_Pre_diag_Select;
    String spn_Where_Select;

    Button inferButton;
    TextView outputNumberA;
    TextView outputNumberB;
    TextView outputNumberC;
    TextView outputNumberD;

    Interpreter tflite;

    float Age_mean = (float) 51.612565; float Age_std = (float) 20.526918;
    float BMI_mean = (float) 24.790890; float BMI_std = (float) 4.303283;
    float Temperature_mean = (float) 36.460209; float Temperature_std = (float) 0.503135;
    float Pulse_mean = (float) 81.638743; float Pulse_std = (float) 13.235533;
    float Respiration_mean = (float) 16.774869; float Respiration_std = (float) 2.228192;

    float OP_CODE_mean = (float) 7850.015707; float OP_CODE_std = (float) 3540.000314;
    float wBC_mean = (float) 8.542408; float wBC_std = (float) 3.459226;
    float HEMOGLOBIN_mean = (float) 13.169634; float HEMOGLOBIN_std = (float) 1.917460;
    float HCT_mean = (float) 40.267016; float HCT_std = (float) 4.966711;
    float PLATELETS_mean = (float) 271.104712; float PLATELETS_std = (float) 62.967824;

    float BUN_mean = (float) 14.851309; float BUN_std = (float) 4.081200;
    float CREATININE_mean = (float) 0.756545; float CREATININE_std = (float) 0.189032;

    float occupation_A_mean = (float) 0.230366; float occupation_A_std = (float) 0.422174;
    float occupation_B_mean = (float) 0.151832; float occupation_B_std = (float) 0.359802;
    float occupation_C_mean = (float) 0.204188; float occupation_C_std = (float) 0.404167;
    float occupation_D_mean = (float) 0.167539; float occupation_D_std = (float) 0.374438;
    float occupation_E_mean = (float) 0.073298; float occupation_E_std = (float) 0.261311;
    float occupation_F_mean = (float) 0.073298; float occupation_F_std = (float) 0.261311;
    float occupation_G_mean = (float) 0.031414; float occupation_G_std = (float) 0.174891;
    float occupation_H_mean = (float) 0.068063; float occupation_H_std = (float) 0.252516;

    float WHERE_A_mean = (float) 0.109948; float WHERE_A_std = (float) 0.313647;
    float WHERE_B_mean = (float) 0.068063; float WHERE_B_std = (float) 0.252516;

    float WHERE_C_mean = (float) 0.115183; float WHERE_C_std = (float) 0.320082;
    float WHERE_D_mean = (float) 0.225131; float WHERE_D_std = (float) 0.418766;
    float WHERE_E_mean = (float) 0.089005; float WHERE_E_std = (float) 0.285500;
    float WHERE_F_mean = (float) 0.219895; float WHERE_F_std = (float) 0.415264;
    float WHERE_G_mean = (float) 0.052356; float WHERE_G_std = (float) 0.223329;

    float WHERE_H_mean = (float) 0.020942; float WHERE_H_std = (float) 0.143568;
    float WHERE_I_mean = (float) 0.047120; float WHERE_I_std = (float) 0.212453;
    float WHERE_J_mean = (float) 0.052356; float WHERE_J_std = (float) 0.223329;

    float PRE_DIAG_A_mean = (float) 0.130890; float PRE_DIAG_A_std = (float) 0.338166;
    float PRE_DIAG_B_mean = (float) 0.157068; float PRE_DIAG_B_std = (float) 0.364821;
    float PRE_DIAG_C_mean = (float) 0.225131; float PRE_DIAG_C_std = (float) 0.418766;

    float PRE_DIAG_D_mean = (float) 0.376963; float PRE_DIAG_D_std = (float) 0.485899;
    float PRE_DIAG_E_mean = (float) 0.109948; float PRE_DIAG_E_std = (float) 0.313647;
    float POST_DIAG_A_mean = (float) 0.125654; float POST_DIAG_A_std = (float) 0.332331;
    float POST_DIAG_B_mean = (float) 0.157068; float POST_DIAG_B_std = (float) 0.364821;
    float POST_DIAG_C_mean = (float) 0.235602; float POST_DIAG_C_std = (float) 0.425490;

    float POST_DIAG_D_mean = (float) 0.371728; float POST_DIAG_D_std = (float) 0.484536;
    float POST_DIAG_E_mean = (float) 0.109948; float POST_DIAG_E_std = (float) 0.313647;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Age = (EditText) findViewById(R.id.Age);
        BMI = (EditText) findViewById(R.id.BMI);
        Temperature = (EditText) findViewById(R.id.Temperature);
        Pulse = (EditText) findViewById(R.id.Pulse);
        Respiration = (EditText) findViewById(R.id.Respiration);

        Wbc = (EditText) findViewById(R.id.Wbc);
        Hemoglobin = (EditText) findViewById(R.id.Hemoglobin);
        Hct = (EditText) findViewById(R.id.Hct);
        Platelets = (EditText) findViewById(R.id.Platelets);
        BUN = (EditText) findViewById(R.id.Bun);
        CREATININE = (EditText) findViewById(R.id.Creatinine);

        spn_occupation = (Spinner) findViewById(R.id.Occupation);
        spn_OP_CODE = (Spinner) findViewById(R.id.Op_code);
        spn_Post_diag = (Spinner) findViewById(R.id.Post_diag);
        spn_Pre_diag = (Spinner) findViewById(R.id.Pre_diag);
        spn_WHERE = (Spinner) findViewById(R.id.Where);

        inferButton = (Button) findViewById(R.id.inferButton);

        outputNumberA = (TextView) findViewById(R.id.outputNumberA);
        outputNumberB = (TextView) findViewById(R.id.outputNumberB);
        outputNumberC = (TextView) findViewById(R.id.outputNumberC);
        outputNumberD = (TextView) findViewById(R.id.outputNumberD);


        ArrayList<occupation_spinner> occupation_List = new ArrayList<occupation_spinner>();
            occupation_List.add(new occupation_spinner("select_occupation","請選擇病患職業"));
            occupation_List.add(new occupation_spinner("1,0,0,0,0,0,0,0","無"));
            occupation_List.add(new occupation_spinner("0,1,0,0,0,0,0,0","退休"));
            occupation_List.add(new occupation_spinner("0,0,1,0,0,0,0,0","其他服務業"));
            occupation_List.add(new occupation_spinner("0,0,0,1,0,0,0,0","營建工程業"));
            occupation_List.add(new occupation_spinner("0,0,0,0,1,0,0,0","教育業"));
            occupation_List.add(new occupation_spinner("0,0,0,0,0,1,0,0","批發及零售業"));
            occupation_List.add(new occupation_spinner("0,0,0,0,0,0,1,0","醫療保健集社會工作服務業"));
            occupation_List.add(new occupation_spinner("0,0,0,0,0,0,0,1","其他職業"));


        ArrayList<OP_CODE_sipnner> OP_CODE_List = new ArrayList<OP_CODE_sipnner>();
            OP_CODE_List.add(new OP_CODE_sipnner("select_OP_CODE","請選擇手術代碼"));
            OP_CODE_List.add(new OP_CODE_sipnner("420","48001C(淺部創傷處理 － 傷口長 小於5公分者)"));
            OP_CODE_List.add(new OP_CODE_sipnner("3043","48005C(深部複雜創傷處理 － 傷口長 5-10 公分者)"));
            OP_CODE_List.add(new OP_CODE_sipnner("4792","48006C(深部複雜創傷處理 － 傷口長 10 公分以上者)"));
            OP_CODE_List.add(new OP_CODE_sipnner("2445","48033C(深部複雜臉部創傷處理– 小5公分以內)"));
            OP_CODE_List.add(new OP_CODE_sipnner("3588","62016B(多層皮膚移植 － 每增加100平方公分)"));

            OP_CODE_List.add(new OP_CODE_sipnner("4018","64002B(骨或軟骨移植術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("5852","64003C(骨髓炎之死骨切除術或蝶形手術及擴創術(包含指骨、掌骨、蹠骨）)"));
            OP_CODE_List.add(new OP_CODE_sipnner("5604","64015C(鎖骨骨折開放復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("6057","64023B(四肢切斷術 － 小腿、上臂、前臂)"));
            OP_CODE_List.add(new OP_CODE_sipnner("11000","64028C(股骨幹骨折開放性復位術)"));

            OP_CODE_List.add(new OP_CODE_sipnner("12000","64029B(股骨頸骨折開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("10000","64031C(脛骨骨折開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("4938","64032B(橈骨、尺骨骨折開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("4480","64034B(膝蓋骨骨折開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("6720","64035C(腕、跗、掌、蹠骨骨折開放性復位術)"));

            OP_CODE_List.add(new OP_CODE_sipnner("2845","64044C(前臂骨骨折徒手復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("6373","64053B(急性化膿性關節炎切開術 － 肩關節、肘關節、腕關節、膝關節、踝關節)"));
            OP_CODE_List.add(new OP_CODE_sipnner("5834","64065B(肩關節脫位開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("6439","64067C(膝關節脫位開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("4331","64069C(踝關節脫位開放性復位術)"));

            OP_CODE_List.add(new OP_CODE_sipnner("11154","64108B(下顎骨骨折開放性復位 ( 簡單 ))"));
            OP_CODE_List.add(new OP_CODE_sipnner("6491","64117C(跟腱斷裂縫合術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("7070","64122B(肩旋轉袖破裂修補術 － 大破裂)"));
            OP_CODE_List.add(new OP_CODE_sipnner("4940","64128B(足踝韌帶修補術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("5275","64133C(大腳趾外翻（ 截骨術 ）)"));

            OP_CODE_List.add(new OP_CODE_sipnner("10560","64161B(骨盆骨折開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("19608","64162B(全股關節置換術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("19608","64164B(全膝關節置換術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("9035","64165B(全肘關節置換術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("11500","64170B(部份關節置換術併整型術 － 只置換髖臼或股骨或半股關節或半肩關節)"));

            OP_CODE_List.add(new OP_CODE_sipnner("7920","64175B(踝關節整形術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("9040","64180B(膝關節固定術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("8200","64183B(踝關節固定術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("11830","64187B(十字韌帶重建術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("7060","64188B(十字韌帶修補術)"));

            OP_CODE_List.add(new OP_CODE_sipnner("5236","64195C(肌腱或韌帶完全切斷修補)"));
            OP_CODE_List.add(new OP_CODE_sipnner("8000","64239B(開放性或閉鎖性肱骨粗隆或骨幹或踝部骨折，開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("4827","64242B(橈骨頭切除術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("8000","64244B(關節鏡手術 － 關節鏡下關節面磨平成形術，打洞，游離體或骨軟骨碎片取出手術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("4182","64245C(骨內固定物拔除術 － 骨盆，髖骨，肱骨，股骨，尺骨，橈骨，脛骨)"));

            OP_CODE_List.add(new OP_CODE_sipnner("3589","64247C(骨內固定物拔除術 － 其他部位)"));
            OP_CODE_List.add(new OP_CODE_sipnner("11500","64259B(半肩關節成形術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("7640","64263B(膝關節半月軟骨修補術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("15300","64266B(脊椎骨全部切除術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("6000","64267C(舟狀骨骨折開放性復位術)"));

            OP_CODE_List.add(new OP_CODE_sipnner("5691","64272C(腓外踝或脛內踝單一骨折開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("6376","64273C(足踝關節內、外或後踝之雙踝或三踝骨折開放性復位術)"));
            OP_CODE_List.add(new OP_CODE_sipnner("18992","83044B(脊椎融合術－前融合 2.有固定物(1)≦四節)"));
            OP_CODE_List.add(new OP_CODE_sipnner("19406","83046B(脊椎融合術－後融合 2.有固定物(1)≦六節)"));

        ArrayList<Pre_diag_spinner> Pre_Diag_List = new ArrayList<Pre_diag_spinner>();
            Pre_Diag_List.add(new Pre_diag_spinner("select_pre_diag","請選擇手術前診斷"));
            Pre_Diag_List.add(new Pre_diag_spinner("1,0,0,0,0","前臂骨折(S52)"));
            Pre_Diag_List.add(new Pre_diag_spinner("0,1,0,0,0","股骨骨折(S72)"));
            Pre_Diag_List.add(new Pre_diag_spinner("0,0,1,0,0","小腿骨折(包含踝)(S82)"));
            Pre_Diag_List.add(new Pre_diag_spinner("0,0,0,1,0","其他損傷(S10-S51,S53-S71,S73-S81,S83-T14,T79)"));
            Pre_Diag_List.add(new Pre_diag_spinner("0,0,0,0,1","其於ICD-10代碼"));


         ArrayList<Post_diag_spinner> Post_Diag_List = new ArrayList<Post_diag_spinner>();
            Post_Diag_List.add(new Post_diag_spinner("select_post_diag","請選擇手術後診斷"));
            Post_Diag_List.add(new Post_diag_spinner("1,0,0,0,0","前臂骨折(S52)"));
            Post_Diag_List.add(new Post_diag_spinner("0,1,0,0,0","股骨骨折(S72)"));
            Post_Diag_List.add(new Post_diag_spinner("0,0,1,0,0","小腿骨折(包含踝)(S82)"));
            Post_Diag_List.add(new Post_diag_spinner("0,0,0,1,0","其他損傷(S10-S51,S53-S71,S73-S81,S83-T14,T79)"));
            Post_Diag_List.add(new Post_diag_spinner("0,0,0,0,1","其於ICD-10代碼"));

         ArrayList<Where_spinner> Where_List = new ArrayList<Where_spinner>();
            Where_List.add(new Where_spinner("select_Where","請選擇病患骨折位置"));
            Where_List.add(new Where_spinner("1,0,0,0,0,0,0,0,0,0","肱骨"));
            Where_List.add(new Where_spinner("0,1,0,0,0,0,0,0,0,0","鎖骨"));
            Where_List.add(new Where_spinner("0,0,1,0,0,0,0,0,0,0","橈骨 或 尺骨"));
            Where_List.add(new Where_spinner("0,0,0,1,0,0,0,0,0,0","股骨"));
            Where_List.add(new Where_spinner("0,0,0,0,1,0,0,0,0,0","髕骨"));

            Where_List.add(new Where_spinner("0,0,0,0,0,1,0,0,0,0","脛骨 或 腓骨"));
            Where_List.add(new Where_spinner("0,0,0,0,0,0,1,0,0,0","踝骨"));
            Where_List.add(new Where_spinner("0,0,0,0,0,0,0,1,0,0","脊椎 或 月狀骨"));
            Where_List.add(new Where_spinner("0,0,0,0,0,0,0,0,1,0","骨盆 或 髖臼"));
            Where_List.add(new Where_spinner("0,0,0,0,0,0,0,0,0,1","跟骨 或 蹠骨"));

        ArrayAdapter<occupation_spinner> occupation_Adapter = new ArrayAdapter<occupation_spinner>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,occupation_List);
        ArrayAdapter<OP_CODE_sipnner> OP_CODE_Adapter = new ArrayAdapter<OP_CODE_sipnner>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,OP_CODE_List);
        ArrayAdapter<Post_diag_spinner> post_diag_Adapter = new ArrayAdapter<Post_diag_spinner>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,Post_Diag_List);
        ArrayAdapter<Pre_diag_spinner> pre_diag_Adapter = new ArrayAdapter<Pre_diag_spinner>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,Pre_Diag_List);
        ArrayAdapter<Where_spinner> Where_Adapter = new ArrayAdapter<Where_spinner>(MainActivity.this,android.R.layout.simple_spinner_dropdown_item,Where_List);

        spn_occupation.setAdapter(occupation_Adapter);
        spn_OP_CODE.setAdapter(OP_CODE_Adapter);
        spn_Post_diag.setAdapter(post_diag_Adapter);
        spn_Pre_diag.setAdapter(pre_diag_Adapter);
        spn_WHERE.setAdapter(Where_Adapter);

        spn_occupation.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                occupation_spinner occupation_choose = (occupation_spinner) parent.getItemAtPosition(position);
                spn_occupation_Select = occupation_choose.occupation_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_OP_CODE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                OP_CODE_sipnner  OP_CODE_choose = (OP_CODE_sipnner) parent.getItemAtPosition(position);
                spn_OP_CODE_Select = OP_CODE_choose.OP_CODE_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_Post_diag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Post_diag_spinner post_diag_choose = (Post_diag_spinner) parent.getItemAtPosition(position);
                spn_Post_diag_Select = post_diag_choose.Post_diag_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_Pre_diag.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Pre_diag_spinner pre_diag_choose = (Pre_diag_spinner) parent.getItemAtPosition(position);
                spn_Pre_diag_Select = pre_diag_choose.Pre_diag_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spn_WHERE.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Where_spinner where_choose = (Where_spinner) parent.getItemAtPosition(position);
                spn_Where_Select = where_choose.Where_id;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        try {
            tflite = new Interpreter(loadModelFile());
        }catch(Exception ex){
            ex.printStackTrace();
        }

        inferButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] occupation_array = spn_occupation_Select.split(",");
                String[] OP_CODE_array = spn_OP_CODE_Select.split(",");
                String[] Post_diag_array = spn_Post_diag_Select.split(",");
                String[] Pre_diag_array = spn_Pre_diag_Select.split(",");
                String[] Where_array = spn_Where_Select.split(",");

                if( Age.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患年齡欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( BMI.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患BMI欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Temperature.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患體溫欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Pulse.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患脈搏欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Respiration.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患呼吸欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Wbc.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患白血球欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Hemoglobin.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患血紅素檢查欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Hct.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患血球比容值欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Platelets.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患血小板欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( BUN.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "病患血液尿素氮欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( CREATININE.getText().toString().matches("") ) {
                    Toast.makeText(MainActivity.this, "肌酐欄位未填入", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }



                if( occupation_array[0] == "select_occupation"){
                    Toast.makeText(MainActivity.this,"未選擇病患職業",Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Where_array[0] == "select_Where"){
                    Toast.makeText(MainActivity.this,"未選擇病患骨折位置",Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( OP_CODE_array[0] == "select_OP_CODE"){
                    Toast.makeText(MainActivity.this,"未選擇手術代碼",Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Pre_diag_array[0] == "select_pre_diag"){
                    Toast.makeText(MainActivity.this,"未選擇手術前診斷",Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }
                if( Post_diag_array[0] == "select_post_diag") {
                    Toast.makeText(MainActivity.this, "未選擇手術後診斷", Toast.LENGTH_LONG).show();
                    outputNumberA.setText("");
                    outputNumberB.setText("");
                    outputNumberC.setText("");
                    outputNumberD.setText("");
                }

                if( (Age.getText().toString().matches("") == false) && (BMI.getText().toString().matches("") == false) && (Temperature.getText().toString().matches("") == false) &&
                        (Pulse.getText().toString().matches("") == false) && (Respiration.getText().toString().matches("") == false) && (Wbc.getText().toString().matches("") == false) &&
                        (Hemoglobin.getText().toString().matches("") == false) && (Hct.getText().toString().matches("") == false) && (Platelets.getText().toString().matches("") == false) &&
                        (BUN.getText().toString().matches("") == false) && (CREATININE.getText().toString().matches("") == false) && (occupation_array[0] != "select_occupation") &&
                        (Where_array[0] != "select_Where") && (OP_CODE_array[0] != "select_OP_CODE") && (Pre_diag_array[0] != "select_pre_diag") && (Post_diag_array[0] != "select_post_diag")  ){
                    float[] inputVal = new float[40];

                    float Age_scale =  ( Float.valueOf( Age.getText().toString() ) - Age_mean ) / Age_std ;
                    float BMI_scale = ( Float.valueOf( BMI.getText().toString() ) - BMI_mean ) / BMI_std;
                    float Temperature_scale = ( Float.valueOf( Temperature.getText().toString() )  - Temperature_mean ) / Temperature_std ;
                    float Pulse_scale = ( Float.valueOf( Pulse.getText().toString() ) - Pulse_mean ) / Pulse_std;
                    float Respiration_scale = ( Float.valueOf( Respiration.getText().toString() ) - Respiration_mean ) / Respiration_std ;

                    float OP_CODE_scale = ( Float.valueOf( OP_CODE_array[0]) - OP_CODE_mean ) / OP_CODE_std;
                    float wBC_scale = ( Float.valueOf( Wbc.getText().toString() ) - wBC_mean ) / wBC_std ;
                    float HEMOGLOBIN_scale = ( Float.valueOf( Hemoglobin.getText().toString()) - HEMOGLOBIN_mean ) / HEMOGLOBIN_std;
                    float HCT_scale = ( Float.valueOf( Hct.getText().toString() ) - HCT_mean ) / HCT_std ;
                    float PLATELETS_scale = ( Float.valueOf( Platelets.getText().toString()) - PLATELETS_mean ) / PLATELETS_std;

                    float BUN_scale = ( Float.valueOf( BUN.getText().toString() ) - BUN_mean ) / BUN_std ;
                    float CREATININE_scale = ( Float.valueOf( CREATININE.getText().toString()) - CREATININE_mean ) / CREATININE_std;

                    float occupation_A_scale =  ( Float.valueOf(occupation_array[0]) - occupation_A_mean ) / occupation_A_std;
                    float occupation_B_scale =  ( Float.valueOf(occupation_array[1]) - occupation_B_mean ) / occupation_B_std;
                    float occupation_C_scale =  ( Float.valueOf(occupation_array[2]) - occupation_C_mean ) / occupation_C_std;
                    float occupation_D_scale =  ( Float.valueOf(occupation_array[3]) - occupation_D_mean ) / occupation_D_std;
                    float occupation_E_scale =  ( Float.valueOf(occupation_array[4]) - occupation_E_mean ) / occupation_E_std;
                    float occupation_F_scale =  ( Float.valueOf(occupation_array[5]) - occupation_F_mean ) / occupation_F_std;
                    float occupation_G_scale =  ( Float.valueOf(occupation_array[6]) - occupation_G_mean ) / occupation_G_std;
                    float occupation_H_scale =  ( Float.valueOf(occupation_array[7]) - occupation_H_mean ) / occupation_H_std;

                    float WHERE_A_scale = ( Float.valueOf(Where_array[0]) -WHERE_A_mean ) / WHERE_A_std ;
                    float WHERE_B_scale = ( Float.valueOf(Where_array[1]) -WHERE_B_mean ) / WHERE_B_std ;
                    float WHERE_C_scale = ( Float.valueOf(Where_array[2]) -WHERE_C_mean ) / WHERE_C_std ;
                    float WHERE_D_scale = ( Float.valueOf(Where_array[3]) -WHERE_D_mean ) / WHERE_D_std ;
                    float WHERE_E_scale = ( Float.valueOf(Where_array[4]) -WHERE_E_mean ) / WHERE_E_std ;
                    float WHERE_F_scale = ( Float.valueOf(Where_array[5]) -WHERE_F_mean ) / WHERE_F_std ;
                    float WHERE_G_scale = ( Float.valueOf(Where_array[6]) -WHERE_G_mean ) / WHERE_G_std ;
                    float WHERE_H_scale = ( Float.valueOf(Where_array[7]) -WHERE_H_mean ) / WHERE_H_std ;
                    float WHERE_I_scale = ( Float.valueOf(Where_array[8]) -WHERE_I_mean ) / WHERE_I_std ;
                    float WHERE_J_scale = ( Float.valueOf(Where_array[9]) -WHERE_J_mean ) / WHERE_J_std ;

                    float PER_DIAG_A_scale = ( Float.valueOf(Pre_diag_array[0]) - PRE_DIAG_A_mean ) / PRE_DIAG_A_std ;
                    float PER_DIAG_B_scale = ( Float.valueOf(Pre_diag_array[1]) - PRE_DIAG_B_mean ) / PRE_DIAG_B_std ;
                    float PER_DIAG_C_scale = ( Float.valueOf(Pre_diag_array[2]) - PRE_DIAG_C_mean ) / PRE_DIAG_C_std ;
                    float PER_DIAG_D_scale = ( Float.valueOf(Pre_diag_array[3]) - PRE_DIAG_D_mean ) / PRE_DIAG_D_std ;
                    float PER_DIAG_E_scale = ( Float.valueOf(Pre_diag_array[4]) - PRE_DIAG_E_mean ) / PRE_DIAG_E_std ;

                    float POST_DIAG_A_scale = ( Float.valueOf(Pre_diag_array[0]) - POST_DIAG_A_mean ) / POST_DIAG_A_std;
                    float POST_DIAG_B_scale = ( Float.valueOf(Pre_diag_array[1]) - POST_DIAG_B_mean ) / POST_DIAG_B_std;
                    float POST_DIAG_C_scale = ( Float.valueOf(Pre_diag_array[2]) - POST_DIAG_C_mean ) / POST_DIAG_C_std;
                    float POST_DIAG_D_scale = ( Float.valueOf(Pre_diag_array[3]) - POST_DIAG_D_mean ) / POST_DIAG_D_std;
                    float POST_DIAG_E_scale = ( Float.valueOf(Pre_diag_array[4]) - POST_DIAG_E_mean ) / POST_DIAG_E_std;

                    inputVal[0] = Age_scale;
                    inputVal[1] = BMI_scale;
                    inputVal[2] = Temperature_scale;
                    inputVal[3] = Pulse_scale;
                    inputVal[4] = Respiration_scale;
                    inputVal[5] = OP_CODE_scale;
                    inputVal[6] = wBC_scale;
                    inputVal[7] = HEMOGLOBIN_scale;
                    inputVal[8] = HCT_scale;
                    inputVal[9] = PLATELETS_scale;
                    inputVal[10] = BUN_scale;
                    inputVal[11] = CREATININE_scale;
                    inputVal[12] = occupation_A_scale;
                    inputVal[13] = occupation_B_scale;
                    inputVal[14] = occupation_C_scale;

                    inputVal[15] = occupation_D_scale;
                    inputVal[16] = occupation_E_scale;
                    inputVal[17] = occupation_F_scale;
                    inputVal[18] = occupation_G_scale;
                    inputVal[19] = occupation_H_scale;
                    inputVal[20] = WHERE_A_scale;
                    inputVal[21] = WHERE_B_scale;
                    inputVal[22] = WHERE_C_scale;
                    inputVal[23] = WHERE_D_scale;
                    inputVal[24] = WHERE_E_scale;
                    inputVal[25] = WHERE_F_scale;
                    inputVal[26] = WHERE_G_scale;
                    inputVal[27] = WHERE_H_scale;
                    inputVal[28] = WHERE_I_scale;
                    inputVal[29] = WHERE_J_scale;

                    inputVal[30] = PER_DIAG_A_scale;
                    inputVal[31] = PER_DIAG_B_scale;
                    inputVal[32] = PER_DIAG_C_scale;
                    inputVal[33] = PER_DIAG_D_scale;
                    inputVal[34] = PER_DIAG_E_scale;
                    inputVal[35] = POST_DIAG_A_scale;
                    inputVal[36] = POST_DIAG_B_scale;
                    inputVal[37] = POST_DIAG_C_scale;
                    inputVal[38] = POST_DIAG_D_scale;
                    inputVal[39] = POST_DIAG_E_scale;

                    float[][] outputVal = new float[1][4];


                    tflite.run(inputVal, outputVal);

                    float inferredValue_A = outputVal[0][0] ;
                    float inferredValue_B = outputVal[0][1] ;
                    float inferredValue_C = outputVal[0][2] ;
                    float inferredValue_D = outputVal[0][3] ;

                    DecimalFormat df = new DecimalFormat("##.0");
                    float new_inferredValue_A = Float.valueOf(df.format(inferredValue_A * 100));
                    float new_inferredValue_B = Float.valueOf(df.format(inferredValue_B * 100));
                    float new_inferredValue_C = Float.valueOf(df.format(inferredValue_C * 100));
                    float new_inferredValue_D = Float.valueOf(df.format(inferredValue_D * 100));

                    if( ( new_inferredValue_A >= new_inferredValue_B ) && ( new_inferredValue_A >= new_inferredValue_C ) && ( new_inferredValue_A >= new_inferredValue_D ) ) {
                        outputNumberA.setText(Float.toString(new_inferredValue_A) + "%");
                        outputNumberA.setTextColor( Color.parseColor("#5555FF") );
                        outputNumberB.setText(Float.toString(new_inferredValue_B) + "%");
                        outputNumberB.setTextColor( Color.parseColor("#000000") );
                        outputNumberC.setText(Float.toString(new_inferredValue_C) + "%");
                        outputNumberC.setTextColor( Color.parseColor("#000000") );
                        outputNumberD.setText(Float.toString(new_inferredValue_D) + "%");
                        outputNumberD.setTextColor( Color.parseColor("#000000") );
                    }

                    else if( ( new_inferredValue_B >= new_inferredValue_A ) && ( new_inferredValue_B >= new_inferredValue_C ) && ( new_inferredValue_B >= new_inferredValue_D ) ) {
                        outputNumberA.setText(Float.toString(new_inferredValue_A) + "%");
                        outputNumberA.setTextColor( Color.parseColor("#000000") );
                        outputNumberB.setText(Float.toString(new_inferredValue_B) + "%");
                        outputNumberB.setTextColor( Color.parseColor("#5555FF") );
                        outputNumberC.setText(Float.toString(new_inferredValue_C) + "%");
                        outputNumberC.setTextColor( Color.parseColor("#000000") );
                        outputNumberD.setText(Float.toString(new_inferredValue_D) + "%");
                        outputNumberD.setTextColor( Color.parseColor("#000000") );
                    }

                    else if( ( new_inferredValue_C >= new_inferredValue_A ) && ( new_inferredValue_C >= new_inferredValue_B ) && ( new_inferredValue_C >= new_inferredValue_D ) ) {
                        outputNumberA.setText(Float.toString(new_inferredValue_A) + "%");
                        outputNumberA.setTextColor( Color.parseColor("#000000") );
                        outputNumberB.setText(Float.toString(new_inferredValue_B) + "%");
                        outputNumberB.setTextColor( Color.parseColor("#000000") );
                        outputNumberC.setText(Float.toString(new_inferredValue_C) + "%");
                        outputNumberC.setTextColor( Color.parseColor("#5555FF") );
                        outputNumberD.setText(Float.toString(new_inferredValue_D) + "%");
                        outputNumberD.setTextColor( Color.parseColor("#000000") );
                    }

                    else if( ( new_inferredValue_D >= new_inferredValue_A ) && ( new_inferredValue_D >= new_inferredValue_B ) && ( new_inferredValue_D >= new_inferredValue_C ) ) {
                        outputNumberA.setText(Float.toString(new_inferredValue_A) + "%");
                        outputNumberA.setTextColor( Color.parseColor("#000000") );
                        outputNumberB.setText(Float.toString(new_inferredValue_B) + "%");
                        outputNumberB.setTextColor( Color.parseColor("#000000") );
                        outputNumberC.setText(Float.toString(new_inferredValue_C) + "%");
                        outputNumberC.setTextColor( Color.parseColor("#000000") );
                        outputNumberD.setText(Float.toString(new_inferredValue_D) + "%");
                        outputNumberD.setTextColor( Color.parseColor("#5555FF") );
                    }
/*
                    outputNumberA.setText(Float.toString(inferredValue_A));
                    outputNumberB.setText(Float.toString(inferredValue_B));
                    outputNumberC.setText(Float.toString(inferredValue_C));
                    outputNumberD.setText(Float.toString(inferredValue_D));
*/
/*
                    outputNumberA.setText(Float.toString(new_inferredValue_A ) + "%");
                    outputNumberB.setText(Float.toString(new_inferredValue_B ) + "%");
                    outputNumberC.setText(Float.toString(new_inferredValue_C ) + "%");
                    outputNumberD.setText(Float.toString(new_inferredValue_D ) + "%");
 */

                }
            }
        });
    }

    private MappedByteBuffer loadModelFile() throws IOException {
        //AssetFileDescriptor fileDescriptor = this.getAssets().openFd("0513hospital_ann.tflite");
        AssetFileDescriptor fileDescriptor = this.getAssets().openFd("0720hospital_ann_OK.tflite");
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY,startOffset,declaredLength);
    }
}
