select
	foo.*,
	bar.*,
	levenshtein(foo.last_name,bar.last_name) + levenshtein(foo.first_name,bar.first_name) as distance
from person as foo, person as bar
where
	(   levenshtein(foo.first_name, bar.first_name) < 3 
	 or (substring(foo.first_name from '^([A-Z])\.$') = substring(bar.first_name from '^([A-Z])[^.]'))
	 or (substring(foo.first_name from '^([A-Z])[^.]') = substring(bar.first_name from '^([A-Z])\.$'))
	 )
	and levenshtein(foo.last_name, bar.last_name) < 3
	and levenshtein(foo.last_name, bar.last_name) >= 0
	and foo.last_name <= bar.last_name
	and foo.first_name <= bar.first_name
	and foo.id != bar.id
order by foo.last_name, distance;

select
	foo.*,
	bar.*,
	levenshtein(foo.location,bar.location) as distance
from location as foo, location as bar
where
	    levenshtein(foo.location, bar.location) = 3
	and levenshtein(foo.location, bar.location) >= 0
	and foo.location <= bar.location
	and foo.id != bar.id
order by foo.location, distance;
