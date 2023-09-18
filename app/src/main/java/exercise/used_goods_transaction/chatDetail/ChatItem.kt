package exercise.used_goods_transaction.chatDetail

data class ChatItem (
    val senderId: String,
    val message: String,
        ){
    constructor(): this("","")
}

