package exercise.used_goods_transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import exercise.used_goods_transaction.chatList.ChatListFragment
import exercise.used_goods_transaction.home.HomeFragment
import exercise.used_goods_transaction.myPage.MyPageFragment

class MainActivity : AppCompatActivity() {



    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val homeFragment = HomeFragment()
        val chatListFragment = ChatListFragment()
        val myPageFragment = MyPageFragment()






    }
}