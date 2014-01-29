
#
# Install httpd
#
class { 'apache': }
apache::vhost { "kibana.local.host":
    port    => "8080",
    docroot => "/var/www/kibana",
}

#chcon -R -v -t httpd_sys_rw_content_t kibana/
#
# Install kibana
#
$enhancers = [ "tar", "wget" ]
package { $enhancers: ensure => "installed" }

Exec { user => "apache" }
File { owner => "apache", group => "apache" }

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
    seltype => "httpd_sys_content_t",
    require => [ Class ["apache"] ]   
}

exec {"cleanKibanaWwwDir":
    command => "/bin/rm -rf /var/www/kibana/*",
}

exec {"moveKibanaToWwwDir":
    command => "/bin/mv -f /tmp/kibanaUnziped/kibana-3.0.0milestone4/** /var/www/kibana",
    require => [ File["/var/www/kibana"], Exec["unzipKibana"], Exec["cleanKibanaWwwDir"], User["apache"] ]
}

exec {"chconKibana":
    command => "/usr/bin/chcon -R -v -t httpd_sys_rw_content_t /var/www/kibana",
    require => Exec["moveKibanaToWwwDir"]
}



info "http://localhost:8080/"
