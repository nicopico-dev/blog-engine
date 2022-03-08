package fr.nicopico.blogengine.domain.request.blog.comment.delete

import fr.nicopico.blogengine.domain.repository.CommentRepository
import fr.nicopico.blogengine.domain.request.CommandHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Transactional
class DeleteCommentRequestHandler(
    private val commentRepository: CommentRepository,
) : CommandHandler<DeleteCommentRequest> {
    override fun handle(request: DeleteCommentRequest) {
        val comment = commentRepository.findByIdOrNull(request.commentId)
            ?: throw NoSuchElementException("Comment with ID ${request.commentId} not found")
        require(comment.post.id == request.postId) {
            "Comment ID ${request.commentId} is not associated with post ID ${request.postId}"
        }

        commentRepository.delete(comment)
    }
}
