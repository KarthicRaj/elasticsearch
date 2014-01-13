elasticsearch
=============

Aim of this project is to demonstrate usage of decentralized db with real time search support. Also this example shows how multi server environment can be prepared / debugged locally and deployed in prod cloud with zero configuration on puppet provisioning 

Technology stack
-----------------
 * Elastic search 0.90.x (with plugins HQ, head, ...) - for storage claster and real time search  
 * Kibana 3.0 - search visualisation 
 * Logstash 1.3.2 - log aggregation, management
 * Puppet 6.7 - infrastructure provisioning
 * Gradle 1.10 - build system 
 * Log4j2 2.0 - application logs 
 * Twitter4j 3.0 - twitter query 
 

Seting up env. 
==============

preparing vagrant and plugins
-----------------------------
 * install vagrant (tested on 1.4.3)
 * install VirtualBox (tested on 4.2.20)
 * install aws plugin (for deployment to prod) 
	-> 'sudo vagrant plugin install vagrant-aws'

preparing twitter crawler 
-------------------------
 * set creditianlas to 'twitter4j.properties' 

preparing aws deployment 
------------------------ 
 * allow aws sg connection
 * import ssh keys to aws
 * set access keys to 'VagrantProperties.yaml' 

Starting cluster
=================

runing local on VM's with vagrant  
---------------------------------
	vagrant up /localInstance[1-2]/  - (will run two instances with 1 Gb RAM) 

running on AWS ec2 
------------------
	vagrant up /awsInstance[1-2]/ --provider=aws - (m1.small x2 in ireland )


Worth of mentioning 
===================

in case of error "undefined method 'sort!'"
-------------------------------------------
	delete ~/.vagrant.d -> http://stackoverflow.com/questions/20528884/vagrant-plugin-install-failing 

virtual box 
-----------
	For private subnet use ip range from  Host-Subnet Go to File->Preferences->Network->Host-only Networks (192.168.56.0/24)	
	
"sorry, you must have a tty to run sudo"
-----------------------------------------
	upgrade to vagrant 1.4  -> https://github.com/mitchellh/vagrant/issues/1482
