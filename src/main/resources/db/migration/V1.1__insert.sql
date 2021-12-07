insert into USERS (username, password, first_name, last_name, create_date, update_date)
values ('migtest1', '$2a$12$afZEpfh1CZCY4E9gTq.AKugGp6H/bmXSmC9Oa1iNt3rO2f9kxig1q', 'Migration', 'Tester', CURRENT_DATE, CURRENT_DATE);

insert into tasks (name, description, date_time, status, user_id, create_date, update_date)
values ('Migration Task', 'Task for migration', CURRENT_DATE, 'pending', 1, CURRENT_DATE, CURRENT_DATE);
