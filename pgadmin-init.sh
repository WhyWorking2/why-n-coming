#!/bin/sh -eu

cat >/pgadmin4/servers.json <<EOF
{
  "Servers": {
    "1": {
      "Name": "local-docker",
      "Group": "Servers",
      "Host": "db",
      "Port": 5432,
      "MaintenanceDB": "${POSTGRES_DB}",
      "Username": "${POSTGRES_USER}",
      "SSLMode": "prefer",
      "PassFile": "/pgpass"
    }
  }
}
EOF

echo "db:5432:${POSTGRES_DB}:${POSTGRES_USER}:${POSTGRES_PASSWORD}" > /pgpass
chmod 600 /pgpass
chown 5050:5050 /pgpass /pgadmin4/servers.json   # ★ 중요

exec /entrypoint.sh
