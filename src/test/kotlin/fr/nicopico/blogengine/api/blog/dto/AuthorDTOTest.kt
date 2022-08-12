package fr.nicopico.blogengine.api.blog.dto

import fr.nicopico.blogengine.api.blog.author.dto.AuthorDTO
import fr.nicopico.blogengine.domain.entities.Email
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Assertions.assertAll
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.json.JsonTest
import org.springframework.boot.test.json.JacksonTester

@JsonTest
internal class AuthorDTOTest {

    @Autowired
    private lateinit var jacksonTester: JacksonTester<AuthorDTO>

    @Test
    fun `Serialize named author to JSON`() {
        val author = AuthorDTO(12, "Elvis", Email("elvis@mail.co"))

        val json = jacksonTester.write(author)

        assertAll(
            "json",
            { assertThat(json).extractingJsonPathValue("$.id").isEqualTo(12) },
            { assertThat(json).extractingJsonPathValue("$.name").isEqualTo("Elvis") },
            { assertThat(json).extractingJsonPathValue("$.email").isEqualTo("elvis@mail.co") },
        )
    }

    @Test
    fun `Serialize anonymous author to JSON`() {
        val author = AuthorDTO(32, null, Email("someone@mail.co"))

        val json = jacksonTester.write(author)

        assertAll(
            "json",
            { assertThat(json).extractingJsonPathValue("$.id").isEqualTo(32) },
            { assertThat(json).hasEmptyJsonPathValue("$.name") },
            { assertThat(json).extractingJsonPathValue("$.email").isEqualTo("someone@mail.co") },
        )
    }

    @Test
    fun `Deserialize named author from JSON`() {
        val json = """{
          "id": 32,
          "name": "Barbie",
          "email": "barbie@mattel.co"
        }""".trimIndent()
        val expected = AuthorDTO(32, "Barbie", Email("barbie@mattel.co"))

        val author: AuthorDTO = jacksonTester.parseObject(json)

        assertThat(author).isEqualTo(expected)
    }

    @Test
    fun `Deserialize anonymous author from JSON`() {
        val json = """{
          "id": 33,
          "email": "ken@mattel.co"
        }""".trimIndent()
        val expected = AuthorDTO(33, null, Email("ken@mattel.co"))

        val author: AuthorDTO = jacksonTester.parseObject(json)

        assertThat(author).isEqualTo(expected)
    }
}
