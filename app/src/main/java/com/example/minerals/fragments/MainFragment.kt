package com.example.minerals.fragments

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.graphics.Bitmap
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.minerals.data.Mineral
import com.example.minerals.data.MineralsViewModel
import com.example.minerals.databinding.FragmentMainBinding
import com.example.minerals.ml.MineralsModel
import org.tensorflow.lite.DataType
import org.tensorflow.lite.support.common.ops.NormalizeOp
import org.tensorflow.lite.support.image.ImageProcessor
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.support.image.ops.ResizeOp
import org.tensorflow.lite.support.image.ops.Rot90Op
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer

val SELECT_PICTURE: Int = 200


class MainFragment() : Fragment() {
    private lateinit var binding: FragmentMainBinding
    private var Mineral = Mineral(0)
    private lateinit var mMineralsViewModel: MineralsViewModel
    val fileName = "labels.txt"
    private lateinit var minerallist : List<String>

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

        mMineralsViewModel = ViewModelProvider(this).get(MineralsViewModel::class.java)

        val inputString = requireActivity().application.assets.open(fileName).bufferedReader().use{it.readText()}
        minerallist = inputString.split("\n")

        binding.NewImageButton.setOnClickListener {
            takePicturePreview.launch(null)
        }
        binding.PickImageButton.setOnClickListener {
            val gallery = Intent(ACTION_OPEN_DOCUMENT, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(gallery, SELECT_PICTURE)
        }

        binding.addBtn.setOnClickListener {
            saveProps()
            mMineralsViewModel.addMineral(Mineral)
            onMineralAdded?.invoke(Mineral)
        }


    }

    private fun saveProps() {
        Mineral.name = binding.nameTextField.editText?.text.toString()
        Mineral.note = binding.notesTextField.editText?.text.toString()
        Mineral.type = binding.materialTextField.text.toString()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            Mineral.image = data?.data.toString()
            binding.MineralImageView.setImageURI(data?.data)
            val contentResolver = requireActivity().contentResolver
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
            outputGenerator( bitmap )
        }
    }
    //gallery
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview())
    { bitmap ->
        if (bitmap != null) {
            binding.MineralImageView.setImageBitmap(bitmap)
            outputGenerator(bitmap)
        }
    }

    private fun outputGenerator(bitmap: Bitmap){
        val model = MineralsModel.newInstance(requireContext())
        val imageProcessor : ImageProcessor = ImageProcessor.Builder()
            .add(ResizeOp(224, 224, ResizeOp.ResizeMethod.NEAREST_NEIGHBOR))
            .add(Rot90Op())
            .add(NormalizeOp(127.5f, 127.5f))
            .build()
        var tensorImage = TensorImage(DataType.FLOAT32)
        tensorImage.load(bitmap);
        tensorImage = imageProcessor.process(tensorImage);
        val byteBuffer = tensorImage.buffer
        val inputFeature0 = TensorBuffer.createFixedSize(intArrayOf(1, 224, 224, 3), DataType.FLOAT32)
        inputFeature0.loadBuffer(byteBuffer)
        val outputs = model.process(inputFeature0)
        val outputFeature0 = outputs.outputFeature0AsTensorBuffer
        var max = outputFeature0.floatArray.maxOrNull()
        Log.d("Predicted Value", minerallist.elementAt(max!!.toInt()))
        binding.materialTextField.setText(minerallist.elementAt(max!!.toInt()).substring(2))

        model.close()
    }

}
