package fr.nicopico.blogengine.domain.repository

import fr.nicopico.blogengine.domain.entities.Comment
import org.springframework.data.repository.CrudRepository

interface CommentRepository : CrudRepository<Comment, Long> {
    fun findAllByPostId(postId: Long): Iterable<Comment>
}
