package exercise.used_goods_transaction.home

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.ChildEvent
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import exercise.used_goods_transaction.R
import exercise.used_goods_transaction.databinding.FragmentHomeBinding

class HomeFragment : Fragment(R.layout.fragment_home){


    private lateinit var articleDB: DatabaseReference
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

        // DB에 있는 아이템들을 어댑터로 가져와서 보여주기
        // 1. DB 설정 (파베 realtime database)
        // 2. 어댑터 만들고 아이템 리스너로 DB에 있는 아이템 가져오기

        articleDB = Firebase.database.reference.child("Articles")

    }
}