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
__version__ = "1.0.1"

from argparse import ArgumentParser
from contextlib import suppress
from dataclasses import dataclass
from pathlib import Path
from subprocess import Popen
from time import asctime
from typing import IO, Any, Final

from tomllib import load


CONFIG_FILE: Final[Path] = Path("./runnerconfig.toml").resolve()


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

    @classmethod
    def from_raw(cls, raw: dict[str, Any]):
        stdout = StreamFile.from_raw(raw["stdout"])
        stderr = StreamFile.from_raw(raw["stderr"])

        return cls(
            raw["jar_path"],
            stdout,
            stderr,
        )


def load_config(environment: str) -> Config:
    with CONFIG_FILE.open("rb") as config_file:
        raw_configs = load(config_file)

    try:
        raw_environment_config = raw_configs[environment]
    except KeyError as e:
        raise ValueError(f"No environment '{environment}' in {CONFIG_FILE.name}") from e

    return Config.from_raw(raw_environment_config)


def log_runner(stream: IO[str], text: str):
    message = f"[RUNNER] {asctime()} -- {text}"
    print(message)
    print(message, file=stream)


def main():
    parser = ArgumentParser()
    parser.add_argument("environment", help=f"Environment from {CONFIG_FILE.name}")
    argv = parser.parse_args()

    config = load_config(argv.environment)

    with (
        config.stdout_file.path.open(config.stdout_file.mode, encoding="utf-8") as stdout,
        config.stderr_file.path.open(config.stderr_file.mode, encoding="utf-8") as stderr,
        Popen(
            ["java", "-jar", config.jar_path],
            stdout=stdout,
            stderr=stderr,
        ) as process,
    ):
        log_runner(stdout, f"Process with pid {process.pid} started in {argv.environment} environment.")

        with suppress(KeyboardInterrupt):
            process.wait()

        log_runner(stdout, "Runner exited")


if __name__ == "__main__":
    main()
