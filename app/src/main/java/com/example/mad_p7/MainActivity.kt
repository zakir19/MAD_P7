package com.example.mad_p7

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.json.JSONArray
import org.json.JSONObject

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var personAdapter: PersonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)

        // Set up edge-to-edge display
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        val btn=findViewById<Button>(R.id.btn)
        btn.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

        // Initialize RecyclerView
        recyclerView = findViewById(R.id.recycler_view)  // Assumes there's a RecyclerView with id `recycler_view` in activity_main.xml
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Fetch and display person details
        fetchPersonDetails()
    }

    private fun fetchPersonDetails() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val data = HttpRequest().makeServiceCall(
                    "https://api.json-generator.com/templates/VB6Cs9XI66_F/data",
                    "c41jxqw2nalvkjii5pj84qahsenshzajhuo02ad8"
                )

                withContext(Dispatchers.Main) {
                    if (data != null) {
                        getPersonDetailsFromJson(data)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun getPersonDetailsFromJson(data: String) {
        try {
            val jsonArray = JSONArray(data)
            val personList = Array(jsonArray.length()) { index ->
                val jsonObject = jsonArray.getJSONObject(index)
                Person.fromJson(jsonObject)
            }

            // Set up the adapter with the parsed data
            personAdapter = PersonAdapter(personList)
            recyclerView.adapter = personAdapter

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
