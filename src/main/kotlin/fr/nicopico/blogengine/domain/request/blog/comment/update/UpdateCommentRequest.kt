package fr.nicopico.blogengine.domain.request.blog.comment.update

data class UpdateCommentRequest(
    val postId: Long,
    val commentId: Long,
    val text: String,
)
