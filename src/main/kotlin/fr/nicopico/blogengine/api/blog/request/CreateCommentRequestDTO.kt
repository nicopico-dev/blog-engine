package fr.nicopico.blogengine.api.blog.request

import fr.nicopico.blogengine.domain.entities.Email
import fr.nicopico.blogengine.domain.request.blog.comment.create.CreateCommentRequest

data class CreateCommentRequestDTO(
    val text: String,
    val authorEmail: Email
)

fun CreateCommentRequestDTO.toRequest(postId: Long) = CreateCommentRequest(
    postId = postId,
    text = text,
    authorEmail = authorEmail
)
