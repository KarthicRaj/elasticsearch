#
# Java 
#
class { "java":
 	version	=> "latest"
}

#
#  install Es 
#
class { 'elasticsearch':
   	autoupgrade => true ,
   	package_url => 'https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.7.noarch.rpm',
	config => {
	   	'network' => {
	    	'publish_host' => '_ec2:publicIpv4_'
	    },
	    'cluster' => {
	     	'name' => 'lvTwetterCluster' 
	    },
	    'node' =>{
	     	'name' => 'slave01'
	    }
    }
}

elasticsearch::plugin{'org.elasticsearch/elasticsearch-cloud-aws/1.16.0':
	module_dir => "/usr/share/elasticsearch/plugin",
	require => Class["Java"]
} 

#discovery.zen.ping.multicast.enabled: false
#discovery.zen.ping.unicast.hosts: ["153.32.228.250[9300-9400]", "10.122.234.19[9300-9400]"]
#discovery.zen.minimum_master_nodes: 2
#discovery.zen.ping.multicast.enabled: false
#discovery.ec2.host_type: public_ip
