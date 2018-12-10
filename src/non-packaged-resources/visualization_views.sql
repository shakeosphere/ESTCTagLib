create materialized view all_roles as
select pubdate, person.pid, first_name, last_name, count(*)
from estc.pub_year, navigation.person, navigation.person_effective
where pub_year.id=person_effective.effective_id
  and person_effective.pid=person.pid
group by 1,2,3,4;

create index all_date on all_roles(pubdate);
analyze all_roles;

create materialized view author as
select pubdate, person.pid, first_name, last_name, count(*)
from estc.pub_year, navigation.person, navigation.person_effective, navigation.author
where pub_year.id=author.id
  and author.pid = person_effective.effective_id
  and person_effective.pid=person.pid
group by 1,2,3,4;

create index author_date on author(pubdate);
analyze author;

create materialized view printer as
select pubdate, person.pid, first_name, last_name, count(*)
from estc.pub_year, navigation.person, navigation.person_effective, navigation.printer
where pub_year.id=printer.id
  and printer.pid = person_effective.effective_id
  and person_effective.pid=person.pid
group by 1,2,3,4;

create index printer_date on printer(pubdate);
analyze printer;

create materialized view publisher as
select pubdate, person.pid, first_name, last_name, count(*)
from estc.pub_year, navigation.person, navigation.person_effective, navigation.publisher
where pub_year.id=publisher.id
  and publisher.pid = person_effective.effective_id
  and person_effective.pid=person.pid
group by 1,2,3,4;

create index publisher_date on publisher(pubdate);
analyze publisher;

create materialized view bookseller as
select pubdate, person.pid, first_name, last_name, count(*)
from estc.pub_year, navigation.person, navigation.person_effective, navigation.bookseller
where pub_year.id=bookseller.id
  and bookseller.pid = person_effective.effective_id
  and person_effective.pid=person.pid
group by 1,2,3,4;

create index bookseller_date on bookseller(pubdate);
analyze bookseller;

create materialized view author_printer as
select pubdate, author.pid as author,printer.pid as printer,count(*)
from navigation.author,navigation.printer,estc.pub_year
where exists (select person.pid from navigation.person where person.pid=author.pid)
  and exists (select person.pid from navigation.person where person.pid=printer.pid)
  and author.id=printer.id
  and author.id=pub_year.id
group by 1,2,3;

create index author_printer_date on author_printer(pubdate);
analyze author_printer;

create materialized view author_publisher as
select pubdate, author.pid as author,publisher.pid as publisher,count(*)
from navigation.author,navigation.publisher,estc.pub_year
where exists (select person.pid from navigation.person where person.pid=author.pid)
  and exists (select person.pid from navigation.person where person.pid=publisher.pid)
  and author.id=publisher.id
  and author.id=pub_year.id
group by 1,2,3;

create index author_publisher_date on author_publisher(pubdate);
analyze author_publisher;

create materialized view author_bookseller as
select pubdate, author.pid as author,bookseller.pid as bookseller,count(*)
from navigation.author,navigation.bookseller,estc.pub_year
where exists (select person.pid from navigation.person where person.pid=author.pid)
  and exists (select person.pid from navigation.person where person.pid=bookseller.pid)
  and author.id=bookseller.id
  and author.id=pub_year.id
group by 1,2,3;

create index author_bookseller_date on author_bookseller(pubdate);
analyze author_bookseller;

create materialized view printer_publisher as
select pubdate, printer.pid as printer,publisher.pid as publisher,count(*)
from navigation.printer,navigation.publisher,estc.pub_year
where exists (select person.pid from navigation.person where person.pid=printer.pid)
  and exists (select person.pid from navigation.person where person.pid=publisher.pid)
  and printer.id=publisher.id
  and printer.id=pub_year.id
group by 1,2,3;

create index printer_publisher_date on printer_publisher(pubdate);
analyze printer_publisher;

create materialized view printer_bookseller as
select pubdate, printer.pid as printer,bookseller.pid as bookseller,count(*)
from navigation.printer,navigation.bookseller,estc.pub_year
where exists (select person.pid from navigation.person where person.pid=printer.pid)
  and exists (select person.pid from navigation.person where person.pid=bookseller.pid)
  and printer.id=bookseller.id
  and printer.id=pub_year.id
group by 1,2,3;

create index printer_bookseller_date on printer_bookseller(pubdate);
analyze printer_bookseller;

create materialized view publisher_bookseller as
select pubdate, publisher.pid as publisher,bookseller.pid as bookseller,count(*)
from navigation.publisher,navigation.bookseller,estc.pub_year
where exists (select person.pid from navigation.person where person.pid=publisher.pid)
  and exists (select person.pid from navigation.person where person.pid=bookseller.pid)
  and publisher.id=bookseller.id
  and publisher.id=pub_year.id
group by 1,2,3;

create index publisher_bookseller_date on publisher_bookseller(pubdate);
analyze publisher_bookseller;

grant usage on schema visualization to estc;
grant select on all tables in schema visualization to estc;

refresh materialized view all_roles;
refresh materialized view author;
refresh materialized view printer;
refresh materialized view publisher;
refresh materialized view bookseller;
refresh materialized view author_printer;
refresh materialized view author_publisher;
refresh materialized view author_bookseller;
refresh materialized view printer_publisher;
refresh materialized view printer_bookseller;
refresh materialized view publisher_bookseller;

analyze all_roles;
analyze author;
analyze printer;
analyze publisher;
analyze bookseller;
analyze author_printer;
analyze author_publisher;
analyze author_bookseller;
analyze printer_publisher;
analyze printer_bookseller;
analyze publisher_bookseller;

