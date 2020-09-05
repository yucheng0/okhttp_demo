package com.example.givemepass.okhttpdemo

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.squareup.okhttp.*
import kotlinx.android.synthetic.main.activity_main.*
import java.io.IOException
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors


val TAG = "myTag"
class MainActivity : AppCompatActivity() {
    private lateinit var service: ExecutorService
    private lateinit var client: OkHttpClient

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initData()
        initView()
    }

    private fun initData() {
        client = OkHttpClient()
        service = Executors.newSingleThreadExecutor()
    }

    //按鍵處理
    private fun initView() {
        setContentView(R.layout.activity_main)
        send_request_in_background.setOnClickListener { handleRequestInBackground() }
        send_request.setOnClickListener { handleRequest() }
        get_json_btn.setOnClickListener { handleJson() }
    }

    //在網路讀一個存在的json檔案而已（那我是不是可以用來讀Rest的呢？）
    private fun handleJson() {
        val JSON = MediaType.parse("application/json; charset=utf-8");
        val body= RequestBody.create(JSON, "{\"account\":\"w06\"    ,\"pw\":\"w\" , \"timezone\":8 }")
        val request = Request.Builder()
    //            .url("https://raw.githubusercontent.com/givemepassxd999/okhttp_demo/master/app/src/main/res/sample.json")
      //       .addHeader("Authorization",token) //token 給你的session
                .url("http://202.88.100.249/SAWELLPlus_Club/php/3rd_login.php")
                .post(body)
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {
                runOnUiThread { text.text = e.message }
            }

            @Throws(IOException::class)
            //正確回應
            override fun onResponse(response: Response) {
               val resStr = response.body().string()  //讀到值了
                val sessionid =response.header("Set-Cookie")
             Log.d(TAG, "onResponse: $resStr")
               Log.d(TAG, "sessionid: $sessionid")
//已有了值了所以解析Json
                //把name, city, country  印出來
       /*         val jsonData = Gson().fromJson<List<JsonData>>(resStr, object : TypeToken<List<JsonData>>() {

                }.type)
                runOnUiThread {
                    val sb = StringBuffer()
                    for (json in jsonData) {
                        sb.append("name:")
                        sb.append(json.name)
                        sb.append("\n")
                        sb.append("city:")
                        sb.append(json.city)
                        sb.append("\n")
                        sb.append("country:")
                        sb.append(json.country)
                        sb.append("\n")
                    }
                    text.text = sb.toString()
                } */
            }
        })
    }

    private fun handleRequest() {
        val request = Request.Builder()
                .url("http://www.google.com.tw")
                .build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(request: Request, e: IOException) {
                runOnUiThread { text.text = e.message }
            }

            @Throws(IOException::class)
            override fun onResponse(response: Response) {
                val resStr = response.body().string()
                Log.d(TAG, "onResponse1: $resStr")
                runOnUiThread { text.text = resStr }
            }
        })
    }

    private fun handleRequestInBackground() {
        service.submit {
            val builder = HttpUrl.parse("https://www.google.com.tw/search?").newBuilder()
            builder.addQueryParameter("q", "givemepass")
            builder.addQueryParameter("oq", "givemepass")

            val request = Request.Builder()
                    .url(builder.toString())
                    .build()
            try {
                val response = client.newCall(request).execute()
                val resStr = response.body().string()
                runOnUiThread { text.text = resStr }

            } catch (e: IOException) {
                e.printStackTrace()
            }
        }
    }
}