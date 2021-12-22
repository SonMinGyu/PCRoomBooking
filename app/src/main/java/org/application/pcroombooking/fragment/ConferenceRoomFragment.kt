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
import org.application.pcroombooking.recyclerView.adapter.ConferenceRoomAdapter
import org.application.pcroombooking.domain.ConferenceRoom
import org.application.pcroombooking.dto.ConferenceRoomResponse
import org.application.pcroombooking.recyclerView.decorator.VerticalDecorator
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ConferenceRoomFragment : Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var conferenceRoomRecyclerView: RecyclerView
    lateinit var conferenceRoomLinearLayoutManager: LinearLayoutManager
    lateinit var conferenceRoomRecyclerViewAdapter: RecyclerView.Adapter<ConferenceRoomAdapter.Holder>
    lateinit var conferenceRoomSwipeRefreshLayout: SwipeRefreshLayout
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
        val view = inflater.inflate(R.layout.fragment_conferenceroom, container, false)
        initView(view)
        getConferenceRoomList(retrofitService)
        conferenceRoomRecyclerView.addItemDecoration(VerticalDecorator(7))

        return view
    }

    override fun onStart() {
        super.onStart()

        conferenceRoomSwipeRefreshLayout.setOnRefreshListener {
            conferenceRoomRecyclerView.adapter = null
            getConferenceRoomList(retrofitService)
            conferenceRoomSwipeRefreshLayout.isRefreshing = false
        }
    }

    fun initView(view: View) {
        conferenceRoomRecyclerView = view.findViewById(R.id.fragment_conferenceroom_recyclerView)
        conferenceRoomSwipeRefreshLayout =
            view.findViewById(R.id.fragment_conferenceroom_swipeRefreshLayout)
    }

    fun initRecyclerView(fragmentConferenceRoomList: List<ConferenceRoom>) {
        conferenceRoomRecyclerViewAdapter =
            ConferenceRoomAdapter(fragmentConferenceRoomList, mainActivity)
        conferenceRoomLinearLayoutManager = LinearLayoutManager(mainActivity)
        conferenceRoomRecyclerViewAdapter.notifyDataSetChanged()
        conferenceRoomRecyclerViewAdapter.notifyItemRangeInserted(0, 16)
        conferenceRoomRecyclerViewAdapter.notifyItemRangeRemoved(0, 16)

        conferenceRoomRecyclerView.adapter = conferenceRoomRecyclerViewAdapter
        conferenceRoomRecyclerView.layoutManager = conferenceRoomLinearLayoutManager
        conferenceRoomRecyclerView.setHasFixedSize(true)
    }

    fun getConferenceRoomList(retrofitService: RetrofitService) {
        retrofitService.getConferenceRoomList()
            .enqueue(object : Callback<ConferenceRoomResponse> {
                override fun onFailure(call: Call<ConferenceRoomResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("ConferenceRoomFragment",
                        "get conferenceroom onfailure: error by network!!!!!")
                    Log.d("ConferenceRoomFragment", t.toString())
                }

                override fun onResponse(
                    call: Call<ConferenceRoomResponse>,
                    response: Response<ConferenceRoomResponse>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2006) {
//                            fragmentConferenceRoomList.addAll(it.conferenceRooms)
                            initRecyclerView(it.conferenceRooms)
//                            Log.d("ConferenceRoomFragment", "retrofit" + fragmentConferenceRoomList.toString())
                            Toast.makeText(mainActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("ConferenceRoomFragment",
                                "get conferenceroom onfailure: error by server!!!!!")
                            Log.d("ConferenceRoomFragment",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("ConferenceRoomFragment",
                                "responseCode " + it.responseCode.toString())
                            Log.d("ConferenceRoomFragment", "result " + it.result)
                            Log.d("ConferenceRoomFragment", "responseMessage" + it.responseMessage)

                            var emptyConferenceRoomList = mutableListOf<ConferenceRoom>()
                            initRecyclerView(emptyConferenceRoomList)

                            Toast.makeText(mainActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }
}