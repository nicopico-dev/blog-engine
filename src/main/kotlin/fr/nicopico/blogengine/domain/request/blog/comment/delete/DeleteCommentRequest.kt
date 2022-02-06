package fr.nicopico.blogengine.domain.request.blog.comment.delete

data class DeleteCommentRequest(
    val postId: Long,
    val commentId: Long,
)
