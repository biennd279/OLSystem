package org.teamseven.ols.ui.classes

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.MimeTypeMap
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.initialize
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import com.google.firebase.storage.ktx.storageMetadata
import org.teamseven.ols.databinding.FragmentAddFileBinding
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileInputStream

class AddFileFragment : Fragment() {
    private lateinit var binding : FragmentAddFileBinding
    private lateinit var navController : NavController

    private val TAG = "StorageActivity"
    private val CHOOSING_IMAGE_REQUEST = 1234

    private var fileUri: Uri? = null
    private var bitmap: Bitmap? = null
    private var imageReference: StorageReference? = null
    lateinit var storage: FirebaseStorage

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAddFileBinding.inflate(inflater)

        binding.tvFileName.text = ""
        imageReference = FirebaseStorage.getInstance().reference.child("images")

        binding.btnChooseFile.setOnClickListener {
            val intent = Intent()
            intent.type = "*/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select Image"),
                CHOOSING_IMAGE_REQUEST
            )
        }
        binding.btnUploadFile.setOnClickListener {
            if (fileUri != null) {
                val fileName = binding.editFileName.text.toString()

                if (!validateInputFileName(fileName)) {
                    Toast.makeText(requireContext(), "Enter file name!", Toast.LENGTH_SHORT).show()
                }

                val fileRef = imageReference!!.child(fileName + "." + getFileExtension(fileUri!!))
                fileRef.putFile(fileUri!!)
                    .addOnSuccessListener { taskSnapshot ->
//                        Log.e(TAG, "Uri: " + taskSnapshot.downloadUrl)
//                        Log.e(TAG, "Name: " + taskSnapshot.metadata!!.name)
                        binding.tvFileName.text = taskSnapshot.metadata!!.path + " - " + taskSnapshot.metadata!!.sizeBytes / 1024 + " KBs"
                        Toast.makeText(requireContext(), "File Uploaded ", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { exception ->
                        Log.e(TAG, "Failure Listener")
                        Toast.makeText(requireContext(), exception.message, Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        // progress percentage
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount

                        // percentage in progress dialog
                        val intProgress = progress.toInt()
                        binding.tvFileName.text = "Uploaded " + intProgress + "%..."
                    }
                    .addOnPausedListener { System.out.println("Upload is paused!") }

            } else {
                Toast.makeText(requireContext(), "No File!", Toast.LENGTH_LONG).show()
            }
        }

        return binding.root
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

//        if (bitmap != null) {
//            bitmap!!.recycle()
//        }

        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
//            try {
//                bitmap = MediaStore.Images.Media.getBitmap(contentResolver, fileUri)
//                imgFile.setImageBitmap(bitmap)
//            } catch (e: IOException) {
//                e.printStackTrace()
//            }
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()

        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun validateInputFileName(fileName: String): Boolean {
        if (TextUtils.isEmpty(fileName)) {
            //Toast.makeText(requireContext(), "Enter file name!", Toast.LENGTH_SHORT).show()
            return false
        }

        return true
    }

}