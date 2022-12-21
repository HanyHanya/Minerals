package com.example.minerals.fragments

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.example.minerals.Mineral
import com.example.minerals.Repositories.IRepository
import com.example.minerals.databinding.FragmentMainBinding

val SELECT_PICTURE: Int = 200


class MainFragment() : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private var Mineral = Mineral()
    private lateinit var MineralRepository: IRepository

    var onMineralAdded: ((Mineral) -> Unit)? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.NewImageButton.setOnClickListener {
            if (ContextCompat.checkSelfPermission( requireContext(), android.Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED
            ){
                takePicturePreview.launch(null)
            }
            else{
                requestPermission.launch(android.Manifest.permission.CAMERA)
            }
        }
        binding.PickImageButton.setOnClickListener {
            val gallery = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI)
            startActivityForResult(gallery, SELECT_PICTURE)
        }
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Mineral.image = data?.data
            binding.MineralImageView.setImageURI(data?.data)
        }
    }
    //camera
    private val requestPermission = registerForActivityResult(ActivityResultContracts.RequestPermission())
    { granted->
        if (granted) {
           takePicturePreview.launch(null)
        }
        else {
            Toast.makeText(requireContext(), "Add permission", Toast.LENGTH_SHORT).show()
        }
    }
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview())
    { bitmap ->
        if (bitmap != null) {
            binding.MineralImageView.setImageBitmap(bitmap)
        }
    }
}
