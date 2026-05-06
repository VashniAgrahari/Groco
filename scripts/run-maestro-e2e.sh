#!/usr/bin/env bash
set -euo pipefail

MAESTRO_BIN="${HOME}/.maestro/bin/maestro"

if [[ ! -x "${MAESTRO_BIN}" ]]; then
  echo "Maestro CLI not found at ${MAESTRO_BIN}" >&2
  exit 1
fi

if ! command -v adb >/dev/null 2>&1; then
  echo "adb not found in PATH. Install Android platform-tools and ensure adb is available." >&2
  exit 1
fi

if ! adb get-state >/dev/null 2>&1; then
  echo "No connected Android device/emulator detected by adb." >&2
  exit 1
fi

"${MAESTRO_BIN}" test maestro/flows/full_e2e_suite.yaml
