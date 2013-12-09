#http://stackoverflow.com/questions/17413598/vagrant-rsync-error-before-provisioning
#!/bin/bash
SUDOERS_FILE=/etc/sudoers.d/999-vagrant-cloud-init-requiretty
echo 'Defaults:ec2-user !requiretty' > $SUDOERS_FILE
echo 'Defaults:root !requiretty' >> $SUDOERS_FILE
chmod 440 $SUDOERS_FILE
