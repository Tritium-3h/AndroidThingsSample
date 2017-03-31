package com.example.android.androidthingssample;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.things.contrib.driver.button.Button;
import com.google.android.things.contrib.driver.button.ButtonInputDriver;
import com.google.android.things.pio.Gpio;
import com.google.android.things.pio.PeripheralManagerService;


import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    // RASPBERRY
//    private static final String BUTTON_PIN_1_NAME = "BCM21";
//    private static final String BUTTON_PIN_2_NAME = "";
//    private static final String BUTTON_PIN_3_NAME = "";
//    private static final String BUTTON_PIN_4_NAME = "";

    // EDISON
    private static final String BUTTON_PIN_1_NAME = "IO2";
    private static final String BUTTON_PIN_2_NAME = "IO4";
    private static final String BUTTON_PIN_3_NAME = "IO6";
    private static final String BUTTON_PIN_4_NAME = "IO8";

    private static final String LED_PIN_1_NAME = "IO3";
    private static final String LED_PIN_2_NAME = "IO5";
    private static final String LED_PIN_3_NAME = "IO7";
    private static final String LED_PIN_4_NAME = "IO9";


    private ButtonInputDriver mButtonInputDriver;
    private Gpio mLedGpio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            // Step 3. Initialize button driver with selected GPIO pin
            mButtonInputDriver = new ButtonInputDriver(
                    BUTTON_PIN_1_NAME,
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_SPACE);

            mLedGpio = service.openGpio(LED_PIN_1_NAME);
            // Step 2. Configure as an output.
            mLedGpio.setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);


        } catch (IOException e) {
            Log.e(TAG, "Error configuring GPIO pin", e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (mButtonInputDriver != null) {
            try {
                mButtonInputDriver.close();
            } catch (IOException e) {
                Log.e(TAG, "Error closing Button driver", e);
            }
        }
        if (mLedGpio != null) {
            try {
                mLedGpio.close();
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        mButtonInputDriver.register();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mButtonInputDriver.unregister();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            if (mLedGpio != null) {
                try {
                    mLedGpio.setValue(false);
                    return true;
                } catch (IOException e) {
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_SPACE) {
            if (mLedGpio != null) {
                try {
                    mLedGpio.setValue(true);
                    return true;
                } catch (IOException e) {
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }
            }
        }
        return super.onKeyUp(keyCode, event);
    }
}

