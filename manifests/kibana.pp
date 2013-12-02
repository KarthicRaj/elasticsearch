

#  config.vm.provision "shell", inline: "curl -Lv https://download.elasticsearch.org/kibana/kibana/kibana-3.0.0milestone4.tar.gz | tar -xz"
#  config.vm.provision "shell", inline: "mv -v kibana-3.0.0milestone4/* /var/www/kibana/"

$enhancers = [ "tar", "wget" ]
package { $enhancers: ensure => "installed" }

exec { "getKibana" :
	command => "/usr/bin/wget https://download.elasticsearch.org/kibana/kibana/kibana-3.0.0milestone4.tar.gz -O /tmp/kibana-3.tar.gz",
    creates => "/tmp/kibana-3.tar.gz",
    require => [ Package["wget"] ],
}

file { "/tmp/kibanaUnziped":
	ensure  => directory,
}

exec {"unzipKibana":
    command => "/bin/tar -xf /tmp/kibana-3.tar.gz -C /tmp/kibanaUnziped",
    require => [ Package["tar"], Exec["getKibana"], File["/tmp/kibanaUnziped/"] ],
}

file { "/var/www/kibana":
	ensure  => directory,
}

exec {"moveKibanaToWwwDir":
    command => "/bin/mv /tmp/kibanaUnziped/kibana-3.0.0milestone4/** /var/www/kibana",
    require => [ File["/var/www/kibana"], Exec["unzipKibana"] ],
}
