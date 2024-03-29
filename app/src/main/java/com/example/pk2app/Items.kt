package com.example.pk2app

import Data.DataDbHelper
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.pk2app.ui.*
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.textfield.TextInputLayout

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [Items.newInstance] factory method to
 * create an instance of this fragment.
 */
class Items : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    private lateinit var db: DataDbHelper
    private lateinit var adapter: ItemsAdapter
    private var recyclerView: RecyclerView? = null
    private var txtSearch: TextInputLayout? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_items, container, false)
    }

    override fun onViewCreated(itemView: View, savedInstanceState: Bundle?) {
        super.onViewCreated(itemView, savedInstanceState)
        recyclerView = view?.findViewById(R.id.recyclerViewItems)
        txtSearch = view?.findViewById(R.id.txtSearch)
        db = DataDbHelper(context)

        updateRecyclerView(recyclerView, this)

        txtSearch?.editText?.doOnTextChanged { text, start, before, count ->
            adapter.filter(text.toString())
        }

        val btAddNewItem = view?.findViewById<FloatingActionButton>(R.id.btaddItem)

        btAddNewItem?.setOnClickListener {
            PopUpAddItem(
                onSubmitClickListener = { item ->
                    Toast.makeText(activity, "Usted ingreso: ${item.getName()}", Toast.LENGTH_SHORT)
                        .show()
                    db.insertItem(item.getName(), item.getPrice(), item.getDescription())
                    //Refresh new data on this fragment

                    updateRecyclerView(recyclerView, this)

                    adapter.notifyDataSetChanged()

                },null
            ).show(parentFragmentManager, "dialog")
        }


    }

    private fun updateRecyclerView(recyclerView: RecyclerView?, fr: Fragment) {
        val dataItems = db.getItemData()

        recyclerView.apply {
            // set a LinearLayoutManager to handle Android
            // RecyclerView behavior
            recyclerView?.layoutManager = GridLayoutManager(activity, 2)
            // set the custom adapter to the RecyclerView
            adapter = ItemsAdapter(dataItems)
            recyclerView?.adapter = adapter

            adapter.setOnItemClickListener(object : ItemsAdapter.onItemClickLister {
                override fun onItemClick(id: Int) {
                    PopUpUpdateItem(
                        onSubmitClickListener = { item ->
                            Toast.makeText(activity, "Usted actualizó: ${item.getName()}", Toast.LENGTH_SHORT)
                                .show()
                            db.updateItem(id,item.getName(), item.getPrice(), item.getDescription())

                            //Refresh new data on this fragment
                            updateRecyclerView(recyclerView, fr)

                            adapter.notifyDataSetChanged()

                        },db.getItem(id)[0]
                    ).show(parentFragmentManager, "dialog")
                }
            })

            adapter.setOnBtDeleteClickListener(object : ItemsAdapter.onBtClickLister {
                override fun onBtClick(id: Int) {


                    PopUpAreUSure(
                        onSubmitClickListener = {
                            db.deleteItem(id)
                            Toast.makeText(
                                activity,
                                "You delete item no. ${id}",
                                Toast.LENGTH_SHORT
                            )
                                .show()
                            val fragmentManager: FragmentManager = parentFragmentManager
                            fragmentManager.beginTransaction().detach(fr).commit()
                            fragmentManager.beginTransaction().attach(fr).commit()

                        }
                    ).show(parentFragmentManager, "dialog")

                }

            })

        }

    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment Items.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            Items().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}