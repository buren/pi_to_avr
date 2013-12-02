#!/bin/bash

#$lth_user=dt09to9
#$keyfile=/user/pi/rtsp/misc/dongle/raspi_wan_info_rsa
#$tmp=/tmp/ifconfig_out

log=/home/pi/connect.log

echo "" >> $log
echo "----------" >> $log
date >> $log

/bin/echo "LTE-dongle is on $DEVNAME" >> $log
sleep 30

/bin/echo -e "AT^NDISDUP=1,1,\"online.telia.se\"\r" > $DEVNAME
sleep 10

/usr/bin/sudo /sbin/dhclient wwan0 >> $log
sleep 10

#ifconfig > $tmp
#scp -i $keyfile $tmp $lth_user@login.student.lth.se:public_html/raspi_ip

exit

