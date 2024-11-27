package xyz.ibo.ibolsposed

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedBridge
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage
import java.lang.reflect.Field

class Tutorial : IXposedHookLoadPackage {
    override fun handleLoadPackage(lpparam: XC_LoadPackage.LoadPackageParam) {
        XposedBridge.log("Global sensor spoofing initialized for ${lpparam.packageName}")

        XposedHelpers.findAndHookMethod(
            SensorManager::class.java,
            "registerListener",
            SensorEventListener::class.java,
            Sensor::class.java,
            Int::class.java,
            Int::class.java,
            object : XC_MethodHook() {
                override fun afterHookedMethod(param: MethodHookParam) {
                    val listener = param.args[0] as SensorEventListener
                    val sensor = param.args[1] as Sensor

                    // Apply spoofing for accelerometer
                    if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        XposedBridge.log("Intercepted accelerometer registration globally")

                        // Start a thread to inject fake sensor data
                        Thread {
                            try {
                                while (true) {
                                    val fakeEvent = createFakeSensorEvent(sensor)
                                    setSensorValues(fakeEvent, floatArrayOf(1.0f, 2.0f, 3.0f)) // Set spoofed values
                                    fakeEvent.timestamp = System.nanoTime()

                                    listener.onSensorChanged(fakeEvent)
                                    Thread.sleep(100) // Update frequency
                                }
                            } catch (e: Exception) {
                                XposedBridge.log("Error in global spoofing thread: ${e.message}")
                            }
                        }.start()
                    }
                }
            }
        )
    }

    private fun createFakeSensorEvent(sensor: Sensor): SensorEvent {
        // Create an instance of SensorEvent using reflection
        val event = XposedHelpers.newInstance(SensorEvent::class.java, 3) as SensorEvent
        event.sensor = sensor
        return event
    }

    private fun setSensorValues(event: SensorEvent, values: FloatArray) {
        try {
            // Access the 'values' field of SensorEvent using reflection
            val valuesField: Field = SensorEvent::class.java.getDeclaredField("values")
            valuesField.isAccessible = true
            valuesField.set(event, values)
        } catch (e: Exception) {
            XposedBridge.log("Error setting sensor values: ${e.message}")
        }
    }
}
