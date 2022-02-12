import com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import org.junit.Test
import java.io.File

class ProfileAwareDotenvTest {

    @Test
    fun `should retrieve values from default dotenv file`() {
        // Given
        val dotenv = ProfileAwareDotenv(getEnvDir())

        // Then
        dotenv[FOO] shouldBe "FOO_DEFAULT"
        dotenv[BAR] shouldBe "BAR_DEFAULT"
    }

    @Test
    fun `should retrieve values from dotenv default and profile files`() {
        // Given
        val dotenv = ProfileAwareDotenv(getEnvDir(), profiles = listOf("profileA"))

        // Then
        dotenv[FOO] shouldBe "FOO_A"
        dotenv[BAR] shouldBe "BAR_DEFAULT"
    }

    @Test
    fun `should retrieve values from default and profile files in the provided order (AB)`() {
        // Given
        val dotenv = ProfileAwareDotenv(getEnvDir(), profiles = listOf("profileA", "profileB"))

        // Then
        dotenv[FOO] shouldBe "FOO_B"
        dotenv[BAR] shouldBe "BAR_DEFAULT"
    }

    @Test
    fun `should retrieve values from default and profile files in the provided order (BA)`() {
        // Given
        val dotenv = ProfileAwareDotenv(getEnvDir(), profiles = listOf("profileB", "profileA"))

        // Then
        dotenv[FOO] shouldBe "FOO_A"
        dotenv[BAR] shouldBe "BAR_DEFAULT"
    }

    @Test
    fun `should override default values with environment variables`() {
        withEnvironmentVariable(BAR, "BAR_SYSTEM")
            .execute {
                // Given
                val dotenv = ProfileAwareDotenv(getEnvDir())

                // Then
                dotenv[FOO] shouldBe "FOO_DEFAULT"
                dotenv[BAR] shouldBe "BAR_SYSTEM"
            }
    }

    @Test
    fun `should override profile values with environment variables`() {
        withEnvironmentVariable(BAR, "BAR_SYSTEM")
            .execute {
                // Given
                val dotenv = ProfileAwareDotenv(getEnvDir(), profiles = listOf("profileA"))

                // Then
                dotenv[FOO] shouldBe "FOO_A"
                dotenv[BAR] shouldBe "BAR_SYSTEM"
            }
    }

    @Test
    fun `should throw an error for unknown keys - default profile only`() {
        val dotenv = ProfileAwareDotenv(getEnvDir())

        val key = "UNKNOWN_KEY"
        val exception = shouldThrow<IllegalStateException> {
            dotenv[key]
        }

        exception.message shouldBe "Environment variable $key is not provided (profiles: -- None --)"
    }

    @Test
    fun `should throw an error for unknown keys - profiles`() {
        val dotenv = ProfileAwareDotenv(getEnvDir(), profiles = listOf("profileA", "profileB"))

        val key = "UNKNOWN_KEY"
        val exception = shouldThrow<IllegalStateException> {
            dotenv[key]
        }

        exception.message shouldBe "Environment variable $key is not provided (profiles: profileA, profileB)"
    }

    @Test
    fun `should not throw an error for unknown keys if a default value is provided`() {
        val dotenv = ProfileAwareDotenv(getEnvDir(), profiles = listOf("profileA", "profileB"))

        val key = "UNKNOWN_KEY"
        val defaultValue = "VALUE"
        dotenv[key, defaultValue] shouldBe defaultValue
    }

    companion object {
        private val fixturesFolder = File("src/test/resources/fixtures")
        private const val FOO = "foo"
        private const val BAR = "bar"

        private fun getEnvDir(fixtureName: String = "default") = File(fixturesFolder, fixtureName)
    }
}
