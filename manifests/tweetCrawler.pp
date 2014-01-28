
$appName = "tweetCrawler"
$appVersion = "0.7"
$appClassifier = "shadow"
$appExtension = "jar"
$appFullName = "${appName}-${appVersion}-${appClassifier}.${appExtension}"
$appSymlink = "${appName}.${appExtension}"
$user = "vagrant"
$appPath = "/home/${user}/es"

notify{"appFullName : ${appFullName}": }

File{
    owner  => $user,
    group  => $user,	
} 

file { $appPath :
    ensure => "directory"
}

#
# Ensure user exists on system 
#
user { $user:
  ensure => "present",
  managehome => true
}

#
# coppy jar 
#
file { "${appPath}/${appFullName}":
    source => "/vagrant/build/distributions/${appFullName}",
    require  => [File[ $appPath ], User[ $user]]
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
# copy app restart script 
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
  command => "cd  ~/es; ${appPath}/restartTweetCrawler.sh",
  user    => vagrant,
  minute  => [ 1, 11, 21, 31, 41, 51 ]
}

#
# start app 
#
#exec { "runTweetCrawler":
#    path => "${appPath}",
#    command => "/usr/lib/jvm/java/bin/java -jar ${appPath}/${appFullName}"
#}
