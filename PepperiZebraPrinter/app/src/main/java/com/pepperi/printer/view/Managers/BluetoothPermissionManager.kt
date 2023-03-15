package com.pepperi.printer.view.Managers

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment

class BluetoothPermissionManager(val fragment: Fragment) {

    private var isBluetoothScanPermissionGranted = false
    private var isBluetoothConnectPermissionGranted = false
    private var isBluetoothAdvertisePermissionGranted = false

    private var isPermissionGranted = true

    private lateinit var permissionLauncher : ActivityResultLauncher<Array<String>>

    fun requestPermissions(){

       permissionLauncher()

       requestBluetoothPermission()

   }
       private fun permissionLauncher() {
           permissionLauncher =
               fragment.registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permission ->

                   if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
                       isBluetoothScanPermissionGranted =
                           permission[Manifest.permission.BLUETOOTH_SCAN]
                               ?: isBluetoothScanPermissionGranted
                       isBluetoothConnectPermissionGranted =
                           permission[Manifest.permission.BLUETOOTH_CONNECT]
                               ?: isBluetoothConnectPermissionGranted
                       isBluetoothAdvertisePermissionGranted =
                           permission[Manifest.permission.BLUETOOTH_ADVERTISE]
                               ?: isBluetoothAdvertisePermissionGranted
                   } else {
                       Log.e("PermissionGranted", "older permission needed")
                   }

                   if (!isBluetoothScanPermissionGranted) {
                       isPermissionGranted = false
                   }
                   if (!isBluetoothConnectPermissionGranted) {
                       isPermissionGranted = false
                   }
                   if (!isBluetoothAdvertisePermissionGranted) {
                       isPermissionGranted = false
                   }
               }
       }

    private fun requestBluetoothPermission() {

        Log.e("requestBluetoothPermiss", "stzrt requestBluetoothPermission")


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            isBluetoothScanPermissionGranted = ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.BLUETOOTH_SCAN,
            ) == PackageManager.PERMISSION_GRANTED

            isBluetoothConnectPermissionGranted = ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.BLUETOOTH_CONNECT,
            ) == PackageManager.PERMISSION_GRANTED

            isBluetoothAdvertisePermissionGranted = ContextCompat.checkSelfPermission(
                fragment.requireContext(),
                Manifest.permission.BLUETOOTH_ADVERTISE,
            ) == PackageManager.PERMISSION_GRANTED
        }else{
            Log.e("getBluetoothPermission", "older permission needed")
        }

        val permissionRequest : MutableList<String> = ArrayList()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            if (!isBluetoothScanPermissionGranted) {
                permissionRequest.add(Manifest.permission.BLUETOOTH_SCAN)
            }
            if (!isBluetoothConnectPermissionGranted) {
                permissionRequest.add(Manifest.permission.BLUETOOTH_CONNECT)
            }
            if (!isBluetoothAdvertisePermissionGranted) {
                permissionRequest.add(Manifest.permission.BLUETOOTH_ADVERTISE)
            }
        }

        if (permissionRequest.isNotEmpty()){
            permissionLauncher.launch(permissionRequest.toTypedArray())
        }
    }

    fun getPermissionsGranted() :Boolean{
        return isPermissionGranted
    }
}