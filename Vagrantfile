Vagrant.configure("2") do |config|
  config.vm.box = "Centos6.4 Minimal Puppet 3.2.3"
  config.vm.box_url = "http://developer.nrel.gov/downloads/vagrant-boxes/CentOS-6.4-x86_64-v20130731.box"

  config.vm.provider :virtualbox do |virtualbox|
    virtualbox.customize ["modifyvm", :id, "--memory", "1024"]
    virtualbox.customize ["modifyvm", :id, "--cpus", "2"] 
  end

  config.vm.network "forwarded_port", guest: 9200, host: 9200
  config.vm.network "forwarded_port", guest: 9300, host: 9300
  config.vm.network "forwarded_port", guest: 8080, host: 8080
  #config.vm.network "private_network", ip: "127.0.0.101"

  config.ssh.username = "vagrant"

  #
  # Prepare env.
  #
  config.vm.provision "puppet" do |puppet|
    puppet.manifest_file = "packages.pp"
  end

  #
  # Prepare install httpd, java7, configure firewall 
  #
  config.vm.provision "shell", inline: "sudo gem install librarian-puppet --verbose"
  config.vm.provision "shell", inline: "sudo cp /vagrant/Puppetfile /etc/puppet/Puppetfile"
  config.vm.provision "shell", inline: "cd /etc/puppet && sudo librarian-puppet install --verbose"
  config.vm.provision "puppet" do |puppet|
        puppet.manifest_file = "init.pp"
  end

  # => 
  # Install elasticsearch 
  #
  config.vm.provision "shell", inline: "wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.7.tar.gz -O elasticsearch.tar.gz"
  config.vm.provision "shell", inline: "tar -xf elasticsearch.tar.gz && rm elasticsearch.tar.gz"
  config.vm.provision "shell", inline: "mv -vf elasticsearch-* elasticsearch"
  config.vm.provision "shell", inline: "sudo mv -v elasticsearch /usr/local/share"

  #
  # Set up elasticsearch as deamon with wrapper
  #
  config.vm.provision "shell", inline: "curl -Lv http://github.com/elasticsearch/elasticsearch-servicewrapper/tarball/master | tar -xz"
  config.vm.provision "shell", inline: "mv -v *servicewrapper*/service /usr/local/share/elasticsearch/bin/"
  config.vm.provision "shell", inline: "rm -Rfv *servicewrapper*"
  config.vm.provision "shell", inline: "sudo /usr/local/share/elasticsearch/bin/service/elasticsearch install"
  config.vm.provision "shell", inline: "sudo /etc/init.d/elasticsearch start"

  #
  # Test service 
  #
  #config.vm.provision "shell", inline: "curl localhost:9200/_nodes/process?pretty"

  #
  # Install kibana
  #
  config.vm.provision "shell", inline: "curl -Lv https://download.elasticsearch.org/kibana/kibana/kibana-3.0.0milestone4.tar.gz | tar -xz"
  config.vm.provision "shell", inline: "mv -v kibana-3.0.0milestone4/* /var/www/kibana/"

  config.vm.provision "puppet" do |puppet|
    puppet.manifest_file = "kibana.pp"
  end

end
