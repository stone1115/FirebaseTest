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

    val db = FirebaseFirestore.getInstance()

    val TAG = "TAG_MAIN"

    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

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

        viewButton.setOnClickListener {
            startActivity(preview)
        }

        // ログイン
        auth = Firebase.auth
        // auth = FirebaseAuth.getInstance ()

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        signInButton.setOnClickListener {
            signIn()
        }

        signOutButton.setOnClickListener {
            signOut()
        }

        signinMailButton.setOnClickListener {
            // sendSignInLink(inputMail.text.toString(), inputPassword.text.toString())
        }
    }

    private fun sendSignInLink(email: String, actionCodeSettings: ActionCodeSettings) {
        // [START auth_send_sign_in_link]
        Firebase.auth.sendSignInLinkToEmail(email, actionCodeSettings)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "Email sent.")
                }
            }
        // [END auth_send_sign_in_link]
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    private fun signOut() {
        // Firebase sign out
        auth.signOut()

        // Google sign out
        googleSignInClient.signOut().addOnCompleteListener(this) {
            // updateUI(null)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                // Google Sign In was successful, authenticate with Firebase
                val account = task.getResult(ApiException::class.java)!!
                Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                // Google Sign In failed, update UI appropriately
                Log.w(TAG, "Google sign in failed", e)
                // ...
            }
        }
    }

    // [START auth_with_google]
    private fun firebaseAuthWithGoogle(idToken: String) {
        // [START_EXCLUDE silent]
        // showProgressBar()
        // [END_EXCLUDE]
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithCredential:success")
                    val user = auth.currentUser


                    // updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithCredential:failure", task.exception)
                    // val view = binding.mainLayout

                    // Snackbar.make(view, "Authentication Failed.", Snackbar.LENGTH_SHORT).show()
                    // updateUI(null)
                }

                // [START_EXCLUDE]
                // hideProgressBar()
                // [END_EXCLUDE]
            }
    }
    // [END auth_with_google]

    fun addDataTest() {
        // val testData = TestData(inputNameView.text.toString(), inputFoodView.text.toString())
        val testData = hashMapOf(
            "name" to inputNameView.text.toString(),
            "food" to inputFoodView.text.toString()
        )
        db.collection("users").document()
            .set(testData)
            .addOnSuccessListener { Log.d(TAG, "DocumentSnapshot succesfully written") }
            .addOnFailureListener { e -> Log.w(TAG, "Error adding document", e) }
    }

    companion object {
        // private const val TAG = "GoogleActivity"
        private const val RC_SIGN_IN = 9001
    }
}