# --- !Ups

create table todo_lists (
  id uuid not null,
  name varchar(50) not null,

  constraint pk_todo_lists primary key (id)
)
;

create table todo_items (
  id uuid not null,
  list_id uuid not null,
  description varchar(500) not null,
  completed bool not null,

  constraint pk_todo_items primary key (id),
  constraint fk_todo_items_list_id foreign key (list_id) references todo_lists (id)
)
;

# --- !Downs

drop table todo_items if exists;
drop table todo_lists if exists;
