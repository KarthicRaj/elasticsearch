require "yaml"
properties = YAML.load_file( "VagarntProperties.yaml")

Vagrant.configure("2") do |config|

  #
  # TODO : bootstrap upgrade puppet  !!!!
  #

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

  #
  #  AWS insnstance with Centos6.4. Puppet.2.6.18"
  #
  config.vm.define "slave" do |slave|
    slave.vm.box = "dummy"
    slave.vm.box_url = "https://github.com/mitchellh/vagrant-aws/raw/master/dummy.box"


    slave.vm.provider :aws do |aws, override|
        aws.access_key_id = properties['aws']['access_key_id']
        aws.secret_access_key = properties['aws']['secret_access_key']
        aws.keypair_name = properties['aws']['keypair_name']
        override.ssh.username = "root"
        override.ssh.private_key_path = properties['aws']['private_key_path']

        aws.ami = "ami-ca168ffa"
        aws.instance_type = "t1.micro"
        aws.region = "us-west-2"
        aws.availability_zone = "us-west-2c"
    end
  end

  #
  #  Local insatnce with Centos6.4 Minimal Puppet 3.2.3
  #
  config.vm.define "master" do |master|
    master.vm.box = "Centos6.4 Minimal Puppet 3.2.3"
    master.vm.box_url = "http://developer.nrel.gov/downloads/vagrant-boxes/CentOS-6.4-x86_64-v20130731.box"
    
    master.vm.provider :virtualbox do |virtualbox|
      virtualbox.customize ["modifyvm", :id, "--memory", "1024"]
      virtualbox.customize ["modifyvm", :id, "--cpus", "2"]
    end 

    master.vm.network "forwarded_port", guest: 9200, host: 9200
    master.vm.network "forwarded_port", guest: 9300, host: 9300
    master.vm.network "forwarded_port", guest: 8080, host: 8080
    master.vm.network "private_network", ip: "127.0.0.101"


  end

  #
  # Prepare env.
  #
  #config.vm.provision "shell", inline: "sudo yum install -y git mc wget curl"
  #config.vm.provision "shell", inline: "sudo gem install librarian-puppet --verbose"
  #config.vm.provision "shell", inline: "sudo cp /vagrant/Puppetfile /etc/puppet/Puppetfile"
  #config.vm.provision "shell", inline: "cd /etc/puppet && sudo librarian-puppet install --verbose"
  #config.vm.provision "shell", inline: "rpm -i http://apt.sw.be/redhat/el6/en/x86_64/rpmforge/RPMS/htop-1.0.2-1.el6.rf.x86_64.rpm"

  #config.vm.provision "puppet" do |puppet|
  #  puppet.manifests_path = "manifests"
  #  puppet.manifest_file = "init.pp"
  #  puppet.options = "--verbose --debug"
  #end

  #
  # Install elasticsearch 
  #
  #config.vm.provision "shell", inline: "wget https://download.elasticsearch.org/elasticsearch/elasticsearch/elasticsearch-0.90.7.tar.gz -O elasticsearch.tar.gz"
  #config.vm.provision "shell", inline: "tar -xf elasticsearch.tar.gz && rm -v elasticsearch.tar.gz"
  #config.vm.provision "shell", inline: "mv -vf elasticsearch-* elasticsearch"
  #config.vm.provision "shell", inline: "sudo mv -v elasticsearch /usr/local/share"

  #
  # Set up elasticsearch as deamon with wrapper
  #
  #config.vm.provision "shell", inline: "curl -Lv http://github.com/elasticsearch/elasticsearch-servicewrapper/tarball/master | tar -xz"
  #config.vm.provision "shell", inline: "mv -v *servicewrapper*/service /usr/local/share/elasticsearch/bin/"
  #config.vm.provision "shell", inline: "rm -Rfv *servicewrapper*"
  #config.vm.provision "shell", inline: "sudo /usr/local/share/elasticsearch/bin/service/elasticsearch install"
  #config.vm.provision "shell", inline: "sudo /etc/init.d/elasticsearch start"

  #
  # Test service 
  #
  #config.vm.provision "shell", inline: "curl localhost:9200/_nodes/process?pretty"

  #
  # Install kibana
  #
  #config.vm.provision "puppet" do |puppet|
  #  puppet.manifest_file = "kibana.pp"
  #end

end
