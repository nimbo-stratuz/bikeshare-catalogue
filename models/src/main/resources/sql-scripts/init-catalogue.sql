SET session_replication_role = replica;

INSERT INTO bicycle (id, available, date_added, location_lat, location_lng, owner_id) VALUES (1, true, '2018-10-20T15:00:00Z000', '45.000123', '14.4434344', 1);
INSERT INTO smart_lock (id, owner_id, bicycle_id) VALUES (1, 1, 1);

SET session_replication_role = DEFAULT;