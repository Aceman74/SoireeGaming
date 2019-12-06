package com.aceman.soireegaming.ui.tablayout.passedevents


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.ui.adapters.passedevents.PassedEventsAdapter
import com.aceman.soireegaming.ui.event.detail.EventDetailActivity
import com.aceman.soireegaming.ui.profile.ProfileActivity
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.empty_list_tab.*
import kotlinx.android.synthetic.main.fragment_passed_events.*
import timber.log.Timber

/**
 * Created by Lionel JOFFRAY - on 19/11/2019.
 *
 * Shows all passed in a recyclerview.
 */
class PassedEventsFragment : Fragment(), BaseView, PassedEventsContract.PassedEventsViewInterface {
    private val mPresenter: PassedEventsPresenter = PassedEventsPresenter()
    lateinit var mRecyclerView: RecyclerView
    private var eventList: MutableList<EventInfos> = mutableListOf()
    var remoteSize = mutableListOf<String>()

    companion object {
        fun newInstance(): PassedEventsFragment {
            return PassedEventsFragment()
        }
    }

    /**
     * Attach presenter.
     */
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val mView = inflater.inflate(R.layout.fragment_passed_events, container, false)
        mPresenter.attachView(this)
        return mView
    }

    /**
     * Clear eventList to prevent duplicate data, get all events.
     */
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        passed_event_loading.visibility = View.VISIBLE
        eventList.clear()
        mPresenter.getAllEvents()
    }

    /**
     * Initialize the recyclerview for picture preview.
     */
    override fun configureRecyclerView() {
        passed_event_loading.visibility = View.GONE
        mRecyclerView = passed_events_rv
        mRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = PassedEventsAdapter(eventList) {
            Timber.tag("Passe Events RV click").i(it)
            if (it.length < 9)
                launchEventDetailActivity(it)
            else
                launchProfileDetailActivity(it)
        }
    }

    /**
     * Launch profile on click user picture.
     */
    override fun launchProfileDetailActivity(uid: String) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra("uid", uid)
        startActivity(intent)
    }

    /**
     * Launch eventdetail on click event.
     */
    override fun launchEventDetailActivity(eid: String) {
        val intent = Intent(requireContext(), EventDetailActivity::class.java)
        intent.putExtra("eid", eid)
        startActivity(intent)
    }

    /**
     * Update UI with event list.
     */
    override fun updateUI(eventsList: MutableList<String>) {
        configureRecyclerView()
        remoteSize = eventsList
        for (event in eventsList) {
            mPresenter.addEventInfos(event)
        }
    }

    /**
     * Update event with informations.
     */
    override fun updateEvents(event: EventInfos) {
        eventList.add(event)
        if (eventList.size == remoteSize.size)
            filterList()
        if(eventList.isEmpty()){
            passed_event_empty_list.visibility = View.VISIBLE
            empty_list_tab_tv.text = getString(R.string.no_one_create_event)
        }
        else
            passed_event_empty_list.visibility = View.GONE
    }

    /**
     * Filter the list to show only passed events.
     */
    override fun filterList() {
        var i = 0
        val todayDate = Utils.dateWithBSToMillis(Utils.todayDate)
        while (i < eventList.size) {
            val item = eventList[i]
            val eventDate = Utils.dateWithBSToMillis(item.dateList[0])
            if (eventDate > todayDate - 5000) {
                eventList.removeAt(i)
                i = 0
            } else i++
        }
        configureRecyclerView()
        sortByDate()
    }

    /**
     * Sort by date.
     */
    override fun sortByDate() {
        eventList.sortWith(Comparator { o1, o2 -> o1.dateList[0].compareTo(o2.dateList[0]) })
        eventList.reverse()
        mRecyclerView.adapter!!.notifyDataSetChanged()
    }

}
