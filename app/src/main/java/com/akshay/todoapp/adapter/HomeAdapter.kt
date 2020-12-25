package com.akshay.todoapp.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.akshay.todoapp.helper.DatabaseHelper
import com.akshay.todoapp.R
import com.akshay.todoapp.helper.SwipeGesture
import com.akshay.todoapp.model.Task

class HomeAdapter(private val context: Context, private var taskList: ArrayList<Task>) :
    RecyclerView.Adapter<HomeAdapter.HomeViewHolder>() {
    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        //recyclerview data is to be here
        val cardContent: CardView = view.findViewById(R.id.cardContent)
        val txtContent: TextView = view.findViewById(R.id.txtContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recyclerview_single_task, parent, false)
        return HomeViewHolder(view)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val curr = taskList[position]
        val myDb = DatabaseHelper(context)

        holder.txtContent.text = curr.text

        if(curr.completed){
            holder.txtContent.paintFlags =
                Paint.STRIKE_THRU_TEXT_FLAG
            holder.txtContent.alpha = 0.4F
        }

        holder.cardContent.setOnTouchListener(object : SwipeGesture(context) {
            //it will delete the task
            override fun onSwipeLeft() {
                super.onSwipeLeft()
                val txt = holder.txtContent.text.toString()
                myDb.deleteName(txt)
                taskList.removeAt(position)

                notifyDataSetChanged()
            }
            //it will complete the task
            override fun onSwipeRight() {
                super.onSwipeRight()
                val txt = holder.txtContent.text.toString()
                myDb.updateName(txt)

                taskList[position].completed = true

                holder.txtContent.paintFlags =
                    holder.txtContent.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
                holder.txtContent.alpha = 0.4F

                notifyItemChanged(position)
            }
        })
    }

    override fun getItemCount(): Int {
        return taskList.size
    }
}