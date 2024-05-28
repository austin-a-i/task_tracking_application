INSERT INTO `app_user` (id, username, password, email, enabled) VALUES (1001, 'john.doe', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'john@doe.com', true);
INSERT INTO `app_user` (id, username, password, email, enabled) VALUES (1002, 'jane.doe', '$2a$04$Vbug2lwwJGrvUXTj6z7ff.97IzVBkrJ1XfApfGNl.Z695zqcnPYra', 'jane@doe.com', true);

INSERT INTO `authorities` (id, name) VALUES (1, 'USER');
INSERT INTO `authorities` (id, name) VALUES (2, 'ADMIN');

INSERT INTO `task` (task, description, due_date, status, user_id) VALUES ('Buy Water', 'Buy water', '2024-05-27', 'IN_PROGRESS', 1001);
INSERT INTO `task` (task, description, due_date, status, user_id) VALUES ('Buy Food', 'Buy food', '2024-05-27', 'IN_PROGRESS', 1002);

INSERT INTO `user_authority` (user_id, authority_id) VALUES (1001, 1);
INSERT INTO `user_authority` (user_id, authority_id) VALUES (1002, 2);