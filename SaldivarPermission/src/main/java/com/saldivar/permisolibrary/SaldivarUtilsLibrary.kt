package com.saldivar.permisolibrary


import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.graphics.Typeface
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.provider.Settings
import android.telephony.TelephonyManager
import android.view.View
import android.widget.ProgressBar
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import com.saldivar.permisolibrary.ToolsBuscador.newWord
import com.saldivar.permisolibrary.ToolsBuscador.wordSearch
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlin.collections.ArrayList

/**
 * BY Saldivar Leon Cesar
 */

/**Permisos*/
fun  permissionsSaldivar(permission: String, context: Context): Boolean {
    var permiso = false
    Dexter.withActivity(context as Activity)
            .withPermission(permission)
            .withListener(object: PermissionListener{
                override fun onPermissionGranted(response: PermissionGrantedResponse){
                    permiso = true
                }

                override fun onPermissionDenied(response: PermissionDeniedResponse) {
                    permiso = if (response.isPermanentlyDenied){
                        false
                    }else{
                        false
                    }
                }

                override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                    token.continuePermissionRequest()
                }
            }).check()
    return permiso
}

fun multiplePermissionsSaldivar(context: Activity, requestedPermissions: ArrayList<String>):Boolean {
    var permiso = false
    Dexter.withActivity(context).withPermissions(
            requestedPermissions
    ).withListener(object : MultiplePermissionsListener {
        override fun onPermissionsChecked(report: MultiplePermissionsReport) {
            if (report.areAllPermissionsGranted()) {
                permiso = true
            }
        }

        override fun onPermissionRationaleShouldBeShown(
                permissions: List<PermissionRequest>,
                token: PermissionToken
        ) {
            permiso = false
            token.continuePermissionRequest()
        }
    }).check()
    return permiso
}

/**progrees*/
fun showModalProgressSaldivar(progress: ProgressBar){progress.visibility = View.VISIBLE}
fun goneModalProgressSaldivar(progress: ProgressBar){progress.visibility = View.GONE}

/**preferences*/
fun preferencesSaldivar(context: Context,privateMode:Int,namePreference:String): SharedPreferences {
    return context.getSharedPreferences(namePreference,privateMode)
}

/**fuentesRoboto**/
fun styleRobotoBlackSaldivar(context:Context)= Typeface.createFromAsset(context.assets,"Roboto-Black.ttf")!!
fun styleRobotoBlackItalicSaldivar(context: Context) = Typeface.createFromAsset(context.assets,"Roboto-BlackItalic.ttf")!!
fun styleRobotoBoldSaldivar(context: Context)= Typeface.createFromAsset(context.assets,"Roboto-Bold.ttf")!!
fun styleRobotoBoldItalicSaldivar(context: Context)= Typeface.createFromAsset(context.assets,"Roboto-BoldItalic.ttf")!!
fun styleRobotoItalicSaldivar(context: Context)=Typeface.createFromAsset(context.assets,"Roboto-Italic.ttf")!!
fun styleRobotoLightSaldivar(context: Context)= Typeface.createFromAsset(context.assets,"Roboto-Light.ttf")!!
fun styleRobotoLightItalicSaldivar(context: Context)= Typeface.createFromAsset(context.assets,"Roboto-LightItalic.ttf")!!
fun styleRobotoMediumSaldivar(context: Context) = Typeface.createFromAsset(context.assets,"Roboto-Medium.ttf")!!
fun styleRobotoMediumItalicSaldivar(context: Context) = Typeface.createFromAsset(context.assets,"Roboto-MediumItalic.ttf")!!
fun styleRobotoRegularSaldivar(context: Context)= Typeface.createFromAsset(context.assets,"Roboto-Regular.ttf")!!
fun styleRobotoThinSaldivar(context: Context)= Typeface.createFromAsset(context.assets,"Roboto-Thin.ttf")!!
fun styleRobotoThinItalicSaldivar(context: Context)= Typeface.createFromAsset(context.assets,"Roboto-ThinItalic.ttf")!!

/**Verificar si hay internet*/
fun isInternetAvailableSaldivar(context: Context): Boolean {
    var result = false
    val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        result = when {

            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
    } else {
        connectivityManager.run {
            connectivityManager.activeNetworkInfo?.run {
                result = when (type) {
                    ConnectivityManager.TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
    }
    return result
}

/**Obtener Imei*/
/**Permisos multiple incluido*/
fun getImeiSaldivar(context: Context, activity: Activity,requestedPermissions: ArrayList<String>): String {
    val telephonyManager = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
    var imei=""
    if (ContextCompat.checkSelfPermission(context, Manifest.permission.READ_PHONE_STATE)
            != PackageManager.PERMISSION_GRANTED
    ) {
        multiplePermissionsSaldivar(context=activity,requestedPermissions = requestedPermissions)
    } else {
        imei = try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                telephonyManager.imei
            } else {
                telephonyManager.deviceId
            }
        } catch (e: Exception) {
            Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
        }
    }
    return imei
}
/**
 * Ocultar Barras
 * */
fun ocultarBarsSaldivar(context: Activity){
    context.window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_LAYOUT_STABLE
            or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
            or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
            or View.SYSTEM_UI_FLAG_FULLSCREEN
            or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY)

}

/**Buscador automatico*/

fun searchAutomatic(repetitiveTask:()->Unit,successTask:()->Unit) {
    CoroutineScope(Dispatchers.Default).async {
        while (wordSearch == newWord) {
            delay(1500)
            repetitiveTask()
            /*wordSearch = buscarpalabra.text.toString().toUpperCase(Locale.getDefault())*/
        }
        newWord = wordSearch
        successTask()
        /*openFragment(EnroladosFragment.newInstance())*/

    }
}
