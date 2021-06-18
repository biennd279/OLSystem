package org.teamseven.ols.ui.classes.all_classes

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import org.teamseven.ols.R
import org.teamseven.ols.repositories.MessageRepository
import org.teamseven.ols.ui.classes.tabs.file.FilesFragment
import org.teamseven.ols.ui.classes.tabs.messages.ConversationFragment
import org.teamseven.ols.viewmodel.MessageViewModel


private val TAB_TITLES = arrayOf(
        R.string.tab_text_1,
        R.string.tab_text_2
)

/**
 * A [FragmentPagerAdapter] that returns a fragment corresponding to
 * one of the sections/tabs/pages.
 */
@Suppress("DEPRECATION")
class AllClassesSectionsPagerAdapter(
    private val context: Context,
    fm: FragmentManager,
    classId : Int,
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
            else -> ConversationFragment.newInstance(
                position + 1,
                mClassId,
                messageViewModel
            )
        }
    }

    override fun getPageTitle(position: Int): CharSequence? {
        return context.resources.getString(TAB_TITLES[position])
    }

    override fun getCount(): Int {
        // Show 2 total pages.
        return 2
    }
}