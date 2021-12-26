package org.application.pcroombooking.dialog

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.zxing.integration.android.IntentIntegrator
import com.google.zxing.integration.android.IntentResult
import org.application.pcroombooking.R
import org.application.pcroombooking.domain.Seat
import org.application.pcroombooking.dto.*
import org.application.pcroombooking.retrofit.MasterApplication
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CustomDialog(context: Context) {

    private val dialog = Dialog(context)
    val retrofitService: RetrofitService = MasterApplication.retrofitService

    val STATUS_NULL = "N"
    val STATUS_EMPTY_SEAT = "E"
    val STATUS_PC_SEAT = "P"
    val STATUS_BOOKED_PC_SEAT = "K"
    val STATUS_INUSE_PC_SEAT = "C"
    val STATUS_NOTEBOOK_SEAT = "B"
    val STATUS_BOOKED_NOTEBOOK_SEAT = "T"
    val STATUS_INUSE_NOTEBOOK_SEAT = "O"

    interface ClickListener {
        fun resultData(result: Boolean)
    }

    private lateinit var onClickedListener: ClickListener

    fun setOnClickListener(listener: ClickListener) {
        onClickedListener = listener
    }

    fun showDialog(
        pcroomName: String,
        pcroomType: String,
        seatName: String,
        seatViewList: MutableList<TextView>,
        viewId: Int,
        activity: Activity,
    ) {
        dialog.setContentView(R.layout.dialog_reserve_pc_seat_confirm)
        dialog.window!!.setLayout(WindowManager.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.WRAP_CONTENT)
        dialog.setCanceledOnTouchOutside(true)
        dialog.setCancelable(true)

        val dialogReservePCRoomNameText: TextView =
            dialog.findViewById(R.id.dialog_reserve_pc_seat_confirm_pcroomName)
        val dialogReservePCRoomSeatTypeText: TextView =
            dialog.findViewById(R.id.dialog_reserve_pc_seat_confirm_seatType)
        val dialogReservePCRoomSeatNameText: TextView =
            dialog.findViewById(R.id.dialog_reserve_pc_seat_confirm_seatName)
        val dialogReservePCRoomCancelButton: Button =
            dialog.findViewById(R.id.dialog_reserve_pc_seat_confirm_cancel_button)
        val dialogReservePCRoomReserveButton: Button =
            dialog.findViewById(R.id.dialog_reserve_pc_seat_confirm_reserve_button)
        val dialogReservePCRoomUseButton: Button =
            dialog.findViewById(R.id.dialog_reserve_pc_seat_confirm_use_button)

        dialogReservePCRoomNameText.text = pcroomName
        dialogReservePCRoomSeatTypeText.text = pcroomType
        dialogReservePCRoomSeatNameText.text = seatName

        dialogReservePCRoomCancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialogReservePCRoomReserveButton.setOnClickListener {
            bookSeat(seatViewList, pcroomName, activity, viewId)
        }

        dialogReservePCRoomUseButton.setOnClickListener {
            onClickedListener.resultData(true)
            dialog.dismiss()
//            useSeat(seatViewList, pcroomName, activity, viewId)
        }

        dialog.show()
    }

    // dialog에서 예약 버튼을 눌렀을 때 실행하는 함수
    fun bookSeat(
        seatViewList: MutableList<TextView>,
        pcroomName: String,
        activity: Activity,
        viewId: Int,
    ) {
        var updateSeatName = ""
        var updateViewTag = ""
        var seatsStr = ""

        var count = 0
        for (view in seatViewList) {
            if (view.tag == STATUS_PC_SEAT || view.tag == STATUS_NOTEBOOK_SEAT
                || view.tag == STATUS_BOOKED_PC_SEAT || view.tag == STATUS_INUSE_PC_SEAT
                || view.tag == STATUS_BOOKED_NOTEBOOK_SEAT || view.tag == STATUS_INUSE_NOTEBOOK_SEAT
            ) {
                count++
            }

            if (view.id == viewId) {
                if (view.tag == STATUS_PC_SEAT) {
                    view.tag = STATUS_BOOKED_PC_SEAT
                } else if (view.tag == STATUS_NOTEBOOK_SEAT) {
                    view.tag = STATUS_BOOKED_NOTEBOOK_SEAT
                }
                updateSeatName = "${pcroomName}-${count}"
                updateViewTag = view.tag.toString()
            }
            seatsStr += view.tag.toString()
        }

        // pcroom의 seats 정보를 update -> booked = true
        // seatList와 seats 모두 업데이트해야함
        // 나중에 로그인부터 할 때에는 companion object에서 userEmail 가져오기
//        updateSeat(retrofitService,
//            pcroomName,
//            updateSeatName,
//            "son@naver.com",
//            updateViewTag,
//            true,
//            false,
//            seatViewList,
//            viewId,
//            activity)

        bookOrUseSeat(retrofitService, pcroomName, updateSeatName, "littleking13@naver.com", updateViewTag, true, false, seatsStr, activity)

    }

    // dialog에서 사용 버튼을 눌렀을 때 실행하는 함수, 바로 QR코드 화면으로 넘어가야함, QR정상 작동하면 정보 업데이트 및 추가
//    fun useSeat(seatViewList: MutableList<TextView>, pcroomName: String, activity: Activity, viewId: Int) {
//        var updateSeatName = ""
//        var updateViewTag = ""
//        var seatStr = ""
//
//        var count = 0
//        for (view in seatViewList) {
//            if (view.tag == STATUS_PC_SEAT || view.tag == STATUS_NOTEBOOK_SEAT) {
//                count++
//            }
//
//            if(view.id == viewId) {
//                if(view.tag == STATUS_PC_SEAT) {
//                    view.tag = STATUS_INUSE_PC_SEAT
//                } else if(view.tag == STATUS_NOTEBOOK_SEAT) {
//                    view.tag = STATUS_INUSE_NOTEBOOK_SEAT
//                }
//                updateSeatName = "${pcroomName}-${count}"
//                updateViewTag = view.tag.toString()
//            }
//            seatStr += view.tag.toString()
//        }
//
//        // pcroom의 seats 정보를 update -> booked = true
//        // seatList와 seats 모두 업데이트해야함
//
//        updatePCRoom(retrofitService, pcroomName, seatStr, updateSeatName, "son@naver.com", updateViewTag, true, true, activity)
//
//    }

    fun updateSeat(
        retrofitService: RetrofitService,
        pcroomName: String,
        seatName: String,
        userEmail: String,
        viewTag: String,
        booked: Boolean,
        inuse: Boolean,
        seatViewList: MutableList<TextView>,
        viewId: Int,
        activity: Activity,
    ) {
        // pcroom 찾아서 seat update
        // user의 seat에 예약한 seat 넣어주기
        // booked와 inuse 구별하기
        // 예약할때 예약하기 하면 예약만추가 -> user booked에 추가
        // 예약할때 바로사용 하면 qr코드 넘어가기 -> user inuse에 추가

        val seatUpdateResponse =
            SeatUpdateRequest(pcroomName, seatName, userEmail, viewTag, booked, inuse)

        retrofitService.updateSeat(seatUpdateResponse)
            .enqueue(object : Callback<SeatUpdateResponse> {
                override fun onFailure(call: Call<SeatUpdateResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("CustomDialog", "update seat onfailure: error by network!!!!!")
                    Log.d("CustomDialog", t.toString())
                }

                override fun onResponse(
                    call: Call<SeatUpdateResponse>,
                    response: Response<SeatUpdateResponse>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2013) {

                            // seat db 업데이트 성공 후 seatViewList에 반영하고 seatStr 생성, 인자로 넘김
                            var seatStr = ""
                            for (view in seatViewList) {
                                if (view.id == viewId) {
                                    if (view.tag == STATUS_PC_SEAT) {
                                        view.tag = STATUS_BOOKED_PC_SEAT
                                    } else if (view.tag == STATUS_NOTEBOOK_SEAT) {
                                        view.tag = STATUS_BOOKED_NOTEBOOK_SEAT
                                    }
                                }
                                seatStr += view.tag.toString()
                            }

                            updatePCRoom(retrofitService,
                                pcroomName,
                                inuse,
                                it.seats,
                                seatStr,
                                activity)

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("CustomDialog",
                                "update seat pcroom onfailure: error by server!!!!!")
                            Log.d("CustomDialog",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("CustomDialog", "responseCode " + it.responseCode.toString())
                            Log.d("CustomDialog", "result " + it.result)
                            Log.d("CustomDialog", "responseMessage" + it.responseMessage)

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    fun updatePCRoom(
        retrofitService: RetrofitService,
        pcroomName: String,
        inuse: Boolean,
        seats: MutableList<Seat>,
        seatStr: String,
        activity: Activity,
    ) {
        // pcroom 찾아서 seat update
        // user의 seat에 예약한 seat 넣어주기
        // booked와 inuse 구별하기
        // 예약할때 예약하기 하면 예약만추가 -> user booked에 추가
        // 예약할때 바로사용 하면 qr코드 넘어가기 -> user inuse에 추가

//        var tempSeatStr: String = ""
//        for (seat in seats) {
//            tempSeatStr += seat.seatType
//        }

//        Log.d("CustomDialog", "tempSeatStr: ${tempSeatStr}")
//        Log.d("CustomDialog", "seatStr: ${tempSeatStr}")

        val updatePCRoomRequest = PCRoomUpdateRequest(
            pcroomName,
            seatStr,
            seats,
        )

        retrofitService.updatePCRoom(updatePCRoomRequest)
            .enqueue(object : Callback<PCRoomUpdateResponse> {
                override fun onFailure(call: Call<PCRoomUpdateResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("CustomDialog", "update pcroom onfailure: error by network!!!!!")
                    Log.d("CustomDialog", t.toString())
                }

                override fun onResponse(
                    call: Call<PCRoomUpdateResponse>,
                    response: Response<PCRoomUpdateResponse>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2011) {

                            // inuse가 false 이면 booked, true 이면 use
                            onClickedListener.resultData(inuse)
                            dialog.dismiss()

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("CustomDialog", "update pcroom onfailure: error by server!!!!!")
                            Log.d("CustomDialog",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("CustomDialog", "responseCode " + it.responseCode.toString())
                            Log.d("CustomDialog", "result " + it.result)
                            Log.d("CustomDialog", "responseMessage" + it.responseMessage)

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

    fun bookOrUseSeat(
        retrofitService: RetrofitService,
        pcroomName: String,
        seatName: String,
        userEmail: String,
        viewTag: String,
        booked: Boolean,
        inuse: Boolean,
        seatsStr: String,
        activity: Activity,
    ) {
        // 하나의 request로 모두 update
        // 1. seatRepository 업데이트
        // 2. userRepository 업데이트
        // 3. pcRoomRepository 업데이트


        Log.d("CustomDialog", "pcroomName" + pcroomName)
        Log.d("CustomDialog", "seatName" + seatName)
        val seatBookOrUseRequest = SeatBookOrUseRequest(
            pcroomName,
            seatName,
            userEmail,
            viewTag,
            booked,
            inuse,
            seatsStr
        )

        Log.d("CustomDialog", "pcroomName" + seatBookOrUseRequest.pcroomName)
        Log.d("CustomDialog", "seatName" + seatBookOrUseRequest.seatName)
        retrofitService.updateSelectSeat(seatBookOrUseRequest)
            .enqueue(object : Callback<SeatBookOrUseResponse> {
                override fun onFailure(call: Call<SeatBookOrUseResponse>, t: Throwable) {
                    //todo 실패처리

                    Log.d("CustomDialog", "update seat onfailure: error by network!!!!!")
                    Log.d("CustomDialog", t.toString())
                }

                override fun onResponse(
                    call: Call<SeatBookOrUseResponse>,
                    response: Response<SeatBookOrUseResponse>,
                ) {
                    //todo 성공처리

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2013) {

                            // inuse가 false 이면 booked, true 이면 use
                            onClickedListener.resultData(inuse)
                            dialog.dismiss()

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        } else {
                            Log.d("CustomDialog", "update seat onfailure: error by server!!!!!")
                            Log.d("CustomDialog",
                                "httpStatus " + it.responseHttpStatus.toString())
                            Log.d("CustomDialog", "responseCode " + it.responseCode.toString())
                            Log.d("CustomDialog", "result " + it.result)
                            Log.d("CustomDialog", "responseMessage" + it.responseMessage)

                            Toast.makeText(activity,
                                it.responseMessage,
                                Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            })
    }

}