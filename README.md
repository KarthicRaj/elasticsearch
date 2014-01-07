elasticsearch
=============

Seting up env. 
==============

preparing vagrant and plugins
-----------------------------
	* install vagrant (tested on 1.3.5)
	* install VirtualBox (tested on 4.2.20)
	* install aws plugin -> 'sudo vagrant plugin install vagrant-aws'

preparing twitter crawler 
-------------------------
	* set creditianlas to 'twitter4j.properties' 


preparing aws 
------------- 
 * allow aws sg connection
 * import ssh keys to aws
 * set aws keys "VagrantProperties.yaml"

vagrant debug 
-------------
	* set VAGRANT_LOG=debug vagrant xx

start master
------------
	* vagrant up master

start slave 
-----------
	* vagrant up slave --provider=aws

virtual box 
-----------
	* For private subnet use ip range from  Host-Subnet Go to File->Preferences->Network->Host-only Networks (192.168.56.0/24)	
