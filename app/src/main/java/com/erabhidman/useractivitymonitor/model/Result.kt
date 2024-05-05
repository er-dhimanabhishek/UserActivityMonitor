package com.erabhidman.useractivitymonitor.model

sealed class Result<T>(usageDataList: T? = null, error: String? = null){
    class Loading<T>(): Result<T>()
    class Success<T>(dataList: T? = null): Result<T>()
    class Error<T>(errorMsg: String? = null): Result<T>()
}
