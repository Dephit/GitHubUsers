package com.example.githubuserlistapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import javax.inject.Inject


class MainActivity : AppCompatActivity(){
    @Inject lateinit var viewModel: LoginViewModel
    @Inject lateinit var listFragment: ListFragment


    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updateList(recyclerView)
                super.onScrolled(recyclerView, dx, dy)
            }
        }

    //updates current user list(pagination)
    fun updateList(recyclerView: RecyclerView){
        recyclerView.getLinearLayoutManager()?.apply {
            val adapter = viewModel.getAdapter()
            if (childCount + findFirstVisibleItemPosition() >= adapter.itemCount / 2) {
                if (!viewModel.isListRefreshed){
                    viewModel.isListRefreshed = true
                    viewModel.loadUsers(showLoader = false, _since = adapter.getLastItemId())
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        (applicationContext as App).appComponent.inject(this)
        viewModel.initialize(savedInstanceState)
        manageState()
    }

    //Manages current state
    private fun manageState() {
        viewModel.state.observe(this, {
            when(it){
                State.LoadingState -> showLoading()
                is State.ErrorState -> showError(it.message)
                is State.LoadedState -> showList()
                else -> showError(getString(R.string.no_items_in_list))
            }
        })
    }

    //Saves current screen state
    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState(outState)
        listFragment.getRecyclerView()?.getLinearLayoutManager()?.findFirstVisibleItemPosition()?.let { outState.putInt("first_visible_position", it) }
        super.onSaveInstanceState(outState)
    }

    //Displays user list fragment
    private fun showList() {
        replaceFragment(listFragment)
        if(listFragment.getRecyclerView()?.adapter == null) {
            initRecyclerView()
        }
        if((listFragment.getRecyclerView()?.adapter as UserListAdapter).itemCount == 0){
            showError(getString(R.string.no_items_in_list))
        }
    }

    //Replacing of the current fragment
    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, fragment)
                .commitNow()
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

//Returns recyclerView's LinearLayoutManager if it's exists
fun RecyclerView.getLinearLayoutManager(): LinearLayoutManager? {
    return if(layoutManager is LinearLayoutManager) {
        layoutManager as LinearLayoutManager
    }else {
        null
    }
}