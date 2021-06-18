package org.teamseven.ols.repositories

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import io.socket.client.Socket
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import org.teamseven.ols.db.*
import org.teamseven.ols.entities.Conversation
import org.teamseven.ols.entities.Message
import org.teamseven.ols.entities.User
import org.teamseven.ols.entities.crossref.ClassroomAndConversationCrossRef
import org.teamseven.ols.entities.crossref.ConversationAndMemberCrossRef
import org.teamseven.ols.entities.crossref.ConversationAndMessageCrossRef
import org.teamseven.ols.entities.crossref.SenderAndMessageCrossRef
import org.teamseven.ols.entities.requests.FirstMessageRequest
import org.teamseven.ols.entities.requests.NewConversationRequest
import org.teamseven.ols.entities.requests.NewMessageRequest
import org.teamseven.ols.entities.responses.NewConversationResponse
import org.teamseven.ols.network.MessageApiService
import org.teamseven.ols.network.socketio.MessageSocketIOService
import org.teamseven.ols.utils.Constants
import org.teamseven.ols.utils.IOEvent
import org.teamseven.ols.utils.Resource
import org.teamseven.ols.utils.SessionManager
import retrofit2.Response
import timber.log.Timber
import javax.inject.Inject

class MessageRepository @Inject constructor(
    val database: AppDatabase,
    val messageApiService: MessageApiService,
    val sessionManager: SessionManager
) {
    private val userDao: UserDao by lazy { database.userDao() }
    private val conversationDao: ConversationDao by lazy { database.conversationDao() }
    private val messageDao: MessageDao by lazy { database.messageDao() }

    //TODO update SocketIOService when token changes
    private var messageSocketIOService = MessageSocketIOService(
        uri = "${Constants.BASE_WS_URL}/${Constants.MESSAGE_NAMESPACE}",
        token = sessionManager.token!!
    )

    init {
        messageSocketIOService.socket.apply {
            on(Socket.EVENT_CONNECT) {
                Timber.i("socket io connected")
            }
            on(Socket.EVENT_DISCONNECT) {
                Timber.i("socket io disconnected")
            }
            on(Socket.EVENT_CONNECT_ERROR) {
                Timber.i("socket io connect error")
                Timber.i(it.toList().toString())
            }
            on(IOEvent.PROCESS_ERROR) {
                Timber.i(it.toList().toString())
            }
        }
    }

    fun getAllConversation(): Flow<Resource<List<Conversation>>> {
        return object : NetworkBoundResource<List<Conversation>, List<Conversation>>() {
            override fun query(): Flow<List<Conversation>> {
                return conversationDao.getAllConversations()
            }

            override fun shouldFetch(data: List<Conversation>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun fetch(): Response<List<Conversation>> {
                return messageApiService.getAllConversation()
            }

            override fun processResponse(response: Response<List<Conversation>>): List<Conversation> {
                return response.body()!!
            }

            override suspend fun saveCallResult(item: List<Conversation>) {
                conversationDao.insert(
                    *item.toTypedArray()
                )

                val classroomAndConversation = item.map {
                    ClassroomAndConversationCrossRef(
                        classroomId = it.classroomId,
                        conversationId = it.id
                    )
                }

                conversationDao.insertClassroomConversation(
                    *classroomAndConversation.toTypedArray()
                )
            }

        }.asFlow()
    }

    fun getAllConversationsInClassroom(classroomId: Int): Flow<Resource<List<Conversation>>> {
        return object : NetworkBoundResource<List<Conversation>, List<Conversation>>() {
            override fun query(): Flow<List<Conversation>> {
                return conversationDao.getAllConversationsInClassroom(classroomId)
            }

            override fun shouldFetch(data: List<Conversation>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun fetch(): Response<List<Conversation>> {
                return messageApiService.getClassroomConversation(classroomId)
            }

            override fun processResponse(response: Response<List<Conversation>>): List<Conversation> {
                return response.body()!!
            }

            override suspend fun saveCallResult(item: List<Conversation>) {
                conversationDao.insert(
                    *item.toTypedArray()
                )

                val classroomAndConversation = item.map {
                    ClassroomAndConversationCrossRef(
                        classroomId = it.classroomId,
                        conversationId = it.id
                    )
                }

                conversationDao.insertClassroomConversation(
                    *classroomAndConversation.toTypedArray()
                )
            }

        }.asFlow()
    }

    fun getConversationMembers(conversationId: Int): Flow<Resource<List<User>>> {
        return object : NetworkBoundResource<List<User>, List<User>>() {
            override fun query(): Flow<List<User>> {
                return conversationDao.getConversationMembers(conversationId).map { it.members }
            }

            override fun shouldFetch(data: List<User>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun fetch(): Response<List<User>> {
                return messageApiService.getConversationMembers(conversationId)
            }

            override fun processResponse(response: Response<List<User>>): List<User> {
                Timber.i(response.body().toString())
                return response.body()!!
            }

            override suspend fun saveCallResult(item: List<User>) {
                userDao.insertAll(
                    *item.toTypedArray()
                )

                val crossRefs = item.map {
                    ConversationAndMemberCrossRef(
                        conversationId = conversationId,
                        memberId = it.id
                    )
                }

                conversationDao.insertClassroomMember(
                    *crossRefs.toTypedArray()
                )
            }

        }.asFlow()
    }

    fun getConversationMessage(conversationId: Int): Flow<Resource<List<Message>>> {
        return object : NetworkBoundResource<List<Message>, List<Message>>() {
            override fun query(): Flow<List<Message>> {
                return conversationDao.getConversationMessages(conversationId).map { it.messages }
            }

            override fun shouldFetch(data: List<Message>?): Boolean {
                return data.isNullOrEmpty()
            }

            override suspend fun fetch(): Response<List<Message>> {
                return messageApiService.getConversationMessages(conversationId)
            }

            override fun processResponse(response: Response<List<Message>>): List<Message> {
                return response.body()!!
            }

            override suspend fun saveCallResult(item: List<Message>) {
                messageDao.insert(
                    *item.toTypedArray()
                )

                val conversationCrossRefs = item.map {
                    ConversationAndMessageCrossRef(
                        conversationId = conversationId.toLong(),
                        messageId = it.id
                    )
                }

                messageDao.insertConversationMessage(
                    *conversationCrossRefs.toTypedArray()
                )

                val userCrossRefs = item.map {
                    SenderAndMessageCrossRef(
                        messageId = it.id,
                        senderId = it.senderId
                    )
                }

                messageDao.insertMessageSender(
                    *userCrossRefs.toTypedArray()
                )
            }

        }.asFlow()
    }

    /**
     * @author biennd3
     * @return LifecycleObserver can auto connect and disconnect
     * by **ON_START** and **ON_STOP** events in Fragment/Activity.
     */
    fun getSocketIoLifecycleObserver() = object : LifecycleObserver {
        @OnLifecycleEvent(Lifecycle.Event.ON_START)
        fun openConnection() {
            messageSocketIOService.open()
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_STOP)
        fun closeConnection() {
            messageSocketIOService.close()
        }
    }

    @ExperimentalCoroutinesApi
    fun getNewMessage(): Flow<Resource<Message?>> {
        return messageSocketIOService
            .messages()
            .map { message ->
                if (message != null) {
                    messageDao.insert(message)
                    messageDao.insertConversationMessage(
                        ConversationAndMessageCrossRef(
                            conversationId = message.conversationId,
                            messageId = message.id
                        )
                    )
                    return@map Resource.success(message)
                } else {
                    return@map Resource.error(null, "null message")
                }
            }
            .onStart {
                emit(Resource.loading(null))
            }

    }

    /**
     * @author biennd3
     * @return Flow of new conversation response with new conversation and first message. But not
     * include participant of conversation. Should fetch with REST API in other method.
     */
    @ExperimentalCoroutinesApi
    fun getNewConversation(): Flow<Resource<NewConversationResponse>> {
        return messageSocketIOService
            .newConversations()
            .map { newConversation: NewConversationResponse? ->
                if (newConversation != null) {
                    val conversation = newConversation.conversation
                    val firstMessage = newConversation.firstMessage

                    conversationDao.insert(conversation)
                    messageDao.insert(firstMessage)
                    messageDao.insertMessageSender(
                        SenderAndMessageCrossRef(
                            firstMessage.senderId,
                            firstMessage.id
                        )
                    )
                    return@map Resource.success(newConversation)
                } else {
                    return@map Resource.error(null, "New conversation null")
                }
            }
            .onStart {
                emit(Resource.loading(null))
            }
    }

    fun sendMessage(
        newMessageRequest: NewMessageRequest
    ) = messageSocketIOService.sendMessage(newMessageRequest)

    fun createNewConversation(
        senderId: Int,
        receiverIds: List<Int>,
        firstMessage: FirstMessageRequest,
        classroomId: Int
    ) {
        messageSocketIOService.newConversation(
            NewConversationRequest(
                senderId,
                firstMessage,
                receiverIds,
                firstMessage.attachment,
                classroomId
            )
        )
    }
}