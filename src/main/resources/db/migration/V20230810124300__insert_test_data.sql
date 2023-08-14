
insert into authors (name) values ('Alois Jirasek');
insert into authors (name) values ('Bozena Nemcova');

insert into books (author_id,name,price) values ((SELECT id FROM authors WHERE name='Alois Jirasek'),'Psohlavci',300);
insert into books (author_id,name,price) values ((SELECT id FROM authors WHERE name='Bozena Nemcova'),'Babicka',150);
insert into books (author_id,name,price) values ((SELECT id FROM authors WHERE name='Bozena Nemcova'),'Diva Bara',450);
