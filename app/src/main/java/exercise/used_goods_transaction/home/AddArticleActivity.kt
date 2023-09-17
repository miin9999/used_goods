package exercise.used_goods_transaction.home

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.ktx.storage
import exercise.used_goods_transaction.DBkeys.Companion.DB_ARTICLES
import exercise.used_goods_transaction.R
import exercise.used_goods_transaction.databinding.ActivityAddArticleBinding

class AddArticleActivity : AppCompatActivity() {


    private var selectedUri: Uri? = null

    private val auth: FirebaseAuth by lazy {
        Firebase.auth
    }

    private val storage: FirebaseStorage by lazy {
        Firebase.storage
    }

    private val articleDB: DatabaseReference by lazy {
        Firebase.database.reference.child(DB_ARTICLES)
    }

    var binding: ActivityAddArticleBinding? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        var binding = ActivityAddArticleBinding.inflate(layoutInflater)
        setContentView(binding!!.root)

        binding?.let { binding ->
            binding.imageRegistration.setOnClickListener {


                when {
                    ContextCompat.checkSelfPermission(
                        this,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE
                    )
                            == PackageManager.PERMISSION_GRANTED -> {
                        // 권한이 승인되면 앨범을 여는 intent발생
                        // 여기는 그냥 안드로이드 스스로 이미 권한을 승인 했었는지 검사하는건가???
                        startContextProvider()
                    }

                    shouldShowRequestPermissionRationale(android.Manifest.permission.READ_EXTERNAL_STORAGE) -> {
                        // 교육용 팝업
                        showPermissionContextPopup()

                    }

                    else -> {
                        // 여기가 '새로',처음으로 권한을 받아서 리퀘스트 받아오는 곳 같음
                        requestPermissions(
                            arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE), 1010
                        )

                    }
                }

            }

            binding.Registration.setOnClickListener {
                // 최종 등록하기 버튼 - model형태로 DB에 올려야 함
                val title = binding.titleEditText.text.toString()
                val price = binding.priceEditText.text.toString()
                val sellerId = auth.currentUser?.uid.orEmpty()

                showProgress() // 프로그래스 ui 보이게 하기

                // 이미지가 있을 경우엔 storage에 image를 올리는 식으로 추가 과정을 진행한다
                if (selectedUri != null) {
                    val photoUri = selectedUri ?: return@setOnClickListener
                    uploadPhoto(photoUri,
                        successHandler = { uri -> uploadArticle(sellerId, title, price, uri) },
                        errorHandler = {
                            Toast.makeText(this, "사진 업로드 실패", Toast.LENGTH_SHORT).show()
                            hideProgress()
                        })
                } else {
                    uploadArticle(sellerId, title, price, "")
                }

            }

        }

    }

    private fun uploadPhoto(uri: Uri, successHandler: (String) -> Unit, errorHandler: () -> Unit) {
        val fileName = "${System.currentTimeMillis()}.png"

        storage.reference.child("article/photo").child(fileName)
            .putFile(uri)
            .addOnCompleteListener {

                if (it.isSuccessful) {
                    storage.reference.child("article/photo").child(fileName)
                        .downloadUrl
                        .addOnSuccessListener { uri ->
                            successHandler(uri.toString())
                        }
                        .addOnFailureListener {
                            errorHandler()
                        }
                } else {
                    errorHandler()
                }
            }
    }

    private fun uploadArticle(sellerId: String, title: String, price: String, imageUrl: String) {

        val model = ArticleModel(sellerId, title, System.currentTimeMillis(), "$price 원", imageUrl)
        articleDB.push().setValue(model)

        hideProgress()
        finish()
    }

    private fun showProgress() {
        binding?.let {
            it.progressBar.isVisible = true
        }
    }

    private fun hideProgress() {
        binding?.let {
            it.progressBar.isVisible = false
        }

    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // 앨범을 열어서 선택한 사진들이 data로 넘어온다

        if(resultCode != Activity.RESULT_OK) return

        when(requestCode){
            2020 ->{
                val uri = data?.data
                if(uri!=null){
                    binding?.let { it.imageViewArea.setImageURI(uri) }
                    selectedUri = uri
                }else{
                    Toast.makeText(this, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()

                }
            }
            else ->{
                Toast.makeText(this, "사진을 가져오지 못했습니다", Toast.LENGTH_SHORT).show()

            }


        }


    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        when (requestCode) {
            1010 ->
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startContextProvider()
                } else {
                    Toast.makeText(this, "권한을 거부하셨습니다.", Toast.LENGTH_SHORT).show()
                }
        }
    }


    private fun startContextProvider() {

        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "image/*"
        startActivityForResult(intent, 2020)
    }

    private fun showPermissionContextPopup() {
        AlertDialog.Builder(this)
            .apply {
                setTitle("권한이 필요합니다")
                setMessage("사진을 가져오기 위해 필요합니다")
                setPositiveButton("동의", { _, _ ->
                    requestPermissions(
                        arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE),
                        1010
                    )
                })
                create()
                show()
            }
    }
}