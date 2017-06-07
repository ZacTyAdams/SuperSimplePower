package com.zacadams.supersimplepower;

import android.app.ApplicationErrorReport;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.text.DecimalFormat;

public class MainActivity extends AppCompatActivity {
    boolean run = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        run = true;
    }

    protected void onResume(){
        super.onResume();
        run = true;
        getBattery();
    }

    protected void onStop(){
        super.onStop();
        run = false;


    }

    protected void onPause(){
        super.onPause();
        run = false;
    }

    public void toggle(View v){
        if(run){
            run = false;
        }
        else{
            run = true;
        }
    }

    public void getBattery(){
        TextView textView = (TextView)findViewById(R.id.textView);
        final IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        final Intent batteryStatus = this.registerReceiver(null,ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);

        //initial read being called here
        getBatteryPercent(batteryStatus);

        getBatteryHealth(batteryStatus);

        getBatteryTechnology(batteryStatus);

        getBatteryTemperature(batteryStatus); //Unable to pull the battery temperature

       getChargingStatus(batteryStatus);

        //System.out.println("stuck looping");
        //pulling and displaying battery health
        //int countdown = 1;
        Thread updater = new Thread(new Runnable() {
            @Override
            public void run() {
                while(run) {
                    //if(run) {
                        int countdown = 1;
                        while (countdown < 10) {
                            System.out.println(countdown);
                            ++countdown;
                            try {
                                Thread.sleep(1000);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                        IntentFilter updateFilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
                        final Intent batteryUpdate = registerReceiver(null, updateFilter);

                        getBatteryPercent(batteryUpdate);

                        getBatteryHealth(batteryUpdate);

                        getBatteryTechnology(batteryUpdate);

                        getBatteryTemperature(batteryUpdate);

                        getChargingStatus(batteryUpdate);
                    //}
                    //else{
                    //    return;
                    //}
                }
            }
        });
        updater.start();
    }

    public void getBatteryPercent(Intent intent){
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale  = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        final float batteryPercent = level/(float)scale;
        final TextView textView = (TextView)findViewById(R.id.textView);
        textView.post(new Runnable() {
              public void run() {
                  textView.setText(String.valueOf(((int)(batteryPercent*100)))+"%");
              }
        });

    }

    public void getBatteryHealth(Intent intent){
        int status = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        String holder = null;
        final TextView textView1 = (TextView)findViewById(R.id.textView2);
        switch (status){
            case BatteryManager.BATTERY_HEALTH_COLD: //int value of 7
                //textView.setText("Battery Health = Cold");
                holder = "Battery Health: Cold";
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD: //int value of 4
                //textView.setText("Battery Health = Dead");
                holder = "Battery Health: Dead";
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD: //int value of 2
                //textView.setText("Battery Health = Good");
                holder = "Battery Health: Good";
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT: //int value of 3
                //textView.setText("Battery Health = Overheating");
                holder = "Battery Health: Overheating";
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE: //int value of 5
                //textView.setText("Battery Health = Over Voltage");
                holder="Battery Health: Over Voltage";
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN: // int value of 1
                //textView.setText("Battery Health = Unknown");
                holder="Battery Health: Unknown";
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:// int value of 6
                //textView.setText("Battery Health = Unspecified Failure");
                holder = "Battery Health: Unspecified Failure";
                break;
        }
        //final TextView textView1 = (TextView)findViewById(R.id.textView2);
        final String finalHolder = holder;
        textView1.post(new Runnable() {
            public void run() {
                textView1.setText(finalHolder);
            }
        });
    }

    public void getBatteryTechnology(Intent intent){
        final String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
        final TextView textView2 = (TextView)findViewById(R.id.textView3);
        textView2.post(new Runnable() {
            public void run() {
                textView2.setText("Battery Technology: "+technology);
            }
        });
    }

    public void getBatteryTemperature(Intent intent){
        final DecimalFormat df2 = new DecimalFormat(".##");
        final double holder3 = ((((double)intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0))/10)*1.8+32);
        final TextView textView3 = (TextView)findViewById(R.id.textView4);
        //System.out.println(temperature);
        textView3.post(new Runnable() {
            public void run() {
                textView3.setText("Battery Temperature: "+String.valueOf(df2.format(holder3))+"°F");

            }
        });
    }

    public void getChargingStatus(Intent intent){
       final TextView textView4 = (TextView)findViewById(R.id.textView5);
        String holder = null;
        int status = intent.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        //check if device is charging
        boolean isCharging = status == BatteryManager.BATTERY_STATUS_CHARGING || status == BatteryManager.BATTERY_STATUS_FULL;
        //check if device is plugged in
        int chargePlug = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED,-1);
        //check if device is charging via USB
        boolean usbCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_USB;
        //check if charging via AC
        boolean acCharge = chargePlug == BatteryManager.BATTERY_PLUGGED_AC;
        if(isCharging){
            if(usbCharge){
                //textView.setText("The device is charging via USB");
                holder = "The device is charging via USB";
            }
            else if(acCharge){
                //textView.setText("The device is charging via A/C");
                holder = "The device is charging via A/C";
            }
            else{
                //textView.setText("The device is charging but could not detect though which method");
                holder = "The device is charging but could not detect though which method";
            }
        }
        else{
            //textView.setText("The device is not charging");
            holder = "The device is not charging";
        }
        final String finalHolder2 = holder;
        textView4.post(new Runnable() {
            public void run() {
                textView4.setText(finalHolder2);
            }
        });
        //textView.setText("isCharging: "+isCharging+"chargePlug: "+ chargePlug+"usbCharge: "+usbCharge+"acCharge: "+acCharge);
    }

}
