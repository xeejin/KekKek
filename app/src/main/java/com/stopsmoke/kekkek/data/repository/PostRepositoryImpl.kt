package com.stopsmoke.kekkek.data.repository

import androidx.paging.PagingData
import androidx.paging.map
import com.stopsmoke.kekkek.common.Result
import com.stopsmoke.kekkek.data.mapper.asExternalModel
import com.stopsmoke.kekkek.data.mapper.toEntity
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostCategory
import com.stopsmoke.kekkek.domain.model.PostWrite
import com.stopsmoke.kekkek.domain.model.User
import com.stopsmoke.kekkek.domain.model.Written
import com.stopsmoke.kekkek.domain.repository.PostRepository
import com.stopsmoke.kekkek.domain.repository.UserRepository
import com.stopsmoke.kekkek.firestore.dao.PostDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class PostRepositoryImpl @Inject constructor(
    private val postDao: PostDao,
    private val userRepository: UserRepository,
) : PostRepository {
    override fun getPost(category: PostCategory): Result<Flow<PagingData<Post>>> = try {
        val categoryString = when (category) {
            PostCategory.NOTICE -> "notice"
            PostCategory.QUIT_SMOKING_SUPPORT -> "quit_smoking_support"
            PostCategory.POPULAR -> "popular"
            PostCategory.QUIT_SMOKING_AIDS_REVIEWS -> "quit_smoking_aids_reviews"
            PostCategory.SUCCESS_STORIES -> "success_stories"
            PostCategory.GENERAL_DISCUSSION -> "general_discussion"
            PostCategory.FAILURE_STORIES -> "failure_stories"
            PostCategory.RESOLUTIONS -> "resolutions"
            PostCategory.UNKNOWN -> null
            PostCategory.ALL -> null
        }
        postDao.getPost(category = categoryString)
            .map { pagingData ->
                pagingData.map {
                    it.asExternalModel()
                }
            }
            .let {
                Result.Success(it)
            }

    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(e)
    }

    override fun getPost(uid: String): Result<Flow<PagingData<Post>>> {
        return try {
            postDao.getPostUserFilter(uid).map { pagingData ->
                pagingData.map {
                    it.asExternalModel()
                }
            }
                .let {
                    Result.Success(it)
                }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override fun getPostForWrittenUid(writtenUid: String): Result<Flow<PagingData<Post>>> = try {
        postDao.getPostForWrittenUid(writtenUid = writtenUid)
            .map { pagingData ->
                pagingData.map {
                    it.asExternalModel()
                }
            }.let {
                Result.Success(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(e)
    }

    override fun getBookmark(postIdList: List<String>): Result<Flow<PagingData<Post>>> = try {
        postDao.getBookmark(postIdList)
            .map { pagingData ->
                pagingData.map {
                    it.asExternalModel()
                }
            }.let {
                Result.Success(it)
            }
    } catch (e: Exception) {
        e.printStackTrace()
        Result.Error(e)
    }

    override fun getPostItem(postId: String): Result<Flow<List<Post>>> {
        return try {
            postDao.getPostItem(postId)
                .map {
                    it.map { it.asExternalModel() }
                }
                .let {
                    Result.Success(it)
                }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun addPost(post: PostWrite): Result<Unit> =
        try {
            val user = (userRepository.getUserData().first() as User.Registered)
            val written = Written(
                uid = user.uid,
                name = user.name,
                profileImage = user.profileImage,
                ranking = user.ranking
            )
            postDao.addPost(post.toEntity(written))
            Result.Success(Unit)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.Error(e)
        }

    override suspend fun deletePost(postId: String): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun editPost(post: PostWrite): Result<Unit> {
        TODO("Not yet implemented")
    }

    override suspend fun getTopPopularItems(): List<Post> =
        postDao.getPopularPostItems().map {
            it.asExternalModel()
        }

    override suspend fun getTopNotice(): Post =
        postDao.getTopNotice().asExternalModel()

    override suspend fun getPopularPostList(): List<Post> =
        postDao.getPopularPostList().map {
            it.asExternalModel()
        }

    override suspend fun getPostForPostId(postId: String): Post {
        return postDao.getPostForPostId(postId).asExternalModel()

    }

    override suspend fun addViews(postId: String): Result<Unit> {
        return postDao.addViews(postId)
    }

    override suspend fun addLikeToPost(postId: String): Result<Unit> {
        return try {
            val user = (userRepository.getUserData().first() as User.Registered)
            postDao.addLike(postId, user.uid)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun deleteLikeToPost(postId: String): Result<Unit> {
        return try {
            val user = (userRepository.getUserData().first() as User.Registered)
            postDao.deleteLike(postId, user.uid)
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
}