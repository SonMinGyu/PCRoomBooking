package org.application.pcroombooking

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import org.application.pcroombooking.domain.PCRoom
import org.application.pcroombooking.dto.PCRoomAddRequest
import org.application.pcroombooking.dto.PCRoomAddResponse
import org.application.pcroombooking.dto.PCRoomResponse
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AdminAddPCRoomDetailActivity : AppCompatActivity(), View.OnClickListener {
    lateinit var layout: ViewGroup
    lateinit var seatViewList: MutableList<TextView>

    lateinit var seatLayout: LinearLayout
    lateinit var seatLayoutParams: LinearLayout.LayoutParams
    lateinit var adminAddPCRoomDetailPCRoomName: TextView
    lateinit var adminAddPCRoomDetailPCRoomRestPC: TextView
    lateinit var adminAddPCRoomDetailPCRoomRestNotebook: TextView
    lateinit var adminAddPCRoomDetailPCRoomSaveButton: Button

    val retrofitService: RetrofitService = MasterApplication.retrofitService

    var pcroomName: String = "PC실"
    var pcroomBuildingNumber: Int = 0
    var pcroomLayer: Int = 0
    var pcSeatNumber: Int = 0
    var notebookSeatNumber: Int = 0 
    var pcroomRow: Int = 0
    var pcroomLine: Int = 0

    // 좌석 배치하는 list
    var seatList: MutableList<MutableList<String>> = mutableListOf()

    var seatSize: Int = 100
    var seatGap: Int = 10

    val STATUS_NULL = "N"
    val STATUS_EMPTY_SEAT = "E"
    val STATUS_PC_SEAT = "P"
    val STATUS_NOTEBOOK_SEAT = "B"

    var selectedIds: String = ""
    var selectedSeatId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_pcroom_detail)

        var actIntent: Intent = intent

        initView()
        getIntentValue(actIntent)
        initSeatList()

        createLinearLayout(this@AdminAddPCRoomDetailActivity)
        addSeatBySeatList(this@AdminAddPCRoomDetailActivity)

        adminAddPCRoomDetailPCRoomSaveButton.setOnClickListener {
            saveSeats()
        }
    }

    override fun onClick(view: View?) {
        if (view != null) {
            if (view.tag == STATUS_EMPTY_SEAT) {
//                view.id = seatNumberCount
                view.setBackgroundResource(R.drawable.screen_icon)
                view.tag = STATUS_PC_SEAT
            } else if(view.tag == STATUS_PC_SEAT) {
//                view.id = seatNumberCount
                view.setBackgroundResource(R.drawable.notebook_icon)
                view.tag = STATUS_NOTEBOOK_SEAT
            } else if(view.tag == STATUS_NOTEBOOK_SEAT) {
//                view.id = seatNumberCount
                view.setBackgroundColor(Color.TRANSPARENT)
                view.tag = STATUS_NULL
            } else if(view.tag == STATUS_NULL) {
//                view.id = seatNumberCount
                view.setBackgroundResource(R.drawable.empty_seat_icon)
                view.tag = STATUS_EMPTY_SEAT
            }
            Log.d("AdminPCRoomDetailAct", "clicked view id is ${view.id}")
        }
    }

    fun initView() {
        layout = findViewById(R.id.activity_admin_add_pcroom_detail_horizontal_scrollView)
        adminAddPCRoomDetailPCRoomName = findViewById(R.id.activity_admin_add_pcroom_detail_pcroomName)
        adminAddPCRoomDetailPCRoomRestPC = findViewById(R.id.activity_admin_add_pcroom_detail_restPC)
        adminAddPCRoomDetailPCRoomRestNotebook = findViewById(R.id.activity_admin_add_pcroom_detail_restNotebook)
        adminAddPCRoomDetailPCRoomSaveButton = findViewById(R.id.activity_admin_add_pcroom_detail_save_button)
    }

    fun getIntentValue(actIntent: Intent) {
        if(actIntent.hasExtra("CreatePCRoomPCRoomName")) {
            pcroomName = actIntent.getStringExtra("CreatePCRoomPCRoomName").toString()
        }
        pcSeatNumber = actIntent.getIntExtra("CreatePCRoomPCSeat", 0)
        notebookSeatNumber = actIntent.getIntExtra("CreatePCRoomNotebookSeat", 0)
        pcroomLine = actIntent.getIntExtra("CreatePCRoomLine", 0)
        pcroomRow = actIntent.getIntExtra("CreatePCRoomRow", 0)

        if(actIntent.hasExtra("CreatePCRoomBuildingNumber")) {
            var pcroomBuildingNumberStr = actIntent.getStringExtra("CreatePCRoomBuildingNumber").toString()
            var pcroomBuildingNumberList = pcroomBuildingNumberStr.split("관")
            pcroomBuildingNumber = pcroomBuildingNumberList[0].toInt()
        }

        if(actIntent.hasExtra("CreatePCRoomLayer")) {
            var pcroomLayerStr = actIntent.getStringExtra("CreatePCRoomLayer").toString()
            var pcroomLayerList = pcroomLayerStr.split("층")
            if(pcroomLayerList[0].startsWith("B")) {
                pcroomLayerList[0].replace("B", "-")
                pcroomLayer = pcroomLayerList[0].toInt()
            } else {
                pcroomLayer = pcroomLayerList[0].toInt()
            }
        }
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

    fun initSeatList() {
        for(i in 0 until pcroomRow) {
            var tempList: MutableList<String> = mutableListOf()
            for(j in 0 until pcroomLine) {
                tempList.add("E")
            }
            seatList.add(tempList)
        }
    }

    fun addSeatBySeatList(activity: Activity) {
        lateinit var newLayout: LinearLayout
        seatViewList = mutableListOf()

        var seatNumberCount: Int = 0

        for(row in seatList) {
            newLayout = LinearLayout(activity)
            newLayout.orientation = LinearLayout.HORIZONTAL
            seatLayout.addView(newLayout)

            for(seat in row) {
                if (seat == "E") {
                    seatNumberCount++
                    val view = TextView(this)
                    val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGap)
                    view.id = seatNumberCount
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.empty_seat_icon)
                    view.setText(seatNumberCount.toString() + "")
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                    view.setTextColor(Color.WHITE)
                    view.tag = STATUS_EMPTY_SEAT
                    newLayout.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener(this)
                }
//                else if (seat == "_") {
//                    val view = TextView(this)
//                    val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
//                    layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
//                    view.layoutParams = layoutParams
//                    view.setBackgroundColor(Color.TRANSPARENT)
//                    view.setText("")
//                    newLayout.addView(view)
//                }
            }
        }
        newLayout = LinearLayout(activity)
        newLayout.orientation = LinearLayout.HORIZONTAL
        seatLayout.addView(newLayout)
    }

    fun saveSeats() {
        var seatStr = ""
        for(view in seatViewList) {
            seatStr += view.tag.toString()
        }

        Log.d("AdminAddPCRoomDetailAct", seatStr)

        addPCRoom(retrofitService, seatStr)
    }

    fun addPCRoom(retrofitService: RetrofitService, seatsStr: String) {
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