package com.example.githubuserlistapp

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.activity_main.*
import javax.inject.Inject


class MainActivity : AppCompatActivity(){
    @Inject lateinit var viewModel: LoginViewModel

    private val recyclerViewOnScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                updateList(recyclerView)
                super.onScrolled(recyclerView, dx, dy)
            }
        }

    fun updateList(recyclerView: RecyclerView){
        recyclerView.getLinearLayoutManager()?.apply {
            val adapter = viewModel.getAdapter()
            if (childCount + findFirstVisibleItemPosition() >= adapter.itemCount ) {
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

    private fun manageState() {
        viewModel.state.observe(this, {
            when(it){
                State.LoadingState -> showSplash()
                is State.ErrorState -> showError(it.message)
                is State.LoadedState -> {showList(it.userList)}
                else -> showError(getString(R.string.no_items_in_list))
            }
        })
    }

    override fun onSaveInstanceState(outState: Bundle) {
        viewModel.saveState(outState)
        recycler_view.getLinearLayoutManager()?.findFirstVisibleItemPosition()?.let { outState.putInt("first_visible_position", it) }
        super.onSaveInstanceState(outState)
    }

    private fun showList(userList: List<UserInList>) {
        root.changeVisible(false)
        if(recycler_view.adapter == null) {
            initRecyclerView(userList)
        }
        if((recycler_view.adapter as UserListAdapter).itemCount == 0){
            showError(getString(R.string.no_items_in_list))
        }
    }

    private fun initRecyclerView(userList: List<UserInList>) {
        val layoutManager = LinearLayoutManager(applicationContext)
        layoutManager.scrollToPosition(viewModel.position)
        recycler_view.layoutManager = layoutManager
        recycler_view.adapter = viewModel.getAdapter()
        recycler_view.addOnScrollListener(recyclerViewOnScrollListener)
    }

    fun repeatPressed(view: View){
        viewModel.loadUsers()
    }

    private fun showError(message: String? = "") {
        root.changeVisible(true)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, ErrorFragment.newInstance(message!!))
                .commitNow()
    }

    private fun showSplash() {
        root.changeVisible(true)
        supportFragmentManager
                .beginTransaction()
                .replace(R.id.root, SplashFragment.newInstance())
                .commitNow()
    }

}

private fun View.changeVisible(b: Boolean) {
    visibility = if(b) View.VISIBLE else View.GONE
}

fun RecyclerView.getLinearLayoutManager(): LinearLayoutManager? {
    return if(layoutManager is LinearLayoutManager) {
        layoutManager as LinearLayoutManager
    }else {
        null
    }
}