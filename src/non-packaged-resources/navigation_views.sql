// take 2, using real tables for the base person and each of the roles

create table navigation.person_base(pid serial primary key, first_name text, last_name text, seqnum int, unique(first_name,last_name,seqnum));
create table navigation.person_authority(pid int, alias int);

create index pap on navigation.person_authority(pid);
create index paa on navigation.person_authority(alias);

grant select on table navigation.person_base to estc;
grant select on table navigation.person_authority to estc;

truncate navigation.person_base;

insert into navigation.person_base select id, first_name, last_name, seqnum from extraction.person;

analyze navigation.person_base;

create view person_aliased as select * from person_authority natural join person_base;
create view person_aliases as select * from person_aliased where pid != alias;
create view person as
	select pid,first_name,last_name,seqnum from person_aliased where pid = alias
	union
	select * from person_base where not exists (select pid from person_authority where alias=person_base.pid);
	
create view navigation.all_roles as
	select * from author
	union
	select * from bookseller
	union
	select * from printer
	union
	select * from publisher;

// take 1, trying to use materialized views derived from extraction.  This requires exposure of extraction tables to manage authority


drop materialized view
	navigation.person,
	navigation.author,
	navigation.bookseller,
	navigation.printer,
	navigation.publisher,
	navigation.all_roles,
	navigation.location,
	navigation.located,
	navigation.located_by_year,
	navigation.sublocations_by_year,
	navigation.locator,
	navigation.sublocator;
	
create materialized view navigation.person as select distinct id as pid,first_name,last_name from extraction.person;
create materialized view navigation.author as select distinct estc_id as id, person_id as pid from extraction.role where role='Author';
create materialized view navigation.bookseller as select distinct estc_id as id, person_id as pid from extraction.role where role='Bookseller';
create materialized view navigation.printer as select distinct estc_id as id, person_id as pid from extraction.role where role='Printer';
create materialized view navigation.publisher as select distinct estc_id as id, person_id as pid from extraction.role where role='Publisher';

create materialized view navigation.all_roles as select distinct estc_id as id, first_name, last_name from extraction.role,extraction.person where role.person_id=person.id;

create materialized view navigation.location as select distinct id as lid, location as label from extraction.location;
create materialized view navigation.located as select distinct person_id as pid, location_id as lid from extraction.place;

create materialized view navigation.located_by_year as
select person_id as pid, location_id as lid, pubdate as pubyear, locational, count(*)
	from extraction.place,estc.pub_year
	where place.estc_id=pub_year.id
	group by 1,2,3,4;

create materialized view navigation.sublocations_by_year as
select role.person_id, parent_id, pubdate as pubyear, locational,location_id,location,count(*)
		from extraction.sublocation,extraction.location,estc.pub_year,extraction.role
		where sublocation.location_id=location.id
			and sublocation.estc_id=pub_year.id
			and role.estc_id=sublocation.estc_id
		group by 1,2,3,4,5,6;

create materialized view navigation.locator as
select estc_id as id, locational, location_id, location, count(*)
	from extraction.place, extraction.location
	where place.location_id=location.id
	group by 1,2,3,4;

create materialized view navigation.sublocator as
select estc_id as id, parent_id, locational, location_id, location, count(*)
	from extraction.location, extraction.sublocation
	where sublocation.location_id=location.id
	group by 1,2,3,4,5;

create index ppid on navigation.person(pid);
create index aei on navigation.author(id);
create index apid on navigation.author(pid);
create index bei on navigation.bookseller(id);
create index bpid on navigation.bookseller(pid);
create index prei on navigation.printer(id);
create index prpid on navigation.printer(pid);
create index puei on navigation.publisher(id);
create index pupid on navigation.publisher(pid);
create index arei on navigation.all_roles(id);
create index arn on navigation.all_roles(last_name,first_name);
create index lid on navigation.location(lid);
create index llab on navigation.location(label);
create index llid on navigation.located(lid);
create index lpid on navigation.located(pid);
create index lbyp on navigation.located_by_year(pid);
create index lbyl on navigation.located_by_year(lid);
create index slbyper on navigation.sublocations_by_year(person_id);
create index slbypar on navigation.sublocations_by_year(parent_id);
create index slbyloc on navigation.sublocations_by_year(location_id);
create index locid on navigation.locator(id);
create index slocyper on navigation.sublocator(id);
create index slocpar on navigation.sublocator(parent_id);
create index slocloc on navigation.sublocator(location_id);

analyze verbose navigation.person;
analyze verbose navigation.author;
analyze verbose navigation.bookseller;
analyze verbose navigation.printer;
analyze verbose navigation.publisher;
analyze verbose navigation.all_roles;
analyze verbose navigation.location;
analyze verbose navigation.located;
analyze verbose navigation.located_by_year;
analyze verbose navigation.sublocations_by_year;
analyze verbose navigation.locator;
analyze verbose navigation.sublocator;

grant select on table
	navigation.person,
	navigation.author,
	navigation.bookseller,
	navigation.printer,
	navigation.publisher,
	navigation.all_roles,
	navigation.location,
	navigation.located,
	navigation.located_by_year,
	navigation.sublocations_by_year,
	navigation.locator,
	navigation.sublocator to estc;

delete from extraction.role where person_id not in (select id from extraction.person);
refresh materialized view navigation.person;
refresh materialized view navigation.author;
refresh materialized view navigation.bookseller;
refresh materialized view navigation.printer;
refresh materialized view navigation.publisher;
refresh materialized view navigation.all_roles;
refresh materialized view navigation.location;
refresh materialized view navigation.located;
refresh materialized view navigation.located_by_year;
refresh materialized view navigation.sublocations_by_year;
refresh materialized view navigation.locator;
refresh materialized view navigation.sublocator;
