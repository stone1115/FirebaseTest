package app.ishizuka.ryo.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_view.*

class ViewActivity: AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view)

        EmailBackButton.setOnClickListener {
            finish()
        }

        viewContentList()

        viewUserInfo()
    }

    private fun viewContentList() {
        val adapter = RecyclerViewAdapter(this)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        db.collection("users")
            .get()
            .addOnSuccessListener { result ->
                val dataList: MutableList<TestData> = mutableListOf()
                for (documentSnapshot in result) {
                    val data = documentSnapshot.toObject(TestData::class.java)
                    dataList.add(data)
                }
                adapter.addAll(dataList)
            }
            .addOnFailureListener { e -> Log.w(TAG, "Error reading document", e) }
    }

    private fun viewUserInfo() {
        val user = FirebaseAuth.getInstance().currentUser
        user?.let {
            val name = it.displayName
            val email = it.email
            // val photoUrl = it.photoUrl
            // val emailVerified = it.isEmailVerified

            mailText.text = name.toString()
            emailText.text = email.toString()
        }
    }

    companion object {
        private const val TAG = "VIEW"
    }
}