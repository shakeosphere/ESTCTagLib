-- LDA views

create materialized view lda as
select distinct 1510 as year, document, topic, score from lda_1510
union
select distinct 1520 as year, document, topic, score from lda_1520
union
select distinct 1530 as year, document, topic, score from lda_1530
union
select distinct 1540 as year, document, topic, score from lda_1540
union
select distinct 1550 as year, document, topic, score from lda_1550
union
select distinct 1560 as year, document, topic, score from lda_1560
union
select distinct 1570 as year, document, topic, score from lda_1570
union
select distinct 1580 as year, document, topic, score from lda_1580
union
select distinct 1590 as year, document, topic, score from lda_1590
union
select distinct 1600 as year, document, topic, score from lda_1600
union
select distinct 1610 as year, document, topic, score from lda_1610
union
select distinct 1620 as year, document, topic, score from lda_1620
union
select distinct 1630 as year, document, topic, score from lda_1630
;
