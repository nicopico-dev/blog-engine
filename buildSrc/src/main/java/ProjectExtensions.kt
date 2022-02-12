import io.github.cdimascio.dotenv.Dotenv
import org.gradle.api.Project

fun Project.getDotenv(): Dotenv {
    val envProperty = findProperty("env") as? String
    val profiles = envProperty?.split(",") ?: listOf()
    return ProfileAwareDotenv(rootDir, profiles = profiles)
}
