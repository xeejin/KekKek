package com.stopsmoke.kekkek.presentation.community

import com.stopsmoke.kekkek.domain.model.DateTime
import com.stopsmoke.kekkek.domain.model.ElapsedDateTime

data class CommunityWritingItem(
    val userInfo: UserInfo,
    val postInfo: PostInfo,
    val postImage: String,
    val post: String,
    val postTime: ElapsedDateTime,
)

data class CommunityPopularItem(
    val postInfo1: PostInfo,
    val postInfo2: PostInfo
)

data class PostInfo(
    val title: String,
    val postType: String,
    val view: Long,
    val like: Long,
    val comment: Long
)

data class UserInfo(
    val name: String,
    val rank: Long,
    val profileImage: String
)

