# Demo data — PHP File Inclusion Companion

For capturing the real Marketplace screenshot:

1. `./gradlew runIde`
2. Open `demo/page_loader.php` as a scratch/standalone file (or drop
   it into any sandbox project) inside the sandbox IDE.
3. The `include($_GET['page'] . '.php')` call inside `load_page` shows
   the warning — hover it for the tooltip. `load_page_safely`'s
   allowlist-validated local variable stays clean, for contrast.
4. Enter Full Screen (`View > Appearance > Enter Full Screen`), capture
   with `Win+Shift+S`, save directly to `docs/screenshots/` in this
   repo.
