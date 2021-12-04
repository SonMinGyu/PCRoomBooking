package org.application.pcroombooking.recyclerView.adapter

import android.app.Activity
import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.application.pcroombooking.ConferenceRoomDetailActivity
import org.application.pcroombooking.R
import org.application.pcroombooking.domain.ConferenceRoom
import org.application.pcroombooking.domain.ConferenceRoomReservation

class ConferenceRoomReservationAdapter(val conferenceRoomReservationList: List<ConferenceRoomReservation>) :
    RecyclerView.Adapter<ConferenceRoomReservationAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return conferenceRoomReservationList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val conferenceRoomReservationCheckbox =
            itemView.findViewById<CheckBox>(R.id.item_conferenceroom_reservation_checkbox)
        val conferenceRoomReservationUseTimeText =
            itemView.findViewById<TextView>(R.id.item_conferenceroom_reservation_useTime_text)
        val conferenceRoomReservationStatusText =
            itemView.findViewById<TextView>(R.id.item_conferenceroom_reservation_status)

        fun bind(conferenceRoomReservation: ConferenceRoomReservation) {
            conferenceRoomReservationCheckbox.isChecked = false
            conferenceRoomReservationUseTimeText.text =
                "${conferenceRoomReservation.startTime}시~${conferenceRoomReservation.endTime}시"

            if(conferenceRoomReservation.enabled) {
                if(conferenceRoomReservation.reserved) {
                    conferenceRoomReservationStatusText.text = "예약완료"
                } else {
                    conferenceRoomReservationStatusText.text = "예약가능"
                }
            } else {
                conferenceRoomReservationStatusText.text = "사용불가"
            }

//            itemView.setOnClickListener {
//                val intent = Intent(activity, ConferenceRoomDetailActivity::class.java)
//                    .apply {
//                        putExtra("ConferenceRoomName", conferenceRoom.name)
//                    }
//                activity.startActivity(intent)
//            }
        }
    }
}