package com.example.admincampusbite

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.example.admincampusbite.databinding.ActivityLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.firebase.Firebase
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.database


class LoginActivity : AppCompatActivity() {

    private lateinit var email: String
    private lateinit var password: String
    private lateinit var auth: FirebaseAuth
    private lateinit var database: DatabaseReference
    private lateinit var googleSignInClient: GoogleSignInClient


    private val binding: ActivityLoginBinding by lazy {
        ActivityLoginBinding.inflate(layoutInflater)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        val googleSignInOptions = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestIdToken(getString(androidx.biometric.R.string.default_web_client_id))
            .requestIdToken(getString(R.string.default_web_client_id))

            .requestEmail().build()


        //inti fire auth
        auth = Firebase.auth
        //init fire base database
        database = Firebase.database.reference
        //google sign in
        googleSignInClient = GoogleSignIn.getClient(this, googleSignInOptions)



        binding.loginbutton.setOnClickListener {

            email = binding.email.text.toString().trim()
            password = binding.password.text.toString().trim()


            if (email.isBlank() || password.isBlank()) {
                Toast.makeText(this, "Please fill all details", Toast.LENGTH_SHORT).show()

            } else {
                signInUser(email, password)
            }

        }
        binding.googleButton.setOnClickListener {
            val signIntent = googleSignInClient.signInIntent
            launcher.launch(signIntent)
        }

        binding.donthavebutton.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

    }

    private fun signInUser(email: String, password: String) {
        auth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                val user: FirebaseUser? = auth.currentUser
                updateUi(user)
            } else {
                Toast.makeText(this, "Login failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
            }

        }
    }

    // launcher for googel sign in
    private val launcher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val task: Task<GoogleSignInAccount> =
                    GoogleSignIn.getSignedInAccountFromIntent(result.data)
                if (task.isSuccessful) {
                    val account: GoogleSignInAccount = task.result
                    val credential: AuthCredential =
                        GoogleAuthProvider.getCredential(account.idToken, null)
                    auth.signInWithCredential(credential).addOnCompleteListener { authTask ->
                        if (authTask.isSuccessful) {
                            Toast.makeText(
                                this,
                                "Sucessfully sign in with google",
                                Toast.LENGTH_SHORT
                            ).show()

//                            updateUi(null)
                            updateUi(auth.currentUser)

                            finish()
                        } else {
                            Toast.makeText(this, "Google Sign in failed", Toast.LENGTH_SHORT).show()

                        }
                    }

                } else {
                    Toast.makeText(this, "Google Sign in failed", Toast.LENGTH_SHORT).show()

                }
            }
        }

    //chack user olready login

    override fun onStart()
    {
        super.onStart()
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser!=null)
        {

                startActivity(Intent(this, MainActivity::class.java))
                finish()


        }

    }
    private fun updateUi(user: FirebaseUser?) {

        user?.let {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

    }


}


//        startActivity(Intent(this, MainActivity::class.java))
//
//        Toast.makeText(this, "Login Successfully", Toast.LENGTH_SHORT).show()
//        finish()