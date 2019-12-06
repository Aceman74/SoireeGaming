package com.aceman.soireegaming.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.PagerAdapter
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.ui.adapters.mainlist.MainListAdapter
import com.aceman.soireegaming.ui.event.create.CreateEventActivity
import com.aceman.soireegaming.ui.event.detail.EventDetailActivity
import com.aceman.soireegaming.ui.profile.ProfileActivity
import com.aceman.soireegaming.ui.tablayout.allevents.AllEventsFragment
import com.aceman.soireegaming.ui.tablayout.comingevents.ComingEventsFragment
import com.aceman.soireegaming.ui.tablayout.passedevents.PassedEventsFragment
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.empty_list.*
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * Home fragment is showing the list of owner event, and a tab layout with the rest of event.
 */
class HomeFragment : Fragment(), BaseView, HomeContract.HomeViewInterface {
    private val mPresenter: HomePresenter = HomePresenter()
    lateinit var mRecyclerView: RecyclerView
    private var eventList: MutableList<EventInfos> = mutableListOf()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    /**
     * Attach Presenter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
        val mView = inflater.inflate(R.layout.fragment_home, container, false)
        mPresenter.attachView(this)
        return mView
    }

    /**
     * Once view is created, get user data, configure recyclerview with event received and set
     * the Tab Layout with 3 categories: All, Present and passed.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        home_event_loading.visibility = View.VISIBLE
        mPresenter.getUserDataFromFirestore()
        configureRecyclerView()
        val adapter = PagerAdapter(childFragmentManager)
        adapter.addFragment(AllEventsFragment.newInstance(), "Tous")
        adapter.addFragment(ComingEventsFragment.newInstance(), "J'y vais")
        // adapter.addFragment(FollowedEventsFragment.newInstance(), "Marquées") //Future implementation
        adapter.addFragment(PassedEventsFragment.newInstance(), "Passées")
        main_vp.adapter = adapter
        main_tab_ly.setupWithViewPager(main_vp)
        (activity as AppCompatActivity).setSupportActionBar(main_tb)
    }

    /**
     * Initialize the recyclerview of User Events.
     */
    fun configureRecyclerView() {
        mRecyclerView = main_my_events_rv
        mRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView.adapter = MainListAdapter(eventList) {
            Timber.tag("Home Fragment RV click").i(it)
            if (it.length < 9)
                launchEventDetailActivity(it)
            else
                launchProfileDetailActivity(it)
        }
    }

    /**
     * Launch profile on click picture.
     */
    private fun launchProfileDetailActivity(uid: String) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra("uid", uid)
        startActivity(intent)
    }

    /**
     * Launch event on click item.
     */
    private fun launchEventDetailActivity(eid: String) {
        val intent = Intent(requireContext(), EventDetailActivity::class.java)
        intent.putExtra("eid", eid)
        startActivity(intent)
    }

    /**
     * Set the click on profile toolbar.
     */
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu_tb, menu)
        super.onCreateOptionsMenu(menu, inflater)
        create_event_tv.setOnClickListener {
            val intent = Intent(this.requireContext(), CreateEventActivity::class.java)
            startActivity(intent)
        }
    }

    /**
     * Configure the toolbar click on the profile picture.
     */
    override fun onOptionsItemSelected(item: MenuItem) = when (item.itemId) {
        R.id.main_tb_profile -> {
            val intent = Intent(requireContext(), ProfileActivity::class.java)
            startActivity(intent)
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    /**
     * Update the UI with events infos.
     */
    override fun updateUI(eventId: String) {
        mPresenter.addEventInfos(eventId)
    }

    /**
     * Update the UI with events infos.
     */
    override fun updateEvents(event: EventInfos) {
        home_event_loading.visibility = View.GONE
        eventList.add(event)
        mRecyclerView.adapter!!.notifyDataSetChanged()
        emptyEvent()
    }

    override fun emptyEvent() {
        home_event_loading.visibility = View.GONE
        if(eventList.isEmpty()){
            home_event_empty_list.visibility = View.VISIBLE
            empty_list_tv.text = "Tu n'as pas encore crée d'event! \n Tes events apparaitrons ici."
        }
        else
            home_event_empty_list.visibility = View.GONE
    }
}
