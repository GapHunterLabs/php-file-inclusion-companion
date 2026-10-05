# PHP File Inclusion Companion

Warning on `include`, `include_once`, `require`, or `require_once`
whose path argument directly references a PHP superglobal (`$_GET`,
`$_POST`, `$_REQUEST`, `$_COOKIE`). This is the textbook Local/Remote
File Inclusion (LFI/RFI) anti-pattern — an attacker can point this at
an arbitrary file via directory traversal, or a remote URL if
`allow_url_include` is on.

Confirmed real gap: "PHP Inspections (EA Extended)" has a related but
distinct inspection — it only flags a *relative* include path that
depends on the `include_path` configuration setting, not a path built
directly from a superglobal. Confirmed by reading its own
documentation before building this.

## Why it exists

```php
include($_GET['page'] . '.php');
```

compiles and runs fine — until someone requests `?page=../../../../etc/passwd%00`
(or, with `allow_url_include` on, a remote attacker-hosted PHP file),
at which point it's a full file inclusion vulnerability.

## Why built this way

- **100% static text analysis** — a regex-based line scanner, not a
  real PHP parser, so it works whether the PHP plugin is installed or
  not.

## v0.1 scope — stated honestly, not exhaustively

Deliberately narrow — only flags a superglobal referenced directly in
the include/require argument, the least ambiguous and highest-
confidence shape. A path built from an intermediate variable that was
itself assigned from a superglobal several lines earlier isn't traced
(real data-flow analysis, out of scope for a text scanner).

## Usage

Open any `.php` file. An `include`/`include_once`/`require`/
`require_once` whose path is built from a superglobal shows a
warning.

## Support

- **Bugs and feature requests:** [GitHub Issues](https://github.com/GapHunterLabs/php-file-inclusion-companion/issues)
- **Questions, or custom rules for a team's codebase:** **gaphunterlabs@gmail.com**
- **Security vulnerabilities:** report privately as described in [SECURITY.md](SECURITY.md), not in a public issue.
- **Privacy and network behavior:** [PRIVACY.md](PRIVACY.md)

## Development

```
./gradlew test           # unit tests
./gradlew buildPlugin    # generates build/distributions/*.zip
./gradlew verifyPlugin   # checks compatibility against real IDEs
```

## License

Apache-2.0. See `LICENSE`.
