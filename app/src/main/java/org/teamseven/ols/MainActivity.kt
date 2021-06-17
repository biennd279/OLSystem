package org.teamseven.ols

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.widget.Toast
import androidx.activity.viewModels
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
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.network.ClassroomService
import org.teamseven.ols.ui.classes.all_classes.AllClassesFragment
import org.teamseven.ols.ui.classes.class_joined.ClassJoinedFragment
import org.teamseven.ols.ui.classes.class_owned.ClassOwnedFragment
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.viewmodel.ClassroomViewModel
import org.teamseven.ols.viewmodel.ClassroomViewModelFactory
import timber.log.Timber


class MainActivity : AppCompatActivity() , NavigationView.OnNavigationItemSelectedListener  {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var currentClassId: Int = -1

    private val classroomService by lazy {
        ClassroomService.create(application)
    }

    private val appDatabase by lazy {
        AppDatabase.create(application)
    }

    private val classroomViewModel : ClassroomViewModel by viewModels {
        ClassroomViewModelFactory(
            classroomService,
            appDatabase,
            application
        )
    }

    private var _classOwned: List<Classroom> = listOf()

    private var _classJoined: List<Classroom> = listOf()


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

        showClassroomJoinedList()
        showClassroomOwnerList()

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

                //this is for now, remove later
                //delete session in SessionManager
                //navigate to loadingFragment, right there, delete the database.
                //navController.navigate(R.id.loadingFragment)
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

    private fun getClassFragment(classId: Int, className: String): Fragment {

        return when (classId) {
            -1 -> {
                AllClassesFragment.newInstance(classId, className)
            }
            else -> {
                if (className in _classJoined.map { it.name }) {
                    ClassOwnedFragment.newInstance(classId, className, classroomViewModel)
                } else {
                    ClassJoinedFragment.newInstance(classId, className, classroomViewModel)
                }

            }
        }
    }

    private fun replaceClassFragment(classFragment: Fragment) {
        val fragmentManagerTransaction = supportFragmentManager.beginTransaction()
        fragmentManagerTransaction.replace(
            R.id.home_frame_layout,
            classFragment
        )
        //transaction.setReorderingAllowed(true)
        //fragmentManagerTransaction.addToBackStack(null)
        fragmentManagerTransaction.commit()
    }

    private fun setAppBarTitle(title: String) {
        supportActionBar?.title = title
    }

    //for default all classes at first time and other class when navigation
    fun setUpCurrentClass() {
        val navigationView: NavigationView = binding.navView

        val itemClass: MenuItem = if (currentClassId == -1) {
            navigationView.menu.findItem(R.id.item_all_classes)
        } else {
            navigationView.menu.findItem(currentClassId)
        }

        replaceClassFragment(getClassFragment(currentClassId, itemClass.toString()))
        setAppBarTitle(itemClass.toString())

    }

    private fun showClassroomOwnerList() {
        val classesOwnedGroupItem: MenuItem = navView.menu.findItem(R.id.item_classes_owned)
        val classesOwnedSubMenu: SubMenu = classesOwnedGroupItem.subMenu


        classroomViewModel.classOwner.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS, Resource.Status.LOADING -> {

                    if (it.data.isNullOrEmpty()) {
                        return@observe
                    }

                    _classOwned = it.data

                    _classOwned.map { classroom -> classroom.name }
                        .withIndex()
                        .forEach { (index, value) ->
                            classesOwnedSubMenu.add(
                                R.id.classes_owned,
                                index + 1,
                                0,
                                value
                            )
                                .setIcon(R.drawable.ic_action_class)
                        }

                }

                Resource.Status.ERROR -> {
                    Timber.i("Load error")
                }
            }
        }
    }

    private fun showClassroomJoinedList() {
        val classesJoinedGroupItem: MenuItem = navView.menu.findItem(R.id.item_classes_joined)
        val classesJoinedSubMenu: SubMenu = classesJoinedGroupItem.subMenu

        classroomViewModel.classJoined.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                    if (it.data.isNullOrEmpty()) {
                        return@observe
                    }

                    _classJoined = it.data

                    _classJoined.map { classroom -> classroom.name }
                        .withIndex()
                        .forEach { (index, value) ->
                            classesJoinedSubMenu.add(
                                R.id.classes_owned,
                                index + 1,
                                0,
                                value
                            )
                                .setIcon(R.drawable.ic_action_class)
                        }
                }

                Resource.Status.ERROR -> {
                    Timber.i("Load error ${it.message}")
                }
            }
        }
    }
}