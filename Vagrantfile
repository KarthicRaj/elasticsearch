Vagrant.configure("2") do |config|
  config.vm.box = "Centos6.4 Minimal Puppet 3.2.3"
  config.vm.box_url = "http://developer.nrel.gov/downloads/vagrant-boxes/CentOS-6.4-x86_64-v20130731.box"

  config.vm.provider :virtualbox do |virtualbox|
    virtualbox.customize ["modifyvm", :id, "--memory", "1024"]
    virtualbox.customize ["modifyvm", :id, "--cpus", "2"] 
  end

  config.vm.network "forwarded_port", guest: 8080, host: 8080
  config.ssh.username = "vagrant"

	config.vm.provision "shell", inline: "sudo yum install -y git mc wget curl"
	config.vm.provision "shell", inline: "sudo gem install librarian-puppet --verbose"
	config.vm.provision "shell", inline: "sudo cp /vagrant/Puppetfile /etc/puppet/Puppetfile" 
	config.vm.provision "shell", inline: "cd /etc/puppet && sudo librarian-puppet install --verbose"

  config.vm.provision "puppet" do |puppet|
    puppet.manifests_path = "manifests"
    puppet.manifest_file = "init.pp"
    puppet.options = "--verbose --debug"
  end

  config.vm.provision "shell", inline: "wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.7.tar.gz -O elasticsearch.tar.gz"
  config.vm.provision "shell", inline: "tar -xf elasticsearch.tar.gz && rm -v elasticsearch.tar.gz"
  config.vm.provision "shell", inline: "mv -vf elasticsearch-* elasticsearch"
  config.vm.provision "shell", inline: "sudo mv -v elasticsearch /usr/local/share"

  config.vm.provision "shell", inline: "curl -Lv http://github.com/elasticsearch/elasticsearch-servicewrapper/tarball/master | tar -xz"
  config.vm.provision "shell", inline: "mv -v *servicewrapper*/service /usr/local/share/elasticsearch/bin/"
  config.vm.provision "shell", inline: "rm -Rfv *servicewrapper*"
  config.vm.provision "shell", inline: "sudo /usr/local/share/elasticsearch/bin/service/elasticsearch install"
  config.vm.provision "shell", inline: "sudo /etc/init.d/elasticsearch start"

end
