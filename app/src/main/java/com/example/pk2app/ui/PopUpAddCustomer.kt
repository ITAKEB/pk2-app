package com.example.pk2app.ui

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.example.pk2app.Board
import com.example.pk2app.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputLayout

class PopUpAddCustomer(
    private val onSubmitClickListener: (Board) -> Unit

) :DialogFragment() {

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val inflater = this.layoutInflater
        val dialogView = inflater.inflate(R.layout.popup_add_customer, null)

        val builder = AlertDialog.Builder(requireContext())
        builder.setView(dialogView)

        val btClose = dialogView.findViewById<MaterialButton>(R.id.btClose)
        val btAdd = dialogView.findViewById<MaterialButton>(R.id.btAgregar)
        val btCancel = dialogView.findViewById<MaterialButton>(R.id.btCancelar)
        val board = dialogView.findViewById<TextInputLayout>(R.id.txtMesa)
        val customer = dialogView.findViewById<TextInputLayout>(R.id.txtCliente)

        btAdd.setOnClickListener {
            onSubmitClickListener.invoke(Board(0,board.editText?.text.toString(),customer.editText?.text.toString(),"0"))
            dismiss()
        }

        btClose.setOnClickListener {
            dismiss()
        }

        btCancel.setOnClickListener {
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        return dialog

    }
}