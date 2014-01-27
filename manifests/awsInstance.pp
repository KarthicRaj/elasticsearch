
#
# Java 
#
class { "java":
 	version	=> "latest"
}

#
# configure firewall
#
firewall { "0000 accpet all elasticsearch trafic":
    port   => [9200, 9300, 8080],
    proto  => "tcp",
    action => "accept"
}

#
# Load aws access keys from yaml properties, absolute path on server  
#
$data = loadyaml("/vagrant/VagrantProperties.yaml") 

#
#  install Es 
#
class { 'elasticsearch':
   	autoupgrade => true ,
   	package_url => 'https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.9.noarch.rpm',
	config => {
	   	'network' => {
	    	'publish_host' => '_ec2:publicIpv4_'
	    },
	    'cluster' => {
	     	'name' => 'lvTwetterCluster' 
	    },

	    'discovery' => {
	     	'type' => 'ec2',
	     	'ec2' => {
	     		'ping_timeout' => 10s,
        		'host_type' => private_ip
	     	}
	    },

	   	'cloud.aws' => {
	     	'region' => 'eu-west',
	     	'access_key' => $data['aws']['access_key_id'],
	     	'secret_key' => $data['aws']['secret_access_key']
	    }
	}
}

elasticsearch::plugin{'org.elasticsearch/elasticsearch-cloud-aws/1.16.0':
	module_dir => "/usr/share/elasticsearch/plugin",
	require => Class["Java"]
} 

elasticsearch::plugin{'royrusso/elasticsearch-HQ':
  module_dir => "/usr/share/elasticsearch/plugin",
  require => Class["Java"]
} 

