package org.teamseven.ols.ui.message_box.new_message

/*
    // contact is a people / member or group - class group - conversation
    // contact_role: 0 for conversation, 1 for people (new message)
 */

data class ContactItem (val contact_id: Int, val contact_name: String, val contact_role: Int, val contact_avatar: Int?) {

}