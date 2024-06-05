package com.stopsmoke.kekkek.firestore.dao

import androidx.paging.PagingData
import com.stopsmoke.kekkek.firestore.model.PostEntity
import kotlinx.coroutines.flow.Flow

interface PostDao {

    /**
     * 게시글을 가져오는 함수입니다
     * [category] 공지사항, 인기글 등등 필터를 적용해서 가져올 수 있습니다
     */
    // https://www.notion.so/stopsmoke/enum-PostCategory-c6956f5b008d4185bcd3dfe42dfbc14e?pvs=4
    fun getPost(categories: List<String> = emptyList()): Flow<PagingData<PostEntity>>

    suspend fun addPost(postEntity: PostEntity)

    suspend fun updateOrInsertPost(postEntity: PostEntity)

    suspend fun deletePost(postId: String)
}