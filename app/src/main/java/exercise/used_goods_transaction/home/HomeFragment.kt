package exercise.used_goods_transaction.home

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.ChildEvent
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import exercise.used_goods_transaction.DBkeys
import exercise.used_goods_transaction.DBkeys.Companion.CHILD_CHAT
import exercise.used_goods_transaction.DBkeys.Companion.DB_ARTICLES
import exercise.used_goods_transaction.DBkeys.Companion.DB_USERS
import exercise.used_goods_transaction.R
import exercise.used_goods_transaction.chatList.ChatListItem
import exercise.used_goods_transaction.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home){


    private lateinit var articleDB: DatabaseReference
    private lateinit var userDB: DatabaseReference
    private val articleList = mutableListOf<ArticleModel>()
    private lateinit var articleAdapter : ArticleAdapter

    private var binding : FragmentHomeBinding? = null

    private val auth : FirebaseAuth by lazy{
        Firebase.auth
    }

    private val listener = object : ChildEventListener {
        override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

            val articleModelFromDB = snapshot.getValue(ArticleModel::class.java)
            articleModelFromDB ?: return

            //Log.d("onChildAddedTest",articleModelFromDB.price)
            articleList.add(articleModelFromDB)
            articleAdapter.submitList(articleList)


        }
        override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onChildRemoved(snapshot: DataSnapshot) {}
        override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
        override fun onCancelled(error: DatabaseError) {}

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        val fragmentHomeBinding = FragmentHomeBinding.bind(view)
        binding = fragmentHomeBinding
        articleList.clear() // 리스트 한 번 초기화인데 왜 하는지 모르겠음 일단 주석처리해서 없을 때를 확인해보기

        userDB = Firebase.database.reference.child(DB_USERS)
        articleDB = Firebase.database.reference.child(DB_ARTICLES)

        articleAdapter = ArticleAdapter(onItemClicked = { articleModel ->
            if(auth.currentUser != null) {
                // 로그인을 한 상태
                if(auth.currentUser!!.uid != articleModel.sellerId){
                    // 현재 나의 id와 seller의 아이디가 같지 않다면 채팅방을 열기
                    val chatRoom = ChatListItem(
                        buyerId = auth.currentUser!!.uid,
                        sellerId = articleModel.sellerId,
                        itemTitle = articleModel.title,
                        key = System.currentTimeMillis() // 현재 시각을 key로
                    )

                    // 위의 채팅방 정보를 DB에 넘겨야 함
                    userDB.child(auth.currentUser!!.uid)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    userDB.child(articleModel.sellerId)
                        .child(CHILD_CHAT)
                        .push()
                        .setValue(chatRoom)

                    Snackbar.make(view,"채팅방이 생성되었습니다. 채팅탭에서 확인해주세요",Snackbar.LENGTH_LONG).show()

                }
            }else{
                //로그인이 안 되어 있을 때
                Snackbar.make(view,"로그인 후 사용해주세요",Snackbar.LENGTH_LONG).show()

            }
        })

        fragmentHomeBinding.articleRecyclerView.layoutManager = LinearLayoutManager(context)
        fragmentHomeBinding.articleRecyclerView.adapter = articleAdapter

        fragmentHomeBinding.addFloatingButton.setOnClickListener {
            // 등록하기 버튼을 눌렀을 때의 행동
            // 로그인 후에 사용해야 하니 로그인이 되어 있는지 체크, 되어 있다면 새로운 activity로 넘어간다

            if(auth.currentUser?.uid != null){
                val intent = Intent(requireContext(),AddArticleActivity::class.java)
                startActivity(intent)
            }else{
                Snackbar.make(view,"로그인 후 사용해주세요",Snackbar.LENGTH_LONG).show()

            }
        }


        articleDB.addChildEventListener(listener)

    }

    override fun onResume() {
        super.onResume()
        articleAdapter.notifyDataSetChanged()
    }

    override fun onDestroy() {
        super.onDestroy()
        articleDB.removeEventListener(listener)
    }
}