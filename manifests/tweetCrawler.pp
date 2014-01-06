
$appName = "tweetCrawler"
$appVersion = "0.6"
$appClassifier = "shadow"
$appExtension = "jar"
$appFullName = "${appName}-${appVersion}-${appClassifier}.${appExtension}"
$appSymlink = "${appName}.${appExtension}"
$appPath = "/home/vagrant/es"

notify{"appFullName : ${appFullName}": }

File{
    owner  => "vagrant",
    group  => "vagrant",	
} 

file { $appPath :
    ensure => "directory"
}

#
# coppy jar 
#
file { "${appPath}/${appFullName}":
    source => "/vagrant/build/libs/${appFullName}",
    require  => File[ $appPath ]
}

#
# create symlink
#
file { "${appPath}/${appSymlink}":
   ensure => "link",
   target => "${appPath}/${appFullName}",
   require  => File[ "${appPath}/${appFullName}" ]
}

#
# copy log4j2
#
file { "${appPath}/log4j2.xml":
    source => "/vagrant/log4j2.xml",
    require  => File[ $appPath ]
}

#
# copy twitter cred.
#
file { "${appPath}/twitter4j.properties":
    source => "/vagrant/twitter4j.properties",
    require  => File[ $appPath ]
}

#
# coppt app restart script 
#
file { "${appPath}/restartTweetCrawler.sh":
    source => "/vagrant/scripts/restartTweetCrawler.sh",
    mode => 750,
    require => File[ $appPath ]
}

#
# cron for jvm restart every 10 min
#
cron { "restartJvm":
  command => "${appPath}/restartTweetCrawler.sh",
  user    => root,
  minute  => [ 1, 11, 21, 31, 41, 51 ]
}

#
# start app 
#
#exec { "runTweetCrawler":
#    path => "${appPath}",
#    command => "/usr/lib/jvm/java/bin/java -jar ${appPath}/${appFullName}"
#}
