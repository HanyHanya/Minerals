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
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.ViewModelProvider
import com.example.minerals.data.Mineral
import com.example.minerals.data.MineralsViewModel
import com.example.minerals.data.Quality
import com.example.minerals.databinding.FragmentCurrentMineralBinding
import com.example.minerals.helpers.ImageCoder

class CurrentMineralFragment (val MineralToUpdate: Mineral? = null) : Fragment() {
    private lateinit var binding: FragmentCurrentMineralBinding
    private lateinit var qualityAdapter: ArrayAdapter<Quality>
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
        Mineral.quality =
            Quality.convertToQuality(binding.qualitySpinner.selectedItem.toString())
        Mineral.name = binding.nameTextField.editText?.text.toString()
        Mineral.type = binding.materialTextField.text.toString()
        Mineral.subtype = binding.subtypeTextField.editText?.text.toString()
        Mineral.weight = binding.weightTextField.editText?.text.toString()
        Mineral.location = binding.locationTextField.editText?.text.toString()
    }

    private fun setProps(Mineral : Mineral) {
        var position = qualityAdapter.getPosition(Mineral.quality)
        binding.qualitySpinner.setSelection(position)
        binding.nameTextField.editText?.setText(Mineral.name)
        binding.subtypeTextField.editText?.setText(Mineral.subtype)
        binding.locationTextField.editText?.setText(Mineral.location)
        binding.weightTextField.editText?.setText(Mineral.weight)
        binding.materialTextField.setText(Mineral.type)
        binding.MineralImageView.setImageBitmap(ImageCoder.decodeBitmap(Mineral.image))
    }
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview())
    { bitmap ->
        if (bitmap != null) {
            binding.MineralImageView.setImageBitmap(bitmap)
        }
    }
}