package com.example.demoapp1

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.demoapp1.databinding.FragmentDashboardBinding
import com.google.firebase.auth.FirebaseAuth

class DashboardFragment : Fragment() {

    private lateinit var binding: FragmentDashboardBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, view: ViewGroup?, bundle: Bundle?): View? {
        auth = FirebaseAuth.getInstance()
        binding = FragmentDashboardBinding.inflate(inflater, view, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnSignOut.text = "${auth.currentUser?.displayName}"

        binding.btnSignOut.setOnClickListener {
            auth.signOut()
            findNavController().popBackStack()
        }
    }
}