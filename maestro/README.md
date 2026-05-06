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
$HOME/.maestro/bin/maestro test maestro/flows/quantity_management_flow.yaml
$HOME/.maestro/bin/maestro test maestro/flows/location_settings_flow.yaml
$HOME/.maestro/bin/maestro test maestro/flows/compare_prices_flow.yaml
```

## Notes
- Flows depend on debug-only controls (`Load Demo Data`, `Clear All`) added for deterministic E2E setup.
- `navigation_scan_smoke` validates route and scan screen rendering; it does not perform live camera object capture.
- `quantity_management_flow` validates on-device quantity increment/decrement behavior.
- `location_settings_flow` validates one-time location config save/edit.
- `compare_prices_flow` validates inline compare bottom-sheet interaction and result rendering (falls back to deterministic demo result in debug if live providers fail).
