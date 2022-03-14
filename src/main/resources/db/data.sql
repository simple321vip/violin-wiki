delete from location;
insert into location (id, type, latitude, longtitude) values(11,'beijing',0,0);
insert into location (id, type, latitude, longtitude) values(1,'shanghai',0,0);

select * from websites b inner join
(select country , count(country)
from websites
where url like '%http%'
group by country
having count(country) > 2)
a on a.country = b.country