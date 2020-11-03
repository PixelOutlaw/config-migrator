package com.tealcube.minecraft.bukkit.mythicdrops.config.migration

/**
 * Version number in [Semantic Versioning 2.0.0](http://semver.org/spec/v2.0.0.html) specification (SemVer).
 *
 * Modified version of the SemVer class from the SemVer library by swiftzer.
 *
 * @property major major version, increment it when you make incompatible API changes.
 * @property minor minor version, increment it when you add functionality in a backwards-compatible manner.
 * @property patch patch version, increment it when you make backwards-compatible bug fixes.
 * @property preRelease pre-release version.
 * @property buildMetadata build metadata.
 */
data class SemVer(
    val major: Int = 0,
    val minor: Int = 0,
    val patch: Int = 0,
    val preRelease: String? = null,
    val buildMetadata: String? = null
) {
    companion object {
        private const val MAJOR_INDEX = 1
        private const val MINOR_INDEX = 2
        private const val PATCH_INDEX = 3
        private const val PRE_RELEASE_INDEX = 4
        private const val BUILD_METADATA_INDEX = 5
        private val buildMetadataRegex = """[\dA-z\-]+(?:\.[\dA-z\-]+)*""".toRegex()
        private val preReleaseRegex = """[\dA-z\-]+(?:\.[\dA-z\-]+)*""".toRegex()
        private val semanticVersionRegex =
            """(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:\.)?(0|[1-9]\d*)?(?:-([\dA-z\-]+(?:\.[\dA-z\-]+)*))?(?:\+([\dA-z\-]+(?:\.[\dA-z\-]+)*))?""".toRegex()

        /**
         * Parse the version string to [SemVer] data object.
         * @param version version string.
         * @throws IllegalArgumentException if the version is not valid.
         */
        fun parse(version: String): SemVer {
            val result = semanticVersionRegex.matchEntire(version)
            require(result != null) { "version is not a valid semantic version: $version" }
            return SemVer(
                major = getIntFromResult(result.groupValues, MAJOR_INDEX),
                minor = getIntFromResult(result.groupValues, MINOR_INDEX),
                patch = getIntFromResult(result.groupValues, PATCH_INDEX),
                preRelease = getStringFromResult(result.groupValues, PRE_RELEASE_INDEX),
                buildMetadata = getStringFromResult(result.groupValues, BUILD_METADATA_INDEX)
            )
        }

        private fun getIntFromResult(list: List<String>, idx: Int, def: Int = 0): Int {
            return if (list[idx].isEmpty()) def else list[idx].toInt()
        }

        private fun getStringFromResult(list: List<String>, idx: Int, def: String? = null): String? {
            return if (list[idx].isEmpty()) def else list[idx]
        }
    }

    init {
        require(major >= 0) { "Major version must be a positive integer" }
        require(minor >= 0) { "Minor version must be a positive integer" }
        require(patch >= 0) { "Patch version must be a positive integer" }
        preRelease?.let {
            require(it.matches(preReleaseRegex)) { "Pre-release version is not valid" }
        }
        buildMetadata?.let {
            require(it.matches(buildMetadataRegex)) { "Build metadata is not valid" }
        }
    }

    override fun toString(): String = buildString {
        append("$major.$minor.$patch")
        preRelease?.let { append("-$it") }
        buildMetadata?.let { append("+$it") }
    }
}
