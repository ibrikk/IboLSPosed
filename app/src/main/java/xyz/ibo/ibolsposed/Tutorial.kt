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
        XposedBridge.log("[Tutorial] Loaded package: ${lpparam.packageName}")

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

                    if (sensor.type == Sensor.TYPE_ACCELEROMETER) {
                        XposedBridge.log("[Tutorial] Hooking accelerometer listener for ${lpparam.packageName}")
                        hookListener(listener)
                    }
                }
            }
        )
    }

    private fun hookListener(listener: SensorEventListener) {
        XposedHelpers.findAndHookMethod(
            listener.javaClass,
            "onSensorChanged",
            SensorEvent::class.java,
            object : XC_MethodHook() {
                override fun beforeHookedMethod(param: MethodHookParam) {
                    try {
                        val event = param.args[0] as SensorEvent

                        // Log real sensor event
                        val eventHashCode = System.identityHashCode(event)
                        XposedBridge.log("[Tutorial] Event HashCode: $eventHashCode | Real Values: ${event.values.joinToString()}")

                        // Spoof sensor values directly
                        if (event.sensor.type == Sensor.TYPE_ACCELEROMETER) {
                            val spoofedValues = floatArrayOf(10.0f, 10.0f, 10.0f) // Example spoofed values
                            setSensorValues(event, spoofedValues)

                            // Log spoofed values
                            XposedBridge.log("[Tutorial] Spoofed Values: ${event.values.joinToString()} for Event HashCode: $eventHashCode")
                        }
                    } catch (e: Exception) {
                        XposedBridge.log("[Tutorial] Error in onSensorChanged hook: ${e.message}")
                    }
                }
            }
        )
    }

    private fun setSensorValues(event: SensorEvent, values: FloatArray) {
        try {
            val field: Field = SensorEvent::class.java.getDeclaredField("values")
            field.isAccessible = true
            field.set(event, values)
            XposedBridge.log("[Tutorial] Sensor values successfully spoofed")
        } catch (e: Exception) {
            XposedBridge.log("[Tutorial] Error setting sensor values: ${e.message}")
        }
    }
}
