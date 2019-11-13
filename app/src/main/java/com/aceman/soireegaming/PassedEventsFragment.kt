package com.aceman.soireegaming


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

/**
 * A simple [Fragment] subclass.
 */
class PassedEventsFragment : Fragment() {

    companion object {
        fun newInstance(): PassedEventsFragment {
            return PassedEventsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_passed_events, container, false)
    }


}
