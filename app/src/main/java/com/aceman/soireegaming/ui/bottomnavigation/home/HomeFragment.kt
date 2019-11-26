package com.aceman.soireegaming.ui.bottomnavigation.home


import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.PagerAdapter
import com.aceman.soireegaming.ui.tablayout.passedevents.PassedEventsFragment
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.ui.adapters.mainlist.MainListAdapter
import com.aceman.soireegaming.ui.event.CreateEventActivity
import com.aceman.soireegaming.ui.profile.ProfileActivity
import com.aceman.soireegaming.ui.tablayout.allevents.AllEventsFragment
import com.aceman.soireegaming.ui.tablayout.comingevents.ComingEventsFragment
import com.aceman.soireegaming.ui.tablayout.followedevents.FollowedEventsFragment
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.fragment_home.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment(), BaseView, HomeContract.HomeViewInterface {
    private val mPresenter: HomePresenter = HomePresenter()
    lateinit var mRecyclerView: RecyclerView
     var  eventList: MutableList<EventInfos> = mutableListOf()

    companion object {
        fun newInstance(): HomeFragment {
            return HomeFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        setHasOptionsMenu(true)
       val mView = inflater.inflate(R.layout.fragment_home, container, false)
        mPresenter.attachView(this)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.getUserDataFromFirestore()
        configureRecyclerView()
        val adapter = PagerAdapter(childFragmentManager)
        adapter.addFragment(AllEventsFragment.newInstance(), "Tous")
        adapter.addFragment(ComingEventsFragment.newInstance(), "J'y vais")
        adapter.addFragment(FollowedEventsFragment.newInstance(), "Marquées")
        adapter.addFragment(PassedEventsFragment.newInstance(), "Passées")
        main_vp.adapter = adapter
        main_tab_ly.setupWithViewPager(main_vp)
        (activity as AppCompatActivity).setSupportActionBar(main_tb)
    }

    /**
     * Initialize the recyclerview for picture preview.
     */
     fun configureRecyclerView() {
        mRecyclerView = main_my_events_rv
        mRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView.adapter = MainListAdapter(eventList) {
            Timber.tag("RV click").i("$it")
            // launchDetailActivity(it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu_tb, menu)
        super.onCreateOptionsMenu(menu, inflater)
        create_event_tv.setOnClickListener { val intent = Intent(this.requireContext(), CreateEventActivity::class.java)
            startActivity(intent)}
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

    override fun updateUI(currentUser: User) {
        var i = 0
        while(i < currentUser.eventList.size && currentUser.eventList[0] != ""){
            mPresenter.addEventInfos(currentUser.eventList[i])
            i++
        }
    }

    override fun updateEvents(event: EventInfos) {
        eventList.add(event)
        mRecyclerView.adapter!!.notifyDataSetChanged()
    }

    override fun onResume() {
        super.onResume()
        mPresenter.getUserDataFromFirestore()
    }
}
