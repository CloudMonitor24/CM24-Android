package com.cloudmonitor24.sample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

// Import CloudMonitor24 classes
import com.cloudmonitor24.sdk.AlarmIds;
import com.cloudmonitor24.sdk.CloudMonitor24;
import com.cloudmonitor24.sdk.VarIds;

/**
 * ------------------------------------------------------------------------------------------------
 * Created by CloudMonitor24 on 19/01/16.
 * ------------------------------------------------------------------------------------------------
 * Setting Up
 * ------------------------------------------------------------------------------------------------
 * This is a sample application to help with CloudMonitor24 integration in Android Studio apps
 * You will need to sign up for an account and to create at least plant.
 * Each plant has its own credentials (identifier and token), automatically generated by the system
 * and available on the Sensor Map area of the portal
 * ------------------------------------------------------------------------------------------------
 * Usage
 * ------------------------------------------------------------------------------------------------
 * 1. Open the app
 * 2. Entry plant credentials
 * 3. Tap the 'LAUNCH' button and check on the platform what's happening
 * 4. Tap the 'STOP' button to stop sending data to the platform
 * ------------------------------------------------------------------------------------------------
 * Error checking
 * ------------------------------------------------------------------------------------------------
 * Report any issue on CloudMonitor24 or on our Github repo
 * ------------------------------------------------------------------------------------------------
 */

public class MainActivity extends AppCompatActivity {

    private Button btnLaunch;
    private Button btnStop;
    private EditText plantIdentifier;
    private EditText plantToken;
    private double testCounter;
    private Handler handler;

    // CloudMonitor24 CloudMonitor24 instance
    private CloudMonitor24 logger;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        // Set up graphics and layout
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        btnLaunch = (Button) findViewById(R.id.launch);
        btnStop = (Button) findViewById(R.id.stop);
        plantIdentifier = (EditText) findViewById(R.id.plant_identifier);
        plantToken = (EditText) findViewById(R.id.plant_token);

        // Button "Stop" Listener
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v)
            {
                if (handler != null) {
                    handler.removeCallbacksAndMessages(null);

                    btnStop.setEnabled(false);
                    btnLaunch.setEnabled(true);
                    plantIdentifier.setEnabled(true);
                    plantToken.setEnabled(true);

                    // Send a custom user alarm on stop
                    try {
                        logger.logAlarm(AlarmIds.ALARM_ID_USER_ALARM, (short) 0 );
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    // Destroy the CloudMonitor24 object and unbind the service
                    logger.destroyLogger(getApplicationContext());
                }
            }
        });

        // Button "Launch" listener
        btnLaunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View arg0) {

                testCounter = 0;

                final String identifier = plantIdentifier.getText().toString();
                final String token = plantToken.getText().toString();

                if (identifier.matches("") || token.matches("") || token.length() < 16 || identifier.length() < 16)
                {
                    Toast.makeText(getApplicationContext(), "set credentials (16 characters)", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    btnStop.setEnabled(true);
                    btnLaunch.setEnabled(false);
                    plantIdentifier.setEnabled(false);
                    plantToken.setEnabled(false);

                    // Get CloudMonitor24 CloudMonitor24
                    // In order to have a fully working CloudMonitor24 instance you should specify valid plant identifier and token
                    logger = CloudMonitor24.createInstance(getApplicationContext(), plantIdentifier.getText().toString(), plantToken.getText().toString());

                    // Create a Runnable that sends two variables every 10 seconds
                    handler = new Handler();
                    final Runnable r = new Runnable() {
                        public void run() {
                            try
                            {
                                // Send temperature for sensor_id 0 and humidity for sensor_id 1
                                logger.logVar(VarIds.VAR_ID_TEMPERATURE, (float) Math.sin(testCounter++), (short) 0);
                                logger.logVar(VarIds.VAR_ID_HUMIDITY, (float) Math.cos(testCounter++), (short) 1);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            handler.postDelayed(this, 10000);
                        }
                    };
                    handler.postDelayed(r, 10000);
                }
            }});
    }
}