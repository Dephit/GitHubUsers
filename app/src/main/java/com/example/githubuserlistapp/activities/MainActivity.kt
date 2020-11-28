package com.example.githubuserlistapp.activities

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.githubuserlistapp.App
import com.example.githubuserlistapp.R
import com.example.githubuserlistapp.adapters.UserListAdapter
import com.example.githubuserlistapp.data.User
import com.example.githubuserlistapp.fragments.ErrorFragment
import com.example.githubuserlistapp.fragments.ListFragment
import com.example.githubuserlistapp.fragments.LoadingFragment
import com.example.githubuserlistapp.fragments.UserDetailFragment
import com.example.githubuserlistapp.interfaces.FragmentCallback
import com.example.githubuserlistapp.sealedClasses.State
import com.example.githubuserlistapp.utills.getLinearLayoutManager
import com.example.githubuserlistapp.utills.setVisible
import com.example.githubuserlistapp.viewModels.MainViewModel
import kotlinx.android.synthetic.main.user_deatail_fragment.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(), FragmentCallback {

    @Inject lateinit var viewModel: MainViewModel
    @Inject lateinit var listFragment: ListFragment

    //Realisation on an interface FragmentCallback's method onUserPressed
    //called when user's card in list is touched
    override fun onUserPressed(login: String) {
        viewModel.loadUserProfile(login, listFragment.getRecyclerView()?.getLinearLayoutManager()?.findFirstVisibleItemPosition())
    }

    //OnScrollListener, used for pagination, when recycler view is scrolled calls specific fun
    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updateList(recyclerView)
                super.onScrolled(recyclerView, dx, dy)
            }
        }

    //updates current user list(pagination)
    fun updateList(recyclerView: RecyclerView){
        //if recycler view's linear layout manager is not null call view model update list function
        recyclerView.getLinearLayoutManager()?.let {
            viewModel.updateList(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as App).appComponent.inject(this)
        viewModel.initialize(fragmentCallback = this, savedInstanceState = savedInstanceState)
        manageState()
    }

    //Manages current state, show certain fragment depending on a current state
    private fun manageState() {
        viewModel.state.observe(this, {
            when(it){
                State.LoadingState -> showLoading()
                is State.ErrorState -> showError(it.message)
                is State.LoadedState -> showList(it.lastPosition)
                is State.UserOpenState -> showUserProfile(it.user)
                State.NoItemState -> showNoItems()
            }
        })
    }

    private fun showNoItems() {
        showError(getString(R.string.no_items_in_list))
    }

    //Displays User's detail fragment
    private fun showUserProfile(user: User) {
        replaceFragment(UserDetailFragment.newInstance(user = user))
        toolbar.setVisible(false)
    }

    //realization of a onBackPressed method to catch a back press when User detail Profile is opened
    override fun onBackPressed() {
        when(viewModel.state.value){
            is State.UserOpenState -> viewModel.closeUserProfile()
            else -> super.onBackPressed()
        }
    }

    //Saves current screen state
    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState(outState)
        super.onSaveInstanceState(outState)
    }

    //Displays user list fragment
    private fun showList(lastPosition: Int?) {
        replaceFragment(listFragment)
        //if recycler view's adapter is null then we call this fun for first time, so it has to de initialized
        if(listFragment.getRecyclerView()?.adapter == null) {
            initRecyclerView()
        }else {
            //if recycler view's adapter then list is updated for pagination,
            //check if list has to scrolled to certain position
            lastPosition?.let {
                listFragment.getRecyclerView()?.getLinearLayoutManager()?.scrollToPosition(it)
            }
        }
        //recycler view's adapter has no items them we show an error
        if((listFragment.getRecyclerView()?.adapter as UserListAdapter).isEmpty()){
            showNoItems()
        }
    }

    //Replacing of the current fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, fragment)
                .commitNow()
        toolbar.setVisible(true)
    }

    //Initializing of recyclerView
    private fun initRecyclerView() {
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.scrollToPosition(viewModel.position)
        listFragment.getRecyclerView()?.layoutManager = layoutManager
        listFragment.getRecyclerView()?.adapter = viewModel.getAdapter()
        listFragment.getRecyclerView()?.addOnScrollListener(recyclerViewOnScrollListener)
    }

    //onClick method of error screen's repeat_button
    fun repeatPressed(view: View){
        viewModel.loadUsers()
    }

    //Displays error fragment on screen
    private fun showError(message: String? = "") {
        replaceFragment(ErrorFragment.newInstance(message!!))
    }

    //Displays loading fragment on screen
    private fun showLoading() {
        replaceFragment(LoadingFragment.newInstance())
    }

}


