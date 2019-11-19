package com.aceman.soireegaming.ui.tablayout.comingevents


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aceman.soireegaming.R
import com.aceman.soireegaming.ui.bottomnavigation.messages.ComingEventsContract
import com.aceman.soireegaming.ui.bottomnavigation.messages.ComingEventsPresenter
import com.aceman.soireegaming.utils.base.BaseView

/**
 * A simple [Fragment] subclass.
 */
class ComingEventsFragment : Fragment(), BaseView, ComingEventsContract.ComingEventsViewInterface {
    private val mPresenter: ComingEventsPresenter = ComingEventsPresenter()

    companion object {

        fun newInstance(): ComingEventsFragment {
            return ComingEventsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_coming_events, container, false)
        mPresenter.attachView(this)
        return mView
    }


}
