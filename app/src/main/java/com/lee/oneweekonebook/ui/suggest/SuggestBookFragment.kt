package com.lee.oneweekonebook.ui.suggest

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import com.lee.oneweekonebook.databinding.FragmentSuggestBookBinding
import com.lee.oneweekonebook.ui.suggest.viewmodel.SuggestBookViewModel
import com.lee.oneweekonebook.ui.suggest.viewmodel.SuggestBookViewModelFactory

class SuggestBookFragment : Fragment() {

    var binding: FragmentSuggestBookBinding? = null
    private val args by navArgs<SuggestBookFragmentArgs>()

    override fun onDestroyView() {
        super.onDestroyView()
        binding = null
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val viewModelFactory = SuggestBookViewModelFactory(args.categoryId)
        val viewModel = ViewModelProvider(this, viewModelFactory).get(SuggestBookViewModel::class.java)

        binding = FragmentSuggestBookBinding.inflate(inflater, container, false)
            .apply {
                lifecycleOwner = this@SuggestBookFragment

                swipeRefreshLayoutContainer.apply {
                    setOnRefreshListener {
//                        viewModel.refreshBooks()
                        isRefreshing = false
                    }
                }

                val suggestBookAdapter = SuggestBookAdapter(SuggestBookListener { book ->
                    findNavController().navigate(SuggestBookFragmentDirections.actionSuggestBookFragmentToBookDetailFragment(book = book))
                })
                val gridLayoutManager = GridLayoutManager(requireContext(), 3)

                recyclerViewSuggestBook.apply {
                    layoutManager = gridLayoutManager
                    adapter = suggestBookAdapter
                }

                viewModel.books.observe(viewLifecycleOwner, {
                    suggestBookAdapter.data = it
                })
            }

        return binding?.root
    }

}