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
    private var currentClassId: Int = -1

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

        //Dynamic Drawer Setup
        setUpDrawerMenu()
        navView.setupWithNavController(navController)
        navView.setNavigationItemSelectedListener(this)


        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.homeFragment
            ),
            drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)

        //custom toolbar for destination
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.loadingFragment || destination.id == R.id.signOptionFragment
                || destination.id == R.id.signInFragment || destination.id == R.id.signUpFragment
            ) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }

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
        setContentView(R.layout.activity_main)
    }


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        //val fragmentManagerTransaction = supportFragmentManager.beginTransaction()
        //Log.e("check_drawer_setup", "onNav Called " + currentClassId.toString())

        when(item.itemId) {
            R.id.item_create_a_class -> {
                navController.navigate(R.id.createAClassFragment)
            }

            R.id.item_join_a_class -> {
                navController.navigate(R.id.joinAClassFragment)
            }
            R.id.item_classes_joined, R.id.item_classes_owned, R.id.classes_joined, R.id.classes_owned -> {
                Toast.makeText(applicationContext, "Chose a class", Toast.LENGTH_SHORT).show()
            }
            R.id.item_all_classes -> {
                if (currentClassId != -1) {
                    currentClassId = -1
                    replaceClassFragment(getClassFragment(currentClassId, item.toString()))
                    setAppBarTitle(item.toString())
                }
                //end
            }
            else -> {
                if (currentClassId != item.itemId) {
                    currentClassId = item.itemId
                    replaceClassFragment(getClassFragment(currentClassId, item.toString()))
                    setAppBarTitle(item.toString())
                }
                //end
            }
        }


        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        drawerLayout.closeDrawer(GravityCompat.START)

        return true
    }


    private fun getClassFragment(classId : Int, className : String) : Fragment{
        val classFragment: Fragment

        when (classId) {
            -1 -> {
                classFragment = AllClassesFragment.newInstance(classId, className)
            }
            else -> {
                classFragment = ClassFragment.newInstance(classId, className)

            }
        }

        return classFragment
    }

    private fun replaceClassFragment(classFragment: Fragment) {
        val fragmentManagerTransaction = supportFragmentManager.beginTransaction()
        fragmentManagerTransaction.replace(
            R.id.home_frame_layout,
            classFragment
        )
        //transaction.setReorderingAllowed(true)
        fragmentManagerTransaction.addToBackStack(null)
        fragmentManagerTransaction.commit()
    }

    private fun setAppBarTitle(title: String) {
        supportActionBar?.title = title
    }

    //for default all classes at first time and other class when navigation
    fun setUpCurrentClass() {
        //val navigationView : NavigationView = findViewById(R.id.nav_view)
        val navigationView: NavigationView = binding.navView
        val itemClass: MenuItem

        if (currentClassId == -1) {
            itemClass = navigationView.menu.findItem(R.id.item_all_classes)
        } else {
            itemClass = navigationView.menu.findItem(currentClassId)
        }

        replaceClassFragment(getClassFragment(currentClassId, itemClass.toString()))
        setAppBarTitle(itemClass.toString())

        //onNavigationItemSelected(itemClass)
        //Log.e("check_drawer_setup", currentClassId.toString())
    }
}