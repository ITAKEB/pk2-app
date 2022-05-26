package com.example.pk2app.ui

import Data.DataDbHelper
import Data.Tables
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pk2app.ItemBoard
import com.example.pk2app.R
import com.google.android.material.button.MaterialButton

class ItemsAccountAdapter(dataItemBoard: MutableList<ItemBoard>, db:DataDbHelper, totalAccount:TextView, bool:Int): RecyclerView.Adapter<ItemsAccountAdapter.ViewHolder>() {

    private val itemsBoards =  dataItemBoard
    private val db = db
    private var totalPrice:Int = 0
    private val totalAccount:TextView = totalAccount
    private val bool:Int = bool

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v =
            LayoutInflater.from(viewGroup.context).inflate(R.layout.card_item_account, viewGroup, false)
        return ViewHolder(v)
    }

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemAccountTitle.text = itemsBoards[i].getItemTitle()
        viewHolder.itemAccountCount.text = itemsBoards[i].getQuantity().toString()
        viewHolder.itemAccountPrice.text = "$ "+(itemsBoards[i].getItemPrice().toInt()*itemsBoards[i].getQuantity())
        totalPrice = itemsBoards[i].getItemPrice().toInt()*itemsBoards[i].getQuantity() + totalPrice
        totalAccount.setText("$ "+totalPrice)

    }

    override fun getItemCount(): Int {
        return itemsBoards.size
    }

    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        var itemAccountTitle: TextView
        var itemAccountCount: TextView
        var itemAccountPrice: TextView
        var addLess: MaterialButton
        var addMore: MaterialButton
        var info:MaterialButton
        var delete:MaterialButton
        init{
            itemAccountTitle = itemView.findViewById(R.id.itemAccountTitle)
            itemAccountCount = itemView.findViewById(R.id.itemAccountCount)
            itemAccountPrice = itemView.findViewById(R.id.itemAccountPrice)
            addLess = itemView.findViewById(R.id.btAddLess)
            addMore = itemView.findViewById(R.id.btAddMore)
            info = itemView.findViewById(R.id.btInfo)
            delete = itemView.findViewById(R.id.btDelete)


            if(bool==1){
                addLess.visibility = View.GONE
                addMore.visibility = View.GONE
                delete.visibility = View.GONE
                info.visibility=View.GONE
            }

            addMore.setOnClickListener {
                val count = itemAccountCount.text.toString().toInt() + 1
                val price = itemsBoards[position].getItemPrice().toInt() * count
                totalPrice += itemsBoards[position].getItemPrice().toInt()
                db.updateQuantityItemBoard(itemsBoards[position].getId(),count, price)
                itemAccountCount.setText(count.toString())
                itemAccountPrice.setText("$ "+price.toString())
                totalAccount.setText("$ "+totalPrice.toString())
            }

            addLess.setOnClickListener {
                val count = itemAccountCount.text.toString().toInt() - 1
                val price = itemsBoards[position].getItemPrice().toInt() * count
                totalPrice -= itemsBoards[position].getItemPrice().toInt()
                db.updateQuantityItemBoard(itemsBoards[position].getId(),count,price)
                itemAccountCount.setText(count.toString())
                itemAccountPrice.setText("$ "+price.toString())
                totalAccount.setText("$ "+totalPrice.toString())
            }

            totalAccount.setText("$ "+totalPrice.toString())

        }
    }

    fun getTotalPrice():Int{
        return totalPrice
    }
}