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
import kotlinx.android.synthetic.main.fragment_passed_events.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class PassedEventsFragment : Fragment(), BaseView, PassedEventsContract.PassedEventsViewInterface {
    private val mPresenter: PassedEventsPresenter = PassedEventsPresenter()
    lateinit var mRecyclerView: RecyclerView
    private var  eventList: MutableList<EventInfos> = mutableListOf()
    var remoteSize = mutableListOf<String>()

    companion object {
        fun newInstance(): PassedEventsFragment {
            return PassedEventsFragment()
        }
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        val mView = inflater.inflate(R.layout.fragment_passed_events, container, false)
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
        mRecyclerView = passed_events_rv
        mRecyclerView.layoutManager = LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = PassedEventsAdapter(eventList) {
            Timber.tag("Passe Events RV click").i(it)
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
        var i = 0
        val todayDate = Utils.dateWithBSToMillis(Utils.todayDate)
        while (i<eventList.size){
            val item = eventList[i]
            val eventDate = Utils.dateWithBSToMillis(item.dateList[0])
            if(eventDate > todayDate-5000){
                eventList.removeAt(i)
            i=0
            }else i++
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
