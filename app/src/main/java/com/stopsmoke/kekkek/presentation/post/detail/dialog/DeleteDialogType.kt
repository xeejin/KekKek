package com.stopsmoke.kekkek.presentation.post.detail.dialog

sealed class DeleteDialogType {
    data class CommentDeleteDialog(var commentId: String): DeleteDialogType()
    data class PostDeleteDialog(var postId: String): DeleteDialogType()
}