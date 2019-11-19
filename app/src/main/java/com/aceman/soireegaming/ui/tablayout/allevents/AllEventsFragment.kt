package com.aceman.soireegaming.ui.tablayout.allevents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aceman.soireegaming.R
import com.aceman.soireegaming.ui.bottomnavigation.messages.AllEventsContract
import com.aceman.soireegaming.ui.bottomnavigation.messages.AllEventsPresenter
import com.aceman.soireegaming.utils.base.BaseView

class AllEventsFragment : Fragment(), BaseView, AllEventsContract.AllEventsViewInterface {
    private val mPresenter: AllEventsPresenter = AllEventsPresenter()

    companion object {
        fun newInstance(): AllEventsFragment {
            return AllEventsFragment()
        }
    }
    /**
     * Create the view.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_all_events, container, false)
        mPresenter.attachView(this)
        return mView
    }
}
