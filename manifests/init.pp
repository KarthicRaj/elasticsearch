class { "java":
 	version	=> "latest"
}

class { 'apache': }

apache::vhost { "kibana.local.host":
    port    => "8080",
    docroot => "/var/www/kibana",
}

firewall { "0000 accpet all elasticsearch trafic":
    port   => [9200, 9300, 8080],
    proto  => "tcp",
    action => "accept"
}