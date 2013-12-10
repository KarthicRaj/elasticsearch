#!/bin/bash

command -v gem >/dev/null 2>&1 || { 
	echo >&2 "Installing gems";
	sudo yum -y install ruby
	sudo yum -y install gcc g++ make automake autoconf curl-devel openssl-devel zlib-devel httpd-devel apr-devel apr-util-devel sqlite-devel
	sudo yum -y install ruby-rdoc ruby-devel
	sudo yum -y install rubygems
}
