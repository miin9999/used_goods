package exercise.used_goods_transaction.chatList

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import exercise.used_goods_transaction.DBkeys.Companion.CHILD_CHAT
import exercise.used_goods_transaction.DBkeys.Companion.DB_USERS
import exercise.used_goods_transaction.R
import exercise.used_goods_transaction.chatDetail.ChatRoomActivity
import exercise.used_goods_transaction.databinding.FragmentChatlistBinding

class ChatListFragment : Fragment(R.layout.fragment_chatlist){

    private var binding : FragmentChatlistBinding? = null
    lateinit var chatListAdapter: ChatListAdapter
    private val chatRoomList = mutableListOf<ChatListItem>()


    private val auth:FirebaseAuth by lazy{
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        var fragmentChatlistBinding = FragmentChatlistBinding.bind(view)
        binding = fragmentChatlistBinding

        // homefragment에서 채팅방 리스트? 정보를 DB에 넣었으니
        // DB에 넣어놓은 정보를 내려받으면 됨
        // 채팅 '탭' 클릭시(리사이클러뷰 아이템 클릭) DB정보를 가져오고 어댑터에 연결해서 보여준다

        // 채팅 리스트 중 하나를 눌렀을 때 채팅방 속으로 진입
        chatListAdapter = ChatListAdapter(onItemClicked = { ChatListItem ->

            val intent = Intent(requireContext(),ChatRoomActivity::class.java)
            intent.putExtra("chatKey",ChatListItem.key)
            startActivity(intent)
        })

        chatRoomList.clear()

        fragmentChatlistBinding.chattingListRecyclerView.adapter = chatListAdapter
        fragmentChatlistBinding.chattingListRecyclerView.layoutManager = LinearLayoutManager(requireContext())


        if(auth.currentUser == null) return

        val chatDB = Firebase.database.reference.child(DB_USERS).child(auth.currentUser!!.uid)
            .child(CHILD_CHAT)


        chatDB.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {

                snapshot.children.forEach{
                    val model = it.getValue(ChatListItem::class.java)
                    model ?: return

                    chatRoomList.add(model)
                }

                chatListAdapter.submitList(chatRoomList)
                chatListAdapter.notifyDataSetChanged()

            }
            override fun onCancelled(error: DatabaseError) {}
        })

    }

    override fun onResume() {
        super.onResume()
        chatListAdapter.notifyDataSetChanged()

    }
}