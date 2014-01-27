require "yaml"
properties = YAML.load_file( "VagrantProperties.yaml")

Vagrant.configure("2") do |config|


  #
  #  AWS insnstance with Centos6.4." -> vagrant up /awsInstance[1-2]/ --provider=aws
  #
  awsInstances = [ "awsInstance1", "awsInstance2"]
  awsInstances.each do |awsInstance_name|
      config.vm.define awsInstance_name do |awsInstance_config|
        awsInstance_config.ssh.pty = true
        awsInstance_config.vm.box = "dummy"
        awsInstance_config.vm.box_url = "https://github.com/mitchellh/vagrant-aws/raw/master/dummy.box"
        awsInstance_config.vm.provider :aws do |aws, override|
            aws.access_key_id = properties['aws']['access_key_id']
            aws.secret_access_key = properties['aws']['secret_access_key']
            aws.keypair_name = properties['aws']['keypair_name']
            override.ssh.username = "root"
            override.ssh.private_key_path = properties['aws']['private_key_path']

            aws.ami = "ami-75190b01"
            aws.instance_type = "t1.micro"
            aws.region = "eu-west-1"
            aws.availability_zone = "eu-west-1c"
            aws.user_data = "#!/bin/bash\necho 'say hello to aws'"
        end
        awsInstance_config.vm.provision "shell", path: "./scripts/puppetBootstrap.sh"
        awsInstance_config.vm.provision "puppet", manifest_file: "packages.pp"
        awsInstance_config.vm.provision "shell", path: "./scripts/gemBootstrap.sh"
        awsInstance_config.vm.provision "shell", path: "./scripts/installLibrarianPuppet.sh"
        awsInstance_config.vm.provision "puppet", manifest_file: "awsInstance.pp"
        awsInstance_config.vm.provision "puppet", manifest_file: "kibana.pp"
        awsInstance_config.vm.provision "puppet", manifest_file: "logstash.pp"    
        `./gradlew shadow`  
        awsInstance_config.vm.provision "puppet", manifest_file: "tweetCrawler.pp"
      end
  end

  #
  # Multiple local Es instances -> start with vagrant up /localInstance[1-2]/
  #
  localInstances = { :localInstance1 => '192.168.56.101', :localInstance2 => '192.168.56.102'}
  localInstances.each do |localInstance_name, localInstance_ip|
    config.vm.define localInstance_name do |localInstance_config|
      localInstance_config.vm.box = localInstance_name.to_s
      localInstance_config.vm.box_url = "https://github.com/2creatives/vagrant-centos/releases/download/v0.1.0/centos64-x86_64-20131030.box"
      localInstance_config.vm.network :private_network, ip: localInstance_ip, virtualbox__intnet: true
      localInstance_config.vm.provider :virtualbox do |virtualbox|
        virtualbox.customize ["modifyvm", :id, "--memory", "1024"]
        virtualbox.customize ["modifyvm", :id, "--cpus", "1"]
      end 
      localInstance_config.vm.provision "shell", path: "./scripts/puppetBootstrap.sh"
      localInstance_config.vm.provision "puppet", manifest_file: "packages.pp"
      localInstance_config.vm.provision "shell", path: "./scripts/gemBootstrap.sh"
      localInstance_config.vm.provision "shell", path: "./scripts/installLibrarianPuppet.sh"
      localInstance_config.vm.provision "puppet", manifest_file: "localInstance.pp"
      localInstance_config.vm.provision "puppet", manifest_file: "kibana.pp"
      localInstance_config.vm.provision "puppet", manifest_file: "logstash.pp"    
      `./gradlew shadow`  
      localInstance_config.vm.provision "puppet", manifest_file: "tweetCrawler.pp"
    end
  end
end
