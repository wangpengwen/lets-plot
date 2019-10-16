#!/usr/bin/env bash
#
# SUDO may be required for Docker launch.


platform_name="manylinux2010_x86_64"
docker_image="quay.io/pypa/${platform_name}"

root_path=$HOME
python_extension_path="${root_path}/datalore-plot/python-extension"
python_package_path="`pwd`"


docker pull $docker_image

docker run --rm \
  -e PLAT=$platform_name \
  -v $python_package_path:/io/python-package \
  -v $python_extension_path:/io/python-extension \
  -v /${root_path}/datalore-plot/LICENSE:/tmp/LICENSE \
  $docker_image \
  /io/python-package/build_linux_wheel.sh