set -e

mysql -u root -p"${MYSQL_ROOT_PASSWORD}" <<-EOSQL
  GRANT ALL PRIVILEGES ON authentication_db.*   TO '${MYSQL_USER}'@'%';
  GRANT ALL PRIVILEGES ON delivery_person_db.*  TO '${MYSQL_USER}'@'%';
  GRANT ALL PRIVILEGES ON delivery_db.*         TO '${MYSQL_USER}'@'%';
  GRANT ALL PRIVILEGES ON notification_db.*     TO '${MYSQL_USER}'@'%';
  GRANT ALL PRIVILEGES ON order_db.*            TO '${MYSQL_USER}'@'%';
  GRANT ALL PRIVILEGES ON product_db.*          TO '${MYSQL_USER}'@'%';
  GRANT ALL PRIVILEGES ON restaurant_db.*       TO '${MYSQL_USER}'@'%';
  GRANT ALL PRIVILEGES ON user_db.*             TO '${MYSQL_USER}'@'%';
  FLUSH PRIVILEGES;
EOSQL

echo "[mysql-init] Grants aplicados com sucesso."