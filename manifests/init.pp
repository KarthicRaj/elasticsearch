class { "java":
 	version	=> "latest"
}

firewall { "0000 accpet all elasticsearch trafic":
    port   => [9200],
    proto  => "tcp",
    action => "accept"
}