package com.aceman.soireegaming.ui.bottomnavigation.messages.chat

import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aceman.soireegaming.R
import com.aceman.soireegaming.data.models.MessageType
import com.aceman.soireegaming.data.models.TextMessage
import com.aceman.soireegaming.data.models.User
import com.aceman.soireegaming.utils.base.BaseActivity
import com.aceman.soireegaming.utils.base.BaseView
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ListenerRegistration
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Section
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import java.util.*


class ChatLogActivity(override val activityLayout: Int = R.layout.activity_chat_log) : BaseActivity(), BaseView, ChatLogContract.ChatLogViewInterface {
    private val mPresenter: ChatLogPresenter = ChatLogPresenter()
    private lateinit var messagesListenerRegistration: ListenerRegistration
    private lateinit var messagesSection: Section
    private var shouldInitRecyclerView = true
    private var mOtherUser: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mPresenter.attachView(this)
        checkIntent()
    }

    private fun checkIntent() {
        mOtherUser = intent.getStringExtra("uid")!!
        mPresenter.getUserDataFromFirestore(mOtherUser)
    }

    override fun updateUI(otherUser: User) {
        var mUser = FirebaseAuth.getInstance().currentUser!!
        Glide.with(this)
            .load(otherUser.urlPicture)
            .circleCrop()
            .into(chat_tb_img)
        chat_log_tb.title = otherUser.name

        mPresenter.getChannel(mOtherUser) { channelId ->
            messagesListenerRegistration =
                mPresenter.addChatMessagesListener(channelId, this, this::updateRecyclerView)
            chat_send_btn.setOnClickListener {
                val messageToSend =
                    TextMessage(
                        chat_log_et.text.toString(), Calendar.getInstance().time, mUser.displayName!!,
                        mUser.uid, otherUser.uid, MessageType.TEXT
                    )
                chat_log_et.setText("")
                mPresenter.sendMessage(messageToSend, channelId)
            }
        }
    }
    private fun updateRecyclerView(messages: List<Item>) {
        fun init() {
            chat_log_rv.apply {
                layoutManager = LinearLayoutManager(this@ChatLogActivity)
                adapter = GroupAdapter<ViewHolder>().apply {
                    messagesSection = Section(messages)
                    this.add(messagesSection)
                }
            }
            shouldInitRecyclerView = false
        }
        fun updateItems() = messagesSection.update(messages)

        if (shouldInitRecyclerView)
            init()
        else
            updateItems()

        chat_log_rv.scrollToPosition(chat_log_rv.adapter!!.itemCount - 1)
    }
}
