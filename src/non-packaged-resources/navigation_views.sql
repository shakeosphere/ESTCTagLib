drop materialized view
	navigation.person,
	navigation.author,
	navigation.bookseller,
	navigation.printer,
	navigation.publisher,
	navigation.all_roles,
	navigation.location,
	navigation.located;
	
create materialized view navigation.person as select distinct id as pid,first_name,last_name from extraction.person;
create materialized view navigation.author as select distinct estc_id as id, person_id as pid from extraction.role where role='Author';
create materialized view navigation.bookseller as select distinct estc_id as id, person_id as pid from extraction.role where role='Bookseller';
create materialized view navigation.printer as select distinct estc_id as id, person_id as pid from extraction.role where role='Printer';
create materialized view navigation.publisher as select distinct estc_id as id, person_id as pid from extraction.role where role='Publisher';

create materialized view navigation.all_roles as select distinct estc_id as id, first_name, last_name from extraction.role,extraction.person where role.person_id=person.id;

create materialized view navigation.location as select distinct id as lid, location as label from extraction.location;
create materialized view navigation.located as select distinct person_id as pid, location_id as lid from extraction.place;

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

analyze verbose navigation.person;
analyze verbose navigation.author;
analyze verbose navigation.bookseller;
analyze verbose navigation.printer;
analyze verbose navigation.publisher;
analyze verbose navigation.all_roles;
analyze verbose navigation.location;
analyze verbose navigation.located;

grant select on table
	navigation.person,
	navigation.author,
	navigation.bookseller,
	navigation.printer,
	navigation.publisher,
	navigation.all_roles,
	navigation.location,
	navigation.located to estc;

delete from extraction.role where person_id not in (select id from extraction.person);
refresh materialized view navigation.person;
refresh materialized view navigation.author;
refresh materialized view navigation.bookseller;
refresh materialized view navigation.printer;
refresh materialized view navigation.publisher;
refresh materialized view navigation.all_roles;
refresh materialized view navigation.location;
refresh materialized view navigation.located;
