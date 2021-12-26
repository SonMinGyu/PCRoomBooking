package org.application.pcroombooking.fragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import org.application.pcroombooking.MainActivity
import org.application.pcroombooking.R
import org.application.pcroombooking.domain.PCRoom
import org.application.pcroombooking.dto.PCRoomsResponse
import org.application.pcroombooking.retrofit.RetrofitService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MySeatFragment: Fragment() {

    lateinit var mainActivity: MainActivity
    lateinit var mySeatFragmentMyStatusTextView: TextView
    lateinit var mySeatFragmentLocationTextView: TextView
    lateinit var mySeatFragmentSeatInfoTextView: TextView
    lateinit var mySeatFragmentStartTimeTextView: TextView
    lateinit var mySeatFragmentExpireTimeTextView: TextView
    lateinit var mySeatFragmentSeatExtensionButton: Button
    lateinit var mySeatFragmentSeatReturnButton: Button

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        var view = inflater.inflate(R.layout.fragment_myseat, container, false)
        initView(view)



        return view
    }

    fun initView(view: View) {
        mySeatFragmentMyStatusTextView = view.findViewById(R.id.fragment_myseat_status)
        mySeatFragmentLocationTextView = view.findViewById(R.id.fragment_myseat_locationText)
        mySeatFragmentSeatInfoTextView = view.findViewById(R.id.fragment_myseat_infoText)
        mySeatFragmentStartTimeTextView = view.findViewById(R.id.fragment_myseat_startTimeText)
        mySeatFragmentExpireTimeTextView = view.findViewById(R.id.fragment_myseat_expiredTimeText)
        mySeatFragmentSeatExtensionButton = view.findViewById(R.id.framgent_myseat_seatExtension_button)
        mySeatFragmentSeatReturnButton = view.findViewById(R.id.fragment_myseat_seatReturn_button)
    }

//    fun getMySeat(retrofitService: RetrofitService) {
//        retrofitService.getPCRoomList()
//            .enqueue(object : Callback<PCRoomsResponse> {
//                override fun onFailure(call: Call<PCRoomsResponse>, t: Throwable) {
//                    //todo 실패처리
//
//                    Log.d("PCRoomFragment", "get pcroom onfailure: error by network!!!!!")
//                    Log.d("PCRoomFragment", t.toString())
//                }
//
//                override fun onResponse(
//                    call: Call<PCRoomsResponse>,
//                    response: Response<PCRoomsResponse>,
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
//                            Log.d("PCRoomFragment", "get pcroom onfailure: error by server!!!!!")
//                            Log.d("PCRoomFragment",
//                                "httpStatus " + it.responseHttpStatus.toString())
//                            Log.d("PCRoomFragment", "responseCode " + it.responseCode.toString())
//                            Log.d("PCRoomFragment", "result " + it.result)
//                            Log.d("PCRoomFragment", "responseMessage" + it.responseMessage)
//
//                            var emptyPCRoomList = mutableListOf<PCRoom>()
//                            initRecyclerView(emptyPCRoomList)
//                            Toast.makeText(mainActivity,
//                                it.responseMessage,
//                                Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                }
//            })
//    }


}