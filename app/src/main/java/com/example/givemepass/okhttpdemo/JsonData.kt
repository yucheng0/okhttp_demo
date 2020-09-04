package com.example.givemepass.okhttpdemo

    import com.google.gson.annotations.SerializedName

    class JsonData {
    @SerializedName("Name")
    var name: String? = null
    @SerializedName("City")
    var city: String? = null
    @SerializedName("Country")
    var country: String? = null
}