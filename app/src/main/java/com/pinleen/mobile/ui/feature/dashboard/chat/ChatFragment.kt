package com.pinleen.mobile.ui.feature.dashboard.chat

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.pinleen.mobile.databinding.FragmentChatBinding
import com.pinleen.mobile.ui.base.BaseFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.internal.notify
import org.jivesoftware.smack.AbstractXMPPConnection
import org.jivesoftware.smack.ConnectionConfiguration
import org.jivesoftware.smack.android.AndroidSmackInitializer
import org.jivesoftware.smack.chat2.Chat
import org.jivesoftware.smack.chat2.ChatManager
import org.jivesoftware.smack.chat2.IncomingChatMessageListener
import org.jivesoftware.smack.packet.Message
import org.jivesoftware.smack.packet.Presence
import org.jivesoftware.smack.packet.Presence.Mode
import org.jivesoftware.smack.packet.Stanza
import org.jivesoftware.smack.tcp.XMPPTCPConnection
import org.jivesoftware.smack.tcp.XMPPTCPConnectionConfiguration
import org.jxmpp.jid.EntityBareJid
import org.jxmpp.jid.impl.JidCreate
import java.io.File
import java.security.SecureRandom
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager
import org.jivesoftware.smack.roster.Roster
import org.jxmpp.jid.BareJid
import org.jivesoftware.smack.roster.RosterEntry
import org.jivesoftware.smack.SmackException
import org.jivesoftware.smack.SmackException.NotConnectedException
import org.jivesoftware.smack.SmackException.NotLoggedInException
import org.jivesoftware.smack.roster.RosterListener
import org.jxmpp.jid.Jid


class ChatFragment : BaseFragment<FragmentChatBinding>() {
    override val bindingInflater: (LayoutInflater, ViewGroup?, Boolean) -> FragmentChatBinding
        get() = FragmentChatBinding::inflate

    private val TAG = "SMACK"
    private val defaultIP = "pinleele.com"
    private val defaultPort = 5222
    var conf: XMPPTCPConnectionConfiguration? = null
    var con: AbstractXMPPConnection? = null
    var chat: Chat? = null
    lateinit var chatManager: ChatManager
    private var currentUserJid = ""
    private var currentUserPassword = ""
    private var opponentUserJid = ""
    private var configEx: java.lang.Exception? = null
    private lateinit var chatAdapter: ChatAdapter

