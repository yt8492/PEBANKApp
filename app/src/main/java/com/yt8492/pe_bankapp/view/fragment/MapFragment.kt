package com.yt8492.pe_bankapp.view.fragment


import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.viewModels

import com.yt8492.pe_bankapp.R
import com.yt8492.pe_bankapp.databinding.FragmentMapBinding
import com.yt8492.pe_bankapp.viewmodel.MapViewModel
import com.yt8492.pe_bankapp.viewmodel.factory.MapViewModelFactory
import dagger.android.support.AndroidSupportInjection
import javax.inject.Inject

class MapFragment : Fragment() {

    private lateinit var binding: FragmentMapBinding

    @Inject
    internal lateinit var viewModelFactory: MapViewModelFactory

    private val viewModel: MapViewModel by viewModels { viewModelFactory }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_map, container, false)
        binding.lifecycleOwner = this
        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        AndroidSupportInjection.inject(this)
    }

    companion object {
        fun newInstance() = MapFragment()
    }
}
