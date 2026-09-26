<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# PHP File Inclusion Companion Changelog

## [Unreleased]

## [0.1.1]

### Fixed

- Review/star CTA now links to this plugin's own Marketplace
  reviews page instead of the vendor's generic plugin list.

## [0.1.0]

### Added

- Warning on `include`/`include_once`/`require`/`require_once` whose
  path argument directly references a PHP superglobal -- a
  Local/Remote File Inclusion vulnerability (CWE-98), not covered by
  "PHP Inspections (EA Extended)".
- 100% static text analysis, no PHP plugin dependency, no network
  calls, no telemetry. Free.

[Unreleased]: https://github.com/GapHunterLabs/php-file-inclusion-companion/compare/0.1.1...HEAD
[0.1.1]: https://github.com/GapHunterLabs/php-file-inclusion-companion/compare/0.1.0...0.1.1
[0.1.0]: https://github.com/GapHunterLabs/php-file-inclusion-companion/commits/0.1.0
