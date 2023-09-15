package exercise.used_goods_transaction.chatList

data class ChatListItem (
    val buyerId: String,
    val sellerId :String,
    val itemTitle:String,
    val key:Long,
        )
{
    constructor() : this("","","",0)
}