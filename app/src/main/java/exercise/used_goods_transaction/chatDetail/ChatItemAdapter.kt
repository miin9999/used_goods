package exercise.used_goods_transaction.chatDetail


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import exercise.used_goods_transaction.databinding.ItemChatBinding


class ChatItemAdapter : ListAdapter<chatItem, ChatItemAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding :ItemChatBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(chatItem : chatItem){
            binding.senderTextView.text = chatItem.senderId
            binding.messageTextView.text = chatItem.message

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemChatBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(currentList[position])
    }

    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<chatItem>(){
            override fun areItemsTheSame(oldItem: chatItem, newItem: chatItem): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: chatItem, newItem: chatItem): Boolean {
                return oldItem == newItem
            }
        }
    }


}