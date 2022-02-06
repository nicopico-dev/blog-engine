package fr.nicopico.blogengine.domain.request.blog.comment

import fr.nicopico.blogengine.domain.entities.Comment
import fr.nicopico.blogengine.domain.repository.CommentRepository
import fr.nicopico.blogengine.domain.request.blog.post.PostQueries
import org.springframework.stereotype.Component

@Component
class CommentQueries(
    private val commentRepository: CommentRepository,
    private val postQueries: PostQueries
) {
    fun findAllByPostId(postId: Long): Iterable<Comment> {
        postQueries.checkPostExists(postId)
        return commentRepository.findAllByPostId(postId)
    }
}
