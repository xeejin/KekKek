package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.firestore.dao.BookmarkRepository
import com.stopsmoke.kekkek.firestore.dao.PostDao
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class BookmarkRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val userRepository: UserRepository,
) : BookmarkRepository {
    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getBookmarkItems(): Flow<PagingData<Post>> =
        userRepository.getUserData()
            .flatMapLatest { user ->
                postDao.getBookmarkItems((user as User.Registered).uid)
                    .map { pagingData ->
                        pagingData.map { it.asExternalModel() }
                    }

            }
}