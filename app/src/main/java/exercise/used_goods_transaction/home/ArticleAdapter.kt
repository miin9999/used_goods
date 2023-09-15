package exercise.used_goods_transaction.home

import android.icu.text.SimpleDateFormat
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import exercise.used_goods_transaction.databinding.ItemArticleBinding
import java.util.Date

class ArticleAdapter(val onItemClicked: (ArticleModel) -> Unit) :
    ListAdapter<ArticleModel, ArticleAdapter.ViewHolder>(diffUtil) {

    // ItemArticleBinding 형태로 생긴 view들로 조작 중
    // 이제 이 view들에 데이터를 끼워 넣어야 함

    inner class ViewHolder(private val binding: ItemArticleBinding) :
        RecyclerView.ViewHolder(binding.root) {


        // 함수인자로 받은 ArticleModel도 쓸 수 있는 듯?
        fun bind(articleModel: ArticleModel) {
            val format = SimpleDateFormat("MM월 dd일")
            val date = Date(articleModel.createAt)

            // 새로 들어온 articleModel의 title(그외 등등)로 끼워넣어주기
            binding.titleTextView.text = articleModel.title
            // 심플데이트포맷.formate(Date형태의 long타입 인자) << 이 형태임
            binding.dateTextView.text = format.format(date).toString()
            binding.priceTextView.text = articleModel.price

            if (articleModel.imageUrl.isNotEmpty()) {
                Glide.with(binding.thumbnailImageView)
                    .load(articleModel.imageUrl)
                    .into(binding.thumbnailImageView)
            }

            binding.root.setOnClickListener {
                // onItemClicked로 람다 형식의 함수가 넘어왔고
                // 그 함수에 articleModel을 인자로 넣은 것
                onItemClicked(articleModel)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(
            ItemArticleBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(currentList[position])
    }

    companion object {
        val diffUtil = object : DiffUtil.ItemCallback<ArticleModel>() {
            override fun areItemsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {

                return oldItem.createAt == newItem.createAt
            }

            override fun areContentsTheSame(oldItem: ArticleModel, newItem: ArticleModel): Boolean {

                return oldItem == newItem
            }

        }
    }


}