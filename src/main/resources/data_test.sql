insert into USERS (id, username, password, first_name, last_name, create_date, update_date)
values (1, 'admin', '$2a$12$afZEpfh1CZCY4E9gTq.AKugGp6H/bmXSmC9Oa1iNt3rO2f9kxig1q', 'Daniel', 'Gerendas', CURRENT_DATE, CURRENT_DATE);

insert into USERS (id, username, password, first_name, last_name, create_date, update_date)
values (2, 'jsmith', '$2a$12$afZEpfh1CZCY4E9gTq.AKugGp6H/bmXSmC9Oa1iNt3rO2f9kxig1q', 'John', 'Smith', CURRENT_DATE, CURRENT_DATE);

insert into tasks (id, name, description, date_time, status, user_id, create_date, update_date)
values (1, 'task', 'task desc', CURRENT_DATE, 'pending', 1, CURRENT_DATE, CURRENT_DATE);

insert into tasks (id, name, description, date_time, status, user_id, create_date, update_date)
values (2, 'Login screen bug', 'The Login button on the login screen is not working.', CURRENT_DATE, 'done', 1, CURRENT_DATE, CURRENT_DATE);