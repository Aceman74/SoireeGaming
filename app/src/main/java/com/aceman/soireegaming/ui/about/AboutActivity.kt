package com.aceman.soireegaming.ui.about

import android.os.Bundle
import com.aceman.soireegaming.R
import com.aceman.soireegaming.ui.bottomnavigation.home.HomePresenter
import com.aceman.soireegaming.ui.login.AboutContract
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView

class AboutActivity(override val activityLayout: Int = R.layout.activity_about) : BaseActivity(), BaseView, AboutContract.AboutViewInterface {
    private val mPresenter: HomePresenter = HomePresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)

    }
}
