package com.aceman.soireegaming.ui.tablayout.comingevents


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
import com.aceman.soireegaming.ui.adapters.comingevents.ComingEventsAdapter
import com.aceman.soireegaming.ui.event.detail.EventDetailActivity
import com.aceman.soireegaming.ui.profile.ProfileActivity
import com.aceman.soireegaming.utils.Utils
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.fragment_coming_events.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class ComingEventsFragment : Fragment(), BaseView, ComingEventsContract.ComingEventsViewInterface {
    private val mPresenter: ComingEventsPresenter = ComingEventsPresenter()
    lateinit var mRecyclerView: RecyclerView
    private var  eventList: MutableList<EventInfos> = mutableListOf()
    var remoteSize = mutableListOf<String>()

    companion object {

        fun newInstance(): ComingEventsFragment {
            return ComingEventsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_coming_events, container, false)
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
        mRecyclerView = coming_events_rv
        mRecyclerView.addItemDecoration(DividerItemDecoration(requireContext(), DividerItemDecoration.HORIZONTAL))
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = ComingEventsAdapter(eventList) {
            Timber.tag("Coming Events RV click").i(it)
            if(it.length < 9)
                launchEventDetailActivity(it)
            else
                launchProfileDetailActivity(it)
        }
    }

    private fun launchProfileDetailActivity(uid: String) {
        val intent = Intent(requireContext(), ProfileActivity::class.java)
        intent.putExtra("uid",uid)
        startActivity(intent)
    }

    private fun launchEventDetailActivity(eid: String) {
        val intent = Intent(requireContext(), EventDetailActivity::class.java)
        intent.putExtra("eid",eid)
        startActivity(intent)
    }

    override fun updateAllEvents(eventsId: MutableList<String>) {
        for(item in eventsId)
            mPresenter.getUserEvent(item)
    }

    override fun updateUI(eid: String) {
        mPresenter.getEventInfos(eid)
    }

    override fun updateEvents(event: EventInfos) {
        eventList.add(event)
            filterList()
    }

    private fun filterList() {
        val fakeList = eventList
        var i = 0
        val todayDate = Utils.dateWithBSToMillis(Utils.todayDate)
        while (i<fakeList.size){
           val item = eventList[i]
            val eventDate = Utils.dateWithBSToMillis(item.dateList[0])
            if(eventDate < todayDate)
                eventList.removeAt(i)
            i++
        }
        configureRecyclerView()
        sortByDate()
    }

    fun sortByDate() {
        eventList.sortWith(Comparator { o1, o2 -> o1.dateList[0].compareTo(o2.dateList[0]) })
        eventList.reverse()
        mRecyclerView.adapter!!.notifyDataSetChanged()
    }

}
