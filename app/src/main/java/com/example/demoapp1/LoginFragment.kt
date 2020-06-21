package com.example.demoapp1

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.demoapp1.databinding.FragmentLoginBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class LoginFragment : Fragment() {

    private lateinit var binding: FragmentLoginBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, view: ViewGroup?, bundle: Bundle?): View? {
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding = FragmentLoginBinding.inflate(inflater, view, false)
        auth = FirebaseAuth.getInstance()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignIn.setOnClickListener {
            var isValid = true

            if (binding.etUserEmail.text.isNullOrBlank()) {
                binding.etUserEmail.error = getString(R.string.cannot_blank)
                isValid = false
            } else binding.etUserEmail.error = null

            if (binding.etPassword.text.isNullOrBlank()) {
                binding.etPassword.error = getString(R.string.cannot_blank)
                isValid = false
            } else if (binding.etPassword.text?.length!! < 7) {
                binding.etPassword.error = getString(R.string.password_too_short)
                isValid = false
            } else binding.etPassword.error = null

            if (isValid) {
                viewModel.userName = binding.etUserEmail.text?.trim().toString()
                findNavController().navigate(R.id.action_LoginFragment_to_DashboardFragment)
            }
        }
        binding.btnForgotPassword.setOnClickListener {
            Snackbar.make(binding.root, R.string.under_dev, Snackbar.LENGTH_SHORT).show()
        }
        binding.btnFb.setOnClickListener {
            Snackbar.make(binding.root, "Use Facebook Profile...", Snackbar.LENGTH_SHORT).show()
        }
        binding.btnGoogle.setOnClickListener {
            googleSignIn()
        }
    }

    private fun googleSignIn() {

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.google_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, ActivityRequest.GOOGLE_LOGIN.code)
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == ActivityRequest.GOOGLE_LOGIN.code) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account)
            } catch (e: ApiException) {
                Toast.makeText(activity, "Google SignIn Failed", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(account: GoogleSignInAccount) {

        val credential = GoogleAuthProvider.getCredential(account.idToken, null)
        auth.signInWithCredential(credential).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Snackbar.make(
                    binding.root,
                    "Welcome: ${auth.currentUser?.displayName}",
                    Snackbar.LENGTH_LONG
                ).show()
                findNavController().navigate(R.id.action_LoginFragment_to_DashboardFragment)
            } else {
                Snackbar.make(binding.root, "Authentication Failed", Snackbar.LENGTH_LONG).show()
            }
        }
    }

    private enum class ActivityRequest(val code: Int) {
        GOOGLE_LOGIN(1213),
        FACEBOOK_LOGIN(4227)
    }
}