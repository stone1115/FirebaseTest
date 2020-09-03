package app.ishizuka.ryo.firebasetest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.ActionCodeSettings
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.actionCodeSettings
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity: AppCompatActivity() {

    private val db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        addButton.setOnClickListener {
            addDataTest()
        }

        val actionCodeSettings = actionCodeSettings {
            url = "https://www.example.com/finishSignUp?cartId=1234"
            handleCodeInApp = true
            iosBundleId = "com.example.ios"
            setAndroidPackageName(
                "com.example.android",
                true,
                "12"
            )
        }

        val preview = Intent(this, ViewActivity::class.java)
        intentViewButton.setOnClickListener {
            startActivity(preview)
        }

        val emailPreview = Intent(this, EmailPasswordActivity::class.java)
        intentEmailButton.setOnClickListener {
            startActivity(emailPreview)
        }

        val googlePreview = Intent(this, GoogleSignInActivity::class.java)
        intentGoogleButton.setOnClickListener {
            startActivity(googlePreview)
        }

        val databasePreview = Intent(this, RealtimeDatabaseActivity::class.java)
        intentDatabaseButton.setOnClickListener {
            startActivity(databasePreview)
        }
    }

    private fun addDataTest() {
        // val testData = TestData(inputNameView.text.toString(), inputFoodView.text.toString())
        val testData = hashMapOf(
            "name" to inputNameView.text.toString(),
            "food" to inputFoodView.text.toString()
        )
        db.collection("users").document()
            .set(testData)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot successful written") }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    companion object {
        private const val TAG = "Main"
    }
}