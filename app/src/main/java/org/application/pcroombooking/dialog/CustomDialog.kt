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

    // dialog?????? ?????? ????????? ????????? ??? ???????????? ??????
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

        // pcroom??? seats ????????? update -> booked = true
        // seatList??? seats ?????? ?????????????????????
        // ????????? ??????????????? ??? ????????? companion object?????? userEmail ????????????
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

    // dialog?????? ?????? ????????? ????????? ??? ???????????? ??????, ?????? QR?????? ???????????? ???????????????, QR?????? ???????????? ?????? ???????????? ??? ??????
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
//        // pcroom??? seats ????????? update -> booked = true
//        // seatList??? seats ?????? ?????????????????????
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
        // pcroom ????????? seat update
        // user??? seat??? ????????? seat ????????????
        // booked??? inuse ????????????
        // ???????????? ???????????? ?????? ??????????????? -> user booked??? ??????
        // ???????????? ???????????? ?????? qr?????? ???????????? -> user inuse??? ??????

        val seatUpdateResponse =
            SeatUpdateRequest(pcroomName, seatName, userEmail, viewTag, booked, inuse)

        retrofitService.updateSeat(seatUpdateResponse)
            .enqueue(object : Callback<SeatUpdateResponse> {
                override fun onFailure(call: Call<SeatUpdateResponse>, t: Throwable) {
                    //todo ????????????

                    Log.d("CustomDialog", "update seat onfailure: error by network!!!!!")
                    Log.d("CustomDialog", t.toString())
                }

                override fun onResponse(
                    call: Call<SeatUpdateResponse>,
                    response: Response<SeatUpdateResponse>,
                ) {
                    //todo ????????????

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2013) {

                            // seat db ???????????? ?????? ??? seatViewList??? ???????????? seatStr ??????, ????????? ??????
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
        // pcroom ????????? seat update
        // user??? seat??? ????????? seat ????????????
        // booked??? inuse ????????????
        // ???????????? ???????????? ?????? ??????????????? -> user booked??? ??????
        // ???????????? ???????????? ?????? qr?????? ???????????? -> user inuse??? ??????

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
                    //todo ????????????

                    Log.d("CustomDialog", "update pcroom onfailure: error by network!!!!!")
                    Log.d("CustomDialog", t.toString())
                }

                override fun onResponse(
                    call: Call<PCRoomUpdateResponse>,
                    response: Response<PCRoomUpdateResponse>,
                ) {
                    //todo ????????????

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2011) {

                            // inuse??? false ?????? booked, true ?????? use
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
        // ????????? request??? ?????? update
        // 1. seatRepository ????????????
        // 2. userRepository ????????????
        // 3. pcRoomRepository ????????????


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
                    //todo ????????????

                    Log.d("CustomDialog", "update seat onfailure: error by network!!!!!")
                    Log.d("CustomDialog", t.toString())
                }

                override fun onResponse(
                    call: Call<SeatBookOrUseResponse>,
                    response: Response<SeatBookOrUseResponse>,
                ) {
                    //todo ????????????

                    if (response.isSuccessful.not()) {
                        return
                    }

                    response.body()?.let {
                        if (it.responseHttpStatus == 200 && it.responseCode == 2013) {

                            // inuse??? false ?????? booked, true ?????? use
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