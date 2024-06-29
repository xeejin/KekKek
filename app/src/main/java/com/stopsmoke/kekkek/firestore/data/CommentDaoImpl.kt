package com.stopsmoke.kekkek.firestore.data

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.google.firebase.Timestamp
import com.google.firebase.firestore.AggregateSource
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.dataObjects
import com.stopsmoke.kekkek.firestore.COMMENT_COLLECTION
import com.stopsmoke.kekkek.firestore.POST_COLLECTION
import com.stopsmoke.kekkek.firestore.dao.CommentDao
import com.stopsmoke.kekkek.firestore.data.pager.FireStorePagingSource
import com.stopsmoke.kekkek.firestore.model.CommentEntity
import com.stopsmoke.kekkek.firestore.model.DateTimeEntity
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.mapNotNull
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class CommentDaoImpl @Inject constructor(
    private val firestore: FirebaseFirestore,
) : CommentDao {

    override fun getComment(postId: String): Flow<PagingData<CommentEntity>> {
        val query = firestore.collection(POST_COLLECTION)
            .document(postId)
            .collection(COMMENT_COLLECTION)
            .orderBy("date_time.created", Query.Direction.ASCENDING)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = CommentEntity::class.java
            )
        }
            .flow
    }

    override fun getComment(postId: String, commentId: String): Flow<CommentEntity> {
        return firestore.collection(POST_COLLECTION)
            .document(postId)
            .collection(COMMENT_COLLECTION)
            .document(commentId)
            .dataObjects<CommentEntity>()
            .mapNotNull { it }
    }

    override fun getMyCommentItems(uid: String): Flow<PagingData<CommentEntity>> {
        val query = firestore.collectionGroup(COMMENT_COLLECTION)
            .whereEqualTo("written.uid", uid)
            .orderBy("date_time.created", Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = CommentEntity::class.java
            )
        }
            .flow
    }

    override fun getCommentItems(commentIdList: List<String>): Flow<PagingData<CommentEntity>> {
        val query = firestore.collection(COMMENT_COLLECTION)
            .whereIn("id", commentIdList)
            .orderBy("date_time", Query.Direction.DESCENDING)

        return Pager(
            config = PagingConfig(PAGE_LIMIT)
        ) {
            FireStorePagingSource(
                query = query,
                limit = PAGE_LIMIT.toLong(),
                clazz = CommentEntity::class.java
            )
        }
            .flow
    }

    override suspend fun addComment(commentEntity: CommentEntity) {
        firestore
            .collection(POST_COLLECTION)
            .document(commentEntity.parent!!.postId!!)
            .collection(COMMENT_COLLECTION)
            .document().let { documentReference ->
                documentReference.set(
                    commentEntity.copy(
                        id = documentReference.id
                    )
                )
                documentReference.update("date_time.created", FieldValue.serverTimestamp())
                documentReference.update("date_time.modified", FieldValue.serverTimestamp())
            }
            .await()
    }

    override suspend fun setCommentItem(commentEntity: CommentEntity) {
       firestore
           .collection(POST_COLLECTION)
           .document(commentEntity.parent!!.postId!!)
           .collection(COMMENT_COLLECTION)
           .document(commentEntity.id!!)
           .set(commentEntity)
    }

    override suspend fun updateOrInsertComment(commentEntity: CommentEntity) {
        firestore
            .collection(COMMENT_COLLECTION)
            .document(commentEntity.id!!)
            .set(commentEntity)
            .await()
    }

    override suspend fun deleteComment(postId: String, commentId: String) {
        firestore
            .collection(POST_COLLECTION)
            .document(postId)
            .collection(COMMENT_COLLECTION)
            .document(commentId)
            .delete()
            .await()
    }

    override fun getCommentCount(postId: String): Flow<Long> = callbackFlow {
        firestore.collection(POST_COLLECTION)
            .document(postId)
            .collection(COMMENT_COLLECTION)
            .count()
            .get(AggregateSource.SERVER)
            .addOnSuccessListener { trySend(it.count) }
            .addOnFailureListener { throw it }

        awaitClose()
    }

    companion object {
        private const val PAGE_LIMIT = 30
    }
}