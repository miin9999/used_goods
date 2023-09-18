package exercise.used_goods_transaction.chatDetail

data class chatItem (
    val senderId: String,
    val message: String,
        ){
    constructor(): this("","")
}

