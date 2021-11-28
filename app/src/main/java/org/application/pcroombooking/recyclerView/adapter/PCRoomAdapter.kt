package org.application.pcroombooking.recyclerView.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.application.pcroombooking.R
import org.application.pcroombooking.domain.PCRoom

class PCRoomAdapter(val pcroomList: List<PCRoom>):
    RecyclerView.Adapter<PCRoomAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        Log.d("PCRoomAdapter", "onCreateViewHolder")
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pcroom, parent, false)
        return Holder(view)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(pcroomList[position])
    }

    override fun getItemCount(): Int {
        return pcroomList.size
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val pcroomNameText = itemView.findViewById<TextView>(R.id.item_pcroom_pcroomName_text)
        val pcroomEnableText = itemView.findViewById<TextView>(R.id.item_pcroom_enableText)
        val pcroombuildingText = itemView.findViewById<TextView>(R.id.item_pcroom_pcroomBuilding_text)
        val pcroomPCSeatNumberText = itemView.findViewById<TextView>(R.id.item_pcroom_pcSeat_text)
        val pcroomNotebookSeatNumberText = itemView.findViewById<TextView>(R.id.item_pcroom_notebookSeat_text)

        fun bind (pcroom: PCRoom) {
            pcroomNameText.text = pcroom.name
            if(pcroom.enabled) {
                pcroomEnableText.text = "사용가능"
            } else {
                pcroomEnableText.text = "사용불가"
            }
            pcroombuildingText.text = "${pcroom.buildingNumber} 관"

            pcroomPCSeatNumberText.text = "${pcroom.pcSeatNumber}/${(pcroom.pcSeatNumber - pcroom.pcSeatUseableNumber)}"
            pcroomNotebookSeatNumberText.text = "${pcroom.notebookSeatNumber}/${pcroom.notebookSeatNumber - pcroom.notebookSeatUseableNumber}"
        }
    }


}