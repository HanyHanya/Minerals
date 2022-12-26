package com.example.minerals.fragments

import android.app.Activity
import android.content.Intent
import android.content.Intent.ACTION_OPEN_DOCUMENT
import android.content.Intent.ACTION_PICK
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.minerals.data.Mineral
import com.example.minerals.data.MineralsViewModel
import com.example.minerals.data.Quality
import com.example.minerals.databinding.FragmentMainBinding
import com.example.minerals.helpers.ImageCoder
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
    private lateinit var qualityAdapter: ArrayAdapter<Quality>
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

        qualityAdapter = ArrayAdapter<Quality>(
            requireContext(),
            android.R.layout.simple_spinner_item,
            Quality.values()
        )

        binding.qualitySpinner.adapter = qualityAdapter

        /*binding.NewImageButton.setOnClickListener {
            val ACTION_IMAGE_CAPTURE = 1
            val camera = Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE)
            startActivityForResult(camera, ACTION_IMAGE_CAPTURE)
            takePicturePreview.launch(null)
        }*/
        binding.PickImageButton.setOnClickListener {
            val gallery = Intent(ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            gallery.setType("image/*");
            startActivityForResult(gallery, SELECT_PICTURE)
        }

        binding.addBtn.setOnClickListener {
            saveProps()
            mMineralsViewModel.addMineral(Mineral)
            onMineralAdded?.invoke(Mineral)
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == SELECT_PICTURE) {
            binding.MineralImageView.setImageURI(data?.data)
            val contentResolver = requireActivity().contentResolver
            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, data?.data)
            Mineral.image = ImageCoder.encodeBitmap(bitmap)
            outputGenerator( bitmap )
        }
    }
    /*//gallery
    private val takePicturePreview = registerForActivityResult(ActivityResultContracts.TakePicturePreview())
    { bitmap ->
        if (bitmap != null) {
            binding.MineralImageView.setImageBitmap(bitmap)
            outputGenerator(bitmap)
        }
    }*/

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
        var indexOfMax = outputFeature0.floatArray.indexOfFirst { f -> f == max }
        Log.d("Predicted Value", minerallist.elementAt(indexOfMax))
        binding.materialTextField.setText(minerallist.elementAt(indexOfMax).substring(2))

        model.close()
    }

}
