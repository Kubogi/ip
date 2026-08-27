"""Run the black-box cases in test/ui-test-plan.md against Miku."""

from pathlib import Path
import re
import subprocess
import sys


PLAN = Path("test/ui-test-plan.md")
CASE_PATTERN = re.compile(
    r"## Test case: (?P<name>.+?)\n.*?### Inputs\n```text\n(?P<input>.*?)\n```"
    r"\n\n### Expected output\n```text\n(?P<expected>.*?)\n```", re.DOTALL)


def main() -> int:
    """Run every planned case separately and stop at the first mismatch."""
    sys.stdout.reconfigure(encoding="utf-8")
    cases = list(CASE_PATTERN.finditer(PLAN.read_text(encoding="utf-8")))
    if not cases:
        print("No UI test cases found.")
        return 1
    for case in cases:
        console_input = case["input"] + "\n"
        expected = case["expected"].rstrip("\n")
        result = subprocess.run(["java", "-cp", "out", "Miku"], input=console_input,
                                text=True, encoding="utf-8", capture_output=True, check=False)
        actual = result.stdout.rstrip("\n")
        print(f"Test case: {case['name']}\nConsole input:\n{console_input}Actual output:\n{actual}\nExpected output:\n{expected}")
        if result.returncode != 0 or actual != expected:
            print(f"FAILED (exit code {result.returncode})")
            return 1
        print("PASSED\n")
    return 0


if __name__ == "__main__":
    sys.exit(main())
