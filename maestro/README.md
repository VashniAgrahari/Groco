# Maestro E2E Suite

## Prerequisites
- Maestro CLI installed (`~/.maestro/bin/maestro`).
- Android SDK + `adb` available in PATH.
- Running emulator or connected Android device.
- Debug APK installed for app id `com.vashuag.grocery`.

## Run full suite
```bash
./scripts/run-maestro-e2e.sh
```

## Run individual flows
```bash
$HOME/.maestro/bin/maestro test maestro/flows/inventory_seed_and_cleanup.yaml
$HOME/.maestro/bin/maestro test maestro/flows/navigation_scan_smoke.yaml
```

## Notes
- Flows depend on debug-only controls (`Load Demo Data`, `Clear All`) added for deterministic E2E setup.
- `navigation_scan_smoke` validates route and scan screen rendering; it does not perform live camera object capture.
