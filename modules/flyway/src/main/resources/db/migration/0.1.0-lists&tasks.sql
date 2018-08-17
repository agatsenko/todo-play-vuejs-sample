drop table if exists todo_tasks;
drop table if exists todo_lists;

create table todo_lists (
  id uuid not null,
  name varchar(50) not null,

  constraint pk_todo_lists primary key (id)
)
;

create table todo_tasks (
  id uuid not null,
  list_id uuid not null,
  description varchar(1000) not null,
  completed bool not null,

  constraint pk_todo_tasks primary key (id),
  constraint fk_todo_tasks_list_id foreign key (list_id) references todo_lists (id)
)
;

insert into todo_lists (id, name) values
  ('c889672b-ec8d-43a0-b605-d9e4f8c2a0ca', 'list 1'),
  ('adf3c417-7a7b-4a33-8c3b-1e2497f3babf', 'list 2'),
  ('e9397f8a-eb62-4022-b5de-f5e244213afb', 'list 3'),
  ('20fb4b67-4310-4fe8-aff6-b52f1ee7659e', 'list 4'),
  ('8b065a4e-3eec-4b8a-91d0-788ad5ac02a3', 'list 5'),
;

insert into todo_tasks (id, list_id, description, completed) values
  ('edc1ac1f-da61-4a9f-9b92-0fc842f6e545', 'adf3c417-7a7b-4a33-8c3b-1e2497f3babf', 'task 1 of list 2', false),
  ('50876a97-2b61-45aa-aa8b-a63a5e327f7b', 'adf3c417-7a7b-4a33-8c3b-1e2497f3babf', 'task 2 of list 2', true),
  ('e9e6628b-52ee-4ba6-8d10-82ebd301bc10', 'adf3c417-7a7b-4a33-8c3b-1e2497f3babf', 'task 3 of list 2', true),
  ('36d937f3-53cb-49e9-b05d-5fdcc07a9d49', '20fb4b67-4310-4fe8-aff6-b52f1ee7659e', 'task 1 of list 4', false),
  ('829819a4-cc34-45c0-9173-53ad8d6b57c0', '20fb4b67-4310-4fe8-aff6-b52f1ee7659e', 'task 2 of list 4', false),
  ('ee2a484e-4d28-4eb2-9655-d7a70f7f5f23', '20fb4b67-4310-4fe8-aff6-b52f1ee7659e', 'task 3 of list 4', false),
  ('4e4fa19f-2a02-4298-8542-59cd561394f9', '20fb4b67-4310-4fe8-aff6-b52f1ee7659e', 'task 3 of list 4', false)
;
