package dev.gaphunter.phpfileinclusioncompanion.detect

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class FileInclusionScannerTest {

    @Test
    fun `flags include with a GET superglobal path`() {
        val code = "include(\$_GET['page'] . '.php');"
        val hits = FileInclusionScanner.scan(code)
        assertEquals(1, hits.size)
        assertEquals("include", hits[0].callText)
    }

    @Test
    fun `flags require with a POST superglobal path, no parens`() {
        val code = "require \$_POST['tpl'];"
        val hits = FileInclusionScanner.scan(code)
        assertEquals(1, hits.size)
        assertEquals("require", hits[0].callText)
    }

    @Test
    fun `flags include_once with a REQUEST superglobal path`() {
        val code = "include_once(\$_REQUEST['module']);"
        val hits = FileInclusionScanner.scan(code)
        assertEquals(1, hits.size)
        assertEquals("include_once", hits[0].callText)
    }

    @Test
    fun `does not flag include with a static literal path`() {
        val code = "include('config.php');"
        assertTrue(FileInclusionScanner.scan(code).isEmpty())
    }

    @Test
    fun `does not flag include with a local variable path`() {
        val code = "include(\$templatePath);"
        assertTrue(FileInclusionScanner.scan(code).isEmpty())
    }

    @Test
    fun `does not flag a commented-out line`() {
        val code = "// include(\$_GET['page']);"
        assertTrue(FileInclusionScanner.scan(code).isEmpty())
    }
}
