package com.example.sqliteapp

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var db = DatabaseHandler(this)

        btnInsert.setOnClickListener{
            if (etvName.text.toString().length > 0 &&
                    etvAge.text.toString().length > 0){
                var user = User(etvName.text.toString(), etvAge.text.toString().toInt())
                db.insertData(user)
            }else{
                Toast.makeText(this,"Please fill the data!!", Toast.LENGTH_SHORT).show()
            }
        }

        btn_Read.setOnClickListener {
            var data = db.readData()
            tvResult.text = ""

            for (i in 0..data.size-1){
                tvResult.append(data.get(i).id.toString() + " " + data.get(i).name + " " + data.get(i).age + "\n")
            }
        }

        btn_Update.setOnClickListener {
            db.updateData()
            btn_Read.performClick()
        }

        btn_Delete.setOnClickListener {
            db.deleteData()
            btn_Read.performClick()
        }
    }
}
