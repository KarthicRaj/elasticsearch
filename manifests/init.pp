
#
# Java 
#
class { "java":
 	version	=> "latest"
}

#
# Install httpd
#
class { 'apache': }
apache::vhost { "kibana.local.host":
    port    => "8080",
    docroot => "/var/www/kibana",
}

#
# configure firewall
#
firewall { "0000 accpet all elasticsearch trafic":
    port   => [9200, 9300, 8080],
    proto  => "tcp",
    action => "accept"
}

