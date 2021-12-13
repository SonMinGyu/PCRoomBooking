package org.application.pcroombooking.recyclerView.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.application.pcroombooking.R
import org.application.pcroombooking.domain.ConferenceRoomReservation

class ConferenceRoomReservationAdapter(val conferenceRoomReservationList: List<ConferenceRoomReservation>) :

    RecyclerView.Adapter<ConferenceRoomReservationAdapter.Holder>() {

    companion object {
        var selectedReservation: MutableList<ConferenceRoomReservation> = mutableListOf()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conferenceroom_reservation, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(conferenceRoomReservationList[position])
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
                    conferenceRoomReservationCheckbox.isEnabled = false
                } else {
                    conferenceRoomReservationStatusText.text = "예약가능"
                }
            } else {
                conferenceRoomReservationStatusText.text = "사용불가"
            }

            // checkBox를 체크하면 선택된 reservation을 selectedReservation에 추가
            conferenceRoomReservationCheckbox.setOnCheckedChangeListener { view, isCheck ->
                if(isCheck) {
                    selectedReservation.add(ConferenceRoomReservation(conferenceRoomReservation.startTime, conferenceRoomReservation.endTime))
                } else {
                    selectedReservation.remove(ConferenceRoomReservation(conferenceRoomReservation.startTime, conferenceRoomReservation.endTime))
                }
            }
        }
    }
}