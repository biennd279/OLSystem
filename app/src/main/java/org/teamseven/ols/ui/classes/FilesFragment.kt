package org.teamseven.ols.ui.classes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.teamseven.ols.R

class FilesFragment : Fragment() {

    private var filesItems: MutableList<FileItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.fragment_files, container, false)
        var list = root.findViewById<RecyclerView>(R.id.file_list)
        iniData()

        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = activity?.let {
            FileAdapter(it, filesItems) {
                val toast = Toast.makeText(activity, it.name_of_file, Toast.LENGTH_LONG)
                toast.show()
            }
        }
        return root
    }

    companion object {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private const val ARG_SECTION_NUMBER = "section_number"

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        @JvmStatic
        fun newInstance(sectionNumber: Int): FilesFragment {
            return FilesFragment()
        }
    }

    private fun iniData(){
        val name_of_file = resources.getStringArray(R.array.name_of_file)
        val type_of_file = resources.getStringArray(R.array.type_of_file)
        val upload_date = resources.getStringArray(R.array.upload_date)

        filesItems.clear()
        for (i in name_of_file.indices) {
            filesItems.add(
                    FileItem(
                            name_of_file[i],
                            type_of_file[i],
                            upload_date[i]
                    )
            )
        }
    }

}