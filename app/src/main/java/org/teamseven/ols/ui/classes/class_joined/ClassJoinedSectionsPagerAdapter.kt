package org.teamseven.ols.ui.classes.class_joined

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.LiveData
import org.teamseven.ols.R
import org.teamseven.ols.repositories.MessageRepository
import org.teamseven.ols.ui.classes.tabs.class_setting.ClassJoinedSettingFragment
import org.teamseven.ols.ui.classes.tabs.file.FilesFragment
import org.teamseven.ols.ui.classes.tabs.messages.ConversationFragment
import org.teamseven.ols.ui.classes.tabs.people.PeopleFragment
import org.teamseven.ols.viewmodel.ClassroomViewModel
import org.teamseven.ols.viewmodel.MessageViewModel


private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2,
        R.string.tab_text_3,
        R.string.tab_text_4
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
@Suppress("DEPRECATION")
class ClassJoinedSectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    classId : Int,
    val classroomViewModel: ClassroomViewModel,
    private var messageViewModel: MessageViewModel
) : FragmentPagerAdapter(fm) {

    private var mClassId : Int = classId

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        return when(position) {
            0 -> ConversationFragment.newInstance(
                position + 1,
                mClassId,
                messageViewModel
            )
            1 -> FilesFragment.newInstance(position + 1, mClassId)
            2 -> PeopleFragment.newInstance(
                position + 1,
                mClassId,
                classroomViewModel
            )
            3 -> ClassJoinedSettingFragment.newInstance(
                position + 1,
                mClassId,
                classroomViewModel
            )
            else -> ConversationFragment.newInstance(
                position + 1,
                mClassId,
                messageViewModel
            )
        }
    }

    override fun getPageTitle(position: Int): CharSequence {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 4 total pages.
        return 4
    }
}