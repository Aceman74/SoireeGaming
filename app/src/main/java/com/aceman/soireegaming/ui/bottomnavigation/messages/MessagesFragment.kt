package com.aceman.soireegaming.ui.bottomnavigation.messages


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.aceman.soireegaming.R
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.fragment_messages.*

/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : Fragment(), BaseView, MessagesContract.MessagesViewInterface {
    private val mPresenter: MessagesPresenter = MessagesPresenter()
    companion object {
        fun newInstance(): MessagesFragment {
            return MessagesFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).setSupportActionBar(messages_tb)
        val mView = inflater.inflate(R.layout.fragment_messages, container, false)
        mPresenter.attachView(this)
        return mView
    }

}
