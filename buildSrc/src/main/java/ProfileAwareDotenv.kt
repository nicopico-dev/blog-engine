import io.github.cdimascio.dotenv.Dotenv
import io.github.cdimascio.dotenv.DotenvEntry
import java.io.File

/**
 * [Dotenv] implementation where multiple profiles can be enabled at once.
 * The purpose is to mimic `fastlane --env` option (See [https://docs.fastlane.tools/advanced/other/])
 *
 * @param directory Directory were the dotenv files are located
 * @param baseEnvFile Name of the base dotenv file. This file will always be loaded first (default to '.env')
 * @param profiles List of activated profiles. Each profile correspond to a '.env.profile' file that will be loaded
 * following the order of the `profiles` list. A profile will be ignored if no corresponding file is found
 */
class ProfileAwareDotenv(
    directory: File,
    baseEnvFile: String = ".env",
    private val profiles: List<String> = emptyList(),
) : Dotenv {

    private val dotenvList: List<Dotenv> = listOf(
        Dotenv.configure()
            .directory(directory.absolutePath)
            .filename(baseEnvFile)
            .ignoreIfMalformed()    // Dotenv does not support multi-line values
            .load()
    ) + profiles.map { profile ->
        Dotenv.configure()
            .directory(directory.absolutePath)
            .filename(".env.$profile")
            .ignoreIfMissing()
            .ignoreIfMalformed()    // Dotenv does not support multi-line values
            .load()
    }

    override fun entries(): Set<DotenvEntry> {
        return dotenvList.flatMap { it.entries() }.toSet()
    }

    override fun entries(filter: Dotenv.Filter?): Set<DotenvEntry> {
        return dotenvList.flatMap { it.entries(filter) }.toSet()
    }

    override fun get(key: String): String {
        val value = dotenvList.map { it.get(key) }.lastOrNull { it != null }
        return if (value != null) value else {
            val profileList = if (profiles.isEmpty()) EMPTY_PROFILE_LIST_REPRESENTATION
            else profiles.joinToString(", ")
            throw IllegalStateException("Environment variable $key is not provided (profiles: $profileList)")
        }
    }

    override fun get(key: String, defaultValue: String): String {
        return dotenvList.map { it.get(key) }.lastOrNull { it != null }
            ?: defaultValue
    }

    companion object {
        private const val EMPTY_PROFILE_LIST_REPRESENTATION = "-- None --"
    }
}
