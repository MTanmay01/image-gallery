package com.mtanmay.imagegallery.ui.search

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.mtanmay.imagegallery.R
import com.mtanmay.imagegallery.ViewModel
import com.mtanmay.imagegallery.adapter.Adapter
import com.mtanmay.imagegallery.adapter.LoadstateAdapter
import com.mtanmay.imagegallery.api.Result
import com.mtanmay.imagegallery.databinding.SearchFragmentBinding
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.core.Observer
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import io.reactivex.rxjava3.schedulers.Schedulers
import java.util.concurrent.TimeUnit

@AndroidEntryPoint
class SearchFragment : Fragment(R.layout.search_fragment), Adapter.OnItemClickListener {

    private val viewModel by viewModels<ViewModel>()

    private var _binding: SearchFragmentBinding? = null
    private val binding get() = _binding!!

    private val disposable = CompositeDisposable()

    private lateinit var layoutManager: GridLayoutManager
    private val adapter = Adapter(this)
    private var snackbar: Snackbar? = null

    private var oldQuery: String? = ""

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        _binding = SearchFragmentBinding.bind(view)

        binding.apply {

            recyclerView.setHasFixedSize(true)

            layoutManager = GridLayoutManager(context, 1)
            recyclerView.layoutManager = layoutManager

            recyclerView.adapter = adapter.withLoadStateHeaderAndFooter(
                header = LoadstateAdapter { adapter.retry() },
                footer = LoadstateAdapter { adapter.retry() }
            )

            recyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    if (dy > 0 || dy < 0) {
                        val imm =
                            activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(recyclerView.windowToken, 0)
                    }
                }
            })

        }

        val observableQuery: Observable<String> = Observable.create {
            binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                override fun onQueryTextSubmit(query: String?): Boolean {
                    return false
                }

                override fun onQueryTextChange(newText: String?): Boolean {
                    if (!it.isDisposed && newText != oldQuery) {
                        oldQuery = newText
                        it.onNext(newText)
                    }
                    return false
                }
            })
        }

        observableQuery
            .debounce(500, TimeUnit.MILLISECONDS)
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : Observer<String> {

                override fun onSubscribe(d: Disposable?) {
                    disposable.add(d)
                }

                override fun onNext(query: String?) {

                    if (!TextUtils.isEmpty(query)) {

                        if (viewModel.getSearchPhotos(query!!) == null) {
                            snackbar = Snackbar.make(
                                view,
                                "No internet connection",
                                Snackbar.LENGTH_INDEFINITE
                            )
                                .setAction("RETRY") {
                                    this.onNext(query)
                                }
                            snackbar?.show()
                        } else {

                            if (snackbar != null && snackbar!!.isShown)
                                snackbar?.dismiss()

                            viewModel.getSearchPhotos(query)
                                ?.observe(viewLifecycleOwner) { results ->
                                    adapter.submitData(viewLifecycleOwner.lifecycle, results)
                                }
                        }
                    }
                }

                override fun onError(e: Throwable?) {}

                override fun onComplete() {}

            })
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

    override fun onItemClick(image: Result) {
        val action = SearchFragmentDirections.actionSearchToViewImage(image)
        findNavController().navigate(action)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }

}