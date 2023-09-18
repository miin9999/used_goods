package exercise.used_goods_transaction.chatList


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import exercise.used_goods_transaction.databinding.ItemChatlistBinding

class ChatListAdapter(val onItemClicked : (ChatListItem) -> Unit) : ListAdapter<ChatListItem, ChatListAdapter.ViewHolder>(diffUtil) {

    inner class ViewHolder(private val binding : ItemChatlistBinding) : RecyclerView.ViewHolder(binding.root){

        // HomeFragment에서 ChatListItem으로 넘겼으니 매개변수에서 그대로 받아오는 것임
        fun bind(chatListItem :ChatListItem){

            // 매개변수에 람다함수를 받아왔으니 이 함수를 사용
            // binding.root는 이 viewHolder의 어디를 클릭해도 작업이 되도록 한 것임
            binding.root.setOnClickListener {
                onItemClicked(chatListItem)
            }

            binding.chatRoomTitleTextView.text = chatListItem.itemTitle
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        return ViewHolder(ItemChatlistBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.bind(currentList[position])
    }


    companion object{
        val diffUtil = object : DiffUtil.ItemCallback<ChatListItem>(){
            override fun areItemsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
                return oldItem.key == newItem.key
            }

            override fun areContentsTheSame(oldItem: ChatListItem, newItem: ChatListItem): Boolean {
                return oldItem == newItem
            }

        }
        }
}
