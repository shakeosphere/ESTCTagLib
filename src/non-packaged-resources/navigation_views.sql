
-- create schema

create schema if not exists navigation;

-- create person entity - the insertion is assuming that the extraction table is maintaining legacy

create table navigation.person_base(pid serial primary key, first_name text, last_name text, seqnum int, gender_female boolean, unique(first_name,last_name,seqnum));
insert into navigation.person_base select id, first_name, last_name, seqnum from extraction.person;
select setval('navigation.person_base_pid_seq',person_id_seq.last_value) from extraction.person_id_seq;
analyze navigation.person_base;

-- create the user-defined mappings of extracted instances to 'real' instances

create table navigation.person_authority(pid int, alias int, id int, defined timestamp);
insert into navigation.person_authority select pid, alias, id, defined from navigation_old.person_authority;
create index pap on navigation.person_authority(pid);
create index paa on navigation.person_authority(alias);
create index pai on navigation.person_authority(id);
analyze navigation.person_authority;

-- add in the views supporting filtered displays of person entities

create view navigation.person_aliased as select * from navigation.person_authority natural join navigation.person_base;
create view navigation.person_aliases as select * from navigation.person_aliased where pid != alias;
create view navigation.person as
	select pid,first_name,last_name,seqnum,gender_female from navigation.person_aliased where pid = alias
	union
	select * from navigation.person_base where not exists (select pid from navigation.person_authority where alias=person_base.pid);
create view navigation.person_effective as
	select pid, case when alias is not null then alias else pid end as effective_id
	from navigation.person natural left outer join navigation.person_authority;

-- create role elements

create table navigation.all_roles(estc_id int, person_id int, role text);
insert into navigation.all_roles select distinct estc_id, person_id, role from extraction.role;
create index are on navigation.all_roles(estc_id);
create index arp on navigation.all_roles(person_id);
analyze navigation.all_roles;

-- add the views supporting filtered display of roles

create materialized view navigation.author as select estc_id as id, person_id as pid from navigation.all_roles where role='author';
create materialized view navigation.bookseller as select estc_id as id, person_id as pid from navigation.all_roles where role='bookseller';
create materialized view navigation.printer as select estc_id as id, person_id as pid from navigation.all_roles where role='printer';
create materialized view navigation.publisher as select estc_id as id, person_id as pid from navigation.all_roles where role='publisher';
create index aei on navigation.author(id);
create index apid on navigation.author(pid);
create index bei on navigation.bookseller(id);
create index bpid on navigation.bookseller(pid);
create index prei on navigation.printer(id);
create index prpid on navigation.printer(pid);
create index puei on navigation.publisher(id);
create index pupid on navigation.publisher(pid);
analyze navigation.author;
analyze navigation.bookseller;
analyze navigation.printer;
analyze navigation.publisher;

-- create the supplemental entities

create table navigation.location(lid int, location text);
insert into navigation.location select distinct id as lid, location from extraction.location;
create index lid on navigation.location(lid);
create index llab on navigation.location(location);
analyze navigation.location;

create table navigation.establishment(eid int, establishment text);
insert into navigation.establishment select distinct id as eid, establishment from extraction.establishment;
create index eid on navigation.establishment(eid);
create index eest on navigation.establishment(establishment);
analyze navigation.location;

-- map in the publication table

create view navigation.publication as select * from estc.publication;

-- map in the user table for authority attribution

create view navigation.user as select * from admin.user;

-- create the cross-connecting relationships

create table navigation.person_in(estc_id int, location_id int, person_id int, locational text);
insert into navigation.person_in select distinct estc_id,location_id,person_id,locational from extraction.person_in where location_id > 0 and person_id > 0;
create index pie on navigation.person_in(estc_id);
create index pil on navigation.person_in(location_id);
create index pip on navigation.person_in(person_id);
analyze navigation.person_in;

create table navigation.person_at(estc_id int, establishment_id int, person_id int, locational text);
insert into navigation.person_at select distinct estc_id,establishment_id,person_id,locational from extraction.person_at where establishment_id > 0 and person_id > 0;
create index pae on navigation.person_at(estc_id);
create index pal on navigation.person_at(establishment_id);
create index papi on navigation.person_at(person_id);
analyze navigation.person_at;

create table navigation.location_in(estc_id int, sublocation_id int, location_id int, locational text);
insert into navigation.location_in select distinct estc_id,sublocation_id,location_id,locational from extraction.location_in where sublocation_id > 0 and location_id > 0;
create index lie on navigation.location_in(estc_id);
create index lil on navigation.location_in(sublocation_id);
create index lip on navigation.location_in(location_id);
analyze navigation.location_in;

-- generate analytic views

create materialized view navigation.person_in_by_year as
select person_id as pid, location_id as lid, pubdate as pubyear, locational, count(*)
	from navigation.person_in,estc.pub_year
	where person_in.estc_id=pub_year.id
	group by 1,2,3,4;
create index lbyp on navigation.person_in_by_year(pid);
create index lbyl on navigation.person_in_by_year(lid);
analyze navigation.person_in_by_year;

create materialized view navigation.location_in_by_year as
select all_roles.person_id, location_id as parent_id, pubdate as pubyear, locational,sublocation_id as location_id,location,count(*)
		from navigation.location_in,navigation.location,estc.pub_year,navigation.all_roles
		where location_in.sublocation_id=location.lid
			and location_in.estc_id=pub_year.id
			and all_roles.estc_id=location_in.estc_id
		group by 1,2,3,4,5,6;
create index slbyper on navigation.location_in_by_year(person_id);
create index slbypar on navigation.location_in_by_year(parent_id);
create index slbyloc on navigation.location_in_by_year(location_id);
analyze navigation.location_in_by_year;

create materialized view navigation.locator as
select estc_id as id, locational, location_id, location, count(*)
	from navigation.location_in, navigation.location
	where location_in.location_id=location.lid
	group by 1,2,3,4;
create index locid on navigation.locator(id);
analyze navigation.locator;

create materialized view navigation.sublocator as
select estc_id as id, location_id, locational, sublocation_id, location, count(*)
	from navigation.location, navigation.location_in
	where location_in.location_id=location.lid
	group by 1,2,3,4,5;
create index slocyper on navigation.sublocator(id);
create index slocpar on navigation.sublocator(location_id);
create index slocloc on navigation.sublocator(sublocation_id);
analyze navigation.sublocator;

-- make things visible to the app

grant usage on schema navigation to estc;
grant select on all tables in schema navigation to estc;

