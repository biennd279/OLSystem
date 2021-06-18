package org.teamseven.ols.ui.conversation.message_box

/*
    // contact is a people / member or group - class group - conversation
    // contact_role: 0 for conversation, 1 for people (new message)
 */

data class ConversationItem (val message_id: Int, val message: String,
                             val sender_id: Int, val sender_name: String, val sender_avatar: Int?,
                             val date: String, val time: String) {

}