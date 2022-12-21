package com.example.minerals

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.minerals.Repositories.IRepository
import com.example.minerals.databinding.FragmentCurrentMineralBinding

class CurrentMineralFragment ( val MineralToUpdate: Mineral? = null) : Fragment() {
    private lateinit var binding: FragmentCurrentMineralBinding
    private var Mineral = Mineral()
    private lateinit var MineralRepository: IRepository

    init {
        if(MineralToUpdate != null) {
            Mineral = MineralToUpdate
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentCurrentMineralBinding.inflate(inflater, container, false)
        return binding.root
    }
}