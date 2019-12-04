package com.aceman.soireegaming.ui.bottomnavigation.messages


import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.DateStamp
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.ui.adapters.contactlist.ContactListAdapter
import com.aceman.soireegaming.ui.bottomnavigation.messages.chat.ChatLogActivity
import com.aceman.soireegaming.utils.base.BaseView
import kotlinx.android.synthetic.main.fragment_messages.*
import timber.log.Timber

/**
 * A simple [Fragment] subclass.
 */
class MessagesFragment : Fragment(), BaseView, MessagesContract.MessagesViewInterface {
    private val mPresenter: MessagesPresenter = MessagesPresenter()
    lateinit var mRecyclerView: RecyclerView
    private var chanList = mutableListOf<String>()
    private var userList = mutableListOf<User>()
    private var dateList = mutableListOf<DateStamp>()

    companion object {
        fun newInstance(): MessagesFragment {
            return MessagesFragment()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as AppCompatActivity).setSupportActionBar(messages_tb)
        val mView = inflater.inflate(R.layout.fragment_messages, container, false)
        mPresenter.attachView(this)
        return mView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mPresenter.getEngagedChat()
    }

    override fun updateUI(
        list: MutableList<String>,
        chanList: MutableList<String>
    ) {
        this.chanList = chanList
        for (user in list) {
            mPresenter.addUserInfos(user)
        }
    }

    override fun updateUsers(user: User) {
        userList.add(user)
        configureRecyclerView()
        var i = 0
        for (item in chanList) {
            mPresenter.getLastMessageTime(item).addOnCompleteListener {
                var date = it.result!!.toObject(DateStamp::class.java)
                dateList.add(date!!)
                i++
                if (i == userList.size)
                    mRecyclerView.adapter!!.notifyDataSetChanged()
            }
        }

    }

    /**
     * Initialize the recyclerview for picture preview.
     */
    fun configureRecyclerView() {
        mRecyclerView = messages_rv
        mRecyclerView.addItemDecoration(
            DividerItemDecoration(
                requireContext(),
                DividerItemDecoration.VERTICAL
            )
        )
        mRecyclerView.layoutManager =
            LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false)
        mRecyclerView.adapter = ContactListAdapter(userList, dateList) {
            Timber.tag("Home Fragment RV click").i(it)
            val intent = Intent(requireContext(), ChatLogActivity::class.java)
            intent.putExtra("uid", it)
            startActivity(intent)

        }

    }
}
