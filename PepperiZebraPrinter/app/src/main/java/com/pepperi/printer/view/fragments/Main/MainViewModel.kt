package com.appa.viewModel

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.pepperi.printer.model.Repository
import com.pepperi.printer.model.api.zebra.OnFinishPrintListener
import com.pepperi.printer.model.api.zebra.ZebraApi
import com.pepperi.printer.model.entities.UserPrinterModel
import com.pepperi.printer.view.Managers.PrintDialogManager

const val DATA_URI="dataURI"
class MainViewModel(private val repository: Repository,private val zebraApi: ZebraApi) : ViewModel(){

    var allPrintersLiveData : MutableLiveData<List<UserPrinterModel>?> =  MutableLiveData<List<UserPrinterModel>?>()
    val errorsLiveData :MutableLiveData<Boolean?> =  MutableLiveData<Boolean?>()
    var userDefaultPrinter : UserPrinterModel? =  null

    init {
        getAllUserPrinters()
        userDefaultPrinter =  getDefaultPrinter()

        setZebraApiListener()
    }

    private fun setZebraApiListener() {
        zebraApi.setOnFinishPrintListener(object : OnFinishPrintListener{
            override fun onFinishPrintListener(isPrintSuccess: Boolean) {

        if(isPrintSuccess){
            errorsLiveData.value = true
        }else{
            errorsLiveData.value = false
        }
    }
        })
    }

    fun getAllUserPrinters(){
        allPrintersLiveData.value = repository.getAllUserPrinters()
    }
    fun removeUserPrinter(userPrinter: UserPrinterModel){
        repository.removePrinter(userPrinter.mac)
    }

    fun setUserPrinterAsDefault(userPrinter: UserPrinterModel){

            resetDefaultUser()

            userPrinter.isDefault = true

            repository.saveUserPrinter(userPrinter)

    }
    private fun resetDefaultUser(){

        removeAllPrinters()

        allPrintersLiveData.value?.let { list ->
            for (i in 0 until list.size ){
                list[i].isDefault = false
                repository.saveUserPrinter(list[i])
            }
        }

        getAllUserPrinters()
    }

    private fun getDefaultPrinter(): UserPrinterModel? {

        var userPrinterDefault : UserPrinterModel? = null

        allPrintersLiveData.value?.let { list ->
            for (i in 0 until list.size ){
                if (list[i].isDefault == true){
                    userPrinterDefault = list[i]
                }
            }
        }
        return userPrinterDefault
    }

    private fun removeAllPrinters(){
        repository.removeAllPrinters()
    }

    fun printDataByUri(data: Uri){

        val dataUri = getDataUriFromUri(data)

        zebraApi.printData(dataUri,userDefaultPrinter!!.mac)
    }

    fun getDataUriFromUri(uri: Uri) :String{
        val parameters = uri.queryParameterNames.associateWith { uri.getQueryParameters(it) }

        return parameters[DATA_URI]?.get(0).toString()


    }
}


