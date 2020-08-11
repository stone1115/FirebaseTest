package app.ishizuka.ryo.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity: AppCompatActivity() {

    val db = FirebaseFirestore.getInstance()    // MainActivityでも定義している

    val TAG = "TAG_VIEW"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        val adapter = RecyclerViewAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val dataList: MutableList<TestData> = mutableListOf()
                for (documentSnapshot in result) {
                    val data = documentSnapshot.toObject(TestData::class.java)
                    dataList.add (data)
                }
                adapter.addAll(dataList)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error reading document", e) }

        backButton.setOnClickListener {
            finish()
        }
    }
}