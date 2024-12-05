insert into users (role, nickname, rating, password, email) values
    (0, 'admin', 100.00, '$2a$10$A2n9Sksxz6hBYw0HcSbuC.2aVAWfW4Dhdrbj.AMR31E/kLrIiZ18.', 'admin@admin'),
    (1, 'user', 0.00, '$2a$10$1zG7QtMOy2BPA.2IAbUaluh.6xl/qGF.cZW/8VINEkE1RGwvrQ3/.', 'user@user');

insert into activity_categories (name) values ('c1'), ('c2'), ('c3');

insert into activities (category_id, name) values (1, 'a1'), (2, 'a2'), (2, 'a3');

