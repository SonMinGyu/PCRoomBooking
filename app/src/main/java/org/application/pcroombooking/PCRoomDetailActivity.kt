package org.application.pcroombooking

import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.graphics.Color
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
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.application.pcroombooking.dialog.CustomDialog
import org.application.pcroombooking.dto.*
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
    var pcroomName = "PC실"

    var seatSize: Int = 100
    var seatGap: Int = 10

    val STATUS_NULL = "N"
    val STATUS_EMPTY_SEAT = "E"
    val STATUS_PC_SEAT = "P"
    val STATUS_NOTEBOOK_SEAT = "B"
    val STATUS_BOOKED_PC_SEAT = "K"
    val STATUS_INUSE_PC_SEAT = "C"
    val STATUS_BOOKED_NOTEBOOK_SEAT = "T"
    val STATUS_INUSE_NOTEBOOK_SEAT = "O"

    var selectedIds: String = ""
    var selectedSeatId: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pcroom_detail)

        var intent = getIntent()
        if (intent.hasExtra("PCRoomName")) {
            pcroomName = intent.getStringExtra("PCRoomName").toString()
            intent.getStringExtra("PCRoomName")?.let { Log.d("PCRoomDetailActivity", it) }
        }

        initView()
        getPCRoom(retrofitService, pcroomName)

