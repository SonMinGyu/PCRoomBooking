package org.application.pcroombooking.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.application.pcroombooking.MainActivity
import org.application.pcroombooking.R
import org.application.pcroombooking.recyclerView.adapter.ConferenceRoomAdapter
import org.application.pcroombooking.domain.ConferenceRoom
import org.application.pcroombooking.domain.ConferenceRoomReservation
import org.application.pcroombooking.recyclerView.decorator.VerticalDecorator
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService

class ConferenceRoomFragment: Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var conferenceRoomRecyclerView: RecyclerView
    lateinit var conferenceRoomLinearLayoutManager: LinearLayoutManager
    lateinit var conferenceRoomRecyclerViewAdapter: RecyclerView.Adapter<ConferenceRoomAdapter.Holder>
    //    var fragmentConferenceRoomList: MutableList<PCRoom> = mutableListOf()
    var fragmentConferenceRoomList2: MutableList<ConferenceRoom> = mutableListOf()
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

        var reservation1 = ConferenceRoomReservation("팀플실 1", 208,
            "6층 PC실", 6, 6, 14, 15, "son@naver.com")
        var reservation2 = ConferenceRoomReservation("팀플실 1", 208,
            "6층 PC실", 6, 6, 15, 16, "son@naver.com")
        var reservation3 = ConferenceRoomReservation("팀플실 1", 208,
            "6층 PC실", 6, 6, 16, 17, "son@naver.com")

        var reservationList = mutableListOf<ConferenceRoomReservation>()
        reservationList.add(reservation1)
        reservationList.add(reservation2)
        reservationList.add(reservation3)

        var conferenceRoom = ConferenceRoom("팀플실 1", 208, "6층 PC실", 6, 6, reservationList, false, true)

        fragmentConferenceRoomList2.add(conferenceRoom)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_conferenceroom, container, false)
        conferenceRoomRecyclerView = view.findViewById(R.id.fragment_conferenceroom_recyclerView)

        return view
    }

    override fun onStart() {
        super.onStart()

        // view control
        initView(fragmentConferenceRoomList2)

    }

    fun initView(fragmentConferenceRoomList: List<ConferenceRoom>) {
        Log.d("MainActivity", "initView 실행")
        conferenceRoomRecyclerViewAdapter = ConferenceRoomAdapter(fragmentConferenceRoomList)
        conferenceRoomLinearLayoutManager = LinearLayoutManager(mainActivity)

        conferenceRoomRecyclerView.adapter = conferenceRoomRecyclerViewAdapter
        conferenceRoomRecyclerView.layoutManager = conferenceRoomLinearLayoutManager
        conferenceRoomRecyclerView.addItemDecoration(VerticalDecorator(7))
        conferenceRoomRecyclerView.setHasFixedSize(true)
    }
}