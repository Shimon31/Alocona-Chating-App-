package com.iaa2401.aloconachattingapp



interface Message{
    val msgID : String
    val senderID : String
    val receiverID : String

}

data class TextMessage(
    val text : String? = null,
    override var msgID: String = "",
    override val senderID: String = "",
    override val receiverID: String = ""
):Message


data class MessageWithImage(
    val imageMsg : String? = null,
    override val msgID: String,
    override val senderID: String,
    override val receiverID: String
):Message