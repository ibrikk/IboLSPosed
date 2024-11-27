package xyz.ibo.ibolsposed

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import de.robv.android.xposed.IXposedHookLoadPackage
import de.robv.android.xposed.XC_MethodHook
import de.robv.android.xposed.XposedHelpers
import de.robv.android.xposed.callbacks.XC_LoadPackage.LoadPackageParam


class Tutorial : IXposedHookLoadPackage {
    @Throws(Throwable::class)
    override fun handleLoadPackage(lpparam: LoadPackageParam) {
//        XposedBridge.log("Loaded app: " + lpparam.packageName)
//        if (lpparam.packageName == "xyz.ibo.ibolsposed") {
//            XposedBridge.log("Loaded app Ibo: ${lpparam.packageName}")
//
//            XposedHelpers.findAndHookMethod(
//                "xyz.ibo.ibolsposed.MainActivity",
//                lpparam.classLoader,
//                "onCreate",
//                android.os.Bundle::class.java,
//                object : XC_MethodHook() {
//                    @Throws(Throwable::class)
//                    override fun afterHookedMethod(param: MethodHookParam) {
//                        val activity = param.thisObject as Activity
//                        XposedBridge.log("Hooked MainActivity: ${activity.localClassName}")
//
//                        activity.runOnUiThread {
//                            val textView = TextView(activity)
//                            textView.text = "Hello from Xposed Hook!"
//                            textView.textSize = 20f
//                            textView.setPadding(50, 50, 50, 50)
//
//                            activity.setContentView(textView) // Replace the content
//                        }
//                    }
//                }
//            )
//        }
//
//        XposedHelpers.findAndHookMethod(
//            "java.lang.ClassLoader",
//            lpparam.classLoader,
//            "loadClass",
//            String::class.java,
//            Boolean::class.javaPrimitiveType, // second parameter: resolve
//            object : XC_MethodHook() {
//                override fun beforeHookedMethod(param: MethodHookParam) {
//                    val className = param.args[0] as String
//                    XposedBridge.log("Class being loaded: $className")
//                }
//            }
//        )
//        XposedHelpers.findAndHookMethod(
//            "android.hardware.SensorManager",
//            lpparam.classLoader,
//            "registerListener",
//            SensorEventListener::class.java, // Listener
//            Sensor::class.java,             // Sensor
//            Int::class.java,                // Sampling rate
//            object : XC_MethodHook() {
//                override fun afterHookedMethod(param: MethodHookParam) {
//                    val sensor = param.args[1] as Sensor
//                    XposedBridge.log("Sensor registered: ${sensor.name}")
//
//                    // Check if it's the accelerometer or gyroscope
//                    if (sensor.type == Sensor.TYPE_ACCELEROMETER || sensor.type == Sensor.TYPE_GYROSCOPE) {
//                        XposedBridge.log("Intercepting sensor: ${sensor.name}")
//                    }
//                }
//            }
//        )


    }
}