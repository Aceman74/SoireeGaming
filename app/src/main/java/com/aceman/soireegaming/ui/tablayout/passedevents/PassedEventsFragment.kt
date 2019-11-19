package com.aceman.soireegaming.ui.tablayout.passedevents


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aceman.soireegaming.R
import com.aceman.soireegaming.ui.bottomnavigation.messages.PassedEventsContract
import com.aceman.soireegaming.ui.bottomnavigation.messages.PassedEventsPresenter
import com.aceman.soireegaming.utils.base.BaseView

/**
 * A simple [Fragment] subclass.
 */
class PassedEventsFragment : Fragment(), BaseView, PassedEventsContract.PassedEventsViewInterface {
    private val mPresenter: PassedEventsPresenter = PassedEventsPresenter()

    companion object {
        fun newInstance(): PassedEventsFragment {
            return PassedEventsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_passed_events, container, false)
        mPresenter.attachView(this)
        return mView
    }


}
