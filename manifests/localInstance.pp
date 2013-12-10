
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
#  install Es 
#
class { 'elasticsearch':
   	autoupgrade => true ,
   	package_url => 'https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.7.noarch.rpm',
	config => {
		'node' => {
    		'master' => true
     	},
     	'cluster' => {
     		'name' => 'lvTwetterCluster' 
     	},
     	#'node' => {
     	#	'name' => 'master01'
     	#}
   	}
}
 
