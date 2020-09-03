package app.ishizuka.ryo.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.database.ktx.getValue
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_realtime_database.*

class RealtimeDatabaseActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_realtime_database)

        dataBaseTestButton1.setOnClickListener {
            baseWriteRead()
        }

        dataBaseTestButton2.setOnClickListener {
            writeUserInfo()
        }

        databaseBackButton.setOnClickListener {
            finish()
        }
    }

    private fun baseWriteRead() {
        val database = Firebase.database
        val myRef = database.getReference("message")

        myRef.setValue("Hello World!")

        myRef.addValueEventListener(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val value = snapshot.getValue<String>()
                Log.d(TAG, "Value is $value")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.w(TAG, "Failed to read value.", error.toException())
            }
        })
    }

    private fun writeUserInfo() {
        val user = Firebase.auth.currentUser
        if (user == null) {
            Log.e(TAG, "Haven't signed in yet.")
            return
        }

        val database = Firebase.database.reference
        val userRef = database.child("users").child(user.uid.toString())

        val userInfo: UserInfoData = UserInfoData(user.displayName, user.email)
        userRef.setValue(userInfo)
    }

    companion object {
        private const val TAG = "RealtimeDatabase"
    }
}