package org.application.pcroombooking

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.*

class AdminAddPCRoomActivity : AppCompatActivity() {

    lateinit var adminAddPCRoomPCRoomNameEdit: EditText
    lateinit var adminAddPCRoomBuildingSpinner: Spinner
    lateinit var adminAddPCRoomLayerSpinner: Spinner
    lateinit var adminAddPCRoomPCSeatNumberPicker: NumberPicker
    lateinit var adminAddPCRoomNotebookNumberPicker: NumberPicker
    lateinit var adminAddPCRoomPCRoomLinePicker: NumberPicker
    lateinit var adminAddPCRoomPCRoomRowPicker: NumberPicker
    lateinit var adminAddPCRoomCancelButton: Button
    lateinit var adminAddPCRoomOkButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_add_pcroom)

        initView()
        initSpinner()
        initPicker()
        setListener()

    }

    fun initView() {
        adminAddPCRoomPCRoomNameEdit = findViewById(R.id.activity_admin_add_pcroom_pcroomName_edit)
        adminAddPCRoomBuildingSpinner = findViewById(R.id.activity_admin_add_pcroom_buildingNumber_spinner)
        adminAddPCRoomLayerSpinner = findViewById(R.id.activity_admin_add_pcroom_layer_spinner)
        adminAddPCRoomPCSeatNumberPicker =findViewById(R.id.activity_admin_add_pcroom_pcSeatNumber_picker)
        adminAddPCRoomNotebookNumberPicker = findViewById(R.id.activity_admin_add_pcroom_notebookNumber_picker)
        adminAddPCRoomPCRoomLinePicker = findViewById(R.id.activity_admin_add_pcroom_line_picker)
        adminAddPCRoomPCRoomRowPicker = findViewById(R.id.activity_admin_add_pcroom_row_picker)
        adminAddPCRoomCancelButton = findViewById(R.id.activity_admin_add_pcroom_cancel_button)
        adminAddPCRoomOkButton = findViewById(R.id.activity_admin_add_pcroom_ok_button)
    }

    fun initPicker() {
        adminAddPCRoomPCSeatNumberPicker.wrapSelectorWheel = false
        adminAddPCRoomNotebookNumberPicker.wrapSelectorWheel = false
        adminAddPCRoomPCRoomLinePicker.wrapSelectorWheel = false
        adminAddPCRoomPCRoomRowPicker.wrapSelectorWheel = false

        adminAddPCRoomPCSeatNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        adminAddPCRoomNotebookNumberPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        adminAddPCRoomPCRoomLinePicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS
        adminAddPCRoomPCRoomRowPicker.descendantFocusability = NumberPicker.FOCUS_BLOCK_DESCENDANTS

        adminAddPCRoomPCSeatNumberPicker.minValue = 0
        adminAddPCRoomPCSeatNumberPicker.maxValue = 1000

        adminAddPCRoomNotebookNumberPicker.minValue = 0
        adminAddPCRoomNotebookNumberPicker.maxValue = 1000

        adminAddPCRoomPCRoomLinePicker.minValue = 0
        adminAddPCRoomPCRoomLinePicker.maxValue = 1000

        adminAddPCRoomPCRoomRowPicker.minValue = 0
        adminAddPCRoomPCRoomRowPicker.maxValue = 1000
    }

    fun initSpinner() {
        adminAddPCRoomBuildingSpinner.adapter = ArrayAdapter.createFromResource(this,
            R.array.spinner_admin_add_pcroom_buildingNumber,
            android.R.layout.simple_spinner_item)

        adminAddPCRoomLayerSpinner.adapter = ArrayAdapter.createFromResource(this,
            R.array.spinner_admin_add_pcroom_layer_no_choice,
            android.R.layout.simple_spinner_item)
    }

    fun setListener() {
        adminAddPCRoomCancelButton.setOnClickListener {
            finish()
        }

        adminAddPCRoomOkButton.setOnClickListener {
            Log.d("AdminAddPCRoomActivity", "OK button clicked")
            var intent = Intent(this, AdminAddPCRoomDetailActivity::class.java)
                .apply {
                    putExtra("CreatePCRoomPCRoomName", adminAddPCRoomPCRoomNameEdit.text.toString())
                    putExtra("CreatePCRoomBuildingNumber", adminAddPCRoomBuildingSpinner.selectedItem.toString())
                    putExtra("CreatePCRoomLayer", adminAddPCRoomLayerSpinner.selectedItem.toString())
                    putExtra("CreatePCRoomPCSeat", adminAddPCRoomPCSeatNumberPicker.value)
                    putExtra("CreatePCRoomNotebookSeat", adminAddPCRoomNotebookNumberPicker.value)
                    putExtra("CreatePCRoomLine", adminAddPCRoomPCRoomLinePicker.value)
                    putExtra("CreatePCRoomRow", adminAddPCRoomPCRoomRowPicker.value)
                }
            startActivity(intent)
        }

        adminAddPCRoomBuildingSpinner.onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                if(position == 0) {
                    adminAddPCRoomLayerSpinner.adapter = null
                    adminAddPCRoomLayerSpinner.adapter = ArrayAdapter.createFromResource(this@AdminAddPCRoomActivity,
                        R.array.spinner_admin_add_pcroom_layer_no_choice,
                        android.R.layout.simple_spinner_item)
                } else if(position == 1) {
                    adminAddPCRoomLayerSpinner.adapter = null
                    adminAddPCRoomLayerSpinner.adapter = ArrayAdapter.createFromResource(this@AdminAddPCRoomActivity,
                        R.array.spinner_admin_add_pcroom_layer_208,
                        android.R.layout.simple_spinner_item)
                } else if(position == 2) {
                    adminAddPCRoomLayerSpinner.adapter = null
                    adminAddPCRoomLayerSpinner.adapter = ArrayAdapter.createFromResource(this@AdminAddPCRoomActivity,
                        R.array.spinner_admin_add_pcroom_layer_310,
                        android.R.layout.simple_spinner_item)
                }
            }
        }
    }

}