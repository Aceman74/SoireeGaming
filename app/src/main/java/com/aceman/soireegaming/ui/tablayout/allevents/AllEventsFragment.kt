package com.aceman.soireegaming.ui.tablayout.allevents

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.EventInfos
import com.aceman.soireegaming.ui.adapters.allevents.AllEventsAdapter
import com.aceman.soireegaming.utils.base.BaseView
import com.google.firebase.firestore.QuerySnapshot
import kotlinx.android.synthetic.main.fragment_all_events.*
import timber.log.Timber

class AllEventsFragment : Fragment(), BaseView, AllEventsContract.AllEventsViewInterface {
    private val mPresenter: AllEventsPresenter = AllEventsPresenter()
    lateinit var mRecyclerView: RecyclerView
    var  eventList: MutableList<EventInfos> = mutableListOf()

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
        mPresenter.getAllEvents()
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        configureRecyclerView()
    }

    /**
     * Initialize the recyclerview for picture preview.
     */
    fun configureRecyclerView() {
        mRecyclerView = all_events_rv
        mRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
        mRecyclerView.adapter = AllEventsAdapter(eventList) {
            Timber.tag("RV click").i("$it")
            // launchDetailActivity(it)
        }
    }


    override fun updateUI(eventsList: MutableList<QuerySnapshot>) {
        var i = 0
        var event = eventsList[i].toObjects<EventInfos>(EventInfos::class.java)
        while(i < event.size){
            mPresenter.addEventInfos(event[i].eid)
            i++
        }
    }

    override fun updateEvents(event: EventInfos) {
        eventList.add(event)
        mRecyclerView.adapter!!.notifyDataSetChanged()
    }
}