//        createLinearLayout(this@PCRoomDetailActivity)
//        addSeat(this@PCRoomDetailActivity)
    }

    override fun onClick(view: View?) {
        val dialog = CustomDialog(this)

        if (view != null) {
            if (view.tag == STATUS_PC_SEAT) {
                // intent로 activity실행 후 예약하시겠습니까 알림창 -> 예약
                dialog.showDialog(pcroomName, "PC", view.id.toString(), seatViewList, view.id, this)
                dialog.setOnClickListener(object : CustomDialog.ClickListener {
                    override fun resultData(result: Boolean) {
                        if (result) {
                            scanQRCode()
//                            view.tag = STATUS_INUSE_PC_SEAT
//                            view.setBackgroundResource(R.drawable.screen_use_icon)
                        } else {
                            view.tag = STATUS_BOOKED_PC_SEAT
                            view.setBackgroundResource(R.drawable.screen_booked_icon)
                        }
                    }
                })
                Log.d("PCRoomDetailActivity", "dialog")
//                view.id = seatNumberCount
//                view.setBackgroundResource(R.drawable.notebook_icon)
//                view.tag = STATUS_NOTEBOOK_SEAT

            } else if (view.tag == STATUS_NOTEBOOK_SEAT) {
                // intent로 activity실행 후 예약하시겠습니까 알림창 -> 예약
                dialog.showDialog(pcroomName, "NOTEBOOK", view.id.toString(), seatViewList, view.id, this)
                dialog.setOnClickListener(object : CustomDialog.ClickListener {
                    override fun resultData(result: Boolean) {
                        if (result) {
                            scanQRCode()
//                            view.tag = STATUS_INUSE_NOTEBOOK_SEAT
//                            view.setBackgroundResource(R.drawable.notebook_use_icon)
                        } else {
                            view.tag = STATUS_BOOKED_NOTEBOOK_SEAT
                            view.setBackgroundResource(R.drawable.notebook_booked_icon)
                        }
                    }
                })

            } else if (view.tag == STATUS_BOOKED_PC_SEAT && view.tag == STATUS_BOOKED_NOTEBOOK_SEAT) {
                Toast.makeText(this, "이미 예약된 좌석입니다.", Toast.LENGTH_SHORT).show()
                Log.d("PCRoomDetailDetailAct", "이미 예약된 좌석입니다.")
            } else if (view.tag == STATUS_BOOKED_NOTEBOOK_SEAT && view.tag == STATUS_INUSE_NOTEBOOK_SEAT) {
                Toast.makeText(this, "이미 사용중인 좌석입니다.", Toast.LENGTH_SHORT).show()
                Log.d("PCRoomDetailDetailAct", "이미 사용중인 좌석입니다.")
            }
            Log.d("PCRoomDetailDetailAct", "clicked view id is ${view.id}")
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

    fun addSeat(activity: Activity, seatsStr: String, pcroomLine: Int, pcroomRow: Int) {
        lateinit var newLayout: LinearLayout
        seatViewList = mutableListOf()

        var seatNumberCount: Int = 0

        // 좌석 배치하는 list
        Log.d("PCRoomFragment", "seatsStr " + seatsStr)

        var seatCharList: MutableList<Char> = seatsStr.toMutableList()
        var seatList: MutableList<MutableList<String>> = mutableListOf()

        for (i in 0 until pcroomRow) {
            var tempList: MutableList<String> = mutableListOf()
            for (j in 0 until pcroomLine) {
                tempList.add(seatCharList[i * pcroomLine + j].toString())
            }
            seatList.add(tempList)
        }

        for (row in seatList) {
            newLayout = LinearLayout(activity)
            newLayout.orientation = LinearLayout.HORIZONTAL
            seatLayout.addView(newLayout)

            for (seat in row) {
                if (seat == STATUS_EMPTY_SEAT) {
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
                } else if (seat == STATUS_PC_SEAT) {
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
                    view.setTextColor(Color.WHITE)
                    view.tag = STATUS_PC_SEAT
                    newLayout.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener(this)
                } else if (seat == STATUS_NOTEBOOK_SEAT) {
                    seatNumberCount++
                    val view = TextView(this)
                    val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGap)
                    view.id = seatNumberCount
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.notebook_icon)
                    view.setText(seatNumberCount.toString() + "")
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                    view.setTextColor(Color.WHITE)
                    view.tag = STATUS_NOTEBOOK_SEAT
                    newLayout.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener(this)
                } else if (seat == STATUS_BOOKED_PC_SEAT) {
                    seatNumberCount++
                    val view = TextView(this)
                    val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGap)
                    view.id = seatNumberCount
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.screen_booked_icon)
                    view.setText(seatNumberCount.toString() + "")
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                    view.setTextColor(Color.WHITE)
                    view.tag = STATUS_BOOKED_PC_SEAT
                    newLayout.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener(this)
                } else if (seat == STATUS_INUSE_PC_SEAT) {
                    seatNumberCount++
                    val view = TextView(this)
                    val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGap)
                    view.id = seatNumberCount
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.screen_use_icon)
                    view.setText(seatNumberCount.toString() + "")
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                    view.setTextColor(Color.WHITE)
                    view.tag = STATUS_INUSE_PC_SEAT
                    newLayout.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener(this)
                } else if (seat == STATUS_BOOKED_NOTEBOOK_SEAT) {
                    seatNumberCount++
                    val view = TextView(this)
                    val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGap)
                    view.id = seatNumberCount
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.notebook_booked_icon)
                    view.setText(seatNumberCount.toString() + "")
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                    view.setTextColor(Color.WHITE)
                    view.tag = STATUS_BOOKED_NOTEBOOK_SEAT
                    newLayout.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener(this)
                } else if (seat == STATUS_INUSE_NOTEBOOK_SEAT) {
                    seatNumberCount++
                    val view = TextView(this)
                    val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                    view.layoutParams = layoutParams
                    view.setPadding(0, 0, 0, 2 * seatGap)
                    view.id = seatNumberCount
                    view.gravity = Gravity.CENTER
                    view.setBackgroundResource(R.drawable.notebook_use_icon)
                    view.setText(seatNumberCount.toString() + "")
                    view.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 9f)
                    view.setTextColor(Color.WHITE)
                    view.tag = STATUS_INUSE_NOTEBOOK_SEAT
                    newLayout.addView(view)
                    seatViewList.add(view)
                    view.setOnClickListener(this)
                } else if (seat == STATUS_NULL) {
                    val view = TextView(this)
                    val layoutParams = LinearLayout.LayoutParams(seatSize, seatSize)
                    layoutParams.setMargins(seatGap, seatGap, seatGap, seatGap)
                    view.layoutParams = layoutParams
                    view.setBackgroundColor(Color.TRANSPARENT)
                    view.setText("")
                    view.tag = STATUS_NULL
                    newLayout.addView(view)
                    seatViewList.add(view)
                }
            }
        }
        newLayout = LinearLayout(activity)
        newLayout.orientation = LinearLayout.HORIZONTAL
        seatLayout.addView(newLayout)
    }

    fun getPCRoom(retrofitService: RetrofitService, pcroomName: String) {
        val getPCRoomRequest = PCRoomGetRequest(pcroomName)

        retrofitService.getPCRoom(getPCRoomRequest)
            .enqueue(object : Callback<PCRoomGetResponse> {
                override fun onFailure(call: Call<PCRoomGetResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("PCRoomDetailActivity", "get pcroom onfailure: error by network!!!!!")
                    Log.d("PCRoomDetailActivity", t.toString())
                }

                override fun onResponse(
                    call: Call<PCRoomGetResponse>,
                    response: Response<PCRoomGetResponse>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2010) {
//                            fragmentPCRoomList.addAll(it.pcRooms)
//                            Log.d("PCRoomFragment", "retrofit" + fragmentPCRoomList.toString())

                            createLinearLayout(this@PCRoomDetailActivity)
                            addSeat(this@PCRoomDetailActivity,
                                it.pcRoom.seatsStr,
                                it.pcRoom.pcRoomLine,
                                it.pcRoom.pcRoomRow)
                            Toast.makeText(this@PCRoomDetailActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("PCRoomDetailActivity",
                                "get pcroom onfailure: error by server!!!!!")
                            Log.d("PCRoomDetailActivity",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("PCRoomDetailActivity",
                                "responseCode " + it.responseCode.toString())
                            Log.d("PCRoomDetailActivity", "result " + it.result)
                            Log.d("PCRoomDetailActivity", "responseMessage" + it.responseMessage)

                            Toast.makeText(this@PCRoomDetailActivity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    fun scanQRCode() {
        val integrator = IntentIntegrator(this)
        integrator.setBeepEnabled(false)
        integrator.setOrientationLocked(true)
        integrator.setPrompt("QR코드를 찍어주세요.")
        integrator.initiateScan()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val result: IntentResult =
            IntentIntegrator.parseActivityResult(requestCode, resultCode, data)
        if(result != null) {
            if (result.contents == null) {
                Log.e("this", "잘못된 QR코드입니다.")
            } else {
                Log.e("this", result.contents.toString())
            }
        }
        else{
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

}