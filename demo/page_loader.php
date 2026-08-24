<?php
// Demo data for PHP File Inclusion Companion — used with
// `./gradlew runIde` to capture the real Marketplace screenshot. Open
// this file, the warning should appear on the include() line.

function load_page() {
    // Path built directly from a superglobal -- FLAGGED.
    include($_GET['page'] . '.php');
}

function load_page_safely() {
    $allowed = ['home', 'about', 'contact'];
    $page = $_GET['page'] ?? 'home';
    if (!in_array($page, $allowed, true)) {
        $page = 'home';
    }
    // Validated against a strict allowlist first -- NOT flagged
    // (the include argument here is a local variable, not a
    // superglobal reference).
    include($page . '.php');
}
