package com.mrtckr.foodorder.ui.common

import android.app.AlertDialog
import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.lifecycle.observe
import com.mrtckr.foodorder.R
import com.mrtckr.foodorder.domain.entity.ResultData

abstract class BaseFragment<T : BaseViewModel, B : ViewDataBinding> : Fragment() {
    abstract val layoutRes: Int
    abstract val viewModel: T

    open fun initBinding() {}
    abstract fun observeViewModel()
    abstract fun viewCreated(view: View, savedInstanceState: Bundle?)


    private var _binding: B? = null
    val binding get() = _binding!!

    companion object {
        private const val TAG = "BaseFragment"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        this._binding = DataBindingUtil.inflate(inflater, layoutRes, container, false)
        initBinding()
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewCreated(view, savedInstanceState)
        observeLoadingAndError()
        observeViewModel()
    }

    private fun observeLoadingAndError() {
        viewModel.loadingErrorState.observe(viewLifecycleOwner) { it ->
            when (it) {
                is ResultData.Loading -> {
                    showLoading()
                }
                is ResultData.Success -> {
                    hideLoading()
                }
                is ResultData.Failed -> {
                    hideLoading()
                    //showErrorDialog(it.error)
                }
            }
        }
    }

    private val loadingAlertDialog by lazy {
        context?.let {
            Dialog(it).apply {
                requestWindowFeature(Window.FEATURE_NO_TITLE)
                window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
                setContentView(R.layout.dialog_loading)
                setCancelable(false)
            }
        }
    }

    private fun showLoading() {
        Log.i(TAG, "showLoading: ")
        loadingAlertDialog?.show()
    }

    private fun hideLoading() {
        Log.i(TAG, "hideLoading: ")
        loadingAlertDialog?.dismiss()
    }

    private fun showErrorDialog(message: String?, callback: () -> Unit = {}) {
        context?.let {
            AlertDialog.Builder(it).apply {
                setTitle("Warning")
                setMessage(message)
                setPositiveButton("Close") { _, _ -> callback.invoke() }
            }.show()
        }
    }
}