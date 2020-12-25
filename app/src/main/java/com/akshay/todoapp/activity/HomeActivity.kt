package com.akshay.todoapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.akshay.todoapp.helper.DatabaseHelper
import com.akshay.todoapp.adapter.HomeAdapter
import com.akshay.todoapp.R
import com.akshay.todoapp.model.Task


class HomeActivity : AppCompatActivity() {

    private var taskList = ArrayList<Task>()
    private val myDb = DatabaseHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val txtNoTask: TextView = findViewById(R.id.txtNoTask)
        val etAddTaskHere: EditText = findViewById(R.id.etAddTask)
        val imgAdd: ImageView = findViewById(R.id.imgAdd)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerView)

        txtNoTask.alpha = 0.4f

        getTask()

        if(taskList.isNotEmpty()){
            txtNoTask.visibility = View.GONE
            val layoutManager = LinearLayoutManager(this)
            val recyclerAdapter = HomeAdapter(this, taskList)
            recyclerView.adapter = recyclerAdapter
            recyclerView.layoutManager = layoutManager
        } else{
            txtNoTask.visibility = View.VISIBLE
        }

        imgAdd.setOnClickListener {
            val text = etAddTaskHere.text.toString()

            if (text == ""){
                Toast.makeText(this, "Task is empty!", Toast.LENGTH_SHORT).show()
            }else{
                try{
                    myDb.addData(text)
                    Toast.makeText(this, "Task added.", Toast.LENGTH_SHORT).show()
                    etAddTaskHere.text.clear()

                    if (taskList.size == 0){
                        startActivity(Intent(this, HomeActivity::class.java))
                    }
                    taskList.clear()
                    getTask()

                    val recyclerAdapter = HomeAdapter(this, taskList)
                    recyclerView.adapter = recyclerAdapter

                }catch(e: Exception){
                    Toast.makeText(this, "Unable to add Task.", Toast.LENGTH_SHORT).show()
                }
                recyclerView.adapter?.notifyDataSetChanged()
            }
        }
    }

    private fun getTask(){
        val c = myDb.data
        if (c.count != 0){
            while(c.moveToNext()){
                taskList.add(Task(c.getString(1).toString(), c.getString(2) == "1"))
            }
        }
        c.close()
    }
    override fun onPause(){
        super.onPause()
        finish()
    }
}

