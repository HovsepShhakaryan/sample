package com.vylo.main.createfragment.presentation.fragment

import android.os.Bundle
import android.view.*
import com.vylo.common.BaseFragment
import com.vylo.main.databinding.FragmentCreateBinding

class CreateFragment : BaseFragment<FragmentCreateBinding>() {

    override fun getViewBinding() = FragmentCreateBinding.inflate(layoutInflater)

    companion object {

        @JvmStatic
        fun newInstance(): CreateFragment {
            return CreateFragment()
        }

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        viewBinder = getViewBinding()
        return viewBinder.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        beginning()
    }

    private fun beginning() {
    }

}