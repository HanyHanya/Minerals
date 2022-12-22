package com.example.minerals.fragments

import android.content.Intent
import android.content.Intent.*
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.minerals.data.Mineral
import com.example.minerals.data.MineralsViewModel
import com.example.minerals.databinding.FragmentCurrentMineralBinding

class CurrentMineralFragment (val MineralToUpdate: Mineral? = null) : Fragment() {
    private lateinit var binding: FragmentCurrentMineralBinding
    private var Mineral = Mineral(0)
    private lateinit var mMineralsViewModel: MineralsViewModel

    var onMineralAdded: ((Mineral) -> Unit)? = null

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        mMineralsViewModel = ViewModelProvider(this).get(MineralsViewModel::class.java)
        binding.NewImageButton.setOnClickListener {
            takePicturePreview.launch(null)
        }
        binding.MineralImageView.setOnClickListener {
            val gallery = Intent(ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, SELECT_PICTURE)
        }
        binding.saveBtn.setOnClickListener {
            saveProps()
            mMineralsViewModel.updateMineral(Mineral)
            onMineralAdded?.invoke(Mineral)
        }
        binding.delBtn.setOnClickListener {
            mMineralsViewModel.deleteMineral(Mineral)
        }
        if (MineralToUpdate != null) {
            setProps(Mineral)
        }
    }

    private fun saveProps() {
        Mineral.name = binding.nameTextField.text.toString()
        Mineral.note = binding.notesTextField.text.toString()
        Mineral.type = binding.materialTextField.text.toString()
    }

    private fun setProps(Mineral : Mineral) {
        binding.nameTextField.setText(Mineral.name)
        binding.notesTextField.setText(Mineral.note)
        binding.materialTextField.setText(Mineral.type)
        binding.MineralImageView.setImageURI(Uri.parse(Mineral.image))
    }
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview())
    { bitmap ->
        if (bitmap != null) {
            binding.MineralImageView.setImageBitmap(bitmap)
        }
    }
}