package com.example.minerals.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minerals.MainActivity
import com.example.minerals.R
import com.example.minerals.Repositories.IRepository
import com.example.minerals.Services.FragmentManagerService
import com.example.minerals.databinding.ActivityMainBinding
import com.example.minerals.databinding.FragmentListBinding
import com.example.minerals.databinding.FragmentMainBinding

class ListFragment : Fragment() {
    private lateinit var binding: FragmentListBinding
    var addMineral : (()->(Unit))? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentListBinding.inflate(inflater, container, false)
        binding.fab.setOnClickListener { view->
            addMineral?.invoke()
        }
        return binding.root
    }
}


