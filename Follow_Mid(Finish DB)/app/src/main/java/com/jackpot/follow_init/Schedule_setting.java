package com.jackpot.follow_init;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;


/**
 * Created by KWAK on 2018-05-23.
 */

public class Schedule_setting extends AppCompatActivity {

    EditText inEvent;
    EditText inDept;
    EditText inDest;
    TimePicker startTime;
    TimePicker endTime;

    Obj_schedule inSchedule;

    int set_code = 0;        // code 0 - set result to departure / code 1 - set result to destination.
    int week;

    public Schedule_setting() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.schedule_setting);

        inSchedule = new Obj_schedule();

        // XML의 widget에 접근하기 위한 구체화.
        inEvent = findViewById(R.id.inEvent);
        inDept = findViewById(R.id.inDept);
        inDest = findViewById(R.id.inDest);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);

        Button checkMon = findViewById(R.id.btnMon);
        Button checkTue = findViewById(R.id.btnTue);
        Button checkWed = findViewById(R.id.btnWed);
        Button checkThu = findViewById(R.id.btnThu);
        Button checkFri = findViewById(R.id.btnFri);
        // 요일 숫자 변경 (월요일 -> 2 로 시작)
        checkMon.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                week = 1;
            }
        });
        checkTue.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                week = 2;
            }
        });
        checkWed.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                week = 3;
            }
        });
        checkThu.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                week = 4;
            }
        });
        checkFri.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                week = 5;
            }
        });

        Button btnDept = findViewById(R.id.btnDept);
        Button btnDest = findViewById(R.id.btnDest);
        Button btnCancel = findViewById(R.id.btnCancel);
        Button btnSave = findViewById(R.id.btnSave);

        btnDept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), Map.class), 1);
            }
        });

        btnDest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivityForResult(new Intent(getApplicationContext(), Map.class), 2);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View v) {
                // Schedule 의 정보를 객체 생성하고 내부에 값 저장.
                //inSchedule = new Obj_schedule();

                // 객체에 이름, 출발, 도착지 저장.
                inSchedule.setEvent_name(inEvent.getText().toString());
                inSchedule.setDept_name(inDept.getText().toString());
                inSchedule.setDest_name(inDest.getText().toString());

                // 객체에 time 저장.
                inSchedule.setStart_hour(startTime.getHour());
                inSchedule.setStart_minute(startTime.getMinute());
                inSchedule.setEnd_hour(endTime.getHour());
                inSchedule.setEnd_minute(endTime.getMinute());

                // 객체에 요일 저장
                inSchedule.setWeek(week);

                // 객체를 tab_schedule로 전달
                Intent intent = new Intent();
                Bundle bundle = new Bundle();
                bundle.putSerializable("object", inSchedule);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);

                finish();
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // set_code 로 나누지 말고, request code 로 출발, 도착 나눠도 될 거 같은데? 어차피 애초에 request 를 구분지어서 Map 을 불러준것이라서?
        if(set_code == 0){
            String name = data.getStringExtra("Name");
            inDept.setText(name);
            set_code = 1;

            Double latitude = data.getDoubleExtra("Latitude", 0);
            Double longitude = data.getDoubleExtra("Longitude", 0);

            // Log 용으로 제대로 값 넘어오는지 확인. (나중에 지워도 됨.)
            //Toast.makeText(getApplicationContext(),"Name : "+name +"\nLatitude : "+latitude +"\nLongitude : "+longitude,Toast.LENGTH_SHORT).show();

            // 객체에 출발지 위도, 경도 저장.
            inSchedule.setDept_name(name);
            inSchedule.setDept_latitude(latitude);
            inSchedule.setDept_longitude(longitude);
        } else {
            String name = data.getStringExtra("Name");
            inDest.setText(name);
            set_code = 0;

            Double latitude = data.getDoubleExtra("Latitude", 0);
            Double longitude = data.getDoubleExtra("Longitude", 0);

            // Log 용으로 제대로 값 넘어오는지 확인. (나중에 지워도 됨.)
            //Toast.makeText(getApplicationContext(),"Name : "+name +"\nLatitude : "+latitude +"\nLongitude : "+longitude,Toast.LENGTH_SHORT).show();

            // 객체에 도착지 위도, 경도 저장.
            inSchedule.setDest_name(name);
            inSchedule.setDest_latitude(latitude);
            inSchedule.setDest_longitude(longitude);
        }
    }
}
