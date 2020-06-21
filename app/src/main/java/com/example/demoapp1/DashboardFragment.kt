package com.example.demoapp1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.demoapp1.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var viewModel: MainActivityViewModel
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, view: ViewGroup?, bundle: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentDashboardBinding.inflate(inflater, view, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this).get(MainActivityViewModel::class.java)
        binding.txtDisplayName.text = auth.currentUser?.displayName ?: viewModel.displayName
        binding.txtUserName.text = auth.currentUser?.email ?: viewModel.userName

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            findNavController().popBackStack()
        }
    }
}