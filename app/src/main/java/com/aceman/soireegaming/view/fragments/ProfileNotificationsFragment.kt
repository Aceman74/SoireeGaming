package com.aceman.soireegaming.view.fragments


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup

import com.aceman.soireegaming.R

/**
 * A simple [Fragment] subclass.
 */
class ProfileNotificationsFragment : Fragment() {
    companion object {
        fun newInstance(): ProfileNotificationsFragment {
            return ProfileNotificationsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile_notifications, container, false)
    }


}
