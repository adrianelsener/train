#!/bin/bash
sudo sysctl -w net.ipv4.ip_forward=1

#sudo iptables -A FORWARD -o enx847beb52b12b -i wlp2s0 -m conntrack --ctstate NEW -j ACCEPT
#sudo iptables -A FORWARD -o enx847beb52b12b -s 192.168.10.0/24 -m conntrack --ctstate NEW -j ACCEPT
#sudo iptables -t nat -A POSTROUTING -o enx847beb52b12b -j MASQUERADE

sudo iptables -A FORWARD -o wlp2s0 -i enx847beb52b12b -m conntrack --ctstate NEW -j ACCEPT
sudo iptables -A FORWARD -o wlp2s0 -s 192.168.10.0/24 -m conntrack --ctstate NEW -j ACCEPT 
sudo iptables -A FORWARD -o wlp2s0 -s 172.16.100.0/24 -m conntrack --ctstate NEW -j ACCEPT
sudo iptables -t nat -A POSTROUTING -o wlp2s0 -j MASQUERADE
