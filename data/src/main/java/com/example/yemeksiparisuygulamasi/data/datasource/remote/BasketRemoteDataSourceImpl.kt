package com.example.yemeksiparisuygulamasi.data.datasource.remote

import android.content.Context
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.example.yemeksiparisuygulamasi.data.datasource.BasketRemoteDataSource
import com.example.yemeksiparisuygulamasi.domain.entity.Basket
import com.example.yemeksiparisuygulamasi.domain.entity.Food
import com.example.yemeksiparisuygulamasi.domain.entity.ResultData
import kotlinx.coroutines.channels.sendBlocking
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowViaChannel
import org.json.JSONException
import org.json.JSONObject
import javax.inject.Inject

class BasketRemoteDataSourceImpl @Inject constructor() :
    BasketRemoteDataSource {
    override suspend fun addBasket(
        context: Context,
        food: Food,
        counter: Int
    ): Flow<ResultData<Unit>> {
        return flowViaChannel { flowChannel ->
            val webServiceUrl = "http://kasimadalan.pe.hu/yemekler/insert_sepet_yemek.php"
            val requestToUrl = object : StringRequest(
                Method.POST,
                webServiceUrl,
                Response.Listener { responseOfUrl ->
                    flowChannel.sendBlocking(ResultData.Success())
                }, Response.ErrorListener {
                    flowChannel.sendBlocking(ResultData.Failed())
                }) {

                override fun getParams(): MutableMap<String, String> {
                    val parameter = HashMap<String, String>()
                    parameter["yemek_siparis_adet"] = counter.toString()
                    parameter["yemek_id"] = food.id.toString()
                    parameter["yemek_adi"] = food.name
                    parameter["yemek_resim_adi"] = food.image_path
                    parameter["yemek_fiyat"] = food.price.toString()
                    return parameter
                }
            }

            Volley.newRequestQueue(context).add(requestToUrl)
        }
    }

    override suspend fun removeBasket(
        context: Context,
        foodFromBasket: Basket
    ): Flow<ResultData<Unit>> {
        return flowViaChannel { flowChannel ->
            val webServiceUrl = "http://kasimadalan.pe.hu/yemekler/delete_sepet_yemek.php"

            val requestToUrl = object : StringRequest(
                Request.Method.POST,
                webServiceUrl,
                Response.Listener { responseOfUrl ->

                    flowChannel.sendBlocking(ResultData.Success())
                },
                Response.ErrorListener {
                    flowChannel.sendBlocking(ResultData.Failed())
                }) {

                override fun getParams(): MutableMap<String, String> {

                    val parameter = HashMap<String, String>()

                    parameter["yemek_id"] = ((foodFromBasket.food).id).toString()

                    return parameter
                }
            }
            Volley.newRequestQueue(context).add(requestToUrl)
        }
    }

    override suspend fun getBasket(context: Context): Flow<List<Basket>?> {
        return flowViaChannel { flowChannel ->
            val webServiceUrl = "http://kasimadalan.pe.hu/yemekler/tum_sepet_yemekler.php"

            val requestToUrl = StringRequest(Request.Method.GET, webServiceUrl, { responseOfUrl ->

            val basketList: ArrayList<Basket> = arrayListOf()
                    try {
                        val jsonObj = JSONObject(responseOfUrl)
                        val basketJsonArray = jsonObj.getJSONArray("sepet_yemekler")


                        for (index in 0 until basketJsonArray.length()) {

                            val basketJsonObject = basketJsonArray.getJSONObject(index)

                            val foodId = basketJsonObject.getInt("yemek_id")
                            val foodName = basketJsonObject.getString("yemek_adi")
                            val foodImagePath = basketJsonObject.getString("yemek_resim_adi")
                            val foodPrice = basketJsonObject.getInt("yemek_fiyat")
                            val foodQuantity = basketJsonObject.getInt("yemek_siparis_adet")

                            val food = Food(foodId, foodName, foodImagePath, foodPrice)
                            val basketData = Basket(food, foodQuantity)
                            basketList.add(basketData)
                        }
                        flowChannel.sendBlocking(basketList)

                    } catch (e: JSONException) {
                        flowChannel.sendBlocking(null)
                    }
                }, Response.ErrorListener { flowChannel.sendBlocking(null) }
            )
            Volley.newRequestQueue(context).add(requestToUrl)
        }
    }
}