package fr.nicopico.blogengine.domain.request.blog.post.create

import fr.nicopico.blogengine.domain.entities.ContentMetadata
import fr.nicopico.blogengine.domain.entities.Post
import fr.nicopico.blogengine.domain.repository.AuthorRepository
import fr.nicopico.blogengine.domain.repository.PostRepository
import fr.nicopico.blogengine.domain.request.RequestHandler
import fr.nicopico.blogengine.infra.logDebug
import fr.nicopico.blogengine.infra.logInfo
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import java.time.Instant
import javax.transaction.Transactional

@Component
@Transactional
class CreatePostRequestHandler(
    private val postRepository: PostRepository,
    private val authorRepository: AuthorRepository,
) : RequestHandler<CreatePostRequest, Post> {

    override fun handle(request: CreatePostRequest): Post {
        val author = authorRepository.findByEmail(request.authorEmail) ?:
            throw NoSuchElementException("No author found with address ${request.authorEmail}")

        val post = Post(
            title = request.title,
            content = request.content,
            contentMetadata = ContentMetadata(
                author = author,
                creationDate = Instant.now()
            ),
        )

        return postRepository.save(post).also {
            logInfo { "Created Post $it" }
        }
    }
}
