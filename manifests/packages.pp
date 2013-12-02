#
# Base packages 
#
Package { ensure => "installed" }
$enhancers = [ "git", "mc", "wget", "curl", "unzip" ]
package { $enhancers:  }

#
# install htop
#
package { "htop":
	ensure => installed,
    provider => 'rpm',
	source => "http://apt.sw.be/redhat/el6/en/x86_64/rpmforge/RPMS/htop-1.0.2-1.el6.rf.x86_64.rpm",    
}
