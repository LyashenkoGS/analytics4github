# -*- mode: ruby -*-
# vi: set ft=ruby :

Vagrant.configure(2) do |config|

  config.vm.box = "centos/7"

  # Create a private network, which allows host-only access to the machine using a specific IP.
  config.vm.network "private_network", ip: "192.168.10.10"

  config.vm.provision "ansible" do |ansible|
      ansible.playbook = "provision-vagrant.yml"
      ansible.verbose = "vv"
  end

end
