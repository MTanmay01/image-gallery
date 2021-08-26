package com.mtanmay.imagegallery.ui.gallery

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.mtanmay.imagegallery.R
import com.mtanmay.imagegallery.databinding.GalleryFragmentBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GalleryFragment : Fragment(R.layout.gallery_fragment) {

    private val viewModel by viewModels<ViewModel>()

    private var _binding: GalleryFragmentBinding? = null
    private val binding get() = _binding!!

    private var layoutManager = GridLayoutManager(context, 1)
    private val adapter = GalleryAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        _binding = GalleryFragmentBinding.bind(view)

        binding.apply {
            recyclerView.setHasFixedSize(true)
            recyclerView.layoutManager = layoutManager
            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadstateAdapter { adapter.retry() },
                footer = LoadstateAdapter { adapter.retry() }
            )

            swipeRefreshLayout.setOnRefreshListener {
                adapter.refresh()
                swipeRefreshLayout.isRefreshing = false
            }
        }

        if (viewModel.getPhotos() == null)
            showResults(false)
        else {
            showResults(true)
            viewModel.getPhotos()?.observe(viewLifecycleOwner) { results ->
                adapter.submitData(viewLifecycleOwner.lifecycle, results)
            }
        }

    }

    private fun showResults(show: Boolean) {
        if (show) {
            binding.noConnection.visibility = View.INVISIBLE
            binding.wifiOff.visibility = View.INVISIBLE
            binding.recyclerView.visibility = View.VISIBLE
        } else {
            binding.noConnection.visibility = View.VISIBLE
            binding.wifiOff.visibility = View.VISIBLE
            binding.recyclerView.visibility = View.INVISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_toolbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.change_layout_style) {
            if (layoutManager.spanCount == 1) {
                layoutManager.spanCount = 3
                item.title = "Grid View"
                item.icon = resources.getDrawable(R.drawable.ic_grid_view)
            } else {
                layoutManager.spanCount = 1
                item.title = "List View"
                item.icon = resources.getDrawable(R.drawable.ic_list_view)
            }
            adapter.notifyItemRangeChanged(0, adapter.itemCount)
        } else if (item.itemId == R.id.refresh) {
            adapter.refresh()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}