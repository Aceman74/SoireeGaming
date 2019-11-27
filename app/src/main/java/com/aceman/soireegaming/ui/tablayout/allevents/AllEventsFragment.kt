package com.aceman.soireegaming.ui.tablayout.allevents

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.ui.adapters.allevents.AllEventsAdapter
import com.aceman.soireegaming.ui.event.EventDetailActivity
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.fragment_all_events.*
import timber.log.Timber
import java.util.*

class AllEventsFragment : Fragment(), BaseView, AllEventsContract.AllEventsViewInterface {
    private val mPresenter: AllEventsPresenter = AllEventsPresenter()
    lateinit var mRecyclerView: RecyclerView
   private var  eventList: MutableList<EventInfos> = mutableListOf()
    var remoteSize = mutableListOf<String>()

    companion object {
        fun newInstance(): AllEventsFragment {
            return AllEventsFragment()
        }
    }
    /**
     * Create the view.
     */
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_all_events, container, false)
        mPresenter.attachView(this)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        eventList.clear()
        mPresenter.getAllEvents()
    }

    /**
     * Initialize the recyclerview for picture preview.
     */
    fun configureRecyclerView() {
        mRecyclerView = all_events_rv
        mRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = AllEventsAdapter(eventList) {
            Timber.tag("All Events RV click").i("$it")
             launchDetailActivity(it)
        }
    }

    private fun launchDetailActivity(eid: Int) {
        val intent = Intent(requireContext(), EventDetailActivity::class.java)
        intent.putExtra("eid",eid)
        startActivity(intent)
    }


    override fun updateUI(eventsList: MutableList<String>) {
        configureRecyclerView()
        remoteSize = eventsList
        for(event in eventsList){
            mPresenter.addEventInfos(event)
        }
    }

    override fun updateEvents(event: EventInfos) {
        eventList.add(event)
        if(eventList.size == remoteSize.size)
            filterList()
    }

    private fun filterList() {

        configureRecyclerView()
        sortByDate()
    }
    fun sortByDate() {
        eventList.sortWith(Comparator { o1, o2 -> o1.dateList[0].compareTo(o2.dateList[0]) })
        eventList.reverse()
        mRecyclerView.adapter!!.notifyDataSetChanged()
    }
}
