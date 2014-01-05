#!/bin/bash 

command -v librarian-puppet >/dev/null 2>&1 || { 
	echo >&2 "Installing librarian-puppet";
	sudo gem install librarian-puppet
	sudo cp /vagrant/Puppetfile /etc/puppet/Puppetfile
	cd /etc/puppet && sudo librarian-puppet install
}