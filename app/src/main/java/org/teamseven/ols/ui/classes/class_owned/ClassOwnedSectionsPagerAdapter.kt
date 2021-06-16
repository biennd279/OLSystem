package org.teamseven.ols.ui.classes.class_owned

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.teamseven.ols.R
import org.teamseven.ols.ui.classes.tabs.class_setting.ClassOwnedSettingFragment
import org.teamseven.ols.ui.classes.tabs.file.FilesFragment
import org.teamseven.ols.ui.classes.tabs.message.MessagesFragment
import org.teamseven.ols.ui.classes.tabs.people.PeopleFragment


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
class ClassOwnedSectionsPagerAdapter(private val context: Context, fm: FragmentManager, classId : Int)
    : FragmentPagerAdapter(fm) {

    private var mClassId : Int = classId

    override fun getItem(position: Int): Fragment {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).

        return when(position) {
            0 -> MessagesFragment.newInstance(position + 1, mClassId)
            1 -> FilesFragment.newInstance(position + 1, mClassId)
            2 -> PeopleFragment.newInstance(position + 1, mClassId)
            3 -> ClassOwnedSettingFragment.newInstance(position + 1, mClassId)
            else -> MessagesFragment.newInstance(position + 1, mClassId)
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 4 total pages.
        return 4
    }
}