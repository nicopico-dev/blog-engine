package fr.nicopico.blogengine.domain.request.blog.comment.create

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.entities.Comment
import fr.nicopico.blogengine.domain.entities.ContentMetadata
import fr.nicopico.blogengine.domain.entities.Email
import fr.nicopico.blogengine.domain.repository.AuthorRepository
import fr.nicopico.blogengine.domain.repository.CommentRepository
import fr.nicopico.blogengine.domain.repository.PostRepository
import fr.nicopico.blogengine.domain.request.RequestHandler
import fr.nicopico.blogengine.infra.logInfo
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Component
import java.time.Instant
import javax.transaction.Transactional

@Component
@Transactional
class CreateCommentRequestHandler(
    private val commentRepository: CommentRepository,
    private val postRepository: PostRepository,
    private val authorRepository: AuthorRepository,
) : RequestHandler<CreateCommentRequest, Comment> {

    override fun handle(request: CreateCommentRequest): Comment {
        val post = postRepository.findByIdOrNull(request.postId)
            ?: throw NoSuchElementException("No post found with ID ${request.postId}")

        // Commenter do not have to create an account first
        val author = getOrCreateAuthor(request.authorEmail)

        val comment = Comment(
            text = request.text,
            post = post,
            contentMetadata = ContentMetadata(
                author = author,
                creationDate = Instant.now()
            )
        )

        return commentRepository.save(comment).also {
            logInfo { "Created comment $it" }
        }
    }

    private fun getOrCreateAuthor(authorEmail: Email): Author {
        var author = authorRepository.findByEmail(authorEmail)
        if (author == null) {
            author = Author(email = authorEmail)
            authorRepository.save(author)
        }
        return author
    }
}
