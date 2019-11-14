package com.aceman.soireegaming.view.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.aceman.soireegaming.R

class AllEventsFragment : Fragment() {

    companion object {
        fun newInstance(): AllEventsFragment {
            return AllEventsFragment()
        }
    }
    /**
     * Create the view.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_all_events, container, false)
    }
}
