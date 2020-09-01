package app.ishizuka.ryo.firebasetest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_email_password.*

class EmailPasswordActivity: AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_email_password)

        auth = Firebase.auth

        EmailCreateAccountButton.setOnClickListener {
            createAccount(emailInput.text.toString(), passwordInput.text.toString())
        }

        EmailSignInButton.setOnClickListener {
            signIn(emailInput.text.toString(), passwordInput.text.toString())
        }

        EmailSignOutButton.setOnClickListener {
            signOut()
        }

        EmailBackButton.setOnClickListener {
            finish()
        }

        // reload
    }

    private fun createAccount(email: String, password: String) {
        if (!validateForm()) {
            return
        }
        // showProgressBar()

        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "createUserWithEmail: success")
                    val user = auth.currentUser
                    // updateUI (user)
                } else {
                    Log.w(TAG, "createUserWithEmail: failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.", Toast.LENGTH_SHORT).show()
                    // updateUI(null)
                }

                // hideProgressBar()
            }
    }

    private fun signIn(email: String, password: String) {
        Log.d(TAG, "signIn: $email")
        if (!validateForm()) {
            return
        }
        // showProgressBar()

        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {task ->
                if (task.isSuccessful) {
                    Log.d(TAG, "signInWithEmail: success")
                    // val user = auth.currentUser
                    // updateUI(user)
                } else {
                    Log.w (TAG, "signInWithEmail: failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed", Toast.LENGTH_SHORT).show()
                    // updateUI(null)
                    // checkForMultiFactorFailure(task.exception!!)
                }
            }
    }

    private fun signOut() {
        auth.signOut()
        // updateUI(null)

        Log.d(TAG, "Email sign out")
    }

    private fun reload() {
        auth.currentUser!!.reload().addOnCompleteListener {task ->
            if (task.isSuccessful) {
                // updateUI (auth.currentUser)
                Toast.makeText(this@EmailPasswordActivity,
                "Reload successful!",
                Toast.LENGTH_SHORT).show()
            } else {
                Log.e(TAG, "reload", task.exception)
                Toast.makeText(this@EmailPasswordActivity,
                "Failed to reload user.",
                Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun validateForm(): Boolean {
        var valid = true

        val email = emailInput.text.toString()
        if (TextUtils.isEmpty(email)) {
            emailInput.error = "Required."
            valid = false
        } else {
            emailInput.error = null
        }

        val password = passwordInput.text.toString()
        if (TextUtils.isEmpty(password)) {
            passwordInput.error = "Required."
            valid = false
        } else {
            passwordInput.error = null
        }

        return valid
    }

    private fun getUserInfo () {
        val user = Firebase.auth.currentUser
        user?.let {
            val name = user.displayName
            val email = user.email
            val photoUrl = user.photoUrl

            val emailVerified = user.isEmailVerified
        }
    }

    companion object {
        private const val TAG = "EmailPassword"
        private const val RC_MULTI_FACTOR = 9005
    }
}