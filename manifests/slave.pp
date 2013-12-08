#
#  install Es 
#
class { 'elasticsearch':
   autoupgrade => true ,
   package_url => 'https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.7.noarch.rpm'
   config                   => {
     'network'              => {
       'publish_host'       => _ec2:publicIpv4_
     }
   }

}

 elasticsearch::plugin{'elasticsearch/elasticsearch-cloud-aws':
   module_dir => 'head'
 }
