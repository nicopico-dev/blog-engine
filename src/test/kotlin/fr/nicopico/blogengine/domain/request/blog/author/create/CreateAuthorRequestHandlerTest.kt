package fr.nicopico.blogengine.domain.request.blog.author.create

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.repository.AuthorRepository
import fr.nicopico.blogengine.test.anyEmail
import fr.nicopico.blogengine.test.anyName
import fr.nicopico.blogengine.test.checkAllBlocking
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll

internal class CreateAuthorRequestHandlerTest {

    private lateinit var handler: CreateAuthorRequestHandler

    @MockK
    private lateinit var authorRepository: AuthorRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)

        handler = CreateAuthorRequestHandler(authorRepository)
    }

    @Test
    fun `Created author will be saved to database`() {
        checkAllBlocking(anyName(), anyEmail()) { name, email ->
            val authorSlot = slot<Author>()
            val expectedAuthor = mockk<Author>()

            every { authorRepository.save(capture(authorSlot)) } returns expectedAuthor

            val request = CreateAuthorRequest(name = name, email = email)
            val author = handler.handle(request)

            val capturedAuthor = authorSlot.captured
            assertAll(
                "capturedAuthor",
                { assertThat(capturedAuthor.name).isEqualTo(name) },
                { assertThat(capturedAuthor.email).isEqualTo(email) },
            )

            assertThat(author).isSameAs(expectedAuthor)
        }
    }
}
