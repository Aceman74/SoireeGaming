package com.aceman.soireegaming.ui.bottomnavigation.explore


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.aceman.soireegaming.R
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.fragment_explore.*


/**
 * A simple [Fragment] subclass.
 */
class ExploreFragment : Fragment(), BaseView, ExploreContract.ExploreViewInterface {
    private val mPresenter: ExplorePresenter = ExplorePresenter()

    companion object {
        fun newInstance(): ExploreFragment {
            return ExploreFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val mView = inflater.inflate(R.layout.fragment_explore, container, false)
        mPresenter.attachView(this)
        return mView
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        (activity as AppCompatActivity).setSupportActionBar(explore_tb)
    }


}
