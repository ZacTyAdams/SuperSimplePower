package com.zacadams.supersimplepower;

import android.app.ApplicationErrorReport;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
    public void getBattery(View v){
        TextView textView = (TextView)findViewById(R.id.textView);
        IntentFilter ifilter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent batteryStatus = this.registerReceiver(null,ifilter);
        int status = batteryStatus.getIntExtra(BatteryManager.EXTRA_STATUS, -1);
        System.out.println(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        //pulling and displaying battery health
        getBatteryPercent(batteryStatus);
        getBatteryHealth(batteryStatus);
        getBatteryTechnology(batteryStatus);
        getBatteryTemperature(batteryStatus);
        getChargingStatus(batteryStatus);
    }

    public void getBatteryPercent(Intent intent){
        int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,-1);
        int scale  = intent.getIntExtra(BatteryManager.EXTRA_SCALE,-1);
        float batteryPercent = level/(float)scale;
        TextView textView = (TextView)findViewById(R.id.textView);
        textView.setText(String.valueOf(batteryPercent*100));
    }

    public void getBatteryHealth(Intent intent){
        int status = intent.getIntExtra(BatteryManager.EXTRA_HEALTH, 0);
        TextView textView = (TextView)findViewById(R.id.textView2);
        switch (status){
            case BatteryManager.BATTERY_HEALTH_COLD: //int value of 7
                textView.setText("Batter Health = Cold");
                break;
            case BatteryManager.BATTERY_HEALTH_DEAD: //int value of 4
                textView.setText("Battery Health = Dead");
                break;
            case BatteryManager.BATTERY_HEALTH_GOOD: //int value of 2
                textView.setText("Battery Health = Good");
                break;
            case BatteryManager.BATTERY_HEALTH_OVERHEAT: //int value of 3
                textView.setText("Battery Health = Overheating");
                break;
            case BatteryManager.BATTERY_HEALTH_OVER_VOLTAGE: //int value of 5
                textView.setText("Battery Health = Over Voltage");
                break;
            case BatteryManager.BATTERY_HEALTH_UNKNOWN: // int value of 1
                textView.setText("Battery Health = Unknown");
                break;
            case BatteryManager.BATTERY_HEALTH_UNSPECIFIED_FAILURE:// int value of 6
                textView.setText("Battery Health = Unspecified Failure");
                break;
        }
    }

    public void getBatteryTechnology(Intent intent){
        String technology = intent.getExtras().getString(BatteryManager.EXTRA_TECHNOLOGY);
        TextView textView = (TextView)findViewById(R.id.textView3);
        textView.setText(technology);
    }

    public void getBatteryTemperature(Intent intent){
        int temperature = intent.getIntExtra(BatteryManager.EXTRA_TEMPERATURE,0);
        TextView textView = (TextView)findViewById(R.id.textView4);
        System.out.println(temperature);
        textView.setText(String.valueOf(temperature));
    }

    public void getChargingStatus(Intent intent){
        TextView textView = (TextView)findViewById(R.id.textView5);
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
                textView.setText("The device is charging via USB");
            }
            else if(acCharge){
                textView.setText("The device is charging via A/C");
            }
            else{
                textView.setText("The device is charging but could not detect though which method");
            }
        }
        else{
            textView.setText("The device is not charging");
        }

        //textView.setText("isCharging: "+isCharging+"chargePlug: "+ chargePlug+"usbCharge: "+usbCharge+"acCharge: "+acCharge);
    }

}
