package org.application.pcroombooking.recyclerView.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.application.pcroombooking.AdminPCRoomDetailActivity
import org.application.pcroombooking.R
import org.application.pcroombooking.domain.PCRoom

class AdminPCRoomAdapter(val pcroomList: List<PCRoom>, val activity: Activity, val applicationContext: Context, val menuInflater: MenuInflater):
    RecyclerView.Adapter<AdminPCRoomAdapter.Holder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
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

            itemView.setOnClickListener {
                var adminItemPCRoomPopUpMenu = PopupMenu(applicationContext, it)
                menuInflater.inflate(R.menu.admin_item_pcroom_pop_up_menu, adminItemPCRoomPopUpMenu.menu)
                adminItemPCRoomPopUpMenu.setOnMenuItemClickListener {
                    when(it.itemId) {
                        R.id.admin_item_pcroom_pop_up_menu_manage_pcroom -> {
                            // pcroom 열어서 좌석관리
                            Log.d("AdminPCRoomAdapter", "pcroom manage clicked")
                            val intent = Intent(activity, AdminPCRoomDetailActivity::class.java)
                                .apply {
                                    putExtra("AdminPCRoomName", pcroom.name)
                                    Log.d("AdminPCRoomName", "AdminPCRoom putExtra 실행")
                                    Log.d("AdminPCRoomName", pcroom.name)
                                }
                            activity.startActivity(intent)
//                        return@setOnMenuItemClickListener true
                        }
                        R.id.admin_pop_up_menu_manage_conferenceroom -> {
                            // pc실 삭제 api 요청 보내기
                            Log.d("AdminPCRoomAdapter", "pcroom remove clicked")
//                        return@setOnMenuItemClickListener true
                        }
                        R.id.admin_item_pcroom_pop_up_menu_change_status -> {
                            // pc실 상태 변경 api 요청 보내기
                            Log.d("AdminPCRoomAdapter", "pcroom change status clicked")
//                        return@setOnMenuItemClickListener true
                        }
                        else -> {
//                        return@setOnMenuItemClickListener false
                        }
                    }
                    true
                }
                adminItemPCRoomPopUpMenu.show()

            }
        }
    }
}