package com.stopsmoke.kekkek.presentation.post

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stopsmoke.kekkek.domain.model.Post
import com.stopsmoke.kekkek.domain.model.PostEdit
import com.stopsmoke.kekkek.domain.model.toPostCategory
import com.stopsmoke.kekkek.domain.repository.PostRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostWriteViewModel @Inject constructor(
    private val postRepository: PostRepository
) : ViewModel() {

    private val _post = MutableStateFlow<Post?>(null)
    val post get() = _post.asStateFlow()

    private val _myText = MutableLiveData<String>()
    val myText: LiveData<String> = _myText

    fun setString(text: String) {
        _myText.value = text
    }

    fun addPost(post: PostEdit) {
        viewModelScope.launch {
            postRepository.addPost(post)
        }
    }

    fun editPost(postEdit: PostEdit) {
        viewModelScope.launch {
            val editPost = post.value?.copy(
                title = postEdit.title,
                text = postEdit.text,
                dateTime =  postEdit.dateTime,
                category = postEdit.category.toPostCategory()
            )

            editPost?.let { postRepository.editPost(editPost) }
        }
    }

    fun updatePostId(postId:String) = viewModelScope.launch{
        val post = postRepository.getPostForPostId(postId)
        _post.emit(post)
    }
}