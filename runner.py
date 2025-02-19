"""
MIT License

Copyright (c) 2024 v01d

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
"""

__author__ = "v01d"
__version__ = "1.2.0"

from argparse import ArgumentParser
from dataclasses import dataclass
from logging import INFO, basicConfig, getLogger
from pathlib import Path
from subprocess import Popen
from tomllib import load
from typing import Any, Final


CONFIG_FILE: Final[Path] = Path("./runnerconfig.toml").resolve()
LOG_FILE: Final[Path] = Path("./runner.log").resolve()
LOG_FORMAT: Final[str] = "%(asctime)s - %(name)s - %(levelname)s - %(funcName)s - %(message)s"
LOG_LEVEL: Final[int] = INFO


basicConfig(
    filename=LOG_FILE,
    filemode="a",
    format=LOG_FORMAT,
    level=LOG_LEVEL,
)

logger = getLogger("RUNNER")


@dataclass
class StreamFile:
    path: Path
    mode: str

    @classmethod
    def from_raw(cls, raw: dict[str, Any]):
        return cls(Path(raw["path"]), raw["mode"])


@dataclass
class Config:
    jar_path: str
    stdout_file: StreamFile
    stderr_file: StreamFile
    correct_exit_codes: frozenset[int]

    @classmethod
    def from_raw(cls, raw: dict[str, Any]):
        stdout = StreamFile.from_raw(raw["stdout"])
        stderr = StreamFile.from_raw(raw["stderr"])

        return cls(
            raw["jar_path"],
            stdout,
            stderr,
            frozenset(raw["correct_exit_codes"]),
        )


def load_config(environment: str) -> Config:
    with CONFIG_FILE.open("rb") as config_file:
        raw_configs = load(config_file)

    try:
        raw_environment_config = raw_configs[environment]
    except KeyError as e:
        raise ValueError(f"No environment '{environment}' in {CONFIG_FILE.name}") from e

    return Config.from_raw(raw_environment_config)


def wait(process: Popen) -> int:
    try:
        exit_code = process.wait()
    except KeyboardInterrupt:
        logger.info(f"Runner interrupted (Ctrl+C)")
        return 0
    else:
        message = f"Process exited with code {exit_code}"

        if exit_code:
            logger.error(message)
        else:
            logger.info(message)

        return exit_code


def run_instance(config: Config, environment: str) -> int:
    with (
        config.stdout_file.path.open(config.stdout_file.mode, encoding="utf-8") as stdout,
        config.stderr_file.path.open(config.stderr_file.mode, encoding="utf-8") as stderr,
        Popen(
            ["java", "-jar", config.jar_path],
            stdout=stdout,
            stderr=stderr,
        ) as process,
    ):
        logger.info(f"Process with pid {process.pid} started in {environment} environment.")

        exit_code = wait(process)

        return exit_code


def main():
    parser = ArgumentParser()
    parser.add_argument("environment", help=f"Environment from {CONFIG_FILE.name}")
    argv = parser.parse_args()

    config = load_config(argv.environment)

    while True:
        exit_code = run_instance(config, argv.environment)

        if exit_code in config.correct_exit_codes:
            break


if __name__ == "__main__":
    main()
