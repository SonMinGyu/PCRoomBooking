package org.application.pcroombooking

import android.app.Activity
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.application.pcroombooking.dto.PCRoomAddRequest
import org.application.pcroombooking.dto.PCRoomAddResponse
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class PCRoomDetailActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var layout: ViewGroup
    lateinit var seatViewList: MutableList<TextView>

    lateinit var seatLayout: LinearLayout
    lateinit var seatLayoutParams: LinearLayout.LayoutParams


    val retrofitService: RetrofitService = MasterApplication.retrofitService

    // 좌석 배치하는 list
    var seatList: MutableList<MutableList<String>> = mutableListOf()

    // 추후 2차원 배열로
    var seats: String = ("_UUUUUUAAAAARRRR_/"
            + "_________________/"
            + "UU__AAAARRRRR__RR/"
            + "UU__UUUAAAAAA__AA/"
            + "AA__AAAAAAAAA__AA/"
            + "AA__AARUUUURR__AA/"
            + "UU__UUUA_RRRR__AA/"
            + "AA__AAAA_RRAA__UU/"
            + "AA__AARR_UUUU__RR/"
            + "AA__UUAA_UURR__RR/"
            + "_________________/"
            + "UU_AAAAAAAUUUU_RR/"
            + "RR_AAAAAAAAAAA_AA/"
            + "AA_UUAAAAAUUUU_AA/"
            + "AA_AAAAAAUUUUU_AA/"
            + "_________________/")

    var seatSize: Int = 100
    var seatGap: Int = 10

    val STATUS_AVAILABLE = 1
    val STATUS_INUSE = 2
    val STATUS_BROKEN = 3

    var selectedIds: String = ""
    var selectedSeatId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pcroom_detail)

        var intent = getIntent()
        if (intent.hasExtra("PCRoomName")) {
            intent.getStringExtra("PCRoomName")?.let { Log.d("PCRoomDetailActivity", it) }
        }

        initView()
        seats = "/" + seats
        createLinearLayout(this@PCRoomDetailActivity)
        addSeat(this@PCRoomDetailActivity)
    }

    override fun onClick(view: View?) {
        if (view != null) {
            if (view.tag == STATUS_AVAILABLE) {
                if (selectedSeatId == -1) {
                    selectedSeatId = view.id
                    Log.d("PCRoomDetailActivity", "selected view id is ${selectedSeatId}")
                } else {
                    selectedSeatId = -1
                    Log.d("PCRoomDetailActivity", "selected view id is ${selectedSeatId}")
                }
            } else if (view.tag == STATUS_INUSE) {
                Toast.makeText(this, "SeatID: ${view.id} / Status: INUSE", Toast.LENGTH_SHORT)
                    .show()
            } else if (view.tag == STATUS_BROKEN) {
                Toast.makeText(this, "SeatID: ${view.id} / Status: BROKEN", Toast.LENGTH_SHORT)
                    .show()
            }
            Log.d("PCRoomDetailActivity", "clicked view id is ${view.id}")
        }
    }

    fun initView() {
        layout = findViewById(R.id.activity_pcroom_detail_horizontal_scrollView)
    }

    fun createLinearLayout(activity: Activity) {
        // scrollView 인 layout에 linearLayout인 seatLayout 추가
        seatLayout = LinearLayout(activity)
        seatLayoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT)
        seatLayout.orientation = LinearLayout.VERTICAL
        seatLayout.layoutParams = seatLayoutParams
        seatLayout.setPadding(8 * seatGap, 8 * seatGap, 8 * seatGap, 8 * seatGap)
        layout.addView(seatLayout)
    }

    fun addSeat(activity: Activity) {
        lateinit var newLayout: LinearLayout
        seatViewList = mutableListOf()

        var seatNumberCount: Int = 0

        for (i in seats.indices) {
            if (seats[i] == '/') {
                newLayout = LinearLayout(activity)
                newLayout.orientation = LinearLayout.HORIZONTAL
                seatLayout.addView(newLayout)
            } else if (seats[i] == 'U') {
                seatNumberCount++
                val view = TextView(activity)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGap)
                view.id = seatNumberCount
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.reserved_icon)
                view.setText(seatNumberCount.toString() + "")
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.WHITE)
                view.tag = STATUS_INUSE
                newLayout.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seats[i] == 'A') {
                seatNumberCount++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGap)
                view.id = seatNumberCount
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.screen_icon)
                view.setText(seatNumberCount.toString() + "")
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.BLACK)
                view.tag = STATUS_AVAILABLE
                newLayout.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seats[i] == 'R') {
                seatNumberCount++
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                view.layoutParams = layoutParams
                view.setPadding(0, 0, 0, 2 * seatGap)
                view.id = seatNumberCount
                view.gravity = Gravity.CENTER
                view.setBackgroundResource(R.drawable.broken_icon)
                view.setText(seatNumberCount.toString() + "")
                view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                view.setTextColor(Color.WHITE)
                view.tag = STATUS_BROKEN
                newLayout.addView(view)
                seatViewList.add(view)
                view.setOnClickListener(this)
            } else if (seats[i] == '_') {
                val view = TextView(this)
                val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                view.layoutParams = layoutParams
                view.setBackgroundColor(Color.TRANSPARENT)
                view.setText("")
                newLayout.addView(view)
            }
        }
    }

    fun getPCRoom(retrofitService: RetrofitService) {
        val addPCRoomRequest = PCRoomAddRequest(pcroomName, pcroomBuildingNumber,
            pcroomLayer, pcroomLine, pcroomRow, seatsStr, pcSeatNumber + notebookSeatNumber, pcSeatNumber, notebookSeatNumber, true)

        retrofitService.addPCRoom(addPCRoomRequest)
            .enqueue(object : Callback<PCRoomAddResponse> {
                override fun onFailure(call: Call<PCRoomAddResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("AdminAddPCRoomDetailAct", "add pcroom onfailure: error by network!!!!!")
                    Log.d("AdminAddPCRoomDetailAct", t.toString())
                }

                override fun onResponse(
                    call: Call<PCRoomAddResponse>,
                    response: Response<PCRoomAddResponse>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2009) {
//                            fragmentPCRoomList.addAll(it.pcRooms)
//                            Log.d("PCRoomFragment", "retrofit" + fragmentPCRoomList.toString())
                            Toast.makeText(this@AdminAddPCRoomDetailActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("AdminAddPCRoomDetailAct", "add pcroom onfailure: error by server!!!!!")
                            Log.d("AdminAddPCRoomDetailAct",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("AdminAddPCRoomDetailAct", "responseCode " + it.responseCode.toString())
                            Log.d("AdminAddPCRoomDetailAct", "result " + it.result)
                            Log.d("AdminAddPCRoomDetailAct", "responseMessage" + it.responseMessage)

                            Toast.makeText(this@AdminAddPCRoomDetailActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }
}