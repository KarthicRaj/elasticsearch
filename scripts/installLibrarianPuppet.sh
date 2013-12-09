#!/bin/bash 

sudo gem install librarian-puppet
sudo cp /vagrant/Puppetfile /etc/puppet/Puppetfile
cd /etc/puppet && sudo librarian-puppet install