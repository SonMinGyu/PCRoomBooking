package org.application.pcroombooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.application.pcroombooking.domain.PCRoom
import org.application.pcroombooking.dto.PCRoomResponse
import org.application.pcroombooking.recyclerView.adapter.AdminPCRoomAdapter
import org.application.pcroombooking.recyclerView.adapter.PCRoomAdapter
import org.application.pcroombooking.recyclerView.decorator.VerticalDecorator
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminPCRoomActivity : AppCompatActivity() {

    lateinit var adminPCRoomRecyclerView: RecyclerView
    lateinit var adminPCRoomLinearLayoutManager: LinearLayoutManager
    lateinit var adminPCRoomRecyclerViewAdapter: RecyclerView.Adapter<AdminPCRoomAdapter.Holder>
    lateinit var adminPCRoomSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var adminPCRoomAddPCRoomButton: Button

    val retrofitService: RetrofitService = MasterApplication.retrofitService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_pcroom)

        initView()
        getAdminPCRoomList(retrofitService)
        adminPCRoomRecyclerView.addItemDecoration(VerticalDecorator(7))

        adminPCRoomSwipeRefreshLayout.setOnRefreshListener {
            adminPCRoomRecyclerView.adapter = null
            getAdminPCRoomList(retrofitService)
            adminPCRoomSwipeRefreshLayout.isRefreshing = false
        }

        adminPCRoomAddPCRoomButton.setOnClickListener {
            var intent = Intent(this, AdminAddPCRoomActivity::class.java)
            startActivity(intent)
        }
    }

    fun initView() {
        adminPCRoomRecyclerView = findViewById(R.id.activity_admin_pcroom_recyclerView)
        adminPCRoomSwipeRefreshLayout = findViewById(R.id.activity_admin_pcroom_swipeRefreshLayout)
        adminPCRoomAddPCRoomButton = findViewById(R.id.activity_admin_pcroom_add_pcroom_button)
    }

    fun initRecyclerView(adminPCRoomList: List<PCRoom>) {
        adminPCRoomRecyclerViewAdapter =
            AdminPCRoomAdapter(adminPCRoomList, this, applicationContext, menuInflater)
        adminPCRoomLinearLayoutManager = LinearLayoutManager(this)
        adminPCRoomRecyclerViewAdapter.notifyDataSetChanged()

        adminPCRoomRecyclerView.adapter = adminPCRoomRecyclerViewAdapter
        adminPCRoomRecyclerView.layoutManager = adminPCRoomLinearLayoutManager
        adminPCRoomRecyclerView.setHasFixedSize(true)
    }

    fun getAdminPCRoomList(retrofitService: RetrofitService) {
        retrofitService.getPCRoomList()
            .enqueue(object : Callback<PCRoomResponse> {
                override fun onFailure(call: Call<PCRoomResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("AdminPCRoomActivity",
                        "get admin pcroom onfailure: error by network!!!!!")
                    Log.d("AdminPCRoomActivity", t.toString())
                }

                override fun onResponse(
                    call: Call<PCRoomResponse>,
                    response: Response<PCRoomResponse>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2005) {
//                            fragmentPCRoomList.addAll(it.pcRooms)
                            initRecyclerView(it.pcRooms)
//                            Log.d("PCRoomFragment", "retrofit" + fragmentPCRoomList.toString())
                            Toast.makeText(this@AdminPCRoomActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("AdminPCRoomActivity",
                                "get admin pcroom onfailure: error by server!!!!!")
                            Log.d("AdminPCRoomActivity",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("AdminPCRoomActivity",
                                "responseCode " + it.responseCode.toString())
                            Log.d("AdminPCRoomActivity", "result " + it.result)
                            Log.d("AdminPCRoomActivity", "responseMessage" + it.responseMessage)

                            var emptyPCRoomList = mutableListOf<PCRoom>()
                            initRecyclerView(emptyPCRoomList)
                            Toast.makeText(this@AdminPCRoomActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }
}