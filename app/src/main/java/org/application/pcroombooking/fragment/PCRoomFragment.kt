package org.application.pcroombooking.fragment

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
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.application.pcroombooking.MainActivity
import org.application.pcroombooking.R
import org.application.pcroombooking.recyclerView.adapter.PCRoomAdapter
import org.application.pcroombooking.domain.PCRoom
import org.application.pcroombooking.dto.PCRoomsResponse
import org.application.pcroombooking.recyclerView.decorator.VerticalDecorator
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PCRoomFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var pcroomRecyclerView: RecyclerView
    lateinit var pcroomLinearLayoutManager: LinearLayoutManager
    lateinit var pcroomRecyclerViewAdapter: RecyclerView.Adapter<PCRoomAdapter.Holder>
    lateinit var pcroomSwipeRefreshLayout: SwipeRefreshLayout
    val retrofitService: RetrofitService = MasterApplication.retrofitService


    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // view control
        val view = inflater.inflate(R.layout.fragment_pcroom, container, false)
        initView(view)
        getPCRoomList(retrofitService)
        pcroomRecyclerView.addItemDecoration(VerticalDecorator(7))

        return view
    }

    override fun onStart() {
        super.onStart()

        pcroomSwipeRefreshLayout.setOnRefreshListener {
            pcroomRecyclerView.adapter = null
            getPCRoomList(retrofitService)
            pcroomSwipeRefreshLayout.isRefreshing = false
        }
    }

    fun initView(view: View) {
        pcroomRecyclerView = view.findViewById(R.id.fragment_pcroom_recyclerView)
        pcroomSwipeRefreshLayout = view.findViewById(R.id.fragment_pcroom_swipeRefreshLayout)
    }

    fun initRecyclerView(fragmentPCRoomList: List<PCRoom>) {
        pcroomRecyclerViewAdapter = PCRoomAdapter(fragmentPCRoomList, mainActivity)
        pcroomLinearLayoutManager = LinearLayoutManager(mainActivity)
        pcroomRecyclerViewAdapter.notifyDataSetChanged()

        pcroomRecyclerView.adapter = pcroomRecyclerViewAdapter
        pcroomRecyclerView.layoutManager = pcroomLinearLayoutManager
        pcroomRecyclerView.setHasFixedSize(true)
    }

    fun getPCRoomList(retrofitService: RetrofitService) {
        retrofitService.getPCRoomList()
            .enqueue(object : Callback<PCRoomsResponse> {
                override fun onFailure(call: Call<PCRoomsResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("PCRoomFragment", "get pcroom onfailure: error by network!!!!!")
                    Log.d("PCRoomFragment", t.toString())
                }

                override fun onResponse(
                    call: Call<PCRoomsResponse>,
                    response: Response<PCRoomsResponse>,
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
                            Toast.makeText(mainActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("PCRoomFragment", "get pcroom onfailure: error by server!!!!!")
                            Log.d("PCRoomFragment",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("PCRoomFragment", "responseCode " + it.responseCode.toString())
                            Log.d("PCRoomFragment", "result " + it.result)
                            Log.d("PCRoomFragment", "responseMessage" + it.responseMessage)

                            var emptyPCRoomList = mutableListOf<PCRoom>()
                            initRecyclerView(emptyPCRoomList)
                            Toast.makeText(mainActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

}