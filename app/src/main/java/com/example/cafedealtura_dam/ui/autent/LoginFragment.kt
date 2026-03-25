package com.example.cafedealtura_dam.ui.autent

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.cafedealtura_dam.R
import com.example.cafedealtura_dam.utils.applyTopInsets

import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetCredentialResponse
import androidx.credentials.CustomCredential
import androidx.credentials.exceptions.GetCredentialException

import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch
import com.example.cafedealtura_dam.utils.SessionManager

class LoginFragment : Fragment() {

    private lateinit var auth: FirebaseAuth
    private lateinit var credentialManager: CredentialManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        auth = FirebaseAuth.getInstance()
        credentialManager = CredentialManager.create(requireContext())
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.applyTopInsets()

        val btnLogin = view.findViewById<Button>(R.id.btnLogin)
        val tvRegister = view.findViewById<TextView>(R.id.tvRegister)
        val btnGoogle = view.findViewById<ImageView>(R.id.btnGoogle)

        btnLogin.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
        }

        tvRegister.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
        }

        btnGoogle.setOnClickListener {
            android.widget.Toast.makeText(requireContext(), "Click Google", android.widget.Toast.LENGTH_SHORT).show()
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val googleIdOption = GetGoogleIdOption.Builder()
            .setServerClientId(getString(R.string.server_client_id))
            .setFilterByAuthorizedAccounts(false)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        lifecycleScope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = requireContext()
                )
                handleSignIn(result)
            } catch (e: GetCredentialException) {
                e.printStackTrace()
                android.widget.Toast.makeText(
                    requireContext(),
                    "Error Google: ${e.message}",
                    android.widget.Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun handleSignIn(result: GetCredentialResponse) {
        val credential = result.credential

        if (credential is CustomCredential &&
            credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
        ) {
            try {
                val googleIdTokenCredential =
                    GoogleIdTokenCredential.createFrom(credential.data)

                val firebaseCredential = GoogleAuthProvider.getCredential(
                    googleIdTokenCredential.idToken,
                    null
                )

                auth.signInWithCredential(firebaseCredential)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val firebaseUser = auth.currentUser
                            val fullName = firebaseUser?.displayName ?: "Usuario"
                            val firstName = fullName.split(" ").firstOrNull() ?: "Usuario"

                            val sessionManager = SessionManager(requireContext())
                            sessionManager.saveUserName(firstName)

                            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                        } else {
                            task.exception?.printStackTrace()
                            android.widget.Toast.makeText(
                                requireContext(),
                                "Firebase error: ${task.exception?.message}",
                                android.widget.Toast.LENGTH_LONG
                            ).show()
                        }
                    }

            } catch (e: GoogleIdTokenParsingException) {
                e.printStackTrace()
            }
        }
    }
}