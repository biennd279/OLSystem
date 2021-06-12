package org.teamseven.ols.ui.classes.tab.file

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.teamseven.ols.R
import org.teamseven.ols.databinding.FragmentFilesBinding
import org.teamseven.ols.databinding.FragmentMessagesBinding
import org.teamseven.ols.ui.classes.tab.message.MessagesFragment

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
        val list = binding.fileList
        getFileList()

        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = activity?.let {
            FileAdapter(it, filesItems) {
                val toast = Toast.makeText(activity, it.name_of_file, Toast.LENGTH_LONG)
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
        val name_of_file = resources.getStringArray(R.array.name_of_file)
        //val type_of_file = resources.getStringArray(R.array.type_of_file)
        val type_of_file = resources.obtainTypedArray(R.array.avatar)
        val upload_date = resources.getStringArray(R.array.upload_date)

        filesItems.clear()
        for (i in name_of_file.indices) {
            filesItems.add(
                    FileItem(
                            name_of_file[i],
                            type_of_file.getResourceId(i, 0),
                            upload_date[i]
                    )
            )
        }
    }

}