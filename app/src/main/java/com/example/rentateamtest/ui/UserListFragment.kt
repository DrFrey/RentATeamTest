package com.example.rentateamtest.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.rentateamtest.TestApplication
import com.example.rentateamtest.databinding.FragmentUserListBinding
import com.google.android.material.snackbar.Snackbar
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers

class UserListFragment : Fragment(), UserAdapter.OnItemClickListener {
    private lateinit var binding: FragmentUserListBinding
    private lateinit var adapter: UserAdapter
    private lateinit var subscription: CompositeDisposable

    private val viewModel: UserListViewModel by viewModels {
        UserListViewModelFactory(TestApplication.repository)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater)
        adapter = UserAdapter()
        binding.userListRecyclerview.adapter = adapter
        adapter.setOnItemClickListener(this)

        subscription = CompositeDisposable()
        subscription.add(
            viewModel.getUsers()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    adapter.submitList(it)
                }, {
                    Log.d("___", it?.message.toString())
                })
        )

        viewModel.isLoading.observe(viewLifecycleOwner, {
            if (it == true) {
                binding.progressCircular.visibility = View.VISIBLE
            } else {
                binding.progressCircular.visibility = View.GONE
            }
        })

        viewModel.error.observe(viewLifecycleOwner, {
            if (it.isNotEmpty()) {
                Snackbar.make(binding.root, it, Snackbar.LENGTH_SHORT).show()
            }
        })


        return binding.root
    }

    override fun onDestroy() {
        super.onDestroy()
        if (!subscription.isDisposed) {
            subscription.clear()
        }
    }

    override fun onItemClick(position: Int) {
        findNavController().navigate(
            UserListFragmentDirections.actionUserListFragmentToUserDetailsFragment(
                adapter.currentList[position]
            )
        )
    }

}