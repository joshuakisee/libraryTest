package com.paylater.paylater.activities

import com.paylater.paylater.utils.Model

interface CallBrands {

    fun onActionSuccess(body: Model.GetBrand)

    fun onActionFailure(stringMsg:String)

}