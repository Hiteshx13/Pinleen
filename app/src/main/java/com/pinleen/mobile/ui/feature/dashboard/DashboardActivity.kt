package com.pinleen.mobile.ui.feature.dashboard

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.widget.AppCompatImageView
import androidx.navigation.Navigation.findNavController
import com.pinleen.mobile.R
import com.pinleen.mobile.databinding.ActivityDashboardBinding
import com.pinleen.mobile.ui.base.BaseActivity
import org.jivesoftware.smack.chat2.Chat

class DashboardActivity : BaseActivity<ActivityDashboardBinding>() {

    companion object {
        fun getIntent(mContext: Context): Intent {
            return Intent(mContext, DashboardActivity::class.java)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initObserver()
        changeStatue(binding.icHomeShop)
        // initXmppConnection()
    }

    /* private fun initXmppConnection() {

         *//*{"status":"success",
        "message":"Authentication successful.",
        "user_data":{"email":"a1@mailinator.com",
        "username_part_of_jid":"7071cfd0-981c-4a2c-b032-de5328657b36",
        "user_xmpp_pass":"AYetjAaY9p3zKl41990",
        "domain_part_of_jid": "pinleele.com",
        "country_code":"+371",
        "country":"Latvia"}}*//*


        *//*{"status":"success",
        "message":"Authentication successful.",
        "user_data":{"email":"a2@mailinator.com",
        "username_part_of_jid":"bdfa9f69-e2a5-4e8c-bd4d-f1d7fd85c310",
        "user_xmpp_pass":"aiws5w5NWrx9wf29350",
        "domain_part_of_jid":"pinleele.com",
        "country_code":"+371",
        "country":"Latvia"}}*//*

//        val conHandler1 = XMPPTCPConnection(
//            "7071cfd0-981c-4a2c-b032-de5328657b36@pinleele.com",
//            "AYetjAaY9p3zKl41990"
//        )
////        val conHandler2 = XMPPTCPConnection(
////            "bdfa9f69-e2a5-4e8c-bd4d-f1d7fd85c310@pinleele.com",
////            "aiws5w5NWrx9wf29350"
////        )

        AndroidSmackInitializer.initialize(this)
        //val conHandler = XmppTCon()


        var connectStat = false



        lifecycleScope.launch {
            initXMPP()

            connectStat = connectAndLogin(
                "7071cfd0-981c-4a2c-b032-de5328657b36",
                "AYetjAaY9p3zKl41990"
            )

            if (connectStat) {
                val authi = con?.isAuthenticated
                Log.i("connectStart", "The host is ******" + authi)
            }
            val chatManager: ChatManager = ChatManager.getInstanceFor(con)

            chatManager.addIncomingListener(object : IncomingChatMessageListener {
                override fun newIncomingMessage(
                    from: EntityBareJid?,
                    message: Message?,
                    chat: Chat?
                ) {
                    Log.i("#Message", message.toString())
                }
            })
            try {
                val entityBareJid =
                    JidCreate.entityBareFrom("ed4e729b-467b-46ad-8da3-f2c59a0473e9@pinleele.com")
               chat = chatManager.chatWith(entityBareJid)
                chat?.send("waraaaaaaaaaaaaaaaaaaaaaaaaaaaup !!!!")
                //conHandler.con.disconnect()
            } catch (e: Exception) {
                Log.i("the error", e.message!!)
            }
        }


    }*/

    override val bindingInflater: (LayoutInflater) -> ActivityDashboardBinding
        get() = ActivityDashboardBinding::inflate

    override fun initListener() {

        binding.icHomeChat.setOnClickListener {
            changeStatue(binding.icHomeChat)
            findNavController(binding.fragmentContainerView).navigate(R.id.chatFragment)
        }
        binding.icHomeContacts.setOnClickListener {
            changeStatue(binding.icHomeContacts)
            findNavController(binding.fragmentContainerView).navigate(R.id.contactsFragment)

        }
        binding.icHomeShop.setOnClickListener {
            changeStatue(binding.icHomeShop)
            findNavController(binding.fragmentContainerView).navigate(R.id.shopFragment)

        }
        binding.icHomeMe.setOnClickListener {
            changeStatue(binding.icHomeMe)
            findNavController(binding.fragmentContainerView).navigate(R.id.meFragment)


        }
    }


    private fun changeStatue(view: AppCompatImageView) {

        binding.icHomeShop.isSelected = false
        binding.icHomeMe.isSelected = false
        binding.icHomeContacts.isSelected = false
        binding.icHomeChat.isSelected = false
        view.isSelected = true
    }


//    private val TAG = "SMACK"
//    private val defaultIP = "pinleele.com"
//    private val defaultPort = 5222
//    var conf: XMPPTCPConnectionConfiguration? = null
//    var con: AbstractXMPPConnection? = null
//
//    private var configEx: java.lang.Exception? = null
//
//    //This variable will keep track if during the construction of a configuration object...
//    // some error occurred or not so that the conf can be used safely.  private Exception configEx;
//    private var conEx: java.lang.Exception? = null

//    suspend fun initXMPP() {
//        try {
//            val sslContext = SSLContext.getInstance("TLS")
//            sslContext.init(
//                null,
//                arrayOf<X509TrustManager>(),
//                SecureRandom()
//            )
//
//            configEx = null
//
//            var str: String? = null
//            if (str == null) {
//                str =
//                    System.getProperty("java.home") + File.separator + "etc" + File.separator + "security" + File.separator + "cacerts.bks"
//            }
//            val serviceName: DomainBareJid = JidCreate.domainBareFrom(defaultIP)
//
//            conf =
//                XMPPTCPConnectionConfiguration.builder()
//                    .setXmppDomain(defaultIP)
//                    .setPort(defaultPort)
//                    .enableDefaultDebugger()
//                    .setSecurityMode(ConnectionConfiguration.SecurityMode.ifpossible)
//                    .setCompressionEnabled(false)
//                    .setSendPresence(true)
////                    .setServiceName(serviceName)
//                    .setKeystoreType("AndroidCAStore")
//                    .build()
//
//        } catch (e: java.lang.Exception) {
//            configEx = e
//
//        }
//    }
//
//
//    suspend fun connectAndLogin(uname: String?, pwd: String?): Boolean {
//        conEx = null // check first to see if no exception occurred while creating config
//        if (configEx == null) {
//
//            try {
//                val job = lifecycleScope.launch {
//                    con = XMPPTCPConnection(conf)
//                }
//                job.join()
//
//                val job1 = CoroutineScope(Dispatchers.IO).launch {
//                    con?.connect()
//                    con?.login(uname, pwd)
//                }
//                job1.join()
//                return true
//
//            } catch (e: java.lang.Exception) {
//                Log.i(TAG, e.message!!)
//                conEx = e
//                return false
//            }
//        } else {
//            return false
//        }
//    }

    private fun initObserver() {

    }
}