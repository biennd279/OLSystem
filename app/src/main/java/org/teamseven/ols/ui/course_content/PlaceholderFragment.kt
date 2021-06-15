package org.teamseven.ols.ui.course_content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.teamseven.ols.R


/**
 * A placeholder fragment containing a simple view.
 */
class PlaceholderFragment : Fragment() {

    private lateinit var courseContentViewModel: CourseContentViewModel
    private var messageItems: MutableList<MessageItem> = mutableListOf()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        courseContentViewModel = ViewModelProvider(this).get(CourseContentViewModel::class.java).apply {
            setIndex(arguments?.getInt(ARG_SECTION_NUMBER) ?: 1)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

//        val root = inflater.inflate(R.layout.content_main, container, false)
//        val textView: TextView = root.findViewById(R.id.message_list)
//        courseContentViewModel.text.observe(this, Observer<String> {
//            textView.text = it
//        })

//        val manager: FragmentManager = getSupportFragmentManager()
//        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
       // val root = inflater.inflate(R.layout.fragment_messages, container, false)
//        val navHostFragment: NavHostFragment = childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
//        courseContentViewModel.frag.observe(this, Observer<String> {
//            courseContentViewModel. = it
//        })

        val root = inflater.inflate(R.layout.fragment_messages, container, false)
        var list = root.findViewById<RecyclerView>(R.id.message_list)
        iniData()

        list.layoutManager = LinearLayoutManager(activity)
        list.adapter = activity?.let {
            MessageAdapter(it, messageItems) {
                val toast = Toast.makeText(activity, it.username, Toast.LENGTH_LONG)
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
        fun newInstance(sectionNumber: Int): PlaceholderFragment {
            return PlaceholderFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_SECTION_NUMBER, sectionNumber)
                }
            }
        }
    }

    private fun iniData(){
        val avatar = resources.obtainTypedArray(R.array.avatar)
        val username = resources.getStringArray(R.array.username)
        val status = resources.getStringArray(R.array.status)
        val time = resources.getStringArray(R.array.time)

        messageItems.clear()
        for (i in username.indices) {
            messageItems.add(
                MessageItem(
                    username[i],
                    status[i],
                    avatar.getResourceId(i, 0),
                    time[i]
                )
            )
        }
    }
}