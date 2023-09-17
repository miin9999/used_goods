package exercise.used_goods_transaction.myPage

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import exercise.used_goods_transaction.R
import exercise.used_goods_transaction.databinding.FragmentMypageBinding

class MyPageFragment : Fragment(R.layout.fragment_mypage) {


    private var binding: FragmentMypageBinding? = null
    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragmentMypageBinding = FragmentMypageBinding.bind(view)
        binding = fragmentMypageBinding


        // 로그 인 아웃 버튼
        fragmentMypageBinding.signInOutBtn.setOnClickListener {
            binding?.let {
                val email = it.emailEditText.text.toString()
                val password = it.passwordEditText.text.toString()


                if (auth.currentUser == null) {
                    // 현재 유저가 없을 시 로그인 작업 시행
                    auth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(requireActivity()) { task ->
                            if (task.isSuccessful) {
                                successSignIn()
                            } else {
                                Toast.makeText(context,"로그인 실패",Toast.LENGTH_SHORT).show()
                            }
                        }
                } else {
                    // 이미 로그인 되어 있는 상태일 때
                    //  로그아웃버튼일테니까 로그아웃버튼을 누르면 edittext 다 초기화하고 버튼을 true로 바꿈, auth 로그아웃
                    // 여기 몬가 이상한데
                    // 먼저 버튼을 누르고 나서 currentuser를 검사하는게 잘못됨
                    // 먼저 유저를 검사하고 버튼의 true false를 정해야 할듯
                    // 나중에 고치기ㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣㅣ
                    auth.signOut()
                    it.emailEditText.text.clear()
                    it.passwordEditText.text.clear()
                    it.signInOutBtn.isEnabled = false
                    it.signUpBtn.isEnabled = false
                    it.emailEditText.isEnabled = true
                    it.passwordEditText.isEnabled = true
                    it.signInOutBtn.text = "로그인"

                }
            }
        }


        fragmentMypageBinding.signUpBtn.setOnClickListener {
            binding?.let{

                val email = it.emailEditText.text.toString()
                val password = it.passwordEditText.text.toString()


                auth.createUserWithEmailAndPassword(email,password)
                    .addOnCompleteListener(requireActivity()){ task->
                        if(task.isSuccessful){
                            Toast.makeText(context, "회원가입에 성공했습니다. 로그인 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show()

                        }else{
                            Toast.makeText(context, "회원가입에 실패했습니다. 이미 가입한 이메일일 수 있습니다.", Toast.LENGTH_SHORT).show()

                        }
                    }
            }
        }

        // emailText와 passwordText 둘 중 하나라도 비어 있으면 회원가입,로그인 버튼을 비활성화

        fragmentMypageBinding.emailEditText.addTextChangedListener {
            binding?.let{ binding->
                val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutBtn.isEnabled = enable
                binding.signUpBtn.isEnabled = enable

            }
        }

        fragmentMypageBinding.passwordEditText.addTextChangedListener {
            binding?.let{ binding->
                val enable = binding.emailEditText.text.isNotEmpty() && binding.passwordEditText.text.isNotEmpty()
                binding.signInOutBtn.isEnabled = enable
                binding.signUpBtn.isEnabled = enable

            }
        }
    }

    private fun successSignIn() {
        if(auth.currentUser == null){
            Toast.makeText(context, "로그인 실패", Toast.LENGTH_SHORT).show()
        }
        binding?.let{
            it.emailEditText.isEnabled = false
            it.passwordEditText.isEnabled =false
            it.signUpBtn.isEnabled = false
            it.signInOutBtn?.text = "로그아웃"
        }

    }

    override fun onStart() {
        super.onStart()

        if(auth.currentUser == null) {
            binding?.let { binding ->
                binding.emailEditText.text.clear()
                binding.passwordEditText.text.clear()
                binding.emailEditText.isEnabled = true
                binding.passwordEditText.isEnabled = true
                binding.signInOutBtn.text = "로그인"
                binding.signInOutBtn.isEnabled = false
                binding.signUpBtn.isEnabled = false
            }
        } else {
            binding?.let { binding ->
                binding.emailEditText.setText(auth.currentUser!!.email)
                binding.passwordEditText.setText("***********")
                binding.emailEditText.isEnabled = false
                binding.passwordEditText.isEnabled = false
                binding.signInOutBtn.text = "로그아웃"
                binding.signInOutBtn.isEnabled = true
                binding.signUpBtn.isEnabled = false
            }
        }



    }
}