package com.example.mystoryapp.ui.activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import androidx.lifecycle.lifecycleScope
import com.example.mystoryapp.R
import com.example.mystoryapp.data.model.StorySession
import com.example.mystoryapp.data.network.Result
import com.example.mystoryapp.databinding.ActivityStoryBinding
import com.example.mystoryapp.di.ViewModelFactory
import com.example.mystoryapp.ui.viewmodel.StoryViewModel
import com.example.mystoryapp.util.ImageUtils
import com.example.mystoryapp.util.ImageUtils.reduceFileImage
import com.example.mystoryapp.util.ImageUtils.rotateBitmap
import com.example.mystoryapp.util.ImageUtils.uriToFile
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File

class StoryActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStoryBinding
    private val storyViewModel by viewModels<StoryViewModel> {
        ViewModelFactory.getInstance(this)
    }
    private lateinit var currentPhotoPath: String
    private var getFile: File? = null
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private var lat: Double? = null
    private var lon: Double? = null

    @SuppressLint("QueryPermissionsNeeded")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStoryBinding.inflate(layoutInflater)
        setContentView(binding.root)

        if (!allPermissionsGranted()) {
            ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
        }

        val user: String? = StorySession(this).getToken()

        binding.progressBar.visibility = View.GONE

        binding.apply {
            btnCamera.setOnClickListener {
                val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
                intent.resolveActivity(packageManager)

                ImageUtils.createCustomTempFile(application).also {
                    val photoUri: Uri = FileProvider.getUriForFile(
                        this@StoryActivity,
                        "com.example.mystoryapp",
                        it,
                    )
                    currentPhotoPath = it.absolutePath
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, photoUri)
                    launchCameraIntent.launch(intent)
                }
            }

            btnGallery.setOnClickListener {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                val chooser = Intent.createChooser(intent, "Pilih Gambar")
                launchGalleryIntent.launch(chooser)
            }

            cbAddLocation.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    if (ContextCompat.checkSelfPermission(this@StoryActivity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                        ActivityCompat.requestPermissions(this@StoryActivity, REQUIRED_PERMISSIONS, REQUEST_CODE_PERMISSIONS)
                    } else {
                        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this@StoryActivity)
                        fusedLocationProviderClient.lastLocation.addOnSuccessListener { location ->
                            lat = location?.latitude
                            lon = location?.longitude
                        }
                    }
                } else {
                    lat = null
                    lon = null
                }
            }

            buttonAdd.setOnClickListener {
                if (getFile != null && edAddDescription.text.toString().isNotEmpty()) {
                    val file = reduceFileImage(getFile as File)
                    val description = edAddDescription.text.toString().toRequestBody("text/plain".toMediaType())
                    val requestImageFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    val imageMultiPart: MultipartBody.Part = MultipartBody.Part.createFormData(
                        "photo",
                        file.name,
                        requestImageFile,
                    )

                    lifecycleScope.launch {
                        storyViewModel.uploadStory("Bearer $user", imageMultiPart, description, lat, lon).observe(this@StoryActivity) {
                            if (it != null) {
                                when (it) {
                                    is Result.Loading -> {
                                        binding.progressBar.visibility = View.VISIBLE
                                    }
                                    is Result.Success -> {
                                        binding.progressBar.visibility = View.GONE
                                        val intent = Intent(this@StoryActivity, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                        Toast.makeText(
                                            this@StoryActivity,
                                            getString(R.string.story_uploaded),
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                    is Result.Error -> {
                                        binding.progressBar.visibility = View.GONE
                                        Toast.makeText(
                                            this@StoryActivity,
                                            getString(R.string.error_upload_story),
                                            Toast.LENGTH_SHORT,
                                        ).show()
                                    }
                                }
                            }
                        }
                        Log.d("Hasil", "$description, $lat, $lon")
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray,
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (!allPermissionsGranted()) {
                Toast.makeText(
                    this@StoryActivity,
                    getString(R.string.permissions_not_granted),
                    Toast.LENGTH_SHORT,
                ).show()
            }
        }
    }

    private fun allPermissionsGranted() = REQUIRED_PERMISSIONS.all {
        ContextCompat.checkSelfPermission(baseContext, it) == PackageManager.PERMISSION_GRANTED
    }

    private val launchCameraIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == Activity.RESULT_OK) {
            val myFile = File(currentPhotoPath)
            getFile = myFile
            val result = rotateBitmap(
                BitmapFactory.decodeFile(getFile?.path),
            )
            binding.ivImage.setImageBitmap(result)
        }
    }

    private val launchGalleryIntent = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
        if (it.resultCode == RESULT_OK) {
            val selectedImg: Uri = it.data?.data as Uri
            val myFile = uriToFile(selectedImg, this@StoryActivity)
            getFile = myFile
            binding.ivImage.setImageURI(selectedImg)
        }
    }

    companion object {
        private val REQUIRED_PERMISSIONS = arrayOf(Manifest.permission.CAMERA, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION)
        private const val REQUEST_CODE_PERMISSIONS = 10
    }
}
