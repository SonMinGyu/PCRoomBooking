package org.application.pcroombooking.fragment

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.application.pcroombooking.MainActivity
import org.application.pcroombooking.R
import org.application.pcroombooking.recyclerView.adapter.PCRoomAdapter
import org.application.pcroombooking.domain.PCRoom
import org.application.pcroombooking.domain.Seat
import org.application.pcroombooking.dto.PCRoomResponse
import org.application.pcroombooking.recyclerView.decorator.VerticalDecorator
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PCRoomFragment: Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var pcroomRecyclerView: RecyclerView
    lateinit var pcroomLinearLayoutManager: LinearLayoutManager
    lateinit var pcroomRecyclerViewAdapter: RecyclerView.Adapter<PCRoomAdapter.Holder>
//    var fragmentPCRoomList: MutableList<PCRoom> = mutableListOf()
    var fragmentPCRoomList2: MutableList<PCRoom> = mutableListOf()
    val retrofitService: RetrofitService = MasterApplication.retrofitService


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        if(!fragmentPCRoomList.isNullOrEmpty()) {
//            fragmentPCRoomList.clear()
//        }
//        getPCRoomList(retrofitService, mainActivity)

        var seat: Seat = Seat("A-15")
        var seats: MutableSet<Seat> = mutableSetOf<Seat>()
        seats.add(seat)

        val tempPCRoom:PCRoom = PCRoom("6층 PC실", 208, 6,
            20, 10, 0, 6,
            4, 10, 0,
            8, 2, seats, true)
        fragmentPCRoomList2.add(tempPCRoom)
        Log.d("MainActivity", fragmentPCRoomList2.toString())

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_pcroom, container, false)
        pcroomRecyclerView = view.findViewById(R.id.fragment_pcroom_recyclerView)

        return view
    }

    override fun onStart() {
        super.onStart()

        // view control
        initView(fragmentPCRoomList2)

    }

    fun initView(fragmentPCRoomList: List<PCRoom>) {
        Log.d("MainActivity", "initView 실행")
        pcroomRecyclerViewAdapter = PCRoomAdapter(fragmentPCRoomList)
        pcroomLinearLayoutManager = LinearLayoutManager(mainActivity)

        pcroomRecyclerView.adapter = pcroomRecyclerViewAdapter
        pcroomRecyclerView.layoutManager = pcroomLinearLayoutManager
        pcroomRecyclerView.addItemDecoration(VerticalDecorator(7))
        pcroomRecyclerView.setHasFixedSize(true)
    }

    fun getPCRoomList(retrofitService: RetrofitService, activity: Activity) {
        retrofitService.getPCRoomList()
            .enqueue(object : Callback<PCRoomResponse> {
                override fun onFailure(call: Call<PCRoomResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("PCRoomFragment", "get pcroom onfailure: error by network!!!!!")
                    Log.d("PCRoomFragment", t.toString())
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
//                            fragmentPCRoomList = it.pcroomList
                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("PCRoomFragment", "get pcroom onfailure: error by server!!!!!")
                            Log.d("PCRoomFragment",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("PCRoomFragment", "responseCode " + it.responseCode.toString())
                            Log.d("PCRoomFragment", "result " + it.result)
                            Log.d("PCRoomFragment", "responseMessage" + it.responseMessage)

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

}