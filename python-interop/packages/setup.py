#!/usr/bin/python

import os
import platform
from setuptools import setup, Extension

LIB_NAME = "libdatalore_plot_python_interop"
MACOS_LIB_NAME = LIB_NAME + ".dylib"
LINUX_LIB_NAME = LIB_NAME + ".so"

build_paths = {
    "Linux": "../build/bin/linuxX64/debugShared",
    "Darwin": "../build/bin/macosX64/debugShared"
}


def detect_platform():
    platform_type = platform.system()
    build_path = build_paths.get(platform_type, None)
    assert build_paths is not None, "Invalid platform: " + platform_type
    if platform_type == "Darwin":
        os.rename(build_path + "/" + MACOS_LIB_NAME, build_path + "/" + LINUX_LIB_NAME)

    return build_path


BUILD_PATH = detect_platform()

setup(
    name="datalore-plot",
    version="1.0.0",
    maintainer="JetBrains",
    maintainer_email="info@jetbrains.com",
    author="JetBrains",
    author_email="info@jetbrains.com",
    description="Graphing library for Python",
    long_description="Graphing library for Python",

    package_dir={"": "python-runtime"},   # tell distutils packages are under src
    packages=[
        "datalore",
        "datalore.plot",
    ],

    data_files=[("datalore/plot", [BUILD_PATH + "/" + LINUX_LIB_NAME])],
)

