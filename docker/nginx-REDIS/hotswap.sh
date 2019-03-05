#!/bin/bash
if grep -q "$@:6379" /etc/nginx/nginx.conf
then
    echo "The IP address is already being used."
else
    sed -i "11s/.*/    server $@:6379;/" /etc/nginx/nginx.conf
    /usr/sbin/nginx -s reload
fi
/usr/sbin/nginx -s reload
