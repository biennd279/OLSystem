package org.teamseven.ols

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationView
import org.teamseven.ols.databinding.ActivityMainBinding
import org.teamseven.ols.ui.classes.`class`.ClassFragment
import org.teamseven.ols.ui.classes.all_classes.AllClassesFragment
import timber.log.Timber


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController

    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Timber.plant(Timber.DebugTree())

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        navController = findNavController(R.id.nav_host_fragment_content_main)

        val toolbar: Toolbar = binding.appBarMain.toolbar
        setSupportActionBar(toolbar)

        drawerLayout = binding.drawerLayout
        navView = binding.navView

        setUpDrawerMenu()

        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)
        //navView.setupWithNavController(navController)


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment
            ),
            drawerLayout
        )

        setupActionBarWithNavController(navController, appBarConfiguration)


        /*
        //custom toolbar for destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loadingFragment || destination.id == R.id.signOptionFragment
                || destination.id == R.id.signInFragment || destination.id == R.id.signUpFragment
            ) {
                supportActionBar?.hide()
                //tabs.visibility = View.GONE

            } /*else {
                if (destination.id == R.id.homeFragment) {
                    //tabs.removeTabAt(3)
                    //tabs.removeTabAt(2)
                } else {
                    //toolbar.visibility = View.VISIBLE
                    //drawerLayout.visibility = View.VISIBLE
                }
            }*/
        }*/



    }

    private fun setUpDrawerMenu() {
        val classedOwnedGroupItem: MenuItem = navView.menu.findItem(R.id.item_classes_owned)
        val classedOwnedSubMenu: SubMenu = classedOwnedGroupItem.subMenu

        //get all owned classes -> array -> for
        //use class_id (id) for item_id (Menu.NONE for present)
        //classedOwnedSubMenu.add(R.id.classes_owned, Menu.NONE, 0, "class_test").setIcon(R.drawable.ic_action_class)
        classedOwnedSubMenu.add(R.id.classes_owned, 1, 0, "class_test").setIcon(R.drawable.ic_action_class)
        classedOwnedSubMenu.add(R.id.classes_owned, Menu.NONE, 0, "class_funny").setIcon(R.drawable.ic_action_class)

        val classedJoinedGroupItem: MenuItem = navView.menu.findItem(R.id.item_classes_joined)
        val classedJoinedSubMenu: SubMenu = classedJoinedGroupItem.subMenu

        //get all joined classes -> array -> for
        classedJoinedSubMenu.add(R.id.classes_joined, Menu.NONE, 0, "class_enjoined").setIcon(R.drawable.ic_action_class)
        classedJoinedSubMenu.add(R.id.classes_joined, Menu.NONE, 0, "class_interested").setIcon(R.drawable.ic_action_class)

        drawerLayout.closeDrawers()

    }


    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.overflow_home_menu, menu)
        return true
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.account_settings -> {
                navController.navigate(R.id.accountSettingFragment)
                true
            }
            R.id.sign_out -> {
                Toast.makeText(applicationContext, "signOutClicked", Toast.LENGTH_SHORT)
                navController.navigate(R.id.signOptionFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }


    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }


    override fun onBackPressed() {
        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START)
        }
        else {
            super.onBackPressed()
        }
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val transaction = supportFragmentManager.beginTransaction()

        when(item.itemId) {
            R.id.item_all_classes -> {
                transaction.replace(
                    R.id.home_frame_layout,
                    getClassFragment(-1, item.toString())
                )
                transaction.commit()
                setAppBarTitle(item.toString())
            }
            else -> {
                transaction.replace(
                    R.id.home_frame_layout,
                    getClassFragment(item.itemId, item.toString())
                )
                transaction.commit()
                setAppBarTitle(item.toString())
            }
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }


    fun getClassFragment(classId : Int, className : String) : Fragment{
        val classfragment: Fragment

        if (classId == -1) {
            classfragment= AllClassesFragment.newInstance(classId, className)
        } else {
            classfragment = ClassFragment.newInstance(classId, className)
        }

        return classfragment
    }
/*
    private fun replaceFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.nav_host_fragment_content_main, fragment)
        transaction.commit();
    }
*/

    fun setAppBarTitle(title: String) {
        supportActionBar?.title = title
    }
}