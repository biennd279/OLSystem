package org.teamseven.ols

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.SubMenu
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.google.android.material.navigation.NavigationView
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.InternalCoroutinesApi
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.teamseven.ols.databinding.ActivityMainBinding
import org.teamseven.ols.databinding.NavHeaderMainBinding
import org.teamseven.ols.db.AppDatabase
import org.teamseven.ols.entities.Classroom
import org.teamseven.ols.entities.User
import org.teamseven.ols.network.AuthService
import org.teamseven.ols.network.ClassroomService
import org.teamseven.ols.network.MessageApiService
import org.teamseven.ols.network.UserService
import org.teamseven.ols.ui.classes.HomeFragmentDirections
import org.teamseven.ols.ui.classes.all_classes.AllClassesFragment
import org.teamseven.ols.ui.classes.class_joined.ClassJoinedFragment
import org.teamseven.ols.ui.classes.class_owned.ClassOwnedFragment
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import org.teamseven.ols.viewmodel.*
import timber.log.Timber


@ExperimentalCoroutinesApi
class MainActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private var currentClassId: Int = -1

    private lateinit var navHeader: View
    private lateinit var headerBinding: NavHeaderMainBinding


    private val userService by lazy { UserService.create(application) }

    private val authService by lazy { AuthService.create(application) }

    private val classroomService by lazy { ClassroomService.create(application) }

    private val appDatabase by lazy { AppDatabase.create(application) }

    private val sessionManager by lazy { SessionManager(application) }

    private val messageApiService by lazy { MessageApiService.create(application) }



    private val classroomViewModel: ClassroomViewModel by viewModels {
        ClassroomViewModelFactory(
            classroomService,
            appDatabase,
            application
        )
    }

    private val userViewModel: UserViewModel by viewModels {
        UserViewModelFactory(
            authService,
            userService,
            appDatabase,
            application
        )
    }

    private val messageViewModel: MessageViewModel by viewModels {
        MessageViewModelFactory(
            messageApiService,
            appDatabase,
            application
        )
    }


    init {
        lifecycleScope.launchWhenResumed {
            refreshProfile()
            refreshClassroomJoined()
            refreshClassroomOwner()
        }

        lifecycleScope.launchWhenStarted {
            sessionManager.flow.collect {
                if (it.isNullOrEmpty()) {
                    navController.navigate(R.id.signInFragment)
                } else {
                    val newToken = userViewModel.validateToken.first()
                    if (newToken.status != Resource.Status.SUCCESS) {
                        sessionManager.token = null
                        navController.navigate(R.id.signInFragment)
                    } else {
                        sessionManager.token = newToken.data?.token!!
                        messageViewModel.onUpdateToken()
                        refreshProfile()
                        refreshClassroomJoined()
                        refreshClassroomOwner()
                    }
                }
            }
        }
    }


    private var _classOwned: MutableLiveData<List<Classroom>> = MutableLiveData()

    private var _classJoined: MutableLiveData<List<Classroom>> = MutableLiveData()

    private var _currentPerson: MutableLiveData<User> = MutableLiveData()


    @ExperimentalCoroutinesApi
    @InternalCoroutinesApi
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

        navHeader =  binding.navView.getHeaderView(0)
        headerBinding  = NavHeaderMainBinding.bind(navHeader)

        setUpUi()
        drawerLayout.closeDrawers()

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
            if (destination.id == R.id.loadingFragment || destination.id == R.id.signOptionFragment
                || destination.id == R.id.signInFragment || destination.id == R.id.signUpFragment
            ) {
                supportActionBar?.hide()
            } else {
                supportActionBar?.show()
            }
        }

    }

    private fun setUpUi() {
        val classesOwnedGroupItem: MenuItem = navView.menu.findItem(R.id.item_classes_owned)
        val classesOwnedSubMenu: SubMenu = classesOwnedGroupItem.subMenu
        val classesJoinedGroupItem: MenuItem = navView.menu.findItem(R.id.item_classes_joined)
        val classesJoinedSubMenu: SubMenu = classesJoinedGroupItem.subMenu

        _classOwned.observe(this) {
            classesOwnedSubMenu.clear()

            it.map { classroom -> classroom.name }
                .withIndex()
                .forEach { (index, value) ->
                    classesOwnedSubMenu.add(
                        R.id.classes_owned,
                        index + 1,
                        0,
                        value
                    )
                        .setIcon(R.drawable.ic_class_icon)
                }
        }

        _classJoined.observe(this) {
            classesJoinedSubMenu.clear()

            it.map { classroom -> classroom.name }
                .withIndex()
                .forEach { (index, value) ->
                    classesJoinedSubMenu.add(
                        R.id.classes_owned,
                        index + 1,
                        0,
                        value
                    )
                        .setIcon(R.drawable.ic_class_icon)
                }
        }

        _currentPerson.observe(this) {
            headerBinding.userName.text = it.name

            if (it.avatarUrl != null) {
                Glide.with(this).load(it.avatarUrl).into(headerBinding.avatar)
            } else {
                Glide.with(this).load(R.drawable.ic_person_outline)
                    .into(headerBinding.avatar)

            }
        }

        //Only call for active lazy load
        messageViewModel

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
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToAccountSettingFragment())
                true
            }
            R.id.sign_out -> {
                Toast.makeText(applicationContext, "signOutClicked", Toast.LENGTH_SHORT)

                sessionManager.token = null
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
        } else {
            super.onBackPressed()
        }
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {

        when (item.itemId) {
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
                AllClassesFragment.newInstance(
                    classId,
                    className
                )
            }
            else -> {
                if (className in _classOwned.value?.map { it.name } ?: listOf()) {
                    ClassOwnedFragment.newInstance(
                        classId,
                        className
                    )
                } else {
                    ClassJoinedFragment.newInstance(
                        classId,
                        className
                    )
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

    fun setAppBarTitle(title: String) {
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

    private fun refreshClassroomOwner() {
        classroomViewModel.classOwner.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                    if (it.data.isNullOrEmpty()) {
                        return@observe
                    }

                    _classOwned.value = it.data
                }

                Resource.Status.ERROR -> Timber.i("Load owned classroom error ${it.message}")

            }
        }
    }

    private fun refreshClassroomJoined() {


        classroomViewModel.classJoined.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                    if (it.data.isNullOrEmpty()) {
                        return@observe
                    }

                    _classJoined.value = it.data

                }

                Resource.Status.ERROR -> Timber.i("Load joined classroom error ${it.message}")
            }
        }
    }

    private fun refreshProfile() {
        userViewModel.currentUser.observe(this) {
            when (it.status) {
                Resource.Status.SUCCESS, Resource.Status.LOADING -> {
                    if (it.data == null) {
                        return@observe
                    }

                    _currentPerson.value = it.data
                }

                Resource.Status.ERROR -> Timber.i("Load profile error ${it.message}")
            }
        }
    }

    fun onLeaveClassroom() {
        refreshClassroomJoined()
    }

    fun onJoinedClassroom(classId: Int) {
        refreshClassroomJoined()
        currentClassId = -1
    }

    fun onCreateClassroom(classId: Int, classroomName: String) {
        refreshClassroomOwner()
        currentClassId = -1
    }
}