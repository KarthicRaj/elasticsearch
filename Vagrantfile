require "yaml"
properties = YAML.load_file( "VagarntProperties.yaml")

Vagrant.configure("2") do |config|

  #
  # Prepare env.
  #
  #config.vm.provision "shell", inline: "sudo yum -y install git mc wget curl unzip nano"
  #config.vm.provision "shell", inline: "sudo rpm -i http://apt.sw.be/redhat/el6/en/x86_64/rpmforge/RPMS/htop-1.0.2-1.el6.rf.x86_64.rpm"
  
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

        aws.ami = "ami-75190b01"
        aws.instance_type = "t1.micro"
        aws.region = "eu-west-1"
        aws.availability_zone = "eu-west-1c"
        aws.user_data = "#!/bin/bash\necho 'Defaults:ec2-user !requiretty' > /etc/sudoers.d/999-vagrant-cloud-init-requiretty && chmod 440 /etc/sudoers.d/999-vagrant-cloud-init-requiretty"
    end

    # install puppet
    #slave.vm.provision "shell", inline: "rpm -ivh http://yum.puppetlabs.com/el/6/products/x86_64/puppetlabs-release-6-7.noarch.rpm"
    #slave.vm.provision "shell", inline: "yum -y install puppet"

    #
    # instal gems 
    #
    #gem_installed = `gem --version` rescue nil
    #if !gem_installed
    #  slave.vm.provision "shell", path: "./scripts/installGem.sh"
    #end

    #slave.vm.provision "shell", path: "./scripts/installLibrarianPuppet.sh"
    
    #  Install ES
    slave.vm.provision "puppet", manifest_file: "slave.pp"
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

    config.vm.provision "shell", path: "./scripts/installLibrarianPuppet.sh"

    #  Install ES
    master.vm.provision "puppet", manifest_file: "master.pp"

    # Install kibana
    master.vm.provision "puppet", manifest_file: "kibana.pp"
  end


  #
  # Test service 
  #
  #config.vm.provision "shell", inline: "curl localhost:9200/_nodes/process?pretty"

end
