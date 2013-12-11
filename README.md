elasticsearch
=============

preparing vagrant and plugins
---------------------------
 * install vagrant 
 * install aws plugin -> sudo vagrant plugin install vagrant-aws

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
