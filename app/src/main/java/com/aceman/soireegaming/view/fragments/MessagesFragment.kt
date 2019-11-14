package com.aceman.soireegaming.view.fragments


import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

import com.aceman.soireegaming.R
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.fragment_messages.*

/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : Fragment() {
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
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }

}
