
class { 'logstash': 
	status => "enabled",
	autoupgrade => false,
	provider => "custom",
	jarfile => "https://download.elasticsearch.org/logstash/logstash/logstash-1.3.2-flatjar.jar"
}

logstash::input::file{'sysLogInput':
    type => "syslog",
    sincedb_path => "~/.sincedb*",
    path => [ "/var/log/*.log", "/var/log/messages", "/var/log/syslog" ]
}

logstash::input::file{'tweetCrawlerLogInput':
    type => "tweetCrawlerLog",
    sincedb_path => "~/.sincedb*",    
    path => [ "/home/vagrant/es/logs/forLogstash.log" ]
}

logstash::input::file{'elasticSearchLogInput':
    type => "syslog",
    sincedb_path => "~/.sincedb*",    
    path => [ "/var/log/elasticsearch/*.log" ]
}

logstash::output::elasticsearch{ 'elasticsearch':
    cluster => "lvTwetterCluster",
    host => "127.0.0.1",
    bind_host  => $ipaddress_eth1
}
