package fr.nicopico.blogengine.domain.request.blog.comment.create

import fr.nicopico.blogengine.domain.entities.Email

data class CreateCommentRequest(
    val postId: Long,
    val text: String,
    val authorEmail: Email
)
