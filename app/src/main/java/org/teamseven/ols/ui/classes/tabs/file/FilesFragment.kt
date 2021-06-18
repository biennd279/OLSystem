package org.teamseven.ols.ui.classes.tabs.file

import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.contentValuesOf
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.tasks.Task
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ListResult
import com.google.firebase.storage.StorageReference
import com.google.firebase.storage.ktx.storage
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentFilesBinding
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStream
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class FilesFragment : Fragment() {

    private lateinit var binding: FragmentFilesBinding
    private lateinit var navController: NavController
    private var filesItems: MutableList<FileItem> = mutableListOf()
    lateinit var storage: FirebaseStorage
    private val filepath = "MyFileStorage"
    internal var myExternalFile: File?=null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFilesBinding.inflate(inflater)
        navController = findNavController()

        //recyclerView
        val recyclerView = binding.recyclerFileList
        storage = Firebase.storage

        //call func from FileViewModel to get all the file information
        //this is a test, remove it latter
        //but i dont know, what happen when click the file item
        getFileList()

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = activity?.let {
            FileAdapter(it, filesItems) {
                val fileRef = storage.reference.child(it.file_name.toString())
                Log.d(TAG, fileRef.toString())
                downloadToLocalFile(fileRef, it)
            }
        }

        binding.btnNewFile.setOnClickListener {
            //i dont know about this stuff
            navController.navigate(R.id.addFileFragment)
        }

        return binding.root
    }

    companion object {
        val TAG = FilesFragment::class.java.simpleName

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(tab: Int, classId: Int): FilesFragment {
            val filesFragment = FilesFragment()
            val args = Bundle()
            args.putInt("tab", tab)
            args.putInt("classId", classId)
            filesFragment.arguments = args
            return filesFragment
        }
    }

    private fun getFileList(){
        val listRef = storage.reference
        val listAllTask: Task<ListResult> = listRef.listAll()
        listAllTask
            .addOnSuccessListener { result ->
                val items: List<StorageReference> = result.items
                filesItems.clear()
                items.forEachIndexed { index, item ->
                    // All the items under listRef.
                    item.downloadUrl.addOnSuccessListener {
                        val startIndex = item.name.lastIndexOf('_', item.name.length - 1, true)
                        var time_stamp = ""
                        if (startIndex > -1) {
                            time_stamp = item.name.substring(startIndex + 1, startIndex + 9)
                            Log.d(TAG, time_stamp + " start_index = " + startIndex.toString())
                        } else {
                            time_stamp = "20210619"
                        }
                        val year = time_stamp.substring(0, 4).toInt()
                        val month = time_stamp.substring(4, 6).toInt()
                        val day = time_stamp.substring(6, 8).toInt()

                        val time = LocalDateTime.of(year, month, day, 0, 0)
                        val formatter = DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
                        val formatted = time.format(formatter)

                        time_stamp = formatted.substring(0, formatted.length - 13)

                        filesItems.add(
                            FileItem(
                                item.name,
                                resources.obtainTypedArray(R.array.avatar).getResourceId(0, 0),
                                time_stamp
                            )
                        )
                    }.addOnCompleteListener {
                        Log.d(TAG, "ok addOnCompleteListener " + item.path.toString())
                    }
                }
            }
            .addOnFailureListener { exception ->
                Log.e(TAG, "list file failure")
            }
    }

    private fun downloadToLocalFile(fileRef: StorageReference?, fileItem: FileItem) {
        if (fileRef != null) {
            val localFile: File = File(Environment.getExternalStorageDirectory(), fileItem.file_name)
            try {

               // val localFile: File = File.createTempFile("images", "jpg")
                fileRef.getFile(localFile)
                    .addOnSuccessListener {
                        val bitmap = BitmapFactory.decodeFile(localFile.absolutePath)
                        val uri: Uri = saveImageToExternalStorage(bitmap, fileItem)

                        Toast.makeText(activity, "Success!", Toast.LENGTH_LONG).show()
                    }
                    .addOnFailureListener { exception ->
                        Toast.makeText(activity, exception.message, Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        // progress percentage
                        val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount

                        // percentage in progress
                        val intProgress = progress.toInt()
                        Toast.makeText(activity, "Download " + fileItem.file_name + " " + intProgress + "%...", Toast.LENGTH_LONG).show()
                    }


//                val fileOutPutStream = FileOutputStream(myExternalFile)
//                fileOutPutStream.write(fileData.text.toString().toByteArray())
//                fileOutPutStream.close()
            } catch (e: IOException) {
                e.printStackTrace()
            }
        }


    }

    private fun saveImageToExternalStorage(bitmap: Bitmap, fileItem: FileItem):Uri{
        // Get the external storage directory path
        val path = Environment.getExternalStorageDirectory().toString()

        // Create a file to save the image
        val file = File(path, fileItem.file_name)

        try {
            // Get the file output stream
            val stream: OutputStream = FileOutputStream(file)

            // Compress the bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)

            // Flush the output stream
            stream.flush()

            // Close the output stream
            stream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }

        // Return the saved image path to uri
        return Uri.parse(file.absolutePath)
    }

}