    //This variable will keep track if during the construction of a configuration object...
    // some error occurred or not so that the conf can be used safely.  private Exception configEx;
    private var conEx: java.lang.Exception? = null
    private var listMessage = ArrayList<ChatModel>()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initListener()
        chatAdapter = ChatAdapter(mActivity, listMessage)
        binding.rvChat.layoutManager = LinearLayoutManager(mActivity)
        binding.rvChat.adapter = chatAdapter
    }

    private fun initListener() {
        binding.btnUser1.setOnClickListener {
            binding.llUsers.visibility = View.GONE
            currentUserJid = "7071cfd0-981c-4a2c-b032-de5328657b36"
            currentUserPassword = "AYetjAaY9p3zKl41990"
            opponentUserJid = "bdfa9f69-e2a5-4e8c-bd4d-f1d7fd85c310"
            initXmppConnection()
        }
        binding.btnUser2.setOnClickListener {
            binding.llUsers.visibility = View.GONE
            opponentUserJid = "7071cfd0-981c-4a2c-b032-de5328657b36"
            currentUserJid = "bdfa9f69-e2a5-4e8c-bd4d-f1d7fd85c310"
            currentUserPassword = "aiws5w5NWrx9wf29350"
            initXmppConnection()
        }
        binding.btnSend.setOnClickListener {
            try {
                chat?.send(binding.etMessage.text.toString())
                chatAdapter.addData(ChatModel(binding.etMessage.text.toString(), 0))
                chatAdapter.notifyItemInserted(chatAdapter.listMessage.size-1)
                binding.rvChat.smoothScrollToPosition(chatAdapter.listMessage.size-1)
                binding.etMessage.text?.clear()
            } catch (e: Exception) {
                Log.i("the error", e.message!!)
            }
        }
    }

    private fun initXmppConnection() {
        AndroidSmackInitializer.initialize(mActivity)
        var connectStat = false

        lifecycleScope.launch {
            initXMPP()

            connectStat = connectAndLogin(
                currentUserJid,
                currentUserPassword
            )

            if (connectStat) {
                val authi = con?.isAuthenticated
                Log.i("connectStart", "The host is ******" + authi)
               // checkPresence()
                getRoaster()
            }
            chatManager = ChatManager.getInstanceFor(con)

            chatManager.addIncomingListener { from, message, chat ->
                lifecycleScope.launch {
                withContext(Dispatchers.Main) {
                    chatAdapter.addData(ChatModel(message?.body.toString(), 1))
                    chatAdapter.notifyItemInserted(chatAdapter.listMessage.size-1)
                    binding.rvChat.smoothScrollToPosition(chatAdapter.listMessage.size-1)
                }}
                Log.i("#Message", message.toString())
            }

            try {
                val entityBareJid =
                    JidCreate.entityBareFrom("$opponentUserJid@$defaultIP")
                chat = chatManager.chatWith(entityBareJid)
            } catch (e: Exception) {
                Log.i("the error", e.message!!)
            }
        }
    }

    private suspend fun initXMPP() {
        try {
            val sslContext = SSLContext.getInstance("TLS")
            sslContext.init(
                null,
                arrayOf<X509TrustManager>(),
                SecureRandom()
            )

            configEx = null
            conf =
                XMPPTCPConnectionConfiguration.builder()
                    .setXmppDomain(defaultIP)
                    .setPort(defaultPort)
                    .enableDefaultDebugger()
                    .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
                    .setCompressionEnabled(false)
                    .setSendPresence(true)
                    .setHost(defaultIP)
                    .setKeystoreType("AndroidCAStore")
                    .build()

        } catch (e: java.lang.Exception) {
            configEx = e

        }
    }

    private suspend fun connectAndLogin(uname: String?, pwd: String?): Boolean {
        conEx = null // check first to see if no exception occurred while creating config
        if (configEx == null) {

            try {
                val job = lifecycleScope.launch {
                    con = XMPPTCPConnection(conf)
                }
                job.join()

                val job1 = CoroutineScope(Dispatchers.IO).launch {
                    con?.connect()
                    con?.login(uname, pwd)
                }
                job1.join()
                return true

            } catch (e: java.lang.Exception) {
                Log.i(TAG, e.message!!)
                conEx = e
                return false
            }
        } else {
            return false
        }
    }
    private fun checkPresence(){

//        val roster: Roster =Roster.getInstanceFor(con);
//        val entries: Collection<RosterEntry> = roster.entries
//        var presence: Presence
//// roster.getPresence(con.user.asBareJid())
//        for (entry in entries) {
//            presence = roster.getPresence(entry.jid)
//            println(entry.user)
//            println(presence.type.name)
//            println(presence.status)
//        }

//        val presence = Presence(Presence.Type.available)
//
//        con?.sendStanza(presence)
//        val roster = Roster.getInstanceFor(con)
//
//
//        val entries: Collection<RosterEntry> = roster.entries
//        Log.i("entry", entries.toString() + "")
//        for (entry in entries) {
//            val  availability:Presence = roster.getPresence(entry.jid);
//            val  userMode:Mode = availability.getMode();
//
//            lifecycleScope.launch {
//                withContext(Dispatchers.Main){
//                    val presence=retrieveState(availability.mode,availability.isAvailable);
//                    Log.d("#Presence: $userMode",":$presence")
//                }
//            }
//        }

    }

    fun getRoaster() {
        val roster = Roster.getInstanceFor(con)
        if (!roster.isLoaded) try {
            roster.reloadAndWait()
        } catch (e: NotLoggedInException) {
            e.printStackTrace()
        } catch (e: NotConnectedException) {
            e.printStackTrace()
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        val entries: Collection<RosterEntry> = roster.entries
        for (entry in entries) {
            Log.d("##CON", entry.name)
        }



        roster.addRosterListener(object : RosterListener {
            // Ignored events public void entriesAdded(Collection<String> addresses) {}

            override fun entriesAdded(addresses: MutableCollection<Jid>?) {
                println("Presence changed: entriesAdded")
            }

            override fun entriesUpdated(addresses: MutableCollection<Jid>?) {
                println("Presence changed: entriesUpdated")
            }

            override fun entriesDeleted(addresses: MutableCollection<Jid>?) {
                println("Presence changed: entriesDeleted")
            }

            override fun presenceChanged(presence: Presence) {
                showToast("Presence changed: " + presence.from + " " + presence)
                println("Presence changed: " + presence.from + " " + presence)
            }
        })


    }
    fun retrieveState(userMode: Mode, isOnline: Boolean): Int {
        /** 0 for offline, 1 for online, 2 for away,3 for busy */
        var userState = 0 // default return value
        if (userMode === Mode.dnd) {
            Log.e("busy", "busy")
            userState = 3
        } else if (userMode === Mode.away || userMode === Mode.xa) {
            userState = 2
        } else if (isOnline) {
            Log.e("online", "online")
            userState = 1
        }
        return userState
    }
}
//https://stackoverflow.com/questions/26670762/android-with-asmack-how-to-check-user-presence-status-in-xmpp-in-android
/* /* /*{"status":"success",
               "message":"Authentication successful.",
               "user_data":{"email":"a1@mailinator.com",
               "username_part_of_jid":"7071cfd0-981c-4a2c-b032-de5328657b36",
               "user_xmpp_pass":"AYetjAaY9p3zKl41990",
               "domain_part_of_jid": "pinleele.com",
               "country_code":"+371",
               "country":"Latvia"}}*/


               /*{"status":"success",
               "message":"Authentication successful.",
               "user_data":{"email":"a2@mailinator.com",
               "username_part_of_jid":"bdfa9f69-e2a5-4e8c-bd4d-f1d7fd85c310",
               "user_xmpp_pass":"aiws5w5NWrx9wf29350",
               "domain_part_of_jid":"pinleele.com",
               "country_code":"+371",
               "country":"Latvia"}}*/*/*/