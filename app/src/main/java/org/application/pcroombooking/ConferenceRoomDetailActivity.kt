package org.application.pcroombooking

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.application.pcroombooking.domain.ConferenceRoomReservation
import org.application.pcroombooking.dto.ConferenceRoomReservationGetRequest
import org.application.pcroombooking.dto.ConferenceRoomReservationGetResponse
import org.application.pcroombooking.recyclerView.adapter.ConferenceRoomReservationAdapter
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class ConferenceRoomDetailActivity : AppCompatActivity() {

    lateinit var conferenceRoomDetailActcroomNameText: TextView
    lateinit var conferenceRoomDetailActDateText: TextView
    lateinit var conferenceRoomDetailActSwipeRefreshLayout: SwipeRefreshLayout
    lateinit var conferenceRoomDetailActRecyclerView: RecyclerView
    lateinit var conferenceRoomDetailActReserveButton: Button
    lateinit var conferenceRoomReservationRecyclerViewAdapter: RecyclerView.Adapter<ConferenceRoomReservationAdapter.Holder>
    lateinit var conferenceRoomReservationLinearLayoutManager: LinearLayoutManager
    lateinit var selectedDate: String
    lateinit var conferenceRoomName: String

    var reservationData: MutableList<ConferenceRoomReservation> = mutableListOf()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_conference_room_detail)

        val intent = getIntent()
        conferenceRoomName = intent.getStringExtra("ConferenceRoomName").toString()

        initView(this@ConferenceRoomDetailActivity)

        conferenceRoomDetailActDateText.setOnClickListener {
            var dateList = selectedDate.split("-")

//            val cal = Calendar.getInstance()    //캘린더뷰 만들기
            val dateSetListener =
                DatePickerDialog.OnDateSetListener { view, year, month, dayOfMonth ->
                    conferenceRoomDetailActDateText.text = "${year}년 ${month + 1}월 ${dayOfMonth}일"
                    selectedDate = "${year}-${month + 1}-${dayOfMonth}"

                    // 여기서 retrofit 불러와서 ui 업데이트
                    Log.d("ConferenceRoomDetailAct", "날짜 변경 완료")
                }
            DatePickerDialog(this,
                dateSetListener,
                dateList[0].toInt(),
                dateList[1].toInt() - 1,
                dateList[2].toInt()).show()
            Log.d("ConferenceRoomDetailAct", "날짜 변경 실행")
        }

    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun initView(activity: Activity) {
        conferenceRoomDetailActcroomNameText =
            activity.findViewById(R.id.activity_conferenceroom_detail_croomName_text)
        conferenceRoomDetailActDateText =
            activity.findViewById(R.id.activity_conferenceroom_detail_date_text)
        conferenceRoomDetailActSwipeRefreshLayout =
            activity.findViewById(R.id.activity_conferenceroom_detail_swipeRefreshLayout)
        conferenceRoomDetailActRecyclerView =
            activity.findViewById(R.id.activity_conferenceroom_detail_recyclerView)
        conferenceRoomDetailActReserveButton =
            activity.findViewById(R.id.activity_conferenceroom_detail_reserve_button)

        if (intent.hasExtra("ConferenceRoomName")) {
            conferenceRoomDetailActcroomNameText.text = intent.getStringExtra("ConferenceRoomName")
        }

        var todayDate: LocalDate = LocalDate.now()
        val formatter = DateTimeFormatter.ofPattern("yyyy년 MM월 dd일")
        val formatter2 = DateTimeFormatter.ofPattern("yyyy-MM-dd")
        val formattedDate = todayDate.format(formatter)
        selectedDate = todayDate.format(formatter2)

        conferenceRoomDetailActDateText.text = formattedDate
    }

    fun initRecyclerView(
        conferenceRoomReservationList: List<ConferenceRoomReservation>,
        activity: Activity,
    ) {
        conferenceRoomReservationRecyclerViewAdapter =
            ConferenceRoomReservationAdapter(conferenceRoomReservationList)
        conferenceRoomReservationLinearLayoutManager = LinearLayoutManager(activity)
        conferenceRoomReservationRecyclerViewAdapter.notifyDataSetChanged()

        conferenceRoomDetailActRecyclerView.adapter = conferenceRoomReservationRecyclerViewAdapter
        conferenceRoomDetailActRecyclerView.layoutManager =
            conferenceRoomReservationLinearLayoutManager
        conferenceRoomDetailActRecyclerView.setHasFixedSize(true)
    }

    fun initData() {
        for (i in 0 until 16) {
            reservationData.add(ConferenceRoomReservation(conferenceRoomName, i + 8, i + 9))
        }

    }

//    fun getConferenceRoomReservation(retrofitService: RetrofitService) {
//        retrofitService.getConferenceRoomReservationList(ConferenceRoomReservationGetRequest(conferenceRoomName, selectedDate))
//            .enqueue(object : Callback<ConferenceRoomReservationGetResponse> {
//                override fun onFailure(call: Call<ConferenceRoomReservationGetResponse>, t: Throwable) {
//                    //todo 실패처리
//
//                    Log.d("ConferenceRoomDetailAct", "get pcroom onfailure: error by network!!!!!")
//                    Log.d("ConferenceRoomDetailAct", t.toString())
//                }
//
//                override fun onResponse(
//                    call: Call<ConferenceRoomReservationGetResponse>,
//                    response: Response<ConferenceRoomReservationGetResponse>,
//                ) {
//                    //todo 성공처리
//
//                    if (response.isSuccessful.not()) {
//                        return
//                    }
//
//                    response.body()?.let {
//                        if (it.responseHttpStatus == 200 && it.responseCode == 2005) {
////                            fragmentPCRoomList.addAll(it.pcRooms)
//                            initRecyclerView(it.pcRooms)
////                            Log.d("PCRoomFragment", "retrofit" + fragmentPCRoomList.toString())
//                            Toast.makeText(mainActivity,
//                                it.responseMessage,
//                                Toast.LENGTH_SHORT).show()
//                        } else {
//                            Log.d("ConferenceRoomDetailAct", "get pcroom onfailure: error by server!!!!!")
//                            Log.d("ConferenceRoomDetailAct",
//                                "httpStatus " + it.responseHttpStatus.toString())
//                            Log.d("ConferenceRoomDetailAct", "responseCode " + it.responseCode.toString())
//                            Log.d("ConferenceRoomDetailAct", "result " + it.result)
//                            Log.d("ConferenceRoomDetailAct", "responseMessage" + it.responseMessage)
//
//                            Toast.makeText(mainActivity,
//                                it.responseMessage,
//                                Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            })
//    }

}