package app.ishizuka.ryo.firebasetest

data class UserInfoData(
    var name: String? = "",
    var email: String? = "",
    var roomName: MutableList<String>? = mutableListOf()
)