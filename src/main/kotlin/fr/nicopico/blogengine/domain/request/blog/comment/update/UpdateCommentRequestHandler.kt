package fr.nicopico.blogengine.domain.request.blog.comment.update

import fr.nicopico.blogengine.domain.entities.Comment
import fr.nicopico.blogengine.domain.repository.CommentRepository
import fr.nicopico.blogengine.domain.request.RequestHandler
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import javax.transaction.Transactional

@Component
@Transactional
class UpdateCommentRequestHandler(
    private val commentRepository: CommentRepository,
) : RequestHandler<UpdateCommentRequest, Comment> {

    override fun handle(request: UpdateCommentRequest): Comment {
        val comment = commentRepository.findByIdOrNull(request.commentId)
            ?: throw NoSuchElementException("Comment with ID ${request.commentId} not found")
        require(comment.post.id == request.postId) {
            "Comment ID ${request.commentId} is not associated with post ID ${request.postId}"
        }

        val updatedComment = Comment(
            id = comment.id,
            text = request.text,
            post = comment.post,
            contentMetadata = comment.contentMetadata,
        )

        return commentRepository.save(updatedComment)
    }
}
