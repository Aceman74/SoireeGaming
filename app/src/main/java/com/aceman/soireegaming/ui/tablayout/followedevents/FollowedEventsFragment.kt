package com.aceman.soireegaming.ui.tablayout.followedevents


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.aceman.soireegaming.R
import com.aceman.soireegaming.utils.base.BaseView

/**
 * A simple [Fragment] subclass.
 */
class FollowedEventsFragment : Fragment(), BaseView, FollowedEventsContract.FollowedEventsViewInterface {
    private val mPresenter: FollowedEventsPresenter = FollowedEventsPresenter()

    companion object {
        fun newInstance(): FollowedEventsFragment {
            return FollowedEventsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_followed_events, container, false)
        mPresenter.attachView(this)
        return mView
    }


}
