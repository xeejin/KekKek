package com.stopsmoke.kekkek.core.domain.repository

import androidx.paging.PagingData
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.core.domain.model.Comment
import com.stopsmoke.kekkek.core.domain.model.CommentFilter
import kotlinx.coroutines.flow.Flow

interface CommentRepository {

    fun getCommentItems(commentFilter: CommentFilter): Flow<PagingData<Comment>>

    suspend fun addCommentItem(comment: Comment): String

    fun getCommentItems(commentIdList: List<String>): Result<Flow<PagingData<Comment>>>

    suspend fun setCommentItem(comment: Comment):Result<Unit>

    suspend fun insertOrReplaceCommentItem(comment: Comment): Result<Unit>

   suspend fun deleteCommentItem(postId: String, commentId: String): Result<Unit>

   fun getCommentCount(postId: String): Flow<Long>

   fun getComment(postId: String, commentId: String): Flow<Comment>

   suspend fun addCommentLike(postId: String, commentId: String)

   suspend fun removeCommentLike(postId: String, commentId: String)
}