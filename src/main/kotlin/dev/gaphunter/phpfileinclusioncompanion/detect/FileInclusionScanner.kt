package dev.gaphunter.phpfileinclusioncompanion.detect

import dev.gaphunter.phpfileinclusioncompanion.model.FileInclusionHit

/**
 * Plain-text line scanner for a PHP file -- flags `include(`,
 * `include_once(`, `require(`, or `require_once(` whose path argument
 * directly references a PHP superglobal (`$_GET`, `$_POST`,
 * `$_REQUEST`, `$_COOKIE`). This is the textbook Local/Remote File
 * Inclusion (LFI/RFI) anti-pattern -- the PHP manual's own security
 * guidance and the wider PHP community consistently warn that "Local
 * File Inclusion and Remote File Inclusion vulnerabilities occur when
 * you use an input variable in the include statement without proper
 * input validation", allowing directory-traversal or (if
 * `allow_url_include` is enabled) execution of a remote attacker-
 * controlled file.
 *
 * Confirmed real gap: "PHP Inspections (EA Extended)" has a related
 * but distinct inspection -- it only flags a *relative* include path
 * that depends on the `include_path` configuration setting, not a
 * path built directly from a superglobal. Confirmed by reading its
 * own documentation before building this.
 *
 * **v0.1 scope, stated honestly:** deliberately narrow -- only flags
 * a superglobal referenced directly in the include/require argument,
 * the least ambiguous and highest-confidence shape. A path built from
 * an intermediate variable that was itself assigned from a
 * superglobal several lines earlier isn't traced (that would require
 * real data-flow analysis, out of scope for a text scanner).
 */
object FileInclusionScanner {

    // include/require are PHP language constructs, not functions -- `include $file;` (no parens) is valid
    // syntax alongside the more common `include($file);`, so the keyword match doesn't require `(`.
    private val INCLUDE_CALL = Regex("""\b(include_once|require_once|include|require)\b""")
    private val SUPERGLOBAL = Regex("\\\$_(GET|POST|REQUEST|COOKIE)\\b")

    fun scan(text: String): List<FileInclusionHit> {
        val hits = mutableListOf<FileInclusionHit>()
        text.lines().forEachIndexed { index, rawLine ->
            val trimmed = rawLine.trimStart()
            if (trimmed.startsWith("//") || trimmed.startsWith("#") || trimmed.startsWith("*")) return@forEachIndexed

            val callMatch = INCLUDE_CALL.find(rawLine) ?: return@forEachIndexed
            val afterCall = rawLine.substring(callMatch.range.last + 1)
            if (!SUPERGLOBAL.containsMatchIn(afterCall)) return@forEachIndexed

            hits += FileInclusionHit(callMatch.groupValues[1], index + 1, callMatch.range.first, callMatch.range.last + 1)
        }
        return hits
    }
}
