$data = loadyaml("/vagrant/VagrantProperties.yaml") 

package { "newRelicRepo": 
   ensure => "installed",
   provider => rpm, 
   source => "http://download.newrelic.com/pub/newrelic/el5/x86_64/newrelic-repo-5-3.noarch.rpm"
} 

package {"newrelic-sysmond": 
   ensure => "installed",
   require => Package["newRelicRepo"]
}

exec {"unzipKibana":
    command => "/usr/sbin/nrsysmond-config  --set license_key='${data['newrelic']['license_key']}'",
    require => Package["newrelic-sysmond"]
}

