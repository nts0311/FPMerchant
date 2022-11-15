package com.sonnt.fpmerchant.ui.base

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment

abstract class BaseFragment<T: ViewDataBinding>: Fragment() {

    abstract var layoutResId: Int

    lateinit var binding: T

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate<T>(inflater, layoutResId, container, false).apply {
            lifecycleOwner = viewLifecycleOwner
        }
        return binding.root
    }

    protected fun setActionBarTitle(title: String) {
        (requireActivity() as AppCompatActivity).supportActionBar?.title = title
    }

    protected fun toast(content: String) {
        Toast.makeText(requireContext(), content, Toast.LENGTH_LONG).show()
    }
}