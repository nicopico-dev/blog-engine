package fr.nicopico.blogengine.domain.request.blog.author

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.repository.AuthorRepository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import io.mockk.slot
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatExceptionOfType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull
import kotlin.random.Random

internal class AuthorQueriesTest {

    private lateinit var queries: AuthorQueries

    @MockK
    private lateinit var authorRepository: AuthorRepository

    @BeforeEach
    internal fun setUp() {
        MockKAnnotations.init(this)

        queries = AuthorQueries(authorRepository)
    }

    @Test
    fun `findAll should return all known authors`() {
        val expected = mockk<Iterable<Author>>()
        every { authorRepository.findAll() } returns expected

        val authors = queries.findAll()

        assertThat(authors).isEqualTo(expected)
    }

    @Test
    fun `getById should return the corresponding author`() {
        val authorIdSlot = slot<Long>()
        val expected = mockk<Author>()
        every { authorRepository.findByIdOrNull(capture(authorIdSlot)) } returns expected

        val authorId = Random.nextLong(100)
        val author = queries.getById(authorId)

        assertThat(author).isEqualTo(expected)
        assertThat(authorIdSlot.captured).isEqualTo(authorId)
    }

    @Test
    fun `getById should throw if the author does not exist`() {
        every { authorRepository.findByIdOrNull(any()) } returns null

        val authorId = Random.nextLong(100)

        assertThatExceptionOfType(NoSuchElementException::class.java)
            .isThrownBy { queries.getById(authorId) }
            .withMessage("No author found with ID $authorId")
    }
}
