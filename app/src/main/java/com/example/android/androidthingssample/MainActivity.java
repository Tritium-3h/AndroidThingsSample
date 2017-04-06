package com.example.android.androidthingssample;

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
//    private static final String BUTTON_PIN_0_NAME = "BCM21";
//    private static final String BUTTON_PIN_1_NAME = "BCM26";
//    private static final String BUTTON_PIN_2_NAME = "BCM16";
//    private static final String BUTTON_PIN_3_NAME = "BCM6";
//
//    private static final String LED_PIN_0_NAME = "BCM20";
//    private static final String LED_PIN_1_NAME = "BCM19";
//    private static final String LED_PIN_2_NAME = "BCM12";
//    private static final String LED_PIN_3_NAME = "BCM5";

    // EDISON
    private static final String BUTTON_PIN_0_NAME = "IO2";
    private static final String BUTTON_PIN_1_NAME = "IO4";
    private static final String BUTTON_PIN_2_NAME = "IO6";
    private static final String BUTTON_PIN_3_NAME = "IO8";

    private static final String LED_PIN_0_NAME = "IO3";
    private static final String LED_PIN_1_NAME = "IO5";
    private static final String LED_PIN_2_NAME = "IO7";
    private static final String LED_PIN_3_NAME = "IO9";


    private ButtonInputDriver mButtonInputDrivers[];
    private Gpio mLedGpio[];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mButtonInputDrivers = new ButtonInputDriver[4];
        mLedGpio = new Gpio[4];

        PeripheralManagerService service = new PeripheralManagerService();
        try {
            // Step 3. Initialize button driver with selected GPIO pin
            mButtonInputDrivers[0] = new ButtonInputDriver(
                    BUTTON_PIN_0_NAME,
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_0);
            mButtonInputDrivers[1] = new ButtonInputDriver(
                    BUTTON_PIN_1_NAME,
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_1);
            mButtonInputDrivers[2] = new ButtonInputDriver(
                    BUTTON_PIN_2_NAME,
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_2);
            mButtonInputDrivers[3] = new ButtonInputDriver(
                    BUTTON_PIN_3_NAME,
                    Button.LogicState.PRESSED_WHEN_LOW,
                    KeyEvent.KEYCODE_3);

            mLedGpio[0] = service.openGpio(LED_PIN_0_NAME);
            mLedGpio[0].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio[1] = service.openGpio(LED_PIN_1_NAME);
            mLedGpio[1].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio[2] = service.openGpio(LED_PIN_2_NAME);
            mLedGpio[2].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);
            mLedGpio[3] = service.openGpio(LED_PIN_3_NAME);
            mLedGpio[3].setDirection(Gpio.DIRECTION_OUT_INITIALLY_LOW);


        } catch (IOException e) {
            Log.e(TAG, "Error configuring GPIO pin", e);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        for (ButtonInputDriver b : mButtonInputDrivers) {
            if (b != null) {
                try {
                    b.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error closing Button driver", e);
                }
            }
        }
        for (Gpio g : mLedGpio)
            if (g != null) {
                try {
                    g.close();
                } catch (IOException e) {
                    Log.e(TAG, "Error on PeripheralIO API", e);
                }
            }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (ButtonInputDriver b : mButtonInputDrivers) {
            b.register();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (ButtonInputDriver b : mButtonInputDrivers) {
            b.unregister();
        }
    }

    boolean turnLed(int ledNum, boolean value) {
        Gpio gpio = mLedGpio[ledNum];
        if (gpio != null) {
            try {
                gpio.setValue(value);
                return true;
            } catch (IOException e) {
                Log.e(TAG, "Error on PeripheralIO API", e);
            }
        }
        return false;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                if (turnLed(0,false)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_1:
                if (turnLed(1,false)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_2:
                if (turnLed(2,false)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_3:
                if (turnLed(3,false)) {
                    return true;
                }
                break;

        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_0:
                if (turnLed(0,true)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_1:
                if (turnLed(1,true)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_2:
                if (turnLed(2,true)) {
                    return true;
                }
                break;
            case KeyEvent.KEYCODE_3:
                if (turnLed(3,true)) {
                    return true;
                }
                break;

        }
        return super.onKeyUp(keyCode, event);
    }
}

