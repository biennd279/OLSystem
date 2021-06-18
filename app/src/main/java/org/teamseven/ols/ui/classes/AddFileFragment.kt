package org.teamseven.ols.ui.classes

import android.app.Activity.RESULT_OK
import android.content.ClipData
import android.content.Intent
import android.graphics.Bitmap
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
import com.google.android.gms.tasks.Task
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import org.teamseven.ols.databinding.FragmentAddFileBinding
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import kotlin.math.log

class AddFileFragment : Fragment() {
    private lateinit var binding : FragmentAddFileBinding

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
        binding.imgSuccess.visibility = View.INVISIBLE

        imageReference = FirebaseStorage.getInstance().reference

        binding.btnChooseFile.setOnClickListener {
            val intent = Intent()
            intent.type = "*/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(
                Intent.createChooser(intent, "Select File"),
                CHOOSING_IMAGE_REQUEST
            )
        }
        binding.btnUploadFile.setOnClickListener {
            if (fileUri != null) {
                val fileName = binding.editFileName.text.toString()

                if (!validateInputFileName(fileName)) {
                    Toast.makeText(requireContext(), "Enter file name!", Toast.LENGTH_SHORT).show()
                }

                val current = LocalDateTime.now()
                val formatter = DateTimeFormatter.BASIC_ISO_DATE
                val formatted = current.format(formatter)

                val fileRef = imageReference!!.child(fileName + "_" + formatted + "." + getFileExtension(fileUri!!))
                fileRef.putFile(fileUri!!)
                    .addOnSuccessListener { taskSnapshot ->
//                        val name = taskSnapshot.metadata!!.name
//                        val url = taskSnapshot.uploadSessionUri.toString()
                        binding.tvFileName.text = taskSnapshot.metadata!!.path + " - " + taskSnapshot.metadata!!.sizeBytes / 1024 + " KBs"
                        binding.imgSuccess.visibility = View.VISIBLE
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

        if (requestCode == CHOOSING_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.data != null) {
            fileUri = data.data
        }
    }

    private fun getFileExtension(uri: Uri): String? {
        val contentResolver = requireContext().contentResolver
        val mime = MimeTypeMap.getSingleton()

        return mime.getExtensionFromMimeType(contentResolver.getType(uri))
    }

    private fun validateInputFileName(fileName: String): Boolean {
        if (TextUtils.isEmpty(fileName)) {
            return false
        }

        return true
    }

}