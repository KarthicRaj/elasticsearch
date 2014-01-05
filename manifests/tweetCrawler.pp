
$appName = "elasticsearch"
$appVersion = "0.3"
$appExtension = "shadow.jar"
$appFullName = "${appName}-${appVersion}-${appExtension}"
$appPath = "/home/vagrant/es"

notify{"appFullName : ${appFullName}": }

File{
    owner  => "vagrant",
    group  => "vagrant",	
} 

file { $appPath :
    ensure => "directory"
}

file { "${appPath}/${appFullName}":
    source => "/vagrant/build/libs/${appFullName}",
    require  => File[ $appPath ]
}

file { "${appPath}/log4j2.xml":
    source => "/vagrant/log4j2.xml",
    require  => File[ $appPath ]
}

file { "${appPath}/twitter4j.properties":
    source => "/vagrant/twitter4j.properties",
    require  => File[ $appPath ]
}

#exec { "runTweetCrawler":
#    path => "${appPath}",
#    command => "/usr/lib/jvm/java/bin/java -jar ${appPath}/${appFullName}"
#}
