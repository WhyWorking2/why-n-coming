#!/bin/sh -eu

cat >/pgadmin4/servers.json <<EOF
{
  "Servers": {
    "1": {
      "Name": "local-docker",
      "Group": "Servers",
      "Host": "db",
      "Port": 5432,
      "MaintenanceDB": "whyncoming",
      "Username": "app_user",
      "SSLMode": "prefer",
      "PassFile": "/pgpass"
    }
  }
}
EOF

echo "db:5432:whyncoming:app_user:1234" > /pgpass
chmod 600 /pgpass
chown 5050:5050 /pgpass /pgadmin4/servers.json   # ★ 중요

exec /entrypoint.sh
