package com.saldivar.permisolibrary


import android.Manifest
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.TextView
import androidx.core.content.ContextCompat
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.single.PermissionListener
import com.saldivar.permisolibrary.enums.PermissionStatusEnum
import kotlinx.android.synthetic.main.activity_mai.*

/**
 * BY
 */
class SaldivarActi : Activity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mai)

        setButtonClick()
    }

    private fun setButtonClick() {
        buttonCamera.setOnClickListener{checkCameraPermissions()}
        buttonsContacts.setOnClickListener{checkContactsPermissions()}
        buttonsAudio.setOnClickListener{checkAudioPermissions()}
    }

    public fun setPermissionHandler(permission: String,context: Context){
        Dexter.withActivity(context as Activity?)
                .withPermission(permission)
                .withListener(object: PermissionListener{
                    override fun onPermissionGranted(response: PermissionGrantedResponse) {
                        /*setPermissionStatus(textView, PermissionStatusEnum.GRANTED)*/
                    }

                    override fun onPermissionDenied(response: PermissionDeniedResponse) {
                        if (response.isPermanentlyDenied){
                            /*setPermissionStatus(textView, PermissionStatusEnum.PERMANENTLY_DENIED)*/
                        }else{
                            /*setPermissionStatus(textView, PermissionStatusEnum.DENIED)*/
                        }
                    }

                    override fun onPermissionRationaleShouldBeShown(permission: PermissionRequest?, token: PermissionToken) {
                        token.continuePermissionRequest()
                    }
                }).check()
    }

    public fun setPermissionStatus(textView: TextView,status: PermissionStatusEnum){
        when(status){
            PermissionStatusEnum.GRANTED ->{
                textView.text = getString(R.string.permission_status_grated)
                textView.setTextColor(ContextCompat.getColor(this,R.color.colorPermissionStatusGrated))
            }
            PermissionStatusEnum.DENIED ->{
                textView.text = getString(R.string.permission_status_denied)
                textView.setTextColor(ContextCompat.getColor(this,R.color.colorPermissionStatusDenied))
            }
            PermissionStatusEnum.PERMANENTLY_DENIED ->{
                textView.text = getString(R.string.permission_status_denied_permanently)
                textView.setTextColor(ContextCompat.getColor(this,R.color.colorPermissionStatusPermanentlyDenied))
            }
        }

    }

    private fun checkCameraPermissions()= setPermissionHandler(Manifest.permission.CAMERA,this)
    private fun checkContactsPermissions() = setPermissionHandler(Manifest.permission.READ_CONTACTS,this)
    private fun checkAudioPermissions()= setPermissionHandler(Manifest.permission.RECORD_AUDIO,this)
}
