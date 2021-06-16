package org.teamseven.ols.ui.classes.tabs.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentFilesBinding

class FilesFragment : Fragment() {

    private lateinit var binding: FragmentFilesBinding
    private var filesItems: MutableList<FileItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        binding = FragmentFilesBinding.inflate(inflater)

        //recyclerView
        val recyclerView = binding.recyclerFileList
        getFileList()

        recyclerView.layoutManager = LinearLayoutManager(activity)
        recyclerView.adapter = activity?.let {
            FileAdapter(it, filesItems) {
                val toast = Toast.makeText(activity, it.file_name, Toast.LENGTH_LONG)
                toast.show()
            }
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
        val file_name = resources.getStringArray(R.array.name_of_file)
        val file_type_icon = resources.obtainTypedArray(R.array.avatar)
        val file_upload_date = resources.getStringArray(R.array.upload_date)

        filesItems.clear()
        for (i in file_name.indices) {
            filesItems.add(
                    FileItem(
                        file_name[i],
                        file_type_icon.getResourceId(i, 0),
                        file_upload_date[i]
                    )
            )
        }
    }

}