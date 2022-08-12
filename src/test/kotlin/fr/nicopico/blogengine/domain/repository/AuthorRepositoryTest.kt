package fr.nicopico.blogengine.domain.repository

import fr.nicopico.blogengine.domain.entities.Author
import fr.nicopico.blogengine.domain.entities.Email
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager


@DataJpaTest
class AuthorRepositoryTest {

    @Autowired
    private lateinit var entityManager: TestEntityManager

    @Autowired
    private lateinit var authorRepository: AuthorRepository

    @Test
    fun `Saving a new author will generate a unique ID`() {
        val newAuthor = Author(name = "World", email = Email("other@email.com"))
        val savedAuthor = authorRepository.save(newAuthor)

        assertThat(savedAuthor)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(newAuthor)

        assertThat(savedAuthor.id).isNotNull
    }

    @Test
    fun `Saving an existing author will keep the current ID`() {
        val author = Author(name = "Hello", email = Email("some@email.com"))
        val authorId: Long = entityManager.persistAndGetId(author) as Long

        val updatedAuthor = Author(id = authorId, name = "World", email = author.email)
        val savedAuthor = authorRepository.save(updatedAuthor)

        assertThat(savedAuthor)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(updatedAuthor)

        assertThat(savedAuthor.id).isEqualTo(authorId)
    }

    @Test
    fun `findByEmail will retrieve an author by its email`() {
        val author = Author(name = "Hello", email = Email("some@email.com"))
        entityManager.persistAndFlush(author)

        val found = authorRepository.findByEmail(author.email)

        assertThat(found).isNotNull
        assertThat(found)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(author)
    }

    @Test
    fun `findByEmail will return null if the email is not found`() {
        val author = Author(name = "Hello", email = Email("some@email.com"))
        entityManager.persistAndFlush(author)

        val found = authorRepository.findByEmail(Email("other@email.com"))

        assertThat(found).isNull()
    }
}
