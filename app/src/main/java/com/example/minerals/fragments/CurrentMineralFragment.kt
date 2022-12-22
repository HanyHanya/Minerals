package com.example.minerals.fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.minerals.Mineral
import com.example.minerals.Repositories.IRepository
import com.example.minerals.Repositories.MineralSQLiteRepository
import com.example.minerals.databinding.FragmentCurrentMineralBinding

class CurrentMineralFragment (val MineralToUpdate: Mineral? = null) : Fragment() {
    private lateinit var binding: FragmentCurrentMineralBinding
    private var Mineral = Mineral()
    private lateinit var MineralRepository: IRepository

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
        MineralRepository = MineralSQLiteRepository(requireContext())
        binding.NewImageButton.setOnClickListener {
            takePicturePreview.launch(null)
        }
        binding.MineralImageView.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, SELECT_PICTURE)
        }
        binding.saveBtn.setOnClickListener {
            saveProps()
            MineralRepository.updateMineral(Mineral)
            onMineralAdded?.invoke(Mineral)
        }
        binding.delBtn.setOnClickListener {
            MineralRepository.deleteMineral(Mineral.id)
        }
        if (MineralToUpdate != null) {
            setProps(Mineral)
        }
    }

    private fun saveProps() {
        Mineral.name = binding.nameTextField.toString()
        Mineral.note = binding.notesTextField.toString()
        Mineral.type = binding.materialTextField.toString()
    }

    private fun setProps(Mineral : Mineral) {
        binding.nameTextField.setText(Mineral.name)
        binding.notesTextField.setText(Mineral.note)
        binding.materialTextField.setText(Mineral.type)
        binding.MineralImageView.setImageURI(Mineral.image)
    }
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview())
    { bitmap ->
        if (bitmap != null) {
            binding.MineralImageView.setImageBitmap(bitmap)
        }
    }
}