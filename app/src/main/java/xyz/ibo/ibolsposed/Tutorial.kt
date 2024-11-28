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
        // Debug: Log the loaded package name
        XposedBridge.log("Loaded package: ${lpparam.packageName}")

        // Hook SensorManager.registerListener globally
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

                    // Debug: Log sensor registration
                    XposedBridge.log("Sensor registered: ${sensor.name} (Type: ${sensor.type})")

                    // Hook for accelerometer to spoof data
                    if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        XposedBridge.log("Intercepted accelerometer registration globally")

                        // Hook the listener's onSensorChanged method to log real sensor values
                        XposedHelpers.findAndHookMethod(
                            listener.javaClass,
                            "onSensorChanged",
                            SensorEvent::class.java,
                            object : XC_MethodHook() {
                                override fun beforeHookedMethod(param: MethodHookParam) {
                                    val event = param.args[0] as SensorEvent
                                    if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                                        val x = event.values[0]
                                        val y = event.values[1]
                                        val z = event.values[2]
                                        XposedBridge.log("Real Accelerometer Values: X=$x, Y=$y, Z=$z")
                                    }
                                }
                            }
                        )

                        // Start a thread to inject fake accelerometer data
                        Thread {
                            try {
                                while (true) {
                                    val fakeEvent = createFakeSensorEvent(sensor)
                                    val spoofedValues = floatArrayOf(1.0f, 2.0f, 3.0f) // Example spoofed values
                                    setSensorValues(fakeEvent, spoofedValues)
                                    fakeEvent.timestamp = System.nanoTime()

                                    // Debug: Log the spoofed values
                                    XposedBridge.log("Spoofed Accelerometer Values: X=${spoofedValues[0]}, Y=${spoofedValues[1]}, Z=${spoofedValues[2]}")

                                    listener.onSensorChanged(fakeEvent)
                                    Thread.sleep(100) // Update frequency (every 100ms)
                                }
                            } catch (e: Exception) {
                                XposedBridge.log("Error in spoofing thread: ${e.message}")
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
