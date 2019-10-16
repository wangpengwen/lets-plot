#!/usr/bin/env bash
#
# Must be run inside the manylinux docker container


set -e -x


working_dir="/io/python-package/"
dist_dir="build/dist/"
python_bin_version="cp3[5-7]*"

# Install a system package required by our library
yum install -y atlas-devel

cd $working_dir

# Compile wheels
for PYBIN in /opt/python/${python_bin_version}/bin; do
    "${PYBIN}/pip" wheel $working_dir -w $dist_dir
done

# Bundle external shared libraries into the wheels
for whl in ${dist_dir}/*.whl; do
    auditwheel repair "$whl" --plat $PLAT -w $dist_dir
done