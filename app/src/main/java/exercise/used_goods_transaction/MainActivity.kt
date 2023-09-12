package exercise.used_goods_transaction

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
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

        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottmNavi)

        replaceFragment(homeFragment)

        // 바텀 내비 안에 있는 아이템을 눌렀을때 그 프래그먼트로 대체하면 됨

        bottomNavigationView.setOnNavigationItemSelectedListener {

            when(it.itemId){
                R.id.home-> replaceFragment(homeFragment)
                R.id.chat->replaceFragment(chatListFragment)
                R.id.myPage->replaceFragment(myPageFragment)

            }
            true
        }

    }

    private fun replaceFragment(fragment: Fragment){

        supportFragmentManager.beginTransaction().apply{
            replace(R.id.frameLayout,fragment)
            commit()
        }

    }
}