package com.vylo.auth.activity

import android.os.Bundle
import com.vylo.auth.databinding.ActivityAuthBinding
import com.vylo.common.BaseActivity


class AuthActivity : BaseActivity<ActivityAuthBinding>() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun getViewBinding() = ActivityAuthBinding.inflate(layoutInflater)

    override fun showProgress() {
        viewBinder.progressBar.show()
        freesScreen()
    }

    override fun hideProgress() {
        viewBinder.progressBar.hide()
        unFreesScreen()
    }

    override fun onBackPressed() {
        super.onBackPressed()
        hideProgress()
    }

}