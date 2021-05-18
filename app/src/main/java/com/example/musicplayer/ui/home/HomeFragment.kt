package com.example.musicplayer.ui.home

import android.content.Context
import org.koin.androidx.viewmodel.ext.android.viewModel
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.musicplayer.R
import com.example.musicplayer.databinding.HomeFragmentBinding
import com.example.musicplayer.ui.BindingFragment
import com.example.musicplayer.utils.observe
import kotlinx.android.synthetic.main.home_fragment.*

class HomeFragment : BindingFragment<HomeFragmentBinding>() {

    override val layoutId = R.layout.home_fragment

    private val viewModel: HomeViewModel by viewModel()

    private lateinit var homeAdapter: HomeAdapter

    override fun onViewCreated() {
        super.onViewCreated()

        binding.viewModel = viewModel
        binding.lifecycleOwner = this.viewLifecycleOwner

        setupObservers()
        setupRV()
        initVM()
    }

    override fun onPause() {
        super.onPause()
        (context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager)
                .hideSoftInputFromWindow(activity?.currentFocus?.windowToken ?: return, 0)
    }

    private fun initVM() {
        viewModel.refresh(true)
    }

    private fun setupObservers() {
        observeError(viewModel.error)

        viewModel.isDataLoadingError.observe(this) {
            it?.let {
                homeSwipeLayout.isEnabled = it
                homeSwipeLayout.isRefreshing = it
            }
        }
    }

    private fun setupRV() {
        homeAdapter = HomeAdapter()

        with(homeFragRv) {
            val layoutManagerRv = GridLayoutManager(context,2)
            layoutManager = layoutManagerRv
            adapter = homeAdapter
            (this.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val totalItemCount = layoutManagerRv.itemCount
                    val lastVisibleItem = layoutManagerRv.findLastVisibleItemPosition()

                    if (!homeSwipeLayout.isRefreshing)
                        viewModel.listScrolled(lastVisibleItem, totalItemCount)
                }
            })
        }
    }
}