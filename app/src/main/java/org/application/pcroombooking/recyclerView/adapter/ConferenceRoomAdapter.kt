package org.application.pcroombooking.recyclerView.adapter

import android.app.Activity
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.application.pcroombooking.ConferenceRoomDetailActivity
import org.application.pcroombooking.R
import org.application.pcroombooking.domain.ConferenceRoom

class ConferenceRoomAdapter(val conferenceRoomList: List<ConferenceRoom>, val activity: Activity):
    RecyclerView.Adapter<ConferenceRoomAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_conferenceroom, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(conferenceRoomList[position])
    }

    override fun getItemCount(): Int {
        return conferenceRoomList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val conferenceRoomNameText = itemView.findViewById<TextView>(R.id.item_conferenceroom_croomName_text)
        val conferenceRoomAllBookedText = itemView.findViewById<TextView>(R.id.item_conferenceroom_bookEnableText)
        val conferenceRoombuildingText = itemView.findViewById<TextView>(R.id.item_conferenceroom_croomBuilding_text)
        val conferenceRoomLimitText = itemView.findViewById<TextView>(R.id.item_conferenceroom_limit_text)
        val conferenceRoomLocationText = itemView.findViewById<TextView>(R.id.item_conferenceroom_croomLocation_text)
//        val conferenceRoomNextClassNotiText = itemView.findViewById<TextView>(R.id.item_conferenceroom_nextClassNoti)

        fun bind (conferenceRoom: ConferenceRoom) {
            conferenceRoomNameText.text = conferenceRoom.name
            if(conferenceRoom.allBooked) {
                conferenceRoomAllBookedText.text = "예약완료"
            } else {
                conferenceRoomAllBookedText.text = "예약가능"
            }
            conferenceRoombuildingText.text = "${conferenceRoom.buildingNumber.toString()} 관"
            conferenceRoomLimitText.text = "${conferenceRoom.limit} 명"
            conferenceRoomLocationText.text = conferenceRoom.locationName

            itemView.setOnClickListener {
                val intent = Intent(activity, ConferenceRoomDetailActivity::class.java)
                    .apply {
                        putExtra("ConferenceRoomName", conferenceRoom.name)
                        Log.d("ConferenceRoomName", "putExtra 실행")
                        Log.d("ConferenceRoomName", conferenceRoom.name)
                    }
                activity.startActivity(intent)
            }
        }
    }

}